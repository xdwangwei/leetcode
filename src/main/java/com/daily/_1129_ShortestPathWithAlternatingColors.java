package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2023/2/2 18:24
 * @description: _1129_ShortestPathWithAlternatingColors
 *
 * 1129. 颜色交替的最短路径
 * 在一个有向图中，节点分别标记为 0, 1, ..., n-1。图中每条边为红色或者蓝色，且存在自环或平行边。
 *
 * red_edges 中的每一个 [i, j] 对表示从节点 i 到节点 j 的红色有向边。类似地，blue_edges 中的每一个 [i, j] 对表示从节点 i 到节点 j 的蓝色有向边。
 *
 * 返回长度为 n 的数组 answer，其中 answer[X] 是从节点 0 到节点 X 的红色边和蓝色边交替出现的最短路径的长度。如果不存在这样的路径，那么 answer[x] = -1。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 3, red_edges = [[0,1],[1,2]], blue_edges = []
 * 输出：[0,1,-1]
 * 示例 2：
 *
 * 输入：n = 3, red_edges = [[0,1]], blue_edges = [[2,1]]
 * 输出：[0,1,-1]
 * 示例 3：
 *
 * 输入：n = 3, red_edges = [[1,0]], blue_edges = [[2,1]]
 * 输出：[0,-1,-1]
 * 示例 4：
 *
 * 输入：n = 3, red_edges = [[0,1]], blue_edges = [[1,2]]
 * 输出：[0,1,2]
 * 示例 5：
 *
 * 输入：n = 3, red_edges = [[0,1],[0,2]], blue_edges = [[1,0]]
 * 输出：[0,1,1]
 *
 *
 * 提示：
 *
 * 1 <= n <= 100
 * red_edges.length <= 400
 * blue_edges.length <= 400
 * red_edges[i].length == blue_edges[i].length == 2
 * 0 <= red_edges[i][j], blue_edges[i][j] < n
 * 通过次数14,579提交次数32,855
 */
public class _1129_ShortestPathWithAlternatingColors {


    /**
     * 方法：广度优先搜索
     * 使用 0 表示红色，1 表示蓝色，对于某一节点 x，从节点 0 到节点 x 的红色边和蓝色边交替出现的路径有两种类型：
     *
     *      类型 0：路径最终到节点 x 的有向边为红色；
     *      类型 1：路径最终到节点 x 的有向边为蓝色。
     *
     * 如果存在从节点 0 到节点 x 的类型 0 的颜色交替路径，并且从节点 x 到节点 y 的有向边为蓝色，
     * 那么该路径加上该有向边组成了从节点 0 到节点 y 的类型 1 的颜色交替路径。
     * 类似地，如果存在从节点 0 到节点 x 的类型 1 的颜色交替路径，并且从节点 x 到节点 y 的有向边为红色，
     * 那么该路径加上该有向边组成了从节点 0 到节点 y 的类型 0 的颜色交替路径。
     *
     * bfs [齐头并进]，第一次到达某个点时所走的步数（无论类型0还是1）就是到达它的最短交替路径数。
     *
     * 我们对所有的边进行预处理，将所有的边按照颜色分类，存储到二维数组 g 中。
     * 其中 g[0] 存储所有红色边，而 g[1] 存储所有蓝色边。
     *
     * 接着，我们定义以下数据结构或变量：
     *
     * 队列 q：元素为 (x, c) 用来存储当前搜索到的节点x，以及到达此点的路径的颜色c；
     * 集合 vis：二维数组[n][2] 用来存储已经搜索过的节点，以及到达此点时的边的颜色（队列中的元素是否已访问）；
     * 变量 d：用来表示当前搜索的层数，即当前搜索到的节点到起点的距离；（bfs齐头并进的层数）
     * 数组 ans：用来存储每个节点到起点的最短距离。
     *      初始时，我们将 ans 数组中的所有元素初始化为 −1，表示所有节点到起点的距离都未知，也是默认返回值
     *
     * 我们首先将起点 0 和起点边的颜色 0 或 1 入队，表示 通过红色边到达起点0、通过蓝色边到达起点0 的初始元素。
     *
     * 接下来，我们开始进行 BFS 搜索。
     * 我们每次从队列中取出一个节点 (i,c)，
     * 如果当前节点的答案还未更新（ans[i]=-1），则将当前节点的答案更新为当前层数d，即 ans[i]=d。
     * 然后，我们将当前边的颜色 c 取反，即如果当前边为红色，则将其变为蓝色，反之亦然。
     * 我们取出此点此颜色的所有邻接点（边），如果边的另一端节点 j 未被搜索过，则将其入队。
     *
     * 搜索结束后，返回答案数组即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/shortest-path-with-alternating-colors/solution/python3javacgo-yi-ti-yi-jie-bfsqing-xi-t-ag0i/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/shortest-path-with-alternating-colors/solution/yan-se-jiao-ti-de-zui-duan-lu-jing-by-le-vnuu/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param redEdges
     * @param blueEdges
     * @return
     */
    public int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {
        // g[0] 存储所有红色边，而 g[1] 存储所有蓝色边。
        // 初始化
        List<Integer>[][] g = new List[2][n];
        for (List<Integer>[] g1 : g) {
            Arrays.setAll(g1, k -> new ArrayList<>());
        }
        // 记录红色边（邻接点）
        for (int[] edge : redEdges) {
            int f = edge[0], t = edge[1];
            g[0][f].add(t);
        }
        // 记录蓝色边（邻接点）
        for (int[] edge : blueEdges) {
            int f = edge[0], t = edge[1];
            g[1][f].add(t);
        }
        // bfs，队列元素 (x, c) 通过颜色 c 到达点 x
        Deque<int[]> q = new ArrayDeque<>();
        // 初始点：红色到0、蓝色到0
        q.offer(new int[]{0, 0});
        q.offer(new int[]{0, 1});
        // 记录每个状态点是否已访问（是否入队列）
        boolean[][] vis = new boolean[n][2];
        // 起点到每个点颜色交替的最短路径长度
        int[] ans = new int[n];
        // 初始化为-1
        Arrays.fill(ans, -1);
        // bfs进行到的层数
        int d = 0;
        // bfs
        while (!q.isEmpty()) {
            int sz = q.size();
            // 当前层节点
            for (int i = 0; i < sz; ++i) {
                int[] cur = q.poll();
                // 编号，到达时的边颜色
                int x = cur[0], c = cur[1];
                // 按照bfs层序遍历的特点，第一次到达某个点时的步数就是起点到它的最短路径，无论是通过红色还是蓝色边到达
                if (ans[x] == -1) {
                    ans[x] = d;
                }
                // 标记当前状态点已访问
                vis[x][c] = true;
                // 按照颜色交替规则向下遍历，这里颜色取反
                c ^= 1;
                // 遍历当前点x在此颜色c边上的邻接点
                for (Integer v : g[c][x]) {
                    // 对于还未访问的状态点
                    // 入队
                    if (!vis[v][c]) {
                        q.offer(new int[]{v, c});
                    }
                }
            }
            // 层数+1
            d++;
        }
        // 返回
        return ans;
    }
}
