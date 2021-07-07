package com.window;

/**
 * @author wangwei
 * 2020/7/29 9:49
 *
 * 209. 长度最小的子数组
 * 给定一个含有 n 个正整数的数组和一个正整数 s ，找出该数组中满足其和 ≥ s 的长度最小的 连续 子数组，并返回其长度。如果不存在符合条件的子数组，返回 0。
 *
 *
 * 示例：
 *
 * 输入：s = 7, nums = [2,3,1,2,4,3]
 * 输出：2
 * 解释：子数组 [4,3] 是该条件下的长度最小的子数组。
 *
 */
public class _209_MinimumSizeSubarraySum {

    /**
     * 滑动窗口
     * @param s
     * @param nums
     * @return
     */
    public int minSubArrayLen(int s, int[] nums) {
        int left = 0, right = 0;
        // 最终结果
        int len = nums.length + 1;
        // 当前窗口连续和
        int sum = 0;
        while (right < nums.length) {
            // 加入当前元素到窗口
            // 右边界扩大
            sum += nums[right++];

            // 判断缩小左窗口
            while (sum >= s) {
                // 更新结果集
                if (right - left < len) {
                    len = right - left;
                }
                // 窗口左边界右移
                sum -= nums[left++];
            }
        }
        // len未改变就是未找到合适结果
        return len == nums.length + 1 ? 0 : len;
    }

}
