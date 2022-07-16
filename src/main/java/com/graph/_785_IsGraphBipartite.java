package com.graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author wangwei
 * 2021/11/15 15:29
 *
 * 存在一个 无向图 ，图中有 n 个节点。其中每个节点都有一个介于 0 到 n - 1 之间的唯一编号。给你一个二维数组 graph ，其中 graph[u] 是一个节点数组，由节点 u 的邻接节点组成。形式上，对于 graph[u] 中的每个 v ，都存在一条位于节点 u 和节点 v 之间的无向边。该无向图同时具有以下属性：
 * 不存在自环（graph[u] 不包含 u）。
 * 不存在平行边（graph[u] 不包含重复值）。
 * 如果 v 在 graph[u] 内，那么 u 也应该在 graph[v] 内（该图是无向图）
 * 这个图可能不是连通图，也就是说两个节点 u 和 v 之间可能不存在一条连通彼此的路径。
 * 二分图 定义：如果能将一个图的节点集合分割成两个独立的子集 A 和 B ，并使图中的每一条边的两个节点一个来自 A 集合，一个来自 B 集合，就将这个图称为 二分图 。
 *
 * 如果图是二分图，返回 true ；否则，返回 false 。
 *
 *  
 *
 * 示例 1：
 *
 *
 * 输入：graph = [[1,2,3],[0,2],[0,1,3],[0,2]]
 * 输出：false
 * 解释：不能将节点分割成两个独立的子集，以使每条边都连通一个子集中的一个节点与另一个子集中的一个节点。
 * 示例 2：
 *
 *
 * 输入：graph = [[1,3],[0,2],[1,3],[0,2]]
 * 输出：true
 * 解释：可以将节点分成两组: {0, 2} 和 {1, 3}
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/is-graph-bipartite
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _785_IsGraphBipartite {

    /**
     * dfs 染色，每个点 给它的邻接点染成相反的颜色，如果不能做到，那就不是二分图
     * @param graph
     * @return
     */
    // 当前节点是否已访问
    private boolean[] visited;
    // 每个点的颜色，因为只需要两种颜色，所以用bool
    private boolean[] color;
    // 是否是二分图
    private boolean valid = true;
    public boolean isBipartite(int[][] graph) {
        int nodes = graph.length;
        // 初始化
        visited = new boolean[nodes];
        color = new boolean[nodes];
        // 所有未访问的点进行dfs, 为什么要从每个点开始，因为一幅图是由多个树组成的，就有多个根
        for (int i = 0; i < nodes; i++) {
            if (!visited[i]) {
                dfs(graph, i);
                // 如果 valid = false，直接退出
                if (!valid) {
                    break;
                }
            }
        }
        return valid;
    }

    /**
     * dfs 染色，每个点 给它的邻接点染成相反的颜色
     * @param graph
     * @param i
     */
    private void dfs(int[][] graph, int i) {
        if (!valid) {
            return;
        }
        // 标记当前点已经访问过
        visited[i] = true;
        // 对于所有邻接点
        for (int neighbor : graph[i]) {
            // 这个点未访问过
            if (!visited[neighbor]) {
                // 把它的color标记为相反的颜色
                color[neighbor] = !color[i];
                // dfs
                dfs(graph, neighbor);
                // 可以退出
                if (!valid) {
                    return;
                }
            // 这个点已经visited了，
            } else {
                // 如果它的颜色和我一样，那肯定不是二分图，返回
                if (color[neighbor] == color[i]) {
                    valid = false;
                    return;
                }
            }
        }
    }

    /**
     * bfs 染色，每个点 给它的邻接点染成相反的颜色，如果不能做到，那就不是二分图
     * @param graph
     * @return
     */
    public boolean isBipartite2(int[][] graph) {
        int nodes = graph.length;
        // 初始化
        visited = new boolean[nodes];
        color = new boolean[nodes];
        // 所有未访问的点进行dfs
        for (int i = 0; i < nodes; i++) {
            if (!visited[i]) {
                bfs(graph, i);
                // 如果 valid = false，直接退出
                if (!valid) {
                    break;
                }
            }
        }
        return valid;
    }

    /**
     * bfs
     * @param graph
     * @param i
     */
    private void bfs(int[][] graph, int i) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(i);
        visited[i] = true;
        // 如果valid为false，就不用继续了
        while (!queue.isEmpty() && valid) {
            int v = queue.poll();
            // 从节点 v 向所有相邻节点扩散
            for (int w : graph[v]) {
                if (!visited[w]) {
                    // 相邻节点 w 没有被访问过
                    // 那么应该给节点 w 涂上和节点 v 不同的颜色
                    color[w] = !color[v];
                    // 标记 w 节点，并放入队列
                    visited[w] = true;
                    queue.offer(w);
                } else {
                    // 相邻节点 w 已经被访问过
                    // 根据 v 和 w 的颜色判断是否是二分图
                    if (color[w] == color[v]) {
                        // 若相同，则此图不是二分图
                        valid = false;
                        return;
                    }
                }
            }
        }
    }
}
