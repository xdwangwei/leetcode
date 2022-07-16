package com.freefish.week01;

import java.util.Scanner;

/**
 * @author wangwei
 * 2021/8/10 11:17
 *
 * 最大连续子序列和
 *
 * 输入：
 *      9
 *      -2 2 -3 4 -1 2 1 -5 3
 * 输出：
 *      6
 */
public class Main1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = scanner.nextInt();
        }
        // dp[i] 表示以i位置结束的子序列的最大和
        // dp[i] = dp[i-1] > 0 ? dp[i-1] + nums[i] : dp[i-1](舍弃前面部分，自己独立)
        // dp[i] 只与 dp[i-1] 有关，所以状态压缩
        // 此处的dp相当于上面的dp[j - 1]
        int dp = nums[0];
        int max = nums[0];
        for(int j = 1; j < n; j++){
            // 因为是连续子序列，所以对于当前元素，要么和前一个连接在一起，要么自己独立，选择更大的那个
            dp = Math.max(dp + nums[j], nums[j]);
            max = Math.max(max, dp);
        }
        System.out.println(max);
        scanner.close();
    }
}
