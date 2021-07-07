package com.bag;

/**
 * @author wangwei
 * 2020/4/25 22:40
 * <p>
 * 给定不同面额的硬币和一个总金额。写出函数来计算可以凑成总金额的硬币组合数。假设每一种面额的硬币有无限个。 
 * <p>
 *  
 * <p>
 * 示例 1:
 * <p>
 * 输入: amount = 5, coins = [1, 2, 5]
 * 输出: 4
 * 解释: 有四种方式可以凑成总金额:
 * 5=5
 * 5=2+2+1
 * 5=2+1+1+1
 * 5=1+1+1+1+1
 * 示例 2:
 * <p>
 * 输入: amount = 3, coins = [2]
 * 输出: 0
 * 解释: 只用面额2的硬币不能凑成总金额3。
 * 示例 3:
 * <p>
 * 输入: amount = 10, coins = [10]
 * 输出: 1
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/coin-change-2
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _518_CoinChange2 {

    /**
     * 动态规划 自下而上
     * 完全背包问题
     * <p>
     * dp[i][j] 若只使用 coins 中的前 i 个硬币的面值，想凑出金额 j，有 dp[i][j] 种凑法。
     * base case 为 dp[0][..] = 0， dp[..][0] = 1。因为如果不使用任何硬币面值，就无法凑出任何金额；如果凑出的目标金额为 0，那么“无为而治”就是唯一的一种凑法。
     * 我们最终想得到的答案就是 dp[N][amount]，其中 N 为 coins 数组的大小。
     * 首先由于 i 是从 1 开始的，所以 coins 的索引是 i-1 时表示第 i 个硬币的面值。
     *
     * 注意状态转移方程与01背包的区别
     *
     * 01背包中dp[i][j]中的i代表的是这一个，完全背包中代表的是这一种
     *
     * @param amount
     * @param coins
     * @return
     */
    public int change(int amount, int[] coins) {
        if (coins == null) return 0;

        int[][] dp = new int[coins.length + 1][amount + 1];
        // base case
        for (int i = 0; i <= coins.length; ++i) dp[i][0] = 1;
        // 若只使用前i种硬币
        for (int i = 1; i <= coins.length; ++i) {
            // 对于各种容量的背包
            for (int j = 0; j <= amount; j++) {
                // 背包容量够
                if (j >= coins[i - 1]) {
                    // 当前硬币可放入，也可不放入
                    // 注意此处如果选择放入，对于01背包应该等于dp[i - 1][j - coins[i - 1]]
                    // 也就是在前i-1个硬币中去凑
                    // 但对于完全背包，硬币是无限的，所以此处仍然是dp[i]

                    // 如果选择不放入，那就是这一种都不放入，所以是dp[i - 1]
                    dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]];
                    // 背包容量不够，当前硬币无法放入
                } else
                    dp[i][j] = dp[i - 1][j];
            }
        }
        return dp[coins.length][amount];
    }

    /**
     * 状态压缩
     */
    public int change2(int amount, int[] coins) {
        if (coins == null) return 0;
        // 此处的dp[j]相当于dp[i-1][j]
        int[] dp = new int[amount + 1];
        // base case
        dp[0] = 1;
        // 若只使用前i种硬币
        for (int i = 1; i <= coins.length; ++i) {
            // 对于各种容量的背包
            for (int j = 0; j <= amount; j++) {
                // 背包容量够
                if (j >= coins[i - 1]) {
                    // 当前硬币可放入，也可不放入
                    dp[j] = dp[j] + dp[j - coins[i - 1]];
                }
                // 背包容量不够，当前硬币无法放入
                // else
                //     dp[j] = dp[j];
            }
        }
        return dp[amount];
    }
}
