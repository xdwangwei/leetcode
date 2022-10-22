package com.offerassult;

import com.common.ListNode;

/**
 * @author wangwei
 * @date 2022/10/22 14:10
 * @description: _027_PalindromeLinkedList
 *
 * 剑指 Offer II 027. 回文链表
 * 给定一个链表的 头节点 head ，请判断其是否为回文链表。
 *
 * 如果一个链表是回文，那么链表节点序列从前往后看和从后往前看是相同的。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入: head = [1,2,3,3,2,1]
 * 输出: true
 * 示例 2：
 *
 *
 *
 * 输入: head = [1,2]
 * 输出: false
 *
 *
 * 提示：
 *
 * 链表 L 的长度范围为 [1, 105]
 * 0 <= node.val <= 9
 *
 *
 * 进阶：能否用 O(n) 时间复杂度和 O(1) 空间复杂度解决此题？
 *
 *
 *
 * 注意：本题与主站 234 题相同：https://leetcode-cn.com/problems/palindrome-linked-list/
 */
public class _027_PalindromeLinkedList {

    /**
     * 借助快慢指针思想，先找到列表的中间节点，然后反转中间节点及之后部分，然后匹配
     * 空间复杂度 O(1)
     *
     * 寻找中间节点：快指针每次走两步，慢指针每次走一步，当快指针走完时，返回slow，这种情况下
     *          fast = head, slow = head;
     *          while (fast != null && fast.next != null) {
     *             fast = fast.next.next;
     *             slow = slow.next;
     *         }
     *
     *      * 如果有偶数个节点，1->2->3->5->null 结束后 fast = null，slow = 3，此时 slow及后半部分与前半部分长度一致
     *      * 如果有奇数个节点，1->2->3->4->5->null 结束后 fast = 5 != null，slow = 3；此时 slow及后半部分比前半部分多了一个节点
     *                                  但是，只要让slow后移一个节点，就可以让两部分一样长
     *                主要是考虑链表节点个数为奇数和偶数时，如何调整，让两部分长度相等
     *                              那么如何区分这两种情况呢
     *                        偶数节点，退出时，fast == null
     *                        奇数节点，退出时：fast.next = null 但 fast != null，此时 让slow再前进一步
     *
     */
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;
        ListNode fast = head, slow = head;
        // 快慢指针找到最中间的节点
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        // 此时，说明有奇数个节点，slow需要后移一个
        if (fast != null) slow = slow.next;
        // 反转slow及之后的列表节点
        ListNode left = head, right = reverseList(slow);
        // 判断两部分是否对称
        while (right != null) {
            if (left.val != right.val) return false;
            left = left.next;
            right = right.next;
        }
        return true;
    }

    /**
     * 反转以head开始的列表，返回反转后的首节点(反转前的最后一个)
     * @param head
     * @return
     */
    private ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode pre = null, cur = head, nxt;
        while (cur != null) {
            nxt = cur.next;
            // 指针反向
            cur.next = pre;
            // 后移
            pre = cur;
            cur = nxt;
        }
        return pre;
    }
}
