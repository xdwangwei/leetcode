package com.dp;

/**
 * @author wangwei
 * 2020/4/21 22:15
 * <p>
 * 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
 * <p>
 * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 两笔 交易。
 * <p>
 * 注意: 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * <p>
 * 示例 1:
 * <p>
 * 输入: [3,3,5,0,0,3,1,4]
 * 输出: 6
 * 解释: 在第 4 天（股票价格 = 0）的时候买入，在第 6 天（股票价格 = 3）的时候卖出，这笔交易所能获得利润 = 3-0 = 3 。
 *      随后，在第 7 天（股票价格 = 1）的时候买入，在第 8 天 （股票价格 = 4）的时候卖出，这笔交易所能获得利润 = 4-1 = 3 。
 * 示例 2:
 * <p>
 * 输入: [1,2,3,4,5]
 * 输出: 4
 * 解释: 在第 1 天（股票价格 = 1）的时候买入，在第 5 天 （股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。  
 *      注意你不能在第 1 天和第 2 天接连购买股票，之后再将它们卖出。  
 *      因为这样属于同时参与了多笔交易，你必须在再次购买前出售掉之前的股票。
 * 示例 3:
 * <p>
 * 输入: [7,6,4,3,1]
 * 输出: 0
 * 解释: 在这个情况下, 没有交易完成, 所以最大利润为 0。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _123_BestTime2BuyAndSellStock3 {

    /**
     * 动态规划
     * <p>
     * 121题限制只能买1次，122题限制买无数次，这两种情况下k都可以被省略考虑
     * 188题需要需要k所有取值
     *
     * 这道题相当于 188题的特殊情况
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
     *
     * 因为一次交易是从买入股票开始，所以dp[x][k-1][y]只出现在当天持有股票的状态转移方程中
     * <p>
     * 还记得前面总结的「穷举框架」吗？就是说我们必须穷举所有状态。其实我们之前的解法，都在穷举所有状态，只是之前的题目中 k 都被化简掉了。
     * 这道题由于没有消掉 k 的影响，所以必须要对 k 进行穷举：
     * <p>
     * int max_k = 2;
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
     * <p>
     * 这里 k 取值范围比较小，所以可以不用 for 循环，直接把 k = 1 和 2 的情况手动列举出来也可以：
     * dp[i][2][0] = max(dp[i-1][2][0], dp[i-1][2][1] + prices[i])
     * dp[i][2][1] = max(dp[i-1][2][1], dp[i-1][1][0] - prices[i])
     * dp[i][1][0] = max(dp[i-1][1][0], dp[i-1][1][1] + prices[i])
     * dp[i][1][1] = max(dp[i-1][1][1], -prices[i])
     * <p>
     * 作者：labuladong
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii/solution/yi-ge-tong-yong-fang-fa-tuan-mie-6-dao-gu-piao-wen/
     * <p>
     * 作者：windliang
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii/solution/xiang-xi-tong-su-de-si-lu-fen-xi-duo-jie-fa-by--29/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param prices
     * @return
     */

    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int max_k = 2;
        // k至少取1，也可以把后二维合并成4个状态
        int[][][] dp = new int[prices.length][max_k + 1][2];
        for (int i = 0; i < prices.length; i++) {
            for (int k = 1; k <= max_k; k++) {
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
     * 动态规划，复习算法小抄时自己写出来的
     * @param prices
     * @return
     */
    public int maxProfit8(int[] prices) {
        int n = prices.length;
        if (n == 1) {
            return 0;
        }
        int k = 2;
        int[][][] dp = new int[n][k + 1][2];
        for (int j = 1; j <= k; ++j) {
            // dp[0][j][0] = 0;
            dp[0][j][1] = -prices[0];
        }
        for (int i = 1; i < n; ++i) {
            dp[i][1][0] = Math.max(dp[i - 1][1][0], dp[i - 1][1][1] + prices[i]);
            dp[i][1][1] = Math.max(dp[i - 1][1][1], -prices[i]);
            dp[i][2][0] = Math.max(dp[i - 1][2][0], dp[i - 1][2][1] + prices[i]);
            dp[i][2][1] = Math.max(dp[i - 1][2][1], dp[i - 1][1][0] - prices[i]);
        }
        return dp[n - 1][k][0];
    }

    /**
     * 动态规划状态压缩，复习算法小抄时自己写出来的
     * @param prices
     * @return
     */
    public int maxProfit9(int[] prices) {
        int n = prices.length;
        if (n < 2) {
            return 0;
        }
        // int k = 2;
        // int[][][] dp = new int[n][k + 1][2];
        // for (int j = 1; j <= k; ++j) {
        //     // dp[0][j][0] = 0;
        //     dp[0][j][1] = -prices[0];
        // }
        int dp_i_1_0 = 0, dp_i_2_0 = 0, dp_i_1_1 = -prices[0], dp_i_2_1 = -prices[0];
        for (int i = 1; i < n; ++i) {
            int temp = dp_i_1_0;
            // dp[i][1][0] = Math.max(dp[i - 1][1][0], dp[i - 1][1][1] + prices[i]);
            dp_i_1_0 = Math.max(dp_i_1_0, dp_i_1_1 + prices[i]);
            // dp[i][1][1] = Math.max(dp[i - 1][1][1], -prices[i]);
            dp_i_1_1 = Math.max(dp_i_1_1, -prices[i]);
            // dp[i][2][0] = Math.max(dp[i - 1][2][0], dp[i - 1][2][1] + prices[i]);
            dp_i_2_0 = Math.max(dp_i_2_0, dp_i_2_1 + prices[i]);
            // dp[i][2][1] = Math.max(dp[i - 1][2][1], dp[i - 1][1][0] - prices[i]);
            dp_i_2_1 = Math.max(dp_i_2_1, temp - prices[i]);
        }
        return dp_i_2_0;
    }

    /**
     * 因为k只取1，2，所以我们可以把 dp[prices.length][max_k+1][2]合并成一个二维数组
     * dp[i][0] --         第i天 啥也不干
     * dp[i][1] -- s11 表示 第i天 第一次 买入股票 收益
     * dp[i][2] -- s10 表示 第i天 第一次 卖出股票 收益
     * dp[i][3] -- s21 表示 第i天 第二次 买入股票 收益
     * dp[i][4] -- s20 表示 第i天 第二次 卖出股票 收益
     */
    public int maxProfit2(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int len = prices.length;
        int[][] dp = new int[len][5];
        // 初始值
        dp[0][0] = 0;
        // 第 0 天 买入第一支股票
        dp[0][1] = -prices[0];
        // 3 状态都还没有发生，因此应该赋值为一个不可能的数
        for (int i = 0; i < len; i++) {
            dp[i][3] = Integer.MIN_VALUE;
        }
        for (int i = 1; i < len; i++) {
            dp[i][4] = Math.max(dp[i - 1][4], dp[i - 1][3] + prices[i]);
            dp[i][3] = Math.max(dp[i - 1][3], dp[i - 1][2] - prices[i]);
            dp[i][2] = Math.max(dp[i - 1][2], dp[i - 1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
            dp[i][0] = 0;
        }
        // 最大值只发生在不持股的时候，因此来源有 3 个：j = 0 ,j = 2, j = 4
        return Math.max(0, Math.max(dp[len - 1][2], dp[len - 1][4]));
    }

    /**
     * 同样的，dp[i]只和dp[i-1]有关，而且只涉及到了5个状态量的迭代，又不需要记录中间过程，
     * 我们可以用一个一维数组代替
     */
    public int maxProfit3(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int len = prices.length;
        // j = 0：什么都不操作
        // j = 1：第 1 次买入一支股票
        // j = 2：第 1 次卖出一支股票
        // j = 3：第 2 次买入一支股票
        // j = 4：第 2 次卖出一支股票
        int[] dp = new int[5];
        // 初始值
        dp[0] = 0;
        // 第 0 天 买入第一支股票
        dp[1] = -prices[0];
        // 3 状态都还没有发生，因此应该赋值为一个不可能的数
        dp[3] = Integer.MIN_VALUE;
        // 下面是迭代过程，dp[i]代表的是上一次的结果
        // 这里不存在dp[i-1]这种操作，请区分索引和迭代时的区别,这里的dp[1234]都是常数，相当于四个状态
        // 倒序更新可以放置覆盖
        for (int i = 1; i < len; i++) {
            dp[4] = Math.max(dp[4], dp[3] + prices[i]);
            dp[3] = Math.max(dp[3], dp[2] - prices[i]);
            dp[2] = Math.max(dp[2], dp[1] + prices[i]);
            dp[1] = Math.max(dp[1], dp[0] - prices[i]);
            dp[0] = 0;
        }
        // 最大值只发生在不持股的时候，因此来源有 3 个：j = 0 ,j = 2, j = 4
        return Math.max(0, Math.max(dp[2], dp[4]));
    }

    /**
     * 我们可以看到，dp[0] = 0是一直没有改变的，所以我们直接用0替换她就可以了
     */
    public int maxProfit4(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int len = prices.length;
        // j = 0：第 1 次买入一支股票
        // j = 1：第 1 次卖出一支股票
        // j = 2：第 2 次买入一支股票
        // j = 3：第 2 次卖出一支股票
        int[] dp = new int[4];
        // 初始值
        // 第 0 天 买入第一支股票
        dp[0] = -prices[0];
        // 2 状态都还没有发生，因此应该赋值为一个不可能的数
        dp[2] = Integer.MIN_VALUE;
        // 下面是迭代过程，dp[i]代表的是上一次的结果
        // 这里不存在dp[i-1]这种操作，请区分索引和迭代时的区别,这里的dp[1234]都是常数，相当于四个状态
        // 倒序更新可以放置覆盖
        for (int i = 1; i < len; i++) {
            // dp[4] = Math.max(dp[4], dp[3] + prices[i]);
            // dp[3] = Math.max(dp[3], dp[2] - prices[i]);
            // dp[2] = Math.max(dp[2], dp[1] + prices[i]);
            // dp[1] = Math.max(dp[1], dp[0] - prices[i]);
            // dp[0] = 0;
            dp[3] = Math.max(dp[3], dp[2] + prices[i]);
            dp[2] = Math.max(dp[2], dp[1] - prices[i]);
            dp[1] = Math.max(dp[1], dp[0] + prices[i]);
            dp[0] = Math.max(dp[0], -prices[i]);
        }
        // 最大值只发生在不持股的时候，因此来源有 3 个：j = 0 ,j = 2, j = 4
        return Math.max(0, Math.max(dp[1], dp[3]));
    }

}
