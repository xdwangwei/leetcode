package com.window;

import com.common.ListNode;

import java.util.HashSet;

/**
 * @author wangwei
 * 2020/7/27 9:34
 *
 * 给定一个链表，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
 *
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。
 *
 * 说明：不允许修改给定的链表。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：head = [3,2,0,-4], pos = 1
 * 输出：tail connects to node index 1
 * 解释：链表中有一个环，其尾部连接到第二个节点。
 *
 *
 * 示例 2：
 *
 * 输入：head = [1,2], pos = 0
 * 输出：tail connects to node index 0
 * 解释：链表中有一个环，其尾部连接到第一个节点。
 *
 *
 * 示例 3：
 *
 * 输入：head = [1], pos = -1
 * 输出：no cycle
 * 解释：链表中没有环。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/linked-list-cycle-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _142_LinkedListCycle2 {

    /**
     * hash表，时间复杂度O(n) 空间复杂度O(n)
     *
     * @param head
     * @return
     */
    public ListNode detectCycle(ListNode head) {
        // 只有一个节点
        if (head == null || head.next == null) return null;
        HashSet<ListNode> set = new HashSet<>();
        while (head != null) {
            // set中已有这个节点
            if (set.contains(head)) return head;
            else {
                set.add(head);
                head = head.next;
            }
        }
        return null;
    }

    /**
     * 双指针 时间复杂度 O(n) 空间复杂度 O(1)
     *
     * 先用双指针法判断链表是否有环，若有则返回第一次相遇的节点
     *
     * 第一次相遇时，假设【慢指针】 slow 走了 k 步，那么快指针 fast 一定走了 2k 步，
     * 也就是说比 slow 多走了 k 步（也就是环的长度）。
     *
     * 设【相遇点】距【环的起点】的距离为 m，
     *      那么【环的起点】距【头结点】 head 的距离为 k - m，（慢指针是第一次走到这）
     * 也就是说如果从 head 前进 k - m 步就能到达环起点。
     * 巧的是，如果从相遇点继续前进 k - m 步，也能再次到达环起点。
     *
     * 所以，我们让一个指针从起点出发，另一个指针从相遇点出发，二者速度相同，相遇时，就到达了环的起点
     *
     * @param head
     * @return
     */
    public ListNode detectCycle2(ListNode head) {
        // 找到快慢指针相遇的节点
        ListNode intersect = hasCycle(head);
        // 说明无环
        if (intersect == null) return null;
        // 一个指针从head出发，一个指针从相遇点出发，速度相同
        // 二者相遇时的地点，即为环的起点
        while (head != intersect) {
            head = head.next;
            intersect = intersect.next;
        }
        return head;
    }

    /**
     * 双指针法判断链表是否有环 时间复杂度 O(n) 空间复杂度 O(1)
     *
     * 有则返回快慢指针第一次相遇的节点，无则返回null
     *
     * @param head
     * @return
     */
    public ListNode hasCycle(ListNode head) {
        // 只有一个节点
        if (head == null || head.next == null) return null;
        // 同时出发
        ListNode fast = head, slow = head;
        // 快指针速度是慢指针二倍
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            // 最终相遇
            if (fast == slow) return fast;
        }
        return null;
    }

}
