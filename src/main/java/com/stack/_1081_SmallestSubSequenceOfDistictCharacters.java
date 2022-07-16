package com.stack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * 2021/12/8 11:21
 *
 * 返回 s 字典序最小的子序列，该子序列包含 s 的所有不同字符，且只包含一次。
 *
 * 注意：该题与 316 https://leetcode.com/problems/remove-duplicate-letters/ 相同
 *
 *  
 *
 * 示例 1：
 *
 * 输入：s = "bcabc"
 * 输出："abc"
 * 示例 2：
 *
 * 输入：s = "cbacdcbc"
 * 输出："acdb"
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/smallest-subsequence-of-distinct-characters
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 */
public class _1081_SmallestSubSequenceOfDistictCharacters {

    /**
     * 单调栈
     *
     * 因为要保持相对顺序，所以不能用集合去重
     * 每个字符只能出现一次，所以要用boolean数组记录每个字符是否已经统计
     * 栈和队列都能保证原来的相对顺序
     *
     * 但是，我们还要保证 字典序最小，如何保证呢？
     *      遍历到字符c时，假如它前面统计的那个字符是 x，
     *          如果x>c并且后面还有x，那么应该把前面这个x移走，使用后面那个还没有统计到的x，这样的字典序肯定比直接把c放在x后面小
     *                  同理，如果把x移走后，c前面变成了y，而y在之后也还存在，那么应该把y也移除了
     *              这样的话，就存在对序列尾部连续的操作，我们可以使用 栈 或者 双端队列完成
     *          如果x>c但是x只有一个，那没办法了，只能把它保留下来
     * 为什么说是单调栈呢？因为我们是在尽可能地让栈中元素维持一个从底到顶的增序，当然可能存在某个字符违背这个规则，没办法，它只出现一次，为了维持原来顺序，只能这样
     * 所以其实是一种贪心思想，每遇到一个字符，就把破坏递增性的栈顶元素尽量去除，
     * @param s
     * @return
     */
    public String smallestSubsequence(String s) {
        char[] chars = s.toCharArray();
        // 统计每个字符出现(剩余)次数
        int[] count = new int[256];
        // 每个字符是否已经统计过
        boolean[] inStack = new boolean[256];
        // 使用栈保存结果
        Deque<Character> stack = new ArrayDeque<>();
        // 先统计每个字符的次数
        for (char c : chars) {
            count[c]++;
        }
        // 逐个遍历
        for (char c : chars) {
            // 如果它已经在栈中，那么不能重复，跳过，剩下部分里面这个字符的个数-1
            if (inStack[c]) {
                count[c]--;
                continue;
            }
            // 保持字典序最小
            // 上一个元素比c大
            while (!stack.isEmpty() && stack.peek() > c) {
                // 并且c后面还有这个字符，那就出栈，使用后面的
                if (count[stack.peek()] > 1) {
                    // 它的个数减少1
                    count[stack.peek()]--;
                    // 因为出栈了，所以标记它未统计
                    inStack[stack.peek()] = false;
                    // 出栈
                    stack.pop();
                    // 虽然栈顶元素比c大，但是它只有一个，没办法，不能弹栈，这里要break，不然会死循环
                } else {
                    break;
                }
            }
            // 当前元素入栈
            stack.push(c);
            // 标记它已统计
            inStack[c] = true;
        }
        // 最终栈中元素自底到顶就是要返回的结果，所以需要builder再反转一下
        StringBuilder buider = new StringBuilder();
        while (!stack.isEmpty()) {
            buider.append(stack.pop());
        }
        return buider.reverse().toString();
    }
}
