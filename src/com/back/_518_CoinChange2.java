package com.back;

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

    private int res = 0;

    public int change(int amount, int[] coins) {
        if (coins == null) return 0;
        if (amount == 0) return 1;
        back(coins, amount, 0);
        return res;
    }

    /**
     * 回溯 超时
     *
     * @param coins
     * @param need       还需要凑多少钱
     * @param startIndex 每次可以选择coins数组中任意一个银币，但为了去重，加了一个限制范围
     *                   比如 5 [1,2,3]
     *                   我可以先 1 再 1 再 1 再 2
     *                   我也可以 先 2 再 1 再 1 再 1
     *                   因为数组本身有序(暗含)，所以我们每次从当前硬币及之后的硬币向下扩展
     */
    private void back(int[] coins, int need, int startIndex) {
        // 找到一种方案
        if (need == 0) {
            res += 1;
            return;
        }
        // 可以选择 当前指定范围内的硬币
        for (int i = startIndex; i < coins.length; i++) {
            // 再排除掉不合法的选择
            if (need - coins[i] < 0)
                continue;
            back(coins, need - coins[i], i);
        }
    }


    /**
     * 动态规划 自下而上
     * <p>
     * 这个问题特殊的地方在于去重 也就是说  1 2 3 和 3 2 1是一种方案，也就是说，求的是组合数
     * 如果我们这样写循环，求的就是排列数
     * for (int coin : coins) {
     *             for (int i = 1; i <= amount; ++i) {
     *                     dp[i] += dp[i - coin];
     *             }
     * }
     * 比如 [7] 可以由 [4] 和 [3] 组成， 这样写会认为 先用3再用4和先用4再用3是两种方案
     * 也就是说，如果先考虑金额，那么在每种金额下，去考虑硬币，就会出现硬币顺序反一下，就被认为是不同方案
     *
     * 我们只需要交换循环顺序，就可以把排列变为组合
     *         for (int coin : coins) {
     *             for (int i = 1; i <= amount; ++i) {
     *                     // 可以是已有方案
     *                     dp[i] += dp[i - coin];
     *             }
     *         }
     *  我们先考虑硬币，对于每个硬币，使用它的情况下，去更新组成每种金额的组合数
     *  这样我们的银币使用顺序就是唯一的，要么ABC要么ACB要么BAC，因为是按顺序此役扫描过的coins数组
     *
     * @param amount
     * @param coins
     * @return
     */
    public int change2(int amount, int[] coins) {
        if (coins == null) return 0;
        // dp[i] 凑够金额i的银币组合方案数
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        // 使用当前硬币的情况下
        for (int coin : coins) {
            // 更新凑成每种金额的方案数
            for (int i = 1; i <= amount; ++i) {
                // 首先这个银币能凑够
                if (i - coin >= 0)
                    // 问题本身是二维的，因为硬币随便取，所以取消了硬币数目这个维度的限制
                    // 在降维的情况下，这是迭代更新，dp[i]保存的是之前的状态相当于dp[j-1][i]
                    // 刚开始时 dp[] = {0},只有dp[0] = 1,这也是为什么 i- coin == 0也要考虑
                    dp[i] += dp[i - coin];
            }
        }
        return dp[amount];
    }

    public static void main(String[] args) {
        _518_CoinChange2 obj = new _518_CoinChange2();
        System.out.println(obj.change(5, new int[]{1, 2, 5}));
    }
}
