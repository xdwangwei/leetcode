package com.common;

/**
 * @author wangwei
 * 2020/7/27 9:35
 */
public class ListNode {

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode(int x) { val = x; }
     * }
     */

    public int val;
    public ListNode next;

    public ListNode(int x) {
        val = x;
        next = null;
    }

    public ListNode(int x, ListNode next) {
        val = x;
        this.next = next;
    }

    public static ListNode buildFromArray(int[] arr) {
        assert arr != null && arr.length > 0;
        ListNode head = new ListNode(-1), cur = head;
        for (int num : arr) {
            cur.next = new ListNode(num);
            cur = cur.next;
        }
        return head.next;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        ListNode cur = this;
        while (cur != null) {
            if (cur.next == null) {
                builder.append(cur.val + "]");
                break;
            }
            builder.append(cur.val + ", ");
            cur = cur.next;
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        ListNode list = ListNode.buildFromArray(new int[]{1, 3, 5, 2, 4});
        System.out.println(list);
        System.out.println(list);
    }
}
