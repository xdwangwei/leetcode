package com.string;

/**
 * @author wangwei
 * @date 2022/10/17 19:42
 * @description: _125_ValidPalindrome
 *
 * 125. 验证回文串
 * 如果在将所有大写字符转换为小写字符、并移除所有非字母数字字符之后，短语正着读和反着读都一样。则可以认为该短语是一个 回文串 。
 *
 * 字母和数字都属于字母数字字符。
 *
 * 给你一个字符串 s，如果它是 回文串 ，返回 true ；否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入: s = "A man, a plan, a canal: Panama"
 * 输出：true
 * 解释："amanaplanacanalpanama" 是回文串。
 * 示例 2：
 *
 * 输入：s = "race a car"
 * 输出：false
 * 解释："raceacar" 不是回文串。
 * 示例 3：
 *
 * 输入：s = " "
 * 输出：true
 * 解释：在移除非字母数字字符之后，s 是一个空字符串 "" 。
 * 由于空字符串正着反着读都一样，所以是回文串。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 2 * 105
 * s 仅由可打印的 ASCII 字符组成
 * 通过次数414,294提交次数887,267
 */
public class _125_ValidPalindrome {

    /**
     * 双指针：直接在原字符串 s 上使用双指针。
     * l = 0, r = s.length() - 1
     * 回文串则有 s[l++] == s[r--]
     * 需要跳过非英文字母和数字
     * 需要忽略大小写
     * 在移动任意一个指针时，需要不断地向另一指针的方向移动，直到遇到一个字母或数字字符，或者两指针重合为止。
     * 也就是说，我们每次将指针移到下一个字母字符或数字字符，再判断这两个指针指向的字符是否相同。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/XltzEq/solution/you-xiao-de-hui-wen-by-leetcode-solution-uj86/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public boolean isPalindrome(String s) {
        // 双指针
        int l = 0, r = s.length() - 1;
        while (l < r) {
            // 忽略非英文字母和数字字符
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) {
                l++;
            }
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) {
                r--;
            }
            // s[l] 必须 == s[r]
            if (l < r) {
                // 忽略大小写比较
                if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) {
                    return false;
                }
                // 双指针跟进
                l++;r--;
            }
        }
        return true;
    }
}
