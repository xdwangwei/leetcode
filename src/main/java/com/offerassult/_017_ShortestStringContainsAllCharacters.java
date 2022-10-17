package com.offerassult;

/**
 * @author wangwei
 * @date 2022/10/17 10:55
 * @description: _017_ShortestStringContainsAllCharacters
 *
 * 剑指 Offer II 017. 含有所有字符的最短字符串
 * 给定两个字符串 s 和 t 。返回 s 中包含 t 的所有字符的最短子字符串。如果 s 中不存在符合条件的子字符串，则返回空字符串 "" 。
 *
 * 如果 s 中存在多个符合条件的子字符串，返回任意一个。
 *
 *
 *
 * 注意： 对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "ADOBECODEBANC", t = "ABC"
 * 输出："BANC"
 * 解释：最短子字符串 "BANC" 包含了字符串 t 的所有字符 'A'、'B'、'C'
 * 示例 2：
 *
 * 输入：s = "a", t = "a"
 * 输出："a"
 * 示例 3：
 *
 * 输入：s = "a", t = "aa"
 * 输出：""
 * 解释：t 中两个字符 'a' 均应包含在 s 的子串中，因此没有符合条件的子字符串，返回空字符串。
 *
 *
 * 提示：
 *
 * 1 <= s.length, t.length <= 105
 * s 和 t 由英文字母组成
 *
 *
 * 进阶：你能设计一个在 o(n) 时间内解决此问题的算法吗？
 *
 *
 *
 * 注意：本题与主站 76 题相似（本题答案不唯一）：https://leetcode-cn.com/problems/minimum-window-substring/
 *
 * 通过次数22,945提交次数45,733
 */
public class _017_ShortestStringContainsAllCharacters {

    public String minWindow(String s, String t) {
        if (s.length() < t.length()) {
            return "";
        }
        int[] need = new int[255];
        int[] window = new int[255];
        int needK = 0, windowK = 0;
        for (char c : t.toCharArray()) {
            if (++need[c] == 1) {
                needK++;
            }
        }

        int left = 0, right = 0, start = -1, len = s.length() + 1;
        while (right < s.length()) {
            char c = s.charAt(right++);
            if (++window[c] == need[c]) {
                windowK++;
            }
            while (windowK == needK) {
                if (right - left < len) {
                    len = right - left;
                    start = left;
                }
                char d = s.charAt(left++);
                if (window[d] == need[d]) {
                    windowK--;
                }
                window[d]--;
            }
        }
        return start == -1 ? "" : s.substring(start, start + len);
    }

    public static void main(String[] args) {
        _017_ShortestStringContainsAllCharacters obj = new _017_ShortestStringContainsAllCharacters();
        System.out.println(obj.minWindow("a", "a"));
    }
}
