package com.daily;

/**
 * @author wangwei
 * @date 2022/12/3 10:41
 * @description: _1792_SecondLargestNumberInString
 *
 * 1796. 字符串中第二大的数字
 * 给你一个混合字符串 s ，请你返回 s 中 第二大 的数字，如果不存在第二大的数字，请你返回 -1 。
 *
 * 混合字符串 由小写英文字母和数字组成。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "dfa12321afd"
 * 输出：2
 * 解释：出现在 s 中的数字包括 [1, 2, 3] 。第二大的数字是 2 。
 * 示例 2：
 *
 * 输入：s = "abc1111"
 * 输出：-1
 * 解释：出现在 s 中的数字只包含 [1] 。没有第二大的数字。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 500
 * s 只包含小写英文字母和（或）数字。
 * 通过次数17,805提交次数34,173
 */
public class _1792_SecondLargestNumberInString {

    public int secondHighest(String s) {
        char[] chars = s.toCharArray();
        // max1 记录最大值，max2记录第二大的值
        // max2 需要严格小于max1
        // 不存在第二大的值时需要返回-1.所以直接初始化为-1
        int max1 = -1, max2 = -1;
        for (char c : chars) {
            // 跳过英文字母
            if (Character.isDigit(c)) {
                int num = c - '0';
                // 比 最大值 大
                if (num > max1) {
                    // 更新 第二大值
                    max2 = max1;
                    // 更新 最大值
                    max1 = num;
                // 比最大值小但是比 第二大的值 大
                } else if (num < max1 && num > max2) {
                    // 更新第二大值
                    max2 = num;
                }
            }

        }
        // 返回第二大的值
        return max2;
    }
}
