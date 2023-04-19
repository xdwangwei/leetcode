package com.daily;

/**
 * @author wangwei
 * @date 2023/4/19 21:19
 * @description: _1043_PartitionArrayForMaximumSum
 *
 * 1043. 分隔数组以得到最大和
 * 给你一个整数数组 arr，请你将该数组分隔为长度 最多 为 k 的一些（连续）子数组。分隔完成后，每个子数组的中的所有值都会变为该子数组中的最大值。
 *
 * 返回将数组分隔变换后能够得到的元素最大和。本题所用到的测试用例会确保答案是一个 32 位整数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：arr = [1,15,7,9,2,5,10], k = 3
 * 输出：84
 * 解释：数组变为 [15,15,15,9,10,10,10]
 * 示例 2：
 *
 * 输入：arr = [1,4,1,5,7,3,6,1,9,9,3], k = 4
 * 输出：83
 * 示例 3：
 *
 * 输入：arr = [1], k = 1
 * 输出：1
 *
 *
 * 提示：
 *
 * 1 <= arr.length <= 500
 * 0 <= arr[i] <= 109
 * 1 <= k <= arr.length
 * 通过次数12,896提交次数18,365
 */
public class _1043_PartitionArrayForMaximumSum {

    /**
     * 方法一：动态规划
     *
     * 题目：把 arr 分成多个连续子数组，每个子数组的长度不能超过k，每个子数组所有元素变为其中最大值，求分割完后所有子数组的元素和的最大值
     *
     * 思路与算法
     *
     * 从 不超过 k 入手，不超过 k，也就是长度可以是 1、2、3、...、k
     *
     * 那么 对于 arr[0...n-1]，
     *      可以考虑最后一个子数组的长度分别为 1、2、、3、...、k
     *      对应的起始位置j分别为      i、i-1、i-2、...、i-k+1
     *      那么子数组的最后元素和为   长度 * 最大值 = (n-1 - j + 1) * max(arr[j...n-1])，取所有长度下的最大值
     *      至于剩余部分，arr[0...j-1] 就是一个子问题求解了
     *
     * 因此，设 d[i] 为以 i 结尾时（即 arr[0...i]）分割后的最大和，最后只需要返回 dp[n-1]
     * 递推：
     *      dp[i] = max(dp[j-1] + (i-j+1) * max(arr[j...i]))，[j, i] 是最后一个子数组，长度为 i-j+1，不能超过k，所以 j>=i-k+1
     *      关于这个 max(arr[j...i]) 可以通过倒序枚举j的方式 避免for循环来统计
     *      j是最后一段子数组的起始位置，如果 倒序遍历 j [i...i-k+1]，用 max 记录每个 不同 j 时 [j...i] 范围内的最大值
     *      倒序遍历每个 j，
     *          max = max(max, arr[j])，就能直接得到 [j...i] 范围内的最大值
     *          然后，dp[i] = max(dp[j-1] + (i-j+1) * max)
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/partition-array-for-maximum-sum/solution/fen-ge-shu-zu-yi-de-dao-zui-da-he-by-lee-mydv/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param arr
     * @param k
     * @return
     */
    public int maxSumAfterPartitioning(int[] arr, int k) {
        int n = arr.length;
        // 设 d[i] 为以 i 结尾时（即 arr[0...i]）分割后的最大和，最后只需要返回 dp[n-1]
        int[] dp = new int[n];
        // 递推，i 是结束位置
        for (int i = 0; i < n; ++i) {
            // 倒序枚举最后一个子数组的起点，[j...i] 长度不能k
            // 在倒序枚举j的过程中 更新 max，得到 arr[j...i]内的最大值
            int max = arr[i];
            // 1 <= len([j...i] = i-j+1) <= k
            for (int j = i; j >= Math.max(i - k + 1, 0); --j) {
                // 当前 arr[j...i] 内最大值
                max = Math.max(max, arr[j]);
                // 为以 i 结尾时（即 arr[0...i]）分割后的最大和 = 最后一段子数组[j...i]的最大和 + 前面[0...j-1]的最大分割和
                // 注意不要越界
                dp[i] = Math.max(dp[i], (j > 0 ? dp[j - 1] : 0) + (i - j + 1) * max);
            }
        }
        // 返回
        return dp[n - 1];
    }
}
