package com.offerassult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * 2020/7/27 12:41
 *
 * 剑指 Offer II 015. 字符串中的所有变位词
 * 给定两个字符串 s 和 p，找到 s 中所有 p 的 变位词 的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
 *
 * 变位词 指字母相同，但排列不同的字符串。
 *
 *
 *
 * 示例 1：
 *
 * 输入: s = "cbaebabacd", p = "abc"
 * 输出: [0,6]
 * 解释:
 * 起始索引等于 0 的子串是 "cba", 它是 "abc" 的变位词。
 * 起始索引等于 6 的子串是 "bac", 它是 "abc" 的变位词。
 *  示例 2：
 *
 * 输入: s = "abab", p = "ab"
 * 输出: [0,1,2]
 * 解释:
 * 起始索引等于 0 的子串是 "ab", 它是 "ab" 的变位词。
 * 起始索引等于 1 的子串是 "ba", 它是 "ab" 的变位词。
 * 起始索引等于 2 的子串是 "ab", 它是 "ab" 的变位词。
 *
 *
 * 提示:
 *
 * 1 <= s.length, p.length <= 3 * 104
 * s 和 p 仅包含小写字母
 *
 *
 * 注意：本题与主站 438 题相同： https://leetcode-cn.com/problems/find-all-anagrams-in-a-string/
 */
public class _015_FindAllAnagramsInAString {

    /**
     *
     * 和14题一样啊
     *
     * 双指针滑动窗口写法
     *
     *  简化使用双指针滑动窗口，保持窗口内每个字符次数与原串对应一致，否则收缩左边界，
     *  当窗口能扩张到大小和原串一样时，说明当前窗口内每个字符次数都和原串中一致，并且窗口内字符个数=原串字符个数，说明匹配成功
     *  具体：
     *
     *  先统计s中每个字符出现次数 for (char c : s) cnt[c]--; 欠债
     *  t，滑动窗口 left =0， right = 0.
     *  while (rigth < n) {
     *      // 扩张
     *      char c = t[right]; right++;
     *      // 还债
     *      cnt[c]++;
     *      // 窗口内字符c的次数多于原串s，此时进行窗口收缩,对于未在原串出现的字符e，也会触发收缩，所以能自动移除去
     *      while (cnt[c] > 0) {
     *          char d = t[left];
     *          cnt[d]--;
     *          left++;
     *      }
     *      // 收缩完成后，窗口内字符c个数与原串一样，
     *      // 当 窗口大小恰好能够扩张到原串长度时，说明窗口恰好包含了原串每个字符，且次数都一样，此时 返回 true
     *      if (right - left + 1  == m) return true;
     *  }
     *  记得统计每一个符合要求的窗口起始位置就好了
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new ArrayList<>();
        int m = p.length(), n = s.length();
        // s必须比p长
        if (m > n) {
            return ans;
        }
        int[] cnt = new int[26];
        // 原串字符个数统计
        for (char c : p.toCharArray()) {
            cnt[c - 'a']--;
        }
        // 滑动窗口
        int l = 0, r = 0;
        while (r < n) {
            // 当前字符入窗口，右边界扩展
            char c = s.charAt(r++);
            cnt[c - 'a']++;
            // 当前字符个数多于原串中个数
            while (cnt[c - 'a'] > 0) {
                // 窗口收缩
                cnt[s.charAt(l++) - 'a']--;
            }
            // 窗口收缩内，窗口内每个字符个数都和原串中一致，若此时窗口大小恰好=原串长度
            // 说明窗口内字符排序恰好为串s1的一个变位词
            // 记录当前窗口左边界
            if (r - l == m) {
                ans.add(l);
            }
        }
        // 返回
        return ans;
    }
}
