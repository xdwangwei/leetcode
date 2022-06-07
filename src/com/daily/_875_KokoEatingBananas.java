package com.daily;

/**
 * @author wangwei
 * @date 2022/6/7 9:23
 * @description: _875_KokoEatingBananas
 *
 * 875. 爱吃香蕉的珂珂
 * 珂珂喜欢吃香蕉。这里有 n 堆香蕉，第 i 堆中有 piles[i] 根香蕉。警卫已经离开了，将在 h 小时后回来。
 *
 * 珂珂可以决定她吃香蕉的速度 k （单位：根/小时）。每个小时，她将会选择一堆香蕉，从中吃掉 k 根。如果这堆香蕉少于 k 根，她将吃掉这堆的所有香蕉，然后这一小时内不会再吃更多的香蕉。
 *
 * 珂珂喜欢慢慢吃，但仍然想在警卫回来前吃掉所有的香蕉。
 *
 * 返回她可以在 h 小时内吃掉所有香蕉的最小速度 k（k 为整数）。
 *
 *
 *
 * 示例 1：
 *
 * 输入：piles = [3,6,7,11], h = 8
 * 输出：4
 * 示例 2：
 *
 * 输入：piles = [30,11,23,4,20], h = 5
 * 输出：30
 * 示例 3：
 *
 * 输入：piles = [30,11,23,4,20], h = 6
 * 输出：23
 *
 *
 * 提示：
 *
 * 1 <= piles.length <= 104
 * piles.length <= h <= 109
 * 1 <= piles[i] <= 109
 * 通过次数75,816提交次数152,413
 */
public class _875_KokoEatingBananas {


    /**
     * 方法：寻找左边界的二分搜索
     *
     * 由于存在「吃完这堆的所有香蕉，然后这一小时内不会再吃更多的香蕉」的条件，因此不会存在多堆香蕉共用一个小时的情况，
     * 即每堆香蕉都是相互独立，同时可以明确每堆香蕉的耗时为 ⌈piles[i]/k⌉（其中 k 为速度）。
     *
     * 因此我们可以二分 k 值，在以 k 为分割点的数组上具有「二段性」：
     *
     * 小于 k 的值，总耗时 ans 必然不满足 ans ≤ h；
     * 大于等于 k 的值，总耗时 ans 必然满足 ans ≤ h。
     * 然后我们需要确定二分的范围，每堆香蕉至少消耗一个小时，因此大于 max(piles[i]) 的速度值 k 是没有意义的（与 k=max(piles[i]) 等价），
     * 因此我们可以先对 piles 进行一次遍历，找最大值，再二分；
     * 也可以直接利用数据范围 1 <= piles[i] <= 10^9,确定一个粗略边界进行二分。
     *
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/koko-eating-bananas/solution/by-ac_oier-4z7i/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param piles
     * @param h
     * @return
     */
    public int minEatingSpeed(int[] piles, int h) {
        // 确定二分搜索上下边界
        // 下边界1，上边界 最大堆香蕉数量 或者 题目给定的数据范围
        // high = Arrays.stream(piles).max();
        int low = 1, high = (int) 1e9;
        // 二分搜索
        while (low < high) {
            // 中间值（速度）
            int mid = low + (high - low) / 2;
            // 以这个速度吃完所有香蕉最少需要几个小时
            int hours = getTime(piles, mid);
            // 速度不够
            if (hours > h) {
                low = mid + 1;
            } else {
                // 能够吃完，这种情况下尽可能吃的慢，所以去寻找 更小的速度
                high = mid;
            }
        }
        // 退出时，low==high
        return low;
    }


    /**
     * 每小时最多只能吃speed根香蕉，吃完所有香蕉最少需要几小时
     * @param piles
     * @param speed
     * @return
     */
    private int getTime(int[] piles, int speed) {
        int hours = 0;
        for (int pile : piles) {
            // 每堆香蕉的耗时为 ⌈piles[i]/k⌉（其中 k 为速度）。
            hours += (pile + speed - 1) / speed;
        }
        return hours;
    }
}
