package com.daily;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author wangwei
 * @date 2022/12/14 9:42
 * @description: _1697_CheckingExistenceOfEdgeLengthLimitedPaths
 *
 * 1697. 检查边长度限制的路径是否存在
 * 给你一个 n 个点组成的无向图边集 edgeList ，其中 edgeList[i] = [ui, vi, disi] 表示点 ui 和点 vi 之间有一条长度为 disi 的边。请注意，两个点之间可能有 超过一条边 。
 *
 * 给你一个查询数组queries ，其中 queries[j] = [pj, qj, limitj] ，你的任务是对于每个查询 queries[j] ，判断是否存在从 pj 到 qj 的路径，且这条路径上的每一条边都 严格小于 limitj 。
 *
 * 请你返回一个 布尔数组 answer ，其中 answer.length == queries.length ，当 queries[j] 的查询结果为 true 时， answer 第 j 个值为 true ，否则为 false 。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：n = 3, edgeList = [[0,1,2],[1,2,4],[2,0,8],[1,0,16]], queries = [[0,1,2],[0,2,5]]
 * 输出：[false,true]
 * 解释：上图为给定的输入数据。注意到 0 和 1 之间有两条重边，分别为 2 和 16 。
 * 对于第一个查询，0 和 1 之间没有小于 2 的边，所以我们返回 false 。
 * 对于第二个查询，有一条路径（0 -> 1 -> 2）两条边都小于 5 ，所以这个查询我们返回 true 。
 * 示例 2：
 *
 *
 * 输入：n = 5, edgeList = [[0,1,10],[1,2,5],[2,3,9],[3,4,13]], queries = [[0,4,14],[1,4,13]]
 * 输出：[true,false]
 * 解释：上图为给定数据。
 *
 *
 * 提示：
 *
 * 2 <= n <= 105
 * 1 <= edgeList.length, queries.length <= 105
 * edgeList[i].length == 3
 * queries[j].length == 3
 * 0 <= ui, vi, pj, qj <= n - 1
 * ui != vi
 * pj != qj
 * 1 <= disi, limitj <= 109
 * 两个点之间可能有 多条 边。
 * 通过次数4,728提交次数7,850
 */
public class _1697_CheckingExistenceOfEdgeLengthLimitedPaths {


    /**
     * 方法一：离线查询 + 并查集
     *
     * 本题两点之间可能有多条边，并且不求最短路径，只是判断两点间是否存在路径（能够抵达），因此和dijkstra没有关系
     * 最终目的是判断两点之间是否存在路径，采用并查集方式，转换为 当这些边加入后，判断两点是否联通即可。
     *
     * 对于一个查询，p，q，limit，我们可以去遍历edges中所有边，取出所有满足小于长度<limit的边，在并查集中将这些边的两个端点联通
     * 最后判断 p 和 q 是否联通即可。
     *
     * 但如果对于每个查询我们都去遍历edges，时间复杂度太高，并且，每次查询后还得恢复并查集状态，代价太大
     *
     * 因此我们对此过程进行优化：
     *      如果我们让 queries 的 limit 是非递减的，那么上一次查询的并查集里的边都是满足当前查询的 limit 要求的，
     *      不用恢复并查集，我们只需要再次遍历edges中剩下的满足当前limit的边，进行同样操作即可。
     *      由于题目已经给出了所有的queries情况，我们是可以通过对queries根据limit排序实现这个目标的。
     *      当然了，最后的结果需要按照原始queries的顺序，因此创建索引数组，采用索引排序
     *
     *      同理，为了让遍历edges边的过程更快，我们初始时对edges的边按照length部分排序。
     *      这样的话，假如满足上一次limit的边是[...k]，那么对于当前limit，只需要从edges[k+1]进行补充查找即可，直到不满足limit
     *
     * 综上：
     *
     * 我们首先将 edgeList 按边长度从小到大进行排序，
     * 然后将 queries 按 limit 从小到大进行排序，
     * 使用 k 指向上一次查询中不满足 limit 要求的长度最小的边，显然初始时 k=0。
     *
     * 依次遍历 queries：如果 k 指向的边的长度小于对应查询的 limit，则将该边加入并查集中，然后将 k 加 1，直到 k 指向的边不满足要求；
     * 最后根据并查集查询对应的 p 和 q 是否属于同一集合来保存查询的结果。
     *
     * 如果 p 和 q 属于同一个集合，则说明存在从 p 到 q 的路径，且这条路径上的每一条边的长度都严格小于 limit，查询返回 true，否则查询返回 false。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/checking-existence-of-edge-length-limited-paths/solution/jian-cha-bian-chang-du-xian-zhi-de-lu-ji-cdr5/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param edgeList
     * @param queries
     * @return
     */
    public boolean[] distanceLimitedPathsExist(int n, int[][] edgeList, int[][] queries) {
        // 对所有edges按照长度部分排序
        Arrays.sort(edgeList, Comparator.comparingInt(o -> o[2]));
        // queries查询的索引数组
        Integer[] idx = new Integer[queries.length];
        // 索引数组排序，按照对应的queries的limit字段排序
        // 排序完后 queries[idx[?]] 是按照limit字段升序的
        Arrays.sort(idx, Comparator.comparingInt(o -> queries[o][2]));
        // 并查集初始化
        int[] uf = new int[n];
        for (int i = 0; i < n; ++i) {
            uf[i] = i;
        }
        // 保存结果
        boolean[] res = new boolean[queries.length];
        // k标记edges中最后一个满足上一个query的limit限制的边的索引
        int k = 0;
        for (int i = 0; i < idx.length; ++i) {
            // 待查询的两个点，和limit，注意这里是用idx[i]去queries中取，而不是i本身，
            int qu = queries[idx[i]][0], qv = queries[idx[i]][0], limit = queries[idx[i]][0];
            // 由于edges已经有序，因此从上一次结束的位置继续遍历edge，查找剩下的的满足当前limit限制的边
            while (k < edgeList.length && edgeList[k][2] < limit) {
                // 联通这条边的两端
                union(uf, edgeList[k][0], edgeList[k][1]);
                // k递增
                k++;
            }
            // 满足limit的边的两点都联通后，判断待查询的两个点是否联通
            // 对应的是 queries[idx[i]] 的答案。不是 queries[i] 的答案
            res[idx[i]] = find(uf, qu) == find(uf, qv);
        }
        // 返回
        return res;
    }

    // 并查集，带路径压缩的寻找祖宗节点方法
    private int find(int[] uf, int x) {
        while (x != uf[x]) {
            uf[x] = uf[uf[x]];
            x = uf[x];
        }
        return x;
    }

    // 并查集，联通 u 和 v
    private void union(int[] uf, int u, int v) {
        int p = find(uf, u);
        int q = find(uf, v);
        if (p == q) {
            return;
        }
        uf[p] = q;
    }
}
