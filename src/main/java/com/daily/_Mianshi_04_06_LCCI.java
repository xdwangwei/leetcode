package com.daily;

import com.common.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/5/16 14:01
 * @description: _Mianshi_04_06_LCCI
 *
 * 面试题 04.06. 后继者
 * 设计一个算法，找出二叉搜索树中指定节点的“下一个”节点（也即中序后继）。
 *
 * 如果指定节点没有对应的“下一个”节点，则返回null。
 *
 * 示例 1:
 *
 * 输入: root = [2,1,3], p = 1
 *
 *   2
 *  / \
 * 1   3
 *
 * 输出: 2
 * 示例 2:
 *
 * 输入: root = [5,3,6,2,4,null,null,1], p = 6
 *
 *       5
 *      / \
 *     3   6
 *    / \
 *   2   4
 *  /
 * 1
 *
 * 输出: null
 * 通过次数33,160提交次数54,802
 */
public class _Mianshi_04_06_LCCI {


    /**
     * 方法一：中序遍历 的 迭代形式实现
     * 也没用到二叉搜索树的性质，适用于全部二叉树
     *
     * 为了找到二叉搜索树中的节点 p 的后继节点，最直观的方法是中序遍历。
     * 由于只需要找到节点 p 的后继节点，因此不需要维护完整的中序遍历序列，只需要在中序遍历的过程中维护上一个访问的节点和当前访问的节点。
     * 如果上一个访问的节点是节点 p，则当前访问的节点即为节点 p 的后继节点。
     *
     * 所以要关注的地方在于 访问节点 部分，在这里，才知道当前访问和下一个要访问节点的前驱后继关系，也应该在此处更新prev
     *
     * 如果节点 p 是最后被访问的节点，则不存在节点 p 的后继节点，返回 null。
     *
     * 因为要护上一个访问的节点prev和当前访问的节点cur，所以 借助 栈 来模拟递归过程
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/successor-lcci/solution/hou-ji-zhe-by-leetcode-solution-6hgc/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @param p
     * @return
     */
    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        if (root == null) {
            return null;
        }
        Deque<TreeNode> stack = new ArrayDeque<>();
        // 初始，待访问节点是root，上一个访问节点是null
        TreeNode cur = root, prev = null;
        // 如果还有未被访问的节点，就继续
        while (cur != null || !stack.isEmpty()) {
            // -------访问左子树部分---------
            // 中序访问node，得先访问完node的左子树（自己要在栈底）
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            // 当前访问节点
            cur = stack.pop();
            // 说明 cur 就是 p 的后继
            if (prev == p) {
                return cur;
            }
            // 访问完cur，去访问cur的右子树，这里就存在访问的先后关系，在此处更新prev
            // 注意 第一部分while入栈只是模拟递归调用，保存访问顺序，真正执行节点的访问在此处，或者说我们关心的是中序后继，所以得在左子树访问完之后进行处理
            // 在这里才能得到访问的后继关系
            // 更新prev
            prev = cur;
            // --------访问右子树部分----------
            cur = cur.right;
        }
        // 不存在
        return null;
    }


    /**
     * 方法二：利用二叉搜索树的性质
     *
     * 二叉搜索树的一个性质是中序遍历序列单调递增，因此二叉搜索树中的节点 p 的后继节点满足以下条件：
     *
     *      后继节点的节点值大于 p 的节点值；
     *
     *      后继节点是节点值大于 p 的节点值的所有节点中节点值最小的一个节点。
     *
     * 利用二叉搜索树的性质，可以在不做中序遍历的情况下找到节点 p 的后继节点。
     *
     * 如果节点 p 的右子树不为空，则节点 p 的后继节点在其右子树中，在其右子树中定位到最左边的节点，即为节点 p 的后继节点。
     *
     * 如果节点 p 的右子树为空，则需要从根节点开始遍历寻找节点 p 的祖先节点。
     *
     * 这两种情况可以合并为 从根节点开始遍历寻找节点 p 的祖先节点。
     *
     * 将答案初始化为 null。用 node 表示遍历到的节点，初始时 node=root。
     * 每次比较 node 的节点值和 p 的节点值，执行相应操作：
     *
     *      如果 node 的节点值【大于】 p 的节点值，
     *              则 p 的后继节点可能是 node 或者在 node 的左子树中，因此用 node 更新答案，并将 node 移动到其左子节点继续遍历；
     *      如果 node 的节点值【小于或等于】 p 的节点值，
     *              则 p 的后继节点可能在 node 的右子树中，因此将 node 移动到其右子节点继续遍历。
     *
     * 由于在遍历过程中，当且仅当 node 的节点值大于 p 的节点值的情况下，才会用 node 更新答案，
     * 因此当节点 p 有后继节点时一定可以找到后继节点，当节点 p 没有后继节点时答案一定为 null。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/successor-lcci/solution/hou-ji-zhe-by-leetcode-solution-6hgc/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @param p
     * @return
     */
    public TreeNode inorderSuccessor2(TreeNode root, TreeNode p) {

        // p的后继
        TreeNode successor = null;
        // 二叉搜索树中序遍历是有序，p 的后继 一定 是 原树中 大于 它的所有节点中的最小值
        while (root != null) {
            // 比p大
            if (root.val > p.val) {
                // 先更新successor
                successor = root;
                // 然后去当前节点的左子树，因为要找的是 比 p 大的所有节点中最小的那个
                root = root.left;
            } else {
                // 当前节点<=p，不是p的后继
                // 要比p大，就去右子树
                root = root.right;
            }
        }
        return successor;


        // 递归写法
        // 当前节点大于p，那么 p 的后继就是 cur 或者 cur的left中 最小的 比p大的那个
        // if (root.val > p.val) {
        //     先去 cur 的 左子树 去找 比 p大的
        //     TreeNode temp = inorderSuccessor(root.left, p);
        //     如果没有，那 就只剩下 cur 了
        //     return temp == null ? root : temp;
        // } else {
        //     cur 小于 p，那么 p 的后继只可能在 cur 的右子树存在
        //     return inorderSuccessor(root.right, p);
        // }
    }
}
