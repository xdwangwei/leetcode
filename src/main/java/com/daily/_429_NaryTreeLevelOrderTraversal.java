package com.daily;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangwei
 * 2022/4/8 10:47
 *
 * 429. N 叉树的层序遍历
 * 给定一个 N 叉树，返回其节点值的层序遍历。（即从左到右，逐层遍历）。
 *
 * 树的序列化输入是用层序遍历，每组子节点都由 null 值分隔（参见示例）。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：root = [1,null,3,2,4,null,5,6]
 * 输出：[[1],[3,2,4],[5,6]]
 * 示例 2：
 *
 *
 *
 * 输入：root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
 * 输出：[[1],[2,3,4,5],[6,7,8,9,10],[11,12,13],[14]]
 *
 *
 * 提示：
 *
 * 树的高度不会超过 1000
 * 树的节点总数在 [0, 10^4] 之间
 */
public class _429_NaryTreeLevelOrderTraversal {

    /**
     *
     * 树节点定义
     */
    class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    class Solution {
        /**
         * 多叉树层序遍历
         * 广度优先搜索，使用队列结构
         * @param root
         * @return
         */
        public List<List<Integer>> levelOrder(Node root) {
            List<List<Integer>> res = new ArrayList<>();
            // 空数直接返回空列表
            if (root == null) {
                return res;
            }
            // 队列，一次保存一层节点
            Queue<Node> queue = new LinkedList<>();
            // 根节点在第一层，第一层只有根节点
            queue.offer(root);
            while (!queue.isEmpty()) {
                // 当层节点的个数
                int sz = queue.size();
                // 保存这一层节点的值(顺序)
                List<Integer> temp = new ArrayList<>();
                // 队列前sz个节点都是当前层节点
                for (int i = 0; i < sz; ++i) {
                    Node node = queue.poll();
                    // 保存节点值
                    temp.add(node.val);
                    // 当前层节点的孩子都是下一层节点
                    for (Node child : node.children) {
                        queue.offer(child);
                    }
                }
                // 保存当前层的遍历结果
                res.add(temp);
            }
            // 返回
            return res;
        }
    }
}
