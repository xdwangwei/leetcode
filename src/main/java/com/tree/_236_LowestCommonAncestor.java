package com.tree;

import com.common.TreeNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * 2020/8/1 9:01
 *
 * 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
 *
 * 百度百科中最近公共祖先的定义为：“对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
 *
 * 例如，给定如下二叉树:  root = [3,5,1,6,2,0,8,null,null,7,4]
 *
 *
 *
 *  
 *
 * 示例 1:
 *
 * 输入: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 * 输出: 3
 * 解释: 节点 5 和节点 1 的最近公共祖先是节点 3。
 * 示例 2:
 *
 * 输入: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
 * 输出: 5
 * 解释: 节点 5 和节点 4 的最近公共祖先是节点 5。因为根据定义最近公共祖先节点可以为节点本身。
 *  
 *
 * 说明:
 *
 * 所有节点的值都是唯一的。
 * p、q 为不同节点且均存在于给定的二叉树中。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _236_LowestCommonAncestor {

    /**
     * 方法二：存储父节点
     * 思路
     *
     * 我们可以用哈希表存储所有节点的父节点，
     * 然后我们就可以利用节点的父子节点信息从 p 结点开始不断往上跳，并记录已经访问过的节点，
     * 再从 q 节点开始不断往上跳，如果碰到已经访问过的节点，那么这个节点就是我们要找的最近公共祖先。
     *
     * 算法
     *
     * 从根节点开始遍历整棵二叉树，用哈希表记录每个节点的父节点指针。
     * 从 p 节点开始不断往它的祖先移动，并用数据结构记录已经访问过的祖先节点。
     * 同样，我们再从 q 节点开始不断往它的祖先移动，
     * 如果有祖先已经被访问过，即意味着这是 p 和 q 的深度最深的公共祖先，即 LCA 节点。
     *
     * 从下往上找到的第一个相交节点才是最近 公共祖先
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/solution/er-cha-shu-de-zui-jin-gong-gong-zu-xian-by-leetc-2/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param root
     * @param p
     * @param q
     * @return
     */
    HashMap<TreeNode, TreeNode> parent = new HashMap<>();
    Set<TreeNode> visited = new HashSet<>();

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        // 递归访问全部节点，保存父子关系
        dfs(root);
        // 从 p 结点开始不断往上跳，并记录已经访问过的节点，
        while (p != null) {
            visited.add(p);
            p = parent.get(p);
        }
        // 从 q 节点开始不断往上跳，如果碰到已经访问过的节点，那么这个节点就是最近公共祖先
        while (q != null) {
            if (visited.contains(q))
                return q;
            q = parent.get(q);
        }
        return null;
    }

    /**
     * dfs遍历二叉树
     * @param root
     */
    private void dfs(TreeNode root) {
        if (root.left != null) {
            // 保存父子关系
            parent.put(root.left, root);
            // 递归遍历左子树
            dfs(root.left);
        }
        if (root.right != null) {
            // 保存父子关系
            parent.put(root.right, root);
            // 递归遍历右子树
            dfs(root.right);
        }
    }

    /**
     * 递归
     *
     * 解题思路：
     *
     * 祖先的定义： 若节点 p 在节点 root的左（右）子树中，或 p = root，则称 root是 p 的祖先。
     *
     *
     * 最近公共祖先的定义：
     *      设节点 root 为节点 p,q 的某公共祖先，
     *      若其左子节点 root.left 和右子节点 root.right 都【不是】 p,q 的公共祖先，
     *      则称 root 是 “最近的公共祖先” 。
     *
     * 根据以上定义，若 root是 p, q 的 最近公共祖先 ，则只可能为以下情况之一：
     *
     *      p 和 q 在 root 的子树中，且分列 root 的 异侧（即分别在左、右子树中）；
     *      p = root ，且 q 在 root的左或右子树中；(自己是自己的祖先)
     *      q = root ，且 p 在 root 的左或右子树中；
     *
     *
     * 考虑通过递归对二叉树进行【后序】遍历，当遇到节点 p 或 q 时返回。【从底至顶】回溯，
     *      当节点 p, q在节点 root 的异侧时，节点 root 即为最近公共祖先，则向上返回root 。
     *
     * 递归解析：
     *      终止条件：
     *          当越过叶节点，则直接返回 null ；
     *          当 root 等于 p, q，则直接返回 root ；
     *      递推工作：
     *          开启递归左子节点，返回值记为 left ；
     *          开启递归右子节点，返回值记为 right ；
     *      返回值： 根据 left 和 right ，可展开为四种情况；
     *          当 left 和 right 同时为空 ：说明 root的左 / 右子树中都不包含 p,q，返回 nullnull ；
     *          当 left和 right 同时不为空 ：说明 p,q 分列在 root 的异侧 （分别在 左 / 右子树），
     *              因此 root 为最近公共祖先，返回 root ；
     *          当 left为空 ，right 不为空 ：p,q 都不在 root 的左子树中，直接返回 right 。
     *              具体可分为两种情况：
     *              p,q 其中一个在 root 的 右子树 中，此时 right 指向 p（假设为 p ）；
     *              p,q 两节点都在 root 的 右子树 中，此时的 right 指向 最近公共祖先节点 ；
     *          当 left 不为空 ， right 为空 ：与情况 3. 同理；
     * 观察发现， 情况 1. 可合并至 3. 和 4. 内，详见文章末尾代码。
     *
     *
     * 复杂度分析：
     * 时间复杂度 O(N)： 其中 N 为二叉树节点数；最差情况下，需要递归遍历树的所有节点。
     * 空间复杂度 O(N)： 最差情况下，递归深度达到 N ，系统使用 O(N) 大小的额外空间。
     *
     * 作者：jyd
     * 链接：https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/solution/236-er-cha-shu-de-zui-jin-gong-gong-zu-xian-hou-xu/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        // 自己是自己的祖先
        if (root == null || root == p || root == q) return root;
        // 在左子树中寻找pq的最近公共祖先
        TreeNode left = lowestCommonAncestor2(root.left, p, q);
        // 在右子树中寻找pq的最近公共祖先
        TreeNode right = lowestCommonAncestor2(root.right, p, q);

        // 1. 都为空，root的左 / 右子树中都不包含 p,q
        if (left == null && right == null) return null;
        // 2. left为空 ，right 不为空 ：p,q 都不在 root 的左子树中，直接返回 right
        // 具体可分为两种情况：
        // 其中一个在 root 的 右子树 中，此时 right 指向 p（假设为 p ）；
        // p,q 两节点都在 root 的 右子树 中，此时的 right 指向 最近公共祖先节点 ；
        if (left == null) return right;
        // 3.当 left 不为空 ， right 为空 ：与情况 3. 同理；
        if (right == null) return left;
        // 4. left和 right 同时不为空 ：说明 p,q 分列在 root 的异侧
        return root;
    }
}
