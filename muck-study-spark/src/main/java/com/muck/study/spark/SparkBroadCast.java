package com.muck.study.spark;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

public class SparkBroadCast extends BaseMain {

	public static void main(String[] args) {
		SparkParams params = parseArgs(args);
		SparkConf conf = new SparkConf().setAppName(params.getAppName())
				.setMaster(params.getMaster());
		if (!params.isDefault()) {
			conf.setJars(new String[] { params.getJarPath() });
		}
		JavaSparkContext sc = new JavaSparkContext(conf);
		List<String> result = Arrays.asList(new String[] { "1", "2", "3", "4",
				"5", "6" });

		Broadcast<List<String>> broadCastValue = sc.broadcast(result);
		List<String> results = broadCastValue.getValue();
		for (String string : results) {
			System.out.println("id:" + broadCastValue.id() + " , content:"
					+ string);
		}
	}
}
