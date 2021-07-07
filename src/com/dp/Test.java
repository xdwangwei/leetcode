package com.dp;

import java.util.Scanner;

/**
 * @author wangwei
 * 2020/9/4 8:58
 */
public class Test {

    private static final int RADIX = 998244353;

    private static long calculate(int n) {
        if (n == 0) return 1;
        if (n <= 2) return n;
        return n * calculate(n - 1);
    }

    public static void main(String[] args) {
        int n, a, b, c, d;
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        a = sc.nextInt();
        b = sc.nextInt();
        c = sc.nextInt();
        d = sc.nextInt();
        int res = (int) ((calculate(n * n) / calculate(a) / calculate(b) / calculate(c) / calculate(d)) % RADIX);
        System.out.println(res);
        sc.close();
    }
}
