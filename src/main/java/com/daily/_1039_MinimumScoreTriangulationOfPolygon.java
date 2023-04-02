package com.daily;

/**
 * @author wangwei
 * @date 2023/4/2 9:46
 * @description: _1039_MinimumScoreTriangulationOfPolygon
 *
 * 1039. 多边形三角剖分的最低得分
 * 你有一个凸的 n 边形，其每个顶点都有一个整数值。给定一个整数数组 values ，其中 values[i] 是第 i 个顶点的值（即 顺时针顺序 ）。
 *
 * 假设将多边形 剖分 为 n - 2 个三角形。对于每个三角形，该三角形的值是顶点标记的乘积，三角剖分的分数是进行三角剖分后所有 n - 2 个三角形的值之和。
 *
 * 返回 多边形进行三角剖分后可以得到的最低分 。
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：values = [1,2,3]
 * 输出：6
 * 解释：多边形已经三角化，唯一三角形的分数为 6。
 * 示例 2：
 *
 *
 *
 * 输入：values = [3,7,4,5]
 * 输出：144
 * 解释：有两种三角剖分，可能得分分别为：3*7*5 + 4*5*7 = 245，或 3*4*5 + 3*4*7 = 144。最低分数为 144。
 * 示例 3：
 *
 *
 *
 * 输入：values = [1,3,1,4,1,5]
 * 输出：13
 * 解释：最低分数三角剖分的得分情况为 1*1*3 + 1*1*4 + 1*1*5 + 1*1*1 = 13。
 *
 *
 * 提示：
 *
 * n == values.length
 * 3 <= n <= 50
 * 1 <= values[i] <= 100
 * 通过次数8,958提交次数15,062
 */
public class _1039_MinimumScoreTriangulationOfPolygon {

    /**
     * 方法：区间动态规划
     *
     * 根据题目values数组的定义方式：values[i] 是第 i 个顶点的值（即 顺时针顺序 ）。
     *
     * 可以将 n边形定义为「沿着边从 顶点 0 顺时针到顶点 n-1)，再加上直接从 顶点0和n-1之间的这条边  所组成的多边形
     * 相当于 将 顶点 0 和 顶点 n-1 之间的边 作为多边形的底边，（也可以把其他边作为底边）
     *
     * 基于此，无论怎么剖分，顶点0和顶点n-1组成的边一定在一个三角形中，至于是哪个三角形，取决于第三个顶点
     * 第三个顶点可以选择0和n-1之间的任意一个顶点，
     * 这样0到n-1组成的多边形就会被切割成 0到k的多边形，k到n-1的多边形，和 0、n-1、k 组成的三角形
     * 三角形得分的计算规则已经知道了，0到k的多边形，k到n-1的多边形 就是两个子问题了
     *
     *
     * 因此，定义 dp[i][j]，表示将i到j顺时针组成的多边形进行三角剖分后的最低分数。那么答案就是 dp[0][n-1]
     *
     * base case：
     *      如果 i+1 == j，就是一条边，只有两个顶点，无法进行三角剖分，得分为0
     * 递推 dp[i][j]：
     *      枚举 i 和 j 之间的一个顶点 k，即 i < k < j，
     *      i 到 j的多边形可被分割为：i到k的多边形、k到j的多边形、i、j、k组成的三角形
     *      因此，i到j的多边形剖分分数最小值 等于  i到k的多边形剖分最小值 +  k到j的多边形最小值 +  i、j、k组成的三角形分值（固定值）
     *      取所有可能的最小值
     *      即 dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j] + v[i]*v[j]*v[k]), i < k < j
     *
     * 最终返回 dp[0][n-1]，至少要有三个顶点，所以 i 从 n-3 开始，j 从 i + 2 开始
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/solution/python3javacgo-yi-ti-shuang-jie-ji-yi-hu-5ghb/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param values
     * @return
     */
    public int minScoreTriangulation(int[] values) {
        int n = values.length;
        // dp[i][j]，表示将i到j顺时针组成的多边形进行三角剖分后的最低分数。
        // 最终返回 dp[0][n-1]，至少要有三个顶点，所以 i 从 n-3 开始，j 从 i + 2 开始
        int[][] dp = new int[n][n];
        for (int i = n - 3; i >= 0; --i) {
            for (int j = i + 2; j < n; ++j) {
                // 枚举 i、j 之间的顶点 k，dp[i][j] 求最小值，所以初始化为一个最大值
                dp[i][j] = 1 << 30;
                // 枚举
                for (int k = i + 1; k < j; ++k) {
                    // i到j的多边形剖分分数最小值 等于  i到k的多边形剖分最小值 +  k到j的多边形最小值 +  i、j、k组成的三角形分值（固定值）
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j] + values[i] * values[j] * values[k]);
                }
            }
        }
        // 返回
        return dp[0][n - 1];
    }
}
