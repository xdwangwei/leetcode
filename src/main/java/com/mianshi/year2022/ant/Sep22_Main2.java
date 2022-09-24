package com.mianshi.year2022.ant;

import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/9/22 10:11
 * @description: Main2
 *
 * 给定一个由1-9组成的数字串，和一个整数k。
 * 截取子串，要求子串表示的数字小于k，
 * 问：有多少种截取方案？
 */
public class Sep22_Main2 {

    public static class Main {
        public static void main(String[] args) {
            Scanner in = new Scanner(System.in);
            String line = in.nextLine();
            int k = in.nextInt();
            int temp = k;
            int len = 0;
            int max = 0;
            while (temp > 0) {
                len++;
                if (temp / 10 == 0) {
                    max = temp;
                }
                temp /= 10;
            }
            int res = 0;
            int n = line.length();
            for (int i = 0; i < n; ++i) {
                if ((line.charAt(i) - '0') < max) {
                    res += n - i;
                    continue;
                }
                if ((line.charAt(i) - '0') == max) {
                    for (int j = i + len - 1; j >= i ; --j) {
                        if (Integer.parseInt(line.substring(i, j + 1)) < k) {
                            res += j - i + 1;
                            break;
                        }
                    }
                }
            }
            System.out.println(res);
        }


    }
}
