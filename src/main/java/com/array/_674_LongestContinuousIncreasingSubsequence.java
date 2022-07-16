package com.array;

/**
 * @author wangwei
 * 2020/4/21 16:54
 *
 * 给定一个未经排序的整数数组，找到最长且连续的递增序列。
 *
 * 示例 1:
 *
 * 输入: [1,3,5,4,7]
 * 输出: 3
 * 解释: 最长连续递增序列是 [1,3,5], 长度为3。
 * 尽管 [1,3,5,7] 也是升序的子序列, 但它不是连续的，因为5和7在原数组里被4隔开。
 * 示例 2:
 *
 * 输入: [2,2,2,2,2]
 * 输出: 1
 * 解释: 最长连续递增序列是 [2], 长度为1。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-continuous-increasing-subsequence
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _674_LongestContinuousIncreasingSubsequence {

    /**
     * 注意题目要求的连续子序列
     *
     *
     * 方法一：动态规划
     * dp[i]表示以nums[i]结尾的最长连续子序列的长度
     * 对于nums[i]来说，如果 nums[i - 1] < nums[i]，那么就可以接在它后面，所以 dp[i] = dp[i - 1] + 1
     * 否则，没法接，只能另起炉灶，dp[i] = 1
     *
     * base case
     * dp[0] = 1
     *
     * 需要返回的是 最大的dp[i]。而不是 dp[n-1]
     * @param nums
     * @return
     */
    public int findLengthOfLCIS(int[] nums) {
        int n = nums.length;
        int res = 1;
        // dp[i]表示以nums[i]结尾的最长连续子序列的长度
        int[] dp = new int[n];
        // base case
        dp[0] = 1;
        for (int i = 1; i < n; ++i) {
            // 可以接在后面
            if (nums[i  - 1] < nums[i]) {
                dp[i] = dp[i - 1] + 1;
                // 更新res
                res = Math.max(res, dp[i]);
            } else {
                dp[i] = 1;
            }
        }
        // 返回
        return res;
    }

    /**
     * 方法二：双指针，窗口 [left, right)
     *
     * left表示当前连续子序列的起始位置，right表示当前扫描到的元素，那么当前 窗口内元素个数（序列长度）为 right - left
     * 若 nums[right] > nums[right - 1]，满足递增，right++
     * 否则，破坏递增，就是要断开了。此时先更新res = Math.max(res, right - left);
     * 然后移动左指针left到right位置，开始新的窗口
     * @param nums
     * @return
     */
    public int findLengthOfLCIS2(int[] nums) {
        // 窗口 [left, right) 初始只有一个 nums[0]元素，序列长度是1
        int left = 0, right = 1, res = 1;
        // right指针扫描元素
        while (right < nums.length) {
            // 不满足递增时，
            if (nums[right] <= nums[right - 1]) {
                // 更新res
                res = Math.max(res, right - left);
                // 移动左指针left
                left = right;
            }
            // 移动right
            right++;
        }
        // for循环结束后，最后一个窗口并未更新，再次更新res
        res = Math.max(res, right - left);
        // 返回
        return res;
    }

    public static void main(String[] args) {
        _674_LongestContinuousIncreasingSubsequence obj = new _674_LongestContinuousIncreasingSubsequence();
        int res = obj.findLengthOfLCIS(new int[]{1, 3, 5, 4, 7});
        System.out.println(res);
    }

}
