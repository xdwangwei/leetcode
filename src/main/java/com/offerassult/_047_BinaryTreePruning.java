package com.offerassult;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2022/11/29 13:27
 * @description: _047_BinaryTreePruning
 *
 * 剑指 Offer II 047. 二叉树剪枝
 * 给定一个二叉树 根节点 root ，树的每个节点的值要么是 0，要么是 1。请剪除该二叉树中所有节点的值为 0 的子树。
 *
 * 节点 node 的子树为 node 本身，以及所有 node 的后代。
 *
 *
 *
 * 示例 1:
 *
 * 输入: [1,null,0,0,1]
 * 输出: [1,null,0,null,1]
 * 解释:
 * 只有红色节点满足条件“所有不包含 1 的子树”。
 * 右图为返回的答案。
 *
 *
 * 示例 2:
 *
 * 输入: [1,0,1,0,0,0,1]
 * 输出: [1,null,1,null,1]
 * 解释:
 *
 *
 * 示例 3:
 *
 * 输入: [1,1,0,1,1,0,1,0]
 * 输出: [1,1,0,1,1,null,1]
 * 解释:
 *
 *
 *
 *
 * 提示:
 *
 * 二叉树的节点个数的范围是 [1,200]
 * 二叉树节点的值只会是 0 或 1
 *
 *
 * 注意：本题与主站 814 题相同：https://leetcode-cn.com/problems/binary-tree-pruning/
 */
public class _047_BinaryTreePruning {


    /**
     * 递归
     * 根据题意，我们将原函数 pruneTree 作为递归函数，
     * 递归函数的含义为「 在以root为根的树中，移除所有节点值全为0的子树，并返回新树根结点」。
     *
     * 不失一般性的考虑任意节点作为入参该如何处理：
     *      若 root 为空，直接返回
     *      否则通过可以递归处理左右子树，并将新左右子树【重新赋值】给 root。
     *      若处理后的左右子树都为空树，且root节点值为0，那么整棵树都应该被移除（即此时root是个值为0的叶子节点），返回null
     *      否则返回root
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/binary-tree-pruning/solution/by-ac_oier-7me9/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @return
     */
    public TreeNode pruneTree(TreeNode root) {
        // 空树直接返回
        if (root == null) {
            return null;
        }
        // 递归处理得到左右子树，重新赋值
        root.left = pruneTree(root.left);
        root.right = pruneTree(root.right);
        // 如果此时root成了叶子节点，并且值为0，那么以它为头的这棵子树应该被移除，返回null
        if (root.left == null && root.right == null && root.val == 0) {
            return null;
        }
        // 否则，说明以root为根的子树不是全0的子树，返回root
        return root;
    }
}
