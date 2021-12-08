package com.array;

/**
 * @Author: wangwei
 * @Description:
 * 	给定一个数组 nums 和一个值 val，原地(O(1)空间)移除所有数值等于 val 的元素，返回移除后数组的新长度。
 * @Time: 2019/12/12 周四 15:23
 **/
public class _27_RemoveElement {
	
	/**
	 * 双指针
	 * j从头扫描数组
	 * i记录下一个不等于val的元素应该放的位置，只有出线符合要求的数，i才后移，挪出一个位置保存它
	 * 相当于两次扫描
	 *
	 * @param nums
	 * @param val
	 * @return
	 */
	public int solution(int[] nums, int val) {
		
		int i = -1;
		for (int j = 0; j < nums.length; j++) {
			if (val != nums[j]) {
				++i;
				nums[i] = nums[j];
			}
		}
		return i + 1;
	}
	
	
	/**
	 * 一次扫描，题目要求去掉与val相等的元素，但去重后剩余元素顺序不重要
	 * 因此我们从头开始扫描，若不等于val则保留，指针后移
	 * 否则把最后一个元素移到这个位置，数组有效长度减一，指针不后移，继续进行判断
	 *
	 * @param nums
	 * @param val
	 * @return
	 */
	public int solution2(int[] nums, int val) {
		
		int i = 0, n = nums.length;
		// 下标不会超过有效长度
		while (i < n) {
			if (nums[i] != val) {
				nums[i] = nums[n - 1];
				--n;
			} else {
				++i;
			}
		}
		return i + 1;
	}
}
