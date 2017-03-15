package com.muck.study.spark;

public class BaseMain {

	public static SparkParams parseArgs(String[] args) {
		SparkParams params = new SparkParams();
		if (args.length == 0) {
		} else if (args.length == 2) {
			params.setAppName(args[0]);
			params.setInputPath(args[1]);
		} else if (args.length == 3) {
			params.setAppName(args[0]);
			params.setInputPath(args[1]);
			params.setMaster(args[2]);
			params.setDefault(false);
		}
		return params;
	}

	static class SparkParams {
		private String appName = "Test Spark";
		private String inputPath = "file:\\E:\\bigdata\\muck-study\\muck-study-spark\\src\\main\\resources\\data\\wyp.txt";
		private String master = "local[*]";
		private String jarPath = "E:\\work_space\\muck-study\\muck-study-spark\\target\\muck-study-spark.jar";
		private boolean isDefault = true;

		public boolean isDefault() {
			return isDefault;
		}

		public void setDefault(boolean isDefault) {
			this.isDefault = isDefault;
		}

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public String getInputPath() {
			return inputPath;
		}

		public void setInputPath(String inputPath) {
			this.inputPath = inputPath;
		}

		public String getMaster() {
			return master;
		}

		public void setMaster(String master) {
			this.master = master;
		}

		public String getJarPath() {
			return jarPath;
		}

		public void setJarPath(String jarPath) {
			this.jarPath = jarPath;
		}

	}
}
