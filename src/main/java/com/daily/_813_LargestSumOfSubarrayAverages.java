package com.daily;

/**
 * @author wangwei
 * @date 2022/11/28 12:30
 * @description: _813_LargestSumOfSubarrayAverages
 *
 * 813. 最大平均值和的分组
 * 给定数组 nums 和一个整数 k 。我们将给定的数组 nums 分成 最多 k 个相邻的非空子数组 。 分数 由每个子数组内的平均值的总和构成。
 *
 * 注意我们必须使用 nums 数组中的每一个数进行分组，并且分数不一定需要是整数。
 *
 * 返回我们所能得到的最大 分数 是多少。答案误差在 10-6 内被视为是正确的。
 *
 *
 *
 * 示例 1:
 *
 * 输入: nums = [9,1,2,3,9], k = 3
 * 输出: 20.00000
 * 解释:
 * nums 的最优分组是[9], [1, 2, 3], [9]. 得到的分数是 9 + (1 + 2 + 3) / 3 + 9 = 20.
 * 我们也可以把 nums 分成[9, 1], [2], [3, 9].
 * 这样的分组得到的分数为 5 + 2 + 6 = 13, 但不是最大值.
 * 示例 2:
 *
 * 输入: nums = [1,2,3,4,5,6,7], k = 4
 * 输出: 20.50000
 *
 *
 * 提示:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 104
 * 1 <= k <= nums.length
 * 通过次数13,138提交次数22,673
 */
public class _813_LargestSumOfSubarrayAverages {


    /**
     *
     * 动态规划
     *
     * 我们使用 dp[i][j] 表示 nums 前i个元素(即nums[0,i−1]) 被切分成 j 个子数组的最大平均值和，
     *
     * 最终结果为 dp[n][k]
     *
     * 显然 i ≥ j，计算分两种情况讨论：
     *
     *      当 j=1 时，dp[i][j] 是对应区间 [0,i−1] 的平均值；
     *
     *      当 j>1 时，可以枚举最后一个子数组的起点，从而将区间[0...i-1]分为分成 前x个元素（即[0,x−1]） 和 后i-x个元素（即[x,i−1]） 两个部分，
     *          那么 dp[i][j] 等于所有这些合法的切分方式的平均值和的最大值。
     *          对于每一种划分值，相当于 [0...i-1] 划分为 前x个元素（即[0,x−1]） 和 后i-x个元素（即[x,i−1]）
     *          其中前半部分可以划分为 j-1 份，后半部分为 1 个子数组，
     *          那么 后半部分的子数组平均值为 sum(nums[x...i-1]) = (preSum[i] - preSum[x]) / (i - x)
     *          而前半部分的结果等于 前x个元素划分成j-1个子数组所得到的最大平均值和，即 dp[x][j-1]
     *          因为子数组必须非空，所以若要划分为j份，那么至少要有j个元素，也就是 i >= j
     *              并且枚举最后一个子数组起点从而得到前后两部分，前半部分要能划分为j-1份，则 x >= j-1
     *                                                    后部分要非空，则 x < i
     *
     * 因此转移方程为：
     *
     * dp[i][j] =
     *      preSum[i] / i                                       j == 1, 1 <= i <= n
     *      max(dp[x][j-1] + (preSum[i]-preSum[x])/(i-x))       j > 1, j <= i <= n, j - 1 <= x < i
     *
     * 其中求解连续段之和可以用「前缀和」进行优化。
     * 同时，想要简化代码，还可以利用一个简单的数学结论：划分份数越多，平均值之和越大，因此想要取得最大值必然是恰好划分成 k 份。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/largest-sum-of-averages/solution/by-ac_oier-yfnt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/largest-sum-of-averages/solution/zui-da-ping-jun-zhi-he-de-fen-zu-by-leet-09xt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */
    public double largestSumOfAverages(int[] nums, int k) {
        int n = nums.length;
        double[] preSum = new double[n + 1];
        // preSum[i] 表示 nums 前i个元素(即nums[0,i−1]) 的元素和
        for (int i = 1; i <= n; ++i) {
            preSum[i] = preSum[i - 1] + nums[i - 1];
        }
        // dp[i][j] 表示 nums 前i个元素(即nums[0,i−1]) 被切分成 j 个子数组的最大平均值和，
        double[][] dp = new double[n + 1][k + 1];
        // base，j = 1，dp[i][j] 代表前i个元素分为一份的最大平均值，即 前缀和/数量
        for (int i = 1; i <= n; ++i) {
            dp[i][1] = preSum[i] / i;
        }
        // 迭代，划分j份，i必须 >= j
        for (int j = 2; j <= k; ++j) {
            for (int i = j; i <= n; ++i) {
                // 枚举分割点，分为 前x个元素(nums[0...x-1])和后i-x个元素的子数组
                // 在所有枚举结果中取最大值
                for (int l = j - 1; l < i; ++l) {
                    // 前半部分可以划分为 j-1份，因此 l 必须 >= j-1
                    // 后半部分是一个子数组，平均值为 (preSum[i]-preSum[x])/(i-x)
                    dp[i][j] = Math.max(dp[i][j], dp[l][j - 1] + (preSum[i] - preSum[l]) / (i - l));
                }
            }
        }
        // 返回前n个元素划分为j份子数组得到的最大平均值和
        return dp[n][k];
    }

    /**
     * 空间优化
     *
     * 由于 dp[i][j] 的计算只利用到 dp[?][j−1] 的数据，因此可以省去j维度，使用一维数组对 dp[i][j] 进行计算，
     *
     * 因为 dp[i] 依靠 dp[? < i] 来更新，因此 在省略掉j维度后， 要注意对 i 进行逆序遍历。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/largest-sum-of-averages/solution/zui-da-ping-jun-zhi-he-de-fen-zu-by-leet-09xt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */
    public double largestSumOfAverages2(int[] nums, int k) {
        int n = nums.length;
        double[] preSum = new double[n + 1];
        // preSum[i] 表示 nums 前i个元素(即nums[0,i−1]) 的元素和
        for (int i = 1; i <= n; ++i) {
            preSum[i] = preSum[i - 1] + nums[i - 1];
        }
        // dp[i][j] 表示 nums 前i个元素(即nums[0,i−1]) 被切分成 j 个子数组的最大平均值和，
        // 省略j维度
        double[] dp = new double[n + 1];
        // base，j = 1，dp[i][j] 代表前i个元素分为一份的最大平均值，即 前缀和/数量
        // 省略j维度
        for (int i = 1; i <= n; ++i) {
            dp[i] = preSum[i] / i;
        }
        // 迭代，划分j份，i必须 >= j
        for (int j = 2; j <= k; ++j) {
            // i 必须逆序遍历
            for (int i = n; i >= j; --i) {
                // 枚举分割点，分为 前x个元素(nums[0...x-1])和后i-x个元素的子数组
                // 在所有枚举结果中取最大值
                for (int l = j - 1; l < i; ++l) {
                    // 前半部分可以划分为 j-1份，因此 l 必须 >= j-1
                    // 后半部分是一个子数组，平均值为 (preSum[i]-preSum[x])/(i-x)
                    dp[i] = Math.max(dp[i], dp[l] + (preSum[i] - preSum[l]) / (i - l));
                }
            }
        }
        // 返回前n个元素划分为j份子数组得到的最大平均值和
        return dp[n];
    }
}
