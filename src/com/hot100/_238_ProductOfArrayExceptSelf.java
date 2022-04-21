package com.hot100;

/**
 * @author wangwei
 * @date 2022/4/21 9:41
 * @description: _238_ProductOfArrayExceptSelf
 *
 * 238. 除自身以外数组的乘积
 * 给你一个整数数组 nums，返回 数组 answer ，其中 answer[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积 。
 *
 * 题目数据 保证 数组 nums之中任意元素的全部前缀元素和后缀的乘积都在  32 位 整数范围内。
 *
 * 请不要使用除法，且在 O(n) 时间复杂度内完成此题。
 *
 *
 *
 * 示例 1:
 *
 * 输入: nums = [1,2,3,4]
 * 输出: [24,12,8,6]
 * 示例 2:
 *
 * 输入: nums = [-1,1,0,-3,3]
 * 输出: [0,0,9,0,0]
 *
 *
 * 提示：
 *
 * 2 <= nums.length <= 105
 * -30 <= nums[i] <= 30
 * 保证 数组 nums之中任意元素的全部前缀元素和后缀的乘积都在  32 位 整数范围内
 *
 *
 * 进阶：你可以在 O(1) 的额外空间复杂度内完成这个题目吗？（ 出于对空间复杂度分析的目的，输出数组不被视为额外空间。
 */
public class _238_ProductOfArrayExceptSelf {

    /**
     * 方法一：
     * 记录每个元素往左(不包含自己)的累乘结果 leftSum[]，记录每个元素往右的累乘(不包含自己)结果 rightSum[]
     * 那么，整个数组除过元素nums[i]以为其他所有元素的累乘结果就是 leftSum[i] * rightSum[i]
     *
     * 空间复杂度 O(N)
     * @param nums
     * @return
     */
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        // leftSum[i] 表示 nums[0] * nums[1] * ... * nums[i - 1]，即 nums中 i 往左所有元素乘积
        int[] leftSum = new int[n];
        // rightSum[i] 表示 nums[i + 1] * nums[i + 2] * ... * nums[n - 1]，即 nums中 i 往右所有元素乘积
        int[] rightSum = new int[n];
        // res[i] 表示 nums中除过 nums[i]之外所有元素乘积 = leftSum[i] * rightSum[i]
        int[] res = new int[n];
        // 初始化边界值
        leftSum[0] = 1;
        // 递推求leftSum
        for (int i = 1; i < n; ++i) {
            leftSum[i] = leftSum[i - 1] * nums[i - 1];
        }
        // 初始化边界值
        rightSum[n - 1] = 1;
        // 递推求rightSum
        for (int i = n - 2; i >= 0; --i) {
            rightSum[i] = rightSum[i + 1] * nums[i + 1];
        }
        // 求 res
        for (int i = 0; i < n; ++i) {
            res[i] = leftSum[i] * rightSum[i];
        }
        return res;
    }

    /**
     * 优化，可以看到 leftSum[i]只和leftSum[i - 1]有关， rightSum[i]只和rightSum[i + 1]有关
     * 因此可以用变量代替迭代过程
     * 在迭代推进 leftSum 的时候更新 res，(相当于保留左边累乘和)
     * 在迭代推进 rightSum 的时候再次更新 res，(在原来基础上累乘右边累乘和)
     *
     * 观察下面代码，可以看出起始可以不用leftSum这个变量，为了与上面对比好理解，这里还是用上了
     * @param nums
     * @return
     */
    public int[] productExceptSelf2(int[] nums) {
        int n = nums.length;
        // // leftSum[i] 表示 nums[0] * nums[1] * ... * nums[i - 1]，即 nums中 i 往左所有元素乘积
        // int[] leftSum = new int[n];
        // // rightSum[i] 表示 nums[i + 1] * nums[i + 2] * ... * nums[n - 1]，即 nums中 i 往右所有元素乘积
        // int[] rightSum = new int[n];
        int leftSum = 1, rightSum = 1;
        // res[i] 表示 nums中除过 nums[i]之外所有元素乘积 = leftSum[i] * rightSum[i]
        int[] res = new int[n];
        // 初始化边界值
        // leftSum[0] = 1;
        // 递推求leftSum
        // 从左往右类推leftSum的时候不会更新res[0],默认是0
        for (int i = 1; i < n; ++i) {
            leftSum *= nums[i - 1];
            res[i] = leftSum;
        }
        // 初始化边界值
        // rightSum[n - 1] = 1;
        // 递推求rightSum
        // 【注意！】从右往左递推rightSum会一直推进到i=0，因为要累乘上一次的结果，所以这里先赋值res[0]=1,这样的做法是正确的，因为并不改变正确值
        res[0] = 1;
        for (int i = n - 2; i >= 0; --i) {
            rightSum *= nums[i + 1];
            res[i] *= rightSum;
        }
        return res;
    }
}
