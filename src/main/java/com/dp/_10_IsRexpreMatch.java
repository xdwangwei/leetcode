package com.dp;

import javax.swing.text.Element;
import javax.swing.text.StyledEditorKit;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/28 周四 10:31
 * <p>
 * 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
 * <p>
 * '.' 匹配任意单个字符
 * '*' 匹配零个或多个前面的那一个元素
 * 所谓匹配，是要涵盖 整个 字符串 s的，而不是部分字符串。
 * <p>
 * 说明:
 * <p>
 * s 可能为空，且只包含从 a-z 的小写字母。
 * p 可能为空，且只包含从 a-z 的小写字母，以及字符 . 和 *。
 * 示例 1:
 * <p>
 * 输入:
 * s = "aa"
 * p = "a"
 * 输出: false
 * 解释: "a" 无法匹配 "aa" 整个字符串。
 * 示例 2:
 * <p>
 * 输入:
 * s = "aa"
 * p = "a*"
 * 输出: true
 * 解释: 因为 '*' 代表可以匹配零个或多个前面的那一个元素, 在这里前面的元素就是 'a'。因此，字符串 "aa" 可被视为 'a' 重复了一次。
 * 示例 3:
 * <p>
 * 输入:
 * s = "ab"
 * p = ".*"
 * 输出: true
 * 解释: ".*" 表示可匹配零个或多个（'*'）任意字符（'.'）。
 * 示例 4:
 * <p>
 * 输入:
 * s = "aab"
 * p = "c*a*b"
 * 输出: true
 * 解释: 因为 '*' 表示零个或多个，这里 'c' 为 0 个, 'a' 被重复一次。因此可以匹配字符串 "aab"。
 * 示例 5:
 * <p>
 * 输入:
 * s = "mississippi"
 * p = "mis*is*p*."
 * 输出: false
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/regular-expression-matching
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 **/
public class _10_IsRexpreMatch {

    /**
     * 动态规划
     * 很关键的一点是 空串匹配表达式，只可能在 * 号位置成功 空 a | 空 a*
     * 同理： 当 p[j]为'*'时，此时 dp[i][j]一定不用考虑dp[i][j-1]，而要考虑dp[i][j-2]
     * 【*必须和它前面的字符一起 才会提现出它的作用】，所以匹配位置一定是考虑 *，而不是 *前的字符
     * 可以认位 字符 x* 才是一个整体
     * 比如(ab, ab c* )。遇到 * 往前看两个
     * 发现前面 s[i] 的 ab 对 p[j-2] 的 ab 能匹配，
     * 虽然后面是 c*，但是可以看做匹配 0 次 c，相当于直接去掉 c*，所以也是 True
     *
     * 最后来个归纳：
     * 如果 p.charAt(j) == s.charAt(i) :  dp[i][j] = dp[i-1][j-1]；
     * 如果 p.charAt(j) == '.' :          dp[i][j] = dp[i-1][j-1]；
     * 如果 p.charAt(j) == '*'：
     *      如果 p.charAt(j-1) != s.charAt(i) : dp[i][j] = dp[i][j-2] //in this case, a* only counts as empty
     *      如果 p.charAt(i-1) == s.charAt(i) or p.charAt(i-1) == '.'：
     *          dp[i][j] = dp[i-1][j]        //in this case, a* counts as multiple a
     *          or dp[i][j] = dp[i][j-1]     // in this case, a* counts as single a，根据之前的分析，这个可以省略，没有意义
     *          or dp[i][j] = dp[i][j-2]     // in this case, a* counts as empty
     *
     * 作者：lala-333
     * 链接：https://leetcode-cn.com/problems/regular-expression-matching/solution/dong-tai-gui-hua-zen-yao-cong-0kai-shi-si-kao-da-b/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * https://leetcode-cn.com/problems/regular-expression-matching/solution/zheng-ze-biao-da-shi-pi-pei-by-leetcode-solution/
     * https://leetcode-cn.com/problems/regular-expression-matching/solution/dong-tai-gui-hua-zen-yao-cong-0kai-shi-si-kao-da-b/
     */
    public static boolean solution1(String s, String p) {
        int slen = s.length();
        int plen = p.length();
        if (plen == 0) return slen == 0;
        // dp[i][j]表示s[0...i]和p[0...j]是否匹配
        // 注意区分dp[i][j]中i、j代表的是第几个元素,s.charAt(i)中i代表的当前字符在原串的下标
        boolean[][] dp = new boolean[slen + 1][plen + 1];
        dp[0][0] = true;
        dp[0][1] = false;
        for (int j = 2; j <= plen; j++) {
            // 空串匹配表达式，只可能在 * 号位置成功 空 a | 空 a*
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2];
            }
        }
        for (int i = 1; i <= slen; i++)
            for (int j = 1; j <= plen; j++) {
                if (s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.') {
                    dp[i][j] = dp[i - 1][j - 1];
                }
                // 因为*至少会出现在第2个位置，也就是if成立时， j - 1 >= 1 j >= 2
                // 所以下面的索引不会越界
                else if (p.charAt(j - 1) == '*') {
                    // a
                    // xb* 这种情况，直接跳过 b*
                    if (s.charAt(i - 1) != p.charAt(j - 2) && p.charAt(j - 2) != '.') {
                        dp[i][j] = dp[i][j - 2];
                    } else {
                        //  a
                        //  xa*  a重复0次、1次、多次
                        dp[i][j] = dp[i - 1][j] || dp[i][j - 2];
                        /*
                            dp[i][j] = dp[i-1][j] // 多个字符匹配的情况
                            or dp[i][j] = dp[i][j-1] // 单个字符匹配的情况,我们说了单独分析*前面的字符说没有意义的，二者应该合起来作为一个字符处理，所以第二个条件就可以省略
                            or dp[i][j] = dp[i][j-2] // 没有匹配的情况
                             */
                    }
                }
            }
        return dp[slen][plen];
    }
    
    /*
        回溯
            如果没有星号（正则表达式中的 * ），问题会很简单——
            我们只需要从左到右检查匹配串 s 是否能匹配模式串 p 的每一个字符  
            如果没有星号，我们的代码会像这样：
            def match(text, pattern):
                if not pattern: return not text
                first_match = bool(text) and pattern[0] in {text[0], '.'}
                // 从第一个活节点作为扩展节点继续搜索解
                return first_match and match(text[1:], pattern[1:])
            如果模式串中有星号，它会出现在第二个位置，即 pattern[1]，因为*是对之前字符的重复，所以它不会单独存在。
            这种情况下，我们可以直接忽略模式串中这一部分(重复0次)
            如果考虑重复1次，如 a*重复一次，去掉一个a，剩下的应该还是 a*,因为单独一个*时没有意义的，而且重复一次，还可以继续重复，*就一定要保留
            或者删除匹配串的第一个字符，前提是它能够匹配模式串当前位置字符，即 pattern[0] 。
            如果两种操作中有任何一种使得剩下的字符串能匹配，
            那么初始时，匹配串和模式串就可以被匹配
    * */

    public static boolean isMatch(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();
        // 第一个位置字符是否匹配
        boolean first_match = (!s.isEmpty() &&
                (p.charAt(0) == s.charAt(0) || p.charAt(0) == '.'));
        // *出现，之前肯定是有个字符的，所以它不会出现在0位置，最早就是在第2个位置
        // 之所以分这两种，是因为若第2个位置是*，不能直接切割从1之后，因为单独的*没有意义
        if (p.length() >= 2 && p.charAt(1) == '*')
            // *重复0次，就是直接去掉这一段，s不变，p截取 a*
            // a* 重复1次，就是 s去掉一个a，a*也去掉一个a，a*去掉一个a还是a*
            return isMatch(s, p.substring(2)) || first_match && isMatch(s.substring(1), p);
            // 如果第2个位置不是*，那就可以去掉第一个字符
        else
            return first_match && isMatch(s.substring(1), p.substring(1));
    }

    public static void main(String[] args) {
        // System.out.println(solution1("aab","c*a*b"));
        // System.out.println(solution1("mississippi","mis*is*p*."));
        // System.out.println(solution1("a","ab*"));
        // System.out.println(solution1("aaa","ab*a"));
        System.out.println(solution1("ab", ".*.."));
    }
}
