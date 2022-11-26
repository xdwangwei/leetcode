package com.daily;


import java.net.ConnectException;
import java.net.http.HttpConnectTimeoutException;
import java.util.*;

/**
 * @author wangwei
 * @date 2022/11/26 12:35
 * @description: _882_ReachableNodesInSubdividedGraph
 *
 * 882. 细分图中的可到达节点
 * 给你一个无向图（原始图），图中有 n 个节点，编号从 0 到 n - 1 。你决定将图中的每条边 细分 为一条节点链，每条边之间的新节点数各不相同。
 *
 * 图用由边组成的二维数组 edges 表示，其中 edges[i] = [ui, vi, cnti] 表示原始图中节点 ui 和 vi 之间存在一条边，cnti 是将边 细分 后的新节点总数。注意，cnti == 0 表示边不可细分。
 *
 * 要 细分 边 [ui, vi] ，需要将其替换为 (cnti + 1) 条新边，和 cnti 个新节点。新节点为 x1, x2, ..., xcnti ，新边为 [ui, x1], [x1, x2], [x2, x3], ..., [xcnti+1, xcnti], [xcnti, vi] 。
 *
 * 现在得到一个 新的细分图 ，请你计算从节点 0 出发，可以到达多少个节点？如果节点间距离是 maxMoves 或更少，则视为 可以到达 。
 *
 * 给你原始图和 maxMoves ，返回 新的细分图中从节点 0 出发 可到达的节点数 。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：edges = [[0,1,10],[0,2,1],[1,2,2]], maxMoves = 6, n = 3
 * 输出：13
 * 解释：边的细分情况如上图所示。
 * 可以到达的节点已经用黄色标注出来。
 * 示例 2：
 *
 * 输入：edges = [[0,1,4],[1,2,6],[0,2,8],[1,3,1]], maxMoves = 10, n = 4
 * 输出：23
 * 示例 3：
 *
 * 输入：edges = [[1,2,4],[1,4,5],[1,3,1],[2,3,4],[3,4,5]], maxMoves = 17, n = 5
 * 输出：1
 * 解释：节点 0 与图的其余部分没有连通，所以只有节点 0 可以到达。
 *
 *
 * 提示：
 *
 * 0 <= edges.length <= min(n * (n - 1) / 2, 104)
 * edges[i].length == 3
 * 0 <= ui < vi < n
 * 图中 不存在平行边
 * 0 <= cnti <= 104
 * 0 <= maxMoves <= 109
 * 1 <= n <= 3000
 * 通过次数6,616提交次数11,149
 */
public class _882_ReachableNodesInSubdividedGraph {


    /**
     * 方法一：Dijkstra 算法
     * 思路
     *
     * 当图中只存在原始节点而不存在细分节点时，此题可以用 Dijkstra 算法解决：
     * 将输入的 edges 转换成邻接表 adList，维护一个小顶堆 pq 可以依次计算出图中的起点到各个点最短路径，从而计算出可到达节点。
     * pq 中的元素为节点编号以及起点到该节点的路径长度，并以路径长度为比较元素。
     * 每次取出未访问过的节点中的路径最短的节点，并访问其邻接点，若路径长度仍小于等于 maxMoves 且未访问过，
     * 可将其放入 pq，直至 pq 为空或 pq 最短路径大于 maxMoves。
     *
     * 随后考虑如何统计答案（可达点的数量），根据统计点的类型分情况讨论：
     *
     * 对于原点：若有 dist[x]<max 的话，说明原点 x 可达，累加到答案中，这个过程可以在dijkstra过程中完成；
     *
     * 对于细分点：由于所有的细分点都在原图边上，因此我们可以统计所有原图边上有多少细分点可达。
     *
     * 对于任意一条边 e(u,v) 而言，该边上可达点数量包含「经过原点 u 可达」以及「经过原点 v 可达」的交集，
     * 其中原点 0 到达原点 u 以及原点 v 的距离，我们是已知的。
     * 因此经过原点 u 可达的数量为 max(0,max−dist[u])，经过原点 v 可达的数量为 max(0,max−dist[v])，
     * 两者之和与该边上细分点的总数取 min 即是这条边可达点的数量。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/reachable-nodes-in-subdivided-graph/solution/by-ac_oier-yrhg/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param edges
     * @param maxMoves
     * @param n
     * @return
     */
    public int reachableNodes(int[][] edges, int maxMoves, int n) {
        List<int[]>[] graph = new List[n];
        // 构建邻接表
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        // 无向图
        // a、b之间插入n个点，相当于a、b间的权重（距离）变为 n + 1
        for (int[] edge : edges) {
            int f = edge[0], t = edge[1];
            graph[f].add(new int[]{t, edge[2] + 1});
            graph[t].add(new int[]{f, edge[2] + 1});
        }
        // 下面这种dijkstra写法不需要额外visited数组（因为入队限制，及出队后距离判断，保证了队列元素不会无限增加，节点也不会重复松弛）
        // 优先队列，
        // 元素 [idx, dis] 代表起点0到点idx的最短路径长度（到目前为止，不是最终）
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(o -> o[1]));
        // 起点0到每个点的最短距离（最终）
        int[] dist = new int[n];
        // 初始化为有效范围的数字
        Arrays.fill(dist, maxMoves + 1);
        // base ，0到0距离是0
        dist[0] = 0;
        // base，队列加入初始元素
        pq.offer(new int[]{0, 0});
        // 最终，起点0能够到达多少节点
        // dijkstra过程只统计原始图中起点能到达多少点
        int ans = 0;
        while (!pq.isEmpty()) {
            // 从已访问节点中取出距离起点最近的点
            int[] poll = pq.poll();
            int idx = poll[0], dis = poll[1];
            // 最近距离已经超过阈值，那么dijkstra过程可以提前结束（后续过程不可能使得某个点到起点的距离<maxMoves）
            if (dis > maxMoves) {
                break;
            }
            // 这个点加入到队列后，到它出队的过程中，在它之前出队的某个点更新了起点到它的最短距离
            // 那么在它出队之前肯定队列中已经有个和它同idx但dis比它小的元素出队，进行了松弛过程
            // 所以，这里不需要也不应该再以他为中心点去更新起点到别的点的最短路径
            if (dis > dist[idx]) {
                continue;
            }
            // 以idx为跳点进行松弛过程，能进行到这里，就是0点到idx的最短距离肯定<maxMoves，上面的判断保证了以下过程不会重复进行
            ans++;
            // 遍历邻接点
            for (int[] neigh : graph[idx]) {
                int v = neigh[0], d = neigh[1];
                // 以idx为跳点，能使得起点到v点的距离更短
                if (dis + d < dist[v]) {
                    // 更新最短距离
                    dist[v] = dis + d;
                    // 加入队列
                    pq.offer(new int[]{v, dis + d});
                }
            }
        }
        // 在原图的基础上，遍历每条边，统计起点在此边上能够到达的中间点数量
        for (int[] edge : edges) {
            int f = edge[0], t = edge[1];
            // 分左右部分
            int nodes = Math.max(maxMoves - dist[f], 0) + Math.max(maxMoves - dist[t], 0);
            // 合起来不能超过此边上的节点数目
            ans += Math.min(nodes, edge[2]);
        }
        return ans;
    }

    public static void main(String[] args) {
        _882_ReachableNodesInSubdividedGraph obj = new _882_ReachableNodesInSubdividedGraph();

        //[[1,2,5],[0,3,3],[1,3,2],[2,3,4],[0,4,1]]
        //        7
        //        5
        obj.reachableNodes(new int[][]{{1,2,5},{0,3,3},{1,3,2},{2,3,4},{0,4,1}}, 7, 5);
        obj.reachableNodes(new int[][]{{2,4,2},{3,4,5},{2,3,1},{0,2,1},{0,3,5}}, 14, 5);
    }
}
