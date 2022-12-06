package com.offerassult;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2022/12/6 11:53
 * @description: _051_MaximumPathForSumOfNodes
 *
 * 剑指 Offer II 051. 节点之和最大的路径
 * 路径 被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中 至多出现一次 。该路径 至少包含一个 节点，且不一定经过根节点。
 *
 * 路径和 是路径中各节点值的总和。
 *
 * 给定一个二叉树的根节点 root ，返回其 最大路径和，即所有路径上节点值之和的最大值。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：root = [1,2,3]
 * 输出：6
 * 解释：最优路径是 2 -> 1 -> 3 ，路径和为 2 + 1 + 3 = 6
 * 示例 2：
 *
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
 *
 *
 * 注意：本题与主站 124 题相同： https://leetcode-cn.com/problems/binary-tree-maximum-path-sum/
 */
public class _051_MaximumPathForSumOfNodes {

    /**
     * 递归 + 后序遍历
     *
     * 二叉树中的路径结构可能比较复杂，我们可以从节点出发考虑，经过节点的路径肯定分为
     *      左子树部分 --> 当前节点 --> 右子树部分
     *      左子树部分 --> 当前节点 --> 往上延申
     *      右子树部分 --> 当前节点 --> 往上延申
     *
     *      而后两种情况都可以看作 ，，，经过当前节点的祖先节点的 左子树部分路径
     *
     * 因此，可以认为所有路径都是这样的形式：
     *          从某个节点往上走（左子树部分），走到中间顶部节点（当前节点），又开始往下走（右子树部分），
     *          我们称中间点为“拐点”
     *
     *
     * 当然了，由于节点值可能为负，所以最大和路径可能只是一条单边路径，（没有左半部分或右半部分）
     * 但是，这种情况仍然可以看作是最高点是拐点，只是因为右单边路径路径和为负数，所以拐点舍弃了右边部分
     *
     * 因此，问题转换为，求树中每个点作为拐点时，能取得的最大路径和
     *
     * 假设当前节点为node，它作为拐点，能取得的最大路径和应该等于
     *      以node.left开头的左下最大单边路径和（left） + node.val + 以node.right开头的右下最大单边路径和（right）
     *      当然了，如果 left 或 right 为 负数，那么我们选择废弃
     *
     * 综上，遍历到当前节点node时，需要知道 以node.left开头的左下最大单边路径和、以node.right开头的右下最大单边路径和
     *
     * 因此，这是一个后序遍历的递归
     *
     *      递归内部：
     *          拿到了 以node.left开头的左下最大单边路径和（left），以node.right开头的右下最大单边路径和（right）
     *          那么，以当前节点为拐点的最大路径和为
     *              Math.max(left, 0) + node.val + Math.max(right, 0)
     *      递归结束：
     *          向上返回 以当前节点开头的（向左或向右的）最长单边路径 （它的父亲需要啥，它就要返回啥）
     *              对于node的父节点来说，node只能连向一个方向，不然岂不是分叉了？？？
     *              return node.val + Math.max(left, right)
     * 在所有节点为拐点的最大路径和中 取最大值
     *
     * 所以在递归过程中，注意 更新全局最大路径和 和 返回给上一层的 最大路径和 这两个值的区别
     *
     *
     * @param root
     * @return
     */

    // 题目中节点值取值范围   -1000  ---- 1000
    private int res = -100001;


    public int maxPathSum(TreeNode root) {
        // 在后序遍历过程中，得到了全部可能路径和，这里只需要dfs(root)
        oneSideMaxPathSum(root);
        return res;
    }

    /**
     * 递归
     * 向上返回 以当前节点开头的（向左或向右的）最长单边路径 （它的父亲需要啥，它就要返回啥）
     * 但是在递归的过程中，能得到以当前节点作为拐点的最大路径和
     * @param root
     * @return
     */
    private int oneSideMaxPathSum(TreeNode root) {
        // null节点返回0
        if (root == null) {
            return 0;
        }

        // 以node.left开头的左下最大单边路径和，可选择丢弃
        int maxLeftPathSum = Math.max(oneSideMaxPathSum(root.left), 0);
        // 以node.right开头的右下最大单边路径和，可选择丢弃
        int maxRightPathSum = Math.max(oneSideMaxPathSum(root.right), 0);

        // 以当前节点为拐点的组合成的这条路径的最大路径和
        int tempPathSum = maxLeftPathSum + maxRightPathSum + root.val;
        // 这个路径和用于更新全局最大路径和
        res = Math.max(res, tempPathSum);

        // 递归向上返回当前节点开头的最大单边路径和，只能选择左右其中一个方向
        return Math.max(maxLeftPathSum, maxRightPathSum) + root.val;
    }
}
