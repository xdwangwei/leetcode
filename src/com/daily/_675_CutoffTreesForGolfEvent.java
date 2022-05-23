package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/5/23 12:28
 * @description: _675_CutoffTreesForGolfEvent
 *
 * 675. 为高尔夫比赛砍树
 * 你被请来给一个要举办高尔夫比赛的树林砍树。树林由一个 m x n 的矩阵表示， 在这个矩阵中：
 *
 * 0 表示障碍，无法触碰
 * 1 表示地面，可以行走
 * 比 1 大的数 表示有树的单元格，可以行走，数值表示树的高度
 * 每一步，你都可以向上、下、左、右四个方向之一移动一个单位，如果你站的地方有一棵树，那么你可以决定是否要砍倒它。
 *
 * 你需要按照树的高度从低向高砍掉所有的树，每砍过一颗树，该单元格的值变为 1（即变为地面）。
 *
 * 你将从 (0, 0) 点开始工作，返回你砍完所有树需要走的最小步数。 如果你无法砍完所有的树，返回 -1 。
 *
 * 可以保证的是，没有两棵树的高度是相同的，并且你至少需要砍倒一棵树。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：forest = [[1,2,3],[0,0,4],[7,6,5]]
 * 输出：6
 * 解释：沿着上面的路径，你可以用 6 步，按从最矮到最高的顺序砍掉这些树。
 * 示例 2：
 *
 *
 * 输入：forest = [[1,2,3],[0,0,0],[7,6,5]]
 * 输出：-1
 * 解释：由于中间一行被障碍阻塞，无法访问最下面一行中的树。
 * 示例 3：
 *
 * 输入：forest = [[2,3,4],[0,0,5],[8,7,6]]
 * 输出：6
 * 解释：可以按与示例 1 相同的路径来砍掉所有的树。
 * (0,0) 位置的树，可以直接砍去，不用算步数。
 *
 *
 * 提示：
 *
 * m == forest.length
 * n == forest[i].length
 * 1 <= m, n <= 50
 * 0 <= forest[i][j] <= 109
 * 通过次数5,496提交次数11,679
 */
public class _675_CutoffTreesForGolfEvent {


    /**
     * 题目要求从 (0,0) 开始并按照树的高度大小进行砍树并求出最小步数，
     * 假设所有树按照从高度从小到大的排序顺序为 t_1, t_2, t_3, t_4, ,t_n
     * 设 d(x,y) 表示从 x 到 y 之间的步数，设 t_0 = (0, 0) ，则可推出砍树的总的步数为 total=∑d(t_i, t_i+1),  0 <= i <= n - 1
     * 若使得 total 最小，只需满足所有的 d(i,i+1) 都为最小，即可使得 total 最小，该题即转为求相邻树的两点之间的最短距离。
     *
     * 也就是，先找到所有树所在位置，对这些位置按照树高度从小到大排序
     *
     * 再找出排序后的这些位置，每两个位置之间的最短距离(最少移动步数)，再累加
     * 若任意两点无法通过，则整体任务无法完成，返回-1
     *
     * **********************************************************
     * 同时题目限定了我们只能按照「从低到高」的顺序进行砍树，并且图中不存在高度相等的两棵树，这意味着 整个砍树的顺序唯一确定，
     * 就是对所有有树的地方进行「高度」排升序，即是完整的砍树路线。
     * 而另外一个更为重要的性质是：点与点之间的最短路径，不会随着砍树过程的进行而发生变化（某个树点被砍掉，只会变为平地，不会变为阻碍点，仍可通过）。
     * 综上，砍树的路线唯一确定，当我们求出每两个相邻的砍树点最短路径，并进行累加即是答案（整条砍树路径的最少步数）。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/cut-off-trees-for-golf-event/solution/by-ac_oier-ksth/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 方法一：广度优先搜索
     * 思路与算法
     *
     * 首先对矩阵中的树按照树的高度进行排序，我们依次求出相邻的树之间的最短距离。
     * 利用广度优先搜索，按照层次遍历，处理队列中的节点（网格位置）。
     * \visited 记录在某个时间点已经添加到队列中的节点，这些节点已被处理或在等待处理的队列中。
     * 对于下一个要处理的每个节点，查看他们的四个方向上相邻的点，如果相邻的点没有被遍历过且不是障碍，将其加入到队列中，直到找到终点为止，
     * 返回当前的步数即可。最终返回所有的步数之和即为最终结果。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/cut-off-trees-for-golf-event/solution/wei-gao-er-fu-bi-sai-kan-shu-by-leetcode-rlrc/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    // 方向数组
    int[] dirs = {-1, 0, 1, 0, -1};

    public int cutOffTree(List<List<Integer>> forest) {
        int row = forest.size();
        int col = forest.get(0).size();
        // 记录所有树的位置
        // 按照树高对这些位置排序
        PriorityQueue<int[]> trees = new PriorityQueue<>(Comparator.comparingInt(a -> forest.get(a[0]).get(a[1])));
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                // 树
                if (forest.get(i).get(j) > 1) {
                    trees.add(new int[]{i, j});
                }
            }
        }

        // 初始起点
        int cx = 0;
        int cy = 0;
        int ans = 0;
        // 按顺序的树位置
        while (!trees.isEmpty()) {
            int[] tree = trees.poll();
            // 两个位置之间的最少移动步数
            int steps = bfs(forest, cx, cy, tree[0], tree[1]);
            if (steps == -1) {
                return -1;
            }
            // 累加总步数
            ans += steps;
            // 更新当前位置
            cx = tree[0];
            cy = tree[1];
        }
        return ans;
    }


    /**
     * bfs求位置 (sx, sy) 到 (tx, ty) 之间的最少移动步数
     * @param forest
     * @param sx
     * @param sy
     * @param tx
     * @param ty
     * @return
     */
    public int bfs(List<List<Integer>> forest, int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) {
            return 0;
        }
        int row = forest.size();
        int col = forest.get(0).size();

        // bfs
        int step = 0;
        Queue<int[]> queue = new ArrayDeque<>();
        // 记录已访问位置
        boolean[][] visited = new boolean[row][col];
        queue.offer(new int[]{sx, sy});
        visited[sx][sy] = true;
        while (!queue.isEmpty()) {
            // 齐头并进
            step++;
            int sz = queue.size();
            for (int i = 0; i < sz; ++i) {
                int[] cell = queue.poll();
                int cx = cell[0], cy = cell[1];
                // 四个方向邻接点
                for (int j = 0; j < 4; ++j) {
                    int nx = cx + dirs[j];
                    int ny = cy + dirs[j + 1];
                    // 位置合法性
                    if (nx >= 0 && nx < row && ny >= 0 && ny < col) {
                        // 未遍历过，并且要能通过
                        if (!visited[nx][ny] && forest.get(nx).get(ny) > 0) {
                            if (nx == tx && ny == ty) {
                                return step;
                            }
                            // 邻接点入队列并标记
                            queue.offer(new int[]{nx, ny});
                            visited[nx][ny] = true;
                        }
                    }
                }
            }
        }
        return -1;
    }
}
