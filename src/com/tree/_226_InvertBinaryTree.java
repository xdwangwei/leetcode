package com.tree;

import com.common.TreeNode;

import java.util.TreeMap;

/**
 * @author wangwei
 * 2020/12/12 10:06
 *
 * 翻转一棵二叉树。
 *
 * 示例：
 *
 * 输入：
 *
 *      4
 *    /   \
 *   2     7
 *  / \   / \
 * 1   3 6   9
 * 输出：
 *
 *      4
 *    /   \
 *   7     2
 *  / \   / \
 * 9   6 3   1
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/invert-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _226_InvertBinaryTree {

    /**
     * 只需要交换每个节点的左右孩子即可，可以按先序顺序。也可以按后续顺序
     * @param root
     * @return
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        /*  先序遍历代码  */
        // 交换当前节点左右子树
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        // 递归处理左右子树
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }
}
