package com.dp;

/**
 * @author wangwei
 * 2021/12/9 14:25
 *
 * 给定两个单词word1和word2，找到使得word1和word2相同所需的最小步数，每步可以删除任意一个字符串中的一个字符。
 *
 * 示例：
 *
 * 输入: "sea", "eat"
 * 输出: 2
 * 解释: 第一步将"sea"变为"ea"，第二步将"eat"变为"ea"
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/delete-operation-for-two-strings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _583_DeleteOptionForTwoStrings {

    /**
     * 动态规划
     *
     * 假设字符串 word1 和 word2 的长度分别为 m 和 n，创建 m+1 行 n+1 列的二维数组 dp，
     * 其中 dp[i][j] 表示使word1[0:i] 和 word2[0:j] 相同的最少删除操作次数。
     *
     * 上述表示中，word1[0...i]表示word1前i个字符，word2[0...j]表示word2前j个字符
     * dp[0][0] 表示空到空的情况
     *
     * 动态规划的边界情况如下：
     *
     * 空字符串和任何字符串要变成相同，只有将另一个字符串的字符全部删除，
     * 因此对于 i = 0, 有 dp[0][j] = j，对于 j = 0 有， dp[i][0] = i
     *
     * 状态转移：
     *   dp的下标代表的是字符在原串中的序号，不是索引，多了个1
     * 对于 dp[i][j]，
     *      如果 word1[i-1] == word2[j - 1]，当前字符相同，那么 将 word1[0...i]变成word2[0...j]就等同于将word1[0...i-1]变成word2[0...j-1]
     *      如果 word1[i-1] != word2[j - 1]，当前字符不同，
     *          要么把word1[0...i]变成word2[0...j-1]，再把word2当前字符删了，
     *          要么把word1[0...i-1]变成word2[0...j],再把word1当前字符删了
     *      应取两项中较小的一项，dp[i][j] = min(dp[i−1][j], dp[i][j−1]) + 1。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/delete-operation-for-two-strings/solution/liang-ge-zi-fu-chuan-de-shan-chu-cao-zuo-14uw/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param word1
     * @param word2
     * @return
     */
    public int minDistance(String word1, String word2) {
        if (word1 == null || word2 == null) {
            return 0;
        }
        int m = word1.length(), n = word2.length();
        if (m == 0) {
            return n;
        }
        if (n == 0) {
            return m;
        }
        // dp[i][j] 表示使word1[0:i] 和 word2[0:j] 相同的最少删除操作次数。
        int[][] dp = new int[m + 1][n + 1];
        // base case
        for (int i = 0; i <= m; ++i) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; ++j) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= m; ++i) {
            char ci = word1.charAt(i - 1);
            for (int j = 1; j <= n; ++j) {
                char cj = word2.charAt(j - 1);
                // 当前字符相同
                if (ci == cj) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
                // 当前字符不同
                else {
                    dp[i][j] = Math.min(dp[i][j - 1], dp[i - 1][j]) + 1;
                }
            }
        }
        return dp[m][n];
    }

    /**
     * 方法二：最长公共子序列
     *
     * 分别删除若干字符之后使得两个字符串相同，则剩下的字符为两个字符串的公共子序列。
     * 为了使删除操作的次数最少，剩下的字符应尽可能多。
     * 当剩下的字符为两个字符串的最长公共子序列时，删除操作的次数最少。
     * 因此，可以计算两个字符串的最长公共子序列的长度，
     * 然后分别计算两个字符串的长度和最长公共子序列的长度之差，即为两个字符串分别需要删除的字符数，
     * 两个字符串各自需要删除的字符数之和即为最少的删除操作的总次数。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/delete-operation-for-two-strings/solution/liang-ge-zi-fu-chuan-de-shan-chu-cao-zuo-14uw/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int minDistance2(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            char c1 = word1.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                char c2 = word2.charAt(j - 1);
                if (c1 == c2) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        int lcs = dp[m][n];
        return m - lcs + n - lcs;
    }

}
