package com.daily;

import com.common.TreeNode;

/**
 * @author wangwei
 * @date 2023/5/21 10:03
 * @description: _1373_MaximumSumBSTInBinaryTree
 *
 * 1373. 二叉搜索子树的最大键值和
 * 给你一棵以 root 为根的 二叉树 ，请你返回 任意 二叉搜索子树的最大键值和。
 *
 * 二叉搜索树的定义如下：
 *
 * 任意节点的左子树中的键值都 小于 此节点的键值。
 * 任意节点的右子树中的键值都 大于 此节点的键值。
 * 任意节点的左子树和右子树都是二叉搜索树。
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：root = [1,4,3,2,4,2,5,null,null,null,null,null,null,4,6]
 * 输出：20
 * 解释：键值为 3 的子树是和最大的二叉搜索树。
 * 示例 2：
 *
 *
 *
 * 输入：root = [4,3,null,1,2]
 * 输出：2
 * 解释：键值为 2 的单节点子树是和最大的二叉搜索树。
 * 示例 3：
 *
 * 输入：root = [-4,-2,-5]
 * 输出：0
 * 解释：所有节点键值都为负数，和最大的二叉搜索树为空。
 * 示例 4：
 *
 * 输入：root = [2,1,3]
 * 输出：6
 * 示例 5：
 *
 * 输入：root = [5,4,8,3,null,6,3]
 * 输出：7
 *
 *
 * 提示：
 *
 * 每棵树有 1 到 40000 个节点。
 * 每个节点的键值在 [-4 * 10^4 , 4 * 10^4] 之间。
 * 通过次数27,042提交次数56,893
 */
public class _1373_MaximumSumBSTInBinaryTree {

    private int ans;

    private final int inf = 1 << 30;

    public int maxSumBST(TreeNode root) {
        dfs(root);
        return ans;
    }

    /**
     * 返回 以 root 为根的子树的信息 [是否是BST，节点键值和，所有最大节点值，最小节点值]
     *
     * ans 只有在 root为根的子树是 BST 的时候才需要更新
     *
     * 因此当以root为根的子树不是BST的时候，只需要保证返回值 [0，x，x，x] x 随意取值，因为用不到
     *
     * 当 root 是个空节点时，返回 [1，0，它的最大值为 -inf，最小值为 inf]
     * 这样对于一个叶子节点，递归得到左右子树后，就能满足   左子树的最大值 < 我 < 右子树的最小值
     * 这种情况下，对于这个叶子节点的返回值，不能直接返回 [1, 节点值，右子树最大值，左子树最小值]
     * 虽然理论上，左子树的最大值 < 我 < 右子树的最小值  成立的前提下，子树的最大值肯定是右子树的最大值，最小值肯定是左子树的最小值
     * 但是由于它是叶子节点，那么递归得到的左右子树（空）的返回值都是 最大值为 -inf，最小值为 inf
     * 所以如果直接返回 [1, 节点值，右子树最大值，左子树最小值] 会得到 [1, 节点值，-inf，inf]
     *
     * 你看，这是不是个错误答案？所以需要修正，返回 [1, 节点值，max(节点值，右子树最大值)，min(节点值，左子树最小值)]
     * 修正后，叶子节点的返回结果就是 [1, 节点值，节点值，节点值] 就正确了
     *
     * 当叶子节点的返回值 修正后，再向上返回就不会出现那种问题，
     * @param root
     * @return
     */
    private int[] dfs(TreeNode root) {
        // 空树，是BST，最大值返回 -inf，最小值返回 inf
        if (root == null) {
            return new int[]{1, 0, -inf, inf};
        }
        // 递归得到左右子树
        int[] left = dfs(root.left);
        int[] right = dfs(root.right);

        // 满足 BST 的条件
        if (left[0] == 1 && right[0] == 1 && root.val > left[2] && root.val < right[3]) {
            // 更新 ans
            ans = Math.max(ans, root.val + left[1] + right[1]);
            // 不能直接返回 [1, 节点值，右子树最大值，左子树最小值] ，因为 叶子节点也是由左右递归得到，会返回  [1, 节点值，-inf，inf] 错误
            // 需要修正 [1, 节点值，max(节点值，右子树最大值)，min(节点值，左子树最小值)]
            return new int[]{1, root.val + left[1] + right[1], Math.max(root.val, right[2]), Math.min(root.val, left[3])};
        }
        // 其他情况，不是 BST，对 ans 不影响，随便返回，保证 第一个位置为0即可（不是BST）
        return new int[]{0};
    }

    /**
     * 方法二：
     *
     * 上面方法不好理解的地方在于
     *      左子树的最大值 < 我 < 右子树的最小值  成立的前提下，子树的最大值肯定是右子树的最大值，最小值肯定是左子树的最小值
     *      却不能直接返回 直接返回 [1, 节点值，右子树最大值，左子树最小值]
     *      还需要 [1, 节点值，max(节点值，右子树最大值)，min(节点值，左子树最小值)]
     * 问题在于，
     *      空节点会返回 {1, 0, -inf, inf};
     *      而，处理叶子节点时，会 左右递归得到左右子树（空节点）的返回值，这种情况下
     *              直接返回 [1, 节点值，右子树最大值，左子树最小值] 会得到 [1, 节点值，-inf，inf]，是错误的
     *              需要修正 由于空节点的返回值 导致 递归回退到 叶子节点时，叶子节点的返回值 出错，所以修正
     *
     * 那么我们对叶子节点特殊处理一下，不就行了，对于叶子节点，不让它通过左右递归的方式得到自己的返回值
     * 因为叶子节点只有一个值，那么直接返回 [1,我，我，我] 不就行了
     *
     * 但还不够，如果空节点的返回值还是{1, 0, -inf, inf}，那么  对于 只有一个孩子的 节点，返回值还是会出错，
     *
     * 那么 对于这种只有一个孩子的节点，也特殊处理一下不就行了？
     *      既然只有一个孩子，那空节点不用管，先 递归得到 非空节点就行了，再得到自己的返回值就行了
     *          递归得到 非空节点 的结果需要矫正吗？不需要，叶子节点我们已经特殊处理了，它向上的返回就不会再出错
     *
     * 对于 左右都非空的节点，和之前一样，先左右递归，再得到自己就行了
     *
     * 根源在于，方法一为了对所有节点统一处理，给 空节点返回 {1, 0, -inf, inf};
     *      保证 在叶子节点位置左右递归后，也能满足 左子树的最大值 < 我 < 右子树的最小值  成立
     *      从而保证叶子节点返回值是BST，但却需要对最大最小值做修正，
     *
     * 我们这里，直接特殊处理叶子节点和只有一个孩子的节点，不让空节点的返回值对它造成影响，那么所有节点的返回值就不需要修正
     * 空节点也不需要再返回 -inf，inf 这样的特殊值，返回 0 就行，因为都不用到
     *
     *
     * @param root
     * @return
     */
    private int[] dfs2(TreeNode root) {
        // 空树，它不会对上层节点（叶子或只有一个孩子）造成影响，因此它的最大最小值不用特殊返回
        if (root == null) {
            return new int[]{1};
        }
        // 叶子节点，不再通过左右递归的方式得到，直接特殊处理
        if (root.left == null && root.right == null) {
            // 满足BST，更新ans
            ans = Math.max(ans, root.val);
            // 直接返回(1,我，我，我)，不用修正
            return new int[]{1, root.val, root.val, root.val};
        }
        // 只有右子树
        if (root.left == null) {
            // 就递归右子树
            int[] right = dfs(root.right);
            // 判断条件也不用考虑左子树，
            if (right[0] == 1 && root.val < right[3]) {
                // 满足BST，更新ans
                ans = Math.max(ans, root.val + right[1]);
                // 直接返回(1, 键值和，右子树最大值，我)，不用修正
                return new int[]{1, root.val + right[1], right[2], root.val};
            }
            // 不是BST,随便返回，保证第一个位置是0即可
            return new int[]{0};
        }
        // 只有左子树
        if (root.right == null) {
            // 就递归左子树
            int[] left = dfs(root.left);
            // 判断条件也不用考虑右子树，
            if (left[0] == 1 && root.val > left[2]) {
                // 满足BST，更新ans
                ans = Math.max(ans, root.val + left[1]);
                // 直接返回(1,键值和，我，左子树最小值)，不用修正
                return new int[]{1, root.val + left[1], root.val, left[3]};
            }
            // 不是BST,随便返回，保证第一个位置是0即可
            return new int[]{0};
        }
        // 左右子树都非空，递归
        int[] left = dfs(root.left);
        int[] right = dfs(root.right);
        // 判断BST
        if (left[0] == 1 && right[0] == 1 && root.val > left[2] && root.val < right[3]) {
            // 满足BST，更新ans
            ans = Math.max(ans, root.val + left[1] + right[1]);
            // 直接返回 (1,键值和，右子树最大值，左子树最小值)，不用修正
            return new int[]{1, root.val + left[1] + right[1], right[2], left[3]};
        }
        // 不是BST,随便返回，保证第一个位置是0即可
        return new int[]{0};
    }
}
