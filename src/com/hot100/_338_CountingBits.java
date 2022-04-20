package com.hot100;

/**
 * @author wangwei
 * 2022/4/19 15:50
 *
 * 338. 比特位计数
 * 给你一个整数 n ，对于 0 <= i <= n 中的每个 i ，计算其二进制表示中 1 的个数 ，返回一个长度为 n + 1 的数组 ans 作为答案。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 2
 * 输出：[0,1,1]
 * 解释：
 * 0 --> 0
 * 1 --> 1
 * 2 --> 10
 * 示例 2：
 *
 * 输入：n = 5
 * 输出：[0,1,1,2,1,2]
 * 解释：
 * 0 --> 0
 * 1 --> 1
 * 2 --> 10
 * 3 --> 11
 * 4 --> 100
 * 5 --> 101
 *
 *
 * 提示：
 *
 * 0 <= n <= 105
 */
public class _338_CountingBits {

    /**
     * 题目：n 是非零整数
     * 方法一，对于每个数字进行二进制表示中位1统计
     * n&n-1 是 去掉 n 的二进制表示中最后一个1
     * @param n
     * @return
     */
    public int[] countBits(int n) {
        int[] bits = new int[n + 1];
        for (int i = 1; i <= n; ++i) {
            int x = i;
            while (x != 0) {
                x &= (x - 1);
                bits[i]++;
            }
        }
        return bits;
    }

    /**
     * 动态规划
     * 从方法一可以看出来，n&n-1 是 n 去掉了最后一个1，那么 动态规划应该有 bits[x] = bits[x&x-1] + 1
     * 并且因为是去掉最后1个1了，所以 n 肯定 大于 n&n-1 ，所以 递归方程是可以用的
     * @param n
     * @return
     */
    public int[] countBits2(int n) {
        int[] bits = new int[n + 1];
        for (int i = 1; i <= n; ++i) {
            bits[i] = bits[i & (i - 1)] + 1;
        }
        return bits;
    }

    /**
     * 动态规划
     * 和方法二类似的原理，x >> 1 得到 了 x / 2，那么 bits[x] = bits[x/2] + 1(?)
     * 当然，如果 x 是偶数，那么最后一位是0，所以 bit[i] = bit[i / 2]
     * 如果 x 是 奇数，那么 最后一位是 1， 所以 bit[i] = bit[i/2] + 1
     * 并且 i / 2 会在 i 之前计算出来
     * @param n
     * @return
     */
    public int[] countBits3(int n) {
        int[] bits = new int[n + 1];
        for (int i = 1; i <= n; ++i) {
            // 奇偶性决定要不要 +1
            bits[i] = bits[i >> 1] + (i & 1);
        }
        return bits;
    }
}
