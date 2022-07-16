package com.offerassult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/5/24 10:31
 * @description: _011_LongestContinuousSubarrayOfEqualNumberOf0and1
 *
 * 剑指 Offer II 011. 0 和 1 个数相同的子数组
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
 *
 * 注意：本题与主站 525 题相同： https://leetcode-cn.com/problems/contiguous-array/
 */
public class _011_LongestContinuousSubarrayOfEqualNumberOf0and1 {

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
     *
     * @param nums
     * @return
     */
    public int findMaxLength2(int[] nums) {
        int preSum = 0, ans = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        for (int i = 0; i < nums.length; ++i) {
            preSum += (nums[i] == 1 ? 1 : -1);
            if (map.containsKey(preSum)) {
                if (i - map.get(preSum) > ans) {
                    ans = i - map.get(preSum);
                }
            } else {
                map.put(preSum, i);
            }
        }
        return ans;
    }
}
