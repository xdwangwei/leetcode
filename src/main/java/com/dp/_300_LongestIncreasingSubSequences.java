package com.dp;

import java.util.Arrays;

/**
 * @author wangwei
 * 2020/8/29 8:18
 *
 * 300. 最长上升子序列
 * 给定一个无序的整数数组，找到其中最长上升子序列的长度。
 *
 * 示例:
 *
 * 输入: [10,9,2,5,3,7,101,18]
 * 输出: 4
 * 解释: 最长的上升子序列是 [2,3,7,101]，它的长度是 4。
 * 说明:
 *
 * 可能会有多种最长上升子序列的组合，你只需要输出对应的长度即可。
 * 你算法的时间复杂度应该为 O(n2) 。
 * 进阶: 你能将算法的时间复杂度降低到 O(n log n) 吗?
 */
public class _300_LongestIncreasingSubSequences {

    /**
     * 动态规划
     * dp[i]表示以nums[i]结尾的最长递增子序列的长度
     * 初始，dp数组初始化为1，最少包含自己
     * 对于dp[i]，在所有的dp[j](j<i && nums[j] <nums[i])中，找到最长的那个，把自己接上去
     * 最终返回dp数组中的最大值
     */
    public int lengthOfLIS1(int[] nums) {
        // dp[i]表示以nums[i]结尾的最长递增子序列的长度
        int[] dp = new int[nums.length];
        // 初始化dp数组
        Arrays.fill(dp, 1);
        int res  = 0;
        // 求解每一个dp[i]
        for (int i = 0; i < nums.length; i++) {
            // 在所有的dp[j](j<i && nums[j] <nums[i])中
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    // 找到最长的那个，把自己接上去
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            // 最终结果是dp数组中的最大值
            res = Math.max(res, dp[i]);
        }
        return res;
    }


    /**
     * 二分查找，扑克牌算法 O(NlogN)
     *
     * 给你一排扑克牌，我们像遍历数组那样从左到右一张一张处理这些扑克牌，最终要把这些牌分成若干堆。
     *
     * 处理这些扑克牌要遵循以下规则：
     *
     * 只能把点数小的牌压到点数比它大的牌上；
     * 如果当前牌点数较大没有可以放置的堆，则新建一个堆，把这张牌放进去；
     * 如果当前牌有多个堆可供选择，则选择最左边的那一堆放置。
     *
     * 遇到多个可选择堆的时候要放到最左边的堆上呢？因为这样可以保证牌堆顶的牌有序（证明：略）
     *
     * 牌堆顶的排有序，那么 遇到一个新的数字，就可以采用 左边界查找的二分查找找到他的合适位置，否则就另起一堆
     *
     * 按照上述规则执行，可以算出最长递增子序列，牌的堆数就是最长递增子序列的长度
     *
     * 作者：labuladong
     * 链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/solution/dong-tai-gui-hua-she-ji-fang-fa-zhi-pai-you-xi-jia/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int lengthOfLIS2(int[] nums) {
        int len = nums.length;
        // 从左到右每个牌堆顶的牌组成有序序列有序
        int[] top = new int[len];
        // 牌堆数
        int piles = 0;
        for (int i = 0; i < len; i++) {
            // 当前待处理的扑克牌
            int poker = nums[i];

            /** 寻找左侧边界的二分查找 **/
            int left = 0, right = piles;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (top[mid] >= poker) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            // 没找到合适位置(比所有堆顶的牌都大)，就单独作为一个牌堆，牌堆数加1
            if (left == piles) piles++;
            // 把它放在最左边那个牌堆顶
            top[left] = poker;
        }
        // 牌堆数就是LIS长度
        return piles;
    }
}
