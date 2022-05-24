package com.presum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/5/24 10:31
 * @description: _011_LongestContinuousSubarrayOfEqualNumberOf0and1
 *
 * 525. 0 和 1 个数相同的子数组
 * 给定一个二进制数组 nums , 找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。
 *
 *
 *
 * 示例 1：
 *
 * 输入: nums = [0,1]
 * 输出: 2
 * 说明: [0, 1] 是具有相同数量 0 和 1 的最长连续子数组。
 * 示例 2：
 *
 * 输入: nums = [0,1,0]
 * 输出: 2
 * 说明: [0, 1] (或 [1, 0]) 是具有相同数量 0 和 1 的最长连续子数组。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * nums[i] 不是 0 就是 1
 *
 */
public class _525_LongestContinuousSubarrayOfEqualNumberOf0and1 {

    /**
     * 方法一：枚举 枚举所有满足符合条件的连续子数组的右端点：超时，就过了 40 / 564
     * 思路和算法
     *
     * 考虑以 i 结尾 的连续子数组，我们需要判断符合条件的下标 j 的个数，其中 0≤j≤i 且 [j..i] 这个子数组的中0和1的个数相同
     *
     * 我们可以枚举 [0..i] 里所有的下标 j 来判断是否符合条件，
     *
     * 可能有读者会认为假定我们确定了子数组的开头和结尾，还需要 O(n) 的时间复杂度遍历子数组来求0和1的个数，那样复杂度就将达到 O(n^3)
     * 从而无法通过所有测试用例。
     * 但是如果我们知道 [j,i] 子数组中0和1的个数，就能 O(1) 推出 [j−1,i] 中0和1的个数，因此这部分的遍历求和是不需要的，
     * 我们在枚举下标 j 的时候已经能 O(1) 求出 [j,i] 的0和1的个数。
     *
     * 并且：可以提前统计[0...i]中的0的个数和1的个数，这样，就可以在0(1)时间内得到每个区间内0和1的个数
     *
     * 复杂度分析
     *
     * 时间复杂度：O(n^2)，其中 n 为数组的长度。
     * 枚举子数组开头和结尾需要 O(n^2) 的时间，其中求和需要 O(1) 的时间复杂度，因此总时间复杂度为 O(n^2)
     * 空间复杂度：O(N)。提前存储0和1的个数
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/subarray-sum-equals-k/solution/he-wei-kde-zi-shu-zu-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int findMaxLength(int[] nums) {
        int n = nums.length;
        // 提前统计0和1的个数
        // ones[i]表示nums[0...i-1]中1的个数
        int[] ones = new int[n + 1];
        // zeros[i]表示nums[0...i-1]中0的个数
        int[] zeros = new int[n + 1];
        for (int i = 1; i <= n; ++i) {
            zeros[i] = zeros[i - 1] + (nums[i - 1] == 0 ? 1 : 0);
            ones[i] = ones[i - 1] + (nums[i - 1] & 1);
        }
        int ans = 0;
        // 枚举区间右端点
        for (int right = 0; right < nums.length; ++right) {
            // 枚举左端点,只考虑长度超过已记录最大长度的区间
            for (int left = right - ans; left >= 0; --left) {
                // 当前区间长度一定>ans
                // 判断当前区间内0和1的个数是否相等
                if (ones[right + 1] - ones[left] == zeros[right + 1] - zeros[left]) {
                    // 若满足要求，则更新ans
                    ans = right - left + 1;
                }
            }
        }
        return ans;
    }


    /**
     * 方法二：前缀和 + 哈希表优化
     * 思路和算法
     *
     * 我们可以基于方法一利用数据结构进行进一步的优化，
     * 我们知道方法一的瓶颈在于对每个 i，我们需要枚举所有的 j 来判断是否符合条件，这一步是否可以优化呢？答案是可以的。
     *
     * 首先可以先计算出前缀和数组，然后枚举右端点r，枚举左端点l时，就能很快得出[l,r]的累加和，这样仍然超时
     *
     * 我们有更好的做法：【核心思想】如果我们把 0 视作 -1，就把题目转变成了：寻找和为 0 的最长子数组。
     *
     * 对于一个固定右端点i，我们想找到的是是否存在个某j满足 num[j...i]累加和为0
     *
     * 如果 [l, r]累加为0，说明[0...l]和[0...r]的前缀和相等
     *
     *
     * 所以对于当前右端点r，想知道是否存在l满足，nums[l...r]累加和是否为0
     * 也就是 是否存在l满足nums[0...l]累加和的结果与当前累加和一样
     *
     * 我们定义 preSum为[0...i]前缀累加和，那么 当前累加和 = preSum
     * 定义 pre[i] 为i之前部分[0...j]前缀和 %k == i 的下标j，
     * 如果存在 j 满足 pre[j] == preSum，那么 [j, i]部分和为0，计算此区间长度，更新ans
     *
     * 那么我们考虑以 i 结尾的连续子数组时，假设当前累加和=preSum, 只要判断前面是否存在[0...j]前缀和也等于preSum即可。
     * 我们建立哈希表 mp，以前前缀为键，【第一次】出现的位置j对应的值，从左往右边更新 mp 边计算答案，
     * 记住这里保存的是某个累加和第一次出现的位置，后面不更新，这样每当第二次出现时，就能得到最长的连续数组长度，只更新ans
     * 那么以 i 结尾的答案 即可在 O(1) 时间内得到。
     *
     * 需要注意的是，从左往右边更新边计算的时候已经保证了mp里记录的 pre[j] 的下标范围是 0≤j≤i 。
     * 同时，由于pre[i] 的计算只与前一项的答案有关，因此我们可以不用建立 pre 数组，直接用 pre 变量来记录 pre[i−1] 的答案即可。
     *
     * 复杂度分析
     *
     * 时间复杂度：O(n)，其中 n 为数组的长度。
     * 我们遍历数组的时间复杂度为 O(n)，中间利用哈希表查询删除的复杂度均为 O(1)，因此总时间复杂度为 O(n)。
     *
     * 空间复杂度：O(n)，其中 n 为数组的长度。哈希表在最坏情况下可能有 n 个不同的键值，因此需要 O(n) 的空间复杂度。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/subarray-sum-equals-k/solution/he-wei-kde-zi-shu-zu-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int findMaxLength2(int[] nums) {
        // 签注和
        int preSum = 0, ans = 0;
        Map<Integer, Integer> map = new HashMap<>();
        // base case，前缀和为0，标记出现位置为-1
        // 由于空的前缀的元素和为 0，因此在哈希表中存入键值对 (0,-1)，这一点是必要且合理的。
        map.put(0, -1);
        // 枚举右端点
        for (int i = 0; i < nums.length; ++i) {
            // [0...r]的累加和。0当作-1处理
            preSum += (nums[i] == 1 ? 1 : -1);
            // 的确存在j满足 [0...j]累加和与当前累加和一样
            // 并且子数组大小更大
            if (map.containsKey(preSum)) {
                if (i - map.get(preSum) > ans) {
                    // 更新ans
                    ans = i - map.get(preSum);
                }
                // 这里一定要加else，我们记录的是某个累加和第一次出现的位置，后面不更新，这样每当第二次出现时，就能得到最长的连续数组长度
            } else {
                // 否则，记录 当前累加和第一次出现的位置
                map.put(preSum, i);
            }
        }
        return ans;
    }
}
