package com.daily;

/**
 * @author wangwei
 * @date 2023/1/15 15:25
 * @description: _2293_MinMaxGame
 *
 * 2293. 极大极小游戏
 * 给你一个下标从 0 开始的整数数组 nums ，其长度是 2 的幂。
 *
 * 对 nums 执行下述算法：
 *
 * 设 n 等于 nums 的长度，如果 n == 1 ，终止 算法过程。否则，创建 一个新的整数数组 newNums ，新数组长度为 n / 2 ，下标从 0 开始。
 * 对于满足 0 <= i < n / 2 的每个 偶数 下标 i ，将 newNums[i] 赋值 为 min(nums[2 * i], nums[2 * i + 1]) 。
 * 对于满足 0 <= i < n / 2 的每个 奇数 下标 i ，将 newNums[i] 赋值 为 max(nums[2 * i], nums[2 * i + 1]) 。
 * 用 newNums 替换 nums 。
 * 从步骤 1 开始 重复 整个过程。
 * 执行算法后，返回 nums 中剩下的那个数字。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：nums = [1,3,5,2,4,8,2,2]
 * 输出：1
 * 解释：重复执行算法会得到下述数组。
 * 第一轮：nums = [1,5,4,2]
 * 第二轮：nums = [1,4]
 * 第三轮：nums = [1]
 * 1 是最后剩下的那个数字，返回 1 。
 * 示例 2：
 *
 * 输入：nums = [3]
 * 输出：3
 * 解释：3 就是最后剩下的数字，返回 3 。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 1024
 * 1 <= nums[i] <= 109
 * nums.length 是 2 的幂
 * 通过次数19,348提交次数26,659
 */
public class _2293_MinMaxGame {

    /**
     * 简单模拟 + 原地修改
     *
     * 若数组 nums 的长度 n 等于 1，直接返回 nums[0] 作为答案。
     * 否则，按照题意求出一个长度为 n/2 的 newNums 数组，按要求赋值，重复此过程直到长度为1。
     *
     * 注意到在顺序遍历的情况下，newNumbers 只使用 nums 前一半空间，
     * 因此newNums[i] 的计算结果可以直接存储到 nums[i] 中。
     * 这是因为 nums[i] 早在计算 newNums[i/2] 时就已经被使用，而且它在未来一定不会再被使用。
     * 有一个特例是 i=0，但此时可以原地修改的原因是很显然的。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/min-max-game/solution/ji-da-ji-xiao-you-xi-by-leetcode-solutio-ucpt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int minMaxGame(int[] nums) {
        // 原地修改nums
        int n = nums.length;
        // 直到nums长度为1结束
        while (n > 1) {
            // 只占用前n/2个元素
            for (int i = 0; i < n / 2; ++i) {
                // 按规则取值
                if ((i & 1) == 0) {
                    nums[i] = Math.min(nums[2 * i], nums[2 * i + 1]);
                } else {
                    nums[i] = Math.max(nums[2 * i], nums[2 * i + 1]);
                }
            }
            // 更新n为n/2
            n /= 2;
        }
        // 返回最后剩下的元素
        return nums[0];
    }
}
