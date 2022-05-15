package com.mianshi.year2020.tecent.April;

import java.util.Scanner;

/**
 * @author wangwei
 * 2020/4/26 21:28
 *
 * 假设有一个深度无限制的完全二叉树，序号从 1开始
 * 有 n 次输入，每一行是 x y，x是这个节点的序号，y是指定的层
 * 这个元素x，在y层是否存在一个祖先，如果存在，输出那个祖先的序号，否则，输出 -1
 */
public class Main5 {

    // 根据序号，求出它在哪一层
    // logid 向下取整 + 1
    private static int getLevelById(int id){
        return (int) (Math.floor(Math.log10(id) / Math.log10(2)) + 1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int parentLevel = scanner.nextInt();
            int temp = x / 2;
            // 逐个判断它所有祖先的深度
            while (temp >= 1){
                int level = getLevelById(temp);
                // 是否有一个祖先所在的层，就是给定的那一层
                if (level == parentLevel){
                    // 如果有就输出这个祖先的序号
                    System.out.println(temp);
                    break;
                }
                temp = temp/2;
            }
            // 在指定的那一层，不存在祖先
            if (temp < 1)
                System.out.println(-1);
        }
    }
}
// 4
// 10 1
// 10 2
// 10 3
// 10 4