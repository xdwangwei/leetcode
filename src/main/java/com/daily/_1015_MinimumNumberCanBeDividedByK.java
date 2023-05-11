package com.daily;

/**
 * @author wangwei
 * @date 2023/5/10 20:36
 * @description: _1015_MinimumNumberCanBeDividedByK
 *
 * 1015. 可被 K 整除的最小整数
 * 给定正整数 k ，你需要找出可以被 k 整除的、仅包含数字 1 的最 小 正整数 n 的长度。
 *
 * 返回 n 的长度。如果不存在这样的 n ，就返回-1。
 *
 * 注意： n 不符合 64 位带符号整数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：k = 1
 * 输出：1
 * 解释：最小的答案是 n = 1，其长度为 1。
 * 示例 2：
 *
 * 输入：k = 2
 * 输出：-1
 * 解释：不存在可被 2 整除的正整数 n 。
 * 示例 3：
 *
 * 输入：k = 3
 * 输出：3
 * 解释：最小的答案是 n = 111，其长度为 3。
 *
 *
 * 提示：
 *
 * 1 <= k <= 105
 * 通过次数20,638提交次数45,691
 */
public class _1015_MinimumNumberCanBeDividedByK {

    /**
     * 方法一：枚举
     *
     * 我们注意到，正整数 n 初始值为 1，每次乘以 10 后再加 1，即 n=n×10+1，
     *
     * 而 (n×10+1)mod k = ((n mod k)×10+1) mod k，
     *
     * 因此我们可以通过计算 n mod k 来判断 n 是否能被 k 整除。
     *
     * 我们从 n=1 开始，每次计算 n mod k（初始时 1 mod k），直到 n mod k = 0，
     * 此时我们找到了最小正整数，其长度即为 n 的位数。
     * 否则，我们更新 n = (n×10+1) mod k。
     *
     * 如果循环 k 次后，仍然没有找到 n mod k = 0，则说明不存在这样的 n，返回 −1。
     *
     * 为什么是循环 k 次
     *
     * 每次都是 mod k，最多有 k 个不同的余数，即 0,1,2,...k-1，所以循环找 k 次，如果没找到，说明前面有余数重复出现，会进入循环节，一直找下去也不会找到。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/smallest-integer-divisible-by-k/solution/python3javacgotypescript-yi-ti-yi-jie-sh-8ox9/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param k
     * @return
     */
    public int smallestRepunitDivByK(int k) {
        // n 只由1组成，不可能为 2 或 5 的倍数
        if (k % 2 == 0 || k % 5 == 0) {
            return -1;
        }
        // n = n * 10 + 1
        // 用计算 n % k 代替 n
        // 初始化
        int x = 1 % k;
        // i 代表位数
        for (int i = 1; i <= k; i++) { // 一定有解
            // n % k 的结果在 0,k-1 ，最多循环 k 次
            if (x == 0) {
                return i;
            }
            // n = n * 10 + 1，(n × 10 + 1) % k = ((n % k) × 10 + 1) % k
            x = (x * 10 + 1) % k;
        }
        return -1;
    }
}
