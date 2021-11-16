package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2021/11/14 9:18
 * <p>
 * 给你一棵以root为根的  【二叉树】，请你返回 任意【二叉搜索子树】的最大键值和。
 * <p>
 * 二叉搜索树的定义如下：
 * <p>
 * 任意节点的左子树中的键值都小于此节点的键值。
 * 任意节点的右子树中的键值都 大于此节点的键值。
 * 任意节点的左子树和右子树都是二叉搜索树。
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * <p>
 * 输入：root = [1,4,3,2,4,2,5,null,null,null,null,null,null,4,6]
 * 输出：20
 * 解释：键值为 3 的子树是和最大的二叉搜索树。
 * 示例 2：
 * <p>
 * <p>
 * <p>
 * 输入：root = [4,3,null,1,2]
 * 输出：2
 * 解释：键值为 2 的单节点子树是和最大的二叉搜索树。
 * 示例 3：
 * <p>
 * 输入：root = [-4,-2,-5]
 * 输出：0
 * 解释：所有节点键值都为负数，和最大的二叉搜索树为空。
 * 示例 4：
 * <p>
 * 输入：root = [2,1,3]
 * 输出：6
 * 示例 5：
 * <p>
 * 输入：root = [5,4,8,3,null,6,3]
 * 输出：7
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximum-sum-bst-in-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1373_MaximumSumBSTInBinaryTree {

    /**
     * 二叉树相关题目最核心的思路是明确当前节点需要做的事情是什么。剩下的交给遍历框架(递归)
     *
     * 如果当前节点要做的事情需要通过左右子树的计算结果推导出来，就要用到【后序】遍历。
     *
     * 那么我们想计算子树中 BST 的最大和，站在当前节点的视角，需要做什么呢？
     *
     * 1、我肯定得知道左右子树是不是合法的 BST，如果这俩儿子有一个不是 BST，以我为根的这棵树肯定不会是 BST，对吧。
     * 2、如果左右子树都是合法的 BST，我得瞅瞅左右子树加上自己还是不是合法的 BST 了。因为按照 BST 的定义，当前节点的值应该大于左子树的最大值，小于右子树的最小值，否则就破坏了 BST 的性质。
     * 3、因为题目要计算最大的节点之和，如果左右子树加上我自己还是一棵合法的 BST，也就是说以我为根的整棵树是一棵 BST，
     *      那我需要知道我们这棵 BST 的所有节点值之和是多少，方便和别的 BST 争个高下，对吧。
     *
     * 根据以上三点，站在当前节点的视角，需要知道以下具体信息：
     *
     * 1、左右子树是否是 BST。
     * 2、左子树的最大值和右子树的最小值。
     * 3、左右子树的节点值之和。
     *
     * 只有知道了这几个值，我们才能满足题目的要求，后面我们会想方设法计算这些值。
     *
     * 那么，对于每一个子树。我是否有必要写四个辅助函数findMax, findMin, getSum, isBST，分别判断是否是BST，求最小值，最大值，总和 ？
     * 如果需要：这几个辅助函数都是递归函数，都要遍历输入的二叉树，外加 traverse 函数本身的递归，可以说是递归上加递归，所以这个解法的复杂度是非常高的。
     *
     * 能否将这四部分的逻辑也在 traverse 函数中完成 ？？ 可以 ！！！
     *
     * 你计算以 root 为根的二叉树的节点之和，是不是可以通过左右子树的和加上 root.val 计算出来？
     *
     * 你计算以 root 为根的二叉树的最大值/最小值，是不是可以通过左右子树的最大值/最小值和 root.val 比较出来？
     *
     * 你判断以 root 为根的二叉树是不是 BST，是不是得先判断左右子树是不是 BST？是不是还得看看左右子树的最大值和最小值？
     *
     * 那么，当前节点要做的事情需要通过左右子树的计算结果推导出来，所以选择使用【后序】遍历。
     *
     * traverse函数返回一个数组 int[4] {是否是BST, 最小值， 最大值， 总和}
     */

    // 返回值
    int maxSum = 0;
    public int maxSumBST(TreeNode root) {
        if (root == null) {
            return 0;
        }
        traverse(root);
        return maxSum;
    }

    /**
     * 返回一个数组 int[4] {是否是BST, 最小值， 最大值， 总和}
     * @param root
     * @return
     */
    private int[] traverse(TreeNode root) {
        // 空节点，认为是BST，这样才能保证每个单个的叶子节点也能判断成为True
        // 这里为什么最小值的部分是maxvalue，最大值部分是minvalue呢？
        // 因为 叶子节点在判断自己是不是BST的时候，要满足：左右子树都是BST , minRight > val > maxLeft
        // 叶子节点左右都是 null。 所以返回的时候必须让 minRight = maxvalue, maxLeft = minvalue
        if (root == null) {
            return new int[]{1, Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
        }

        // *********** 后序遍历逻辑
        // 得到左右子树的res{是否是BST, 最小值， 最大值， 总和}
        int[] left = traverse(root.left);
        int[] right = traverse(root.right);

        // *********** 根节点逻辑

        // 得到当前根代表的子树的res
        int[] res = new int[4];
        if (left[0] == 1 && right[0] == 1 && left[2] < root.val && root.val < right[1]) {
            // 以 root 为根的二叉树是 BST
            res[0] = 1;
            // 计算以 root 为根的这棵 BST 的最小值，注意 ： root 肯定比 leftMax 大了。按道理说组合之后这颗树的最小值依然还是leftMin。
            //    但正是由于我们每次进if都只使用了 maxLeft 和 minRight。导致 minLeft 和 maxRight 一直没有更新，他们初始时分别是 MAX_VALUE, MIN_VALUE
            //    为了下一次递归时用到的数据是对的，必须更新这一次的返回值
            //    不能写， res[1] = left[1]， res[2] = right[2], 这样的话， min 和 max 一直就没变过，分别是 MAX_VALUE, MIN_VALUE，
            //    【下一次递归你不确定当前子树是作为左子树还是右子树去和新的根做判断】，就可能导致判断错误。所以这两个值必须要更新
            // 重要！！！！！！！
            res[1] = Math.min(left[1], root.val);
            // 计算以 root 为根的这棵 BST 的最大值
            res[2] = Math.max(right[2], root.val);

            // 计算以 root 为根的这棵 BST 所有节点之和
            res[3] = left[3] + right[3] + root.val;
            // 更新全局变量
            maxSum = Math.max(maxSum, res[3]);
        }
        // 否则就不是一棵BST，根本无作用，返回值默认是 [0,0,0,0] 不用担心初始化问题
        return res;
    }
}
