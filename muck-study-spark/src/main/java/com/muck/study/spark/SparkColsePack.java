package com.muck.study.spark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkColsePack extends BaseMain {

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
		JavaRDD<String> javaRdd = sc.parallelize(result);

		List<String> results = new ArrayList<String>();
		javaRdd.foreach(x -> {
			System.out.println("x--->" + x);
			results.add(x);
			System.out.println(results);
		});
		// 拉取到驱动器上面再打印，就可以算出来
		List<String> collectList = javaRdd.collect();
		System.out.println("行数为：" + collectList.size());
	}
}
