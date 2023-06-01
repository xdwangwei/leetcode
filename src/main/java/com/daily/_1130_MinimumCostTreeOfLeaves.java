package com.daily;

import java.util.concurrent.ForkJoinPool;

/**
 * @author wangwei
 * @date 2023/6/1 20:03
 * @description: _1130_MinimumCostTreeOfLeaves
 *
 * 1130. 叶值的最小代价生成树
 * 给你一个正整数数组 arr，考虑所有满足以下条件的二叉树：
 *
 * 每个节点都有 0 个或是 2 个子节点。
 * 数组 arr 中的值与树的中序遍历中每个叶节点的值一一对应。
 * 每个非叶节点的值等于其左子树和右子树中叶节点的最大值的乘积。
 * 在所有这样的二叉树中，返回每个非叶节点的值的最小可能总和。这个和的值是一个 32 位整数。
 *
 * 如果一个节点有 0 个子节点，那么该节点为叶节点。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：arr = [6,2,4]
 * 输出：32
 * 解释：有两种可能的树，第一种的非叶节点的总和为 36 ，第二种非叶节点的总和为 32 。
 * 示例 2：
 *
 *
 * 输入：arr = [4,11]
 * 输出：44
 *
 *
 * 提示：
 *
 * 2 <= arr.length <= 40
 * 1 <= arr[i] <= 15
 * 答案保证是一个 32 位带符号整数，即小于 231 。
 * 通过次数20,229提交次数28,615
 */
public class _1130_MinimumCostTreeOfLeaves {

    /**
     * 方法一：动态规划
     * 已知数组 arr 与二叉树的中序遍历的所有叶子节点对应，并且二叉树的每个节点都有 0 个节点或 2 个节点。
     *
     * 考虑数组 arr 可以生成的所有二叉树，
     *
     * 我们可以将 arr 切分成任意两个非空子数组，分别对应左子树和右子树，
     *
     * 然后递归地对两个非空子树组执行相同的操作，直到子数组大小等于 1，即叶子节点，那么一种切分方案对应一个合法的二叉树。
     *
     * 使用 dp[i][j] 表示子数组 [i,j](i≤j) 对应的子树所有非叶子节点的最小总和，那么 dp[i][j] 可以通过切分子树求得，
     *
     * 状态转移方程如下：
     *
     *      dp[i][j] = 0;   i == j 空树
     *      dp[i][j] = min(dp[i][j], dp[i][k] + dp[k + 1][j] + g[i][k] * g[k + 1][j]); i < j, i <= k < j
     *
     * 其中 g[i][k] 表示 [i,k]（叶子节点） 中的最大值，可以在 dp 的迭代过程中顺便计算。
     *
     * 注意到 dp 的迭代中需要用到 dp[k+1][j] 和 g[k + 1][j]，而 k+1 > i，因此 dp 迭代时 i 需要 倒序遍历
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/minimum-cost-tree-from-leaf-values/solution/xie-zhi-de-zui-xiao-dai-jie-sheng-cheng-26ozf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param arr
     * @return
     */
    public int mctFromLeafValues(int[] arr) {
        int n = arr.length;
        // 表示子数组 [i,j](i≤j) 对应的子树所有非叶子节点的最小总和
        int[][] f = new int[n][n];
        // g[i][k] 表示 [i,k]（叶子节点） 中的最大值，可以在 dp 的迭代过程中顺便计算。
        int[][] g = new int[n][n];
        // i 要倒序遍历
        for (int i = n - 1; i >= 0; --i) {
            // 因为 j 从 i + 1 开始（保证树非空），初始化 g[i][i] = arr[i]
            g[i][i] = arr[i];
            // 遍历 j
            for (int j = i + 1; j < n; ++j) {
                // 更新计算 g[i][j]
                g[i][j] = Math.max(g[i][j - 1], arr[j]);
                // 求 f[][] 最小值，初始化为最大值
                f[i][j] = 1 << 30;
                // 枚举分割点k，，此时 f[k+1][...]、g[k+1][...] 必然已经计算过
                for (int k = i; k < j; ++k) {
                    // 求 f[i][j] 最小值  =  min(f[i][j], 左子树最小值 + 右子树最小值 + 当前节点值（左子树最大叶子节点值 * 右子树最大叶子节点值）)
                    f[i][j] = Math.min(f[i][j], f[i][k] + f[k + 1][j] + g[i][k] * g[k + 1][j]);
                }
            }
        }
        // 返回
        return f[0][n - 1];
    }
}
