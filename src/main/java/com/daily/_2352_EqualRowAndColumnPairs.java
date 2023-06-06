package com.daily;

import java.util.concurrent.ForkJoinPool;

/**
 * @author wangwei
 * @date 2023/6/6 10:35
 * @description: _2352_EqalRowAndColumnPairs
 *
 * 2352. 相等行列对
 * 给你一个下标从 0 开始、大小为 n x n 的整数矩阵 grid ，返回满足 Ri 行和 Cj 列相等的行列对 (Ri, Cj) 的数目。
 *
 * 如果行和列以相同的顺序包含相同的元素（即相等的数组），则认为二者是相等的。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：grid = [[3,2,1],[1,7,6],[2,7,7]]
 * 输出：1
 * 解释：存在一对相等行列对：
 * - (第 2 行，第 1 列)：[2,7,7]
 * 示例 2：
 *
 *
 *
 * 输入：grid = [[3,1,2,2],[1,4,4,5],[2,4,2,2],[2,4,2,2]]
 * 输出：3
 * 解释：存在三对相等行列对：
 * - (第 0 行，第 0 列)：[3,1,2,2]
 * - (第 2 行, 第 2 列)：[2,4,2,2]
 * - (第 3 行, 第 2 列)：[2,4,2,2]
 *
 *
 * 提示：
 *
 * n == grid.length == grid[i].length
 * 1 <= n <= 200
 * 1 <= grid[i][j] <= 105
 * 通过次数19,006提交次数25,719
 */
public class _2352_EqualRowAndColumnPairs {

    /**
     * 方法：模拟
     *
     * 我们直接将矩阵 grid 的每一行和每一列进行比较，如果相等，那么就是一对相等行列对，答案加一。
     * @param grid
     * @return
     */
    public int equalPairs(int[][] grid) {
        int n = grid.length;
        int ans = 0;
        // 遍历行号
        for (int i = 0; i < n; ++i) {
            // 遍历列号
            for (int j = 0; j < n; ++j) {
                // 假设i行和j列匹配
                boolean match = true;
                // 比较n个位置的元素是否都等
                for (int k = 0; k < n; ++k) {
                    // 不相等，匹配失败，结束
                    if (grid[i][k] != grid[k][j]) {
                        match = false;
                        break;
                    }
                }
                // 确实匹配，ans++
                if (match) {
                    ans++;
                }
            }
        }
        // 返回
        return ans;
    }
}
