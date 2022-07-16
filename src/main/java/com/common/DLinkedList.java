package com.common;

/**
 * @author wangwei
 * 2020/7/31 8:46
 * <p>
 * 自己实现双向列表
 * <p>
 * 在双向链表的实现中，使用一个伪头部（dummy head）和伪尾部（dummy tail）标记界限，
 * 这样在添加节点和删除节点的时候就不需要检查相邻的节点是否存在。
 */
public class DLinkedList {

    // 伪头部和伪尾部
    private DNode head, tail;

    // 链表元素数
    private int size;

    // 构造函数
    public DLinkedList() {
        head = new DNode(-1, -1);
        tail = new DNode(-1, -1);
        // 初始化伪头部和伪尾部
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    /**
     * 在尾部（伪尾部之前）插入一个新节点 时间 O(1)
     *
     * @param DNode
     */
    public void addLast(DNode DNode) {
        DNode.prev = tail.prev;
        DNode.next = tail;
        tail.prev.next = DNode;
        tail.prev = DNode;
        size++;
    }

    /**
     * 删除链表中的 x 节点（x 一定存在）
     * 由于是双链表且给的是目标 Node 节点，时间 O(1)
     *
     * @param DNode
     */
    public void remove(DNode DNode) {
        DNode.prev.next = DNode.next;
        DNode.next.prev = DNode.prev;
        size--;
    }

    /**
     * 删除链表中第一个节点，并返回该节点，时间 O(1)
     *
     * @return
     */
    public DNode removeFirst() {
        if (head.next == tail)
            return null;
        DNode first = head.next;
        remove(first);
        return first;
    }

    /**
     * 返回链表长度，时间 O(1)
     *
     * @return
     */
    public int size() {
        return size;
    }

}
