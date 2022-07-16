package com.dp;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/19 周二 20:44
 *
 * 给你两个单词 word1 和 word2，请你计算出将 word1 转换成 word2 所使用的最少操作数 。
 *
 * 你可以对一个单词进行如下三种操作：
 *
 * 插入一个字符
 * 删除一个字符
 * 替换一个字符
 *  
 *
 * 示例 1：
 *
 * 输入：word1 = "horse", word2 = "ros"
 * 输出：3
 * 解释：
 * horse -> rorse (将 'h' 替换为 'r')
 * rorse -> rose (删除 'r')
 * rose -> ros (删除 'e')
 * 示例 2：
 *
 * 输入：word1 = "intention", word2 = "execution"
 * 输出：5
 * 解释：
 * intention -> inention (删除 't')
 * inention -> enention (将 'i' 替换为 'e')
 * enention -> exention (将 'n' 替换为 'x')
 * exention -> exection (将 'n' 替换为 'c')
 * exection -> execution (插入 'u')
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/edit-distance
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 **/
public class _72_MinChangeOpratation {
    /**
     * 动态规划
     *
     * 本质上与 583：最少的删除次数把s1和s2变一样
     *         712：最小的删除字母ascii总和把s1和s2变一样
     *       一个思路。
     *       只是上面两个题，每次只能删除一个字符
     *       而本题，每次可以 插入 、删除、修改 一个字符
     *
     * 假设字符串 word1 和 word2 的长度分别为 m 和 n，创建 m+1 行 n+1 列的二维数组 dp，
     * 其中 dp[i][j] 表示使word1[0:i] 和 word2[0:j] 相同的最少操作次数。
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
     *      如果 word1[i-1] != word2[j - 1]，当前字符不同，三种选择
     *          要么把word1[0...i]变成word2[0...j-1]，再把word2当前字符删了，就匹配了   ---- 删除
     *          要么把word1[0...i-1]变成word2[0...j],再给word1后面补一个word2当前字符，就匹配了    ---- 插入
     *          要么在word[0...i-1] word2[0...j-1] 的基础上，把word1当前字符改成和word2当前字符一样  ---- 编辑
     *      应取三者中较小的一项，dp[i][j] = min(dp[i−1][j], dp[i][j−1], dp[i-1][j-1]) + 1。
     *
     *
     * @param word1
     * @param word2
     * @return
     */
    public static int solution(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        // 对于边界情况，一个空串和一个非空串的编辑距离为 D[i][0] = i 和 D[0][j] = j，
        // D[i][0] 相当于对 word1 执行 i 次删除操作，D[0][j] 相当于对 空串执行 j 次插入操作。
        //把长度为i的串转为长度为0的串需要i次删除/dp[i-1][0]+1，在上次的基础上再删一个字符
        for (int i = 0; i <= m; i++)
            dp[i][0] = i;
        //把长度为0的串转为长度为j的串需要j次增加/或者dp[0][j-1]+1,在上次的基础上再加一个字符
        for (int j = 0; j <= n; j++)
            dp[0][j] = j;
        // 状态转移
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++) {
                // 两串最后一个字符一样，相当于只转换前面部分
                if (word1.charAt(i - 1) == word2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                    // 两串最后一个字符不一样，有三种转换方式，取操作次数最少的
                else
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1], dp[i][j - 1]), dp[i - 1][j]) + 1;
            }
        return dp[m][n];
    }
}
