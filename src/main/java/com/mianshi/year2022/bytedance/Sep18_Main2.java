package com.mianshi.year2022.bytedance;

import java.util.*;

/**
 * 金字塔-石头下落
 * 每个石头认为是 1m * 1m
 * 输入：第一行 一个数字 n，代表有 n 层石头
 *      接下来 n 行，每一行，第一个数字 m ，代表 这一层有 m 个石头，接下来 m 个数字代表这些石头左边界的位置（cm）
 * 若石头下落后无法支撑，认为破碎
 * 输出，n 层石头全部下落完后剩下的石头个数
 *
 * 边界相邻认为不可靠，考虑重心位置，即至少一半宽度需要有支撑
 * 或者 下方正好有两块石头支撑左右边界
 */
public class Sep18_Main2 {
    public static class Main {

        static class Stone {
            int left, right, flag;
            double mid;

            @Override
            public int hashCode() {
                return left + right + (int)(mid * 10);
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null || !(obj instanceof Stone)) {
                    return false;
                }
                Stone obj1 = (Stone) obj;
                return obj1.left == left && obj1.right == right && obj1.mid == mid;
            }
        }

        public static void main(String[] args) {
            Scanner in = new Scanner(System.in);
            // n 层
            int n = in.nextInt();
            List<List<Stone>> stones = new ArrayList<>(n);
            for (int i = 0; i < n; ++i) {
                // 每一层 m 个石头
                int m = in.nextInt();
                List<Stone> line = new ArrayList<>(m);
                // m个石头左边界，注意 单位是 cm，石头宽高1m
                for (int j = 0; j < m; ++j) {
                    Stone stone = new Stone();
                    int left = in.nextInt();
                    stone.left = left;
                    stone.right = left + 100;
                    stone.mid = left + 50.0;
                    stone.flag = -1;
                    line.add(stone);
                }
                stones.add(line);
            }
            // 如果只有一层
            if (stones.size() == 1) {
                System.out.println(stones.get(0).size());
                return;
            }
            // 第一层落地
            int res = stones.get(0).size();
            // 从第二层考虑
            for (int i = 1; i < stones.size(); ++i) {
                // 它低一层的所有石头
                List<Stone> last = stones.get(i - 1);
                if (last.size() == 0) {
                    break;
                }
                List<Stone> cur = stones.get(i);
                int lastIdx = -1;
                for (int j = 0; j < cur.size(); ++j) {
                    if (cur.get(j).right <= last.get(0).left || cur.get(j).left >= last.get(last.size() - 1).right) {
                        continue;
                    }
                    // 找到支撑它的那个石头
                    lastIdx = findIdx(cur.get(j), last, lastIdx);
                    if (cur.get(j).flag > 0) {
                        res++;
                    }
                }
            }
            System.out.println(res);
        }

        private static int findIdx(Stone cur, List<Stone> last, int lastIdx) {
            if (last.size() == 0) {
                return -1;
            }
            if (last.size() == 1) {
                if (cur.mid <= last.get(0).left || cur.mid >= last.get(0).right) {
                    return -1;
                }
                return 0;
            }
            for (int i = lastIdx + 1; i < last.size() - 1; ++i) {
                if (cur.mid > last.get(i).left) {
                    if (cur.mid < last.get(i).right) {
                        cur.flag = 1;
                        return i;
                    }
                }
                if (cur.left <= last.get(i).right && cur.right >= last.get(i + 1).left) {
                    cur.flag = 1;
                    return i;
                }
                if (cur.left >= last.get(i).mid || cur.right <= last.get(i).mid) {
                    cur.flag = -1;
                    cur.left = last.get(i).left;
                    cur.right = last.get(i).right;
                    cur.mid = last.get(i).mid;
                    return -1;
                }
            }
            return -1;
        }
    }
}
