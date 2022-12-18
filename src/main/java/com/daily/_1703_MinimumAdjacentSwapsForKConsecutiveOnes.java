package com.daily;

/**
 * @author wangwei
 * @date 2022/12/18 14:00
 * @description: _1703_MinimumAdjacentSwapsForKConsecutiveOnes
 *
 * 1703. 得到连续 K 个 1 的最少相邻交换次数
 * 给你一个整数数组 nums 和一个整数 k 。 nums 仅包含 0 和 1 。每一次移动，你可以选择 相邻 两个数字并将它们交换。
 *
 * 请你返回使 nums 中包含 k 个 连续 1 的 最少 交换次数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,0,0,1,0,1], k = 2
 * 输出：1
 * 解释：在第一次操作时，nums 可以变成 [1,0,0,0,1,1] 得到连续两个 1 。
 * 示例 2：
 *
 * 输入：nums = [1,0,0,0,0,0,1,1], k = 3
 * 输出：5
 * 解释：通过 5 次操作，最左边的 1 可以移到右边直到 nums 变为 [0,0,0,0,0,1,1,1] 。
 * 示例 3：
 *
 * 输入：nums = [1,1,0,1], k = 2
 * 输出：0
 * 解释：nums 已经有连续 2 个 1 了。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * nums[i] 要么是 0 ，要么是 1 。
 * 1 <= k <= sum(nums)
 */
public class _1703_MinimumAdjacentSwapsForKConsecutiveOnes {


    /**
     * 贪心 + 中位数 + 前缀和
     *
     * 假如 共 k 个 1，设第i个1的下标为qi，最终第一个1移动到了位置x
     * 那么  q0   移动 到 x
     *      q1   移动 到 x + 1
     *      q2   移动 到 x + 2
     *      qk-1 移动 到 x + k-1
     * 稍稍转换：
     *      q0            移动到 x
     *      q1   -  1     移动到 x
     *      q2   -  2     移动到 x
     *      qk-1 - (k-1)  移动到 x
     *
     * 设 pi = qi - i，那么 最终转换次数为 sum(|pi - x|), 求sum最小值
     * 取 x 为 pi 的中位数时，sum 最小。
     *      证明：
     *      首先，如果 x 取在区间 [p[0],p[k−1]] 之外，那么 x 向区间方向移动可以使距离和变小；
     *      同时，如果 x 取在区间 [p[0],p[k−1]] 之内，无论如何移动 x，它到 p[0] 和 p[k−1] 的距离和都是一个定值 p[k−1]−p[0]，
     *      那么去掉 p[0] 和 p[k−1] 这两个最左最右的数，问题规模缩小。
     *      不断缩小问题规模，如果最后剩下 1 个数，那么 x 就取它；如果最后剩下 2 个数，那么 x 取这两个数之间的任意值都可以（包括这两个数）。
     *      因此中位数可以取 p[k/2]。
     *
     * 那么，当 x 取 p[k/2] 时，此时 的sum是多少呢？
     *      设 k = 5，p[k/2] = p2
     *      sum = |p0-p2| + |p1-p2| + |p3-p2| + |p4-p2| + |p5-p2|
     *          = (p2-p0) + (p2-p1) + (p3-p2) + (p4-p2) + (p5-p2)
     *          为了避免遍历统计效率低，我们采用前缀和优化，假设 s 是 p数组的 前缀和 数组
     *      sum = 2p2 - (p0+p1) + (p2+p3+p4) - 3p2
     *          = -(s2 - s0) + (s5 - s2) - p2
     *          = s0 + s5 - 2s2 - p2
     *
     *      当 k = 4 时，
     *      sum = s0 + s4 - 2s2
     *
     * 因此，一般情况下， sum = s0 + sk - 2s[k/2] - p[k/2] * (k mod 2)
     *
     * 回到原问题：上述过程计算的是， nums 中一共有 k 个1，且最终第一个1移动到 位置x时的最小转换次数
     *
     * 但实际上nums中可能有 m 个1，且第一个1不一定移动到哪个位置，当然了，按照题目要求，至少要有k个连续1，所以第一个1最多能移动到m-k位置
     *
     * 因此，我们可以枚举第一个1移动到的位置i，从而得到不同的sum，最终在这些sum中取最小值
     *
     * @param nums
     * @param k
     * @return
     */
    public int minMoves(int[] nums, int k) {
        int n = nums.length;
        // nums中第i个1的索引为qi，假如第一个1移动到位置x，那么第i个1移动到位置x+i
        // 令pi = qi - i，那么 问题转换为 sum(pi - x) 的最小值
        // 枚举第一个1移动到的位置x，在所有sum中取最小值
        // pi=qi-i
        int[] p = new int[n];
        // m时nums中1的个数，当前统计到的第i个1
        int m = 0;
        for (int i = 0; i < n; ++i) {
            // 记录pi=qi-i
            if (nums[i] == 1) {
                p[m] = i - m;
                // 更新m
                m++;
            }
        }
        // 如果 nums全是1，直接返回
        if (m == n) {
            return 0;
        }
        // 计算 q 的前缀和 数组
        int[] s = new int[m + 1]; // q 的前缀和
        for (int i = 0; i < m; i++) {
            s[i + 1] = s[i] + p[i];
        }
        int ans = Integer.MAX_VALUE;
        // 枚举第一个1的移动位置x，共m个1，要保证k个连续1，所以x最大为m-k
        for (int i = 0; i <= m - k; ++i) {
            // 对于每一个x，sum(pi-x)的最小值计算公式
            // 所有结果取最小值
            ans = Math.min(ans, s[i] + s[i + k] - 2 * s[i + k / 2] - p[i + k / 2] * (k % 2));
        }
        return ans;
    }
}
