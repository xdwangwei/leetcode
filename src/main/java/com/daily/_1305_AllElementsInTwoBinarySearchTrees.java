package com.daily;

import com.common.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/5/1 20:21
 * @description: _1305_AllElementsInTwoBinarySearchTrees
 *
 * 1305. 两棵二叉搜索树中的所有元素
 * 给你 root1 和 root2 这两棵二叉搜索树。请你返回一个列表，其中包含 两棵树 中的所有整数并按 升序 排序。.
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：root1 = [2,1,4], root2 = [1,0,3]
 * 输出：[0,1,1,2,3,4]
 * 示例 2：
 *
 *
 *
 * 输入：root1 = [1,null,8], root2 = [8,1]
 * 输出：[1,1,8,8]
 *
 *
 * 提示：
 *
 * 每棵树的节点数在 [0, 5000] 范围内
 * -105 <= Node.val <= 105
 */
public class _1305_AllElementsInTwoBinarySearchTrees {


    /**
     * 两个都是二叉搜索树，所以通过中序遍历救能得到两个有序序列
     * 然后按照合并有序序列的规则进行合并即可
     * @param root1
     * @param root2
     * @return
     */
    public List<Integer> getAllElements(TreeNode root1, TreeNode root2) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        // 得到两个有序序列
        inOrder(root1, list1);
        inOrder(root2, list2);
        List<Integer> ans = new ArrayList<>();
        // 合并两个有序序列
        // 有效范围 -10^5 - 10^5，
        // 简便写法，给 一个 INF，注意 while 条件
        int i = 0, j = 0, len1 = list1.size(), len2 = list2.size(), INF = 500001;
        while (i < len1 || j < len2) {
            // 某个列表已扫描完就给取值INF
            int num1 = i < len1 ? list1.get(i) : INF, num2 = j < len2 ? list2.get(j) : INF;
            // 取二者中较小值
            if (num1 < num2) {
                ans.add(num1);
                i++;
            } else {
                ans.add(num2);
                j++;
            }
        }
        return ans;
    }

    /**
     * 中序遍历代码
     * @param root
     * @param list
     */
    private void inOrder(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }
        inOrder(root.left, list);
        list.add(root.val);
        inOrder(root.right, list);
    }
}
