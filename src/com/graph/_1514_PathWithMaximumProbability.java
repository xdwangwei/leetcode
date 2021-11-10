package com.graph;

import java.util.*;

/**
 * @author wangwei
 * @version 1.0.0
 * @date 2021/9/23 19:34
 * @description: 给你一个由 n 个节点（下标从 0 开始）组成的无向加权图，该图由一个描述边的列表组成，其中 edges[i] = [a, b] 表示连接节点 a 和 b 的一条无向边，且该边遍历成功的概率为 succProb[i] 。
 *
 * 指定两个节点分别作为起点 start 和终点 end ，请你找出从起点到终点成功概率最大的路径，并返回其成功概率。
 *
 * 如果不存在从 start 到 end 的路径，请 返回 0 。只要答案与标准答案的误差不超过 1e-5 ，就会被视作正确答案。
 *
 *  
 *
 * 示例 1：
 *
 *
 *
 * 输入：n = 3, edges = [[0,1],[1,2],[0,2]], succProb = [0.5,0.5,0.2], start = 0, end = 2
 * 输出：0.25000
 * 解释：从起点到终点有两条路径，其中一条的成功概率为 0.2 ，而另一条为 0.5 * 0.5 = 0.25
 * 示例 2：
 *
 *
 *
 * 输入：n = 3, edges = [[0,1],[1,2],[0,2]], succProb = [0.5,0.5,0.3], start = 0, end = 2
 * 输出：0.30000
 * 示例 3：
 *
 *
 *
 * 输入：n = 3, edges = [[0,1]], succProb = [0.5], start = 0, end = 2
 * 输出：0.00000
 * 解释：节点 0 和 节点 2 之间不存在路径
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/path-with-maximum-probability
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1514_PathWithMaximumProbability {


    /**
     * Dijkstra 算法的【前提】，[加权有向图，没有负权重边，求最短路径]
     *
     * 无向图本质上可以认为是「双向图」，从而转化成有向图。
     *
     * 重点说说最大值和最小值这个问题，其实 Dijkstra 和很多最优化算法一样，计算的是「最优值」，这个最优值可能是最大值，也可能是最小值。
     *
     * 标准 Dijkstra 算法是计算最短路径的，但你有想过为什么 Dijkstra 算法不允许存在负权重边么？
     *
     * 因为 Dijkstra 计算最短路径的正确性依赖一个【前提】：路径中每增加一条边，路径的总权重就会增加。
     *
     * 这个前提的数学证明大家有兴趣可以自己搜索一下，我这里只说结论，其实你把这个结论【反过来】也是 OK 的：
     *
     * 想计算[最长]路径，路径中每[增加]一条边，路径的总权重就会[减少]，要是能够满足这个条件，也可以用 Dijkstra 算法。
     *
     * 你看这道题是不是符合这个条件？边和边之间是乘法关系，每条边的概率都是小于 1 的，所以肯定会越乘越小。
     *
     * 只不过，这道题的解法要把优先级队列的排序顺序反过来，一些 if 大小判断也要反过来.
     * 标准算法是，每次从未确定点的集合中选择距离起点最近的那个，现在改为选择 最远那个
     *            更新dist时，前提是dist[j]+g[j][k] < dist[k]，现在反过来 dist[j] * g[j][k] > dist[k]
     * @param n
     * @param edges
     * @param succProb
     * @param start
     * @param end
     * @return
     */
    public double maxProbability(int n, int[][] edges, double[] succProb, int start, int end) {
        List<double[]>[] graph = new ArrayList[n];
        // 初始化
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        // 建立邻接表
        for (int i = 0; i < edges.length; i++) {
            int from = edges[i][0];
            int to = edges[i][1];
            double prob = succProb[i];
            // 注意这里是双向
            graph[from].add(new double[]{to, prob});
            graph[to].add(new double[]{from, prob});
        }
        return dijkstra(graph, start, end);
    }

    /**
     * 记录Dijkstra算法求最短路径过程中，遇到的点的转态
     */
    class PointState {
        // 点id
        int id;
        // 当前路径，从起点到它的可能性
        double probFromStart;

        public PointState(final int id, final double probFromStart) {
            this.id = id;
            this.probFromStart = probFromStart;
        }
    }

    private double dijkstra(List<double[]>[] graph, int start, int end) {
        // 初始化最短路径(最大概率)
        double[] maxProb = new double[graph.length];
        Arrays.fill(maxProb, -1);
        // 从起点到起点 1
        maxProb[start] = 1.0;
        // 注意优先队列的排序规则要从大到小,和原算法[相反]
        PriorityQueue<PointState> queue = new PriorityQueue<>((o1, o2) -> Double.compare(o2.probFromStart, o1.probFromStart));
        // 初始点
        queue.offer(new PointState(start, 1.0));
        while (!queue.isEmpty()) {
            PointState curNode = queue.poll();
            int curId = curNode.id;
            double probFromStart = curNode.probFromStart;
            // 当前点就是重点
            if (curId == end) {
                return probFromStart;
            }
            // 之前某个路径到达这个点的概率更大，此条路径作废，这里的判断条件也是和原始算法[相反]的
            if (probFromStart < maxProb[curId]) {
                continue;
            }
            // 遍历邻接点
            for (double[] neighbor : graph[curId]) {
                int nId = (int) neighbor[0];
                double probToNeighbor = neighbor[1];
                // 选择性更新，注意判断条件和原始算法[相反]
                if (probFromStart * probToNeighbor > maxProb[nId]) {
                    maxProb[nId] = probFromStart * probToNeighbor;
                    queue.offer(new PointState(nId, maxProb[nId]));
                }
            }
        }
        // 不存在
        return 0.0;
    }
}
