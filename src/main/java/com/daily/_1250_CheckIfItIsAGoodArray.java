package com.daily;

/**
 * @author wangwei
 * @date 2023/2/15 17:02
 * @description: _1250_CheckIfItIsAGoodArray
 *
 * 1250. 检查「好数组」
 * 给你一个正整数数组 nums，你需要从中任选一些子集，然后将子集中每一个数乘以一个 任意整数，并求出他们的和。
 *
 * 假如该和结果为 1，那么原数组就是一个「好数组」，则返回 True；否则请返回 False。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [12,5,7,23]
 * 输出：true
 * 解释：挑选数字 5 和 7。
 * 5*3 + 7*(-2) = 1
 * 示例 2：
 *
 * 输入：nums = [29,6,10]
 * 输出：true
 * 解释：挑选数字 29, 6 和 10。
 * 29*1 + 6*(-3) + 10*(-1) = 1
 * 示例 3：
 *
 * 输入：nums = [3,6]
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 通过次数15,035提交次数21,605
 */
public class _1250_CheckIfItIsAGoodArray {

    /**
     * 方法：数学（裴蜀定理）
     *
     * 我们先可以考虑选取两个数的情况，
     * 若选取的数是 a 和 b，那么根据题目的要求，我们需要满足 a×x+b×y=1，其中 x 和 y 是任意整数。
     *
     * 根据裴蜀定理，可以得知，如果 a 和 b 互质，那么上述等式一定有解。
     *
     * 实际上，裴蜀定理也可以推广到多个数的情况，
     * 即如果 a1,a2,a3,...an互质，那么 a1*x1 + a2*x2 +...+ an*xn = 1 一定有解，
     * 其中 x1、x2、x3、...、xn 是任意整数。
     *
     * 因此，我们只需要判断在数组 nums 中是否存在 i 个互质的数即可。
     * 两个数互质的充要条件是它们的最大公约数为 1。
     * 如果数组 nums 存在 i 个互质的数，那么数组 nums 中的所有数的最大公约数也为 1。
     *
     * 所以我们将题目转化为：判断数组 nums 中的所有数的最大公约数是否为 1 即可。
     * 遍历数组 nums，求出数组 nums 中的所有数的最大公约数即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/solution/python3javacgo-yi-ti-yi-jie-shu-xue-pei-3f4da/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public boolean isGoodArray(int[] nums) {
        // 计算 num 所有元素最大公约数，初始为 0
        int g = 0;
        for (int num : nums) {
            g = gcd(g, num);
        }
        // 根据裴蜀定理，g必须为1
        return g == 1;
    }

    /**
     * 计算 a、b最大公约数，(a, b) --> (b, a%b) ，当 a%b == 0时，直接返回b，
     * 即 b==0，直接返回 a
     * @param a
     * @param b
     * @return
     */
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
