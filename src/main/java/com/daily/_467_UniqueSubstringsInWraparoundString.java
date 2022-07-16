package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/5/25 18:44
 * @description: _467_UniqueSubstringsInWraparoundString
 *
 * 467. 环绕字符串中唯一的子字符串
 * 把字符串 s 看作是 “abcdefghijklmnopqrstuvwxyz” 的无限环绕字符串，所以 s 看起来是这样的：
 *
 * "...zabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcd...." .
 * 现在给定另一个字符串 p 。返回 s 中 唯一 的 p 的 非空子串 的数量 。
 *
 *
 *
 * 示例 1:
 *
 * 输入: p = "a"
 * 输出: 1
 * 解释: 字符串 s 中只有一个"a"子字符。
 * 示例 2:
 *
 * 输入: p = "cac"
 * 输出: 2
 * 解释: 字符串 s 中的字符串“cac”只有两个子串“a”、“c”。.
 * 示例 3:
 *
 * 输入: p = "zab"
 * 输出: 6
 * 解释: 在字符串 s 中有六个子串“z”、“a”、“b”、“za”、“ab”、“zab”。
 *
 *
 * 提示:
 *
 * 1 <= p.length <= 105
 * p 由小写英文字母构成
 * 通过次数23,106提交次数46,292
 */
public class _467_UniqueSubstringsInWraparoundString {


    /**
     * 方法一：动态规划
     *
     * 首先：
     * 字符串 abcde 的所有非空子串个数 可以看作是以每个位置为结尾的子串的子串个数之和 == 以每个位置为结尾的子串长度之和
     * 比如 abcde 的所有子串可以看作 a 的子串 + ab的子串 + abc的子串 + abcd的子串 + abcde的子串
     *                         == len(a) +  len(ab) + len(abc) + len(abcd) + len(abcde)
     *
     * 由于 s 是周期字符串，我们要考虑在s中出现的p的子串，就需要计算，以p每个位置为结尾的子串在s中的最大匹配长度，并求和
     * 并且，对于在 s 中的子串，只要知道子串的第一个字符（或最后一个字符）和子串长度，就能确定这个子串。
     * 例如子串以 ‘d’ 结尾，长度为 3，那么该子串为 “bcd”。
     *
     * 所以若要考虑出现在s中的子串，那么对于两个以同一个字符结尾的子串，长的那个子串必然包含短的那个。
     * 例如 “abcd” 和 “bcd” 均以 ‘d’ 结尾，“bcd” 是 “abcd” 的子串。
     *
     * 所以这里并非计算以p每个位置为结尾的子串在s中最大匹配长度，而要计算以p每个位置字符结尾的子串在s中最大匹配长度，
     * 对于p不同位置字符相等，在s中最长匹配长度，二者应该取较大值，并非直接加和
     *
     * 据此，我们可以定义 dp[α] 表示 pp 中以字符 α 结尾且在 s 中的子串的最长长度，知道了最长长度，也就知道了不同的子串的个数。
     *
     * 如何计算 dp[α] 呢？我们可以在遍历 p 时，维护连续递增的子串长度 k。
     * 具体来说，遍历到 p[i] 时，如果 p[i] 是 p[i−1] 在字母表中的下一个字母，则将 k 加一，否则将 k 置为 1，表示重新开始计算连续递增的子串长度。
     * 然后，用 k 更新 dp[p[i]] 的最大值。
     *
     * 遍历结束后，p 中以字符 c 结尾且在 s 中的子串有 dp[c] 个。例如 dp[‘d’]=3 表示子串 “bcd”、“cd” 和 “d”。
     *
     * 最后答案为
     *  sum[dp[]]
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/unique-substrings-in-wraparound-string/solution/huan-rao-zi-fu-chuan-zhong-wei-yi-de-zi-ndvea/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param p
     * @return
     */
    public int findSubstringInWraproundString(String p) {
        int n = p.length();
        // dp[α] 表示 p 中以字符 α 结尾且在 s 中的最长匹配子串的最长长度
        int dp[] = new int[26];
        int len = 0;
        // 枚举p每个位置
        for (int i = 0; i < n; ++i) {
            // 以当前位置字符结尾，在s中的最大匹配长度，s 是周期字符串，除非当前字符比上一个字符>1或者大-25，否则肯定不在s中
            if (i > 0 && ((p.charAt(i) - p.charAt(i - 1) + 25) % 26 == 0)) {
                len++;
            } else {
                // 当前字符不满足和前一个字符顺序相接，另起炉灶，长度为1
                len = 1;
            }
            // 对于两个以同一个字符结尾的子串，长的那个子串必然包含短的那个。取较大值
            dp[p.charAt(i) - 'a'] = Math.max(dp[p.charAt(i) - 'a'], len);
        }
        // 返回以p不同字符结尾的子串在s中的最大匹配子串长度 之和
        return Arrays.stream(dp).sum();
    }
}
