package com.daily;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author wangwei
 * @date 2022/10/25 12:08
 * @description: _934_ShortestBridge
 *
 * 934. 最短的桥
 * 给你一个大小为 n x n 的二元矩阵 grid ，其中 1 表示陆地，0 表示水域。
 *
 * 岛 是由四面相连的 1 形成的一个最大组，即不会与非组内的任何其他 1 相连。grid 中 恰好存在两座岛 。
 *
 * 你可以将任意数量的 0 变为 1 ，以使两座岛连接起来，变成 一座岛 。
 *
 * 返回必须翻转的 0 的最小数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：grid = [[0,1],[1,0]]
 * 输出：1
 * 示例 2：
 *
 * 输入：grid = [[0,1,0],[0,0,0],[0,0,1]]
 * 输出：2
 * 示例 3：
 *
 * 输入：grid = [[1,1,1,1,1],[1,0,0,0,1],[1,0,1,0,1],[1,0,0,0,1],[1,1,1,1,1]]
 * 输出：1
 *
 *
 * 提示：
 *
 * n == grid.length == grid[i].length
 * 2 <= n <= 100
 * grid[i][j] 为 0 或 1
 * grid 中恰有两个岛
 * 通过次数46,803提交次数92,983
 */
public class _934_ShortestBridge {


    private int[][] grid;
    private int n;
    // 上下左右方向导航
    private int[] dirs = new int[]{-1, 0, 1, 0, -1};

    // dfs找到的第一个岛屿的所有位置点
    private Queue<int[]> queue;


    /**
     * 题目中求最少的翻转 0 的数目等价于求矩阵中两个岛的最短距离，因此我们可以广度优先搜索来找到矩阵中两个块的最短距离。(类似于从a状态转变到b状态的最少操作次数，只不过a、b均是多个状态的集合)
     *
     * 首先找到其中一座岛，以这些点做为起始状态，进行bfs，每次任选一个点将其不断向外延伸一圈，直到到达了另一座岛，按照bfs齐头并进的特点，第一次遇到另一个导时，延伸的圈数即为最短距离。
     *
     * 先利用深度优先搜索求出其中的一座岛，然后利用广度优先搜索来找到两座岛的最短距离。
     *
     * 原数组，0代表海水，1代表陆地；
     *
     * 深度度优先搜索时，我们可以将已经遍历过的位置标记为 2，并且将这些位置加入一个队列中，作为后序bfs的初始点集合；实际计算过程如下：
     *
     * 我们通过遍历找到数组 grid，遇到第一个1 时进行深度优先搜索，dfs结束后就可以当前点所在岛屿的全部位置集合，记为 island，并将其位置全部标记为 2，并全部加入队列。
     * 随后我们从 island 中的所有位置开始同时进行广度优先搜索，当它们到达了任意的 1 时（此时剩下的1一定是另一个岛屿），即表示搜索到了第二个岛，搜索的层数就是答案。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/shortest-bridge/solution/zui-duan-de-qiao-by-leetcode-solution-qe44/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param grid
     * @return
     */
    public int shortestBridge(int[][] grid) {
        this.grid = grid;
        this.n = grid.length;
        this.queue = new LinkedList<>();
        // 找到1个1
        out:for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 1) {
                    // dfs找出它所在岛屿的全部点。修改值为2，加入队列
                    dfs(i, j);
                    // 这里需要跳出外围循环
                    break out;
                }
            }
        }
        // 以第一个到全部节点为起始状态进行bfs，直到扩展到第二个岛屿（任意一个值为1的点）
        int step = 0;
        while (!queue.isEmpty()) {
            int sz = queue.size();
            for (int i = 0; i < sz; ++i) {
                // 队首
                int[] poll = queue.poll();
                int r = poll[0], c = poll[1];
                // 它的上下左右
                for (int k = 0; k < dirs.length - 1; ++k) {
                    int nr = r + dirs[k], nc = c + dirs[k + 1];
                    // 跳过非法位置
                    if (!validPos(nr, nc)) {
                        continue;
                    }
                    // 遇到了另一个岛屿，直接返回步数
                    if (grid[nr][nc] == 1) {
                        return step;
                    }
                    // 海水，合并到当前岛屿(值为2)
                    if (grid[nr][nc] == 0) {
                        grid[nr][nc] = 2;
                        // 加入队列
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
            // 一轮结束，起头并进
            step++;
        }
        // 因为题目保证只有两个岛屿，所以一定有解，这里不会抵达
        return step;
    }


    /**
     * dfs找到row，col代表位置所在岛屿的全部节点，将其值改为2，并加入队列
     * @param row
     * @param col
     */
    private void dfs(int row, int col) {
        // 跳过非法位置，跳过海水位置，跳过已经访问过的位置
        if (!validPos(row, col) || grid[row][col] != 1) {
            return;
        }
        // 标记
        grid[row][col] = 2;
        // 入队
        queue.offer(new int[]{row, col});
        // dfs访问四周
        for (int k = 0; k < dirs.length - 1; ++k) {
            dfs(row + dirs[k], col + dirs[k + 1]);
        }
    }

    /**
     * 判断索引是否越界
     * @param row
     * @param col
     * @return
     */
    private boolean validPos(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }
}
