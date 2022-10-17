package com.mianshi.year2022.tencent;

import com.common.ListNode;

/**
 * @author wangwei
 * @date 2022/10/16 19:57
 * @description: _Oct16_Main1
 *
 * a 和 b 的二进制表示，分别存储在两个链表中
 * a在链表1中，最高位在头节点，其他位按顺序往后
 * b在链表2中，最高位在尾节点，其他位按顺序往前
 * 想计算a^b,保存在链表中，要求最高位在链表头，并且不包含前导0
 */
public class _Oct16_Main1 {

    public ListNode xorList (ListNode a, ListNode b) {
        // write code here
        a = reverse(a);
        ListNode p1 = a, p2 = b;
        ListNode p3 = new ListNode(-1), cur = p3;
        while (p1 != null || p2 != null) {
            int a1 = p1 == null ? 0 : p1.val;
            int b1 = p2 == null ? 0 : p2.val;
            ListNode node = new ListNode(a1 ^ b1);
            cur.next = node;
            cur = node;
            if (p1 != null) p1 = p1.next;
            if (p2 != null) p2 = p2.next;
        }
        ListNode h = reverse(p3.next);
        while (h != null && h.val == 0) {
            h = h.next;
        }
        return h;
    }

    private ListNode reverse(ListNode h) {
        if (h == null || h.next == null) {
            return h;
        }
        ListNode nh = reverse(h.next);
        h.next.next = h;
        h.next = null;
        return nh;
    }
}
