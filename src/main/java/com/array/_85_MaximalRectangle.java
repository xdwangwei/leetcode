package com.array;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * 2020/4/8 17:07
 * <p>
 * 给定一个仅包含 0 和 1 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积。
 * <p>
 * 示例:
 * <p>
 * 输入:
 * [
 * ["1","0","1","0","0"],
 * ["1","0","1","1","1"],
 * ["1","1","1","1","1"],
 * ["1","0","0","1","0"]
 * ]
 * 输出: 6
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximal-rectangle
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _85_MaximalRectangle {

    /**
     * https://leetcode-cn.com/problems/maximal-rectangle/solution/xiang-xi-tong-su-de-si-lu-fen-xi-duo-jie-fa-by-1-8/
     * 借助84题直方图最大矩形面积 的思想。相当于平移x轴上下，对于每一次的x轴，求出每一列的连续的1的高度，作为矩形高度，传给84题的solution
     * 每个x轴会得到一个直方图最大矩形面积，在所有结果中取最大值
     *
     * @param matrix
     * @return
     */
    public int maximalRectangle(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0)
            return 0;
        int rows = matrix.length, cols = matrix[0].length;
        int[] curLayerHeights = new int[cols];
        int res = 0;
        // x轴逐渐向下平移
        for (int i = 0; i < rows; i++) {
            // 更新当前列上连续的1的个数，(从当前的x轴往上)
            for (int j = 0; j < cols; j++) {
                // 只有连续的1，当前列的矩阵高度才增加
                if (matrix[i][j] == '1')
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

    /**
     * 以每个点为矩阵右下角，逐个遍历所有矩阵，找出包含1最多的矩阵
     *
     * 不过，枚举右下角时，如何找到所有以它为右下角的矩阵，不能通过再枚举左端点完成，时间复杂度太高，我们采用以下策略：
     * 若 (x,y) 为右下角点
     * 我们可以在遍历的过程中计算出 x 行，以该位置结束的连续的1的个数，依次作为矩形的 底边
     * 然后与当前位置行向上，找到同一列上所有点，判断以这些点结束的连续的1的个数，(都已经计算过)
     * 这些值都可以作为 矩形的 顶边，对应的矩形高就是当前行-向上推进到的那一行，
     * 在组成的所有矩形中取最大面积
     *
     * 理论上，我们在向上推进过程中应该保证当前列的点始终为1，不然矩阵会断开，但是我们可以省去这个过滤过程
     * 因为假如 (x-k, j)='0' ，那么在x-k行以这个点结束的连续1个数是0，矩形 顶边 宽度为 0，矩形面积就是 0，
     * 我们统计的是所有矩形最大面积，因此它并不会影响到最终答案
     *
     * @param matrix
     * @return
     */
    public int maximalRectangle2(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0)
            return 0;
        int rows = matrix.length, cols = matrix[0].length;
        int[][] curMatrix = new int[rows][cols];
        int res = 0;
        // 逐行逐个，求以它为右下角
        for (int i = 0; i < rows; i++) {
            // 统计以它结束的连续的1
            for (int j = 0; j < cols; j++) {
                // 当前位置是 ‘1’
                if (matrix[i][j] == '1') {
                    if (j == 0) {
                        curMatrix[i][j] = 1;
                    } else {
                        // 如果它之前也是 ‘1’，那么以它结束的连续1的个数+1
                        curMatrix[i][j] = curMatrix[i][j - 1] + 1;
                    }
                } else {
                    // 它是‘0’，以它结束的连续1的个数为0
                    curMatrix[i][j] = 0;
                }

                // 求以他为右下角顶点的所有矩阵中含1最多的那个
                // 向左不用扩展，我们已经记录了当前点所在行以它结束的连续1的个数
                // 我们需要向上扩展，找到最小的宽
                int minWidth = curMatrix[i][j];
                for (int up = i; up >= 0; up--) {
                    // 找到最小的宽
                    minWidth = Math.min(minWidth, curMatrix[up][j]);
                    // 高度
                    int height = i - up + 1;
                    // 更新结果
                    res = Math.max(res, minWidth * height);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        char[][] arr1 = new char[8][];
        System.out.println(arr1[0] == null);
    }

}
