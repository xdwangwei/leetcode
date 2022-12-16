package com.daily;

/**
 * @author wangwei
 * @date 2022/12/16 14:30
 * @description: _1785_MinimumElementsToAddToFormAGivenSum
 *
 * 1785. 构成特定和需要添加的最少元素
 * 给你一个整数数组 nums ，和两个整数 limit 与 goal 。数组 nums 有一条重要属性：abs(nums[i]) <= limit 。
 *
 * 返回使数组元素总和等于 goal 所需要向数组中添加的 最少元素数量 ，添加元素 不应改变 数组中 abs(nums[i]) <= limit 这一属性。
 *
 * 注意，如果 x >= 0 ，那么 abs(x) 等于 x ；否则，等于 -x 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,-1,1], limit = 3, goal = -4
 * 输出：2
 * 解释：可以将 -2 和 -3 添加到数组中，数组的元素总和变为 1 - 1 + 1 - 2 - 3 = -4 。
 * 示例 2：
 *
 * 输入：nums = [1,-10,9,1], limit = 100, goal = 0
 * 输出：1
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * 1 <= limit <= 106
 * -limit <= nums[i] <= limit
 * -109 <= goal <= 109
 * 通过次数17,981提交次数42,963
 */
public class _1785_MinimumElementsToAddToFormAGivenSum {

    /**
     * 方法一：贪心
     *
     * 思路与算法
     *
     * 我们用 sum 表示 nums 中所有元素的和，用 diff = abs(sum - goal) 来表示「当前总和」与「目标总和」的差距。
     *
     * 由于添加的元素所需要满足的范围是关于 0 对称的，所以当 sum>goal 时添加负数的情况可以被当做 sum<goal 时添加正数来处理。
     *
     * 因此只需要考虑 使用多少个不超过 limit 的数字 来凑齐 diff（>0），分两种情况：
     *
     * 贪心
     *
     *      若 limit 整除 diff，答案是 ⌊diff / limit⌋。
     *      若 limit 不整除 diff，答案是 ⌊diff / limit⌋ + 1。
     *
     *      以上两种情况可以使用一个表达式来计算：⌊(diff + limit - 1) / limit⌋
     *
     * 注意，本题中数组元素的数据范围为 [-10^6, 10^6]，元素个数最大为 10^5
     * 总和 s 以及差值 d 可能会超过 32 位整数的表示范围，因此我们使用 64 位整数来表示。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/minimum-elements-to-add-to-form-a-given-sum/solution/gou-cheng-te-ding-he-xu-yao-tian-jia-de-m3gnt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param limit
     * @param goal
     * @return
     */
    public int minElements(int[] nums, int limit, int goal) {
        long sum = 0;
        // 计算元素和
        for (int num : nums) {
            sum += num;
        }
        // 计算与目标的差值，取绝对值，只考虑 sum < goal 时，需要 几个 满足 < limit 的数字 来 让sum=goal
        sum = Math.abs(sum - goal);
        // 贪心。每次取limit，最后一次取<limit，合起来就是
        return (int)((sum + limit - 1) / limit);
    }
}
