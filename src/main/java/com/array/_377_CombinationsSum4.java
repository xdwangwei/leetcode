package com.array;

/**
 * @author wangwei
 * 2020/4/6 11:45
 * <p>
 * 给定一个由正整数组成且不存在重复数字的数组，找出和为给定目标正整数的组合的个数。
 * <p>
 * 示例:
 * <p>
 * nums = [1, 2, 3]
 * target = 4
 * <p>
 * 所有可能的组合为：
 * (1, 1, 1, 1)
 * (1, 1, 2)
 * (1, 2, 1)
 * (1, 3)
 * (2, 1, 1)
 * (2, 2)
 * (3, 1)
 * <p>
 * 请注意，【顺序不同的序列被视作不同的组合。】
 * <p>
 * 因此输出为 7。
 * 进阶：
 * 如果给定的数组中含有负数会怎么样？
 * 问题会产生什么变化？
 * 我们需要在题目中添加什么限制来允许负数的出现？
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/combination-sum-iv
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _377_CombinationsSum4 {

    private int res = 0;

    public int solution1(int[] nums, int target) {
        if (nums == null || nums.length == 0) return 0;
        backTrack(nums, target);
        return res;
    }

    /**
     * 回溯/递归，回溯一般涉及记录路径，因此存在做选择撤销选择，否则就是单纯的递归
     * 每次可选数组中任意一个元素作为当前的选择
     * <p>
     * 因为没有剪枝，当数组元素比较小，并且数量比较少而target比较大时，
     * 树枝太复杂，深度太大，时间复杂度太高，会超时
     */
    private void backTrack(int[] nums, int target) {
        // 满足结束条件
        if (target == 0) {
            res++;
            return;
        }
        // 遍历选择列表，做选择
        for (int i = 0; i < nums.length; i++) {
            // 排除不合法选项
            if (target - nums[i] < 0) continue;
            // 做选择，不涉及记录路径
            // 进入下一层试探
            backTrack(nums, target - nums[i]);
            // 撤销当前选择，不涉及记录路径
        }
    }

    /**
     * 不涉及求解路径，只想知道解的个数，采用动态规划
     * dp[i] ：对于给定的由正整数组成且不存在重复数字的数组，和为 i 的组合的个数
     * <p>
     * dp[i] = sum{dp[i - num] for num in nums and if i >= num}
     * <p>
     * 注意：在 0 这一点，我们定义 dp[0] = 1 的，它表示如果 nums 里有一个数恰好等于 target，它单独成为 1种可能。
     * 此时 dp[i-num] = dp[0]，因为这也是一种可能，所以规定dp[0] = 1
     *
     * @param nums
     * @param target
     * @return
     */
    public int solution2(int[] nums, int target) {
        if (nums == null || nums.length == 0) return 0;
        int[] dp = new int[target + 1];
        dp[0] = 1; // 比如nums=[2]，target=2
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                // 比如求dp[35],nums=[2,3,5]
                //      35 = 33 + 2(nums[1]), 35 = 32 + 3(nums[2]), 35 = 30 + 5(nums[3])
                // dp[35] = dp[33]  +   dp[32]   +   dp[30]
                if (num <= i)
                    dp[i] += dp[i - num];
            }
        }

        return dp[target];
    }

    public static void main(String[] args) {
        // System.out.println(new _377_CombinationsSum4().solution1(new int[]{1, 2, 3}, 35));
        System.out.println(new _377_CombinationsSum4().solution2(new int[]{1, 2, 3}, 35));
    }
}
