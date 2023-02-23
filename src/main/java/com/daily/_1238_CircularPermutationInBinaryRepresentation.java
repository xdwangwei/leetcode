package com.daily;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/2/23 11:43
 * @description: _1238_CircularPermutationInBinaryRepresentation
 *
 * 1238. 循环码排列
 * 给你两个整数 n 和 start。你的任务是返回任意 (0,1,2,,...,2^n-1) 的排列 p，并且满足：
 *
 * p[0] = start
 * p[i] 和 p[i+1] 的二进制表示形式只有一位不同
 * p[0] 和 p[2^n -1] 的二进制表示形式也只有一位不同
 *
 *
 * 示例 1：
 *
 * 输入：n = 2, start = 3
 * 输出：[3,2,0,1]
 * 解释：这个排列的二进制表示是 (11,10,00,01)
 *      所有的相邻元素都有一位是不同的，另一个有效的排列是 [3,1,0,2]
 * 示例 2：
 *
 * 输出：n = 3, start = 2
 * 输出：[2,6,7,5,4,0,1,3]
 * 解释：这个排列的二进制表示是 (010,110,111,101,100,000,001,011)
 *
 *
 * 提示：
 *
 * 1 <= n <= 16
 * 0 <= start < 2^n
 * 通过次数10,941提交次数14,400
 */
public class _1238_CircularPermutationInBinaryRepresentation {

    /**
     * 方法一：回溯 （数据规模不大）
     * List<Integer> path 记录已选择的数字， 初始时 path.add(start)
     * last = path.get(path.size()-1) 代表上一个选择的数字
     * Set<Integer> visited 记录用过的数字
     * 做选择：枚举与last只有一个二进制位不同的所有数字 for(int i=0;i<n;++i) num = last ^ (1<<i)； // 撤销选择
     * 回溯出口：当 path.size() == 2^n 时，得到一个排列，判断 f1=path[0] 和 f2=path[2^n-1] 是否满足只有一个二进制位置不同
     *          即 x = f1 ^ f2; (x & (x-1)) == 0
     *          若满足，返回此时的path
     *
     * @param n
     * @param start
     * @return
     */
    public List<Integer> circularPermutation(int n, int start) {
        List<Integer> path = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        // 初始点，指定p[0]
        path.add(start);
        visited.add(start);
        // 回溯，此题目一定有解
        backTrack(n, path, visited);
        // 返回path
        return path;
    }

    /**
     * 回溯
     * @param n
     * @param path
     * @param visited
     * @return
     */
    private boolean backTrack(int n, List<Integer> path, Set<Integer> visited) {
        // 得到一个排列
        if (path.size() == 1 << n) {
            // 判断 第一个数字 和 最后一个数字 是否满足 只有一个二进制位不同
            int f1 = path.get(0), f2 = path.get(path.size() - 1);
            int xor = f1 ^ f2;
            return (xor & xor - 1) == 0;
        }
        // 上一个选择的数字
        int last = path.get(path.size() - 1);
        // 枚举与 last 只有一个二进制不同的所有数字
        for (int i = 0; i < n; ++i) {
            int x = last ^ (1 << i);
            // 不能使用已经用过的数字
            if (!visited.contains(x)) {
                // 做选择
                path.add(x);
                visited.add(x);
                // 后续成功，提前返回
                if (backTrack(n,path, visited)) {
                    return true;
                }
                // 撤销选择
                path.remove(path.size() - 1);
                visited.remove(x);
            }
        }
        return false;
    }


    /**
     * 方法二：格雷码公式法
     *
     * _89_GrayCode 生成 n 位格雷码序列，数字 x 由原二进制编码得到的对应格雷码为 x ^ (x>>1)
     * 上述方法得到的格雷码序列是从0开始的，本题要求从 start 开始
     *
     * 我们可以按上述方法将 [0,..2^−1] 这些整数转换成对应的格雷码数组序列，
     * 然后找到 start 在格雷码数组中的位置，将格雷码数组从该位置开始截取，再将截取的部分拼接到格雷码数组的前面，就得到了题目要求的排列。
     *
     *          int[] g = new int[1 << n];
     *         int j = 0;
     *         for (int i = 0; i < 1 << n; ++i) {
     *             g[i] = i ^ (i >> 1);
     *             if (g[i] == start) {
     *                 j = i;
     *             }
     *         }
     *         List<Integer> ans = new ArrayList<>();
     *         for (int i = j; i < j + (1 << n); ++i) {
     *             ans.add(g[i % (1 << n)]);
     *         }
     *         return ans
     *
     * 改进
     *
     * 由于 gray(0)=0，那么 gray(0) ⊕ start = start，
     * 而 gray(i) 与 gray(i−1) 只有一个二进制位不同，所以 gray(i)⊕start 与 gray(i−1)⊕start 也只有一个二进制位不同。
     * 我们可以给上述方法得到的 格雷码序列每个元素 ⊕ start，即可得到首项为 start 的格雷码排列。
     *
     * 即 gray[0] = 0 ^ start；；；gray[i] = gray[i] ^ (gray[i - 1]) ^ start
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/circular-permutation-in-binary-representation/solution/python3javacgotypescript-yi-ti-shuang-ji-zhm7/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param start
     * @return
     */
    public List<Integer> circularPermutation2(int n, int start) {
        List<Integer> ans = new ArrayList<>();
        // gray[0] = 0 ^ start
        ans.add(start);
        for (int i = 1; i < 1 << n; ++i) {
            // gray[i] = gray[i] ^ (gray[i - 1]) ^ start
            ans.add(i ^ (i >> 1) ^ start);
        }
        return ans;
    }
}
