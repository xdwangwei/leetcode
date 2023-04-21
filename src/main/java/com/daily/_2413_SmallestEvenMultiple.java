package com.daily;

/**
 * @author wangwei
 * @date 2023/4/21 9:49
 * @description: _2413_SmallestEvenMultiple
 *
 * 2413. 最小偶倍数
 * 给你一个正整数 n ，返回 2 和 n 的最小公倍数（正整数）。
 *
 *
 * 示例 1：
 *
 * 输入：n = 5
 * 输出：10
 * 解释：5 和 2 的最小公倍数是 10 。
 * 示例 2：
 *
 * 输入：n = 6
 * 输出：6
 * 解释：6 和 2 的最小公倍数是 6 。注意数字会是它自身的倍数。
 *
 *
 * 提示：
 *
 * 1 <= n <= 150
 * 通过次数26,015提交次数29,601
 */
public class _2413_SmallestEvenMultiple {

    /**
     * 数学：如果 n 为偶数，那么 2 和 n 的最小公倍数就是 n 本身。否则，2 和 n 的最小公倍数就是 n×2。
     *
     * return n % 2 == 0 ? n : n * 2;
     *
     * 简化：
     *
     * return n << (n & 1);
     *
     * @param n
     * @return
     */
    public int smallestEvenMultiple(int n) {
        return n << (n & 1);
    }
}
