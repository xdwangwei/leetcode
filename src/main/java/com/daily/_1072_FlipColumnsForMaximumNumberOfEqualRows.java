package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/5/15 21:08
 * @description: _1072_FlipColumnsForMaximumNumberOfEqualRows
 *
 * 1072. 按列翻转得到最大值等行数
 * 给定 m x n 矩阵 matrix 。
 *
 * 你可以从中选出任意数量的列并翻转其上的 每个 单元格。（即翻转后，单元格的值从 0 变成 1，或者从 1 变为 0 。）
 *
 * 返回 经过一些翻转后，行与行之间所有值都相等的最大行数 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：matrix = [[0,1],[1,1]]
 * 输出：1
 * 解释：不进行翻转，有 1 行所有值都相等。
 * 示例 2：
 *
 * 输入：matrix = [[0,1],[1,0]]
 * 输出：2
 * 解释：翻转第一列的值之后，这两行都由相等的值组成。
 * 示例 3：
 *
 * 输入：matrix = [[0,0,0],[0,0,1],[1,1,0]]
 * 输出：2
 * 解释：翻转前两列的值之后，后两行由相等的值组成。
 *
 *
 * 提示：
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 300
 * matrix[i][j] == 0 或 1
 * 通过次数16,994提交次数24,197
 */
public class _1072_FlipColumnsForMaximumNumberOfEqualRows {

    /**
     * 方法一：哈希表
     *
     * 我们观察发现，如果矩阵中的两行满足以下条件之一，则它们可以通过翻转某些列的方式得到相等的行：
     *
     *      两行的对应位置元素相等，即其中一行元素为 1,0,0,1，另一行元素也为 1,0,0,1；翻转2、3列
     *      两行的对应位置元素相反，即其中一行元素为 1,0,0,1，另一行元素为 0,1,1,0。翻转1、4列
     *
     * 我们称满足以上条件之一的两行元素为“等价行”，那么题目所求的答案即为矩阵中最多包含等价行的行数。
     *
     * 因此，我们可以遍历矩阵的每一行，将每一行转换成第一个元素为 0 的“等价行”。
     *
     * 具体做法如下：
     *
     * 如果当前行的第一个元素为 0，那么当前行的元素保持不变；
     * 如果当前行的第一个元素为 1，那么我们将当前行的每个元素进行翻转，即 0 变成1,1 变成0。也就是说，我们将以 1 开头的行翻转成以0 开头的“等价行”。
     * 这两种操作可以统一为：
     *      for (int x : row) {
     *          x = x ^ row[0] // x 取值只有0或1，当row[0]=0时，所有元素未改变；当 row[0]=1时，所有元素翻转
     *      }
     *
     * 这样一来，我们只需要用一个哈希表来统计转换后的每一行的出现次数，其中键为转换后的行（可以将所有数字拼接成一个字符串），值为该行出现的次数。
     *
     * 最后，哈希表中值的最大值即为矩阵中最多包含等价行的行数。
     *
     * 如何想到开头那两个规则 ？？
     *
     * 可以从答案出发倒着思考。
     *
     * 假如最后两行为 000、111
     *
     * 考虑倒数第二步是什么样的？
     *      假如是通过翻转最后一列得到，那么 翻转前最后两行为 001、110；两行互补
     *
     * 从这个例子可以发现，对于相同的行，或者「互补」的行，一定存在一种翻转方式，可以使这些行最终全为 0 或者全为 1
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/flip-columns-for-maximum-number-of-equal-rows/solution/ni-xiang-si-wei-pythonjavacgo-by-endless-915k/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/flip-columns-for-maximum-number-of-equal-rows/solution/python3javacgotypescript-yi-ti-yi-jie-ha-gl17/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param matrix
     * @return
     */
    public int maxEqualRowsAfterFlips(int[][] matrix) {
        // 矩阵中最多包含等价行的行数。
        int ans = 0;
        // 不同等价行的数目
        Map<String, Integer> map = new HashMap<>();
        for (int[] row : matrix) {
            char[] str = new char[row.length];
            // 当前行元素转为 等价行
            for (int i = 0; i < row.length; i++) {
                str[i] = (char) (row[0] ^ row[i]);
            }
            // 更新这个等价行的数量+1，更新 ans
            ans = Math.max(ans, map.merge(new String(str), 1, Integer::sum));
        }
        // 返回
        return ans;
    }
}
