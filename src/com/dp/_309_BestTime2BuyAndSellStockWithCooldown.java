package com.dp;

/**
 * @author wangwei
 * 2020/4/22 16:01
 * <p>
 * 309. 最佳买卖股票时机含冷冻期
 * 给定一个整数数组，其中第 i 个元素代表了第 i 天的股票价格 。​
 * <p>
 * 设计一个算法计算出最大利润。在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:
 * <p>
 * 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * 卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)。
 * 示例:
 * <p>
 * 输入: [1,2,3,0,2]
 * 输出: 3
 * 解释: 对应的交易状态为: [买入, 卖出, 冷冻期, 买入, 卖出]
 */
public class _309_BestTime2BuyAndSellStockWithCooldown {

    /**
     * 动态规划
     * 这道题相比于上一题(122)的唯一区别在于，上一道题可买卖无数次，这一道题也是买卖无数次，但是卖出后需要停一天才能继续买
     * 无非就是改一下状态递推方程
     * <p>
     * 在上一题中
     * 当天不持有股票：昨天不持有或昨天持有今天卖出
     * dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i])
     * 当天持有股票：昨天持有股票 或 昨天不持有今天买入股票
     * dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i])
     * <p>
     * 需要改动的地方就是，
     * 现在我们有三个状态
     *      1: 持有股票；
     *      0： 不持有股票，不处于冷冻期(没有卖股票)
     *      2 : 不持有股票，处于冻结期（卖出股票后的不持有状态）
     * <p>
     * 不持股可以由这两个状态转换而来：（1）昨天不持股，今天什么都不操作，仍然不持股。（2）昨天持股，今天卖了一股。
     * 持股可以由这两个状态转换而来：（1）昨天持股，今天什么都不操作，仍然持股；（2）昨天不持股，今天买了一股(由于交易只能隔天进行，所以昨天必须是冷冻期)；
     * dp[i][1] = Math.max(dp[i-1][1], dp[i-1][2] - prices[i])
     * 处在冷冻期[只可以由昨天不持股转换而来]，因为题目中说，刚刚把股票卖了，需要冷冻 1天。
     * <p>
     * 总结：
     * 今天不持有股票，也没有卖股票，来源是：昨天不持有，没买股票 或者 昨天不持有，但是是卖了股票后的不持有状态
     * dp[i][0] = Math.max(dp[i-1][0], dp[i-1][2])
     * 今天持有股票，来源是 昨天持有股票，或者 昨天没有持有股票，并且没有交易，今天买入股票
     * dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i])
     * 今天不持有股票，并且是卖了股票后的不持有，那来源 就是昨天有股票，今天卖出
     * dp[i][2] = dp[i-1][1] + prices[i]
     * <p>
     * 同样的，dp[i]只取决于dp[i-1]，又不需要记录中间过程，所以用两个变量代替即可
     */
    public int maxProfit(int[] prices) {
        int n = prices.length;
        if (n < 2)
            return 0;
        int[][] dp = new int[n][3];
        // 初始化第0天
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        dp[0][2] = 0;
        // 从第1天开始
        for (int i = 1; i < n; ++i) {
            // 今天不持有股票，也没有卖股票，来源是：昨天不持有，没买股票 或者 昨天不持有，但是是卖了股票后的不持有状态
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][2]);
            // 今天持有股票，来源是 昨天持有股票，或者 昨天没有持有股票，并且没有交易，今天买入股票
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
            // 今天不持有股票，并且是卖了股票后的不持有，那来源 就是昨天有股票，今天卖出
            dp[i][2] = dp[i - 1][1] + prices[i];
        }
        // 最后一天，一定不能剩股票, 两种不持有状态，返回较大的那个
        return Math.max(dp[n - 1][0], dp[n - 1][2]);
    }

    /**
     * 同样的，我们可以看到，dp[i]之和dp[i-1]有关，我们不需要保留过程就可以砍掉天数这一维度
     * 用三个变量表示第0天的三个状态，再往后进行迭代更新即可
     */
    public int maxProfit2(int[] prices) {
        int n = prices.length;
        if (n < 2)
            return 0;
        int[] dp = new int[3];
        // 初始化第0天
        dp[0] = 0;
        dp[1] = -prices[0];
        dp[2] = 0;
        // 迭到最后一天
        for (int i = 1; i < n; ++i) {
            int pre0 = dp[0], pre1 = dp[1];
            // 今天不持有股票，也没有卖股票，来源是：昨天不持有，没买股票 或者 昨天不持有，但是是卖了股票后的不持有状态
            dp[0] = Math.max(dp[0], dp[2]);
            // 今天持有股票，来源是 昨天持有股票，或者 昨天没有持有股票，并且没有交易，今天买入股票
            // 注意它来源于上一次的dp[0],保存在pre0中，而不是刚更新了的dp[0]
            dp[1] = Math.max(dp[1], pre0 - prices[i]);
            // 今天不持有股票，并且是卖了股票后的不持有，那来源 就是昨天有股票，今天卖出
            // 注意它来源于上一次的dp[1],保存在pre1中，而不是刚更新了的dp[1]
            dp[2] = pre1 + prices[i];
        }
        return Math.max(dp[0], dp[2]);
    }
}
