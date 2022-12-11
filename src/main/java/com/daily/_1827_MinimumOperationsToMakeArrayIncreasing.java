package com.daily;

/**
 * @author wangwei
 * @date 2022/12/11 9:50
 * @description: _1827_MinimumOperationsToMakeArrayIncreasing
 *
 * 1827. 最少操作使数组递增
 * 给你一个整数数组 nums （下标从 0 开始）。每一次操作中，你可以选择数组中一个元素，并将它增加 1 。
 *
 * 比方说，如果 nums = [1,2,3] ，你可以选择增加 nums[1] 得到 nums = [1,3,3] 。
 * 请你返回使 nums 严格递增 的 最少 操作次数。
 *
 * 我们称数组 nums 是 严格递增的 ，当它满足对于所有的 0 <= i < nums.length - 1 都有 nums[i] < nums[i+1] 。一个长度为 1 的数组是严格递增的一种特殊情况。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,1,1]
 * 输出：3
 * 解释：你可以进行如下操作：
 * 1) 增加 nums[2] ，数组变为 [1,1,2] 。
 * 2) 增加 nums[1] ，数组变为 [1,2,2] 。
 * 3) 增加 nums[2] ，数组变为 [1,2,3] 。
 * 示例 2：
 *
 * 输入：nums = [1,5,2,4,1]
 * 输出：14
 * 示例 3：
 *
 * 输入：nums = [8]
 * 输出：0
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 5000
 * 1 <= nums[i] <= 104
 * 通过次数20,836提交次数26,356
 */
public class _1827_MinimumOperationsToMakeArrayIncreasing {

    /**
     * 贪心：
     * 因为每个数字只能增加不能减少，每次只能增加1，
     * 为了让操作次数最少，我们让每个数字至少为前一个数字+1
     * 当然，如果它本身就比前一个数字大，那就不用增加了
     * @param nums
     * @return
     */
    public int minOperations(int[] nums) {
        // 只有一个元素
        if (nums.length <= 1) {
            return 0;
        }
        // pre记录前一个数字
        int pre = nums[0];
        // 操作操作
        int ans = 0;
        // 从第2个元素往后
        for (int i = 1; i < nums.length; ++i) {
            // 每个元素至少要变为前一个数字加1
            pre = Math.max(pre + 1, nums[i]);
            // 变成前一个数字加1需要的操作次数
            ans += pre - nums[i];
        }
        // 返回
        return ans;
    }
}
