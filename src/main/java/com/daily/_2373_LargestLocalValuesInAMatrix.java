package com.daily;

/**
 * @author wangwei
 * @date 2023/3/1 12:20
 * @description: _2373_LargestLocalValuesInAMatrix
 *
 * 2373. 矩阵中的局部最大值
 * 给你一个大小为 n x n 的整数矩阵 grid 。
 *
 * 生成一个大小为 (n - 2) x (n - 2) 的整数矩阵  maxLocal ，并满足：
 *
 * maxLocal[i][j] 等于 grid 中以 i + 1 行和 j + 1 列为中心的 3 x 3 矩阵中的 最大值 。
 * 换句话说，我们希望找出 grid 中每个 3 x 3 矩阵中的最大值。
 *
 * 返回生成的矩阵。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：grid = [[9,9,8,1],[5,6,2,6],[8,2,6,4],[6,2,2,2]]
 * 输出：[[9,9],[8,6]]
 * 解释：原矩阵和生成的矩阵如上图所示。
 * 注意，生成的矩阵中，每个值都对应 grid 中一个相接的 3 x 3 矩阵的最大值。
 * 示例 2：
 *
 *
 *
 * 输入：grid = [[1,1,1,1,1],[1,1,1,1,1],[1,1,2,1,1],[1,1,1,1,1],[1,1,1,1,1]]
 * 输出：[[2,2,2],[2,2,2],[2,2,2]]
 * 解释：注意，2 包含在 grid 中每个 3 x 3 的矩阵中。
 *
 *
 * 提示：
 *
 * n == grid.length == grid[i].length
 * 3 <= n <= 100
 * 1 <= grid[i][j] <= 100
 * 通过次数21,466提交次数24,842
 */
public class _2373_LargestLocalValuesInAMatrix {

    /**
     * 方法：遍历
     *
     * 设 grid 的大小为 n×n，那么我们申请一个大小为 (n−2)×(n−2) 的矩阵 res 用来存放答案。
     * 我们遍历 grid 中每个 3×3 子矩阵的左上角，然后统计当前子矩阵的最大值放入 res 中。
     *
     * res[i][j] 等于 grid 中以 i + 1 行和 j + 1 列为中心的 3 x 3 矩阵中的 最大值 。
     * 因此，其所在的3x3矩阵位于原grid中 i~i+2行，j~j+2列
     *
     * 具体做法是，我们顺序遍历 (0≤i<n−2)，再顺序遍历 (0≤j<n−2)，接着遍历求解 i≤x<i+3,j≤y<j+3 的最大值放入 res[i][j] 中。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/largest-local-values-in-a-matrix/solution/ju-zhen-zhong-de-ju-bu-zui-da-zhi-by-lee-o703/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param grid
     * @return
     */
    public int[][] largestLocal(int[][] grid) {
        int n = grid.length;
        int m = n - 2;
        int[][] ans = new int[m][m];
        // 顺序遍历求解 res[i][j]
        // res[i][j] 等于 grid 中以 i + 1 行和 j + 1 列为中心的 3 x 3 矩阵中的 最大值 。
        // 其所在的3x3矩阵位于原grid中 i~i+2行，j~j+2列
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < m; ++j) {
                // grid中对应的3x3矩阵
                for (int k = i; k < i + 3; ++k) {
                    for (int l = j; l < j + 3; ++l) {
                        // 取最大值
                        ans[i][j] = Math.max(ans[i][j], grid[k][l]);
                    }
                }
            }
        }
        return ans;
    }
}
