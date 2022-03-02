package com.order;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * 2020/8/30 9:49
 *
 * 你的任务是计算a^b对1337 取模，a 是一个正整数，b 是一个非常大的正整数且会以数组形式给出。
 *
 * 示例 1:
 *
 * 输入: a = 2, b = [3]
 * 输出: 8
 * 示例 2:
 *
 * 输入: a = 2, b = [1,0]
 * 输出: 1024
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/super-pow
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _372_SuperPower {

    /**
     *      a^[1,5,6,4]
     *   =  a^4 * a^[1,5,6,0]
     *   =  a^4 * (a^[1,5,6])^10  递归
     *
     *   (a * b) % k = (a % k)(b % k) % k
     *   a = Ak+B, b = Ck+D, ab = ACk^2 + ADk+ BCk + BD
     *   所以 ab % k = BD % k,
     *   而 B = a % k, D = b % k
     *   所以 ab % k = (a % k)(b % k) % k
     *
     * @param a
     * @param b
     * @return
     */
    public int superPow(int a, int[] b) {
        // 将数组转为deque
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i : b) {
            deque.offer(i);
        }
        // a ^ b % k
        int k = 1337;
        return superPowAndMod(a, deque, k);
    }

    /**
     *
     * 计算 a^[a,a,a,,a] % k
     *
     *  a^[1,5,6,4]  =  a^4  * (a^[1,5,6])^10 = part1 * part2
     * @param a
     * @param deque
     * @return
     */
    private int superPowAndMod(int a, Deque<Integer> deque, int k) {
        // base case 递归出口，a^[] = 1
        if (deque.isEmpty()) return 1;
        // 取出最后一个数
        int last = deque.pollLast();
        // a^[1,5,6,4]  =  a^4  * (a^[1,5,6])^10  递归
        // 将原问题化简，缩小规模递归求解
        int part1 = powerAndMod(a, last, k);
        int part2 = powerAndMod(superPowAndMod(a, deque, k), 10, k);
        // 合并出结果
        return (part1 * part2) % k;
    }

    /**
     * 高效计算 a的n次方 % k
     * ab % k = (a % k)(b % k) % k
     * ==>  a^n % k = (a % k)^n % k
     * @param a
     * @param n
     * @return
     */
    private int powerAndMod(int a, int n, int k) {
        // base case, 1 % k = k
        if (n == 0) return 1;
        // a^n % k = (a % k)^n % k
        a = a % k;
        // n是偶数
        if (n % 2 == 0)
            return (powerAndMod(a * a, n / 2, k)) % k;
            // n是奇数
        else
            return (powerAndMod(a * a, n / 2, k) * a) % k;
    }



    /**
     *
     * 如果只计算 a^[a,a,a,,a]，为方便计算，将数组转为双端列表[]
     * @param a
     * @param deque
     * @return
     */
    private int superPow(int a, Deque<Integer> deque) {
        // base case 递归出口，a^[] = 1
        if (deque.isEmpty()) return 1;
        // 取出最后一个数
        int last = deque.pollLast();
        // a^[1,5,6,4]  =  a^4  * (a^[1,5,6])^10  递归
        // 将原问题化简，缩小规模递归求解
        int part1 = power(a, last);
        int part2 = power(superPow(a, deque), 10);
        // 合并出结果
        return part1 * part2;
    }


    /**
     * 高效计算a的n次方
     * @param a
     * @param n
     * @return
     */
    private int power(int a, int n) {
        // base case
        if (n == 0) return 1;
        // n是偶数
        if (n % 2 == 0)
            return power(a * a, n / 2);
        // n是奇数
        else
            return power(a * a, n / 2) * a;
    }

}
