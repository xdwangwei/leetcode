package com.daily;

import java.io.FileReader;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * @author wangwei
 * @date 2023/5/26 21:42
 * @description: _1091_ShortestPathInBinaryMatrix
 *
 * 1091. 二进制矩阵中的最短路径
 * 给你一个 n x n 的二进制矩阵 grid 中，返回矩阵中最短 畅通路径 的长度。如果不存在这样的路径，返回 -1 。
 *
 * 二进制矩阵中的 畅通路径 是一条从 左上角 单元格（即，(0, 0)）到 右下角 单元格（即，(n - 1, n - 1)）的路径，该路径同时满足下述要求：
 *
 * 路径途经的所有单元格的值都是 0 。
 * 路径中所有相邻的单元格应当在 8 个方向之一 上连通（即，相邻两单元之间彼此不同且共享一条边或者一个角）。
 * 畅通路径的长度 是该路径途经的单元格总数。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：grid = [[0,1],[1,0]]
 * 输出：2
 * 示例 2：
 *
 *
 * 输入：grid = [[0,0,0],[1,1,0],[1,1,0]]
 * 输出：4
 * 示例 3：
 *
 * 输入：grid = [[1,0,0],[1,1,0],[1,1,0]]
 * 输出：-1
 *
 *
 * 提示：
 *
 * n == grid.length
 * n == grid[i].length
 * 1 <= n <= 100
 * grid[i][j] 为 0 或 1
 * 通过次数73,166提交次数182,998
 */
public class _1091_ShortestPathInBinaryMatrix {

    /**
     * 方法一：BFS
     *
     * 根据题目描述，一条畅通路径是从左上角单元格 (0,0) 到右下角单元格 (n−1,n−1) 的路径，且路径上所有单元格的值都是 0。
     *
     * 因此，如果左上角单元格 (0,0) 或 右下角单元格 (n-1,n-1) 的值为 1，则不存在满足要求的路径，直接返回 −1。
     *
     * 否则，最短路径，bfs
     *
     * 我们创建一个队列 q，将左上角单元格 (0,0) 加入队列，并且将其标记为已访问，记录 搜索轮数（多叉树层数） step = 1
     *
     * 然后开始广度优先搜索。
     *
     * 在每一轮搜索中，我们每次取出队首节点 (i,j)，如果 (i,j) 为右下角单元格 (n−1,n−1)，则路径长度为当前的搜索轮数，直接返回。
     *
     * 否则，我们将当前节点的所有未被访问过的相邻节点加入队列，并且将它们标记为已访问。
     *      这里的相邻节点包括 上、下、左、右、左上、右上、左下、右下 八个位置
     *                  如果 当前在 (i, j) 那么它的八个临接点为
     *                  for (int x = i - 1; x <= i + 1; ++x) {
     *                     for (int y = j - 1; y <= j + 1; ++y) {
     *      注意节点不能越界
     *
     * 每一轮搜索结束后，我们将搜索轮数增加 1。
     *
     * 然后继续执行上述过程，直到队列为空或者找到目标节点。
     *
     * 如果在搜索结束后，我们仍然没有到达右下角的节点，那么说明右下角的节点不可达，返回 −1。
     *
     * 【666】
     * 由于本题中只寻找节点为0的位置，所以 可以通过将对应位置置1的方式实现 visited 数组的功能，省去额外空间。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/shortest-path-in-binary-matrix/solution/python3javacgotypescript-yi-ti-yi-jie-bf-ipkf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param grid
     * @return
     */
    public int shortestPathBinaryMatrix(int[][] grid) {
        int n = grid.length;
        // 要求路径上所有单元格的值都是 0
        if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) {
            return -1;
        }
        // bfs
        Deque<Integer> q = new ArrayDeque<>();
        // 起点 (0, 0)  将二维坐标转为一维
        q.offer(0);
        // 标记起点已经访问
        grid[0][0] = 1;
        // 初始化搜索轮数，层数
        int step = 1;
        // bfs
        while (!q.isEmpty()) {
            // 当前层所有节点
            for (int k = q.size(); k > 0; --k) {
                // 出队列
                var p = q.poll();
                int i = p / n, j = p % n;
                // 如果已经到达了终点，直接返回 搜索轮数
                if (i == n - 1 && j == n - 1) {
                    return step;
                }
                // 遍历八个临接点
                for (int x = i - 1; x <= i + 1; ++x) {
                    for (int y = j - 1; y <= j + 1; ++y) {
                        // 没有越界，且没有访问过
                        if (x >= 0 && x < n && y >= 0 && y < n && grid[x][y] == 0) {
                            // 加入队列
                            q.offer(x * n + y);
                            // 标记已经访问
                            grid[x][y] = 1;
                        }
                    }
                }
            }
            // 搜索轮数（层数）增加
            step++;
        }
        // 其他，返回 -1
        return -1;
    }
}
