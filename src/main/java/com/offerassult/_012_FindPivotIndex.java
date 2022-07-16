package com.offerassult;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/5/25 18:57
 * @description: _012_FindPivotIndex
 *
 * 剑指 Offer II 012. 左右两边子数组的和相等
 * 给你一个整数数组 nums ，请计算数组的 中心下标 。
 *
 * 数组 中心下标 是数组的一个下标，其左侧所有元素相加的和等于右侧所有元素相加的和。
 *
 * 如果中心下标位于数组最左端，那么左侧数之和视为 0 ，因为在下标的左侧不存在元素。这一点对于中心下标位于数组最右端同样适用。
 *
 * 如果数组有多个中心下标，应该返回 最靠近左边 的那一个。如果数组不存在中心下标，返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,7,3,6,5,6]
 * 输出：3
 * 解释：
 * 中心下标是 3 。
 * 左侧数之和 sum = nums[0] + nums[1] + nums[2] = 1 + 7 + 3 = 11 ，
 * 右侧数之和 sum = nums[4] + nums[5] = 5 + 6 = 11 ，二者相等。
 * 示例 2：
 *
 * 输入：nums = [1, 2, 3]
 * 输出：-1
 * 解释：
 * 数组中不存在满足此条件的中心下标。
 * 示例 3：
 *
 * 输入：nums = [2, 1, -1]
 * 输出：0
 * 解释：
 * 中心下标是 0 。
 * 左侧数之和 sum = 0 ，（下标 0 左侧不存在元素），
 * 右侧数之和 sum = nums[1] + nums[2] = 1 + -1 = 0 。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 104
 * -1000 <= nums[i] <= 1000
 *
 *
 * 注意：本题与主站 724 题相同： https://leetcode-cn.com/problems/find-pivot-index/
 */
public class _012_FindPivotIndex {


    /**
     * pivot位置满足 sum[0...pivot) == sum(pivot+1...len-1] = x
     * 所以 x + nums[pivot] + x == sum(nums)
     * 所以，先求出数组元素和 sum
     * 再遍历数组元素num[i]，某个位置满足 2 * sum[0...i) + nums[i] == sum，就返回 i
     * @param nums
     * @return
     */
    public int pivotIndex(int[] nums) {
        // 数组元素和 sum
        int sum = Arrays.stream(nums).sum();
        // leftSum表示 sum[0...i)即 i 位置左边元素和
        int leftSum = 0;
        // 遍历
        for (int i = 0; i < nums.length; ++i) {
            // 位置满足 i左边元素和 = i 右边元素和
            // 那么 2 * leftSum 就应该 ==  sum - nums[i]
            if (2 * leftSum + nums[i] == sum) {
                // 返回
                return i;
            }
            // leftSum不包含nums[i]
            leftSum += nums[i];
        }
        // 不存在pivot位置
        return -1;
    }

    public static void main(String[] args) {
        _012_FindPivotIndex obj = new _012_FindPivotIndex();
        obj.pivotIndex(new int[]{1, 7, 3, 6, 5, 6});
        obj.pivotIndex(new int[]{-1, -1, -1, -1,- 1, -1});
    }
}
