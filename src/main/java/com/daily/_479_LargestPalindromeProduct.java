package com.daily;

/**
 * @author wangwei
 * 2022/4/16 9:49
 *
 * 479. 最大回文数乘积
 * 给定一个整数 n ，返回 可表示为两个 n 位整数乘积的 最大回文整数 。因为答案可能非常大，所以返回它对 1337 取余 。
 *
 *
 *
 * 示例 1:
 *
 * 输入：n = 2
 * 输出：987
 * 解释：99 x 91 = 9009, 9009 % 1337 = 987
 * 示例 2:
 *
 * 输入： n = 1
 * 输出： 9
 *
 *
 * 提示:
 *
 * 1 <= n <= 8
 */
public class _479_LargestPalindromeProduct {

    public int largestPalindrome(int n) {
        if (n == 1) {
            return 9;
        }
        // n 位数的最大值
        int upper = (int)Math.pow(10, n) - 1;
        int res = 0;
        // 【枚举】左半部分n位数，通过它构造对称的右半部分，得到回文数p
        // 注意 for 退出条件，内循环得到结果后，这里自然退出
        for (int left = upper; res == 0; left--) {
            // 由左半部分构造对称部分得到回文数p
            // n最大是8，所以 回文数最大是 16位， 10^16 > 2 ^32，所以这里用long保存
            long p = left;
            for (int x = left; x > 0; x /= 10) {
                p = p * 10 + x % 10;
            }
            // 对于这个回文数，尝试将其拆分成 x 和 p / x，要求两部分都是 n 位数，并且能被 p 整除，也就是不存在小数
            // 要将其分解成两个n位数乘机，同样【枚举】第一个n位数，第二个n位数是 p/ x
            // 避免重复，枚举时 x 最大时 upper，最小是 x^2 = p
            // 这里写法是 x * x ，因为 p 是 long，所以必须保证这个结果也是 long， 也就是 x 也要是 long
            for (long x = upper; x * x >= p; x--) {
                // 必须是整除
                if (p % x == 0) {
                    res = (int) (p % 1337);
                    // break之后，res != 0， 外围 for 也能退出
                    break;
                }
            }
        }
        return res;
    }
}
