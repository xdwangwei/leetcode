package com.daily;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wangwei
 * 2022/4/17 14:43
 *
 * 819. 最常见的单词
 * 给定一个段落 (paragraph) 和一个禁用单词列表 (banned)。返回出现次数最多，同时不在禁用列表中的单词。
 *
 * 题目保证至少有一个词不在禁用列表中，而且答案唯一。
 *
 * 禁用列表中的单词用小写字母表示，不含标点符号。段落中的单词不区分大小写。答案都是小写字母。
 *
 *
 *
 * 示例：
 *
 * 输入:
 * paragraph = "Bob hit a ball, the hit BALL flew far after it was hit."
 * banned = ["hit"]
 * 输出: "ball"
 * 解释:
 * "hit" 出现了3次，但它是一个禁用的单词。
 * "ball" 出现了2次 (同时没有其他单词出现2次)，所以它是段落里出现次数最多的，且不在禁用列表中的单词。
 * 注意，所有这些单词在段落里不区分大小写，标点符号需要忽略（即使是紧挨着单词也忽略， 比如 "ball,"），
 * "hit"不是最终的答案，虽然它出现次数更多，但它在禁用单词列表中。
 *
 *
 * 提示：
 *
 * 1 <= 段落长度 <= 1000
 * 0 <= 禁用单词个数 <= 100
 * 1 <= 禁用单词长度 <= 10
 * 答案是唯一的, 且都是小写字母 (即使在 paragraph 里是大写的，即使是一些特定的名词，答案都是小写的。)
 * paragraph 只包含字母、空格和下列标点符号!?',;.
 * 不存在没有连字符或者带有连字符的单词。
 * 单词里只包含字母，不会出现省略号或者其他标点符号。
 * 通过次数33,161提交次数75,110
 */
public class _819_MostCommonWord {

    /**
     * 直接统计每个【不在禁用列表】中的单词的出现次数，返回次数最多的那个单词
     * @param paragraph
     * @param banned
     * @return
     */
    public String mostCommonWord(String paragraph, String[] banned) {
        // 保存所有单词和出现次数
        Map<String, Integer> wordCountMap = new HashMap<>();
        // 禁用列表
        Set<String> bannedSet = new HashSet<>();
        for (String word: banned) {
            bannedSet.add(word);
        }
        // 保存结果和对应次数
        int max = 0;
        String res = "";
        // 分割字符串，注意题目提示中的可能的分割字符
        // String[] words = paragraph.split("[ ,.!;?']");
        // \w == [a-zA-z0-9] ^\w就是除此之外的字符
        String[] words = paragraph.split("[^\\w+]");
        for (String word: words) {
            // 跳过空串
            if (word.equals("")) {
                continue;
            }
            // 统一用小写判断
            word = word.toLowerCase();
            // 跳过被禁用的
            if (bannedSet.contains(word)) {
                continue;
            }
            // 获取次数并更新
            int count = wordCountMap.getOrDefault(word, 0);
            count += 1;
            // 更新结果
            if (count > max) {
                max = count;
                res = word;
            }
            // 更新map
            wordCountMap.put(word, count);
        }
        // 返回
        return res;
    }

    public static void main(String[] args) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        wordCountMap.put("Hello" ,1);
        wordCountMap.put("hello", 2);
        String word = "hello";
        word = word.toLowerCase();
        System.out.println(word);
        System.out.println(wordCountMap.get("Hello"));
    }
}
