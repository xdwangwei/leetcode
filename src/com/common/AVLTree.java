package com.common;

import java.nio.BufferUnderflowException;

/**
 * @author wangwei
 *
 * 自己实现 AVL 树
 * 2020/9/5 8:30
 */
public class AVLTree<AnyType extends Comparable<? super AnyType>> {

    // 左右子树高度差阈值
    private static final int ALLOWED_IMBALANCE = 1;

    // AVL树节点定义
    private static class AvlNode<AnyType> {

        AnyType element; // 节点元素值
        AvlNode<AnyType> left; // 节点左孩子
        AvlNode<AnyType> right; // 节点右孩子
        int height; // 树高

        // 构造函数
        public AvlNode(final AnyType element) {
            this(element, null, null);
        }

        public AvlNode(final AnyType element, final AvlNode<AnyType> left, final AvlNode<AnyType> right) {
            this.element = element;
            this.left = left;
            this.right = right;
            this.height = 0;
        }
    }

    // 根节点
    private AvlNode<AnyType> root;

    // 构造方法
    public AVLTree() {
        root = null;
    }

    // 置为空树
    public void makeEmpty() {
        root = null;
    }

    // 判断树空
    public boolean isEmpty() {
        return root == null;
    }

    // 判断树中是否包含值为x的节点
    public boolean contains(AnyType x) {
        return contains(root, x);
    }

    // 判断树中是否包含值为x的节点
    public boolean contains(AvlNode<AnyType> t, AnyType x) {
        if (t == null) return false;
        // 判断目标值与当前树根的大小关系
        int res = x.compareTo(t.element);
        // 去左子树中寻找
        if (res < 0) {
            return contains(t.left, x);
            // 去右子树中寻找
        } else if (res > 0) return contains(t.right, x);
        // 当前节点就是目标节点
        return true;
    }

    // 获取树高
    public int height(AvlNode<AnyType> t) {
        return t == null ? -1 : t.height;
    }

    // 向树中插入节点x
    public void insert(AnyType x) {
        root = insert(root, x);
    }

    // 向树中插入节点x
    private AvlNode<AnyType> insert(AvlNode<AnyType> t, AnyType x) {
        if (t == null) return new AvlNode<>(x, null, null);
        // 判断插入位置
        int res = x.compareTo(t.element);
        // 在左子树中插入，新树成为左子树
        if (res < 0) {
            t.left = insert(t.left, x);
            // 在右子树中插入，新树成为右子树
        } else if (res > 0)
            t.right = insert(t.right, x);
        else
            ; // 此节点已存在，无动作
        // 插入之后平衡
        return balance(t);
    }

    // 从树中删除节点x
    public void remove(AnyType x) {
        root = remove(root, x);
    }

    // 从树中删除节点x
    private AvlNode<AnyType> remove(AvlNode<AnyType> t, AnyType x) {
        if (t == null) return null;
        // 判断节点位置
        int res = x.compareTo(t.element);
        // 在左子树中删除，新树成为左子树
        if (res < 0) t.left = remove(t.left, x);
            // 在右子树中删除，新树成为右子树
        else if (res > 0) t.right = remove(t.right, x);
            // 当前节点就是要删除的节点
            // 如果左右孩子都不空
        else if (t.left != null && t.right != null) {
            // 找到右子树中的最小节点或左子树中的最大节点代替自己
            t.element = findMin(t.right).element;
            // 去右子树中删除原来的最小节点，更新右子树
            t.right = remove(t.right, t.element);
        }
        // 删除自己，有一个孩子为空，就返回另一个孩子，两个都为空就返回null
        else t = (t.left == null) ? t.right : t.left;
        // 删除后平衡
        return balance(t);
    }

    // 返回树中元素值最小的那个节点
    public AnyType findMin() {
        if (isEmpty()) throw new BufferUnderflowException();
        return findMin(root).element;
    }

    // 返回树中元素值最小的那个节点
    private AvlNode<AnyType> findMin(AvlNode<AnyType> t) {
        if (t == null) return null;
        // 最左孩子
        if (t.left == null) return t;
        return findMin(t.left);
    }

    // 返回子树x中元素值最大的节点
    public AnyType findMax() {
        if (isEmpty()) throw new BufferUnderflowException();
        return findMax(root).element;
    }

    // 返回子树x中元素值最大的节点
    private AvlNode<AnyType> findMax(AvlNode<AnyType> t) {
        if (t != null) {
            while (t.right != null) {
                t = t.right;
            }
        }
        return t;
    }

    // 平衡、
    // 1. 左左插。右旋
    // 2. 左右插。先绕左孩子左旋，再绕自己右旋
    // 3. 右左插。先绕右孩子右旋，再绕自己左旋
    // 4. 右右插。左旋
    private AvlNode<AnyType> balance(AvlNode<AnyType> t) {
        if (t == null) return t;
        int lh = height(t.left), rh = height(t.right);
        // 左子树更高
        if (lh - rh > ALLOWED_IMBALANCE) {
            // 左子树的左孩子比右孩子更高，左左，记得接收结果
            if (height(t.left.left) >= height(t.left.right)) {
                t = rotateWithLeftChild(t);
            // 左子树的右孩子比左孩子更高，左右，记得接收结果
            } else {
                t = doubleRotateWithLeftChild(t);
            }
        // 右子树更高
        } else if (rh - lh > ALLOWED_IMBALANCE) {
            // 右子树的右孩子比左孩子更高，右右，记得接收结果
            if (height(t.right.right) >= height(t.right.left)) {
                t = rotateWithRightChild(t);
            // 右子树的左孩子比右孩子更高，右左，记得接收结果
            } else {
                t = doubleRotateWithRightChild(t);
            }
        }
        // 平衡完后更新树高
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    // 处理左左插失去失去平衡，【右】旋
    private AvlNode<AnyType> rotateWithLeftChild(AvlNode<AnyType> k2) {
        // 以它的左孩子为中心右旋
        AvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        // 更新树高，k1是根，所以先更新k2
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        // 旋转完后k1是根
        return k1;
    }

    // 处理右右插失去失去平衡，【左】旋
    private AvlNode<AnyType> rotateWithRightChild(AvlNode<AnyType> k2) {
        // 以它的右孩子为中心左旋
        AvlNode<AnyType> k1 = k2.right;
        k2.right = k1.left;
        k1.left = k2;
        // 更新树高，k1是根，所以先更新k2
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        // 旋转完后k1是根
        return k1;
    }

    // 处理左右插
    private AvlNode<AnyType> doubleRotateWithLeftChild(AvlNode<AnyType> k3) {
        // 先对左子树左旋，转为左左，再对自己进行右旋
        rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    // 处理右左插
    private AvlNode<AnyType> doubleRotateWithRightChild(AvlNode<AnyType> k3) {
        // 先对右子树右旋，转为右右，再对自己进行左旋
        rotateWithLeftChild(k3.right);
        return rotateWithRightChild(k3);
    }

    // 树的遍历
    public void printTree() {
        if (isEmpty()) System.out.println("Empty tree");
        else printTree(root);
    }

    // 中序遍历
    private void printTree(AvlNode<AnyType>  t) {
        if (t != null) {
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();
        for (int i = 0; i < 7; i++) {
            tree.insert(i);
        }
        tree.printTree();
    }
}
