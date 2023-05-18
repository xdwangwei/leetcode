package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/5/16 22:17
 * @description: _1335_MinimumDifficultyOfAJobSchedule
 *
 * 1335. 工作计划的最低难度
 * 你需要制定一份 d 天的工作计划表。工作之间存在依赖，要想执行第 i 项工作，你必须完成全部 j 项工作（ 0 <= j < i）。
 *
 * 你每天 至少 需要完成一项任务。工作计划的总难度是这 d 天每一天的难度之和，而一天的工作难度是当天应该完成工作的最大难度。
 *
 * 给你一个整数数组 jobDifficulty 和一个整数 d，分别代表工作难度和需要计划的天数。第 i 项工作的难度是 jobDifficulty[i]。
 *
 * 返回整个工作计划的 最小难度 。如果无法制定工作计划，则返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：jobDifficulty = [6,5,4,3,2,1], d = 2
 * 输出：7
 * 解释：第一天，您可以完成前 5 项工作，总难度 = 6.
 * 第二天，您可以完成最后一项工作，总难度 = 1.
 * 计划表的难度 = 6 + 1 = 7
 * 示例 2：
 *
 * 输入：jobDifficulty = [9,9,9], d = 4
 * 输出：-1
 * 解释：就算你每天完成一项工作，仍然有一天是空闲的，你无法制定一份能够满足既定工作时间的计划表。
 * 示例 3：
 *
 * 输入：jobDifficulty = [1,1,1], d = 3
 * 输出：3
 * 解释：工作计划为每天一项工作，总难度为 3 。
 * 示例 4：
 *
 * 输入：jobDifficulty = [7,1,7,1,7,1], d = 3
 * 输出：15
 * 示例 5：
 *
 * 输入：jobDifficulty = [11,111,22,222,33,333,44,444], d = 6
 * 输出：843
 *
 *
 * 提示：
 *
 * 1 <= jobDifficulty.length <= 300
 * 0 <= jobDifficulty[i] <= 1000
 * 1 <= d <= 10
 * 通过次数14,985提交次数22,753
 */
public class _1335_MinimumDifficultyOfAJobSchedule {

    /**
     * 方法：动态规划
     *
     * 题目相当于 要将 arr 分割成 d 个子数组，每个子数组长度为1，每个子数组的 难度为 数组元素最大值
     * 求：所有子数组难度和的最小值
     *
     * 假设数组长度为 n
     *
     * 因为每个子数组长度至少为1，所以 如果 d > n，返回 -1
     *
     * 否则：
     *
     * 考虑 最后一个子数组的元素范围为 [k...n]，其中 n - k >= 1，即 0 <= k <= n - 1
     * 那么 最后一个子数组的难度为 max(arr[k...n])
     * 剩下部分 arr[0...k-1] 需要划分为 d - 1 段，这就是一个子问题；
     *
     * 因此
     *
     * 定义 dp[i][j] 代表将 nums[0...i] 划分为 j 段，所有可行方案得到的 最小难度
     *
     * 由于 dp 求最小值，因此 初始时 初始化 dp 所有元素为 MaxValue
     *
     * 递推
     *
     * 对于 dp[i][j]，即 把 nums[0...i] 分成 j 个子数组 的最小难度，
     *
     * 枚举最后一个子数组的起点 k，那么
     *      dp[i][j] = min(dp[i][j], dp[k-1][j-1] + max(nums[k...j]))
     *      其中，dp[k-1][j-1] 是 nums[0...k-1] 所有划分方案的最小难度；max(nums[k...j]) 是 最后一个子数组的难度
     *
     *      为了避免 max(nums[k...j]) 用 for 循环求解
     *      可以 倒序枚举 最后一个子数组的起点 k，用一个 变量 cur 代表 nums[k...i] 的最大值
     *      for (int k = i; k >= 0; --k) {
     *          cur = max(cur, nums[k]) ，这样就能在 O(1) 时间得到每一个起点k对应的 max(nums[k...j])
     *      }
     *
     * 最终，返回 dp[n-1][d]，即 把 nums[0...n-1] 分成 d 个子数组的 所有方案的 最小难度
     *
     * base case：
     *
     * 当 j = 1 时，即分成一段，那么 dp[i][1] = max(nums[0...i])，一个for循环搞定
     *         for (int i = 0; i < n; ++i) {
     *             dp[i][1] = Math.max(i > 0 ? dp[i - 1][1] : 0, jobDifficulty[i]);
     *         }
     * 之后递推时 j 从 2 开始枚举，且 nums[0...i]最多只能分成n+1部分，所以 j <= min(d, i+1)
     *
     * 当 i = 0 时，即只有 nums[0]，那么只可能分成一段，即 dp[0][1] = nums[0]，其他情况都不可能
     * 之后递推时 i 从 1 开始， 且 i < n
     *
     * 因此递推时
     *
     *         for (int i = 1; i < n; ++i) {
     *             for (int j = 2; j <= Math.min(d, i + 1); ++j) {
     *                 // 对于 dp[i][j]，即 把 nums[0...i] 分成 j 个子数组 的最小难度，
     *                 // 倒序枚举 最后一个子数组的起点 k，用一个 变量 cur 代表 nums[k...i] 的最大值
     *                 int curDiff = 0;
     *                 for(int k = i; k >= 0; --k) {
     *                     // O(1) 时间得到每一个起点k对应的 max(nums[k...j])
     *                     curDiff = Math.max(curDiff, jobDifficulty[k]);
     *                     // dp[i][j] = min(dp[i][j], dp[k-1][j-1] + max(nums[k...j]))
     *                     // 其中，dp[k-1][j-1] 是 nums[0...k-1] 所有划分方案的最小难度；max(nums[k...j]) 是 最后一个子数组的难度
     *                     dp[i][j] = Math.min(dp[i][j], (k > 0 ? dp[k - 1][j - 1] : dp[0][j - 1]) + curDiff);
     *                 }
     *             }
     *         }
     *
     *
     *  返回，题目规定不可能时返回 -1，所以 不要直接返回 dp[n - 1][d]
     *       return dp[n - 1][d] >= inf ? -1 : dp[n - 1][d];
     *
     * @param jobDifficulty
     * @param d
     * @return
     */
    public int minDifficulty(int[] jobDifficulty, int d) {
        int n = jobDifficulty.length;
        // 每个子数组长度至少为1，所以 如果 d > n，返回 -1
        if (n < d) {
            return -1;
        }
        //  dp[i][j] 代表将 nums[0...i] 划分为 j 段，所有可行方案得到的 最小难度
        int[][] dp = new int[n][d + 1];
        final int inf = 1 << 30;
        // 由于 dp 求最小值，因此 初始时 初始化 dp 所有元素为 MaxValue
        for (int[] arr : dp) {
            Arrays.fill(arr, inf);
        }
        // base case
        // 当 j = 1 时，即分成一段，那么 dp[i][1] = max(nums[0...i])，一个for循环搞定
        for (int i = 0; i < n; ++i) {
            dp[i][1] = Math.max(i > 0 ? dp[i - 1][1] : 0, jobDifficulty[i]);
        }
        // 当 i = 0 时，即只有 nums[0]，那么只可能分成一段，即 dp[0][1] = nums[0]，上面情况已经计算过
        // 其他情况都不可能，所以 i 从 1 开始
        for (int i = 1; i < n; ++i) {
            // j = 1已经计算过，所以 j 从 2 开始，
            // nums[0...i]最多只能分成n+1部分（每个部分只有1个元素），所以 j <= min(d, i+1)
            for (int j = 2; j <= Math.min(d, i + 1); ++j) {
                int curDiff = 0;
                // 倒序枚举 最后一个子数组的起点 k，用一个 变量 cur 代表 nums[k...i] 的最大值
                for(int k = i; k >= 0; --k) {
                    // O(1) 时间得到每一个起点k对应的 max(nums[k...j])
                    curDiff = Math.max(curDiff, jobDifficulty[k]);
                    // dp[i][j] = min(dp[i][j], dp[k-1][j-1] + max(nums[k...j]))
                    // 其中，dp[k-1][j-1] 是 nums[0...k-1] 所有划分方案的最小难度；max(nums[k...j]) 是 最后一个子数组的难度
                    dp[i][j] = Math.min(dp[i][j], (k > 0 ? dp[k - 1][j - 1] : dp[0][j - 1]) + curDiff);
                }
            }
        }
        // 返回，注意不要直接返回 dp[n-1][d]
        return dp[n - 1][d] >= inf ? -1 : dp[n - 1][d];
    }
}
