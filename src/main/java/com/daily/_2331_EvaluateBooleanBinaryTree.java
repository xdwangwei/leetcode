package com.daily;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2023/2/7 14:10
 * @description: _2331_EvaluateBooleanBinaryTree
 *
 * 2331. 计算布尔二叉树的值
 * 给你一棵 完整二叉树 的根，这棵树有以下特征：
 *
 * 叶子节点 要么值为 0 要么值为 1 ，其中 0 表示 False ，1 表示 True 。
 * 非叶子节点 要么值为 2 要么值为 3 ，其中 2 表示逻辑或 OR ，3 表示逻辑与 AND 。
 * 计算 一个节点的值方式如下：
 *
 * 如果节点是个叶子节点，那么节点的 值 为它本身，即 True 或者 False 。
 * 否则，计算 两个孩子的节点值，然后将该节点的运算符对两个孩子值进行 运算 。
 * 返回根节点 root 的布尔运算值。
 *
 * 完整二叉树 是每个节点有 0 个或者 2 个孩子的二叉树。
 *
 * 叶子节点 是没有孩子的节点。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：root = [2,1,3,null,null,0,1]
 * 输出：true
 * 解释：上图展示了计算过程。
 * AND 与运算节点的值为 False AND True = False 。
 * OR 运算节点的值为 True OR False = True 。
 * 根节点的值为 True ，所以我们返回 true 。
 * 示例 2：
 *
 * 输入：root = [0]
 * 输出：false
 * 解释：根节点是叶子节点，且值为 false，所以我们返回 false 。
 *
 *
 * 提示：
 *
 * 树中节点数目在 [1, 1000] 之间。
 * 0 <= Node.val <= 3
 * 每个节点的孩子数为 0 或 2 。
 * 叶子节点的值为 0 或 1 。
 * 非叶子节点的值为 2 或 3 。
 * 通过次数29,078提交次数34,256
 */
public class _2331_EvaluateBooleanBinaryTree {

    /**
     * 方法一：递归
     *
     * 我们可以使用递归的方式来求解本题。
     *
     * 对于当前节点 root：
     *
     * 如果是叶子节点，此时判断其值是否为 1，如果是，则返回 true，否则返回 false。
     * 否则，对其左右孩子分别递归求解，得到其左右孩子的值 l 和 r。
     * 然后根据当前节点值的不同，分别进行如下操作：
     * 如果当前节点值为 2，则返回 l or r。
     * 如果当前节点值为 3，则返回 l && r。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/evaluate-boolean-binary-tree/solution/python3javacgo-yi-ti-yi-jie-di-gui-by-lc-g9b0/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @return
     */
    public boolean evaluateTree(TreeNode root) {
        // 叶子节点，题目已告知，完整二叉树 是每个节点有 0 个或者 2 个孩子的二叉树。
        if (root.left == null) {
            // 判断其值是否为1即可
            return root.val == 1;
        }
        // 非叶子节点，递归计算左右子树值l和r
        // 当前节点值为2，返回 l || r
        if (root.val == 2) {
            return evaluateTree(root.left) || evaluateTree(root.right);
        }
        // 当前节点值为3，返回 l && r
        return evaluateTree(root.left) && evaluateTree(root.right);
    }
}
