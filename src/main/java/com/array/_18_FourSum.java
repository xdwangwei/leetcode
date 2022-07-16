package com.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: wangwei
 * @Description: 四数之和等于target，找出所有的四数组和
 * @Time: 2019/12/10 周二 23:37
 **/
public class _18_FourSum {
	
	public static List<List<Integer>> solution(int[] nums, int target){
		
		List<List<Integer>> res = new ArrayList<>();
		// 最少四个数
		if (nums.length < 4) return res;
		// 为避免重复统计，先排序
		Arrays.sort(nums);
		// 因为有四个数，所以第一个元素下标最大只会在 nums.length - 4
		for (int i = 0; i < nums.length - 3; ++i){
			// 注意这些特殊情况
			// 注意此处不能写为 nums[i] > target，如-2 -1 1 1 1 target = 0，第一个1直接braek，错过了答案
			// 从此处往后不会存在答案了，nums[i]是第一个数，后面的都是正的，不可能和为负
			if (nums[i] > 0 && target <= 0) break;
			// 因为数组已经排好序并且结果集不能重复，因此以相同元素开始的结果直接跳过
			if (i > 0 && nums[i] == nums[i-1]) continue;
			// 从当前元素开始的连续四个元素是和最小的组合
			if (nums[i] + nums[i+1] + nums[i+2] +nums[i+3] > target) continue;
			// 当前元素和最后三个元素的组合，是和最大的组合
			if (nums[i] + nums[nums.length-1] + nums[nums.length-2] +nums[nums.length-3] < target) continue;
			// 同理第二个元素下标最大只会在 nums.length - 3
			for (int j = i + 1; j < nums.length - 2; ++j){
				// 在第一个数已确定的情况下，从第二个开始进行优化后的ThreeSum = target
				// 也要排除以相同元素开始的重复情况
				if (j > i + 1 && nums[j] == nums[j-1]) continue;
				// 双指针
				int head = j + 1, tail = nums.length - 1;
				while (head < tail){
					// 和太小，扩大左边界
					if (nums[head] + nums[tail] < target - nums[i] - nums[j])
						++head;
					// 和太大，缩小右边界
					else if(nums[head] + nums[tail] > target - nums[i] - nums[j])
						tail--;
					else {
						// i j head tail满足要求
						res.add(Arrays.asList(nums[i], nums[j], nums[head], nums[tail]));
						// head向后滑的过程中排除掉与当前元素相同的元素
						while (head < nums.length - 1 && nums[head] == nums[++head]);
						// tail向前滑的过程中排除掉与当前元素相同的元素
						while (tail > 0 && nums[tail] == nums[--tail]);
					}
				}
			}
		}
		return res;
	}
	
	public static void main(String[] args) {
		List<List<Integer>> solution = solution(new int[]{1, 0, -1, 0, -2, 2}, 0);
		for (List<Integer> integers : solution) {
			System.out.println(integers);
		}
	}
}
