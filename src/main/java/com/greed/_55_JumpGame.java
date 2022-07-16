package com.greed;

/**
 * @author wangwei
 * 2020/4/20 18:00
 * <p>
 * 给定一个非负整数数组，你最初位于数组的第一个位置。
 * <p>
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * <p>
 * 判断你是否能够到达最后一个位置。
 * <p>
 * 示例1:
 * <p>
 * 输入: [2,3,1,1,4]
 * 输出: true
 * 解释: 我们可以先跳 1 步，从位置 0 到达 位置 1, 然后再从位置 1 跳 3 步到达最后一个位置。
 * 示例2:
 * <p>
 * 输入: [3,2,1,0,4]
 * 输出: false
 * 解释: 无论怎样，你总会到达索引为 3 的位置。但该位置的最大跳跃长度是 0 ， 所以你永远不可能到达最后一个位置。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/jump-game
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _55_JumpGame {

    /**
     * 贪心思想
     *
     * 题目中的跳跃规则，最多能跳多远？如果能够越过最后一格，返回 true，否则返回 false。
     * 从前往后跳，判断每个位置能够跳到的最远位置，如果超过了数组最后一个位置，返回true
     *
     * 对于 2 0 2 3 会返回true，因为 2 的确可以到达3，但实际上 0 是不能到达 2 的
     * 也就是说没有排除掉这种情况，一旦 dis < i 了，说明之前的位置都到不了当前位置，不用继续了
     * @param nums
     */
    public boolean canJump(int[] nums) {
        // i 当前位置，dis 最远能到达的位置
        int maxDis = 0;
        // 不需要考虑最后一个位置(如果能直接到最后一个位置，还考虑啥，它只能从前面跳过来)
        for (int i = 0; i < nums.length - 1; i++) {
            // 从当前位置最多跳到哪里
            maxDis = Math.max(maxDis, i + nums[i]);
            // 之前可能跳到0位置了，无法继续往后了
            // 因为是先更新当前位置能跳到最远位置的，所以如果更新完之后还不能超过当前位置，那肯定没戏
            if (maxDis <= i) return false;
        }
        // 能够跳到的最远位置是否达到了数组末尾
        return maxDis >= nums.length - 1;
    }
    // 也可以这样写
    public boolean canJump2(int[] nums) {
        int n = nums.length;
        // 能到达的最远位置
        int farthest = 0;
        for (int i = 0; i < n; ++i) {
            if (i <= farthest) {
                farthest = Math.max(farthest, i + nums[i]);
                if (farthest >= n - 1) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 从后往前跳
     * 如果从倒数第二个位置能到达最后一个位置，就判断之前位置能不能到达倒数第二个位置
     * @param nums
     * @return
     */
    public boolean canJump3(int[] nums){
        // start 起始位置
        // end 终点
        int start = nums.length - 2, end = nums.length - 1;

        while (start >= 0){
            // 从这个位置能到终点，就判断之前位置能不能到达这个位置,也就是让当前位置作为终点
            if (start + nums[start] >= end) end = start;
            start--;
        }
        // 也可以写成 return end <= 0;
        // 最终应该回到起点，根据while条件和end  = start情况得出结论,end最终不会小于0
        // 但必须能回到起点才算成功
        return end == 0;
    }

    /**
     * 复习算法小抄，动态规划
     * dp[i]表示从i位置能够跳到末尾
     * @param nums
     * @return
     */
    public boolean canJump8(int[] nums) {
        if (nums.length == 1) {
            return true;
        }
        boolean[] dp = new boolean[nums.length];
        dp[nums.length - 1] = true;
        for (int i = nums.length - 2; i >= 0; --i) {
            if (nums[i] == 0) {
                continue;
            }
            for (int j = nums[i]; j >= 1; --j) {
                if (i + j >= nums.length || dp[i + j]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[0];
    }


    public static void main(String[] args) {
        _55_JumpGame obj = new _55_JumpGame();
        System.out.println(obj.canJump2(new int[]{2, 3, 1, 1, 4}));
        System.out.println(obj.canJump2(new int[]{3, 2, 1, 0, 4}));
    }
}
