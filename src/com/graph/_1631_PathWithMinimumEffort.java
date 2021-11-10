package com.graph;

import java.util.*;

/**
 * @author wangwei
 * @version 1.0.0
 * @date 2021/9/23 18:59
 * @description: 你准备参加一场远足活动。给你一个二维 rows x columns 的地图 heights ，其中 heights[row][col] 表示格子 (row, col) 的高度。一开始你在最左上角的格子 (0, 0) ，且你希望去最右下角的格子 (rows-1, columns-1) （注意下标从 0 开始编号）。你每次可以往 上，下，左，右 四个方向之一移动，你想要找到耗费 体力 最小的一条路径。
 *
 * 一条路径耗费的 体力值 是路径上相邻格子之间 高度差绝对值 的 最大值 决定的。
 *
 * 请你返回从左上角走到右下角的最小 体力消耗值 。
 *
 *  
 *
 * 示例 1：
 *
 *
 *
 * 输入：heights = [[1,2,2],[3,8,2],[5,3,5]]
 * 输出：2
 * 解释：路径 [1,3,5,3,5] 连续格子的差值绝对值最大为 2 。
 * 这条路径比路径 [1,2,2,2,5] 更优，因为另一条路径差值最大值为 3 。
 * 示例 2：
 *
 *
 *
 * 输入：heights = [[1,2,3],[3,8,4],[5,3,5]]
 * 输出：1
 * 解释：路径 [1,2,3,4,5] 的相邻格子差值绝对值最大为 1 ，比路径 [1,3,5,3,5] 更优。
 * 示例 3：
 *
 *
 * 输入：heights = [[1,2,1,1,1],[1,2,1,2,1],[1,2,1,2,1],[1,2,1,2,1],[1,1,1,2,1]]
 * 输出：0
 * 解释：上图所示路径不需要消耗任何体力。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/path-with-minimum-effort
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1631_PathWithMinimumEffort {

    /**
     * 记录Dijkstra算法求最短路径过程中，遇到的点的转态
     */
    class PointState {
        // 坐标
        int x;
        int y;
        // 当前路径，起点到它的距离
        int effortFromStart;

        public PointState(final int x, final int y, final int effortFromStart) {
            this.x = x;
            this.y = y;
            this.effortFromStart = effortFromStart;
        }
    }


    /**
     * Dijkstra
     * 如果你把二维数组中每个(x, y)坐标看做一个节点，它的上下左右坐标就是相邻节点，它对应的值和相邻坐标对应的值之差的绝对值就是题目说的「体力消耗」，你就可以理解为边的权重。
     *
     * 这样一想，是不是就在让你以左上角坐标为起点，以右下角坐标为终点，计算起点到终点的最短路径？
     * Dijkstra 算法是不是可以做到？
     *
     * 输入起点 start 和终点 end，计算起点到终点的最短距离
     * int dijkstra(int start, int end, List<Integer>[] graph)
     * 只不过，这道题中评判一条路径是长还是短的标准不再是路径经过的权重总和，而是路径经过的权重最大值。
     *
     * 明白这一点，再想一下使用 Dijkstra 算法的【前提】，[加权有向图，没有负权重边，求最短路径]，OK，可以使用，咱们来套框架。
     * @param heights
     * @return
     */
    public int minimumEffortPath(int[][] heights) {
        int m = heights.length, n = heights[0].length;
        // 从起点0,0 到 任意点 的最短路径 初始化为最大值
        int[][] minEffort = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(minEffort[i], Integer.MAX_VALUE);
        }
        // 起点到起点
        minEffort[0][0] = 0;
        // 保存所有点(dijkstra算法，未确定的点的集合)，按照到起点的最短距离排序保存，每次选出一个最近的，进行 "放缩"
        PriorityQueue<PointState> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o.effortFromStart));
        // 加入起点
        queue.offer(new PointState(0, 0, 0));
        while (!queue.isEmpty()) {
            PointState curNode = queue.poll();
            int curX = curNode.x;
            int curY = curNode.y;
            int effortFromStart = curNode.effortFromStart;
            // 当前点就是终点
            if (curX == m - 1 && curY == n - 1) {
                return effortFromStart;
            }
            // 之前某个路径到当前点，距离更小，已更新过minEffort[curX][curY]。当前路径作废
            if (effortFromStart > minEffort[curX][curY]) {
                continue;
            }
            // 遍历它的邻接点
            for (int[] neighbor : adj(heights, curX, curY)) {
                int nx = neighbor[0];
                int ny = neighbor[1];
                // 当前点到它的邻接点的权重
                int effortToNeighbor = Math.abs(heights[curX][curY] - heights[nx][ny]);
                // 如果以当前点为中间点，那么起点到当前点到邻接点的权重就是 max(起点到当前点的权重，当前点到邻接点的权重)，而不是二者之和
                int effortToNeighborFromStart = Math.max(effortFromStart, effortToNeighbor);
                // 以当前点为中间点，得到的权重更小
                if (effortToNeighborFromStart < minEffort[nx][ny]) {
                    // 那么更新起点到邻接点的最短距离
                    minEffort[nx][ny] = effortToNeighborFromStart;
                    // 把这个邻接点入队
                    queue.add(new PointState(nx, ny, effortToNeighborFromStart));
                }
            }
        }
        // 正常情况，不会走到这里
        return -1;
    }

    /**
     * 得到 点 (x, y) 的邻接点，上下左右四个方向，但不能出边界
     * @param heights
     * @param x
     * @param y
     * @return
     */
    private List<int[]> adj(int[][] heights, int x, int y) {
        ArrayList<int[]> list = new ArrayList<>();
        int m = heights.length, n = heights[0].length;
        // 上下左右 四个方向，xy各自的偏移量
        int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            // 不能出边界
            if (nx < 0 || nx >= m || ny < 0 || ny >= n) {
                continue;
            }
            list.add(new int[]{nx, ny});
        }
        return list;
    }
}
