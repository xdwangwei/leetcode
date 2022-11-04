package com.offerassult;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/11/4 12:30
 * @description: _040_MaximalRectangleAreaInMatrix
 * 剑指 Offer II 040. 矩阵中最大的矩形
 * 给定一个由 0 和 1 组成的矩阵 matrix ，找出只包含 1 的最大矩形，并返回其面积。
 *
 * 注意：此题 matrix 输入格式为一维 01 字符串数组。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：matrix = ["10100","10111","11111","10010"]
 * 输出：6
 * 解释：最大矩形如上图所示。
 * 示例 2：
 *
 * 输入：matrix = []
 * 输出：0
 * 示例 3：
 *
 * 输入：matrix = ["0"]
 * 输出：0
 * 示例 4：
 *
 * 输入：matrix = ["1"]
 * 输出：1
 * 示例 5：
 *
 * 输入：matrix = ["00"]
 * 输出：0
 *
 *
 * 提示：
 *
 * rows == matrix.length
 * cols == matrix[0].length
 * 0 <= row, cols <= 200
 * matrix[i][j] 为 '0' 或 '1'
 *
 *
 * 注意：本题与主站 85 题相同（输入参数格式不同）： https://leetcode-cn.com/problems/maximal-rectangle/
 *
 */
public class _040_MaximalRectangleAreaInMatrix {

    /**
     * https://leetcode-cn.com/problems/maximal-rectangle/solution/xiang-xi-tong-su-de-si-lu-fen-xi-duo-jie-fa-by-1-8/
     * 借助39题直方图最大矩形面积 的思想。相当于平移x轴上下，对于每一次的x轴，求出每一列的连续的1的高度，作为矩形高度，传给39题的solution
     * 每个x轴会得到一个直方图最大矩形面积，在所有结果中取最大值
     *
     * @param matrix
     * @return
     */
    public int maximalRectangle(String[] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length() == 0)
            return 0;
        int rows = matrix.length, cols = matrix[0].length();
        int[] curLayerHeights = new int[cols];
        int res = 0;
        // x轴逐渐向下平移
        for (int i = 0; i < rows; i++) {
            // 更新当前列上连续的1的个数，(从当前的x轴往上)
            for (int j = 0; j < cols; j++) {
                // 只有连续的1，当前列的矩阵高度才增加
                if (matrix[i].charAt(j) == '1')
                    curLayerHeights[j] += 1;
                    // 一旦出现0，相当于断开，不管对于当前的水平轴，还是再往下的水平轴，这个矩阵高度都要重新统计
                else
                    curLayerHeights[j] = 0;
            }
            // 扫描完一行，会更新列对应的每个矩阵高度，计算一次最大面积
            res = Math.max(res, largestRectangleArea8(curLayerHeights));
        }
        return res;
    }

    /**
     * 单调栈，二刷重写
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
    public int largestRectangleArea8(int[] heights) {
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
