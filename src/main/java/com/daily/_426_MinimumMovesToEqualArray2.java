package com.daily;

import java.util.Random;

/**
 * @author wangwei
 * @date 2022/5/19 12:46
 * @description: _426_MinimumMovesToEqualArray2
 *
 * 462. 最少移动次数使数组元素相等 II
 * 给你一个长度为 n 的整数数组 nums ，返回使所有数组元素相等需要的最少移动数。
 *
 * 在一步操作中，你可以使数组中的一个元素加 1 或者减 1 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,2,3]
 * 输出：2
 * 解释：
 * 只需要两步操作（每步操作指南使一个元素加 1 或减 1）：
 * [1,2,3]  =>  [2,2,3]  =>  [2,2,2]
 * 示例 2：
 *
 * 输入：nums = [1,10,2,9]
 * 输出：16
 *
 *
 * 提示：
 *
 * n == nums.length
 * 1 <= nums.length <= 105
 * -109 <= nums[i] <= 109
 */
public class _426_MinimumMovesToEqualArray2 {


    /**
     * 假设最后找到的数字是 x
     * 那么 对于 nums[i]来说，最少操作次数就是 abs(nums[i] - x)
     * 因此，题目转换为，寻找一个整数x，让数组所有元素到x的距离的绝对值之和最小
     *
     * 数学结论：
     *      若要 min(sum(|a[i] - x|)) , 则 x 为数组 a 的中位数
     *      若要 min(sum(|a[i] - x|^2)) , 则 x 为数组 a 的平均数
     *
     * 本题核心就在于找中文数，我们来证明为什么是中位数
     * 可以直接参考这个：https://leetcode.cn/problems/minimum-moves-to-equal-array-elements-ii/solution/by-fuxuemingzhu-13z3/
     * 假设数组元素最大值是max，最小值是min
     * 首先 我们所找的这个整数x，要么 在 [min, max] ，要么在此区间之外
     *
     *    若 x 在 [min, max] 之外，比如 x = max + 1
     *       因为所有元素都<=max，那么把所有元素都变为max所需要的操作次数肯定 < 把所有元素都变为 x 所需要的操作次数
     *       当 x < min时，道理一致
     *    因此，x 一定在 [min, max]之内
     *       此时，对于 min 和 max 这两个数字来说，无论x取中间值是多少，x到min和max的距离之和都等于 max-min
     *       所有，此时要考虑剩余元素到x的距离和最小
     *       假设新的min和max分别为mmin和mmax
     *       根据前面的结论，为了让所有元素到x的距离和最小，首先，x必须在[mmin.mmax]之内取值
     *       然后，不论x取多少，x到mmin和mmax的距离之和固定为 mmax-mmin，
     *       那么问题转变为 剩下的元素到x的距离之和最小。。。。
     *
     *   综上， x 必须是不断选择 数组（以及子数组）最大值和最小值之间的数字。
     *   那么，若数组有奇数个元素，最后剩下的那个子数组只有1个元素，最大最小值一样，都是原数组的中位数
     *        若原数组有偶数个元素，最后剩下的那个子数组只有两个元素，这两个数字都是原数组的中位数
     *          此时，x任取一个就行，因为x到其他元素距离和已经固定，x到这两个数字的距离和=0+b-a或者a-b+0，取绝对值那不是相等么
     *
     *  所以问题转变为：
     *      寻找原数组的中位数，计算每个元素和中位数的绝对值之和
     *  方法一：排序后，索引 n / 2处就是中位数
     *  方法二：快速选择排序，借助快速排序的partition，每一次partition后返回的j代表数组前j个元素比j位置元素小，
     *      那么，根据二分思想，也能找到中位数
     *
     * @param nums
     * @return
     */
    public int minMoves2(int[] nums) {
        // // 方法一排序
        // Arrays.sort(nums);
        // // 找到中位数
        // int median = nums[nums.length / 2];

        // 方法二：快速选择法得到中位数
        int median = quickSelect(nums);

        // 计算每个元素和中位数的绝对值之和
        int ans = 0;
        for (int num : nums) {
            ans += Math.abs(num - median);
        }
        return ans;
    }


    /**
     * 快速选择方法，返回中位数
     * @param nums
     * @return
     */
    private int quickSelect(int[] nums) {
        // 只有1个元素
        int l = 0, h = nums.length - 1;
        while (l <= h) {
            // 每一次partition返回的j代表nums[j]是原数组中第j大的元素
            int j = randomPartition(nums, l, h);
            // 我们要找到的是中位数，也就是原数组第 n / 2 大的元素
            if (j == nums.length / 2) {
                return nums[j];
            } else if (j < nums.length / 2) {
                l = j + 1;
            } else {
                h = j - 1;
            }
        }
        // 中位数一定存在，不会走到这里
        return 0;
    }


    Random random = new Random();

    /**
     * 学会一种新的partition写法
     * 快速排序，partition过程
     * 以nums[right]为pivot，返回j，保证 nums[left, j] 都 < pivot=nums[right]
     * @param nums
     * @param low
     * @param high
     * @return
     */
    private int randomPartition(int[] nums, int low, int high) {
        if (low == high) return low;
        // 避免最坏时间复杂度，先随机选择一个位置和 预定的pivot元素位置交换
        int randIndex = low + random.nextInt(high - low + 1);
        swap(nums, randIndex, high);

        // 选取最右边元素为pivot，j用于顺序扫描当前范围内的全部元素
        // i 用于保证 nums[low, i]位置元素都 满足 <= pivot
        // 也就是 每一次 nums[j] < pivot ，就把nums[j]交换到i位置去，保证<pivot的元素全部放置在左边部分
        int pivot = nums[high], i = low - 1, j = low;
        while (j <= high) {
            // 每一次 nums[j] < pivot ，就把nums[j]交换到i位置去，保证<pivot的元素全部放置在左边部分
            if (nums[j] < pivot) {
                swap(nums, ++i, j);
            }
            ++j;
        }
        // 把pivot放置在正确位置
        swap(nums, ++i, high);
        // nums[low, i]位置 都满足 <= pivot
        return i;
    }

    private void swap(int[] nums, int i, int j) {
        if (i == j) {
            return;
        }
        nums[i] = nums[i] ^ nums[j];
        nums[j] = nums[i] ^ nums[j];
        nums[i] = nums[i] ^ nums[j];
    }
}
