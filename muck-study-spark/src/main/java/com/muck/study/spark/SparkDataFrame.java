package com.muck.study.spark;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;

import com.alibaba.fastjson.JSONObject;

public class SparkDataFrame extends BaseMain {

	public static class People implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Integer age;
		private String name;

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static void main(String[] args) {
		SparkParams params = parseArgs(args);
		SparkConf conf = new SparkConf().setAppName(params.getAppName())
				.setMaster(params.getMaster());
		if (!params.isDefault()) {
			conf.setJars(new String[] { params.getJarPath() });
		}
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaSQLContext sqlContext = new JavaSQLContext(sc);
		String path = "hdfs://ns1/user/hive/warehouse/people.json";
		JavaSchemaRDD people_schema = sqlContext.jsonFile(path);
		people_schema.printSchema();

		people_schema.registerAsTable("people");

		JavaSchemaRDD teenagers = sqlContext
				.sql("select * from people where age >=13 and age <= 19");
		JavaRDD<People> peopleRdd = teenagers.map(row -> {
			People one = new People();
			one.setAge(row.getInt(0));
			one.setName(row.getString(1));
			return one;
		});

		List<People> list = peopleRdd.collect();
		list.stream().forEach(people -> {
			System.out.println(JSONObject.toJSONString(people));
		});

	}
}
