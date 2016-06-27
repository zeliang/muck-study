package muck.study.arithmetic.sort;

public class SortUtils {

	/***
	 * // 交换位置
	 * 
	 * @param datas
	 * @param a_i
	 * @param b_i
	 */
	private static void swap(int[] datas, int a_i, int b_i) {
		int tmp = datas[a_i];
		datas[a_i] = datas[b_i];
		datas[b_i] = tmp;
	}

	/***
	 * 冒泡排序，执行次数----> n-1 + n-2 + n-3 + .... + 2 +1 = n *(n -1 ) /2 ，复杂度为 O(n^2)
	 */
	public static void bubblSort(int[] datas) {
		if (datas.length == 0) {
			return;
		} else {
			int out;
			for (out = datas.length - 1; out > 0; out--) {
				for (int i = 0; i < out; i++) {
					if (datas[i] > datas[i + 1]) {
						swap(datas, i, i + 1);
					}
				}
			}
		}
	}

	/***
	 * 选择排序
	 * 
	 * @param datas
	 */
	public static void selectSort(int[] datas) {
		if (datas.length == 0) {
			return;
		} else {
			int select;
			for (select = 0; select < datas.length - 1; select++) {
				int min = datas[select];
				int min_index = select;
				for (int i = select + 1; i < datas.length - 1; i++) {
					if (min > datas[i]) {
						min = datas[i];
						min_index = i;
					}
				}
				swap(datas, select, min_index);
			}
		}
	}
}
