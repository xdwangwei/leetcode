package com.tree;

import com.common.TreeNode;

/**
 * @author wangwei
 * 2020/4/26 23:22
 * <p>
 * 遍历二叉树
 * 1. O(1)空间复杂度，即只能使用常数空间；
 * 2. 二叉树的形状不能被破坏（中间过程允许改变其形状）。
 * https://www.cnblogs.com/AnnieKim/archive/2013/06/15/morristraversal.html
 * <p>
 * Morris Traversal方法可以做到这两点，与前两种方法的不同在于该方法只需要O(1)空间，而且同样可以在O(n)时间内完成。
 * <p>
 * 要使用O(1)空间进行遍历，最大的难点在于，遍历到子节点的时候怎样重新返回到父节点（假设节点中没有指向父节点的p指针），
 * 由于不能用栈作为辅助空间。为了解决这个问题，Morris方法用到了线索二叉树（threaded binary tree）的概念。
 * 在Morris方法中不需要为每个节点额外分配指针指向其前驱（predecessor）和后继节点（successor），
 * 只需要利用【叶子节点中的左右空指针】指向 某种顺序遍历规则下的 前驱节点或后继节点就可以了。
 * <p>
 * 相当于在遍历过程中把树变成了链表
 * <p>
 * 使用前驱节点的右孩子指向自己，能够保证在进入左子树会再回来，
 * 前序与中序的唯一区别在于，找到前驱，将其右孩子指向自己后：
 * 先访问自己，再进入左子树，就是先序；
 * 先进入左子树，等从左子树回来，再访问自己，就是中序
 * <p>
 * 后序有点复杂，它的访问顺序是 左右中，也就是说，它需要从右子树回到自己
 * 而我们利用的是前驱节点原本是null的右孩子,【这也决定了我们所找的前驱一定是当前节点的左子树的最右孩子】，
 * 所以我们只能从左子树回到自己，
 *
 * 为了解决后序的问题，我们需要一个临时节点，访问当前节点curr时，把它作为这个临时节点的左孩子，以临时节点开始去进行访问，
 * 临时节点的左子树能被完全访问，也就保证了原本的curr能被访问
 * 否则，最多也只能把curr的左子树访问完，而不会访问到curr
 * <p>
 * 另外一个不同的地方在于输出节点的语句，具体看后续遍历步骤说明
 */
public class _00_MorrisBinaryTreeTravel {

    /**
     * 中序遍历 左-》中-》右
     * <p>
     * 步骤：
     * <p>
     * 1. 如果当前节点的左孩子为空，则输出当前节点并将其右孩子作为当前节点。
     * 左孩子为空，就可以访问当前节点了，接下来是右孩子
     * <p>
     * 2. 如果当前节点的左孩子不为空，在当前节点的左子树中找到当前节点在中序遍历下的前驱节点。(左子树的最右孩子)
     * <p>
     * a) 如果前驱节点的右孩子为空(是叶子节点)，将它的右孩子设置为当前节点。当前节点更新为当前节点的左孩子。
     * <p>
     * b) 如果前驱节点的右孩子为当前节点，将它的右孩子重新设为空（恢复树的形状）。输出当前节点。当前节点更新为当前节点的右孩子。
     * <p>
     * 第一次访问root的时候，我找到了它的prev，此时prev是个叶子节点它的右孩子为空，我设置 prev->right = root
     * 然后我 root = root -> left，因为中序是先访问左孩子，再访问自己
     * 当我再次访问到root，找到他的prev，发现它的prev的右孩子已经是自己的时候，说明我已经把左子树遍历完毕，又重新走会回到自己了
     * 这个时候，我应该访问自己，然后把prev->再设为null，也就是说恢复它叶子节点的身份，恢复这个树本来的形状
     * 然后继续去访问我的右子树
     * <p>
     * 3. 重复以上1、2直到当前节点为空。
     *
     * @param root
     */
    public void inorderTraversal(TreeNode root) {

        // 保存当前节点
        TreeNode curr = root;

        while (curr != null) {
            // 如果当前节点左孩子不为空，就得先处理左孩子
            if (curr.left != null) {
                // 当前节点在中序遍历规则下的前驱
                // 找到中序遍历下的前驱(左子树的最右孩子)
                TreeNode prev = curr.left;
                while (prev.right != null && prev.right != curr) {
                    prev = prev.right;
                }
                // 第一次访问curr时，找到它的前驱prev，是个叶子节点，没有孩子
                if (prev.right == null) {
                    // 链接上
                    prev.right = curr;
                    // 先去访问左子树
                    curr = curr.left;
                // prev.right == curr 说明我已经是第二次访问到curr了，
                } else {
                    // 也就是说，我的左子树已经访问完毕了
                    // 把自己加上的链接拆掉，恢复左半边树的结构
                    prev.right = null;
                    // 访问自己
                    System.out.println(curr.val + " ");
                    // 继续去访问右子树
                    curr = curr.right;
                }
            } else {
                // 当前节点左孩子为空，访问当前节点，并继续访问右孩子
                System.out.println(curr.val + " ");
                curr = curr.right;
            }
        }

    }

    /**
     * 二、前序遍历 左-》中-》右
     * <p>
     * 前序遍历与中序遍历相似，代码上只有一行不同，不同就在于输出的顺序。
     * <p>
     * 步骤：
     * <p>
     * 1. 如果当前节点的左孩子为空，则输出当前节点，并将其右孩子作为当前节点。
     * <p>
     * 2. 如果当前节点的左孩子不为空，在当前节点的左子树中找到当前节点前驱节点。
     * <p>
     * a) 如果前驱节点的右孩子为空，将它的右孩子设置为当前节点。【输出当前节点（在这里输出，这是与中序遍历唯一一点不同）】。当前节点更新为当前节点的左孩子。
     * 就是因为前序是先访问自己，所以在这里输出
     * 中序是先访问左孩子，所以在这里处理，第二次回到自己的时候，才访问自己
     * <p>
     * b) 如果前驱节点的右孩子为当前节点，将它的右孩子重新设为空。当前节点更新为当前节点的右孩子。
     * 第二次回到自己，说明 自己遍历完成，自己的左孩子也遍历完成，该遍历右孩子了
     * <p>
     * 3. 重复以上1、2直到当前节点为空。
     */

    // Mirrios前序遍历法
    public void preorderTraversal(TreeNode root) {
        TreeNode curr = root;
        while (curr != null) {
            // 左孩子为空，不用处理，访问自己，访问右孩子
            if (curr.left == null) {
                System.out.println(curr.val + " ");
                curr = curr.right;
            // 左孩子不为空，需要处理
            } else {
                // 当前节点在前序遍历规则下的先驱（还是它的左孩子的最右孩子）
                TreeNode prev = curr.left;
                while (prev.right != null && prev.right != curr)
                    prev = prev.right;
                // 第一次到当前节点（找到前驱）
                if (prev.right == null) {
                    // 链接上
                    prev.right = curr;
                    // 根据前序遍历规则，先访问自己,【唯一区别于中序遍历的地方】
                    System.out.println(curr.val + " ");
                    // 再去访问左孩子
                    curr = curr.left;
                // prev.right == curr
                } else {
                    // 第二次到自己，说明左孩子已经遍历完了
                    // 去遍历右孩子
                    curr = curr.right;
                    // 把链接去掉，恢复树的结构
                    prev.right = null;
                }
            }
        }
    }

    /**
     * 后序遍历
     * <p>
     * 后序有点复杂，它的访问顺序是 左右中，也就是说，它需要从右子树回到自己
     * 而我们利用的是前驱节点原本是null的右孩子,【这也决定了我们所找的前驱的一定是当前节点的左子树的最右孩子】，
     * 所以我们只能从左子树回到自己，
     *
     * 为了解决后序的问题，我们需要一个临时节点，访问当前节点时，把它作为这个临时节点的左孩子，以临时节点开始去进行访问，
     * 临时节点的左子树能被完全访问，也就保证了原本的curr能被访问
     * <p>
     * 步骤：
     * 建立一个临时结点dump,令其左孩子是root。
     * 当前结点设置为临时结点dump，从dump开始。
     * while 结点存在:
     * if 左子树存在:
     * 找到左子树的最右结点，#这个最右结点也就是当前结点在中序遍历下的前驱结点
     * 如果发现前驱为空:
     * 该结点右指针指向当前结点
     * 否则如果最右结点的右指针已经指向当前结点:
     * 【倒序输出当前结点的左孩子到该前驱节点这条路径上的所有结点】。
     * else
     * 左子树不存在就转到右孩子结点
     */

    public void postorderTraversal(TreeNode root) {
        // 临时节点
        TreeNode temp = new TreeNode(0);
        // 把root作为它的左孩子
        temp.left = root;
        // 以temp为开始，继续套路
        TreeNode curr = temp;
        while (curr != null) {
            // 左孩子为空
            if (curr.left == null) {
                // 直接访问呢右孩子
                curr = curr.right;
            } else {
                // 左孩子不为空
                TreeNode prev = curr.left;
                // 找到curr的前驱
                while (prev.right != null && prev.right != curr)
                    prev = prev.right;
                // 如果前驱右指针未被改变,// 第一次到当前节点（找到前驱）
                if (prev.right == null) {
                    // 让它指向自己
                    prev.right = curr;
                    // 去处理左子树
                    curr = curr.left;
                } else {
                    // 前驱右指针已指向自己，说明是第二次到自己
                    // 倒序输出当前结点的左孩子到该前驱节点这条路径上的所有结点
                    reverse(curr.left, prev);
                    print(prev, curr.left);
                    // 倒序完再恢复
                    reverse(prev, curr.left);

                    // 再把前驱的右指针恢复为null，恢复树结构
                    prev.right = null;
                    // 进入右子树
                    curr = curr.right;
                }
            }
        }
    }

    // 反转链表 start -> x -> y -> z -> end
    private void reverse(TreeNode startNode, TreeNode endNode) {
        if (startNode == endNode) return;
        TreeNode prev = startNode, curr = startNode.right, next = null;
        while (prev != endNode) {
            next = curr.right;
            curr.right = prev;
            prev = curr;
            curr = next;
        }
    }

    // 打印链表 start -> x -> y -> z -> end
    private void print(TreeNode startNode, TreeNode endNode) {
        TreeNode curr = startNode;
        while (curr != endNode) {
            System.out.print(curr.val + " ");
            curr = curr.right;
        }
        System.out.println(curr.val);
    }
}
