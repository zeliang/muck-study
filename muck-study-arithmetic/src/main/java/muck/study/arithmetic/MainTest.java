package muck.study.arithmetic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import muck.study.arithmetic.search.ArrayUtils;
import muck.study.arithmetic.sort.SortUtils;
import muck.study.arithmetic.utils.Utis;

import org.junit.Before;
import org.junit.Test;

/***
 * 数组
 * 
 * @author muck
 */
public class MainTest {
	private int size = 10;
	private int[] datas = new int[size];

	@Before
	public void init() {
		Integer[] sourceData = new Integer[size];
		for (int i = 0; i < size; i++) {
			sourceData[i] = i;
		}
		List<Integer> list = Arrays.asList(sourceData);
		for (int i = 0; i < 5; i++) {
			Collections.shuffle(list);
		}
		sourceData = list.toArray(sourceData);
		for (int i = 0; i < sourceData.length; i++) {
			datas[i] = sourceData[i];
		}
	}

	/***
	 * 二分查找 复杂度为 O lon2n
	 */
	public void secendFind() {
		int searchKey = 778;
		long cur_time = Utis.get_cur_sys_time();
		boolean result = ArrayUtils.secondFind(searchKey, datas);
		System.out.println("result is :" + result);
		System.out.println(Utis.get_cur_sys_time() - cur_time);
	}

	/***
	 * 冒泡排序 复杂度为 O n^2 ===>n * n-1 * n-2 * n-3 ......*2 *1
	 */
	public void bubblSort() {
		long cur_time = Utis.get_cur_sys_time();
		Utis.printArray(datas);
		SortUtils.bubblSort(datas);
		System.out.println(Utis.get_cur_sys_time() - cur_time);
		Utis.printArray(datas);
	}

	/***
	 * 选择排序
	 */
	public void selectSort() {
		long cur_time = Utis.get_cur_sys_time();
		Utis.printArray(datas);
		SortUtils.selectSort(datas);
		System.out.println(Utis.get_cur_sys_time() - cur_time);
		Utis.printArray(datas);
	}

	@Test
	public void testggg() {
		int[] bb = new int[] { 1, 1, 1, 1 };
		for (int i = 0; i < bb.length; i++) {

		}
	}

}
