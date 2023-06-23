package com.daily;

/**
 * @author wangwei
 * @date 2023/6/23 22:02
 * @description: _2496_MaximumValueOfStringInArray
 *
 * 2496. 数组中字符串的最大值
 * 一个由字母和数字组成的字符串的 值 定义如下：
 *
 * 如果字符串 只 包含数字，那么值为该字符串在 10 进制下的所表示的数字。
 * 否则，值为字符串的 长度 。
 * 给你一个字符串数组 strs ，每个字符串都只由字母和数字组成，请你返回 strs 中字符串的 最大值 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：strs = ["alic3","bob","3","4","00000"]
 * 输出：5
 * 解释：
 * - "alic3" 包含字母和数字，所以值为长度 5 。
 * - "bob" 只包含字母，所以值为长度 3 。
 * - "3" 只包含数字，所以值为 3 。
 * - "4" 只包含数字，所以值为 4 。
 * - "00000" 只包含数字，所以值为 0 。
 * 所以最大的值为 5 ，是字符串 "alic3" 的值。
 * 示例 2：
 *
 * 输入：strs = ["1","01","001","0001"]
 * 输出：1
 * 解释：
 * 数组中所有字符串的值都是 1 ，所以我们返回 1 。
 *
 *
 * 提示：
 *
 * 1 <= strs.length <= 100
 * 1 <= strs[i].length <= 9
 * strs[i] 只包含小写英文字母和数字。
 * 通过次数15,434提交次数19,328
 * 请问您在哪类招聘中遇到此题？
 */
public class _2496_MaximumValueOfStringInArray {

    /**
     * 方法一：模拟
     *
     * 我们定义一个函数 f(s)，用于计算字符串 s 的值。
     *
     * 如果 s 只包含数字，那么 f(s) 就是 s 在十进制下的值；否则 f(s) 就是 s 的长度。
     *
     * 答案为 s ∈ strs, max { f(s) } 。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/maximum-value-of-a-string-in-an-array/solution/python3javacgotypescriptcrust-yi-ti-yi-j-srkr/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param strs
     * @return
     */
    public int maximumValue(String[] strs) {
        int ans = 0;
        // 返回所有字符串值的最大值
        for (String str : strs) {
            ans = Math.max(ans, f(str));
        }
        return ans;
    }

    /**
     * f(s)，用于计算字符串 s 的值。
     *
     * 如果 s 只包含数字，那么 f(s) 就是 s 在十进制下的值；
     * 否则 f(s) 就是 s 的长度。
     *
     * @return
     */
    private int f(String str) {
        int n = str.length();
        int ret = 0;
        // 遍历 + 计算数字串的值
        for (int i = 0; i < n; ++i) {
            char c = str.charAt(i);
            // 是字母
            if (Character.isLetter(c)) {
                // 返回 字符串长度
                return n;
            }
            // 计算十进制的值
            ret = ret * 10 + c - '0';
        }
        // 返回
        return ret;
    }
}
