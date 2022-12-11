package com.freefish.temp;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/12/11 18:25
 * @description: Main2
 */
public class Main2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        int k = scanner.nextInt();
        PriorityQueue<Integer> minPq = new PriorityQueue<>((a, b) -> a - b);
        for (int i = 0; i < 2 * k; ++i) {
            minPq.offer(scanner.nextInt());
        }
        PriorityQueue<Integer> maxPq = new PriorityQueue<>((a, b) -> b - a);
        for (int i = 0; i < m; ++i) {
            int op = scanner.nextInt();
            if (op == 2) {
                List<Integer> list = new ArrayList<>();
                int j = 0;
                while (j++ < 2 * k - 1) {
                    System.out.print(minPq.peek() + " ");
                    list.add(minPq.remove());
                }
                System.out.println(minPq.peek());
                for (Integer nm : list) {
                    minPq.offer(nm);
                }
            } else if (op == 3) {
                int x = scanner.nextInt();
                List<Integer> list = new ArrayList<>();
                int j = 1;
                while (j++ != x) {
                    list.add(minPq.remove());
                }
                minPq.remove();
                for (Integer nm : list) {
                    minPq.offer(nm);
                }
            } else {
                int x = scanner.nextInt();
                if (x < minPq.peek()) {
                    maxPq.offer(x);
                } else {
                    minPq.offer(x);
                }
            }
            int extra = minPq.size() - 2 * k;
            while (maxPq.size() < extra || maxPq.size() > extra + 1) {
                if (maxPq.size() > extra) {
                    minPq.offer(maxPq.remove());
                } else {
                    maxPq.offer(minPq.remove());
                }
            }
        }
    }
}
// 3 1
// 2 3
// 1 4
// 3 1
// 2

// 2 4

// 5 2
// 8 4 2 6
// 2
// 1 5
// 2
// 1 3
// 2

// 2 4 6 8
// 4 5 6 8
// 3 4 5 6
