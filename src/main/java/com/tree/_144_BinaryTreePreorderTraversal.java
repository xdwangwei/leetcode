package com.tree;

import com.common.Pair;
import com.common.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author wangwei
 * 2020/4/26 18:01
 *
 * 给定一个二叉树，返回它的 前序 遍历。
 *
 *  示例:
 *
 * 输入: [1,null,2,3]
 *    1
 *     \
 *      2
 *     /
 *    3
 *
 * 输出: [1,2,3]
 * 进阶: 递归算法很简单，你可以通过迭代算法完成吗？
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/binary-tree-preorder-traversal
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _144_BinaryTreePreorderTraversal {

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res  = new ArrayList<>();
        if (root == null) return res;
        helper(root, res);
        return res;
    }

    /**
     * 递归
     * @param root
     * @param list
     */
    private void helper(TreeNode root, List<Integer> list){
        if (root != null){
            list.add(root.val);
            helper(root.left, list);
            helper(root.right, list);
        }
    }

    /**
     * 栈
     *
     * 递归思路：先树根，然后左子树，然后右子树。每棵子树递归。
     *
     * 在迭代算法中，思路演变成，每到一个节点 A，就应该立即访问它。
     *
     * 因为，每棵子树都先访问其根节点。对节点的左右子树来说，也一定是先访问根。
     *
     * 在 A 的两棵子树中，遍历完左子树后，再遍历右子树。
     *
     * 因此，在访问完根节点后，遍历左子树前，要将右子树压入栈。
     *
     * 注意这里的 【访问】 就指的是输出节点或者加入列表，而 【遍历】 指的是这个处理过程，因为栈是后进先出
     * 所以本来是 中》左-》右，访问自己后就要先把右子树入栈再继续处理过程 ，才能保证 访问的时候 左在右之前
     *
     * 作者：jason-2
     * 链接：https://leetcode-cn.com/problems/binary-tree-inorder-traversal/solution/die-dai-fa-by-jason-2/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> res  = new ArrayList<>();
        if (root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        while (curr != null || !stack.isEmpty()){
            while (curr != null){
                // 每到一个节点 A，就应该立即访问它。
                res.add(curr.val);
                // 先序 遍历完左子树后，再遍历右子树
                // 因此，在访问完根节点后，遍历左子树前，要将右子树压入栈。
                stack.push(curr.right);
                // 遍历左子树
                curr = curr.left;
            }
            // 左下角到头了(null)，弹出他的爸爸（成为最左下的那个节点）
            curr = stack.pop();
        }
        return res;
    }

    /**
     * 颜色标记法 + 栈
     * <p>
     * 使用颜色标记节点的状态，新节点为白色，被标记(访问)的节点为灰色。
     * 如果出栈的节点为白色，则将其标记为灰色，然后将其右子节点、左子节点、自身依次入栈。（根据遍历顺序调整入栈顺序）
     * 如果出栈的节点为灰色，则将节点的值输出。
     * <p>
     * 这种方法的好处是 无论是 前序中序还是后序，只需要调整入栈顺序即可
     * 缺点是：每个节点都要入栈，出栈，入栈，出栈，本质上和递归没太大区别
     * 比如，我把根节点入栈，出栈，它是白色，把他变为黑色入栈，再出栈，它是黑色，把它加入结果集，每个节点都是这样
     * <p>
     * 作者：hzhu212
     * 链接：https://leetcode-cn.com/problems/binary-tree-inorder-traversal/solution/yan-se-biao-ji-fa-yi-chong-tong-yong-qie-jian-ming/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public List<Integer> preorderTraversal3(TreeNode root) {
        List<Integer> res  = new ArrayList<>();
        if (root == null) return res;
        final int WHITE = 0, BLACK = 1;
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
        // 根节点，白色，入栈
        stack.push(new Pair<>(root, WHITE));
        while (!stack.isEmpty()){
            // 拿出一个节点
            Pair<TreeNode, Integer> pair = stack.pop();
            // 首先节点要不为null
            if (pair.getKey() != null){
                // 如果它是白色的，没有访问，根据先序遍历的反序入栈
                if (pair.getValue() == WHITE){
                    // 右孩子，白色，入栈
                    stack.push(new Pair<>(pair.getKey().right, WHITE));
                    // 左孩子，白色，入栈
                    stack.push(new Pair<>(pair.getKey().left, WHITE));
                    // 自己，以访问。变为黑色
                    stack.push(new Pair<>(pair.getKey(), BLACK));
                }else
                    // 它是黑色的，标记了，就加入结果集
                    res.add(pair.getKey().val);
            }
        }
        return res;
    }
}
