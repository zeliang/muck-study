package com.muck.study.spark;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SparkFlatMap extends BaseMain {
	public static void main(String[] args) {
		SparkParams params = parseArgs(args);
		SparkConf conf = new SparkConf().setAppName(params.getAppName()).setMaster(params.getMaster());
		if (!params.isDefault()) {
			conf.setJars(new String[] { params.getJarPath() });
		}
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(params.getInputPath());
		// 把一行文本进行空格分割，变为单词，然后合并到一个List里面
		JavaRDD<String> wordsRdd = lines.flatMap(line -> {
			return Arrays.asList(line.split(" ")).iterator();
		});
		// 把一个单词变为元组形式(key,num)
		JavaPairRDD<String, Integer> word_tuple_rdd = wordsRdd.mapToPair(word -> {
			return new Tuple2<String, Integer>(word, 1);
		});

		// 最后进行reducebykey
		JavaPairRDD<String, Integer> result = word_tuple_rdd.reduceByKey((x, y) -> {
			return x + y;
		});

		System.out.println(result.collectAsMap().toString());
	}
}
