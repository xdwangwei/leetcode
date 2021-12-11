package com.dp;

/**
 * @author wangwei
 * 2021/12/10 13:43
 * <p>
 * 给你一个 n x n 的 方形 整数数组 matrix ，请你找出并返回通过 matrix 的下降路径 的 最小和 。
 * <p>
 * 下降路径 可以从第一行中的任何元素开始，并从每一行中选择一个元素。在下一行选择的元素和当前行所选元素最多相隔一列（即位于正下方或者沿对角线向左或者向右的第一个元素）。具体来说，位置 (row, col) 的下一个元素应当是 (row + 1, col - 1)、(row + 1, col) 或者 (row + 1, col + 1) 。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：matrix = [[2,1,3],[6,5,4],[7,8,9]]
 * 输出：13
 * 解释：下面是两条和最小的下降路径，用加粗+斜体标注：
 * [[2,1,3],      [[2,1,3],
 * [6,5,4],       [6,5,4],
 * [7,8,9]]       [7,8,9]]
 * 示例 2：
 * <p>
 * 输入：matrix = [[-19,57],[-40,-5]]
 * 输出：-59
 * 解释：下面是一条和最小的下降路径，用加粗+斜体标注：
 * [[-19,57],
 * [-40,-5]]
 * 示例 3：
 * <p>
 * 输入：matrix = [[-48]]
 * 输出：-48
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/minimum-falling-path-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _931_MinimumFallingPathSum {

    /**
     * 动态规划
     * dp[i][j] 表示从第一行下降到当前位置的最小路径和
     *
     * base case：
     * 当 i = 0 时，从第一行下降到 [0][j] ，路径和就是 matrix[0][j]
     *
     * 状态转移：
     *  [i][j] 位置 可以 由它的左上(i-1,j-1)、正上(i-1,j)、右上(i-1,j+1)得到
     *  因此 dp[i][j] = min(dp[i-1][j-1], dp[i-1][j], dp[i-1][j+1]) + matrix[i][j]
     *
     * 返回：
     *  ，因为到达最后一行任一位置均可，所以返回最后一行dp中，最小的那个，min(dp[n-1][j], 0 <= j < n)
     *
     * 注意每一行最左边和最右边的点只可能由两个来源
     *
     *
     * 因为是自上往下下降，而且我们的dp定义也是自上往下的下降和，所以dp递推时正向遍历即可。
     *
     * 另外，每次更新一行dp，实际上只和上一行dp有关，所以可以创建一维dp数组
     * 在这种情况下，可以增加左边边界，初始化为MAX，这样就不用单独考虑原来左边界和有边界上的点与中间部分点的不同了。
     * @param matrix
     * @return
     */
    public int minFallingPathSum(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return 0;
        }
        int n = matrix.length;
        // 创建dp，dp[i][j] 表示从第一行下降到当前位置的最小路径和
        int[][] dp = new int[n][n];
        // base case 由第一行到第一行j位置
        for (int j = 0; j < n; ++j) {
            dp[0][j] = matrix[0][j];
        }
        // 递推
        for (int i = 1; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // 最左边的点，考虑 正上 和 右上
                if (j == 0) {
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i - 1][j + 1]) + matrix[i][j];
                // 最右边的点，考虑 正上 和 左上
                } else if (j == n - 1) {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], dp[i - 1][j]) + matrix[i][j];
                // 中间点，考虑 左上、正上、右上
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i - 1][j + 1])) + matrix[i][j];
                }
            }
        }
        // 返回最后一行dp中，最小的值
        int res = dp[n - 1][0];
        for (int j = 1; j < n; ++j) {
            res = Math.min(res, dp[n - 1][j]);
        }
        return res;
    }
}
