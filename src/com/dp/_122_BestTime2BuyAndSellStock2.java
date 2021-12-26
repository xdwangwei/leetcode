package com.dp;

/**
 * @author wangwei
 * 2020/4/21 14:05
 * <p>
 * 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
 * <p>
 * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
 * <p>
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 * <p>
 * 示例 1:
 * <p>
 * 输入: [7,1,5,3,6,4]
 * 输出: 7
 * 解释: 在第 2 天（股票价格 = 1）的时候买入，在第 3 天（股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。
 *      随后，在第 4 天（股票价格 = 3）的时候买入，在第 5 天（股票价格 = 6）的时候卖出, 这笔交易所能获得利润 = 6-3 = 3 。
 * 示例 2:
 * <p>
 * 输入: [1,2,3,4,5]
 * 输出: 4
 * 解释: 在第 1 天（股票价格 = 1）的时候买入，在第 5 天 （股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。
 *      注意你不能在第 1 天和第 2 天接连购买股票，之后再将它们卖出。
 *      因为这样属于同时参与了多笔交易，你必须在再次购买前出售掉之前的股票。
 * 示例 3:
 * <p>
 * 输入: [7,6,4,3,1]
 * 输出: 0
 * 解释: 在这种情况下, 没有交易完成, 所以最大利润为 0。
 *  
 * <p>
 * 提示：
 * <p>
 * 1 <= prices.length <= 3 * 10 ^ 4
 * 0 <= prices[i] <= 10 ^ 4
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _122_BestTime2BuyAndSellStock2 {

    /**
     * 波峰波谷
     * 先波谷，再波峰，波峰-波谷就是一次收益，再重复
     * 也就是找到所有 上升段
     *
     * @param prices
     * @return
     */
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int profit = 0;
        int peak, valley;
        int i = 0;
        while (i < prices.length - 1) {
            // 向下先找波谷
            while (i < prices.length - 1 && prices[i] >= prices[i + 1])
                i++;
            valley = prices[i];
            // 再向上找接着的波峰
            while (i < prices.length - 1 && prices[i] <= prices[i + 1])
                i++;
            peak = prices[i];
            // 本次收益
            profit += peak - valley;
        }
        return profit;
    }

    /**
     * 方法二：简单的一次遍历 （贪心算法）
     * <p>
     * 这道题 “贪心” 的地方在于，对于 “今天的股价 - 昨天的股价”，得到的结果有 3 种可能：（1）正数（2）0（3）负数。
     * 贪心算法的决策是：只加正数。
     * <p>
     * 该解决方案遵循 方法一 的本身使用的逻辑，但有一些轻微的变化。
     * 在这种情况下，我们可以简单地继续在斜坡上爬升并持续增加从连续交易中获得的利润，而不是在谷之后寻找每个峰值。
     * 最后，我们将有效地使用峰值和谷值，但我们不需要跟踪峰值和谷值对应的成本以及最大利润，
     * 但我们可以直接继续增加加数组的连续数字之间的差值，
     * 如果第二个数字大于第一个数字，我们获得的总和将是最大利润。这种方法将简化解决方案。
     * 这个例子可以更清楚地展现上述情况：
     * <p>
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/solution/mai-mai-gu-piao-de-zui-jia-shi-ji-ii-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int maxProfit2(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            // 满足递增，是向上的线段，就累加每一段的收益
            // 拐弯和下坡时，if不会成立
            if (prices[i] > prices[i - 1])
                maxProfit += prices[i] - prices[i - 1];
        }
        return maxProfit;
    }

    /**
     * 动态规划
     * 这道题相比于上一题(121)的唯一区别在于，上一题要求只能买卖一次股票，这道题可以说是可买卖无数次
     * dp[i][j] 表示 第i天，持有状态为j的情况下，能获得的最大利润，j=0表示不持有股票，j=1表示持有股票
     * 在上一题中：
     * 当天不持有股票的上一状态：昨天不持有股票或今天卖出股票
     * dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i])
     * 当前持有股票的上一状态：昨天持有股票或今天买入股票
     * dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i])
     *  但由于只能买一次，所以之前没有交易，就是没有收益的，也就是 dp[i-1][0] - prices[i] = -prices[i]
     * <p>
     * 在这一题中，就不存在交易次数限制，所以
     * dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i])
     * 当天持有股票：昨天持有股票或今天买入股票
     * dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i])
     * <p>
     * 同样的，dp[i]只取决于dp[i-1]，又不需要记录中间过程，所以用两个变量代替即可
     */
    public int maxProfit3(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        // last_0 第0天不持有股票的收益，是0
        // last_1 第0天持有股票的收益， 是 -prices[0]
        int last_0 = 0, last_1 = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            // dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i])
            // dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i])
            // 注意式子中交叉使用了，所以先把其中一个保存一下
            int temp = last_0;
            // 今天不持有股票
            // 昨天不持有，今天啥都不干，收益不变
            // 昨天持有，今天卖出，收益增加
            last_0 = Math.max(last_0, last_1 + prices[i]);
            // 今天持有股票
            // 昨天持有，今天啥都不干，收益不变
            // 昨天不持有，今天买入，花费增加
            // 注意这里用到的是昨天的last_0,就是temp保存的那个，而不是上面更新了的今天的last_0
            last_1 = Math.max(last_1, temp - prices[i]);
        }
        // 更新完后 last_0就是最后一天不持有股票的收益，last_1就是最后一天持有股票的收益
        // 全部卖完肯定收益更高
        return last_0;
    }
}
