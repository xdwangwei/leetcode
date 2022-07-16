package com.dp;

/**
 * @author wangwei
 * @version 1.0.0
 * @date 2021/9/20 18:36
 * @description: TODO 贪心+前缀和+二分 / 贪心+树状数组 ， 看不太明白
 *
 * 673. 最长递增子序列的个数
 * 给定一个未排序的整数数组，找到最长递增子序列的个数。
 *
 * 示例 1:
 *
 * 输入: [1,3,5,4,7]
 * 输出: 2
 * 解释: 有两个最长递增子序列，分别是 [1, 3, 4, 7] 和[1, 3, 5, 7]。
 * 示例 2:
 *
 * 输入: [2,2,2,2,2]
 * 输出: 5
 * 解释: 最长递增子序列的长度是1，并且存在5个子序列的长度为1，因此输出5。
 * 注意: 给定的数组长度不超过 2000 并且结果一定是32位有符号整数。
 */
public class _673_NumberOfLongestIncreasingSequences {

    /**
     * LIS -- 最长递增子序列
     * 动态规划：
     * dp_len[i] -- 以 nums[i] 结尾的 LIS 的长度
     * dp_cnt[i] -- 以 nums[i] 结尾的 LIS 的个数
     * 由于每个数都能独自一个成为子序列：
     * 所以初始：dp_len[i] = dp_cnt[i] = 1
     * @param nums
     * @return
     */
    public int findNumberOfLIS(int[] nums) {
        int n = nums.length;
        int[] dp_len = new int[n];
        int[] dp_cnt = new int[n];
        int maxLen = 1, cnt = 0;
        // 以 nums[i] 结尾的 LIS, 在它之前的dp_len 和 dp_cnt 已计算过
        for (int i = 0; i < n; i++) {
            dp_len[i] = 1;
            dp_cnt[i] = 1;
            // 枚举区间 [0, i) 的所有数 nums[j]，
            for (int j = 0; j < i; j++) {
                // 如果满足 nums[j] < nums[i]，说明 nums[i] 可以接在 nums[j] 后面形成上升子序列，
                if (nums[j] < nums[i]) {
                    // 选择性更新 dp[i]
                    // 如果能变更长，那么 LIS 就变为 以 nums[i] 结尾
                    if (dp_len[j] + 1 > dp_len[i]) {
                        dp_len[i] = dp_len[j] + 1;
                        // 因为实在之前的基础上变长，所以 cnt 没有变化
                        dp_cnt[i] = dp_cnt[j];
                    // 存在另一条路径到达i，这两个 IS 长度一样，那么 cnt 就是这二者之和
                    // 比如 [1,3,5,4,7] 135到7,134到7，当j到元素4的时候，就是一条并列路径
                    } else if (dp_len[j] + 1 == dp_len[i]) {
                        dp_cnt[i] += dp_cnt[j];
                    }
                }
            }
            // 以当前num[i]结束的串是目前最长的
            if (maxLen < dp_len[i]) {
                // 更新maxLen，cnt就是当前cnt
                maxLen = dp_len[i];
                cnt = dp_cnt[i];
            // 当前LIS和之前某个LIS是并列关系
            } else if (maxLen == dp_len[i]) {
                // 那么maxLen不用更新，因为一样长，但是 cnt 就是多条路径的叠加
                cnt += dp_cnt[i];
            }
        }
        return cnt;
    }
}
