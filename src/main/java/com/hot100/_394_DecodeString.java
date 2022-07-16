package com.hot100;

/**
 * @author wangwei
 * @date 2022/4/27 10:48
 * @description: _DecodeString
 *
 * 394. 字符串解码
 * 给定一个经过编码的字符串，返回它解码后的字符串。
 *
 * 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
 *
 * 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
 *
 * 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "3[a]2[bc]"
 * 输出："aaabcbc"
 * 示例 2：
 *
 * 输入：s = "3[a2[c]]"
 * 输出："accaccacc"
 * 示例 3：
 *
 * 输入：s = "2[abc]3[cd]ef"
 * 输出："abcabccdcdcdef"
 * 示例 4：
 *
 * 输入：s = "abc3[cd]xyz"
 * 输出："abccdcdcdxyz"
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 30
 * s 由小写英文字母、数字和方括号 '[]' 组成
 * s 保证是一个 有效 的输入。
 * s 中所有整数的取值范围为 [1, 300]
 */
public class _394_DecodeString {

    // 全局索引
    private int index;

    /**
     * 模拟：顺序遍历，遇到连续数字就计算数值，遇到 [ ，就递归调用去计算[]这部分结果，重复num次，再继续遍历
     * @param s
     * @return
     */
    public String decodeString(String s) {
        // 本次要返回的结果
        StringBuilder builder = new StringBuilder();
        int num = 0;
        while (index < s.length()) {
            // 判断当前字符
            char c = s.charAt(index);
            // 数字，记录，前进
            if (Character.isDigit(c)) {
                num = num * 10 + c - '0';
                index++;
            // 左 [
            } else if (c == '[') {
                // 索引前进
                index++;
                // 递归去处理 这个 [ 内部，得到 [] 这部分结果
                String sub = decodeString(s);
                // 这部分结果重复 num 次
                while (num-- > 0) {
                    builder.append(sub);
                }
                // num 清0
                num = 0;
            // 普通字符，直接保留，前进
            } else if (Character.isLetter(c)) {
                builder.append(c);
                index++;
            // ‘]’， 当前 [] 部分处理完毕，就要返回
            } else {
                index++;
                break;
            }
        }
        // 返回当前部分处理结果
        return builder.toString();
    }
}
