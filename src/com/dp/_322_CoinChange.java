package com.dp;

import java.util.Arrays;

/**
 * @author wangwei
 * 2020/4/25 21:47
 * <p>
 * 给定不同面额的硬币 coins 和一个总金额 amount。编写一个函数来计算可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1。
 * <p>
 * 示例 1:
 * <p>
 * 输入: coins = [1, 2, 5], amount = 11
 * 输出: 3
 * 解释: 11 = 5 + 5 + 1
 * 示例 2:
 * <p>
 * 输入: coins = [2], amount = 3
 * 输出: -1
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/coin-change
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _322_CoinChange {

    private int res = Integer.MAX_VALUE;

    public int coinChange(int[] coins, int amount) {
        if (amount == 0) return 0;
        if (coins == null || coins.length <= 0) return -1;
        back(coins, 0, amount);
        return res < Integer.MAX_VALUE ? res : -1;
    }

    /**
     * 回溯，尝试所有可能
     * 超时
     *
     * @param coins
     * @param count  当前已用到的硬币数
     * @param amount 还需要多少钱
     */
    public void back(int[] coins, int count, int amount) {
        // 已凑够
        if (amount == 0) {
            // 当前方案用了count个硬币
            res = Math.min(res, count);
            return;
        }
        // 遍历，选择哪个面额的硬币
        for (int coin : coins) {
            // 排除不合法选择
            if (coin > amount)
                continue;
            back(coins, count + 1, amount - coin);
        }
    }

    /**
     * 递归
     * @param coins
     * @param amount
     * @return
     */
    public int coinChange1(int[] coins, int amount) {
        if (amount == 0) return 0;
        if (coins == null || coins.length <= 0) return -1;
        // return helper2(coins, amount);

        int[] memo = new int[amount + 1];
        return helper2(memo, coins, amount);
    }

    /**
     * 超时
     * @param coins
     * @param amount
     * @return
     */
    private int helper(int[] coins, int amount) {
        // base case
        if (amount == 0) return 0;
        // 求子问题可能会遇到amount < 0
        if (amount < 0) return -1;
        // 当前问题的最优解
        int res = Integer.MAX_VALUE;
        // 逐个选择硬币
        for (int coin: coins) {
            // 求子问题最优解
            int subRes = helper(coins, amount - coin);
            if (subRes == -1) continue;
            res = Math.min(res, 1 + subRes);
        }
        // 当前问题是否存在最优解
        return res == Integer.MAX_VALUE ? -1 : res;
    }

    /**
     * 带备忘录
     * @param coins
     * @param amount
     * @return
     */
    private int helper2(int[] memo, int[] coins, int amount) {
        // base case
        if (amount == 0) return 0;
        // 求子问题可能会遇到amount < 0
        if (amount < 0) return -1;
        // 已计算过，放在这里以免出现-1越界
        if (memo[amount] != 0) return memo[amount];
        // 当前问题的最优解
        int res = Integer.MAX_VALUE;
        // 逐个选择硬币
        for (int coin: coins) {
            // 求子问题最优解
            int subRes = helper2(memo, coins, amount - coin);
            if (subRes == -1) continue;
            // 更新当前问题最优解
            res = Math.min(res, 1 + subRes);
        }
        // 保存进备忘录
        memo[amount] = res == Integer.MAX_VALUE ? -1 : res;
        // 当前最优解
        return  memo[amount];
    }

    /**
     * 动态规划，自下而上 == 备忘录
     *
     * @param coins
     * @param amount
     * @return
     */
    public int coinChange2(int[] coins, int amount) {
        if (coins == null || coins.length < 0) return -1;
        // 因为硬币可以随便取，所以状态只需要考虑 当前金额 一维
        // dp[i] 表示凑够金额为i，需要的最少的硬币数
        int[] dp = new int[amount + 1];
        // 初始化
        // 这里要注意，如果初始化为MAX, 可能在状态转移方程种，刚开始某个 dp[i-coin] = Integer.Max_Vlue
        // 再 +1 会溢出，导致它的结果成了 Integer.MIN_VALUE
        // Arrays.fill(dp, Integer.MAX_VALUE);
        // 这里我们可以初始化成 amount + 1，为啥？？因为凑成总数为amount，最多也就全是1块的，需要amount个
        Arrays.fill(dp, amount + 1);
        // 凑够0元，不需要硬币
        dp[0] = 0;
        // 对于每种金额
        for (int i = 1; i <= amount; i++) {
            // 选择硬币
            for (int coin : coins) {
                // 这种情况下，i 元 可以 从 i - coin 元， + 这个硬币 得到
                if (i >= coin)
                    // 这里要注意，如果初始化为MAX, 可能刚开始某个 dp[i-coin] = Integer.Max_Vlue
                    // 但是 +1 会溢出，导致它的结果成了 Integer.MIN_VALUE
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
        // 需要先判断当前金额是否存在最优解
        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    /**
     * 因为这个题不用纠结 排列 还是 组合 问题， 也就是 对于 123 和231 是否重复统计无关紧要，
     * 我们关心的是使用的最少硬币，重复情况下，数目是一样的，所以我们可以交换for循环顺序，
     * 转为求 组合 形式的写法，这样我们可以省略 上面循环中的那个 if，提高效率
     * @param coins
     * @param amount
     * @return
     */
    public int coinChange3(int[] coins, int amount) {
        if (coins == null || coins.length < 0) return -1;
        // dp[i] 表示凑够金额为i，需要的最少的硬币数
        int[] dp = new int[amount+1];
        // 初始化，最多全选1块的，需要amount个
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins) {
            // x 直接从 coin开始，省略if，每个硬币，一定是小于总数amount的，不然也不用凑了
            for (int x = coin; x <= amount; ++x){
                // 直接更新
                dp[x] = Math.min(dp[x], dp[x-coin] + 1);
            }
        }
        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }
}
