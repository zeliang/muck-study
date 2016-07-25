package com.muck.study.spark;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;

import com.alibaba.fastjson.JSONObject;

public class SparkSqlTest extends BaseMain {

	public static class People implements Serializable {
		private static final long serialVersionUID = -1518269751097658168L;
		private String id;
		private String name;
		private String age;
		private String phone;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
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

		JavaRDD<String> lines = sc.textFile(params.getInputPath());
		JavaRDD<People> rddPeoples = lines.map(line -> {
			People people = new People();
			String[] items = line.split(" ");
			people.setId(items[0]);
			people.setName(items[1]);
			people.setAge(items[2]);
			people.setPhone(items[3]);
			return people;
		});
		JavaSchemaRDD schemaPeople = sqlContext.applySchema(rddPeoples,
				People.class);
		schemaPeople.registerAsTable("people");
		JavaSchemaRDD teenagers = sqlContext
				.sql("SELECT * FROM people where age > '30' ");
		List<People> result = teenagers.map(row -> {
			People one = new People();
			one.setId(row.getString(0));
			one.setName(row.getString(1));
			one.setAge(row.getString(2));
			one.setPhone(row.getString(3));
			return one;
		}).collect();
		for (People people2 : result) {
			System.out.println(JSONObject.toJSONString(people2));
		}
	}
}
