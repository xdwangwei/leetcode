package com.mianshi.year2022.bytedance.main3;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/9/25 19:38
 * @description: Main
 *
 * 给定x和y，问从x变化到y的最少步数
 * 第一步 步长必须为1，最后一步步长必须为1
 * 每一次的步长必须为上一次步长的-1，0，1
 * 比如：x = 12，y = 6
 * 输出4，(步长分别为： 1，2，2，1)
 */
public class Main {

    static class Pair {
        int step, val;

        public Pair(int v, int s) {
            this.step = v;
            this.val = s;
        }

        @Override
        public int hashCode() {
            return Objects.hash(step, val);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Pair && ((Pair)obj).hashCode() == hashCode();
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = 0; i < n; ++i) {
            int start = sc.nextInt();
            int end = sc.nextInt();
            System.out.println(bfs(start, end));
        }
    }

    private static int bfs(int start, int end) {
        if (start == end) {
            return 0;
        }
        if (Math.abs(start - end) == 2) {
            return 2;
        }
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        end -= start;
        start = 0;
        Set<Pair> visited = new HashSet<>();
        Set<Pair> q1 = new HashSet<>();
        int step = 2;
        q1.add(new Pair(start + 1, 1));

        while (!q1.isEmpty()) {
            Set<Pair> temp = new HashSet<>();
            for (Pair pair : q1) {
                visited.add(pair);
                for (int i = -1; i <= 1; ++i) {
                    int ns = pair.step + i;
                    if (ns < 0) {
                        continue;
                    }
                    if (pair.val + ns == end - 1) {
                        return ++step;
                    }
                    Pair np1 = new Pair(pair.val + ns, ns);
                    if (!visited.contains(np1)) {
                        temp.add(np1);
                    }
                }
            }
            step++;
            q1 = temp;
        }

        return -1;
    }
}

// 3
// 12 6
// 34 45
// 50 30
