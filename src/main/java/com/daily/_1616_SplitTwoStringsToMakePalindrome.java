package com.daily;

/**
 * @author wangwei
 * @date 2023/3/18 18:58
 * @description: _1616_SplitTwoStringsToMakePalindrome
 *
 * 1616. 分割两个字符串得到回文串
 * 给你两个字符串 a 和 b ，它们长度相同。请你选择一个下标，将两个字符串都在 相同的下标 分割开。由 a 可以得到两个字符串： aprefix 和 asuffix ，满足 a = aprefix + asuffix ，同理，由 b 可以得到两个字符串 bprefix 和 bsuffix ，满足 b = bprefix + bsuffix 。请你判断 aprefix + bsuffix 或者 bprefix + asuffix 能否构成回文串。
 *
 * 当你将一个字符串 s 分割成 sprefix 和 ssuffix 时， ssuffix 或者 sprefix 可以为空。比方说， s = "abc" 那么 "" + "abc" ， "a" + "bc" ， "ab" + "c" 和 "abc" + "" 都是合法分割。
 *
 * 如果 能构成回文字符串 ，那么请返回 true，否则返回 false 。
 *
 * 注意， x + y 表示连接字符串 x 和 y 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：a = "x", b = "y"
 * 输出：true
 * 解释：如果 a 或者 b 是回文串，那么答案一定为 true ，因为你可以如下分割：
 * aprefix = "", asuffix = "x"
 * bprefix = "", bsuffix = "y"
 * 那么 aprefix + bsuffix = "" + "y" = "y" 是回文串。
 * 示例 2：
 *
 * 输入：a = "abdef", b = "fecab"
 * 输出：true
 * 示例 3：
 *
 * 输入：a = "ulacfd", b = "jizalu"
 * 输出：true
 * 解释：在下标为 3 处分割：
 * aprefix = "ula", asuffix = "cfd"
 * bprefix = "jiz", bsuffix = "alu"
 * 那么 aprefix + bsuffix = "ula" + "alu" = "ulaalu" 是回文串。
 *
 *
 * 提示：
 *
 * 1 <= a.length, b.length <= 105
 * a.length == b.length
 * a 和 b 都只包含小写英文字母
 * 通过次数17,480提交次数49,605
 */
public class _1616_SplitTwoStringsToMakePalindrome {

    /**
     * 方法一：双指针
     * 思路
     *
     * 记字符串的长度为n，分割的下标为k，即下标k 之前的字符构成前缀，下标k 和之后的字符构成后缀，前缀长度为k，后缀长度为n−k，0≤k≤n。
     *
     * 接下来需要判断 a_prefix + b_suffix 或者 a_suffix + b_prefix 能否构成回文字符串，
     *
     * 首先判断 a_prefix + b_suffix 能否构成回文字符串。
     *
     * 这个字符串的起始位置是由 a 组成的，末尾位置是由 b 构成的。
     *
     * 我们可以使用双指针，其中一个指针 i 从字符串 a 的头部开始，另一个指针 j 从字符串 b 的尾部开始，
     * 如果两个指针指向的字符相等，那么两个指针同时往中间移动，直到遇到不同的字符或两指针交叉。
     *
     * 如果两指针交叉，即 i≥j，说明  a_prefix + b_suffix 已经可以得到回文串，返回 true；
     *
     * 否则，说明 a[0...i-1] 和 b[j+1...n-1] 匹配，此时切割位置只能是 i 位置 或 j 位置，比如如下：
     *
     *      idx:       0 1 2 i     ...      j     n-1
     *      a:         a b c d     ...      x x x x
     *      b:         x x x x     ...      e c b a
     *
     * 此时，如果 a[i...j] 本身回文，切割 j 位置
     *      那么 a[0...j] + b[j+1...n-1]  == a[0...i-1] + a[i...j] + b[j+1...n-1] 拼接后也回文，返回 true
     *      如果 b[i...j] 本身回文，切割 i 位置
     *      那么 a[0...i-1] + b[i...n-1]  == a[0...i-1] + b[i...j] + b[j+1...n-1] 拼接后也回文，返回 true
     *
     * 否则 返回 false
     *
     *
     * 至于判断 a_suffix + b_prefix 是否回文，等价于 交换两个字符串 a 和 b，重复上述同样的过程。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/split-two-strings-to-make-palindrome/solution/python3javacgotypescriptrust-yi-ti-yi-ji-r0ks/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/split-two-strings-to-make-palindrome/solution/fen-ge-liang-ge-zi-fu-chuan-de-dao-hui-w-bjzk/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param a
     * @param b
     * @return
     */
    public boolean checkPalindromeFormation(String a, String b) {
        // 判断 a_prefix + b_suffix 或者 a_suffix + b_prefix 能否构成回文字符串，
        // 判断 a_suffix + b_prefix 是否回文，等价于 交换两个字符串 a 和 b，重复上述同样的过程。
        return checkPalindrome(a, b) || checkPalindrome(b, a);
    }

    /**
     * 判断 a_prefix + b_suffix 能否构成回文字符串，
     * @param a
     * @param b
     * @return
     */
    public boolean checkPalindrome(String a, String b) {
        int n = a.length();
        // 双指针，两头往中间匹配
        int l = 0, r = n - 1;
        while (l < r && a.charAt(l) == b.charAt(r)) {
            l++;
            r--;
        }
        // 如果l >= r，返回true （从l位置切割）
        // 否则 必须 满足 a[l...r]本身回文 （切割位置j，a[0...j] + b[j+1...n-1] = a[0...i-1] + a[i...j] + b[j+1...n-1]）
        // 或者 必须 满足 b[l...r]本身回文 （切割位置i，a[0...i-1] + b[i...n-1] = a[0...i-1] + b[i...j] + b[j+1...n-1]）
        return l >= r || isPalindrome(a, l, r) || isPalindrome(b, l, r);
    }

    /**
     * 判断 s[l....r] 是否回文
     * @param s
     * @param l
     * @param r
     * @return
     */
    private boolean isPalindrome(String s, int l, int r) {
        // 两头往中间匹配判断
        while (l < r && s.charAt(l) == s.charAt(r)) {
            l++;
            r--;
        }
        // 返回
        return l >= r;
    }
}
