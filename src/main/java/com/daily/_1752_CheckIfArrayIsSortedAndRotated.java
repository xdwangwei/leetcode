package com.daily;

/**
 * @author wangwei
 * @date 2022/11/27 10:37
 * @description: _1752_CheckIfArrayIsSortedAndRotated
 * 1752. 检查数组是否经排序和轮转得到
 * 给你一个数组 nums 。nums 的源数组中，所有元素与 nums 相同，但按非递减顺序排列。
 *
 * 如果 nums 能够由源数组轮转若干位置（包括 0 个位置）得到，则返回 true ；否则，返回 false 。
 *
 * 源数组中可能存在 重复项 。
 *
 * 注意：我们称数组 A 在轮转 x 个位置后得到长度相同的数组 B ，当它们满足 A[i] == B[(i+x) % A.length] ，其中 % 为取余运算。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [3,4,5,1,2]
 * 输出：true
 * 解释：[1,2,3,4,5] 为有序的源数组。
 * 可以轮转 x = 3 个位置，使新数组从值为 3 的元素开始：[3,4,5,1,2] 。
 * 示例 2：
 *
 * 输入：nums = [2,1,3,4]
 * 输出：false
 * 解释：源数组无法经轮转得到 nums 。
 * 示例 3：
 *
 * 输入：nums = [1,2,3]
 * 输出：true
 * 解释：[1,2,3] 为有序的源数组。
 * 可以轮转 x = 0 个位置（即不轮转）得到 nums 。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 100
 * 通过次数16,153提交次数26,386
 */
public class _1752_CheckIfArrayIsSortedAndRotated {

    /**
     * 其实就是判断数组nums是否为有序数组从某个位置断开后，拼接右部分，再拼接左部分得到
     * 比如 nums=[3,4,5,1,2] 就来自于 nums=[1,2,3,4,5] 从 2后面断开，然后[3,4,5]拼接[1,2]得到
     *
     * 按照这种规则，重新得到的nums中只会有一个位置满足 nums[i] > nums[i+1]，并且 nums[0...i],nums[i+1...n]都是递增的
     * 但这还不够，比如[2,1,3,4]满足 [2]、[1,3,4]都递增，但是它是没办法通过 [1,2,3,4]得到的
     *
     * 因此，在限制一：nums中只会有一个位置满足 nums[i] > nums[i+1]，并且 nums[0...i],nums[i+1...n]都是递增的
     * 的基础上。还有另一个限制：就是  nums[0...i] 的元素一定是都大于等于 nums[i+1...n] 的元素的
     * 因为 nums[0...i] 来自于 原有序数组后半部分，而 nums[i+1...n] 来自于原有序数组前半部分
     *
     * 综上，nums应该满足：
     *      有且只有一个位置 满足 nums[i] > nums[i+1]
     *      且 min(nums[0...i]) >= max(nums[i+1...n])
     *
     * @param nums
     * @return
     */
    public boolean check(int[] nums) {
        int n = nums.length;
        // i 用来寻找 满足 nums[i] > nums[i+1] 的位置
        // min 保存 nums[0...i] 的最小值
        int i = 0, min = 101;
        // 注意取值不要越界
        while (i < n - 1) {
            // 更新min
            min = Math.min(min, nums[i]);
            // 发现 nums[i] > nums[i+1]
            if (nums[i] > nums[i + 1]) {
                // nums[i+1...n] 位置 不再存在 j 满足 nums[j] > nums[j + 1]
                // 且 nums[i+1...n]位置所有元素都要 < min
                //  注意下标不要越界
                for (int j = i + 1; j < n; ++j) {
                    if (nums[j] > min || (j < n - 1 && (nums[j] > nums[j + 1] || nums[j + 1] > min))) {
                        return false;
                    }
                }
                // 如果 后半部分满足规则，直接返回
                return true;
            }
            i++;
        }
        // 返回
        return true;
    }


    /**
     * 简化方法一过程：
     *
     * 从方法一我们知道 nums 需要满足
     *
     *      有且只有一个位置 满足 nums[i] > nums[i+1]
     *      且 min(nums[0...i]) >= max(nums[i+1...n])
     *
     * 那么 当寻找到 位置 i 后，我们知道 nums[0...i]递增，nums[i+1...n]递增，且 min(nums[0...i]) >= max(nums[i+1...n])
     *  也就是 nums[0] >= nums[n]
     * @param nums
     * @return
     */
    public boolean check2(int[] nums) {
        int n = nums.length, idx = -1, flag = 0;
        for (int i = 0; i < n - 1; ++i) {
            if (nums[i] > nums[i + 1]) {
                // 有且只有一个位置 满足 nums[i] > nums[i+1]
                if (++flag > 1) {
                    return false;
                }
                // 记录这个位置
                idx = i;
            }
        }
        // idx=-1说明不存在nums[i] > nums[i+1]，即nums递增，返回true，
        // 否则，idx将nums分为两部分，后一部分最大小不得超过前半部分最小值
        return idx == -1 || nums[0] >= nums[n - 1];
    }
}
