package com.daily;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2023/5/21 11:10
 * @description: _1080_InsufficientNodesInRootToLeafPath
 *
 * 1080. 根到叶路径上的不足节点
 * 给定一棵二叉树的根 root，请你考虑它所有 从根到叶的路径：从根到任何叶的路径。（所谓一个叶子节点，就是一个没有子节点的节点）
 *
 * 假如通过节点 node 的每种可能的 “根-叶” 路径上值的总和全都小于给定的 limit，则该节点被称之为「不足节点」，需要被删除。
 *
 * 请你删除所有不足节点，并返回生成的二叉树的根。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：root = [1,2,3,4,-99,-99,7,8,9,-99,-99,12,13,-99,14], limit = 1
 *
 * 输出：[1,2,3,4,null,null,7,8,9,null,14]
 * 示例 2：
 *
 *
 * 输入：root = [5,4,8,11,null,17,4,7,1,null,null,5,3], limit = 22
 *
 * 输出：[5,4,8,11,null,17,4,7,null,null,null,5]
 * 示例 3：
 *
 *
 * 输入：root = [5,-6,-6], limit = 0
 * 输出：[]
 *
 *
 * 提示：
 *
 * 给定的树有 1 到 5000 个节点
 * -10^5 <= node.val <= 10^5
 * -10^9 <= limit <= 10^9
 *
 *
 * 通过次数8,127提交次数15,412
 */
public class _1080_InsufficientNodesInRootToLeafPath {

    /**
     * 方法：后序遍历
     *
     * 一、思考
     * 对于一个叶子节点，要想删除它，需要满足什么条件？
     *
     * 对于一个非叶节点，要想删除它，需要满足什么条件？
     *
     * 二、解惑
     *
     * 首先，对于某个节点，要知道经过它的所有 ”根-叶子“路径节点和，那必然至少访问过一次它的全部子节点（包括叶子节点），才能知道所有路径和。
     * 既然如此，先处理它的子节点，回过头来再处理自己，是不是很合理？
     *
     * 那么 对于一个叶子节点 leaf，由于根到 leaf 的路径仅有一条，所以如果这条路径的元素和小于 limit，就删除 leaf。
     *
     * 对于一个非叶节点 node，如果 node 有一个儿子没被删除，那么 node 就不能被删除。
     * 这可以用反证法证明：假设可以把 node 删除，那么经过 node 的所有路径和都小于 limit，
     * 也就意味着经过 node 的儿子的路径和也小于 limit，说明 node 的儿子需要被删除，矛盾，所以 node 不能被删除。
     *
     * 如果 node 的儿子都被删除，说明经过 node 的所有儿子的路径和都小于 limit，由于经过node儿子的所有路径必然也都经过node，
     * 所以这等价于经过 node 的所有路径和都小于 limit，所以 node 需要被删除。
     *
     * 因此，要删除非叶节点 node，当且仅当 node 的所有儿子都被删除。
     *
     * 三、算法
     * 一个直接的想法是，添加一个递归参数 sumPath，表示从根到当前节点的路径和。
     *
     * 但为了能直接调用 sufficientSubset，还可以从 limit 中减去当前节点值。
     *
     * 如果当前节点是叶子，且此时 limit>0，说明从根到这个叶子的路径和小于 limit，那么删除这个叶子。
     *
     * 如果当前节点不是叶子，那么往下递归，
     * 更新它的左儿子为对左儿子调用 sufficientSubset 的结果，
     * 更新它的右儿子为对右儿子调用 sufficientSubset 的结果。
     *
     * 如果左右儿子都为空，那么就删除当前节点，返回空；否则不删，返回当前节点。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/insufficient-nodes-in-root-to-leaf-paths/solution/jian-ji-xie-fa-diao-yong-zi-shen-pythonj-64lf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public TreeNode sufficientSubset(TreeNode root, int limit) {
        // 空树直接返回
        if (root == null) {
            return null;
        }
        // 本来是计算从根到当前节点的元素和 和 limit 的关系，转变为 从根开始，从limit中扣除每个元素，比较 limit 和 0 的关系
        limit -= root.val;
        // 对于叶子节点，已经没有向下的节点了
        if (root.left == null && root.right == null) {
            // 说明节点值没有凑够limit，那么需要移除
            if (limit > 0) {
                return null;
            }
            // 否则保留
            return root;
        }
        // 先递归把左右子树处理完，记得接收返回值
        root.left = sufficientSubset(root.left, limit);
        root.right = sufficientSubset(root.right, limit);
        // 对于非叶子节点，如果左右子树都删除了，那么自己也需要被删除；否则只要有一个孩子没被删除，那么自己也不能被删除
        return root.left == null && root.right == null ? null : root;
    }
}
