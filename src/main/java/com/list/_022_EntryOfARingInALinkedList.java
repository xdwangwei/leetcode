package com.list;

import com.common.ListNode;

/**
 * @author wangwei
 * @date 2022/10/19 19:49
 * @description: _022_EntryOfARingInALinkedList
 *
 * 剑指 Offer II 022. 链表中环的入口节点
 * 给定一个链表，返回链表开始入环的第一个节点。 从链表的头节点开始沿着 next 指针进入环的第一个节点为环的入口节点。如果链表无环，则返回 null。
 *
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。注意，pos 仅仅是用于标识环的情况，并不会作为参数传递到函数中。
 *
 * 说明：不允许修改给定的链表。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：head = [3,2,0,-4], pos = 1
 * 输出：返回索引为 1 的链表节点
 * 解释：链表中有一个环，其尾部连接到第二个节点。
 * 示例 2：
 *
 *
 *
 * 输入：head = [1,2], pos = 0
 * 输出：返回索引为 0 的链表节点
 * 解释：链表中有一个环，其尾部连接到第一个节点。
 * 示例 3：
 *
 *
 *
 * 输入：head = [1], pos = -1
 * 输出：返回 null
 * 解释：链表中没有环。
 *
 *
 * 提示：
 *
 * 链表中节点的数目范围在范围 [0, 104] 内
 * -105 <= Node.val <= 105
 * pos 的值为 -1 或者链表中的一个有效索引
 *
 *
 * 进阶：是否可以使用 O(1) 空间解决此题？
 *
 *
 *
 * 注意：本题与主站 142 题相同： https://leetcode-cn.com/problems/linked-list-cycle-ii/
 */
public class _022_EntryOfARingInALinkedList {


    /**
     * 双指针 时间复杂度 O(n) 空间复杂度 O(1)
     *
     * 先用双指针法判断链表是否有环，若有则返回第一次相遇的节点
     *
     * fast 每次走 2 步， slow 每次走 1 步
     *
     * 第一次相遇时，假设【慢指针】 slow 走了 k 步，那么快指针 fast 一定走了 2k 步，
     * 也就是说比 slow 多走了 k 步（也就是环的长度）。
     *
     * 设【相遇点】距【环的起点】的距离为 m，
     *      那么【环的起点】距【头结点】 head 的距离为 k - m，（慢指针是第一次走到这）
     * 也就是说如果从 head 前进 k - m 步就能到达环起点。
     * 巧的是，如果从相遇点继续前进 k - m 步，也能再次到达环起点(画个图，结合fast和slow的2倍关系。很清楚)。
     *
     * 所以，我们让一个指针从起点出发，另一个指针从相遇点出发，二者速度相同，相遇时，就到达了环的起点
     *
     * @param head
     * @return
     */
    public ListNode detectCycle(ListNode head) {
        // 单节点不存在环
        if (head == null || head.next == null) {
            return null;
        }
        // 快慢指针，快指针是慢指针二倍速
        ListNode first = head, second = head;
        while (first != null && first.next != null) {
            first = first.next.next;
            second = second.next;
            // 如果二者能相遇，说明有环，此时快指针路程是慢指针路程二倍
            // 此时让满指针回起点，快慢同时前进，二者下次相遇的位置必然是环的起点处
            if (first == second) {
                second = head;
                while (first != second) {
                    first = first.next;
                    second = second.next;
                }
                return second;
            }
        }
        return null;
    }
}
