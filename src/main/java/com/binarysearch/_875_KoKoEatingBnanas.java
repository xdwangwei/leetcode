package com.binarysearch;

/**
 * @author wangwei
 * 2020/8/30 18:28
 * 珂珂喜欢吃香蕉。这里有N堆香蕉，第 i 堆中有piles[i]根香蕉。警卫已经离开了，将在H小时后回来。
 *
 * 珂珂可以决定她吃香蕉的速度K（单位：根/小时）。每个小时，她将会选择一堆香蕉，从中吃掉 K 根。如果这堆香蕉少于 K 根，她将吃掉这堆的所有香蕉，然后这一小时内不会再吃更多的香蕉。  
 *
 * 珂珂喜欢慢慢吃，但仍然想在警卫回来前吃掉所有的香蕉。
 *
 * 返回她可以在 H 小时内吃掉所有香蕉的最小速度 K（K 为整数）。
 *
 *
 * 示例 1：
 *
 * 输入: piles = [3,6,7,11], H = 8
 * 输出: 4
 * 示例2：
 *
 * 输入: piles = [30,11,23,4,20], H = 5
 * 输出: 30
 * 示例3：
 *
 * 输入: piles = [30,11,23,4,20], H = 6
 * 输出: 23
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/koko-eating-bananas
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 */
public class _875_KoKoEatingBnanas {

    /**
     * 二分搜索
     * 并非只有有序数组搜索指定值类的问题才可能用二分
     * 只要题目所要求的值和存在的某个限制能够形成单调性，就可以二分
     * <p>
     * 比如这道题，由于一个小时只能进食一次，且一次最多吃掉一堆，要求的时能吃完所有香蕉的最小速度x
     * 所以 x 越大，吃掉所有香蕉的时间越短，x 最大应该考虑 最多的那堆香蕉数，再大没有意义
     * 当然如果这样的话需要先遍历一次数组找到最大值，
     * 题目给出了每个参数的取值范围，比如 数组元素最大值是 10^8 ， 我们可以直接使用这个值
     * <p>
     * 先构造一个函数f，计算以速度x吃完所有香蕉花费的时间，以它作为二分的【有序基础】
     * 然后就是一个求 左边界的二分搜索
     * <p>
     * 吃的最慢就是每小时吃1个香蕉，
     * 吃的最快就是每小时吃掉最多的那一堆香蕉（更快无意义，每小时只能吃一堆香蕉）
     * 所以 1  <=  k   <=  max(piles)
     * 在这个范围中找到能够满足在h小时内吃完所有香蕉的最小的k，相当于找左边界的二分
     *
     * @param piles
     * @param H
     * @return
     */
    public int minEatingSpeed(int[] piles, int H) {
        // speed的上下边界
        int left = 1, right = 1;
        // 上界是最多的那堆香蕉数量
        // right = 100000000;
        for (int count: piles) right = Math.max(right, count);
        // 上界不可达[a,b)形式的二分查找
        right += 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            // 在当前速度下吃完，需要多少消失
            int cost = calculate(piles, mid);
            // 比目标时间少，那速度可以更小一些
            if (cost <= H) {
                right = mid;
            // 吃不完，速度得加快
            } else {
                left = mid + 1;
            }
        }
        // 由于这个题目一定存在答案，所以不用判断不存在情况
        return left;
    }

    /**
     * 假设每小时吃掉speed根香蕉，吃完所有香蕉需要花费几小时
     *
     * 吃每一堆香蕉pile用的时间是 pile对speed向上取整，
     * pile / speed + (pile % speed == 0?0:1)
     * Math.ceil(pile * 1.0 / speed);
     * (pile + speed - 1) / speed  比较实用的技巧
     * @param piles
     * @param speed
     * @param
     * @return
     */
    private int calculate(int[] piles, int speed) {
        int time = 0;
        // 统计吃完每堆香蕉的时间之和
        // for (int i = 0; i < n; ++i) {
        //     res += bananas[i] / x;
        //     if (bananas[i] % x > 0) {
        //         res++;
        //     }
        // }
        for (int count : piles) {
            time += (count + speed - 1) / speed;
        }
        return time;
    }
}
