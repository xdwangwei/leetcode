package com.dfs;

/**
 * @author wangwei
 * 2021/11/10 9:33
 *
 * 给你一个大小为 m x n 的二进制矩阵 grid 。
 *
 * 岛屿是由一些相邻的1(代表土地) 构成的组合，这里的「相邻」要求两个 1 必须在 水平或者竖直的四个方向上 相邻。你可以假设 grid 的四个边缘都被 0（代表水）包围着。
 *
 * 岛屿的面积是岛上值为 1 的单元格的数目。
 *
 * 计算并返回 grid 中最大的岛屿面积。如果没有岛屿，则返回面积为 0 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/max-area-of-island
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _695_MaximumAreaOfIslands {

    public int maxAreaOfIsland(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int res = 0, m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 找到面积最大的连同分量
                res = Math.max(res, dfs(grid, i, j));
            }
        }
        return res;
    }

    /**
     * 每次dfs得到一个岛屿的面积(连通分量)，并将这个岛屿淹没
     * @param grid
     * @param i
     * @param j
     * @return
     */
    private int dfs(int[][] grid, int i, int j) {
        // 数组越界
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
            return 0;
        }
        // 当前点已经是海水
        if (grid[i][j] == 0) {
            return 0;
        }
        // 淹没当前点
        grid[i][j] = 0;
        // 统计连通分量面积，并淹没
        return 1 +
                dfs(grid, i + 1, j) +
                dfs(grid, i - 1, j) +
                dfs(grid, i, j + 1) +
                dfs(grid, i, j - 1);
    }
}
