package com.array;

import java.util.Arrays;
import java.util.BitSet;

/**
 * @author wangwei
 * 2020/8/30 8:57
 *
 * 统计所有小于非负整数 n 的质数的数量。
 *
 * 示例:
 *
 * 输入: 10
 * 输出: 4
 * 解释: 小于 10 的质数一共有 4 个, 它们是 2, 3, 5, 7 。
 */
public class _204_CountPrimes {

    /**
     * 逐个判断，超出时间限制
     * @param n
     * @return
     */
    public int countPrimes(int n) {
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 排除法
     *  2 是一个素数，那么 2 × 2 = 4, 3 × 2 = 6, 4 × 2 = 8... 都不可能是素数了。
     *  3 也是素数，那么 3 × 2 = 6, 3 × 3 = 9, 3 × 4 = 12... 也都不可能是素数了
     *
     */
    public int countPrimes2(int n) {
        boolean[] isPrime = new boolean[n];
        // 假设都是素数,2,3,5,7进入排除
        Arrays.fill(isPrime, true);
        int count = 0;
        // 由于因子的对称性，其中的 for 循环只需要遍历 [2,sqrt(n)] 就够了
        // for (int i = 2; i < n; i++) {
        for (int i = 2; i < Math.sqrt(n); i++) {
            // 2 是一个素数，那么 2 × 2 = 4, 3 × 2 = 6, 4 × 2 = 8... 都不可能是素数了。
            if (isPrime[i]) {
                // 把 i 的整数倍都标记为 false，仍然存在计算冗余。
                // 比如 n = 25，i = 4 时算法会标记 4 × 2 = 8，4 × 3 = 12 等等数字，
                // 但是这两个数字已经被 i = 2 和 i = 3 的 2 × 4 和 3 × 4 标记了。
                // 我们可以稍微优化一下，让 j 从 i 的平方开始遍历，而不是从 2 * i 开始：
                // for (int j = 2 * i; j < n; j += i) {
                for (int j = i * i; j < n; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        // 再判断还有几个素数，从2开始
        for (int i = 2; i < n; i++) {
            if (isPrime[i]) count++;
        }
        return count;
    }

    /**
     * 使用布尔数组标记一个数是否为质数时，每个值都占用了一个字节（Byte）。
     * 但是，我们仅需要两个不同的值来表示是否为质数即可。
     * 即一个比特（bit）来表示即可（0、1）。
     *
     * BitSet
     *
     * 作者：magicalchao
     * 链接：https://leetcode-cn.com/problems/count-primes/solution/ji-shu-zhi-shu-bao-li-fa-ji-you-hua-shai-fa-ji-you/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int countPrimes3(int n) {
        // 2是质数，但题目要统计的是小于n的
        if (n <= 2) return 0;
        // 初始化全为0，代表全是素数
        BitSet isPrime = new BitSet(n);
        // 由于因子的对称性，其中的 for 循环只需要遍历 [2,sqrt(n)] 就够了
        // for (int i = 2; i < n; i++) {
        for (int i = 2; i < Math.sqrt(n); i++) {
            // 2 是一个素数，那么 2 × 2 = 4, 3 × 2 = 6, 4 × 2 = 8... 都不可能是素数了。
            if (!isPrime.get(i)) {
                // 把 i 的整数倍都标记为 false，仍然存在计算冗余。
                // 比如 n = 25，i = 4 时算法会标记 4 × 2 = 8，4 × 3 = 12 等等数字，
                // 但是这两个数字已经被 i = 2 和 i = 3 的 2 × 4 和 3 × 4 标记了。
                // 我们可以稍微优化一下，让 j 从 i 的平方开始遍历，而不是从 2 * i 开始：
                // for (int j = 2 * i; j < n; j += i) {
                for (int j = i * i; j < n; j += i) {
                    isPrime.set(j);
                }
            }
        }
        // isPrime.cardinality()统计位数组中1的个数，我们从2开始算，所以把0，1也要排除
        return n - isPrime.cardinality() - 2;
    }

    /**
     * 判断一个数是否是素数
     * @param n
     * @return
     */
    boolean isPrime(int n) {
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        _204_CountPrimes countPrimes = new _204_CountPrimes();
        countPrimes.countPrimes(10);
    }

}
