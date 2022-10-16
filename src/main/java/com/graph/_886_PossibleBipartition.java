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
     *
     * 如果你把每个人看做图中的节点，相互讨厌的关系看做图中的边，那么 dislikes 数组就可以构成一幅图；
     *
     * 又因为题目说互相讨厌的人不能放在同一组里，相当于图中的所有相邻节点都要放进两个不同的组；
     *
     * 那就回到了「双色问题」，如果能够用两种颜色着色所有节点，且相邻节点颜色都不同，那么你按照颜色把这些节点分成两组不就行了嘛。
     *
     * 所以解法就出来了，我们把 dislikes 构造成一幅图，然后执行二分图的判定算法即可。
     *
     * 二分图判定模板：https://labuladong.github.io/algo/2/22/52/
     *
     * 染色法，一共两种颜色，让每一条边的两个节点都是不同颜色
     *
     * dfs/bfs 染色方式
     *
     * 先由 dislikes[][] 构造一副无向图，再进行二色染色
     *
     * 注意 n 是 从 1 开始的
     * @param n
     * @param dislikes
     * @return
     */
    // 访问过、染色过的点
    private boolean[] visited;
    // 每个点的颜色，起始二者可以用1个int数组代替，0代表未访问，1代表红色，2代表黑色
    private boolean[] color;
    // 到目前为止，图是否有效
    private boolean valid = true;
    public boolean possibleBipartition(int n, int[][] dislikes) {
        // 构件图
        List<ArrayList<Integer>> graph = buildGraph(n, dislikes);
        // 已染色的点
        visited = new boolean[n + 1];
        // 每个点的颜色
        color = new boolean[n + 1];
        // 所有未访问的点进行dfs, 为什么要从每个点开始，因为一幅图是由多个树组成的，就有多个根
        for (int i = 1; i <= n; i++) {
            // 跳过已处理的
            if (!visited[i]) {
                dfs(graph, i);
                // 如果 valid = false，直接退出
                if (!valid) {
                    return false;
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
        // 图已经染色失败了
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
            } else {
                // 这个点已经visited了，并且它的颜色和我一样，那肯定不是二分图，返回
                // 比如处理1，染色为红色，1不能和2一起，所以处理2染色为黑色，2不能和3一起，染色3为红色，再返回到1时处理下一个1不能和3一起，但是3已经是红色了，所以冲突
                // 起始就是存在环
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
     * 反向点 + 并查集
     *
     * 我们知道对于 ds[i]=(a,b) 而言，点 a 和点 b 必然位于不同的集合中，
     * 同时由于只有两个候选集合，因此这样的关系具有推断性：即对于 (a,b) 和 (b,c) 可知 a 和 c 位于同一集合。
     *
     * 简单说就是：并查集维护的是需要联通的两个点，题目给出的不能联通的两个点a,b，等价于a和b的一个不能联通的点c联通
     *
     * 因此，我们可以对于每个点 x 而言，建议一个反向点 x + n：默认情况下，x 和 n + x 肯定是并未联通的
     * 若点 x 位于集合 A 则其反向点 x + n 位于集合 B，反之同理。
     * 那么当要求a和b不能联通时，我们就去联通a和b+n就可以了
     *
     * 基于此，我们可以使用「并查集」维护所有点的连通性：维护并检查每个 ds[i] 的联通关系，
     * 若 ds[i]=(a,b) 联通，必然是其反向点联通所导致，必然是此前的其他 ds[j] 导致的关系冲突，必然不能顺利划分成两个集合，返回 false，否则返回 true。
     *
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/possible-bipartition/solution/by-ac_oier-6j0n/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     *
     * 另一种思路是，假如a和bcd不能联通，那么bcd必然联通，所以对于a的dislike列表list，让list[i]和list[0]去联通，并保证a不能list中任何一个元素联通即可
     *          for (int i = 1; i <= n; ++i) {
     *             // a的dislike列表
     *             for (int j = 0; j < g[i].size(); ++j) {
     *                 // 列表中所有元素和list[0]联通
     *                 unit(g[i].get(0), g[i].get(j));
     *                 // a 不能和这些点联通
     *                 if (isconnect(i, g[i].get(j))) {
     *                     return false;
     *                 }
     *             }
     *         }
     */
    // father节点
    int[] p = new int[4010];
    // 带路径压缩
    int find(int x) {
        if (p[x] != x) p[x] = find(p[x]);
        return p[x];
    }
    // 联通a，b，并未考虑两棵子树的节点数目
    void union(int a, int b) {
        p[find(a)] = p[find(b)];
    }
    // 是否联通
    boolean query(int a, int b) {
        return find(a) == find(b);
    }
    public boolean possibleBipartition3(int n, int[][] ds) {
        // 初始化父节点为自己
        for (int i = 1; i <= 2 * n; i++) p[i] = i;
        for (int[] info : ds) {
            // a，b不能联通
            int a = info[0], b = info[1];
            // 若 (a,b) 联通，必然是其反向点联通所导致，必然是此前的其他 ds[j] 导致的关系冲突，必然不能顺利划分成两个集合
            if (query(a, b)) return false;
            // 联通a和b的反向点。b和a的反向点
            union(a, b + n); union(b, a + n);
        }
        return true;
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
            graph.add(new ArrayList<>());
        }
        // 构造无向图
        for (int[] edge : dislikes) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        return graph;
    }
}
