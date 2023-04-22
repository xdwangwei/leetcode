package com.daily;

/**
 * @author wangwei
 * @date 2023/4/22 19:52
 * @description: _1027_LongestArithmeticSubsequence
 *
 * 1027. 最长等差数列
 * 给你一个整数数组 nums，返回 nums 中最长等差子序列的长度。
 *
 * 回想一下，nums 的子序列是一个列表 nums[i1], nums[i2], ..., nums[ik] ，且 0 <= i1 < i2 < ... < ik <= nums.length - 1。并且如果 seq[i+1] - seq[i]( 0 <= i < seq.length - 1) 的值都相同，那么序列 seq 是等差的。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [3,6,9,12]
 * 输出：4
 * 解释：
 * 整个数组是公差为 3 的等差数列。
 * 示例 2：
 *
 * 输入：nums = [9,4,7,2,10]
 * 输出：3
 * 解释：
 * 最长的等差子序列是 [4,7,10]。
 * 示例 3：
 *
 * 输入：nums = [20,1,15,3,10,5,8]
 * 输出：4
 * 解释：
 * 最长的等差子序列是 [20,15,10,5]。
 *
 *
 * 提示：
 *
 * 2 <= nums.length <= 1000
 * 0 <= nums[i] <= 500
 * 通过次数22,138提交次数50,155
 */
public class _1027_LongestArithmeticSubsequence {

    /**
     * 方法：动态规划
     *
     * 子序列问题不要求连续，一般定义 dp[i] 为 以 nums[i] 结尾的 xxxx
     *
     * 在本题中，我们定义 dp[i] 为以 nums[i] 结尾的等差子序列的最长长度
     * 由于公差不确定，所以需要给 dp 增加一个维度
     *
     * 我们定义 f[i][j] 表示以 nums[i] 结尾且公差为 j 的等差子序列的最大长度。
     *
     * 初始时 f[i][j]=1，即每个元素自身都是一个长度为 1 的等差数列。返回值为 所有 f[i][j] 的最大值
     *
     * 考虑 f[i]，nums[i]是最后一项，我们需要枚举 公差 d，来决定前一项 k = i - d
     * 然后 f[i][d] = max(f[i][d], f[k][d] + 1)
     * 但这样的话，d 需要枚举 0-1000，需要判断 k 的有效范围
     *
     * 【优化】
     * 换种方式，我们直接枚举 nums[i] 的前一个元素 nums[k]，那么公差 j=nums[i]−nums[k]+500，此时有 f[i][j]=max(f[i][j],f[k][j]+1)，
     * 然后我们更新答案 ans=max(ans,f[i][j])。最后返回答案即可。
     *
     * 【优化】
     * （所有元素都赋值为1再递推，求最值，等价于 所有元素都赋值0，递推求解后，给答案加1，这样可以省略掉初始化的for循环）
     * 初始时 f[i][j]=0，那么我们需要在最后返回答案时加上 1。
     *
     * 【优化】
     * 由于 0 <= nums[i] <= 500，所以 公差可能为负数，且最大差值为 500，因此，我们可以将统一将公差加上 500，这样公差的范围就变成了 [0,1000]。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/longest-arithmetic-subsequence/solution/python3javacgotypescript-yi-ti-yi-jie-do-h9lz/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int longestArithSeqLength(int[] nums) {
        int n = nums.length;
        // f[i][j] 表示以 nums[i] 结尾且公差为 j 的等差子序列的最大长度。
        // 省略初始化dp为1的过程，最后给 ans 加 1 即可
        int[][] dp = new int[n][1001];
        // 所有 dp[i][j] 的最大值
        int ans = 0;
        // 递推，nums[i] 为等差数列末尾项
        for (int i = 0; i < n; ++i) {
            // 枚举前一项 nums[j]
            for (int j = 0; j < i; ++j) {
                // 那么公差为 nums[i] - nums[j] + 500
                int d = nums[i] - nums[j] + 500;
                // 更新 dp[i][d]
                dp[i][d] = dp[j][d] + 1;
                // 更新 ans
                ans = Math.max(ans, dp[i][d]);
            }
        }
        // 返回时别忘记给 ans + 1
        return ans + 1;
    }
}
