package com.array;

import java.util.Arrays;
import java.util.Stack;

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
     * 借助84题的思想。相当于平移x轴上下，求出每一列的连续的1的高度，作为矩形高度，传给84题的solution
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
            res = Math.max(res, getLargestArea(curLayerHeights));
        }
        return res;
    }

    /**
     * 给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
     * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
     *
     * @param heights
     * @return
     */
    private int getLargestArea(int[] heights) {
        if (heights == null || heights.length == 0) return 0;
        if (heights.length == 1) return heights[0];
        int len = heights.length;
        int[] newHeights = new int[len + 2];
        System.arraycopy(heights, 0, newHeights, 1, len);
        len += 2;
        // 增加前后哨兵
        newHeights[0] = 0;
        newHeights[len - 1] = 0;

        heights = newHeights;

        Stack<Integer> stack = new Stack<>();
        stack.push(0);

        int res = 0;

        for (int i = 1; i < len; i++) {
            // 打破递增，向前计算
            while (heights[i] < heights[stack.peek()]) {
                int curHeight = heights[stack.pop()];
                int curWidth = i - stack.peek() - 1;
                res = Math.max(res, curHeight * curWidth);
            }
            // 继续递增，无法计算
            stack.push(i);
        }
        return res;
    }

    /**
     * 以每个点为矩阵右下角，逐个遍历所有矩阵，找出包含1最多的矩阵
     * 我们可以在遍历的过程中计算出 行方向 上，以该位置结束的连续的1的个数
     * 再从当前位置向上，找到该列上所有点，以他们结束的连续的1的个数都是确定的(因为我们按行遍历)
     * 所以找到他们中的最小值就是矩阵的宽，至于高就是当前行-向上推进到的那一行
     *
     * @param matrix
     * @return
     */
    public int maximalRectangl2(char[][] matrix) {
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
                    if (j == 0)
                        curMatrix[i][j] = 1;
                    else
                        // 如果它之前也是 ‘1’，那么以它结束的连续1的个数+1
                        curMatrix[i][j] = curMatrix[i][j - 1] + 1;
                } else
                    // 它是‘0’，以它结束的连续1的个数为0
                    curMatrix[i][j] = 0;

                // 求以他为右下角顶点的所有矩阵中含1最多的那个
                // 向左不用扩展，我们已经记录了以它结束的连续1的个数
                // 我们需要向上扩展，找到最小的宽
                int minWeight = curMatrix[i][j];
                for (int up = i; up >= 0; up--) {
                    // 找到最小的宽
                    minWeight = Math.min(minWeight, curMatrix[up][j]);
                    // 高度
                    int height = i - up + 1;
                    // 更新结果
                    res = Math.max(res, minWeight * height);
                }
            }
        }
        return res;
    }

    /**
     * 思路二:动态规划
     * <p>
     * 用height_j记录第i行为底,第j列有效高度是多少.
     * <p>
     * 用left_j记录第i行为底, 第j列左边第一个小于height_j[j]的位置，左边界
     * <p>
     * 用right_j记录第i行为底, 第j列右边第一个小于height_j[j]的位置，有边界
     * <p>
     * 作者：powcai
     * 链接：https://leetcode-cn.com/problems/maximal-rectangle/solution/yu-zhao-zui-da-ju-xing-na-ti-yi-yang-by-powcai/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param matrix
     */

    public int maximalRectangle3(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0)
            return 0;
        int rows = matrix.length, cols = matrix[0].length;
        // 注意这里数组大小全是cols，因为j代表的是列
        // 第 i 行 为底时，第j列有效高度，随着 底 的向下移动而增加
        int[] height_j = new int[cols];
        // 第 i 行 为底时，第j列有效高度，左边第一个小于其高度的下标位置
        int[] left_j = new int[cols];
        // 刚开始左边界 全在 最左面
        Arrays.fill(left_j, -1);
        // 第 i 行 为底时，第j列有效高度，左边第一个小于其高度的下标位置，
        int[] right_j = new int[cols];
        // 刚开始右边界 全在 最右面
        Arrays.fill(right_j, cols);

        int res=  0;
        // 底 从 0 到最后一行
        for (int i = 0; i < rows; i++){
            //
            int lastLeftBound = -1;
            int lastRightBound = cols;
            // 更新每一列的有效高度
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == '1')
                    height_j[j] += 1;
                else
                    height_j[j] = 0;
            }
            // 更新 每一列有效高度，对应的左边界
            for (int j = 0; j < cols; j++){
                // 当前位置是 ‘1’
                if (matrix[i][j] == '1')
                    // 左边界更新为更靠近它，left_j[j]保存了·底是上一层的时候，当前列有效高的左边界
                    // lastLeftBound代表，底 为当前行时 这个列左变出现了一个 ‘0’，那么就要重新确定新高度的左边界
                    left_j[j] = Math.max(left_j[j], lastLeftBound);
                else {
                    // 当前位置是 0 ，那么比当前矩形高度还低的不存在，所以右边界是cols
                    left_j[j] = -1;
                    // 当前位置就是下一个矩形的左边界
                    lastLeftBound = j;
                }
            }

            // 更新 每一列有效高度，对应的右边界
            // 注意更新有边界需要从右往左
            for (int j = cols - 1; j >= 0; j--){
                // 当前位置是 ‘1’
                if (matrix[i][j] == '1')
                    // 右边界更新为更靠近它，right_j[j]保存了·底是上一层的时候，当前列有效高的右边界
                    // lastRightBound代表，底 为当前行时 这个列右边出现了一个 ‘0’，那么就要重新确定新高度的右边界
                    right_j[j] = Math.min(right_j[j], lastRightBound);
                else {
                    // 当前位置是 0 ，那么比当前矩形高度还低的不存在，所以右边界是cols
                    right_j[j] = cols;
                    // 当前位置就是下一个矩形的右边界
                    lastRightBound = j;
                }
            }
            // 找到了每一列有效高的左右边界后，我们就能得到以该列高为高的矩形的面积
            for (int j = 0; j < cols; j++) {
                res = Math.max(res, height_j[j] * (right_j[j] - left_j[j] - 1));
            }
        }
        return res;
    }

    public static void main(String[] args) {
        char[][] arr1 = new char[8][];
        System.out.println(arr1[0] == null);
    }

}
