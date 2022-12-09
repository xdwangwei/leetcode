package com.tree;

import com.common.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/12/9 16:10
 * @description: _897_FlattenBinarySearchTree
 *
 * 897. 递增顺序搜索树
 * 给你一棵二叉搜索树的 root ，请你 按中序遍历 将其重新排列为一棵递增顺序搜索树，使树中最左边的节点成为树的根节点，并且每个节点没有左子节点，只有一个右子节点。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：root = [5,3,6,2,4,null,8,1,null,null,null,7,9]
 * 输出：[1,null,2,null,3,null,4,null,5,null,6,null,7,null,8,null,9]
 * 示例 2：
 *
 *
 * 输入：root = [5,1,7]
 * 输出：[1,null,5,null,7]
 *
 *
 * 提示：
 *
 * 树中节点数的取值范围是 [1, 100]
 * 0 <= Node.val <= 1000
 * 通过次数74,684提交次数100,794
 */
public class _897_FlattenBinarySearchTree {

    /**
     * 方法一：递归
     *
     * 假设展平 root 的左子树 得到单链表 left
     * 假设展平 root 的右子树 得到单链表 right
     *
     * 那么。展平root得到结果应该为 head(left) -> root -> tail(right)
     *
     * 因此，左右子树递归应该返回展平后链表的头节点和尾节点
     * @param root
     * @return
     */
    public TreeNode increasingBST(TreeNode root) {
        if (root == null) {
            return null;
        }
        // 返回展平后链表的头节点
        return traverse(root)[0];
    }

    /**
     * 将以root为根的子树展平为单链表，返回链表的头节点和尾节点
     * @param root
     * @return
     */
    private TreeNode[] traverse(TreeNode root) {
        // 空树返回
        if (root == null) {
            return null;
        }
        // 初始化返回值，首尾都为root
        TreeNode[] res = new TreeNode[]{root, root};
        // 如果root有右子树
        if (root.right != null) {
            // 展平右子树
            TreeNode[] right = traverse(root.right);
            // 将右子树链接到root.right
            root.right = right[0];
            // 更新返回值链表的尾部节点
            res[1] = right[1];
        }
        // 如果root没有左子树，可以提前返回了
        if (root.left == null) {
            return res;
        }
        // 否则，展平root的左子树
        TreeNode[] left = traverse(root.left);
        // 将root开头的右半部分链表链接到左部分链表的末尾
        left[1].right = root;
        // 将root的left置空
        root.left = null;
        // 更新返回值链表的头节点
        res[0] = left[0];
        // 返回
        return res;
    }


    /**
     * 方法二：递归
     *
     * 实际上，可以对二叉树的中序遍历轻微改造，达到同样的目的
     *
     * 中序遍历
     * 使用变量pre指向遍历到的上一个节点，
     * 遍历到当前节点 cur 时，让 pre.right = cur
     * 重置 cur 的left为 null
     * 更新 pre = cur
     *
     * 因为最后要返回链表的头节点
     * 因此，初始时，初始化一个dummy节点，让dummy.right = root，让 pre = dummy
     * 然后对 root 进行上述 中序遍历过程
     *
     * 最后返回 dummy.right 即可
     * @param root
     * @return
     */
    // pre指向中序遍历过程中上一个节点
    private TreeNode pre;
    public TreeNode increasingBST2(TreeNode root) {
        // 初始化一个dummy。让 dummy.right = root
        TreeNode dummy = new TreeNode(-1, null, root);
        // 初始 pre 指向 dummy
        pre = dummy;
        // 对 root 进行上述 中序遍历过程
        preOrderTraverse(root);
        // 返回
        return dummy.right;
    }

    /**
     * 中序遍历，将root为根的子树展平为单向链表
     * pre 代表遍历到的上一个节点
     * @param root
     */
    private void preOrderTraverse(TreeNode root) {
        // 退出
        if (root == null) {
            return;
        }
        // 先访问左子树
        preOrderTraverse(root.left);
        // 访问当前节点
        // 让 pre.right = 当前节点
        pre.right = root;
        // 断开当前节点的left
        root.left = null;
        // 更新pre为当前节点
        pre = root;
        // 访问右子树
        preOrderTraverse(root.right);
    }


    /**
     * 方法三：迭代
     *
     * 思路与方法二一样，仍然是二叉树的中序遍历，只是采用迭代的方式实现
     * @param root
     * @return
     */
    public TreeNode increasingBST3(TreeNode root) {
        if (root == null) {
            return null;
        }
        // 初始化一个dummy。让 dummy.right = root
        TreeNode dummy = new TreeNode(-1, null, root);
        // 初始 pre 指向 dummy
        TreeNode pre = dummy;

        /** 二叉树的中序遍历，迭代写法 **/
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        while (!stack.isEmpty() || cur != null) {
            /** 左子树部分 **/
            // 左子节点一路向左下入栈
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            // 出栈节点为当前待访问节点
            cur = stack.pop();

            /** 访问当前节点 **/
            // 让 pre.right = 当前节点
            pre.right = root;
            // 断开当前节点的left
            root.left = null;
            // 更新pre为当前节点
            pre = root;

            /** 右子树部分 **/
            // 准备访问右子树
            cur = cur.right;
        }

        // 返回
        return dummy.right;
    }
}
