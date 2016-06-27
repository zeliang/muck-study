package com.muck.study.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkMap extends BaseMain{
	public static void main(String[] args) {
		SparkParams params = parseArgs(args);
		SparkConf conf = new SparkConf().setAppName(params.getAppName())
				.setMaster(params.getMaster());
		if (!params.isDefault()) {
			conf.setJars(new String[] { params.getJarPath() });
		}
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(params.getInputPath());
		JavaRDD<Integer> list = lines.map(line -> {
			return line.length();
		});
		Integer allLength = list.reduce((x, y) -> {
			return x + y;
		});
		System.out.println("文本总长度为：" + allLength);
	}
}
