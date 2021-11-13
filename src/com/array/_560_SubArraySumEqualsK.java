package com.array;

import java.util.HashMap;

/**
 * @author wangwei
 * 2020/8/28 11:04
 *
 *
给定一个整数数组和一个整数 k，你需要找到该数组中和为 k 的连续的子数组的个数。

示例 1 :

输入:nums = [1,1,1], k = 2
输出: 2 , [1,1] 与 [1,1] 为两种不同的情况。
说明 :

数组的长度为 [1, 20,000]。
数组中元素的范围是 [-1000, 1000] ，且整数 k 的范围是 [-1e7, 1e7]。
 */
public class _560_SubArraySumEqualsK {

    public int subarraySum(int[] nums, int k) {
        // return subarraySum1(nums, k);
        return subarraySum2(nums, k);
    }

    /**
     * 计算前缀和，sum[i +1]表示num[0]+...+nums[i]
     * 想求 nums[i..j] 的和，只需要一步操作 preSum[j+1]-preSum[i] 即可
     */
    public int subarraySum1(int[] nums, int k) {
        // java数组默认初始化为0
        int[] sum = new int[nums.length + 1];
        // 计算前缀和
        for (int i = 0; i < nums.length; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        int ans = 0;
        // 找出所有差为k的组合
        for (int i = 1; i <= nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (sum[i] - sum[j] == k)
                    ans++;
            }
        }
        return ans;
    }

    /**
     * 第一种解法，最后的双重循环，其实对于每个sum[i]来说，想知道有多少个sum[j]满足 sum[j]=sum[i]-k
     * 我直接记录下有几个 sum[j] 和 sum[i] - k 相等，直接更新结果，就避免了内层的 for 循环
     */
    public int subarraySum2(int[] nums, int k) {
        int len = nums.length;
        // 保存每个前缀和，及其出现的次数
        HashMap<Integer, Integer> sumMap = new HashMap<>();
        // 对于一开始的情况，下标 0 之前没有元素，可以认为前缀和为 0，个数为 1 个
        sumMap.put(0, 1);
        int sum_i = 0, ans = 0;
        for (int num : nums) {
            // 计算前缀和 num[0...i]
            sum_i += num;
            // 对于每个sum[i]，想知道有多少个sum[j]满足 sum[j]=sum[i]-k
            // sum[j]一定会小于sum[i]，所以它一定在之前统计过了(最后一行)
            int sum_j = sum_i - k;
            if (sumMap.containsKey(sum_j)) {
                // 更新组合数
                ans += sumMap.get(sum_j);
            }
            // 当前这个前缀和在map中的出现次数增1
            sumMap.put(sum_i, sumMap.getOrDefault(sum_i, 0) + 1);
        }
        return ans;
    }
}
