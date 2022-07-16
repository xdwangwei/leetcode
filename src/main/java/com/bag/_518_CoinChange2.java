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
     * 我们最终想得到的答案就是 dp[N][amount]，其中 N 为 coins 数组的大小。(前N种面额的硬币，凑出金额amount)
     * 首先由于 i 是从 1 开始的，所以 coins 的索引是 i-1 时表示第 i 个硬币的面值。
     *
     * 注意状态转移方程与01背包的区别
     *
     * 01背包中dp[i][j]中的i代表的是这一个，完全背包中代表的是这一种(同种面额有无数个)
     *      0-1背包：dp[i][j] = dp[i - 1][j] + dp[i - 1][j - coins[i - 1]];
     *      完全背包：dp[i][j] = dp[i - 1][j] + dp[  i  ][j - coins[i - 1]];
     *
     * 关于 完全背包的状态转移方程的数学推导：
     * https://leetcode-cn.com/problems/coin-change-2/solution/dong-tai-gui-hua-wan-quan-bei-bao-wen-ti-by-liweiw/
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
                    // 如果选择不放入，那么 就等于 是 从前i-1个（种）面额硬币中凑出金额amount
                    // 如果选择放入，那么应该由之前的硬币先凑出 amount - coins[i - 1]
                    // 对于01背包，每个面额硬币只有一个，所以 之前的硬币就是 dp[i-1]
                    // 但对于完全背包，硬币是无限的，所以此处仍然是dp[i]，（我现在在考虑硬币coins[i]，实际上这个硬币之前也可以用）
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

    /**
     * 直接一维动态规划
     *
     * 这道题中，给定总金额 amount 和数组 coins[]，要求计算金额之和等于 amount 的硬币组合数。
     * 其中，coins 的每个元素可以选取多次，且不考虑选取元素的顺序，因此这道题需要计算的是选取硬币的【组合】数。
     *
     * 可以通过动态规划的方法计算可能的组合数。用 dp[x] 表示金额之和等于 x 的硬币组合数，目标是求 dp[amount]。
     *
     * 动态规划的边界是 dp[0]=1。只有当不选取任何硬币时，金额之和才为 0，因此只有 1 种硬币组合。
     *
     * 对于面额为 coin 的硬币，当  coin ≤ i ≤ amount 时，如果存在一种硬币组合的金额之和等于 i −coin，则在该硬币组合中增加一个面额为 coin 的硬币，即可得到一种金额之和等于 ii的硬币组合。
     * 因此需要遍历 coins，【对于其中的每一种面额的硬币，更新数组 dp】 中的每个大于或等于该面额的元素的值。
     *  所以 外循环 遍历 coins，内循环遍历 dp
     * 由此可以得到动态规划的做法：
     *
     *  初始化 dp[0]=1；
     *
     * 遍历coins，对于其中的每个元素coin，进行如下操作：
     *
     * 遍历 i 从 coin 到 amount，将dp[i−coin] 的值加到 dp[i]。
     * 最终得到 dp[amount] 的值即为答案。
     *
     * 上述做法不会重复计算不同的排列。
     * 因为外层循环是遍历数组 \coins 的值，内层循环是遍历不同的金额之和，在计算 dp[i] 的值时，可以确保金额之和等于 i的硬币面额的顺序，
     * 由于顺序确定，因此不会重复计算不同的排列。
     *
     * 例如，coins=[1,2]，对于 dp[3] 的计算，一定是先遍历硬币面额 1后遍历硬币面额 2，只会出现以下 2 种组合：
     *
     * 3 = 1+1+1 ==  1+2
     *
     *
     * 硬币面额 2 不可能出现在硬币面额 1 之前，即不会重复计算 3=2+1 的情况。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/coin-change-2/solution/ling-qian-dui-huan-ii-by-leetcode-soluti-f7uh/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    public int change8(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        // 外循环遍历coins
        for (int coin : coins) {
            // 内循环更新dp
            // 这样就实现了每一个coin都对每一个dp生效，也就相当于每个硬币都被用了无数次
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        return dp[amount];
    }
}
