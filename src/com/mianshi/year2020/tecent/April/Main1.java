package com.mianshi.year2020.tecent.April;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * @author wangwei
 * 2020/4/26 20:31
 */
public class Main1 {

    private static Queue<Integer> queue = new LinkedList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 一共有 多少轮
        int t = scanner.nextInt();
        for (int i = 0; i < t; i++) {
            // 这一次有多少操作
            int operateCount = scanner.nextInt();
            // 读完数字再读字符串，需要加这一句
            scanner.nextLine();
            for (int j = 0; j < operateCount; j++) {
                // 获取输入，五种可能
                String line = scanner.nextLine();
                String[] info = line.split(" ");
                if (info.length > 1) {
                    // 插入 PUSH 1
                    queue.add(Integer.valueOf(info[1]));
                } else if (line.equals("TOP")) {
                    // 队列空，输出 -1
                    if (queue.size() == 0)
                        System.out.println(-1);
                    else
                        // 输出队首元素
                        System.out.println(queue.peek());
                } else if (line.equals("POP")) {
                    // 队列空，输出 -1
                    if (queue.size() == 0)
                        System.out.println(-1);
                    else
                        // 移除队首元素
                        queue.poll();
                } else if (line.equals("SIZE")) {
                    // 输出队列大小
                    System.out.println(queue.size());
                } else if (line.equals("CLEAR"))
                    // 清空队列
                    queue.clear();
            }
        }
    }
}
// 2
// 7
// PUSH 1
// PUSH 2
// TOP
// POP
// TOP
// POP
// POP
// 5
// PUSH 1
// PUSH 2
// SIZE
// POP
// SIZE
