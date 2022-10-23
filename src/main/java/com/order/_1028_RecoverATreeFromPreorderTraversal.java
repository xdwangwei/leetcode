package com.order;

import com.common.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/10/23 12:42
 * @description: _1028_RecoverATreeFromPreorderTraversal
 *
 * 1028. 从先序遍历还原二叉树
 * 我们从二叉树的根节点 root 开始进行深度优先搜索。
 *
 * 在遍历中的每个节点处，我们输出 D 条短划线（其中 D 是该节点的深度），然后输出该节点的值。（如果节点的深度为 D，则其直接子节点的深度为 D + 1。根节点的深度为 0）。
 *
 * 如果节点只有一个子节点，那么保证该子节点为左子节点。
 *
 * 给出遍历输出 S，还原树并返回其根节点 root。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入："1-2--3--4-5--6--7"
 * 输出：[1,2,5,3,4,6,7]
 * 示例 2：
 *
 *
 *
 * 输入："1-2--3---4-5--6---7"
 * 输出：[1,2,5,3,null,6,null,4,null,7]
 * 示例 3：
 *
 *
 *
 * 输入："1-401--349---90--88"
 * 输出：[1,401,null,349,88,90]
 *
 *
 * 提示：
 *
 * 原始树中的节点数介于 1 和 1000 之间。
 * 每个节点的值介于 1 和 10 ^ 9 之间。
 * 通过次数23,949提交次数32,834
 */
public class _1028_RecoverATreeFromPreorderTraversal {

    /**
     * 如果是一个完整的二叉树遍历序列（空节点用特殊值代替），相当于每一个节点的位置都得到了保留，就能够还原二叉树
     * 常规的单个前序遍历序列或单个后序遍历无法还原二叉树就是因为空节点的值在遍历序列中被省略，导致树的结构无法被确定，所以需要结合中序遍历序列来判断每个节点的位置
     *
     * 本题中，仍然没有保留null节点，但是给出了 -- 这种带有层级指示的符号，能够在一定程度上帮我们确定树的结构
     *
     * 比如对于一个完整的遍历序列 "1-2--3--4-5--6--7"，
     *          数字前面0个-，就是根节点
     *          数字前面一个-，就是第一层节点，也就是根节点的左孩子，或者右孩子
     *          数字前面两个-，就是第二层节点，也就是第一层节点的左右孩子(按顺序)
     *          能够明显看出 1是根，-2--3--4 这部分是左子树序列，-5--6--7 这部分是右子树序列
     * 采用递归方式还原，使用 j 标记当前处理到的索引位置，整体框架如下：
     *          root = new node(s[j]), 初始时 j = 0，也就时根节点，j++
     *          root.left = dfs(s),    进入时：j 是 左子树的根节点，
     *          root.right = dfs(s)    进入时：j 是 右子树的根节点，相当于 j 在每一次递归中顺利的走到了对应子树的末尾，不用我们再手动更新判断j的位置
     *          return root;
     *
     * 那么在每一次 dfs 中，我们如何利用 -- 字符 来判断什么时候应该返回 空节点呢？
     *
     * 我们给dfs函数增加一个参数level，或者说depth，也就是当前处理的子树根节点的在原树中的深度，初始值为0，那么每次进入dfs，j应该先前进level才能走到当前子树的根节点位置
     *
     * 还是以 "1-2--3--4-5--6--7" 为例，dfs(s, 0)：
     *      第一次进入 dfs，level = 0，j 需要前进0个位置，j = 0，此时 遇到的就是当前子树的根节点
     *      root = 1，然后 j 前进 = 1
     *      root.left  = dfs(s, level + 1 = 1)，左右子树深度都是当前根的深度加1
     *           进入后：  j前进1，j=2
     *                    root = 2, 然后 j 前进 = 3
     *                    root.left = dfs(s, level+1 = 2)
     *                        进入后：j 前进2，j = 5
     *                              root = 3，然后 j 前进 = 6
     *                              root.left = dfs(level+1 = 3)
     *                                  进入后：j 前进 3，j = 9（回退，还原为 6）
     *                                  【此时发现当前位置不是数字，代表当前是个空节点】，直接返回null
     *                                  这种情况下，需要把 j 回退，因为 j 前进是为了找root，但如果root不存在，那么j就不应该前进！！！
     *                              root.right = dfs(level+1 = 3)
     *                                  。。。。类似
     *                    root.right = dfs(s, level+1 = 2)
     *                              。。。类似
     *                    return root;
     *                      。。。。
     *      root.right = dfs(s, level + 1 = 1),左右子树深度都是当前根的深度加1
     *                      。。。。
     *      return root;
     *
     *  【总结】：识别出空节点的情况：
     *
     *
     *      如果 j 前进level会超过 序列范围，说明 此次dfs要寻找的root也不存在，也 回退 j
     *      j 前进 level 才能找到当前 root，如果 前进 level 后发现 当前位置不是数字，说明此次dfs的root不存在，此时回退 j
     *      j 在前进过程中应该遇到的都是 '-' ，如果遇到了数字字符，说明 此次dfs要寻找的root 也不存在，
     *
     * @param traversal
     * @return
     */

    // 全局索引，处理到了序列哪个字符，j每次前进level后就是当前子树的根位置
    private int j;

    public TreeNode recoverFromPreorder(String traversal) {
        return recoverFromPreorder(traversal, 0);
    }

    /**
     * dfs找出 深度为level 的子树的根节点
     * j 每次前进 level 个 ’-‘ 就会走到当前子树根节点位置
     * @param traversal
     * @param level
     * @return
     */
    private TreeNode recoverFromPreorder(String traversal, int level) {
        // 空节点1：若不存在level个'-'
        // 空节点2：前进level个'-'后发现不是数字
        if (j + level >= traversal.length() || !Character.isDigit(traversal.charAt(j + level))) {
            return null;
        }
        // 空节点3：前进level个'-'过程中遇到了非'-'字符a
        for (int i = 1; i < level; ++i) {
            if (Character.isDigit(traversal.charAt(j + i))) {
                return null;
            }
        }
        // 以上三种情况都应该回退 j，返回null，从而回退到上一层 dfs
        // 否则，前进 level
        j += level;
        // 此时 j 在当前 根位置
        int n = 0;
        // 跳过数字部分
        while (j < traversal.length() && Character.isDigit(traversal.charAt(j))) {
            n = n * 10 + (traversal.charAt(j++) - '0');
        }
        // 得到根节点
        TreeNode root = new TreeNode(n);
        // dfs去处理 左子树
        root.left = recoverFromPreorder(traversal, level + 1);
        // dfs去处理 左子树
        root.right = recoverFromPreorder(traversal, level + 1);
        // 返回根节点
        return root;
    }


    /**
     * 迭代写法：核心还是通过-的个数，判断当前节点的层数或者说深度，需要考虑的问题是当前节点的父节点是谁
     * 递归写法不用考虑这个问题是因为，每一次递归结束自然回回退到上一次递归内给left或right赋值的位置
     * 迭代写法，需要自己考虑
     *
     * 迭代处理的话，每次都会处理到一个节点，记当前节点为 T，上一个节点为 S，因为是先序序列，所以实际上只有两种情况：
     *      T 是 S 的左子节点；（S的左右孩子都在S之后，但题目说了，如果只有一个孩子一定是左孩子，也就是说第一个遇到的一定是左孩子）
     *      T 是根节点到 S 这一条路径上（不包括 S）某一个节点的右子节点。
     *
     * 想一想先序遍历的递归 + 回溯方法，
     * 对于在先序遍历中任意的两个相邻的节点 S 和 T，
     * 要么 T 就是 S 的左子节点（对应上面的第一种情况），
     * 要么在遍历到 S 之后发现 S 是个叶子节点null，于是回溯到之前的某个节点，并开始递归地遍历其右子节点（对应上面的第二种情况）。
     *
     * 因此，我们按照顺序维护从根节点到当前节点的路径上的所有节点，就可以方便地处理这两种情况。
     * 仔细想一想，这实际上就是使用递归 + 回溯的方法遍历一棵树时，栈中的所有节点，也就是可以回溯到的节点。
     * 因此我们只需要使用一个栈来模拟递归 + 回溯即可。
     *
     * 回到上面的分析，当我们得到当前节点的值以及深度信息之后，我们可以发现：
     *      如果 T 是 S 的左子节点，那么 T 的深度恰好比 S 的深度大 1；此时 T 的深度恰好为 栈里面元素个数
     *      在其它的情况下，T 是栈中某个节点（不包括 S）的右子节点，那么我们将栈顶的节点不断地出栈，直到 T 的深度恰好比栈顶节点的深度大 1，也就是t的深度，和栈中元素个数相同，
     *          此时我们就找到了 T 的双亲节点。
     *          为什么直接出栈，因为如果不是情况一，说明当前当前节点不是栈顶节点的孩子，也就是说栈顶节点子树部分的序列已经处理完了，栈顶节点的左右孩子也都找完了
     *          那么弹出栈顶后，如果还不满足情况一，那么类似的，此时的栈顶的左右孩子也处理完了，可以直接弹栈
     *          直到，某次弹栈后，满足情况一了，也就是给当前节点找到父亲了
     *          那么，当前节点到底是此时栈顶的左孩子还是右孩子呢？
     *          如果，栈顶的左孩子为null，那就是它的左孩子
     *          如果，栈顶的左孩子不为null，这里就是它的右孩子
     *          因为题目说了优先左孩子，并且这里是先序顺序，所以先判断左孩子
     *
     * 最终栈底元素就是root
     *
     *
     * 通由于题目给出的 traversal 一定是某棵二叉树的先序遍历结果，因此我们在代码中不需要处理任何异常异常情况。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/recover-a-tree-from-preorder-traversal/solution/cong-xian-xu-bian-li-huan-yuan-er-cha-shu-by-leetc/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param traversal
     * @return
     */
    private TreeNode recoverFromPreorder2(String traversal) {
        // 栈结构，顺序记录处理到的每一个节点，栈顶是处理到的上一个节点
        Deque<TreeNode> stack = new ArrayDeque<>();
        int i = 0, n = traversal.length();
        while (i < n) {
            // 当前节点的level
            int level = 0;
            while (i < n && traversal.charAt(i) == '-') {
                level++;
                i++;
            }
            // 当前节点的值
            int num = 0;
            while (i < n && Character.isDigit(traversal.charAt(i))) {
                num = num * 10 + (traversal.charAt(i) - '0');
                i++;
            }
            TreeNode cur = new TreeNode(num);
            // 栈空，直接入栈，跳过后续步骤
            if (stack.isEmpty()) {
                stack.push(cur);
                continue;
            }
            // 情况二：当前节点深度 != 栈顶节点深度+1（栈大小），当前节点属于之间某个节点x的孩子
            // 并且节点x和当前节点之间的那些节点已经处理完了，因为在先序序列中已经不满足层级关系了，
            while (level != stack.size()) {
                stack.pop();
            }
            // 情况一：当前节点是栈顶节点的孩子
            // 如果left还没赋值，则为其左孩子
            // 否则为其右孩子
            if (stack.peek().left == null) {
                stack.peek().left = cur;
            } else {
                stack.peek().right = cur;
            }
            // 当且节点入栈，别漏了
            stack.push(cur);
        }
        // 最终需要返回栈底元素
        while (stack.size() > 1) {
            stack.pop();
        }
        return stack.pop();
    }

    public static void main(String[] args) {
        _1028_RecoverATreeFromPreorderTraversal obj = new _1028_RecoverATreeFromPreorderTraversal();
        TreeNode root = obj.recoverFromPreorder("1-2--3--4-5--6--7");
        System.out.println(root);

        obj.j = 0;
        root = obj.recoverFromPreorder("1-2--3---4-5--6---7");
        System.out.println(root);
    }
}
