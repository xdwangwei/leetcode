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
        System.out.println(1 % 0);
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
        return calc(1 + diff) ^ 1;
    }

    /**
     * 正向找规律 + 递归
     * 思路与算法
     *
     * 尝试写表中的前几行：
     *
     * 00
     * 0101
     * 01100110
     * 0110100101101001
     * ....
     * 我们可以注意到规律：每一行的后半部分正好为前半部分的“翻转”
     *      前半部分是 0 后半部分变为 1，前半部分是 1，后半部分变为 0。
     *      且每一行的前半部分和上一行相同。
     *      我们可以通过「数学归纳法」来进行证明。
     *
     * n 和 k 都是从1开始，第 n 行的字符个数 = 2 ^ n-1, 一半字符数是 2 ^ n-2
     *
     *
     * 有了这个性质，那么我们再次思考原问题：对于查询某一个行第 k 个数字，如果 k 在后半部分，那么原问题就可以转化为求解该行前半部分的对应位置的“翻转”数字，又因为该行前半部分与上一行相同，所以又转化为上一行对应对应的“翻转”数字。那么按照这样一直递归下去，并在第一行时返回数字 0 即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/k-th-symbol-in-grammar/solution/di-kge-yu-fa-fu-hao-by-leetcode-solution-zgwd/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param k
     * @return
     */
    public int kthGrammar(int n, int k) {
        // 第一行，或者每一行的第1个字符都是0
        if (k == 1) {
            return 0;
        }
        // 当前行字符个数  = 2 ^ n-1, 一半字符数是 2 ^ n-2
        // k <= 一半，前半部分和上一行一样
        if (k <= (1 << n - 2)) {
            return kthGrammar(n - 1, k);
        }
        // 后半部分，来自于上一行前半部分的反转，所以要异或1
        return kthGrammar(n - 1, k - (1 << n - 2)) ^ 1;
    }

    /**
     * 逆向倒推+递归
     * 思路与算法
     *
     * 比如当 n=3 时，第 1 行是 0，第 2行是 01，第 3 行是 0110。
     *
     * 现在要求表第 n 行中第 k 个数字，1≤k≤2n
     *
     * 首先我们可以看到第 i 行中会有 2^{i-1}数字，1 ≤ i ≤ n，且其中第 j 个数字按照构造规则会生第 i+1 行中的第 2∗j−1 和 2∗j 个数字，1 ≤ j ≤ 2^{i-1}
     * 即对于第 i+1 行中的第 k 个数字 num1，1 ≤ x ≤ 2^i，会被第 i 行中第 (k+1)/2 个数字 nums2 生成。且满足规则：
     *
     * 当 nums2 = 0 时，会生成 01:
     *      如果num1在奇数位置，也就是nums2生成的第一个位置（k & 1 == 1），那么 nums1=0
     *      如果num1在偶数位置，也就是nums2生成的第二个位置（k & 1 == 0），那么 nums1=1
     * 当 nums2 = 1 时，会生成 10:
     *      如果num1在奇数位置，也就是nums2生成的第一个位置（k & 1 == 1），那么 nums1=1
     *      如果num1在偶数位置，也就是nums2生成的第二个位置（k & 1 == 0），那么 nums1=0
     *
     * 真值表
     *
     *      num2     k&1      -->       nums1
     *      0         1                   0
     *      0         0                   1
     *      1         1                   1
     *      1         0                   0
     *
     * 结我们可以得到：nums1 = nums2 ⊕ (k&1) ⊕ 1 ，其中 & 为「与」运算符， ⊕ 为「异或」运算符。
     * 那么我们从第 n 不断往上递归求解，并且当在第一行时只有一个数字，直接返回 0 即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/k-th-symbol-in-grammar/solution/di-kge-yu-fa-fu-hao-by-leetcode-solution-zgwd/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param k
     * @return
     */
    public int kthGrammar2(int n, int k) {
        // 第一行，或者每一行的第1个字符都是0
        if (k == 1) {
            return 0;
        }
        // 第 i+1 行中的第 k 个数字 num1，，会被第 i 行中第 (k+1)/2 个数字 nums2 生成。
        // nums1 = (k&1) ⊕ nums2 ⊕ 1
        return 1 ^ (k & 1) ^ kthGrammar(n - 1, (k + 1) / 2);
    }
}
