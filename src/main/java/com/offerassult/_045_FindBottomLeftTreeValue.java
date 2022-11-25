package com.offerassult;

import com.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/11/25 12:59
 * @description: _045_FindBottomLeftTreeValue
 *
 * 剑指 Offer II 045. 二叉树最底层最左边的值
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
 * 注意：本题与主站 513 题相同： https://leetcode-cn.com/problems/find-bottom-left-tree-value/
 */
public class _045_FindBottomLeftTreeValue {

    /**
     * 方法一：dfs，保存每一层第一个节点的值到list中，最后返回list中最后一个元素
     * @param root
     * @return
     */
    public int findBottomLeftValue(TreeNode root) {
        // 保存每一层第一个节点的值
        List<Integer> list = new ArrayList<>();
        // dfs
        dfs(list, root, 0);
        // 返回list中最后一个元素，即最后一层第一个节点的值
        return list.get(list.size() - 1);

    }


    /**
     * dfs：list[i]代表树中i层第一个节点
     * 开始时，list为空，node为树根root，level=0
     * @param list
     * @param node
     * @param curLevel
     */
    private void dfs(List<Integer> list, TreeNode node, int curLevel) {
        // list中还没有当前层节点的值，说明是第一次来到curLevel层节点
        // 这个条件只会在每次进入更深一层第一个节点时出发
        // 比如现在遍历过前三层部分节点，list.size()=3。当某次dfs进入第四层时，这个条件就会触发
        // 并且当第二次进入第四层时，此条件不再成立
        if (curLevel == list.size()) {
            // 直接标记当前层节点最大值为当前节点的值
            // 由于不存在跳层，所以可以直接用add方法
            list.add(node.val);
        }
        // 正常dfs左右节点
        if (node.left != null) {
            dfs(list, node.left, curLevel + 1);
        }
        if (node.right != null) {
            dfs(list, node.right, curLevel + 1);
        }
    }

    /**
     * 方法二：dfs，原理和方法一类似，只是省略list
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
