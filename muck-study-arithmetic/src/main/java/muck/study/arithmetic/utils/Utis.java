package muck.study.arithmetic.utils;

public class Utis {
	
	public static long get_cur_sys_time(){
		return System.currentTimeMillis();
	}
	
	public static void printArray(int[] datas){
		StringBuffer sb=new StringBuffer();
		for (int i : datas) {
			sb.append(i+",");
		}
		System.out.println(sb.toString());
	}
}
