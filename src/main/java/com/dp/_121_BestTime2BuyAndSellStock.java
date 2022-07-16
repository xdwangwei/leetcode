package com.dp;

/**
 * @author wangwei
 * 2020/4/21 16:22
 * <p>
 * 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
 * <p>
 * 如果你最多只允许完成一笔交易（即买入和卖出一支股票一次），设计一个算法来计算你所能获取的最大利润。
 * <p>
 * 注意：你不能在买入股票前卖出股票。
 * <p>
 * <p>
 * 示例 1:
 * <p>
 * 输入: [7,1,5,3,6,4]
 * 输出: 5
 * 解释: 在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。
 * 注意利润不能是 7-1 = 6, 因为卖出价格需要大于买入价格；同时，你不能在买入前卖出股票。
 * 示例 2:
 * <p>
 * 输入: [7,6,4,3,1]
 * 输出: 0
 * 解释: 在这种情况下, 没有交易完成, 所以最大利润为 0。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _121_BestTime2BuyAndSellStock {

    /**
     * 暴力法，尝试从每一个点买股票，再在之后的所有天数里找到卖价最高的那天卖出
     *
     * @param prices
     * @return
     */
    public int maxProfit1(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int maxProfit = 0;
        for (int i = 0; i < prices.length - 1; i++)
            for (int j = i + 1; j < prices.length; j++) {
                // 从这里可以看出，优化方案就是记录之前最小的price
                maxProfit = Math.max(maxProfit, prices[j] - prices[i]);
            }
        return maxProfit;
    }

    /**
     * 方法二：针对暴力枚举的优化
     * 如果我们真的在买卖股票，我们肯定会想：如果我是在历史最低点买的股票就好了！
     * 太好了，在题目中，我们只要用一个变量记录一个历史最低价格 minprice，我们就可以假设自己的股票是在那天买的。
     * 那么我们在第 i 天卖出股票能得到的利润就是 prices[i] - minprice。
     *
     * 因此，我们只需要遍历价格数组一遍，记录历史最低点，
     * 然后在每一天考虑这么一个问题：如果我是在历史最低点买进的，那么我今天卖出能赚多少钱？当考虑完所有天数之时，我们就得到了最好的答案。
     *
     * 我们发现：其实只需要关心【之前（不包括现在）看到的最低股价】，
     * 于是在遍历的过程中，记录下之前看到的最低股价，就可以省去内层循环。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/solution/121-mai-mai-gu-piao-de-zui-jia-shi-ji-by-leetcode-/
     * 来源：力扣（LeetCode）
     */
    public int maxProfit2(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int maxProfit = 0;
        int minPrice = prices[0];
        for (int i = 1; i < prices.length; i++) {
            // 截止到当天卖出可获得的最大收益 prices[i] - minPrice
            maxProfit = Math.max(maxProfit, prices[i] - minPrice);
            // 更新一下最低价格
            minPrice = Math.min(minPrice, prices[i]);
        }

        return maxProfit;
    }

    /**
     * 方法三：动态规划
     *
     * 首先我们来确定一天会有几个状态, 最容易想到的状态其实是四种:
     *
     * 当天买入了股票(持有股票)
     * 当天卖出了股票(不持有股票)
     * 当天没有进行任何操作, 但之前是买入股票(持有)的状态
     * 当天没有进行任何操作, 但之前是卖出股票(不持有)的状态
     *
     * 限制条件是：只能交易一次，出售只能在买入后进行
     *
     * 动态规划的 5 个步骤：
     * <p>
     * 1、设定状态
     * 这道题其实是一个典型的二维 dp 问题。“动态规划”用于多阶段最优化问题的求解。
     * 这里天数代表每个阶段，即一天一天看，设置为第一维。
     * 为了消除后效性（前面的状态确定下来以后不会因为后面状态而更改），将当天是否持股设置为第二维的状态。
     * <p>
     * 于是：
     * <p>
     * 状态 dp[i][j] 表示：在索引为 i 的这一天，用户手上持股状态为 j 所获得的最大利润。
     *      j 只有 2 个值：0 表示不持股（特指卖出股票以后的不持股状态），1 表示持股。
     * <p>
     * 2、思考状态转移方程
     * dp[i][0] (今天不持股的利润) 怎样转移？
     * <p>
     * dp[i - 1][0] ：当然可以从昨天不持股转移过来，表示从昨天到今天什么都不操作，这一点是显然的；
     * <p>
     * dp[i - 1][1] + prices[i]：昨天持股，就在索引为 i 的这一天，我卖出了股票，状态由 1 变成了 0，此时卖出股票，因此加上这一天的股价。
     * <p>
     * 综上：dp[i][0] = max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
     * <p>
     * dp[i][1] (今天持股的收益) 怎样转移？
     * <p>
     * dp[i - 1][1] ：昨天持股，今天什么都不操作，当然可以从昨天持股转移过来，这一点是显然的；
     * <p>
     * -prices[i]：注意：状态 1 不能由状态 0 来，因为事实上，状态 0 特指：“卖出股票以后不持有股票的状态”，请注意这个状态和“没有进行过任何一次交易的不持有股票的状态”的区别。
     * <p>
     * 因此，-prices[i] 就表示，在索引为 i 的这一天，执行买入操作得到的收益。
     * <p>
     * 也可以这样理解：因为题目只允许一次交易，因此 伟买股票前的收益都为0 dp[i - 1][0] = 0。
     * <p>
     * 综上：dp[i][1] = max(dp[i - 1][1], -prices[i]);
     * <p>
     * 3、考虑初始值
     * 第 0 天不持股，显然 dp[0][0] = 0；
     * <p>
     * 第 0 天持股，显然dp[0][1] = -prices[0](只有输出)。
     * <p>
     * 4、考虑输出
     * 从状态转移方程可以看出，每一天的状态都考虑了之前的状态。在只发生一次交易的情况下，持有这支股票一定不能使我们获得最大利润。
     * 因此输出是 dp[len - 1][0]，不可能是持股的状态 dp[len - 1][1]，股票出售了才有更多利润
     * <p>
     * 作者：liweiwei1419
     * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/solution/bao-li-mei-ju-dong-tai-gui-hua-chai-fen-si-xiang-b/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int maxProfit3(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        // dp[i][j] 第i天持股状态为j 的收益
        int[][] dp = new int[prices.length][2];
        // 初始值
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        // 动态规划
        for (int i = 1; i < prices.length; i++) {
            // 今天不持股，可以来自于
            // 昨天不持股，今天什么也不做
            // 昨天持股，今天卖出，收益会则更加
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            // 今天持股，可以来自于
            // 昨天持股，今天什么也不做
            // 昨天不持股，今天买(花费)，只有一次购买机会，所以之前没有交易，那么收益只能是0
            dp[i][1] = Math.max(dp[i - 1][1], 0 - prices[i]);
        }
        // 最后一天，不持股时(卖出)的利润
        return dp[prices.length - 1][0];
    }

    /**
     * 由于 dp[i] 仅仅依赖于 dp[i - 1]，新状态只和相邻的一个状态有关，
     * 就相当于一个数一直在不断更新，也不需要保留中间过程，只需得到最后一天就可
     * 其实不用整个 dp 数组，只需要一个变量储存相邻的那个状态就足够了，
     * 这样可以把空间复杂度降到 O(1):
     */

    public int maxProfit4(int[] prices) {
        if (prices == null || prices.length == 0)
            return 0;
        // 一维数组即可
        int[] dp = new int[2];
        // 第0天的两种状态 对应收益
        dp[0] = 0;
        dp[1] = -prices[0];
        // 更新迭代
        for (int i = 1; i < prices.length; i++) {
            // 今天不持有股票
            // 昨天不持有，今天啥都不干，收益不变
            // 昨天持有，今天卖出，收益增加
            dp[0] = Math.max(dp[0], dp[1] + prices[i]);
            // 今天持有股票
            // 昨天持有，今天啥都不干，收益不变
            // 昨天不持有，今天买入，花费增加，只能买一次，之前收益都是0
            dp[1] = Math.max(dp[1], 0 - prices[i]);
        }
        // 更新到最后就是最后一天卖出股票，收益
        return dp[0];
    }
}
