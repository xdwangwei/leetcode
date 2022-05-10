package com.tree;

import com.common.Pair;
import com.common.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 * @author wangwei
 * 2020/4/26 15:08
 * <p>
 * 给定一个二叉树，返回它的中序 遍历。
 * <p>
 * 示例:
 * <p>
 * 输入: [1,null,2,3]
 * 1
 * \
 * 2
 * /
 * 3
 * <p>
 * 输出: [1,3,2]
 * 进阶: 递归算法很简单，你可以通过迭代算法完成吗？
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/binary-tree-inorder-traversal
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _94_BinaryTreeInorderTraversal {

    /**
     * 递归
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        dfs(root, res);
        return res;
    }

    private void dfs(TreeNode root, List<Integer> list) {
        if (root != null) {
            dfs(root.left, list);
            list.add(root.val);
            dfs(root.right, list);
        }
    }

    /**
     * 迭代
     * 思路：每到一个节点 A，因为根的访问在中间，将 A 入栈。然后遍历左子树，接着访问 A，最后遍历右子树。
     * <p>
     * 在访问完 A 后，A 就可以出栈了。因为 A 和其左子树都已经访问完成
     *
     * 注意这里的 【访问】 就指的是输出节点或者加入列表，而 【遍历】 指的是这个处理过程，因为栈是后进先出
     * 所以本来是 左》中-》右，就要先把 自己入栈，再继续过程，才能保证 访问的时候 自己在左之前
     *
     * 中序遍历的一般顺序就是  左-》左-》左-》左-》父-》右-》父-》右-》父-》右
     *
     *
     * <p>
     * 作者：jason-2
     * 链接：https://leetcode-cn.com/problems/binary-tree-inorder-traversal/solution/die-dai-fa-by-jason-2/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public List<Integer> inorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        // 注意这个条件
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                // 每到一个节点 A，因为根的访问在中间，将 A 入栈
                stack.push(curr);
                // 然后遍历（处理）左子树，
                curr = curr.left;
            }
            // 左下角到头了(null)，弹出他的爸爸（成为最左下的那个节点）
            // 接着访问 A，这里才叫访问
            curr = stack.pop();
            res.add(curr.val);
            // 最后遍历（处理）右子树。
            curr = curr.right;
        }
        return res;
    }

    /**
     * 颜色标记法 + 栈
     * <p>
     * 使用颜色标记节点的状态，新节点为白色，被标记(访问)的节点为灰色。
     * 如果出栈的节点为白色，则将其标记为灰色，然后将其右子节点、自身、左子节点依次入栈。（根据遍历顺序调整入栈顺序）
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

    public List<Integer> inorderTraversal3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        final int WHITE = 0, BLACK = 1;
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
        // 根节点以白色身份入栈
        stack.push(new Pair<>(root, WHITE));
        while (!stack.isEmpty()) {
            // 拿出一个节点
            Pair<TreeNode, Integer> pair = stack.pop();
            // 首先，这个节点不为空
            if (pair.getKey() != null) {
                // 如果它是白色，没被标记访问
                if (pair.getValue() == WHITE) {
                    // 根据要求，中序  左 -》 自己 -》 右，入栈的时候就要反过来
                    // 右孩子，白色
                    stack.push(new Pair<>(pair.getKey().right, WHITE));
                    // 把自己标记上，变成黑色
                    stack.push(new Pair<>(pair.getKey(), BLACK));
                    // 左孩子，白色
                    stack.push(new Pair<>(pair.getKey().left, WHITE));
                } else
                    // 这个节点是黑色，被标记过，那就是访问了
                    res.add(pair.getKey().val);
            }
        }
        return res;
    }

}
