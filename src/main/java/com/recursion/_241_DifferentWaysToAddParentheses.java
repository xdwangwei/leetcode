package com.recursion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author wangwei
 * 2022/3/18 18:35
 *
 * 241. 为运算表达式设计优先级
 * 给你一个由数字和运算符组成的字符串 expression ，按不同优先级组合数字和运算符，计算并返回所有可能组合的结果。你可以 按任意顺序 返回答案。
 *
 *
 *
 * 示例 1：
 *
 * 输入：expression = "2-1-1"
 * 输出：[0,2]
 * 解释：
 * ((2-1)-1) = 0
 * (2-(1-1)) = 2
 * 示例 2：
 *
 * 输入：expression = "2*3-4*5"
 * 输出：[-34,-14,-10,-10,10]
 * 解释：
 * (2*(3-(4*5))) = -34
 * ((2*3)-(4*5)) = -14
 * ((2*(3-4))*5) = -10
 * (2*((3-4)*5)) = -10
 * (((2*3)-4)*5) = 10
 *
 *
 * 提示：
 *
 * 1 <= expression.length <= 20
 * expression 由数字和算符 '+'、'-' 和 '*' 组成。
 * 输入表达式中的所有整数值在范围 [0, 99]
 */
public class _241_DifferentWaysToAddParentheses {

    private HashMap<String, List<Integer>> memo;

    /**
     * 分治思想
     * 对于任意一个表达式，以任意一个运算符分割，都会是 expr = expLeft op expRight 这种形式
     * expr有多少种结果，取决于 expLeft 和 expRight 分别有多少种结果，分而治之
     *
     * 并且可以选择任意一个运算符分割
     *
     * 为避免重复计算，比如 (1 + 1) + (1 + 1 + 1) 和 (1 + 1 + 1) + (1 + 1) 这种，增加备忘录
     * @param expression
     * @return
     */
    public List<Integer> diffWaysToCompute(String expression) {
        memo = new HashMap<>();
        return helper(expression);
    }

    /**
     * 注意 base case，如果一个表示式无法拆分(不包含运算符)，那就是单个数字，也就是只可能有1种结果
     * @param exp
     * @return
     */
    private List<Integer> helper(String exp) {
        // 此表达式的各种结果已计算过
        if (memo.containsKey(exp)) {
            return memo.get(exp);
        }
        // 保存返回值
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            // 遇到操作符
            if (c == '+' || c == '-' || c == '*') {
                // 左半部分表示式的各种可能结果
                List<Integer> left = helper(exp.substring(0, i));
                // 右半部分表示式的各种可能结果
                List<Integer> right = helper(exp.substring(i + 1));
                // 组合结果
                for (Integer l : left) {
                    for (Integer r : right) {
                        // 根据运算符得到结果
                        if (c == '+') {
                            res.add(l + r);
                        } else if (c == '-') {
                            res.add(l - r);
                        } else {
                            res.add(l * r);
                        }
                    }
                }
            }
        }
        // 说明当前的exp就是个数字，不含运算符
        if (res.isEmpty()) {
            res.add(Integer.parseInt(exp));
        }
        // 记录
        memo.put(exp, res);
        // 返回
        return res;
    }

    public static void main(String[] args) {
        int[][] a = new int[3][3];
        Arrays.sort(a, (o1, o2) -> o1[1] == o2[1] ? o1[0] - o2[0] : 1);
    }
}
