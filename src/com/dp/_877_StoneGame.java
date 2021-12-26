package com.dp;

/**
 * @author wangwei
 * 2020/7/24 15:23
 * <p>
 * 亚历克斯和李用几堆石子在做游戏。[偶数堆]石子排成一行，每堆都有正整数颗石子piles[i]。
 * <p>
 * 游戏以谁手中的石子最多来决出胜负。石子的总数是[奇数]，所以没有平局。
 * <p>
 * 亚历克斯和李轮流进行，亚历克斯先开始。 每回合，玩家从行的开始或结束处取走整堆石头。 这种情况一直持续到没有更多的石子堆为止，此时手中石子最多的玩家获胜。
 * <p>
 * 假设亚历克斯和李都发挥出最佳水平，当亚历克斯赢得比赛时返回true，当李赢得比赛时返回false。
 * <p>
 *
 * <p>
 * 示例：
 * <p>
 * 输入：[5,3,4,5]
 * 输出：true
 * 解释：
 * 亚历克斯先开始，只能拿前 5 颗或后 5 颗石子 。
 * 假设他取了前 5 颗，这一行就变成了 [3,4,5] 。
 * 如果李拿走前 3 颗，那么剩下的是 [4,5]，亚历克斯拿走后 5 颗赢得 10 分。
 * 如果李拿走后 5 颗，那么剩下的是 [3,4]，亚历克斯拿走后 4 颗赢得 9 分。
 * 这表明，取前 5 颗石子对亚历克斯来说是一个胜利的举动，所以我们返回 true 。
 *
 * <p>
 * 提示：
 * <p>
 * 2 <= piles.length <= 500
 * piles.length 是偶数。
 * 1 <= piles[i] <= 500
 * sum(piles)是奇数。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/stone-game
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _877_StoneGame {

    /**
     * 注意，石头的堆的数量为偶数，所以你们两人拿走的堆数一定是相同的。石头的总数为奇数，也就是你们最后不可能拥有相同多的石头，一定有胜负之分。
     * 举个例子，piles=[2, 1, 9, 5]，你先拿，可以拿 2 或者 5，你选择 2。
     * piles=[1, 9, 5]，轮到对手，可以拿 1 或 5，他选择 5。
     * piles=[1, 9] 轮到你拿，你拿 9。
     * 最后，你的对手只能拿 1 了。
     * 这样下来，你总共拥有 2 + 9 = 11 颗石头，对手有 5 + 1 = 6 颗石头，你是可以赢的，所以算法应该返回 true。
     * 你看到了，并不是简单的挑数字大的选，为什么第一次选择 2 而不是 5 呢？因为 5 后面是 9，你要是贪图一时的利益，就把 9 这堆石头暴露给对手了，那你就要输了。
     * 这也是强调双方都很聪明的原因，算法也是求最优决策过程下你是否能赢。
     * 这道题又涉及到两人的博弈，也可以用动态规划算法暴力试，比较麻烦。但我们只要对规则深入思考，就会大惊失色：只要你足够聪明，你是必胜无疑的，因为你是先手。
     * <p>
     * 这是为什么呢，因为题目有两个条件很重要：一是石头总共有偶数堆，石头的总数是奇数。这两个看似增加游戏公平性的条件，反而使该游戏成为了一个割韭菜游戏。我们以 piles=[2, 1, 9, 5] 讲解，假设这四堆石头从左到右的索引分别是 1，2，3，4。
     * 如果我们把这四堆石头按索引的奇偶分为两组，即第 1、3 堆和第 2、4 堆，那么这两组石头的数量一定不同，也就是说一堆多一堆少。因为石头的总数是奇数，不能被平分。
     * 而作为第一个拿石头的人，你可以控制自己拿到所有偶数堆，或者所有的奇数堆。
     * 你最开始可以选择第 1 堆或第 4 堆。如果你想要偶数堆，你就拿第 4 堆，这样留给对手的选择只有第 1、3 堆，他不管怎么拿，第 2 堆又会暴露出来，你就可以拿。同理，如果你想拿奇数堆，你就拿第 1 堆，留给对手的只有第 2、4 堆，他不管怎么拿，第 3 堆又给你暴露出来了。
     * 也就是说，你可以在第一步就观察好，奇数堆的石头总数多，还是偶数堆的石头总数多，然后步步为营，就一切尽在掌控之中了。
     */
    public boolean stoneGame1(int[] piles) {
        return true;
    }

    /**
     * 动态规划+二维数组
     * <p>
     * 判断的结果为先拿者是否可以拿到较多的石子。可以将问题转换为先拿者的石子数相对于后拿者的石子数的差值是否为正数，
     * 即先拿者的石子数是否多于后拿者的石子数。所以在依次拿石子的过程中，依次判断先拿者相对于后拿者的石子数即可。
     * <p>
     * 不妨以 f(i,j)表示对于下标 i 到下标 j的 (j-i+1) 堆石子，当前选手相对于对手能够多出的石子数。
     * 若 i==j，则明显当前选手相对于对手多出的石子数为 f(i,j)=piles[i]，因为只有一堆石子。
     * <p>
     * 若有两堆或多堆石子，则 f(i,j)=max(piles[i]-f(i+1,j), piles[j]-f(i,j-1))，
     * 其中 f(i+1,j)表示对手相对于当前选手多出的石子，
     * 若当前选手选择 piles[i]，所以当前选手相对于对手多出的石子为 piles[i]-f(i+1,j)。
     * 同理若当前选手选择 piles[j] ，则 能够多出的石子为 piles[j]-f(i,j-1)) 。所以取两种情况中的最大值。
     * <p>
     * 以 dp[i][j]二维数组存储 f(i,j) 的值，递推过程如下图所示：
     * <p>
     * <p>
     * 因为初始情况只知道 i==j，即对角线上的 dp 值，所以推导 dp 数组按照红色序号顺序进行。
     * dp[i][j] = Math.max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
     * i需要倒序遍历 [i,j]是有效区间， j < i 不用考虑
     * <p>
     * 作者：cliant
     * 链接：https://leetcode-cn.com/problems/stone-game/solution/dong-tai-gui-hua-by-cliant/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public boolean stoneGame2(int[] piles) {
        int n = piles.length;
        // dp[i][j]表示对piles[i...j]堆石子，先手能够比后手多出多少石子
        int[][] dp = new int[n][n];
        // base case，若i == j 只有一堆石子，全归先手
        for (int i = 0; i < n; i++) {
            dp[i][i] = piles[i];
        }
        // i倒序遍历
        for (int i = n - 1; i >= 0; --i) {
            // j <i 不用考虑，j = i 以初始化
            for (int j = i + 1; j < n; j++) {
                // 先手选择piles[i]或piles[j]
                dp[i][j] = Math.max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }
        // 若对于全部石子进行选择，先手能比后手多多少石子，若大于0，先手胜利
        return dp[0][n - 1] > 0;
    }

    /**
     * 动态规划+一维数组
     * <p>
     * 观察上图可知，得到 dp[0][1]的值之后，就不再需要 dp[0][0] 得值。
     * 同理得到 dp[1][2] 的值之后，就不再需要 dp[1][1] 的值。
     * 即每个 j 值对应的一位数组可以覆盖 j-1 对应的一位数组，
     * 所以只需要一位数组即可得到最终的结果。
     */
    // class Solution:
    //     def stoneGame(self, piles: List[int]) -> bool:
    //     dp=piles[:]
    //             for j in range(1,len(piles)):
    //             for i in range(j-1,-1,-1):
    //     dp[i]=max(piles[i]-dp[i+1],piles[j]-dp[i])
    //         return dp[0]>0
    public boolean stoneGame3(int[] piles) {
        int n = piles.length;
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = piles[i];
        }
        for (int j = 1; j < n; ++j) {
            for (int i = j - 1; i >= 0; --i) {
                dp[i] = Math.max(piles[i] - dp[i + 1], piles[j] - dp[i]);
            }
        }
        return dp[0] > 0;
    }


    /**
     * 我们「石头游戏」改的更具有一般性：
     * 你和你的朋友面前有一排石头堆，用一个数组 piles 表示，piles[i] 表示第 i 堆石子有多少个。你们轮流拿石头，一次拿一堆，但是只能拿走最左边或者最右边的石头堆。所有石头被拿完后，谁拥有的石头多，谁获胜。
     * 石头的[堆数]可以是[任意]正整数，石头的[总数]也可以是[任意]正整数，这样就能打破先手必胜的局面了。
     * 比如有三堆石头 piles = [1, 100, 3]，先手不管拿 1 还是 3，能够决定胜负的 100 都会被后手拿走，后手会获胜。
     * 假设两人都很聪明，请你设计一个算法，返回先手和后手的最后得分（石头总数）之差。比如上面那个例子，先手能获得 4 分，后手会获得 100 分，你的算法应该返回 -96。
     * 这样推广之后，这个问题算是一道 Hard 的动态规划问题了。博弈问题的难点在于，两个人要轮流进行选择，而且都贼精明，应该如何编程表示这个过程呢？
     * 还是强调多次的套路，首先明确 dp 数组的含义，然后和股票买卖系列问题类似，只要找到「状态」和「选择」，一切就水到渠成了。
     * <p>
     * dp[i][j].fir 表示，对于 piles[i...j] 这部分石头堆，先手能获得的最高分数。
     * dp[i][j].sec 表示，对于 piles[i...j] 这部分石头堆，后手能获得的最高分数。
     * <p>
     * 举例理解一下，假设 piles = [3, 9, 1, 2]，索引从 0 开始
     * dp[0][1].fir = 9 意味着：面对石头堆 [3, 9]，先手最终能够获得 9 分。
     * dp[1][3].sec = 2 意味着：面对石头堆 [9, 1, 2]，后手最终能够获得 2 分。
     * <p>
     * 我们想求的答案是先手和后手最终分数之差，按照这个定义也就是 dp[0][n-1].fir - dp[0][n-1].sec，
     * 即面对整个 piles，先手的最优得分和后手的最优得分之差。
     * <p>
     * 状态转移方程
     * 写状态转移方程很简单，首先要找到所有「状态」和每个状态可以做的「选择」，然后择优。
     * 根据前面对 dp 数组的定义，状态显然有三个：开始的索引 i，结束的索引 j，当前轮到的人。
     * dp[i][j][fir or sec]
     * 其中：
     * 0 <= i < piles.length
     * i <= j < piles.length
     * 选择有两个：选择最左边的那堆石头，或者选择最右边的那堆石头
     * <p>
     * dp[i][j].fir = max(piles[i] + dp[i+1][j].sec, piles[j] + dp[i][j-1].sec)
     * dp[i][j].fir = max(    选择最左边的石头堆     ,     选择最右边的石头堆     )
     * # 解释：我作为先手，面对 piles[i...j] 时，有两种选择：
     * # 要么我选择最左边的那一堆石头，然后面对 piles[i+1...j]
     * # 但是此时轮到对方，相当于我变成了后手；
     * # 要么我选择最右边的那一堆石头，然后面对 piles[i...j-1]
     * # 但是此时轮到对方，相当于我变成了后手。
     * <p>
     * if 先手选择左边:
     * dp[i][j].sec = dp[i+1][j].fir
     * if 先手选择右边:
     * dp[i][j].sec = dp[i][j-1].fir
     * # 解释：我作为后手，要等先手先选择，有两种情况：
     * # 如果先手选择了最左边那堆，给我剩下了 piles[i+1...j]
     * # 此时轮到我，我变成了先手；
     * # 如果先手选择了最右边那堆，给我剩下了 piles[i...j-1]
     * # 此时轮到我，我变成了先手。
     * <p>
     * 根据 dp 数组的定义，我们也可以找出 base case，也就是最简单的情况：
     * dp[i][j].fir = piles[i]
     * dp[i][j].sec = 0
     * 其中 0 <= i == j < n
     * # 解释：i 和 j 相等就是说面前只有一堆石头 piles[i]
     * # 那么显然先手的得分为 piles[i]
     * # 后手没有石头拿了，得分为 0
     */
    class Pair {
        int fir, sec;

        Pair(int fir, int sec) {
            this.fir = fir;
            this.sec = sec;
        }
    }

    /* 返回游戏最后先手和后手的得分之差 */
    public boolean stoneGame4(int[] piles) {
        int n = piles.length;
        // 初始化 dp 数组
        Pair[][] dp = new Pair[n][n];
        for (int i = 0; i < n; i++)
            for (int j = i; j < n; j++)
                dp[i][j] = new Pair(0, 0);
        // 填入 base case
        for (int i = 0; i < n; i++) {
            dp[i][i].fir = piles[i];
            dp[i][i].sec = 0;
        }
        // 倒着遍历数组，只需填满右上三角
        for (int i = n - 1; i >= 0; --i) {
            // j = i 作为base case已经处理过
            for (int j = i + 1; j < n; j++) {
                // 先手选择最左边或最右边的分数
                int left = piles[i] + dp[i + 1][j].sec;
                int right = piles[j] + dp[i][j - 1].sec;
                // 套用状态转移方程
                // 先手选择取最左边
                if (left > right) {
                    dp[i][j].fir = left;
                    dp[i][j].sec = dp[i + 1][j].fir;
                // 先手选择取最右边
                } else {
                    dp[i][j].fir = right;
                    dp[i][j].sec = dp[i][j - 1].fir;
                }
            }
        }
        Pair res = dp[0][n - 1];
        return res.fir - res.sec > 0;
    }

    public static void main(String[] args) {
        _877_StoneGame countPrimes = new _877_StoneGame();
        System.out.println(countPrimes.stoneGame2(new int[]{1,100,4,1}));
        System.out.println(countPrimes.stoneGame3(new int[]{1,100,4,1}));
    }

}
