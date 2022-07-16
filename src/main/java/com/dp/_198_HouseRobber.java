package com.dp;

import java.util.Arrays;

/**
 * @author wangwei
 * 2020/7/24 19:46
 * <p>
 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 * <p>
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：[1,2,3,1]
 * 输出：4
 * 解释：偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
 *      偷窃到的最高金额 = 1 + 3 = 4 。
 * 示例 2：
 * <p>
 * 输入：[2,7,9,3,1]
 * 输出：12
 * 解释：偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
 *      偷窃到的最高金额 = 2 + 9 + 1 = 12 。
 *  
 * <p>
 * 提示：
 * <p>
 * 0 <= nums.length <= 100
 * 0 <= nums[i] <= 400
 * 通过次数156,249提交次数339,461
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/house-robber
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _198_HouseRobber {

    /**
     * 你面前房子的索引就是状态，抢和不抢就是选择。
     * dp[i] 表示从下标为 i的房子开始往后抢劫，最多能抢到的钱
     */

    /**
     * 自下而上，记忆化搜索  超时
     * @param nums
     * @return
     */
    public int rob1(int[] nums) {
        // return dp1(nums, 0);
        memo = new int[nums.length];
        Arrays.fill(memo, -1);
        return dp2(nums, 0);
    }

    private int dp1(int[] nums, int start) {
        if (start >= nums.length) return 0;
        // 不抢劫这家，就可以直接去抢劫下一家
        // 抢劫这一家就，只能去下下家抢劫
        return Math.max(dp2(nums, start + 1),
                dp2(nums, start + 2) + nums[start]);
    }
    /**
     * 带备忘录的递归
     * 上面的写法会有很多重复计算，比如盗贼可以直接去第三家抢劫，也可以抢劫完第一家再去第三家
     * 而从第三家开始抢劫能够获得的最多的钱是一定的，无需重复计算
     */
    int[] memo;
    private int dp2(int[] nums, int start) {
        if (start >= nums.length) return 0;
        // 避免重复计算
        if (memo[start] != -1) return memo[start];
        // 不抢劫这家，就可以直接去抢劫下一家
        // 抢劫这一家就，只能去下下家抢劫
        int res =  Math.max(dp1(nums, start + 1),
                dp1(nums, start + 2) + nums[start]);
        // 保存进备忘录
        memo[start] = res;
        return res;
    }

    /**
     * 将带备忘录的递归改成自底向上的动态规划
     */
    private int rob2(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n + 2];
        // 不抢劫这家，就可以直接去抢劫下一家
        // 抢劫这一家就，只能去下下家抢劫
        // 注意从后往前遍历
        for (int i = n - 1; i >= 0; --i) {
            dp[i] = Math.max(dp[i + 1], nums[i] + dp[i + 2]);
        }
        return dp[0];
    }

    /**
     * 状态压缩
     * @param nums
     * @return
     */
    private int rob21(int[] nums) {
        int n = nums.length;
        int dp_i = 0, dp_i_1 = 0, dp_i_2 = 0;
        // 不抢劫这家，就可以直接去抢劫下一家
        // 抢劫这一家就，只能去下下家抢劫
        // 注意从后往前遍历
        for (int i = n - 1; i >= 0; --i) {
            dp_i = Math.max(dp_i_1, nums[i] + dp_i_2);
            dp_i_2 = dp_i_1;
            dp_i_1 = dp_i;
        }
        return dp_i;
    }
}
