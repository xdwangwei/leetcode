package com.daily;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangwei
 * @date 2022/12/20 14:28
 * @description: _1971_FindIfPathExistsInGraph
 *
 * 1971. 寻找图中是否存在路径
 * 有一个具有 n 个顶点的 双向 图，其中每个顶点标记从 0 到 n - 1（包含 0 和 n - 1）。图中的边用一个二维整数数组 edges 表示，其中 edges[i] = [ui, vi] 表示顶点 ui 和顶点 vi 之间的双向边。 每个顶点对由 最多一条 边连接，并且没有顶点存在与自身相连的边。
 *
 * 请你确定是否存在从顶点 source 开始，到顶点 destination 结束的 有效路径 。
 *
 * 给你数组 edges 和整数 n、source 和 destination，如果从 source 到 destination 存在 有效路径 ，则返回 true，否则返回 false 。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：n = 3, edges = [[0,1],[1,2],[2,0]], source = 0, destination = 2
 * 输出：true
 * 解释：存在由顶点 0 到顶点 2 的路径:
 * - 0 → 1 → 2
 * - 0 → 2
 * 示例 2：
 *
 *
 * 输入：n = 6, edges = [[0,1],[0,2],[3,5],[5,4],[4,3]], source = 0, destination = 5
 * 输出：false
 * 解释：不存在由顶点 0 到顶点 5 的路径.
 *
 *
 * 提示：
 *
 * 1 <= n <= 2 * 105
 * 0 <= edges.length <= 2 * 105
 * edges[i].length == 2
 * 0 <= ui, vi <= n - 1
 * ui != vi
 * 0 <= source, destination <= n - 1
 * 不存在重复边
 * 不存在指向顶点自身的边
 * 通过次数10,809提交次数21,800
 */
public class _1971_FindIfPathExistsInGraph {

    /**
     * 方法一：深度优先搜索
     * 思路与算法
     *
     * 我们使用深度优先搜索检测顶点 source,destination 的连通性，
     * 需要从顶点 source 开始依次遍历每一条可能的路径，判断可以到达顶点 destination，
     * 同时还需要记录每个顶点的访问状态防止重复访问。
     *
     * 首先从顶点 source 开始遍历并进行递归搜索。
     * 搜索时每次访问一个顶点 vertex 时，如果 vertex 等于 destination 则直接返回，
     * 否则将该顶点设为已访问，并递归访问与 vertex 相邻且未访问的顶点 next。
     * 如果通过 next 的路径可以访问到 destination，此时直接返回 true，
     * 当访问完所有的邻接节点仍然没有访问到 destination，此时返回 false。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/find-if-path-exists-in-graph/solution/xun-zhao-tu-zhong-shi-fou-cun-zai-lu-jin-d0q0/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class Solution1 {
        public boolean validPath(int n, int[][] edges, int source, int destination) {
            List<Integer>[] adj = new List[n];
            // 构建无向图
            for (int i = 0; i < n; i++) {
                adj[i] = new ArrayList<>();
            }
            for (int[] edge : edges) {
                int x = edge[0], y = edge[1];
                adj[x].add(y);
                adj[y].add(x);
            }
            // 节点访问状态标记数组
            boolean[] visited = new boolean[n];
            // 从source开始dfs，能到达des则返回true，否则返回false
            return dfs(source, destination, adj, visited);
        }

        /**
         * dfs 从 source 能否 到达 destination
         * @param source
         * @param destination
         * @param adj
         * @param visited
         * @return
         */
        public boolean dfs(int source, int destination, List<Integer>[] adj, boolean[] visited) {
            // 当前节点恰好是des，返回true
            if (source == destination) {
                return true;
            }
            // 标记当前节点已访问
            visited[source] = true;
            // 遍历邻接节点
            for (int next : adj[source]) {
                // 如果节点x未访问过，并且从v能够到达des，那么从当前节点也能到达des
                if (!visited[next] && dfs(next, destination, adj, visited)) {
                    return true;
                }
            }
            // 其他，返回false
            return false;
        }
    }

    /**
     * 方法二：广度优先搜索
     * 思路与算法
     *
     * 使用广度优先搜索判断顶点 source 到顶点 destination 的连通性，
     * 需要我们从顶点 source 开始按照层次依次遍历每一层的顶点，检测是否可以到达顶点 destination。
     *
     * 遍历过程我们使用队列存储最近访问过的顶点，同时记录每个顶点的访问状态，每次从队列中取出顶点 vertex 时，
     * 将其未访问过的邻接顶点入队列。
     *
     * 初始时将顶点 source 设为已访问，并将其入队列。
     * 每次将队列中的节点 vertex 出队列，并将与 vertex 相邻且未访问的顶点 next 入队列，并将 next 设为已访问。
     * 当队列为空或访问到顶点 destination 时遍历结束，返回顶点 destination 的访问状态即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/find-if-path-exists-in-graph/solution/xun-zhao-tu-zhong-shi-fou-cun-zai-lu-jin-d0q0/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class Solution2 {
        public boolean validPath(int n, int[][] edges, int source, int destination) {
            List<Integer>[] adj = new List[n];
            // 构建无向图
            for (int i = 0; i < n; i++) {
                adj[i] = new ArrayList<>();
            }
            for (int[] edge : edges) {
                int x = edge[0], y = edge[1];
                adj[x].add(y);
                adj[y].add(x);
            }
            // 标记每个点访问状态
            boolean[] visited = new boolean[n];
            // bfs，队列
            Queue<Integer> queue = new ArrayDeque<>();
            // 加入起点
            queue.offer(source);
            // 标记起点
            visited[source] = true;
            // bfs
            while (!queue.isEmpty()) {
                // 出队列
                int vertex = queue.poll();
                // 因为是从source开始的dfs，所以如果遇到des。提前结束，返回true
                if (vertex == destination) {
                    return true;
                }
                // 遍历邻接节点
                for (int next : adj[vertex]) {
                    if (!visited[next]) {
                        // 加入并标记 还未访问过的 邻接节点
                        queue.offer(next);
                        visited[next] = true;
                    }
                }
            }
            // 无法到达des
            return false;
        }
    }

    /**
     * 方法三：并查集
     * 思路与算法
     *
     * 我们将图中的每个强连通分量视为一个集合，强连通分量中任意两点均可达，
     * 如果两个点 source 和 destination 处在同一个强连通分量中，则两点一定可连通，因此连通性问题可以使用并查集解决。
     *
     * 并查集初始化时，n 个顶点分别属于 n 个不同的集合，每个集合只包含一个顶点。
     * 初始化之后遍历每条边，由于图中的每条边均为双向边，因此将同一条边连接的两个顶点所在的集合做合并。
     *
     * 遍历所有的边之后，判断顶点 source 和顶点 destination 是否在同一个集合中，
     * 如果两个顶点在同一个集合则两个顶点连通，如果两个顶点所在的集合不同则两个顶点不连通。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/find-if-path-exists-in-graph/solution/xun-zhao-tu-zhong-shi-fou-cun-zai-lu-jin-d0q0/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class Solution {
        public boolean validPath(int n, int[][] edges, int source, int destination) {
            if (source == destination) {
                return true;
            }
            // 初始化，每个节点的父节点初始化为自己
            int[] fa = new int[n];
            for (int i = 0; i < n; ++i) {
                fa[i] = i;
            }
            // 将每条边的两个端点进行联通
            for (int[] edge : edges) {
                union(fa, edge[0], edge[1]);
            }
            // 判断指定节点是否联通即可
            return findRoot(fa, source) == findRoot(fa, destination);
        }

        // 找到祖宗节点，带路径压缩
        private int findRoot(int[] fa, int u) {
            while (fa[u] != u) {
                // 路径压缩
                fa[u] = fa[fa[u]];
                u = fa[u];
            }
            return u;
        }

        // 联通两个点，就是把u的祖先改为和v的祖先一样
        private void union(int[] fa, int u, int v) {
            int fp = findRoot(fa, u);
            int fq = findRoot(fa, v);
            fa[fp] = fq;
        }
    }
}
