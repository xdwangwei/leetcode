package com.mianshi.year2022.tencent;

import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/10/16 19:57
 * @description: _Oct16_Main1
 *
 * 给定数字n和k，
 * 给定n个数字·，
 * 每次可以从n个数字中随机挑选一个，将其替换为 它的二进制表示中1的个数
 * 最多可以进行k次操作
 * 问：操作完后n个数字的元素和的最小值是多少
 */
public class _Oct16_Main2 {

    private static int res = Integer.MAX_VALUE;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> b -a);
        for (int i = 0; i < n; ++i) {
            queue.offer(scanner.nextInt());
        }
        int loop = Math.min(n, k);
        while (loop-- > 0) {
            Integer poll = queue.poll();
            int val = Integer.bitCount(poll);
            queue.offer(val);
        }
        int res = 0;
        while (!queue.isEmpty()) {
            res += queue.poll();
        }
        System.out.println(res);
    }

    // private void back(int[] nums, )
}
