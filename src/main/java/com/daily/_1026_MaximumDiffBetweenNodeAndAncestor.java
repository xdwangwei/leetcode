package com.daily;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2023/4/18 22:20
 * @description: _1026_MaximumDiffBetweenNodeAndAncestor
 *
 * 1026. 节点与其祖先之间的最大差值
 * 给定二叉树的根节点 root，找出存在于 不同 节点 A 和 B 之间的最大值 V，其中 V = |A.val - B.val|，且 A 是 B 的祖先。
 *
 * （如果 A 的任何子节点之一为 B，或者 A 的任何子节点是 B 的祖先，那么我们认为 A 是 B 的祖先）
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：root = [8,3,10,1,6,null,14,null,null,4,7,13]
 * 输出：7
 * 解释：
 * 我们有大量的节点与其祖先的差值，其中一些如下：
 * |8 - 3| = 5
 * |3 - 7| = 4
 * |8 - 1| = 7
 * |10 - 13| = 3
 * 在所有可能的差值中，最大值 7 由 |8 - 1| = 7 得出。
 * 示例 2：
 *
 *
 * 输入：root = [1,null,2,null,0,3]
 * 输出：3
 *
 *
 * 提示：
 *
 * 树中的节点数在 2 到 5000 之间。
 * 0 <= Node.val <= 105
 * 通过次数15,206提交次数21,895
 */
public class _1026_MaximumDiffBetweenNodeAndAncestor {

    private int ans;

    /**
     * 方法一：DFS
     *
     * 对于每个节点，求其与祖先节点的最大差值，我们只需要求出该节点与祖先节点最大值和最小值的差值，取所有差值的最大值即可。
     *
     * 因此，我们设计一个函数 dfs(root,mi,mx)，表示当前搜索到的节点为 root，其祖先节点的最大值为 mx，最小值为 mi，
     *
     * 函数内更新最大差值 ans。
     *
     * 函数 dfs(root,mi,mx) 的逻辑如下：
     *
     *      若 root 为空，直接返回。
     *      否则，我们更新 ans = max(ans, abs(mi−root.val), abs(mx−root.val))。
     *      然后更新 mi = min(mi,root.val), mx=max(mx,root.val)，并且递归搜索左右子树。
     *
     * 在主函数中，我们调用 dfs(root,root.val,root.val)，最后返回 ans 即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/maximum-difference-between-node-and-ancestor/solution/python3javacgo-yi-ti-yi-jie-dfsqing-xi-t-qbqc/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @return
     */
    public int maxAncestorDiff(TreeNode root) {
        dfs(root, root.val, root.val);
        return 0;
    }

    /**
     * dfs(root,mi,mx)，表示当前搜索到的节点为 root，其祖先节点的最大值为 mx，最小值为 mi，
     * @param root
     * @param max
     * @param min
     */
    private void dfs(TreeNode root, int max, int min) {
        if (root == null) {
            return;
        }
        // 当前节点和其祖先节点的最大插值
        ans = Math.max(ans, Math.max(Math.abs(root.val - max), Math.abs(root.val - min)));
        // 递归遍历左右子树之前，更新 max 和 min
        max = Math.max(root.val, max);
        min = Math.min(root.val, min);
        dfs(root.left, max, min);
        dfs(root.right, max, min);
    }
}
