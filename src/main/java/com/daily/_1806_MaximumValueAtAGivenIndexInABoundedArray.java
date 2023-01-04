package com.daily;

/**
 * @author wangwei
 * @date 2023/1/4 17:46
 * @description: _1806_MaximumValueAtAGivenIndexInABoundedArray
 *
 * 1802. 有界数组中指定下标处的最大值
 * 给你三个正整数 n、index 和 maxSum 。你需要构造一个同时满足下述所有条件的数组 nums（下标 从 0 开始 计数）：
 *
 * nums.length == n
 * nums[i] 是 正整数 ，其中 0 <= i < n
 * abs(nums[i] - nums[i+1]) <= 1 ，其中 0 <= i < n-1
 * nums 中所有元素之和不超过 maxSum
 * nums[index] 的值被 最大化
 * 返回你所构造的数组中的 nums[index] 。
 *
 * 注意：abs(x) 等于 x 的前提是 x >= 0 ；否则，abs(x) 等于 -x 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 4, index = 2,  maxSum = 6
 * 输出：2
 * 解释：数组 [1,1,2,1] 和 [1,2,2,1] 满足所有条件。不存在其他在指定下标处具有更大值的有效数组。
 * 示例 2：
 *
 * 输入：n = 6, index = 1,  maxSum = 10
 * 输出：3
 *
 *
 * 提示：
 *
 * 1 <= n <= maxSum <= 109
 * 0 <= index < n
 * 通过次数13,035提交次数37,578
 */
public class _1806_MaximumValueAtAGivenIndexInABoundedArray {

    /**
     * 方法一：贪心 + 二分查找
     * 思路
     *
     * 根据题意，需要构造一个长度为 n 的数组 nums，所有元素均为正整数，
     * 元素之和不超过 maxSum，相邻元素之差不超过 1，
     * 且需要最大化 nums[index]。
     *
     * 根据贪心的思想，在元素和受限情况下，为了使 nums[index] 成为数组最大的元素，那就让其他元素尽可能小，
     * 并且为了满足相邻元素差不超过1，那就让 nums[index] 左右两侧的元素，形成公差为-1的递减等差数列，
     * 即 下标每相差 1，元素值就减少 1，直到到达数组边界，或者某个位置减少到仅为 1 后，其后位置保持为 1 不变。
     *
     * 此时，numsSum 由三部分组成，nums[index]，nums[index] 左边的部分之和，和 nums[index] 右边的部分之和。
     * 设计一个函数 calcSum() 计算nums[index] 左边、右边元素和。
     * 以 index 右边部分元素和计算为例：
     *     假设 nums[index] 的大小为 x，假设右侧有 k 个数，则右侧的第 i 个数大小为 ri = max{x−i，1}，
     *     若 x > k，则右侧元素为等差数列，且后面不会有多余的 1 ，于是右侧元素的和为：
     *          rightSum = sum(x-i), 1 <= i <= k，
     *          rightSum = kx - k(k+1)/2 = k(2x  -k - 1) / 2
     *     若 x ≤ k，则右侧第x-1个元素值为1，剩余元素值均保持为1，
     *          即 右侧前 x - 1 个数是一个等差数列，第一个数为 x-1，第x−1个数为1；且等差数列后面，还跟着 k−(x-1) 个 1。
     *          所以此时：
     *          rightSum = sum(x-i) + k-x+1, 1 <= i <= x-1，
     *          rightSum = (x-1)x - (x-1)x/2 + k-x+1 = x(x-3)/2 + k + 1
     * nums[index]左侧元素的和 leftSum ，求解公式是相同的。
     *
     * 然后，我们只需要枚举 nums[index] 的值x，并在每一种取值x下，计算nums最小元素和sum，找到使得sum<=maxSum的最大x即可。
     * x最小取值为1，最大取值为maxSum
     * 若采用线性枚举
     * for (int x = rightSum; x >= 1; --x) {
     *     int sum = x + calc(index, left) + calc(index, right);
     *     if (sum <= maxSum) return x;
     * }
     * 会超时
     *
     * 注意到nums[index] 越大，数组和 numsSum 也越大。
     * 据此，可以使用二分搜索来找出最大的使得 sum ≤ maxSum 成立的 x。
     *
     * 注意到数据取值范围，计算数组元素和，使用 long 类型
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/maximum-value-at-a-given-index-in-a-bounded-array/solution/you-jie-shu-zu-zhong-zhi-ding-xia-biao-c-aav4/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param index
     * @param maxSum
     * @return
     */
    public int maxValue(int n, int index, int maxSum) {
        // 右边界二分搜索，下界 1， 上界 maxSum，左闭右开写法
        int left = 1, right = maxSum + 1;
        while (left < right) {
            int x = left + (right - left) / 2;
            // nums[index] 取 x 时，nums 最小元素和 （左右两侧都为公差为-1的等差数列及一串1）
            // 左侧元素个数 index， 右侧元素个数 n - 1 - index
            long sum = x + calc(x, index) + calc(x, n - index - 1);
            if (sum > maxSum) {
                right = x;
            } else {
                // 寻找满足限制下的最大值
                left = x + 1;
            }
        }
        // 满足限制时执行 left = x + 1，所以合法的 x = left - 1
        return left - 1;
    }

    /**
     * 计算 nums[index] 左侧、右侧元素和
     *
     * nums[index] 的大小为 x，假设右侧有 k 个数，则右侧的第 i 个数大小为 ri = max{x−i，1}，
     *
     *     若 x > k，则右侧元素为等差数列，且后面不会有多余的 1 ，于是右侧元素的和为：
     *          rightSum = sum(x-i), 1 <= i <= k，
     *          rightSum = kx - k(k+1)/2 = k(2x  -k - 1) / 2
     *     若 x ≤ k，则右侧第x-1个元素值为1，剩余元素值均保持为1，
     *          即 右侧前 x - 1 个数是一个等差数列，第一个数为 x-1，第x−1个数为1；且等差数列后面，还跟着 k−(x-1) 个 1。
     *          所以此时：
     *          rightSum = sum(x-i) + k-x+1, 1 <= i <= x-1，
     *          rightSum = (x-1)x - (x-1)x/2 + k-x+1 = x(x-3)/2 + k + 1
     * @param x
     * @param k
     * @return
     */
    private long calc(int x, int k) {
        // 若 x > k，Sum = k(2x  -k - 1) / 2
        if (x > k) {
            return (long) k * (2 * x - k - 1) / 2;
        }
        // 若 x <= k，Sum = x(x-3)/2 + k + 1
        return (long) x * (x - 3) / 2 + k + 1;
    }
}
