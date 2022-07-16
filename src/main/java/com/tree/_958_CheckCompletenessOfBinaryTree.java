package com.tree;

import com.common.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangwei
 * 2020/4/27 22:00
 * <p>
 * 给定一个二叉树，确定它是否是一个完全二叉树。
 * <p>
 * 百度百科中对完全二叉树的定义如下：
 * <p>
 * 若设二叉树的深度为 h，除第 h 层外，其它各层 (1～h-1) 的结点数都达到最大个数，第 h 层所有的结点都连续集中在最左边，这就是完全二叉树。（注：第 h 层可能包含 1~ 2h 个节点。）
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * <p>
 * 输入：[1,2,3,4,5,6]
 * 输出：true
 * 解释：最后一层前的每一层都是满的（即，结点值为 {1} 和 {2,3} 的两层），且最后一层中的所有结点（{4,5,6}）都尽可能地向左。
 * 示例 2：
 * <p>
 * <p>
 * <p>
 * 输入：[1,2,3,4,5,null,7]
 * 输出：false
 * 解释：值为 7 的结点没有尽可能靠向左侧
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/check-completeness-of-a-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _958_CheckCompletenessOfBinaryTree {

    /**
     * 层序遍历，对于每一层进行判断
     * 当前节点之前是否出现null，如果出现，说明它不满足靠左
     * <p>
     *             1
     *         2        3
     *       4        5   6
     *
     * @param root
     * @return
     */

    public boolean isCompleteTree(TreeNode root) {
        if (root == null) return true;
        boolean flag = false;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            // 当前层有size个节点
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (node == null)
                    // 当前节点为null并不能返回(就算是完整的完全二叉树，遍历到最后一个叶子节点加入两个空孩子，这个条件也能成立)
                    // 只能设置一个标志，null一定只能在最后出现
                    flag = true;
                else {
                    // 只有在当前节点之前出现了null，才能说明它不符合完全二叉树
                    if (flag) return false;
                    // 加入左孩子
                    queue.add(node.left);
                    // 加入右孩子
                    queue.add(node.right);
                }
            }
        }
        return true;
    }

    /**
     * 广度优先遍历
     * 对于根节点，我们定义其编号为 1。然后，对于每个节点 v，我们将其左节点编号为 2 * v，将其右节点编号为 2 * v + 1。
     * <p>
     * 我们可以发现，树中所有节点的编号按照广度优先搜索顺序正好是升序。（也可以使用深度优先搜索，之后对序列排序）。
     * <p>
     * 然后，我们检测编号序列是否为无间隔的 1, 2, 3, …，事实上，我们只需要检查最后一个编号是否正确，因为最后一个编号的值最大。
     * <p>
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/check-completeness-of-a-binary-tree/solution/er-cha-shu-de-wan-quan-xing-jian-yan-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    public boolean isCompleteTree2(TreeNode root) {
        List<ANode> nodes = new ArrayList();
        // root编号为1
        nodes.add(new ANode(root, 1));
        int i = 0;
        // 对于所有节点
        while (i < nodes.size()) {
            ANode anode = nodes.get(i++);
            // 不为null的节点，它的左右孩子会按正常的序号加进去，如果是完全的，最后一个节点的序号就会等于加入的节点数
            // 如果它为null，它的两个孩子就不会被加进去，就会导致之最后一个节点的编号不等于实际的节点数目，因为只要加入的节点的序号都是正确的，
            // 但是由于它的两孩子没加进入，所以实际的节点数会比加进去的节点中最大的那个编号小
            //              1
            //         2          3
            //       4   空     6     7
            //     8   9 。 。12 13 14 15
            if (anode.node != null) {
                nodes.add(new ANode(anode.node.left, anode.code * 2));
                nodes.add(new ANode(anode.node.right, anode.code * 2 + 1));
            }
        }
        // 最后的节点就是编号最大的那个
        return nodes.get(i - 1).code == nodes.size();
    }

    // 给节点加个编号
    class ANode {  // Annotated Node
        TreeNode node;
        int code;

        ANode(TreeNode node, int code) {
            this.node = node;
            this.code = code;
        }
    }


}
