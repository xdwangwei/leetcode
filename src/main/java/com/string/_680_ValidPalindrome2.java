package com.string;

/**
 * @author wangwei
 * @date 2022/10/19 12:56
 * @description: _680_ValidPalindrome2
 *
 * 680. 验证回文串 II
 * 给你一个字符串 s，最多 可以从中删除一个字符。
 *
 * 请你判断 s 是否能成为回文字符串：如果能，返回 true ；否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "aba"
 * 输出：true
 * 示例 2：
 *
 * 输入：s = "abca"
 * 输出：true
 * 解释：你可以删除字符 'c' 。
 * 示例 3：
 *
 * 输入：s = "abc"
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 105
 * s 由小写英文字母组成
 */
public class _680_ValidPalindrome2 {

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
