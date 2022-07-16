package com.daily;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/4/21 9:22
 * @description: _824_GoatLatin
 *
 * 824. 山羊拉丁文
 * 给你一个由若干单词组成的句子 sentence ，单词间由空格分隔。每个单词仅由大写和小写英文字母组成。
 *
 * 请你将句子转换为 “山羊拉丁文（Goat Latin）”（一种类似于 猪拉丁文 - Pig Latin 的虚构语言）。山羊拉丁文的规则如下：
 *
 * 如果单词以元音开头（'a', 'e', 'i', 'o', 'u'），在单词后添加"ma"。
 * 例如，单词 "apple" 变为 "applema" 。
 * 如果单词以辅音字母开头（即，非元音字母），移除第一个字符并将它放到末尾，之后再添加"ma"。
 * 例如，单词 "goat" 变为 "oatgma" 。
 * 根据单词在句子中的索引，在单词最后添加与索引相同数量的字母'a'，索引从 1 开始。
 * 例如，在第一个单词后添加 "a" ，在第二个单词后添加 "aa" ，以此类推。
 * 返回将 sentence 转换为山羊拉丁文后的句子。
 *
 *
 *
 * 示例 1：
 *
 * 输入：sentence = "I speak Goat Latin"
 * 输出："Imaa peaksmaaa oatGmaaaa atinLmaaaaa"
 * 示例 2：
 *
 * 输入：sentence = "The quick brown fox jumped over the lazy dog"
 * 输出："heTmaa uickqmaaa rownbmaaaa oxfmaaaaa umpedjmaaaaaa overmaaaaaaa hetmaaaaaaaa azylmaaaaaaaaa ogdmaaaaaaaaaa"
 *
 *
 * 提示：
 *
 * 1 <= sentence.length <= 150
 * sentence 由英文字母和空格组成
 * sentence 不含前导或尾随空格
 * sentence 中的所有单词由单个空格分隔
 */
public class _824_GoatLatin {

    /**
     * 按照给定规则进行模拟即可，注意 aeiou 和 AEIOU 都是元音字母(第一个例子就是首字母是大写元音字母)，并且 hashcode不同
     * Character类 hashcode 返回 的 是字符的ascii码的整数值
     * @param sentence
     * @return
     */
    public String toGoatLatin(String sentence) {
        Set<Character> set = new HashSet<>();
        set.add('a');
        set.add('e');
        set.add('i');
        set.add('o');
        set.add('u');
        set.add('A');
        set.add('E');
        set.add('I');
        set.add('O');
        set.add('U');
        StringBuilder builder = new StringBuilder();
        // 分割
        String[] words = sentence.split(" ");
        for (int i = 0; i < words.length; ++i) {
            String word = words[i];
            // 是否是元音字母开头哦
            if (set.contains(word.charAt(0))) {
                builder.append(word);
                builder.append("ma");
            } else {
                builder.append(word.substring(1));
                builder.append(word.charAt(0));
                builder.append("ma");
            }
            // 第i个单词，拼接i个’a‘
            for (int j = i; j >= 0; --j) {
                builder.append("a");
            }
            if (i != words.length - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        HashSet<Character> set = new HashSet<>();
        set.add('a');
        set.add('A');
        System.out.println(set.size());
    }
}
