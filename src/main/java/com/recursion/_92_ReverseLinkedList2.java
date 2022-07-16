package com.recursion;

/**
 * @author wangwei
 * 2020/7/26 8:22
 *
 * 92. 反转链表 II
 * 反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。
 *
 * 说明:
 * 1 ≤ m ≤ n ≤ 链表长度。
 *
 * 示例:
 *
 * 输入: 1->2->3->4->5->NULL, m = 2, n = 4
 * 输出: 1->4->3->2->5->NULL
 */
@SuppressWarnings("ALL")
public class _92_ReverseLinkedList2 {

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode(int x) { val = x; }
     * }
     */
    class ListNode {
          int val;
          ListNode next;
          ListNode(int x) { val = x; }
    }

    /**
     * 反转以head开始的链表的第M到第N个节点
     * 对于以head开始，那就是第m到第n，
     * 对于以head.next开始，就是第m-1到第n-1
     * 对于以head.next.next开始，就是第m-2到第n-2
     * 对于以head.next.next. ...开始，就是第1到第n-x
     * 此时就相当于反转以head.next.next. ...开始的前N个节点
     * @param head
     * @param m
     * @param n
     * @return
     */
    public ListNode reverseBetween(ListNode head, int m, int n) {
        // 递归出口 base case
        if (m == 1) return reverseListN(head, n);
        // 前进到反转的起点触发 base case
        head.next = reverseBetween(head.next, m - 1, n - 1);
        return head;
    }

    /**
     * 反转以head开始的全部节点，返回反转后新链表的第一个节点
     * @param head
     * @return
     */
    public ListNode reverseListAll(ListNode head) {
        // 递归出口，如果只有一个节点，直接返回
        if (head.next == null) return head;
        ListNode last = reverseListAll(head.next);
        // 把原来的头链接到尾部
        head.next.next = head;
        // 最后一个节点的next置为null
        head.next = null;
        // 返回反转后新连表的首节点
        return last;
    }

    /**
     * 反转以head开始的N个节点，返回反转后新链表的第一个节点
     * 如果是反转全部节点，反转完后head就成了最后一个节点，要把他的next置为null
     * 只反转前N个，反转完后应该把head的next置为第N个节点的下一个节点
     * @param head
     * @return
     */
    // 保存第n个节点之后的那个节点
    ListNode next = null;
    public ListNode reverseListN(ListNode head, int n) {
        // 递归出口，如果只有一个节点（第N个节点），直接返回自己，
        if (n == 1) {
            // 保存第N+1个节点
            next = head.next;
            return head;
        }
        // 反转前N个节点
        ListNode last = reverseListN(head.next, n - 1);
        // 把原来的头链接到尾部
        head.next.next = head;
        // 最后一个节点的next置为 第N+1个节点
        head.next = next;
        // 返回反转后新连表的首节点
        return last;
    }

    /**
     * 迭代
     * 反转第[m,n]
     * con 保存第m-1个节点
     * tail保存第m个节点，也就是要反转的这一部分反转之后的最后一个节点
     * 反转[m,n]需要 prev,cur,third三个指针，反转之后，
     * x->1->2->3->y      x  1<-2<-3  y
     * 反转之后 prev = 3, cur = y, tail = 1, con = x
     */
    public ListNode reverseBetween1(ListNode head, int m, int n) {
        if (head == null) return null;
        // 找到第m-1个节点
        ListNode con = null, tail = null, prev = null, cur = head, third = null;
        // 对于head来说是[m,n] 对于head.next来说是[m-1,n-1]
        while (m > 1) {
            prev = cur;
            cur = cur.next;
            m--;
            n--;
        }
        // 此时m=1,cur就是第一个要反转的节点，也就是反转后最后一个节点
        // 保存前面的那个节点con和tail
        con = prev; tail = cur;
        // 反转n个节点
        while (n-- >0) {
            // 1->2->3
            third = cur.next;
            // 1<-2 3
            cur.next = prev;
            prev = cur;
            cur = third;
        }
        // x->1->2->3->y      x  1<-2<-3  y
        // 反转之后 prev = 3, cur = y, tail = 1, con = x
        // 注意处理con=null也就是head就是第一个要反转的节点的情况，也就是m=1
        // 这种情况下prev直接成了头结点
        if (con == null) head = prev;
        // 否则，prev应该跟在con的后面
        else con.next = prev;
        // 反转后的这部分 接上原链表后面部分
        tail.next = cur;
        return head;
    }
}
