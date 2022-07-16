package com.graph;

import java.util.*;

/**
 * @author wangwei
 * @version 1.0.0
 * @date 2021/9/23 16:12
 * @description: 有 n 个网络节点，标记为1到 n。
 *
 * 给你一个列表times，表示信号经过 有向 边的传递时间。times[i] = (ui, vi, wi)，其中ui是源节点，vi 是目标节点， wi 是一个信号从源节点传递到目标节点的时间。
 *
 * 现在，从某个节点K发出一个信号。需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回 -1 。
 *
 *  
 *
 * 示例 1：
 *
 *
 *
 * 输入：times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
 * 输出：2
 * 示例 2：
 *
 * 输入：times = [[1,2,1]], n = 2, k = 1
 * 输出：1
 * 示例 3：
 *
 * 输入：times = [[1,2,1]], n = 2, k = 2
 * 输出：-1
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/network-delay-time
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _743_NetworkDelayTime {

    /**
     * 从k开始到全部节点的最短路径，选择里面的最大值
     * @param times
     * @param n
     * @param k
     * @return
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        // 构建邻接表
        List<int[]>[] graph = buildGraph(times, n);
        // 求起点到其他点最短路径
        int[] distTo = dijkstra(graph, k);

        int res = 0;
        // 遍历
        for (int i = 1; i <= n; i++) {
            // 起点到点i不可达，
            if (distTo[i] == Integer.MAX_VALUE) {
                return -1;
            }
            // 找到所有最短路径中最长那个
            res = Math.max(res, distTo[i]);
        }
        return res;
    }


    /**
     * Dijsktra算法，计算指定起点到每个点的最短距离
     * 加权图中的 Dijkstra 算法和无权图中的普通 BFS 算法不同，
     * 在 Dijkstra 算法中，你第一次经过某个节点时的路径权重，不见得就是最小的，
     * 所以对于同一个节点，我们可能会经过多次，而且每次(不同路径)的distFromStart可能都不一样，
     *
     * 关于 Dijkstra
     * 其主要思想是贪心。
     *
     * 将所有节点分成两类：已确定从起点到当前点的最短路长度的节点，以及未确定从起点到当前点的最短路长度的节点（下面简称「未确定节点」和「已确定节点」）。
     *
     * 每次从「未确定节点」中取一个与起点距离最短的点，将它归类为「已确定节点」，并用它「更新」从起点到其他所有「未确定节点」的距离。直到所有点都被归类为「已确定节点」。
     *
     * 用节点 AA「更新」节点 BB 的意思是，用起点到节点 AA 的最短路长度加上从节点 AA 到节点 BB 的边的长度，去比较起点到节点 BB 的最短路长度，如果前者小于后者，就用前者更新后者。这种操作也被叫做「松弛」。
     *
     * 这里暗含的信息是：每次选择「未确定节点」时，起点到它的最短路径的长度可以被确定。
     *
     * 可以这样理解，因为我们已经用了每一个「已确定节点」更新过了当前节点，无需再次更新（因为一个点不能多次到达）。而当前节点已经是所有「未确定节点」中与起点距离最短的点，不可能被其它「未确定节点」更新。所以当前节点可以被归类为「已确定节点」。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/network-delay-time/solution/wang-luo-yan-chi-shi-jian-by-leetcode-so-6phc/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param graph
     * @param k
     * @return
     */
    private int[] dijkstra(List<int[]>[] graph, int k) {
        // 起点到每个点的最短路径的距离
        int[] distTo = new int[graph.length];
        Arrays.fill(distTo, Integer.MAX_VALUE);
        // base case , start 到start 距离是 0
        distTo[k] = 0;
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o[1]));
        // 从起点开始遍历，queue中元素是 int[编号，起点到它的距离]，按照distFromStart从小到大排列
        queue.offer(new int[]{k, 0});
        while (!queue.isEmpty()) {
            // 找到当前 distFromStart 最小的那个点
            int[] minDisFromStartNode = queue.poll();
            int nodeIdx = minDisFromStartNode[0];
            int disFromStart = minDisFromStartNode[1];
            // 之前某个路径经过它时，得到的distFromStart值更小，已经将其更新到distTo中
            if (disFromStart > distTo[nodeIdx]) {
                // 所以这条路径作废,
                continue;
            }
            // 对于这个点v的所有邻接点j
            for (int[] neighbor : graph[nodeIdx]) {
                // 看看从 curNode 达到 nextNode 的距离是否会更短
                // neighbor[0] --> j
                // neighbor[1] --> dis[v][j]
                if (disFromStart + neighbor[1] < distTo[neighbor[0]]) {
                    distTo[neighbor[0]] = disFromStart + neighbor[1];
                    // 入队，
                    queue.offer(new int[]{neighbor[0], distTo[neighbor[0]]});
                }
            }
        }
        return distTo;
    }

    /**
     * 一般的Dijkstra算法，这里graph的索引是从0开始的，我们题目中是1，这里只是一般的算法，与题目无关
     * 两组集合：起点到它的最短距离已确定，xxx未确定
     * @param graph 邻接矩阵
     * @param k 起点
     * @return
     */
    private int[] dijkstra2(int[][] graph, int k) {
        // n 个节点
        int n = graph.length;
        // 起点到每个点的最短距离
        int[] dist = new int[n];
        // 因为后面可能涉及权重相加，为了避免移除，这里去一半
        int INF = Integer.MAX_VALUE / 2;
        // 根据邻接矩阵，初始化起点到其他点的最短距离
        for (int j = 0; j < n; j++) {
            dist[j] = graph[k][j];
        }
        // 起点到起点为0
        dist[k] = 0;
        // used数组记录起点到点i的最短距离是否已经确定
        boolean[] used = new boolean[n];
        // 起点到起点已确定，剩下n-1个不确定的点，for循环遍历n-1次
        used[k] = true;
        for (int i = 1; i < n; ++i) {
            // 每次从未确定的点集合中，选择一个到起点距离最小的点
            int x = -1;
            int min = INF;
            for (int y = 0; y < n; ++y) {
                if (!used[y] && dist[y] < min) {
                    min = dist[y];
                    x = y;
                }
            }
            // 一般情况下，这里需要判断x
            if (x == -1) {
                // 说明，从给定的起点到剩下的这些点的距离都是INF，也就是不存在路径，选不出来一个最小值对应的点
                // 此时 ， used[x] = true 会抛出异常 ArrayIndexOutOfBounds
                // 但其实程序在这里就结束了(起点到所有点的最短距离已经计算完了)，所以直接break
                break;
            }
            // 如果能选出一个距离最小的点，标记它为确定的点的集合中的一员
            used[x] = true;
            // 如果只想得到到某个点end的最短距离，在这里加一个判断就行了，其他代码不用改
            // if (x == end) {
            //     return dist[x];
            // }
            // 遍历它所有的邻接点，选择性更新起点到这些点的最短距离
            for (int y = 0; y < n; ++y) {
                if (!used[y]) {
                    dist[y] = Math.min(dist[y], dist[x] + graph[x][y]);
                }
            }
        }
        return dist;
    }



    /**
     * 由给定的输入数组和节点个数，构造出邻接矩阵
     * @param data
     * @param n
     * @return
     */
    private List<int[]>[] buildGraph(int[][] data, int n) {
        // 构建传入的数值是从1开始的
        ArrayList<int[]>[] graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        // 构建邻接矩阵
        for (int i = 0; i < data.length; i++) {
            int from = data[i][0];
            int to = data[i][1];
            int weight = data[i][2];
            graph[from].add(new int[]{to, weight});
        }
        return graph;
    }


    public static void main(String[] args) {
        _743_NetworkDelayTime obj = new _743_NetworkDelayTime();
        // [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
        // int[][] ints = new int[3][3];
        // ints[0] = new int[]{2,1,1};
        // ints[1] = new int[]{2,3,1};
        // ints[2] = new int[]{3,4,1};
        // System.out.println(obj.networkDelayTime(ints, 4, 2));
    }
}
