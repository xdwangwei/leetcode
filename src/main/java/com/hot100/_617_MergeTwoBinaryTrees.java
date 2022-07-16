package com.hot100;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2022/5/6 22:13
 * @description: _617_MergeTwoBinaryTrees
 *
 * 617. 合并二叉树
 * 给你两棵二叉树： root1 和 root2 。
 *
 * 想象一下，当你将其中一棵覆盖到另一棵之上时，两棵树上的一些节点将会重叠（而另一些不会）。你需要将这两棵树合并成一棵新二叉树。合并的规则是：如果两个节点重叠，那么将这两个节点的值相加作为合并后节点的新值；否则，不为 null 的节点将直接作为新二叉树的节点。
 *
 * 返回合并后的二叉树。
 *
 * 注意: 合并过程必须从两个树的根节点开始。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：root1 = [1,3,2,5], root2 = [2,1,3,null,4,null,7]
 * 输出：[3,4,5,5,4,null,7]
 * 示例 2：
 *
 * 输入：root1 = [1], root2 = [1,2]
 * 输出：[2,2]
 *
 *
 * 提示：
 *
 * 两棵树中的节点数目在范围 [0, 2000] 内
 * -104 <= Node.val <= 104
 */
public class _617_MergeTwoBinaryTrees {

    /**
     * 可以使用深度优先搜索合并两个二叉树。从根节点开始同时遍历两个二叉树，并将对应的节点进行合并。
     *
     * 两个二叉树的对应节点可能存在以下三种情况，对于每种情况使用不同的合并方式。
     *
     * 如果两个二叉树的对应节点都为空，则合并后的二叉树的对应节点也为空；
     *
     * 如果两个二叉树的对应节点只有一个为空，则合并后的二叉树的对应节点为其中的非空节点；
     *
     * 如果两个二叉树的对应节点都不为空，则合并后的二叉树的对应节点的值为两个二叉树的对应节点的值之和，此时需要显性合并两个节点。
     *
     * 对一个节点进行合并之后，还要对该节点的左右子树分别进行合并。这是一个递归的过程。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/merge-two-binary-trees/solution/he-bing-er-cha-shu-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root1
     * @param root2
     * @return
     */
    public TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
        // 两个都为空，返回 空
        // 一个为空。返回另一个
        if (root1 == null) {
            return root2;
        } else if (root2 == null) {
            return root1;
        }
        // 两个root都不为null
        // 新节点值 = 两个节点值的 和
        TreeNode root = new TreeNode(root1.val + root2.val);
        // 新的左孩子等于两个数的左孩子合并
        root.left = mergeTrees(root1.left, root2.left);
        // 新的左孩子等于两个数的左孩子合并
        root.right = mergeTrees(root1.right, root2.right);
        // 返回新的根节点
        return root;
    }
}
