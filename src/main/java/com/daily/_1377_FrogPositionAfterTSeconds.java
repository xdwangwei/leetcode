package com.daily;

import com.common.Pair;

import javax.naming.event.EventDirContext;
import java.util.*;

/**
 * @author wangwei
 * @date 2023/5/24 21:12
 * @description: _1377_FrogPositionAfterTSeconds
 *
 * 1377. T 秒后青蛙的位置
 * 给你一棵由 n 个顶点组成的无向树，顶点编号从 1 到 n。青蛙从 顶点 1 开始起跳。规则如下：
 *
 * 在一秒内，青蛙从它所在的当前顶点跳到另一个 未访问 过的顶点（如果它们直接相连）。
 * 青蛙无法跳回已经访问过的顶点。
 * 如果青蛙可以跳到多个不同顶点，那么它跳到其中任意一个顶点上的机率都相同。
 * 如果青蛙不能跳到任何未访问过的顶点上，那么它每次跳跃都会停留在原地。
 * 无向树的边用数组 edges 描述，其中 edges[i] = [ai, bi] 意味着存在一条直接连通 ai 和 bi 两个顶点的边。
 *
 * 返回青蛙在 t 秒后位于目标顶点 target 上的概率。与实际答案相差不超过 10-5 的结果将被视为正确答案。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：n = 7, edges = [[1,2],[1,3],[1,7],[2,4],[2,6],[3,5]], t = 2, target = 4
 * 输出：0.16666666666666666
 * 解释：上图显示了青蛙的跳跃路径。青蛙从顶点 1 起跳，第 1 秒 有 1/3 的概率跳到顶点 2 ，然后第 2 秒 有 1/2 的概率跳到顶点 4，因此青蛙在 2 秒后位于顶点 4 的概率是 1/3 * 1/2 = 1/6 = 0.16666666666666666 。
 * 示例 2：
 *
 *
 *
 * 输入：n = 7, edges = [[1,2],[1,3],[1,7],[2,4],[2,6],[3,5]], t = 1, target = 7
 * 输出：0.3333333333333333
 * 解释：上图显示了青蛙的跳跃路径。青蛙从顶点 1 起跳，有 1/3 = 0.3333333333333333 的概率能够 1 秒 后跳到顶点 7 。
 *
 *
 *
 *
 * 提示：
 *
 * 1 <= n <= 100
 * edges.length == n - 1
 * edges[i].length == 2
 * 1 <= ai, bi <= n
 * 1 <= t <= 50
 * 1 <= target <= n
 * 通过次数15,102提交次数36,716
 */
public class _1377_FrogPositionAfterTSeconds {

    /**
     * 方法：bfs
     *
     * 根据题意，青蛙只能一路向下，不能回头。如果青蛙跳到了一棵不包含 target 的子树，它就无法到达 target。
     *
     * 如果当前节点有 c 个儿子，青蛙跳到其中一个节点的概率是 1/c 。
     *
     * 看示例 1，第一步有 1/3 的概率跳到节点 2，第二步有 1/2 的概率跳到节点 4。
     * 所以 t=2 秒后位于 target=4 的概率是 1/3 * 1/2 = 1/6。
     *
     * 我们先根据题目给出的无向树的边，建立一个邻接表 g，其中 g[u] 表示顶点 u 的所有相邻顶点。
     *
     * 然后，我们定义以下数据结构：队列 q，用于存储每一轮搜索的顶点及其概率，
     *
     * 初始时 q=[(1,1.0)]，表示青蛙在顶点 1 的概率为 1.0；
     * 数组 vis，用于记录每个顶点是否被访问过，初始时 vis[1]=true，其余元素均为 false。
     *
     * 接下来，我们开始进行广度优先搜索。
     *
     * 在每一轮搜索中，我们每次取出队首元素 (u,p)，其中 u 和 p 分别表示当前顶点及其概率。
     *
     * 当前顶点 u 的相邻顶点中未被访问过的顶点的个数记为 cnt。
     *
     * 如果 u=target，说明青蛙已经到达目标顶点，此时我们判断
     *      青蛙是否是在 第 t 秒到达目标顶点，或者不到 t 秒到达目标顶点但是无法再跳跃到其它顶点（即 t=0 或者 cnt=0）。
     *      如果是，则返回 p，
     *      否则，说明 还有别的邻接点可以访问，必须前进，之后又不能回退，那么 只能 返回 0。
     * 如果 u != target，那么我们将概率 p 均分给 u 的所有未被访问过的相邻顶点，然后将这些顶点加入队列 q 中，并且将这些顶点标记为已访问。
     *
     * 在 bfs时，不计算从 0 开始增加到 当前层节点所用的时间，而是从 t 开始减小到 0，
     * 这样代码中只需 t 和 0 比较，从而减少一个 BFS 之外变量的引入
     *
     * 在一轮搜索结束后，我们将 t 减少 1，然后继续进行下一轮搜索，直到队列为空或者 t<0。
     *
     * 当 t==0 时仍然要进入bfs，是因为 当t==0时，队列中的节点就是t=1时加进去的，也是有效的。
     *
     * 最后，若青蛙仍然没有到达目标顶点，那么我们返回 0。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/frog-position-after-t-seconds/solution/python3javacgo-yi-ti-yi-jie-bfsqing-xi-t-nl1i/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param n
     * @param edges
     * @param t
     * @param target
     * @return
     */
    public double frogPosition(int n, int[][] edges, int t, int target) {
        // 建立邻接表，节点编号从1开始
        List<Integer>[] g = new List[n + 1];
        Arrays.setAll(g, k -> new ArrayList<>());
        for (int[] edge : edges) {
            int f = edge[0], to = edge[1];
            g[f].add(to);
            g[to].add(f);
        }
        // bfs
        Set<Integer> visited = new HashSet<>();
        // 队列元素，节点编号，到达此节点的概率
        Deque<Pair<Integer, Double>> q = new ArrayDeque<>();
        // 初始节点，编号1，概率1
        q.offer(new Pair<>(1, 1.0));
        // 标记已访问
        visited.add(1);
        // bfs，退出条件 q 为空，或 t < 0
        while (!q.isEmpty() && t >= 0) {
            // 层序遍历
            int sz = q.size();
            // 当前层节点
            for (int i = 0; i < sz; ++i) {
                Pair<Integer, Double> cur = q.poll();
                // 位置
                int x = cur.getKey();
                // 概率
                double p = cur.getValue();
                // 此节点所有还未被访问过的邻接点的个数
                long cnt = g[x].stream().filter(v -> !visited.contains(v)).count();
                // 如果到达了目标位置
                if (x == target) {
                    // 如果 t==0，说明恰好到达，那么返回 此节点的概率
                    // 如果 t >0 & cnt==0，说明 提前到达目标位置，且其他邻接点都访问过了，只能原地不动，那么也返回此时的概率
                    // 其他情况，还有临接点没访问，还需要前进，之后不能回退，只能返回 0 了
                    return t * cnt == 0 ? p : 0;
                }
                // 没到达目标位置
                for (Integer v : g[x]) {
                    // 遍历还没有被访问的邻接节点
                    if (!visited.contains(v)) {
                        // 加入队列，将概率 p 均分
                        q.offer(new Pair<>(v, p / cnt));
                        // 标记这些节点访问过
                        visited.add(v);
                    }
                }
            }
            // 层序遍历，消耗时间+1，剩余时间-1
            t--;
        }
        // 其他，返回0
        return 0;
    }

    public static void main(String[] args) {
        _1377_FrogPositionAfterTSeconds obj = new _1377_FrogPositionAfterTSeconds();
        System.out.println(obj.frogPosition(7, new int[][]{{1,2}, {1,3}, {1,7}, {2,4}, {2,6}, {3,5}}, 2, 4));
    }
}
