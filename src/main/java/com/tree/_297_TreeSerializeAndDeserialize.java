package com.tree;

import com.common.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author wangwei
 * 2020/7/30 8:04
 */
public class _297_TreeSerializeAndDeserialize {

    /**
     * 中序遍历的方式行不通，因为无法实现反序列化方法 deserialize
     *
     * 要想实现反序列方法，首先要构造 root 节点。
     * 前序遍历得到的 nodes 列表中，第一个元素是 root 节点的值；
     * 后序遍历得到的 nodes 列表中，最后一个元素是 root 节点的值。
     *
     * 中序遍历的代码，root 的值被夹在两棵子树的中间，也就是在 nodes 列表的中间，
     * 我们不知道确切的索引位置，所以无法找到 root 节点，也就无法进行反序列化
     */

    /***
     * 一般语境下，单单前序遍历结果是不能还原二叉树结构的，因为缺少空指针的信息，
     * 至少要得到前、中、后序遍历中的两种才能还原二叉树。
     * 但是这里的 node 列表包含空指针的信息，所以只使用 node 列表就可以还原二叉树。
     * @param root
     * @return
     */
    private final String NULL = "#";
    private final String SEP = ",";

    /***
     * 把一棵二叉树序列化成字符串  前序遍历顺序
     * @param root
     * @return
     */
    public String serialize1(TreeNode root) {
        StringBuilder builder = new StringBuilder();
        serialize1(root, builder);
        return builder.toString();
    }
    /* 辅助函数，将二叉树存入 StringBuilder */
    private void serialize1(TreeNode root, StringBuilder builder) {
        if (root == null) {
            builder.append(NULL).append(SEP);
            return;
        }
        /****** 前序遍历位置 ******/
        builder.append(root.val).append(SEP);
        /***********************/

        serialize1(root.left, builder);
        serialize1(root.right, builder);
    }

    /***
     * 把字符串反序列化成二叉树 前序遍历序列
     * 1,2,#,4,,#,#,3,#,#
     * @param data
     * @return
     */
    public TreeNode deserialize1(String data) {
        LinkedList<String> nodes = new LinkedList<>();
        for (String str : data.split(SEP)) {
            nodes.addLast(str);
        }
        return deserialize1(nodes);
    }
    /* 辅助函数，通过 nodes 列表构造二叉树 */
    private TreeNode deserialize1(LinkedList<String> nodes) {
        if (nodes.isEmpty())
            return null;
        /****** 前序遍历位置 ******/
        // 列表最左侧就是根节点
        String first = nodes.removeFirst();
        if (first.equals(NULL)) return null;
        TreeNode root = new TreeNode(Integer.parseInt(first));
        /***********************/
        // 按照前序遍历顺序进行恢复
        root.left = deserialize1(nodes);
        root.right = deserialize1(nodes);
        return root;
    }

    /***
     * 把一棵二叉树序列化成字符串  后序遍历顺序
     * @param root
     * @return
     */
    public String serialize2(TreeNode root) {
        StringBuilder builder = new StringBuilder();
        serialize2(root, builder);
        return builder.toString();
    }
    /* 辅助函数，将二叉树存入 StringBuilder */
    private void serialize2(TreeNode root, StringBuilder builder) {
        if (root == null) {
            builder.append(NULL).append(SEP);
            return;
        }

        serialize2(root.left, builder);
        serialize2(root.right, builder);
        /****** 后序遍历位置 ******/
        builder.append(root.val).append(SEP);
        /***********************/
    }

    /***
     * 把字符串反序列化成二叉树 后序遍历序列
     * @param data
     * @return
     */
    public TreeNode deserialize2(String data) {
        LinkedList<String> nodes = new LinkedList<>();
        for (String str : data.split(SEP)) {
            nodes.addLast(str);
        }
        return deserialize2(nodes);
    }
    /* 辅助函数，通过 nodes 列表构造二叉树 */
    private TreeNode deserialize2(LinkedList<String> nodes) {
        if (nodes.isEmpty()) return null;
        // 后序遍历顺序，最右边的是根节点，从后往前取出元素
        String last = nodes.removeLast();
        if (last.equals(NULL)) return null;
        TreeNode root = new TreeNode(Integer.parseInt(last));
        // 按后序遍历序列，从右往左，先构造右子树，后构造左子树
        root.right = deserialize2(nodes);
        root.left = deserialize2(nodes);

        return root;
    }

    /***
     * 把一棵二叉树序列化成字符串  层序遍历顺序
     * @param root
     * @return
     */
    public String serialize3(TreeNode root) {
        if (root == null) return "";
        StringBuilder builder = new StringBuilder();
        // 初始化队列，将 root 加入队列
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();

            /* 层级遍历代码位置 */
            if (cur == null) {
                builder.append(NULL).append(SEP);
                continue;
            }
            // 访问自己
            builder.append(cur.val).append(SEP);
            /*****************/
            // 按顺序加入左孩子和右孩子
            queue.offer(cur.left);
            queue.offer(cur.right);
        }

        return builder.toString();
    }

    /***
     * 把字符串反序列化成二叉树 层序遍历序列
     *
     * 层序遍历的结果 每一个非空节点都会对应两个子节点，
     * 那么反序列化的思路也是用队列进行层级遍历，同时用索引 i 记录对应子节点的位置：
     * @param data
     * @return
     */
    public TreeNode deserialize3(String data) {
        if (data.isEmpty()) return null;
        String[] nodes = data.split(SEP);
        // 第一个元素就是 root 的值
        TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));

        // 队列 queue 记录父节点，将 root 加入队列
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        // 节点从1开始。root的孩子是1,2 左孩子的孩子是3,4 右孩子的孩子是 5,6
        for (int i = 1; i < nodes.length; ) {
            // 队列中存的都是父节点
            TreeNode parent = queue.poll();
            // 父节点对应的左侧子节点的值
            String left = nodes[i++];
            if (!left.equals(NULL)) {
                parent.left = new TreeNode(Integer.parseInt(left));
                // 左孩子入队列
                queue.offer(parent.left);
            } else {
                parent.left = null;
            }
            // 父节点对应的右侧子节点的值
            String right = nodes[i++];
            if (!right.equals(NULL)) {
                parent.right = new TreeNode(Integer.parseInt(right));
                // 右孩子入队列
                queue.offer(parent.right);
            } else {
                parent.right = null;
            }
        }
        return root;
    }
}
