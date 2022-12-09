package com.offerassult;

import com.common.TreeNode;

import java.util.Stack;

/**
 * @author wangwei
 * @date 2022/12/9 16:47
 * @description: _054_MakeNodeValueGreaterInBinarySearchTree
 *
 * 剑指 Offer II 054. 所有大于等于节点的值之和
 * 给定一个二叉搜索树，请将它的每个节点的值替换成树中大于或者等于该节点值的所有节点值之和。
 *
 *
 *
 * 提醒一下，二叉搜索树满足下列约束条件：
 *
 * 节点的左子树仅包含键 小于 节点键的节点。
 * 节点的右子树仅包含键 大于 节点键的节点。
 * 左右子树也必须是二叉搜索树。
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：root = [4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
 * 输出：[30,36,21,36,35,26,15,null,null,null,33,null,null,null,8]
 * 示例 2：
 *
 * 输入：root = [0,null,1]
 * 输出：[1,null,1]
 * 示例 3：
 *
 * 输入：root = [1,0,2]
 * 输出：[3,3,2]
 * 示例 4：
 *
 * 输入：root = [3,2,4,1]
 * 输出：[7,9,4,10]
 *
 *
 * 提示：
 *
 * 树中的节点数介于 0 和 104 之间。
 * 每个节点的值介于 -104 和 104 之间。
 * 树中的所有值 互不相同 。
 * 给定的树为二叉搜索树。
 *
 *
 * 注意：
 *
 * 本题与主站 538 题相同： https://leetcode-cn.com/problems/convert-bst-to-greater-tree/
 * 本题与主站 1038 题相同：https://leetcode-cn.com/problems/binary-search-tree-to-greater-sum-tree/
 */
public class _054_MakeNodeValueGreaterInBinarySearchTree {

    /**
     * 递归
     * 由于是二叉搜索树，那么每个节点要知道比它大的节点和相等节点，必须在访问它之前已经访问过了它的父节点和右子树
     *
     * 所以这里的递归顺序为，先遍历右子树，再遍历自己，再遍历左子树
     * 利用sum维护累加和，。。
     * 右子树和父节点都比它大，所以可以说是一路累加。。。。。。
     * @param root
     * @return
     */
    public TreeNode convertBST(TreeNode root) {
        traverse(root);
        return root;
    }
    // 比当前节点大的节点值的累加和（已访问过的父节点及父节点的右子树的节点值之和)
    int sum = 0;
    private void traverse(TreeNode root) {
        if (root == null) {
            return;
        }
        // ********* 遍历右孩子
        traverse(root.right);
        // 处理根，先累加当前值(题目要求:每个节点 node的新值等于原树中大于或等于node.val的值之和)
        // 所以要先改变sum,再更新val
        sum += root.val;
        // 再更新为新值
        root.val = sum;
        // ********** 遍历左孩子
        traverse(root.left);
    }

    /**
     * 递归能实现,栈也应该能实现
     */
    public TreeNode convertBST2(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        int sum = 0;

        // 中序遍历对称便利的迭代写法
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            /** 右子树部分 **/
            // 先把 右右右右右 路径压栈
            while (cur != null) {
                stack.push(cur);
                cur = cur.right;
            }
            /** 当前节点 **/
            // 逐个出栈,更新节点值
            TreeNode pop = stack.pop();
            // 先累加sum,再更新val,至于原因与方法一所写一样
            sum += pop.val;
            pop.val = sum;

            /** 左子树部分 **/
            // 右右右完了,就是父亲和右孩子都完了,那么切换到left,
            cur = pop.left;
        }
        return root;
    }
}
