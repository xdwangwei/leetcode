package com.array;

/**
 * @author wangwei
 * 2021/11/29 8:59
 *
 * 给定一个整数数组  nums，求出数组从索引 i 到 j（i ≤ j）范围内元素的总和，包含 i、j 两点。
 *
 * 实现 NumArray 类：
 *
 * NumArray(int[] nums) 使用数组 nums 初始化对象
 * int sumRange(int i, int j) 返回数组 nums 从索引 i 到 j（i ≤ j）范围内元素的总和，包含 i、j 两点（也就是 sum(nums[i], nums[i + 1], ... , nums[j])）
 *  
 *
 * 示例：
 *
 * 输入：
 * ["NumArray", "sumRange", "sumRange", "sumRange"]
 * [[[-2, 0, 3, -5, 2, -1]], [0, 2], [2, 5], [0, 5]]
 * 输出：
 * [null, 1, -1, -3]
 *
 * 解释：
 * NumArray numArray = new NumArray([-2, 0, 3, -5, 2, -1]);
 * numArray.sumRange(0, 2); // return 1 ((-2) + 0 + 3)
 * numArray.sumRange(2, 5); // return -1 (3 + (-5) + 2 + (-1))
 * numArray.sumRange(0, 5); // return -3 ((-2) + 0 + 3 + (-5) + 2 + (-1))
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/range-sum-query-immutable
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _303_RangeSumQuery {

    /**
     * 前缀和
     *
     * 核心思路是 new 一个新的数组 preSum 出来，preSum[i] 记录 nums[0..i-1] 的累加和，
     * persum[0]=0 != nums[0] = presum[1]
     * 所以presum里面的索引和实际数组元素的索引差了1，要找到nums[0,i]应该返回presum[i+1]
     *
     * 看这个 preSum 数组，如果我想求索引区间 [1, 4] 内的所有元素之和，
     * 就可以通过 preSum[5] - preSum[1] 得出。
     *
     * 这样，sumRange 函数仅仅需要做一次减法运算，避免了每次进行 for 循环调用，最坏时间复杂度为常数 O(1)。
     */
    class NumArray {
        // 前缀和数组
        private int[] preSum;

        /* 输入一个数组，构造前缀和 */
        public NumArray(int[] nums) {
            // preSum[0] = 0，便于计算累加和
            preSum = new int[nums.length + 1];
            // 计算 nums 的累加和
            for (int i = 1; i < preSum.length; i++) {
                preSum[i] = preSum[i - 1] + nums[i - 1];
            }
        }

        /* 查询闭区间 [left, right] 的累加和 */
        public int sumRange(int left, int right) {
            return preSum[right + 1] - preSum[left];
        }
    }
}
