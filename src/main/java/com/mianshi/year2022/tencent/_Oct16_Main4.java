package com.mianshi.year2022.tencent;

import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/10/16 19:57
 * @description: _Oct16_Main1
 *
 * 按照以下规则构造序列：
 * 第一个字符是1
 * 第二个字符是0，此时序列是10
 * 第3-4个字符是 第1-2个字符反转得到，（1变为0，0变为1），此时序列是 1001
 * 第5-8个字符是 第1-4个字符反转得到，此时序列是  10010110
 * 以此类推
 * 给定l和r，问，第l个字符和第r个字符之间有多少个'1'????
 */
public class _Oct16_Main4 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int l = scanner.nextInt();
        int r = scanner.nextInt();
        int ans = 0;
        for (int i = l; i <= r; ++i) {
            ans += calc(i);
        }
        System.out.println(ans);
    }

    private static int calc(int i) {
        if (i <= 8) {
            return "10010110".charAt(i - 1) - '0';
        }
        // [j+1, 2j] --< [1, j]
        int j = Integer.highestOneBit(i - 1);
        int diff = i - (j + 1);
        return calc(1 + diff);
    }
}
