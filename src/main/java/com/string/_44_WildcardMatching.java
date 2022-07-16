package com.string;

/**
 * @author wangwei
 * 2020/4/6 15:53
 * <p>
 * 给定一个字符串 (s) 和一个字符模式 (p) ，实现一个支持 '?' 和 '*' 的通配符匹配。
 * <p>
 * '?' 可以匹配任何单个字符。
 * '*' 可以匹配任意字符串（包括空字符串）。
 * 两个字符串完全匹配才算匹配成功。
 * <p>
 * 说明:
 * <p>
 * s 可能为空，且只包含从 a-z 的小写字母。
 * p 可能为空，且只包含从 a-z 的小写字母，以及字符 ? 和 *。
 * 示例 1:
 * <p>
 * 输入:
 * s = "aa"
 * p = "a"
 * 输出: false
 * 解释: "a" 无法匹配 "aa" 整个字符串。
 * 示例 2:
 * <p>
 * 输入:
 * s = "aa"
 * p = "*"
 * 输出: true
 * 解释: '*' 可以匹配任意字符串。
 * 示例 3:
 * <p>
 * 输入:
 * s = "cb"
 * p = "?a"
 * 输出: false
 * 解释: '?' 可以匹配 'c', 但第二个 'a' 无法匹配 'b'。
 * 示例 4:
 * <p>
 * 输入:
 * s = "adceb"
 * p = "*a*b"
 * 输出: true
 * 解释: 第一个 '*' 可以匹配空字符串, 第二个 '*' 可以匹配字符串 "dce".
 * 示例 5:
 * <p>
 * 输入:
 * s = "acdcb"
 * p = "a*c?b"
 * 输入: false
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/wildcard-matching
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _44_WildcardMatching {

    /**
     * 动态规划
     *
     * @param s
     * @param p
     * @return
     */
    public boolean isMatch(String s, String p) {
        // dp[i][j]表示s截止到第i个位置(s[i-1])的子串与p截止到第j个位置(p[j-1])的子串是否匹配
        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
        // dp[0][0]表示 空s 和 空p 匹配
        dp[0][0] = true;
        // for循环的开始条件是i=1,也就是没考虑 空s 的匹配情况，dp[0][0]只考虑了 空s匹配空p
        // 但 空s 也可以匹配 *,**,而且对于 *abc*, 空s的匹配情况可以简化
        // 这里对空s的匹配情况进行初始化
        for (int j = 1; j <= p.length(); j++) {
            // s匹配*,相当于s匹配空；s匹配**相当于s匹配*；s匹配*cb*相当于s匹配*cb
            if (p.charAt(j - 1) == '*') dp[0][j] = dp[0][j-1];
        }
        // 注意 i,j表示的是s和p中第几个字符，对应的索引索引是i-1,j-1
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 1; j <= p.length(); j++) {
                // 根据模式串的当前位置的字符来分类讨论
                // 当前位置是 *
                // * 可以匹配任意字符包括空字符，所以考虑 dp[i-1][j-1],dp[i-1][j],dp[i][j-1]这前面已处理过的三个结果
                // 但其实 dp[i-1][j-1]不用考虑，因为考虑dp[i-1][j]时也会执行这个if，还是会考虑到dp[i-1][j-1]
                if (p.charAt(j - 1) == '*') {
                    dp[i][j] = dp[i - 1][j] || dp[i][j - 1];
                // 当前位置是 ？,可以匹配任意单个字符，所以和前一个位置结果一样
                // p当前位置字符和s当前字符一样，所以跟前一个位置匹配结果一样
                } else if (p.charAt(j - 1) == '?' || s.charAt(i - 1) == p.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
            }
        }
        // 返回
        return dp[s.length()][p.length()];
    }


    public static void main(String[] args) {
        _44_WildcardMatching obj = new _44_WildcardMatching();
        // System.out.println(obj.isMatch("aa", "a")); // false
        // System.out.println(obj.isMatch("aa", "*")); // true
        // System.out.println(obj.isMatch("cb", "?a")); // false
        System.out.println(obj.isMatch("adceb", "*a*b")); // true
        // System.out.println(obj.isMatch("acdcb", "a*c?b")); // false
        // System.out.println(obj.isMatch("aab", "c*a*b")); // false
    }
}
