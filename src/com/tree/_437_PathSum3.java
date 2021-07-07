package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2020/8/29 13:03
 * 给定一个二叉树，它的每个结点都存放着一个整数值。
 *
 * 找出路径和等于给定数值的路径总数。
 *
 * 路径不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
 *
 * 二叉树不超过1000个节点，且节点数值范围是 [-1000000,1000000] 的整数。
 *
 * 示例：
 *
 * root = [10,5,-3,3,2,null,11,3,-2,null,1], sum = 8
 *
 *       10
 *     /    \
 *     5    -3
 *   /  \     \
 *  3    2     11
 * / \   \
 * 3 -2   1
 *
 * 返回 3。和等于 8 的路径有:
 *
 * 1.  5 -> 3
 * 2.  5 -> 2 -> 1
 * 3.  -3 -> 11
 *
 */
public class _437_PathSum3 {

    /**
     * 题目要求 路径不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点） 。
     * 这就要求我们只需要去求三部分即可：
     *
     * 以当前节点作为开始结点的路径数量
     * 左子树中的路径数量
     * 右子树中的路径数量
     *
     * 将这三部分之和作为最后结果即可。
     *
     * 最后的问题是：我们应该如何去求以当前节点作为头结点的路径的数量？
     *
     * 这里依旧是递归，
     * 如果node.val == sum 自身作为一个路径，否则再去计算
     * 以左右孩子为开始节点，和为sum - root.val的路径数量
     *
     * 相当于有两个递归，第一个递归是dfs，节点的访问顺序，
     * 第二个递归是访问每个节点时，计算以它开始的和为sum的路径数量
     *
     * 作者：Geralt_U
     * 链接：https://leetcode-cn.com/problems/path-sum-iii/solution/437lu-jing-zong-he-iii-di-gui-fang-shi-by-ming-zhi/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @param sum
     * @return
     */
    /* 有了以上铺垫，详细注释一下代码 */
    public int pathSum(TreeNode root, int sum) {
        if (root == null) return 0;
        // 自己为开头，满足和为sum的路径数
        int pathImLeading = countPath(root, sum);
        // 左子树中满足和为sum的路径数
        int leftPathSum = pathSum(root.left, sum);
        // 右子树中满足和为sum的路径数
        int rightPathSum = pathSum(root.right, sum);
        // 求和，返回
        return leftPathSum + rightPathSum + pathImLeading;
    }

    /**
     * 计算以自己为开头的路径中，和为sum的路径数
     * @param node
     * @param sum
     * @return
     */
    private int countPath(TreeNode node, int sum) {
        if (node == null) return 0;
        // 我自己能不能独当一面，作为一条单独的路径呢？
        int isMe = (node.val == sum) ? 1 : 0;
        // 左边的小老弟，你那边能凑几个和为 sum - node.val 的路径呀？
        int leftBrother = countPath(node.left, sum - node.val);
        // 右边的小老弟，你那边能凑几个和为 sum - node.val 的路径呀？
        int rightBrother = countPath(node.right, sum - node.val);
        // 求和，我这能凑这么多个
        return  isMe + leftBrother + rightBrother;
    }
}
