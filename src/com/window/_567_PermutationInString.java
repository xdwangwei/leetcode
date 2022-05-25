package com.window;

import java.util.HashMap;

/**
 * @author wangwei
 * 2020/7/27 12:40
 * 给定两个字符串 s1 和 s2，写一个函数来判断 s2 是否包含 s1 的排列。
 *
 * 换句话说，第一个字符串的排列之一是第二个字符串的子串。
 *
 * 示例1:
 *
 * 输入: s1 = "ab" s2 = "eidbaooo"
 * 输出: True
 * 解释: s2 包含 s1 的排列之一 ("ba").
 *  
 *
 * 示例2:
 *
 * 输入: s1= "ab" s2 = "eidboaoo"
 * 输出: False
 *  
 *
 * 注意：
 *
 * 输入的字符串只包含小写字母
 * 两个字符串的长度都在 [1, 10,000] 之间
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/permutation-in-string
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 */
public class _567_PermutationInString {

    /**
     * 滑动窗口
     * 是明显的滑动窗口算法，相当给你一个 S 和一个 T，
     * 请问你 S 中是否存在一个子串，包含 T 中所有字符 且不包含 其他字符？
     *
     * 其实和_438_一样，438是找s中所有子串中 那些子串==t的另一种排列（长度一样，字符一样，次数一样）
     *
     * 这里只需要找到一个就可以返回
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
     * 当 当前窗口大小>=t的长度时 收缩窗口；
     * 应该在收缩窗口的时候更新最终结果（缩小窗口的前提是当前窗口大小=t.length，在这个前提下，如果窗口包含了t的所有字母，所以才能更新结果）
     *
     * 注意题目问的是 s2 是否包含 s1 的排列
     * @param s1
     * @param s2
     * @return
     */
    public boolean checkInclusion(String s1, String s2) {
        if (s2.length() < s1.length()) return false;
        // 窗口中应该包含哪些字符，及其个数
        HashMap<Character, Integer> need = new HashMap<>();
        // 窗口中包含的字符，及其个数
        HashMap<Character, Integer> window = new HashMap<>();
        // 窗口中包含的满足要求的字符个数
        int valid = 0;

        // 统计需要出现的字符和个数
        for (int i = 0; i < s1.length(); ++i) {
            need.put(s1.charAt(i), need.getOrDefault(s1.charAt(i), 0) + 1);
        }

        // 滑动窗口
        int left = 0, right = 0;

        while (right < s2.length()) {
            char c = s2.charAt(right);
            // 扩大窗口右边界
            right++;
            // 更新窗口内数据
            // 如果这个字符需要包含
            if (need.containsKey(c)) {
                // 他在窗口出现次数加1
                window.put(c, window.getOrDefault(c, 0) + 1);
                // 如果他的出现次数达到了s2中的次数
                // 那么当前窗口内满足要求的字符数加1
                if (window.get(c).intValue() == need.get(c).intValue())
                    valid++;
            }

            // 因为寻找的是s1的排列，所以窗口大小应该保持和s1的长度一样
            // 缩小窗口
            while (right - left >= s1.length()) {
                // 如果此时窗口的合法字符数达到要求
                // 说明未包含其他字符，此时的窗口代表的子串就是s2的一个排列
                if (valid == need.size()) return true;

                // 当前窗口最左边的字符
                char d = s2.charAt(left);
                // 扩大窗口左边界，缩小窗口
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
        return false;
    }

    public boolean checkInclusion2(String s1, String s2) {
        if (s2.length() < s1.length()) return false;
        // 窗口中应该包含哪些字符，及其个数
        HashMap<Character, Integer> need = new HashMap<>();
        // 窗口中包含的字符，及其个数
        HashMap<Character, Integer> window = new HashMap<>();
        // 窗口中包含的满足要求的字符个数
        int valid = 0;

        // 统计需要出现的字符和个数
        for (int i = 0; i < s1.length(); ++i) {
            need.put(s1.charAt(i), need.getOrDefault(s1.charAt(i), 0) + 1);
        }

        // 滑动窗口
        int left = 0, right = 0;

        while (right < s2.length()) {
            char c = s2.charAt(right);
            // 扩大窗口右边界
            right++;

            // 更新窗口内数据
            // 他在窗口出现次数加1
            window.put(c, window.getOrDefault(c, 0) + 1);
            // 如果他的出现次数达到了s2中的次数
            // 那么当前窗口内满足要求的字符数加1
            if (window.get(c).intValue() == need.get(c).intValue())
                valid++;

            // 因为寻找的是s1的排列，所以窗口大小应该保持和s1的长度一样
            // 缩小窗口
            while (right - left >= s1.length()) {
                // 如果此时窗口的合法字符数达到要求
                // 说明未包含其他字符，此时的窗口代表的子串就是s2的一个排列
                if (valid == need.size()) return true;

                // 当前窗口最左边的字符
                char d = s2.charAt(left);
                // 扩大窗口左边界，缩小窗口
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
        return false;
    }

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
    public boolean checkInclusion8(String s1, String s2) {
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
