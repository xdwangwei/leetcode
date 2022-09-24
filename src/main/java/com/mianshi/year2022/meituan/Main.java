package com.mianshi.year2022.meituan;

import java.util.*;

public class Main {

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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int sum = 0, mult = 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            int item = sc.nextInt();
            builder.append(item + ",");
            sum += item;
            mult *= item;
        }
        if (sum != 0 && mult != 0) {
            System.out.println(0);
            return;
        }
        builder.setLength(builder.length() - 1);
        int step = 1;
        Queue<Node> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Node root = new Node(builder.toString(), sum, mult);
        queue.offer(root);
        visited.add(root.str);
        while (!queue.isEmpty()) {
            int sz = queue.size();
            for (int j = 0; j < sz; ++j) {
                Node cur = queue.poll();
                builder = new StringBuilder(cur.str);
                sum = cur.sum;
                mult = cur.mult;
                int tempSum = sum, tempMult = mult;
                for (int i = 0; i < n; ++i) {
                    int old = builder.charAt(i << 1) - '0';
                    builder.setCharAt(i << 1, (char) (old + 1 + '0'));
                    sum += 1;
                    if (old != 0) {
                        mult = mult / old * (old + 1);
                    } else {
                        mult = 1;
                        for (int k = 0; k < n; ++k) {
                            mult *= (builder.charAt(k << 1) - '0');
                        }
                    }
                    if (sum != 0 && mult != 0) {
                        System.out.println(step);
                        return;
                    }
                    Node newNode = new Node(builder.toString(), sum, mult);
                    if (!visited.contains(newNode.str)) {
                        queue.offer(newNode);
                        visited.add(newNode.str);
                    }
                    builder.setCharAt(i << 1, (char) (old + '0'));
                    sum = tempSum;
                    mult = tempMult;
                }
            }
            step++;
        }
    }
}