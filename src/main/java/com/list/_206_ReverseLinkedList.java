package com.list;

import com.common.ListNode;

/**
 * @author wangwei
 * 2020/7/26 8:24
 *
 * 206. 反转链表
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
 *
 *
 * 示例 1：
 *
 *
 * 输入：head = [1,2,3,4,5]
 * 输出：[5,4,3,2,1]
 * 示例 2：
 *
 *
 * 输入：head = [1,2]
 * 输出：[2,1]
 * 示例 3：
 *
 * 输入：head = []
 * 输出：[]
 *
 *
 * 提示：
 *
 * 链表中节点的数目范围是 [0, 5000]
 * -5000 <= Node.val <= 5000
 *
 *
 * 进阶：链表可以选用迭代或递归方式完成反转。你能否用两种方法解决这道题？
 */
@SuppressWarnings("ALL")
public class _206_ReverseLinkedList {


    /**
     * 反转链表
     * @param head
     * @return
     */
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
