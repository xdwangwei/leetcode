package com.daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/5/8 13:02
 * @description: _442_FindAllDulplicatesInArray
 *
 * 442. 数组中重复的数据
 * 给你一个长度为 n 的整数数组 nums ，其中 nums 的所有整数都在范围 [1, n] 内，且每个整数出现 一次 或 两次 。请你找出所有出现 两次 的整数，并以数组形式返回。
 *
 * 你必须设计并实现一个时间复杂度为 O(n) 且仅使用常量额外空间的算法解决此问题。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [4,3,2,7,8,2,3,1]
 * 输出：[2,3]
 * 示例 2：
 *
 * 输入：nums = [1,1,2]
 * 输出：[1]
 * 示例 3：
 *
 * 输入：nums = [1]
 * 输出：[]
 *
 *
 * 提示：
 *
 * n == nums.length
 * 1 <= n <= 105
 * 1 <= nums[i] <= n
 * nums 中的每个元素出现 一次 或 两次
 */
public class _442_FindAllDulplicatesInArray {


    /**
     * 由于给定的 n 个数都在 [1, n] 的范围内，有数字出现了两次，其他数字只出现一次，并且每个数字都是正整数
     * 那么如果建立 数字到索引的映射关系，idx = num - 1  [0, n - 1]
     * 如果 num 只出现一次，那么 num-1这个索引位置 就操作一次，我们将对应位置的数字变成对应的负数
     * 如果 num 出现两次，那么 第二次遇到 num 时，又会得到 num-1这个索引，我们发现这个位置数字已经变成负数，就说明，num(产生这个索引的数字) 这个数字出现了两次
     * 就意味着 [1,n][1,n] 中有数字没有出现过。
     *
     * 因此，我们可以尝试将每一个数放在对应的位置。
     *
     * 具体地，我们首先对数组进行一次遍历。当遍历到位置 i 时，我们考虑 nums[nums[i]−1] 的正负性：
     *
     * 如果 nums[nums[i]−1] 是正数，说明 nums[i] 还没有出现过，我们将 nums[nums[i]−1] 加上负号；
     *
     * 如果 nums[nums[i]−1] 是负数，说明 nums[i] 已经出现过一次，我们将 nums[i] 放入答案。
     *
     * 细节
     *
     * 由于 nums[i] 本身可能已经为负数，因此在将 \nums[i] 作为下标或者放入答案时，需要取绝对值。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/find-all-duplicates-in-an-array/solution/shu-zu-zhong-zhong-fu-de-shu-ju-by-leetc-782l/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public List<Integer> findDuplicates(int[] nums) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            // 建立数字到索引的映射关系，注意 有些数字可能已经被变为相反数，因此取索引要加绝对值
            int idx = Math.abs(nums[i]) - 1;
            // 如果这个位置的数字已经是负数，说明这个索引已经出现过，说明 产生这个索引的数字 在原数组中出现了两次
            if (nums[idx] < 0) {
                res.add(idx + 1);
            } else {
                // 否则变为相反数
                nums[idx] = -nums[idx];
            }
        }
        return res;
    }
}
