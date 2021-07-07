package com.list;

import com.common.ListNode;

/**
 * @author wangwei
 * 2020/7/26 8:24
 *
 *
反转一个单链表。

示例:

输入: 1->2->3->4->5->NULL
输出: 5->4->3->2->1->NULL
进阶:
你可以迭代或递归地反转链表。你能否用两种方法解决这道题？
 */
@SuppressWarnings("ALL")
public class _206_ReverseLinkedList {

    public ListNode reverseList(ListNode head) {
        // return reverseList1(head);
        if (head == null) return head;
        return reverseList2(head);
    }

    /**
     * 迭代法
     * @param head
     * @return
     */
    public ListNode reverseList1(ListNode head) {
        // 将原链表节点一个一个拆下来，采用头插法往tail前面放，tail向前移
        ListNode pre = null, cur = head, nxt = null;
        while (cur != null) {
            nxt = cur.next;
            // 指针反向
            cur.next = pre;
            pre = cur;
            cur = nxt;
        }
        return pre;
    }

    /**
     * 递归，反转以head开始的链表，并返回反转后的头结点
     *
     * 比如反转 1->2->3->4->5->null
     * 就可以先把 2->3->4->5->null反转成 5->4->3->2->null
     * 然后再把 head 1 接到后面
     *
     * 反转之前，head=1,head.next=2,
     * 所以反转后 head.next.next = head;就能把1接到2后面
     * head.next = null; head成为最后一个节点后，就要把next置为null
     */
    public ListNode reverseList2(ListNode head) {
        // 递归出口，如果只有一个节点，直接返回
        if (head.next == null) return head;
        // 反转head后面部分
        ListNode last = reverseList2(head.next);
        // 把原来的头链接到尾部
        head.next.next = head;
        // 最后一个节点的next置为null
        head.next = null;
        // 返回反转后新连表的首节点
        return last;
    }
}
