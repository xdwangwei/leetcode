package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2021/11/13 10:44
 *
 * 给定一个二叉搜索树的根节点 root ，和一个整数 k ，请你设计一个算法查找其中第k个最小元素（从 1 开始计数）。
 *
 *
 * 示例 1：
 *
 *
 * 输入：root = [3,1,4,null,2], k = 1
 * 输出：1
 * 示例 2：
 *
 *
 * 输入：root = [5,3,6,2,4,null,null,1], k = 3
 * 输出：3
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/kth-smallest-element-in-a-bst
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _230_KthSmallestElementInBST {

    /**
     * 如果一个序列是自增有序，那么我们可以直接返回第k个元素
     * BST的中序遍历结果就是一个有序序列，我们可以先得到中序遍历结果，再返回第k个元素
     * 也可以，在中序遍历的过程中，判断当前节点是不是在整棵树中排行第k，如果是就返回
     *  我们知道 node 的左孩子一定是都比自己小，右孩子一定都比自己大，所以如果我知道左子树的节点个数，那么是不是就知道我排名第几了。
     *  所以维护一个变量来保存当前根节点的左子树节点数目
     *
     * 按照这个思路，也可以利用栈，先把自己入栈，再入自己左孩子，再入左孩子的左孩子，。。。这条路径结束，每次弹栈，k--.如果k==0，就返回当前点。
     * 然后 当前点 = 当前点.right，再重复上述过程，
     *
     * 实际上是同一种思路，就是对于每个节点，都已经得到了它左边整个子树的节点个数，那么也就知道它排名第几
     * @param root
     * @param k
     * @return
     */
    int res = 0;
    public int kthSmallest(TreeNode root, int k) {
        traverse(root, k);
        return res;
    }

    /**
     * 中序遍历一棵BST，只有中序遍历才能得到有序结果，也可能基于这个有序得到第k个元素
     * 对于递归，最重要的分析出当前root应该做什么，其他的都交给递归了
     * @param root
     * @param k
     * @return
     */
    // 当前子树的左子树的节点数目
    int left = 0;
    private void traverse(TreeNode root, int k) {
        if (root == null) {
            return;
        }
        // ********遍历左子树，遍历完后就知道左子树有几个节点，也就知道当前root排名第几了
        traverse(root.left, k);
        // 每次进入traverse，left加1，但是这一行要不能写在traverse之前，虽然无论这两个位置都能达到统计出左子树节点数目的目的，
        // 但应该是先得到左子树节点数目，再加1就是自己的排名。所以应该先traverse(left), 再 left++， 再 判断left 与 k
        left++;
        // 处理 root，
        // 如果正好排名k，返回
        if (left == k) {
            res = root.val;
        }
        // *******遍历右子树
        traverse(root.right, k);
    }
}
