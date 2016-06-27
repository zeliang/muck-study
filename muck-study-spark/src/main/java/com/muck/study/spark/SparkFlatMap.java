package com.muck.study.spark;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SparkFlatMap extends BaseMain {
	public static void main(String[] args) {
		new SparkFlatMap().test(args);
	}

	/***
	 * 正常能用
	 * 
	 * @param args
	 */
	private void test(String[] args) {
		SparkParams params = parseArgs(args);
		SparkConf conf = new SparkConf().setAppName(params.getAppName())
				.setMaster(params.getMaster());
		if (!params.isDefault()) {
			conf.setJars(new String[] { params.getJarPath() });
		}
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(params.getInputPath());
		JavaRDD<String> words = lines.flatMap(line -> {
			return new Iterable<String>() {
				@Override
				public Iterator<String> iterator() {
					return Arrays.asList(line.split(" ")).iterator();
				}
			};
		});

		JavaPairRDD<String, Integer> reuslt = words.mapToPair((word) -> {
			return new Tuple2<String, Integer>(word, 1);
		}).reduceByKey((x, y) -> {
			return x + y;
		});

		System.out.println(reuslt.collectAsMap().toString());
	}

}
