package com.daily;

/**
 * @author wangwei
 * @date 2023/3/8 12:29
 * @description: _offer_047_MaximumValueOfGift
 *
 * 剑指 Offer 47. 礼物的最大价值
 * 在一个 m*n 的棋盘的每一格都放有一个礼物，每个礼物都有一定的价值（价值大于 0）。你可以从棋盘的左上角开始拿格子里的礼物，并每次向右或者向下移动一格、直到到达棋盘的右下角。给定一个棋盘及其上面的礼物的价值，请计算你最多能拿到多少价值的礼物？
 *
 *
 *
 * 示例 1:
 *
 * 输入:
 * [
 *   [1,3,1],
 *   [1,5,1],
 *   [4,2,1]
 * ]
 * 输出: 12
 * 解释: 路径 1→3→5→2→1 可以拿到最多价值的礼物
 *
 *
 * 提示：
 *
 * 0 < grid.length <= 200
 * 0 < grid[0].length <= 200
 * 通过次数242,707提交次数349,885
 */
public class _offer_047_MaximumValueOfGift {

    /**
     * 方法一：动态规划
     *
     * m = grid.length, n = grid[0].length
     *
     * 我们定义 f[i][j] 为从棋盘左上角走到 (i,j)位置 的礼物最大累计价值，
     * 那么 f[i][j] 的值由 f[i−1][j] 和 f[i][j−1] 决定，即从上方格子和左方格子走过来的两个方案中选择一个价值较大的方案。
     *
     * 返回 dp[m-1][n-1]
     *
     * 因此我们可以写出动态规划转移方程：
     *      f[i][j] = max(f[i−1][j], f[i][j−1]) + grid[i][j], 0<= i < m,  0 <= j < n
     *
     * 由于用到了 i-1 和 j-1，这样写 则需要加 i,j > 1 的 if 判断
     *
     * 简单起见，定义 f[i][j] 为从棋盘左上角走到 (i-1,j-1)位置 的礼物最大累计价值，则
     *      f[i][j] = max(f[i−1][j], f[i][j−1]) + grid[i-1][j-1], 1 <= i <= m,  1 <= j <= n
     *      不用再判断 i，j 和 1 的大小关系
     * 最终返回 f[m][n]
     *
     * 时间复杂度 O(m×n)，空间复杂度 O(m×n)。其中
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/li-wu-de-zui-da-jie-zhi-lcof/solution/python3javacgotypescript-yi-ti-yi-jie-do-8s4e/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param grid
     * @return
     */
    public int maxValue(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        // f[i][j] 为从棋盘左上角走到 (i-1,j-1)位置 的礼物最大累计价值
        int[][] dp = new int[m + 1][n + 1];
        // 1 <= i <= m,  1 <= j <= n
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                // 左边，上边，取最大值，加当前位置
                dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]) + grid[i - 1][j - 1];
            }
        }
        // 返回
        return dp[m][n];
    }

    /**
     * 动态规划空间优化
     *
     * 我们注意到 f[i][j] 只与 f[i−1][j] 和 f[i][j−1] 有关，
     *
     * 因此我们可以仅用两行数组 f[2][n+1] 来存储状态，从而将空间复杂度优化到 O(n)。
     *
     * f[i][j]  --> f[ i & 1 ][j]
     * f[i-1][j]  --> f[ i&1 ^ 1 ][j]
     *
     * 最终返回 f[m][n]  -->   f[m & 1][n]
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/li-wu-de-zui-da-jie-zhi-lcof/solution/python3javacgotypescript-yi-ti-yi-jie-do-8s4e/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param grid
     * @return
     */
    public int maxValue2(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        // 状态压缩。
        int[][] dp = new int[2][n + 1];
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                // dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]) + grid[i - 1][j - 1];
                // f[i][j]  --> f[ i & 1 ][j]，f[i-1][j]  --> f[ i&1 ^ 1 ][j]
                dp[i & 1][j] = Math.max(dp[i & 1][j - 1], dp[i & 1 ^ 1][j]) + grid[i - 1][j - 1];
            }
        }
        // 返回 f[m][n]  -->   f[m & 1][n]
        return dp[m & 1][n];
    }
}
