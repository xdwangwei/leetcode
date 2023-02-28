package com.daily;

/**
 * @author wangwei
 * @date 2023/2/28 12:02
 * @description: _1144_DecreaseElementsToMakeArrayZigzag
 *
 * 1144. 递减元素使数组呈锯齿状
 * 给你一个整数数组 nums，每次 操作 会从中选择一个元素并 将该元素的值减少 1。
 *
 * 如果符合下列情况之一，则数组 A 就是 锯齿数组：
 *
 * 每个偶数索引对应的元素都大于相邻的元素，即 A[0] > A[1] < A[2] > A[3] < A[4] > ...
 * 或者，每个奇数索引对应的元素都大于相邻的元素，即 A[0] < A[1] > A[2] < A[3] > A[4] < ...
 * 返回将数组 nums 转换为锯齿数组所需的最小操作次数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,2,3]
 * 输出：2
 * 解释：我们可以把 2 递减到 0，或把 3 递减到 1。
 * 示例 2：
 *
 * 输入：nums = [9,6,1,6,2]
 * 输出：4
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 1000
 * 通过次数30,838提交次数61,004
 */
public class _1144_DecreaseElementsToMakeArrayZigzag {


    /**
     * 方法：贪心
     *
     * 两种锯齿形
     * 一：每个奇数位置元素值都小于左右两次相邻元素值，相当于把nums[i]减小到 min(nums[i-1],nums[i+1])-1  （i为奇数）
     * 二：每个奇数位置元素值都大于左右两次相邻元素值，相当于把nums[i]增加到 max(nums[i-1],nums[i+1])+1  （i为奇数）
     *
     * 由于题目限制 nums[i] 只能减小，不能增加，因此 第二种锯齿形可以看作
     * 二：每个偶数位置元素值都小于左右两次相邻元素值，相当于把nums[i]减小到 min(nums[i-1],nums[i+1])-1  （i为偶数）
     *
     * 统计两种方式下（减小奇数位置，减小偶数位置），所需要的变化次数，选择更小的值
     * @param nums
     * @return
     */
    public int movesToMakeZigzag(int[] nums) {
        int n = nums.length;
        // 两种变换实现，所需操作次数
        int odd = 0, even = 0;
        for (int i = 0; i < n; i++) {
            // 左边数字
            int left = i > 0 ? nums[i - 1] : Integer.MAX_VALUE;
            // 右边数字
            int right = i + 1 < n ? nums[i + 1] : Integer.MAX_VALUE;
            // 将 nums[i] 减小到 target = min(left,right) - 1 所需的操作次数
            // 若 nums[i] >= target, 操作次数为 nums[i] - target
            // 若 nums[i] < target, 已经满足，操作次数为 0
            int ops =  Math.max(0, nums[i] - (Math.min(left, right) - 1));
            // 根据当前是奇数位置还是偶数位置数字，给对应变换方式需要的次数进行累加
            if ((i & 1) == 0) {
                even += ops;
            } else {
                odd += ops;
            }
        }
        // 返回两种变换需要的操作次数的最小值
        return Math.min(odd ,even);
    }
}
