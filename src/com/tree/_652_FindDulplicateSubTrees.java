package com.tree;

import com.common.TreeNode;

import java.util.*;

/**
 * @author wangwei
 * 2021/11/13 10:27
 *
 * 给定一棵二叉树，返回所有重复的子树。对于同一类的重复子树，你只需要返回其中任意一棵的根结点即可。
 *
 * 两棵树重复是指它们具有相同的结构以及相同的结点值。
 *
 * 示例 1：
 *
 *         1
 *        / \
 *       2   3
 *      /   / \
 *     4   2   4
 *        /
 *       4
 * 下面是两个重复的子树：
 *
 *       2
 *      /
 *     4
 * 和
 *
 *     4
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-duplicate-subtrees
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _652_FindDulplicateSubTrees {

    /**
     * 所谓重复就是 结构相同，节点数据相同。那么如果我得到它的前中后序遍历结果都应该相同
     * 这里选择，将树转为字符串形式，
     * @param root
     * @return
     */
    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        // 保存结果
        List<TreeNode> res = new LinkedList<>();
        if (root == null) {
            return res;
        }
        // 统计每个子树的字符串形式及其出现次数的对应关系
        HashMap<String, Integer> treeMap = new HashMap<>();
        traverse(root, treeMap, res);
        return res;
    }

    /**
     * 返回一棵树序列化成字符串的结果
     * @param root
     * @param treeMap
     * @param res
     * @return
     */
    private String traverse(TreeNode root, HashMap<String, Integer> treeMap, List<TreeNode> res) {
        // 用 # 代表 null
        if (root == null) {
            return "#";
        }
        // 得到左子树字符串
        String left = traverse(root.left, treeMap, res);
        // 得到右子树字符串
        String right = traverse(root.right, treeMap, res);
        // 得到当前树的字符串，这里建议先序或后序顺序拼接，我们知道中序结果相同是不能区分两颗树的
        String treeStr = left + "," + right + "," + root.val;
        // 得到当前树串的出现次数
        Integer count = treeMap.getOrDefault(treeStr, 0);
        // 已存在某个子树，和当前子树结构相同，那么加入结果集，不重复添加
        if (count == 1) {
            res.add(root);
        }
        // 记录串的出现次数
        treeMap.put(treeStr, count + 1);
        // 返回当前子树的串
        return treeStr;

    }
}
