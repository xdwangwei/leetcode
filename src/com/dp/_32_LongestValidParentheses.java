package com.dp;


import java.util.Stack;

/**
 * @author wangwei
 * 2020/4/2 23:11
 * <p>
 * 给定一个只包含 '(' 和 ')' 的字符串，找出最长的包含有效括号的子串的长度。
 * <p>
 * 示例 1:
 * <p>
 * 输入: "(()"
 * 输出: 2
 * 解释: 最长有效括号子串为 "()"
 * 示例 2:
 * <p>
 * 输入: ")()())"
 * 输出: 4
 * 解释: 最长有效括号子串为 "()()"
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-valid-parentheses
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _32_LongestValidParentheses {

    /**
     * 方法一：暴力法，取出s所有长度为偶数的子串去判断是否有效，并变更最大有效长度
     * 会超时
     *
     * @param s
     * @return
     */
    public int solution1(String s) {
        if (s == null || s.length() < 2) return 0;
        int res = 0;
        for (int i = 0; i < s.length() - 1; ++i) {
            // 0-2,0-4,0-6, 1-3,1-5,1-7, 2-4,2-6
            for (int j = i + 2; j <= s.length(); j += 2) {
                if (isValid(s.substring(i, j))) {
                    res = Math.max(res, j - i);
                }
            }
        }
        return res;
    }

    /**
     * 判断当前字符串是否是有效的括号序列
     * 长度至少为2
     *
     * @param s
     * @return
     */
    private boolean isValid(String s) {
        // 以 ) 开始，绝对无效
        if (s == null || s.length() < 2 || s.charAt(0) == ')') return false;
        // int count = 0;
        // for (int i = 0; i < s.length(); i++) {
        //     // 遇到 ( ,count加1
        //     if (s.charAt(i) == '(') ++count;
        //         // 遇到 ) ,count减1
        //     else --count;
        // }
        // // ()匹配，count一定为0,无法识别 ())(
        // return count == 0;
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            // 遇到 ( ,入栈
            if (s.charAt(i) == '(') stack.push("(");
                // 遇到一个 )，弹出一个 (
            else if (s.charAt(i) == ')' && !stack.isEmpty()) stack.pop();
                // 遇到其他字符
            else return false;
        }
        // ( )是否成对出现
        return stack.isEmpty();
    }

    /**
     * 方法二：两次扫描
     * 从左往右：一定以(开始，以)结束，在此过程中记录出现的(和)的数目left,right，
     * 每次left == right时，都是配对的()，此时right * 2就是有效串长度
     * 一旦 right > left，[从开头到此位置的串一定无效]，而此串中的有效部分已经记录过
     * 所以重新赋值left=0,right=0开始统计之后部分
     * 从右往左：一定以 ) 开始，以 ( 结束，在此过程中记录出现的(和)的数目left,right，
     * 每次left == right时，都是配对的()，此时left * 2就是有效串长度
     * 一旦 left > right，从末尾到此位置的串一定无效，而此串中的有效部分已经记录过
     * 所以重新赋值left=0,right=0开始统计之前部分
     *
     * @param s
     * @return
     */
    public int solution2(String s) {
        if (s == null || s.length() < 2) return 0;

        int res = 0, left = 0, right = 0;

        for (int i = 0; i < s.length() - 1; ++i) {
            if (s.charAt(i) == '(') ++left;
            else ++right;
            if (left == right)
                res = Math.max(res, 2 * right);
            else if (left < right) {
                left = right = 0;
            }
        }
        left = right = 0;
        for (int i = s.length() - 1; i >= 0; --i) {
            if (s.charAt(i) == ')') ++right;
            else ++left;
            if (left == right)
                res = Math.max(res, 2 * left);
            else if (left > right) {
                left = right = 0;
            }
        }
        return res;
    }

    /**
     * 栈
     * 我们用一个栈来存储坐标。为了方便计算，在最开始的时候，我们将栈里面放入一个-1.
     * 当遇到的是'('的时候，我们将其坐标入栈，
     * 当遇到的是'）'的时候，弹出栈顶元素。此时需要分两种情况。
     *      此时如果栈空了，其实相当于前面已经正好匹配了，然后再进来了一个'）',此时无需更新最大值max，
     *              只需将当期坐标入栈。其作用和上面栈初始化的时候放入一个-1相同。
     *      如果此时栈非空，说明又多了一对匹配。需要更新max的值。
     * 以)()())为例。如下所示。
     * s.charAt(i)      stack
     * [-1]
     * )                弹出栈顶元素，栈空，当前下标入栈[0]
     * )(               1入栈，[0,1]
     * )()              栈顶出栈，栈不空，更新res, 2 - 0,[0]
     * )()(             3入栈，[0,3]
     * )()()            3出栈，栈不空，更新res ,4-0, [0]
     * )()())           0出栈，栈空[]，5入栈,[5]
     * <p>
     * 每次遇到 ( 就下标入栈， 遇到 )就弹出，其实栈中留着的下标相当于是一个标记，表示从开始到当前位置的有效长度已统计过
     * 如刚开始相当于从开始到-1位置的串已统计过，每次()()成对出现时，不用更新res，因为成对出现的有效串总会被弹完
     * 而每次弹完栈不空，说明上上一个位置(栈顶)到现在这个位置又是一个有效串(中间的都被弹并抵消完了)，此时更新res
     *
     * @param s
     */
    public int solution3(String s) {
        if (s == null || s.length() < 2) return 0;
        int res = 0;
        Stack<Integer> stack = new Stack<>();
        // 为了操作统一，每次遇到 ) 会直接出栈，再判断栈是否空
        // 一开始 从开端截止到-1位置的串有效
        stack.push(-1);
        for (int i = 0; i < s.length(); i++) {
            // 遇到 ( ,入栈
            if (s.charAt(i) == '(') stack.push(i);
            else {
                // 遇到一个 )，弹出一个 (,成对出现的()()总会被弹完
                stack.pop();
                // 栈空了，把当前下标压栈，相当于从开始截止到当前位置是一个有效串，标记统计过
                if (stack.isEmpty()) {
                    stack.push(i);
                } else {
                    // stack.peek()获取栈顶元素，但不移除
                    // 栈顶始终保持着之前那个有效串的末尾字符下表
                    // 栈不空说明从上一次截止到这个位置，出出进进了多个()()，这部分的有效长度就是当前下标-上次的末尾位置
                    res = Math.max(res, i - stack.peek());
                }
            }
        }
        return res;
    }

    /**
     * 利用栈，另一种写法，因为每一段最长有效串肯定是互相分开的，所以利用start表示当前最长有效段的起始位置
     * 遇到 （ ，下标入栈
     * 遇到 ），如果栈空(没有（)，无效段，从下一个位置重新统计，
     *      否则，弹出栈顶，如果此时栈空了，需要更新res，当前段的起始位置是start，所以有效长度是 i - start + 1
     *      如果栈不空(栈里还有左括号)，说明 目前只是当前最长有效段的一部分有效段，当前最长有效段的起始位置是栈底元素，而栈顶元素代表了目前这部分有效段的其实位置
     * @param s
     * @return
     */
    public int solution31(String s) {
        Stack<Integer> stack = new Stack<>();
        int ans = 0;
        for(int i = 0 ,start = 0;i < s.length();i ++) {
            // 遇到 （，下标入栈
            if( s.charAt(i) == '(') stack.push(i);
            // 遇到 ）
            else {
                // 栈里有 （
                if(!stack.isEmpty()) {
                    // 弹出
                    stack.pop();
                    // 栈空，当前是个有效段，更新res，其实位置是start
                    if(stack.isEmpty()) {
                        ans = Math.max(ans, i - start + 1);
                    }
                    // 栈不空，当前有效段的其实位置是栈顶元素，而start标记的是栈底元素(也就是目前只是当前最长有效段的一部分有效段)
                    else {
                        ans = Math.max(ans, i - stack.peek());
                    }
                }
                // 栈里没有 （，无效段，从下个位置重新统计
                else {
                    start = i + 1;
                }
            }
        }
        return ans;
    }

    /**
     * 动态规划
     * 定义一个 dp 数组，其中第 i 个元素表示以下标为 ii的字符结尾的最长有效子字符串的长度。
     * 我们将 dp 数组全部初始化为 0 。现在，很明显有效的子字符串一定以 ‘)’ 结尾。
     * 这进一步可以得出结论：以 ‘(’ 结尾的子字符串对应的 dp 数组位置上的值必定为 0 。
     * 所以说我们只需要更新 ‘)’ 在 dp 数组中对应位置的值。
     * <p>
     * 为了求出 dp 数组，我们每两个字符检查一次，如果满足如下条件
     * 1. s[i] = ')' 且 s[i - 1] = '(' ，也就是字符串形如"……()"，我们可以推出：
     * dp[i] = dp[i-2] + 2 ,结束部分的 "()" 是一个有效子字符串，并且将之前有效子字符串的长度增加了 2 。
     * 2. s[i] = ')' 且 s[i - 1] = ')'，也就是形如 ".......))"
     * 这种情况下，dp[i-1]表示以i-1结尾的有效串的长度，假如这个【有效串是subs】
     * 截止到i的字符串就可表示为 ....[字符x]subs)，如果subs之前的那个字符是 (,
     * 那么它(s[k])和s[i]就形成一对括号，且其中包含的subs也有效，设字符x的下表是k.
     * 那么dp[i] = dp[k - 1] + 2 + subs.length; 其中 subs.length = dp[i-1]，k = i-1 - subs.length
     * <p>
     * 注意边界位置
     * <p>
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/longest-valid-parentheses/solution/zui-chang-you-xiao-gua-hao-by-leetcode/
     * 来源：力扣（LeetCode）
     *
     * @param s
     */
    public int solution4(String s) {
        if (s == null || s.length() < 2) return 0;
        int res = 0;
        int[] dp = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            // 以 （ 结尾，肯定是无效段
            if (s.charAt(i) == ')') {
                // 当前是 ），前一个是 （
                if (s.charAt(i - 1) == '(') {
                    // dp[i] = dp[i - 2] + 2;
                    // 保证不越界
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                    // s.charAt(dp[i - 1 - dp[i - 1]]) == '('
                // 当前是），前一个也是），类似 .....(.....))
                } else if (i > dp[i - 1] && s.charAt(i - 1 - dp[i - 1]) == '(') {
                    // dp[i] = dp[i - 1 - dp[i - 1] - 1] + 2 + dp[i - 1];
                    // 保证不越界，这里有三部分，最前面，中间，和新凑成的 ()
                    dp[i] = (i - dp[i - 1] >= 2 ? dp[i - 1 - dp[i - 1] - 1] : 0)
                            + 2 + dp[i - 1];
                }
            }
            // dp[i]代表的是以i结尾的最长有效串长，而整个str中最长的那部分不一定是在哪个位置结尾，所以不能直接返回 dp[s.length()-1]
            res = Math.max(res, dp[i]);
        }
        return res;
    }



    public static void main(String[] args) {
        System.out.println(new _32_LongestValidParentheses().solution3(")()())()()("));
        System.out.println(new _32_LongestValidParentheses().solution3("((()))()"));
    }
}
