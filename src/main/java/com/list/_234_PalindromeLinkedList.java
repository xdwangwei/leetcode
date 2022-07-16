package com.list;

import com.common.ListNode;

/**
 * @author wangwei
 * 2020/8/31 20:52
 * 234. 回文链表
 * 请判断一个链表是否为回文链表。
 *
 * 示例 1:
 *
 * 输入: 1->2
 * 输出: false
 * 示例 2:
 *
 * 输入: 1->2->2->1
 * 输出: true
 * 进阶：
 * 你能否用 O(n) 时间复杂度和 O(1) 空间复杂度解决此题？
 *
 */
public class _234_PalindromeLinkedList {

    /**
     * 方法一：可以构造一个原列表的逆序列表，然后逐个对比节点值，空间复杂度 O(n)
     * 方法二：借助后续遍历的思想实现类似栈的功能，模仿双指针实现回文判断的功能
     * @param head
     * @return
     */
    ListNode left; // 首指针
    public boolean isPalindrome12(ListNode head) {
        left = head;
        return postTraverse(head);
    }
    private boolean postTraverse(ListNode right) {
        if (right == null) return true;
        // 后续遍历代码，先递归到列表末尾，然后left和right指针开始匹配
        boolean res = postTraverse(right.next);
        res = res && left.val == right.val;
        // 匹配完后，递归返回上一层(倒数第二个节点)，left前进到(第二个节点)
        left = left.next;
        // 返回当前匹配结果
        return res;
    }

    /**
     * 方法三：借助快慢指针思想，先找到列表的中间节点，然后反转中间节点及之后部分，然后匹配
     * 空间复杂度 O(1)
     *
     * 快指针每次走两步，慢指针每次走一步
     *
     * 初始  fast = head, slow = head;
     *
     * 如果有偶数个节点，1->2->3->5->null 结束后 fast = null，slow = 3
     * 如果有奇数个节点，1->2->3->4->5->null 结束后 fast = 5 != null，slow = 3
     *      因为奇数情况下最中间节点不需要考虑，所以此时slow需要继续后移一步
     *
     *  然后返回slow往后的列表 分别得到  1->2  null<-3<-5         1->2 3 null<-4<-5
     *
     */
    public boolean isPalindrome3(ListNode head) {
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
        if (head == null) return head;
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
