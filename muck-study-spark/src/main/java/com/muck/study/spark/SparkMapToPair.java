package com.muck.study.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.muck.study.spark.BaseMain.SparkParams;

import scala.Tuple2;

public class SparkMapToPair extends BaseMain{
	
	public static void main(String[] args) {
		SparkParams params = parseArgs(args);
		SparkConf conf = new SparkConf().setAppName(params.getAppName())
				.setMaster(params.getMaster());
		if (!params.isDefault()) {
			conf.setJars(new String[] { params.getJarPath() });
		}
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(params.getInputPath());

		JavaPairRDD<String, Integer> mapper = lines.mapToPair(line -> {
			return new Tuple2<String, Integer>(line, 1);
		});

		JavaPairRDD<String, Integer> result = mapper.reduceByKey((x, y) -> {
			return x + y;
		});
		System.out.println(result.collectAsMap().toString());
	}
}
