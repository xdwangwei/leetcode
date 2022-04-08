package com.array;

/**
 * @author wangwei
 * @date 2022/3/31 9:53
 * <p>
 * 给定一个 n × n 的二维矩阵 matrix 表示一个图像。请你将图像顺时针旋转 90 度。
 * <p>
 * 你必须在 原地 旋转图像，这意味着你需要直接修改输入的二维矩阵。请不要 使用另一个矩阵来旋转图像。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * 输出：[[7,4,1],[8,5,2],[9,6,3]]
 * 示例 2：
 * <p>
 * <p>
 * 输入：matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
 * 输出：[[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
 *  
 * <p>
 * 提示：
 * <p>
 * n == matrix.length == matrix[i].length
 * 1 <= n <= 20
 * -1000 <= matrix[i][j] <= 1000
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/rotate-image
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _48_RotateImage {

    /**
     * 数学分析法
     *
     * 具体来看每一个元素去了那个位置
     *
     * 对于矩阵中的【第一【行】而言，在旋转后，它出现在【倒数第一【列】的位置：
     * 并且，第一行的第 x 个元素在旋转后恰好是倒数第一列的第 x 个元素
     *
     * 这样我们可以得到规律：
     *
     *      对于矩阵中 【第 i 行】 的第 j 个元素，在旋转后，它出现在 【倒数第 i 列】 的第 j 个位置
     *
     * 由于矩阵中的行列从 0开始计数，因此对于矩阵中的元素 matrix[row][col]，在旋转后，它的新位置为 matrix[col][n−row−1]。
     * 但是，这样直接赋值，会覆盖掉原来 matrix[col][n−row−1] 这个位置元素
     *
     * 所以，我们使用一个与 matrix 大小相同的辅助数组 newmatrix 临时存储旋转后的结果。
     * 我们遍历 matrix 中的每一个元素，根据上述规则将该元素存放到 newmatrix 中对应的位置。
     * 在遍历完成之后，再将 newmatrix 中的结果复制到原数组中即可。
     *
     * 这样虽然能达到目的，但是不符合题目要求 ，原地完成更新
     *
     * @param matrix
     */
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        // 临时矩阵，存储 原矩阵每个元素旋转到对应位置后得到的 矩阵，避免中间过程的覆盖
        int[][] matrix_new = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                // 矩阵中 【第 i 行】 的第 j 个元素，在旋转后，它出现在 【倒数第 i 列】 的第 j 个位置
                matrix_new[j][n - i - 1] = matrix[i][j];
            }
        }
        // 再根据临时矩阵，恢复原矩阵
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                matrix[i][j] = matrix_new[i][j];
            }
        }
    }

    /**
     * 方法一找出了规律，但是造成了覆盖，原因是没有找出全部的规律，
     * 比如 a位置 元素 会 变更到 b 位置，b -> c， c -> d， d -> a，
     * 相当于方法一只找出了第一步，现在我们一次性把一圈上的元素转换关系都找出来，最终要完成的是 a->b,b->c,c->d,d->a
     * 现在的目标是 如何在避免覆盖的情况下一次性完成这四个变换。
     * 加入一个临时变量 temp
     * temp = a, a = d, d = c, c = b, b = temp 就完成了
     *
     * 第一个问题是 怎么根据 方法一找到的规律 (矩阵中 【第 i 行】 的第 j 个元素，在旋转后，它出现在 【倒数第 i 列】 的第 j 个位置)
     *                    得到这一圈涉及的多个元素的全部变换关系
     * 第二个问题是 找到一个位置，就能完成一圈元素转换，那么既要避免重复，又要保证能涉及到全部位置，所以应该选择几个元素作为“头”，进行圈转换
     *
     * 关于这两个问题，具体参考官方题解方法二，很清楚
     * https://leetcode-cn.com/problems/rotate-image/solution/xuan-zhuan-tu-xiang-by-leetcode-solution-vu3m/
     * @param matrix
     */
    public void rotate2(int[][] matrix) {
        int n = matrix.length;
        // i,j 取值范围，保证不重复、不遗漏，矩阵正中央的点无需旋转
        // 当 n 为偶数时，我们需要枚举 n^2 / 4 个位置，可以将该图形分为四块，以 4×4 的矩阵为例
        // 当 n 为奇数时，由于中心的位置经过旋转后位置不变，我们需要枚举 (n^2-1) / 4 = ((n−1)/2)×((n+1)/2) 个位置，需要换一种划分的方式，以 5×5 的矩阵为例：
        for (int i = 0; i < n / 2; ++i) {
            for (int j = 0; j < (n + 1) / 2; ++j) {
                // 保存一圈的 头 “a”，完成 a->b,b->c,c->d,d->a
                // temp = a, a = d, d = c, c = b, b = temp
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - 1 - j][i];
                matrix[n - 1 - j][i] = matrix[n - 1 - i][n - 1 - j];
                matrix[n - 1 - i][n - 1 - j] = matrix[j][n - 1 - i];
                matrix[j][n - 1 - i] = temp;
            }
        }
    }

    /**
     * 方法三，也是观察加数学(线代)分析，按行来看，每一行的元素实际上最终换到了一列上
     * 只有按照对角线的对称操作（矩阵转置）是可以轻松完成这一点的，
     * 转置后和目标看起来不一样，但稍加观察就能发现，只需要再将每一行元素进行反转，就得到答案了
     * @param matrix
     */
    public void rotate3(int[][] matrix) {
        int n = matrix.length;
        // 先按主对角线完成对称转换
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        // 再每一行完成反转
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n / 2; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[i][n - 1 - j];
                matrix[i][n - 1 - j] = temp;
            }
        }
    }
}
