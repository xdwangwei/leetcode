package com.common;

/**
 * @author wangwei
 * 2020/9/1 17:37
 */
// 双向链表中的节点
public class DNode {
    public int key, val;
    DNode prev, next;

    public DNode(final int key, final int val) {
        this.key = key;
        this.val = val;
    }
}
