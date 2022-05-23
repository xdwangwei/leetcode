package com.offerassult;

/**
 * @author wangwei
 * 2020/7/29 9:49
 *
 * offer assult 008. 长度最小的子数组
 * 给定一个含有 n 个正整数的数组和一个正整数 s ，找出该数组中满足其和 ≥ s 的长度最小的 连续 子数组，并返回其长度。如果不存在符合条件的子数组，返回 0。
 *
 *
 * 示例：
 *
 * 输入：s = 7, nums = [2,3,1,2,4,3]
 * 输出：2
 * 解释：子数组 [4,3] 是该条件下的长度最小的子数组。
 *
 */
public class _008_MinimumSizeSubarraySum {

    /**
     * 滑动窗口
     *
     * 定义两个指针 start 和 end 分别表示子数组（滑动窗口窗口）的开始位置和结束位置，维护变量 sum 存储子数组中的元素和（即从 nums[start] 到 nums[end] 的元素和）。
     *
     * 初始状态下，start 和 end 都指向下标 0，sum 的值为 0。
     *
     * 每一轮迭代，将 nums[end] 加到 sum，如果 sum≥s，则更新子数组的最小长度（此时子数组的长度是 end−start+1），
     * 然后将 nums[start] 从 sum 中减去并将 start 右移，直到 sum<s，在此过程中同样更新子数组的最小长度。
     * 在每一轮迭代的最后，将 end 右移。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/minimum-size-subarray-sum/solution/chang-du-zui-xiao-de-zi-shu-zu-by-leetcode-solutio/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param target
     * @param nums
     * @return
     */
    public int minSubArrayLen(int target, int[] nums) {
        int left = 0, right = 0;
        // 最终结果，初始化一个不可能结果
        int len = nums.length + 1;
        // 当前窗口连续和
        int sum = 0;
        while (right < nums.length) {
            // 加入当前元素到窗口
            // 右边界扩大
            // 这里直接扩大有边界了，所以当前窗口内元素个数变为 right - left
            sum += nums[right++];

            // 判断缩小左窗口
            while (sum >= target) {
                // 更新结果集
                len = Math.min(len, right - left);
                // if (right - left < len) {
                //     len = right - left;
                // }
                // 窗口左边界右移
                sum -= nums[left++];
            }
        }
        // len未改变就是未找到合适结果
        return len == nums.length + 1 ? 0 : len;
    }

}
