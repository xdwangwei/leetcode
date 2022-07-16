package com.hot100;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2022/5/4 16:31
 * @description: _543_DiameterOfBinaryTree
 *
 * 543. 二叉树的直径
 * 给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过也可能不穿过根结点。
 *
 *
 *
 * 示例 :
 * 给定二叉树
 *
 *           1
 *          / \
 *         2   3
 *        / \
 *       4   5
 * 返回 3, 它的长度是路径 [4,2,1,3] 或者 [5,2,1,3]。
 *
 *
 *
 * 注意：两结点之间的路径长度是以它们之间边的数目表示。
 */
public class _543_DiameterOfBinaryTree {

    private int res;


    /**
     * 首先我们知道一条路径的长度为该路径经过的节点数减一，所以求直径（即求路径长度的最大值）等效于求路径经过节点数的最大值减一。
     *
     * 而任意一条路径均可以被看作由某个节点为中点，从其左儿子和右儿子向下遍历的路径拼接得到。
     * 所以在这里就体现出后续遍历的特点
     *
     * 假设当前节点为root，如果它的左孩子为L，右孩子为R，我们需要知道从L到叶子节点的所有路径上经过的最多的节点数，也就是 L的深度
     * 同理，得到R的深度，那么再通过root连接，就是当前得到的最长路径，这条路径上的边数应该是节点个数减1
     * L的深度是left，相当于从L到叶子节点经过left个节点，R深度是right，相当于从R到叶子节点经过right个接待你
     * 那么再加上中间节点root，总节点个数就是 left + right + 1， 再减去1， 就是 left + right
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/diameter-of-binary-tree/solution/er-cha-shu-de-zhi-jing-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @return
     */
    public int diameterOfBinaryTree(TreeNode root) {
        depth(root);
        return res;
    }

    /**
     * 后续遍历，计算节点root的深度，在此过程中更新能够得到的全部路径的最长路径长度
     * @param root
     * @return
     */
    private int depth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 左节点深度（到null经过的节点个数）
        int left = depth(root.left);
        // 右节点深度（到null经过的节点个数）
        int right = depth(root.right);
        // 若以当前root为连接点，节点个数为 left + right + 1, 当前路径长度是 节点个数 - 1， 也就是 left + right
        res = Math.max(res, left + right);
        // 返回值是root的深度，也就是 左右孩子深度的更大值+1
        return 1 + Math.max(left, right);
    }
}
