package com.offerassult;

import com.common.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/12/3 10:47
 * @description: _050_SumOfDownNodePaths
 *
 * 剑指 Offer II 050. 向下的路径节点之和
 * 给定一个二叉树的根节点 root ，和一个整数 targetSum ，求该二叉树里节点值之和等于 targetSum 的 路径 的数目。
 *
 * 路径 不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：root = [10,5,-3,3,2,null,11,3,-2,null,1], targetSum = 8
 * 输出：3
 * 解释：和等于 8 的路径有 3 条，如图所示。
 * 示例 2：
 *
 * 输入：root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
 * 输出：3
 *
 *
 * 提示:
 *
 * 二叉树的节点个数的范围是 [0,1000]
 * -109 <= Node.val <= 109
 * -1000 <= targetSum <= 1000
 *
 *
 * 注意：本题与主站 437 题相同：https://leetcode-cn.com/problems/path-sum-iii/
 */
public class _050_SumOfDownNodePaths {

    /**
     * 方法一：深度优先搜索
     *
     * 题目要求 路径不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点） 。
     * 这就要求我们只需要去求三部分即可：
     *
     * 以当前节点作为开始结点的路径数量
     * 左子树中的路径数量
     * 右子树中的路径数量
     *
     * 将这三部分之和作为最后结果即可。
     *
     * 最后的问题是：我们应该如何去求以当前节点作为头结点的路径的数量？
     *
     * 这里依旧是递归，
     * 如果node.val == sum 自身作为一个路径，否则再去计算
     * 以左右孩子为开始节点，和为sum - root.val的路径数量
     *
     * 相当于有两个递归，第一个递归是dfs，节点的访问顺序，
     * 第二个递归是访问每个节点时，计算以它开始的和为sum的路径数量
     *
     * 路径和累加过程可能溢出，使用long
     *
     * 作者：Geralt_U
     * 链接：https://leetcode-cn.com/problems/path-sum-iii/solution/437lu-jing-zong-he-iii-di-gui-fang-shi-by-ming-zhi/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @param sum
     * @return
     */
    /* 有了以上铺垫，详细注释一下代码 */
    public int pathSum(TreeNode root, int sum) {
        if (root == null) return 0;
        // 自己为开头，满足和为sum的路径数
        int pathImLeading = leadingPathSum(root, sum);
        // 左子树中满足和为sum的路径数
        int leftPathSum = pathSum(root.left, sum);
        // 右子树中满足和为sum的路径数
        int rightPathSum = pathSum(root.right, sum);
        // 求和，返回
        return leftPathSum + rightPathSum + pathImLeading;
    }

    /**
     * 计算以自己为开头的路径中，和为sum的路径数
     * @param node
     * @param sum
     * @return
     */
    private int leadingPathSum(TreeNode node, long sum) {
        if (node == null) return 0;
        // 我自己能不能独当一面，作为一条单独的路径呢？
        int isMe = (node.val == sum) ? 1 : 0;
        // 左边的小老弟，你那边能凑几个和为 sum - node.val 的路径呀？
        int leftBrother = leadingPathSum(node.left, sum - node.val);
        // 右边的小老弟，你那边能凑几个和为 sum - node.val 的路径呀？
        int rightBrother = leadingPathSum(node.right, sum - node.val);
        // 求和，我这能凑这么多个
        return  isMe + leftBrother + rightBrother;
    }

    /**
     * 方法二: 前缀和
     * 思路与算法
     *
     * 我们仔细思考一下，解法一中应该存在许多重复计算。
     * 在「解法一」中，我们统计的是以每个节点为根的（往下的）所有路径，也就是说统计的是以每个节点为「路径开头」的所有合法路径。
     *
     * 本题的一个优化切入点为「路径只能往下」，因此如果我们转换一下，统计以每个节点为「路径结尾」的合法路径数量的话，
     * 因为从根节点到每个节点的路径唯一，相当于在这条路径中找到有多少个节点到当前节点的路径总和为 targetSum。
     *
     * 于是这个树上问题彻底转换「一维前缀和」问题：
     * 求解从原始起点（根节点）到当前节点 b 的路径中，有多少节点 a 满足 sum[a...b] = targetSum
     *
     * 我们定义节点p的前缀和为：由根结点root到当前结点p的路径上所有节点的和。
     * 我们利用先序遍历二叉树，记录下根节点 root 到当前节点 p 的路径上(root->p1..p2...pk)所有节点的前缀和，
     *
     * 假如root到p的前缀和为cur，
     * 那么如果我们想知道以p结尾的路径和为targetSum的祖先起点有多少个，
     * 只需要p的祖先节点中多多少节点到root的前缀和为 cur-targetSum
     *
     * 由于不需要知道具体路径，只需要知道路径数目，因此用hash表存储 <前缀和，数目>
     * （虽然root到p的路径唯一，但因为题目中节点取值可正可负，因此中间可能存在部分连续节点和为0，这样就会存在多个祖先到p的连续和为targetSum）
     *
     * 【注意】
     * 由于我们是先序遍历，（统计以p为结尾节点、以p.left为结尾、以p.right为结尾）
     * 为了保证遍历到每个节点p时，map中存储的始终是 root 到 p 的祖先节点的前缀和情况。
     *
     * 我们应当
     *      在完成p的访问后，将root到p的前缀和更新到map中，
     *      然后进行对p的左右孩子的访问
     *      访问完p的左右孩子后，回溯地将root到p的前缀和从哈希表中删除，防止统计到跨越两个方向的路径。
     *      （因为此时会回退到p的上一层节点pf的right，map中应该只保留pf及以上节点的前缀和信息）
     * 【细节】
     *      初始时，我们应该给map中放入(0,1)，代表以root以上的节点为结尾，前缀和为0的路径个数为1
     *      这样，当root节点值恰好为targetSum中，map中就存在 key为 cur-targetSum=0，值为1
     *      否则，就会错过 root 单独作为路径这种情况（只是拿顶层举例子，在整个过程中可能多次遇到这种情况，这个初始化是必要的）
     *
     *      路径和累加过程可能溢出，使用long
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/path-sum-iii/solution/lu-jing-zong-he-iii-by-leetcode-solution-z9td/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int pathSum2(TreeNode root, int targetSum) {
        if (root == null) {
            return 0;
        }
        // root到p（将要访问）的所有祖先节点的前缀和及次数
        Map<Long, Integer> preSumMap = new HashMap<>();
        // 初始化，前缀和为0，出现次数1
        preSumMap.put(0L, 1);
        // dfs
        return dfs(root, preSumMap, 0L, targetSum);
    }

    /**
     * 以root为根的子树中，路径上节点和为targetSum的路径数目
     * 三部分：
     *      以 p 为结尾 。。。。
     *      以 p.left 为结尾 。。。。
     *      以 p.right 为结尾 。。。。
     * 完整的dfs后，统计了以每个节点为结尾的符合要求的路径数目
     * @param node  将要访问的节点
     * @param preSumMap root到node所有祖先节点的前缀和及出现次数
     * @param curSum root到node的父节点的前缀和
     * @param targetSum 目标路径和
     * @return
     */
    private int dfs(TreeNode node, Map<Long, Integer> preSumMap, long curSum, int targetSum) {
        if (node == null) {
            return 0;
        }
        // root到当前节点p的前缀和
        curSum += node.val;
        // 以 p 为结尾节点的 节点和为targetSum 的路径（起始节点）数目
        int ans = preSumMap.getOrDefault(curSum - targetSum, 0);

        // 访问p的左右孩子前，将root到p的前缀和信息更新到map
        preSumMap.put(curSum, preSumMap.getOrDefault(curSum, 0) + 1);

        // 访问 p的左右孩子
        // 统计 以 p 为左右孩子为结尾节点的 节点和为targetSum 的路径（起始节点）数目
        ans += dfs(node.left, preSumMap, curSum, targetSum);
        ans += dfs(node.right, preSumMap, curSum, targetSum);

        // 访问完p的左右孩子后，因为要回退到上一层，将root到p的前缀和信息从map中移除，
        // 保证dfs到每个节点时，map中只保留root到其祖先节点的前缀和信息
        preSumMap.put(curSum, preSumMap.get(curSum) - 1);

        // 返回
        return ans;
    }
}
