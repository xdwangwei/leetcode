package com.offerassult;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2022/12/9 16:25
 * @description: _053_FindSuccessorInBinarySearchTree
 *
 * 剑指 Offer II 053. 二叉搜索树中的中序后继
 * 给定一棵二叉搜索树和其中的一个节点 p ，找到该节点在树中的中序后继。如果节点没有中序后继，请返回 null 。
 *
 * 节点 p 的后继是值比 p.val 大的节点中键值最小的节点，即按中序遍历的顺序节点 p 的下一个节点。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：root = [2,1,3], p = 1
 * 输出：2
 * 解释：这里 1 的中序后继是 2。请注意 p 和返回值都应是 TreeNode 类型。
 * 示例 2：
 *
 *
 *
 * 输入：root = [5,3,6,2,4,null,null,1], p = 6
 * 输出：null
 * 解释：因为给出的节点没有中序后继，所以答案就返回 null 了。
 *
 *
 * 提示：
 *
 * 树中节点的数目在范围 [1, 104] 内。
 * -105 <= Node.val <= 105
 * 树中各节点的值均保证唯一。
 *
 *
 * 注意：本题与主站 285 题相同： https://leetcode-cn.com/problems/inorder-successor-in-bst/
 */
public class _053_FindSuccessorInBinarySearchTree {

    /**
     * 方法一：中序遍历递归
     * 用pre记录中序遍历中访问的上一个节点，
     * 当遍历到某个节点cur时，如果 pre 恰好与 p 一致，那么 cur就是p的后继节点
     */
    TreeNode pre = new TreeNode(-1);
    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        // 结束
        if (root == null) {
            return null;
        }
        // 中序遍历左子树
        TreeNode res = inorderSuccessor(root.left, p);
        // 左子树中不存在p的后继
        if (res != null) {
            return res;
        }

        // 访问当前节点
        // 如果pre恰好为p，那么 当前节点为 p 的后继
        if (pre == p) {
            return root;
        }
        // 更新pre
        pre = root;

        // 中序访问右子树
        return inorderSuccessor(root.right, p);
    }


    /**
     * 方法二：递归
     * 思路与方法一类似。只是结合二叉搜索树的特性，避免不必要的递归
     * 比如 当 root.val < p.val 时，p 的后继肯定不可能在 root.left 中，不必对左子树进行递归
     * 具体地
     *      递归root
     *      若 root 为null，返回 null
     *      若 root.val > p.val，
     *          p 的后继可能在 root 的左子树中，如果不在，那么 root 为 p 的后继
     *          否则，p的后继在root的右子树中，如果不在，那就是 null
     *
     * @param root
     * @param p
     * @return
     */
    public TreeNode inorderSuccessor2(TreeNode root, TreeNode p) {
        // 若 root 为null，返回 null
        if (root == null) {
            return null;
        }
        // 若 root.val > p.val，
        if (root.val > p.val) {
            // p 的后继可能在 root 的左子树中，如果不在，那么 root 为 p 的后继
            TreeNode successor = inorderSuccessor2(root.left, p);
            return successor != null ? successor : root;
        }
        // 否则，p的后继在root的右子树中，如果不在，那就是 null
        return inorderSuccessor2(root.right, p);
    }
}
