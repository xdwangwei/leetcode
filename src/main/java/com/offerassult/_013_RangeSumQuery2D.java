package com.offerassult;

/**
 * @author wangwei
 *
 * 剑指 Offer II 013. 二维子矩阵的和
 * 给定一个二维矩阵 matrix，以下类型的多个请求：
 *
 * 计算其子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2) 。
 * 实现 NumMatrix 类：
 *
 * NumMatrix(int[][] matrix) 给定整数矩阵 matrix 进行初始化
 * int sumRegion(int row1, int col1, int row2, int col2) 返回左上角 (row1, col1) 、右下角 (row2, col2) 的子矩阵的元素总和。
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入:
 * ["NumMatrix","sumRegion","sumRegion","sumRegion"]
 * [[[[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,0,1,7],[1,0,3,0,5]]],[2,1,4,3],[1,1,2,2],[1,2,2,4]]
 * 输出:
 * [null, 8, 11, 12]
 *
 * 解释:
 * NumMatrix numMatrix = new NumMatrix([[3,0,1,4,2],[5,6,3,2,1],[1,2,0,1,5],[4,1,0,1,7],[1,0,3,0,5]]]);
 * numMatrix.sumRegion(2, 1, 4, 3); // return 8 (红色矩形框的元素总和)
 * numMatrix.sumRegion(1, 1, 2, 2); // return 11 (绿色矩形框的元素总和)
 * numMatrix.sumRegion(1, 2, 2, 4); // return 12 (蓝色矩形框的元素总和)
 *
 *
 * 提示：
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 200
 * -105 <= matrix[i][j] <= 105
 * 0 <= row1 <= row2 < m
 * 0 <= col1 <= col2 < n
 * 最多调用 104 次 sumRegion 方法
 *
 *
 * 注意：本题与主站 304 题相同： https://leetcode-cn.com/problems/range-sum-query-2d-immutable/
 *
 */
public class _013_RangeSumQuery2D {

    /**
     * 二维前缀和
     * preSum[i][j]表示 matrix 左上角为 (0,0) 右下角为 (i-1, j-1)的矩形区域元素和
     * 相当于，左上角都固定为原点，考虑的是右下角
     */
    class NumMatrix {


        // preSum[i][j]表示 matrix 左上角为 (0,0) 右下角为 (i-1, j-1)的矩形区域元素和
        private int[][] preSum;

        public NumMatrix(int[][] matrix) {
            int m = matrix.length, n = matrix[0].length;
            // 注意前缀和数组和matrix数组下表差1
            preSum = new int[m + 1][n + 1];
            // 初始化前缀和数组
            for (int i = 1; i <= m; ++i) {
                for (int j = 1; j <= n; ++j) {
                    // 画个图自己研究研究
                    preSum[i][j] = preSum[i - 1][j] + preSum[i][j - 1] - preSum[i - 1][j - 1] + matrix[i - 1][j - 1];
                }
            }
        }

        /**
         * 返回左上角为 (r1, c1) 右下角为 (r2, c2) 的矩形区域元素和
         * == 右下角为 (r2, c2) - 右下角为 (r2, c1-1) - 右下角为 (r1-1, c2) + 右下角为 (r1-1, c1-1) 的元素和
         * 再根据matrix下标和preSum下标差1得到递推公式
         * @param row1
         * @param col1
         * @param row2
         * @param col2
         * @return
         */
        public int sumRegion(int row1, int col1, int row2, int col2) {
            return preSum[row2 + 1][col2 + 1] - preSum[row2 + 1][col1] - preSum[row1][col2 + 1] + preSum[row1][col1];
        }
    }
}
