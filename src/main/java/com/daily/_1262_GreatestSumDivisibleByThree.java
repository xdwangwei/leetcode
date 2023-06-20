package com.daily;

/**
 * @author wangwei
 * @date 2023/6/19 12:30
 * @description: _1262_GreatestSumDivisibleByThree
 *
 * 1262. 可被三整除的最大和
 * 给你一个整数数组 nums，请你找出并返回能被三整除的元素最大和。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [3,6,5,1,8]
 * 输出：18
 * 解释：选出数字 3, 6, 1 和 8，它们的和是 18（可被 3 整除的最大和）。
 * 示例 2：
 *
 * 输入：nums = [4]
 * 输出：0
 * 解释：4 不能被 3 整除，所以无法选出数字，返回 0。
 * 示例 3：
 *
 * 输入：nums = [1,2,3,4,4]
 * 输出：12
 * 解释：选出数字 1, 3, 4 以及 4，它们的和是 12（可被 3 整除的最大和）。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 4 * 10^4
 * 1 <= nums[i] <= 10^4
 * 通过次数27,548提交次数50,375
 */
public class _1262_GreatestSumDivisibleByThree {

    /**
     * 方法：动态规划
     *
     * 我们定义 f[i][j] 表示前 i 个数中选取若干个数，使得这若干个数的和 模 3 余 j 时 和的最大值。
     *
     * 初始时 f[0][0] = 0，其余（f[0][1...2]）为 −∞（f求最大值，初始化为最小值）。
     *
     * 对于 f[i][j]，我们可以考虑第 i 个数 x 的状态：
     *
     * 如果我们不选 x，那么 f[i][j] = f[i−1][j]；
     * 如果我们选 x，那么  f[i][j] = f[i−1][j − x%3] + x。 避免越界，f[i][j] = f[i−1][(j − x%3 + 3) % 3] + x
     *
     * 因此我们可以得到状态转移方程：
     *
     *      f[i][j] = max{ f[i−1][j], f[i][j] = f[i−1][(j − x%3 + 3) % 3] + x }
     *
     * 最终答案为 f[n][0]。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/greatest-sum-divisible-by-three/solution/python3javacgotypescript-yi-ti-yi-jie-do-95kq/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int maxSumDivThree(int[] nums) {
        int n = nums.length;
        // f[i][j] 表示前 i 个数中选取若干个数，使得这若干个数的和 模 3 余 j 时 和的最大值。
        int[][] dp = new int[n + 1][3];
        final int inf = 1 << 30;
        // 初始化 f[0][1...2]）为 −∞
        // dp[0][0] = 0;
        dp[0][1] = dp[0][2] = -inf;
        // 迭代递推，前 i 个元素
        for(int i = 1; i <= n; ++i) {
            // 当前位置（第i个位置）数字
            int x = nums[i - 1];
            // 枚举 模3 余 j 分别为 0、1、2
            for(int j = 0; j < 3; ++j) {
                // 选择 x、不选 x，二者取结果最大值
                dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][(j - x % 3 + 3) % 3] + x);
            }
        }
        // 返回 前 n 个数字中选部分数字 元素和 模3 为 0 时 和的最大值
        return dp[n][0];
    }

    /**
     * 空间优化
     *
     * 上述方法 时间复杂度 O(n)，空间复杂度  O(n)。其中 n 为数组  nums 的长度。
     *
     * 注意到 f[i][j] 的值只与 f[i−1][j] 和 f[i−1][(j − x%3 + 3) % 3] 有关，
     *
     * 因此我们可以使用滚动数组优化空间复杂度，省去i维度，使空间复杂度降低为 O(3) 即 O(1)。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/greatest-sum-divisible-by-three/solution/python3javacgotypescript-yi-ti-yi-jie-do-95kq/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int maxSumDivThree2(int[] nums) {
        final int inf = 1 << 30;
        // f[i][j] 表示前 i 个数中选取若干个数，使得这若干个数的和 模 3 余 j 时 和的最大值。
        // 省去 i 维度，初始化 f[i][0]，f[i][1]，f[i][2] --> f[0]、f[1]、f[2]
        int[] f = new int[]{0, -inf, -inf};
        // 迭代，前 i 个元素，第 i 个元素为 x
        for(int x : nums) {
            // 滚动数组，不能原地修改
            int[] g = f.clone();
            // 模 3 分别为 0、1、2 时的结果
            for(int j = 0; j < 3; ++j) {
                // 得到新一轮的结果
                g[j] = Math.max(f[j], f[(j - x % 3 + 3) % 3] + x);
            }
            // 替换，进行后续迭代
            f = g;
        }
        // 返回 f[n][0] --> f[0]
        return f[0];
    }
}
