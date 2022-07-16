package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2020/7/30 21:15
 *
 * 给定一个二叉搜索树的根节点 root 和一个值 key，删除二叉搜索树中的 key 对应的节点，并保证二叉搜索树的性质不变。返回二叉搜索树（有可能被更新）的根节点的引用。
 *
 * 一般来说，删除节点可分为两个步骤：
 *
 * 首先找到需要删除的节点；
 * 如果找到了，删除它。
 * 说明： 要求算法时间复杂度为 O(h)，h 为树的高度。
 *
 * 示例:
 *
 * root = [5,3,6,2,4,null,7]
 * key = 3
 *
 *     5
 *    / \
 *   3   6
 *  / \   \
 * 2   4   7
 *
 * 给定需要删除的节点值是 3，所以我们首先找到 3 这个节点，然后删除它。
 *
 * 一个正确的答案是 [5,4,6,2,null,null,7], 如下图所示。
 *
 *     5
 *    / \
 *   4   6
 *  /     \
 * 2       7
 *
 * 另一个正确答案是 [5,2,6,null,4,null,7]。
 *
 *     5
 *    / \
 *   2   6
 *    \   \
 *     4   7
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/delete-node-in-a-bst
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _450_DeleteInBinarySearchTree {

    /**
     * 本质上和在二叉搜索树中寻找目标节点或者插入节点区别不大
     * 仍然是借助二叉搜索树的特性和二分思想
     *
     * 删除分为三种情况
     *      左右孩子都为空，返回null
     *      一个孩子为空，返回另一个孩子
     *      俩孩子都不为空，为了保证结构特性，必须找合适节点代替他
     *          这个节点可以是左子树的最右孩子（左子树中最大的节点），也可以是右子树的最左节点（右子树的最小节点）
     * @param root
     * @param key
     * @return
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;
        // 去左子树找目标值并删除
        if (key < root.val)
            root.left = deleteNode(root.left, key);
        // 去右子树中找目标值并删除
        else if (key > root.val)
            root.right = deleteNode(root.right, key);
        // 当前节点就是目标节点
        else {
            // 1.如果左右孩子都空，直接删除当前节点，返回null
            // if (root.left == null && root.right == null)
            //     return null;
            // 2.如果是左孩子非空或右孩子非空
            /** 这样写把12正确处理了 **/
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            // 如果左右都非空，找到右子树中最小的那个节点
            TreeNode minRight = getMinRight(root);
            // 替换当前节点
            root.val = minRight.val;
            // 然后从右子树从删除最小的节点
            root.right = deleteNode(root.right, minRight.val);
        }
        // 返回根节点
        return root;
    }

    /**
     * root为当前子树的根
     * 寻找当前子树的右子树中最小的那个节点
     * @param root
     * @return
     */
    private TreeNode getMinRight(TreeNode root) {
        root = root.right;
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }
}
