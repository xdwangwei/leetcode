package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/3/22 12:15
 * @description: _1626_BestTeamWithNoConflicts
 *
 * 1626. 无矛盾的最佳球队
 * 假设你是球队的经理。对于即将到来的锦标赛，你想组合一支总体得分最高的球队。球队的得分是球队中所有球员的分数 总和 。
 *
 * 然而，球队中的矛盾会限制球员的发挥，所以必须选出一支 没有矛盾 的球队。如果一名年龄较小球员的分数 严格大于 一名年龄较大的球员，则存在矛盾。同龄球员之间不会发生矛盾。
 *
 * 给你两个列表 scores 和 ages，其中每组 scores[i] 和 ages[i] 表示第 i 名球员的分数和年龄。请你返回 所有可能的无矛盾球队中得分最高那支的分数 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：scores = [1,3,5,10,15], ages = [1,2,3,4,5]
 * 输出：34
 * 解释：你可以选中所有球员。
 * 示例 2：
 *
 * 输入：scores = [4,5,6,5], ages = [2,1,2,1]
 * 输出：16
 * 解释：最佳的选择是后 3 名球员。注意，你可以选中多个同龄球员。
 * 示例 3：
 *
 * 输入：scores = [1,2,3,5], ages = [8,9,10,1]
 * 输出：6
 * 解释：最佳的选择是前 3 名球员。
 *
 *
 * 提示：
 *
 * 1 <= scores.length, ages.length <= 1000
 * scores.length == ages.length
 * 1 <= scores[i] <= 106
 * 1 <= ages[i] <= 1000
 * 通过次数12,106提交次数24,603
 */
public class _1626_BestTeamWithNoConflicts {


    /**
     * 排序 + 动态规划
     *
     * 本题的数据范围显然不可能支持我们进行所有子集的枚举。我们希望找到一种顺序，使得我们在进行选择时，总是不会发生冲突。
     *
     * 我们可以将所有队员按照年龄升序进行排序，年龄相同时，则按照分数升序进行排序。排序之后，我们可以进行动态规划。
     *
     * 令 dp[i] 表示最后一个队员是第 i个队员时的最大分数（这里的 i 是重新排序后的编号）。
     *
     * 最终答案为 max(dp[i])
     *
     * base case 是队伍只有1个人的时候，dp[i] = scores[i]，但是可以和之后的状态转移合并在一起，所以不用单独初始化
     *
     * 对于状态转移：dp[i]，需要在 [0,i−1] 的范围内枚举上一个队员 j。
     * 由于 j 的年龄满足 小于等于 i 的年龄，那么 如果 j 的分数不超过 i 的分数，就可以将 i 加入这个队伍。
     * 在这些符合要求的 j 中取最大值
     *
     * dp[i] = max{dp[j], 0 < j < i} + scores[i]
     *
     * 为什么这样的枚举一定是合法的呢？
     *
     * 因为我们的最大分数总是在最后一个队员处取得（对于相同年龄的，我们是按照分数升序排序的，所以分数较高的一定在更后面），
     * 同时第 i 个队员的年龄不小于之前任意队员的年龄，
     * 所以只要第 i 个队员的分数大于等于之前的分组中最后一个队员的分数，就一定可以将第 i 个队员加入到组里，
     * 从而得到一个以第 i 个队员为最后一名队员的新的组。
     *
     * 作者：lucifer1004
     * 链接：https://leetcode.cn/problems/best-team-with-no-conflicts/solution/pai-xu-dong-tai-gui-hua-by-lucifer1004/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param scores
     * @param ages
     * @return
     */
    public int bestTeamScore(int[] scores, int[] ages) {
        int n = scores.length;
        // 索引排序
        Integer[] idx = new Integer[n];
        // 初始化索引
        for (int i = 0; i < n; ++i) {
            idx[i] = i;
        }
        // 按照年龄升序，相同年龄时分数升序 排序
        Arrays.sort(idx, (i, j) -> ages[i] != ages[j] ? ages[i] - ages[j] : scores[i] - scores[j]);
        //  dp[i] 表示最后一个队员是第 i个队员时的最大分数（这里的 i 是重新排序后的编号， 即 idx[i]）。
        int[] dp = new int[n];
        int ans = 0;
        // 状态转移
        for (int i = 0; i < n; ++i) {
            // dp[i] = max{dp[j], 0 < j < i} + scores[i]
            for (int j = 0; j < i; ++j) {
                // 注意这里scores数组索引是重新排序后的编号，即 i,j --> idx[i], idx[j]
                if (scores[idx[j]] <= scores[idx[i]]) {
                    dp[i] = Math.max(dp[i], dp[j]);
                }
            }
            dp[i] += scores[idx[i]];
            // ans = max(dp[i])
            ans = Math.max(ans, dp[i]);
        }
        // 返回
        return ans;
    }
}
