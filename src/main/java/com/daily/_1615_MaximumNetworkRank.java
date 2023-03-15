package com.daily;

/**
 * @author wangwei
 * @date 2023/3/15 19:05
 * @description: _1615_MaximumNetworkRank
 *
 * 1615. 最大网络秩
 * n 座城市和一些连接这些城市的道路 roads 共同组成一个基础设施网络。每个 roads[i] = [ai, bi] 都表示在城市 ai 和 bi 之间有一条双向道路。
 *
 * 两座不同城市构成的 城市对 的 网络秩 定义为：与这两座城市 直接 相连的道路总数。如果存在一条道路直接连接这两座城市，则这条道路只计算 一次 。
 *
 * 整个基础设施网络的 最大网络秩 是所有不同城市对中的 最大网络秩 。
 *
 * 给你整数 n 和数组 roads，返回整个基础设施网络的 最大网络秩 。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：n = 4, roads = [[0,1],[0,3],[1,2],[1,3]]
 * 输出：4
 * 解释：城市 0 和 1 的网络秩是 4，因为共有 4 条道路与城市 0 或 1 相连。位于 0 和 1 之间的道路只计算一次。
 * 示例 2：
 *
 *
 *
 * 输入：n = 5, roads = [[0,1],[0,3],[1,2],[1,3],[2,3],[2,4]]
 * 输出：5
 * 解释：共有 5 条道路与城市 1 或 2 相连。
 * 示例 3：
 *
 * 输入：n = 8, roads = [[0,1],[1,2],[2,3],[2,4],[5,6],[5,7]]
 * 输出：5
 * 解释：2 和 5 的网络秩为 5，注意并非所有的城市都需要连接起来。
 *
 *
 * 提示：
 *
 * 2 <= n <= 100
 * 0 <= roads.length <= n * (n - 1) / 2
 * roads[i].length == 2
 * 0 <= ai, bi <= n-1
 * ai != bi
 * 每对城市之间 最多只有一条 道路相连
 * 通过次数25,092提交次数41,662
 */
public class _1615_MaximumNetworkRank {

    /**
     *
     * 方法：计数 + 枚举
     *
     * 数据规模，点数不超过100，
     *
     * 根据题意 两个点之间的秩 = a 的度 + b 的度 - （a、b相连？1 ： 0）
     *
     * 网络的秩 = 网络中任意两点间的秩的最大值
     *
     * 我们可以用一维数组 degreee 记录每个城市的度，用二维数组 g 记录每对城市之间是否有道路相连，
     * 如果城市 a 和城市 b 之间有道路相连，则 g[a][b]=g[b][a]=1，否则 g[a][b]=g[b][a]=0。
     *
     * 接下来，我们枚举每对城市 (a,b)，其中 a<b，计算它们的网络秩，
     * 即 cnt[a]+cnt[b]−(g[a][b] ? 1 : 0)，
     * 取其中的最大值即为答案。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/maximal-network-rank/solution/python3javacgotypescript-yi-ti-yi-jie-ji-lzcr/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param roads
     * @return
     */
    public int maximalNetworkRank(int n, int[][] roads) {
        // g[i][j]为1代表i、j之间相连
        boolean[][] g = new boolean[n][n];
        // 每个点的度
        int[] degree = new int[n];
        for (int[] road : roads) {
            int f = road[0], t = road[1];
            // 更新g和degree
            g[f][t] = g[t][f] = true;
            // 两点的度都加1
            degree[f]++;
            degree[t]++;
        }
        int ans = 0;
        // 遍历，枚举 i、j的网络秩
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                // 取最大值
                ans = Math.max(ans, degree[i] + degree[j] - (g[i][j] ? 1 : 0));
            }
        }
        // 返回
        return ans;
    }
}
