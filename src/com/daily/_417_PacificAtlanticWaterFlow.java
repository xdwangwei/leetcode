package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/4/27 9:53
 * @description: _417_PacificAtlanticWaterFlow
 *
 * 417. 太平洋大西洋水流问题
 * 有一个 m × n 的矩形岛屿，与 太平洋 和 大西洋 相邻。 “太平洋” 处于大陆的左边界和上边界，而 “大西洋” 处于大陆的右边界和下边界。
 *
 * 这个岛被分割成一个由若干方形单元格组成的网格。给定一个 m x n 的整数矩阵 heights ， heights[r][c] 表示坐标 (r, c) 上单元格 高于海平面的高度 。
 *
 * 岛上雨水较多，如果相邻单元格的高度 小于或等于 当前单元格的高度，雨水可以直接向北、南、东、西流向相邻单元格。水可以从海洋附近的任何单元格流入海洋。
 *
 * 返回 网格坐标 result 的 2D列表 ，其中 result[i] = [ri, ci] 表示雨水可以从单元格 (ri, ci) 流向 太平洋和大西洋 。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入: heights = [[1,2,2,3,5],[3,2,3,4,4],[2,4,5,3,1],[6,7,1,4,5],[5,1,1,2,4]]
 * 输出: [[0,4],[1,3],[1,4],[2,2],[3,0],[3,1],[4,0]]
 * 示例 2：
 *
 * 输入: heights = [[2,1],[1,2]]
 * 输出: [[0,0],[0,1],[1,0],[1,1]]
 *
 *
 * 提示：
 *
 * m == heights.length
 * n == heights[r].length
 * 1 <= m, n <= 200
 * 0 <= heights[r][c] <= 105
 */
public class _417_PacificAtlanticWaterFlow {

    /**
     * 整体思路：
     *
     * 我们开局能够知道的是，左边和上边的点，一定能到达太平洋；右边和下边的点一定能到达大西洋
     * 那么想要知道中间部分的点是否能够到达太平洋和大西洋
     * 就需要判断边界上的点能够到达中间，每个点能够到达它的邻接点的条件是它的邻接点的height值比自己大(我们是反向推进)
     *
     * 所以，从矩阵的左边界和上边界开始反向搜索即可找到雨水流向太平洋的单元格，从矩阵的右边界和下边界开始反向搜索即可找到雨水流向大西洋的单元格。
     * 搜索过程中需要记录每个单元格是否可以从太平洋反向到达以及是否可以从大西洋反向到达。
     * 反向搜索结束之后，遍历每个网格，如果一个网格既可以从太平洋反向到达也可以从大西洋反向到达，则该网格满足太平洋和大西洋都可以到达，将该网格添加到答案中。
     *
     * 至于，搜索过程可以使用dfs，也可以使用bfs
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/pacific-atlantic-water-flow/solution/tai-ping-yang-da-xi-yang-shui-liu-wen-ti-sjk3/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    // 方向
    static final int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    int[][] heights;
    int m, n;

    /**
     * Bfs
     * 如果采用dfs，只需要把for循环中的bfs全换为dfs即可
     * @param heights
     * @return
     */
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        this.heights = heights;
        this.m = heights.length;
        this.n = heights[0].length;
        // 在搜索过程中完成当前点是否能流到 记录
        boolean[][] pacific = new boolean[m][n];
        boolean[][] atlantic = new boolean[m][n];
        // 第一列的点能到达太平洋，以他们为起点进行bfs，搜索出能够到达太平洋的点，更新的是 pacific数组
        // 最后一列的点能到达太平洋，以他们为起点进行bfs，搜索出能够到达大西洋的点，更新的是 atlantic数组
        for (int i = 0; i < m; i++) {
            bfs(i, 0, pacific);
            bfs(i, n - 1, atlantic);
        }
        // 第一行的点能到达太平洋，以他们为起点进行bfs，搜索出能够到达太平洋的点，更新的是 pacific数组
        // 最后一行的点能到达太平洋，以他们为起点进行bfs，搜索出能够到达大西洋的点，更新的是 atlantic数组
        for (int j = 0; j < n; j++) {
            bfs(0, j, pacific);
            bfs(m - 1, j, atlantic);
        }
        // 如果某个点既能到达太平洋又能到达大西洋，就加入结果
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pacific[i][j] && atlantic[i][j]) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }
        return result;
    }

    /**
     * 从点r,c 开始，进行bfs，更新canFlow数组
     * 也就是当前位置能够流到的所有位置
     * @param row
     * @param col
     * @param canFlow
     */
    public void bfs(int row, int col, boolean[][] canFlow) {
        // 已更新过
        if (canFlow[row][col]) {
            return;
        }
        // 更新
        canFlow[row][col] = true;
        // 借用队列结构
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{row, col});
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            // 判断所有邻接点
            for (int[] dir : dirs) {
                // 下标合法
                int newRow = cell[0] + dir[0], newCol = cell[1] + dir[1];
                // 邻接点的height要大于等于自身，并且邻接点未被访问，才进行下一步扩展
                if (newRow >= 0 && newRow < m && newCol >= 0 && newCol < n && heights[newRow][newCol] >= heights[cell[0]][cell[1]] && !canFlow[newRow][newCol]) {
                    canFlow[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
    }

    /**
     * 与bfs功能相同，只是采取dfs的方式去更新canFlow数组
     * @param row
     * @param col
     * @param canFlow
     */
    public void dfs(int row, int col, boolean[][] canFlow) {
        // 已访问过
        if (canFlow[row][col]) {
            return;
        }
        // 标记
        canFlow[row][col] = true;
        for (int[] dir : dirs) {
            int newRow = row + dir[0], newCol = col + dir[1];
            // 下表合法，能够抵达，未访问过，再进行dfs
            if (newRow >= 0 && newRow < m && newCol >= 0 && newCol < n && heights[newRow][newCol] >= heights[row][col] && !canFlow[newRow][newCol]) {
                dfs(newRow, newCol, canFlow);
            }
        }
    }
}
