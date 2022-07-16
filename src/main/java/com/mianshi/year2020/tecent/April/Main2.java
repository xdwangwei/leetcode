package com.mianshi.year2020.tecent.April;

import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @author wangwei
 * 2020/4/26 20:58
 */
public class Main2 {

    // 每一个点的坐标
    static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 有多少轮
        int num = sc.nextInt();
        for (int k = 0; k < num; k++) {
            // 这一次，每个集合里有几个点
            int n = sc.nextInt();
            // A集合
            Position[] posA = new Position[n];
            // B集合
            Position[] posB = new Position[n];
            for (int i = 0; i < n; i++) {
                posA[i] = new Position(sc.nextInt(), sc.nextInt());
            }
            for (int i = 0; i < n; i++) {
                posB[i] = new Position(sc.nextInt(), sc.nextInt());
            }
            // 求出 AB集合中，那两个点的距离最近，输出最近的那个距离
            PriorityQueue<Double> queue = new PriorityQueue<>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int diffX = posA[i].x - posB[j].x;
                    int diffY = posA[i].y - posB[j].y;
                    queue.add(Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)));
                }
            }
            Double poll = queue.poll();
            System.out.printf("%.3f\n", poll);
        }
    }
}
// 2
// 4
// 0 0
// 0 1
// 1 0
// 1 1
// 2 2
// 2 3
// 3 2
// 3 3
// 4
// 0 0
// 0 0
// 0 0
// 0 0
// 0 0
// 0 0
// 0 0
// 0 0