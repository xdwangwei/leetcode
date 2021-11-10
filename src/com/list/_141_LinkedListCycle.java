package com.list;

import com.common.ListNode;

import java.util.HashSet;

/**
 * @author wangwei
 * 2020/7/27 9:20
 * <p>
 * 给定一个链表，判断链表中是否有环。
 * <p>
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：head = [3,2,0,-4], pos = 1
 * 输出：true
 * 解释：链表中有一个环，其尾部连接到第二个节点。
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * 输入：head = [1,2], pos = 0
 * 输出：true
 * 解释：链表中有一个环，其尾部连接到第一个节点。
 * <p>
 * <p>
 * 示例 3：
 * <p>
 * 输入：head = [1], pos = -1
 * 输出：false
 * 解释：链表中没有环。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/linked-list-cycle
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _141_LinkedListCycle {

    /**
     * hash表，时间复杂度O(n) 空间复杂度O(n)
     *
     * @param head
     * @return
     */
    public boolean hasCycle1(ListNode head) {
        // 只有一个节点
        if (head == null || head.next == null) return false;
        HashSet<ListNode> set = new HashSet<>();
        while (head != null) {
            // set中已有这个节点
            if (set.contains(head)) return true;
            else {
                set.add(head);
                head = head.next;
            }
        }
        return false;
    }

    /**
     * 双指针 时间复杂度 O(n) 空间复杂度 O(1)
     * 两个速度不同的人在同一个赛道上跑步，最终速度快的会追上速度慢的，超过他一圈(第一次相遇)
     * 不妨让快指针的速度是慢指针速度的二倍
     *      如果有环，快指针会追上慢指针
     *      无环，快指针会跑到null
     * @param head
     * @return
     */
    public boolean hasCycle2(ListNode head) {
        // 只有一个节点
        if (head == null || head.next == null) return false;
        // 同时出发
        ListNode fast = head, slow = head;
        // 快指针速度是慢指针二倍
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            // 最终相遇(此时，fast领先slow一圈)
            if (fast == slow) return true;
        }
        return false;
    }
}
