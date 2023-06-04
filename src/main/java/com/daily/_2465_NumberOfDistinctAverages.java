package com.daily;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/6/4 16:35
 * @description: _2465_NumberOfDistinctAverages
 */
public class _2465_NumberOfDistinctAverages {

    /**
     * 方法一：排序 + 哈希表
     * 思路与算法
     *
     * 我们对数组 nums 进行排序，随后使用两个指针，初始分别指向 nums 首元素和尾元素对数组进行遍历，就可以不断得到当前数组的最大值和最小值。
     *
     * 由于「不同平均值的数目」和「不同和的数目」是等价的，因此在计算时，可以直接求出两个指针指向元素的和，代替平均值，避免浮点运算。
     * 我们只需要使用一个哈希集合，将所有的和添加进去，随后哈希集合中的元素个数即为答案。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/number-of-distinct-averages/solution/bu-tong-de-ping-jun-zhi-shu-mu-by-leetco-9279/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int distinctAverages(int[] nums) {
        // 排序
        Arrays.sort(nums);
        Set<Integer> set = new HashSet<>();
        // 每次取首尾元素和，加入集合；数组长度是偶数，一半的末尾位置是 n/2
        for (int i = 0, n = nums.length; i < n / 2; ++i) {
            set.add(nums[i] + nums[n - 1 - i]);
        }
        // 返回 set 元素个数
        return set.size();
    }
}
