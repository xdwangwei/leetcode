package com.hot100;

/**
 * @author wangwei
 * 2022/4/17 15:15
 *
 * 647. 回文子串
 * 给你一个字符串 s ，请你统计并返回这个字符串中 回文子串 的数目。
 *
 * 回文字符串 是正着读和倒过来读一样的字符串。
 *
 * 子字符串 是字符串中的由连续字符组成的一个序列。
 *
 * 具有不同开始位置或结束位置的子串，即使是由相同的字符组成，也会被视作不同的子串。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "abc"
 * 输出：3
 * 解释：三个回文子串: "a", "b", "c"
 * 示例 2：
 *
 * 输入：s = "aaa"
 * 输出：6
 * 解释：6个回文子串: "a", "a", "a", "aa", "aa", "aaa"
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 1000
 * s 由小写英文字母组成
 */
public class _647_PalindromicSubstrings {

    /**
     * 动态规划，由中心往两边扩展
     * dp[i][j] 表示 s[i...j]是否是回文子串
     * 所以j的取值范围是 [i, n-1]，并且 i 要倒序遍历
     * dp[i][j] 为true，当且仅当 i == j 或 (s[i] == s[j] && (j==i+1 || dp[i+1][j-1])
     * j==i+1是串长度为2的时候，i+1,j-1间隔大于等于2，所以会漏掉这个段串
     * 合起来就是 dp[i][j] 为true，当且仅当 s[i] == s[j] && (j<=i+1 || dp[i+1][j-1])
     * 每一个dp[i][j]为true，表示回文串多了一个，res++
     * @param s
     * @return
     */
    public int countSubstrings(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int res = 0;
        // 倒序i
        for (int i = n - 1; i >= 0; --i) {
            // j 从 i 到 n - 1
            for (int j = i; j < n; ++j) {
                // dp[i][j] 为 true 的必要条件
                if (s.charAt(i) == s.charAt(j) && (j <= i + 1 || dp[i + 1][j - 1])) {
                    dp[i][j] = true;
                    res++;
                }
            }
        }
        return res;
    }

    /**
     * 中心扩展法
     * 上面动态规划的递推式子核心还是由中间往两边扩展，所以直接采用中心扩展法
     * 回文串可能是奇数串也可能是偶数串，所以每个中心可以是(i,i)也可以是(i,i+1)
     * 从中心往左右同时扩展，每扩展一步，就能得到一个回文串，res++
     * @param s
     * @return
     */
    public int countSubstrings2(String s) {
        int n = s.length();
        int res = 0;
        for (int i = 0; i < n; ++i) {
            // 以 (i,i) 为中心同时向两边扩展
            // 左右指针同时往两个方向前进，必须保证不越界，并且 对应位置字符相等
            int left = i, right = i;
            while (left >= 0 && right < n && s.charAt(left) == s.charAt(right)) {
                left--;
                right++;
                // 每扩展一步得到的都是一个回文串
                res++;
            }
            // 以 (i,i+1) 为中心同时向两边扩展
            left = i;
            right = i + 1;
            while (left >= 0 && right < n && s.charAt(left) == s.charAt(right)) {
                left--;
                right++;
                res++;
            }
        }
        return res;
    }

}
