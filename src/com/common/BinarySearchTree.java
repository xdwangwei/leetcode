package com.common;

import java.nio.BufferUnderflowException;

/**
 * @author wangwei
 * 2020/9/5 8:10
 *
 * 自己实现的二叉搜索树
 */
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>> {

    // 二叉搜索树节点类定义
    private static class BinaryNode<AnyType> {

        AnyType element; // 节点元素值
        BinarySearchTree.BinaryNode<AnyType> left; // 节点左孩子
        BinarySearchTree.BinaryNode<AnyType> right; // 节点右孩子

        // 构造函数
        public BinaryNode(final AnyType element) {
            this(element, null, null);
        }

        public BinaryNode(final AnyType element, final BinarySearchTree.BinaryNode<AnyType> left, final BinarySearchTree.BinaryNode<AnyType> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

    private BinaryNode<AnyType> root; // 根节点

    // 构造方法
    public BinarySearchTree() {
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
    public boolean contains(BinaryNode<AnyType> t, AnyType x) {
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

    // 返回树中元素值最小的那个节点
    public AnyType findMin() {
        if (isEmpty()) throw new BufferUnderflowException();
        return findMin(root).element;
    }
    // 返回子树x中元素值最小的那个节点
    private BinaryNode<AnyType> findMin(BinaryNode<AnyType> t) {
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
    private BinaryNode<AnyType> findMax(BinaryNode<AnyType> t) {
        if (t != null) {
            while (t.right != null) {
                t = t.right;
            }
        }
        return t;
    }

    // 向树中插入节点x
    public void insert(AnyType x) {
        root = insert(root, x);
    }
    // 向树中插入节点x
    private BinaryNode<AnyType> insert(BinaryNode<AnyType> t, AnyType x) {
        if (t == null) return new BinaryNode<>(x, null, null);
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
        return t;
    }

    // 从树中删除节点x
    public void remove(AnyType x) {
        root = remove(root, x);
    }
    // 从树中删除节点x
    private BinaryNode<AnyType> remove(BinaryNode<AnyType> t, AnyType x) {
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
        // 返回最终树
        return t;
    }
    // 树的遍历
    public void printTree() {
        if (isEmpty()) System.out.println("Empty tree");
        else printTree(root);
    }

    // 中序遍历
    private void printTree(BinaryNode<AnyType> t) {
        if (t != null) {
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        System.out.println(tree.isEmpty());
        tree.insert(5);
        tree.insert(1);
        tree.insert(6);
        tree.insert(2);
        tree.insert(3);
        tree.insert(8);
        tree.insert(0);
        System.out.println(tree.isEmpty());
        System.out.println(tree.contains(9));
        System.out.println(tree.contains(2));
        tree.printTree();
        tree.remove(6);
        tree.printTree();
        tree.remove(1);
        tree.remove(3);
        tree.remove(7);
        tree.printTree();
    }

}
