package com.dfs;

/**
 * @author wangwei
 * 2021/11/10 15:30
 *
 * 有一个二维矩阵 grid，每个位置要么是陆地（记号为0 ）要么是水域（记号为1 ）。
 *
 * 我们从一块陆地出发，每次可以往上下左右4 个方向相邻区域走，能走到的所有陆地区域，我们将其称为一座「岛屿」。
 *
 * 如果一座岛屿完全由水域包围，即陆地边缘上下左右所有相邻区域都是水域，那么我们将其称为 「封闭岛屿」。
 *
 * 请返回封闭岛屿的数目。
 *
 *
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-closed-islands
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1254_NumberOfClosedIslands {

    /**
     * 两次 dfs
     * 第一次，将所有边界上的陆地所处的岛屿全部淹没
     * 第二次，去统计所有剩下的岛屿数目
     *
     * 注意这个题目 0 代表陆地， 1 代表海水
     * @param grid
     * @return
     */
    public int closedIsland(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int m = grid.length, n = grid[0].length;
        // 淹没左右边界上的陆地所处的岛屿
        for (int i = 0; i < m; i++) {
            dfs(grid, i, 0);
            dfs(grid, m - 1, 0);
        }
        // 淹没上下边界上的陆地所处的岛屿
        for (int j = 0; j < n; j++) {
            dfs(grid, 0, j);
            dfs(grid, n - 1, 0);
        }
        // 统计剩下的岛屿
        int res = 0;
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (grid[i][j] == 0) {
                    dfs(grid, i, j);
                    res++;
                }
            }
        }
        return res;
    }

    /**
     * 一次dfs，将grid[i][j]所在岛屿淹没
     * @param grid
     * @param i
     * @param j
     */
    private void dfs(int[][] grid, int i, int j) {
        // 数组越界
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
            return;
        }
        // 当前位置已经是海水了
        if (grid[i][j] == 1) {
            return;
        }
        // 淹没当前位置
        grid[i][j] = 1;
        // 淹没四周
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
}
