package com.tree;

import com.common.TreeNode;

import java.util.Stack;

/**
 * @author wangwei
 * 2021/11/13 11:24
 *
 * 给出二叉 搜索 树的根节点，该树的节点值各不相同，请你将其转换为累加树（Greater Sum Tree），
 * 使每个节点 node的新值等于原树中大于或等于node.val的值之和。
 *
 * 提醒一下，二叉搜索树满足下列约束条件：
 *
 * 节点的左子树仅包含键 小于 节点键的节点。
 * 节点的右子树仅包含键 大于 节点键的节点。
 * 左右子树也必须是二叉搜索树。
 * 注意：本题和 1038:https://leetcode-cn.com/problems/binary-search-tree-to-greater-sum-tree/ 相同
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：[4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
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
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/convert-bst-to-greater-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1038_ConvertBSTToGreaterTree {

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
    public TreeNode bstToGst(TreeNode root) {
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
    public TreeNode bstToGst2(TreeNode root) {
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
