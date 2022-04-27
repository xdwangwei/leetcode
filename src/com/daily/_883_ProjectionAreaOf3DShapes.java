package com.daily;

/**
 * @author wangwei
 * @date 2022/4/26 13:13
 * @description: _883_ProjectionAreaOf3DShapes
 *
 * 883. 三维形体投影面积
 * 在 n x n 的网格 grid 中，我们放置了一些与 x，y，z 三轴对齐的 1 x 1 x 1 立方体。
 *
 * 每个值 v = grid[i][j] 表示 v 个正方体叠放在单元格 (i, j) 上。
 *
 * 现在，我们查看这些立方体在 xy 、yz 和 zx 平面上的投影。
 *
 * 投影 就像影子，将 三维 形体映射到一个 二维 平面上。从顶部、前面和侧面看立方体时，我们会看到“影子”。
 *
 * 返回 所有三个投影的总面积 。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：[[1,2],[3,4]]
 * 输出：17
 * 解释：这里有该形体在三个轴对齐平面上的三个投影(“阴影部分”)。
 * 示例 2:
 *
 * 输入：grid = [[2]]
 * 输出：5
 * 示例 3：
 *
 * 输入：[[1,0],[0,2]]
 * 输出：8
 *
 *
 * 提示：
 *
 * n == grid.length == grid[i].length
 * 1 <= n <= 50
 * 0 <= grid[i][j] <= 50
 */
public class _883_ProjectionAreaOf3DShapes {

    /**
     * 得到这些1立方体组成的结构的主视图+左视图+俯视图投影面积总和，按格子统计
     * @param grid
     * @return
     */
    public int projectionArea(int[][] grid) {
        // 这是一个 n x n 的方格
        int n = grid.length, res = 0;
        for (int i = 0; i < n; ++i) {
            // iMaxH，保存主视图，在 i 位置往前看，在不同j上，最高的柱子的高度
            // 每一次的iMax的总和就是主视图投影面积
            // jMax 保存左视图，在 j 位置往右看，在不同i上，最高的柱子的高度
            // 所有jMaxH的总和就是左视图投影面积
            int iMaxH = 0, jMaxH = 0;
            // 当前位置有格子，那就有投影，俯视图投影面积加1，注意是加1
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] != 0) {
                    res++;
                }
                // 更新主左视图对应一行或一列的最大高度
                iMaxH = Math.max(iMaxH, grid[i][j]);
                // 这里因为整个是个nxn的方格，所以才能在计算主视图投影面积的同时 用 grid[j][i] 来计算左视图投影面积
                jMaxH = Math.max(jMaxH, grid[j][i]);
            }
            // 累加主左视图在某一行或某一列的投影面积
            res += iMaxH;
            res += jMaxH;
        }
        return res;

    }
}
