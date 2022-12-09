package com.daily;

/**
 * @author wangwei
 * @date 2022/12/9 15:07
 * @description: _1780_CheckIfNumberIsASumOfPowersOfThree
 *
 * 1780. 判断一个数字是否可以表示成三的幂的和
 * 给你一个整数 n ，如果你可以将 n 表示成若干个不同的三的幂之和，请你返回 true ，否则请返回 false 。
 *
 * 对于一个整数 y ，如果存在整数 x 满足 y == 3x ，我们称这个整数 y 是三的幂。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 12
 * 输出：true
 * 解释：12 = 31 + 32
 * 示例 2：
 *
 * 输入：n = 91
 * 输出：true
 * 解释：91 = 30 + 32 + 34
 * 示例 3：
 *
 * 输入：n = 21
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= n <= 107
 * 通过次数20,790提交次数28,107
 */
public class _1780_CheckIfNumberIsASumOfPowersOfThree {

    /**
     * 如果n的三进制表示串为 abcdef，其取值均为 0、1或2
     * 那么 n = a*3^5 + b*3^4 + c*3^3 + d*3^2 + e*3^1 + f*3^0
     * 因为 n 要能够表示为 若干个【不同】的三的幂之和
     * 那么 a,b,c,d,e,f 只能取值0或1，（取2代表某个幂次使用两次）
     *
     * 如何得到 a，b，c，d，e，f
     * 类似于 10进制转二进制，循环 除以2--取余，除以2--取余，。。。
     * 这里改为 除以3取余即可
     * @param n
     * @return
     */
    public boolean checkPowersOfThree(int n) {
        // n 的三进制表示中，每个位置上的数字都不能为2
        while (n != 0) {
            // 每次取余结果只能为0或1
            if (n % 3 == 2) {
                return false;
            }
            // n禁止转3进制
            n /= 3;
        }
        return true;
    }
}
