package com.offerassult;

import com.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/11/29 13:18
 * @description: _046_BinaryTreeRightSideView
 *
 * 剑指 Offer II 046. 二叉树的右侧视图
 * 给定一个二叉树的 根节点 root，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
 *
 *
 *
 * 示例 1:
 *
 *
 *
 * 输入: [1,2,3,null,5,null,4]
 * 输出: [1,3,4]
 * 示例 2:
 *
 * 输入: [1,null,3]
 * 输出: [1,3]
 * 示例 3:
 *
 * 输入: []
 * 输出: []
 *
 *
 * 提示:
 *
 * 二叉树的节点个数的范围是 [0,100]
 * -100 <= Node.val <= 100
 *
 *
 * 注意：本题与主站 199 题相同：https://leetcode-cn.com/problems/binary-tree-right-side-view/
 */
public class _046_BinaryTreeRightSideView {


    /**
     * dfs
     * 我们对树进行深度优先搜索，在搜索过程中，我们总是先访问右子树。那么对于每一层来说，我们在这层见到的第一个结点一定是最右边的结点。
     * 这样一来，我们可以存储在每个深度访问的右边第一个结点，就可以得到最终的结果数组。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/binary-tree-right-side-view/solution/er-cha-shu-de-you-shi-tu-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @return
     */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        dfs(root, 0, list);
        return list;
    }

    /**
     * dfs：list[i]代表树中i层最右边节点
     * 开始时，list为空，node为树根root，level=0
     * @param list
     * @param node
     * @param curLevel
     */
    private void dfs(TreeNode node, int curLevel, List<Integer> list) {
        // 跳过空树
        if (node == null) {
            return;
        }
        // list中还没有当前层节点的值，说明是第一次来到curLevel层节点
        // 这个条件只会在每次进入更深一层第一个节点时出发
        // 比如现在遍历过前三层部分节点，list.size()=3。当某次dfs进入第四层时，这个条件就会触发
        // 并且当第二次进入第四层时，此条件不再成立
        if (curLevel == list.size()) {
            // 直接标记当前层节点最大值为当前节点的值
            // 由于不存在跳层，所以可以直接用add方法
            list.add(node.val);
        }
        // 先右后左进行dfs
        dfs(node.right, curLevel + 1, list);
        dfs(node.left, curLevel + 1, list);
    }
}
