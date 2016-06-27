package muck.study.arithmetic.search;

import java.util.Arrays;

public class ArrayUtils {

	/***
	 * 二分查找
	 * 
	 * @param searchKey
	 * @param sourceData
	 * @return
	 */
	public static boolean secondFind(int searchKey, int[] sourceData) {
		// 对数组进行排序，是以升序排序
		Arrays.sort(sourceData);
		int low_index = 0;
		int up_index = sourceData.length - 1;

		if (low_index > up_index) {
			return false;
		}
		while (true) {
			int mid_index = (low_index + up_index) / 2;
			int index_value = sourceData[mid_index];
			if (index_value == searchKey) {
				return true;
			} else if (low_index > up_index) {
				return false;
			} else {
				if (searchKey > index_value) {
					low_index = mid_index + 1;
				} else {
					up_index = mid_index - 1;
				}
			}
		}
	}
}
