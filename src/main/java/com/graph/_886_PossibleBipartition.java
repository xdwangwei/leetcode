package com.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangwei
 * 2021/11/15 16:04
 *
 * 给定一组 N 人（编号为 1, 2, ..., N）， 我们想把每个人分进任意大小的两组。
 *
 * 每个人都可能不喜欢其他人，那么他们不应该属于同一组。
 *
 * 形式上，如果 dislikes[i] = [a, b]，表示不允许将编号为 a 和 b 的人归入同一组。
 *
 * 当可以用这种方法将所有人分进两组时，返回 true；否则返回 false。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：N = 4, dislikes = [[1,2],[1,3],[2,4]]
 * 输出：true
 * 解释：group1 [1,4], group2 [2,3]
 * 示例 2：
 *
 * 输入：N = 3, dislikes = [[1,2],[1,3],[2,3]]
 * 输出：false
 * 示例 3：
 *
 * 输入：N = 5, dislikes = [[1,2],[2,3],[3,4],[4,5],[1,5]]
 * 输出：false
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/possible-bipartition
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _886_PossibleBipartition {

    /**
     * dfs/bfs 染色方式
     *
     * 先由 dislikes[][] 构造一副无向图，再进行二色染色
     *
     * 注意 n 是 从 1 开始的
     * @param n
     * @param dislikes
     * @return
     */
    private boolean[] visited;
    private boolean[] color;
    private boolean valid = true;
    public boolean possibleBipartition(int n, int[][] dislikes) {
        List<ArrayList<Integer>> graph = buildGraph(n, dislikes);
        visited = new boolean[n + 1];
        color = new boolean[n + 1];
        // 所有未访问的点进行dfs, 为什么要从每个点开始，因为一幅图是由多个树组成的，就有多个根
        for (int i = 1; i <= n; i++) {
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
    private void dfs(List<ArrayList<Integer>> graph, int i) {
        if (!valid) {
            return;
        }
        // 标记当前点已经访问过
        visited[i] = true;
        // 对于所有邻接点
        for (int neighbor : graph.get(i)) {
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
     * bfs染色
     * @param graph
     * @param i
     */
    private void bfs(List<ArrayList<Integer>> graph, int i) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(i);
        visited[i] = true;
        // 如果valid为false，就不用继续了
        while (!queue.isEmpty() && valid) {
            int v = queue.poll();
            // 从节点 v 向所有相邻节点扩散
            for (int w : graph.get(v)) {
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


    /**
     * 构造无向图
     * @param n
     * @param dislikes
     * @return
     */
    private List<ArrayList<Integer>> buildGraph(int n, int[][] dislikes) {
        List<ArrayList<Integer>> graph = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList());
        }
        // 构造无向图
        for (int[] edge : dislikes) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        return graph;
    }
}
