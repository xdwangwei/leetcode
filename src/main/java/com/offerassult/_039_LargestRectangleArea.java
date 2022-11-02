package com.offerassult;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/11/2 15:30
 * @description: _039_LargestRectangleArea
 *
 * 剑指 Offer II 039. 直方图最大矩形面积
 * 给定非负整数数组 heights ，数组中的数字用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
 *
 * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
 *
 *
 *
 * 示例 1:
 *
 *
 *
 * 输入：heights = [2,1,5,6,2,3]
 * 输出：10
 * 解释：最大的矩形为图中红色区域，面积为 10
 * 示例 2：
 *
 *
 *
 * 输入： heights = [2,4]
 * 输出： 4
 *
 *
 * 提示：
 *
 * 1 <= heights.length <=105
 * 0 <= heights[i] <= 104
 *
 *
 * 注意：本题与主站 84 题相同： https://leetcode-cn.com/problems/largest-rectangle-in-histogram/
 */
public class _039_LargestRectangleArea {

    /**
     * 单调栈
     *
     * 假设以heights[i]为高度能够扩展的最大宽度为w，那么此举行面积为 heights[i] * w
     * 问题转变为寻找每个高度能扩展到的最左边界和最右边界
     * 也就是 在 arr 中寻找 最左边那个 >= arr[i] 的位置，和 最右边那个 >= arr[i]的位置
     * 转变为，在 arr 中寻找 左边第一个 < arr[i] 的位置 left，和 最右边第一个 < arr[i]的位置 right
     * 那么对于 arr[i] 来说，它扩展到的宽度就是 (left, right)中间部分，宽度为 right - left - 1，面积为 arr[i] * (right - left - 1)
     * 对每一个位置进行这个操作，取所有面积中的最大值
     *
     * 具体：
     *
     * 使用一个栈，
     * 遍历arr每个元素，如果 当前高度 < 栈顶高度，栈顶元素弹栈，
     * 当前元素入栈
     *
     * 按照这个规则，由于每个元素进栈前弹出了栈中所有比它大的元素，因此最终栈中从栈底到栈顶递增，并且每个位置下面就是它左边第一个比它小的数
     *
     * 因此，遍历arr每个元素，如果 当前高度 < 栈顶高度，栈顶元素弹栈，
     *      此时 当前位置就是 栈顶 右边第一个比它小的位置，而弹栈后新的栈顶就是 它左边第一个比它小的位置
     *      那么，以弹出的栈顶为高度的最大矩形面积就可以计算出来
     *
     * 因为要计算宽度，所以栈中记录下标，
     * 对于特殊情况，若弹栈后栈空，认为其left为-1，
     * 对于arr遍历完成后，最后一个元素入栈后，没有后续，它的右边界没有得到计算
     * 因此多加一轮循环，此时 下标是n，对应高度是0，能保证 原数组最后一个位置元素出栈，从而它对应的矩形得到计算
     * @param heights
     * @return
     */
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        // 寻找每个元素，左边第一个小于它的位置left，右边第一个小于它的位置right
        // 以它为高的最大矩形面积为 (right - left - 1) * height
        // 栈底到栈顶元素递增
        int ans = 0, n = heights.length;
        // 逐个遍历，多加一轮循环
        for (int i = 0; i <= n; ++i) {
            // 让下标为n时，对应高度为0
            int curH = i < n ? heights[i] : 0;
            // 若当前高度 < 栈顶高度
            while (!stack.isEmpty() && heights[stack.peek()] > curH) {
                // 栈顶高度，弹栈
                int h = heights[stack.pop()];
                // 此时新的栈顶就是它左边第一个小于它的位置left
                int left = stack.isEmpty() ? -1: stack.peek();
                // 当前位置就是它右边第一个小于它的位置right
                int right = i;
                // 以它为高的最大矩形面积
                int area = (right - left - 1) * h;
                // 取全部矩形最小面积
                ans = Math.max(ans, area);
            }
            // 当前索引入栈
            stack.push(i);
        }
        // 返回
        return ans;
    }
}
