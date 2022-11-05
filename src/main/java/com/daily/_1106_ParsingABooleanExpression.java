package com.daily;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/11/5 12:10
 * @description: _1106_ParsingABooleanExpression
 *
 * 1106. 解析布尔表达式
 * 给你一个以字符串形式表述的 布尔表达式（boolean） expression，返回该式的运算结果。
 *
 * 有效的表达式需遵循以下约定：
 *
 * "t"，运算结果为 True
 * "f"，运算结果为 False
 * "!(expr)"，运算过程为对内部表达式 expr 进行逻辑 非的运算（NOT）
 * "&(expr1,expr2,...)"，运算过程为对 2 个或以上内部表达式 expr1, expr2, ... 进行逻辑 与的运算（AND）
 * "|(expr1,expr2,...)"，运算过程为对 2 个或以上内部表达式 expr1, expr2, ... 进行逻辑 或的运算（OR）
 *
 *
 * 示例 1：
 *
 * 输入：expression = "!(f)"
 * 输出：true
 * 示例 2：
 *
 * 输入：expression = "|(f,t)"
 * 输出：true
 * 示例 3：
 *
 * 输入：expression = "&(t,f)"
 * 输出：false
 * 示例 4：
 *
 * 输入：expression = "|(&(t,f,t),!(t))"
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= expression.length <= 20000
 * expression[i] 由 {'(', ')', '&', '|', '!', 't', 'f', ','} 中的字符组成。
 * expression 是以上述形式给出的有效表达式，表示一个布尔值。
 * 通过次数11,843提交次数17,986
 */
public class _1106_ParsingABooleanExpression {


    /**
     *
     * 方法一：栈
     * 给定的字符串 expression 是有效的布尔表达式，每个运算符后面都有一对括号，括号中有一个或多个表达式。
     * 其中，逻辑非运算符后面的括号中有一个表达式，逻辑与运算符和逻辑或运算符后面的括号中有两个或以上表达式。
     *
     * 可以使用栈实现布尔表达式的解析。从左到右遍历布尔表达式，对于每种类型的字符，执行相应的操作：
     *
     * 如果当前字符是逗号，则跳过该字符；
     *
     * 如果当前字符是除了逗号和右括号以外的任意字符，则将该字符添加到栈内；
     *
     * 如果当前字符是右括号，则一个表达式遍历结束，需要解析该表达式的值，并将结果添加到栈内：
     *
     *          将栈内字符依次弹出，直到栈顶字符是左括号，然后将左括号和运算符从栈内弹出，记录弹出的 ‘t’ 和 ‘f’ 的个数；
     *
     *         根据运算符以及 ‘t’ 和 ‘f’ 的个数计算表达式的值，并将表达式的值添加到栈内：
     *
     *         此时新的栈顶元素就是这些t和f的运算符
     *
     *         如果运算符是 ‘!’，则是逻辑非运算符，表达式的值为括号内的值取反，因此当 ‘f’ 的个数等于 1 时表达式的值为 ‘t’，否则表达式的值为 ‘f’；
     *
     *         如果运算符是 ‘&’，则是逻辑与运算符，当括号内的所有值都是 ‘t’ 时结果是 ‘t’，否则结果是 ‘f’，因此当 ‘f’ 的个数等于 0 时表达式的值为 ‘t’，否则表达式的值为 ‘f’；
     *
     *         如果运算符是 ‘|’，则是逻辑或运算符，当括号内至少有一个值都是 ‘t’ 时结果是 ‘t’，否则结果是 ‘f’，因此当 ‘t’ 的个数大于 0 时表达式的值为 ‘t’，否则表达式的值为 ‘f’；
     *
     *         将当前运算结果压栈
     *
     * 遍历结束之后，栈内只有一个字符，该字符为 ‘t’ 或 ‘f’，如果字符为 ‘t’ 则返回 true，如果字符为 f’ 则返回 false。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/parsing-a-boolean-expression/solution/jie-xi-bu-er-biao-da-shi-by-leetcode-sol-vmvg/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param expression
     * @return
     */
    public boolean parseBoolExpr(String expression) {
        Deque<Character> stack = new ArrayDeque<>();
        // 在最前面加个或运算，即可避免空栈特殊处理，又不会影响结果
        stack.push('|');
        int n = expression.length();
        // 逐个遍历
        for (int i = 0; i < n; i++) {
            char c = expression.charAt(i);
            // 跳过逗号
            if (c == ',') {
                continue;
            }
            // 除右括号外，其他字符全部入栈
            if (c != ')') {
                stack.push(c);
                continue;
            }
            // 遇到右括号
            // 逐个出栈，直到遇到左括号
            // 记录当前部分内t和f的个数
            int t = 0, f = 0;
            while (stack.peek() != '(') {
                char val = stack.pop();
                if (val == 't') {
                    t++;
                } else {
                    f++;
                }
            }
            // 左括号出栈
            stack.pop();
            // 此时的栈顶就是当前()这部分的运算符
            // 运算符出栈，计算结果
            char op = stack.pop();
            switch (op) {
                // 取 反，那么 括号部分只会有1个t或1个f，如果有1个f，那么取反结果是 t
                case '!':
                    stack.push(f == 1 ? 't' : 'f');
                    break;
                    // 与 运算，除非 没有 f，否则 结果就是 f
                case '&':
                    stack.push(f == 0 ? 't' : 'f');
                    break;
                    // 或 运算，除非没有 t，否则 结果 就是 t
                case '|':
                    stack.push(t > 0 ? 't' : 'f');
                    break;
                default:
            }
        }
        // 最后栈顶就是最终结果，判断是否为t
        return stack.pop() == 't';
    }

    public static void main(String[] args) {
        _1106_ParsingABooleanExpression obj = new _1106_ParsingABooleanExpression();
        System.out.println(obj.parseBoolExpr("!(&(!(&(f)),&(t),|(f,f,t)))"));
    }
}
