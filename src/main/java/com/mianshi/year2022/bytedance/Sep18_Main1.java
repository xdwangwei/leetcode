package com.mianshi.year2022.bytedance;

import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/9/18 18:03
 * @description: Sep18_Main1
 *
 * 给定一个字符0和1组成的串，输出最长奇迹串，奇迹串定义为相邻两个字符不同，并且长度至少为3
 */
public class Sep18_Main1 {

    static class Main {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            int n = line.length();
            int l = 0, r = 0, res = 0;
            while (r < n) {
                if (r == 0 || line.charAt(r) != line.charAt(r - 1)) {
                    if (r - l + 1 >= 3) {
                        res = Math.max(res, r - l + 1);
                    }
                } else {
                   l = r;
                }
                r++;
            }
            System.out.println(res);
        }
    }
}
