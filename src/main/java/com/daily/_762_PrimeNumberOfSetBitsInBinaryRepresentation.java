package com.daily;

/**
 * @author wangwei
 * @date 2022/4/5 8:53
 *
 * 762. 二进制表示中质数个计算置位
 * 给你两个整数 left 和 right ，在闭区间 [left, right] 范围内，统计并返回 [计算置位位数为质数] 的整数个数。
 *
 * [计算置位位数] 就是二进制表示中 1 的个数。
 *
 * 例如， 21 的二进制表示 10101 有 3 个计算置位。
 *
 *
 * 示例 1：
 *
 * 输入：left = 6, right = 10
 * 输出：4
 * 解释：
 * 6 -> 110 (2 个计算置位，2 是质数)
 * 7 -> 111 (3 个计算置位，3 是质数)
 * 9 -> 1001 (2 个计算置位，2 是质数)
 * 10-> 1010 (2 个计算置位，2 是质数)
 * 共计 4 个计算置位为质数的数字。
 * 示例 2：
 *
 * 输入：left = 10, right = 15
 * 输出：5
 * 解释：
 * 10 -> 1010 (2 个计算置位, 2 是质数)
 * 11 -> 1011 (3 个计算置位, 3 是质数)
 * 12 -> 1100 (2 个计算置位, 2 是质数)
 * 13 -> 1101 (3 个计算置位, 3 是质数)
 * 14 -> 1110 (3 个计算置位, 3 是质数)
 * 15 -> 1111 (4 个计算置位, 4 不是质数)
 * 共计 5 个计算置位为质数的数字。
 *
 *
 * 提示：
 *
 * 1 <= left <= right <= 106
 * 0 <= right - left <= 104
 */
public class _762_PrimeNumberOfSetBitsInBinaryRepresentation {

    /**
     * 先计算二进制1的个数，再判断是不是素数
     * 实际上注意到 right <=  10^6 < 2^20，因此二进制中 1 的个数不会超过 19，而不超过 19 的质数只有
     *  2, 3, 5, 7, 11, 13, 17, 19
     * 因此我们完全可以使用一个set来保存好这些素数，简化判断素数步骤
     * 当然，更简单的是 我们可以用一个二进制数 mask=665772=b10100010100010101100来存储这些质数，
     * 其中 mask 二进制的从低到高的第 i 位为 1 表示 i 是质数，为 0 表示 i 不是质数。
     *
     * 设整数 x 的二进制中 1 的个数为 c，若mask 按位与 2^c 不为 0，则说明 c 是一个质数。
     *          ans = 0
     *          for (int x = left; x <= right; ++x) {
     *              c = Integer.bitCount(x)
     *              // c 是素数，mask中从右往左第c位是1
     *             if (((1 << c) & 665772) != 0) {
     *                 ++ans;
     *             }
     *         }
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/prime-number-of-set-bits-in-binary-representation/solution/er-jin-zhi-biao-shi-zhong-zhi-shu-ge-ji-jy35g/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param left
     * @param right
     * @return
     */
    public int countPrimeSetBits(int left, int right) {
        int ans = 0;
        for (int i = left; i <= right; i++) {
            // 得到二进制1的个数
            int count = countBitOnes(i);
            // 判断是否是素数
            if (isPrime(count)) {
                ans++;
            }
        }
        return ans;
    }

    /**
     * 计算n的二进制表示中1的个数
     *  == Integer.bitCount(n)
     *
     *  时间复杂度 O(k) k 是 int 的二进制位数 = 32
     * @param n
     * @return
     */
    private int countBitOnes(int n) {
        int ans = 0;
        while (n > 0) {
            // n & n - 1 会消去n的二进制中最后一个1，比如 6 & (6-1) 0110 会变成 0100 = 4
            n = n & (n - 1);
            ans++;
        }
        return ans;
    }

    /**
     * 分组法求解 n 的二进制中1的个数
     * 时间复杂度：O(logk)
     * 不想深入，可以当成模板背过（写法非常固定），但通常如果不是写底层框架，你几乎不会遇到需要一个O(logk) 解法的情况。
     * 而且这个做法的最大作用，不是处理 int，而是处理更大位数的情况，在长度只有 32 位的 int 的情况下，
     * 该做法不一定就比循环要快（该做法会产生多个的中间结果，导致赋值发生多次，而且由于指令之间存在对 n 数值依赖，
     * 可能不会被优化为并行指令），这个道理和对于排序元素少的情况下，我们会选择「选择排序」而不是「归并排序」是一样的。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode-cn.com/problems/er-jin-zhi-zhong-1de-ge-shu-lcof/solution/gong-shui-san-xie-yi-ti-si-jie-wei-shu-j-g9w6/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @return
     */
    private int hammingWeight(int n) {
        n = (n & 0x55555555) + ((n >>> 1)  & 0x55555555);
        n = (n & 0x33333333) + ((n >>> 2)  & 0x33333333);
        n = (n & 0x0f0f0f0f) + ((n >>> 4)  & 0x0f0f0f0f);
        n = (n & 0x00ff00ff) + ((n >>> 8)  & 0x00ff00ff);
        n = (n & 0x0000ffff) + ((n >>> 16) & 0x0000ffff);
        return n;
    }

    /**
     * 判断正整数n是否是素数
     * @param n
     * @return
     */
    private boolean isPrime(int n) {
        // 1 不是素数
        if (n < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        _762_PrimeNumberOfSetBitsInBinaryRepresentation obj = new _762_PrimeNumberOfSetBitsInBinaryRepresentation();
        obj.countPrimeSetBits(6, 10);
    }
}
