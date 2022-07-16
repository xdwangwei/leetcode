package com.window;

import com.common.ListNode;

/**
 * @author wangwei
 * 2020/8/30 22:38
 *
 *
给定一个排序链表，删除所有重复的元素，使得每个元素只出现一次。

示例 1:

输入: 1->1->2
输出: 1->2
示例 2:

输入: 1->1->2->3->3
输出: 1->2->3
 */
public class _83_RemoveDuplicatesFromSortedList {

    /**
     * 快慢指针，和 _26_删除排序数组中的重复元素思路一样
     * 因为已排序，所以重复元素一定是连在一起的
     * 让快指针fast在前面探路，慢指针slow走左后面，
     * fast每发现一个不重复元素，就让slow的下一个指向这个不重复节点，然后slow前进一步
     * @param head
     * @return
     */
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) return head;
        ListNode fast = head.next, slow = head;
        while (fast != null) {
            // 找到一个不重复元素，让slow的下一个指向它
            if (fast.val != slow.val) {
                slow.next = fast;
                // slow前进一步
                slow = slow.next;
            }
            // 快指针一直前进
            fast = fast.next;
        }
        // 断开与后面重复元素的连接
        slow.next = null;
        return head;
    }
}
