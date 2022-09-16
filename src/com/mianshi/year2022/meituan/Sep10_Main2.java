package com.mianshi.year2022.meituan;

import java.util.*;

/**
 * 题目描述：
 * 某天，小美在调试她的神经网络模型，这个模型共有n个参数，
 * 目前这些参数分别为a1、a2、…、an，为了让参数看起来更加随机而且增加训练效果，
 * 她需要调节参数使所有参数满足a1+a2+…+an≠0且a1*a2*…*an≠0，
 * 她每次可以选择一个参数ai并将其变为ai+1，
 * 小美请你帮她计算一下，最少需要几次操作才能使参数达到她的要求。
 *
 *
 *
 * 输入描述
 * 第一行一个正整数n，表示参数个数。
 *
 * 第二行为n个正整数a1, a2,...... an，其中ai表示第i个参数初始值。
 *
 * 1 ≤ n ≤ 30000，-1000 ≤ ai ≤ 1000。
 *
 * 输出描述
 * 输出一个正整数，表示最少需要的操作次数。
 *
 *
 * 样例输入
 * 3
 * 2 -1 -1
 * 样例输出
 * 1
 *
 * 提示
 * 初始时a1+a2+a3=0不符合要求，一次操作将2变成3，a1+a2+a3=1，a1*a2*a3=3满足要求。
 */
public class Sep10_Main2 {

    static class Main {

        static class Node {
            String str;
            int sum;
            int mult;
            public Node (String str, int sum, int mult) {
                this.sum = sum;
                this.mult = mult;
                this.str = str;
            }
        }

        public static String toString(List<Integer> list) {
            StringBuilder builder = new StringBuilder();
            for (Integer num : list) {
                builder.append(num).append(",");
            }
            builder.setLength(builder.length() - 1);
            return builder.toString();
        }

        public static List<Integer> fromString(String[] numStrs) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < numStrs.length; ++i) {
                list.add(Integer.parseInt(numStrs[i]));
            }
            return list;
        }
        
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt();
            int sum = 0, mult = 1;
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < n; ++i) {
                int item = sc.nextInt();
                list.add(item);
                sum += item;
                mult *= item;
            }
            if (sum != 0 && mult != 0) {
                System.out.println(0);
                return;
            }
            int step = 1;
            Queue<Node> queue = new ArrayDeque<>();
            Set<Node> visited = new HashSet<>();
            Node root = new Node(toString(list), sum, mult);
            queue.offer(root);
            visited.add(root);
            while (!queue.isEmpty()) {
                int sz = queue.size();
                for (int j = 0; j < sz; ++j) {
                    Node cur = queue.poll();
                    list = fromString(cur.str.split(","));
                    sum = cur.sum;
                    mult = cur.mult;
                    int tempSum = sum, tempMult = mult;
                    for (int i = 0; i < n; ++i) {
                        int old = list.get(i);
                        list.set(i, old + 1);
                        sum += 1;
                        if (old != 0) {
                            mult = mult / old * (old + 1);
                        } else {
                            mult = 1;
                            for (Integer num : list) {
                                mult *= num;
                            }
                        }
                        if (sum != 0 && mult != 0) {
                            System.out.println(step);
                            return;
                        }
                        Node newNode = new Node(toString(list), sum, mult);
                        if (!visited.contains(newNode) && sum != 0) {
                            queue.offer(newNode);
                            visited.add(newNode);
                        }
                        list.set(i, old);
                        sum = tempSum;
                        mult = tempMult;
                    }
                }
                step++;
            }
        }
    }
}
