package com.offerassult;

import com.common.ListNode;

/**
 * @author wangwei
 * @date 2022/5/24 9:25
 * @description: _024_ReverseList
 * 剑指 Offer II 024. 反转链表
 * 给定单链表的头节点 head ，请反转链表，并返回反转后的链表的头节点。
 *
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
 *
 *
 *
 * 注意：本题与主站 206 题相同： https://leetcode-cn.com/problems/reverse-linked-list/
 *
 */
public class _024_ReverseList {


    /**
     * 方法一：递归
     *
     * 理解反转只是next指针反向
     *
     * 要反转 1->2->3->4->5，反转结果应该是 1<-2<-3<-4<-5
     * 可以看作 先反转 head.next开始的部分，即先反转 2->3->4->5 这部分，反转结果是 2<-3<-4<-5
     * 并且此时head.next指向的还是2，也就是此时链表是 1->2<-3<-4<-5
     * 需要做的就是把1->2之间的指针再反向，再把链表末尾置空
     * head.next.next = head; head.next = null
     * 最后返回新的头节点5
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        // 空链表或者只有一个节点，不用反转
        if (head == null || head.next == null) {
            return head;
        }
        // 先反转head.next开始部分 2->3->4->5
        ListNode newHead = reverseList(head.next);
        // 此时链表是 1->2<-3<-4<-5
        // 需要做的就是把1->2之间的指针再反向，再把链表末尾置空
        head.next.next = head;
        head.next = null;
        // 返回新的头节点5
        return newHead;
    }


    /**
     * 迭代法：逐个反转next指针即可
     * @param head
     * @return
     */
    public ListNode reverseList2(ListNode head) {
        // 空链表或者只有一个节点，不用反转
        if (head == null || head.next == null) {
            return head;
        }
        ListNode prev = null, cur = head, next = null;
        while (cur != null) {
            // 保存cur.next
            next = cur.next;
            // 反转prev 和 cur之间的指向关系
            cur.next = prev;
            // prev指针移动
            prev = cur;
            // cur移动
            cur = next;
        }
        // while退出后，cur==null,最后一个节点是prev
        // 因此指针已全部反向，所以此时prev就是新的首节点
        return prev;
    }
}
