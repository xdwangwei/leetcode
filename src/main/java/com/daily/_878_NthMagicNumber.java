package com.daily;

/**
 * @author wangwei
 * @date 2022/11/22 11:56
 * @description: _878_NthMagicNumber
 *
 * 878. 第 N 个神奇数字
 * 一个正整数如果能被 a 或 b 整除，那么它是神奇的。
 *
 * 给定三个整数 n , a , b ，返回第 n 个神奇的数字。因为答案可能很大，所以返回答案 对 109 + 7 取模 后的值。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 1, a = 2, b = 3
 * 输出：2
 * 示例 2：
 *
 * 输入：n = 4, a = 2, b = 3
 * 输出：6
 *
 *
 * 提示：
 *
 * 1 <= n <= 109
 * 2 <= a, b <= 4 * 104
 */
public class _878_NthMagicNumber {


    /**
     * 辗转相除法求a，b的最大公约数
     * @param a
     * @param b
     * @return
     */
    public int gcb(int a, int b) {
        return b == 0 ? a : gcb(b, a % b);
    }

    /**
     * 求a，b的最小公倍数
     * @param a
     * @param b
     * @return
     */
    public int lcm(int a, int b) {
        return a * b / gcb(a, b);
    }


    /**
     * 法一：容斥原理 + 二分查找
     * 思路与算法
     *
     * 题目给出三个数字 n，a，b，满足 1 ≤ n ≤ 10^9, 2 ≤a,b ≤ 10^4
     * 并给出「神奇数字」的定义：若一个正整数能被 a 和 b 整除，那么它就是「神奇」的。
     *
     * 现在需要求出对于给定 a 和 bb的第 n 个「神奇数字」。
     *
     * 设 f(x) 表示为小于等于 x 的「神奇数字」个数，
     * 因为小于等于 x 中能被 a 整除的数的个数为 ⌊x/a⌋，
     * 小于等于 x 中能被 b 整除的个数为 ⌊a/b⌋，
     * 小于等于 x 中同时能被 a 和 b 整除的个数为 ⌊x/c⌋，其中 c 为 a 和 b 的最小公倍数，
     *
     * 所以 f(x) 的表达式为：
     *      f(x) = ⌊x/a⌋ + ⌊x/b⌋ - ⌊x/c⌋
     *
     * 即f(x) 是一个随着 x 递增单调不减函数。那么我们可以通过「二分查找」来进行查找第 n 个「神奇数字」。
     *
     * 假如 a < b
     * 那么二分搜索的下界就是 a， 二分搜索的上界就是 a*n (如果 a*n <= b,那么 a*n就是答案，否则，a*n之前肯定包括n-1个a，至少一个b，最后答案肯定小于a*n)
     *
     * 因为用到了整数除法，所以在二分搜索中，如果 某一次 f(x) = n，此时不能直接返回x，需要继续缩小上界 h，
     * 假如下一次二分结束，那么 h 就是答案，因为一定是这个h得到的x满足f(x) >= n,而缩小上界（让h=x）后，得到新的f(x)<n，所以h一定是满足f(x)=n中唯一的神奇数字
     *
     * （设答案为 x，循环结束时，≤x 的神奇数字有 n 个，而 ≤x−1 的神奇数字不足 n 个。只有当 x 是一个神奇数字时，才会出现这种情况。）
     *
     * 注意最后答案对mod取模，以及使用long解决溢出
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/nth-magical-number/solution/di-n-ge-shen-qi-shu-zi-by-leetcode-solut-6vyy/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param a
     * @param b
     * @return
     */
    public int nthMagicalNumber(int n, int a, int b) {
        int mod = (int)(1e9 + 7);
        // 保证 a < b
        if (a > b) {
            return nthMagicalNumber(n, b, a);
        }
        int c = a * b / gcb(b, a);
        // 二分搜索，注意数据范围，下界是a，上界是a*n，因为是左闭右开的写法，所以h++
        // 这里计算a*n一定先强转为long，否则可能溢出
        long l = a, h = (long)a * n + 1;
        while (l < h) {
            long mid = l + (h - l) / 2;
            // <= mid 且能被mid整数的数字 <= n，缩小上界
            if (mid / a + mid / b - mid / c >= n) {
                h = mid;
            } else {
                l = mid + 1;
            }
        }
        // 二分结束的上一轮得到的mid就是答案，也就是退出时的h
        // （一定是某一轮f(mid)=n，缩小上界让h=mid，下一轮f(mid)<n增大下界然后二分结束，所以上一轮的mid（此时的h）就是答案）
        return (int)(h % mod);
    }

    public static void main(String[] args) {
        _878_NthMagicNumber obj = new _878_NthMagicNumber();
        System.out.println(obj.gcb(6, 9));
        System.out.println(obj.gcb(12, 9));
        System.out.println(6 % 9);
    }
}
