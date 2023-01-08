package com.daily;

/**
 * @author wangwei
 * @date 2023/1/8 16:00
 * @description: _2185_CountingWordsWithAGivenPrefix
 *
 * 2185. 统计包含给定前缀的字符串
 * 给你一个字符串数组 words 和一个字符串 pref 。
 *
 * 返回 words 中以 pref 作为 前缀 的字符串的数目。
 *
 * 字符串 s 的 前缀 就是  s 的任一前导连续字符串。
 *
 *
 *
 * 示例 1：
 *
 * 输入：words = ["pay","attention","practice","attend"], pref = "at"
 * 输出：2
 * 解释：以 "at" 作为前缀的字符串有两个，分别是："attention" 和 "attend" 。
 * 示例 2：
 *
 * 输入：words = ["leetcode","win","loops","success"], pref = "code"
 * 输出：0
 * 解释：不存在以 "code" 作为前缀的字符串。
 *
 *
 * 提示：
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length, pref.length <= 100
 * words[i] 和 pref 由小写英文字母组成
 * 通过次数26,066提交次数31,801
 */
public class _2185_CountingWordsWithAGivenPrefix {

    /**
     * 方法一：模拟
     *
     * 思路
     *
     * 按照题意，对 words 每个单词进行判断，是否以 pref 开头即可。最后返回满足条件的单词的数量。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/counting-words-with-a-given-prefix/solution/tong-ji-bao-han-gei-ding-qian-zhui-de-zi-aaq7/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param words
     * @param pref
     * @return
     */
    public int prefixCount(String[] words, String pref) {
        int ans = 0;
        // 逐个判断是否以pref开头
        for (String word : words) {
            if (word.startsWith(pref)) {
                ans++;
            }
        }
        return ans;
    }

}
