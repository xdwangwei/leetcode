package com.daily;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2022/5/24 8:36
 * @description: _965_UnivaluedBinaryTree
 *
 * 965. 单值二叉树
 * 如果二叉树每个节点都具有相同的值，那么该二叉树就是单值二叉树。
 *
 * 只有给定的树是单值二叉树时，才返回 true；否则返回 false。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：[1,1,1,1,1,null,1]
 * 输出：true
 * 示例 2：
 *
 *
 *
 * 输入：[2,2,2,5,2]
 * 输出：false
 *
 *
 * 提示：
 *
 * 给定树的节点数范围是 [1, 100]。
 * 每个节点的值都是整数，范围为 [0, 99] 。
 */
public class _965_UnivaluedBinaryTree {


    /**
     * 方法一：深度优先搜索
     * 思路与算法
     *
     * 一棵树的所有节点都有相同的值，可以对树进行一次深度优先搜索。
     *
     * 树 x 为单值树，当且仅当 x.left 为单值树 并且 x.right 为单值树 并且 x.val == x.left.val == x.right.val
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/univalued-binary-tree/solution/dan-zhi-er-cha-shu-by-leetcode-solution-15bn/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @return
     */
    public boolean isUnivalTree(TreeNode root) {
        // 空树默认返回true
        if (root == null) {
            return true;
        }
        // 树 x 为单值树，当且仅当 x.left 为单值树 并且 x.right 为单值树 并且 x.val == x.left.val == x.right.val
        if (root.left != null) {
            // false，子树不满足要求
            if (root.val != root.left.val || !isUnivalTree(root.left)) {
                return false;
            }
        }
        if (root.right != null) {
            // false，子树不满足要求
            if (root.val != root.right.val || !isUnivalTree(root.right)) {
                return false;
            }
        }
        // 返回true
        return true;
    }
}
