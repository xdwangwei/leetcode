package com.offerassult;

import com.common.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/10/20 13:23
 * @description: _026_ReorderLinkedList
 *
 * 剑指 Offer II 026. 重排链表
 * 给定一个单链表 L 的头节点 head ，单链表 L 表示为：
 *
 *  L0 → L1 → … → Ln-1 → Ln
 * 请将其重新排列后变为：
 *
 * L0 → Ln → L1 → Ln-1 → L2 → Ln-2 → …
 *
 * 不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
 *
 *
 *
 * 示例 1:
 *
 *
 *
 * 输入: head = [1,2,3,4]
 * 输出: [1,4,2,3]
 * 示例 2:
 *
 *
 *
 * 输入: head = [1,2,3,4,5]
 * 输出: [1,5,2,4,3]
 *
 *
 * 提示：
 *
 * 链表的长度范围为 [1, 5 * 104]
 * 1 <= node.val <= 1000
 *
 *
 * 注意：本题与主站 143 题相同：https://leetcode-cn.com/problems/reorder-list/
 */
public class _026_ReorderLinkedList {


    /**
     * 双指针
     * 因为链表不支持下标访问，所以我们无法随机访问链表中任意位置的元素。
     *
     * 因此比较容易想到的一个方法是，我们利用线性表存储该链表，然后利用线性表可以下标访问的特点，直接按顺序访问指定元素，重建该链表即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/reorder-list/solution/zhong-pai-lian-biao-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param head
     */
    public void reorderList(ListNode head) {
        if (head == null) {
            return;
        }
        // 转入数组
        List<ListNode> list = new ArrayList<>();
        ListNode node = head;
        while (node != null) {
            list.add(node);
            node = node.next;
        }
        // 双指针
        int i = 0, j = list.size() - 1;
        while (i < j) {
            // 头连尾
            list.get(i).next = list.get(j);
            // 头指针前进
            i++;
            // 说明二者已经是最中间两个相邻位置，结束
            if (i == j) {
                break;
            }
            // 尾连头的下一个
            list.get(j).next = list.get(i);
            // 尾指针前移
            j--;
        }
        // 断开后续连接
        list.get(i).next = null;
    }

    /**
     * 注意到目标链表即为将原链表的左半端和反转后的右半端合并后的结果。
     * <p>
     * 这样我们的任务即可划分为三步：
     * <p>
     * 找到原链表的中点（参考「876. 链表的中间结点」）。
     * <p>
     * 我们可以使用快慢指针来 O(N)O(N) 地找到链表的中间节点。
     * 将原链表的右半端反转（参考「206. 反转链表」）。
     * <p>
     * 我们可以使用迭代法实现链表的反转。
     * 将原链表的两端合并。
     * <p>
     * 因为两链表长度相差不超过 11，因此直接合并即可。
     * <p>
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/reorder-list/solution/zhong-pai-lian-biao-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param
     */
    public void reorderList2(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        ListNode middle = findMiddle(head);
        // 因为最终结果是从开始链接，所以把中点保留到h1尾部，让h1尽可能长，断开h1和h2的连接
        ListNode h1 = head, h2 = middle.next;
        middle.next = null;
        // 反转后半部分
        h2 = reverse(h2);
        // 拼接h1，h2
        merge(h1, h2);
    }

    /**
     * 合并链表
     * h1：a1 -> b1 -> c1 -> d1
     * h2：a2 -> b2 -> c2 -> d2    或   h2：a2 -> b2 -> c2
     *
     * h1长度 == h2长度  ||  h2长度 + 1
     *
     * 按照先h1，后h2的拼接顺序返回最终链表
     *
     * a1 -> a2 -> b1 -> b2 -> c1 -> c2 -> d1
     * @param h1
     * @param h2
     */
    private void merge(ListNode h1, ListNode h2) {
        // 因为h1最多比h2多一个节点，所以先h1链接h2，一定能够完成所有节点链接
        while (h1 != null && h2 != null) {
            // 下面链接过程会改变原来的next，所以先保留
            ListNode h1_tmp = h1.next;
            ListNode h2_tmp = h2.next;
            // 先用h1链接h2
            h1.next = h2;
            // h1前进下一个
            h1 = h1_tmp;

            // 再用h2链接h1
            h2.next = h1;
            // h2前进下一个
            h2 = h2_tmp;
        }
    }
    /**
     * 快慢指针寻找链表中点
     * @param head
     * @return
     */
    private ListNode findMiddle(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }
    /**
     * 反转链表
     * @param head
     * @return
     */
    private ListNode reverse(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode prev = null, cur = head, next;
        while (cur != null) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    public static void main(String[] args) {
        _026_ReorderLinkedList obj = new _026_ReorderLinkedList();
        ListNode list = ListNode.buildFromArray(new int[]{1, 2, 3, 4});
        obj.reorderList(list);
        System.out.println(list);
        list = ListNode.buildFromArray(new int[]{1, 2, 3, 4, 5});
        obj.reorderList(list);
        System.out.println(list);
    }
}
