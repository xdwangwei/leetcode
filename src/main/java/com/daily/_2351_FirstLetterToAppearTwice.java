package com.daily;

/**
 * @author wangwei
 * @date 2023/1/1 17:37
 * @description: _2351_FirstLetterToAppearTwice
 *
 * 2351. 第一个出现两次的字母
 * 给你一个由小写英文字母组成的字符串 s ，请你找出并返回第一个出现 两次 的字母。
 *
 * 注意：
 *
 * 如果 a 的 第二次 出现比 b 的 第二次 出现在字符串中的位置更靠前，则认为字母 a 在字母 b 之前出现两次。
 * s 包含至少一个出现两次的字母。
 *
 *
 * 示例 1：
 *
 * 输入：s = "abccbaacz"
 * 输出："c"
 * 解释：
 * 字母 'a' 在下标 0 、5 和 6 处出现。
 * 字母 'b' 在下标 1 和 4 处出现。
 * 字母 'c' 在下标 2 、3 和 7 处出现。
 * 字母 'z' 在下标 8 处出现。
 * 字母 'c' 是第一个出现两次的字母，因为在所有字母中，'c' 第二次出现的下标是最小的。
 * 示例 2：
 *
 * 输入：s = "abcdd"
 * 输出："d"
 * 解释：
 * 只有字母 'd' 出现两次，所以返回 'd' 。
 *
 *
 * 提示：
 *
 * 2 <= s.length <= 100
 * s 由小写英文字母组成
 * s 包含至少一个重复字母
 * 通过次数25,923提交次数29,998
 */
public class _2351_FirstLetterToAppearTwice {

    /**
     * 二进制表示
     *
     * 因为字符串 s 仅由小写字符构成，因此可以使用一个int类型变量state的二进制每一位表示对应字符是否出现
     * 遍历 s 的每一个字符，对应二进制位为 x = s[i] - 'a'，
     *      若 state 二进制当前位已经为1 ((state >> x) & 1) == 1，则返回当前字符
     *      否则，置 state 二进制当前位置为1，state |= (1 << x)
     *
     * @param s
     * @return
     */
    public char repeatedCharacter(String s) {
        int state = 0;
        // 遍历字符
        for (char c : s.toCharArray()) {
            // 此字符对应二进制位已经为1，代表其已出现过，返回此字符
            if ((state >> (c - 'a') & 1) == 1) {
                return c;
            }
            // 标记state对应二进制位为1
            state |= (1 << c - 'a');
        }
        // never
        return 'a';
    }
}
