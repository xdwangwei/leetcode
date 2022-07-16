package com.dp;


import javax.swing.*;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/19 周二 20:29
 * <p>
 * 给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 * <p>
 * 说明：每次只能向下或者向右移动一步。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：grid = [[1,3,1],[1,5,1],[4,2,1]]
 * 输出：7
 * 解释：因为路径 1→3→1→1→1 的总和最小。
 * 示例 2：
 * <p>
 * 输入：grid = [[1,2,3],[4,5,6]]
 * 输出：12
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/minimum-path-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 **/
public class _64_MinPathSum {

    /**
     * 动态规划
     *
     * 创建二维数组 dp，与原始网格的大小相同，dp[i][j] 表示从左上角出发到(i,j) 位置的最小路径和。
     * 显然，dp[0][0]=grid[0][0]。对于 dp 中的其余元素，通过以下状态转移方程计算元素值。
     *
     * base case:
     *  由于路径的方向只能是向下或向右，因此网格的 【第一行】的每个元素只能从左上角元素开始向右移动到达，
     *  网格的 【第一列】的每个元素只能从左上角元素开始向下移动到达，此时的路径是唯一的，因此每个元素对应的最小路径和即为对应的路径上的数字总和。
     *
     * 状态转移：
     *
     * 对于不在第一行和第一列的元素，可以从其上方相邻元素向下移动一步到达，或者从其左方相邻元素向右移动一步到达，元素对应的最小路径和等于其上方相邻元素与其左方相邻元素两者对应的最小路径和中的最小值加上当前元素的值。
     *
     * 当 i>0 且 j=0 时，dp[i][0]=dp[i−1][0]+grid[i][0]。
     *
     * 当 i=0 且 j>0 时，dp[0][j]=dp[0][j−1]+grid[0][j]。
     *
     * 当 i>0 且 j>0 时，dp[i][j]=min(dp[i−1][j],dp[i][j−1])+grid[i][j]。
     *
     * 最后得到 dp[m−1][n−1] 的值即为从网格左上角到网格右下角的最小路径和。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/minimum-path-sum/solution/zui-xiao-lu-jing-he-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param grid
     * @return
     */
    public static int solution(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0) {
            return 0;
        }
        int m = grid.length;
        int n = grid[0].length;
        // dp[i][j] 表示从左上角出发到(i,j) 位置的最小路径和。
        int[][] dp = new int[m][n];
        // 起点到起点
        dp[0][0] = grid[0][0];
        // base case
        // 第一行只能往右走
        for (int j = 1; j < n; j++)
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        // 第一列只能往下走
        for (int i = 1; i < m; i++)
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        // 状态转移
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // 当前位置来源于 头顶往下，或左边元素往右，选择代价更小的那个
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }
        // 返回从起点到终点的最小代价
        return dp[m - 1][n - 1];
    }

    /**
     * 状态压缩
     *
     * 可以看出，dp[i][j]实际上只和dp[i-1][j]有关，因此我们可以消去 i 维度
     * @param grid
     * @return
     */
    public static int solution2(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0) {
            return 0;
        }
        int m = grid.length;
        int n = grid[0].length;
        // dp[i][j] 表示从左上角出发到(i,j) 位置的最小路径和。
        int[] dp = new int[n];
        // base case起点到起点
        dp[0] = grid[0][0];
        // base case i = 0 时，只能往右走
        for (int j = 1; j < n; j++) {
            dp[j] = dp[j - 1] + grid[0][j];
        }
        for (int i = 1; i < m; i++) {
            // 将原来的base case j = 0 合并到这里，j = 0 只能一直向下
            dp[0] += grid[i][0];
            for (int j = 1; j < n; j++) {
                // 当前位置来源于 头顶往下，或左边元素往右，选择代价更小的那个
                dp[j] = Math.min(dp[j], dp[j - 1]) + grid[i][j];
            }
        }
        // 返回从起点到终点的最小代价
        return dp[n - 1];
    }
}
