package com.daily;

/**
 * @author wangwei
 * @date 2022/10/11 10:21
 * @description: _1790_OneSwapToMakeStringEquals
 *
 * 1790. 仅执行一次字符串交换能否使两个字符串相等
 * 给你长度相等的两个字符串 s1 和 s2 。一次 字符串交换 操作的步骤如下：选出某个字符串中的两个下标（不必不同），并交换这两个下标所对应的字符。
 *
 * 如果对 其中一个字符串 执行 最多一次字符串交换 就可以使两个字符串相等，返回 true ；否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s1 = "bank", s2 = "kanb"
 * 输出：true
 * 解释：例如，交换 s2 中的第一个和最后一个字符可以得到 "bank"
 * 示例 2：
 *
 * 输入：s1 = "attack", s2 = "defend"
 * 输出：false
 * 解释：一次字符串交换无法使两个字符串相等
 * 示例 3：
 *
 * 输入：s1 = "kelb", s2 = "kelb"
 * 输出：true
 * 解释：两个字符串已经相等，所以不需要进行字符串交换
 * 示例 4：
 *
 * 输入：s1 = "abcd", s2 = "dcba"
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= s1.length, s2.length <= 100
 * s1.length == s2.length
 * s1 和 s2 仅由小写英文字母组成
 */
public class _1790_OneSwapToMakeStringEqual {


    /**
     * 题目要求其中一个字符串执行最多一次字符交换使得两个字符串相等，意味着两个字符串中最多只存在两个位置 i,j 处字符不相等，并且 i,j 处字符必须保证交叉相等。
     * 使用 a 和 b 记录不同的位置下标，初始值为 -1，若「不同位置超过 2 个」或「只有 1 个」直接返回 false，若「不存在不同位置」或「不同位置字符相同」，则返回 true。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/check-if-one-string-swap-can-make-strings-equal/solution/by-ac_oier-qeul/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s1
     * @param s2
     * @return
     */
    public boolean areAlmostEqual(String s1, String s2) {
        // a，b 分别记录第1，2个diff位置
        int a = -1, b = -1;
        for (int i = 0; i < s1.length(); ++i) {
            if (s1.charAt(i) != s2.charAt(i)) {
                // 第一个diff位置
                if (a == -1) {
                    a = i;
                    // b 记录第二个diff位置
                } else if (b == -1) {
                    b = i;
                } else {
                    // 不能出现多于2处diff
                    return false;
                }
            }
        }
        // 说明不存在diff位置
        if (a == -1) {
            return true;
        }
        // 说明只有一个diff位置
        if (b == -1) {
            return false;
        }
        // 两个diff位置需要保证交叉相等
        return s1.charAt(a) == s2.charAt(b) && s1.charAt(b) == s2.charAt(a);
    }
}
