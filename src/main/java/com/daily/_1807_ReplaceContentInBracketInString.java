package com.daily;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/1/12 11:51
 * @description: _1807_ReplaceContentInBracketInString
 * 1807. 替换字符串中的括号内容
 * 给你一个字符串 s ，它包含一些括号对，每个括号中包含一个 非空 的键。
 *
 * 比方说，字符串 "(name)is(age)yearsold" 中，有 两个 括号对，分别包含键 "name" 和 "age" 。
 * 你知道许多键对应的值，这些关系由二维字符串数组 knowledge 表示，其中 knowledge[i] = [keyi, valuei] ，表示键 keyi 对应的值为 valuei 。
 *
 * 你需要替换 所有 的括号对。当你替换一个括号对，且它包含的键为 keyi 时，你需要：
 *
 * 将 keyi 和括号用对应的值 valuei 替换。
 * 如果从 knowledge 中无法得知某个键对应的值，你需要将 keyi 和括号用问号 "?" 替换（不需要引号）。
 * knowledge 中每个键最多只会出现一次。s 中不会有嵌套的括号。
 *
 * 请你返回替换 所有 括号对后的结果字符串。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "(name)is(age)yearsold", knowledge = [["name","bob"],["age","two"]]
 * 输出："bobistwoyearsold"
 * 解释：
 * 键 "name" 对应的值为 "bob" ，所以将 "(name)" 替换为 "bob" 。
 * 键 "age" 对应的值为 "two" ，所以将 "(age)" 替换为 "two" 。
 * 示例 2：
 *
 * 输入：s = "hi(name)", knowledge = [["a","b"]]
 * 输出："hi?"
 * 解释：由于不知道键 "name" 对应的值，所以用 "?" 替换 "(name)" 。
 * 示例 3：
 *
 * 输入：s = "(a)(a)(a)aaa", knowledge = [["a","yes"]]
 * 输出："yesyesyesaaa"
 * 解释：相同的键在 s 中可能会出现多次。
 * 键 "a" 对应的值为 "yes" ，所以将所有的 "(a)" 替换为 "yes" 。
 * 注意，不在括号里的 "a" 不需要被替换。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 105
 * 0 <= knowledge.length <= 105
 * knowledge[i].length == 2
 * 1 <= keyi.length, valuei.length <= 10
 * s 只包含小写英文字母和圆括号 '(' 和 ')' 。
 * s 中每一个左圆括号 '(' 都有对应的右圆括号 ')' 。
 * s 中每对括号内的键都不会为空。
 * s 中不会有嵌套括号对。
 * keyi 和 valuei 只包含小写英文字母。
 * knowledge 中的 keyi 不会重复。
 * 通过次数16,766提交次数25,251
 *
 */
public class _1807_ReplaceContentInBracketInString {

    /**
     * hash表 + 模拟
     *
     * 题目，对于 s 中每一个 (key) 子串部分，替换为 knowledge 中 pair(key->value) 确定的 value，
     *      若 knowledge 中不存在此key的映射，则替换为 ?
     *
     * 为方便起见，将 knowledge 中的 pair 转存到 哈希表 map
     *
     * 用 resBuilder 保存替换完成的字符串，即返回值
     * 用 keyBuilder 保存遍历 s 过程中遇到的 每一个 (key) 中的 key 部分
     * 遍历 s 每一个字符
     *      如果 遇到 (，则继续遍历，直到遇到 )，() 中间部分保存到 keyBuilder 中
     *          若 map 中包含 key 的映射，则在 resBuilder 中追加 map.get(key)，否则追加 ?
     *          继续下一个字符，清空 key
     *      否则，直接追加当前字符到 resBuilder, 继续下一个字符
     * @param s
     * @param knowledge
     * @return
     */
    public String evaluate(String s, List<List<String>> knowledge) {
        // pair 转存到 map
        Map<String, String> map = new HashMap<>();
        for (List<String> pair : knowledge) {
            map.put(pair.get(0), pair.get(1));
        }
        // 保存替换后的结果
        StringBuilder resBuilder = new StringBuilder();
        // 保存每一个 (key) 部分 的 key
        StringBuilder keyBuilder = new StringBuilder();
        char[] chars = s.toCharArray();
        // 遍历
        for (int i = 0; i < chars.length; ) {
            // 遇到 (
            if (chars[i] == '(') {
                // 继续遍历直到遇到 )，避免越界
                while (++i < chars.length && chars[i] != ')') {
                    // ()中间部分保存到 keyBuilder
                    keyBuilder.append(chars[i]);
                }
                // 得到此部分的替换值，map.get(key) 或者 ?
                resBuilder.append(map.getOrDefault(keyBuilder.toString(), "?"));
                // 跳过 )，遍历下一个字符
                i++;
                // 清空 keyBuilder
                keyBuilder.setLength(0);
            } else {
                // 遇到其他字符，不用替换，直接追加到 resBuilder
                resBuilder.append(chars[i++]);
            }
        }
        // 返回替换后的结果
        return resBuilder.toString();
    }
}
