package com.offerassult;

/**
 * @author wangwei
 * @date 2022/10/19 12:53
 * @description: _019_DeleteOneCharToGetPalindrome
 *
 * 剑指 Offer II 019. 最多删除一个字符得到回文
 * 给定一个非空字符串 s，请判断如果 最多 从字符串中删除一个字符能否得到一个回文字符串。
 *
 *
 *
 * 示例 1:
 *
 * 输入: s = "aba"
 * 输出: true
 * 示例 2:
 *
 * 输入: s = "abca"
 * 输出: true
 * 解释: 可以删除 "c" 字符 或者 "b" 字符
 * 示例 3:
 *
 * 输入: s = "abc"
 * 输出: false
 *
 *
 * 提示:
 *
 * 1 <= s.length <= 105
 * s 由小写英文字母组成
 *
 *
 * 注意：本题与主站 680 题相同： https://leetcode-cn.com/problems/valid-palindrome-ii/
 */
public class _019_DeleteOneCharToGetPalindrome {

    public boolean validPalindrome(String s) {
        // 有1次删除机会
        return valid(s, 0, s.length() - 1 , true);
    }

    /**
     * 最多删除一个字符，能否得到回文串
     * 递归
     * @param s
     * @param l
     * @param r
     * @param canDel 是否有删除机会
     * @return
     */
    private boolean valid(String s, int l, int r, boolean canDel) {
        while (l < r) {
            // 匹配失败
            if (s.charAt(l) != s.charAt(r)) {
                // 如果已经删除过一个字符，直接返回false
                if (!canDel) {
                    return false;
                }
                // 使用删除机会，可以删除l或r位置字符，再判断剩下的，但后续不能再删除
                return valid(s, l, r - 1, false) || valid(s, l + 1, r, false);
            }
            l++;
            r--;
        }
        return true;
    }
}
