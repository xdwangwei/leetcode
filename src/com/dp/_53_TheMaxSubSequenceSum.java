package com.dp;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/19 周二 19:09
 **/
public class _53_TheMaxSubSequenceSum {
    public static int solution(int[] nums){
        int n = nums.length;
        int[] dp = new int[n]; //dp[j]表示从nums[0]到nums[j]的最大子序列和
        dp[0] = nums[0];
        int max = nums[0];
        for(int j = 1; j < n; j++){
            dp[j] = Math.max(dp[j-1], 0) + nums[j]; // max(dp[j-1]+nums[i],nums[i])
            max = Math.max(max, dp[j]);
        }
        return max;
    }

    /**
     * 一般是这样定义 dp 数组：
     * nums[0..i] 中的「最大的子数组和」为 dp[i]。
     * 如果这样定义的话，整个 nums 数组的「最大子数组和」就是 dp[n-1]。
     * 如何找状态转移方程呢？
     * 按照数学归纳法，假设我们知道了 dp[i-1]，如何推导出 dp[i] 呢？
     *
     * 实际上是不行的，因为子数组一定是连续的，
     * 按照我们当前 dp 数组定义，并不能保证 nums[0..i] 中的最大子数组与 nums[i+1] 是相邻的，
     * 也就没办法从 dp[i] 推导出 dp[i+1]。
     *
     * 所以说我们这样定义 dp 数组是不正确的，无法得到合适的状态转移方程。
     * 对于这类子数组问题，我们就要重新定义 dp 数组的含义：
     * 以 nums[i] 为结尾的「最大子数组和」为 dp[i]。
     * @param nums
     * @return
     */
    // dp[j]表示 以 nums[i] 为结尾的「最大子数组和」
    public static int solution1(int[] nums){
        int n = nums.length;
        int[] dp = new int[n]; //dp[j]表示从nums[0]到nums[j]的最大子序列和
        dp[0] = nums[0];
        int max = nums[0];
        for(int j = 1; j < n; j++){
            // 因为是连续子序列，所以对于当前元素，要么和前一个连接在一起，要么自己独立，选择更大的那个
            dp[j] = Math.max(dp[j-1] + nums[j], nums[j]);
            max = Math.max(max, dp[j]);
        }
        return max;
    }

    /**
     * 状态压缩
     * @param args
     */
    public static int solution2(int[] nums){
        int n = nums.length;
        // 此处的dp相当于上面的dp[j - 1]
        int dp = nums[0];
        int max = nums[0];
        for(int j = 1; j < n; j++){
            // 因为是连续子序列，所以对于当前元素，要么和前一个连接在一起，要么自己独立，选择更大的那个
            dp = Math.max(dp + nums[j], nums[j]);
            max = Math.max(max, dp);
        }
        return max;
    }

    public static void main(String[] args) {
        int[] sums = {-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(solution(sums));
    }
}
