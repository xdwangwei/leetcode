package com.unionfind;

import com.common.UnionFind;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author wangwei
 * 2021/11/22 15:57
 * <p>
 * 想象一下你是个城市基建规划者，地图上有 N 座城市，它们按以 1 到 N 的次序编号。
 * <p>
 * 给你一些可连接的选项 conections，其中每个选项 conections[i] = [city1, city2, cost] 表示将城市 city1 和城市 city2 连接所要的成本。（连接是双向的，也就是说城市 city1 和城市 city2 相连也同样意味着城市 city2 和城市 city1 相连）。
 * <p>
 * 返回使得每对城市间都存在将它们连接在一起的连通路径（可能长度为 1 的）最小成本。该最小成本应该是所用全部连接代价的综合。如果根据已知条件无法完成该项任务，则请你返回 -1。
 * <p>
 * 输入：N = 3, conections = [[1,2,5],[1,3,6],[2,3,1]]
 * 输出：6
 * 解释：
 * 选出任意 2 条边都可以连接所有城市，我们从中选取成本最小的 2 条。
 * 1
 * 2
 * 3
 * 4
 * 示例 2：
 * <p>
 * <p>
 * 输入：N = 4, conections = [[1,2,3],[3,4,4]]
 * 输出：-1
 * 解释：
 * 即使连通所有的边，也无法连接所有城市。
 */
public class _1135_ConnectAllCitiesWithLowestCost {

    /**
     * 克鲁斯卡尔算法，将所有边按权重排序，每次选择权重最小的那条边，如果当前边加入图中会产生环则跳过，最终得到的连通分量要包含所有节点
     * 并查集来判断是否有环，连通分量是否包含了全部节点
     *
     * @param n
     * @param connections
     * @return
     */
    int minimumCost(int n, int[][] connections) {
        UnionFind uf = new UnionFind(n + 1);
        // 城市编号为 1...n，所以初始化大小为 n + 1
        // 对所有边按照权重从小到大排序
        Arrays.sort(connections, Comparator.comparingInt(a -> a[2]));
        // 记录最小生成树的权重之和
        int mst = 0;
        for (int[] edge : connections) {
            int u = edge[0];
            int v = edge[1];
            int weight = edge[2];
            // 若这条边会产生环，则不能加入 mst
            if (uf.connected(u, v)) {
                continue;
            }
            // 若这条边不会产生环，则属于最小生成树
            mst += weight;
            uf.union(u, v);
        }
        // 保证所有节点都被连通
        // 按理说 uf.count() == 1 说明所有节点被连通
        // 但因为节点 0 没有被使用，所以 0 会额外占用一个连通分量
        return uf.count() == 2 ? mst : -1;
    }
}
