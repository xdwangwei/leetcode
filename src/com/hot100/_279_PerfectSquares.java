package com.hot100;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/4/22 15:50
 * @description: _279_PerfectSquares
 *
 * 279. 完全平方数
 * 给你一个整数 n ，返回 和为 n 的完全平方数的最少数量 。
 *
 * 完全平方数 是一个整数，其值等于另一个整数的平方；换句话说，其值等于一个整数自乘的积。例如，1、4、9 和 16 都是完全平方数，而 3 和 11 不是。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 12
 * 输出：3
 * 解释：12 = 4 + 4 + 4
 * 示例 2：
 *
 * 输入：n = 13
 * 输出：2
 * 解释：13 = 4 + 9
 *
 * 提示：
 *
 * 1 <= n <= 104
 */
public class _279_PerfectSquares {

    /**
     * 通过联想背包问题，写出了动态规划。。。。。。。。。。。。
     * 首先 对于 仍和一个数字 n， 可以由 n 个 1 组成
     * 然后：假如现在 有 j 个平方数字，1，4，16，25.。。。
     * 对于 n 来说，要用这些平方数凑出n，每一次可以选择 任意一共平方数，那么最少需要几个？
     * 假如 dp[i] 表示凑出n所需要的最少的这些平方数个数，
     * 那么 对于 可选泽的平方数，dp[i] = min(dp[i], dp[i - j * j])
     *
     * 一：有哪些平方数：题目给出了n，那么就有，1，4，16，。。。，sqrt(n) * sqrt(n)
     * 二：对于 i 来说，它最多只需要考虑 到 sqrt(i) * sqrt(i) 这个平方数
     * 联想背包问题（硬币问题）：
     * 现在有以下面额的硬币：1，4，16，。。。
     * 有目标价格 n，问至少需要几个硬币完成付款
     * @param n
     * @return
     */
    public int numSquares(int n) {
        // dp[i] 表示凑出n所需要的最少的这些平方数个数，
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; ++i) {
            // 首先，每个数字i可以由i个1凑出来
            dp[i] = i;
            // 递推方程式，尝试枚举不同的平方数 j * j, 其余部分就是 i - j*j，这种选择下最少凑出i就等于 最少凑出 i-j*j 再加1
            for (int j = 1; j * j <= i; ++j) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }

    /**
     * 动态规划二
     * 对于上面的硬币问题：
     *      * 现在有以下面额的硬币：1，4，16，。。。
     *      * 有目标价格 n，问至少需要几个硬币完成付款
     * 我们可以先去枚举硬币，对于每个硬币，再去考虑它能为价格n做出的贡献，这样就可以省掉很多无用的循环
     * 比如 硬币 16，它至少也得用在 dp[16] 上吧，所以16以前不用考虑
     * @param n
     * @return
     */
    public int numSquares2(int n) {
        // dp[i] 表示凑出n所需要的最少的这些平方数个数，
        int[] dp = new int[n + 1];
        // 提前初始化dp
        for (int i = 1; i <= n; ++i) {
            dp[i] = i;
        }
        // 先枚举硬币面额
        for (int j = 1; j * j <= n; ++j) {
            // 每个面额硬币能为不同的价格做出的贡献
            // 硬币 x 只能为 价格 x 及以上提供服务
            for (int i = j * j; i <= n; ++i) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }

    /**
     * 贪心思想，因为每个面额的硬币都可重复选取
     * 对于价格n至少需要几枚硬币，我去枚举这个硬币数量，1，2，3，4判断在每种数量下，n能否被凑成，如果可以，那直接返回
     * 那怎么判断 价格 n，硬币数量 j，能够凑出来，
     * 枚举所有面额硬币，判断 价格n-x，数量 j - 1 能否凑出来，递归即可
     * @param n
     * @return
     */
    public int numSquares3(int n) {
        Set<Integer> coins = new HashSet<>();
        // 保存可选的所有面额硬币
        for (int i = 1; i * i <= n; ++i) {
            coins.add(i * i);
        }
        // 枚举所需要的硬币数量，贪心思想，从最少的硬币数量从1开始
        for (int j = 1; j <= n; ++j) {
            // 如果 j 个硬币可以凑出n，直接返回j
            if (divided(coins, n, j)) {
                return j;
            }
        }
        return n;
    }

    /**
     * 判断能够用count个硬币凑出总额monry
     * 硬币面额列表是 coins，可重复选取
     * @param coins
     * @param money
     * @param count
     * @return
     */
    private boolean divided(Set<Integer> coins, int money, int count) {
        // 临界，如果只有1枚硬币
        if (count == 1) {
            // 除非存在面额和这个money一样的硬币
            return coins.contains(money);
        }
        // 枚举硬币面额
        for (int coin: coins) {
            // 判断剩下的价格money-coin，能否被 coin - 1个硬币凑出来
            if (divided(coins, money - coin, count - 1)) {
                return true;
            }
        }
        return false;
    }
}
