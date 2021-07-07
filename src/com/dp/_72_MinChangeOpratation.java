package com.dp;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/19 周二 20:44
 **/
public class _72_MinChangeOpratation {
    // dp[i][j]表示把长度为i的字符串转为长度为j的字符串所需要的最少操作数目
    // 长度为i的字符串，第i个字符串的下标是i-1
    // 具体分析见微信收藏
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
