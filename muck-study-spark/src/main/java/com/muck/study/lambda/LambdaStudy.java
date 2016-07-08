package com.muck.study.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaStudy {
	private List<String> target = null;

	public void init() {
		String[] atp = new String[] { "1", "2", "3", "4" };
		target = Arrays.asList(atp);
	}

	public void testStream() {

		
		// 进行过滤，符合x->y的特征。无类型，无括号，无return关键字，无;符号
		Optional<String> first = target.stream().filter(str -> str.equals("1"))
				.findFirst();
		System.out.println(first.get());
		// 输出 1 字符串

		System.out.println(target.stream().collect(Collectors.toList()));
		// 把String转化为int，然后平方，输出结果
		List<Integer> initStr = target.stream().map((x) -> {
			return Integer.valueOf(x).intValue();
		}).map(x -> x * x).collect(Collectors.toList());
		System.out.println(initStr);

		// 转化为int类型的list，并且计算它的总和
		int result = target.stream().mapToInt(x -> Integer.valueOf(x)).sum();
		System.out.println(result);// 输出10

		// 初始化
		Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1),
				Arrays.asList(2, 3), Arrays.asList(4, 5, 6));
		// flatMap和Map的区别是，flatMap的变化后还是一个Stream，而Map变化后不是Stream，
		// flatMap就会把子Stream的元素抽出来，放在一起
		Stream<Integer> outputStream = inputStream
				.flatMap((childList) -> childList.stream());
		System.out.println(outputStream.collect(Collectors.toList()));
		// 输出：[1, 2, 3, 4, 5, 6]

	}
}
