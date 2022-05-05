package com.daily;

/**
 * @author wangwei
 * @date 2022/5/5 10:14
 * @description: _713_SubarrayProductLessThanK
 *
 * 713. 乘积小于 K 的子数组
 * 给你一个整数数组 nums 和一个整数 k ，请你返回子数组内所有元素的乘积严格小于 k 的连续子数组的数目。
 *
 *
 * 示例 1：
 *
 * 输入：nums = [10,5,2,6], k = 100
 * 输出：8
 * 解释：8 个乘积小于 100 的子数组分别为：[10]、[5]、[2],、[6]、[10,5]、[5,2]、[2,6]、[5,2,6]。
 * 需要注意的是 [10,5,2] 并不是乘积小于 100 的子数组。
 * 示例 2：
 *
 * 输入：nums = [1,2,3], k = 0
 * 输出：0
 *
 *
 * 提示:
 *
 * 1 <= nums.length <= 3 * 104
 * 1 <= nums[i] <= 1000
 * 0 <= k <= 106
 */
public class _713_SubarrayProductLessThanK {


    /**
     * 滑动窗口
     * 求的连续子数组，并且数组元素值 >= 1, 所以 累乘结果单调，求的是累乘结果 < k 的连续子数组个数，可以使用滑动窗口
     *
     * 每一个符合要求的窗口 [left, right]，所包含的 子连续子数组个数 = sum(这个窗口内每个右端点为结尾时的子数组个数)
     *
     * 比如 [1,2,3,4] 窗口滑动，右端点分别为 1，2，3，4
     * 当右端点为4时，以它为结尾的符合要求的子数组有 [1,2,3,4], [2,3,4], [3,4], [4]，共 4 个，等于这个窗口元素个数
     * 当右端点为3时，以它为结尾的符合要求的子数组有 [1,2,3], [2,3], [3], 共 3 个，等于这个窗口元素个数
     * 当右端点为2时，以它为结尾的符合要求的子数组有 [1,2], [2], 共 2 个，等于这个窗口元素个数
     * 当右端点为1时，以它为结尾的符合要求的子数组有 [1], 共 1 个，等于这个窗口元素个数
     *
     * 共10个
     *
     * 当然，统计之前，必须先保证这个窗口的元素累积结果 < k，
     *
     * 枚举右端点，左端点是 使得 窗口内元素累积结果 < k 的 最小的 left
     *
     *
     *
     * @param nums
     * @param k
     * @return
     */
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        int left = 0, right = 0, ans = 0, temp = 1;
        // 枚举右端点
        while (right < nums.length) {
            // 累积
            temp *= nums[right];
            // 找到使得窗口内元素累积和 < k 的第一个left
            while (left <= right && temp >= k) {
                temp /= nums[left];
                left++;
            }
            // 这个窗口内，所包含的所有 符合要求的 以right为右端点的 子连续数组个数
            ans += right - left + 1;
            // 枚举下一个右端点
            right++;
        }
        return ans;
    }

    public static void main(String[] args) {
        _713_SubarrayProductLessThanK obj = new _713_SubarrayProductLessThanK();
        obj.numSubarrayProductLessThanK(new int[]{10, 5, 2, 6}, 100);
    }
}
