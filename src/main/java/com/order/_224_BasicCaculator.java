package com.order;

import java.util.Stack;

/**
 * @author wangwei
 * 2020/8/2 21:59
 *
 * 实现一个基本的计算器来计算一个简单的字符串表达式的值。
 *
 * 字符串表达式可以包含左括号(，右括号 )，加号+，减号-，非负整数和空格。
 *
 * 示例 1:
 *
 * 输入: "1 + 1"
 * 输出: 2
 * 示例 2:
 *
 * 输入: " 2-1 + 2 "
 * 输出: 3
 * 示例 3:
 *
 * 输入: "(1+(4+5+2)-3)+(6+8)"
 * 输出: 23
 * 说明：
 *
 * 你可以假设所给定的表达式都是有效的。
 * 请不要使用内置的库函数 eval。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/basic-calculator
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _224_BasicCaculator {

    public int calculate(String s) {
        int[] index = new int[]{1};
        return advancedCalculator(s, index);
    }

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
     *
     * 思路跟仅处理加减法没啥区别，核心思路依然是把字符串分解成符号和数字的组合。
     *
     * 其他部分都不用变，在switch部分加上对应的 case 就行了：
     *
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

    /**
     * 支持加减乘除以及括号的计算器
     *
     * 为什么说处理括号没有看起来那么难呢，因为括号具有递归性质。
     * 我们拿字符串3*(4-5/2)-6举例：
     *
     * calculate 3*(4-5/2)-6
     * = 3 * calculate 4-5/2  - 6
     * = 3 * 2 - 6
     * = 0
     *
     * 可以脑补一下，无论多少层括号嵌套，通过 calculate 函数递归调用自己，都可以将括号中的算式化简成一个数字。
     * 换句话说，括号包含的算式，我们直接视为一个数字就行了。
     *
     * 现在的问题是，递归的开始条件和结束条件是什么？
     *      遇到(开始递归，遇到)结束递归：
     *
     * 需要补充的就是，遇到 ( 开始递归，遇到 ) 结束递归并返回，
     * 【!!!重点!!!】返回上一层之后我们应该从 ) 之后的字符开始，继续往后处理，
     * 所以这里应该设置一个参数作为起始位置
     *      可以是引用传值的方式，
     *      也可以计算递归处理的这段子串的长度，然后递归返回后下标后移指定长度，继续处理
     *
     *  因为java对于基本类型 int 没有引用传值，所以这里用一个只包含一个元素的一维数组作为参数，
     *  保存每次处理的起始位置
     *
     * 作者：labuladong
     * 链接：https://leetcode-cn.com/problems/basic-calculator-ii/solution/chai-jie-fu-za-wen-ti-shi-xian-yi-ge-wan-zheng-ji-/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     *
     * @param s
     * @return
     */
    /**
     *
     * 不能for循环一进来就 if (c == ' ') continue; 这样写，如果表达式是这这种形式 "5 ",那么for循环直接结束了，那个5没处理
     * 所以注意下面的判断条件，这样也可以跳过空格，并且能正确处理5
     *      if ((!Character.isDigit(c) && c != ' ') || i == s.length() - 1) {
     */
    public int advancedCalculator(String s, int[] arr) {
        // 存放添加了符号的所有操作数，最后累加得到返回值
        Stack<Integer> stack = new Stack<>();
        // res保存返回值，num保存字符串每段数字串代表的值
        int res = 0, num = 0;
        // 给第一个操作数添加默认符号+
        char op = '+';
        // 从指定位置字符开始处理
        for (; arr[0] < s.length(); ++arr[0]) {
            char c = s.charAt(arr[0]);
            // 如果当前字符是数字
            if (Character.isDigit(c)) {
                // 继续计算当前操作数
                num = num * 10 + (c - '0');
            }
            // 遇到 ( 开始递归，递归计算 () 这部分表达式的值
            if (c == '(') {
                // 从 ( 的下一个位置开始
                ++arr[0];
                num = advancedCalculator(s, arr);
                // 这里不能直接 stack.push(num);
                // 必须根据括号部分表达式前面的符号决定最终入栈什么数字
                // 比如 -(expr) 就应该入栈 -num
                // 所以计算括号就跟计算连续数字是一个道理，只得到num的值，知道遇到下一个符号，表示数字部分结束，结合开始的符号决定入栈的值
            }
            // 如果是+-，或者是最后一个字符，都代表着上一个操作数的结束
            // 注意这里不能用 else if, 就是为了处理遇到最后一个字符，一般是操作数
            // 比如 1+3-4，遇到4
            // 如果加了else，上面的if肯定成立，这里就不会执行了，
            // 而实际上4代表着最后一个操作数-4的结束，也需要入栈
            if ((!Character.isDigit(c) && c != ' ') || arr[0] == s.length() - 1) {
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
            // 遇到 ) 结束递归， () 这部分表达式的值已计算完毕，结束本次递归，返回此部分的值
            if (c == ')') {
                break;
            }
        }
        while (!stack.isEmpty()) {
            res += stack.pop();
        }
        return res;
    }


    public static void main(String[] args) {
        _224_BasicCaculator obj = new _224_BasicCaculator();
        System.out.println(obj.advancedCalculator("-1+(2/3-5)*6", new int[]{0}));
    }
}
