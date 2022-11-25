package com.tree;

import com.common.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/11/25 12:29
 * @description: _515_LargestValueInEachLayerOfBinaryTree
 *
 * 给定一棵二叉树的根节点root ，请找出该二叉树中每一层的最大值。
 *
 *
 * 示例1：
 *
 *
 *
 * 输入: root = [1,3,2,5,3,null,9]
 * 输出: [1,3,9]
 * 示例2：
 *
 * 输入: root = [1,2,3]
 * 输出: [1,3]
 *  
 *
 * 提示：
 *
 * 二叉树的节点个数的范围是 [0,104]
 * -231<= Node.val <= 231- 1
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/find-largest-value-in-each-tree-row
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _515_LargestValueInEachLayerOfBinaryTree {

    /**
     * bfs：层序遍历，遍历每一层节点的同时找到其最大值，并顺序保存当前层节点的左右孩子，用于下一层遍历
     * @param root
     * @return
     */
    public List<Integer> largestValues(TreeNode root) {
        // 保存每一层节点的最大值
        List<Integer> list = new ArrayList<>();
        // 空树，直接返回
        if (root == null) {
            return list;
        }
        // 二叉树层序遍历
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            // 当前层节点个数
            int sz = queue.size();
            // 当前层节点值最大值
            int max = Integer.MIN_VALUE;
            // 遍历当前层节点
            for (int i = 0; i < sz; ++i) {
                TreeNode cur = queue.pop();
                // 更新max
                max = Math.max(max, cur.val);
                // 左右孩子入队列
                if (cur.left != null) {
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                }
            }
            // 保存当前层节点最大值
            list.add(max);
        }
        return list;
    }

    /**
     * dfs:我们用树的「先序遍历」来进行「深度优先搜索」处理，并用 curHeight 来标记遍历到的当前节点的高度。
     * 当遍历到 curHeight 高度的节点就判断是否更新该层节点的最大值。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/find-largest-value-in-each-tree-row/solution/zai-mei-ge-shu-xing-zhong-zhao-zui-da-zh-6xbs/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public List<Integer> largestValues2(TreeNode root) {
        // 保存每一层节点的最大值
        List<Integer> list = new ArrayList<>();
        // 空树，直接返回
        if (root == null) {
            return list;
        }
        // dfs
        dfs(list, root, 0);
        return list;
    }


    /**
     * dfs：list[i]代表树中i层节点的最大值，
     * 开始时，list为空，node为树根root，level=0
     * @param list
     * @param node
     * @param curLevel
     */
    private void dfs(List<Integer> list, TreeNode node, int curLevel) {
        // list中还没有当前层节点的最大值，说明是第一次来到curLevel层节点
        // 这个条件只会在每次进入更深一层第一个节点时出发
        // 比如现在遍历过前三层部分节点，list.size()=3。当某次dfs进入第四层时，这个条件就会触发
        // 并且当第二次进入第四层时，此条件不再成立
        if (curLevel == list.size()) {
            // 直接标记当前层节点最大值为当前节点的值
            // 由于不存在跳层，所以可以直接用add方法
            list.add(node.val);
        } else {
            // 这里肯定不是进入更深一层，而是返回到了之前遍历过的层，所以 curLevel 肯定是 小于 list.size() 的，所以肯定不会发生越界问题
            // 那么用当前节点的值去更新它所在层的最大值
            list.set(curLevel, Math.max(list.get(curLevel), node.val));
        }
        // 正常dfs左右节点
        if (node.left != null) {
            dfs(list, node.left, curLevel + 1);
        }
        if (node.right != null) {
            dfs(list, node.right, curLevel + 1);
        }
    }
}
