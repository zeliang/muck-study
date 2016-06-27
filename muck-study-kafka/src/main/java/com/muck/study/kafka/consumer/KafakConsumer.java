package com.muck.study.kafka.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class KafakConsumer implements Runnable {
	
	private ConsumerConfig consumerConfig;
	private static String topic="blog";
	Properties props;
	final int a_numThreads = 3;
	
	public KafakConsumer() {
		props = new Properties();    
        props.put("zookeeper.connect", "hadoop1:2181,hadoop2:2181,hadoop3:2181");  
        props.put("group.id", "blog");  
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000"); 
        props.put("auto.offset.reset", "smallest");
        consumerConfig = new ConsumerConfig(props);
	}
	

	@Override
	public void run() {
	    Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
	    topicCountMap.put(topic, new Integer(a_numThreads));
	    ConsumerConfig consumerConfig = new ConsumerConfig(props);
		ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
	    Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
	    List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
	    ExecutorService executor = Executors.newFixedThreadPool(a_numThreads);
	    for (final KafkaStream stream : streams) {
	        executor.submit(new KafkaConsumerThread(stream));
	    }
	}
	

	public static void main(String[] args) {
		System.out.println(topic);
		Thread t = new Thread(new KafakConsumer());
		t.start();
	}
}
