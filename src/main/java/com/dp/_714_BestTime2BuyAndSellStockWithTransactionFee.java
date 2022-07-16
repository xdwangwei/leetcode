package com.dp;

/**
 * @author wangwei
 * 2020/4/22 17:03
 *
 * 给定一个整数数组 prices，其中第 i 个元素代表了第 i 天的股票价格 ；非负整数 fee 代表了交易股票的手续费用。
 *
 * 你可以无限次地完成交易，但是你每笔交易都需要付手续费。如果你已经购买了一个股票，在卖出它之前你就不能再继续购买股票了。
 *
 * 返回获得利润的最大值。
 *
 * 注意：这里的一笔交易指买入持有并卖出股票的整个过程，每笔交易你只需要为支付一次手续费。
 *
 * 示例 1:
 *
 * 输入: prices = [1, 3, 2, 8, 4, 9], fee = 2
 * 输出: 8
 * 解释: 能够达到的最大利润:
 * 在此处买入 prices[0] = 1
 * 在此处卖出 prices[3] = 8
 * 在此处买入 prices[4] = 4
 * 在此处卖出 prices[5] = 9
 * 总利润: ((8 - 1) - 2) + ((9 - 4) - 2) = 8.
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _714_BestTime2BuyAndSellStockWithTransactionFee {

    /**
     * 动态规划
     * 这道题相比于上一题(122)的唯一区别在于，上一道题可买卖无数次，这一道题也是买卖无数次，但是卖出时(完成一笔交易)需要手续费
     * 无非就是不持股时(卖出)时的收益需要减去手续费
     * <p>
     * 在上一题中
     * 当天不持有股票：昨天不持有或昨天持有今天卖出
     * dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i])
     * 当天持有股票：昨天持有股票 或 昨天不持有今天买入股票
     * dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i])
     * <p>
     * 需要改动的地方就是，
     * dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i] - fee)
     * dp[i][1] = Math.max(dp[i-1][1], dp[i-1][2] - prices[i])
     * <p>
     * 同样的，dp[i]只取决于dp[i-1]，又不需要记录中间过程，所以用两个变量代替即可
     */
    public int maxProfit(int[] prices, int fee) {

        if (prices == null || prices.length < 2)
            return 0;
        // 两个变量，表示第0天的两个状态
        int dp_i_0 = 0;
        int dp_i_1 = -prices[0];
        // 由已有状态迭代更新下一代，直到最后一天
        for (int i = 1; i < prices.length; i++) {
            // 今天不持股，来自于昨天不持股，或昨天持股今天卖出(完成一笔交易扣除手续费)
            dp_i_0 = Math.max(dp_i_0, dp_i_1 + prices[i] - fee);
            // 今天持股来自于昨天持股，或昨天不持股今天买入
            dp_i_1 = Math.max(dp_i_1, dp_i_0 - prices[i]);
        }
        // 最后一天不持股的利润
        return dp_i_0;
    }
}
