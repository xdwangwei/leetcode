package com.mianshi.year2020.alibaba.April;

import java.util.HashSet;

/**
 * 1、找出序列中重复的数字和缺少的数字
 * 由于记账错误，给定的一个正整数序列里面，有两个数字重复了，同时缺少了一个数字。
 * 要求写一个函数，找出序列中重复的数字和缺少的数字。
 *
 *
 * 例如：
 *
 * 输入：[1, 5, 2, 2, 3]
 * 输出：[2, 4]
 */

public class _01_WrongNumbers {

    public static int[] solution(int[] nums){

        if(nums == null || nums.length == 0)
            return null;

        int n = nums.length;
        int[] res = new int[2];
        int i = 0;

        HashSet<Integer> set = new HashSet<>(n);

        for(i = 0; i < n; i++){
            if(!set.contains(nums[i])){
                set.add(nums[i]);
            }else{
                res[0] = nums[i];
                nums[i] = -1;
                break;
            }
        }

        for(i = 0; i < n; i++){
            while(nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i])
                swap(nums, i, nums[i] - 1);
        }

        for(i = 0; i < n; i++){
            if(nums[i] != i + 1){
                res[1] = i + 1;
                break;
            }
        }

        if(i >= n) res[1] = n + 1;
        return res;
    }

    private static void swap(int[] array, int i, int j){
        if(i == j) return;
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args){
        int[] solution = solution(new int[]{1, 5, 2, 2, 3});
        for (int i : solution) {
            System.out.println(i);
        }
    }
}