package com.tree;

import com.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * 2020/4/28 19:27
 *
 * 给定一个二叉搜索树的根节点 root，返回树中任意两节点的差的最小值。
 *
 * 示例：
 *
 * 输入: root = [4,2,6,1,3,null,null]
 * 输出: 1
 * 解释:
 * 注意，root是树节点对象(TreeNode object)，而不是数组。
 *
 * 给定的树 [4,2,6,1,3,null,null] 可表示为下图:
 *
 *           4
 *         /   \
 *       2      6
 *      / \
 *     1   3
 *
 * 最小的差值是 1, 它是节点1和节点2的差值, 也是节点3和节点2的差值
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/minimum-distance-between-bst-nodes
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _783_MinimumDistanceBetweenBSTNodes {

    /**
     * BST 中序遍历的结果是一个有序数组，我们组只需要比较数组种相邻两元素的差值，返回最小的那个
     * @param root
     * @return
     */
    public int minDiffInBST(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) return 0;
        int min = Integer.MAX_VALUE;
        // 因为不知道有多少个节点，所以用列表存储
        List<Integer> nodes = new ArrayList<>();
        dfs(root, nodes);
        // 找到最小差值
        for (int i = 0; i < nodes.size() - 1; i++) {
            min = Math.min(min, Math.abs(nodes.get(i + 1) - nodes.get(i)));
        }
        return min;
    }

    private void dfs(TreeNode root, List<Integer> list) {
        if (root == null) return;
        // 左
        dfs(root.left, list);
        // 中
        list.add(root.val);
        // 右
        dfs(root.right, list);
    }

    /**
     * 既然中序遍历的结果就是有序数组，那么我们不用全部保存，只需要在遍历过程中保存一下上一个节点值，
     * 每次访问当前节点时，更新一下差值，再把当前节点值
     * 作为上一个节点值就可以了
     */
    private Integer prev = null, res = 0;

    public int minDiffInBST2(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) return 0;
        dfs2(root);
        return res;
    }

    private void dfs2(TreeNode root) {
        if (root == null) return;
        // 左
        dfs2(root.left);
        // 中,访问当前节点时更新差值
        if (prev != null)
            res = Math.min(res, Math.abs(prev - root.val));
        // 当前节点值作为上一个值
        prev = root.val;
        // 右
        dfs2(root.right);
    }
}
