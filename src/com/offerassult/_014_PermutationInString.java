package com.offerassult;

/**
 * @author wangwei
 * 2020/7/27 12:40
 *
 * 剑指 Offer II 014. 字符串中的变位词
 * 给定两个字符串 s1 和 s2，写一个函数来判断 s2 是否包含 s1 的某个变位词。
 *
 * 换句话说，第一个字符串的排列之一是第二个字符串的 子串 。
 *
 *
 *
 * 示例 1：
 *
 * 输入: s1 = "ab" s2 = "eidbaooo"
 * 输出: True
 * 解释: s2 包含 s1 的排列之一 ("ba").
 * 示例 2：
 *
 * 输入: s1= "ab" s2 = "eidboaoo"
 * 输出: False
 *
 *
 * 提示：
 *
 * 1 <= s1.length, s2.length <= 104
 * s1 和 s2 仅包含小写字母
 *
 *
 * 注意：本题与主站 567 题相同： https://leetcode-cn.com/problems/permutation-in-string/
 *
 */
public class _014_PermutationInString {

    /**
     * 双指针滑动窗口写法
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
     */
    public boolean checkInclusion(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        // s2 必须比 s1 更长
        if (m > n) {
            return false;
        }
        int[] cnt = new int[26];
        // 原串字符个数统计
        for (char c : s1.toCharArray()) {
            cnt[c - 'a']--;
        }
        // 滑动窗口
        int l = 0, r = 0;
        while (r < n) {
            // 当前字符入窗口，右边界扩展
            char c = s2.charAt(r++);
            cnt[c - 'a']++;
            // 当前字符个数多于原串中个数
            while (cnt[c - 'a'] > 0) {
                // 窗口收缩
                cnt[s2.charAt(l++) - 'a']--;
            }
            // 窗口收缩内，窗口内每个字符个数都和原串中一致，若此时窗口大小恰好=原串长度
            // 说明窗口内字符排序恰好为串s1的一个变位词
            if (r - l == m) {
                return true;
            }
        }
        return false;
    }

}
