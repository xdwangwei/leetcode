package com.order;

/**
 * @author wangwei
 * @date 2022/5/9 12:43
 * @description: _67_BinaryPlus
 *
 * 67. 二进制加法
 * 给定两个 01 字符串 a 和 b ，请计算它们的和，并以二进制字符串的形式输出。
 *
 * 输入为 非空 字符串且只包含数字 1 和 0。
 *
 *
 *
 * 示例 1:
 *
 * 输入: a = "11", b = "10"
 * 输出: "101"
 * 示例 2:
 *
 * 输入: a = "1010", b = "1011"
 * 输出: "10101"
 *
 *
 * 提示：
 *
 * 每个字符串仅由字符 '0' 或 '1' 组成。
 * 1 <= a.length, b.length <= 10^4
 * 字符串如果不是 "0" ，就都不含前导零。
 */
public class _67_BinaryPlus {


    /**
     * 直接模拟，从后往前进行二进制加法，并用cnt记录给前一个位置的进位
     *
     * 末尾对齐，逐位相加。二进制中我们需要「逢二进一」。
     *
     * 类似于合并两个有序数组的写法，双指针，末尾对其，从后往前按位相加
     *
     * 具体的，从最低位开始遍历。我们使用一个变量 cnt 表示上一个位置的进位，初始值为 0。
     * 记当前位置对其的两个位为 a_i 和 b_i ，则每一位的答案为 ((a_i + b_i) + cnt) mod 2，
     * 下一位的进位为 ((a_i + b_i) + cnt) / 2，
     * 。重复上述步骤，直到数字 a 和 b 的每一位计算完毕。
     * 最后如果 cnt 的最高位不为 0，则将最高位添加到计算结果的末尾。
     *
     * 因为是从后往前运算，所以最终字符串要反转
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/JFETK5/solution/er-jin-zhi-jia-fa-by-leetcode-solution-fa6t/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param a
     * @param b
     * @return
     */
    public String addBinary(String a, String b) {
        int m = a.length(), n = b.length();
        int i = m - 1, j = n - 1, cnt = 0;
        StringBuilder builder = new StringBuilder();
        // 末尾对其，按位相加
        while (i >= 0 || j >= 0) {
            // 加上之前的进位
            int cur = cnt;
            // a[i]，a串结束了就给个0
            cur += i >= 0 ? a.charAt(i) - '0' : 0;
            // b[j]
            cur += j >= 0 ? b.charAt(j) - '0' : 0;
            // 当前位加法结果
            builder.append(cur % 2);
            // 进位结果
            cnt = cur / 2;
            // 指针左移
            i--;j--;
        }
        // 进位最高位
        if (cnt > 0) {
            builder.append(cnt);
        }
        // 反转
        return builder.reverse().toString();
    }
}
