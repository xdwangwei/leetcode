package com.presum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/5/23 20:29
 * @description: _1248_CountNumerOfNiceSubarrays
 *
 * 1248. 统计「优美子数组」
 * 给你一个整数数组 nums 和一个整数 k。如果某个连续子数组中恰好有 k 个奇数数字，我们就认为这个子数组是「优美子数组」。
 *
 * 请返回这个数组中 「优美子数组」 的数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,1,2,1,1], k = 3
 * 输出：2
 * 解释：包含 3 个奇数的子数组是 [1,1,2,1] 和 [1,2,1,1] 。
 * 示例 2：
 *
 * 输入：nums = [2,4,6], k = 1
 * 输出：0
 * 解释：数列中不包含任何奇数，所以不存在优美子数组。
 * 示例 3：
 *
 * 输入：nums = [2,2,2,1,2,2,1,2,2,2], k = 2
 * 输出：16
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 50000
 * 1 <= nums[i] <= 10^5
 * 1 <= k <= nums.length
 * 通过次数41,764提交次数73,305
 *
 * 类似题目：560，974，523
 */
public class _1248_CountNumerOfNiceSubarrays {


    /**
     * 方法一：枚举 枚举所有满足要求的连续子数组的右端点
     * 思路和算法
     *
     * 考虑以 i 结尾含奇数数字 k个 的连续子数组个数，我们需要统计符合条件的下标 j 的个数，其中 0≤j≤i 且 [j..i] 这个连续子数组的包含 k个奇数。
     *
     * 我们可以枚举 [0..i] 里所有的下标 j 来判断是否符合条件，
     *
     * 可能有读者会认为假定我们确定了子数组的开头和结尾，还需要 O(n) 的时间复杂度遍历子数组来求奇数个数，那样复杂度就将达到 O(n^3)
     * 从而无法通过所有测试用例。
     * 但是如果我们知道 [j,i] 子数组中奇数个数，就能 O(1) 推出 [j−1,i] 的中奇数个数，因此这部分的遍历是不需要的，
     * 我们在枚举下标 j 的时候已经能 O(1) 求出 [j,i] 的子数组中奇数个数。
     *
     * 复杂度分析
     *
     * 时间复杂度：O(n^2)，其中 n 为数组的长度。
     * 枚举子数组开头和结尾需要 O(n^2) 的时间，其中求奇数数目需要 O(1) 的时间复杂度，因此总时间复杂度为 O(n^2)
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
    public int numberOfSubarrays(int[] nums, int k) {
        // 结果
        int ans = 0;
        // 枚举连续子数组右端点
        for (int right = 0; right < nums.length; ++right) {
            // 枚举连续子数组左端点，统计 [左，右]内奇数数目
            int count = 0;
            for (int left = right; left >= 0; --left) {
                // 若当前数字为奇数，则奇数数目+1
                count += (nums[left] & 1);
                // 如果正好有k个奇数，说明 [left, right]是一个符合要求的连续子数组
                if (count == k) {
                    ans++;
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
     * 我们定义 pre[i] 为 [0..i] 里奇数个数，则 pre[i] 可以由 pre[i−1] 递推而来，即：pre[i] = pre[i−1] + (nums[i] & 1)
     * 所以注意这里的“前缀和”概念
     *
     * 这样可以先计算出前缀和数组，然后枚举右端点r，枚举左端点l时，就能很快得出[l,r]的奇数个数
     *
     * 但我们有更好的办法
     *
     * 对于一个固定右端点i，我们想找到的是有几个j满足 奇数数目[j...i] = k
     * 那么「[j..i] 这个子数组奇数数目为 k 」这个条件我们可以转化为: pre[i] − pre[j−1] == k
     *
     * 简单移项可得 符合条件的下标 j 需要满足: pre[j−1] == pre[i]−k
     *
     * 所以我们考虑以 i 结尾的包含k个奇数的连续子数组个数时 只要统计前面有多少个“前缀和”为 pre[i]−k 的 pre[j] 即可。
     * 我们建立哈希表 mp，以奇数个数为键，出现次数为对应的值，记录 pre[i] 出现的次数，从左往右边更新 mp 边计算答案，
     * 那么以 i 结尾的答案 mp[pre[i]−k] 即可在 O(1) 时间内得到。最后的答案即为所有奇数个数为 k 的子数组个数之和。
     *
     * 需要注意的是，从左往右边更新边计算的时候已经保证了mp[pre[i]−k] 里记录的 pre[j] 的下标范围是 0≤j≤i 。
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
    public int numberOfSubarray2(int[] nums, int k) {
        int ans = 0, preCount = 0;
        // “前缀和”为key，个数为value
        Map<Integer, Integer> map = new HashMap<>();
        // base case，0个奇数，只有1种可能
        // 对于一开始的情况，下标 0 之前没有元素，可以认为奇数个数为 0，个数为 1 个，因此 preSumFreq.put(0, 1);，这一点是必要且合理的。
        map.put(0, 1);
        // 枚举右端点
        for (int num : nums) {
            // [0...r]内奇数个数
            preCount += (num & 1);
            // 要知道有几个j满足 [j...i]内奇数个数为k，及有几个j满足[0...j]内奇数个数为 preCount - k
            ans += map.getOrDefault(preCount - k, 0);
            // 更新奇数个数为preCount的可能情况增多1
            map.put(preCount, map.getOrDefault(preCount, 0) + 1);
        }
        return ans;
    }
}
