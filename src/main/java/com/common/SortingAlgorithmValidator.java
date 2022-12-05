package com.common;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/10/25 16:29
 * @description: SortingAlgorithmValidator
 */
public class SortingAlgorithmValidator {

    /**
     * 生成一个长度随机，数值随机的数组
     */
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int length = (int)((maxSize + 1) * Math.random());
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            // [0...m] - [0...m - 1]  --> [-(m-1)...m]
            arr[i] = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * maxValue);
        }
        return arr;
    }

    /**
     * 得到当前数组一个复制品
     * @param arr
     * @return
     */
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        return Arrays.copyOf(arr, arr.length);
    }


    public static void main(String[] args) {
        final int testTimes = 10000;    //测试次数
        final int maxSize = 20;         //最大测试容量
        final int maxNum = 1000;        //最大测试数据
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxNum);
            int[] arr2 = copyArray(arr1);
            // 自己的排序算法
            Algorithms.quickSort(arr1);
            // Algorithms.threeWayQuickSort(arr1);
            // jdk系统实现
            Arrays.sort(arr2);
            // 验证排序结果
            if (!Arrays.equals(arr1, arr2)) {
                System.out.println("fuck, fucking algorithms");//没错就恭喜，有错就报错
                return;
            }
        }
        System.out.println("good job");
    }
}
