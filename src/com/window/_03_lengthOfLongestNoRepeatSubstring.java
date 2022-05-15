package com.window;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/21 周四 08:51
 *
 * 3. 无重复字符的最长子串
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 *
 *
 *
 * 示例 1:
 *
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 示例 2:
 *
 * 输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 示例 3:
 *
 * 输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 *
 *
 * 提示：
 *
 * 0 <= s.length <= 5 * 104
 * s 由英文字母、数字、符号和空格组成
 **/
public class _03_lengthOfLongestNoRepeatSubstring {
    
    // 滑动窗口 [i, j)
    // 如果从索引 i 到 j - 1之间的子字符串 s[i][j]已经被检查为没有重复字符。
    // 我们只需要检查 s[j] 对应的字符是否已经存在于子字符串Sij(set)中
    // 使用 HashSet 将字符存储在当前窗口 [i, j)（最初 j = i = 0）中。
    // 然后我们向右侧滑动索引 j，如果它不在 HashSet 中，我们会继续滑动 j。直到 s[j] 已经存在于 HashSet 中。
    // 此时，我们找到的没有重复字符的最长子字符串将会以索引 i 开头。对所有的 i这样做，就可以得到答案。
    public static int solution(String s){
        int res = 0;
        HashSet<Character> set = new HashSet<>();
        int i = 0, j = 0;
        int len = s.length();
        while (j < len){
            if(! set.contains(s.charAt(j))){ // 集合中没有这个字符，加进去
                set.add(s.charAt(j++)); // j 后移
                res = Math.max(res, j - i); // 当前窗口大小
            }else { // 集合里有s[j]这个字符了
                // 之前的最大串长度已保存，删除集合开始元素到与s[j]一样的字符的所有字符，从它之后判断其他串，i后移
                // 如 aebcdbpmn ,[a,d)->[a,第二个b)->删除第一个b及之前的字符[c,b),j++
                set.remove(s.charAt(i++)); 
            }
        }
        return res;
    }

    /**
     * 套滑动窗口模板 _76 _438 _567
     *
     * 当 window[c] 值大于 1 时，说明窗口中存在重复字符，不符合条件，就该移动 left 缩小窗口了嘛。
     *
     * 唯一需要注意的是，在哪里更新结果 res 呢？
     *
     * 我们要的是最长无重复子串，哪一个阶段可以保证窗口中的字符串是没有重复的呢？
     *
     * 这里和之前不一样，要在收缩窗口完成后更新 res，因为窗口收缩的 while 条件是存在重复元素，
     * 换句话说[收缩完成后]一定保证窗口中[没有重复]嘛。
     *
     * @param s
     * @param
     * @return
     */
    public int window(String s) {
        // if (s.length() < t.length()) return "";

        // 最小合法字符出串的长度
        int max = 0;
        // String res = "";

        // 统计需要包含的字符
        // HashMap<Character, Integer> need = new HashMap<>();
        // 滑动窗口
        HashMap<Character, Integer> window = new HashMap<>();
        // 统计窗口中包含了几个满足要求的字符
        // int valid = 0;

        // 滑动窗口
        int left = 0, right = 0;
        // 统计需要出现的字符及其个数
        // for (int i = 0; i < t.length(); ++i) {
        //     need.put(t.charAt(i), need.getOrDefault(t.charAt(i), 0) + 1);
        // }

        // 扩大窗口的前提
        while (right < s.length()) {
            // 待加入窗口的字符
            char c = s.charAt(right);
            // 扩大右边界
            right++;
            // 更新窗口中的一系列数据
            // 如果这个字符的确是需要出现的，
            // if (need.containsKey(c)) {
            // 窗口中这个字符次数加1
            window.put(c, window.getOrDefault(c, 0) + 1);
            // 并且在窗口中的出现次数达到要求，那么窗口中的有效字符数加1
            // 因为map存储的是对象，用==比较会非常慢，所以采用intVlue或equals
            // if (need.get(c).intValue() == window.get(c).intValue())
            // valid++;
            // }

            // // 当窗口中的合法字符够了的时候，就可以更新答案，然后缩小窗口(友移左边界)
            // 若当前字符在窗口中的出现次数大于1，就应该缩小左窗口
            // // while (valid == need.size()) {
            while (window.get(c).intValue() > 1) {
                // if (right - left < min) {
                //     // 窗口是左闭右开的
                //     min = right - left;
                //     res = s.substring(left, right);
                // }
                // 待移出窗口的字符
                char d = s.charAt(left);
                // 增加左边界，缩小窗口
                left++;
                // 更新窗口中的一系列数据
                // 如果这个字符是需要包含的，那就更新窗口中的数据
                // if (need.containsKey(d)) {
                //     // 如果这个字符在窗口中的出现次数已经达到要求了
                //     if (need.get(d).intValue() == window.get(d).intValue())
                //         valid--; // 窗口内的有效字符数减1
                // 这个字符出现次数减1
                window.put(d, window.get(d) - 1);
                // }
            }

            // 调整完窗口，更新结果
            max = Math.max(max, right - left);
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(solution("abcabcbb")); // 3
        System.out.println(solution("bbbbbb")); // 1
        System.out.println(solution("pwwkew")); // 3
    }
}
