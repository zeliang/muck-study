package com.muck.study.spark;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SparkFlatMapToPair extends BaseMain{
	public static void main(String[] args) {
		SparkParams params = parseArgs(args);
		SparkConf conf = new SparkConf().setAppName(params.getAppName())
				.setMaster(params.getMaster());
		if (!params.isDefault()) {
			conf.setJars(new String[] { params.getJarPath() });
		}
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> lines = sc.textFile(params.getInputPath());
		System.out.println("line number is :"+lines.count());;
		JavaPairRDD<String, Integer> mapper= lines.flatMapToPair(line->{
			return new Iterable<Tuple2<String,Integer>>() {
				
				@Override
				public Iterator<Tuple2<String, Integer>> iterator() {
					List<String> splits=Arrays.asList(line.split(" "));
					/*
					 * 这段代码会报序列化异常，分析了下，发现这里面都是用的是RDD，RDD是分布式，而line这一行数据是已经在
					 * 某一台机器上面了，所以不需要用RDD了，如果用就会报序列化。这个时候就要用java自带的Stream就可以。
					 * return sc.parallelize(splits).mapToPair(split ->{
						return new Tuple2<String, Integer>(split,1);
					}).collect().iterator();*/
					
					/***
					 * 用java自带的Stream就不会报错
					 */
					return splits.stream().map(split->{
						return new Tuple2<String, Integer>(split, 1);
					}).iterator();
				}
			};
		});

		JavaPairRDD<String, Integer> result = mapper.reduceByKey((x, y) -> {
			return x + y;
		});
		System.out.println(result.collectAsMap().toString());
	}
}
