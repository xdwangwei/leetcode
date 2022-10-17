package com.mianshi.year2022.tencent;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/10/16 19:57
 * @description: _Oct16_Main1
 *
 * 给定n个数字，保存在双端队列中
 * 大q和小q，i从1开始，最大是n
 * 当i是奇数时：大q选择弹出队首或队尾元素 ai
 * 当i是偶数时：小q选择弹出队首或队尾元素 ai
 *
 * 最终得到一个x = ai * 10^n + ai-1 * 10^n-1 + .... an * 10^n-n
 *
 * 大q希望x尽可能大，小q希望x尽可能小
 * 假设他们同样聪明，问，最终x是多少
 * 由于x数值太大，只需要输出每次弹出的队列元素序列 a1，a2，a3.。。
 */
public class _Oct16_Main3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < n; ++i) {
            deque.addLast(scanner.nextInt());
        }
        for (int i = 1; i < n; ++i) {
            Integer first = deque.peekFirst();
            Integer last = deque.peekLast();
            // 大q
            if (i % 2 == 1) {
                if (first > last) {
                    System.out.print(first + " ");
                    deque.removeFirst();
                } else {
                    System.out.print(last + " ");
                    deque.removeLast();
                }
            } else {
                if (first > last) {
                    System.out.print(last + " ");
                    deque.removeLast();
                } else {
                    System.out.print(first + " ");
                    deque.removeFirst();
                }
            }
        }
        System.out.println(deque.removeFirst());
    }
}
