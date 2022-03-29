package com.string;

/**
 * @author wangwei
 * 2022/3/25 10:50
 *
 * 921. 使括号有效的最少添加
 * 只有满足下面几点之一，括号字符串才是有效的：
 *
 * 它是一个空字符串，或者
 * 它可以被写成 AB （A 与 B 连接）, 其中 A 和 B 都是有效字符串，或者
 * 它可以被写作 (A)，其中 A 是有效字符串。
 * 给定一个括号字符串 s ，移动N次，你就可以在字符串的任何位置插入一个括号。
 *
 * 例如，如果 s = "()))" ，你可以插入一个开始括号为 "(()))" 或结束括号为 "())))" 。
 * 返回 为使结果字符串 s 有效而必须添加的最少括号数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "())"
 * 输出：1
 * 示例 2：
 *
 * 输入：s = "((("
 * 输出：3
 */
public class _921_MinimumAddToMakeParenthesesValid {

    /**
     * 错误写法 直接统计左右括号数目，计算差值
     * 对于这种  "()))(("， 会返回 0
     * 其实这种错误很明显，比如 )))((( 明显不匹配
     * @param s
     * @return
     */
    public int minAddToMakeValid(String s) {
        int len = s.length();
        int left = 0, right = 0;
        for (int i = 0; i < len; ++i) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }
        }
        return Math.abs(right - left);
    }

    /**
     * 与判断括号合法性类似，按顺序遍历s每个字符，
     *      如果是 ( ，那么它肯定需要一个右括号，先记着，
     *      如果是 )，那么右括号的需求数 - 1，
     *      但是遇到右括号要判断 右括号多于左括号的情况，此时需要补充 (
     * @param s
     * @return
     */
    public int minAddToMakeValid2(String s) {
        int len = s.length();
        // left 表示需要添加的左括号数目，right 表示 需要添加的 右括号数目
        int left = 0, right = 0;
        for (int i = 0; i < len; ++i) {
            // 遇到左括号，右括号的需求+1
            if (s.charAt(i) == '(') {
                right++;
            // 遇到右括号，有括号的需求数-1
            } else {
                right--;
                // 但如果 此时 right < 0，也就是这种 (()))，此时需要补充左括号
                if (right == -1) {
                    left++;
                    // 补充完 左括号 后，此部分就匹配了，那么把 所需的 right 归零
                    right = 0;
                }
            }
        }
        // 返回所需的左右括号总数
        return left + right;
    }
}
