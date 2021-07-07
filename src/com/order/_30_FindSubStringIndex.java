package com.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangwei
 * @Description: 
 * 给定一个字符串 s 和一些长度相同的单词 words。找出 s 中恰好可以由 words 中所有单词串联形成的子串的起始位置。
 * 注意子串要与 words 中的单词完全匹配，中间不能有其他字符，但不需要考虑 words 中单词串联的顺序
 * @Time: 2019/12/14 周六 09:40
 **/
public class _30_FindSubStringIndex {

    public static List<Integer> solution1(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        Map<String, Integer> wordsMap = new HashMap<>();
        if (s.length() == 0 || words.length == 0) return res;
        for (String word: words) {
            // 主串s中没有这个单词，直接返回空
            if (s.indexOf(word) < 0) return res;
            // map中保存每个单词，和它出现的次数
            wordsMap.put(word, wordsMap.getOrDefault(word, 0) + 1);
        }
        // 主串s长度小于单词总和，返回空
        if (words.length * words[0].length() > s.length()) return res;
        // 每个单词的长度
        int oneLen = words[0].length(), wordsLen = oneLen * words.length;
        // 循环遍历s中长度为 oneLen * words.length 的全部子串
        for (int i = 0; i < s.length() - wordsLen + 1; ++i) {
            String substring = s.substring(i, i + wordsLen);
            // 记录每个子串中 每个长度为 oneLen 的单词的出现次数
            Map<String, Integer> subMap = new HashMap<>();
            int count = 0;
            // 取出子串每一个长度为oneLen的单词
            for (int j = 0; j < substring.length(); j += oneLen) {
                String word = substring.substring(j, j + oneLen);
                // 如果words[]中没有这个单词，当前子串匹配失败
                if (!wordsMap.containsKey(word)) break;
                subMap.put(word, subMap.getOrDefault(word, 0) + 1);
                // 当前子串中这个单词出现的次数 > word[]中这个单词出现的次数，当前字串也失败
                if (subMap.get(word) > wordsMap.get(word)) break;
                // 这个单词既在当前子串，又在words[]中，且个数未超过words[]中它的个数
                ++count;
            }
            // 当前子串中符合要求的单词数 == words[]的单词数，当前字串匹配成功，记录它的索引
            if (count == words.length) res.add(i);
        }
        return res;
    }

    /**
     * solution1的优化  滑动窗口
     * 在解法1中我们每次移动一个字符，去判断从当前位置开始的长度与words[]总长度相等的子串是不是恰好是words[]
     * 中所有单词的一个组合
     * 由于words[]中每个单词长度一样，所以其实哦我们可以每次移动一个单词的长度oneLen，但这样做我们必须讨论
     * 以下情况 如： "barfoothefoobarman" ["foo", "bar"]
     * 1.从b开始 bar foo the foo
     * 2.从a开始 arf oot hef
     * 3.从r开始 rfo oth efo
     * 之后都会是上面三种情况的重复，也就是必须讨论第一个位置是 0，1，..., onelen - 1
     * 
     * 思路：https://leetcode-cn.com/problems/substring-with-concatenation-of-all-words/solution/xiang-xi-tong-su-de-si-lu-fen-xi-duo-jie-fa-by-w-6/
     * 解法：https://leetcode-cn.com/problems/substring-with-concatenation-of-all-words/solution/chuan-lian-suo-you-dan-ci-de-zi-chuan-by-powcai/
     * @param s
     * @param words
     * @return
     */
    public static List<Integer> solution2(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        Map<String, Integer> wordsMap = new HashMap<>();
        if (s.length() == 0 || words.length == 0) return res;
        for (String word: words) {
            // 主串s中没有这个单词，直接返回空
            if (s.indexOf(word) < 0) return res;
            // map中保存每个单词，和它出现的次数
            wordsMap.put(word, wordsMap.getOrDefault(word, 0) + 1);
        }
        // 每个单词的长度， 总长度
        int oneLen = words[0].length(), wordsLen = oneLen * words.length;
        // 主串s长度小于单词总和，返回空
        if (wordsLen > s.length()) return res;
        // 只讨论从0，1，...， oneLen-1 开始的子串情况，
        // 每次进行匹配的窗口大小为 wordsLen，每次后移一个单词长度，由左右窗口维持当前窗口位置
        for (int i = 0; i < oneLen; ++i) {
            // 左右窗口
            int left = i, right = i, count = 0;
            // 统计每个符合要求的word
            Map<String, Integer> subMap = new HashMap<>();
            // 右窗口不能超出主串长度
            while (right + oneLen <= s.length()) {
                // 得到一个单词
                String word = s.substring(right, right + oneLen);
                // 有窗口右移
                right += oneLen;
                // words[]中没有这个单词，那么当前窗口肯定匹配失败，直接右移到这个单词后面
                if (!wordsMap.containsKey(word)) {
                    left = right;
                    // 窗口内单词统计map清空，重新统计
                    subMap.clear();
                    // 符合要求的单词数清0
                    count = 0;
                } else {
                    // 统计当前子串中这个单词出现的次数
                    subMap.put(word, subMap.getOrDefault(word, 0) + 1);
                    ++count;
                    // 如果这个单词出现的次数大于words[]中它对应的次数，又由于每次匹配和words长度相等的子串
                    // 如 ["foo","bar","foo","the"]  "| foobarfoobar| foothe"
                    // 第二个bar虽然是words[]中的单词，但是次数抄了，那么右移一个单词长度后 "|barfoobarfoo|the"
                    // bar还是不符合，所以直接从这个不符合的bar之后开始匹配，也就是将这个不符合的bar和它之前的单词(串)全移出去
                    while (subMap.getOrDefault(word, 0) > wordsMap.getOrDefault(word, 0)) {
                        // 从当前窗口字符统计map中删除从左窗口开始到数量超限的所有单词(次数减一)
                        String w = s.substring(left, left + oneLen);
                        subMap.put(w, subMap.getOrDefault(w, 0) - 1);
                        // 符合的单词数减一
                        --count;
                        // 左窗口位置右移
                        left += oneLen;
                    }
                    // 当前窗口字符串满足要求
                    if (count == words.length) res.add(left);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        // System.out.println(solution1());
        solution1("barfoothefoobarman", new String[]{"foo", "bar"});
    }
}
