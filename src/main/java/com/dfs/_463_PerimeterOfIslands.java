package com.dfs;

/**
 * @author wangwei
 * 2021/11/10 9:40
 *
 * 给定一个 row x col 的二维网格地图 grid ，其中：grid[i][j] = 1 表示陆地， grid[i][j] = 0 表示水域。
 *
 * 网格中的格子 水平和垂直 方向相连（对角线方向不相连）。整个网格被水完全包围，但其中恰好有一个岛屿（或者说，一个或多个表示陆地的格子相连组成的岛屿）。
 *
 * 岛屿中没有“湖”（“湖” 指水域在岛屿内部且不和岛屿周围的水相连）。格子是边长为 1 的正方形。网格为长方形，且宽度和高度均不超过 100 。
 * 计算这个岛屿的周长。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：grid = [[0,1,0,0],[1,1,1,0],[0,1,0,0],[1,1,0,0]]
 * 输出：16
 * 解释：它的周长是上面图片中的 16 个黄色的边
 * 示例 2：
 *
 * 输入：grid = [[1]]
 * 输出：4
 * 示例 3：
 *
 * 输入：grid = [[1,0]]
 * 输出：4
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/island-perimeter
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _463_PerimeterOfIslands {

    public int islandPerimeter(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 题目说了只有一个岛屿，所以一次dfs能找到这个岛屿
                if (grid[i][j] == 1) {
                    return dfs(grid, i, j);
                }
            }
        }
        return 0;
    }

    /**
     * 一次dfs找到一个连通分量
     * 【对于一个陆地格子的每条边，它被算作岛屿的周长当且仅当【这条边为网格的边界或者相邻的另一个格子为水域。】
     * 计算岛屿周长，统计所有边，每个边长是1，边 分为两类：1 是 网格边界上的，2 是内部和海水相接的了，至于四周是陆地的点，不用考虑，因为它不涉及到岛屿外围
     * @param grid
     * @param i
     * @param j
     * @return
     */
    private int dfs(int[][] grid, int i, int j) {
        // 数组越界，每次越界，就代表访问了(上一个点)一个边界上的点，那么 边界边 + 1
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
            return 1;
        }
        // 当前点是个海水，每次遇到海水，就代表访问了(上一个点)一个连接海水的点，那么 海水边 + 1
        if (grid[i][j] == 0) {
            return 1;
        }
        // 对于已访问过的点，不再访问，不再统计
        if (grid[i][j] == 2) {
            return 0;
        }
        // 标记当前点已访问，注意标记为2，不是0
        grid[i][j] = 2;
        // 统计所有边数，注意这里不算自己，我们是通过退出条件来判断上一个点的情况，是靠近边界，还是邻接海水
        return  dfs(grid, i + 1, j) +
                dfs(grid, i - 1, j) +
                dfs(grid, i, j + 1) +
                dfs(grid, i, j - 1);
    }
}
