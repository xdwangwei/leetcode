package com.offerassult;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2022/12/1 10:57
 * @description: _49_SumValueOfRootToLeafNodes
 *
 * 剑指 Offer II 049. 从根节点到叶节点的路径数字之和
 * 给定一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。
 *
 * 每条从根节点到叶节点的路径都代表一个数字：
 *
 * 例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
 * 计算从根节点到叶节点生成的 所有数字之和 。
 *
 * 叶节点 是指没有子节点的节点。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：root = [1,2,3]
 * 输出：25
 * 解释：
 * 从根到叶子节点路径 1->2 代表数字 12
 * 从根到叶子节点路径 1->3 代表数字 13
 * 因此，数字总和 = 12 + 13 = 25
 * 示例 2：
 *
 *
 * 输入：root = [4,9,0,5,1]
 * 输出：1026
 * 解释：
 * 从根到叶子节点路径 4->9->5 代表数字 495
 * 从根到叶子节点路径 4->9->1 代表数字 491
 * 从根到叶子节点路径 4->0 代表数字 40
 * 因此，数字总和 = 495 + 491 + 40 = 1026
 *
 *
 * 提示：
 *
 * 树中节点的数目在范围 [1, 1000] 内
 * 0 <= Node.val <= 9
 * 树的深度不超过 10
 *
 *
 * 注意：本题与主站 129 题相同： https://leetcode-cn.com/problems/sum-root-to-leaf-numbers/
 *
 * 通过次数23,724
 */
public class _49_SumValueOfRootToLeafNodes {

    /**
     * 二叉树的每条从根节点到叶子节点的路径都代表一个数字。
     * 每个节点都对应一个数字，等于其父节点对应的数字乘以 10 再加上该节点的值（这里假设根节点的父节点对应的数字是 0）。
     * 只要计算出每个叶子节点对应的数字，然后计算所有叶子节点对应的数字之和，即可得到结果。
     *
     * 可以通过深度优先搜索和广度优先搜索实现。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/qiu-gen-dao-xie-zi-jie-dian-shu-zi-zhi-he-by-leetc/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    // 最终答案
    private int ans;

    public int sumNumbers(TreeNode root) {
        // dfs，base val=0
        dfs(root, 0);
        return ans;
    }

    /**
     * dfs，从 root 到叶子节点的路径代表的数字，curVal 代表root到当前节点父节点时的路径数字
     * @param root
     * @param curVal
     */
    private void dfs(TreeNode root, int curVal) {
        if (root == null) {
            return;
        }
        // root到当前节点值
        curVal = curVal * 10 + root.val;
        // 如果是叶子节点，累加答案
        if (root.left == null && root.right == null) {
            ans += curVal;
        }
        // dfs左右孩子
        dfs(root.left, curVal);
        dfs(root.right, curVal);
    }
}
