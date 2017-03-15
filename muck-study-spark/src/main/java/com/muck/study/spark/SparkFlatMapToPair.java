package com.muck.study.spark;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SparkFlatMapToPair extends BaseMain {
	public static void main(String[] args) {
		SparkParams params = parseArgs(args);
		SparkConf conf = new SparkConf().setAppName(params.getAppName()).setMaster(params.getMaster());
		if (!params.isDefault()) {
			conf.setJars(new String[] { params.getJarPath() });
		}
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(params.getInputPath());
		// flatMapToPair 返回一个迭代器，并且是一个变成多个元组
		JavaPairRDD<String, Integer> words_pair_rdd = lines.flatMapToPair(line -> {
			List<String> words = Arrays.asList(line.split(" "));
			return words.stream().map(word -> {
				return new Tuple2<String, Integer>(word, 1);
			}).iterator();
		});

		JavaPairRDD<String, Integer> key_value_pair = words_pair_rdd.reduceByKey((x, y) -> {
			return x + y;
		});

		System.out.println(key_value_pair.collectAsMap().toString());
	}
}
