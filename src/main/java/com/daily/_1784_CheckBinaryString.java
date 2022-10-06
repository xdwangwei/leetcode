package com.daily;

/**
 * @author wangwei
 * @date 2022/10/6 17:47
 * @description: _1784_CheckBinaryString
 *
 * 1784. 检查二进制字符串字段
 * 给你一个二进制字符串 s ，该字符串 不含前导零 。
 *
 * 如果 s 包含 零个或一个由连续的 '1' 组成的字段 ，返回 true 。否则，返回 false 。
 *
 * 如果 s 中 由连续若干个 '1' 组成的字段 数量不超过 1，返回 true 。否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "1001"
 * 输出：false
 * 解释：由连续若干个 '1' 组成的字段数量为 2，返回 false
 * 示例 2：
 *
 * 输入：s = "110"
 * 输出：true
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 100
 * s[i] 为 '0' 或 '1'
 * s[0] 为 '1'
 */
public class _1784_CheckBinaryString {

    /**
     * 总结：连续1的子串最多有1个
     * 方法：用last标记上一个1的位置；当遇到s[i]==1时，判断last是否等于i-1，如果不等于，说明当前1是另一个连续1子串的开始，返回false
     *      last初始化为-1，第一次遇到1时，直接更新last，
     * @param s
     * @return
     */
    public boolean checkOnesSegment(String s) {
        int n = s.length();
        // 初始化last
        int last = -1;
        for (int i = 0; i < n; ++i) {
            // 当前是1，
            if (s.charAt(i) == '1') {
                // 除非当前是第一个1，或者上一个字符也是1
                if (last == -1 || last + 1 == i) {
                    last = i;
                } else {
                    // 否则，当前1是另一部分连续1子串，返回false
                    return false;
                }
            }
        }
        return true;
    }
}
