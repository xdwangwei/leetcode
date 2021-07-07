package com.order;

import java.util.Stack;

/**
 * @author wangwei
 * 2020/8/3 9:56
 *
 * 实现一个基本的计算器来计算一个简单的字符串表达式的值。
 *
 * 字符串表达式仅包含非负整数，+， - ，*，/ 四种运算符和空格  。 整数除法仅保留整数部分。
 *
 * 示例 1:
 *
 * 输入: "3+2*2"
 * 输出: 7
 * 示例 2:
 *
 * 输入: " 3/2 "
 * 输出: 1
 * 示例 3:
 *
 * 输入: " 3+5 / 2 "
 * 输出: 5
 * 说明：
 *
 * 你可以假设所给定的表达式都是有效的。
 * 请不要使用内置的库函数 eval。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/basic-calculator-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _227_BasicCaculator2 {

    /**
     * 支持加减的计算器
     *
     * 对于一个字符串运算表达式 1-123+3
     *
     * 1、先给第一个数字加一个默认符号+，变成+1-12+3。
     *
     * 2、把一个运算符和数字组合成一对儿，也就是三对儿+1，-12，+3，把它们转化成数字，然后放到一个栈中。
     *  -- 遇到第一个 + 号，把 0 放入栈
     *  -- 遇到第一个 - 号，把 +1 放入栈
     *  -- 遇到第二个 + 号，把 -123 放入栈
     *  -- 遇到最后一个字符3， 把+3放入栈
     *
     *  每次遇到一个运算符号，标志着前面一个操作数的结束，
     *  需要注意的是，如果当前是最后一个字符，也标志着最后一个操作数的结束
     *
     *  对于每一次操作数（连续几个数字字符）的保存，就==字符串转数字
     *    num = num * 10 + (c - '0')
     *    num = 0
     *    遇到 1 ， num = 1
     *    遇到 2， num = 1 * 10 + 2 = 12
     *    遇到 3， num = 12 * 10 + 3 = 123
     *    遇到 +，上一个操作数结束，【num入栈，num清零，当前+号是下一个操作数的符号】
     *
     * 3、将栈中所有的数字求和，就是原算式的结果。
     *
     * 作者：labuladong
     * 链接：https://leetcode-cn.com/problems/basic-calculator-ii/solution/chai-jie-fu-za-wen-ti-shi-xian-yi-ge-wan-zheng-ji-/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public int basicCalculator(String s) {
        // 存放添加了符号的所有操作数，最后累加得到返回值
        Stack<Integer> stack = new Stack<>();
        // res保存返回值，num保存字符串每段数字串代表的值
        int res = 0, num = 0;
        // 给第一个操作数添加默认符号+
        char op = '+';
        // 从第一个字符开始遍历
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            // 如果当前字符是数字
            if (Character.isDigit(c)) {
                // 继续计算当前操作数
                num = num * 10 + (c - '0');
            }
            // 如果是+-，或者是最后一个字符，都代表着上一个操作数的结束
            // 注意这里不能用 else if, 就是为了处理遇到最后一个字符，一般是操作数
            // 比如 1+3-4，遇到4
            // 如果加了else，上面的if肯定成立，这里就不会执行了，
            // 而实际上4代表着最后一个操作数-4的结束，也需要入栈
            if ((!Character.isDigit(c) && c != ' ') || i == s.length() - 1) {
                // op保存了当前操作数的符号
                switch (op) {
                    // 结合符号，将其入栈
                    case '+' :
                        stack.push(num); break;
                    case '-' :
                        stack.push(-num); break;
                }
                // 此时的c就是下一个操作数的符号
                op = c;
                // num清零
                num = 0;
            }
        }
        // 对栈中的全部带符号数进行求和
        while (!stack.isEmpty()) {
            res += stack.pop();
        }
        // 返回结果
        return res;
    }

    /**
     * 支持加减乘除的计算器
     * <p>
     * 思路跟仅处理加减法没啥区别，核心思路依然是把字符串分解成符号和数字的组合。
     * <p>
     * 其他部分都不用变，在switch部分加上对应的 case 就行了：
     * <p>
     * 乘除法优先于加减法，体现在，乘除法可以和栈顶的数结合，而加减法只能把自己放入栈。
     * 所以遇到乘除符号，先把栈顶元素弹出来，和当前操作数做运算，再把结果圧栈
     *
     * @param s
     * @return
     */
    public int basicCalculatorPlus(String s) {
        // 存放添加了符号的所有操作数，最后累加得到返回值
        Stack<Integer> stack = new Stack<>();
        // res保存返回值，num保存字符串每段数字串代表的值
        int res = 0, num = 0;
        // 给第一个操作数添加默认符号+
        char op = '+';
        // 从第一个字符开始遍历
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            // 如果当前字符是数字
            if (Character.isDigit(c)) {
                // 继续计算当前操作数
                num = num * 10 + (c - '0');
            }
            // 如果是+-，或者是最后一个字符，都代表着上一个操作数的结束
            // 注意这里不能用 else if, 就是为了处理遇到最后一个字符，一般是操作数
            // 比如 1+3-4，遇到4
            // 如果加了else，上面的if肯定成立，这里就不会执行了，
            // 而实际上4代表着最后一个操作数-4的结束，也需要入栈
            if ((!Character.isDigit(c) && c != ' ') || i == s.length() - 1) {
                // op保存了当前操作数的符号
                switch (op) {
                    // 结合符号，将其入栈
                    case '+' :
                        stack.push(num); break;
                    case '-' :
                        stack.push(-num); break;
                    // 遇到乘除符号，先把栈顶元素弹出来，和当前操作数做运算，再把结果圧栈
                    case '*' :
                        stack.push(stack.pop() * num); break;
                    case '/' :
                        stack.push(stack.pop() / num); break;
                }
                // 此时的c就是下一个操作数的符号
                op = c;
                // num清零
                num = 0;
            }
        }
        // 对栈中的全部带符号数进行求和
        while (!stack.isEmpty()) {
            res += stack.pop();
        }
        // 返回结果
        return res;
    }
}
