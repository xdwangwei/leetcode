package com.dfs;

/**
 * @author wangwei
 * 2021/11/10 15:43
 * <p>
 * 给你两个m x n的二进制矩阵grid1 和grid2，它们只包含0（表示水域）和 1（表示陆地）。一个 岛屿是由 四个方向（水平或者竖直）上相邻的1组成的区域。任何矩阵以外的区域都视为水域。
 * <p>
 * 如果 grid2的一个岛屿，被 grid1的一个岛屿完全 包含，也就是说 grid2中该岛屿的每一个格子都被 grid1中[同一个]岛屿完全包含，那么我们称 grid2中的这个岛屿为 子岛屿。
 * <p>
 * 请你返回 grid2中 子岛屿的 数目。
 * <p>
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/count-sub-islands
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1905_CountSubIslands {

    /**
     * 两次 dfs
     *      grid2中该岛屿的每一个格子都被 grid1中[同一个]岛屿完全包含
     *          当岛屿B中所有陆地在岛屿A中也是陆地的时候，岛屿B是岛屿A的子岛。
     *          所以如果 grid2[i][j] == 1, 那么 grid1[i][j] 也应该为1，
     *          【反向思维】
     *          反过来说，如果岛屿B中存在一片陆地，在岛屿A的对应位置是海水，那么岛屿B就不是岛屿A的子岛。
     * 第一次dfs，先把grid2中所有 grid2[i][j] = 1 但是  grid1[i][j] = 0 的岛屿淹没了，肯定不满足
     * 第二次dfs，统计grid2中剩下的岛屿数目
     * @param grid1
     * @param grid2
     * @return
     */
    public int countSubIslands(int[][] grid1, int[][] grid2) {

        if (grid1 == null || grid1.length == 0 || grid2 == null || grid2.length == 0) {
            return 0;
        }
        int m = grid1.length, n = grid1[0].length;
        // 先把grid2中所有 grid2[i][j] = 1 但是  grid1[i][j] = 0 的岛屿淹没了
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid2[i][j] == 1 && grid1[i][j] == 0) {
                    dfs(grid2, i, j);
                }
            }
        }
        // 统计grid2剩下的岛屿
        int res = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid2[i][j] == 1) {
                    dfs(grid2, i, j);
                    res++;
                }
            }
        }
        return res;
    }

    /**
     * 一次dfs，将grid[i][j]所在岛屿淹没
     *
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
        if (grid[i][j] == 0) {
            return;
        }
        // 淹没当前位置
        grid[i][j] = 0;
        // 淹没四周
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
}
