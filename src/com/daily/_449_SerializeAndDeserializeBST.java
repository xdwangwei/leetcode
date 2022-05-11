package com.daily;

import com.common.TreeNode;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/5/11 19:40
 * @description: _449_SerializeAndDeserializeBST
 *
 * 449. 序列化和反序列化二叉搜索树
 * 序列化是将数据结构或对象转换为一系列位的过程，以便它可以存储在文件或内存缓冲区中，或通过网络连接链路传输，以便稍后在同一个或另一个计算机环境中重建。
 *
 * 设计一个算法来序列化和反序列化 二叉搜索树 。 对序列化/反序列化算法的工作方式没有限制。 您只需确保二叉搜索树可以序列化为字符串，并且可以将该字符串反序列化为最初的二叉搜索树。
 *
 * 编码的字符串应尽可能紧凑。
 *
 *
 *
 * 示例 1：
 *
 * 输入：root = [2,1,3]
 * 输出：[2,1,3]
 * 示例 2：
 *
 * 输入：root = []
 * 输出：[]
 *
 *
 * 提示：
 *
 * 树中节点数范围是 [0, 104]
 * 0 <= Node.val <= 104
 * 题目数据 保证 输入的树是一棵二叉搜索树。
 * 通过次数33,097提交次数55,772
 */
public class _449_SerializeAndDeserializeBST {


    /**
     * 如果只是一颗普通的二叉树，序列化可以通过 前序遍历、中序遍历、后序遍历、层序遍历等方式得到
     * 对于反序列化，若要能恢复出原树结构，通常情况下，我们需要 前序、中序、后序遍历 中至少两种遍历结果结合，
     * 但这种需求是因为 我们的遍历代码中派出了空节点，导致最终的结果序列实际上并未保存原树的结构，
     * 如果，对于所有空节点，我们用一个特殊值代替，这样的话，最后的遍历结果就相当于一个完全二叉树的遍历结果，每个节点都有两个孩子，并且两个孩子的值我们也都进行了保存
     * 这样就相当于在遍历结果中保存了原树的结构，在这种情况下，我们仅仅通过前序或者后续或者层序中的一种序列就能恢复出原二叉树。（中序遍历不能找到根节点，所以不行）
     * 这种方式的弊端就是序列中包含了所有空节点的特殊值，
     *
     * 第一种方式：把BST树当普通二叉树处理，保存所有节点值(保存了树结构)，给空节点统一特殊值
     *
     * 使用 前序遍历 或者 后续遍历
     */
    public class Codec1 {


        // 节点值之间的分隔符
        private static final String SEP = ",";

        // 空节点保存特殊值
        private static final String NULL = "#";

        /**
         * 序列化，这里采用 前序序列化
         * @param root
         * @return
         */
        public String serialize(TreeNode root) {
            StringBuilder builder = new StringBuilder();
            preOrder(root, builder);
            return builder.toString();
        }

        /**
         * 二叉树前序遍历
         * @param root
         * @param builder
         */
        private void preOrder(TreeNode root, StringBuilder builder) {
            // 保留空节点，标记为特殊值
            if (root == null) {
                builder.append(NULL).append(SEP);
                return;
            }
            // 前序遍历结果
            builder.append(root.val).append(SEP);
            preOrder(root.left, builder);
            preOrder(root.right, builder);
        }



        /**
         * 反序列化，通过前序遍历结果，恢复出树结构
         * @param data
         * @return
         */
        public TreeNode deserialize(String data) {
            // 空
            if (data.isEmpty()) {
                return null;
            }
            // 去掉分隔符，用 双端队列保存遍历结果，我们采取递归方式恢复树结构，在此过程中会对 序列中的项进行移除，所以需要一个能改变的结构
            Deque<String> queue = new ArrayDeque<>();
            for (String node : data.split(SEP)) {
                queue.addLast(node);
            }
            return deserialize(queue);
        }

        /**
         * 根据前序遍历结果反序列出原树结构
         * @param queue
         * @return
         */
        private TreeNode deserialize(Deque<String> queue) {
            // 空树
            if (queue.isEmpty()) {
                return null;
            }
            // 首节点是根节点
            String node = queue.removeFirst();
            if (node.equals(NULL)) {
                return null;
            }
            TreeNode root = new TreeNode(Integer.parseInt(node));
            // 序列是前序遍历顺序得到，那么对应恢复也是前序顺序
            // 移除掉根节点后，第一个节点就是左子树的根节点，
            root.left = deserialize(queue);
            // 左子树整个处理完后，剩下的就是右子树节点，我们不需要考虑左子树一共几个节点，递归会帮我们完成
            root.right = deserialize(queue);
            return root;
        }

    }

    /**
     * 使用后序遍历进行序列化和重构
     */
    public class Codec2 {


        // 节点值之间的分隔符
        private static final String SEP = ",";

        // 空节点保存特殊值
        private static final String NULL = "#";

        /**
         * 序列化，这里采用 后序序列化
         * @param root
         * @return
         */
        public String serialize(TreeNode root) {
            StringBuilder builder = new StringBuilder();
            postOrder(root, builder);
            return builder.toString();
        }

        /**
         * 二叉树后序遍历
         * @param root
         * @param builder
         */
        private void postOrder(TreeNode root, StringBuilder builder) {
            // 保留空节点，标记为特殊值
            if (root == null) {
                builder.append(NULL).append(SEP);
                return;
            }
            // 后序遍历位置
            postOrder(root.left, builder);
            postOrder(root.right, builder);
            builder.append(root.val).append(SEP);
        }



        /**
         * 反序列化，通过后序遍历结果，恢复出树结构
         * @param data
         * @return
         */
        public TreeNode deserialize(String data) {
            // 空
            if (data.isEmpty()) {
                return null;
            }
            // 去掉分隔符，用 双端队列保存遍历结果，我们采取递归方式恢复树结构，在此过程中会对 序列中的项进行移除，所以需要一个能改变的结构
            Deque<String> queue = new ArrayDeque<>();
            for (String node : data.split(SEP)) {
                queue.addLast(node);
            }
            return deserialize(queue);
        }

        /**
         * 根据后序遍历结果反序列出原树结构
         * @param queue
         * @return
         */
        private TreeNode deserialize(Deque<String> queue) {
            // 空树
            if (queue.isEmpty()) {
                return null;
            }
            // 最后一个节点是根节点
            String node = queue.removeLast();
            if (node.equals(NULL)) {
                return null;
            }
            TreeNode root = new TreeNode(Integer.parseInt(node));
            // 序列是后序遍历顺序得到，那么对应恢复也是前序顺序
            // 移除掉根节点后，倒数第一个节点就是右子树的根节点，
            root.right = deserialize(queue);
            // 右子树整个处理完后，剩下的就是子子树节点，我们不需要考虑右子树一共几个节点，递归会帮我们完成
            root.left = deserialize(queue);
            return root;
        }

    }

    /**
     * 使用层序遍历进行序列化和重构
     *
     * 只要遍历结果包含了全部节点，就相当于保留了树结构，并且能够区分出根节点，就可以根据对应遍历规则恢复出原树结构
     *
     * 那么层序遍历如何体现原树的结构，假设层序遍历结果是 []
     * 我们相当于遍历了一棵完全二叉树，所以 对于 arr[0] 来说，arr[1] 和 arr[2] 就是它的左右孩子，同理， arr[3] 和 arr[4] 就是 arr[1] 的左右孩子
     * 连续两个节点是上一层节点的左右孩子，再连续两个节点是上一层的第二个节点的左右孩子，是不是与层序遍历时的规则对应上了
     *
     *
     * 所以：层序遍历需要注意的就是，以前进行层序遍历的时候，对于当前层某个节点，如果它的左右孩子不为空，就入队列
     *      现在的话就是不区分左右孩子是否非空，都要入队列，然后出队列的时候判断这个值是个节点值还是个空值就可以
     *      所以入队的时候队列中可能会进入很多null值，要选择允许存null值的队列结构，ArrayDeque不可以，LinkedList可以
     */
    public class Codec3 {


        // 节点值之间的分隔符
        private static final String SEP = ",";

        // 空节点保存特殊值
        private static final String NULL = "#";

        /**
         * 序列化，这里采用 层序序列化
         * @param root
         * @return
         */
        public String serialize(TreeNode root) {
            if (root == null) {
                return "";
            }
            StringBuilder builder = new StringBuilder();
            // 层序遍历
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                // 出队列的时候判断当前节点值
                TreeNode node = queue.poll();
                if (node == null) {
                    // 空姐点保存特殊值，空姐点没有左右孩子了
                    builder.append(NULL).append(SEP);
                    continue;
                }
                builder.append(node.val).append(SEP);
                // 所有节点的左右孩子都要入队列，不区分是否非空
                queue.offer(node.left);
                queue.offer(node.right);
            }
            return builder.toString();
        }

        /**
         * 反序列化，通过层遍历结果，恢复出树结构
         * 层序遍历序列是通过 队列 以 迭代形式得到的
         * 同样的，恢复树结构的时候仍然要借助队列，因为要知道 遍历序列当前的两个连续值 是 上一层哪个节点的左右孩子
         * @param data
         * @return
         */
        public TreeNode deserialize(String data) {
            // 空
            if (data.isEmpty()) {
                return null;
            }
            // 去掉分隔符，用 双端队列保存遍历结果，我们采取递归方式恢复树结构，在此过程中会对 序列中的项进行移除，所以需要一个能改变的结构
            String[] nodes = data.split(SEP);
            // 层序遍历结果第一个是根节点
            TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));

            // 队列里面是 顺序的 待构造左右孩子的 父节点
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            // 遍历序列第一个值是根节点，剩下两个一组是 某个父节点(按照原树的层级结构) 的左右孩子值
            for (int i = 1; i < nodes.length; ) {
                // 序列中当前连续两个位置 是 哪个节点的左右孩子
                TreeNode parent = queue.poll();
                // 设置左孩子
                String left = nodes[i++];
                if (!left.equals(NULL)) {
                    parent.left = new TreeNode(Integer.parseInt(left));
                    // 它的左孩子成为下一个待构造左右孩子的父节点
                    queue.offer(parent.left);
                } else {
                    parent.left = null;
                }
                // 设置右孩子
                String right = nodes[i++];
                if (!right.equals(NULL)) {
                    parent.right = new TreeNode(Integer.parseInt(right));
                    // 它的左孩子成为下一个待构造左右孩子的父节点
                    queue.offer(parent.right);
                } else {
                    parent.right = null;
                }
            }
            return root;
        }

    }


    /**
     * 前面，无论是 前序遍历、后序遍历 还是 层序遍历，都是 将BST看作普通二叉树进行处理，因此 需要在遍历过程中保留全部节点的值
     *
     * 一是没有利用到二叉搜素树的性质，而是遍历结果中存储空节点意义不大，并且题目要求我们序列化结果尽可能紧凑，就是暗含了不保留无用节点
     * 那么如果遍历时不保存空节点，相当于未完全保留树结构，就得借助两种遍历顺序才能完成还原。
     * 注意到BST的中序遍历应该是一个有序序列，所以我们可以对前序遍历或者后序遍历的结果进行排序，就能得到中序遍历结果，这样就有两种遍历结果
     * 但是排序的时候复杂度是 0(NlogN)，我们还是希望能够借助BST树的特性，和前序或者后序遍历的结果 一起 完成原树的重构
     * 也就是说，排序得到中序序列，只是 BST树性质的一种运用方式，那我能够不用它来排序，就借助它的规则来恢复原树呢？
     *
     * 还是借鉴根据前序遍历结果加递归方式重构二叉树
     *
     * 我们知道前序遍历结果的第一个节点是根节点，然后剩下的第一个节点是左子树的根节点，然后我们不知道左子树到底几个节点，
     * 但是可以直接将剩下部分交给递归完成，原因就在于前序序列中保存了空节点，递归时能够区分出所有节点，
     *
     * 而现在，相当于 空节点的信息 缺失，所以我们只要把它补上，那么就仍然可以让递归帮我们后序完成
     * 怎么补上？BST的性质就是，左子树节点值都比 自己小，右子树节点值都比自己大，那么 当我们知道 根节点，知道左子树根节点，去构造左子树时，自然能够知道上下界
     * 如果序列中的某个值不在上下界范围内，说明这个位置应该是个空值，相当于找到了确实的空节点信息
     *
     * 【所以】：能够通过一个遍历序列就恢复原树，关键问题在于是否保留了原树空节点的位置信息？或者能够区分遍历序列当前位置是合理的树节点还是由于被丢失了的空节点提前到了这个位置
     */
    public class Codec4 {


        // 节点值之间的分隔符
        private static final String SEP = ",";

        /**
         * 序列化，这里采用 前序序列化
         * @param root
         * @return
         */
        public String serialize(TreeNode root) {
            StringBuilder builder = new StringBuilder();
            preOrder(root, builder);
            return builder.toString();
        }

        /**
         * 前序遍历，不保留空节点
         * @param root
         * @param builder
         */
        private void preOrder(TreeNode root, StringBuilder builder) {
            if (root == null) {
                return;
            }
            builder.append(root.val).append(SEP);
            preOrder(root.left, builder);
            preOrder(root.right, builder);
        }

        /**
         * 反序列化，通过前序遍历结果，恢复出树结构
         * 前序遍历序列是通过 递归 形式得到的
         * 同样的，恢复树结构的时候仍然要递归
         * @param data
         * @return
         */
        public TreeNode deserialize(String data) {
            // 空
            if (data.isEmpty()) {
                return null;
            }
            // 去掉分隔符，用 双端队列保存遍历结果，我们采取递归方式恢复树结构，在此过程中会对 序列中的项进行移除，所以需要一个能改变的结构
            String[] nodes = data.split(SEP);
            // 未保留空节点，所以直接转int
            Deque<Integer> queue = new LinkedList<>();
            for (String node : nodes) {
                queue.offer(Integer.parseInt(node));
            }
            // 初始时的上下界
            // 这样设置是合理的，因为序列第一个元素一定是根，而它的取值符合这个范围，之后左子树的取值上界就是根的值，至于下界完全可以更小，
            return deserialize(queue, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        /**
         * 由前序遍历序列重构二叉搜索树
         * @param queue
         * @param bottom 当前子树节点取值下界
         * @param upper 当前子树节点取值上界
         * @return
         */
        private TreeNode deserialize(Deque<Integer> queue, Integer bottom, Integer upper) {
            if (queue.isEmpty()) {
                return null;
            }
            Integer val = queue.getFirst();
            // 之前前序序列恢复树结构时是这样判空的，因为保留了空节点，所以可以直接判断
            // if (node.equals(NULL)) {
            //     return null;
            // }
            // 现在要借助二叉搜素树的特性来判断序列当前部分到底是节点，还是未保留信息的空节点
            // 如果是节点，取值范围一定在上下界之内
            if (val < bottom || val > upper) {
                return null;
            }
            // 当前根
            TreeNode root = new TreeNode(queue.removeFirst());
            // 构造左子树，上界 变为 当前根，下界未改变
            root.left = deserialize(queue, bottom, root.val);
            // 构造右子树，下界 变为 当前根，上界未改变
            root.right = deserialize(queue, root.val, upper);
            return root;
        }

    }


    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        StringBuilder builder = new StringBuilder();
        // builder.substring(0, builder.length() - 1)
        System.out.println(list.toString());
        for (String s : "a,2,3,4,".split(",")) {
            System.out.println(s);
        }
    }
}
