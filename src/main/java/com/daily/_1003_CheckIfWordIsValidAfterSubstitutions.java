package com.daily;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2023/5/2 15:02
 * @description: _1003_jjj
 * <p>
 * 1003. 检查替换后的词是否有效
 * 给你一个字符串 s ，请你判断它是否 有效 。
 * 字符串 s 有效 需要满足：假设开始有一个空字符串 t = "" ，你可以执行 任意次 下述操作将 t 转换为 s ：
 * <p>
 * 将字符串 "abc" 插入到 t 中的任意位置。形式上，t 变为 tleft + "abc" + tright，其中 t == tleft + tright 。注意，tleft 和 tright 可能为 空 。
 * 如果字符串 s 有效，则返回 true；否则，返回 false。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * 输入：s = "aabcbc"
 * 输出：true
 * 解释：
 * "" -> "abc" -> "aabcbc"
 * 因此，"aabcbc" 有效。
 * 示例 2：
 * <p>
 * 输入：s = "abcabcababcc"
 * 输出：true
 * 解释：
 * "" -> "abc" -> "abcabc" -> "abcabcabc" -> "abcabcababcc"
 * 因此，"abcabcababcc" 有效。
 * 示例 3：
 * <p>
 * 输入：s = "abccba"
 * 输出：false
 * 解释：执行操作无法得到 "abccba" 。
 * <p>
 * <p>
 * 提示：
 * <p>
 * 1 <= s.length <= 2 * 104
 * s 由字母 'a'、'b' 和 'c' 组成
 * 通过次数16,254提交次数27,822
 */
public class _1003_CheckIfWordIsValidAfterSubstitutions {

    /**
     * 方法一：将 s 中所有 "abc" 替换为 ""，判断最终 s 是否为空串
     *
     *      * 我们观察题目中的操作，可以发现，每一次都会在字符串的任意位置插入字符串 "abc"，
     *      * 所以每次插入操作之后，字符串的长度都会增加 3。
     *      * 如果字符串 s 有效，那么它的长度一定是 3 的倍数。
     *      * 因此，我们先对字符串 s 的长度进行判断，如果不是 3 的倍数，那么 s 一定无效，可以直接返回 false。
     *
     * @param s
     * @return
     */
    public boolean isValid(String s) {
        // 长度必须是3的整数倍
        if (s.length() == 0) return true;
        if (s.length() % 3 != 0) return false;
        // 不包含 "abc"，返回 false
        if (!s.contains("abc")) return false;
        int i = s.indexOf("abc");
        // 替换 遇到的第一个 "abc"为 ""，递归
        return isValid(s.substring(0, i) + s.substring(i + 3));
    }

    /**
     * 方法二：栈
     *
     * 我们观察题目中的操作，可以发现，每一次都会在字符串的任意位置插入字符串 "abc"，
     * 所以每次插入操作之后，字符串的长度都会增加 3。
     * 如果字符串 s 有效，那么它的长度一定是 3 的倍数。
     * 因此，我们先对字符串 s 的长度进行判断，如果不是 3 的倍数，那么 s 一定无效，可以直接返回 false。
     *
     * 接下来我们遍历字符串 s 的每个字符 c，我们先将字符 c 压入栈 t 中。
     * 如果此时栈 t 的长度大于等于 3，并且栈顶的三个元素组成了字符串 "abc"，那么我们就将栈顶的三个元素弹出。
     * 然后继续遍历字符串 s 的下一个字符。
     *
     * 遍历结束之后，如果栈 t 为空，那么说明字符串 s 有效，返回 true，否则返回 false。
     *
     * 优化
     *
     * 由于 s 是 空串插入多个 ”abc“ 得到，
     * 所以 在顺序遍历的过程中，遇到的第一个 ‘b’，它的前一个字符一定是 'a'，遇到的 第一个 ‘c’，它的前一个字符一定是 'b'
     * 那么是否要记录当前遇到的 'b' 或 ‘c’ 是否是第一个呢？
     * 不需要，当我们能顺序遍历完第一个 'b','c' 后，必然得到了 第一个完整的 'abc'，此时 弹出 "abc" 后，后面的 'b','c' 又会是第一个遇到
     * 所以就实现了，所有的 ‘b’的前一个字符一定是 'a'， 所有的‘c’的前一个字符一定是 'b'，否则就 返回 false
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/check-if-word-is-valid-after-substitutions/solution/python3javacgotypescript-yi-ti-yi-jie-zh-80tk/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public boolean isValid2(String s) {
        // 长度必须是3的整数倍
        if (s.length() % 3 != 0) {
            return false;
        }
        // 栈
        Deque<Character> stack = new ArrayDeque<>();
        // 顺序遍历
        for (char c : s.toCharArray()) {
            // 遇到 'a'，直接入栈
            if (c == 'a') {
                stack.push(c);
                continue;
            }
            // 遇到 'b'
            if (c == 'b') {
                // 前一个字符必须是 ’a‘
                if (stack.isEmpty() || stack.peek() != 'a') {
                    return false;
                }
                // 'b' 入栈
                stack.push(c);
                continue;
            }
            // 遇到 'c'
            if (c == 'c') {
                // 前一个字符必须是 'b'
                if (stack.isEmpty() || stack.peek() != 'b') {
                    return false;
                }
                // 上面的if已经保证了‘b’的前一个字符一定是 'a'
                // 所有这里一定是遇到了 'abc'，c 还没有入栈，所以把 'ab'都弹出去就行
                stack.pop();
                stack.pop();
            }
        }
        // 最后栈必须为空
        return stack.isEmpty();
    }
}
