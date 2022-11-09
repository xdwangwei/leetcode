package com.daily;

/**
 * @author wangwei
 * @date 2022/11/9 11:11
 * @description: _764_LargestPlusSign
 *
 * 764. 最大加号标志
 * 在一个 n x n 的矩阵 grid 中，除了在数组 mines 中给出的元素为 0，其他每个元素都为 1。mines[i] = [xi, yi]表示 grid[xi][yi] == 0
 *
 * 返回  grid 中包含 1 的最大的 轴对齐 加号标志的阶数 。如果未找到加号标志，则返回 0 。
 *
 * 一个 k 阶由 1 组成的 “轴对称”加号标志 具有中心网格 grid[r][c] == 1 ，以及4个从中心向上、向下、向左、向右延伸，长度为 k-1，由 1 组成的臂。注意，只有加号标志的所有网格要求为 1 ，别的网格可能为 0 也可能为 1 。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入: n = 5, mines = [[4, 2]]
 * 输出: 2
 * 解释: 在上面的网格中，最大加号标志的阶只能是2。一个标志已在图中标出。
 * 示例 2：
 *
 *
 *
 * 输入: n = 1, mines = [[0, 0]]
 * 输出: 0
 * 解释: 没有加号标志，返回 0 。
 *
 *
 * 提示：
 *
 * 1 <= n <= 500
 * 1 <= mines.length <= 5000
 * 0 <= xi, yi < n
 * 每一对 (xi, yi) 都 不重复
 * 通过次数12,099提交次数22,673
 */
public class _764_LargestPlusSign {


    /**
     * 预处理 + 模拟
     *
     * 假设点 (x, y) 为中心的“+”所能够取得最大长度 k，取决于以点 (x,y) 为端点，上下左右四联通方向中「最短的连续 1 的长度」。
     *
     * 基于此，我们可以建立四个大小为 n×n 的矩阵（二维数组）leftOnes、rightOnes、upOnes 和 downOnes
     * 分别代表四个方向上以(x,y)为端点的连续 1 的前缀数：
     *
     * 数据范围为 500，预处理前缀数组复杂度为 O(n^2))，统计答案复杂度为 O(n^2)，时间复杂度没有问题。
     *
     * 再考虑空间，建立四个方向的前缀数组所需空间为 500×500×4=10^6，即使加上原矩阵 g 也不会有 MLE 风险，空间复杂度也没有问题。
     *
     * 另外，建立格子时，默认全部取值为1，只有指定位置为0，我们将0和1反向考虑，就可以省略初始化格子值为1的操作
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/largest-plus-sign/solution/by-ac_oier-q932/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param mines
     * @return
     */
    public int orderOfLargestPlusSign(int n, int[][] mines) {
        int[][] grid = new int[n][n];
        int[][] upOnes = new int[n][n];
        int[][] downOnes = new int[n][n];
        int[][] leftOnes = new int[n][n];
        int[][] rightOnes = new int[n][n];
        // 反向考虑0和1，省略掉赋值操作，默认全为0
        // 指定位置赋值为1
        for (int[] mine : mines) {
            int i = mine[0], j = mine[1];
            grid[i][j] = 1;
        }
        // 遍历，计算每个点为中心，四个方向上连续0（原本是1）的最大长度，
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // (i,j)位置是从上往下，从左往右，能够用来更新 左边和上边连续0的个数
                if (grid[i][j] == 0) {
                    leftOnes[i][j] = j > 0 ? leftOnes[i][j - 1] + 1 : 1;
                    upOnes[i][j] = i > 0 ? upOnes[i - 1][j] + 1 : 1;
                }
                // (n-i-1,n-j-1)位置是从下网上，从右往左，能够用来更新 下边和右边连续0的个数
                if (grid[n - i - 1][n - j - 1] == 0) {
                    rightOnes[n - i - 1][n - j - 1] = j > 0 ? rightOnes[n - i - 1][n - j] + 1 : 1;
                    downOnes[n - i - 1][n - j - 1] = i > 0 ? downOnes[n - i][n - j - 1] + 1 : 1;
                }
            }
        }
        int ans = 0;
        // 每个点为中心的“+”的边长，取决于上下左右四个方向连续“0”的长度的最小值
        // 在所有点为中心的“+”的边长中，取最大值
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                int cur = Math.min(Math.min(leftOnes[i][j], rightOnes[i][j]), Math.min(upOnes[i][j], downOnes[i][j]));
                ans = Math.max(ans, cur);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        _764_LargestPlusSign obj = new _764_LargestPlusSign();
        obj.orderOfLargestPlusSign(5, new int[][]{{4, 2}});
    }
}
