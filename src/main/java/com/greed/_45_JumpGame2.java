package com.greed;

import java.util.Arrays;

/**
 * @author wangwei
 * 2020/4/20 19:02
 * <p>
 * 给定一个非负整数数组，你最初位于数组的第一个位置。
 * <p>
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * <p>
 * 你的目标是使用最少的跳跃次数到达数组的最后一个位置。
 * <p>
 * 示例:
 * <p>
 * 输入: [2,3,1,1,4]
 * 输出: 2
 * 解释: 跳到最后一个位置的最小跳跃数是 2。
 *     从下标为 0 跳到下标为 1 的位置，跳1步，然后跳3步到达数组的最后一个位置。
 * 说明:
 * <p>
 * 假设你总是可以到达数组的最后一个位置。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/jump-game-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _45_JumpGame2 {

    /**
     * 贪心算法
     *
     * 在位置i，可以有nums[i]种跳法，
     * 如果某一个作为 起跳点 的格子可以跳跃的距离是 3，那么表示后面 3 个格子都可以作为 起跳点
     * 如果从这个 起跳点 起跳叫做第 1 次 跳跃，那么从后面 3 个格子起跳 都 可以叫做第 2 次 跳跃
     * 所以，当一次 跳跃 结束时，从i+1开始，到现在 能跳到最远的位置 i+nums[i]，都是下一次 跳跃 的 起跳点，属于同一次跳跃
     *
     * 一句话解释: 从一个位置跳到它能跳到的最远位置之间的都只需要一步!
     *
     * 所以，如果一开始都能跳到，后面再跳到的肯定步数要变多!
     * <p>
     * 我们每次在可跳范围内选择可以使得跳的更远的位置
     * 用 i 表示当前位置，curEnd表示当次跳跃包含的所有起跳点中最后一个起跳点
     *
     * 这里要注意一个细节，就是 for 循环中，i < nums.length - 1，少了末尾。
     * 因为开始的时候边界是第 0 个位置，steps 已经加 1 了。
     * 如下图，2 3 1 1 2 2 2如果最后一步刚好跳到了末尾，此时 steps 其实不用加 1 了。
     * 如果是 i < nums.length，i 遍历到最后的时候，会进入 if 语句中，steps 会多加 1。
     *
     * @param nums
     * @return
     */
    public int jump(int[] nums) {
        // 第一次跳跃只有一个起点，最后一个起跳点就是0
        int curEnd = 0, maxPos = 0;
        int steps = 0;
        // i表示当前位置
        for (int i = 0; i < nums.length - 1; i++) {
            // 找到当次可跳范围内能跳到的最远位置
            maxPos = Math.max(maxPos, i + nums[i]);
            // 如果当次跳跃的所有起点都已试过
            if (i == curEnd) {
                // 这一次跳跃能跳到的最远位置就是下一次跳跃时的所有起点的最后一个起跳点
                curEnd = maxPos;
                // 跳数加1，从[当前起点，当前最后一个起点]起跳，都算一次跳跃
                steps++;
            }
            // i++后成为下一次跳跃第一个起跳点
        }
        // 因为题目假设一定能到达最后，所以最终返回step即可
        return steps;
    }

    /**
     * 复习算法小抄，动态规划
     * dp[i]表示从i位置跳到末尾所需要的最小步数
     * @param nums
     * @return
     */
    public int jump8(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return 0;
        }
        int[] dp = new int[n];
        Arrays.fill(dp, n);
        dp[n - 1] = 0;
        for (int i = n - 2; i >= 0; --i) {
            for (int j = nums[i]; j >= 1; --j) {
                if (i + j >= n) {
                    dp[i] = 1;
                    break;
                } else {
                    dp[i] = Math.min(dp[i], dp[i + j] + 1);
                }
            }
        }
        return dp[0];
    }

}
