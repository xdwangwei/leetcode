package com.mianshi.year2022.meituan;

import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/5/21 15:55
 * @description: May21_Main1
 *
 * 判断一个特别大的数字是否是99的倍数
 * 将这个数字两位一组组成二位数，累加所有二位数的都一个新的数字，
 * 如果这个新的数字 <= 99，则退出，否则 循环进行上一步
 * 如果退出时的数字正好是99，说明这个数字是99的倍数
 *
 *
 * 输入一个特别特别大的数，判断其是否是99的倍数，要求输出所有中间结果
 */
public class May21_Main1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String number = scanner.nextLine();
        int j, sum, low, high;
        // StringBuilder builder = new StringBuilder();
        while (true) {
            j = number.length() - 1;
            sum = 0;
            // List<Integer> list = new ArrayList<>();
            while (j >= 0) {
                low = number.charAt(j--) - '0';
                high = j >= 0 ? number.charAt(j--) - '0' : 0;
                // list.add(high * 10 + low);
                sum += high * 10 + low;
            }
            // for (int i = 0; i < list.size(); ++i) {
            //
            // }
            System.out.println(sum);
            if (sum <= 99) {
                break;
            }
            number = String.valueOf(sum);
        }

    }
}
