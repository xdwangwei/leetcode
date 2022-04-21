package com.hot100;

/**
 * @author wangwei
 * @date 2022/4/20 19:30
 * @description: _221_MaximalSquare
 *
 * 221. 最大正方形
 * 在一个由 '0' 和 '1' 组成的二维矩阵内，找到只包含 '1' 的最大正方形，并返回其面积。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
 * 输出：4
 * 示例 2：
 *
 *
 * 输入：matrix = [["0","1"],["1","0"]]
 * 输出：1
 * 示例 3：
 *
 * 输入：matrix = [["0"]]
 * 输出：0
 *
 *
 * 提示：
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 300
 * matrix[i][j] 为 '0' 或 '1'
 */
public class _221_MaximalSquare {

    /**
     * 动态规划
     * 求最大面积，求最大边长即可
     *
     * 我们用 dp(i,j) 表示以 (i,j) 为右下角，且只包含 1 的正方形的边长最大值。
     * 如果我们能计算出所有 dp(i,j) 的值，那么其中的最大值即为矩阵中只包含 1 的正方形的边长最大值，其平方即为最大正方形的面积。
     *
     * 那么如何计算 dp 中的每个元素值呢？对于每个位置 (i, j)，检查在矩阵中该位置的值：
     *
     *      如果该位置的值是 0，则 dp(i,j)=0，因为当前位置不可能在由 1 组成的正方形中；
     *      如果该位置的值是 1，则 dp(i,j) 的值由其【上方】、【左方】和【左上方】的三个相邻位置的 dp 值的最小值决定，类似于木桶短板效应。
     *      具体而言，当前位置的元素值等于三个相邻位置的元素中的最小值加 1，状态转移方程如下：
     *      dp(i,j)=min(dp(i−1,j),dp(i−1,j−1),dp(i,j−1))+1
     *
     * 如果读者对这个状态转移方程感到不解，可以参考 1277. 统计全为 1 的正方形子矩阵的官方题解，其中给出了详细的证明。
     * 也可参考这个题解的图 https://leetcode-cn.com/problems/maximal-square/solution/li-jie-san-zhe-qu-zui-xiao-1-by-lzhlyle/
     *
     * 此外，还需要考虑边界条件。如果 i 和 j 中至少有一个为 0，则以位置 ((i,j) 为右下角的最大正方形的边长只能是 1，因此 dp(i,j)=1。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/maximal-square/solution/zui-da-zheng-fang-xing-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param matrix
     * @return
     */
    public int maximalSquare(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        // dp(i,j) 表示以 (i,j) 为右下角，且只包含 1 的正方形的边长最大值。
        int[][] dp = new int[m][n];
        // 最大 1正方形 边长
        int maxLen = 0;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                // 0 位置不能组成 1方形
                if (matrix[i][j] == '0') {
                    dp[i][j] = 0;
                } else {
                    // 如果是边界上的“1”，只能组成1个大小为1的正方形
                    if (i == 0 || j == 0) {
                        dp[i][j] = 1;
                    } else {
                        // 以它为右下角的最大的1方形的大小取决于它的左边上边和做上边三个位置的dp值的最小值
                        dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                    }
                    // 更新最大边长
                    maxLen = Math.max(maxLen, dp[i][j]);
                }
            }
        }
        // 返回面积
        return maxLen * maxLen;
    }
}
