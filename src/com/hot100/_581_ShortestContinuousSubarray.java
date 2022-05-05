package com.hot100;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/5/4 16:49
 * @description: _581_ShortestContinuousSubarray
 *
 * 581. 最短无序连续子数组
 * 给你一个整数数组 nums ，你需要找出一个 连续子数组 ，如果对这个子数组进行升序排序，那么整个数组都会变为升序排序。
 *
 * 请你找出符合题意的 最短 子数组，并输出它的长度。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [2,6,4,8,10,9,15]
 * 输出：5
 * 解释：你只需要对 [6, 4, 8, 10, 9] 进行升序排序，那么整个表都会变为升序排序。
 * 示例 2：
 *
 * 输入：nums = [1,2,3,4]
 * 输出：0
 * 示例 3：
 *
 * 输入：nums = [1]
 * 输出：0
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 104
 * -105 <= nums[i] <= 105
 *
 *
 * 进阶：你可以设计一个时间复杂度为 O(n) 的解决方案吗？
 */
public class _581_ShortestContinuousSubarray {


    /**
     * 方法一：排序
     * 思路与算法
     *
     * 我们将给定的数组 nums 表示为三段子数组拼接的形式，分别记作 numsA、numsB，numsC
     *
     * 当我们对 numsB 进行排序，整个数组将变为有序。换而言之，当我们对整个序列进行排序，numsA 和 numsC 都不会改变。
     *
     * 本题要求我们找到最短的 numsB，即找到最大的 numsA 和 numsC 的长度之和。
     * 因此我们将原数组 nums 排序与原数组进行比较，取最长的相同的前缀为 numsA，取最长的相同的后缀为 numsC，这样我们就可以取到最短的 numsB
     *
     * 具体地，我们创建数组 nums 的拷贝，记作数组 arr，并对该数组进行排序，
     * 然后我们从左向右找到第一个两数组不同的位置，即为 numsB 的左边界。同理也可以找到 numsB 右边界。
     * 最后我们输出 numsB 的长度即可。
     *
     * 特别地，当原数组有序时，numsB 的长度为 0，我们可以直接返回结果。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/shortest-unsorted-continuous-subarray/solution/zui-duan-wu-xu-lian-xu-zi-shu-zu-by-leet-yhlf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int findUnsortedSubarray(int[] nums) {
        int n = nums.length;
        // 只有一个元素
        if (n == 1) {
            return 0;
        }
        // 拷贝一份，并排序
        int[] arr = Arrays.copyOf(nums, n);
        Arrays.sort(arr);
        int i = 0;
        // 从左往右找到第一个改变了的元素位置
        for (; i < n; ++i) {
            if (arr[i] != nums[i]) {
                break;
            }
        }
        // 说明原数组是有序数组，直接返回0
        if (i == n) {
            return 0;
        }
        // 从右往左，找到第一个改变了的元素位置
        int j = n - 1;
        for (; j >= 0; --j) {
            if (arr[j] != nums[j]) {
                break;
            }
        }
        // 这两个元素之间的元素 就是 原数组中 需要排序的部分长度，并且是 最小的
        // 因为你完全可以从 left - 1 排序 到 right + 1， 最后结果都是一样的，但是 left-1 和 right+1 这两元素没必要排序，排序完还在原位置
        return j - i + 1;
    }
}
