package com.daily;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangwei
 * @date 2023/3/12 23:12
 * @description: _1617_CountSubtreesWithMaxDistanceBetweenCities
 *
 * 1617. 统计子树中城市之间最大距离
 * 给你 n 个城市，编号为从 1 到 n 。同时给你一个大小为 n-1 的数组 edges ，其中 edges[i] = [ui, vi] 表示城市 ui 和 vi 之间有一条双向边。题目保证任意城市之间只有唯一的一条路径。换句话说，所有城市形成了一棵 树 。
 *
 * 一棵 子树 是城市的一个子集，且子集中任意城市之间可以通过子集中的其他城市和边到达。两个子树被认为不一样的条件是至少有一个城市在其中一棵子树中存在，但在另一棵子树中不存在。
 *
 * 对于 d 从 1 到 n-1 ，请你找到城市间 最大距离 恰好为 d 的所有子树数目。
 *
 * 请你返回一个大小为 n-1 的数组，其中第 d 个元素（下标从 1 开始）是城市间 最大距离 恰好等于 d 的子树数目。
 *
 * 请注意，两个城市间距离定义为它们之间需要经过的边的数目。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：n = 4, edges = [[1,2],[2,3],[2,4]]
 * 输出：[3,4,0]
 * 解释：
 * 子树 {1,2}, {2,3} 和 {2,4} 最大距离都是 1 。
 * 子树 {1,2,3}, {1,2,4}, {2,3,4} 和 {1,2,3,4} 最大距离都为 2 。
 * 不存在城市间最大距离为 3 的子树。
 * 示例 2：
 *
 * 输入：n = 2, edges = [[1,2]]
 * 输出：[1]
 * 示例 3：
 *
 * 输入：n = 3, edges = [[1,2],[2,3]]
 * 输出：[2,1]
 *
 *
 * 提示：
 *
 * 2 <= n <= 15
 * edges.length == n-1
 * edges[i].length == 2
 * 1 <= ui, vi <= n
 * 题目保证 (ui, vi) 所表示的边互不相同。
 * 通过次数10,284提交次数12,922
 */
public class _1617_CountSubtreesWithMaxDistanceBetweenCities {

    /**
     * 方法一：二进制枚举 + BFS 或 DFS
     *
     * 我们注意到 n ≤ 15，因此可以考虑使用二进制枚举的方法枚举所有的子树。
     * 而子树中节点的最大距离，其实就是子树中两个节点之间的最长路径，也即是树的直径，
     *
     * 求解树的直径一般可以使用 DFS 或 BFS，
     * 先找到树直径的一个端点，然后再从该端点出发，找到树的另一个端点，这两个端点之间的路径长度就是树的直径。
     *
     * 接下来，我们详细说明具体的代码实现。
     *
     * 我们先根据数组 edges 构建出邻接表 g[u] 表示节点 u 的所有邻接节点。
     *
     * 用一个二进制数 mask 表示子树，其中  mask 的第 i 位为 1 表示节点 i 在子树中，否则表示节点 i 不在子树中。
     * 每个节点都有两种状态，即在子树中或不在子树中，有 n 个节点，因此一共有 2^n 种状态。
     *
     * 接下来，我们在 [1,..2^n−1] 的范围内枚举子树 mask，对于每个子树：
     *
     * 如果 mask 的二进制表示中只有一个二进制位为 1，即 mask∈[1,2,4,8,⋯,2^(n-1)]，
     * 则跳过该 mask，因为这些 mask 表示的子树只有一个节点，不符合题意；
     *
     * 否则，求直径，我们找到 mask 的二进制表示中最高位的二进制位为 1 的位置，记为 cur。
     * 然后从节点 cur 出发，通过深度优先搜索或者广度优先搜索，找到树直径的一个端点  nxt，
     * 然后我们再从节点 nxt 出发，同样通过深度优先搜索或者广度优先搜索，过程中记录下最大距离 mx。
     * (需要注意的是，枚举的子集不一定是一棵树，可能是森林（多棵树，多个连通块）。
     * 如果从cur出发dfs能访问到mask代表的全部节点，则这是一棵树，否则是森林，不用进行第二步，直接跳过，进形下一次枚举)
     *
     * 当走到最深的节点时，即可得知树的直径。
     * 此时我们更新答案数组 ans，将 ans[mx−1] 的值加 1。注意，这里是 mx−1，因为题目中的最大距离是从 1 开始计数的。
     *
     * 最后，枚举完所有的子树，返回答案数组 ans 即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/count-subtrees-with-max-distance-between-cities/solution/python3javacgotypescript-yi-ti-yi-jie-er-foqs/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param edges
     * @return
     */
    // 图。邻接表
    private List<Integer>[] g;
    // 子树中的最长距离
    private int maxDis;
    // 字数中最长距离对应的端点
    private int nxt;
    // 子树dfs，还没有访问的节点（二进制）
    private int visited;
    public int[] countSubgraphsForEachDiameter(int n, int[][] edges) {
        // 初始化
        g = new List[n];
        Arrays.setAll(g, k -> new ArrayList());
        // 建图
        for (int[] edge : edges) {
            // 节点下标转为从0开始
            int f = edge[0] - 1, t = edge[1] - 1;
            g[f].add(t);
            g[t].add(f);
        }
        // 准备返回值，ans[i] 代表 直径为 i+1 的子树个数
        int[] ans = new int[n - 1];
        // 二进制枚举子树，
        for (int mask = 1; mask < 1 << n; ++mask) {
            // 子树至少要包含超过1个点，即二进制超过1个1
            if ((mask & mask - 1) == 0) {
                continue;
            }
            // 当前枚举状态，子树中寻找一个节点，这里找的是最高位1代表的节点
            int cur = 31 - Integer.numberOfLeadingZeros(mask);
            // 从cur出发进行一次dfs，maxDis记录达到的最大距离，nxt记录对应的另一个端点
            maxDis = 0;
            // visited记录这一次dfs访问了哪些节点(对应二进制位变位0)
            // 初始时所有点都还没有访问
            visited = mask;
            // 从cur出发，目前的最远距离是0
            dfs(cur, 0);
            // dfs结束后，如果visited不为0，说明无法访问全部节点，说明这一次的mask代表的是一个森林，不是树，跳过
            if (visited == 0) {
                // 是树，进行求直径的第二步
                // 以nxt为出发点进行dfs，记录达到的最远距离maxDis
                // 恢复visited和maxDis
                visited = mask;
                maxDis = 0;
                // 从nxt出发进行dfs
                dfs(nxt, 0);
                // 直径为maxDis的子树个数加1，ans下标从0开始
                ans[maxDis - 1]++;
            }
        }
        // 返回
        return ans;
    }

    /**
     * dfs，visited的二进制代表还未访问过的节点
     * @param u 当前遍历到的节点
     * @param dis 当前到达的距离
     */
    private void dfs(int u, int dis) {
        // 将此节点对应二进制位置0，标记已经访问
        visited ^= 1 << u;
        // 更新dfs过程中遇到的最大距离，及对应的端点
        if (dis > maxDis) {
            maxDis = dis;
            nxt = u;
        }
        // dfs邻接点
        for (int v : g[u]) {
            // 只访问未访问过的节点
            if ((visited >> v & 1) == 1) {
                // 这些点距离变长1
                dfs(v, dis + 1);
            }
        }
    }
}
