package com.daily;

/**
 * @author wangwei
 * @date 2022/12/2 18:12
 * @description: _1769_MinimumOperationsToMoveAllBallsToBoxes
 *
 * 1769. 移动所有球到每个盒子所需的最小操作数
 * 有 n 个盒子。给你一个长度为 n 的二进制字符串 boxes ，其中 boxes[i] 的值为 '0' 表示第 i 个盒子是 空 的，而 boxes[i] 的值为 '1' 表示盒子里有 一个 小球。
 *
 * 在一步操作中，你可以将 一个 小球从某个盒子移动到一个与之相邻的盒子中。第 i 个盒子和第 j 个盒子相邻需满足 abs(i - j) == 1 。注意，操作执行后，某些盒子中可能会存在不止一个小球。
 *
 * 返回一个长度为 n 的数组 answer ，其中 answer[i] 是将所有小球移动到第 i 个盒子所需的 最小 操作数。
 *
 * 每个 answer[i] 都需要根据盒子的 初始状态 进行计算。
 *
 *
 *
 * 示例 1：
 *
 * 输入：boxes = "110"
 * 输出：[1,1,3]
 * 解释：每个盒子对应的最小操作数如下：
 * 1) 第 1 个盒子：将一个小球从第 2 个盒子移动到第 1 个盒子，需要 1 步操作。
 * 2) 第 2 个盒子：将一个小球从第 1 个盒子移动到第 2 个盒子，需要 1 步操作。
 * 3) 第 3 个盒子：将一个小球从第 1 个盒子移动到第 3 个盒子，需要 2 步操作。将一个小球从第 2 个盒子移动到第 3 个盒子，需要 1 步操作。共计 3 步操作。
 * 示例 2：
 *
 * 输入：boxes = "001011"
 * 输出：[11,8,5,4,3,4]
 *
 *
 * 提示：
 *
 * n == boxes.length
 * 1 <= n <= 2000
 * boxes[i] 为 '0' 或 '1'
 * 通过次数29,645提交次数33,649
 */
public class _1769_MinimumOperationsToMoveAllBallsToBoxes {


    /**
     * 使用双重循环，第一层循环是遍历所有小球的目的地，第二层循环是计算把所有小球转移到某个目的地盒子的最小操作数，最后返回结果。
     * for (int i = 0; i < n; i++) {
     *             int sm = 0;
     *             for (int j = 0; j < n; j++) {
     *                 if (boxes.charAt(j) == '1') {
     *                     sm += Math.abs(j - i);
     *                 }
     *             }
     *             res[i] = sm;
     *         }
     *
     *
     * 时间复杂度：O(n^2)，需要二重循环。
     *
     *
     * 方法二：根据前一个盒子的操作数得到下一个盒子的操作数
     *
     * 记把所有球转移到当前下标为 i 的盒子的操作数为 operation_i，
     * 初始情况下当前盒子及其左侧有 left_i 个球，右侧有 right_i 个球。
     * 那么，已知这三者的情况下，把所有球转移到当前下标为 i+1 的盒子的操作数 operation_i+1
     * 就可以由 operation_i + left_i - right_i 快速得出，
     *
     * 初始的 operation_0, left_0, right_0 可以通过模拟计算(遍历统计)
     *
     * 将所有球移动到0号盒子的操作数就等于 operation_0
     *
     * operate_i 表示将 所有球（i及其左侧 left_i、i右侧 right_i）移动到 i 号盒子需要的操作次数
     * 那么，对于原来 i 及左侧的 left_i 个球，将他们移动到  i+1 位置，相比较于移动到 i 位置来说，都需要多操作一步，
     *      对于原来 i  右侧的 right_i 个球，将它们移动到 i+1 位置，相比较于移动到 i 位置来说，都可以少操作一步。
     * 那么 res[i+1] = operate_i+1 = operate_i + left_i + right_i
     *
     * 计算完 operation_i+1 后，需要更新 left_i+1 和 right_i+1 ，来为 operate_i+2服务。
     * 若 boxes[i] == '1'，那么 left_i+1 += 1, right_i+1 -= 1
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/minimum-number-of-operations-to-move-all-balls-to-each-box/solution/yi-dong-suo-you-qiu-dao-mei-ge-he-zi-suo-y50e/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param boxes
     * @return
     */
    public int[] minOperations(String boxes) {
        int n = boxes.length();
        int[] res = new int[n];
        // left_0 0号及左边球的个数
        int left = boxes.charAt(0) - '0';
        // right_0 表示 0 号右边球的个数
        // operation_0 表示将所有球移动到 0号盒子需要的操作次数
        int right = 0, operations = 0;
        for (int i = 1; i < n; ++i) {
            // 更新 right_0 及 operation_0
            if (boxes.charAt(i) == '1') {
                right++;
                operations += i;
            }
        }
        // 更新res_0
        res[0] = operations;
        // 通过 operation_i、left_i、right_i 得到 operation_i+1
        for (int i = 1; i < n; ++i) {
            operations += left - right;
            // 更新 left_i+1、right_i+1
            if (boxes.charAt(i) == '1') {
                left++;
                right--;
            }
            res[i] = operations;
        }
        return res;
    }
}
