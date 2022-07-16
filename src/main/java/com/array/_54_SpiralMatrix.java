package com.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/3/31 13:35
 *
 * 给你一个 m 行 n 列的矩阵matrix ，请按照 顺时针螺旋顺序 ，返回矩阵中的所有元素。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * 输出：[1,2,3,6,9,8,7,4,5]
 * 示例 2：
 *
 *
 * 输入：matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
 * 输出：[1,2,3,4,8,12,11,10,9,5,6,7]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/spiral-matrix
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _54_SpiralMatrix {

    private List<Integer> res;

    public List<Integer> spiralOrder(int[][] matrix) {
        res = new ArrayList<>();
        // 左上角第一个元素开始第一圈
        dfs(matrix, 0, 0);
        return res;
    }

    /**
     * 方法一：看成一圈套一圈，每次传入当前圈开始的左上角位置
     * 当前圈 左上角是 row,col 那么 最后右下角就是 m - row - 1，n - col - 1, 左右对称呗
     * 避免重复添加
     * @param matrix
     * @param row
     * @param col
     */
    private void dfs(int[][] matrix, int row, int col) {
        int m = matrix.length, n = matrix[0].length;
        // 递归出口，数组索引越界 或者 元素已添加完，避免重复添加
        if (row == m || col == n || res.size() == m * n) {
            return;
        }
        // 当前圈 左上角是 row,col 那么 最后右下角就是 m - row - 1，n - col - 1
        int rowBottom = m - row, colRight = n - col;
        // 添加上行
        for (int j = col; j < colRight; ++j) {
            res.add(matrix[row][j]);
        }
        // 添加右列
        for (int i = row + 1; i < rowBottom; ++i) {
            res.add(matrix[i][colRight - 1]);
        }
        // 添加下行，避开上下行重复情况
        if (rowBottom - 1 > row) {
            for (int j = colRight - 1 - 1; j >= col; --j) {
                res.add(matrix[rowBottom - 1][j]);
            }
        }
        // 添加左列，避开左右列重复情况
        if (colRight - 1 > col) {
            for (int i = rowBottom - 1 - 1; i > row; --i) {
                res.add(matrix[i][col]);
            }
        }
        // 内圈开始位置
        dfs(matrix, row + 1, col + 1);
    }


    /**
     * 迭代写法
     * @param matrix
     * @return
     */
    public List<Integer> spiralOrder3(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        int m = matrix.length, n = matrix[0].length;
        // 四个边界
        int top = 0, bottom = m - 1;
        int left = 0, right = n - 1;
        // 退出条件：所有元素已加入res
        while (res.size() != m * n) {
            // 避免越界和重复
            // 上边，从左往右
            if (top <= bottom) {
                for (int i = left; i <= right; i++) {
                    res.add(matrix[top][i]);
                }
                // 上边界下移
                top++;
            }
            // 右边，从上倒下
            if (left <= right) {
                for (int i = top; i <= bottom; i++) {
                    res.add(matrix[i][right]);
                }
                // 右边界左移
                right--;
            }
            // 下边，从右往左
            if (top <= bottom) {
                for (int i = right; i >= left; i--) {
                    res.add(matrix[bottom][i]);
                }
                // 下边界上移
                bottom--;
            }
            // 左边，从下往上
            if (left <= right) {
                for (int i = bottom; i >= top; i--) {
                    res.add(matrix[i][left]);
                }
                // 左边界右移
                left++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        _54_SpiralMatrix obj = new _54_SpiralMatrix();
        // List<Integer> res = obj.spiralOrder(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        List<Integer> res = obj.spiralOrder(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}});
        System.out.println(res);
    }
}
