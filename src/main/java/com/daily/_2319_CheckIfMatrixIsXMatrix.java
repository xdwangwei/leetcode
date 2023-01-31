package com.daily;

/**
 * @author wangwei
 * @date 2023/1/31 15:03
 * @description: _2319_CheckIfMatrixIsXMatrix
 *
 * 2319. 判断矩阵是否是一个 X 矩阵
 * 如果一个正方形矩阵满足下述 全部 条件，则称之为一个 X 矩阵 ：
 *
 * 矩阵对角线上的所有元素都 不是 0
 * 矩阵中所有其他元素都是 0
 * 给你一个大小为 n x n 的二维整数数组 grid ，表示一个正方形矩阵。如果 grid 是一个 X 矩阵 ，返回 true ；否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：grid = [[2,0,0,1],[0,3,1,0],[0,5,2,0],[4,0,0,2]]
 * 输出：true
 * 解释：矩阵如上图所示。
 * X 矩阵应该满足：绿色元素（对角线上）都不是 0 ，红色元素都是 0 。
 * 因此，grid 是一个 X 矩阵。
 * 示例 2：
 *
 *
 * 输入：grid = [[5,7,0],[0,3,1],[0,5,0]]
 * 输出：false
 * 解释：矩阵如上图所示。
 * X 矩阵应该满足：绿色元素（对角线上）都不是 0 ，红色元素都是 0 。
 * 因此，grid 不是一个 X 矩阵。
 *
 *
 * 提示：
 *
 * n == grid.length == grid[i].length
 * 3 <= n <= 100
 * 0 <= grid[i][j] <= 105
 * 通过次数10,575提交次数14,432
 */
public class _2319_CheckIfMatrixIsXMatrix {

    /**
     * 方法一：模拟遍历 + 数学
     * 思路与算法
     *
     * 首先题目给出 X 矩阵的定义：矩阵对角线上的所有元素都不是 0。矩阵中的所有其他元素都是 0。
     *
     * 对角线上元素坐标满足：i=j 或者 i+j+1=n，则说明该点在矩阵
     *
     * 那么我们遍历矩阵中的每一个位置来判断是否满足 X 的定义即可：
     *      若 i == j 或 i + j == n - 1，则 grid[i][j] != 0
     *                                  否则 grid[i][j] == 0
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/check-if-matrix-is-x-matrix/solution/pan-duan-ju-zhen-shi-fou-shi-yi-ge-x-ju-aloq7/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param grid
     * @return
     */
    public boolean checkXMatrix(int[][] grid) {
        int n = grid.length;
        // 逐个遍历
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // 对角线元素
                if (i == j || i + j == n - 1) {
                    // 不能为0
                    if (grid[i][j] == 0) {
                        return false;
                    }
                // 其他元素必须为0
                } else if (grid[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
