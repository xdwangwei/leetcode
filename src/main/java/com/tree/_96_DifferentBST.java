package com.tree;

/**
 * @author wangwei
 * 2021/11/13 15:23
 *
 * 给你一个整数 n ，求恰由 n 个节点组成且节点值从 1 到 n 互不相同的 二叉搜索树 有多少种？返回满足题意的二叉搜索树的种数。
 *
 *  
 *
 * 示例 1：
 *
 *
 * 输入：n = 3
 * 输出：5
 * 示例 2：
 *
 * 输入：n = 1
 * 输出：1
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/unique-binary-search-trees
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _96_DifferentBST {

    /**
     * 首先,从 1 到 n 每个数字 都可以作为根,
     * 加入 3 作为 根, 那么 3 左边部分(1,2) 构成左子树, 3 右边部分 (4,5) 构成左子树,
     * 那么 以 3 为根的BST有几种,就是 左子树有几种 * 右子树右子树
     *
     * 为了方便期间.构造辅助函数时选择两个参数, 起始和种终止.
     *
     * 当n比较大时,可能出现重复计算子树情况.所以加入备忘录
     * @param n
     * @return
     */
    public int numTrees(int n) {
        // 从1到n,所以数组规模时n+1
        // memo[x][y]表示 [x, y] 之间的正整数能够组成的BST的种类数
        int[][] memo = new int[n + 1][n + 1];
        return helper(1, n, memo);
    }

    /**
     * 返回从lo到high之间的正整数能够组成的BST的个数
     * @param lo
     * @param high
     * @return
     */
    private int helper(int lo, int high, int[][] memo) {
        // 因为返回树的个数,所以这里应该返回1,代表组成1种空树
        if (lo > high) {
            return 1;
        }
        // 已经计算过
        if (memo[lo][high] != 0) {
            return memo[lo][high];
        }
        int res = 0;
        // 都可以作为根节点
        for (int i = lo; i <= high; i++) {
            // 左边部分能够组成的左子树的种类数目
            int left = helper(lo, i - 1, memo);
            // 右边部分能够组成的右子树的种类数目
            int right = helper(i + 1, high, memo);
            // 左右子树组合数目
            res += left * right;
        }
        // 记录备忘录
        memo[lo][high] = res;
        // 返回所有可能性
        return res;
    }
}
