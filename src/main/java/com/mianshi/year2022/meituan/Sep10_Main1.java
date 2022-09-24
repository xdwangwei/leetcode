package com.mianshi.year2022.meituan;

import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/9/10 15:30
 * @description: Sep10_Main1
 */
public class Sep10_Main1 {

    static class Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            int loop = scanner.nextInt();
            for (int i = 0; i < loop; ++i) {
                int n = scanner.nextInt();
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int k = scanner.nextInt();
                int mei = x * k;
                int oth = (n - k) * y;
                if (mei < oth) {
                    System.out.println("Win");
                } else if (mei > oth) {
                    System.out.println("Lose");
                } else {
                    System.out.println("Tie");
                }
            }
        }
    }
}
