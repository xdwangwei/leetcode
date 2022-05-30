package com.daily;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2022/5/30 12:31
 * @description: _1022_SumOfRootToLeafBinaryNumbers
 *
 * 1022. 从根到叶的二进制数之和
 * 给出一棵二叉树，其上每个结点的值都是 0 或 1 。每一条从根到叶的路径都代表一个从最高有效位开始的二进制数。
 *
 * 例如，如果路径为 0 -> 1 -> 1 -> 0 -> 1，那么它表示二进制数 01101，也就是 13 。
 * 对树上的每一片叶子，我们都要找出从根到该叶子的路径所表示的数字。
 *
 * 返回这些数字之和。题目数据保证答案是一个 32 位 整数。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：root = [1,0,1,0,1,0,1]
 * 输出：22
 * 解释：(100) + (101) + (110) + (111) = 4 + 5 + 6 + 7 = 22
 * 示例 2：
 *
 * 输入：root = [0]
 * 输出：0
 *
 *
 * 提示：
 *
 * 树中的节点数在 [1, 1000] 范围内
 * Node.val 仅为 0 或 1
 */
public class _1022_SumOfRootToLeafBinaryNumbers {


    /**
     * 容易想到「递归」进行求解，在 DFS 过程中记录下当前的值为多少，注意到节点值为0或1，因此通过位运算计算结果
     * 假设遍历到当前节点 x 前，记录的值为 cur，那么根据题意，我们需要先将 cur 进行整体左移（腾出最后一位），
     * 然后将节点 x 的值放置最低位来得到新的值，如果当前节点是叶子节点，则累加当前路径和，并继续进行递归。
     *
     * 递归使用「全局变量」实现方式。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/sum-of-root-to-leaf-binary-numbers/solution/by-ac_oier-1905/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    // 最终结果
    private int res;

    public int sumRootToLeaf(TreeNode root) {
        helper(root, 0);
        return res;
    }


    /**
     * 递归
     * @param root   每个节点值都是0或1，通过 位运算 |运算，很快计算出二进制结果
     * @param val   当前路径上的二进制序列对应的十进制值
     */
    private void helper(TreeNode root, int val) {
        if (root == null) {
            return;
        }
        // 通过|运算，得到树根到当前节点的路径对应的二进制序列对应的十进制值
        val = (val << 1) | root.val;
        // 到叶子节点
        if (root.left == null && root.right == null) {
            // 累加当前路径和
            res += val;
            return;
        }
        // 递归左右子树，我们是传值递归，所以不涉及回溯
        helper(root.left, val);
        helper(root.right, val);
    }
}
