package com.tree;

import com.common.TreeNode;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/12/9 17:04
 * @description: _055_IteratorOfBinarySearchTree
 *
 * 173. 二叉搜索树迭代器
 * 实现一个二叉搜索树迭代器类BSTIterator ，表示一个按中序遍历二叉搜索树（BST）的迭代器：
 *
 * BSTIterator(TreeNode root) 初始化 BSTIterator 类的一个对象。BST 的根节点 root 会作为构造函数的一部分给出。指针应初始化为一个不存在于 BST 中的数字，且该数字小于 BST 中的任何元素。
 * boolean hasNext() 如果向指针右侧遍历存在数字，则返回 true ；否则返回 false 。
 * int next()将指针向右移动，然后返回指针处的数字。
 * 注意，指针初始化为一个不存在于 BST 中的数字，所以对 next() 的首次调用将返回 BST 中的最小元素。
 *
 * 可以假设 next() 调用总是有效的，也就是说，当调用 next() 时，BST 的中序遍历中至少存在一个下一个数字。
 *
 *
 *
 * 示例：
 *
 *
 *
 * 输入
 * inputs = ["BSTIterator", "next", "next", "hasNext", "next", "hasNext", "next", "hasNext", "next", "hasNext"]
 * inputs = [[[7, 3, 15, null, null, 9, 20]], [], [], [], [], [], [], [], [], []]
 * 输出
 * [null, 3, 7, true, 9, true, 15, true, 20, false]
 *
 * 解释
 * BSTIterator bSTIterator = new BSTIterator([7, 3, 15, null, null, 9, 20]);
 * bSTIterator.next();    // 返回 3
 * bSTIterator.next();    // 返回 7
 * bSTIterator.hasNext(); // 返回 True
 * bSTIterator.next();    // 返回 9
 * bSTIterator.hasNext(); // 返回 True
 * bSTIterator.next();    // 返回 15
 * bSTIterator.hasNext(); // 返回 True
 * bSTIterator.next();    // 返回 20
 * bSTIterator.hasNext(); // 返回 False
 *
 *
 * 提示：
 *
 * 树中节点的数目在范围 [1, 105] 内
 * 0 <= Node.val <= 106
 * 最多调用 105 次 hasNext 和 next 操作
 *
 *
 * 进阶：
 *
 * 你可以设计一个满足下述条件的解决方案吗？next() 和 hasNext() 操作均摊时间复杂度为 O(1) ，并使用 O(h) 内存。其中 h 是树的高度。
 *
 *
 * 注意：本题与主站 173 题相同： https://leetcode-cn.com/problems/binary-search-tree-iterator/
 */
public class _173_IteratorOfBinarySearchTree {

    /**
     * 方法一：dfs
     * 我们可以直接对二叉搜索树做一次完全的递归遍历，获取中序遍历的全部结果并保存在数组中。
     * 随后，我们利用得到的数组本身来实现迭代器。
     */
    class BSTIterator {

        private final Iterator<Integer> iter;

        public BSTIterator(TreeNode root) {
            List<Integer> list = new ArrayList<>();
            // 中序遍历
            dfs(root, list);
            // 得到迭代器
            iter = list.iterator();
        }

        public int next() {
            return iter.next();
        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        /**
         * dfs中序遍历，保存节点值到list
         * @param root
         * @param list
         */
        private void dfs(TreeNode root, List<Integer> list) {
            if (root == null) {
                return;
            }
            dfs(root.left, list);
            list.add(root.val);
            dfs(root.right, list);
        }
    }

    /**
     * 方法二：迭代
     * 与方法一思路一致
     * 只是利用栈这一数据结构，通过迭代的方式对二叉树做中序遍历。
     * 无需预先计算出中序遍历的全部结果，只需要实时维护当前栈的情况即可。
     *
     * 中序遍历的一般写法
     *
     *     TreeNode cur = root;
     *     while (cur != null || !stack.isEmpty()) {
     *         // 【第一部分】
     *         while (cur != null) {
     *             stack.push(cur);
     *             cur = cur.right;
     *         }
     *         // 【第二部分】
     *         cur = stack.pop();
     *         System.out.println(cur.val);
     *         // 【第三部分】
     *         cur = cur.right;
     *     }
     *
     * 这里相当于 「中序遍历」代码的「等价拆分」
     *
     * next() 方法中我们需要输出一个值，执行的的是「步骤 2」的逻辑，但是我们需要在其前后添加「步骤 1」和「步骤 3」。
     *
     * 另外，我们还有一个 hasNext() 要处理，显然 hasNext() 应该对应while条件是否成立。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/binary-search-tree-iterator/solution/xiang-jie-ru-he-dui-die-dai-ban-de-zhong-4rxj/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class BSTIterator2 {
        // 当前待访问节点
        private TreeNode cur;
        private final Deque<TreeNode> stack;

        public BSTIterator2(TreeNode root) {
            // 只需初始化，无法递归得到全部结果
            cur = root;
            stack = new LinkedList<>();
        }

        public int next() {
            // 每次要访问一个节点，就进行一次中序遍历的迭代过程
            // 这里实际上就是完整的中序遍历过程迭代写法中while循环体内的过程
            // 左子树处理过程
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            // cur就是当前节点
            cur = stack.pop();
            int ret = cur.val;
            // 处理右子树
            cur = cur.right;
            // 返回当前节点值
            return ret;
        }

        public boolean hasNext() {
            // 这里实际上就是完整的中序遍历过程迭代写法中while的判断条件
            return cur != null || !stack.isEmpty();
        }
    }
}
