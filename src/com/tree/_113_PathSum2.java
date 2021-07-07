package com.tree;

import com.common.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author wangwei
 * 2020/8/29 10:52
 *
 * 给定一个二叉树和一个目标和，找到所有从根节点到叶子节点路径总和等于给定目标和的路径。
 *
 * 说明:叶子节点是指没有子节点的节点。
 *
 * 示例:
 * 给定如下二叉树，以及目标和sum = 22，
 *
 *               5
 *              / \
 *             4   8
 *            /   / \
 *           11  13  4
 *          /  \    / \
 *         7    2  5   1
 * 返回:
 *
 * [
 *    [5,4,11,2],
 *    [5,8,4,5]
 * ]
 * 通过次数69,360提交次数113,897
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/path-sum-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _113_PathSum2 {

    /**
     * 深度优先遍历（回溯算法）
     * @param root
     * @param sum
     * @return
     */
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        // 深度优先遍历，回溯
        pathSum(root, sum, res, new ArrayDeque<>());
        return res;
    }

    /**
     * 回溯 dfs
     * @param node
     * @param sum
     * @param res
     * @param path
     */
    private void pathSum(TreeNode node, int sum, List<List<Integer>> res, Deque<Integer> path) {
        // 递归出口
        if (node == null) return;
        // 当前节点(沿途节点)加入路径
        path.addLast(node.val);
        // sum 减去这个结点的值
        sum -= node.val;

        // 必须在叶子节点终止
        if (node.left == null && node.right == null && sum == node.val) {
            // 加入结果集
            res.add(new ArrayList<>(path));
            // 撤销本次选择
            path.remove(path.size() - 1);
            return;
        }
        // 进入左子树
        pathSum(node.left, sum, res, path);
        // 进入右子树
        pathSum(node.right, sum, res, path);
        // 当前节点移出路径
        path.removeLast();
    }
}
