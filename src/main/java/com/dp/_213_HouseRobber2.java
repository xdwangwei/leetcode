package com.dp;

/**
 * @author wangwei
 * 2020/7/24 20:20
 *
 * 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。这个地方所有的房屋都围成一圈，这意味着第一个房屋和最后一个房屋是紧挨着的。同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 *
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，能够偷窃到的最高金额。
 *
 * 示例 1:
 *
 * 输入: [2,3,2]
 * 输出: 3
 * 解释: 你不能先偷窃 1 号房屋（金额 = 2），然后偷窃 3 号房屋（金额 = 2）, 因为他们是相邻的。
 * 示例 2:
 *
 * 输入: [1,2,3,1]
 * 输出: 4
 * 解释: 你可以先偷窃 1 号房屋（金额 = 1），然后偷窃 3 号房屋（金额 = 3）。
 *      偷窃到的最高金额 = 1 + 3 = 4 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/house-robber-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _213_HouseRobber2 {

    /**
     * 这道题目和第一道_198_描述基本一样，强盗依然不能抢劫相邻的房子，
     * 输入依然是一个数组，但是告诉你这些房子不是一排，而是围成了一个圈。
     *
     * 首先，首尾房间不能同时被抢，那么只可能有三种不同情况：
     *      要么都不被抢；要么第一间房子被抢最后一间不抢；要么最后一间房子被抢第一间不抢。
     *
     * 只要比较情况二和情况三就行了，因为这两种情况对于房子的选择余地比情况一大呀，
     *      房子里的钱数都是非负数，所以选择余地大，最优决策结果肯定不会小。
     *
     * 如果偷窃了第一间房屋，则不能偷窃最后一间房屋，因此偷窃房屋的范围是第一间房屋到最后第二间房屋；
     * 如果偷窃了最后一间房屋，则不能偷窃第一间房屋，因此偷窃房屋的范围是第二间房屋到最后一间房屋。
     *
     * 假设数组 nums 的长度为 n。如果不偷窃最后一间房屋，则偷窃房屋的下标范围是 [0, n-2]；
     * 如果不偷窃第一间房屋，则偷窃房屋的下标范围是 [1, n-1]。
     * 在确定偷窃房屋的下标范围之后，即可用第 198 题的方法解决。
     * 对于两段下标范围分别计算可以偷窃到的最高总金额，其中的最大值即为在 n 间房屋中可以偷窃到的最高总金额。
     *
     * 假设偷窃房屋的下标范围是 [start,end]，用 dp[i] 表示在下标范围[i,end] 内可以偷窃到的最高总金额，那么就有如下的状态转移方程：
     * dp[i] = max(dp[i+2]+nums[i],dp[i+1])
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/house-robber-ii/solution/da-jia-jie-she-ii-by-leetcode-solution-bwja/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];
        // 不抢最后一个房子或不抢第一个房子
        else return Math.max(dp(nums, 0, n - 2), dp(nums, 1, n - 1));
    }

    /**
     * 动态规划
     * 每间房屋，可抢可不抢
     * @param nums
     * @param start
     * @param end
     * @return
     */
    private int dp(int[] nums, int start, int end) {
        int[] dp = new int[end + 1 + 2];
        for (int i = end; i >= start; --i) {
            dp[i] = Math.max(dp[i + 1], nums[i] + dp[i + 2]);
        }
        return dp[start];
    }

    /**
     * 动态规划状态压缩
     * @param nums
     * @param start
     * @param end
     * @return
     */
    private int dp1(int[] nums, int start, int end) {
        int dp_i = 0, dp_i_1 = 0, dp_i_2 = 0;
        for (int i = end; i >= start; --i) {
            // 抢劫这家就得去下下家，或者不抢劫这家就直接去下一家
            // 注意从后往前遍历
            dp_i = Math.max(dp_i_1, nums[i] + dp_i_2);
            dp_i_2 = dp_i_1;
            dp_i_1 = dp_i;
        }
        return dp_i;
    }
}
