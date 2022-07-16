package com.window;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/29 周五 15:39
 *
 * 11. 盛最多水的容器
 * 给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
 *
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 *
 * 返回容器可以储存的最大水量。
 *
 * 说明：你不能倾斜容器。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：[1,8,6,2,5,4,8,3,7]
 * 输出：49
 * 解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
 * 示例 2：
 *
 * 输入：height = [1,1]
 * 输出：1
 *
 *
 * 提示：
 *
 * n == height.length
 * 2 <= n <= 105
 * 0 <= height[i] <= 104
 *
 **/
public class _11_ContainerWithMostWater {

    /**
     * 双指针，
     * 因为不用考虑柱子宽度，那么对于左右边界分别为left和right的范围来说，能装的水量就是 min(h[l], h[r]) * (r - l)
     * 然后，哪边柱子低，就移动哪个指针，直到两指针相遇退出，在此过程中，比较更新最大积水量
     * @param height
     * @return
     */
    public static int maxArea(int[] height) {
        // 左右指针
        int lo = 0, hi = height.length - 1, maxArea = 0;
        // 左右指针相遇，退出
        while (lo < hi) {
            // 当前范围能得到的水量
            maxArea = Math.max(maxArea, Math.min(height[lo], height[hi]) * (hi - lo));
            // 那边低就移动哪个指针
            if (height[lo] < height[hi]) ++lo;
            else --hi;
        }
        // 返回结果
        return maxArea;
    }
}
