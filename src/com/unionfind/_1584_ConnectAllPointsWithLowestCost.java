package com.unionfind;

import com.common.UnionFind;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author wangwei
 * 2021/11/22 16:05
 *
 * 给你一个points数组，表示 2D 平面上的一些点，其中points[i] = [xi, yi]。
 *
 * 连接点[xi, yi] 和点[xj, yj]的费用为它们之间的 曼哈顿距离：|xi - xj| + |yi - yj|，其中|val|表示val的绝对值。
 *
 * 请你返回将所有点连接的最小总费用。只有任意两点之间 有且仅有一条简单路径时，才认为所有点都已连接。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：points = [[0,0],[2,2],[3,10],[5,2],[7,0]]
 * 输出：20
 * 解释：
 *
 * 我们可以按照上图所示连接所有点得到最小总费用，总费用为 20 。
 * 注意到任意两个点之间只有唯一一条路径互相到达。
 * 示例 2：
 *
 * 输入：points = [[3,12],[-2,5],[-4,1]]
 * 输出：18
 * 示例 3：
 *
 * 输入：points = [[0,0],[1,1],[1,0],[-1,1]]
 * 输出：4
 * 示例 4：
 *
 * 输入：points = [[-1000000,-1000000],[1000000,1000000]]
 * 输出：4000000
 * 示例 5：
 *
 * 输入：points = [[0,0]]
 * 输出：0
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/min-cost-to-connect-all-points
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1584_ConnectAllPointsWithLowestCost {

    /**
     * 克鲁斯卡尔算法，每次选择离得最近的两个点组成的边加入生成树
     * 并查集判断这个生成树是否包含了所有点，是否还有环
     *
     * 这道题做了一个小的变通：每个坐标点是一个二元组，那么按理说应该用五元组表示一条带权重的边，但这样的话不便执行 Union-Find 算法；所以我们用 points 数组中的索引代表每个坐标点，这样就可以直接复用之前的 Kruskal 算法逻辑了。
     *
     * @param points
     * @return
     */
    public int minCostConnectPoints(int[][] points) {
        // 让边，以权重从小到大排序
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o[2]));
        // 生成所有边，计算权重
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                int ix = points[i][0], iy = points[i][1];
                int jx = points[j][0], jy = points[j][1];
                int dist = Math.abs(ix - jx) + Math.abs(iy - jy);
                // 用坐标点在 points 中的索引表示坐标点
                queue.offer(new int[]{i, j, dist});
            }
        }
        int res = 0;
        // kruskal算法
        UnionFind uf = new UnionFind(points.length);
        while (!queue.isEmpty()) {
            int[] edge = queue.poll();
            int i = edge[0], j = edge[1], dist = edge[2];
            // 若这条边会产生环，则不能加入 mst
            if (uf.connected(i, j)) {
                continue;
            }
            // 若这条边不会产生环，则属于最小生成树
            uf.union(i, j);
            res += dist;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(new _1584_ConnectAllPointsWithLowestCost().minCostConnectPoints(new int[][]{{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}}));
    }
}
