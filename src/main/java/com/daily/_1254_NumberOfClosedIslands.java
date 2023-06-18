package com.daily;

import java.net.Inet4Address;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/6/18 18:43
 * @description: _1254_NumberOfClosedIslands
 *
 * 1254. 统计封闭岛屿的数目
 * 二维矩阵 grid 由 0 （土地）和 1 （水）组成。岛是由最大的4个方向连通的 0 组成的群，封闭岛是一个 完全 由1包围（左、上、右、下）的岛。
 *
 * 请返回 封闭岛屿 的数目。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：grid = [[1,1,1,1,1,1,1,0],[1,0,0,0,0,1,1,0],[1,0,1,0,1,1,1,0],[1,0,0,0,0,1,0,1],[1,1,1,1,1,1,1,0]]
 * 输出：2
 * 解释：
 * 灰色区域的岛屿是封闭岛屿，因为这座岛屿完全被水域包围（即被 1 区域包围）。
 * 示例 2：
 *
 *
 *
 * 输入：grid = [[0,0,1,0,0],[0,1,0,1,0],[0,1,1,1,0]]
 * 输出：1
 * 示例 3：
 *
 * 输入：grid = [[1,1,1,1,1,1,1],
 *              [1,0,0,0,0,0,1],
 *              [1,0,1,1,1,0,1],
 *              [1,0,1,0,1,0,1],
 *              [1,0,1,1,1,0,1],
 *              [1,0,0,0,0,0,1],
 *              [1,1,1,1,1,1,1]]
 * 输出：2
 *
 *
 * 提示：
 *
 * 1 <= grid.length, grid[0].length <= 100
 * 0 <= grid[i][j] <=1
 * 通过次数51,415提交次数80,752
 */
public class _1254_NumberOfClosedIslands {

    private int[][] grid;

    private static final int[] dirs = new int[]{-1, 0, 1, 0, -1};

    /**
     * 方法一：DFS
     *
     * 本题中：岛屿是0位置（连通分量），封闭岛屿要求周围全被1包裹，
     * 那么 如果从一个 0 出发，向四方向移动，可以移动到网格图的边界（最外面一圈），那么这个 0 所处的岛屿就不是封闭的；
     * 反之，如果无法移动到网格图的边界，就是封闭的。
     *
     * 本题【核心】在于 将 判断封闭岛屿 转换为 所在联通分量不包含边界位置
     *
     * 因此，
     *      遍历矩阵，
     *      对于每个0位置，我们进行深度优先搜索，找到与其相连的所有岛屿点（联通分量），
     *          DFS 访问四方向的 0，并把这些 0 标记成「访问过」。代码实现时可以直接把 0 修改成 1。
     *          并且需要判断是否存在边界上的0点，如果存在，则不是封闭岛屿，否则是封闭岛屿，答案加一。
     *      最后返回答案即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/number-of-closed-islands/solution/python3javacgotypescript-yi-ti-shuang-ji-ttoe/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param grid
     * @return
     */
    public int closedIsland(int[][] grid) {
        this.grid = grid;
        int ans = 0;
        int m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 找到 0 位置
                if (grid[i][j] == 0) {
                    // dfs 寻找联通分量，标记已访问，返回 是否是封闭岛屿（联通分量是否存在边界上的点）
                    // 如果是封闭岛屿，答案加1
                    ans += dfs(i, j) ? 1 : 0;
                }
            }
        }
        return ans;
    }

    /**
     * 寻找 grid[i][j] 所在联通分量，标记所有点已访问，并返回这个联通分量是否是封闭岛屿（不存在边界上的点）
     * @param i
     * @param j
     * @return
     */
    private boolean dfs(int i, int j) {
        int m = grid.length, n = grid[0].length;
        // 边界位置点，返回 false
        if (i < 0 || i >= m || j < 0 || j >= n) {
            return false;
        }
        // 非边界位置，已访问过，或 不是岛屿点，不为0，返回 true
        if (grid[i][j] == 1) {
            return true;
        }
        // 标记已访问
        grid[i][j] = 1;
        // 初始化返回值
        boolean ans = true;
        // dfs 上下左右四个点
        for (int k = 0; k < dirs.length - 1; k++) {
            // 如果四个位置向外扩散都没有到达边界点（返回值为true），那么这个联通分量才是 封闭岛屿
            ans &= dfs(i + dirs[k], j + dirs[k + 1]);
        }
        // 返回
        return ans;
    }
}
