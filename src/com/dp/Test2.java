package com.dp;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author wangwei
 * 2020/9/4 9:33
 */
public class Test2 {

    private static ArrayList<Integer>[] children;
    private static int[] parent;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n, u, v, res = 0;
        n = sc.nextInt();
        parent = new int[n + 1];
        children = new ArrayList[n + 1];
        for (int i = 1; i <= n - 1; i++) {
            u = sc.nextInt();
            v = sc.nextInt();
            parent[v] = u;
            if (children[u] == null) children[u] = new ArrayList<>();
            children[u].add(v);
        }
        for (int i = 1; i <= n; i++) {
            if (children[i] == null) {
                int temp = helper(i);
                res = Math.max(res, temp);
            }
        }
        System.out.println(res);
    }

    private static int helper(int i) {
        Stack<Integer> stack = new Stack<>();
        while (parent[i] != 0) {
            // 清空子集
            // i = parent[i];
            // 清空节点
        }
        // 统计树个数
        return 0;
    }
}
