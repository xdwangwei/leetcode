package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2021/11/13 14:53
 */
public class _106_BuildBinaryTreeFromPostorderAndInorder {

    public TreeNode buildTree(int[] inorder, int[] postorder) {
        if (inorder == null || inorder.length == 0 || postorder == null || postorder.length == 0) {
            return null;
        }
        return helper(inorder, 0, inorder.length - 1,
                        postorder, 0, postorder.length - 1);
    }

    /**
     * 根据中序和后序遍历序列复原二叉树
     * @param inorder
     * @param inStart
     * @param inEnd
     * @param postorder
     * @param postStart
     * @param postEnd
     * @return
     */
    private TreeNode helper(int[] inorder, int inStart, int inEnd, int[] postorder, int postStart, int postEnd) {
        // 递归出口
        if (inStart > inEnd) {
            return null;
        }
        // 后序遍历最后一个节点就是根
        TreeNode root = new TreeNode(postorder[postEnd]);
        // 找到树根在中序遍历中的位置，得到右子树的长度
        int index = inStart;
        for (; index <= inEnd; index++) {
            if (inorder[index] == postorder[postEnd]) {
                break;
            }
        }
        int right = inEnd - index;
        // 后序遍历中。根前面就是右子树部分，所以先构造右子树
        root.right = helper(inorder, index + 1, inEnd,
                            postorder, postEnd - right, postEnd - 1);
        // 再构造左子树
        root.left = helper(inorder, inStart, index - 1,
                            postorder, postStart, postEnd - right - 1);
        // 返回根
        return root;
    }
}
