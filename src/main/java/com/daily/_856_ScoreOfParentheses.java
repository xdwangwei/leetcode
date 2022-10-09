package com.daily;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/10/9 13:49
 * @description: _856_ScoreOfParentheses
 *
 * 856. 括号的分数
 * 给定一个平衡括号字符串 S，按下述规则计算该字符串的分数：
 *
 * () 得 1 分。
 * AB 得 A + B 分，其中 A 和 B 是平衡括号字符串。
 * (A) 得 2 * A 分，其中 A 是平衡括号字符串。
 *
 *
 * 示例 1：
 *
 * 输入： "()"
 * 输出： 1
 * 示例 2：
 *
 * 输入： "(())"
 * 输出： 2
 * 示例 3：
 *
 * 输入： "()()"
 * 输出： 2
 * 示例 4：
 *
 * 输入： "(()(()))"
 * 输出： 6
 */
public class _856_ScoreOfParentheses {

    /**
     *
     * 初始化将答案 0 放入栈中，从前往后处理整个 s，
     * 当遇到 ( 则存入一个占位数值 0，代表以当前(开始的部分值为0，以及当前是个(
     * 遇到 ) 取出栈顶元素 cur，根据栈顶元素数值值分情况讨论：
     *      栈顶元素 cur=0，即当前的 ) 的前一元素即是 ( ，根据 () 得一分的规则可知，我们本次操作得到的分值为 1；
     *      栈顶元素 cur!=0，即当前 ) 与其匹配的 ( 中间相隔了子串A，根据 (A) 的得分规则，此时可知得分为2 * score(A) = cur×2；
     * 将两者结合可统一为 当前(xxxx) 部分的得分为 max(cur×2,1)。
     *
     * 相当于每次遇到 ) 时，都能将最近一次操作(xxx)计算出来。
     * 可以归结为 X + () 的相邻项累加规则，因此将当前新得分累加到栈顶元素上，
     * 这样，最终栈顶就是整个串的得分，并且，初始时，认为 s 的左边为 ""，给栈压入 0
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/score-of-parentheses/solution/by-ac_oier-0mhz/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/score-of-parentheses/solution/gua-hao-de-fen-shu-by-leetcode-solution-we6b/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public int scoreOfParentheses(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        // 归结为 "" + (，初始 压入 0 ，代表当前括号串左边部分得分为0
        stack.push(0);
        for (char c : s.toCharArray()) {
            // 左 ( 代表一部分括号串 的起始及得分
            if (c == '(') {
                stack.push(0);
            } else {
                // 遇到 ), 需要 考虑是 嵌套的 )，还是 并列的 )
                int cur = stack.pop();
                if (cur == 0) {
                    // 若栈顶元素 cur=0，即当前的 ) 的前一元素即是 ( ，根据 () 得一分的规则可知，我们本次操作得到的分值为 1；
                    stack.push(stack.pop() + 1);
                } else {
                    // 若栈顶元素 cur!=0，即当前 ) 与其匹配的 ( 中间相隔了子串A，根据 (A) 的得分规则，此时可知得分为2 * score(A) = cur×2；
                    stack.push(stack.pop() + 2 * cur);
                }
            }
        }
        // 最终栈顶为 整个串的得分
        return stack.pop();
    }

    /**
     * 方法二：计算每个 () 的深度，并累加
     *
     * 我们通过观察发现，由于 (A) 得分为 2 * score(A)
     * 所以对于 ((()))形式，得分为 2 * (()) = 2 * 2 * () = 2 ^ 最内层() 的深度
     *
     * 对于最内层 () 深度的计算
     * 我们用 depth 维护当前括号的深度，对于每个 (，我们将深度加一，对于每个 )，我们将深度减一。
     * 当我们遇到 () 时，我们将 2^depth 累加到答案中。
     *
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/score-of-parentheses/solution/by-lcbin-b9st/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public int scoreOfParentheses2(String s) {
        int n = s.length(), depth = 0, ans = 0;
        for (int i = 0; i < n; ++i) {
            // depth统计每一个最内部() 的深度
            if (s.charAt(i) == '(') {
                depth++;
            } else {
                depth--;
            }
            // 最内部()的特点是 当前是 ),上一个是(,并且此时depth恰好为深度
            if (s.charAt(i) == ')' && i >= 1 &&  s.charAt(i - 1) == '(') {
                ans += (1 << depth);
            }
        }
        return ans;
    }
}
