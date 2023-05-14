package com.daily;

import javax.print.attribute.standard.NumberOfInterveningJobs;

/**
 * @author wangwei
 * @date 2023/5/14 21:24
 * @description: _1330_ReverseSubarrayToMaximizeArrayValue
 *
 * 1330. 翻转子数组得到最大的数组值
 * 给你一个整数数组 nums 。「数组值」定义为所有满足 0 <= i < nums.length-1 的 |nums[i]-nums[i+1]| 的和。
 *
 * 你可以选择给定数组的任意子数组，并将该子数组翻转。但你只能执行这个操作 一次 。
 *
 * 请你找到可行的最大 数组值 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [2,3,1,5,4]
 * 输出：10
 * 解释：通过翻转子数组 [3,1,5] ，数组变成 [2,5,1,3,4] ，数组值为 10 。
 * 示例 2：
 *
 * 输入：nums = [2,4,9,24,2,1,10]
 * 输出：68
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 3*10^4
 * -10^5 <= nums[i] <= 10^5
 * 通过次数10,374提交次数17,923
 */
public class _1330_ReverseSubarrayToMaximizeArrayValue {

    /**
     * 方法：数学  去绝对值
     *
     * 首先不难注意到，反转子数组后，大部分数对的值没有改变：没被翻转的部分肯定不变；被翻转部分也不会变，
     * 因为翻转前子数组中的数对 [...,x,y,...]，翻转后是 [...,y,x,...]，而 ∣x−y∣=∣y−x∣。
     * 因此，会发生改变的数对就是被翻转部分和未翻转部分的交界。
     *
     * 如果不翻转，或者翻转的是一个长为 1 的子数组，那么 nums 不变，此时的「数组值」记作  base。
     * base = sum(abs(nums[i] - nums[i+1], 0 <= i <= n - 2))
     *
     * 为了计算出最大的「数组值」，考虑翻转后与翻转前的差值 d，那么答案为 base+d，所以 d 越大，答案也就越大。
     *
     * 假设从 nums[i] 到 nums[j] 的这段子数组翻转了，且 1≤i<j<n−1（其中 n 为 nums 的长度）。
     *
     * 设 a = nums[i−1], b = nums[i], x = nums[j], y = nums[j+1]。
     *
     * 翻转前，这 4 个数对数组值的贡献为 ∣a−b∣+∣x−y∣ 翻转后，顺序变为 a,x,b,y，贡献为 ∣a−x∣+∣b−y∣
     *
     * 得到 d = ∣a−x∣ + ∣b−y∣ − ∣a−b∣ − ∣x−y∣, ans = max(ans, base + d)
     *
     * 问题转换成求 d 的最大值。
     *
     * 暴力枚举 i 和 j 的时间复杂度是 O(n^2) 的，如何优化是本题的核心。
     *
     * 对于 i=0 或 j=n−1 的翻转，单独用 O(n) 的时间枚举。
     *
     *      当 i = 0 时，
     *              d = |y - b| - |y - x| = |a[j + 1] - a[0]| - |a[j + 1] - a[j]|，0 <= j <= n-2
     *      当 j = n-1 时
     *              d = |a - y| - |a - b| = |a[i - 1] - a[n-1]| - |a[i-1] - a[i]|，1 <= i <= n-1
     *              令 k = i - 1，则 0 <= k <= n-2，此时
     *                  d = |a[k] - a[n-1]| - |a[k] - a[k + 1]|，0 <= k <= n-2
     *                  与一式统一了，二者可在一个for循环完成
     *
     * 对于 0 < j < j < n-1 的情况
     *      d = ∣a−x∣ + ∣b−y∣ − ∣a−b∣ − ∣x−y∣
     *      由于 a + |b| = max(a + b, a - b)
     *          |a| + |b| = max(a+b, a-b, -a+b, -a-b)
     *      所以
     *          d = ∣a−x∣ + ∣b−y∣ − ∣a−b∣ − ∣x−y∣
     *            = max{ (a-x)+(b-y), (a-x)-(b-y), -(a-x)+(b-y), -(a-x)-(b-y) } − ∣a−b∣ − ∣x−y∣
     *            = max{ (a+b)-(x+y), (a-b)-(x-y), -(a-b)+(x-y), -(a+b)+(x+y) } −∣a−b∣ −∣x−y∣
     *            = max{ (a+b)-(x+y)−|a−b|−|x−y|, (a-b)-(x-y)−|a−b|−|x−y|, -(a-b)+(x-y)−|a−b|−|x−y|, -(a+b)+(x+y)−|a−b|−|x−y| }
     *            = max{ (a+b)−|a−b| −|x−y|-(x+y), (a-b)−|a−b|-(x-y)−|x−y|, -(a-b)−|a−b|+(x-y)−|x−y|, -(a+b)−|a−b|+(x+y)−|x−y| }
     *
     *            考虑 max{} 四项的每一项，分为 a、b组成的前部分 和 x、y 组成的后部分，
     *
     *            对于 −|x−y|-(x+y)，需要知道 (a+b)−|a−b| 的最大值，从而得到 (a+b)−|a−b| −|x−y|-(x+y) 的最大值
     *            对于 -(x-y)−|x−y|，需要知道 (a-b)−|a−b| 的最大值，从而得到 (a-b)−|a−b|-(x-y)−|x−y| 的最大值
     *            对于 +(x-y)−|x−y|，需要知道 -(a-b)−|a−b| 的最大值，从而得到 -(a-b)−|a−b|+(x-y)−|x−y| 的最大值
     *            对于 +(x+y)−|x−y|，需要知道 -(a+b)−|a−b| 的最大值，从而得到 -(a+b)−|a−b|+(x+y)−|x−y| 的最大值
     *
     *            由于 a，b 和 x，y 都是相邻的两项，且 a、b 是 x、y 前面的项，
     *            那么，可以用四个前缀变量 m1\m2\m3\m4 分别维护遍历过的 (a+b)−|a−b|、(a-b)−|a−b|、-(a-b)−|a−b|、-(a+b)−|a−b| 的最大值
     *            这样，当遍历到 x、y 时，可以快速得到 max{} 四项的最大值，从而得到 d 的最大值，从而得到 ans = base + d 的最大值
     *
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/reverse-subarray-to-maximize-array-value/solution/bu-hui-hua-jian-qing-kan-zhe-pythonjavac-c2s6/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：vclip
     * 链接：https://leetcode.cn/problems/reverse-subarray-to-maximize-array-value/solution/jian-dan-bao-li-bu-xu-yao-fen-lei-tao-lu-2erb/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     *
     * @param nums
     * @return
     */
    public int maxValueAfterReverse(int[] nums) {
        int n = nums.length;
        int base = 0;
        // 不发生翻转时的值
        for (int i = 0; i < n - 1; ++i) {
            base += Math.abs(nums[i] - nums[i + 1]);
        }
        // 初始化 ans 为 base
        int ans = base;

        // 翻转 [i, j]，特殊情况 ，i = 0 或 j = n - 1
        for (int i = 0; i < n - 1; ++i) {
            // 当 i = 0 时，d = |a[j+1] - a[0]| - |a[j+1] - a[j]|，0 <= j <= n-2
            // 当 j = n-1 时，d = |a[k] - a[n-1]| - |a[k] - a[k + 1]|，0 <= k <= n-2
            ans = Math.max(ans, base + Math.abs(nums[i + 1] - nums[0]) - Math.abs(nums[i + 1] - nums[i]));
            ans = Math.max(ans, base + Math.abs(nums[i] - nums[n - 1]) - Math.abs(nums[i] - nums[i + 1]));
        }
        // 翻转 [i, j]，0 < i < j < n
        // 翻转后增量 d = max{ (a+b)−|a−b| −|x−y|-(x+y), (a-b)−|a−b|-(x-y)−|x−y|, -(a-b)−|a−b|+(x-y)−|x−y|, -(a+b)−|a−b|+(x+y)−|x−y| }
        // 对于 −|x−y|-(x+y)，需要知道 (a+b)−|a−b| 的最大值，从而得到  (a+b)−|a−b|  −|x−y|-(x+y) 的最大值 --> a
        // 对于 -(x-y)−|x−y|，需要知道 (a-b)−|a−b| 的最大值，从而得到  (a-b)−|a−b|  -(x-y)−|x−y| 的最大值 --> b
        // 对于 +(x-y)−|x−y|，需要知道 -(a-b)−|a−b| 的最大值，从而得到 -(a-b)−|a−b| +(x-y)−|x−y| 的最大值 --> c
        // 对于 +(x+y)−|x−y|，需要知道 -(a+b)−|a−b| 的最大值，从而得到 -(a+b)−|a−b| +(x+y)−|x−y| 的最大值 --> d
        // 前缀变量 m1\m2\m3\m4 分别维护遍历过的 (a+b)−|a−b|、(a-b)−|a−b|、-(a-b)−|a−b|、-(a+b)−|a−b| 的最大值
        final int inf = 1 << 30;
        int m1 = -inf;
        int m2 = -inf;
        int m3 = -inf;
        int m4 = -inf;
        // 顺序遍历，每一个相邻位置
        for (int i = 0; i < n - 1; ++i) {
            // 得到 diff = max{} 的四项
            int a = m1 - Math.abs(nums[i] - nums[i + 1]) - (nums[i] + nums[i + 1]);
            int b = m2 - (nums[i] - nums[i + 1]) - Math.abs(nums[i] - nums[i + 1]);
            int c = m3 + (nums[i] - nums[i + 1]) - (nums[i] - nums[i + 1]);
            int d = m4 + (nums[i] + nums[i + 1]) - Math.abs(nums[i] - nums[i + 1]);
            // 取最大值
            int diff = Math.max(Math.max(a, b), Math.max(c, d));
            // 更新 ans
            ans = Math.max(ans, base + diff);

            // 更新 m1、m2、m3、m4 为对应表达式的最大值
            m1 = Math.max(m1, (nums[i] + nums[i + 1]) - Math.abs(nums[i] - nums[i + 1]));
            m2 = Math.max(m2, (nums[i] - nums[i + 1]) - Math.abs(nums[i] - nums[i + 1]));
            m3 = Math.max(m3, -(nums[i] - nums[i + 1]) - Math.abs(nums[i] - nums[i + 1]));
            m4 = Math.max(m4, -(nums[i] + nums[i + 1]) - Math.abs(nums[i] - nums[i + 1]));
        }
        // 返回
        return ans;
    }
}
