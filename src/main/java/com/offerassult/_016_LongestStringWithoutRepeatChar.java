package com.offerassult;

/**
 * @author wangwei
 * @date 2022/10/15 13:37
 * @description: _016_LongestStringWithoutRepeatChar
 *
 * 剑指 Offer II 016. 不含重复字符的最长子字符串
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长连续子字符串 的长度。
 *
 *
 *
 * 示例 1:
 *
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子字符串是 "abc"，所以其长度为 3。
 * 示例 2:
 *
 * 输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子字符串是 "b"，所以其长度为 1。
 * 示例 3:
 *
 * 输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 * 示例 4:
 *
 * 输入: s = ""
 * 输出: 0
 *
 *
 * 提示：
 *
 * 0 <= s.length <= 5 * 104
 * s 由英文字母、数字、符号和空格组成
 *
 *
 * 注意：本题与主站 3 题相同： https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
 */
public class _016_LongestStringWithoutRepeatChar {


    /**
     * 滑动窗口
     *
     * 用window[255]保存当前窗口内每个字符的数量
     *
     * 逐个遍历s字符，每次将当前字符c加入窗口，当前窗口内c的个数加1
     * 当 window[c] 值大于 1 时，说明窗口中存在重复字符，不符合条件，就该移动 left 缩小窗口了嘛。
     * 因为我们每次加入一个字符都要保证它的次数不超过1，也就是不重复，所以我们窗口内保证了当前所有字符都不重复
     *
     * 唯一需要注意的是，在哪里更新结果 res 呢？
     *
     * 我们要的是最长无重复子串，哪一个阶段可以保证窗口中的字符串是没有重复的呢？
     *
     * 这里和之前不一样，要在收缩窗口完成后更新 res，因为窗口收缩的 while 条件是存在重复元素，
     * 换句话说[收缩完成后]一定保证窗口中[没有重复]嘛，所以每次缩小完窗口后就可以更新答案了
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int left = 0, right = 0, ans = 0;
        // 保存当前窗口内字符的数量
        int[] window = new int[255];
        // 逐个遍历
        while (right < n) {
            // 加入当前窗口
            char c = s.charAt(right++);
            // 字符数增加
            window[c]++;
            // 缩小窗口，去重
            while (window[c] > 1) {
                window[s.charAt(left++)]--;
            }
            // 更新答案
            ans = Math.max(ans, right - left);
        }
        return ans;
    }

    public static void main(String[] args) {
        _016_LongestStringWithoutRepeatChar obj = new _016_LongestStringWithoutRepeatChar();
        System.out.println(obj.lengthOfLongestSubstring("abcabcbb"));
        System.out.println(obj.lengthOfLongestSubstring("bbbbbb"));
        System.out.println(obj.lengthOfLongestSubstring("pwwkew"));
    }
}
