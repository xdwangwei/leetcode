package com.daily;

/**
 * @author wangwei
 * @date 2022/10/27 11:50
 * @description: _1822_SignOfTheProductOfAnArray
 *
 * 1822. 数组元素积的符号
 * 已知函数 signFunc(x) 将会根据 x 的正负返回特定值：
 *
 * 如果 x 是正数，返回 1 。
 * 如果 x 是负数，返回 -1 。
 * 如果 x 是等于 0 ，返回 0 。
 * 给你一个整数数组 nums 。令 product 为数组 nums 中所有元素值的乘积。
 *
 * 返回 signFunc(product) 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [-1,-2,-3,-4,3,2,1]
 * 输出：1
 * 解释：数组中所有值的乘积是 144 ，且 signFunc(144) = 1
 * 示例 2：
 *
 * 输入：nums = [1,5,0,2,-3]
 * 输出：0
 * 解释：数组中所有值的乘积是 0 ，且 signFunc(0) = 0
 * 示例 3：
 *
 * 输入：nums = [-1,1,-1,1,-1]
 * 输出：-1
 * 解释：数组中所有值的乘积是 -1 ，且 signFunc(-1) = -1
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 1000
 * -100 <= nums[i] <= 100
 * 通过次数50,738提交次数68,774
 */
public class _1822_SignOfTheProductOfAnArray {


    /**
     * 遍历
     *     如果数组中有一个元素 0，那么所有元素值的乘积肯定为 0，我们直接返回 0。
     *     使用 sign 记录元素值乘积的符号，1 为表示正，−1 表示为负，
     *     初始时 sign=1。
     *     遍历整个数组，如果元素为正，那么 sign 不变，否则令 sign=−sign，
     *     最后返回 sign。
     * @param nums
     * @return
     */
    public int arraySign(int[] nums) {
        int sign = 1;
        for (int num : nums) {
            if (num == 0) {
                return 0;
            }
            if (num < 0) {
                sign = -sign;
            }
        }
        return sign;
    }
}
