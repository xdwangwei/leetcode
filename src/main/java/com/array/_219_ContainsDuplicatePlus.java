package com.array;

import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，
 * 使得 nums [i] = nums [j]，并且 i 和 j 的差的 绝对值 至多为 k。
 *
 * 输入: nums = [1,2,3,1,2,3], k = 2
 * 输出: false
 *
 * @author wangwei
 * 2020/3/31 18:15
 */
public class _219_ContainsDuplicatePlus {

    public static boolean solution1(int[] nums, int k) {
        if (nums == null || nums.length < 2)
            return false;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < Math.min(nums.length, i + k + 1); j++) {
                if (nums[i] == nums[j])
                    return true;
            }
        }
        return false;
    }

    /**
     * 注意排序会打乱原来元素的位置，如果一定要排序必须使用额外空间保存原位置
     *
     * 维护一个大小为k的hash表，逐个遍历数组元素，若hash表中存在与当前元素相等的元素
     * 说明在距离k内找到了重复元素，否则将其加入hash表，
     * 若hash表的大小超过k则移除表中第一个元素
     * （不需要考虑移除了1，之后来了个1，此种情况下，这两个元素虽然相等，但其距离绝对超过k，不满足）
     * @param nums
     * @param k
     * @return
     */
    public static boolean solution2(int[] nums, int k) {
        if (nums == null || nums.length == 0)
            return false;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i]))
                return true;
            set.add(nums[i]);
            if (set.size() > k)
                set.remove(nums[i - k]); //表中最前面那个元素
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(solution1(new int[]{1, 2, 3, 1, 2, 3}, 2));
    }
}
