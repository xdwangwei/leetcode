package com.dfs;

/**
 * @author wangwei
 * 2021/11/10 9:00
 *
 * 给你一个由'1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 *
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 *
 * 此外，你可以假设该网格的四条边均被水包围。
 *
 *
 *
 * 示例 1：
 *
 * 输入：grid = [
 *   ["1","1","1","1","0"],
 *   ["1","1","0","1","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","0","0","0"]
 * ]
 * 输出：1
 * 示例 2：
 *
 * 输入：grid = [
 *   ["1","1","0","0","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","1","0","0"],
 *   ["0","0","0","1","1"]
 * ]
 * 输出：3
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-islands
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _200_NumberOfIslands {

    /**
     *
     * @param grid
     * @return
     */
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int res = 0, m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    // 淹没当前点所处的整片岛屿
                    dfs(grid, i, j);
                    // 岛屿数量加1
                    res++;
                }
            }

        }
        return res;
    }

    /**
     * 每次dfs，淹没一个岛屿（连通分量），将其全部标记为 ‘0’
     * @param grid
     * @param i
     * @param j
     */
    private void dfs(char[][] grid, int i, int j) {
        // 超过范围
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
            return;
        }
        // 当前点已是 '0'
        if (grid[i][j] == '0') {
            return;
        }
        // 比较当前点
        grid[i][j] = '0';
        // dfs它的四个邻接点
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
}
