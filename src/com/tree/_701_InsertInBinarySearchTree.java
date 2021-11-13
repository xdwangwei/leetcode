package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2020/7/30 12:39
 *
 * 给定二叉搜索树（BST）的根节点和要插入树中的值，将值插入二叉搜索树。 返回插入后二叉搜索树的根节点。 保证原始二叉搜索树中不存在新值。
 *
 * 注意，可能存在多种有效的插入方式，只要树在插入后仍保持为二叉搜索树即可。 你可以返回任意有效的结果。
 *
 * 例如, 
 *
 * 给定二叉搜索树:
 *
 *         4
 *        / \
 *       2   7
 *      / \
 *     1   3
 *
 * 和 插入的值: 5
 * 你可以返回这个二叉搜索树:
 *
 *          4
 *        /   \
 *       2     7
 *      / \   /
 *     1   3 5
 * 或者这个树也是有效的:
 *
 *          5
 *        /   \
 *       2     7
 *      / \
 *     1   3
 *          \
 *           4
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/insert-into-a-binary-search-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _701_InsertInBinarySearchTree {

    /**
     * 本质上跟在二叉搜索树中寻找一个目标节点无多大差别
     * 仍然是借助二叉搜索树的特点 + 二分法'
     *
     * 注意涉及到[修改]树结构,那么操作完就要返回树根,同时在外面要[重新接收]这个根
     * @param root
     * @param val
     * @return
     */
    public TreeNode insertIntoBST(TreeNode root, int val) {
        // 节点为空，直接创建一个新的节点
        if (root == null) {
            return new TreeNode(val, null, null);
        }
        // 比根节点值小，需要去左子树中插入
        else if (val < root.val) {
            // 左子树变为插入新值后的子树, 注意要重新接收
            root.left = insertIntoBST(root.left, val);
        }
        // 比根节点值大，需要去右子树中插入
        else {
            // 右子树变为插入新值后的子树,注意重新接收
            root.right = insertIntoBST(root.right, val);
        }

        // 返回树根
        return root;
    }
}
