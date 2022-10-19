package com.mianshi.year2022.baidu;

import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/10/18 18:24
 * @description: Oct18_Main2
 *
 * 给定n组数字
 * 每组包括一个a和x
 * 对于每一组：寻找一个不超过x的数字b，让 a 和 b 的 异或 值尽可能大，输出b
 */
public class Oct18_Main2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n-- > 0) {
            calc(scanner.nextInt(), scanner.nextInt());
        }
    }

    private static void calc(int a, int x) {
        int b = x;
        boolean flag = false;
        for (int i = 31; i >= 0; --i) {
            if (((a >> i) & 1) == 1) {
                if (((x >> i) & 1) == 1) {
                    b ^= (1 << i);
                    flag = true;
                }
            } else {
                if (((x >> i) & 1) == 0) {
                    if (flag) {
                        b |= (1 << i);
                    }
                }
            }
        }
        System.out.println(b);
    }
}
