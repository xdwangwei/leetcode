package com.daily;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/2/24 15:13
 * @description: _2357_MakeArrayZeroBySubtractingEqualAmounts
 *
 * 2357. 使数组中所有元素都等于零
 * 给你一个非负整数数组 nums 。在一步操作中，你必须：
 *
 * 选出一个正整数 x ，x 需要小于或等于 nums 中 最小 的 非零 元素。
 * nums 中的每个正整数都减去 x。
 * 返回使 nums 中所有元素都等于 0 需要的 最少 操作数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,5,0,3,5]
 * 输出：3
 * 解释：
 * 第一步操作：选出 x = 1 ，之后 nums = [0,4,0,2,4] 。
 * 第二步操作：选出 x = 2 ，之后 nums = [0,2,0,0,2] 。
 * 第三步操作：选出 x = 2 ，之后 nums = [0,0,0,0,0] 。
 * 示例 2：
 *
 * 输入：nums = [0]
 * 输出：0
 * 解释：nums 中的每个元素都已经是 0 ，所以不需要执行任何操作。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 100
 * 通过次数30,100提交次数39,101
 */
public class _2357_MakeArrayZeroBySubtractingEqualAmounts {


    /**
     * 方法一：排序 + 模拟
     * 这道题要求计算将非负整数数组 nums 中的所有元素减少到 0 的最少操作数。
     * 用 m 表示数组 nums 中的最小非零元素，则可以选择不超过 m 的正整数 x，将数组中的每个非零元素减 x。
     * 为了使操作数最少，应选择 x=m，理由如下。
     *
     * 当选择 x=m 时，经过一次操作之后，数组中的所有元素 m 都变成 0，且其余的所有非零元素都减少 m。
     *
     * 当选择 x<m 时，经过一次操作之后，数组中的所有元素 m 在减少 x 之后仍大于 0，为了使数组中的最小非零元素变成 0，
     * 至少还需要一次操作，因此至少需要两次操作使数组中的所有元素 m 都变成 0，且其余的所有非零元素都减少 m。
     *
     * 由于当 x<m 时使元素 m 变成 0 的操作数大于当 x=m 时使元素 0 的操作数，
     * 且两种方案中，使元素 0 之后，剩余的最小非零元素相同（所有非零元素都减少 m），因此只有当 x=m 时才能使操作数最少。
     *
     * 根据上述分析，应使用贪心策略使操作数最少，
     * 贪心策略为每次选择数组中的最小非零元素作为 x，将数组中的每个非零元素减 可以根据贪心策略模拟操作过程，计算最少操作数。
     *
     * 【方法一】
     * 首先将数组 nums 按升序排序，然后从左到右遍历排序后的数组 nums。
     * 对于每个遍历到的非零元素，该元素是数组中的最小非零元素，将该元素记为 x，将数组中的每个非零元素都减 x，将操作数加 1。
     * 遍历结束之后，即可得到最少操作数。
     * 【方法二】
     * 我们观察到，每一次操作，都可以把数组 nums 中相同且非零的元素减少到 0，
     * 因此，我们只需要统计数组 nums 中有多少个不同的非零元素，即为最少操作数。
     * 统计不同的非零元素，可以使用哈希表或数组来实现。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/make-array-zero-by-subtracting-equal-amounts/solution/shi-shu-zu-zhong-suo-you-yuan-su-du-deng-ix12/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/make-array-zero-by-subtracting-equal-amounts/solution/python3javacgotypescript-yi-ti-yi-jie-ha-whcd/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param nums
     * @return
     */
    /**
     * 方法一：排序 + 模拟
     * @param nums
     * @return
     */
    public int minimumOperations(int[] nums) {
        // 排序
        Arrays.sort(nums);
        int n = nums.length;
        int ans = 0;
        // 每次选择最小的非零元素x
        for (int i = 0; i < n; ++i) {
            if (nums[i] > 0) {
                // 操作次数增加
                ans++;
                // 让剩下所有元素减去x
                int x = nums[i];
                for (int j = i; j < n; ++j) {
                    nums[j] -= x;
                }
            }
        }
        return ans;
    }

    /**
     * 方法二：哈希表
     * 每一次操作，都可以把数组 nums 中相同且非零的元素减少到 0，
     * 因此，我们只需要统计数组 nums 中有多少个不同的非零元素，即为最少操作数。
     * 统计不同的非零元素，可以使用哈希表或数组来实现。
     * @param nums
     * @return
     */
    public int minimumOperations2(int[] nums) {
        Set<Integer> set = new HashSet<>();
        // 统计不同非零元素个数
        for (int x: nums) {
            if (x != 0) {
                set.add(x);
            }
        }
        // 返回
        return set.size();
    }
}
