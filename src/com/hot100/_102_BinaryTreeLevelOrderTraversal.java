package com.hot100;

import com.common.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangwei
 * 2022/4/17 18:12
 *
 * 102. 二叉树的层序遍历
 * 给你二叉树的根节点 root ，返回其节点值的 层序遍历 。 （即逐层地，从左到右访问所有节点）。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：[[3],[9,20],[15,7]]
 * 示例 2：
 *
 * 输入：root = [1]
 * 输出：[[1]]
 * 示例 3：
 *
 * 输入：root = []
 * 输出：[]
 *
 *
 * 提示：
 *
 * 树中节点数目在范围 [0, 2000] 内
 * -1000 <= Node.val <= 1000
 */
public class _102_BinaryTreeLevelOrderTraversal {

    /**
     * 二叉树层序遍历，没啥好说的，直接bfs，队列
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        // 提前判断，避免null入队列
        if (root == null) {
            return res;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        // 加入根
        queue.offer(root);
        while (!queue.isEmpty()) {
            // 当前层节点个数
            int size = queue.size();
            // 保存当前层节点
            List<Integer> layer = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                // 出队列
                TreeNode node = queue.poll();
                layer.add(node.val);
                // 按顺序加入左右孩子
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            // 加入当层
            res.add(layer);
        }
        // 返回
        return res;
    }
}
