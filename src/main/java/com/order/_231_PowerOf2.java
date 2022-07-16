package com.order;

/**
 * @author wangwei
 * 2020/8/1 23:34
 *
 * 给定一个整数，编写一个函数来判断它是否是 2 的幂次方。
 *
 * 示例 1:
 *
 * 输入: 1
 * 输出: true
 * 解释: 20 = 1
 * 示例 2:
 *
 * 输入: 16
 * 输出: true
 * 解释: 24 = 16
 * 示例 3:
 *
 * 输入: 218
 * 输出: false
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/power-of-two
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _231_PowerOf2 {

    /**
     * 一个数如果是 2 的指数，那么它的二进制表示一定只含有一个 1：
     *
     * 2^0 = 1 = 0b0001
     * 2^1 = 2 = 0b0010
     * 2^2 = 4 = 0b0100
     *
     * n & (n - 1) 的结果应该为0
     *
     * @param n
     * @return
     */
    public boolean isPowerOfTwo(int n) {
        if (n <= 0) return false;
        // 注意运算符优先级，括号不可以省略
        return (n & (n - 1)) == 0;
    }
}
