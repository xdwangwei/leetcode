package com.array;

/**
 * @author wangwei
 * 2020/7/27 10:11
 *
 *
给定一个已按照升序排列 的有序数组，找到两个数使得它们相加之和等于目标数。

函数应该返回这两个下标值 index1 和 index2，其中 index1 必须小于 index2。

说明:

返回的下标值（index1 和 index2）不是从零开始的。
你可以假设每个输入只对应唯一的答案，而且你不可以重复使用相同的元素。
示例:

输入: numbers = [2, 7, 11, 15], target = 9
输出: [1,2]
解释: 2 与 7 之和等于目标数 9 。因此 index1 = 1, index2 = 2
 */
public class _167_TwoSum2 {

    /**
     * 双指针法 + 二分查找
     *
     * 注意运用二分思想的前提是数组必须有序
     * @param
     */
    public int[] twoSum(int[] nums, int target){
        int low = 0, high = nums.length - 1;
        while (low < high) {
            int temp = nums[low] + nums[high];
            if (temp == target) {
                // 题目要求返回的索引是从 1 开始的
                return new int[]{low + 1, high + 1};
            } else if (temp < target)
                low++;
            else
                high--;
        }
        return new int[]{-1, -1};
    }
}
