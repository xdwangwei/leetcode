package com.daily;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/4/27 21:13
 * @description: _1048_LongestStringChain
 *
 * 1048. 最长字符串链
 * 给出一个单词数组 words ，其中每个单词都由小写英文字母组成。
 *
 * 如果我们可以 不改变其他字符的顺序 ，在 wordA 的任何地方添加 恰好一个 字母使其变成 wordB ，那么我们认为 wordA 是 wordB 的 前身 。
 *
 * 例如，"abc" 是 "abac" 的 前身 ，而 "cba" 不是 "bcad" 的 前身
 * 词链是单词 [word_1, word_2, ..., word_k] 组成的序列，k >= 1，其中 word1 是 word2 的前身，word2 是 word3 的前身，依此类推。
 * 一个单词通常是 k == 1 的 单词链 。
 *
 * 从给定单词列表 words 中选择单词组成词链，返回 词链的 最长可能长度 。
 *
 *
 * 示例 1：
 *
 * 输入：words = ["a","b","ba","bca","bda","bdca"]
 * 输出：4
 * 解释：最长单词链之一为 ["a","ba","bda","bdca"]
 * 示例 2:
 *
 * 输入：words = ["xbc","pcxbcf","xb","cxbc","pcxbc"]
 * 输出：5
 * 解释：所有的单词都可以放入单词链 ["xb", "xbc", "cxbc", "pcxbc", "pcxbcf"].
 * 示例 3:
 *
 * 输入：words = ["abcd","dbqca"]
 * 输出：1
 * 解释：字链["abcd"]是最长的字链之一。
 * ["abcd"，"dbqca"]不是一个有效的单词链，因为字母的顺序被改变了。
 *
 *
 * 提示：
 *
 * 1 <= words.length <= 1000
 * 1 <= words[i].length <= 16
 * words[i] 仅由小写英文字母组成。
 * 通过次数30,343提交次数55,672
 */
public class _1048_LongestStringChain {

    /**
     * 方法：动态规划
     *
     * 方法一：动态规划
     * 思路与算法
     *
     * 根据题意可知，对于字符串「前身」的定义为：
     *
     * 不改变其他字符的顺序 ，在 wordA 的任何地方添加恰好一个字母使其变成 wordB，那么我们认为 wordA 是 wordB 的前身。
     * 将 wordB 中去掉任意一个字母，其余字符保持不变构成的字符串即为 wordB 的前身。
     *
     * 对于每个字符串 s，假设其所有的前身 si 为结尾的最长链的长度为 l，即可知道以 s 为结尾的最长链的长度为 l+1。
     *
     * 为保证我们求 s 的最长链时，其所有的前身的最长链的长度均已求出，需要将所有的字符串按照长度大小进行排序。
     *
     * 假设字符串 s 最长链的长度为 cnt(s) 的前身为 s1、s2、s3、...，则此时可以知道
     *
     *      cnt(s) = max(cnt(si)), i ∈ [0,k], k = s.length()-1
     *
     * 根据以上结论，实际计算过程如下：
     *
     * 首先对字符串数组 words 按照字符串长度的大小进行排序；
     * 依次遍历每个字符串 words[i]，并初始以 words[i] 为结尾的最长链的长度 cnt[words[i]] 为 1；
     * 依次尝试去掉 words[i] 中的每个字符，并构成其可能的前身 prev，在哈希表 cnt 查找 prev 对应的最长链长度，如果
     * cnt+1 大于 cnt[words[i]]，则更新 cnt[words[i]]；
     * 最终返回可能的最长链的长度即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/longest-string-chain/solution/zui-chang-zi-fu-chuan-lian-by-leetcode-s-4uoe/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param words
     * @return
     */
    public int longestStrChain(String[] words) {
        // 以 s 为末尾串的最长链的长度
        Map<String, Integer> dp = new HashMap<>();
        // 按长度从小到大排序，保证求 s 的最长链时，其所有前身（长度=s.length-1）的最长链已经求出
        Arrays.sort(words, Comparator.comparingInt(String::length));
        // 所有单词结尾的最长链的最大值
        int ans = 0;
        // 递推，顺序遍历排序后的字符串
        for (String word : words) {
            int n = word.length();
            // 初始化以当前字符串为结尾的最长链的长度为1
            int max = 1;
            // 去除任意位置上的字符，得到所有可能的前身
            for (int i = 0; i < n; ++i) {
                String pre = word.substring(0, i) + word.substring(i + 1);
                // 当前字符串的最长链长度 = max(前身的最长链长度) + 1
                max = Math.max(max, dp.getOrDefault(pre, 0) + 1);
            }
            // 保存当前单词的最长链
            dp.put(word, max);
            // 更新所有最长链的长度最大值
            ans = Math.max(ans, max);
        }
        // 返回
        return ans;
    }
}
