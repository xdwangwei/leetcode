package com.daily;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/3/26 12:38
 * @description: _2395_FindSubarraysWithEqualSum
 *
 * 2395. 和相等的子数组
 * 给你一个下标从 0 开始的整数数组 nums ，判断是否存在 两个 长度为 2 的子数组且它们的 和 相等。注意，这两个子数组起始位置的下标必须 不相同 。
 *
 * 如果这样的子数组存在，请返回 true，否则返回 false 。
 *
 * 子数组 是一个数组中一段连续非空的元素组成的序列。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [4,2,4]
 * 输出：true
 * 解释：元素为 [4,2] 和 [2,4] 的子数组有相同的和 6 。
 * 示例 2：
 *
 * 输入：nums = [1,2,3,4,5]
 * 输出：false
 * 解释：没有长度为 2 的两个子数组和相等。
 * 示例 3：
 *
 * 输入：nums = [0,0,0]
 * 输出：true
 * 解释：子数组 [nums[0],nums[1]] 和 [nums[1],nums[2]] 的和相等，都为 0 。
 * 注意即使子数组的元素相同，这两个子数组也视为不相同的子数组，因为它们在原数组中的起始位置不同。
 *
 *
 * 提示：
 *
 * 2 <= nums.length <= 1000
 * -109 <= nums[i] <= 109
 * 通过次数15,459提交次数20,254
 */
public class _2395_FindSubarraysWithEqualSum {

    /**
     * 方法一：哈希表
     *
     * 寻找的是长度为2的子数组，也就是 nums[i] 和 nums[i+1]，长度为 n 的数组中总共包含 n-1 个长度为2的子数组
     *
     * 我们可以遍历数组 nums，用哈希表 vis 记录数组中每两个相邻元素的和，如果当前两个元素的和已经在哈希表中出现过，则返回 true，否则将当前两个元素的和加入哈希表中。
     *
     * 遍历结束后，说明没有找到满足条件的两个子数组，返回 false。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/find-subarrays-with-equal-sum/solution/python3javacgotypescript-yi-ti-yi-jie-ha-gcx7/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public boolean findSubarrays(int[] nums) {
        Set<Integer> set = new HashSet<>();
        // 遍历所有长度为2的子数组
        for (int i = 0; i < nums.length - 1; ++i) {
            // 子数组元素和
            int cur = nums[i] + nums[i + 1];
            // 如果已出现过，返回true
            if (set.contains(cur)) {
                return true;
            }
            // 加入当前元素和
            set.add(cur);
        }
        // 始终没有重复值，返回 false
        return false;
    }
}
