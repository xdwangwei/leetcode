package com.offerassult;

import com.common.ListNode;

/**
 * @author wangwei
 * @date 2022/10/21 13:09
 * @description: _023_IntersectionOfTwoLinkedList
 *
 * 剑指 Offer II 023. 两个链表的第一个重合节点
 * 给定两个单链表的头节点 headA 和 headB ，请找出并返回两个单链表相交的起始节点。如果两个链表没有交点，返回 null 。
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
 * 示例 2：
 *
 *
 *
 * 输入：intersectVal = 2, listA = [0,9,1,2,4], listB = [3,2,4], skipA = 3, skipB = 1
 * 输出：Intersected at '2'
 * 解释：相交节点的值为 2 （注意，如果两个链表相交则不能为 0）。
 * 从各自的表头开始算起，链表 A 为 [0,9,1,2,4]，链表 B 为 [3,2,4]。
 * 在 A 中，相交节点前有 3 个节点；在 B 中，相交节点前有 1 个节点。
 * 示例 3：
 *
 *
 *
 * 输入：intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2
 * 输出：null
 * 解释：从各自的表头开始算起，链表 A 为 [2,6,4]，链表 B 为 [1,5]。
 * 由于这两个链表不相交，所以 intersectVal 必须为 0，而 skipA 和 skipB 可以是任意值。
 * 这两个链表不相交，因此返回 null 。
 *
 *
 * 提示：
 *
 * listA 中节点数目为 m
 * listB 中节点数目为 n
 * 0 <= m, n <= 3 * 104
 * 1 <= Node.val <= 105
 * 0 <= skipA <= m
 * 0 <= skipB <= n
 * 如果 listA 和 listB 没有交点，intersectVal 为 0
 * 如果 listA 和 listB 有交点，intersectVal == listA[skipA + 1] == listB[skipB + 1]
 *
 *
 * 进阶：能否设计一个时间复杂度 O(n) 、仅用 O(1) 内存的解决方案？
 *
 *
 *
 * 注意：本题与主站 160 题相同：https://leetcode-cn.com/problems/intersection-of-two-linked-lists/
 */
public class _023_IntersectionOfTwoLinkedList {

    /**
     * 这个题直接的想法可能是用HashSet记录一个链表的所有节点，然后和另一条链表对比，但这就需要额外的空间。
     *
     * 如果不用额外的空间，只使用两个指针，你如何做呢？
     *
     * 难点在于，由于两条链表的长度可能不同，两条链表之间的节点无法对应：
     *
     * 所以，解决这个问题的关键是，通过某些方式，让p1和p2能够同时到达相交节点c1。
     *
     * 思路一：
     * 所以，我们可以让p1遍历完链表A之后开始遍历链表B，让p2遍历完链表B之后开始遍历链表A，
     * 这样相当于「逻辑上」两条链表接在了一起。此时，得到两条长度相同的链表，两指针同时扫描。就能找到公共节点。
     *
     * 如果这样进行拼接，就可以让p1和p2同时进入公共部分，也就是同时到达相交节点c1：
     *
     * 那你可能会问，如果说两个链表没有相交点，是否能够正确的返回 null 呢？
     *
     * 这个逻辑可以覆盖这种情况的，相当于c1节点是 null 空指针嘛，可以正确返回 null。
     *
     * 所以必须要保证p1.p2同时前进，也就是必须保证二者走过的步数都是m+n
     *
     * 思路二：先计算两条链表的长度，然后让 p1 和 p2 距离链表尾部的距离相同，然后齐头并进，但这样相当于进行了两次遍历
     *
     * 思路一算法
     *
     * 使用双指针的方法，可以将空间复杂度降至 O(1)。
     *
     * 只有当链表 headA 和 headB 都不为空时，两个链表才可能相交。
     * 因此首先判断链表 headA 和 headB 是否为空，如果其中至少有一个链表为空，则两个链表一定不相交，返回 null。
     *
     * 当链表 headA 和 headB 都不为空时，创建两个指针 pA 和 pB，初始时分别指向两个链表的头节点 headA 和 headB，然后将两个指针依次遍历两个链表的每个节点。具体做法如下：
     *
     * 每步操作需要同时更新指针 pA 和 }pB。
     *
     * 如果指针 pA 不为空，则将指针 pA 移到下一个节点；如果指针 pB 不为空，则将指针 pB 移到下一个节点。
     *
     * 如果指针 pA 为空，则将指针 pA 移到链表 headB 的头节点；如果指针 pB 为空，则将指针 pB 移到链表 headA 的头节点。
     *
     * 当指针 pA 和 pB 指向同一个节点或者都为空时，返回它们指向的节点或者 null。
     *
     * 证明
     *
     * 下面提供双指针方法的正确性证明。考虑两种情况，第一种情况是两个链表相交，第二种情况是两个链表不相交。
     *
     * 情况一：两个链表相交
     *
     * 链表 headA 和 headB 的长度分别是 m 和 n。假设链表 headA 的不相交部分有 aa 个节点，链表 headB 的不相交部分有 b 个节点，两个链表相交的部分有 c 个节点，则有 a+c=m，b+c=n。
     *
     * 如果 a=b，则两个指针会同时到达两个链表相交的节点，此时返回相交的节点；
     *
     * 如果 a!=b，则指针 pA 会遍历完链表 headA，指针 pB 会遍历完链表 headB，两个指针不会同时到达链表的尾节点，
     *      然后指针 pA 移到链表 headB 的头节点，指针 pB 移到链表 headA 的头节点，然后两个指针继续移动，
     *      在指针 pA 移动了 a+c+b 次、指针 pB 移动了 b+c+a 次之后，两个指针会同时到达两个链表相交的节点，该节点也是两个指针第一次同时指向的节点，此时返回相交的节点。
     *
     * 情况二：两个链表不相交
     *
     * 链表 headA 和 headB 的长度分别是 m 和 n。考虑当 m=n 和 m!=n 时，两个指针分别会如何移动：
     *
     * 如果 m=n，则两个指针会同时到达两个链表的尾节点，然后同时变成空值 null，此时返回 null；
     *
     * 如果 m!=n，则由于两个链表没有公共节点，两个指针也不会同时到达两个链表的尾节点，因此两个指针都会遍历完两个链表，
     * 在指针 pA 移动了 m+n 次、指针 pB 移动了 n+m 次之后，两个指针会同时变成空值 null，此时返回 null。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/3u1WK4/solution/liang-ge-lian-biao-de-di-yi-ge-zhong-he-0msfg/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        ListNode p1 = headA, p2 = headB;
        // 退出时，要么 p1和p2同时遇到一个非空节点，要么二者都为null
        while (p1 != p2) {
            // 二者必须同时前进
            // p1为null 说明 A链表走完了。连接B
            p1 = p1 == null ? headB : p1.next;
            // p2为null 说明 B链表走完了。连接A
            p2 = p2 == null ? headA : p2.next;
        }
        return p1;
    }
}
