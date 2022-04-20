package com.hot100;

import com.common.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangwei
 * 2022/4/17 16:12
 *
 * 101. 对称二叉树
 * 给你一个二叉树的根节点 root ， 检查它是否轴对称。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：root = [1,2,2,3,4,4,3]
 * 输出：true
 * 示例 2：
 *
 *
 * 输入：root = [1,2,2,null,3,null,3]
 * 输出：false
 *
 *
 * 提示：
 *
 * 树中节点数目在范围 [1, 1000] 内
 * -100 <= Node.val <= 100
 */
public class _101_SymmetricTree {

    /**
     * 首先避免一个误区，只要求 root 是轴对称，这并不代表 root 的每一棵子树都是 轴对称
     */

    /**
     * 迭代法：层序遍历二叉树，每一层的所有节点值必须是前后对应相等，
     * 同时维护一个 按顺序保存每一层节点的队列，和按顺序保存每一层节点值的列表
     * 队列只用来做层序遍历，节点值列表用来判断当层节点是否前后对应相等，若不相等则返回false
     * 由于有一些节点只有1个左孩子或者右孩子，比如 root 的left只有左孩子，root的right也只有左孩子，这实际是不对称的，但是这两个节点值相等，会误判
     * 那怎么办呢，让 节点值列表 保存每一层全部节点值，对于null，保存一个特殊值，相当于让他占个位置（左，右）
     * 这样，刚才那种情况肯定不满足前后对应相等。
     * 需要注意的是：
     *      1. 题目中节点值取值范围，我们那个代表null的特殊值要在这些值之外，这里取101
     *      2. queue的大小和list的大小不一定相等，因为list里面保存了null节点，而queue里面不需要
     *      3. 每一层遍历完后，list记得清空
     */
    public boolean isSymmetric(TreeNode root) {
        // 每一层节点，不包含null
        Queue<TreeNode> queue = new LinkedList<>();
        // 每一层节点的值，null节点用101代替
        List<Integer> vals = new ArrayList<>();
        // 加入根节点
        queue.offer(root);
        vals.add(root.val);
        while (!queue.isEmpty()) {
            // 注意，这里获取的是 节点值列表 的大小，用 节点值列表 来判断 当层节点是否对称
            int size = vals.size();
            // 首尾对应位置值相等
            for (int i = 0; i < size / 2; ++i) {
                int j = size - i - 1;
                // 提前结束
                if (vals.get(i) != vals.get(j)) {
                    return false;
                }
            }
            // 清空，保存下一层
            vals.clear();
            // queue。当层全部节点，边取出，边加入它的左右孩子 ，当层 共有 queue.size 个节点
            size = queue.size();
            for (int i = 0; i < size; ++i) {
                // 取出
                TreeNode node = queue.poll();
                // 左节点不空
                if (node.left != null) {
                    // 入队列
                    queue.offer(node.left);
                    // 保存值
                    vals.add(node.left.val);
                } else {
                    // 不用入队列，但需要保存值
                    vals.add(-101);
                }
                // 右节点类似处理
                if (node.right != null) {
                    queue.offer(node.right);
                    vals.add(node.right.val);
                } else {
                    vals.add(-101);
                }
            }
        }
        return true;
    }

    /**
     * 递归写法，root 轴对称的话，那么 按照 【先【左右】孩子，再自己】， 和 【先【右左】孩子，再 自己】 两种后序遍历法得到的结果应该一致
     * 同样，为了取出 单个孩子节点的节点造成的影响，给 null 节点添加特殊遍历值
     * @param root
     * @return
     */
    public boolean isSymmetric2(TreeNode root) {
        StringBuilder builder = new StringBuilder();
        dfs1(root, builder);
        String s1 = builder.toString();
        builder.setLength(0);
        dfs2(root, builder);
        return builder.toString().equals(s1);
    }

    /**
     * 先左，再右，再自己 遍历法
     * @param root
     * @param builder
     */
    private void dfs1(TreeNode root, StringBuilder builder) {
        // 空节点特殊值
        if (root == null) {
            builder.append("-101");
            return;
        }
        // 左
        dfs1(root.left, builder);
        // 右
        dfs1(root.right, builder);
        // 自己
        builder.append(root.val);
    }

    /**
     * 先右，再左，再自己 遍历法
     * @param root
     * @param builder
     */
    private void dfs2(TreeNode root, StringBuilder builder) {
        // 空节点特殊值
        if (root == null) {
            builder.append("-101");
            return;
        }
        // 右
        dfs2(root.right, builder);
        // 左
        dfs2(root.left, builder);
        // 自己
        builder.append(root.val);
    }

    /**
     * 看看更简单的递归写法，就是 同时比较 应该对称的两个节点(对应位置) 是否真的对称
     *     作者：wang_ni_ma
     *     链接：https://leetcode-cn.com/problems/symmetric-tree/solution/dong-hua-yan-shi-101-dui-cheng-er-cha-shu-by-user7/
     *     来源：力扣（LeetCode）
     *     著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public boolean isSymmetric3(TreeNode root) {
        if(root==null) {
            return true;
        }
        //调用递归函数，比较左节点，右节点
        return dfs(root.left,root.right);
    }

    boolean dfs(TreeNode left, TreeNode right) {
        //递归的终止条件是两个节点都为空
        //或者两个节点中有一个为空
        //或者两个节点的值不相等
        if(left==null && right==null) {
            return true;
        }
        if(left==null || right==null) {
            return false;
        }
        if(left.val!=right.val) {
            return false;
        }
        //再递归的比较 左节点的左孩子 和 右节点的右孩子
        //以及比较  左节点的右孩子 和 右节点的左孩子
        return dfs(left.left,right.right) && dfs(left.right,right.left);
    }

    /**
     * 看看更简单的迭代写法
     *     作者：wang_ni_ma
     *     链接：https://leetcode-cn.com/problems/symmetric-tree/solution/dong-hua-yan-shi-101-dui-cheng-er-cha-shu-by-user7/
     *     来源：力扣（LeetCode）
     *     著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @return
     */
    public boolean isSymmetric4(TreeNode root) {
        if(root==null || (root.left==null && root.right==null)) {
            return true;
        }
        //用队列保存节点
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        //将根节点的左右孩子放到队列中
        queue.add(root.left);
        queue.add(root.right);
        while(queue.size()>0) {
            //从队列中取出两个节点，再比较这两个节点
            TreeNode left = queue.removeFirst();
            TreeNode right = queue.removeFirst();
            //如果两个节点都为空就继续循环，两者有一个为空就返回false
            if(left==null && right==null) {
                continue;
            }
            if(left==null || right==null) {
                return false;
            }
            if(left.val!=right.val) {
                return false;
            }
            //将左节点的左孩子， 右节点的右孩子放入队列
            queue.add(left.left);
            queue.add(right.right);
            //将左节点的右孩子，右节点的左孩子放入队列
            queue.add(left.right);
            queue.add(right.left);
        }

        return true;
    }
}
