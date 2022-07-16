package com.binarysearch;

import java.util.Arrays;

/**
 * @author wangwei
 * 2021/11/29 21:20
 *
 * 给定一个非负整数数组 nums 和一个整数m ，你需要将这个数组分成m个非空的连续子数组。
 *
 * 设计一个算法使得这m个子数组各自和的最大值最小。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [7,2,5,10,8], m = 2
 * 输出：18
 * 解释：
 * 一共有四种方法将 nums 分割为 2 个子数组。 其中最好的方式是将其分为 [7,2,5] 和 [10,8] 。
 * 因为此时这两个子数组各自的和的最大值为18，在所有情况中最小。
 * 示例 2：
 *
 * 输入：nums = [1,2,3,4,5], m = 2
 * 输出：9
 * 示例 3：
 *
 * 输入：nums = [1,4,4], m = 3
 * 输出：4
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/split-array-largest-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _410_SplitArrayLargestNumber {

    /**
     * 二分搜索
     * 找一个 子数组和的最大值 x （注意x的含义，本身就代表着某个最大值）
     *
     * 这个值越大，我们需要分割的次数就越少，这就得到单调关系了
     *
     * 自变量：题目要求的那个值
     * 因变量：在这个值的限制下，需要划分多少次数组，满足单调递减
     * 限制：f(x) 不能 超过 m
     *
     * 假如 x = sum(nums) 那只需要 1 次分割，
     * 这个值最小应该为 nums 中最大的那个值，不然就算每个子数组只包含一个元素，那它也不满足 这个值是所有子数组的和的最大值啊
     *
     * 时间复杂度 O(N log(sum(nums)))
     *
     * @param nums
     * @param m
     * @return
     */
    public int splitArray(int[] nums, int m) {
        if (nums == null || nums.length * m == 0) {
            return 0;
        }
        // x 代表 子数组和 的最大值
        // x 最小 应该为数组元素中的最大值，x 最大为 数组全部元素和
        int left = 0, right = 0;
        for (int num : nums) {
            left = Math.max(left, num);
            right += num;
        }
        // 左闭右开形式的 左边界二分搜索
        while (left < right) {
            int mid = left + (right - left) / 2;
            // 当 x = mid 时，需要划分成 cnt 个子数组才能保证每个子数组和 的最大值 < mid
            int cnt = split(nums, mid);
            // 如果这个cnt <= m，说明还可以尝试缩小 x
            if (cnt <= m) {
                right = mid;
            // 说明 x 太小了，需要划分的子数组太多了，应该增大 x
            } else {
                left = mid + 1;
            }
        }
        // 一定存在解，直接返回left
        return left;
    }

    /**
     * 加入每个子数组和的最大值为max，那么nums至少需要多少次分割才能满足这个条件
     *
     * 其实这里隐含了贪心思想，比如 [1,2,3,4,5] max = 6, 我们得到的结果是1，也就是尽可能少的划分，不然你完全可以划分出6个数组
     * 不过这个不影响结果，所以不用考虑那么多
     * @param nums
     * @param max
     * @return
     */
    private int split(int[] nums, int max) {
        // curSum 当前子数组的和
        // cnt 一共分割成几个子数组
        int curSum = 0, cnt = 0;
        for (int i = 0; i < nums.length; i++) {
            // 每个子数组和 不能超过 max
            if (curSum + nums[i] > max) {
                cnt++;
                // 这个元素划分到下个子数组
                curSum = nums[i];
            } else {
                curSum += nums[i];
            }
        }
        // for循环结束后，curSum 不为 0， 也就是少统计了最后一个子数组
        cnt++;
        return cnt;
    }

    /**
     * 动态规划
     *
     * 1. 明确dp定义
     * dp[i][k] 表示：将前缀区间 [0, i] 被分成 k 小段的 所有分法中 各小段和的最大值的最小值（题目要求的那个东西）
     * 即：第一维是第 k 个分割的最后一个元素的下标 i ，第二维是分割的总数 i。
     * 最后要返回的就应该是 dp[nums.length - 1][m]
     *
     * [0,i] 分成 k 段，拆分就是 [0, j] 分成 k - 1 段， 加上 [j+1, i] 这一部分。 其中 ( 0 <= j < i)，可以从(0, i)任意位置截断
     * 其中前缀区间 [0, j] （这里 j < i） 被分成 k - 1 段各自和的最大值的最小值为 dp[j][k - 1]。
     * [0, i] 分成 [0,j] + [j+1, i] 两部分，第二部分的和为 subsum(j+1,i)
     * 对于选择的每一个j， dp[i][k] = max(dp[j][k-1], subsum(j+1, i)) 应为需要每部分和的最大值
     * 而对于所有可能的j ，我们应该选择使得 dp[i][k] 最小的那个 j
     *
     * 所以：
     * 第 2 步：推导状态转移方程
     *
     * dp[i][k] = min(max(dp[j][k - 1], rangeSum(j + 1,i)))
     *
     * 3. 边界值
     * 因为需要求的是dp中的最小值(这么说不太准确，但是好理解，你看递推公式最外面那个min就理解了)，所以其他无意义的值都初始化为 MAX_VALUE
     * 然后 如果 m = 1，也就是 只分割成1个数组，那么 dp[i][1] 就应该= subsum(0,i) dp[i][1]就表示把nums[0,i]分成1段得到的所有数组中和的最小值
     * dp[i][1] 就只能得到一个数组 nums[0,i] 它的和就是 它本身的和，只有一个数组所以最大值还是前缀和，因为只有一种分法，所以取最小的分法结果还是它
     *
     *
     * 时间复杂度 O(N^2 * M)
     *
     * 作者：liweiwei1419
     * 链接：https://leetcode-cn.com/problems/split-array-largest-sum/solution/er-fen-cha-zhao-by-liweiwei1419-4/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param m
     * @return
     */
    public int splitArray2(int[] nums, int m) {
        if (nums == null || nums.length * m == 0) {
            return 0;
        }
        int len = nums.length;
        // 前缀和数组，presum[0] = 0, 所以 subsum(0,i) = presum[i+1]
        int[] preSum = new int[len + 1];
        for (int i = 1; i <= len; ++i) {
            preSum[i] = preSum[i - 1] + nums[i - 1];
        }
        // dp[i][k] 表示：将前缀区间 [0, i] 被分成 k 小段的 所有分法中 各小段和的最大值的最小值（题目要求的那个东西）
        // 最后返回 dp[len-1][m] ，所以这里 申请空间是 m + 1
        int[][] dp = new int[len][m + 1];
        // 初始化为不影响结果(求最小值)的值(最大整数)
        for (int i = 0; i < len; ++i) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        // 边界条件
        // 当分割数为1段时，即不分割的情况，不管i是几，分割一段就是 subsum(0,i) = presum[i+1]
        for (int i = 0; i < len; i++) {
            dp[i][1] = preSum[i + 1];
        }
        // 当i=0时，如果m=0，那么dp[0][0]=0, 如果m>=1。因为只有nums[0]一个元素，所以只有1种分法
        for (int k = 1; k <= m; ++k) {
            dp[0][k] = nums[0];
        }
        // 状态转移 i [1, len - 1] , j [0, i], k [2, m]
        // 为什么k最小值是2呢，因为我们要把 [0,i] 划分成 [0, j] + [j+1, i] 所以对于dp[i][k]来说，k至少为2，才有划分空间
        for (int i = 1; i < len; ++i) {
            // 这里注意 k 和 j 的遍历顺序，
            // 以为 对于 每一个 k， dp[i][k] ~ dp[j][k-1]， 会有多个可以选择的j，所以应该在固定k的前提下去遍历j
            for (int k = 2; k <= m; ++k) {
                for (int j = 0; j < i; ++j) {
                    // 将[0,i] 划分成 [0, j] + [j+1, i]两段，取 两段和 中较大的那个值
                    // 对于所有的 j，对应多种分法，我们应该选择使得 dp[i][k] 最小的那种分法
                    // [j+1, i] 的和利用 前缀和数组计算
                    dp[i][k] = Math.min(dp[i][k], Math.max(dp[j][k - 1], preSum[i + 1] - preSum[j + 1]));
                }
            }
        }
        // 返回结果，将 nums[0, len-1] 划分成 m 段
        return dp[len - 1][m];
    }
}
