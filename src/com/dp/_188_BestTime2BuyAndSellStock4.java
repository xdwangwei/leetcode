package com.dp;

/**
 * @author wangwei
 * 2020/4/22 0:30
 * <p>
 * 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
 * <p>
 * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。
 * <p>
 * 注意: 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * <p>
 * 示例 1:
 * <p>
 * 输入: [2,4,1], k = 2
 * 输出: 2
 * 解释: 在第 1 天 (股票价格 = 2) 的时候买入，在第 2 天 (股票价格 = 4) 的时候卖出，这笔交易所能获得利润 = 4-2 = 2 。
 * 示例 2:
 * <p>
 * 输入: [3,2,6,5,0,3], k = 2
 * 输出: 7
 * 解释: 在第 2 天 (股票价格 = 2) 的时候买入，在第 3 天 (股票价格 = 6) 的时候卖出, 这笔交易所能获得利润 = 6-2 = 4 。
 *      随后，在第 5 天 (股票价格 = 0) 的时候买入，在第 6 天 (股票价格 = 3) 的时候卖出, 这笔交易所能获得利润 = 3-0 = 3 。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _188_BestTime2BuyAndSellStock4 {

    /**
     * 动态规划
     * <p>
     * 121题限制只能买1次，122题限制买无数次，这两种情况下k都可以被省略考虑
     * 这道题需要需要k所有取值
     * <p>
     * 按最简单的动态规划的思路想，用 dp[i]表示前i天的最高收益，那么 dp[i+1] 怎么根据 dp[i] 求出来呢？
     * 我们注意到我们题目是求那么多天最多交易两次的最高收益，还有一个最多交易次数的变量，我们把它加到数组中再试一试。
     * 所以此时的dp数组要有【天数】【是否持有股票】【最多交易了几次】单三个状态
     * <p>
     * 用 dp[i][k] 表示前i天最多交易k次的最高收益，那么 dp[i+1][k] 怎么通过之前的解求出来呢？
     * <p>
     * 第i天，最多交易k次，不持有股票 的收益 来源于今天啥都不干 或 昨天持有今天卖出
     * dp[i][k][0] = max(dp[i-1][k][0], dp[i-1][k][1] + prices[i])
     * 第i天，最多交易k次，持有股票 的收益 来源于今天啥都不干 或 昨天不持有今天买入
     * 此时，因为今天买入，所以昨天最多交易k-1次
     * dp[i][k][1] = max(dp[i-1][k][1], dp[i-1][k-1][0] - prices[i])
     * <p>
     * 还记得前面总结的「穷举框架」吗？就是说我们必须穷举所有状态。其实我们之前的解法，都在穷举所有状态，只是之前的题目中 k 都被化简掉了。这道题由于没有消掉 k 的影响，所以必须要对 k 进行穷举：
     * <p>
     * int[][][] dp = new int[n][max_k + 1][2];
     * for (int i = 0; i < n; i++) {
     *      for (int k = max_k; k >= 1; k--) {
     *          if (i - 1 == -1) {
     *          // 处理 base case
     *          continue;
     *          }
     *          dp[i][k][0]=max(dp[i-1][k][0],dp[i-1][k][1]+prices[i]);
     *          dp[i][k][1]=max(dp[i-1][k][1],dp[i-1][k-1][0]-prices[i]);
     *      }
     * }
     * // 穷举了 n × max_k × 2 个状态，正确。
     * return dp[n-1][max_k][0];
     * 作者：labuladong
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii/solution/yi-ge-tong-yong-fang-fa-tuan-mie-6-dao-gu-piao-wen/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 但这样写，对于倒数第二个用例，给出的k=1000000,数组也特别大导致内存溢出
     *
     * @param prices
     * @return
     */
    public int maxProfit1Failed(int max_k, int[] prices) {

        if (prices == null || prices.length < 2 || max_k < 1) return 0;
        // dp[][][] 三个状态 天数/最多交易次数/是否持股
        int[][][] dp = new int[prices.length][max_k + 1][2];

        for (int i = 0; i < prices.length; i++) {
            // 这里倒序可以避免覆盖，但其实正序也能通过
            for (int k = max_k; k >= 1; --k) {
                // 处理初始值(越界特殊值)
                if (i - 1 == -1) {
                    // 第 0 天，没持股时收益肯定是0
                    dp[i][k][0] = 0;
                    // 第 0 天 持股，那肯定是第一次买
                    dp[i][k][1] = -prices[0];
                    continue;
                }
                // 今天不持股，来源于 昨天不持股今天啥也不敢 昨天持股，今天卖出
                dp[i][k][0] = Math.max(dp[i - 1][k][0], dp[i - 1][k][1] + prices[i]);
                // 今天持股，来源于 昨天持股今天啥也不敢 昨天不持股，今天买入
                // 因为今天要买入，所以昨天最多交易k-1次
                dp[i][k][1] = Math.max(dp[i - 1][k][1], dp[i - 1][k - 1][0] - prices[i]);
            }
        }
        // 最后一天，全部交易完成，无剩余股票
        return dp[prices.length - 1][max_k][0];
    }

    /**
     * 动态规划
     * 复习算法小抄时，自己写出来的
     * @param k
     * @param prices
     * @return
     */
    public int maxProfit8(int k, int[] prices) {
        int n = prices.length;
        if (k < 1 || n < 2) {
            return 0;
        }
        k = Math.min(k, n / 2);
        int[][][] dp = new int[n][k + 1][2];
        for (int j = 1; j <= k; ++j) {
            // dp[0][j][0] = 0;
            dp[0][j][1] = -prices[0];
        }
        for (int i = 1; i < n; ++i) {
            for (int j = 1; j <= k; ++j) {
                dp[i][j][0] = Math.max(dp[i - 1][j][0], dp[i - 1][j][1] + prices[i]);
                dp[i][j][1] = Math.max(dp[i - 1][j][1], dp[i - 1][j - 1][0] - prices[i]);
            }
        }
        return dp[n - 1][k][0];
    }

    /**
     * // dp[][][] 三个状态 天数/最多交易次数/是否持股
     *  int[][][] dp = new int[prices.length][max_k + 1][2];
     * 上面那种做法，并未完全通过
     * 对于倒数第二个用例，给出的k=1000000,数组也特别大导致内存溢出
     *
     * 我们知道，一次交易至少需要 2 天，一天买，一天卖。
     * 因此如果 k 很大，大到大于等于 len / 2，就相当于股票系列的第 122 题(可以买卖任意次)
     * 使用贪心算法(只要能获利就进行交易)去做就可以了。这是一个特判。
     *
     * 作者：liweiwei1419
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv/solution/dong-tai-gui-hua-by-liweiwei1419-4/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param prices
     * @return
     */
    public int maxProfit1(int max_k, int[] prices) {

        if (prices == null || prices.length < 2 || max_k < 1) return 0;

        // 判断k是否特别大,相当于可以进行任意次交易
        if (max_k > prices.length / 2)
            return greedy(prices);

        // dp[][][] 三个状态 天数/最多交易次数/是否持股
        int[][][] dp = new int[prices.length][max_k + 1][2];

        for (int i = 0; i < prices.length; i++) {
            // 这里倒序可以避免覆盖，但其实正序也能通过
            for (int k = max_k; k >= 1; --k) {
                // 处理初始值(越界特殊值)
                if (i - 1 == -1) {
                    // 第 0 天，没持股时收益肯定是0
                    dp[i][k][0] = 0;
                    // 第 0 天 持股，那肯定是第一次买
                    dp[i][k][1] = -prices[0];
                    continue;
                }
                // 今天不持股，来源于 昨天不持股今天啥也不敢 昨天持股，今天卖出
                dp[i][k][0] = Math.max(dp[i - 1][k][0], dp[i - 1][k][1] + prices[i]);
                // 今天持股，来源于 昨天持股今天啥也不敢 昨天不持股，今天买入
                // 因为今天要买入，所以昨天最多交易k-1次
                dp[i][k][1] = Math.max(dp[i - 1][k][1], dp[i - 1][k - 1][0] - prices[i]);
            }
        }
        // 最后一天，全部交易完成，无剩余股票
        return dp[prices.length - 1][max_k][0];
    }

    /**
     * 交易任意次，能获得的最大利润
     * @param prices
     * @return
     */
    private int greedy(int[] prices){
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++){
            // 只要能获利就交易
            if (prices[i] > prices[i-1])
                maxProfit += prices[i] - prices[i-1];
        }
        return maxProfit;
    }

    /**
     * 我们可以发现，两个状态转移方程，当前行i只依赖上一行i-1，可以考虑使用“滚动数组”这个技巧。
     * 我们直接砍掉第一维度即可，用一个初始值一直迭代最终得到的自然就是最后一天的收益
     * @param max_k
     * @param prices
     * @return
     */
    public int maxProfit2(int max_k, int[] prices) {

        if (prices == null || prices.length < 2 || max_k < 1) return 0;

        // 判断k是否特别大,相当于可以进行任意次交易
        if (max_k >= prices.length / 2)
            return greedy(prices);

        // dp[][] 二个状态 最多交易次数/是否持股，刚开始是第0天
        int[][] dp = new int[max_k + 1][2];

        for (int i = 0; i < prices.length; i++) {
            // 这里倒序可以避免覆盖，但其实正序也能通过
            for (int k = max_k; k >= 1; --k) {
                // 处理初始值(越界特殊值)第0天，也可以单独提出来赋值，之后for循环就只负责向后迭代
                if (i - 1 == -1) {
                    // 第 0 天，没持股时收益肯定是0
                    dp[k][0] = 0;
                    // 第 0 天 持股，那肯定是第一次买
                    dp[k][1] = -prices[0];
                    continue;
                }
                // 今天不持股，来源于 昨天不持股今天啥也不敢 昨天持股，今天卖出
                dp[k][0] = Math.max(dp[k][0], dp[k][1] + prices[i]);
                // 今天持股，来源于 昨天持股今天啥也不敢 昨天不持股，今天买入
                // 因为今天要买入，所以昨天最多交易k-1次
                dp[k][1] = Math.max(dp[k][1], dp[k - 1][0] - prices[i]);
            }
        }
        // 最后一天，全部交易完成，无剩余股票
        return dp[max_k][0];
    }
}
