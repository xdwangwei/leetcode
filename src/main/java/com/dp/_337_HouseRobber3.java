package com.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * 2020/7/24 21:12
 * <p>
 * 在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为“根”。 除了“根”之外，每栋房子有且只有一个“父“房子与之相连。一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
 * <p>
 * 计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
 * <p>
 * 示例 1:
 * <p>
 * 输入: [3,2,3,null,3,null,1]
 * <p>
 * 3
 * / \
 * 2   3
 * \   \
 * 3   1
 * <p>
 * 输出: 7
 * 解释: 小偷一晚能够盗取的最高金额 = 3 + 3 + 1 = 7.
 * 示例 2:
 * <p>
 * 输入: [3,4,5,1,3,null,1]
 * <p>
 *      3
 * / \
 * 4   5
 * / \   \
 * 1   3   1
 * <p>
 * 输出: 9
 * 解释: 小偷一晚能够盗取的最高金额 = 4 + 5 = 9.
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/house-robber-iii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _337_HouseRobber3 {

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode(int x) { val = x; }
     * }
     */
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    /**
     * 动态规划 + 备忘录，自顶向下记忆化搜索
     * 考虑抢劫每个节点
     */
    Map<TreeNode, Integer> memo = new HashMap<>();

    public int rob(TreeNode root) {
        if (root == null) return 0;
        // 利用备忘录消除重叠子问题
        if (memo.containsKey(root))
            return memo.get(root);
        // 如果抢当前节点，下一家不能抢，下下家就是他的左孩子的左右孩子和他的右孩子的左右孩子
        int rob = root.val
                // 下下家 左孩子的左右孩子
                + (root.left == null ? 0 : rob(root.left.left) + rob(root.left.right))
                // 下下家 右孩子的左右孩子
                + (root.right == null ? 0 : rob(root.right.left) + rob(root.right.right));
        // 不抢当前节点，就可以直接抢左右孩子
        int not_rob = rob(root.left) + rob(root.right);
        // 加进备忘录
        memo.put(root, Math.max(rob, not_rob));
        return memo.get(root);
    }

    /**
     * 对于每个节点，同时计算抢劫和不抢劫的收益，就不需要备忘录
     * res[0]表示不抢劫root能获得的最多的钱
     * res[1]表示抢劫root能获得的最多的钱
     */
    public int rob1(TreeNode root) {
        int[] res = dp(root);
        return Math.max(res[0], res[1]);
    }

    int[] dp(TreeNode root) {
        if (root == null) return new int[]{0, 0};

        // 计算抢或不抢左孩子的收益
        int[] robLeft = dp(root.left);
        // 计算抢或不抢右孩子的收益
        int[] robRight = dp(root.right);

        // 抢劫当前节点，就不能抢他的左右孩子(下家)
        int rob = root.val + robLeft[0] + robRight[0];

        // 不抢当前节点，就能抢他的下家（左右孩子）
        // 不能这么写，他不一定非得去抢左右孩子，万一不抢的话收益更大呢
        // int notRob = robLeft[1] + robRight[1];
        int notRob = Math.max(robLeft[0], robLeft[1]) + Math.max(robRight[0], robRight[1]);

        return new int[]{notRob, rob};
    }
}
