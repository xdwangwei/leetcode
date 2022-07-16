package com.bfs;

import com.common.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author wangwei
 * 2020/8/29 19:43
 *
 * 给定一个二叉树，找出其最小深度。
 *
 * 最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
 *
 * 说明: 叶子节点是指没有子节点的节点。
 *
 * 示例:
 *
 * 给定二叉树 [3,9,20,null,null,15,7],
 *
 *         3
 *        / \
 *       9  20
 *      /  \
 *     15   7
 * 返回它的最小深度  2.
 */
public class _111_MinimumDepthOfBinaryTree {

    /**
     * BFS
     * 显然起点就是root根节点，终点就是最靠近根节点的那个「叶子节点」嘛，叶子节点就是两个子节点都是null的节点：
     *
     * BFS 的逻辑，depth每增加一次，队列中的所有节点都向前迈一步，
     * 这保证了第一次到达终点的时候，走的步数是最少的。
     *
     * visited集合的主要作用是防止走回头路，大部分时候都是必须的，
     * 但是像一般的二叉树结构，没有子节点到父节点的指针，不会走回头路就不需要visited。
     * @param root
     * @return
     */
    public int minDepth(TreeNode root) {
        if (root == null) return 0;
        // BFS解题框架
        Queue<TreeNode> q = new LinkedList<>();
        // 没有回头路就不需要visited集合
        // HashSet<TreeNode> visited = new HashSet<>();
        q.offer(root);
        // root 本身就是一层，depth 初始化为 1
        int depth = 1;

        while (!q.isEmpty()) {
            // 当前层节点个数
            int sz = q.size();
            /* 将当前队列中的所有节点向四周扩散 */
            for (int i = 0; i < sz; i++) {
                TreeNode cur = q.poll();
                /* 判断是否到达终点 */
                if (cur.left == null && cur.right == null)
                    return depth;
                /* 将 cur 的相邻节点加入队列 */
                if (cur.left != null)
                    q.offer(cur.left);
                if (cur.right != null)
                    q.offer(cur.right);
            }
            /* 这里增加深度 */
            depth++;
        }
        return depth;
    }
}
