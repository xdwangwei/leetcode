package com.recursion;

/**
 * @author wangwei
 * 2020/7/20 9:24
 *
 * 斐波那契数，通常用 F(n) 表示，形成的序列称为斐波那契数列。该数列由 0 和 1 开始，后面的每一项数字都是前面两项数字的和。也就是：
 *
 * F(0) = 0,   F(1) = 1
 * F(N) = F(N - 1) + F(N - 2), 其中 N > 1.
 * 给定 N，计算 F(N)。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：2
 * 输出：1
 * 解释：F(2) = F(1) + F(0) = 1 + 0 = 1.
 * 示例 2：
 *
 * 输入：3
 * 输出：2
 * 解释：F(3) = F(2) + F(1) = 1 + 1 = 2.
 * 示例 3：
 *
 * 输入：4
 * 输出：3
 * 解释：F(4) = F(3) + F(2) = 2 + 1 = 3.
 *  
 *
 * 提示：
 *
 * 0 ≤ N ≤ 30
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/fibonacci-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _509_FiboNumber {

    public int fib(int N) {
        if (N < 1) return 0;
        // return solution1(N);
        // int[] memo = new int[N + 1];
        // return solution2(memo, N);
        return solution3(N);
        // return solution4(N);
        // return solution5(N);
    }

    /**
     * 递归
     * @param n
     * @return
     */
    private int solution1(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return solution1(n - 1) + solution1(n - 2);
    }

    /**
     * 带备忘录的递归
     * @param n
     * @return
     */
    private int solution2(int[] memo, int n) {
        // 已经计算过
        if (memo[n] != 0) return memo[n];
        // 初始
        if (n == 0) return 0;
        if (n == 1) return 1;
        // 计算并保存进备忘录
        memo[n] = solution2(memo, n - 1) + solution2(memo, n - 2);
        return memo[n];
    }

    /**
     * 自底向上（动态规划）
     * @param n
     * @return
     */
    private int solution3(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; ++i) dp[i] = dp[i - 1] + dp[i - 2];
        return dp[n];
    }

    /**
     * 自底向上（动态规划）降维处理 == 备忘录
     *
     * 只需要两个位置保存两个状态值
     * @param n
     * @return
     */
    private int solution4(int n) {
        int prev = 0, curr = 1;
        for (int i = 2; i <= n; ++i) {
            int sum = prev + curr;
            prev = curr;
            curr = sum;
        }
        return curr;
    }

    public static void main(String[] args) {
        int[] arr = new int[5];
        for (int i : arr) {
            System.out.println(i);
        }
    }
}
