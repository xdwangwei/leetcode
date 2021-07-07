package com.window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author wangwei
 * 2020/7/27 12:41
 *
 * 438.找到字符串中所有字母异位词
 *
 * 给定一个字符串 s 和一个非空字符串 p，找到 s 中所有是 p 的字母异位词的子串，返回这些子串的起始索引。
 *
 * 字符串只包含小写英文字母，并且字符串 s 和 p 的长度都不超过 20100。
 *
 * 说明：
 *
 * 字母异位词指字母相同，但排列不同的字符串。
 * 不考虑答案输出的顺序。
 * 示例 1:
 *
 * 输入:
 * s: "cbaebabacd" p: "abc"
 *
 * 输出:
 * [0, 6]
 *
 * 解释:
 * 起始索引等于 0 的子串是 "cba", 它是 "abc" 的字母异位词。
 * 起始索引等于 6 的子串是 "bac", 它是 "abc" 的字母异位词。
 *  示例 2:
 *
 * 输入:
 * s: "abab" p: "ab"
 *
 * 输出:
 * [0, 1, 2]
 *
 * 解释:
 * 起始索引等于 0 的子串是 "ab", 它是 "ab" 的字母异位词。
 * 起始索引等于 1 的子串是 "ba", 它是 "ab" 的字母异位词。
 * 起始索引等于 2 的子串是 "ab", 它是 "ab" 的字母异位词。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-all-anagrams-in-a-string
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _438_FindAllAnagramsInAString {

    /**
     * 滑动窗口
     * 其实和_76_一样，也是从s中寻找包含t中全部字母的子串，
     * 不同的是
     *      _76_是选出最短的那个，本题是找到全部的
     *      _76_中的子串可以包含其他字母，本题只能是p的字母其他排列
     *
     *      所以本题中，一旦窗口大小和p的长度一样了，就得更新结果然后收缩左边界
     * @param s
     * @param p
     * @return
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s.length() < p.length())
            return res;
        // 记录需要包含的字符，及其出现次数
        HashMap<Character, Integer> need = new HashMap<>();
        // 记录当前窗口中已包含需要的字符，及其出现次数
        HashMap<Character, Integer> window = new HashMap<>();
        // 当前窗口中达到要求的字符数目
        int valid = 0;

        char[] arrayS = s.toCharArray();
        char[] arrayT = p.toCharArray();

        for (char c : arrayT) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }

        // 滑动窗口，左闭右开
        int left = 0, right = 0;

        while (right < s.length()) {
            // 当前字符
            char c = arrayS[right];
            // 扩大窗口右边界
            right++;
            // 更新窗口内一系列数据
            // 如果这个字符是需要的
            if (need.containsKey(c)) {
                // 记录他在窗口内的出现次数
                window.put(c, window.getOrDefault(c, 0) + 1);
                // 如果他的出现次数达到了要求的次数
                if (window.get(c).intValue() == need.get(c).intValue())
                    // 当前窗口内满足要求的字符数加1
                    valid++;
            }


            // 当当前窗口大小已经和p一样时，就可以更新结果，然后收缩左窗口了
            while (right - left == p.length()) {
                // 更新结果
                if (valid == need.size()) {
                    // 若此时的窗口正好已完全包含t中字符时，
                    // 当前窗口就是一个满足要求的子集
                    res.add(left);
                }
                // 收缩窗口
                // 最左边字符
                char d = arrayS[left];
                // 收缩左边界
                left++;
                // 更新窗口内一系列数据
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
}
