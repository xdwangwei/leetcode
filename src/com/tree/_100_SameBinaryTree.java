package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2020/7/30 12:29
 *
 * 给定两个二叉树，编写一个函数来检验它们是否相同。
 *
 * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
 *
 * 示例 1:
 *
 * 输入:       1         1
 *           / \       / \
 *          2   3     2   3
 *
 *         [1,2,3],   [1,2,3]
 *
 * 输出: true
 * 示例 2:
 *
 * 输入:      1          1
 *           /           \
 *          2             2
 *
 *         [1,2],     [1,null,2]
 *
 * 输出: false
 * 示例 3:
 *
 * 输入:       1         1
 *           / \       / \
 *          2   1     1   2
 *
 *         [1,2,1],   [1,1,2]
 *
 * 输出: false
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/same-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _100_SameBinaryTree {

    public boolean isSameTree(TreeNode p, TreeNode q) {
        // 都为空的话，显然相同
        if (p == null && q == null) return true;
        // 一个为空，一个非空，显然不同
        if (p == null || q == null) return false;
        // 两个都非空，但 val 不一样也不行
        if (p.val != q.val) return false;
        // 左右子树也必须一样
        return isSameTree(p.left, q.left)
                && isSameTree(p.right, q.right);
    }

}
