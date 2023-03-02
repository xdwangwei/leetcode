package com.daily;

/**
 * @author wangwei
 * @date 2023/3/2 18:55
 * @description: _MianShi_05_02_NumberToStringLCCI
 *
 * 面试题 05.02. 二进制数转字符串
 * 二进制数转字符串。给定一个介于0和1之间的实数（如0.72），类型为double，打印它的二进制表达式。如果该数字无法精确地用32位以内的二进制表示，则打印“ERROR”。
 *
 * 示例1:
 *
 *  输入：0.625
 *  输出："0.101"
 * 示例2:
 *
 *  输入：0.1
 *  输出："ERROR"
 *  提示：0.1无法被二进制准确表示
 *
 *
 * 提示：
 *
 * 32位包括输出中的 "0." 这两位。
 * 题目保证输入用例的小数位数最多只有 6 位
 * 通过次数12,704提交次数17,660
 */
public class _MianShi_05_02_NumberToStringLCCI {

    /**
     * 运算规律
     *
     * 假设 num 的二进制表示为 0.abcdedf.... 其中 a、b、c、d、... 取值为 0 或 1
     * 那么 num = a * 2^-1 + b * 2^-2 + c * 2^-3 + ....
     * 只需要顺序判断每一项（从高到低）的系数 是0还是1即可。
     * 第 i 项为 2^-i，如果 num > 2^-i，sb.append("1"), num -= 2^-i
     * @param num
     * @return
     */
    public String printBin(double num) {
        StringBuilder sb = new StringBuilder("0.");
        // 顺序判断每一项的系数是0还是1，第一项也是最高项，为 2^-1 = 0.5
        double x = 0.5;
        // 题目要求，不能超过32位
        while (sb.length() <= 32 && num > 0) {
            if (num >= x) {
                // 当前项系数为1
                sb.append("1");
                // 减去这部分
                num -= x;
            } else {
                // 当前项系数为0
                sb.append("0");
            }
            // 下一项为 2^-(i+1) 次方 = 2^-i / 2
            x /= 2;
        }
        // while退出时 超过32位 或 num变为0（即在32位内成功表示，求出了所有系数）
        // 超过32位返回 “ERROR”
        return num > 0 ? "ERROR" : sb.toString();
    }

    /**
     * 进制转换规律
     *
     * 介于 0 和 1 之间的实数的整数部分是 0，小数部分大于 0，因此其二进制表示的整数部分是 0，需要将小数部分转换成二进制表示。
     *
     * 以示例 1 为例，十进制数 0.625 可以写成 2^-1 + 2^-3，因此对应的二进制数是 0.101，
     * 二进制数中的左边的1 为小数点后第一位，表示 2^-1 ，右边的 1 为小数点后第三位，表示 2^-3。
     *
     * 如果将十进制数 0.625 乘以 2，则得到 1.25，可以写成 1.01 ，因此对应的二进制数是 1.01
     *
     * 二进制数 0.101 的两倍是 1.01 ，因此在二进制表示中，将一个数乘以 2 的效果是将小数点向右移动一位。
     *
     * 【结论】
     *
     * 根据上述结论，将实数的十进制表示转换成二进制表示的方法是：
     *      每次将实数乘以 2，将此时的【整数部分】添加到二进制表示的末尾，
     *      然后将整数部分置为 0，
     *      重复上述操作，直到小数部分变成 0 或者小数部分出现循环时结束操作。
     *      当小数部分变成 0 时，得到二进制表示下的有限小数；当小数部分出现循环时，得到二进制表示下的无限循环小数。
     *
     * 由于这道题要求二进制表示的长度最多为 32 位，否则返回 “ERROR"，
     * 因此不需要判断给定实数的二进制表示的结果是有限小数还是无限循环小数，
     * 而是在小数部分变成 0 或者二进制表示的长度超过 32 位时结束操作。
     * 当操作结束时，如果二进制表示的长度不超过 32 位则返回二进制表示，否则返回 “ERROR"。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/bianry-number-to-string-lcci/solution/er-jin-zhi-shu-zhuan-zi-fu-chuan-by-leet-1rjh/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param num
     * @return
     */
    public String printBin2(double num) {
        StringBuilder sb = new StringBuilder("0.");
        // 十进制小数 【小数部分】转二进制  转换规则
        while (sb.length() <= 32 && num > 0) {
            // 二倍
            num *= 2;
            // 取整数部分，0 或 1
            int digit = (int) num;
            // 追加 整数部分0或1
            sb.append(digit);
            // 整数部分置0
            num -= digit;
        }
        // 返回，超过32位返回“ERROR”
        return sb.length() <= 32 ? sb.toString() : "ERROR";
    }
}
