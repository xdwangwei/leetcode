package com.window;

import java.util.HashMap;

/**
 * @author wangwei
 * 2020/7/27 12:39
 * <p>
 * 76. 最小覆盖子串
 * 给你一个字符串 S、一个字符串 T，请在字符串 S 里面找出：包含 T 所有字符的最小子串。
 * <p>
 * 经过测试， t 中是可能包含重复字符的
 * <p>
 * 示例：
 * <p>
 * 输入: S = "ADOBECODEBANC", T = "ABC"
 * 输出: "BANC"
 * 说明：
 * <p>
 * 如果 S 中不存这样的子串，则返回空字符串 ""。
 * 如果 S 中存在这样的子串，我们保证它是唯一的答案。
 */
public class _76_MinimumWindowSubstring {

    public String minWindow(String s, String t) {
        if (s.length() < t.length()) return "";

        // 最小合法字符出串的长度
        int min = Integer.MAX_VALUE;
        String res = "";

        // 统计需要包含的字符
        HashMap<Character, Integer> need = new HashMap<>();
        // 滑动窗口
        HashMap<Character, Integer> window = new HashMap<>();
        // 统计窗口中包含了几个满足要求的字符
        int valid = 0;

        // 滑动窗口
        int left = 0, right = 0;
        // 统计需要出现的字符及其个数
        for (int i = 0; i < t.length(); ++i) {
            need.put(t.charAt(i), need.getOrDefault(t.charAt(i), 0) + 1);
        }

        // 扩大窗口的前提
        while (right < s.length()) {
            // 待加入窗口的字符
            char c = s.charAt(right);
            // 扩大右边界
            right++;
            // 更新窗口中的一系列数据
            // 如果这个字符的确是需要出现的，
            if (need.containsKey(c)) {
                // 窗口中这个字符次数加1
                window.put(c, window.getOrDefault(c, 0) + 1);
                // 并且在窗口中的出现次数达到要求，那么窗口中的有效字符数加1
                // 因为map存储的是对象，用==比较会非常慢，所以采用intVlue或equals
                if (need.get(c).intValue() == window.get(c).intValue())
                    valid++;
            }

            // 当窗口中的合法字符够了的时候，就可以更新答案，然后缩小窗口(友移左边界)
            while (valid == need.size()) {
                if (right - left < min) {
                    // 窗口是左闭右开的
                    min = right - left;
                    res = s.substring(left, right);
                }
                // 待移出窗口的字符
                char d = s.charAt(left);
                // 增加左边界，缩小窗口
                left++;
                // 更新窗口中的一系列数据
                // 如果这个字符是需要包含的，那就更新窗口中的数据
                if (need.containsKey(d)) {
                    // 如果这个字符在窗口中的出现次数已经达到要求了
                    if (need.get(d).intValue() == window.get(d).intValue())
                        valid--; // 窗口内的有效字符数减1
                    // 这个字符出现次数减1
                    window.put(d, window.get(d) - 1);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        new _76_MinimumWindowSubstring().minWindow("ADOBECODEBANC", "ABC");
    }
}
