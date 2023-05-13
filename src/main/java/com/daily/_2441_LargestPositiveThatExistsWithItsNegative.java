package com.daily;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/5/13 20:33
 * @description: _2441_LargestPositiveThatExists
 *
 * 2441. 与对应负数同时存在的最大正整数
 * 给你一个 不包含 任何零的整数数组 nums ，找出自身与对应的负数都在数组中存在的最大正整数 k 。
 *
 * 返回正整数 k ，如果不存在这样的整数，返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [-1,2,-3,3]
 * 输出：3
 * 解释：3 是数组中唯一一个满足题目要求的 k 。
 * 示例 2：
 *
 * 输入：nums = [-1,10,6,7,-7,1]
 * 输出：7
 * 解释：数组中存在 1 和 7 对应的负数，7 的值更大。
 * 示例 3：
 *
 * 输入：nums = [-10,8,6,7,-2,-3]
 * 输出：-1
 * 解释：不存在满足题目要求的 k ，返回 -1 。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 1000
 * -1000 <= nums[i] <= 1000
 * nums[i] != 0
 * 通过次数24,513提交次数33,775
 */
public class _2441_LargestPositiveThatExistsWithItsNegative {

    /**
     * 方法一：哈希表
     *
     * 我们可以用哈希表 s 记录数组中出现的所有元素，用一个变量 ans 记录满足题目要求的最大正整数，初始时 ans=−1。
     *
     * 具体地，
     * 遍历 nums 中的元素 x
     *      如果 s 中存在 -x，那么 x 符合要求，此时 如果 abs(x) > ans，则 更新 ans = x
     *      将 x 加入 s
     *
     * 遍历结束后，返回 ans 即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/largest-positive-integer-that-exists-with-its-negative/solution/python3javacgotypescript-yi-ti-yi-jie-ha-k3hx/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int findMaxK(int[] nums) {
        // 记录 nums 元素
        Set<Integer> set = new HashSet<>();
        // 初始化
        int ans = -1;
        for (int num : nums) {
            // 如果 set 中存在 其相反数，那么其符合要求
            // 如果其绝对值 > ans，更新 ans
            if (set.contains(-num) && Math.abs(num) > ans) {
                ans = Math.abs(num);
            }
            // 加入 s
            set.add(num);
        }
        // 返回
        return ans;
    }
}
