package com.window;

import java.util.ArrayList;

/**
 * @Author: wangwei
 * @Description: 删除有序数组中重复元素
 * @Time: 2019/12/12 周四 14:22
 *
 * 给定一个排序数组，你需要在 原地 删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
 *
 * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
 *
 *
 *
 * 示例1:
 *
 * 给定数组 nums = [1,1,2],
 *
 * 函数应该返回新的长度 2, 并且原数组 nums 的前两个元素被修改为 1, 2。
 *
 * 你不需要考虑数组中超出新长度后面的元素。
 * 示例2:
 *
 * 给定 nums = [0,0,1,1,1,2,2,3,3,4],
 *
 * 函数应该返回新的长度 5, 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4。
 *
 * 你不需要考虑数组中超出新长度后面的元素。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 **/
public class _26_RemoveDuplicates {

	/**
	 * 由于数组已经排序，所以重复的元素一定连在一起
	 * 要求使用 O(1)空间
	 */

	/**
	 * 不合题意的做法
	 * 因为要保持原来的顺序，所以不能使用Set
	 * @param nums
	 * @return
	 */
	public int solution1(int[] nums) {
		ArrayList<Integer> list = new ArrayList<>();
		// 按顺序把数组中元素加入列表，跳过重复元素
		for (int i: nums)
			if (!list.contains(i)) list.add(i);
		// 再从列表中恢复到数组中
		for (int i = 0; i < list.size(); i++)
			nums[i] = list.get(i);
		// 返回去掉重复元素之后的大小
		return list.size();
	}
	
	/**
	 * 快慢指针
	 * 由于数组已经排序，所以重复的元素一定连在一起
	 * 让慢指针 slow 走左后面，快指针 fast 走在前面探路，找
	 * 到一个不重复的元素就告诉 slow 并让 slow 前进一步。
	 * 这样当 fast 指针遍历完整个数组 nums 后，nums[0..slow] 就是不重复元素
	 * j是原数组位置扫描指针
	 * i记录去重的下一个元素应放在哪个位置
	 * j >= i
	 * @param nums
	 * @return
	 */
	public int solution2(int[] nums) {
		if (nums.length == 0) return 0;
		// 第一个位置不用判断，nums[0]就放在0号位置,j开始向后扫描
		int i = 0;
		for (int j = 0; j < nums.length; j++) {
			if (nums[j] != nums[i]) {
				// 当前扫描到的数在之前没有出现过，i就后移1，用于保存这个元素
				++i;
				// 这个元素保存在i的位置，维护[0,slow]无重复
				nums[i] = nums[j];
			}
		}
		// i是下标，最终长度是 i + 1
		return i + 1;
	}
}
