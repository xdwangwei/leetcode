package com.tree;

import com.common.Pair;
import com.common.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * @author wangwei
 * 2020/4/26 19:29
 *
 * 给定一个二叉树，返回它的 后序 遍历。
 *
 * 示例:
 *
 * 输入: [1,null,2,3]
 *    1
 *     \
 *      2
 *     /
 *    3
 *
 * 输出: [3,2,1]
 * 进阶: 递归算法很简单，你可以通过迭代算法完成吗？
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/binary-tree-postorder-traversal
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _145_BinaryTreePostorderTraversal {

    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
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
            helper(root.left, list);
            helper(root.right, list);
            list.add(root.val);
        }
    }

    /**
     * 后续遍历  左 -》 右 -》 自己
     * 我们可以用与前序遍历相似的方法完成后序遍历。
     *
     * 后序遍历与前序遍历相对称。（并不是反过来）
     * 前序是：自己-》左-》右  因此，在访问完根节点后，遍历左子树前，要将右子树压入栈。
     * 注意这里的 【访问】 就指的是输出节点或者加入列表，而 【遍历】 指的是这个处理过程，因为栈是后进先出
     * 所以本来是 左》中-》右，就要先把 自己入栈，再继续过程，才能保证 访问的时候 自己在左之前
     *
     * 后序遍历倒过来是 自己 -》 右 -》 左 因此，在访问完根节点后，遍历右子树前，要将左子树压入栈。
     *
     * 思路： 每到一个节点 A，就应该立即访问它。 然后将左子树压入栈，再次遍历右子树。
     *
     * 遍历完整棵树后，结果序列【逆序】即可。
     *
     * 作者：jason-2
     * 链接：https://leetcode-cn.com/problems/binary-tree-inorder-traversal/solution/die-dai-fa-by-jason-2/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public List<Integer> postorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        Stack<TreeNode> stack = new Stack<>();
        // 后续 左右中，反过来 中右左，为了保证右在左之前，在遍历(处理)左子树之前，把右子树入栈，才能保证访问时，与右孩子先于左孩子
        TreeNode curr = root;
        while (curr != null || !stack.isEmpty()){
            while (curr != null){
                // 访问自己
                res.add(curr.val);
                // 遍历(处理)右子树之前把左子树入栈，才能保证真正访问时 右孩子先于左孩子
                stack.push(curr.left);
                // 遍历(处理)右子树
                curr = curr.right;
            }
            // 已经到右下脚(null),去找它的爸爸
            curr = stack.pop();
        }
        // 顺序倒过来
        Collections.reverse(res);
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
    public List<Integer> postorderTraversal3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        final int WHITE = 0, BLACK = 1;
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
        // 根节点，白色，入栈
        stack.push(new Pair<>(root, WHITE));
        // 逐个取出栈中节点进行处理
        while (!stack.isEmpty()){
            // 取出一个
            Pair<TreeNode, Integer> pair = stack.pop();
            TreeNode node = pair.getKey();
            Integer color = pair.getValue();
            // 首先节点不能为空
            if (node != null){
                    // 如果没有标记过
                if (color == WHITE){
                    // 后续 左右中 反序 中右左
                    // 自己，黑色，进栈
                    stack.push(new Pair<>(node, BLACK));
                    // 右孩子，白色，入栈
                    stack.push(new Pair<>(node.right, WHITE));
                    // 左孩子，白色，入栈
                    stack.push(new Pair<>(node.left, WHITE));
                }else
                    // 标记过了
                    res.add(node.val);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
    }
}
