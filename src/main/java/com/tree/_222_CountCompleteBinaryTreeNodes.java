package com.tree;

import com.common.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author wangwei
 * 2020/7/29 20:17
 *
 * 给出一个完全二叉树，求出该树的节点个数。
 *
 * 说明：
 *
 * 完全二叉树的定义如下：在完全二叉树中，除了最底层节点可能没填满外，其余每层节点数都达到最大值，并且最下面一层的节点都集中在该层最左边的若干位置。若最底层为第 h 层，则该层包含 1~ 2h 个节点。
 *
 * 示例:
 *
 * 输入:
 *     1
 *    / \
 *   2   3
 *  / \  /
 * 4  5 6
 *
 * 输出: 6
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/count-complete-tree-nodes
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _222_CountCompleteBinaryTreeNodes {

    /**
     * 层序遍历，宽度优先
     * @param root
     * @return
     */
    public int countNodes(TreeNode root) {

        if (root == null) return 0;
        Queue<TreeNode> queue = new LinkedList<>();
        // 加入根节点
        queue.add(root);
        int res = 1;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            // 左孩子非空
            if (node.left != null) {
                // 左孩子入队列
                queue.add(node.left);
                // 节点数加1
                res++;
            }
            // 右孩子非空
            if (node.right != null) {
                // 右孩子入队列
                queue.add(node.right);
                // 节点数加1
                res++;
            }
        }
        return res;
    }

    /**
     * 递归
     * @param root
     * @return
     */
    public int countNodes2(TreeNode root) {

        if (root == null) return 0;

        return 1 + countNodes2(root.left) + countNodes2(root.right);
    }

    /**
     * 递归方法没有利用完全二叉树的特性，即便是一棵普通二叉树也适用
     *
     * 完全二叉树不同于满二叉树
     *
     * 如果是一棵满二叉树，则一共有 2 ^ d - 1 个 节点， d 为树高
     */
    public int countNodes3(TreeNode root) {
        int h = 0;
        // 计算树的高度
        while (root != null) {
            root = root.left;
            h++;
        }
        // 节点总数就是 2^h - 1
        return (int)Math.pow(2, h) - 1;
    }

    /**
     * 完全二叉树比普通二叉树特殊，但又没有满二叉树那么特殊，
     * 计算它的节点总数，可以说是普通二叉树和完全二叉树的结合版
     *
     * 时间复杂度是 O(logN*logN)
     *
     * 最后的两个递归只有一个会真的递归下去，另一个一定会触发hl == hr而立即返回，不会递归下去。
     *
     * 原因如下：一棵完全二叉树(紧凑靠左排列)的两棵子树，至少有一棵是满二叉树：
     *
     * 算法的递归深度就是树的高度 O(logN)，每次递归所花费的时间就是 while 循环，需要 O(logN)，所以总体的时间复杂度是 O(logN*logN)
     */
    public int countCompleteBinaryTreeNodes(TreeNode root) {
        TreeNode l = root, r = root;
        // 当前树根的左右子树高度
        int hl = 0, hr = 0;
        // 计算左子树高度
        while (l != null) {
            // 一直向左
            l = l.left;
            hl++;
        }
        // 计算右子树高度
        while (r != null) {
            // 一直向右
            r = r.right;
            hr++;
        }
        // 如果左右子树的高度相同，则是一棵 满二叉树
        if (hl == hr) {
            return (int)Math.pow(2, hl) - 1;
        }
        // 如果左右高度不同，则按照普通二叉树的逻辑计算，这两个递归只有一个会一直递归下去
        return 1 + countCompleteBinaryTreeNodes(root.left) + countCompleteBinaryTreeNodes(root.right);
    }
}
