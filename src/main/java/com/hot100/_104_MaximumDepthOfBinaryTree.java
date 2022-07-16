package com.hot100;

import com.common.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author wangwei
 * 2022/4/17 18:16
 *
 * 104. 二叉树的最大深度
 * 给定一个二叉树，找出其最大深度。
 *
 * 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 *
 * 说明: 叶子节点是指没有子节点的节点。
 *
 * 示例：
 * 给定二叉树 [3,9,20,null,null,15,7]，
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回它的最大深度 3 。
 *
 * 通过次数694,702提交次数903,156
 */
public class _104_MaximumDepthOfBinaryTree {

    /**
     * 其实还是一个层序遍历，就看最后一层的深度是多少
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 标准层序遍历
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;
        while (!queue.isEmpty()) {
            // 每进来一次，层数(深度)加1
            depth++;
            int size = queue.size();
            for (int i = 0; i < size; ++i) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return depth;
    }


    /**
     * 深度优先搜索/递归
     *
     * 如果我们知道了左子树和右子树的最大深度 l 和 r，那么该二叉树的最大深度即为 max(l,r)+1
     *
     * 而左子树和右子树的最大深度又可以以同样的方式进行计算。
     * 因此我们可以用「深度优先搜索」的方法来计算二叉树的最大深度。
     * 具体而言，在计算当前二叉树的最大深度时，可以先递归计算出其左子树和右子树的最大深度，
     * 然后在 O(1) 时间内计算出当前二叉树的最大深度。
     * 递归在访问到空节点时退出。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/solution/er-cha-shu-de-zui-da-shen-du-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @return
     */
    public int maxDepth2(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            int leftHeight = maxDepth2(root.left);
            int rightHeight = maxDepth2(root.right);
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }
}
