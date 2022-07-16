package com.tree;

import com.common.TreeNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangwei
 * 2021/11/9 17:13
 *
 * 95. 不同的二叉搜索树 II
 * 给你一个整数 n ，请你生成并返回所有由 n 个节点组成且节点值从 1 到 n 互不相同的不同 二叉搜索树 。可以按 任意顺序 返回答案。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：n = 3
 * 输出：[[1,null,2,null,3],[1,null,3,2],[2,1,3],[3,1,null,null,2],[3,2,null,1]]
 * 示例 2：
 *
 * 输入：n = 1
 * 输出：[[1]]
 */
public class _95_DifferentBST2 {

    /**
     * 递归
     * 回溯法没有考虑到二叉搜索树的特性，实际上加入选择i作为树根，那么它的左子树的所有点肯定只有比i小的那些数字，右子树肯定是比i大的那些数字
     * @param n
     * @return
     */
    public List<TreeNode> generateTrees2(int n) {

        if (n == 0) {
            return null;
        }

        return generateTreeList(1, n);

    }

    /**
     * 递归
     * 返回 [start, end] 所有正整数组成的所有二叉搜索树
     * @param start
     * @param end
     * @return
     */
    private List<TreeNode> generateTreeList(int start, int end) {
        List<TreeNode> res = new LinkedList<>();
        // 不直接返回null是因为之后的代码会进行for遍历
        if (end < start) {
            res.add(null);
            return res;
        }
        // 只有一个节点
        if (start == end) {
            res.add(new TreeNode(start));
            return res;
        }
        // 选择这个点作为树根，
        for (int i = start; i <= end; i++) {
            // 获得所有可行的左子树集合
            List<TreeNode> leftTrees = generateTreeList(start, i - 1);

            // 获得所有可行的右子树集合
            List<TreeNode> rightTrees = generateTreeList(i + 1, end);

            // 从左子树集合中选出一棵左子树，从右子树集合中选出一棵右子树，拼接到根节点上
            for (TreeNode left : leftTrees) {
                for (TreeNode right : rightTrees) {
                    // 这里不能公用root，否则下次重组会导致之前的树改变
                    TreeNode currTree = new TreeNode(i);
                    currTree.left = left;
                    currTree.right = right;
                    res.add(currTree);
                }
            }
        }
        return res;
    }

    /**
     * 回溯
     * @param n
     * @return
     */
    public List<TreeNode> generateTrees(int n) {

        if (n == 0) {
            return null;
        }

        // 只有一个节点
        if (n == 1) {
            return Arrays.asList(new TreeNode(1));
        }

        // 保存所有符合要求的树根
        List<TreeNode> res = new LinkedList<>();

        // 保存所有符合要求的树对应的字符串
        HashSet<String> treeSet = new HashSet<>();

        // 标记所有节点是否已加入当前树，默认都是0
        int[] visited = new int[n + 1];

        // 每次选择不同点作为树根
        for (int i = 1; i <= n; i++) {
            // 选择这个点作为树根，标记这个点已加入这颗树，还要找n-1个节点
            visited[i] = 1;
            // 开始构造整棵树
            backTrack(res, treeSet, new TreeNode(i), n - 1, n, visited);
            // 以当前点为根的树已找完，恢复visited
            visited[i] = 0;
        }

        return res;

    }

    /**
     * 回溯
     * @param res 所有符合要求的树
     * @param treeSet 所有符合要求的树对应的字符串，防止重复
     * @param root 树根
     * @param target 还需要几个节点
     * @param n 已拥有几个节点
     * @param visited 各个节点是否已访问过
     */
    private void backTrack(List<TreeNode> res, HashSet<String> treeSet, TreeNode root, int target, int n, int[] visited) {
        // 当前树已拥有 n 个节点，是一颗完整的树
        if (target == 0) {
            // 得到当前树对应的字符串
            String str = treeToString(root);
            // 这棵树不重复
            if (!treeSet.contains(str)) {
                // 添加对应树字符串
                treeSet.add(str);
                // 添加这颗树，这里注意要重拷贝一棵树，因为回溯出去会撤销选择也就是会修改这棵树的结构
                res.add(treeCopy(root));
            }
            return;
        }
        // 可选择的节点列表
        for (int i = 1; i <= n; i++) {
            // 此节点已加入当前树，跳过
            if (visited[i] == 1) {
                continue;
            }
            // 寻找这个点在当前树中的可插入位置
            TreeNode cur = root;
            while (cur != null) {
                if (i > cur.val) {
                    // 找到合适位置
                    if (cur.right == null) {
                        // 加入到这颗树
                        cur.right = new TreeNode(i);
                        // 标记此节点已加入
                        visited[i] = 1;
                        // 进入下一层
                        backTrack(res, treeSet, root, target - 1, n, visited);
                        // 撤销选择，恢复树结构和visited数组
                        visited[i] = 0;
                        cur.right = null;
                        break;
                    }
                    cur = cur.right;
                } else {
                    // 找到合适位置
                    if (cur.left == null) {
                        // 加入树中，做选择
                        cur.left = new TreeNode(i);
                        // 标记此节点已加入
                        visited[i] = 1;
                        // 进入下一层
                        backTrack(res, treeSet, root, target - 1, n, visited);
                        // 撤销选择
                        visited[i] = 0;
                        cur.left = null;
                        break;
                    }
                    cur = cur.left;
                }
            }
        }
    }

    /**
     * 拷贝一棵树
     * @param root
     * @return
     */
    private TreeNode treeCopy(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode newRoot = new TreeNode(root.val);
        newRoot.left = treeCopy(root.left);
        newRoot.right = treeCopy(root.right);
        return newRoot;
    }


    /**
     * 得到一棵树先序遍历顺序组成的字符串
     * @param root
     * @return
     */
    private String treeToString(TreeNode root) {
        if (root == null) {
            return "";
        }
        return root.val + treeToString(root.left) + treeToString(root.right);
    }
}
