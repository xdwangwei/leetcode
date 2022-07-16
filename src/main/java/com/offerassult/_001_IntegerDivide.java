package com.offerassult;

/**
 * @author wangwei
 * @date 2022/5/7 20:49
 * @description: _001_IntegerDivide
 *
 * 剑指 Offer II 001. 整数除法
 * 给定两个整数 a 和 b ，求它们的除法的商 a/b ，要求不得使用乘号 '*'、除号 '/' 以及求余符号 '%' 。
 *
 *
 * 注意：
 *
 * 整数除法的结果应当截去（truncate）其小数部分，例如：truncate(8.345) = 8 以及 truncate(-2.7335) = -2
 * 假设我们的环境只能存储 32 位有符号整数，其数值范围是 [−231, 231−1]。本题中，如果除法结果溢出，则返回 231 − 1
 *
 *
 * 示例 1：
 *
 * 输入：a = 15, b = 2
 * 输出：7
 * 解释：15/2 = truncate(7.5) = 7
 * 示例 2：
 *
 * 输入：a = 7, b = -3
 * 输出：-2
 * 解释：7/-3 = truncate(-2.33333..) = -2
 * 示例 3：
 *
 * 输入：a = 0, b = 1
 * 输出：0
 * 示例 4：
 *
 * 输入：a = 1, b = 1
 * 输出：1
 *
 *
 * 提示:
 *
 * -231 <= a, b <= 231 - 1
 * b != 0
 */
public class _001_IntegerDivide {


    /**
     * 类似二分搜索思想
     * 手写排除越界和特殊情况
     *
     * 比如计算 17 / 3， 我们反向思维，拼凑多个3去靠近17，  3， 3+3， 3+3 + 3+3， 相当于 3 乘以2的指数倍
     * 先找到这个最大的移位次数shift，比如 上面例子， 3 << 2 = 12 , 3 << 3 = 24, shift = 2
     * 那么，当shift = 2时，3 << 2 = 12 < 17，所以 ans += 1 << 2 = 4，这个4其实指的是17里面至少包含12，那么12/3=4，所以结果里面一步分是4
     *      然后 12 部分去除，17 - 12 = 5
     * 然后 shift-- = 1, 3 << 1 = 6 > 5，5 里面包含不下6，这里没有贡献了
     * 一直到shift所有取值结束，也就是 while(shift >= 0) 注意取等也是合理的，shift=0相当于 左移0位
     *
     * a / b核心在于，找到 a 里面包含的 b 的 2的指数倍 那部分，这部分的结果 直接就是 对应的 2 的指数
     * 然后 从 a 里面把这部分去除，对于 剩下部分， 同理， 先找到里面包含的2的指数倍部分，得到这部分结果，再去除。。。。
     * 其实就是 a = (d + f) ，其中 d = b * 2^n。然后 d / b 的结果直接就是 2^n
     * 然后 a = a - d = f = (x + y), x = b * 2^(n - 1)，。。。。重复
     * 所以第一步要找到 最大的n，使得 b *2^n <= a （整数前提下），也就是 最大的shift使得 b << shift <= a
     *
     * 由于题目取值问题，我们排除掉边界情况后，先记录一下被除数和除数的结果符号
     * 然后将二者均变为[负数]，再进行负数除法
     *
     * 比如 -17 / -3， 此时寻找最大的shift的规则变成了 b << shift >= a, 比如 -3， -6， -12， 都是 大于等于 a 的
     *
     *
     * 参考
     * https://leetcode-cn.com/problems/xoh6Oh/solution/hua-luo-yue-que-li-yong-wei-yun-suan-guo-h31x/
     * https://leetcode-cn.com/problems/xoh6Oh/solution/zheng-shu-chu-fa-by-leetcode-solution-3572/
     *
     * @param a
     * @param b
     * @return
     */
    public int divide(int a, int b) {
        // 特殊情况
        if (a == 0) {
            return 0;
        }
        if (b == 1) {
            return a;
        }
        // 越界情况
        if (a == Integer.MIN_VALUE && b == -1) {
            return Integer.MAX_VALUE;
        }
        // 判断最后符号
        boolean positive = true;
        // 并将二者都转为负数
        if (a > 0) {
            a = -a;
            positive = !positive;
        }
        if (b > 0) {
            b = -b;
            positive = !positive;
        }
        // 找到最大的shift使得 b * 2^shift >= a，注意 这里 a,b都是负数
        int temp = b, shift = 0;
        // 结束后 temp 的值就是 最大的那个 b * 2^shift
        // 注意这里条件本来应该是 temp * 2 >= a，但是 首先是因为不能出现乘法，所以用加法代替，然后又因为 担心二者相加越界，所以采用移向处理
        while (temp >= a - temp) {
            // 其实就是 temp *= 2, 也就是 temp = temp * 2^shift
            temp <<= 1;
            shift++;
        }
        int res = 0;
        // 此时shift是最大的移位数，满足 b * 2^shift >= a
        // 比如 -17 / -3， 此时 shift 就是 2， temp 就是 -12 = -3 * 2^2
        // 注意这里等号不能少
        while (shift >= 0) {
            // 指数部分(就是把a拆分成两部分后，能够快速得到结果的部分)
            // 能拆分的前提是要包含在内，如果是整数肯定是 a >= temp，此时是负数所以反过来
            if (temp >= a) {
                // a = temp + x, temp = b * 2^shift
                // a / b = temp / b + x / b = 2^shift + x / b
                res += 1 << shift;
                // 去掉这部分后，进行其余计算
                a -= temp;
            }
            // 下一次移位就减1，下一个能够快速得到结果的可拆分部分就是 b*2^(shift-1)，也就是 temp / 2
            shift--;
            temp >>= 1;
        }
        // 注意最后 根据符号返回
        return positive ? res : -res;
    }
}
