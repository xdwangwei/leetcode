package com.list;

import com.common.ListNode;

/**
 * @author wangwei
 * @version 1.0.0
 * @date 2021/9/27 9:30
 * @description: 给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表没有交点，返回 null 。
 *
 * 图示两个链表在节点 c1 开始相交：
 *
 *
 *
 * 题目数据 保证 整个链式结构中不存在环。
 *
 * 注意，函数返回结果后，链表必须 保持其原始结构 。
 *
 *  
 *
 * 示例 1：
 *
 *
 *
 * 输入：intersectVal = 8, listA = [4,1,8,4,5], listB = [5,0,1,8,4,5], skipA = 2, skipB = 3
 * 输出：Intersected at '8'
 * 解释：相交节点的值为 8 （注意，如果两个链表相交则不能为 0）。
 * 从各自的表头开始算起，链表 A 为 [4,1,8,4,5]，链表 B 为 [5,0,1,8,4,5]。
 * 在 A 中，相交节点前有 2 个节点；在 B 中，相交节点前有 3 个节点。
 * 示例 2：
 *
 *
 *
 * 输入：intersectVal = 2, listA = [0,9,1,2,4], listB = [3,2,4], skipA = 3, skipB = 1
 * 输出：Intersected at '2'
 * 解释：相交节点的值为 2 （注意，如果两个链表相交则不能为 0）。
 * 从各自的表头开始算起，链表 A 为 [0,9,1,2,4]，链表 B 为 [3,2,4]。
 * 在 A 中，相交节点前有 3 个节点；在 B 中，相交节点前有 1 个节点。
 * 示例 3：
 *
 *
 *
 * 输入：intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2
 * 输出：null
 * 解释：从各自的表头开始算起，链表 A 为 [2,6,4]，链表 B 为 [1,5]。
 * 由于这两个链表不相交，所以 intersectVal 必须为 0，而 skipA 和 skipB 可以是任意值。
 * 这两个链表不相交，因此返回 null 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/intersection-of-two-linked-lists
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _160_IntersectionOfTwoLinkedList {

    /**
     * 这个题直接的想法可能是用HashSet记录一个链表的所有节点，然后和另一条链表对比，但这就需要额外的空间。
     *
     * 如果不用额外的空间，只使用两个指针，你如何做呢？
     *
     * 难点在于，由于两条链表的长度可能不同，两条链表之间的节点无法对应：
     *
     * 所以，解决这个问题的关键是，通过某些方式，让p1和p2能够同时到达相交节点c1。
     *
     * 所以，我们可以让p1遍历完链表A之后开始遍历链表B，让p2遍历完链表B之后开始遍历链表A，
     * 这样相当于「逻辑上」两条链表接在了一起。此时，得到两条长度相同的链表，两指针同时扫描。就能找到公共节点。
     *
     * 如果这样进行拼接，就可以让p1和p2同时进入公共部分，也就是同时到达相交节点c1：
     *
     * 那你可能会问，如果说两个链表没有相交点，是否能够正确的返回 null 呢？
     *
     * 这个逻辑可以覆盖这种情况的，相当于c1节点是 null 空指针嘛，可以正确返回 null。
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode p1 = headA, p2 = headB;
        // 退出时，要么 p1和p2同时遇到一个非空节点，要么二者都为null
        while (p1 != p2) {
            // A链表走完了。连接B
            if (p1 == null) p1 = headB;
            else p1 = p1.next;
            // B链表走完了。连接A
            if (p2 == null) p2 = headA;
            else p2 = p2.next;
        }
        return p1;
    }
}
