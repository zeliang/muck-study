package com.muck.study.storm.topology;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.coordination.BatchOutputCollector;
import backtype.storm.drpc.DRPCSpout;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.drpc.ReturnResults;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseBatchBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ReachTopology {
	  public static Map<String, List<String>> TWEETERS_DB = new HashMap<String, List<String>>() {{
	    put("foo.com/blog/1", Arrays.asList("sally", "bob", "tim", "george", "nathan"));
	    put("engineering.twitter.com/blog/5", Arrays.asList("adam", "david", "sally", "nathan"));
	    put("tech.backtype.com/blog/123", Arrays.asList("tim", "mike", "john"));
	  }};
	  
	  public static Map<String, List<String>> FOLLOWERS_DB = new HashMap<String, List<String>>() {{
	    put("sally", Arrays.asList("bob", "tim", "alice", "adam", "jim", "chris", "jai"));
	    put("bob", Arrays.asList("sally", "nathan", "jim", "mary", "david", "vivian"));
	    put("tim", Arrays.asList("alex"));
	    put("nathan", Arrays.asList("sally", "bob", "adam", "harry", "chris", "vivian", "emily", "jordan"));
	    put("adam", Arrays.asList("david", "carissa"));
	    put("mike", Arrays.asList("john", "bob"));
	    put("john", Arrays.asList("alice", "nathan", "jim", "mike", "bob"));
	  }};

	  public static class GetTweeters extends BaseBasicBolt {
	    @Override
	    public void execute(Tuple tuple, BasicOutputCollector collector) {
	      Object id = tuple.getValue(0);
	      String url = tuple.getString(1);
	      List<String> tweeters = TWEETERS_DB.get(url);
	      if (tweeters != null) {
	        for (String tweeter : tweeters) {
	          collector.emit(new Values(id, tweeter));
	        }
	      }
	    }

	    @Override
	    public void declareOutputFields(OutputFieldsDeclarer declarer) {
	      declarer.declare(new Fields("id", "tweeter"));
	    }
	  }

	  public static class GetFollowers extends BaseBasicBolt {
	    @Override
	    public void execute(Tuple tuple, BasicOutputCollector collector) {
	      Object id = tuple.getValue(0);
	      String tweeter = tuple.getString(1);
	      List<String> followers = FOLLOWERS_DB.get(tweeter);
	      if (followers != null) {
	        for (String follower : followers) {
	        	 System.out.println("GetFollowers--->follower:"+follower);
	          collector.emit(new Values(id, follower));
	        }
	      }
	    }

	    @Override
	    public void declareOutputFields(OutputFieldsDeclarer declarer) {
	      declarer.declare(new Fields("id", "follower"));
	    }
	  }
	  public static class PartialUniquer extends BaseBatchBolt {
	    BatchOutputCollector _collector;
	    Object _id;
	    Set<String> _followers = new HashSet<String>();
	    @Override
	    public void prepare(Map conf, TopologyContext context, BatchOutputCollector collector, Object id) {
	      _collector = collector;
	      _id = id;
	      System.out.println("instance PartialUniquer ");
	    }
	    @Override
	    public void execute(Tuple tuple) {
	    	String follwer=tuple.getString(1);
	      _followers.add(follwer);
	    }
	    @Override
	    public void finishBatch() {
	    	System.out.println("DRPC ID:"+_id+"，当前线程ID："+Thread.currentThread().getId()+"，_followers size is :" +_followers.size());
	    	for (String string : _followers) {
	    		System.out.println("finishBatch---->"+string);
			}
	      _collector.emit(new Values(_id, _followers.size()));
	    }
	    @Override
	    public void declareOutputFields(OutputFieldsDeclarer declarer) {
	      declarer.declare(new Fields("id", "partial-count"));
	    }
	  }

	  public static class CountAggregator extends BaseBatchBolt {
	    BatchOutputCollector _collector;
	    Object _id;
	    int _count = 0;

	    @Override
	    public void prepare(Map conf, TopologyContext context, BatchOutputCollector collector, Object id) {
	      _collector = collector;
	      _id = id;
	    }

	    @Override
	    public void execute(Tuple tuple) {
	      _count += tuple.getInteger(1);
	    }

	    @Override
	    public void finishBatch() {
	      _collector.emit(new Values(_id, _count));
	    }

	    @Override
	    public void declareOutputFields(OutputFieldsDeclarer declarer) {
	      declarer.declare(new Fields("id", "reach"));
	    }
	  }

	  public static LinearDRPCTopologyBuilder construct() {
	    LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("reach");
	    builder.addBolt(new GetTweeters(), 4);
	    builder.addBolt(new GetFollowers(), 12).shuffleGrouping();
	    builder.addBolt(new PartialUniquer(), 6).fieldsGrouping(new Fields("id", "follower"));
	    builder.addBolt(new CountAggregator(), 3).fieldsGrouping(new Fields("id"));
	    return builder;
	  }
	  
	  public static void main(String[] args) throws Exception {
	    Config conf = new Config();
	    LinearDRPCTopologyBuilder builder = construct();
	    if (args == null || args.length == 0) {
	      conf.setMaxTaskParallelism(3);
	      LocalDRPC drpc = new LocalDRPC();
	      LocalCluster cluster = new LocalCluster();
	      conf.setDebug(false);
	      cluster.submitTopology("reach-drpc", conf, builder.createLocalTopology(drpc));
	      String[] urlsToTry = new String[]{ "foo.com/blog/1", "engineering.twitter.com/blog/5", "notaurl.com" };
	      for (String url : urlsToTry) {
	        System.out.println("Reach of " + url + ": " + drpc.execute("reach", url));
	      }
	      //cluster.shutdown();
	      //drpc.shutdown();
	    }
	    else {
	      conf.setNumWorkers(6);
	      StormSubmitter.submitTopology(args[0], conf, builder.createRemoteTopology());
	    }
	  }
	}

