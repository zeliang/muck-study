package com.muck.study.kafka.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer implements Runnable {

	private Producer<String, String> producer = null;

	private ProducerConfig config = null;

	public KafkaProducer() {
		Properties props = new Properties();
		props.put("zookeeper.connect", "hadoop1:2181,hadoop2:2181,hadoop3:2181");
		props.put("queue.buffering.max.messages", Integer.MAX_VALUE+"");
		// 指定序列化处理类，默认为kafka.serializer.DefaultEncoder,即byte[]
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		//设置等待broker的ack才发送下一个消息
		props.put("request.required.acks", "1");
		// 同步还是异步，默认2表同步，1表异步。异步可以提高发送吞吐量，但是也可能导致丢失未发送过去的消息
		props.put("producer.type", "sync");
		// 是否压缩，默认0表示不压缩，1表示用gzip压缩，2表示用snappy压缩。压缩后消息中会有头来指明消息压缩类型，故在消费者端消息解压是透明的无需指定。
		props.put("compression.codec", "1");
		// 指定kafka节点列表，用于获取metadata(元数据)，不必全部指定
		props.put("metadata.broker.list", "hadoop1:9092,hadoop2:9092,hadoop3:9092");

		config = new ProducerConfig(props);
	}

	@Override
	public void run() {
		producer = new Producer<String, String>(config);
		// for(int i=0; i<10; i++) {
		// String sLine = "I'm number " + i;
		// KeyedMessage<String, String> msg = new KeyedMessage<String,
		// String>("group1", sLine);
		// producer.send(msg);
		// }
		for (int i = 1; i <= 3; i++) { 
			//List<KeyedMessage<String, String>> messageList = new ArrayList<KeyedMessage<String, String>>();
			for (int j = 0; j < 1000; j++) { 
				/*messageList.add(new KeyedMessage<String, String>
						("blog", "partition[" + i + "]", "message[The " + i+ " message]"));*/
				producer.send(new KeyedMessage<String, String>
				("blog", "message[The " + i+ " message]"+" [j is "+j+"]"));
			}
		}

	}

	public static void main(String[] args) {
		Thread t = new Thread(new KafkaProducer());
		t.start();
	}
}