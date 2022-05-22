package com.mianshi.year2022.jd;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * @author wangwei
 * @date 2022/5/14 19:22
 * @description: Main
 * 时间限制： 3000MS
 * 内存限制： 589824KB
 * 题目描述：
 *        拓扑排序 (toposort) 一般而言是用于有向无环图上的求得拓扑序的算法。而对于无根树来说，我们也可以应用拓扑排序，只是需要稍加修改。具体算法流程如下：
 *
 *        1. 首先将所有叶子按照标号从小到大加入栈中。
 *
 *        2. 取出栈顶元素，输出，将其从原树中删除，再判断之前与其相连的点是否变为了叶子。如果已变为叶子，将其加入栈中。
 *
 *        3. 如果此时栈为空，退出；否则，回到第二步。
 *
 *        这样就可以得到一棵无根树的“拓扑序”。
 *
 *        现在小程得到了一棵无根树，他想知道将上述算法应用起来，得到的拓扑序是什么？
 *
 *
 *
 * 输入描述
 * 第一行一个正整数 n，表示这棵树的点数；
 *
 * 接下来一行 n − 1 个正整数 f2, f3, ..., fn，表示树上 i 与 fi 之间有一条边。
 *
 * 2 ≤ n ≤ 105, 1 ≤ fi < i。
 *
 * 输出描述
 * 输出一行 n 个数，表示最后得到的拓扑序。末尾不要输出空格。
 *
 *
 * 样例输入
 * 6
 * 1 2 1 3 3
 * 样例输出
 * 6 5 3 2 1 4
 */
public class May14_Main2 {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        boolean[][] graph = new boolean[n + 1][n + 1];
        int[] degree = new int[n + 1];
        int from = 2;
        for (int i = 0; i < n - 1; ++i) {
            int to = scanner.nextInt();
            graph[from][to] = graph[to][from] = true;
            degree[from]++;
            degree[to]++;
            from++;
        }

        System.out.println("hello word");

        Deque<Integer> stack = new ArrayDeque<>();
 //         *        1. 首先将所有叶子按照标号从小到大加入栈中。
 // *
 // *        2. 取出栈顶元素，输出，将其从原树中删除，再判断之前与其相连的点是否变为了叶子。如果已变为叶子，将其加入栈中。
 // *
 // *        3. 如果此时栈为空，退出；否则，回到第二步。
        for (int i = 1; i <= n; ++i) {
            if (degree[i] == 1) {
                stack.push(i);
            }
        }
        while (!stack.isEmpty()) {
            Integer pop = stack.pop();
            if (stack.isEmpty()) {
                System.out.print(pop);
            } else {
                System.out.print(pop + " ");
            }
            for (int i = 1; i <= n; ++i) {
                if (graph[pop][i] || graph[i][pop]) {
                    graph[pop][i] = graph[i][pop] = false;
                    degree[i]--;
                    if (degree[i] == 1) {
                        stack.push(i);
                    }
                }
            }
        }
    }
}
