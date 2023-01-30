package com.daily;

import com.common.ListNode;

/**
 * @author wangwei
 * @date 2023/1/29 18:22
 * @description: _1664_MergeInBetweenLinkedList
 *
 * 1669. 合并两个链表
 * 给你两个链表 list1 和 list2 ，它们包含的元素分别为 n 个和 m 个。
 *
 * 请你将 list1 中下标从 a 到 b 的全部节点都删除，并将list2 接在被删除节点的位置。
 *
 * 下图中蓝色边和节点展示了操作后的结果：
 *
 *
 * 请你返回结果链表的头指针。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：list1 = [0,1,2,3,4,5], a = 3, b = 4, list2 = [1000000,1000001,1000002]
 * 输出：[0,1,2,1000000,1000001,1000002,5]
 * 解释：我们删除 list1 中下标为 3 和 4 的两个节点，并将 list2 接在该位置。上图中蓝色的边和节点为答案链表。
 * 示例 2：
 *
 *
 * 输入：list1 = [0,1,2,3,4,5,6], a = 2, b = 5, list2 = [1000000,1000001,1000002,1000003,1000004]
 * 输出：[0,1,1000000,1000001,1000002,1000003,1000004,6]
 * 解释：上图中蓝色的边和节点为答案链表。
 *
 *
 * 提示：
 *
 * 3 <= list1.length <= 104
 * 1 <= a <= b < list1.length - 1
 * 1 <= list2.length <= 104
 * 通过次数21,663提交次数28,918
 */
public class _1664_MergeInBetweenLinkedList {

    /**
     * 思路与算法
     *
     * 题目要求将 list1 的第 a 到 b个节点都删除，将其替换为 list2。
     * 因此，我们首先找到 list1 中第 a−1 个节点 preA，以及第 b+1 个节点 aftB。
     *
     * 由于 1 ≤ a ≤ b < n−1（其中 n 为 list1 的长度），所以 preA 和 aftB 是一定存在的。
     *
     * 然后我们让 preA 的 next 指向 list2 的头节点，再让 list2 的尾节点的 next 指向 aftB 即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/merge-in-between-linked-lists/solution/he-bing-liang-ge-lian-biao-by-leetcode-s-alt8/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param list1
     * @param a
     * @param b
     * @param list2
     * @return
     */
    public ListNode mergeInBetween(ListNode list1, int a, int b, ListNode list2) {
        // 删除 a 到 b
        ListNode preA = list1;
        // 找到第 a - 1 个节点
        for (int i = 0; i < a - 1; i++) {
            preA = preA.next;
        }
        // 找到第 b + 1 个节点（从上面之后找）
        ListNode preB = preA;
        for (int i = a - 1; i < b + 1; i++) {
            preB = preB.next;
        }
        // 将 list2 插入 preA 和 aftB 之间
        preA.next = list2;
        // 找到 list2 的尾指针
        while (list2.next != null) {
            list2 = list2.next;
        }
        list2.next = preB;
        // 返回
        return list1;
    }
}
