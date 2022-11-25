package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2022/11/25 12:59
 * @description: _513_FindBottomLeftTreeValue
 *
 * 513. 二叉树最底层最左边的值
 * 给定一个二叉树的 根节点 root，请找出该二叉树的 最底层 最左边 节点的值。
 *
 * 假设二叉树中至少有一个节点。
 *
 *
 *
 * 示例 1:
 *
 *
 *
 * 输入: root = [2,1,3]
 * 输出: 1
 * 示例 2:
 *
 *
 *
 * 输入: [1,2,3,4,null,5,6,null,null,7]
 * 输出: 7
 *
 *
 * 提示:
 *
 * 二叉树的节点个数的范围是 [1,104]
 * -231 <= Node.val <= 231 - 1
 *
 *
 */
public class _513_FindBottomLeftTreeValue {

    /**
     * dfs，
     *
     * 使用 maxLevel 记录已经访问过的最大层，使用 maxLevelVal 记录已经访问过的最大层 maxLevel 层第一个节点的值；
     * 使用 curLevel 记录当前正在遍历的节点的层数
     *
     * 进入dfs时，判断 curLevel 和 maxLevel 的大小
     *      如果 curLevel > maxLevel，表示进入了更深一层，更新 maxLevel 为 curLevel，更新 maxLevelVal 为 当前节点的 val
     *      否则不做更新
     *
     * 然后 dfs 当前节点的left、right
     *
     * 在深度优先搜索时，我们先搜索当前节点的左子节点，再搜索当前节点的右子节点，
     * 那么对同一高度的所有节点，最左节点肯定是最先被遍历到的，所以 maxLevel 和 maxLevelVal 肯定只会被 更深一层第一个节点更新的
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/LwUNpT/solution/er-cha-shu-zui-di-ceng-zui-zuo-bian-de-z-0nlm/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    // 使用 maxLevel 记录已经访问过的最大层，
    private int maxLevel = -1;
    // 使用 maxLevelVal 记录已经访问过的最大层 maxLevel 层第一个节点的值；
    private int maxLevelVal = 0;

    public int findBottomLeftValue2(TreeNode root) {
        // 题目保证了树不空，所以这里未加判断
        // dfs
        dfs2(root, 0);
        // dfs结束后，maxLevelVal就是最后一层第一个节点的值
        return maxLevelVal;
    }


    /**
     * dfs：得到 树中最后一层第一个节点的值
     * 开始时，node为树根root，level=0
     * @param node
     * @param curLevel
     */
    private void dfs2(TreeNode node, int curLevel) {
        // 这个条件只会在每次进入【更深一层】【第一个】节点时出发
        if (curLevel > maxLevel) {
            // 直接标记当前层节点最大值为当前节点的值
            // 由于不存在跳层，所以可以直接用add方法
            maxLevel = curLevel;
            maxLevelVal = node.val;
        }
        // 正常dfs左右节点
        if (node.left != null) {
            dfs2(node.left, curLevel + 1);
        }
        if (node.right != null) {
            dfs2(node.right, curLevel + 1);
        }
    }
}
