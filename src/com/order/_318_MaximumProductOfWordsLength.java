package com.order;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/5/20 23:52
 * @description: _318_MaximumProductOfWordsLength
 *
 * 318. 单词长度的最大乘积
 * 给定一个字符串数组 words，请计算当两个字符串 words[i] 和 words[j] 不包含相同字符时，它们长度的乘积的最大值。假设字符串中只包含英语的小写字母。如果没有不包含相同字符的一对字符串，返回 0。
 *
 *
 *
 * 示例 1:
 *
 * 输入: words = ["abcw","baz","foo","bar","fxyz","abcdef"]
 * 输出: 16
 * 解释: 这两个单词为 "abcw", "fxyz"。它们不包含相同字符，且长度的乘积最大。
 * 示例 2:
 *
 * 输入: words = ["a","ab","abc","d","cd","bcd","abcd"]
 * 输出: 4
 * 解释: 这两个单词为 "ab", "cd"。
 * 示例 3:
 *
 * 输入: words = ["a","aa","aaa","aaaa"]
 * 输出: 0
 * 解释: 不存在这样的两个单词。
 *
 *
 * 提示：
 *
 * 2 <= words.length <= 1000
 * 1 <= words[i].length <= 1000
 * words[i] 仅包含小写字母
 */
public class _318_MaximumProductOfWordsLength {


    /**
     * 方法一：位运算
     *
     * 为了得到单词长度的最大乘积，朴素的做法是，遍历字符串数组 words 中的每一对单词，判断这一对单词是否有公共字母，如果没有公共字母，则用这一对单词的长度乘积更新单词长度的最大乘积。
     *
     * 用 n 表示数组 words 的长度，用 l_i表示单词 words[i] 的长度，其中 0≤i<n，
     * 则上述做法需要遍历字符串数组 words 中的每一对单词，对于下标为 i 和 j 的单词，其中 i<j，需要 O(l_i * l_j)的时间判断是否有公共字母和计算长度乘积。
     *
     * 因此上述做法的时间复杂度是 O(sum_{0≤i<j<n, l_i * l_j})，该时间复杂度高于 O(n^2)O(n
     *
     * 如果可以将判断两个单词是否有公共字母的时间复杂度降低到 O(1)，则可以将总时间复杂度降低到 O(n^2)
     * 可以使用位运算预处理每个单词，通过位运算操作判断两个单词是否有公共字母。
     *
     * 由于单词只包含小写字母，共有 26 个小写字母，因此可以使用位掩码的最低 26 位分别表示每个字母是否在这个单词中出现。
     * 将 a 到 z 分别记为第 0 个字母到第 25 个字母，则位掩码的从低到高的第 i 位是 1 当且仅当第 i 个字母在这个单词中，其中 0≤i≤25。
     *
     * 用数组 masks 记录每个单词的位掩码表示。计算数组 masks 之后，判断第 i 个单词和第 j 个单词是否有公共字母可以通过判断 masks[i]&masks[j] 是否等于 0 实现，
     * 当且仅当 masks[i]&masks[j]=0 时第 i 个单词和第 j 个单词没有公共字母，此时使用这两个单词的长度乘积更新单词长度的最大乘积。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/aseY1I/solution/dan-ci-chang-du-de-zui-da-cheng-ji-by-le-l5mu/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param words
     * @return
     */
    public int maxProduct(String[] words) {
        int n = words.length;
        // 统计每个单词出现了哪些字母
        // 'a' 出现，代表 mask[i] 的二进制中 第 'a' - 'a' 位置为1
        int[] mask = new int[n];
        for (int i = 0; i < n; ++i) {
            // 统计每个单词出现了哪些字母
            for (int j = 0; j < words[i].length(); ++j) {
                // 将对应二进制位置为1
                mask[i] |= (1 << (words[i].charAt(j) - 'a'));
            }
        }
        int ans = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                // 当二者无公共字母时
                if ((mask[i] & mask[j]) == 0) {
                    // 更新结果
                    ans = Math.max(ans, words[i].length() * words[j].length());
                }
            }
        }
        return ans;
    }


    /**
     * 不难发现，对于词频相同（mask 值相等）的两字符，只需要保留字符长度大的即可，因此我们可以使用「哈希表」代替 masks 数组。
     *
     * 也就是说。不用mask数组保存每个单词出现了哪些字母，当出现字母相同时，只留下更长的那个
     *
     * 少一部份空间 和 时间，整体 还是和一一样呢
     * @param words
     * @return
     */
    public int maxProduct2(String[] words) {
        int n = words.length;
        // 统计每个单词出现了哪些字母，相同词频单词，只保留更长的那个
        // 'a' 出现，代表 mask 的二进制中 第 'a' - 'a' 位置为1
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            // 统计每个单词出现了哪些字母
            int mask = 0;
            for (int j = 0; j < words[i].length(); ++j) {
                // 将对应二进制位置为1
                mask |= (1 << (words[i].charAt(j) - 'a'));
            }
            // 相同词频单词，只保留更长的那个
            if (!countMap.containsKey(mask) || countMap.get(mask) < words[i].length()) {
                countMap.put(mask, words[i].length());
            }
        }
        int ans = 0;
        // 字母没有交集的单词，长度乘积最大值
        for (Integer mask1 : countMap.keySet()) {
            for (Integer mask2 : countMap.keySet()) {
                // 当二者无公共字母时
                if ((mask1 & mask2) == 0) {
                    // 更新结果
                    ans = Math.max(ans, countMap.get(mask1) * countMap.get(mask2));
                }
            }
        }
        return ans;
    }
}
