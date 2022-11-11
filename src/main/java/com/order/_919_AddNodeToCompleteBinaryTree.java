package com.order;

import com.common.TreeNode;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author wangwei
 * @date 2022/11/11 11:12
 * @description: _919_AddNodeToCompleteBinaryTree
 *
 * 919. 往完全二叉树添加节点
 * 完全二叉树是每一层（除最后一层外）都是完全填充（即，节点数达到最大，第 n 层有 2n-1 个节点）的，并且所有的节点都尽可能地集中在左侧。
 *
 * 设计一个用完全二叉树初始化的数据结构 CBTInserter，它支持以下几种操作：
 *
 * CBTInserter(TreeNode root) 使用根节点为 root 的给定树初始化该数据结构；
 * CBTInserter.insert(int v)  向树中插入一个新节点，节点类型为 TreeNode，值为 v 。使树保持完全二叉树的状态，并返回插入的新节点的父节点的值；
 * CBTInserter.get_root() 将返回树的根节点。
 *
 *
 * 示例 1：
 *
 * 输入：inputs = ["CBTInserter","insert","get_root"], inputs = [[[1]],[2],[]]
 * 输出：[null,1,[1,2]]
 * 示例 2：
 *
 * 输入：inputs = ["CBTInserter","insert","insert","get_root"], inputs = [[[1,2,3,4,5,6]],[7],[8],[]]
 * 输出：[null,3,4,[1,2,3,4,5,6,7,8]]
 *
 *
 * 提示：
 *
 * 最初给定的树是完全二叉树，且包含 1 到 1000 个节点。
 * 每个测试用例最多调用 CBTInserter.insert  操作 10000 次。
 * 给定节点或插入节点的每个值都在 0 到 5000 之间。
 *
 *
 */
public class _919_AddNodeToCompleteBinaryTree {

    /**
     * 利用完全二叉数的特性，若用数组存储节点，节点编号从1开始
     * 那么满足 node[i].left = node[i * 2]，node[i].right = node[i * 2 + 1]
     * 用一个变量size保存当前节点个数，
     * 每次添加节点就往数组末尾位置size++添加，它的父节点位置就是 size / 2
     * 如果 size 是偶数，那么它就是它父节点的左孩子，否则就是其右孩子
     */
    class CBTInserter {

        // 根据题目数据范围
        TreeNode[] nodes = new TreeNode[11001];

        int size;

        // 注意需要将root整棵树中节点转移到nodes数组中
        public CBTInserter(TreeNode root) {
            size = 0;
            // 采用bfs层序遍历转移
            Queue<TreeNode> queue = new ArrayDeque<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                TreeNode cur = queue.poll();
                // 按顺序添加到nodes中，编号从1开始
                nodes[++size] = cur;
                if (cur.left != null) {
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                }
            }
        }

        // 加入节点，返回其父节点的值
        public int insert(int v) {
            // 添加进末尾即可
            nodes[++size] = new TreeNode(v);
            // 找到其父节点
            TreeNode fa = nodes[size / 2];
            // 根据自己所在位置奇偶性判断是左还是右
            if ((size & 1) == 0) {
                fa.left = nodes[size];
            } else {
                fa.right = nodes[size];
            }
            // 返回父节点值
            return fa.val;
        }

        // 返回根节点，编号从1开始
        public TreeNode get_root() {
            return nodes[1];
        }
    }
}
