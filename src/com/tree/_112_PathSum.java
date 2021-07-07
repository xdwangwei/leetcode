package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2020/8/29 10:42
 *
 * 给定一个二叉树和一个目标和，判断该树中是否存在根节点到叶子节点的路径，这条路径上所有节点值相加等于目标和。
 *
 * 说明:叶子节点是指没有子节点的节点。
 *
 * 示例:
 * 给定如下二叉树，以及目标和 sum = 22，
 *
 *               5
 *              / \
 *             4   8
 *            /   / \
 *           11  13  4
 *          /  \      \
 *         7    2      1
 * 返回 true, 因为存在目标和为 22 的根节点到叶子节点的路径 5->4->11->2。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/path-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _112_PathSum {

    public boolean hasPathSum(TreeNode root, int sum) {
        if (root == null) return false;
        // 因为题目说了一定是从根到叶子的路径，到叶子结束
        if (root.val == sum && root.left == null && root.right == null) return true;
        // 再去询问它的左右子孩子是否存在以自己开始，到叶子节点结束的路径和为sum-val的路径
        return hasPathSum(root.left, sum - root.val) || hasPathSum(root.right, sum - root.val);
    }
}
