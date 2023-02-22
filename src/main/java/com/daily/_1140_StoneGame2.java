package com.daily;

/**
 * @author wangwei
 * @date 2023/2/22 16:23
 * @description: _1140_StoneGame2
 *
 * 1140. 石子游戏 II
 * 爱丽丝和鲍勃继续他们的石子游戏。许多堆石子 排成一行，每堆都有正整数颗石子 piles[i]。游戏以谁手中的石子最多来决出胜负。
 *
 * 爱丽丝和鲍勃轮流进行，爱丽丝先开始。最初，M = 1。
 *
 * 在每个玩家的回合中，该玩家可以拿走剩下的 前 X 堆的所有石子，其中 1 <= X <= 2M。然后，令 M = max(M, X)。
 *
 * 游戏一直持续到所有石子都被拿走。
 *
 * 假设爱丽丝和鲍勃都发挥出最佳水平，返回爱丽丝可以得到的最大数量的石头。
 *
 *
 *
 * 示例 1：
 *
 * 输入：piles = [2,7,9,4,4]
 * 输出：10
 * 解释：如果一开始Alice取了一堆，Bob取了两堆，然后Alice再取两堆。爱丽丝可以得到2 + 4 + 4 = 10堆。如果Alice一开始拿走了两堆，那么Bob可以拿走剩下的三堆。在这种情况下，Alice得到2 + 7 = 9堆。返回10，因为它更大。
 * 示例 2:
 *
 * 输入：piles = [1,2,3,4,5,100]
 * 输出：104
 *
 *
 * 提示：
 *
 * 1 <= piles.length <= 100
 * 1 <= piles[i] <= 104
 * 通过次数15,928提交次数23,117
 */
public class _1140_StoneGame2 {


    /**
     * 方法：前缀和 + 记忆化搜索
     *
     * 由于玩家每次可以拿走前 X 堆的所有石子，也就是说能拿走一个连续区间的石子，因此，我们可以先预处理出一个长度为 n+1 的前缀和数组 s，
     * 其中 s[i] 表示数组 piles 的前 i 个元素的和。
     *
     * 然后我们设计一个函数 dfs(i,m)，表示当前轮到的人可以从数组 piles 的下标 i [开始拿]，且当前的 M 为 m 时，能够拿到的最大石子数。
     * 初始时爱丽丝从下标 0 开始，且 M=1，所以答案为 dfs(0,1)。
     *
     * 函数 dfs(i,m) 的计算过程如下：
     *
     * 如果当前轮到的人可以拿走剩下的所有石子，即 m×2 ≥ n - 1 − i + 1 = n - i，能够拿到的最大石子数为剩下全部石头，即 s[n]−s[i]；
     * 否则，当前轮到的人可以拿走剩下的前 x 堆的所有石子，其中 1 ≤ x ≤ 2m，我们不知道x如何取值才能保证最优，
     *      但由于剩余的石子总数是固定的，如果拿了某几堆石子后，对手能得到的石子数最少，那么自己能得到的石子数就是最多的。
     *      对于每个x取值可以知道下一个人能够得到的最大石子数为 dfs(i+x,max(m,x))
     *      因此，当前轮到的人可以到的最多石子数为 s[n]-s[i] - min(dfs(i+x,max(m,x)))，其中 1 ≤ x ≤ 2m
     *      我们需要枚举所有的 x，取其中的最大值作为函数 dfs(i,m) 的返回值。
     *
     * 为了避免重复计算，我们可以使用记忆化搜索。
     * 注意到上述过程中，实际用到的是 s[n]-s[i]，我们直接结算后缀和
     *
     * 最后，我们返回将 dfs(0,1) 作为答案返回即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/stone-game-ii/solution/python3javacgo-yi-ti-yi-jie-qian-zhui-he-flo7/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    // 记忆化，用包装类就不用提前赋值-1，之后判断是否为null即可
    private Integer[][] memo;

    public int stoneGameII(int[] piles) {
        int n = piles.length;
        memo = new Integer[n][n + 1];
        // 后缀和，suffixSum[i] 表示 sum(piles[i....n-1])
        int[] suffixSum = new int[n];
        suffixSum[n - 1] = piles[n - 1];
        for (int i = n - 2; i >= 0; --i) {
            suffixSum[i] = suffixSum[i + 1] + piles[i];
        }
        // 从0位置开始拿，m=1，能够拿到的石子数目
        return dfs(0, 1, piles, suffixSum);
    }

    /**
     * 记忆化搜索，从 i 位置开始拿，先手可以选择前 x 堆石子（1<=x<=2m），能够拿到的最大石子数
     * @param start
     * @param m
     * @param piles
     * @param suffixSum
     * @return
     */
    private int dfs(int start, int m, int[] piles, int[] suffixSum) {
        int n = piles.length;
        // 如果能够把剩下的全部拿走
        if (2 * m >= n - 1 - start + 1) {
            // 直接返回剩下石子总和
            return suffixSum[start];
        }
        // 如果已经计算过
        if (memo[start][m] != null) {
            return memo[start][m];
        }
        // 可以选择拿前1到2m堆，让下一个人能够拿到的最少，即是我们最优
        int secondMin = suffixSum[start];
        // 枚举当前选择
        for (int x = 1; x <= 2 * m; ++x) {
            // end = start + x - 1; nextStart = end + 1 = start + x;
            // 下一个人作为先手能够拿到的最多石子，取最小值
            // 当前从start位置开始拿，拿走x堆，则下一个人 start+x 位置开始拿，m 更新为 max(m, x)
            secondMin = Math.min(secondMin, dfs(start + x, Math.max(m, x), piles, suffixSum));
        }
        // 剩下全部石子 - 下一人拿到的最少石子 = 我们拿到最多
        // 备忘，返回
        return memo[start][m] = suffixSum[start] - secondMin;
    }

}
