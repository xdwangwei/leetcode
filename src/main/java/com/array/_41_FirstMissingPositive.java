package com.array;

import java.util.ArrayList;


/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/24 周日 15:05
 **/
public class _41_FirstMissingPositive {


    
    // 桶排序 微信公众号 五分钟学算法
    // We don't care about duplicates or non-positive integers  不考虑全是负数和零，
    // *** 不考虑大于 n 的数字，因为首次缺失的正数一定小于或等于 n + 1 ***
    // 只关心落在区间[1,n]的数字，i 就应该放在索引为 i -1 的位置，否则就交换到正确位置
    // 也可以说最终索引为i的位置上的数字应该是i+1，如果不是，那么这个位置应该放的数(i+1)就是答案
    // 如果所有的位置和值都和谐，那么数组长度加1就是答案(无全负，如果有合适正数，一定能放到正确位置)
    // 遍历一次数组把大于等于1的和小于数组大小的值放到原数组对应位置，
    // 然后再遍历一次数组查当前下标是否和值对应，如果不对应那这个下标就是答案，
    // 否则遍历完都没出现那么答案就是数组长度加1。

    public static int solution(int[] nums){

        int len = nums.length;
        // 遍历每个位置
        for (int i = 0; i  < len; i++){
            // 这个位置放的数字是nums[i]，它合理的位置应该是nums[i] - 1，
            // 判断一下nums[i]-1的位置上的数是否等于nums[i]，如果不等就交换过去
            // 只考虑 [1, n] 
            while (nums[i] > 0 && nums[i] <= len && nums[nums[i] - 1] != nums[i]){
                swap(nums, i, nums[i] - 1);
            }
        }
        // 交换完后， 判断每个位置和值是否满足 nums[i] = i + 1，不满足说明当前位置对应的那个数没有出现
        for (int i = 0; i < len; i++){
            if (nums[i] - 1 != i) // nums[i] != i + 1
                return i + 1; // [1,2,48,5,6]
        }
        return len + 1; //每个位置都满足对应关系，[1,2,3,4,5]
    }
    private static void swap(int[] arr, int i, int j){
        if (i == j) {
            return;
        }
        int temp =  arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return;
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{1, 2, 0}));
        System.out.println(solution(new int[]{3,4,-1,1}));
        System.out.println(solution(new int[]{7,8,9,11,12}));

    }
}
