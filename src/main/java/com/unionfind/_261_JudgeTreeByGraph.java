package com.unionfind;

import com.common.UnionFind;

/**
 * @author wangwei
 * 2021/11/22 15:50
 *
 * 给你输入编号从 0 到 n - 1 的 n 个结点，和一个无向边列表 edges（每条边用节点二元组表示），请你判断输入的这些边组成的结构是否是一棵树。
 */
public class _261_JudgeTreeByGraph {

    /**
     * 并查集
     * 存在环。就不是树
     * @param n
     * @param edges
     * @return
     */
    boolean validTree(int n, int[][] edges) {
        // 初始化 0...n-1 共 n 个节点
        UnionFind uf = new UnionFind(n);
        // 遍历所有边，将组成边的两个节点进行连接
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            // 若两个节点已经在同一连通分量中，会产生环
            if (uf.connected(u, v)) {
                return false;
            }
            // 这条边不会产生环，可以是树的一部分
            uf.union(u, v);
        }
        // 要保证最后只形成了一棵树，即只有一个连通分量
        return uf.count() == 1;
    }
}
