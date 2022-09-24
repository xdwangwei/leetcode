package com.mianshi.year2022.meituan;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * 游戏
 * 时间限制： 3000MS
 * 内存限制： 589824KB
 * 题目描述：
 * 某天，小美在玩一款游戏，游戏开始时，有n台机器，每台机器都有一个能量水平，分别为a1、a2、…、an，小美每次操作可以选其中的一台机器，假设选的是第i台，那小美可以将其变成 ai+10k（k为正整数且0≤k≤9），由于能量过高会有安全隐患，所以机器会在小美每次操作后会自动释放过高的能量，即变成 (ai+10k)%m，其中%m表示对m取模，由于小美还有工作没有完成，所以她想请你帮她计算一下，对于每台机器，将其调节至能量水平为0至少需要多少次操作（机器自动释放能量不计入小美的操作次数）。
 *
 *
 *
 * 输入描述
 * 第一行两个正整数n和m，表示数字个数和取模数值。
 *
 * 第二行为n个正整数a1, a2,...... an，其中ai表示第i台机器初始的能量水平。
 *
 * 1 ≤ n ≤ 30000，2 ≤ m ≤ 30000, 0 ≤ ai < m。
 *
 * 输出描述
 * 输出一行共n个整数，其中第i个表示将第i台机器能量水平调节至0需要的最少操作次数。
 *
 *
 * 样例输入
 * 4 4
 * 0 1 2 3
 * 样例输出
 * 0 2 1 1
 *
 * 提示
 * 0无需操作即为0。
 *
 * 1通过+1+10两次操作可以变为0，变化过程为1->(1+1)%4->(2+10)%4=0。
 *
 * 2通过+10一次操作可以变为0，变化过程为2->(2+10)%4=0。
 *
 * 3通过+1一次操作可以变为0，变化过程为3->(3+1)%4=0。
 *
 * 还可以+100、+1000等操作，但在样例情况下不是最优操作。
 */
public class Sep10_Main4 {

    static class Main {

        private static int step(int num, int mod) {
            if (num == 0) {
                return 0;
            }
            Queue<Integer> queue = new ArrayDeque<>();
            Set<Integer> visited = new HashSet<>();
            queue.offer(num);
            visited.add(num);
            int step = 1;
            while (!queue.isEmpty()) {
                int sz = queue.size();
                for (int i = 0; i < sz; ++i) {
                    Integer cur = queue.poll();
                    for (int j = 0; j < 10; ++j) {
                        int nn = (int) ((cur + (long)Math.pow(10, j)) % mod);
                        if (nn == 0) {
                            return step;
                        }
                        if (!visited.contains(nn)) {
                            queue.offer(nn);
                            visited.add(nn);
                        }
                    }
                }
                step++;
            }
            return step;
        }

        public static void main(String[] args) throws InterruptedException {
            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt();
            int mod = sc.nextInt();
            List<Integer> res = new ArrayList<>(n);
            for (int i = 0; i < n; ++i) {
                res.add(0);
            }
            CountDownLatch countDownLatch = new CountDownLatch(n);
            for (int i = 0; i < n; ++i) {
                int finalI = i;
                int num = sc.nextInt();
                new Thread(() -> {
                    int step = step(num, mod);
                    res.set(finalI, step);
                    countDownLatch.countDown();
                }).start();
            }
            countDownLatch.await();
            for (int i = 0; i < n; ++i) {
                if (i == n - 1) {
                    System.out.print(res.get(i));
                } else {
                    System.out.print(res.get(i) + " ");
                }
            }
        }
    }
}
