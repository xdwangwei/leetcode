package com.daily;

/**
 * @author wangwei
 * @date 2023/5/29 20:45
 * @description: _2455_AverageValueOfEvenNumbersThatAreDivisibleByThree
 *
 * 2455. 可被三整除的偶数的平均值
 * 给你一个由正整数组成的整数数组 nums ，返回其中可被 3 整除的所有偶数的平均值。
 *
 * 注意：n 个元素的平均值等于 n 个元素 求和 再除以 n ，结果 向下取整 到最接近的整数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,3,6,10,12,15]
 * 输出：9
 * 解释：6 和 12 是可以被 3 整除的偶数。(6 + 12) / 2 = 9 。
 * 示例 2：
 *
 * 输入：nums = [1,2,4,7,10]
 * 输出：0
 * 解释：不存在满足题目要求的整数，所以返回 0 。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 1000
 * 通过次数10,399提交次数16,837
 */
public class _2455_AverageValueOfEvenNumbersThatAreDivisibleByThree {

    /**
     * 方法一：遍历
     * 思路与算法
     *
     * 题目给定正整数组成的整数数组 nums，返回其中可被 3 整除的所有偶数的平均值。
     *
     * 偶数要求 % 2 == 0，被 3 整除 要求 % 3 == 0，那么必须满足 % 6 == 0，等价于 6 的倍数。
     *
     * 使用 cnt、total 分别统计满足要求的数字的个数、数字的总和
     *
     * 遍历 nums 中的每一个数，判断是否除以 6 没有余数，没有余数即是 6 的倍数，累加到总和中，cnt++。
     *
     * 最后，返回求和后的平均数， k 个元素的平均值等于求和后除以 k，结果向下取整，即 total / cnt
     *
     * 如果不存在 6 的倍数，返回 0。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/average-value-of-even-numbers-that-are-divisible-by-three/solution/ke-bei-san-zheng-chu-de-ou-shu-de-ping-j-vruh/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int averageValue(int[] nums) {
        // 分别统计满足要求的数字的个数、数字的总和
        int cnt = 0, total = 0;
        // 遍历
        for (int x : nums) {
            // 必须是 6 的倍数
            if (x % 6 == 0) {
                // 个数加1
                cnt++;
                // 总和加x
                total += x;
            }
        }
        // 计算平均值，注意 cnt 不能为 0
        return cnt == 0 ? 0 : total / cnt;
    }
}
