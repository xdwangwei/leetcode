package com.daily;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangwei
 * @date 2023/6/9 19:08
 * @description: _2699_ModifyGraphEdgeWeights
 *
 * 2699. 修改图中的边权
 * 给你一个 n 个节点的 无向带权连通 图，节点编号为 0 到 n - 1 ，再给你一个整数数组 edges ，其中 edges[i] = [ai, bi, wi] 表示节点 ai 和 bi 之间有一条边权为 wi 的边。
 *
 * 部分边的边权为 -1（wi = -1），其他边的边权都为 正 数（wi > 0）。
 *
 * 你需要将所有边权为 -1 的边都修改为范围 [1, 2 * 109] 中的 正整数 ，使得从节点 source 到节点 destination 的 最短距离 为整数 target 。如果有 多种 修改方案可以使 source 和 destination 之间的最短距离等于 target ，你可以返回任意一种方案。
 *
 * 如果存在使 source 到 destination 最短距离为 target 的方案，请你按任意顺序返回包含所有边的数组（包括未修改边权的边）。如果不存在这样的方案，请你返回一个 空数组 。
 *
 * 注意：你不能修改一开始边权为正数的边。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：n = 5, edges = [[4,1,-1],[2,0,-1],[0,3,-1],[4,3,-1]], source = 0, destination = 1, target = 5
 * 输出：[[4,1,1],[2,0,1],[0,3,3],[4,3,1]]
 * 解释：上图展示了一个满足题意的修改方案，从 0 到 1 的最短距离为 5 。
 * 示例 2：
 *
 *
 *
 * 输入：n = 3, edges = [[0,1,-1],[0,2,5]], source = 0, destination = 2, target = 6
 * 输出：[]
 * 解释：上图是一开始的图。没有办法通过修改边权为 -1 的边，使得 0 到 2 的最短距离等于 6 ，所以返回一个空数组。
 * 示例 3：
 *
 *
 *
 * 输入：n = 4, edges = [[1,0,4],[1,2,3],[2,3,5],[0,3,-1]], source = 0, destination = 2, target = 6
 * 输出：[[1,0,4],[1,2,3],[2,3,5],[0,3,1]]
 * 解释：上图展示了一个满足题意的修改方案，从 0 到 2 的最短距离为 6 。
 *
 *
 * 提示：
 *
 * 1 <= n <= 100
 * 1 <= edges.length <= n * (n - 1) / 2
 * edges[i].length == 3
 * 0 <= ai, bi < n
 * wi = -1 或者 1 <= wi <= 107
 * ai != bi
 * 0 <= source, destination < n
 * source != destination
 * 1 <= target <= 109
 * 输入的图是连通图，且没有自环和重边。
 * 通过次数9,212提交次数17,557
 */
public class _2699_ModifyGraphEdgeWeights {

    private final int inf = (int) 1e9 + 1;

    /**
     * 方法：最短路（Dijkstra 算法）
     *
     * 我们先不考虑边权为 −1 的边，使用 Dijkstra 算法求出从 source 到 destination 的最短距离 d。
     *
     * 如果 d < target，说明存在一条完全由正权边组成的最短路径，
     * 此时无论我们如何修改边权为 −1 的边，都无法使得 source 到 destination 的最短距离等于 target，
     * （将它修改为正数，只会使得最短路径不变，或者变得更小，无法使最短路径增大到 target）
     * 因此不存在满足题意的修改方案，返回一个空数组即可。
     *
     *
     * 如果 d = target，说明存在一条完全由正权边组成的、长度为 target 的最短路径，
     * 此时我们可以将所有边权为 −1 的边修改为最大值 10^9 即可。
     *
     * 如果 d > target，
     *      我们可以尝试往图中加入一条边权为 −1 的边，将边权设置为最小正数 1，
     *      然后再次使用 Dijkstra 算法求出从 source 到 destination 的最短距离 d。
     *
     *      如果最短距离 d ≤ target，
     *          说明加入这条边后，可以使得最短路变短，而且新的最短路是 经过这条边 的路径，
     *          那么我们只需要将这条边的边权改为 target−d+1，就可以使得最短路等于 target。
     *          然后我们将其余的边权为 −1 的边修改为最大值 10^9 即可。
     *          （将这条边的权重再次增大后，是否可能导致最短路径不经过这条边?
     *              不可能，没有这条边的时候，最短路径大于target，加入这条边后，最短路径 <= target， 那么必然经过这条边，
     *              只要保证这条边的权重不超过 target-d+1，最短路径就一定是经过这条边的最短路径）
     *
     *      如果最短距离 d>target，
     *          说明加入这条边后，说明这条边不能使最短路变短，
     *          那我们就直接维持它的值为1不变，然后继续尝试加入下一条的边权为 −1 的边。
     *          注意这是一步贪心，因为我们要继续尝试改进最短路让距离更短，所以如果这条边设为1都无解，那设成其它值（更大值）更不可能有解。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/modify-graph-edge-weights/solution/python3javacgotypescript-yi-ti-yi-jie-zu-037o/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @return
     */
    public int[][] modifiedGraphEdges(int n, int[][] edges, int source, int destination, int target) {
        // 排除所有负数边，求出从 source 到 destination 的最短距离 d。
        long dist = dijkstra(n, edges, source, destination);
        // 那么 修改负数边为正数，无法使得最短距离变大
        if (dist < target) {
            return new int[0][];
        }
        // 刚好，那么将 负数边全变为最大值，保证不会产生其他最短路径即可。
        if (dist == target) {
            for (int[] edge : edges) {
                if (edge[2] == -1) {
                    edge[2] = inf;
                }
            }
            return edges;
        }
        // dist > target
        // 考虑 加入 负数边，来产生更短的最短路径
        boolean ok = false;
        for (int[] edge : edges) {
            // 跳过正数边
            if (edge[2] > 0) {
                continue;
            }
            // 将这条负数边改为1，
            edge[2] = 1;
            // 再次求最短距离
            dist = dijkstra(n, edges, source, destination);
            // 说明，新的最短路径　必然　经过这条边，那么　将这条边的权重改为 target-d+1，即可
            // 然后需要将其他负数边的权重改为最大值，保证不会产生其他最短路径
            if (dist <= target) {
                edge[2] += target - dist;
                // 结束循环
                ok = true;
                break;
            }
            // 否则，继续尝试加入其他负数边
        }
        // 加入所有负数边也没用，返回 空
        if (!ok) {
            return new int[0][];
        }
        // 加入某些负数边后 达到预期了，将剩下的负数边权重改为最大值
        for (int[] edge : edges) {
            if (edge[2] == -1) {
                edge[2] = inf;
            }
        }
        return edges;
    }

    /**
     * 朴素版本 dijkstra 求 src 到 dst 的 最短路径
     * @param n
     * @param edges
     * @param src
     * @param dst
     * @return
     */
    private long dijkstra(int n, int[][] edges, int src, int dst) {
        // 创建邻接表
        List<int[]>[] g = new List[n];
        // 初始化
        Arrays.setAll(g, k -> new ArrayList<>());
        // 构建邻接表
        for (int[] edge : edges) {
            // 跳过权重为负数的边
            if (edge[2] == -1) {
                continue;
            }
            // 无相边
            g[edge[0]].add(new int[]{edge[1], edge[2]});
            g[edge[1]].add(new int[]{edge[0], edge[2]});
        }
        // dist[i] 代表 src 到 i 的最短路径
        long[] dist = new long[n];
        // 初始化为 正无穷
        Arrays.fill(dist, inf);
        // 标记节点是否已经访问过（求得了最短路径）
        boolean[] vis = new boolean[n];
        // 初始化 src 到 src 最短距离为 0
        dist[src] = 0;
        // dijkstra
        for (int i = 0; i < n; ++i) {
            // 在还未访问过的节点中 选择 src 到剩余点的距离的最短值对应的点
            int x = -1;
            for (int j = 0; j < n; ++j) {
                if (!vis[j] && (x < 0 || dist[j] < dist[x])) {
                    x = j;
                }
            }
            vis[x] = true;
            // 遍历 x 的所有邻居
            for (int[] neigh : g[x]) {
                int v = neigh[0], wgt = neigh[1];
                // 更新 src 到邻居v的最短距离
                dist[v] = Math.min(dist[v], dist[x] + wgt);
            }
        }
        // 返回 src 到 dst 的最短距离
        return dist[dst];
    }
}
