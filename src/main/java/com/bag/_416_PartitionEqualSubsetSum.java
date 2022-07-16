package com.bag;

import java.util.Arrays;

/**
 * @author wangwei
 * 2020/4/26 0:08
 * <p>
 * 给定一个只包含正整数的非空数组。是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
 * <p>
 * 注意:
 * <p>
 * 每个数组中的元素不会超过 100
 * 数组的大小不会超过 200
 * 示例 1:
 * <p>
 * 输入: [1, 5, 11, 5]
 * <p>
 * 输出: true
 * <p>
 * 解释: 数组可以分割成 [1, 5, 5] 和 [11].
 *  
 * <p>
 * 示例 2:
 * <p>
 * 输入: [1, 2, 3, 5]
 * <p>
 * 输出: false
 * <p>
 * 解释: 数组不能分割成两个元素和相等的子集.
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/partition-equal-subset-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _416_PartitionEqualSubsetSum {

    public boolean canPartition8(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        int sum = Arrays.stream(nums).sum();
        if (sum % 2 == 1) {
            return false;
        }
        int len = nums.length, target = sum / 2;
        boolean[][] dp = new boolean[len + 1][target + 1];
        for (int i = 0; i <= len; ++i) {
            dp[i][0] = true;
        }
        for (int i = 1; i <= len; ++i) {
            for (int j = 1; j <= target; ++j) {
                if (j >= nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j - nums[i - 1]] || dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[len][target];
    }

    /**
     * 0-1 背包
     * <p>
     * 首先回忆一下背包问题大致的描述是什么：
     * <p>
     * 给你一个可装载重量为 W 的背包和 N 个物品，每个物品有重量和价值两个属性。其中第 i 个物品的重量为 wt[i]，价值为 val[i]，现在让你用这个背包装物品，最多能装的价值是多少？
     * <p>
     * 那么对于这个问题，我们可以先对集合求和，得出 sum，把问题转化为背包问题：
     * <p>
     * 给一个可装载重量为 sum / 2 的背包和 N 个物品，每个物品的重量为 nums[i]。现在让你装物品，是否存在一种装法，能够恰好将背包装满？
     * <p>
     * <p>
     * dp[i][j] = x 表示，对于前 i 个物品，当前背包的容量为 j 时，若 x 为 true，则说明可以恰好将背包装满，若 x 为 false，则说明不能恰好将背包装满。
     * <p>
     * 如果不把 nums[i] 算入子集，或者说你不把这第 i 个物品装入背包，那么是否能够恰好装满背包，取决于上一个状态 dp[i-1][j]，继承之前的结果。
     * <p>
     * 如果把 nums[i] 算入子集，或者说你把这第 i 个物品装入了背包，那么是否能够恰好装满背包，取决于状态 dp[i - 1][j-nums[i-1]]。
     * <p>
     * 首先，由于 i 是从 1 开始的，而数组索引是从 0 开始的，所以第 i 个物品的重量应该是 nums[i-1]，这一点不要搞混。
     * <p>
     * dp[i - 1][j-nums[i-1]] 也很好理解：你如果装了第 i 个物品，就要看背包的剩余重量 j - nums[i-1] 限制下是否能够被恰好装满。
     * <p>
     * 换句话说，如果 j - nums[i-1] 的重量可以被恰好装满，那么只要把第 i 个物品装进去，也可恰好装满 j 的重量；否则的话，重量 j 肯定是装不满的。
     * <p>
     * 作者：labuladong
     * 链接：https://leetcode-cn.com/problems/partition-equal-subset-sum/solution/0-1-bei-bao-wen-ti-bian-ti-zhi-zi-ji-fen-ge-by-lab/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param nums
     * @return
     */

    public boolean canPartition(int[] nums) {
        if (nums == null || nums.length < 2) return false;
        int sum = 0;
        // 求总和
        for (int num : nums)
            sum += num;

        // 如果总和是奇数，肯定不可能对半
        if ((sum & 1) == 1) return false;
        // 我们的目标是找到是否能凑成背包重量为sum/2
        sum /= 2;

        // dp[i][j] 前i个数组元素能否组成合为j
        boolean[][] dp = new boolean[nums.length + 1][sum + 1];
        // 前任意个元素，都可以组成和为0
        for (int i = 0; i <= nums.length; ++i)
            dp[i][0] = true;

        for (int i = 1; i <= nums.length; ++i)
            for (int j = 0; j <= sum; j++) {
                // 当前这个物品能放进背包
                if (j >= nums[i - 1])
                    // 选择放入背包或不放入背包
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]];
                // 背包容量不足，当前这个物品不能放进背包
                else
                    dp[i][j] = dp[i - 1][j];
            }
        // 返回的是，对于全部的数组元素，能否组成重量为总和一半的背包
        return dp[nums.length][sum];
    }

    /**
     * 同样的，dp[i]只和dp[i-1]有关，我们可以降维处理，
     * 但是降维存在一个问题，物品不能重复，所以内循环需要倒序，
     *
     * 压缩到一维时，要采用逆序。
     * dp[j] = dp[j] || dp[j - nums[i]] 可以理解为 dp[j] （新）= dp[j] （旧） || dp[j - nums[i]] （旧），
     * 在外循环不变的情况下，全部dp[j]的更新是一个阶段，
     * 也就是说，用到了nums[i]时，和不用nums[i]时，才是 新 与 旧 的划分点
     * 如果采用正序的话 dp[j - nums[i]]会被之前的操作更新为新值
     *
     * 也就是说，我如果倒序，求dp[6] = dp[2] 或 dp[4]，此时dp[2]和dp[4]还未被更新，都是旧值，是合理的
     * 但我如果正序，会先求出dp[4]，然后再求dp[6]的时候，用到的这个dp[4]是刚更新了的，不合理
     *
     * 可能你要说，这个dp[4]不是是在dp[6]之前更新的？它相对于dp[6]就是旧值啊
     *
     * 注意我们这里的旧值不是谁先谁后更新的问题，dp[i--num]全部更新才是一个新的阶段，我们这里区分的阶段的新旧
     *
     * 也就是说，再 外循环 i 不变的情况下，dp[6]用到的dp[4]应该是在外循环 i-1的时候的dp[4]
     *
     * 如果正序，那么这个dp[4]会被改变，所以不是dp[6]需要的那个
     *
     * 比如 在二维情况下，对于
     *      i = 1: a1  b1  c1  d1  e1  f1  g1  h1  j1  k1
     *      i = 2: a2  b2  c2  d2  e2  f2  g2  h2  j2  k2
     *      // dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]];
     *      // 由于 i 维度的存在，更新 a2 ... 时，用到的都是 a1 b1 c1 等，
     *      但是当i维度去掉后，正序遍历就无法保证这个机制，
     *      比如 应该是 c e 得到 g， 然后 再 a b 得到 c，这样就能保证这一排值在同样的上一次状态下进行了迭代更新
     *      如果是正序，那么 a b 得到 c， c e 得到 g，对于此时的g来说，这个 c 不是上一次的状态。
     * 所以要倒序
     */

    public boolean canPartition2(int[] nums) {
        if (nums == null || nums.length < 2) return false;
        int sum = 0;
        // 求总和
        for (int num : nums)
            sum += num;

        // 如果总和是奇数，肯定不可能对半
        if ((sum & 1) == 1) return false;
        // 我们的目标是找到是否能凑成背包重量为sum/2
        sum /= 2;

        // 此时的dp[j]相当于上面的dp[i-1][j] 能否组成合为i的背包
        boolean[] dp = new boolean[sum + 1];
        // 前任意个元素，都可以组成和为0
        dp[0] = true;

        for (int i = 1; i <= nums.length; ++i)
            // 为什么要倒序，这个倒序有点绕，看上面注释，实在不明白就记下压缩到一维时需要倒序
            // 本质原因就是二维时本身就是两个维度的划分，一维时dp[j]其实已经蕴含了第一个维度，它代表的就是dp[i-1][j]
            for (int j = sum; j >= 0; --j) {
                // 当前这个物品放进背包
                if (j >= nums[i - 1])
                    // i是当前这个物品，i是它在数组中的下标
                    dp[j] = dp[j] || dp[j - nums[i - 1]];
                // 当前这个物品不放进背包
                // 我们发现降维之后，这个else已经没用了，降维后的dp[j]相当于降维前的dp[i-1][j]
                // 当前物品不放入背包，那么不用更新
                // else
                //     dp[j] = dp[j];
            }
        // 返回的是，对于全部的数组元素，能否组成重量为总和一半的背包
        return dp[sum];
    }
}
