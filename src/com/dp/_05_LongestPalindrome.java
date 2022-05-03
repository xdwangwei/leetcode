package com.dp;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/24 周日 19:27
 * <p>
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
 * <p>
 * 示例 1：
 * <p>
 * 输入: "babad"
 * 输出: "bab"
 * 注意: "aba" 也是一个有效答案。
 * 示例 2：
 * <p>
 * 输入: "cbbd"
 * 输出: "bb"
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-palindromic-substring
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 **/
public class _05_LongestPalindrome {


    // s[0],s[1]...s[i],s[i+1],s[i+2]...s[j-2],s[j-1],s[j]...
    // 如果 S[i+1,j-1] 是回文串，那么只要 S[i] == S[j]，就可以确定 S[i,j]也是回文串
    // 长度为1和2时的字串需单独判断
    // dp[i][j]代表s[i][j]是不是回文子串
    public static String solution(String s) {
        if (s.length() == 0 || s == null) return "";
        boolean dp[][] = new boolean[s.length()][s.length()];
        int maxLen = 0;
        String res = "";
        for (int len = 1; len <= s.length(); len++) //对于所有长度的字串
            for (int i = 0; i < s.length(); i++) {
                int j = i + len - 1; // i是起点,j是终点，长度是len
                if (j >= s.length()) break; // 当前情况不可能，不存在从i开始长为len的子串
                if (len == 1) dp[i][j] = true;
                else if (len == 2) dp[i][j] = s.charAt(i) == s.charAt(j);
                else dp[i][j] = dp[i + 1][j - 1] && s.charAt(i) == s.charAt(j);
                if (dp[i][j] && len > maxLen) { // 当前串是回文串且比上一次的更长
                    res = s.substring(i, j + 1);
                }
            }
        return res;
    }

    /**
     * dp[j][i] 表示字符串从 j 到 i 是否是为回文串，即当 s[j] == s[i] 如果 dp[j+1][i-1] 也是回文串，那么字符串从 j 到 i 也是回文串，即 dp[j][i] 为真
     *
     * 作者：powcai
     * 链接：https://leetcode-cn.com/problems/longest-palindromic-substring/solution/duo-chong-si-lu-qiu-jie-by-powcai/
     * @param s
     * @return
     */
    public String longestPalindrome(String s) {
        int n = s.length();
        String res = "";
        boolean[][] dp = new boolean[n][n];
        for(int i = 0 ;i < n; i++){
            for(int j = 0; j <= i ; j++){
                // i - j <2 是为了处理 i == j 和 i == j + 1,否则j+1,i-1会直接错过
                if(s.charAt(i) == s.charAt(j) && ( i - j < 2 || dp[j+1][i-1]))
                    dp[j][i] = true;
                if (dp[j][i] && (i - j + 1 > res.length())){
                    res = s.substring(j,i+1);
                }
            }
        }
        return res;
    }

    // 回文中心的两侧互为镜像。因此，回文可以从它的中心展开，并且只有 2n−1 个这样的中心。
    // 所含字母数为偶数的回文的中心可以处于两字母之间（如“abba”的中心在两个'b’之间）
    public static String solution2(String s) {
        if (s == null || s.length() == 0) return "";
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i); // 相当于以s[i]为中心扩展
            int len2 = expandAroundCenter(s, i, i + 1); // 相当于以s[i]和s[i+1]中心的空为空心扩展
            int len = Math.max(len1, len2);
            // 这几个取值分析一下长度为1获2的回文串就得到了
            // 上次的串长为 end - start + 1
            // 这里加不加1都行,不加1代表本次取得的串大于等于上次的串都更新结果
            // 加1代表本次取得的串大于上次的串才更新结果
            if (len > end - start + 1) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private static int expandAroundCenter(String s, int left, int right) {
        int L = left, R = right;
        while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
            L--; // 中心向左扩张
            R++; // 中心向右扩张
        }
        return R - L - 1;
    }
}
