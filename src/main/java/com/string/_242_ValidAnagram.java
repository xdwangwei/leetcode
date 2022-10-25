package com.string;

/**
 * @author wangwei
 * @date 2022/10/25 14:06
 * @description: _242_ValidAnagram
 *
 * 242. 有效的字母异位词
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 *
 * 注意：若 s 和 t 中每个字符出现的次数都相同，则称 s 和 t 互为字母异位词。
 *
 *
 *
 * 示例 1:
 *
 * 输入: s = "anagram", t = "nagaram"
 * 输出: true
 * 示例 2:
 *
 * 输入: s = "rat", t = "car"
 * 输出: false
 *
 *
 * 提示:
 *
 * 1 <= s.length, t.length <= 5 * 104
 * s 和 t 仅包含小写字母
 *
 *
 * 进阶: 如果输入字符串包含 unicode 字符怎么办？你能否调整你的解法来应对这种情况？
 */
public class _242_ValidAnagram {

    /**
     * 简单模拟，分别统计s和t中每个字符的个数，保证对应相等即可
     *
     * 可以用一个数组完成这个过程，遍历s时，字符次数自增，遍历t时，字符次数自减，如果出现负数，说明当前字符次数不匹配，返回false
     * @param s
     * @param t
     * @return
     */
    public boolean isAnagram(String s, String t) {
        // 长度肯定要一致
        if (s.length() != t.length()) {
            return false;
        }
        // 统计每个字符个数
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }
        for (char c : t.toCharArray()) {
            // 当前字符个数不匹配
            if (--cnt[c - 'a'] < 0) {
                return false;
            }
        }
        return true;
    }
}
