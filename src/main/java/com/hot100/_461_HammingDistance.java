package com.hot100;

/**
 * @author wangwei
 * 2022/4/19 15:35
 *
 * 461. 汉明距离
 * 两个整数之间的 汉明距离 指的是这两个数字对应二进制位不同的位置的数目。
 *
 * 给你两个整数 x 和 y，计算并返回它们之间的汉明距离。
 *
 *
 *
 * 示例 1：
 *
 * 输入：x = 1, y = 4
 * 输出：2
 * 解释：
 * 1   (0 0 0 1)
 * 4   (0 1 0 0)
 *        ↑   ↑
 * 上面的箭头指出了对应二进制位不同的位置。
 * 示例 2：
 *
 * 输入：x = 3, y = 1
 * 输出：1
 *
 *
 * 提示：
 *
 * 0 <= x, y <= 231 - 1
 */
public class _461_HammingDistance {

    /**
     * 位运算：先异或，再统计这个异或结果对应的二进制中1的个数
     * @param x
     * @param y
     * @return
     */
    public int hammingDistance(int x, int y) {

        int xor = x ^ y;
        int n = 0;
        while (xor != 0) {
            // n & n - 1 是消除 n 的二进制表示中 最后1个1
            xor  = xor & (xor - 1);
            n++;
        }
        return n;
    }
}
