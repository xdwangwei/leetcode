package com.daily;

/**
 * @author wangwei
 * @date 2022/11/16 10:25
 * @description: _775_GlobalAndLocalInversions
 *
 * 775. 全局倒置与局部倒置
 * 给你一个长度为 n 的整数数组 nums ，表示由范围 [0, n - 1] 内所有整数组成的一个排列。
 *
 * 全局倒置 的数目等于满足下述条件不同下标对 (i, j) 的数目：
 *
 * 0 <= i < j < n
 * nums[i] > nums[j]
 * 局部倒置 的数目等于满足下述条件的下标 i 的数目：
 *
 * 0 <= i < n - 1
 * nums[i] > nums[i + 1]
 * 当数组 nums 中 全局倒置 的数量等于 局部倒置 的数量时，返回 true ；否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,0,2]
 * 输出：true
 * 解释：有 1 个全局倒置，和 1 个局部倒置。
 * 示例 2：
 *
 * 输入：nums = [1,2,0]
 * 输出：false
 * 解释：有 2 个全局倒置，和 1 个局部倒置。
 *
 * 提示：
 *
 * n == nums.length
 * 1 <= n <= 105
 * 0 <= nums[i] < n
 * nums 中的所有整数 互不相同
 * nums 是范围 [0, n - 1] 内所有数字组成的一个排列
 */
public class _775_GlobalAndLocalInversions {

    /**
     * 方法一：维护后缀最小值
     * 思路与算法
     *
     * 一个局部倒置一定是一个全局倒置，因此要判断数组中局部倒置的数量是否与全局倒置的数量相等，只需要检查有没有非局部倒置就可以了。
     * 这里的非局部倒置指的是 nums[i]>nums[j]，其中 j >= i + 2。
     *
     * 朴素的判断方法需要两层循环，设 n 是 nums 的长度，那么该方法的时间复杂度为 O(n^2)，无法通过。
     *
     * 进一步的，对于每一个 nums[i] 判断是否存在一个 (j>=i+2) 使得 nums[i]>nums[j] 即可。
     * 这和检查 nums[i] > min(nums[i+2],…,nums[n−1]) 是否成立是一致的。
     * 因此我们维护一个变量 minSuffix=min(nums[i+2],…,nums[n−1])，
     * 倒序遍历 [0,n−3] 中的每个 i, 如有一个 i 使得 nums[i]>minSuffix 成立，返回 false，
     * 若结束遍历都没有返回 false，则返回 true。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/global-and-local-inversions/solution/quan-ju-dao-zhi-yu-ju-bu-dao-zhi-by-leet-bmjp/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public boolean isIdealPermutation(int[] nums) {
        // 最后两个数字不存在非局部倒置，初始化 rightMin
        int n = nums.length, rightMin = nums[n - 1];
        // i 从倒数第三个数字开始，往前遍历
        for (int i = n - 3; i >= 0; --i) {
            // 存在非局部倒置
            if (nums[i] > rightMin) {
                return false;
            }
            // 更新右边元素最小值，注意这里是用 nums[i + 1]，
            // 这样的话，i 下次变为 i - 1，而 rightMin代表的是 min(nums[i+1], ..nums[n-1])
            // 需要保证间隔2及以上
            rightMin = Math.min(rightMin, nums[i]);
        }
        return true;
    }


    /**
     * 方法二：归纳证明
     *
     * 思路与算法
     *
     * 方法一并没有用到题目核心条件：nums 是一个由 0∼n−1 组成的排列。但是方法一通用性更强
     *
     * 设不存在非局部倒置的排列为「理想排列」。
     *
     * 由于非局部倒置表示存在一个 j>i+1 使得 nums[i]>nums[j] 成立，若要保证无非局部倒置存在，
     *
     * 那么对于[0~n-1]范围上最小的元素 0 来说，它的下标不能够大于等于 2。所以有：
     *
     *      若 nums[0]=0，那么问题转换为 [1,n−1] 区间的一个子问题。
     *      若 nums[1]=0，那么 nums[0] 只能为 1，因为如果是大于 1 的任何元素，都将会与后面的 1 构成非局部倒置。
     *          此时，问题转换为了 [2,n−1] 区间的一个子问题。
     *
     * 根据上述讨论，
     *    对于 [0~n-1]，0、1位置上元素只能为0、1或1、0
     *    而后续 [2~n-1]，0、1（在原数组就是2、3）位置上元素只能为2、3或3、2
     *    ...
     *
     * 由此可归纳出「[0~n-1]理想排列」的充分必要条件为 对于每个元素 nums[i] 都满足 nums[i]−i ≤ 1。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/global-and-local-inversions/solution/quan-ju-dao-zhi-yu-ju-bu-dao-zhi-by-leet-bmjp/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public boolean isIdealPermutation2(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (Math.abs(nums[i] - i) > 1) {
                return false;
            }
        }
        return true;
    }
}
