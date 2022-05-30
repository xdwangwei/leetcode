package com.daily;

/**
 * @author wangwei
 * @date 2022/5/30 10:37
 * @description: _1021_RemoveOutermostParentheses
 *
 * 1021. 删除最外层的括号
 * 有效括号字符串为空 ""、"(" + A + ")" 或 A + B ，其中 A 和 B 都是有效的括号字符串，+ 代表字符串的连接。
 *
 * 例如，""，"()"，"(())()" 和 "(()(()))" 都是有效的括号字符串。
 * 如果有效字符串 s 非空，且不存在将其拆分为 s = A + B 的方法，我们称其为原语（primitive），其中 A 和 B 都是非空有效括号字符串。
 *
 * 给出一个非空有效字符串 s，考虑将其进行原语化分解，使得：s = P_1 + P_2 + ... + P_k，其中 P_i 是有效括号字符串原语。
 *
 * 对 s 进行原语化分解，删除分解中每个原语字符串的最外层括号，返回 s 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "(()())(())"
 * 输出："()()()"
 * 解释：
 * 输入字符串为 "(()())(())"，原语化分解得到 "(()())" + "(())"，
 * 删除每个部分中的最外层括号后得到 "()()" + "()" = "()()()"。
 * 示例 2：
 *
 * 输入：s = "(()())(())(()(()))"
 * 输出："()()()()(())"
 * 解释：
 * 输入字符串为 "(()())(())(()(()))"，原语化分解得到 "(()())" + "(())" + "(()(()))"，
 * 删除每个部分中的最外层括号后得到 "()()" + "()" + "()(())" = "()()()()(())"。
 * 示例 3：
 *
 * 输入：s = "()()"
 * 输出：""
 * 解释：
 * 输入字符串为 "()()"，原语化分解得到 "()" + "()"，
 * 删除每个部分中的最外层括号后得到 "" + "" = ""。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 105
 * s[i] 为 '(' 或 ')'
 * s 是一个有效括号字符串
 */
public class _1021_RemoveOutermostParentheses {


    /**
     * 方法一：计数
     * 因为 s 本身是有效括号串，
     * 所以如果从 s 开始位置计算子数组的和，遇到 ‘(’ 则加 1，遇到 ‘)’ 则减 1，那么每一次和为0时都代表一个有效串的结束，
     * 其中第一次和为 0 时则为第一个原语。
     * 我们保留原语部分除去最外围()的部分
     * 从上一个原语的结束位置的下一个位置开始继续求子数组的和，和首次为 0 时则是另一个新的原语，直到遇到 s 的结尾。
     * 保存结果时，忽略每个原语的开始字符和结尾字符，其他字符均保存下来生成新的字符串。
     * 代码对流程进行了部分优化，减少了判断语句。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/remove-outermost-parentheses/solution/shan-chu-zui-wai-ceng-de-gua-hao-by-leet-sux0/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public String removeOuterParentheses(String s) {
        StringBuilder builder = new StringBuilder();
        // 每次cnt=0，代表遇见一个原语，left记录当前原语部分的起始
        int left = 0, cnt = 0;
        for (int i = 0; i < s.length(); ++i) {
            // 当前字符串
            // 统计
            char c = s.charAt(i);
            if (c == '(') {
                cnt++;
            } else {
                cnt--;
            }
            // 遇到一个原语
            if (cnt == 0) {
                // 去除最外围()
                builder.append(s, left + 1, i);
                // 记录下一个原语起始位置
                left = i + 1;
            }
        }
        return builder.toString();
    }


    /**
     * 与方法一思想一样，只是不用找到每个原语后再去截取它中间部分，而是在遍历过程中直接判断哪些部分需要保留
     * 初始cnt=0，原语结束时cnt=0，cnt两次为0分别代表起始的(和结尾的),那么在这个过程中，cnt!=0时的字符串都需要保留
     * @param s
     * @return
     */
    public String removeOuterParentheses2(String s) {
        StringBuilder builder = new StringBuilder();
        // cnt=0，代表遇见一个原语的开始，cnt=0，代表一个原语的结束
        // cnt两次为0分别代表起始的(和结尾的),那么在这个过程中，cnt!=0时的字符串都需要保留
        int cnt = 0;
        for (int i = 0; i < s.length(); ++i) {
            // 当前字符
            char c = s.charAt(i);
            // 用三个if巧妙的顺序完成，第一次cnt=0时，不会保留(；最后一次cnt=0时，不会保留)；能保留中间过程所有字符
            // 判断)写在这里，能保证结束时c=)不会被保留
            if (c == ')') {
                cnt--;
            }
            // cnt两次为0分别代表起始的(和结尾的),那么在这个过程中，cnt!=0时的字符串都需要保留
            if (cnt != 0) {
                builder.append(c);
            }
            // 判断(写在这里，能保证初始时c=(不会被保留
            if (c == '(') {
                cnt++;
            }
        }
        return builder.toString();
    }
}
