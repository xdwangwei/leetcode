package com.dp;

/**
 * @author wangwei
 * 2020/4/6 17:47
 * <p>
 * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。
 * <p>
 * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。
 * <p>
 * 现在考虑网格中有障碍物。那么从左上角到右下角将会有多少条不同的路径
 * <p>
 * 说明：m 和 n 的值均不超过 100。
 * <p>
 * 示例 1:
 * <p>
 * 输入:
 * [
 *   [0,0,0],
 *   [0,1,0],
 *   [0,0,0]
 * ]
 * 输出: 2
 * 解释:
 * 3x3 网格的正中间有一个障碍物。
 * 从左上角到右下角一共有 2 条不同的路径：
 * 1. 向右 -> 向右 -> 向下 -> 向下
 * 2. 向下 -> 向下 -> 向右 -> 向右
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/unique-paths-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _63_DiffPathPlus {

    /**
     * 动态规划
     * @param obstacleGrid
     * @return
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if (obstacleGrid == null || obstacleGrid.length == 0
                || obstacleGrid[0] == null || obstacleGrid[0].length == 0) return 0;
        // 如果起点是障碍物
        if (obstacleGrid[0][0] == 1) return 0;
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        // 起点到起点有一条路
        dp[0][0] = 1;
        // 初始化第一行，
        for (int i = 1; i < m; i++){
            // 连续的0，从起点横着走即可
            if (obstacleGrid[i][0] == 0) dp[i][0] = 1;
            // 一旦碰到障碍物，之后都不通
            else break;
        }
        // 初始化第一列
        for (int j = 1; j < n; j++){
            // 连续的0，从起点竖着走即可
            if (obstacleGrid[0][j] == 0) dp[0][j] = 1;
            // 一旦碰到障碍物，之后都不通
            else break;
        }
        // 第一行第一个位置开始到全部位置
        for (int i = 1; i < m; i++)
            for (int j = 0; j < n; j++) {
                // 当前位置是障碍物，没有可达路径
                if (obstacleGrid[i][j] == 1)
                    dp[i][j] = 0;
                // 否则，从起点到此位置的路径数等于 左边位置 和 上边位置 路径之和
                else
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        // 返回从起点到最后一个位置的路径数
        return dp[m - 1][n - 1];
    }
}
