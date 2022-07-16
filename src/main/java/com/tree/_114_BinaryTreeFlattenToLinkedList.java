package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2020/12/12 10:11
 *
 * 给定一个二叉树，原地将它展开为一个单链表。
 *
 *  
 *
 * 例如，给定二叉树
 *
 *     1
 *    / \
 *   2   5
 *  / \   \
 * 3   4   6
 * 将其展开为：
 *
 * 1
 *  \
 *   2
 *    \
 *     3
 *      \
 *       4
 *        \
 *         5
 *          \
 *           6
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/flatten-binary-tree-to-linked-list
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _114_BinaryTreeFlattenToLinkedList {

    /**
     * 按照所给示例来看。先把左子树展开为列表。作为跟的右孩子。再把原来的右子树展开为列表。接在链表的后面
     *
     * 递归的魅力，你说 flatten 函数是怎么把左右子树拉平的？说不清楚，但是只要知道 flatten 的定义如此，相信这个定义，让 root 做它该做的事情，然后 flatten 函数就会按照定义工作。
     * 另外注意递归框架是后序遍历，因为我们要先拉平左右子树才能进行后续操作。
     * @param root
     */
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        // 展开左子树为链表
        flatten(root.left);
        // 展开右子树为链表
        flatten(root.right);

        // 拿到两个链表
        TreeNode left = root.left;
        TreeNode right = root.right;

        // 先把左链表连在根右面
        root.right = left;
        // 让左子树为空
        root.left = null;

        TreeNode t = root;
        while (t.right != null) {
            t = t.right;
        }
        // 再把右链表链接到当前链表的最右面
        t.right = right;
    }

}
