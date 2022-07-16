package com.recursion;

import java.util.HashMap;

/**
 * @author wangwei
 * 2020/7/20 21:28
 *
 * 你将获得 K 个鸡蛋，并可以使用一栋从 1 到 N  共有 N 层楼的建筑。
 *
 * 每个蛋的功能都是一样的，如果一个蛋碎了，你就不能再把它掉下去。
 *
 * 你知道存在楼层 F ，满足 0 <= F <= N 任何从高于 F 的楼层落下的鸡蛋都会碎，从 F 楼层或比它低的楼层落下的鸡蛋都不会破。
 *
 * 每次移动，你可以取一个鸡蛋（如果你有完整的鸡蛋）并把它从任一楼层 X 扔下（满足 1 <= X <= N）。
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
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/super-egg-drop
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
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
 * 因为我们要求的是最坏情况下扔鸡蛋的次数，所以鸡蛋在第 i 层楼碎没碎，选择哪种情况需要进行更多的次数：
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
public class _887_SuperEggDrop {

    /**
     * 递归加备忘录
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
            // 在所有扔的结果中选择次数最少那个
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
     * ​
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
        // 第一个鸡蛋去哪一层楼扔
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

}
