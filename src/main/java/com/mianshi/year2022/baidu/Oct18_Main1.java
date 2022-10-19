package com.mianshi.year2022.baidu;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/10/18 18:24
 * @description: Oct18_Main1
 *
 * 输入：
 * n
 * n个数字
 * 长度为n的字符串，由R和B组成，R代表这个位置数字是红色，B代表蓝色
 * 从红色和蓝色中各取出一个数，计算乘积，记为一个方案
 * 返回所有方案的和
 * 因为结果比较大，最后结果对 10^9+7 取模
 *
 * 超时了，30%
 *
 */
public class Oct18_Main1 {

    static class Node {
        long x, y;

        Node(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return (x == node.x && y == node.y) || (x == node.y && y == node.x);
        }

        @Override
        public int hashCode() {
            return (int) Math.abs(x - y);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] nums = new int[n];
        for(int i = 0; i < n; i++) {
            nums[i] = scanner.nextInt();
        }
        scanner.nextLine();
        String line = scanner.nextLine();
        // Set<Integer> red = new HashSet<>();
        // Set<Integer> blue = new HashSet<>();
        List<Integer> red = new ArrayList<>();
        List<Integer> blue = new ArrayList<>();
        for(int i = 0;  i < n ; i++) {
            if (line.charAt(i) == 'R') {
                red.add(nums[i]);
            } else {
                blue.add(nums[i]);
            }
        }
        long mod = (long) (1e9 + 7);
        long res = 0;
        Map<Node, Long> map = new HashMap<>();
        Collections.sort(red);
        long temp = 0;
        for (int i = 0; i < red.size(); ) {
            while (i > 0 && red.get(i) == red.get(i - 1)) {
                res += temp;
                i++;
            }
            temp = 0;
            for (Integer b : blue) {
                long r = red.get(i);
                Node node = new Node(r, b);
                long t = 0;
                if (!map.containsKey(node)) {
                    t = (((r % mod) * (b % mod)) % mod);
                    map.put(node, t);
                } else {
                    t = map.get(node);
                }
                temp += t;
            }
            res += temp;
            i++;
        }
        System.out.println(res);
    }
}
