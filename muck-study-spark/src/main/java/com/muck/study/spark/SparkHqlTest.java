package com.muck.study.spark;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkHqlTest extends BaseMain {

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
		SparkSession spark = SparkSession
				  .builder()
				  .appName("Java Spark SQL Example")
				  .config("spark.some.config.option", "some-value")
				  .getOrCreate();
		
		Dataset<Row> df = spark.read().json("examples/src/main/resources/people.json");
		df.show();
	}
}
