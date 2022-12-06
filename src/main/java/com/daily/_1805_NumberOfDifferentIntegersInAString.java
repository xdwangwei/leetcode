package com.daily;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/12/6 11:30
 * @description: _1805_NumberOfDifferentIntegersInAString
 *
 * 1805. 字符串中不同整数的数目
 * 给你一个字符串 word ，该字符串由数字和小写英文字母组成。
 *
 * 请你用空格替换每个不是数字的字符。例如，"a123bc34d8ef34" 将会变成 " 123  34 8  34" 。注意，剩下的这些整数为（相邻彼此至少有一个空格隔开）："123"、"34"、"8" 和 "34" 。
 *
 * 返回对 word 完成替换后形成的 不同 整数的数目。
 *
 * 只有当两个整数的 不含前导零 的十进制表示不同， 才认为这两个整数也不同。
 *
 *
 *
 * 示例 1：
 *
 * 输入：word = "a123bc34d8ef34"
 * 输出：3
 * 解释：不同的整数有 "123"、"34" 和 "8" 。注意，"34" 只计数一次。
 * 示例 2：
 *
 * 输入：word = "leet1234code234"
 * 输出：2
 * 示例 3：
 *
 * 输入：word = "a1b01c001"
 * 输出：1
 * 解释："1"、"01" 和 "001" 视为同一个整数的十进制表示，因为在比较十进制值时会忽略前导零的存在。
 *
 *
 * 提示：
 *
 * 1 <= word.length <= 1000
 * word 由数字和小写英文字母组成
 * 通过次数23,118提交次数52,490
 */
public class _1805_NumberOfDifferentIntegersInAString {


    /**
     * 模拟
     * 遇到字符就跳过
     * 遇到数字，一直遍历直到末尾或遇到下一个字符
     * 对于这部分数字串，跳过前导0（如果全为0，代表数字0），保留余下字符串部分，存入set
     * 返回set.size()
     * @param word
     * @return
     */
    public int numDifferentIntegers(String word) {
        int n = word.length();
        // 保存所有不同的数字子串部分
        Set<String> set = new HashSet<>();
        // 遍历
        for (int i = 0; i < n; ) {
            // 跳过字母
            if (Character.isLetter(word.charAt(i))) {
                i++;
                continue;
            }
            // 遇到数字，先跳过前导0部分
            while (i < n && word.charAt(i) == '0') {
                i++;
            }
            // 如果到了末尾，或者当前数字部分全是0（下一个字符又是字母），那就代表数字0
            if (i == n || Character.isLetter(word.charAt(i))) {
                set.add("0");
                continue;
            }
            // i指向非0数字字符，寻找数字部分的末尾j
            int j = i + 1;
            while (j < n && Character.isDigit(word.charAt(j))) {
                j++;
            }
            // 保存这部分数字子串到set
            set.add(word.substring(i, j));
            // 更新i到下一个位置
            i = j;
        }
        // 返回
        return set.size();
    }

    public static void main(String[] args) {
        _1805_NumberOfDifferentIntegersInAString obj = new _1805_NumberOfDifferentIntegersInAString();
        System.out.println(obj.numDifferentIntegers("a123bc34d8ef34"));
    }
}
