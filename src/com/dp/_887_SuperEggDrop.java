package com.dp;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author wangwei
 * 2020/7/20 21:28
 *
 * 你将获得K个鸡蛋，并可以使用一栋从1到N共有 N层楼的建筑。
 *
 * 每个蛋的功能都是一样的，如果一个蛋碎了，你就不能再把它掉下去。
 *
 * 你知道存在楼层F ，满足0 <= F <= N 任何从高于 F的楼层落下的鸡蛋都会碎，从F楼层或比它低的楼层落下的鸡蛋都不会破。
 *
 * 每次移动，你可以取一个鸡蛋（如果你有完整的鸡蛋）并把它从任一楼层X扔下（满足1 <= X <= N）。
 *
 * 你的目标是确切地知道 F 的值是多少。
 *
 * 无论 F 的初始值如何，你确定 F 的值的最小移动次数是多少？
 *
 *
 *
 * 示例 1：
 *
 * 输入：K = 1, N = 2
 * 输出：2
 * 解释：
 * 鸡蛋从 1 楼掉落。如果它碎了，我们肯定知道 F = 0 。
 * 否则，鸡蛋从 2 楼掉落。如果它碎了，我们肯定知道 F = 1 。
 * 如果它没碎，那么我们肯定知道 F = 2 。
 * 因此，在最坏的情况下我们需要移动 2 次以确定 F 是多少。
 * 示例 2：
 *
 * 输入：K = 2, N = 6
 * 输出：3
 * 示例 3：
 *
 * 输入：K = 3, N = 14
 * 输出：4
 *
 *
 * 提示：
 *
 * 1 <= K <= 100
 * 1 <= N <= 10000
 *
 */
public class _887_SuperEggDrop {

    /**
     *
     * 这个问题有什么「状态」，有什么「选择」，然后穷举。
     *
     * 「状态」很明显，就是当前拥有的 鸡蛋数 K 和需要测试的 楼层数 N。
     * 随着测试的进行，鸡蛋个数可能减少，楼层的搜索范围会减小，这就是状态的变化。
     *
     * 「选择」其实就是去选择哪层楼扔【第一个】鸡蛋。
     *
     * 现在明确了「状态」和「选择」，动态规划的基本思路就形成了：
     *      肯定是个二维的 dp 数组或者带有两个状态参数的 dp 函数来表示状态转移；外加一个 for 循环来遍历所有选择，择最优的选择更新状态：
     *
     * # 当前状态为 K 个鸡蛋，面对 N 层楼
     * # 返回这个状态下的最优结果
     * def dp(K, N):
     *     int res
     *     # 去哪层楼扔第一个鸡蛋
     *     for 1 <= i <= N:
     *         res = min(res, 这次在第 i 层楼扔鸡蛋)
     *     return res
     *
     * 我们选择在第 i 层楼扔了鸡蛋之后，可能出现两种情况：鸡蛋碎了，鸡蛋没碎。注意，这时候状态转移就来了：
     *
     * 如果鸡蛋碎了，那么鸡蛋的个数 K 应该减一，搜索的楼层区间应该从 [1..N] 变为 [1..i-1] 共 i-1 层楼；
     *
     * 如果鸡蛋没碎，那么鸡蛋的个数 K 不变，搜索的楼层区间应该从 [1..N] 变为 [i+1..N] 共 N-i 层楼。
     *
     * 因为我们要求的是最坏情况下扔鸡蛋的次数，所以鸡蛋在第 i 层楼 碎没碎，选择哪种情况需要进行更多的次数：
     *
     * def dp(K, N):
     *     for 1 <= i <= N:
     *         # 最坏情况下的最少扔鸡蛋次数
     *         res = min(res,
     *                   max(
     *                         dp(K - 1, i - 1), # 碎
     *                         dp(K, N - i)      # 没碎
     *                      ) + 1 # 在第 i 楼扔了一次
     *                  )
     *     return res
     *
     * 递归的 base case 很容易理解：当楼层数 N 等于 0 时，显然不需要扔鸡蛋；当鸡蛋数 K 为 1 时，显然只能线性扫描所有楼层：
     *
     * def dp(K, N):
     *     if K == 1: return N
     *     if N == 0: return 0
     *
     * 这样直接递归会有很多重复计算，只要添加一个备忘录消除重叠子问题即可：
     *
     * 链接：https://leetcode-cn.com/problems/super-egg-drop/solution/ji-ben-dong-tai-gui-hua-jie-fa-by-labuladong/
     * https://leetcode-cn.com/problems/super-egg-drop/solution/dong-tai-gui-hua-zhi-jie-shi-guan-fang-ti-jie-fang/
     * https://leetcode-cn.com/problems/super-egg-drop/solution/ji-dan-diao-luo-by-leetcode-solution-2/
     * https://leetcode-cn.com/problems/super-egg-drop/solution/ji-dan-diao-luo-xiang-jie-by-shellbye/
     */

    /**
     * 自顶向下动态规划(递归，记忆化搜索)加备忘录
     * @param K
     * @param N
     * @return
     */
    public int superEggDrop(int K, int N) {
        return  dp(K, N);
    }

    /**
     * 虽然加了备忘录，但递归内部仍然是线性探测，数据较大时仍然会超时
     */
    HashMap<String, Integer> map = new HashMap<>();
    private int dp8(int k, int n) {
        int[][] dp = new int[k + 1][n + 1];
        for (int j = 1; j <= n; ++j) {
            dp[1][j] = n;
        }
        for (int i = 1; i <= k; ++i) {
            for (int j = 1; j <= n; ++j) {
                for (int x = 1; x <= j; ++x) {
                    dp[i][j] = Math.max(dp[i- 1][x - 1], dp[i][j - x]) + 1;
                }
            }
        }
        return dp[k][n];
    }


    /**
     * k 个鸡蛋，n 层楼，最坏情况下，找到 f 的最小操作次数
     * @param k
     * @param n
     * @return
     */
    private int dp(int k, int n) {
        // 如果只有一个鸡蛋，只能一层一层试，才能保证最坏情况下也能找到F
        if (k == 1) return n;
        // 如果是0层楼，那不用试了
        if (n == 0) return 0;
        String key = k + "," + n;
        // 查备忘录，避免重复计算
        if (map.containsKey(key)) return map.get(key);
        int res = Integer.MAX_VALUE;
        // 第一个鸡蛋去哪一层楼扔
        for (int i = 1; i <= n; ++i) {
            // 在所有选择的结果中选择次数最少那个
            res = Math.min(res,
                    // 扔一次会有两种结果，选择后续需要更多次数的那个
                    Math.max(dp(k, i - 1), dp(k - 1, n - k)) + 1);
        }
        // 当前状态值保存进备忘录
        map.put(key, res);
        return res;
    }

    /**
     * 将递归内部的for循环用二分法取代
     *
     * 我们观察到 dp(K, N)一个关于 N 的单调递增函数，也就是说在鸡蛋数 K 固定的情况下，楼层数 N 越多，需要的步数一定不会变少。
     * 在上述的状态转移方程中，第一项 dp(K-1, X-1)是一个随 X 的增加而单调递增的函数
     * 第二项 dp(K, N-X) 是一个随着 X 的增加而单调递减的函数。
     *
     * 这如何帮助我们来优化这个问题呢？
     * 我们可以想象在一个直角坐标系中，横坐标为 X，纵坐标为 T1 和 T2，所以是一个 “X” 形状
     *
     *  找到一个位置使得它们的最大值最小，也就是找到那个交叉点
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/super-egg-drop/solution/ji-dan-diao-luo-by-leetcode-solution-2/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     */
    private int dp2(int k, int n) {
        // 如果只有一个鸡蛋，只能一层一层试，才能保证最坏情况下也能找到F
        if (k == 1) return n;
        // 如果是0层楼，那不用试了
        if (n == 0) return 0;
        String key = k + "," + n;
        // 查备忘录，避免重复计算
        if (map.containsKey(key)) return map.get(key);
        int res = Integer.MAX_VALUE;
        // 第一个鸡蛋去哪一层楼扔，二分搜索取代 for 循环
        // for (int i = 1; i <= n; ++i) {
        //     int broken = dp8(k - 1, i - 1);
        //     int notBroken = dp8(k, n - i);
        //     res = Math.min(res, Math.max(broken, notBroken) + 1);
        // }
        int left = 1, right = n;
        while (left <= right) {
            // 去这一层扔
            int mid = (left + right) / 2;
            // 如果碎了，后续要试验这么多次
            int broken = dp2(k - 1, mid - 1);
            // 如果没碎，后续要试验这么多次
            int notBroken = dp2(k, n - mid);
            // 扔一次会有两种结果，选择后续需要更多次数的那个
            // res = Math.min(res, Math.max(碎了, 没碎) + 1);
            // 下面就相当于 Math.max(碎了, 没碎) 的展开与二分分类的结合
            // 碎了的大于没碎的
            if (broken > notBroken) {
                // 楼层太高
                right = mid - 1;
                res = Math.min(res, broken + 1);
            } else {
                // 楼层太低
                left = mid + 1;
                res = Math.min(res, notBroken + 1);
            }
        }
        // 当前状态值保存进备忘录
        map.put(key, res);
        return res;
    }

    /**
     * 动态规划自底向上
     *
     * dp[i][j]：一共有 i个鸡蛋 ，j 层楼， 最坏情况下，找到 F 最少实验的次数。
     *      j 表示的是楼层的大小，不是高度（第几层）的意思，例如楼层区间 [8, 9, 10] 的大小为 3。
     *
     * 状态转移：
     * 设指定的楼层为 x，x >= 1 且 x <= j：
     *
     * 如果鸡蛋破碎，测试 F 值的实验就得在 x 层以下做（不包括 x 层），这里已经使用了一个鸡蛋，因此测出 F 值的最少实验次数是：dp[i - 1][x - 1]；
     * 如果鸡蛋完好，测试 F 值的实验就得在 x 层以上做（不包括 x 层），这里这个鸡蛋还能使用，因此测出 F 值的最少实验次数是：dp[i][j - x]，例如总共 8 层，在第 5 层扔下去没有破碎，则需要在 [6, 7, 8] 层继续做实验，因此区间的大小就是 8 - 5 = 3。
     * 最坏情况下，是这两个子问题的较大者，由于在第 x 层扔下鸡蛋算作一次实验，x的值在 1≤x≤j，对于每一个 x 都对应了一组值的最大值，取这些 x 下的最小值（最优子结构），因此：
     *
     * dp[i][j]=
     *      1≤x≤i
     *      min(max(dp[i−1][x−1], dp[i][j-x])+1)
     *
     *      max 指的是，每一次实验的连个结果中，选择更坏的那个结果
     *      min 值得是，在这么多次选择的坏结果中，操作次数最少是多少
     *
     * 解释：
     *
     * 由于仍那一个鸡蛋需要记录一次操作，所以末尾要加上 1；
     * 每一个新值的计算，都参考了比它行数少，列数少的值，这些值一定是之前已经计算出来的，这样的过程就叫做「状态转移」。
     * 这个问题只是状态转移方程稍显复杂，但空间换时间，逐层递推填表的思想依然是常见的动态规划的思路。
     * 一般而言，需要 0 这个状态的值，这里 0 层楼和 0 个鸡蛋是需要考虑进去的，它们的值会被后来的值所参考，并且也比较容易得到
     * 因此表格需要 k + 1 行，n + 1 列。
     *
     * base case
     * 由于 F 值不会超过最大楼层的高度，要求的是最小值，因此初始化的时候，可以叫表格的单元格值设置成一个很大的数，但是这个数肯定也不会超过当前考虑的楼层的高度。
     *
     * 第 0 行：鸡蛋个数为 0 的时候，不管楼层为多少，也测试不出鸡蛋的 F 值，故全为 0，虽然不符合题意，但是这个值有效，它在后面的计算中会被用到；
     * 第 1 行：鸡蛋个数为 1 的时候，这是一种极端情况，要试出 F 值，最少次数就等于楼层高度；(为了保证一定能测出来，只能一层一层去实验)
     * 第 0 列：楼层为 0 的时候，不管鸡蛋个数多少，都测试不出鸡蛋的 F 值，故全为 0；
     * 第 1 列：楼层为 1 的时候，0 个鸡蛋的时候，扔 0 次，1 个以及 1 个鸡蛋以上只需要扔 1 次；
     *
     * 作者：liweiwei1419
     * 链接：https://leetcode-cn.com/problems/super-egg-drop/solution/dong-tai-gui-hua-zhi-jie-shi-guan-fang-ti-jie-fang/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param k
     * @param n
     * @return
     */
    public int superEggDrop2(int k, int n) {
        // dp[i][j]：一共有 i个鸡蛋 ，j 层楼， 最坏情况下，找到 F 最少实验的次数。
        int[][] dp = new int[k + 1][n + 1];
        // 求最小值，则初始化为一个 MAX，注意别溢出
        for (int i = 2; i <= k; ++i) {
            Arrays.fill(dp[i], Integer.MAX_VALUE / 2);
        }
        // base case,
        // i = 0 时，0 个鸡蛋，全为 0，默认就是0
        // i = 1 时，1 个鸡蛋，全为 j，
        for (int j = 0; j <= n; ++j) {
            dp[1][j] = j;
        }
        // j = 0 时，0 层楼， 全为 0，
        // j = 1 时，1 层楼，若 0 个鸡蛋，则 为 0， 若 1 或 多 个鸡蛋，也只需要 1 次，这里比较重要，也容易忘掉
        for (int i = 1; i <= k; ++i) {
            dp[i][0] = 0;
            dp[i][1] = 1;
        }
        // 状态转移，i 个鸡蛋，j 个楼层
        for (int i = 2; i <= k; ++i) {
            for (int j = 2; j <= n; ++j) {
                // 第一个鸡蛋可以选择 1 到 j 之间任意一个楼层去扔
                for (int x = 1; x <= j; ++x) {
                    // 每次扔完有两种结果，选择其中不好的那个结果
                    // 在这么多的选择产生的所有不好结果中，选择最小值
                    dp[i][j] = Math.min(dp[i][j], Math.max(dp[i - 1][x - 1], dp[i][j - x]) + 1);
                }
            }
        }
        // 返回， k 个鸡蛋， n 层楼，最坏情况下，找到 F 最少实验的次数。
        return dp[k][n];
    }

    /**
     * superEggDrop2 会超时，因为 对于dp[i][j]的状态转移，第一个鸡蛋可以去[1, j]任意一个楼层去扔，而 线性扫描太慢，借助之间分析的 递增递减交叉特性，改成 二分搜索
     * @param k
     * @param n
     * @return
     */
    public int superEggDrop2Plus(int k, int n) {
        // dp[i][j]：一共有 i个鸡蛋 ，j 层楼， 最坏情况下，找到 F 最少实验的次数。
        int[][] dp = new int[k + 1][n + 1];
        // 求最小值，则初始化为一个 MAX，注意别溢出
        for (int i = 2; i <= k; ++i) {
            Arrays.fill(dp[i], Integer.MAX_VALUE / 2);
        }
        // base case,
        // i = 0 时，0 个鸡蛋，全为 0，默认就是0
        // i = 1 时，1 个鸡蛋，全为 j，
        for (int j = 0; j <= n; ++j) {
            dp[1][j] = j;
        }
        // j = 0 时，0 层楼， 全为 0，
        // j = 1 时，1 层楼，若 0 个鸡蛋，则 为 0， 若 1 或 多 个鸡蛋，也只需要 1 次，这里比较重要，也容易忘掉
        for (int i = 1; i <= k; ++i) {
            dp[i][0] = 0;
            dp[i][1] = 1;
        }
        // 状态转移，i 个鸡蛋，j 个楼层
        for (int i = 2; i <= k; ++i) {
            for (int j = 2; j <= n; ++j) {
                // 第一个鸡蛋可以选择 1 到 j 之间任意一个楼层去扔
                // for (int x = 1; x <= j; ++x) {
                //     // 每次扔完有两种结果，选择其中不好的那个结果
                //     // 在这么多的选择产生的所有不好结果中，选择最小值
                //     dp[i][j] = Math.min(dp[i][j], Math.max(dp[i - 1][x - 1], dp[i][j - x]) + 1);
                // }
                // 改用 二分搜索
                int lo = 1, hi = j;
                while (lo <= hi) {
                    // 去这一层扔
                    int mid = (lo + hi) / 2;
                    // 如果碎了，要试验这么多次
                    int broken = dp[i - 1][mid - 1];
                    // 如果没碎，要试验这么多次
                    int notBroken = dp[i][j - mid];
                    // 扔一次会有两种结果，选择后续需要更多次数的那个
                    // res = Math.min(res, Math.max(碎了, 没碎) + 1);
                    // 下面就相当于 Math.max(碎了, 没碎) 的展开与二分分类的结合
                    // 碎了的大于没碎的
                    if (broken > notBroken) {
                        // 楼层太高
                        hi = mid - 1;
                        dp[i][j] = Math.min(dp[i][j], broken + 1);
                    } else if (broken < notBroken) {
                        // 楼层太低
                        lo = mid + 1;
                        dp[i][j] = Math.min(dp[i][j], notBroken + 1);
                    } else {
                        dp[i][j] = Math.min(dp[i][j], broken + 1);
                        break;
                    }
                }
            }
        }
        // 返回， k 个鸡蛋， n 层楼，最坏情况下，找到 F 最少实验的次数。
        return dp[k][n];
    }

    /**
     * 重新定义状态转移
     *
     * 再回顾一下我们之前定义的 dp 数组含义：def dp(k, n) -> int
     *          # 当前状态为 k 个鸡蛋，面对 n 层楼 返回这个状态下确定F，最少的扔鸡蛋次数
     * 用 dp 数组表示的话也是一样的：dp[k][n] = m
     *
     * 按照这个定义，就是【确定当前的鸡蛋个数和面对的楼层数】，就知道最小扔鸡蛋次数。最终我们想要的答案就是 dp(K, N) 的结果。
     * 这种思路下，肯定要穷举所有可能的扔法的，用二分搜索优化也只是做了「剪枝」，减小了搜索空间，但本质思路没有变，还是穷举
     *
     *
     *
     * 现在，我们稍微修改 dp 数组的定义，【确定当前的鸡蛋个数和最多允许的扔鸡蛋次数】，就知道能够确定 F 的最高楼层数。
     * 具体来说是这个意思：
     *      dp[k][m] = n
     *             当前有 k 个鸡蛋，可以尝试扔 m 次鸡蛋
     *             这个状态下，最坏情况下最多能确切测试一栋 n 层的楼的F
     *
     * 比如说 dp[1][7] = 7 表示：
     *    现在有 1 个鸡蛋，允许你扔 7 次;
     *    这个状态下你最多能确定出楼层数为7的楼的F，
     *   （一层一层线性探查嘛）
     *
     * 我们最终要求的其实是扔鸡蛋次数 m，但是这时候 m 在状态之中而不是 dp 数组的结果，
     * 可以这样处理：
     * int superEggDrop(int K, int N) {
     *
     *     int m = 0;
     *     while (dp[K][m] < N) {
     *         m++;
     *         // 状态转移...
     *     }
     *     return m;
     * }
     * 题目不是给你 K 鸡蛋，N 层楼，让你求最坏情况下最少的测试次数 m 吗？
     * while 循环结束的条件是 dp[K][m] == N，也就是给你 K 个鸡蛋，测试 m 次，最坏情况下最多能测出 N 层楼。
     *
     * 不用和之前一样在递归内部有个for循环，尝试在每一层扔，取每一次扔之后两种结果中的更大值，再在全部结果中取最小值
     *  现在全部结果都是一样的，就与下面两个事实。所以这个for就没有了
     * 无论你在哪层楼扔鸡蛋，鸡蛋只可能摔碎或者没摔碎，碎了的话就测楼下，没碎的话就测楼上。
     * 无论你上楼还是下楼，总的能确定的楼层数 = 楼上的楼层数 + 楼下的楼层数 + 1（当前这层楼）。
     *
     * dp[k][m] = dp[k][m - 1] + dp[k - 1][m - 1] + 1
     *
     * dp[k][m - 1] 就是没碎，楼上的楼层数，因为鸡蛋个数 k 不变，最多能扔鸡蛋次数 m 减一；
     * dp[k - 1][m - 1] 就是碎了，楼下的楼层数，因为鸡蛋个数 k 减一，同时扔鸡蛋次数 m 减一。
     *
     */
    public int superEggDrop3(int K, int N) {
        int[][] dp = new int[K + 1][N + 1];
        // base case:
        // dp[0][..] = 0
        // dp[..][0] = 0
        // Java 默认初始化数组都为 0

        // 最多允许扔m次
        int m = 0;
        // 这种情况下不足以确定出楼层数为N的楼的F
        while (dp[K][m] < N) {
            // 就放宽一下条件
            m++;
            // 再重新计算一次放宽条件后给定不同鸡蛋数时最高能确定F的楼层数，递推得到最新的dp[K][m]
            for (int k = 1; k <= K; ++k)
                dp[k][m] = dp[k][m - 1] + dp[k - 1][m - 1] + 1;
        }
        return m;
    }

}
