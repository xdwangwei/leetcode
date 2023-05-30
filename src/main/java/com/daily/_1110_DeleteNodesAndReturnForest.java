package com.daily;

import com.common.TreeNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/5/30 21:12
 * @description: _1110_DeleteNodesAndReturnForest
 *
 * 1110. 删点成林
 * 给出二叉树的根节点 root，树上每个节点都有一个不同的值。
 *
 * 如果节点值在 to_delete 中出现，我们就把该节点从树上删去，最后得到一个森林（一些不相交的树构成的集合）。
 *
 * 返回森林中的每棵树。你可以按任意顺序组织答案。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：root = [1,2,3,4,5,6,7], to_delete = [3,5]
 * 输出：[[1,2,null,4],[6],[7]]
 * 示例 2：
 *
 * 输入：root = [1,2,4,null,3], to_delete = [3]
 * 输出：[[1,2,4]]
 *
 *
 * 提示：
 *
 * 树中的节点数最大为 1000。
 * 每个节点都有一个介于 1 到 1000 之间的值，且各不相同。
 * to_delete.length <= 1000
 * to_delete 包含一些从 1 到 1000、各不相同的值。
 */
public class _1110_DeleteNodesAndReturnForest {

    /**
     * 方法：后序遍历 + 哈希表
     *
     * 当一个节点需要被删除时，它的左子树和右子树就会成为孤立的树（如果非空），需要加入待返回的集合
     *
     * 如果采取自顶向下的遍历，要删除某个节点，在将其左右子树（非空）加入结果集合之前，还得递归去其左右子树中进行相同操作。
     * 因此，采用后序遍历，这样当“归”到某个节点时，其左右子树已经是被处理过的子树，可以直接加入结果集合。具体如下：
     *
     * 我们先用哈希表或者一个长度为 1001 的数组 s 记录所有需要删除的节点。
     *
     * 接下来，设计一个函数 dfs(root)，它会返回以 root 为根的子树中，删除所有需要删除的节点后的树的根节点。
     *
     * 函数 dfs(root) 的执行逻辑如下：
     *
     * 如果 root 为空，那么我们返回空；
     * 否则，我们递归执行 dfs(root.left) 和 dfs(root.right)，并将返回值分别赋给 root.left 和 root.right。
     * 如果 root 不需要被删除，那么我们返回 root；
     * 如果 root 需要被删除，那么我们分别判断 root.left 和 root.right 是否为空，如果它们不为空，那么我们将它们加入答案数组中；最后返回空。
     *
     * 在主函数中，我们调用 dfs(root)，如果结果不为空，说明根节点不需要被删除，我们再将根节点加入答案数组中。最后返回答案数组即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/delete-nodes-and-return-forest/solution/python3javacgotypescript-yi-ti-yi-jie-df-jzkp/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    // 保存森林中每棵树的根
    private final List<TreeNode> ans = new ArrayList<>();
    // 所有需要删除的节点
    private final Set<Integer> set = new HashSet<>();

    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        for (int x : to_delete) {
            set.add(x);
        }
        // dfs
        // dfs结束后 root 不为空，说明 root 不用被删除，那这个树（删除完所有节点后留下来的）也要加入结果集
        if (dfs(root) != null) {
            ans.add(root);
        }
        // 返回
        return ans;
    }

    /**
     * 返回以 root 为根的子树中，删除所有需要删除的节点后的树的根节点。
     * @param root
     * @return
     */
    private TreeNode dfs(TreeNode root) {
        if (root == null) {
            return null;
        }
        // 后序遍历，递归处理完左右子树，并接收返回值
        root.left = dfs(root.left);
        root.right = dfs(root.right);
        // 此节点不需要被删除，不会产生 孤立树
        if (!set.contains(root.val)) {
            return root;
        }
        // 需要被删除。左右子树会成为孤立树，如果非空，则需要加入结果集
        if (root.left != null) {
            ans.add(root.left);
        }
        if (root.right != null) {
            ans.add(root.right);
        }
        // 节点被删除了，以它为根的子树的根就是 null
        return null;
    }

}
