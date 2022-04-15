package com.daily;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * @author wangwei
 * 2022/4/15 14:55
 * <p>
 * 385. 迷你语法分析器
 * 给定一个字符串 s 表示一个整数嵌套列表，实现一个解析它的语法分析器并返回解析的结果 NestedInteger 。
 * <p>
 * 列表中的每个元素只可能是整数或整数嵌套列表
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * 输入：s = "324",
 * 输出：324
 * 解释：你应该返回一个 NestedInteger 对象，其中只包含整数值 324。
 * 示例 2：
 * <p>
 * 输入：s = "[123,[456,[789]]]",
 * 输出：[123,[456,[789]]]
 * 解释：返回一个 NestedInteger 对象包含一个有两个元素的嵌套列表：
 * 1. 一个 integer 包含值 123
 * 2. 一个包含两个元素的嵌套列表：
 * i.  一个 integer 包含值 456
 * ii. 一个包含一个元素的嵌套列表
 * a. 一个 integer 包含值 789
 * <p>
 * <p>
 * 提示：
 * <p>
 * 1 <= s.length <= 5 * 104
 * s 由数字、方括号 "[]"、负号 '-' 、逗号 ','组成
 * 用例保证 s 是可解析的 NestedInteger
 * 输入中的所有值的范围是 [-106, 106]
 */
public class _385_MiniParser {

/**
 * // This is the interface that allows for creating nested lists.
 * // You should not implement it, or speculate about its implementation
 * public interface NestedInteger {
 *     // Constructor initializes an empty nested list.
 *     public NestedInteger();
 *
 *     // Constructor initializes a single integer.
 *     public NestedInteger(int value);
 *
 *     // @return true if this NestedInteger holds a single integer, rather than a nested list.
 *     public boolean isInteger();
 *
 *     // @return the single integer that this NestedInteger holds, if it holds a single integer
 *     // Return null if this NestedInteger holds a nested list
 *     public Integer getInteger();
 *
 *     // Set this NestedInteger to hold a single integer.
 *     public void setInteger(int value);
 *
 *     // Set this NestedInteger to hold a nested list and adds a nested integer to it.
 *     public void add(NestedInteger ni);
 *
 *     // @return the nested list that this NestedInteger holds, if it holds a nested list
 *     // Return empty list if this NestedInteger holds a single integer
 *     public List<NestedInteger> getList();
 * }
 */

    class NestedInteger {
        public NestedInteger() {}
        public NestedInteger(int value) {}
        public boolean isInteger() {return false;}
        public Integer getInteger() {return null;}
        public void setInteger(int value) {}
        public void add(NestedInteger ni) {}
        public List<NestedInteger> getList() {return null;}
    }


    /**
     * 方法一：递归
     * 根据题意，一个 NestedInteger 实例只能包含下列两部分之一：1）一个整数；2）一个列表，
     * 列表中的每个元素都是一个 NestedInteger 实例。据此，NestedInteger 是通过递归定义的，
     * 因此也可以用递归的方式来解析。对应的，每次递归处理的 是一个数字串 或 一个 nested列表
     *
     * 从左至右遍历 s，
     *
     *  判断第一个 字符：
     * 如果第一位是 ‘[’ 字符，则表示待解析的是一个列表(起始)，从 ‘[’ 后面的字符开始又是一个新的 NestedInteger 实例，我们仍调用解析函数来解析列表的元素，
     *      调用结束后如果遇到的是 ‘,’ 字符，表示列表仍有其他元素(未遇到‘]’，就是当前列表未结束)，需要继续调用。
     *      如果是 ‘]’ 字符，表示这个列表已经解析完毕，可以返回 NestedInteger 实例。
     * 否则，则表示待解析的 NestedInteger 只包含一个整数。我们可以从左至右解析这个整数，并注意是否是负数，直到遍历完或者遇到非数字字符（‘]’ 或 ‘,’），并返回 NestedInteger 实例。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/mini-parser/solution/mi-ni-yu-fa-fen-xi-qi-by-leetcode-soluti-l2ma/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    // 当前处理到字符串哪个字符，因为多次递归处理的是同一个字符串的不同部分，因此需要传递一个参数表示当前递归从字符串哪个位置开始
    // 并且，不需要记录结束位置，上一次递归的结束，index随之被改变，当前递归起始位置自然也就知道。而每一次递归 要么处理完一个数字串，要么 处理完一部分列表(遇到']')，自然返回
    // 属性字段正好可以。
    private int index = 0;
    public NestedInteger deserialize(String s) {
        // 其实位置是一个 [，当次要处理的是一个 列表
        if (s.charAt(index) == '[') {
            // 准备要返回的对象
            NestedInteger ni = new NestedInteger();
            // 跳过 [
            index++;
            // 遇到 】 本次任务结束，中间部分我们不关心，交给递归，index会在每一次递归中自动前进
            while (s.charAt(index) != ']') {
                // 中间部分全是 我们 要处理的列表的 一部分
                ni.add(deserialize(s));
                // ‘,’说明这部分中间是多个部分组成，仍然交给递归处理，直到遇到 ']'
                if (s.charAt(index) == ',') {
                    // 我们需要跳过 ','
                    index++;
                }
            }
            // 注意这部分处理完了要更新index跳过 ']'，给后续准备
            // 返回此部分处理结果
            return ni;
        }
        // 否则是一个数字字符串，上面已经return，所以这里不需要else
        boolean flag = false;
        // 记录符号位
        if (s.charAt(index) == '-') {
            flag = true;
            index++;
        }
        // 符号后面是数字部分
        int num = 0;
        while (Character.isDigit(s.charAt(index))) {
            num = num * 10 + (s.charAt(index) - '0');
            index++;
        }
        if (flag) {
            num *= -1;
        }
        // 处理完后，封装成一个NestedInteger对象返回
        return new NestedInteger(num);
    }

    /**
     * 栈
     * 从刚才的递归写法来看，我们可以写出对应的 基于 栈 的写法
     * 从左至右遍历 s，如果遇到 ‘[’，则表示是一个新的 NestedInteger 实例，需要将其入栈。
     * 如果遇到 ‘]’ 或 ‘,’，则表示是一个数字或者 NestedInteger 实例的结束，需要将其添加入栈顶的 NestedInteger 实例。
     * 最后需返回栈顶的实例。
     *
     * 栈中实际上自始至终维护一个NestedInteger对象
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/mini-parser/solution/mi-ni-yu-fa-fen-xi-qi-by-leetcode-soluti-l2ma/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public NestedInteger deserialize2(String s) {
        // 第一个字符不是 [，那 代表 s 就是一个 数字串
        if (s.charAt(0) != '[') {
            return new NestedInteger(Integer.parseInt(s));
        }
        Deque<NestedInteger> stack = new ArrayDeque<>();
        // 保存数字串部分数值
        int num = 0;
        // 保存数字串部分符号
        boolean negetive = false;
        // 逐个遍历 s 字符
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // 数字串起始，记录符号
            if (c == '-') {
                negetive = true;
            // 数字串部分，累加 num
            } else if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            // 遇到 [ ，此部分是一个 列表，给栈中 保存一个 NestedInteger 对象，[,....] 中间部分结果 都属于这个对象
            } else if (c == '[') {
                stack.push(new NestedInteger());
            // 遇到 ]， ',' 当前部分结束
            } else if (c == ']' || c == ',') {
                // 如果是 数字串部分结束
                if (Character.isDigit(s.charAt(i - 1))) {
                    if (negetive) {
                        num *= -1;
                    }
                    // 那么把这部分给栈顶
                    stack.peek().add(new NestedInteger(num));
                }
                // 如果是一个列表结束，如果 栈中元素个数 > 1，说明 类似 [[ ，这种，我们现在处理完的是 中间的 [] 部分，此部分结束 属于外围的 []
                if (c == ']' && stack.size() > 1) {
                    // 所以 把此部分结果拿出来，给最外围部分
                    NestedInteger cur = stack.pop();
                    stack.peek().add(cur);
                }
                // 重置 符号 和 数值
                negetive = false;
                num = 0;
            }
        }
        // 最终栈中只有一个结果
        return stack.pop();
    }
}
