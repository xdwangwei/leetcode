package com.daily;

/**
 * @author wangwei
 * @date 2023/1/7 21:04
 * @description: _1658_MinimumOperationsToReduceXToZero
 * 1658. 将 x 减到 0 的最小操作数
 * 给你一个整数数组 nums 和一个整数 x 。每一次操作时，你应当移除数组 nums 最左边或最右边的元素，然后从 x 中减去该元素的值。请注意，需要 修改 数组以供接下来的操作使用。
 *
 * 如果可以将 x 恰好 减到 0 ，返回 最小操作数 ；否则，返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,1,4,2,3], x = 5
 * 输出：2
 * 解释：最佳解决方案是移除后两个元素，将 x 减到 0 。
 * 示例 2：
 *
 * 输入：nums = [5,6,7,8,9], x = 4
 * 输出：-1
 * 示例 3：
 *
 * 输入：nums = [3,2,20,1,1,3], x = 10
 * 输出：5
 * 解释：最佳解决方案是移除后三个元素和前两个元素（总共 5 次操作），将 x 减到 0 。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^4
 * 1 <= x <= 10^9
 * 通过次数28,691提交次数76,400
 */
public class _1658_MinimumOperationsToReduceXToZero {

    /**
     * 逆向思维 + 滑动窗口
     *
     * 我们可以将问题转换为求中间连续子数组的最大长度，使得子数组的和为 sum(nums)−x，其两边就是要移除的元素
     * 为了让操作次数最少，我们希望这个和为 sum(nums)−x 的子数组尽可能长
     *
     * 问题转换为：
     * 在nums中寻找和为 sum(nums)-x 的 最长的 子数组，假设其长度为 k，那么操作次数为 nums.length - k
     *
     * 由于 nums 中元素全部为正整数，其累加和具有单调递增性，采用滑动窗口
     * [left, right) 限制窗口边界，curSum 表示窗口内元素累加和，maxLen 表达满足要求的最大窗口长度
     * 当 curSum < target 时，增大右边界
     * 当 curSum > target 时，增大左边界
     * 当 curSum == target 时，若当前窗口长度 > maxLen，更新 maxLen 为当前窗口长度
     * @param nums
     * @param x
     * @return
     */
    public int minOperations(int[] nums, int x) {
        // 计算 nums 元素和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        // ！特殊情况，需要移除nums所有元素，
        // 若不提前判断，则需要修改最后返回语句为 return maxLen>0 ? n-maxLen : curSum==target ? n : -1;
        if (sum == x) {
            return nums.length;
        }
        // 子数组目标元素和
        sum -= x;
        // 滑动窗口
        int n = nums.length, left = 0, right = 0;
        // 窗口元素和；满足要求的最大窗口大小
        int curSum = 0, maxLen = 0;
        while (right < n) {
            // 加入当前元素并增大右边界
            curSum += nums[right++];
            // 若元素和超出目标，则缩小窗口，即增大左边界
            while (curSum > sum && left < right) {
                curSum -= nums[left++];
            }
            // 若元素和刚好等于目标，若窗口大小超过maxLen，更新maxLen为当前窗口长度
            if (curSum == sum && right - left > maxLen) {
                maxLen = right - left;
            }
        }
        // 若maxLen为0，说明不存在符合要求的子数组或窗口包括整个数组（之前已单独判断）
        // 否则，窗口两边就是要移除的元素，操作次数为元素个数，即 n - maxLen
        return maxLen > 0 ? n - maxLen : -1;
    }
}
