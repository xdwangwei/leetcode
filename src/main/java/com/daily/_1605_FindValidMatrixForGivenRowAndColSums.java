package com.daily;

/**
 * @author wangwei
 * @date 2023/3/14 20:09
 * @description: _1605_FindValidMatrixForGivenRowAndColSums
 * 1605. 给定行和列的和求可行矩阵
 * 给你两个非负整数数组 rowSum 和 colSum ，其中 rowSum[i] 是二维矩阵中第 i 行元素的和， colSum[j] 是第 j 列元素的和。换言之你不知道矩阵里的每个元素，但是你知道每一行和每一列的和。
 *
 * 请找到大小为 rowSum.length x colSum.length 的任意 非负整数 矩阵，且该矩阵满足 rowSum 和 colSum 的要求。
 *
 * 请你返回任意一个满足题目要求的二维矩阵，题目保证存在 至少一个 可行矩阵。
 *
 *
 *
 * 示例 1：
 *
 * 输入：rowSum = [3,8], colSum = [4,7]
 * 输出：[[3,0],
 *       [1,7]]
 * 解释：
 * 第 0 行：3 + 0 = 3 == rowSum[0]
 * 第 1 行：1 + 7 = 8 == rowSum[1]
 * 第 0 列：3 + 1 = 4 == colSum[0]
 * 第 1 列：0 + 7 = 7 == colSum[1]
 * 行和列的和都满足题目要求，且所有矩阵元素都是非负的。
 * 另一个可行的矩阵为：[[1,2],
 *                   [3,5]]
 * 示例 2：
 *
 * 输入：rowSum = [5,7,10], colSum = [8,6,8]
 * 输出：[[0,5,0],
 *       [6,1,0],
 *       [2,0,8]]
 * 示例 3：
 *
 * 输入：rowSum = [14,9], colSum = [6,9,8]
 * 输出：[[0,9,5],
 *       [6,0,3]]
 * 示例 4：
 *
 * 输入：rowSum = [1,0], colSum = [1]
 * 输出：[[1],
 *       [0]]
 * 示例 5：
 *
 * 输入：rowSum = [0], colSum = [0]
 * 输出：[[0]]
 *
 *
 * 提示：
 *
 * 1 <= rowSum.length, colSum.length <= 500
 * 0 <= rowSum[i], colSum[i] <= 108
 * sum(rowSum) == sum(colSum)
 * 通过次数20,588提交次数25,595
 *
 */
public class _1605_FindValidMatrixForGivenRowAndColSums {

    /**
     * 方法：贪心 + 构造
     *
     * 对于 matrix 的每一个位置 matrix[i][j]，0≤i<n 且 0≤j<m，
     * 我们将 matrix[i][j] 设为 min{rowSum[i],colSum[j]}，
     * 然后将 rowSum[i],colSum[j] 同时减去 matrix[i][j] 即可。
     * 当遍历完全部位置后，matrix 即为一个满足要求的答案矩阵。
     *
     * 上述的构造方法的正确性说明如下：
     *
     * 首先我们可以容易得到对于某一个位置 matrix[i][j] 处理完后，rowSum[i]，colSum[j] 一定不会小于 0。
     * 然后我们从第一行开始往最后一行构造，因为初始时
     * ∑rowSum[i]=∑colSum[j]，所以对于第一行显然有 rowSum[0]≤colSum[j]，
     * 所以通过n次操作后（第一行）上述操作一定可以使得 rowSum[0]=0，同时满足 colSum[j]≥0 对于 0≤j<m 恒成立。
     * 然后我们对剩下的  n−1 行和 m 列做同样的处理。当处理完成后，matrix 为一个符合要求的答案矩阵。
     *
     * 在实现的过程中，当遍历过程中 rowSum[i]=0，0≤i<n 时，因为每一个元素为非负整数，所以该行中剩下的元素只能全部为 0，
     * 同理对于 colSum[j]=0，0≤j<m 时，该列中剩下的元素也只能全部为 0。
     * 所以我们可以初始化 matrix 为全零矩阵，在遍历的过程中一旦存在上述情况，则可以直接跳过该行或者列。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/find-valid-matrix-given-row-and-column-sums/solution/gei-ding-xing-he-lie-de-he-qiu-ke-xing-j-u8dj/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param rowSum
     * @param colSum
     * @return
     */
    public int[][] restoreMatrix(int[] rowSum, int[] colSum) {
        int n = rowSum.length, m = colSum.length;
        // 初始化，每个元素都为0
        int[][] matrix = new int[n][m];
        int i = 0, j = 0;
        while (i < n && j < m) {
            // ans[i][j] = min{rowSum[i],colSum[j]}，
            int v = Math.min(rowSum[i], colSum[j]);
            matrix[i][j] = v;
            // 更新 rowSum[i], colSum[j]
            rowSum[i] -= v;
            colSum[j] -= v;
            // 如果 rowSum[i]=0，0≤i<n 时，因为每一个元素为非负整数，所以该行中剩下的元素只能全部为 0，下一行
            if (rowSum[i] == 0) {
                ++i;
            }
            // 如果 colSum[j]=0，0≤j<m 时，该列中剩下的元素也只能全部为 0。下一列
            if (colSum[j] == 0) {
                ++j;
            }
        }
        return matrix;
    }

}
