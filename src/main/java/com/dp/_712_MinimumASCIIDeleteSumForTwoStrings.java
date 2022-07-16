package com.dp;

/**
 * @author wangwei
 * 2021/12/9 14:50
 * 给定两个字符串s1, s2，找到使两个字符串相等所需删除字符的ASCII值的最小和。
 *
 * 示例 1:
 *
 * 输入: s1 = "sea", s2 = "eat"
 * 输出: 231
 * 解释: 在 "sea" 中删除 "s" 并将 "s" 的值(115)加入总和。
 * 在 "eat" 中删除 "t" 并将 116 加入总和。
 * 结束时，两个字符串相等，115 + 116 = 231 就是符合条件的最小和。
 * 示例2:
 *
 * 输入: s1 = "delete", s2 = "leet"
 * 输出: 403
 * 解释: 在 "delete" 中删除 "dee" 字符串变成 "let"，
 * 将 100[d]+101[e]+101[e] 加入总和。在 "leet" 中删除 "e" 将 101[e] 加入总和。
 * 结束时，两个字符串都等于 "let"，结果即为 100+101+101+101 = 403 。
 * 如果改为将两个字符串转换为 "lee" 或 "eet"，我们会得到 433 或 417 的结果，比答案更大。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/minimum-ascii-delete-sum-for-two-strings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _712_MinimumASCIIDeleteSumForTwoStrings {

    /**
     * 动态规划
     *
     * 这个题本质上和 _583_ 以最少的删除次数，把s1和s2变一样， 是 一样的
     * 只不过，538 要求的是 删除次数最少，本题要求的是删除的字符的ascii最小
     * 每次删除，就两种情况，要么删s1[i]，要么删s2[j]，这个和538是一样的，所以只需要稍微改动状态转移方程
     *
     * 假设字符串 word1 和 word2 的长度分别为 m 和 n，创建 m+1 行 n+1 列的二维数组 dp，
     *      * 其中 dp[i][j] 表示使word1[0:i] 和 word2[0:j] 相同的最少删除ascii总和。
     *      *
     *      * 上述表示中，word1[0...i]表示word1前i个字符，word2[0...j]表示word2前j个字符
     *      * dp[0][0] 表示空到空的情况
     *      *
     *      * 动态规划的边界情况如下：
     *      *
     *      * 空字符串和任何字符串要变成相同，只有将另一个字符串的字符全部删除，
     *      * 因此对于 i = 0, 有 dp[0][j] = s2前j个字符的ascii总和，对于 j = 0 有， dp[i][0] = s1前i个字符的ascii总和
     * 状态转移：
     *   dp的下标代表的是字符在原串中的序号，不是索引，多了个1
     * 对于 dp[i][j]，
     *      如果 word1[i-1] == word2[j - 1]，当前字符相同，那么 将 word1[0...i]变成word2[0...j]就等同于将word1[0...i-1]变成word2[0...j-1]
     *      如果 word1[i-1] != word2[j - 1]，当前字符不同，
     *          要么把word1[0...i]变成word2[0...j-1]，再把word2当前字符删了，
     *          要么把word1[0...i-1]变成word2[0...j],再把word1当前字符删了
     *      应取两项中较小的一项，dp[i][j] = min(dp[i−1][j] + s1当前字符的ascii, dp[i][j−1] + s2当前字符的ascii)。
     * @param s1
     * @param s2
     * @return
     */
    public int minimumDeleteSum(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0;
        }
        int m = s1.length(), n = s2.length();
        // 为了方便初始化 base case，把 s1 和 s2 的前缀和计算出来
        int[] preSum1 = new int[m];
        int[] preSum2 = new int[n];
        preSum1[0] = s1.charAt(0);
        for (int i = 1; i < m; ++i) {
            preSum1[i] = preSum1[i - 1] + s1.charAt(i);
        }
        preSum2[0] = s2.charAt(0);
        for (int j = 1; j < n; ++j) {
            preSum2[j] = preSum2[j - 1] + s2.charAt(j);
        }
        // s1为空，删除s2全部字符
        if (m == 0) {
            return preSum2[n - 1];
        }
        // s2为空，删除s1全部字符
        if (n == 0) {
            return preSum1[m - 1];
        }
        int[][] dp = new int[m + 1][n + 1];
        // base case
        // j=0代表把s1前i个字符变成空，相当于前i个字符的ascii总和
        for (int i = 1; i <= m; ++i) {
            dp[i][0] = preSum1[i - 1];
        }
        // i=0代表把s2前j个字符变成空，相当于前j个字符的ascii总和
        for (int j = 1; j <= n; ++j) {
            dp[0][j] = preSum2[j - 1];
        }
        // 状态转移
        for (int i = 1; i <= m; ++i) {
            char ci = s1.charAt(i - 1);
            for (int j = 1; j <= n; ++j) {
                char cj = s2.charAt(j - 1);
                // 当前字符相同
                if (ci == cj) {
                    dp[i][j] = dp[i - 1][j - 1];
                // 当前字符不同，两种选择
                } else {
                    // dp[i][j] = Math.min(dp[i][j - 1], dp[i - 1][j]) + 1;
                    // 要么 在 dp[i][j - 1]  的基础上，删除 s2 当前字符
                    // 要么 在 dp[i - 1][j] 的基础上， 删除 s1 当前字符
                    // 选择结果较小的那个
                    dp[i][j] = Math.min(dp[i][j - 1] + cj, dp[i - 1][j] + ci);
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        _712_MinimumASCIIDeleteSumForTwoStrings obj = new _712_MinimumASCIIDeleteSumForTwoStrings();
        obj.minimumDeleteSum("set", "eat");
    }
}
