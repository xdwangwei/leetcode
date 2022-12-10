package com.tree;

import com.common.TreeNode;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/12/10 14:35
 * @description: _653_TwoSumInBinarySearchTree
 *
 * 653. 二叉搜索树中两个节点之和
 * 给定一个二叉搜索树的 根节点 root 和一个整数 k , 请判断该二叉搜索树中是否存在两个节点它们的值之和等于 k 。假设二叉搜索树中节点的值均唯一。
 *
 *
 *
 * 示例 1：
 *
 * 输入: root = [8,6,10,5,7,9,11], k = 12
 * 输出: true
 * 解释: 节点 5 和节点 7 之和等于 12
 * 示例 2：
 *
 * 输入: root = [8,6,10,5,7,9,11], k = 22
 * 输出: false
 * 解释: 不存在两个节点值之和为 22 的节点
 *
 *
 * 提示：
 *
 * 二叉树的节点个数的范围是  [1, 104].
 * -104 <= Node.val <= 104
 * root 为二叉搜索树
 * -105 <= k <= 105
 *
 */
public class _653_TwoSumInBinarySearchTree {

    /**
     * 方法一：深度优先搜索 + 哈希表
     * 思路和算法
     *
     * 我们可以使用深度优先搜索的方式遍历整棵树，用哈希表记录遍历过的节点的值。
     *
     * 对于一个值为 x 的节点，我们检查哈希表中是否存在 k−x 即可。
     * 如果存在对应的元素，那么我们就可以在该树上找到两个节点的和为 k；
     * 否则，我们将 x 放入到哈希表中。并递归搜索左右子树
     *
     * 如果遍历完整棵树都不存在对应的元素，那么该树上不存在两个和为 k 的节点。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/opLdQZ/solution/er-cha-sou-suo-shu-zhong-liang-ge-jie-di-bqci/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @return
     */
    // 用哈希表记录遍历过的节点的值。
    Set<Integer> set = new HashSet<>();

    public boolean findTarget(TreeNode root, int k) {
        if (root == null) {
            return false;
        }
        // 对于一个值为 x 的节点，我们检查哈希表中是否存在 k−x 即可。
        // 如果存在对应的元素，那么我们就可以在该树上找到两个节点的和为 k；
        if (set.contains(k - root.val)) {
            return true;
        }
        // 否则，我们将 x 放入到哈希表中。
        set.add(root.val);
        // 递归搜索左右子树
        return findTarget(root.left, k) || findTarget(root.right, k);
    }

    /**
     * 方法二：广度优先搜索 + 哈希表
     * 思路和算法
     *
     * 我们可以使用广度优先搜索的方式遍历整棵树，用哈希表记录遍历过的节点的值。
     *
     * 具体地，我们首先创建一个哈希表和一个队列，将根节点加入队列中，然后执行以下步骤：
     *
     * 从队列中取出队头，假设其值为 x；
     * 检查哈希表中是否存在 k−x，如果存在，返回 True；
     * 否则，将该节点的左右的非空子节点加入队尾；
     * 重复以上步骤，直到队列为空；
     * 如果队列为空，说明树上不存在两个和为 kk 的节点，返回 False。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/opLdQZ/solution/er-cha-sou-suo-shu-zhong-liang-ge-jie-di-bqci/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @param k
     * @return
     */
    public boolean findTarget2(TreeNode root, int k) {
        // 二叉树的广度优先搜索
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            // 对于一个值为 x 的节点，我们检查哈希表中是否存在 k−x 即可。
            // 如果存在对应的元素，那么我们就可以在该树上找到两个节点的和为 k；
            if (set.contains(k - root.val)) {
                return true;
            }
            // 否则，我们将 x 放入到哈希表中。
            set.add(root.val);
            // 继续搜索左右子树
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return false;
    }


    /**
     * 方法三：深度优先搜索 + 中序遍历 + 双指针
     * 思路和算法
     *
     * 注意到二叉搜索树的中序遍历是升序排列的，我们可以将该二叉搜索树的中序遍历的结果记录下来，得到一个升序数组。
     *
     * 这样该问题即转化为「167. 两数之和 II - 输入有序数组」。我们可以使用双指针解决它。
     *
     * 具体地，我们使用两个指针分别指向数组的头尾，当两个指针指向的元素之和小于 k 时，让左指针右移；
     * 当两个指针指向的元素之和大于 k 时，让右指针左移；当两个指针指向的元素之和等于 k 时，返回 True。
     *
     * 最终，当左指针和右指针重合时，树上不存在两个和为 k 的节点，返回 False。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/opLdQZ/solution/er-cha-sou-suo-shu-zhong-liang-ge-jie-di-bqci/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param node
     */
    // 保存二叉搜索树中序遍历结果
    List<Integer> list = new ArrayList<>();

    public boolean findTarget3(TreeNode root, int k) {
        // 得到中序遍历序列
        inorderTraversal(root);
        // 在有序数组中进行twosum，双指针
        int left = 0, right = list.size() - 1;
        while (left < right) {
            // 找到
            if (list.get(left) + list.get(right) == k) {
                return true;
            }
            // 左指针右移
            if (list.get(left) + list.get(right) < k) {
                left++;
            // 右指针左移
            } else {
                right--;
            }
        }
        return false;
    }

    /**
     * 中序遍历，递归
     * @param node
     */
    public void inorderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }
        inorderTraversal(node.left);
        list.add(node.val);
        inorderTraversal(node.right);
    }

}
