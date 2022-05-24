package com.presum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/5/23 21:19
 * @description: _523_ContinuousSubarraySum
 *
 * 523. 连续的子数组和
 * 给你一个整数数组 nums 和一个整数 k ，编写一个函数来判断该数组是否含有同时满足下述条件的连续子数组：
 *
 * 子数组大小 至少为 2 ，且
 * 子数组元素总和为 k 的倍数。
 * 如果存在，返回 true ；否则，返回 false 。
 *
 * 如果存在一个整数 n ，令整数 x 符合 x = n * k ，则称 x 是 k 的一个倍数。0 始终视为 k 的一个倍数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [23,2,4,6,7], k = 6
 * 输出：true
 * 解释：[2,4] 是一个大小为 2 的子数组，并且和为 6 。
 * 示例 2：
 *
 * 输入：nums = [23,2,6,4,7], k = 6
 * 输出：true
 * 解释：[23, 2, 6, 4, 7] 是大小为 5 的子数组，并且和为 42 。
 * 42 是 6 的倍数，因为 42 = 7 * 6 且 7 是一个整数。
 * 示例 3：
 *
 * 输入：nums = [23,2,6,4,7], k = 13
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * 0 <= nums[i] <= 109
 * 0 <= sum(nums[i]) <= 231 - 1
 * 1 <= k <= 231 - 1
 * 通过次数80,526提交次数
 */
public class _523_ContinuousSubarraySum {

    /**
     * 方法一：枚举 枚举所有满足累加和可被k整除的连续子数组的右端点
     * 思路和算法
     *
     * 考虑以 i 结尾累加和可被k整除 的连续子数组，我们需要判断符合条件的下标 j 的个数，其中 0≤j≤i 且 [j..i] 这个子数组的累加和可被k整除。
     *
     * 我们可以枚举 [0..i] 里所有的下标 j 来判断是否符合条件，
     *
     * 可能有读者会认为假定我们确定了子数组的开头和结尾，还需要 O(n) 的时间复杂度遍历子数组来求和，那样复杂度就将达到 O(n^3)
     * 从而无法通过所有测试用例。
     * 但是如果我们知道 [j,i] 子数组的和，就能 O(1) 推出 [j−1,i] 的和，因此这部分的遍历求和是不需要的，
     * 我们在枚举下标 j 的时候已经能 O(1) 求出 [j,i] 的子数组之和。
     *
     * 复杂度分析
     *
     * 时间复杂度：O(n^2)，其中 n 为数组的长度。
     * 枚举子数组开头和结尾需要 O(n^2) 的时间，其中求和需要 O(1) 的时间复杂度，因此总时间复杂度为 O(n^2)
     * 空间复杂度：O(1)。只需要常数空间存放若干变量。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/subarray-sum-equals-k/solution/he-wei-kde-zi-shu-zu-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */
    public boolean checkSubarraySum(int[] nums, int k) {
        // 这里不在枚举端点的for循环初始化值上直接考虑枚举区间长度，是为了更简单的递推枚举区间累加和
        // 枚举右端点
        for (int right = 0; right < nums.length; ++right) {
            // 计算 [left, right] 的累加和
            // 倒着枚举左端点，这样，知道 [left, right]的累加和后，能马上知道 [left - 1, right] 的累加和
            int sum = 0;
            // 倒着枚举左端点
            for (int left = right; left >= 0; --left) {
                // [left, right]的累加和,子数组大小至少为2
                sum += nums[left];
                if (sum % k == 0 && right - left >= 1) {
                    // 找到一个累加和可被k整除的连续子数组
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 方法二：前缀和 + 哈希表优化
     * 思路和算法
     *
     * 我们可以基于方法一利用数据结构进行进一步的优化，
     * 我们知道方法一的瓶颈在于对每个 i，我们需要枚举所有的 j 来判断是否符合条件，这一步是否可以优化呢？答案是可以的。
     *
     * 首先可以先计算出前缀和数组，然后枚举右端点r，枚举左端点l时，就能很快得出[l,r]的累加和
     *
     * 但我们有更好的做法：
     *
     * 对于一个固定右端点i，我们想找到的是是否存在个某j满足 num[j...i]累加和能被k整除
     *
     * 如果 [l, r]累加和能够被k整除，说明 [0...l]累加和 % k ==== [0...r]累加和 % k
     *
     *
     * 所以对于当前右端点r，想知道是否存在l满足，nums[l...r]累加和为被k整除，
     * 也就是 是否存在l满足nums[0...l]累加和%k的结果与当前mod值一样
     *
     * 我们定义 preSum为[0...i]前缀累加和，那么 当前累加和 % k = mod = preSum % k
     * 定义 pre[i] 为i之前部分[0...j]前缀和 %k == i 的下标j，
     *
     * 那么我们考虑以 i 结尾的累加和被k整除 的连续子数组个数时，假设当前累加和%k=mod, 只要判断前面是否存在[0...j]前缀和%k也等于mod即可。
     * 我们建立哈希表 mp，以前前缀和%k的结果为键，最近出现位置j对应的值，从左往右边更新 mp 边计算答案，
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
     * @param k
     * @return
     */
    public boolean subarraysDivByK2(int[] nums, int k) {
        int preSum = 0;
        // 前缀和%k的结果为key，前缀和对应的j的位置为value
        Map<Integer, Integer> map = new HashMap<>();
        // base case，前缀和%k为0，标记出现位置为-1
        // 由于空的前缀的元素和为 0，因此在哈希表中存入键值对 (0,-1)，这一点是必要且合理的。
        map.put(0, -1);
        // 枚举右端点 r
        for (int i = 0; i < nums.length; ++i) {
            // 这里相当于得到的是 nums[0...r]的累加和
            preSum += nums[i];
            // [0...r]的累加和 % k 的结果
            int mod = preSum % k;
            // 的确存在j满足 [0...j]累加和%k与当前mod一样
            // 并且子数组大小>=2
            if (map.containsKey(mod) && i - map.get(mod) >= 2) {
                return true;
            }
            // 更新，记录第一次的前缀和%k=mod的位置是i
            map.put(mod, i);
        }
        // 返回
        return false;
    }
}
