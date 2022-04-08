package com.array;

/**
 * @author wangwei
 * @date 2022/3/31 14:00
 *
 * 给你一个正整数n ，生成一个包含 1 到n2所有元素，且元素按顺时针顺序螺旋排列的n x n 正方形矩阵 matrix 。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：n = 3
 * 输出：[[1,2,3],[8,9,4],[7,6,5]]
 * 示例 2：
 *
 * 输入：n = 1
 * 输出：[[1]]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/spiral-matrix-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _59_SpriralMatrix2 {

    /**
     * 和第 54_SpiralMatrix 一样，一圈一圈完成即可。只是这一次是个方阵
     * @param n
     * @return
     */
    public int[][] generateMatrix(int n) {
        int[][] matrix = new int[n][n];
        // 第一个元素是1， 最后一个元素是 n * n
        dfs(matrix, 0, 0, 1);
        return matrix;
    }

    /**
     * 方法一：看成一圈套一圈，每次传入当前圈开始的左上角位置
     * 当前圈 左上角是 row,col 那么 最后右下角就是 m - row - 1，n - col - 1, 左右对称呗
     * 避免重复添加
     * @param matrix
     * @param row
     * @param col
     * @param num 当前圈 开始位置填入数字
     */
    private void dfs(int[][] matrix, int row, int col, int num) {
        int n = matrix.length;
        // 递归出口，数组索引越界 或者 元素已添加完(num是当前圈开始元素，所以这里要用num+1判断，不然会漏掉中心元素赋值)，避免重复添加
        if (row == n || col == n || num + 1 == n * n) {
            return;
        }
        // 当前圈 左上角是 row,col 那么 最后右下角就是 m - row - 1，n - col - 1
        int rowBottom = n - row, colRight = n - col;
        // 添加上行
        for (int j = col; j < colRight; ++j) {
            matrix[row][j] = num++;
        }
        // 添加右列
        for (int i = row + 1; i < rowBottom; ++i) {
            matrix[i][colRight - 1] = num++;
        }
        // 添加下行，避开上下行重复情况
        if (rowBottom - 1 > row) {
            for (int j = colRight - 1 - 1; j >= col; --j) {
                matrix[rowBottom - 1][j] = num++;
            }
        }
        // 添加左列，避开左右列重复情况
        if (colRight - 1 > col) {
            for (int i = rowBottom - 1 - 1; i > row; --i) {
                matrix[i][col] = num++;
            }
        }
        // 内圈开始位置
        dfs(matrix, row + 1, col + 1, num);
    }

    /**
     * 迭代写法
     * @param n
     * @return
     */
    public int[][] generateMatrix2(int n) {
        int[][] matrix = new int[n][n];
        // 四个边界
        int top = 0, bottom = n - 1;
        int left = 0, right = n - 1;
        // 第一个元素是1， 最后一个元素是 n * n
        int num = 1;
        // 退出条件：所有元素已加入res
        while (num <= n * n) {
            // 避免越界和重复
            // 上边，从左往右
            if (top <= bottom) {
                for (int i = left; i <= right; i++) {
                    matrix[top][i] = num++;
                }
                // 上边界下移
                top++;
            }
            // 右边，从上倒下
            if (left <= right) {
                for (int i = top; i <= bottom; i++) {
                    matrix[i][right] = num++;
                }
                // 右边界左移
                right--;
            }
            // 下边，从右往左
            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    matrix[bottom][i] = num++;
                }
                // 下边界上移
                bottom--;
            }
            // 左边，从下往上
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    matrix[i][left] = num++;
                }
                // 左边界右移
                left++;
            }
        }
        return matrix;
    }

}
