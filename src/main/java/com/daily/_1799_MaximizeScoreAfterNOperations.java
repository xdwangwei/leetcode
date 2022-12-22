package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/12/22 18:57
 * @description: _1799_MaximizeScoreAfterNOperations
 *
 * 1799. N 次操作后的最大分数和
 * 给你 nums ，它是一个大小为 2 * n 的正整数数组。你必须对这个数组执行 n 次操作。
 *
 * 在第 i 次操作时（操作编号从 1 开始），你需要：
 *
 * 选择两个元素 x 和 y 。
 * 获得分数 i * gcd(x, y) 。
 * 将 x 和 y 从 nums 中删除。
 * 请你返回 n 次操作后你能获得的分数和最大为多少。
 *
 * 函数 gcd(x, y) 是 x 和 y 的最大公约数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,2]
 * 输出：1
 * 解释：最优操作是：
 * (1 * gcd(1, 2)) = 1
 * 示例 2：
 *
 * 输入：nums = [3,4,6,8]
 * 输出：11
 * 解释：最优操作是：
 * (1 * gcd(3, 6)) + (2 * gcd(4, 8)) = 3 + 8 = 11
 * 示例 3：
 *
 * 输入：nums = [1,2,3,4,5,6]
 * 输出：14
 * 解释：最优操作是：
 * (1 * gcd(1, 5)) + (2 * gcd(2, 4)) + (3 * gcd(3, 6)) = 1 + 4 + 9 = 14
 *
 *
 * 提示：
 *
 * 1 <= n <= 7
 * nums.length == 2 * n
 * 1 <= nums[i] <= 106
 * 通过次数8,226提交次数13,300
 */
public class _1799_MaximizeScoreAfterNOperations {

    /**
     * 方法一：暴力搜索：没有优化，超时了，但思想没有问题
     * 每次从还未被选择的元素中选出两个，计算当前得分，更新累计得分，
     * 对于这两个元素的选择，需要枚举所有可能的选择结果，选出最终得分最大者
     *
     * 若使用int或bool数组来记录每个元素的选择状态，因为每次要选择不同的两个元素，枚举所有情况，所以可能每次需要回溯标记数组
     * 而注意到数组元素个数不超过7，因此我们采用二进制表示法：
     * 使用一个int变量的二进制每一位来代表nums中对应位置的元素是否被选择过。
     * 即 若 ((state >> i)&1) == 1，表示 nums[i] 已被选择
     *
     * 这样，当选择i、j位置后，int nstate = state | (1 << i) | (1 << j); 传递这个nstate到下一次递归，就可避免回溯选择
     *
     * 假设数组元素个数为n，当所有元素都被选择时，state = (1<<n)-1
     *
     * 由于每次都要考虑所选择两个元素的所有选择情况，所以state会以不同的过程从0变化到终态，我们记录过程中获得的分数情况
     * 每一次state达到终态时，用此时的得分情况去更新ans，保留较大值
     *
     * 计算分数需要知道当前是第几次选择，用state中1的个数/2即可，由于要求从1开始，所以 第几步 = Integer.bitCount(state)/2 + 1
     * @param nums
     * @return
     */

    // 保存最终结果
    private int ans;
    // 保存数组元素个数
    private int n;

    public int maxScore(int[] nums) {
        n = nums.length;
        // 暴力搜索，初始状态，没有元素被选择，state为0，累计分数为0
        backTrack(0, nums, 0);
        return ans;
    }

    /**
     * 暴力枚举所有可能的选择情况
     * @param state 当前nums元素的选择状态
     * @param nums
     * @param score 当前获得的分数
     */
    private void backTrack(int state, int[] nums, int score) {
        // 如果达到终态
        if (state == (1 << n) - 1) {
            // 就用当前分数去更新ans，保留较大值
            ans = Math.max(ans, score);
            return;
        }
        // 当前是第几步
        int step = Integer.bitCount(state) / 2 + 1;
        // 枚举当前，所有可能的 两个元素的选择情况
        for (int i = 0; i < n; ++i) {
            // 先选第一个元素，跳过已选择位置
            if (((state >> i) & 1) == 1) {
                continue;
            }
            // 再选择第二个元素
            for (int j = i + 1; j < n; ++j) {
                // 跳过已选择位置
                if (((state >> j) & 1) == 1) {
                    continue;
                }
                // 选择这两个元素，得到一个新的state
                int nstate = state | (1 << i) | (1 << j);
                // 选择这两个元素能获得的分数
                int curScore = step * gcd(nums[i], nums[j]);
                // 进行后序过程
                backTrack(nstate, nums, score + curScore);
            }
        }
    }


    /**
     * 方法二：记忆化搜索
     * 由于方法一没有优化，因此无法通过所有测试用例，
     * 从两方面进行优化：
     * 1、由于枚举所有选择情况，因此存在先选1、2再选3、4和先选2、4再选1、3这种重复情况，
     *      因此，对于不同时候的dfs，可能state是一样的，存在重复
     *      因此，用map<int,int>记录 某个state时，所能够取得的最大分数
     * 2、在上面的重复情况中，1、2和3、4的最大公因数计算过程也会重复进行，
     *      因此，用int[][] 提前计算并记录 nums[i] 和 nums[j] 的 最大公因数
     */
    // memo[state]:val，表示nums选中情况为state时，后序过程所能获取的最大分数
    private Map<Integer, Integer> memo;
    // 记录nums任意两个元素的最大公约数
    private int[][] gcdTmp;

    public int maxScore2(int[] nums) {
        n = nums.length;
        // 初始化
        memo = new HashMap<>();
        gcdTmp = new int[n][n];
        // 提前计算元素间的最大公约数
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                gcdTmp[i][j] = gcd(nums[i], nums[j]);
            }
        }
        // 返回state为0（没有元素被选）时，后序过程能获得的最大分数
        return backTrack2(0, nums);
    }

    /**
     * 记忆化搜索：nums元素选择状态为state时，后序过程能得到的最大分数
     * @param state
     * @param nums
     * @return
     */
    private int backTrack2(int state, int[] nums) {
        // 如果已经有备份，直接返回
        if (memo.containsKey(state)) {
            return memo.get(state);
        }
        // 如果已经全被选了，无法获得分数，就返回0
        if (state == (1 << n) - 1) {
            return 0;
        }
        // 当前是第几步
        int step = Integer.bitCount(state) / 2 + 1;
        // 计算当前state下能获得的最大分数
        // 先选择两个元素出来，后续过程交给递归
        // 在所有可行情况中取最大值
        int res = 0;
        // 先选第一个元素
        for (int i = 0; i < n; ++i) {
            // 跳过已选择元素
            if (((state >> i) & 1) == 1) {
                continue;
            }
            // 再选第二个元素
            for (int j = i + 1; j < n; ++j) {
                if (((state >> j) & 1) == 1) {
                    continue;
                }
                // 选择i、j位置元素后得到的新state
                int nstate = state | (1 << i) | (1 << j);
                // 选择这两个元素所获得的分数
                int curScore = step * gcdTmp[i][j];
                // 当前state下后序过程能获得的最大分数 = max (选择这两个元素得到的分数+选完这两个元素后剩下元素能得到的最大分数)
                res = Math.max(res, curScore + backTrack2(nstate, nums));
            }
        }
        // 记录当前state的答案
        memo.put(state, res);
        // 返回当前state的答案
        return res;
    }

    /**
     * 方法三：记忆化搜索改动态规划
     * 可以看到，记忆化搜索的参数中，只有state是变化的，因此动态规划只需要考虑state这一个维度的变化
     *
     * dp[s]表示nums中元素选择状态为s的情况下，后序过程所能获得的最大分数， 其中 0 <= s <= [(1<<n)-1]
     *
     * 最后，返回 dp[0]
     * base：当s==(1<<n)-1时，没元素可选，dp[s] = 0
     * 迭代：
     *    dp[s] = 枚举选两个元素， max (选择这两个元素得到的分数+选完这两个元素后剩下元素能得到的最大分数)
     *
     * 小细节:
     *     因为我们最后要返回dp[0]，所以倒序迭代
     *     由于1次选两个元素，因此对于dp[s], 当s中1的个数为奇数个时，不同考虑
     *
     * @param nums
     * @return
     */
    public int maxScore3(int[] nums) {
        int n = nums.length;
        // 初始化
        int[][] gcdTmp = new int[n][n];
        // 提前计算元素间的最大公约数
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                gcdTmp[i][j] = gcd(nums[i], nums[j]);
            }
        }
        // 最终状态，即 state 的最大值
        int up = (1 << n) - 1;
        // dp
        int[] dp = new int[up + 1];
        // 倒序迭代
        // base: dp[up] = 0
        for(int s = up - 1; s >= 0; --s) {
            // s 中 1 的个数 必须为 偶数个
            int count =  Integer.bitCount(s);
            if ((count & 1) == 1) {
                continue;
            }
            // 下面基本和之前一致
            // 当前是第几步
            int step = count / 2 + 1;
            // 先选第一个元素
            for (int i = 0; i < n; ++i) {
                // 跳过已选择元素
                if (((s >> i) & 1) == 1) {
                    continue;
                }
                // 再选第二个元素
                for (int j = i + 1; j < n; ++j) {
                    if (((s >> j) & 1) == 1) {
                        continue;
                    }
                    // 选择i、j位置元素后得到的新state
                    int nstate = s | (1 << i) | (1 << j);
                    // 选择这两个元素所获得的分数
                    int curScore = step * gcdTmp[i][j];
                    // 当前state下后序过程能获得的最大分数 = max (选择这两个元素得到的分数+选完这两个元素后剩下元素能得到的最大分数)
                    dp[s] = Math.max(dp[s], curScore + dp[nstate]);
                }
            }
        }
        // 返回state为0（没有元素被选）时，后序过程能获得的最大分数，即 dp[0]
        return dp[0];
    }

    /**
     * 最大公约数
     * @param a
     * @param b
     * @return
     */
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static void main(String[] args) {
        _1799_MaximizeScoreAfterNOperations obj = new _1799_MaximizeScoreAfterNOperations();
        // obj.maxScore3(new int[]{1, 2, 3, 4, 5, 6});
        obj.maxScore3(new int[]{171651,546244,880754,412358});
    }
}
