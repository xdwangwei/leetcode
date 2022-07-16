package com.list;

import com.common.ListNode;

/**
 * @author wangwei
 * 2022/4/20 9:55
 *
 * 147. 对链表进行插入排序
 * 给定单个链表的头 head ，使用 插入排序 对链表进行排序，并返回 排序后链表的头 。
 *
 * 插入排序 算法的步骤:
 *
 * 插入排序是迭代的，每次只移动一个元素，直到所有元素可以形成一个有序的输出列表。
 * 每次迭代中，插入排序只从输入数据中移除一个待排序的元素，找到它在序列中适当的位置，并将其插入。
 * 重复直到所有输入数据插入完为止。
 * 下面是插入排序算法的一个图形示例。部分排序的列表(黑色)最初只包含列表中的第一个元素。每次迭代时，从输入数据中删除一个元素(红色)，并就地插入已排序的列表中。
 *
 * 对链表进行插入排序。
 *
 * 示例 1：
 *
 *
 *
 * 输入: head = [4,2,1,3]
 * 输出: [1,2,3,4]
 * 示例 2：
 *
 *
 *
 * 输入: head = [-1,5,3,4,0]
 * 输出: [-1,0,3,4,5]
 *
 *
 * 提示：
 *
 * 列表中的节点数在 [1, 5000]范围内
 * -5000 <= Node.val <= 5000
 * 通过次数118,788提交次数172,887
 */
public class _147_InsertionSortList {

    /**
     * 对列表进行插入排序，
     * 创建一个新的虚拟头节点，逐个拆除原列表节点，将其插入到新列表合适位置
     * @param head
     * @return
     */
    public ListNode insertionSortList(ListNode head) {
        // 空列表或者只有一个节点的列表
        if (head == null || head.next == null) {
            return head;
        }
        // 虚拟列表头节点，取 题目给定最小值 之外的值
        ListNode dummy = new ListNode(-100001);
        // 逐个拆除原列表节点
        ListNode cur = head;
        while (cur != null) {
            // 保存下一个节点
            ListNode next = cur.next;
            // 在新列表中找合适位置，插入 它
            // 从头开始找
            ListNode prev = dummy, p = dummy.next;
            while (p != null && p.val <= cur.val) {
                prev = p;
                p = p.next;
            }
            // 将其插在prev 和 p中间
            prev.next = cur;
            cur.next = p;
            // 拆下一个节点
            cur = next;
        }
        // 返回新列表第一个位置
        return dummy.next;
    }

    /**
     * 上面写法耗时比较多，是因为上面写法将原链表每一个节点都拆下来，去新链表里面找合适位置
     * 对于原链表中原本就符合递增有序的这部分节点来说，根本不需要一个个拆，直接跳过就行了
     *
     * 所以这里。我们可以只拆除破坏递增规则的节点，在前面部分为他找寻合适位置，将其插入，继续从原位置往后判断就好了
     *
     * 创建一个 虚拟头节点，将原链表接在dummy后面，让 dummy --> cur 一直维护有序，当 cur <= cur.next 不成立时，把 cur.next 拆下来，在前面有序部分重新插入
     * @param head
     * @return
     */
    public ListNode insertionSortList2(ListNode head) {
        // 空列表或者只有一个节点的列表
        if (head == null || head.next == null) {
            return head;
        }
        // 虚拟列表头节点，取 题目给定最小值 之外的值
        ListNode dummy = new ListNode(-100001);
        // 直接把原链表拼接到后面，因为 完全可以在 链表本身上 完成节点去除和重新插入
        dummy.next = head;
        // 从头到尾遍历链表，只拆除破坏递增关系的节点
        ListNode cur = head;
        while (cur != null && cur.next != null) {
            // 合理，继续前进
            if (cur.val <= cur.next.val) {
                cur = cur.next;
            // 不合理，cur.val > next.val，把next拆出来，在前面找一个合适位置
            // 我们保证的是 cur及之前部分是递增的，这里不是拆cur，你拆了cur会发现又将他插入当前位置了，
            } else {
                // 把 cur.next 拆出来
                ListNode temp = cur.next;
                // 跳过cur.next接上
                cur.next = cur.next.next;
                // 为 temp 从头开始找 个合适位置，因为插入要记录目标节点的前驱位置，这里用1个指针搞定
                ListNode prev = dummy;
                while (prev.next != null && prev.next.val <= temp.val) {
                    prev = prev.next;
                }
                // 将temp插入到prev和prev.next中间
                temp.next = prev.next;
                prev.next = temp;

                // 处理完temp(cur.next)之后，cur.next变为了原来的下一个，下一步继续判断 cur 和新的 cur.next
                // 所以这里不需要，也不能 推进 cur
            }
        }
        // 返回新列表第一个位置
        return dummy.next;
    }


}
