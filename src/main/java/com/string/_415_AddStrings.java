package com.string;

import java.util.Stack;

/**
 * @author wangwei
 * 2020/4/20 11:14
 * <p>
 * 给定两个字符串形式的非负整数 num1 和num2 ，计算它们的和。
 * <p>
 * 注意：
 * <p>
 * num1 和num2 的长度都小于 5100.
 * num1 和num2 都只包含数字 0-9.
 * num1 和num2 都不包含任何前导零。
 * 你不能使用任何內建 BigInteger 库，也不能直接将输入的字符串转换为整数形式。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-strings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _415_AddStrings {

    /**
     * 自己写的垃圾代码
     *
     * @param num1
     * @param num2
     * @return
     */
    public String addStrings(String num1, String num2) {
        if (num1.length() < num2.length())
            return addStrings(num2, num1);
        Stack<String> stack = new Stack<>();
        int i = num1.length() - 1, j = num2.length() - 1;
        int incr = 0, sum = 0;
        for (; j >= 0; i--, j--) {
            sum = num2.charAt(j) - '0' + num1.charAt(i) - '0';
            sum += incr;
            stack.push(String.valueOf(sum % 10));
            incr = sum / 10;
        }
        while (i >= 0) {
            sum = num1.charAt(i) - '0';
            sum += incr;
            stack.push(String.valueOf(sum % 10));
            incr = sum / 10;
            i--;
        }
        // 不要忘记进到最前面的那一位
        if (incr > 0) stack.push(String.valueOf(incr));
        StringBuilder builder = new StringBuilder();
        while (!stack.isEmpty())
            builder.append(stack.pop());
        return builder.toString();
    }

    /**
     * 题解的示例代码
     *
     * @param num1
     * @param num2
     * @return
     */
    public String addStrings2(String num1, String num2) {

        StringBuilder builder = new StringBuilder();

        int i = num1.length() - 1, j = num2.length() - 1;
        int incr = 0, sum = 0;

        while (i >= 0 || j >= 0){
            int n1 = i >= 0? num1.charAt(i) - '0' : 0;
            int n2 = j >= 0? num2.charAt(j) - '0' : 0;
            sum  = n1 + n2 + incr;
            builder.append(sum % 10);
            incr = sum / 10;
            i--; j--;
        }
        // 不要忘记进到最前面的那一位
        if (incr > 0) builder.append(incr);
        return builder.reverse().toString();
    }


    public static void main(String[] args) {
        _415_AddStrings obj = new _415_AddStrings();
        System.out.println(obj.addStrings2("123", "1234"));
    }
}
