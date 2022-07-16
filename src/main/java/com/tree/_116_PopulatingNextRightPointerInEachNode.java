package com.tree;

/**
 * @author wangwei
 * 2020/12/12 10:42
 *
 * 给定一个 完美二叉树 ，其所有叶子节点都在同一层，每个父节点都有两个子节点。二叉树定义如下：
 *
 * struct Node {
 *   int val;
 *   Node *left;
 *   Node *right;
 *   Node *next;
 * }
 * 填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 NULL。
 *
 * 初始状态下，所有 next 指针都被设置为 NULL。
 *
 *  
 *
 * 进阶：
 *
 * 你只能使用常量级额外空间。
 * 使用递归解题也符合要求，本题中递归程序占用的栈空间不算做额外的空间复杂度。
 *  
 *
 * 示例：
 *
 *
 *
 * 输入：root = [1,2,3,4,5,6,7]
 * 输出：[1,#,2,3,#,4,5,6,7,#]
 * 解释：给定二叉树如图 A 所示，你的函数应该填充它的每个 next 指针，以指向其下一个右侧节点，如图 B 所示。序列化的输出按层序遍历排列，同一层节点由 next 指针连接，'#' 标志着每一层的结束。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/populating-next-right-pointers-in-each-node
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _116_PopulatingNextRightPointerInEachNode {

    /**
     * 如果只是把每个节点的左孩子的next指向右孩子。无法完成相邻节点之间的next指向
     * 将每一层二叉树节点连接起来」可以细化成「将每两个相邻节点都连接起来
     * @param root
     * @return
     */
    public Node connect(Node root) {
        if (root == null) return root;
        connect(root.left, root.right);
        return root;
    }
    // 连接两个相邻节点(子树)
    private void connect(Node left, Node right) {
        if (left == null || right == null) {
            return;
        }
        // 两节点连接
        left.next = right;
        // 左树的左右孩子连接
        connect(left.left, left.right);
        // 右树的左右孩子连接
        connect(right.left, right.right);
        // 左树的右孩子和右树的左孩子连接
        connect(left.right, right.left);
    }
}

class Node {

    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
};
