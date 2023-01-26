package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/1/26 13:11
 * @description: _1663_SmallestStringWithAGivenNumericValue
 *
 * 1663. 具有给定数值的最小字符串
 * 小写字符 的 数值 是它在字母表中的位置（从 1 开始），因此 a 的数值为 1 ，b 的数值为 2 ，c 的数值为 3 ，以此类推。
 *
 * 字符串由若干小写字符组成，字符串的数值 为各字符的数值之和。例如，字符串 "abe" 的数值等于 1 + 2 + 5 = 8 。
 *
 * 给你两个整数 n 和 k 。返回 长度 等于 n 且 数值 等于 k 的 字典序最小 的字符串。
 *
 * 注意，如果字符串 x 在字典排序中位于 y 之前，就认为 x 字典序比 y 小，有以下两种情况：
 *
 * x 是 y 的一个前缀；
 * 如果 i 是 x[i] != y[i] 的第一个位置，且 x[i] 在字母表中的位置比 y[i] 靠前。
 *
 *
 * 示例 1：
 *
 * 输入：n = 3, k = 27
 * 输出："aay"
 * 解释：字符串的数值为 1 + 1 + 25 = 27，它是数值满足要求且长度等于 3 字典序最小的字符串。
 * 示例 2：
 *
 * 输入：n = 5, k = 73
 * 输出："aaszz"
 *
 *
 * 提示：
 *
 * 1 <= n <= 105
 * n <= k <= 26 * n
 * 通过次数16,363提交次数26,189
 */
public class _1663_SmallestStringWithAGivenNumericValue {

    /**
     * 方法一：贪心
     *
     * 我们先将字符串的每个字符都初始化为 'a'，此时剩余的数值为 k −= n。
     *
     * 接着从后往前遍历字符串，每次贪心地将当前位置的字符替换为能够使得剩余的数字最小的字符 'z'，
     * 直到剩余的数字 k 不超过 25。
     * 最后将剩余的数字加到我们遍历到的位置上即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/smallest-string-with-a-given-numeric-value/solution/by-lcbin-7102/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param k
     * @return
     */
    public String getSmallestString(int n, int k) {
        char[] ans = new char[n];
        // 先初始化每个位置为 ‘a’
        Arrays.fill(ans, 'a');
        // 剩余数值k -= n
        k -= n;
        // 从后往前，贪心增大字符，最大为‘z’
        int i = n - 1;
        for (; k > 25; k -= 25) {
            ans[i--] = 'z';
        }
        // 当不足以增大到z时停止，在当前位置增加k
        ans[i] = (char) ('a' + k);
        // 返回
        return String.valueOf(ans);
    }
}
