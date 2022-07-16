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

    /**
     * 滑动窗口
     *
     * 滑动窗口算法的思路是这样：
     *
     * 1、我们在字符串 S 中使用双指针中的左右指针技巧，初始化 left = right = 0，把索引左闭右开区间 [left, right) 称为一个「窗口」。
     *
     * 2、我们先不断地增加 right 指针扩大窗口 [left, right)，直到窗口中的字符串符合要求（包含了 T 中的所有字符）。
     *
     * 3、此时，我们停止增加 right，转而不断增加 left 指针缩小窗口 [left, right)，直到窗口中的字符串不再符合要求（不包含 T 中的所有字符了）。同时，每次增加 left，我们都要更新一轮结果。
     *
     * 4、重复第 2 和第 3 步，直到 right 到达字符串 S 的尽头。
     *
     * 第 2 步相当于在寻找一个「可行解」，然后第 3 步在优化这个「可行解」，最终找到最优解，也就是最短的覆盖子串。左右指针轮流前进，窗口大小增增减减，窗口不断向右滑动，这就是「滑动窗口」这个名字的来历。
     *
     * 只需要思考以下四个问题：
     *
     * 1、当移动 right 扩大窗口，即加入字符时，应该更新哪些数据？
     *
     * 2、什么条件下，窗口应该暂停扩大，开始移动 left 缩小窗口？
     *
     * 3、当移动 left 缩小窗口，即移出字符时，应该更新哪些数据？
     *
     * 4、我们要的结果应该在扩大窗口时还是缩小窗口时进行更新？
     *
     * 如果一个字符进入窗口，应该增加 window 计数器；
     * 如果一个字符将移出窗口的时候，应该减少 window 计数器；
     * 当 valid 满足 need 时应该收缩窗口；
     * 应该在收缩窗口的时候更新最终结果（缩小窗口的前提是当前窗口满足要求，能够继续缩小，所以才能更新结果）
     *
     *
     * @param s
     * @param t
     * @return
     */
    public String minWindow(String s, String t) {
        if (s.length() < t.length()) return "";

        // 最小合法字符出串的长度
        int min = Integer.MAX_VALUE;
        // 对应的起始位置
        int start = 0;

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
                if (need.get(c).intValue() == window.get(c).intValue()) {
                    valid++;
                }
            }

            // 当窗口中的合法字符够了的时候，就可以更新答案，然后缩小窗口(友移左边界)
            // ① min是我们目前记录的符合要求的最短串，那么 比 min 更大的窗口我们没必要再考虑
            while (valid == need.size() || right - left >= min) {
                if (right - left < min) {
                    min = right - left;
                    start = left;
                    // ② 目标子串不可能比 t 本身更短，直接返回
                    if (min == t.length()) {
                        return s.substring(start, start + min);
                    }
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
        return min == Integer.MAX_VALUE ? "" : s.substring(start, start + min);
    }

    public static void main(String[] args) {
        new _76_MinimumWindowSubstring().minWindow("ADOBECODEBANC", "ABC");
    }
}
