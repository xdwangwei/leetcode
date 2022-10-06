package com.mianshi.year2022.bytedance.main4;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/9/25 19:41
 * @description: Main
 *
 * 第一行：空格分割的数字串，分别代表每一天可能的饭量
 * 第二行：总量m
 * 问：有多少种吃完的方式？
 * 后一天的饭量必须<=前一天
 *
 * 如果所剩量<饭量的最小值，也按一天计算
 */
public class Main {

    private static int res = 0;

    private static int total = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        total = sc.nextInt();
        String[] infos = line.split(" ");
        int[] nums = new int[infos.length];
        for (int i = 0; i < infos.length; ++i) {
            nums[i] = Integer.parseInt(infos[i]);
        }
        Arrays.sort(nums);
        for (int i = nums.length - 1; i >= 0; --i) {
            backTrack(nums, i, nums[i]);
        }
        System.out.println(res);
    }

    public static void backTrack(int[] nums, int idx, int sum) {
        if (sum >= total) {
            res++;
            return;
        }
        for (int i = idx; i >= 0; --i) {
            backTrack(nums, i, sum + nums[i]);
            if (sum + nums[i] >= total) {
                break;
            }
        }
    }
}
