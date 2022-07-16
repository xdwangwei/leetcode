package com.mianshi.year2022.meituan;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/5/21 15:55
 * @description: May21_Main1
 *
 * 给定节点和边
 * 第一行输入节点个数n，节点编号从1开始
 * 第二行n-1个数字，第i个数字x代表 节点 i+1 和 x 之间有条边
 *
 * 可以随机选择两个节点，断掉其周围所有边，求出剩下的全部联通分量的节点个数中的最大值
 * 要求，所求的那个值尽可能的小
 */
public class May21_Main5 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        boolean[][] graph = new boolean[n + 1][n + 1];
        int[] degree = new int[n + 1];
        int idx = 1;
        for (int i = 1; i < n; ++i) {
            int to = scanner.nextInt();
            graph[to][idx + 1] = graph[idx + 1][to] = true;
            degree[to]++;
            degree[idx + 1]++;
            idx++;
        }
        // 错误想法：移除度最大的两个节点
        // 枚举所有的两个节点组合
        int ans = 0;
        for (int i = 1; i <= n; ++i) {
            for (int j = i + 1; j <= n; ++j) {
                int idx1 = i, idx2 = j;
                // TODO 拷贝一份新的图
                List<Integer> list1 = new ArrayList<>();
                List<Integer> list2 = new ArrayList<>();
                for (int k = 1; k <= n; ++k) {
                    if (graph[idx1][k] || graph[k][idx1]) {
                        graph[idx1][k] = graph[k][idx1] = false;
                        list1.add(k);
                    }
                    if (graph[idx2][k] || graph[k][idx2]) {
                        graph[idx2][k] = graph[k][idx2] = false;
                        list2.add(k);
                    }
                }
                int[] count = new int[1];
                Set<Integer> visited = new HashSet<>();
                for (int node = 1; node <= n; ++node) {
                    count[0] = 0;
                    visited.clear();
                    dfs(graph, node, count, visited);
                    ans = Math.max(ans, count[0]);
                }
                for (Integer k : list1) {
                    graph[idx1][k] = graph[k][idx1] = true;
                }
                for (Integer k : list2) {
                    graph[idx2][k] = graph[k][idx2] = true;
                }
            }
        }
        System.out.println(ans);
    }

    private static void dfs(boolean[][] graph, int node, int[] count, Set<Integer> visited) {
        if (visited.contains(node)) {
            return;
        }
        visited.add(node);
        for (int j = 1; j < graph.length; ++j) {
            if (graph[node][j] || graph[j][node]) {
                count[0]++;
                graph[node][j] = graph[j][node] = false;
                dfs(graph, j, count, visited);
            }
        }
    }
}
