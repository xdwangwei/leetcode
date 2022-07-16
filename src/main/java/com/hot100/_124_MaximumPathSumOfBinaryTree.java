package com.hot100;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2022/4/17 18:22
 *
 * 124. 二叉树中的最大路径和
 * 路径 被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中 至多出现一次 。该路径 至少包含一个 节点，且不一定经过根节点。
 *
 * 路径和 是路径中各节点值的总和。
 *
 * 给你一个二叉树的根节点 root ，返回其 最大路径和 。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：root = [1,2,3]
 * 输出：6
 * 解释：最优路径是 2 -> 1 -> 3 ，路径和为 2 + 1 + 3 = 6
 * 示例 2：
 *
 *
 * 输入：root = [-10,9,20,null,null,15,7]
 * 输出：42
 * 解释：最优路径是 15 -> 20 -> 7 ，路径和为 15 + 20 + 7 = 42
 *
 *
 * 提示：
 *
 * 树中节点数目范围是 [1, 3 * 104]
 * -1000 <= Node.val <= 1000
 */
public class _124_MaximumPathSumOfBinaryTree {

    // 题目中节点值取值范围   -1000  ---- 1000
    private int res = -100001;

    /**
     * 递归
     * 树结构中的任意一条路径，一定是这样的，从某个节点往上走，走到中间顶部节点，又开始往下走
     * 此时这个中间节点就看作当前树根，左边部分就是它的左子树部分中的最大路径和，右边部分就是它的右子树中的最大路径和
     * 并且，如果某部分路径和小于0，我们可以选择丢弃这部分，取0即可。
     *
     * 所以需要先计算左右子树贡献的最大路径和，才能得到自己能贡献的最大路径和。
     *
     * 对于节点 node 来说，
     *      它的左子树部分能贡献的最大路径和应该是 maxPathSum(node.left) 和 0 中的最大值
     *      它的右子树部分能贡献的最大路径和应该是 maxPathSum(node.right) 和 0 中的最大值
     *      那么以node为中心的话，
     *          组合在一起的这条路径(用于更新全局res)最大路径和就是 max(maxPathSum(node.left),0) + max(maxPathSum(node.right),0) + node.val
     *              # 因为它是中间连接点，不能不要它
     *          但是它返回给父节点的最大路径和只能是 左子树最大路径和 和 右子树最大路径和 二者较大值 加上本身的节点值，
     *              因为对于父节点p来说，它只能选择 p->node->left部分 或者 p->node->right部分一个方向延申
     *
     * 所以在递归过程中，注意 更新全局最大路径和 和 返回给上一层的 最大路径和 这两个值的区别
     * @param root
     * @return
     */
    public int maxPathSum(TreeNode root) {
        // 在后序遍历过程中，得到了全部可能路径和，这里只需要dfs(root)
        pathSum(root);
        return res;
    }

    /**
     * dfs(node) 经过节点node能够贡献的最大路径和
     * @param root
     * @return
     */
    private int pathSum(TreeNode root) {
        // null节点返回0
        if (root == null) {
            return 0;
        }
        // 左子树贡献的最大路径和，可选择丢弃
        int maxLeftPathSum = Math.max(pathSum(root.left), 0);
        // 右子树贡献的最大路径和，可选择丢弃
        int maxRightPathSum = Math.max(pathSum(root.right), 0);
        // 对于以它为中心节点，组合成的这条路径的路径和
        int tempPathSum = maxLeftPathSum + maxRightPathSum + root.val;
        // 这个路径和用于更新全局最大路径和
        res = Math.max(res, tempPathSum);
        // 但是它贡献给父节点时，只能选择左右其中一个方向，结合自己
        return Math.max(maxLeftPathSum, maxRightPathSum) + root.val;
    }
}
