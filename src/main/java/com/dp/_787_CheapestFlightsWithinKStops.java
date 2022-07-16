package com.dp;

import java.util.*;

/**
 * @author wangwei
 * 2021/12/14 9:31
 *
 * 有 n 个城市通过一些航班连接。给你一个数组flights ，其中flights[i] = [fromi, toi, pricei] ，表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
 *
 * 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，你的任务是找到出一条最多经过 k站中转的路线，使得从 src 到 dst 的 价格最便宜 ，并返回该价格。 如果不存在这样的路线，则输出 -1。
 *
 *
 *
 * 示例 1：
 *
 * 输入:
 * n = 3, edges = [[0,1,100],[1,2,100],[0,2,500]]
 * src = 0, dst = 2, k = 1
 * 输出: 200
 * 解释:
 * 城市航班图如下
 *
 *
 * 从城市 0 到城市 2 在 1 站中转以内的最便宜价格是 200，如图中红色所示。
 * 示例 2：
 *
 * 输入:
 * n = 3, edges = [[0,1,100],[1,2,100],[0,2,500]]
 * src = 0, dst = 2, k = 0
 * 输出: 500
 * 解释:
 * 城市航班图如下
 *
 *
 * 从城市 0 到城市 2 在 0 站中转以内的最便宜价格是 500，如图中蓝色所示。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/cheapest-flights-within-k-stops
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _787_CheapestFlightsWithinKStops {

    /**
     * Dijkstra
     *
     * 不同的是，普通最短路径，只需要代价最小。
     * 此题，加入了额外的限制，也就是路径数，比如 a -> b -> c 本来 比 a -> c的代价更小，但是题目限制 a c 之间最多有1条路径，那么 我们只能选择 a -> c
     * 所以，对于这个题，缩放时，对于 代价更小，或者 路径更短，都要进行缩放。
     *  因为代价更小是最终目的，但 对于中间过程，某个阶段的代价最小 ，并不能保证后续的路径数 也少。
     *  同样地，不能只比较路径数更少，还是上面那个例子，假如限制数变成2，那么只考虑路径小，就会返回错误答案。
     *
     *  那么，是否要同时满足 路径小 和 代价小，不可以！
     *      你能保证每次从剩下的点中找到一个距离起点代价最小并且路径数最少的点吗？实际上是不能同时满足的。
     *      如果可以，那两个条件合并不相当于 没有路径限制的 最短路径算法了？
     *      就算可以，与之前同样，因为有两个条件互相约束。除非你每次都能找到这样的点，否则你就不能保证当此选择的点的后续也能同时让两个条件都达到。
     *
     *  所以，对于  代价更小，或者 路径更短，都能 得到一个新的状态点，加入集合
     *  还是使用优先队列替我们简化筛选过程，排序原则还是按照代价最小，这是我们的最终目的嘛。而且，这样，如果下一个出队列的点就是目标点，我们就可以直接返回了，队列可能有多个状态点指向的是目标点，但是因为是按代价排序的，这几个都可以说是符合路径限制的，自然返回第一个
     *  假如集合里面有两个状态点，指向的是同一个图中点，只是一个是因为代价小而更新进来，一个是因为路径小而更新进来。
     *  那么首先 代价小的那个状态点排在前面，下一次它出队列，如果用它更新其他点时 无法做到代价最小或者 路径更少，自然会被丢弃。
     *  这样，集合最终能够空。
     *
     *  优先队列中的状态元素，int[]{点id，起点到它的最小代价，起点到它的最少路径}
     *  int[] priceTo, 最终：起点到每个点的最小代价
     *  int[] edgesTo, 最终：起点到每个点的最小路径数，限制 + 目标
     * @param n
     * @param flights
     * @param src
     * @param dst
     * @param k
     * @return
     */
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // 记录每个点的邻接点，和这个点和它邻接点的代价
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            adj.add(new ArrayList<>());
        }
        for (int[] flight : flights) {
            int from = flight[0];
            int to = flight[1];
            int price = flight[2];
            // 记录每个点的邻接点，和这个点和它邻接点的代价
            adj.get(from).add(new int[]{to, price});
        }
        // 题目是，最多经过 k 个中转，也就是 src 到 dst 最多有 k + 1 条边
        k++;
        int[] priceTo = new int[n];
        int[] edgesTo = new int[n];
        // 初始化 代价数组 和 路径数组·
        Arrays.fill(priceTo, Integer.MAX_VALUE);
        Arrays.fill(edgesTo, Integer.MAX_VALUE);
        // 这里可以不用初始化src，因为我们关注的是dst，而放入队列的第一个元素就是src，并且指定了它的price和edges都是0，相当于记录了这个初始信息
        // priceTo[src] = 0;
        // edgesTo[src] = 0;
        // 队列元素还是按照 price 排序
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o[1]));
        // 放入起点，price是0，edges 是 0
        queue.offer(new int[]{src, 0, 0});
        while (!queue.isEmpty()) {
            // 当前状态点
            int[] poll = queue.poll();
            // 实际的元素点
            int idx = poll[0];
            // 起点到它的最小代价
            int priceFormStart = poll[1];
            // 起点到它的最少路径数
            int edgesFromStart = poll[2];
            // 如果这个点就是终点，那么直接返回(即便有多个指向终点的状态点，仍然是按照price排序的，第一个出来的还是代价最小的)
            if (idx == dst) {
                return priceFormStart;
            }
            // 缩放
            for (int[] nxt : adj.get(idx)) {
                int w = nxt[0];
                int p = nxt[1];
                // 要以 idx 为中转，首先起点到 idx 的路径数要 <= k-1，以它为中转后，路径数变为 edgesFromStart + 1，不能超过 k
                if (edgesFromStart < k) {
                    // 如果以它为中转，能使得代价更小，或者 路径更少，都得到一个新的状态点
                    if (priceFormStart + p < priceTo[w] || edgesFromStart + 1 < edgesTo[w]) {
                        // 更新当前的最小代价
                        priceTo[w] = priceFormStart + p;
                        // 更新当前的最小路径数
                        edgesTo[w] = edgesFromStart + 1;
                        // 加入一个状态点到队列
                        queue.offer(new int[]{w, priceTo[w], edgesTo[w]});
                    }
                }
            }
        }
        // 集合空了，上面的while中if没有成立过，说明起点无法到达终点
        return -1;
    }

    /**
     * 自定向下动态规划+备忘录
     * dp(j, k) 表示，从起点到j的最小代价，起点到j之间的路径数不能超过k
     * 那么 dp(j, k) = min(dp(x1, k-1) + cos[x1][j], dp(x2, k-1) + cos[x2][j], ...) 其中 x1,x2, 是 j 的入度节点，也就是 存在 x1到j，x2到j的边
     *
     * 因为 x 可能同时是多个y的入度节点，也就是 x->y1,x->y2都存在边，那么x就可能被重复计算，所以需要备忘录
     */
    int dst;
    int src;
    List<List<int[]>> degree;
    int[][] memo;
    public int findCheapestPrice2(int n, int[][] flights, int src, int dst, int k) {
        this.dst = dst;
        this.src = src;
        // 统计每个点的入度，及入度点到自己的代价
        degree = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            degree.add(new ArrayList<>());
        }
        for (int[] flight : flights) {
            int from = flight[0];
            int to = flight[1];
            int price = flight[2];
            // 记录每个点的邻接点，和这个点和它邻接点的代价
            degree.get(to).add(new int[]{from, price});
        }
        // 题目是，最多经过 k 个中转，也就是 src 到 dst 最多有 k + 1 条边
        k++;
        // 备忘录
        memo = new int[n][k + 1];
        // 返回
        return dp(dst, k);
    }

    /**
     * 返回从起点到j的最小代价，起点到j之间的路径数不能超过k
     * @param j
     * @param k
     * @return
     */
    private int dp(int j, int k) {
        // 如果j是起点，不管 k 是几，直接返回 0
        if (j == src) {
            return 0;
        }
        // j不是起点，但是k是0，那么肯定没有路径
        if (k == 0) {
            return -1;
        }
        // 备忘录不空
        if (memo[j][k] != 0) {
            return memo[j][k];
        }
        // dp(j, k) = min(dp(x1, k-1) + cos[x1][j], dp(x2, k-1) + cos[x2][j], ...) 其中 x1,x2, 是 j 的入度节点
        int res = Integer.MAX_VALUE;
        for (int[] node : degree.get(j)) {
            int idx = node[0];
            int price = node[1];
            // 子问题有解才更新
            int subRes = dp(idx, k - 1);
            if (subRes != -1) {
                res = Math.min(res, subRes + price);
            }
        }
        // 注意返回值，上面对子问题的判断是根据返回值是否为-1进行的，所以无解的时候，我们需要保证返回-1
        memo[j][k] = res == Integer.MAX_VALUE ? -1 : res;
        return memo[j][k];
    }

    /**
     * 动态规划，自底向上
     * dp[j][k] 表示，从起点到j的最小代价，起点到j之间的路径数=====k
     * 那么 dp[j][k] = min(dp[x1][k-1] + cos[x1][j], dp[x2][k-1] + cos[x2][j], ...) 其中 x1,x2, 是 j 的入度节点，也就是 存在 x1到j，x2到j的边
     *
     * 最终结果应该返回，min(dp[dst][x])其中 0 <= x <= k，
     * @param n
     * @param flights
     * @param src
     * @param dst
     * @param k
     * @return
     */
    public int findCheapestPrice3(int n, int[][] flights, int src, int dst, int k) {
        // 题目是，最多经过 k 个中转，也就是 src 到 dst 最多有 k + 1 条边
        k++;
        // dp 和 初始化 从起点到j的最小代价，起点到j之间的路径数=k
        int[][] dp = new int[n][k + 1];
        int INF = 666666;
        for (int i = 0; i < n; ++i) {
            Arrays.fill(dp[i], INF);
        }
        // base case 起点到起点
        dp[src][0] = 0;
        for (int i = 1; i <= k; ++i) {
            for (int[] flight : flights) {
                int from = flight[0];
                int to = flight[1];
                int price = flight[2];
                // 这个点和它邻接点的代价
                dp[to][i] = Math.min(dp[to][i], dp[from][i - 1] + price);
            }
        }
        // 返回之前要比较这个最小值是否有意义
        int res = Arrays.stream(dp[dst]).min().getAsInt();
        // 不存在最小代价
        return res == INF ? -1 : res;
    }


    /**
     * 一维dp
     * @param n
     * @param flights
     * @param src
     * @param dst
     * @param k
     * @return
     */
    public int findCheapestPrice8(int n, int[][] flights, int src, int dst, int k) {
        final int INF = 10000 * 101 + 1;
        int[] f = new int[n];
        Arrays.fill(f, INF);
        // base case
        f[src] = 0;
        int ans = INF;
        k++;
        for (int t = 1; t <= k; ++t) {
            // 注意状态转移式子，分清楚一个阶段与下一个阶段
            // 这里借助额外的数组
            int[] g = new int[n];
            Arrays.fill(g, INF);
            for (int[] flight : flights) {
                int j = flight[0], i = flight[1], cost = flight[2];
                g[i] = Math.min(g[i], f[j] + cost);
            }
            f = g;
            ans = Math.min(ans, f[dst]);
        }
        return ans == INF ? -1 : ans;
    }

}
