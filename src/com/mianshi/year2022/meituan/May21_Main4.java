package com.mianshi.year2022.meituan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/5/21 15:55
 * @description: May21_Main4
 *
 * 题目描述：

 *   快到期末了，小美有n门课程需要复习，第i门课程需要ai小时的时间复习。小美不想让自己变成拖延症，因此，一旦小美决定在某一天复习该课程，她就一定会将其复习完毕。
 *
 *   现在距离期末考试还有m天，小美可以自行安排每天复习哪几门课程。
 *   在确保小美每天复习时长尽量均衡的情况下（避免出现某一天复习时间过长），请问小美复习时长最长的一天最少需要复习多少小时？
 *
 *
 *
 * 输入描述
 * 第一行两个正整数n,m(1≤m≤n≤9)。
 *
 * 第二行n个正整数表示ai(1≤ai≤107)
 *
 * 输出描述
 * 仅一行，一个正整数，表示小美复习时长最长的一天最少需要复习的时间。
 *
 *
 * 样例输入
 * 4 2
 * 2 4 7 8
 * 样例输出
 * 11
 *
 * 提示
 * 样例解释
 *
 *        第一天复习第1、4门课程，第二天复习第2，3门课程。第二天复习时长最长，为11小时。其余任何复习方案都会导致某一天复习时长大于11小时。
 */
public class May21_Main4 {

    private static int minDayCost = Integer.MAX_VALUE;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), m = scanner.nextInt();
        int[] costs = new int[n];
        List<Integer>[] buckets = new ArrayList[m];
        for (int i = 0; i < n; ++i) {
            costs[i] = scanner.nextInt();
        }
        Arrays.sort(costs);
        for (int i = 0; i < m; ++i) {
            buckets[i] = new ArrayList<>();
            buckets[i].add(0);
        }
        backTrack(costs, n - 1, buckets);
        System.out.println(minDayCost);
    }

    private static void backTrack(int[] costs, int idx, List<Integer>[] buckets) {
        if (idx == -1) {
            int maxDay = 0;
            for (List<Integer> bucket : buckets) {
                maxDay = Math.max(maxDay, bucket.get(0));
            }
            minDayCost = Math.min(minDayCost, maxDay);
            return;
        }
        for (int i = 0; i < buckets.length; ++i) {
            if (buckets[i].get(0) + costs[idx] >= minDayCost) {
                continue;
            }
            buckets[i].add(costs[idx]);
            buckets[i].set(0, buckets[i].get(0) + costs[idx]);
            backTrack(costs, idx - 1, buckets);
            buckets[i].remove(buckets[i].size() - 1);
            buckets[i].set(0, buckets[i].get(0) - costs[idx]);
        }
    }
}
