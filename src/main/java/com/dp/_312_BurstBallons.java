package com.dp;

import java.util.Arrays;

/**
 * @author wangwei
 * 2020/7/21 18:07
 * <p>
 * 有 n 个气球，编号为0 到 n-1，每个气球上都标有一个数字，这些数字存在数组nums中。
 * <p>
 * 现在要求你戳破所有的气球。如果你戳破气球 i ，就可以获得nums[left] * nums[i] * nums[right]个硬币。这里的left和right代表和i相邻的两个气球的序号。注意当你戳破了气球 i 后，气球 left 和气球 right 就变成了相邻的气球。
 * <p>
 * 求所能获得硬币的最大数量。
 * <p>
 * 说明:
 * <p>
 * 你可以假设nums[-1] = nums[n] = 1，但注意它们不是真实存在的所以并不能被戳破。
 * 0 ≤ n ≤ 500, 0 ≤ nums[i] ≤ 100
 * 示例:
 * <p>
 * 输入: [3,1,5,8]
 * 输出: 167
 * 解释: nums = [3,1,5,8] --> [3,5,8] -->   [3,8]   -->  [8]  --> []
 *    coins =  3*1*5      +  3*5*8    +  1*3*8      + 1*8*1   = 167
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/burst-balloons
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _312_BurstBallons {

    private int[][] dict;

    /**
     * 题目说可以认为nums[-1] = nums[n] = 1，那么我们先直接把这两个边界加进去，形成一个新的数组points：
     *
     * int maxCoins(int[] nums) {
     *     int n = nums.length;
     *     // 两端加入两个虚拟气球
     *     int[] points = new int[n + 2];
     *     points[0] = points[n + 1] = 1;
     *     for (int i = 1; i <= n; i++) {
     *         points[i] = nums[i - 1];
     *     }
     *     // ...
     * }
     */

    /**
     * 动态规划 自顶向下 记忆化搜索
     * 这个动态规划问题和我们之前的动态规划系列文章相比有什么特别之处？为什么它比较难呢？
     * <p>
     * 原因在于，这个问题中我们每戳破一个气球nums[i]，得到的分数和该气球相邻的气球nums[i-1]和nums[i+1]是有相关性的。
     * <p>
     * 我们前文 动态规划套路框架详解 说过运用动态规划算法的一个重要条件：[子问题必须独立]。
     * 所以对于这个戳气球问题，如果想用动态规划，必须巧妙地定义dp数组的含义，避免子问题产生相关性，才能推出合理的状态转移方程。
     *
     * 戳气球的操作会导致左右两个区间从不相邻变成相邻，使得后续操作难以处理。
     * 于是我们倒过来看这些操作，将全过程看作是【每次添加一个气球】。
     *
     * 我们定义方法 solve，令 solve(i,j) 表示将【开区间】(i,j)内的位置全部填满气球能够得到的最多硬币数。
     * 由于是开区间，因此区间两端的气球的编号就是 i和 j，对应着 val[i] 和 al[j]。
     *
     * 当 j <= i + 1 时，开区间中没有气球，solve(i,j) 的值为 0；
     *
     * 当 j > i + 1 时，我们枚举开区间 (i,j) 内的全部位置mid，
     * 令 mid 为当前区间第一个添加的气球，该操作能得到的硬币数为val[i] * val[mid] * val[j]。
     * 同时划分出独立的两区间 (i, mid)  和 (mid, j)
     *
     * 可以得到  solve(i, j) = :
     *              0,                                     j <= i + 1
     *              max solve(i, mid) + solve(mid, j) + val[i] * val[mid] * val[j]
     *
     * 为了防止重复计算，我们存储 solve 的结果
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/burst-balloons/solution/chuo-qi-qiu-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int maxCoins(int[] nums) {
        int n = nums.length;
        // 两端加入两个虚拟气球
        int[] points = new int[n + 2];
        points[0] = points[n + 1] = 1;
        for (int i = 1; i <= n; i++) {
            points[i] = nums[i - 1];
        }
        // 初始化备忘录
        dict = new int[n + 2][n + 2];
        for (int[] ints : dict) {
            Arrays.fill(ints, -1);
        }
        // 我们要填满 1 到 n，因为solve是开区间，所以这里是 (0, n + 1)
        return solve(points, 0, n + 1);
    }

    /**
     *
     * solve(i,j) 表示将【开区间】(i,j)内的位置全部填满气球能够得到的最多硬币数
     */
    private int solve(int[] points, int left, int right) {
        // 区间空了，注意是开区间
        if (left + 1 >= right) return 0;
        String key = left + "," + right;
        // 避免重复计算
        if (dict[left][right] != -1) return dict[left][right];
        // 选择一个位置插入第一个气球
        for (int mid = left + 1; mid < right; ++mid) {
            // 这个气球是最后一个，那么和它相邻的左右气球就分别为未取到的左右边界
            int sum = points[left] * points[mid] * points[right];
            // 在这个位置插入所能获得的分数
            sum += solve(points, left, mid) + solve(points, mid, right);
            // 在多个位置中做出最有益的选择
            dict[left][right] = Math.max(dict[left][right], sum);
        }
        return dict[left][right];
    }

    /**
     * 其实你如果觉得讲题目理解成插入气球不太方便，还是可以按戳破气球来理解的，只不过要换一种理解方式
     * 我们的根本目的是为了保证子问题的相互独立性
     *
     * 不论你先戳哪个气球都会造成得到的两个区间合并在一起，所以我们要考虑【最后一个】戳破哪个气球
     *
     * 那么我们可以改变问题：
     *      在一排气球points中，请你戳破气球0和气球n+1之间的所有气球（不包括0和n+1），
     *      使得最终只剩下气球0和气球n+1两个气球，最多能够得到多少分？
     * int[][] dp = new int[n + 2][n + 2];
     * 现在可以定义dp数组的含义：
     *
     * dp[i][j] = x表示，
     *      戳破气球i和气球j之间（开区间，不包括i和j）的所有气球，可以获得的最高分数为x。
     *
     * 那么根据这个定义，题目要求的结果就是dp[0][n+1]的值，
     *
     * 我们需要【反向思考】，想一想气球i和气球j之间【最后一个】被戳破的气球可能是哪一个？
     *
     * 其实气球i和气球j之间的所有气球都可能是最后被戳破的那一个，不防假设为k。回顾动态规划的套路，这里其实已经找到了「状态」和「选择」：i和j就是两个「状态」，最后戳破的那个气球k就是「选择」。
     *
     * 根据刚才对dp数组的定义，如果最后一个戳破气球k，dp[i][j]的值应该为：
     *
     * dp[i][j] = dp[i][k] + dp[k][j]
     *          + points[i]*points[k]*points[j]
     *
     * 【你不是要最后戳破气球k吗？
     *      那得先把开区间(i, k)的气球都戳破，再把开区间(k, j)的气球都戳破；
     *      最后剩下的气球k，相邻的就是气球i和气球j，
     *      这时候戳破k的话得到的分数就是points[i]*points[k]*points[j]。】
     *
     * 那么戳破开区间(i, k)和开区间(k, j)的气球最多能得到的分数是多少呢？
     *      嘿嘿，就是dp[i][k]和dp[k][j]，这恰好就是我们对dp数组的定义嘛！
     *
     * 由于是开区间，dp[i][k]和dp[k][j]不会影响气球k；
     * 而戳破气球k时，旁边相邻的就是气球i和气球j了，最后还会剩下气球i和气球j，
     * 这也恰好满足了dp数组开区间的定义。
     *
     * 那么，对于一组给定的i和j，我们只要穷举i < k < j的所有气球k，选择得分最高的作为dp[i][j]的值即可，这也就是状态转移方程：
     *
     * // 最后戳破的气球是哪个？
     * for (int k = i + 1; k < j; k++) {
     *     // 择优做选择，使得 dp[i][j] 最大
     *     dp[i][j] = Math.max(
     *         dp[i][j],
     *         dp[i][k] + dp[k][j] + points[i]*points[j]*points[k]
     *     );
     * }
     *
     * 而 base case 就是dp[i][j] = 0，其中0 <= i <= n+1, j <= i+1，
     * 因为这种情况下，开区间(i, j)中间根本没有气球可以戳。
     */

    /**
     * 自底向上 动态规划
     *
     * 注意这里 i 要倒序遍历， 因为我们知道的是左下角(j+1 <= i),而要求的是右上角dp[0][n+1]
     * 所以应该从下往上，从左往右遍历，才能保证状态转移时涉及到的元素是已经计算过的
     */
    public int maxCoins2(int[] nums) {
        int n = nums.length;
        // 两端加入两个虚拟气球
        int[] points = new int[n + 2];
        points[0] = points[n + 1] = 1;
        for (int i = 1; i <= n; i++) {
            points[i] = nums[i - 1];
        }
        // dp[i][k] 表示将【开区间】(i,j)内的位置全部填满气球能够得到的最多硬币数
        int[][] dp = new int[n + 2][n + 2];
        // 当 j <= i + 1 时，dict[i][j] = 0
        for (int i = n + 1; i >= 0; --i) {
            // 有效区间
            for (int j = i + 2; j <= n + 1; j++) {
                // 选择第一个插入位置
                for (int k = i + 1; k < j; ++k) {
                    int sum = points[i] * points[k] * points[j];
                    // 在这个位置插入所能获得的分数
                    sum += dp[i][k] + dp[k][j];
                    dp[i][j] = Math.max(dp[i][j], sum);
                }
            }
        }
        return dp[0][n + 1];
    }

    /**
     * 复习算法小炒，重新写的代码，动态规划思路没变，只是 dp[i][j] 代表戳掉 闭区间[i,j]所有气球能够得到的最大金币
     * @param nums
     * @return
     */
    public int maxCoins8(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        int[] vals = new int[n + 2];
        vals[0] = vals[n + 1] = 1;
        for (int i = 1; i <= n; ++i) {
            vals[i] = nums[i - 1];
        }
        int[][] dp = new int[n + 2][n + 2];
        for (int i = n; i >= 1; --i) {
            for (int j = i; j <= n; ++j) {
                for (int k = i; k <= j; ++k) {
                    int temp = vals[k] * vals[i - 1] * vals[j + 1];
                    temp += dp[i][k - 1] + dp[k + 1][j];
                    dp[i][j] = Math.max(dp[i][j], temp);
                }
            }
        }
        return dp[1][n];
    }

}
