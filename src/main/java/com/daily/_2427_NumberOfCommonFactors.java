package com.daily;

/**
 * @author wangwei
 * @date 2023/4/5 10:07
 * @description: _2427_NumberOfCommonFactors
 *
 * 2427. 公因子的数目
 * 给你两个正整数 a 和 b ，返回 a 和 b 的 公 因子的数目。
 *
 * 如果 x 可以同时整除 a 和 b ，则认为 x 是 a 和 b 的一个 公因子 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：a = 12, b = 6
 * 输出：4
 * 解释：12 和 6 的公因子是 1、2、3、6 。
 * 示例 2：
 *
 * 输入：a = 25, b = 30
 * 输出：2
 * 解释：25 和 30 的公因子是 1、5 。
 *
 *
 * 提示：
 *
 * 1 <= a, b <= 1000
 * 通过次数15,358提交次数18,657
 */
public class _2427_NumberOfCommonFactors {

    /**
     * 方法一：枚举到较小值
     * 思路与算法
     *
     * 由于 a 和 b 的公因子一定不会超过 a 和 b，因此我们只需要在 [1,min(a,b)] 中枚举 x，并判断 x 是否为公因子即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/number-of-common-factors/solution/gong-yin-zi-de-shu-mu-by-leetcode-soluti-u9sl/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param a
     * @param b
     * @return
     */
    public int commonFactors(int a, int b) {
        int max = Math.min(a, b);
        int ans = 0;
        // 在 [1,min(a,b)] 中枚举 x，并判断 x 是否为公因子即可。
        for (int i = 1; i <= max; ++i) {
            if (a % i == 0 && b % i == 0) {
                ans++;
            }
        }
        return ans;
    }

    /**
     * 方法二：枚举到最大公约数
     * 思路与算法
     *
     * x 是 a 和 b 的公因子，当且仅当 x 是 a 和 b 的最大公约数的因子。
     *
     * 因此，我们可以首先求出 a 和 b 的最大公约数 c = gcd(a,b)，再枚举 c 的因子个数。
     *
     * 我们可以使用类似方法一种的遍历，对于 [1,c] 中的整数依次进行枚举，时间复杂度为 O(c)。
     *
     * 我们也可以进行一些优化，因子一定是成对出现的：如果 x 是c 的因子，那么 c/x 一定也是 c 的因子。
     *
     * 因此，我们可以仅对 1,⌊sqrt(c)] 中的整数依此进行枚举，如果枚举到 x 是 c 的因子，并且 x*x != c），那么答案额外增加 1。
     *
     * 这样一来，时间复杂度可以降低至 O(c)。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/number-of-common-factors/solution/gong-yin-zi-de-shu-mu-by-leetcode-soluti-u9sl/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param a
     * @param b
     * @return
     */
    public int commonFactors2(int a, int b) {
        // 最大公约数
        int g = gcd(a, b);
        int ans = 0;
        // 枚举 g 的因子，1 到 sqrt(c)
        for (int i = 1; i * i <= g; ++i) {
            // i 是 g 的因子
            if (g % i == 0) {
                ans++;
                // 且 i != sqrt(g)，那么 g / i 也是 g 的因子
                if (i * i < g) {
                    ans++;
                }
            }
        }
        // 返回
        return ans;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            a = a % b;
            // b, a%b
            a ^= b;
            b ^= a;
            a ^= b;
        }
        return a;
    }
}
