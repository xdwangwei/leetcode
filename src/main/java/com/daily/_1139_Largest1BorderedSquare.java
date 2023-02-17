package com.daily;

/**
 * @author wangwei
 * @date 2023/2/17 13:09
 * @description: _1139_Largest1BorderedSquare
 *
 * 1139. 最大的以 1 为边界的正方形
 * 给你一个由若干 0 和 1 组成的二维网格 grid，请你找出边界全部由 1 组成的最大 正方形 子网格，并返回该子网格中的元素数量。如果不存在，则返回 0。
 *
 *
 *
 * 示例 1：
 *
 * 输入：grid = [[1,1,1],[1,0,1],[1,1,1]]
 * 输出：9
 * 示例 2：
 *
 * 输入：grid = [[1,1,0,0]]
 * 输出：1
 *
 *
 * 提示：
 *
 * 1 <= grid.length <= 100
 * 1 <= grid[0].length <= 100
 * grid[i][j] 为 0 或 1
 * 通过次数13,332提交次数26,908
 */
public class _1139_Largest1BorderedSquare {

    /**
     * 方法：前缀和 + 枚举
     *
     * 因为grid长、宽不超过100，因此可以采用枚举边长和右下角顶点的方式求结果。
     *
     * 具体地，我们枚举正方形的边长 d，从最大的边长开始枚举，然后枚举正方形的右下角位置 (i,j)，
     * 如果满足条件，即可返回 d*d
     *
     * 对于右下角为 (i,j) ，边长为 d 的正方形，必须满足四个边全部是1
     *
     * 我们可以使用类似前缀和的方法预处理出
     * (i,j)位置 向左 和 向上 的连续 1 的个数，记为 leftOnes[i][j] 和 upOnes[i][j]
     *
     * 那么判断此时正方形是否满足条件的判断为
     *
     * leftOnes[i][j] >= d          // 下边
     * && upOnes[i][j] >= d         // 右边
     * && leftOnes[i-d+1][j] >= d   // 上边
     * && upOnes[i][j-d+1] >= d     // 左边
     *
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/largest-1-bordered-square/solution/python3javacgo-yi-ti-yi-jie-qian-zhui-he-wqdk/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param grid
     * @return
     */
    public int largest1BorderedSquare(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        // 预处理得到 (i,j)位置 向左 和 向上 的连续 1 的个数，记为 leftOnes[i][j] 和 upOnes[i][j]
        int[][] leftOnes = new int[m][n];
        int[][] upOnes = new int[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                // 当前位置为1，才能继续连续
                if (grid[i][j] == 1) {
                    // 向左
                    leftOnes[i][j] = j > 0 ? leftOnes[i][j - 1] + 1 : 1;
                    // 向上
                    upOnes[i][j] = i > 0 ? upOnes[i - 1][j] + 1 : 1;
                }
            }
        }
        // 从大到小枚举正方形边长
        for (int d = Math.min(m, n); d > 0; --d) {
            // 枚举右下角左边，
            // 从grid最右下方开始枚举（向左、向上画边），必须保证变成 >= d
            for(int i = m - 1; i >= d - 1; --i) {
                for(int j = n - 1; j >= d - 1; --j) {
                    // 如果四条边都是连续1
                    if (leftOnes[i][j] >= d
                            && upOnes[i][j] >= d
                            && leftOnes[i-d+1][j] >= d
                            && upOnes[i][j-d+1] >= d) {
                        // 直接返回当前正方形元素个数（面积）
                        return d * d;
                    }
                }
            }
        }
        return 0;
    }
}
