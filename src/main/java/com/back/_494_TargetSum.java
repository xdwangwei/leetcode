package com.back;

import java.util.HashMap;

/**
 * @author wangwei
 * 2020/7/20 11:27
 * <p>
 * 给定一个非负整数数组，a1, a2, ..., an, 和一个目标数，S。现在你有两个符号 + 和 -。对于数组中的任意一个整数，你都可以从 + 或 -中选择一个符号添加在前面。
 * <p>
 * 返回可以使最终数组和为目标数 S 的所有添加符号的方法数。
 * <p>
 *  
 * <p>
 * 示例：
 * <p>
 * 输入：nums: [1, 1, 1, 1, 1], S: 3
 * 输出：5
 * 解释：
 * <p>
 * -1+1+1+1+1 = 3
 * +1-1+1+1+1 = 3
 * +1+1-1+1+1 = 3
 * +1+1+1-1+1 = 3
 * +1+1+1+1-1 = 3
 * <p>
 * 一共有5种方法让最终目标和为3。
 *  
 * 提示：
 * <p>
 * 数组非空，且长度不会超过 20 。
 * 初始的数组的和不会超过 1000 。
 * 保证返回的最终结果能被 32 位整数存下。
 *
 * 提示：
 *
 * 1 <= nums.length <= 20
 * 0 <= nums[i] <= 1000
 * 0 <= sum(nums[i]) <= 1000
 * -1000 <= target <= 1000
 *
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/target-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _494_TargetSum {

    private int res = 0;

    public int findTargetSumWays(int[] nums, int S) {
        if (nums == null || nums.length == 0) return 0;
        backTrack(nums, 0, S);
        return res;
    }

    /**
     * 回溯 O(2^N)
     *
     * 对于每一个数字来说，要么选择＋，要么选择负号
     * 相当于一颗完全二叉树
     * @param nums
     * @param i
     * @param rest
     */
    private void backTrack(int[] nums, int i, int rest) {
        // 出口，数组所有元素都已分配正负号
        if (i == nums.length) {
            // 此次分配是否合适
            if (rest == 0) res++;
            return;
        }
        // 对数组当前位置元素选择符号

        // 选择负号
        rest += nums[i];
        // 分配下一位置元素符号
        backTrack(nums, i + 1, rest);
        // 撤销选择
        rest -= nums[i];

        // 选择正号
        rest -= nums[i];
        // 分配下一位置元素符号
        backTrack(nums, i + 1, rest);
        // 撤销选择
        rest += nums[i];
    }

    /**
     * 动态规划之所以比暴力算法快，是因为动态规划技巧消除了重叠子问题。
     * 如何发现重叠子问题？看是否可能出现重复的「状态」。
     * 对于递归函数来说，函数参数中会变的参数就是「状态」，
     * <p>
     * 对于 backtrack 函数来说，会变的参数为 i 和 rest。
     * 因此，状态 (i, rest) 是可以用备忘录技巧进行优化的：
     * <p>
     * 以前我们都是用 Python 的元组配合哈希表 dict 来做备忘录的，
     * 其他语言没有元组，可以用把「状态」转化为字符串作为哈希表的键，这是一个常用的小技巧。
     *
     * 备忘录保存当前状态（当前子树）
     * 最后需要返回根的值 return dp(nums, 0, target);
     * 相当于先得到了叶子，然后向上得到非叶子节点并保存进备忘录
     * 也就是当从其他路线再次走到这个节点时，就不用再往下走了，从这个节点一直到叶子节点满足目标的数目已经保存了
     *
     * 最坏情况下依然是 O(2^N)。
     * 为什么呢？因为我们只不过恰好发现了重叠子问题，顺手用备忘录技巧给优化了，
     * 但是底层思路没有变，依然是暴力穷举的回溯算法，依然在遍历一棵二叉树。
     * 这只能叫对回溯算法进行了「剪枝」，提升了算法在某些情况下的效率，但算不上质的飞跃。
     *
     * 【注意】
     * 这里的备忘录为什么用 map，不能用二维数组吗？int[idx][rest]
     *
     * 不可以：：因为回溯是可以路径回退的，也就是我可以先让和超出目标，然后后面数字取负号，再让它减小到目标
     * 那么这样的话，这个memo[][]初始化时申请多大空间？idx最多是数组length，但是rest，对于回溯来说，它可能最大时全部元素和，最小是最大值的相反数，而且负数是没有索引的
     * 因此，回溯法如果要使用备忘录，不能用二维数组！！！
     * @param args
     */
    // 看不懂就算了
    HashMap<String, Integer> memo = new HashMap<>();
    private int dp(int[] nums, int i, int rest) {
        // 出口，数组所有元素都已分配正负号
        if (i == nums.length) {
            // 此次分配是否合适
            if (rest == 0) return 1;
            return 0;
        }
        // 把「状态」转化为字符串作为哈希表的键
        String key = i + "," + rest;
        // 避免重复计算
        if (memo.containsKey(key)) return memo.get(key);
        // 得到当前状态的值，还是穷举，左子树 加 右子树
        int result = dp(nums, i + 1, rest + nums[i]) + dp(nums, i + 1, rest - nums[i]);
        // 保存进备忘录
        memo.put(key, result);
        return result;
    }

    /**
     * 动态规划总是这么玄学，让人摸不着头脑……
     * 这个问题可以转化为一个子集划分问题，而子集划分问题又是一个典型的背包问题。
     * 首先，如果我们把 nums 划分成两个子集 A 和 B
     * A中的数全部分配加号+, B中的数全部分配减号-，全部分配完符号后他们的和为target
     *
     * 那么对于未分配符号的AB，他们和 target 存在如下关系：
     * sum(A) - sum(B) = target
     * sum(A) = target + sum(B)
     * sum(A) + sum(A) = target + sum(B) + sum(A)
     * 2 * sum(A) = target + sum(nums)
     * 综上，可以推出 sum(A) = (target + sum(nums)) / 2，
     * 也就是把原问题转化成：
     * nums 中存在几个子集 A，使得 A 中元素的和为 (target + sum(nums)) / 2？、
     *
     * 每个元素只可以用1次，所以这是一个0-1背包问题
     * 背包问题可先看 _416_
     *
     * 题目说了 nums 元素 都是 非负整数，target 可能为 负，所以 如果 sum(nums) < abs(target), 那么不可能
     * @param
     */
    public int findTargetSumWays2(int[] nums, int target) {
        if (nums == null || nums.length == 0) return 0;
        int sum = 0;
        // 所有数之和
        for (int num : nums) {
            sum += num;
        }
        // sum < target 不可能，要划分和为 (target + sum(nums)) / 2 的子集，这个数不能为奇数
        if (sum < Math.abs(target) || (sum + target & 1) == 1) return 0;
        return findSubSets(nums, (sum + target) / 2);
    }
    /**
     * 背包问题
     * 有一个背包，容量为 sum，
     * 现在给你 N 个物品，第 i 个物品的重量为 nums[i - 1]（注意 1 <= i <= N），
     * 每个物品只有一个，请问你有几种不同的方法能够恰好装满这个背包？
     *
     * dp[i][j] = x 表示，若只在前 i 个物品中选择，若当前背包的容量为 j，则最多有 x 种方法可以恰好装满背包。
     * 我们所求的答案就是 dp[N][sum]，即使用所有 N 个物品，有几种方法可以装满容量为 sum 的背包。
     */
    private int findSubSets(int[] nums, int sum) {
        // dp[i][j]表示前i个物品有几种装法装满容量为sum的背包
        int[][] dp  = new int[nums.length + 1][sum + 1];
        // 容量为0，只能啥都不装，一种装法
        for (int i = 0; i <= nums.length; i++) {
            dp[i][0] = 1;
        }
        // 前i个物品
        for (int i = 1; i <= nums.length; i++) {
            // 容量为j
            for (int j = 0; j <= sum; ++j) {
                // 容量不够，当前物品放不进去
                if (nums[i - 1] > j)
                    dp[i][j] = dp[i - 1][j];
                // 容量足够，当前物品可选择放入或不放入
                else
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i - 1]];
            }
        }
        return dp[nums.length][sum];
    }

    /**
     * 降维处理 内循环需要倒序 原因请查看 _416_
     *
     * 因为二维压缩到一维的根本原理是，dp[j] 和 dp[j-nums[i-1]] 还没被新结果覆盖的时候，相当于二维 dp 中的 dp[i-1][j] 和 dp[i-1][j-nums[i-1]]。
     * 那么，我们就要做到：在计算新的 dp[j] 的时候，dp[j] 和 dp[j-nums[i-1]] 还是上一轮外层 for 循环的结果。
     * 如果你从前往后遍历一维 dp 数组，dp[j] 显然是没问题的，但是 dp[j-nums[i-1]] 已经不是上一轮外层 for 循环的结果了，这里就会使用错误的状态。
     * @param nums
     * @param sum
     * @return
     */
    private int findSubSets2(int[] nums, int sum) {
        // dp[i][j]表示前i个物品有几种装法装满容量为sum的背包
        // 此时的dp[j]相当于上面的dp[i-1][j] 能否组成合为i的背包
        int[] dp  = new int[sum + 1];
        // 容量为0，只能啥都不装，一种装法
        dp[0] = 1;
        // 前i个物品
        for (int i = 1; i <= nums.length; i++) {
            // 容量为j
            for (int j = sum; j >= 0; --j) {
                // 容量足够，当前物品可选择放入或不放入
                if (nums[i - 1] <= j)
                    dp[j] = dp[j] + dp[j - nums[i - 1]];
                // 当前这个物品不放进背包
                // 我们发现降维之后，这个else已经没用了，降维后的dp[j]相当于降维前的dp[i-1][j]
                // 当前物品不放入背包，那么不用更新
                // else
                //     dp[j] = dp[j];
            }
        }
        return dp[sum];
    }



    public static void main(String[] args) {
        int[] nums = {1, 1, 1, 1, 1};
        new _494_TargetSum().findTargetSumWays(nums, 3);
    }
}
