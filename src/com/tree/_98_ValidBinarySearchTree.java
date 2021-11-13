package com.tree;

import com.common.TreeNode;

import java.util.Stack;

/**
 * @author wangwei
 * 2020/7/30 12:02
 *
 * 给定一个二叉树，判断其是否是一个有效的二叉搜索树。
 *
 * 假设一个二叉搜索树具有如下特征：
 *
 * 节点的左子树只包含小于当前节点的数。
 * 节点的右子树只包含大于当前节点的数。
 * 所有左子树和右子树自身必须也是二叉搜索树。
 * 示例 1:
 *
 * 输入:
 *     2
 *    / \
 *   1   3
 * 输出: true
 * 示例 2:
 *
 * 输入:
 *     5
 *    / \
 *   1   4
 *      / \
 *     3   6
 * 输出: false
 * 解释: 输入为: [5,1,4,null,null,3,6]。
 *      根节点的值为 5 ，但是其右子节点值为 4 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/validate-binary-search-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _98_ValidBinarySearchTree {

    public boolean isValidBST(TreeNode root) {
        return isValidBST(root, null, null);
    }

    /**
     * 递归
     * 每个节点都有上界和下界，root的上下界为null
     * 当前节点作为左子树的上界，作为右子树的下界
     * @param node
     * @param lower
     * @param upper
     * @return
     */
    private boolean isValidBST(TreeNode node, TreeNode lower, TreeNode upper) {
        if (node == null) return true;
        int val = node.val;
        if (lower != null && val <= lower.val) return false;
        if (upper != null && val >= upper.val) return false;
        // 当前节点作为左子树的上界，作为右子树的下界
        return isValidBST(node.left, lower, node)
               && isValidBST(node.right, node, upper);
    }

    /**
     * 按照中序遍历顺序，每一个节点都应该比上一个节点大
     * 在中序遍历的时候实时检查当前节点的值是否大于前一个中序遍历到的节点的值即可
     * 递归写法
     */
    // 测试用例比Integer.MIN_VALUE还小
    private double inorder = -Double.MAX_VALUE;
    public boolean isValidBST2(TreeNode root) {
        if (root == null) return true;

        // 按照中序遍历顺序，先判断左子树
        if (!isValidBST2(root.left)) return false;

        // 访问当前节点：如果当前节点小于等于中序遍历的前一个节点，说明不满足BST，返回 false；否则继续遍历。
        if (root.val <= inorder) return false;

        // 更新inorder
        inorder = root.val;

        // 判断右子树
        return isValidBST2(root.right);
    }

    /**
     * 中序遍历判断的另一种写法
     * 迭代写法
     * @param
     */
    public boolean isValidBST3(TreeNode root) {
        if (root == null) return true;
        Stack<TreeNode> stack = new Stack<>();
        while (!stack.isEmpty() || root != null) {
            // 中序遍历是左子树在前。栈的顺序相反。所以先把自己入栈。再处理左孩子
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            // 访问当前节点：如果当前节点小于等于中序遍历的前一个节点，说明不满足BST，返回 false；否则继续遍历。
            if (root.val <= inorder) return false;

            inorder = root.val;

            // 访问当前节点右子树
            root = root.right;
        }

        return true;
    }

    public static void main(String[] args) {
        // -2147483648
        System.out.println(Integer.MIN_VALUE);

        // Double.MIN_VALUE 是一个很小的正数 4.9E-324
        System.out.println(Double.MIN_VALUE);
    }
}

