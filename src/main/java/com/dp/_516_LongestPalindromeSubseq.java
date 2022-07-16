package com.dp;

/**
 * @author wangwei
 * 2020/7/22 8:42
 *
 * 给定一个字符串 s ，找到其中最长的回文子序列，并返回该序列的长度。可以假设 s 的最大长度为 1000 。
 *
 *
 *
 * 示例 1:
 * 输入:
 *
 * "bbbab"
 * 输出:
 *
 * 4
 * 一个可能的最长回文子序列为 "bbbb"。
 *
 * 示例 2:
 * 输入:
 *
 * "cbbd"
 * 输出:
 *
 * 2
 * 一个可能的最长回文子序列为 "bb"。
 *
 *  
 *
 * 提示：
 *
 * 1 <= s.length <= 1000
 * s 只包含小写英文字母
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-palindromic-subsequence
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _516_LongestPalindromeSubseq {

    /**
     * 这个问题对 dp 数组的定义是：在子串 s[i..j] 中，最长回文子序列的长度为 dp[i][j]
     * 那么最后要返回的是 dp[0][n-1]， 所以 是从 中间 往两边 递推，对于 dp[i][j] 要考虑 dp[i + 1][j - 1]
     *
     * 假设你知道了子问题 dp[i+1][j-1] 的结果（s[i+1..j-1] 中最长回文子序列的长度），
     * 你是否能想办法算出 dp[i][j] 的值（s[i..j] 中，最长回文子序列的长度）呢？
     * 可以！这取决于 s[i] 和 s[j] 的字符：
     *
     * 如果它俩相等，
     *      那么 它俩 加上 s[i+1..j-1] 中的最长回文子序列就是 s[i..j] 的最长回文子序列：
     * 如果它俩不相等，说明它俩不可能同时出现在 s[i..j] 的最长回文子序列中，
     *      那么把它俩分别加入 s[i+1..j-1] 中，看看哪个子串产生的回文子序列更长即可：
     *
     *  if (s[i] == s[j])
     *     // 它俩一定在最长回文子序列中
     *     dp[i][j] = dp[i + 1][j - 1] + 2;
     * else
     *     // s[i+1..j] 和 s[i..j-1] 谁的回文子序列更长？
     *     dp[i][j] = max(dp[i + 1][j], dp[i][j - 1]);
     *
     * 首先明确一下 base case，如果只有一个字符，显然最长回文子序列长度是 1，也就是 dp[i][j] = 1 (i == j)。
     *
     * 因为 i 肯定小于等于 j，所以对于那些 i > j 的位置，根本不存在什么子序列，应该初始化为 0。
     *
     * 另外，看看刚才写的状态转移方程，想求 dp[i][j] 需要知道 dp[i+1][j-1]，dp[i+1][j]，dp[i][j-1] 这三个位置；再看看我们确定的 base case
     * 为了保证每次计算 dp[i][j]，左下右方向的位置已经被计算出来，只能斜着遍历或者反着遍历：
     *
     * @param s
     * @return
     */
    public int longestPalindromeSubseq(String s) {
        if (s == null || s.length() == 0) return 0;
        int n = s.length();
        // dp[i][j] 表示 在子串 s[i..j] 中，最长回文子序列的长度
        int[][] dp = new int[n][n];
        // base case
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        // 状态转移
        // 注意倒着遍历
        for (int i = n - 1; i >= 0; --i) {
            for (int j = i + 1; j < n; j++) {
                // 当前字符相等
                if (s.charAt(i) == s.charAt(j))
                    // 它俩一定在最长回文子序列中
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                else
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
            }
        }
        return dp[0][n - 1];
    }

    /**
     * 方法二：将 s 翻转 得到 t， 求 s 和 t 的最长公共子序列，证明？ 略
     */
}
