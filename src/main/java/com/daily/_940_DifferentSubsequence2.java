package com.daily;

/**
 * @author wangwei
 * @date 2022/10/14 16:25
 * @description: _940_DifferentSubsequence2
 * 940. 不同的子序列 II
 * 给定一个字符串 s，计算 s 的 不同非空子序列 的个数。因为结果可能很大，所以返回答案需要对 10^9 + 7 取余 。
 *
 * 字符串的 子序列 是经由原字符串删除一些（也可能不删除）字符但不改变剩余字符相对位置的一个新字符串。
 *
 * 例如，"ace" 是 "abcde" 的一个子序列，但 "aec" 不是。
 *
 *
 * 示例 1：
 *
 * 输入：s = "abc"
 * 输出：7
 * 解释：7 个不同的子序列分别是 "a", "b", "c", "ab", "ac", "bc", 以及 "abc"。
 * 示例 2：
 *
 * 输入：s = "aba"
 * 输出：6
 * 解释：6 个不同的子序列分别是 "a", "b", "ab", "ba", "aa" 以及 "aba"。
 * 示例 3：
 *
 * 输入：s = "aaa"
 * 输出：3
 * 解释：3 个不同的子序列分别是 "a", "aa" 以及 "aaa"。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 2000
 * s 仅由小写英文字母组成
 *
 */
public class _940_DifferentSubsequence2 {

    /**
     * 动态规划
     * 串abc的 子序列：dp[i] =     a,b,c,ab,ac,bc,abc
     * 串abcd的子序列：dp[i + 1] = a,b,c,ab,ac,bc,abc,ad,bd,cd,abd,acd,bcd,abcd,d
     *                         = a,b,c,ab,ac,bc,abc + (a,b,c,ab,ac,bc,abc)d + d
     *                         = dp[i - 1] + dp[i - 1] + 1
     *                         = 前i-1子串的子序列数 + 加入当前字符后多出来的序列数
     *                  也就是说，尾部新加入当前字符d后，所得子串的子序列的个数增加了 dp[i-1]+1 个，
     *                  但需要考虑重复计数情况
     *                  比如：abcd????d，加入第一个d和加入第二个d时，第一个d前面的abc都能和当前字符d组成得到一部分子序列，这部分子序列不应该被重复计算
     *                  那么这部分子序列数量是多少呢？对当前d来说，恰好就是第一个d加入时，所多出来的那部分（即所有abc的子序列和字符d拼接得到的那部分序列）
     *
     *                  所以，当迭代到当前字符d时，需要考虑遍历上一个d时所多出来的那部分，此时，需要剔除掉
     *                  用一个数组last[c]来记录上一个字符c加入尾部时，新增加了多少子序列，注意保存的是新增char[c]时在上一个子串基础上带来的新的序列数，不是dp[i]
     *
     *  https://leetcode.cn/problems/distinct-subsequences-ii/solution/bu-tong-by-capital-worker-vga3/
     *
     * @param s
     * @return
     */
    public int distinctSubseqII(String s) {
        int mod = (int)1e9 + 7;
        int n = s.length();
        char[] chars = s.toCharArray();
        int[] dp = new int[n];
        int[] last = new int[26];
        // 初始化，只有一个字符
        dp[0] = 1;
        // 第一个字符新增的子序列数是1
        last[chars[0] - 'a'] = 1;
        for (int i = 1; i < n; ++i) {
            char c = chars[i];
            // 当前字符c新增的子序列数是 dp[i - 1] + 1
            // 当前字符c重复的子序列数是 last[c]
            int newCount = dp[i - 1] + 1;
            // dp[i] = 之前的 + 新增的 - 重复的
            dp[i] = ((dp[i - 1] + newCount) % mod - last[c - 'a'] % mod + mod) % mod;
            // 对于下一个c来说，当前的新增量就是它要减去的重复量
            last[c - 'a'] = newCount;
        }
        // 返回
        return dp[n - 1];
    }

    /**
     * 空间优化，从方法一可以看出，dp[i]只和dp[i-1]有关，并且最终只需dp[n-1]，因此可以用一个变量迭代更新
     * @param s
     * @return
     */
    public int distinctSubseqII2(String s) {
        int mod = (int)1e9 + 7;
        int n = s.length();
        char[] chars = s.toCharArray();
        int[] last = new int[26];
        // 初始化，只有一个字符
        int dp = 1;
        // 第一个字符新增的子序列数是1
        last[chars[0] - 'a'] = 1;
        for (int i = 1; i < n; ++i) {
            char c = chars[i];
            // 当前字符c新增的子序列数是 dp[i - 1] + 1
            // 当前字符c重复的子序列数是 last[c]
            int newCount = dp + 1;
            // dp[i] = 之前的 + 新增的 - 重复的
            dp = ((dp + newCount) % mod - last[c - 'a'] % mod + mod) % mod;
            // 对于下一个c来说，当前的新增量就是它要减去的重复量
            last[c - 'a'] = newCount;
        }
        // 返回
        return dp;
    }
}
