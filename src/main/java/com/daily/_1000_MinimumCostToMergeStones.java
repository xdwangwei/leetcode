package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/4/4 21:41
 * @description: _1000_MinimumCostToMergeStones
 *
 * 1000. 合并石头的最低成本
 * 有 N 堆石头排成一排，第 i 堆中有 stones[i] 块石头。
 *
 * 每次移动（move）需要将连续的 K 堆石头合并为一堆，而这个移动的成本为这 K 堆石头的总数。
 *
 * 找出把所有石头合并成一堆的最低成本。如果不可能，返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：stones = [3,2,4,1], K = 2
 * 输出：20
 * 解释：
 * 从 [3, 2, 4, 1] 开始。
 * 合并 [3, 2]，成本为 5，剩下 [5, 4, 1]。
 * 合并 [4, 1]，成本为 5，剩下 [5, 5]。
 * 合并 [5, 5]，成本为 10，剩下 [10]。
 * 总成本 20，这是可能的最小值。
 * 示例 2：
 *
 * 输入：stones = [3,2,4,1], K = 3
 * 输出：-1
 * 解释：任何合并操作后，都会剩下 2 堆，我们无法再进行合并。所以这项任务是不可能完成的。.
 * 示例 3：
 *
 * 输入：stones = [3,5,1,2,6], K = 3
 * 输出：25
 * 解释：
 * 从 [3, 5, 1, 2, 6] 开始。
 * 合并 [5, 1, 2]，成本为 8，剩下 [3, 8, 6]。
 * 合并 [3, 8, 6]，成本为 17，剩下 [17]。
 * 总成本 25，这是可能的最小值。
 *
 *
 * 提示：
 *
 * 1 <= stones.length <= 30
 * 2 <= K <= 30
 * 1 <= stones[i] <= 100
 * 通过次数6,604提交次数14,615
 */
public class _1000_MinimumCostToMergeStones {


    /**
     * 方法：动态规划：
     *
     * 什么时候输出 −1 呢？
     *
     * 从 n 堆变成 1 堆，需要减少 n−1 堆。而每次合并都是把k堆变为1堆，所以每次合并都会减少 k−1 堆，
     * 所以 n−1 必须是 k−1 的倍数，否则返回 -1
     *
     * 因此如果要想把 i到j范围内的石子堆合为一堆，必须满足 j - i = (k - 1) * x
     * （一共 j-i+1 堆，剩余一堆，需要减少 j-i 堆，每一次合并会减少 k-1 堆，因此 j-i 必须被 k-1 整除）
     *
     * 对于不返回 -1 的情况，也就是 n 堆可以通过每次合并k堆的方式 最终合并到1堆，求最小代价
     *
     * 先通过一个例子来思考：比如一开始有7堆石头，k=3，思考最后一步发生了什么？
     *    答：由3堆合并成1堆，可以看作是把左边部分合并为1堆，把右边部分合并为 3-1 堆，
     *
     *    然后我们可以枚举第一堆的来源（合并路径）
     *
     *    有以下几种情况：
     *          就是stones[0],无需合并
     *          由stones[0]到stones2]合并
     *          由stones[0]到stones4通过2次合并得到
     *          ....
     *          由 stones[0] 到 stones[0 + 2*x] 通过 合并x 次得到，（能够合并成1堆的前提是必须是k-1的整数倍）
     *
     *    对于右边剩余的3-1个石头堆，需要计算的问题是：
     *          把这些石头堆合并成2堆的最低成本（子问题）
     *
     * 找到了原问题和子问题的关系，就能通过递归解决
     *
     * 定义 dfs(i,j,p) 表示 把stones[i] 到 stones[j 合并成 p 堆 的 最低成本
     *
     * 比如上述例子中：
     *      目标是计算  dfs(0,6,1)，k=3
     *      那么最后一步必然是3合1，因此 dfs(0,6,1) 等于 dfs(0,6,3) + ∑stones(0...7) （3合1的代价）
     *      对于 dfs(0, 6, 3) 枚举第一堆的来源
     *      那么 dfs(0,6,3) 等于下面几种情况的最小值
     *              dfs(0,0,1) + dfs(1,6,2)
     *              dfs(0,2,1) + dfs(3,6,2)
     *              dfs(0,4,1) + dfs(5,6,2)
     *              dfs(0,m,1) + dfs(m+1, 6, 2), 其中 m = 0 + (3-1) * x
     *
     * 一般化
     *      dfs(i,j,1) = dfs(i,j,k) +  ∑stones[i...j]，其中 子数组和可以用前缀和优化
     *      dfs(i,j,p) = min{ dfs(i,m,1) + dfs(m+1,j,p-1) }   其中 p > 1， m < j 且 m = i + (k-1)*x
     *
     *      从递推表达式看出 dp[i]... 用到 dp[m]...，而 m 必然大于等于 i，所以 dp 迭代时 i 需要倒序遍历
     *
     * base case：
     *      因为我们求dp[][][]最小值，所以初始化 dp 元素为 inf
     *      dfs(i,i,1)=0，只有一堆石子，合并成1堆，那无需合并，代价为0
     *
     * 最终返回 dp[0][n-1][1]，把所有石头堆合并成一堆的最小代价
     *
     * @param stones
     * @param k
     * @return
     */
    public int mergeStones(int[] stones, int k) {
        int n = stones.length;
        // 从 n 堆变成 1 堆，需要减少 n−1 堆。而每次合并都是把k堆变为1堆，所以每次合并都会减少 k−1 堆，
        // 所以 n−1 必须是 k−1 的倍数，否则返回 -1
        if ((n - 1) % (k - 1) != 0) {
            return -1;
        }
        // 前缀和数组
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; ++i) {
            sum[i + 1] = sum[i] + stones[i];
        }
        int inf = 1 << 20;
        // 定义 dfs(i,j,p) 表示 把stones[i] 到 stones[j 合并成 p 堆 的 最低成本
        int[][][] dp = new int[n][n][k + 1];
        // 求dp[][][]最小值，所以初始化 dp 元素为 inf
        for (int[][] row : dp) {
            for (int[] col : row) {
                Arrays.fill(col, inf);
            }
        }
        // base case
        // dfs(i,i,1) = 0，只有一堆石子，合并成1堆，那无需合并，代价为0
        // dfs(i,i,j) j > 1,没有意义，一堆石子不可能合并为多个堆
        // 所以迭代时，j 从 i+1 开始
        for (int i = 0; i < n; ++i) {
            dp[i][i][1] = 0;
        }
        // dfs(i,j,p) = min{ dfs(i,m,1) + dfs(m+1,j,p-1) }, p > 1，p 从 2 开始
        // dp[i]... 用到 dp[m]...，而 m 必然大于等于 i，所以 dp 迭代时 i 需要倒序遍历
        for (int i = n - 1; i >= 0; --i) {
            // j 从 i  + 1 开始
            for (int j = i + 1; j < n; ++j) {
                // p 从 2 开始，得到 dp[i][j][p]
                for (int p = 2; p <= k; ++p) {
                    // 分成 第1堆 和 p-1 堆，枚举第1堆的来源，来自 i 到 i+(k-1)x，注意这里是 k-1 不是 p-1，每次必须合并k个堆
                    for (int x = i; x < j; x += k - 1) {
                        dp[i][j][p] = Math.min(dp[i][j][p], dp[i][x][1] + dp[x + 1][j][p - 1]);
                    }
                }
                // dp[i][j][p]...迭代完，计算 dp[i][j][1]
                // dfs(i,j,1) = dfs(i,j,k) +  ∑stones[i...j]， p = 1
                dp[i][j][1] = dp[i][j][k] + sum[j + 1] - sum[i];
            }
        }
        // 返回
        return dp[0][n - 1][1];
    }

    public static void main(String[] args) {
        _1000_MinimumCostToMergeStones obj = new _1000_MinimumCostToMergeStones();
        System.out.println(obj.mergeStones(new int[]{3, 2, 4, 1}, 2));
    }
}
