package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2021/11/13 14:41
 *
 * 给定一棵树的前序遍历preorder 与中序遍历 inorder。请构造二叉树并返回其根节点。
 *
 *
 *
 * 示例 1:
 *
 *
 * Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
 * Output: [3,9,20,null,null,15,7]
 * 示例 2:
 *
 * Input: preorder = [-1], inorder = [-1]
 * Output: [-1]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _105_BuildBinaryTreeFromPreorderAndInorder {

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder == null || preorder.length == 0 || inorder == null || inorder.length == 0) {
            return null;
        }
        return helper(preorder, 0, preorder.length - 1,
                        inorder, 0, inorder.length - 1);
    }

    /**
     * 根据先序和中序遍历序列复原二叉树
     * @param preorder
     * @param preStart
     * @param preEnd
     * @param inorder
     * @param inStart
     * @param inEnd
     * @return
     */
    private TreeNode helper(int[] preorder, int preStart, int preEnd,
                            int[] inorder, int inStart, int inEnd) {
        // 递归出口
        if (preStart > preEnd) {
            return null;
        }
        // 先序遍历第一个位置就是树根
        TreeNode root = new TreeNode(preorder[preStart]);
        // 找到树根在中序遍历中的位置
        int index = inStart;
        for (; index <= inEnd; index++) {
            if (inorder[index] == preorder[preStart]) {
                break;
            }
        }
        // 中序遍历，树根左边就是左子树, 得到左子树节点个数
        int left = index - inStart;
        // 因为先序遍历中，根后面跟着的就是左子树，所以先构造左子树
        // 构造左子树
        root.left = helper(preorder, preStart + 1, preStart + left,
                            inorder, inStart, index - 1);
        // 构造右子树
        root.right = helper(preorder, preStart + left + 1, preEnd,
                            inorder, index + 1, inEnd);

        // 返回根节点
        return root;

    }
}
