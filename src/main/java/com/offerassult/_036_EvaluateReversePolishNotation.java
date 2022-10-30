package com.offerassult;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/10/30 13:29
 * @description: _036_EvaluateReversePolishNotation
 *
 * 剑指 Offer II 036. 后缀表达式
 * 根据 逆波兰表示法，求该后缀表达式的计算结果。
 *
 * 有效的算符包括 +、-、*、/ 。每个运算对象可以是整数，也可以是另一个逆波兰表达式。
 *
 *
 *
 * 说明：
 *
 * 整数除法只保留整数部分。
 * 给定逆波兰表达式总是有效的。换句话说，表达式总会得出有效数值且不存在除数为 0 的情况。
 *
 *
 * 示例 1：
 *
 * 输入：tokens = ["2","1","+","3","*"]
 * 输出：9
 * 解释：该算式转化为常见的中缀算术表达式为：((2 + 1) * 3) = 9
 * 示例 2：
 *
 * 输入：tokens = ["4","13","5","/","+"]
 * 输出：6
 * 解释：该算式转化为常见的中缀算术表达式为：(4 + (13 / 5)) = 6
 * 示例 3：
 *
 * 输入：tokens = ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]
 * 输出：22
 * 解释：
 * 该算式转化为常见的中缀算术表达式为：
 *   ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
 * = ((10 * (6 / (12 * -11))) + 17) + 5
 * = ((10 * (6 / -132)) + 17) + 5
 * = ((10 * 0) + 17) + 5
 * = (0 + 17) + 5
 * = 17 + 5
 * = 22
 *
 *
 * 提示：
 *
 * 1 <= tokens.length <= 104
 * tokens[i] 要么是一个算符（"+"、"-"、"*" 或 "/"），要么是一个在范围 [-200, 200] 内的整数
 *
 *
 * 逆波兰表达式：
 *
 * 逆波兰表达式是一种后缀表达式，所谓后缀就是指算符写在后面。
 *
 * 平常使用的算式则是一种中缀表达式，如 ( 1 + 2 ) * ( 3 + 4 ) 。
 * 该算式的逆波兰表达式写法为 ( ( 1 2 + ) ( 3 4 + ) * ) 。
 * 逆波兰表达式主要有以下两个优点：
 *
 * 去掉括号后表达式无歧义，上式即便写成 1 2 + 3 4 + * 也可以依据次序计算出正确结果。
 * 适合用栈操作运算：遇到数字则入栈；遇到算符则取出栈顶两个数字进行计算，并将结果压入栈中。
 *
 *
 * 注意：本题与主站 150 题相同： https://leetcode-cn.com/problems/evaluate-reverse-polish-notation/
 */
public class _036_EvaluateReversePolishNotation {


    /**
     * 逆波兰表达式由波兰的逻辑学家卢卡西维兹提出。
     * 逆波兰表达式的特点是：没有括号，运算符总是放在和它相关的操作数之后。因此，逆波兰表达式也称后缀表达式。
     *
     * 方法一：栈
     * 逆波兰表达式严格遵循「从左到右」的运算。
     * 计算逆波兰表达式的值时，使用一个栈存储操作数，从左到右遍历逆波兰表达式，进行如下操作：
     *
     *      如果遇到操作数，则将操作数入栈；
     *
     *      如果遇到运算符，则将两个操作数出栈，其中先出栈的是右操作数，后出栈的是左操作数，
     *      使用运算符对两个操作数进行运算，将运算得到的新操作数入栈。
     *
     * 整个逆波兰表达式遍历完毕之后，栈内只有一个元素，该元素即为逆波兰表达式的值。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/evaluate-reverse-polish-notation/solution/ni-bo-lan-biao-da-shi-qiu-zhi-by-leetcod-wue9/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param tokens
     * @return
     */
    public int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : tokens) {
            // 如果遇到运算符，则将两个操作数出栈，其中先出栈的是右操作数，后出栈的是左操作数，
            // 因为可能有负数，所以这里先判断长度为1，再判断不是数字，才能确定是运算符
            if (token.length() == 1 && !Character.isDigit(token.charAt(0))) {
                int res = 0;
                int op2 = stack.pop();
                int op1 = stack.pop();
                switch (token) {
                    case "+":
                        res = op1 + op2;
                        break;
                    case "-":
                        res = op1 - op2;
                        break;
                    case "*":
                        res = op1 * op2;
                        break;
                    case "/":
                        res = op1 / op2;
                        break;
                }
                // 使用运算符对两个操作数进行运算，将运算得到的新操作数入栈。
                stack.push(res);
            } else {
                // 如果遇到操作数，则将操作数入栈；
                stack.push(Integer.parseInt(token));
            }
        }
        // 整个逆波兰表达式遍历完毕之后，栈内只有一个元素，该元素即为逆波兰表达式的值
        return stack.pop();
    }
}
