package com.daily;

/**
 * @author wangwei
 * @date 2022/11/8 10:25
 * @description: _1684_CountNumberOfConsistStrings
 * 1684. 统计一致字符串的数目
 * 给你一个由不同字符组成的字符串 allowed 和一个字符串数组 words 。如果一个字符串的每一个字符都在 allowed 中，就称这个字符串是 一致字符串 。
 *
 * 请你返回 words 数组中 一致字符串 的数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：allowed = "ab", words = ["ad","bd","aaab","baa","badab"]
 * 输出：2
 * 解释：字符串 "aaab" 和 "baa" 都是一致字符串，因为它们只包含字符 'a' 和 'b' 。
 * 示例 2：
 *
 * 输入：allowed = "abc", words = ["a","b","c","ab","ac","bc","abc"]
 * 输出：7
 * 解释：所有字符串都是一致的。
 * 示例 3：
 *
 * 输入：allowed = "cad", words = ["cc","acd","b","ba","bac","bad","ac","d"]
 * 输出：4
 * 解释：字符串 "cc"，"acd"，"ac" 和 "d" 是一致字符串。
 *
 *
 * 提示：
 *
 * 1 <= words.length <= 104
 * 1 <= allowed.length <= 26
 * 1 <= words[i].length <= 10
 * allowed 中的字符 互不相同 。
 * words[i] 和 allowed 只包含小写英文字母。
 */
public class _1684_CountNumberOfConsistStrings {

    /**
     * 方法一：统计allowed中每个字符出现次数，cnt[]
     * 遍历words中每个word，如果word中某个字符在cnt[]中值为0，则无效跳过
     * @param allowed
     * @param words
     * @return
     */
    public int countConsistentStrings(String allowed, String[] words) {
        int[] cnt = new int[26];
        // 统计allowed中每个字符出现次数，cnt[]
        for (char c : allowed.toCharArray()) {
            cnt[c - 'a']++;
        }
        int ans = 0;
        // 遍历words中每个word，
        for (String word: words) {
            boolean flag = true;
            for (char c : word.toCharArray()) {
                // 如果word中某个字符在cnt[]中值为0，则无效跳过
                if (cnt[c - 'a'] == 0) {
                    flag = false;
                    break;
                }
            }
            if (flag) ans++;
        }
        return ans;
    }


    /**
     * 方法二：方法一空间优化
     * 其实可以看到我们并不需要直到字符出现次数，只需要知道它是否出现
     * 题目表明只可能出现26个小写字母
     * 所以我们可以用一个int变量的二进制的低26位来代表这26个字母是否出现
     * 比如 ‘c’ 出现，让 mask |= 1 << ('c' -'a')
     * @param allowed
     * @param words
     * @return
     */
    public int countConsistentStrings2(String allowed, String[] words) {
        int mask = 0;
        // 用一个int变量的二进制的低26位来代表这26个字母是否出现
        for (char c : allowed.toCharArray()) {
            mask |= 1 << (c - 'a');
        }
        int ans = 0;
        for (String word: words) {
            // 统计当前单词中哪些字符出现
            int mask1 = 0;
            for (char c : word.toCharArray()) {
                mask1 |= 1 << (c - 'a');
            }
            // 如果word中每个字符都在allowed中出现
            if ((mask | mask1) == mask) ans++;
        }
        return ans;
    }
}
