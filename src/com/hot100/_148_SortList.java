package com.hot100;

import com.common.ListNode;

/**
 * @author wangwei
 * 2022/4/20 10:21
 *
 * 148. 排序链表
 * 给你链表的头结点 head ，请将其按 升序 排列并返回 排序后的链表 。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：head = [4,2,1,3]
 * 输出：[1,2,3,4]
 * 示例 2：
 *
 *
 * 输入：head = [-1,5,3,4,0]
 * 输出：[-1,0,3,4,5]
 * 示例 3：
 *
 * 输入：head = []
 * 输出：[]
 *
 *
 * 提示：
 *
 * 链表中节点的数目在范围 [0, 5 * 104] 内
 * -105 <= Node.val <= 105
 *
 */
public class _148_SortList {

    /**
     * 插入排序法，讲原链表节点拆分下来，重新为其在之前部分(排序后部分)寻找合适插入位置
     * 由于原链表可能部分节点有序，所以这部分可跳过，只拆除破坏递增关系的节点
     *
     * 具体参考 _147_InsertionSortList
     *
     * 创建一个 虚拟头节点，将原链表接在dummy后面，让 dummy --> cur 一直维护有序，当 cur <= cur.next 不成立时，把 cur.next 拆下来，在前面有序部分重新插入
     *
     * 时间复杂度o(n^2) ，能通过但效率低，题目建议 时间复杂度 O(nlogn)，空间复杂度 常数级
     * @param head
     * @return
     */
    public ListNode sortList(ListNode head) {
        // 空列表或者只有一个节点的列表
        if (head == null || head.next == null) {
            return head;
        }
        // 虚拟列表头节点，取 题目给定最小值 之外的值
        // 直接把原链表拼接到后面，因为 完全可以在 链表本身上 完成节点去除和重新插入
        // dummy --> cur 一直维护有序
        ListNode dummy = new ListNode(-100001, head);
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


    /**
     * 使用自顶向下的归并排序，时间复杂度O(nlogn)，空间复杂度O(n)，递归栈
     * @param head
     * @return
     */
    public ListNode sortList2(ListNode head) {
        // 空列表或者单节点列表，直接返回
        if (head == null || head.next == null) {
            return head;
        }
        // 找到链表终点 slow，找到中点后要从中间断开
        // 可以选择分为  head->slow, slow.next->null
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        // 将链表拆分成两部分，第一部分 head->slow，第二部分slow.next->null
        // 递归调用，排序 后半部分
        ListNode head1 = sortList2(slow.next);
        // 从中间断开前半部分与后半部分的链接
        slow.next = null;
        // 递归调用，排序 前半部分
        ListNode head2 = sortList2(head);
        // 合并两个有序列表
        return merge(head1, head2);
    }

    /**
     * 自定向下写法的空间复杂度是O(logn)
     * 改成自底向上，空间复杂度 O(1)
     *
     * 首先求得链表的长度 length，然后将链表拆分成子链表进行合并。
     *
     * 具体做法如下。
     *
     * 用 subLength 表示每次需要排序的子链表的长度，初始时 subLength=1。
     *
     * 每次将链表拆分成若干个长度为 subLength 的子链表（最后一个子链表的长度可以小于 subLength），
     * 按照每两个子链表一组进行合并，合并后即可得到若干个长度为 subLength × 2 的有序子链表（最后一个子链表的长度可以小于 subLength×2）。
     * 合并两个子链表仍然使用「21. 合并两个有序链表」的做法。
     *
     * 将subLength 的值加倍，重复第 2 步，对更长的有序子链表进行合并操作，直到有序子链表的长度大于或等于 length，整个链表排序完毕。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/sort-list/solution/pai-xu-lian-biao-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param head
     * @return
     */
    public ListNode sortList3(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        // 统计列表长度
        int len = 0;
        ListNode cur = head;
        while (cur != null) {
            len++;
            cur = cur.next;
        }
        // 构建虚拟头节点，原列表接在后面
        ListNode dummy = new ListNode(-100001, head);
        // 这里不取等，取等的时候说明 整个 list 已经有序了
        // 每次取不同的分割长度值，将list分割为大小为subLength的子列表，然后两个一组，合并为一组
        // 要保证每次合并的两部分列表都是有序列表，subLength从1开始，并且每次变为它的二倍
        for (int subLength = 1; subLength < len; subLength <<= 1) {
            // prev用于把这些合并后的列表重新拼接在一起
            ListNode prev = dummy;
            // 分割长度改变，重新从第一个节点开始统计
            cur = dummy.next;
            // 每次循环都是从上一次结束的位置开始找到挨着的[两组]长度为subLength的子列表进行合并
            while (cur != null) {
                // 第一个列表从 cur开始，刚进来时 cur是第一个节点位置，一次while后，cur是第2*subLength+1个位置节点
                ListNode head1 = cur;
                // 凑够subLength个节点，
                for (int i = 1; i < subLength && cur.next != null; ++i) {
                    // 必须保证for循环条件中cur.next!=null，不然这里可能会空指针
                    cur = cur.next;
                }
                // 第一个长度为subLength的子列表找完了，后面没有了，说明当次while只需要合并这个list和一个空列表，结果还是这个list
                if (cur.next == null) {
                    // 那么直接把这个列表拼接到前面部分后面
                    prev.next = head1;
                    // 然后prev更新为这个里列表的尾部节点，实际上这个if说明当前分割长度下，所有部分已经合并完毕，下一次就会改变subLength，开启新的whlile了，这里prev不赋值也无所谓
                    prev = cur;
                    break;
                }
                // 第一个长度为subLength的子列表找完了，后面还有，那么继续找第二个长度为subLength的列表
                // 起点
                ListNode head2 = cur.next;
                // 断开第一部分和第二部分的连接
                cur.next = null;
                // 从起点开始，找subLength个
                cur = head2;
                for (int i = 1; i < subLength && cur.next != null; ++i) {
                    // 同理，这里需要保证不空
                    cur = cur.next;
                }
                // 保存第二部分后面的起始位置，用于下一次while循环，找第三部分和第四部分
                ListNode next = null;
                // 如果后面的确还有节点
                if (cur != null) {
                    // 保存后面部分的起始位置
                    next = cur.next;
                    // 断开当前部分列表和后面的连接
                    cur.next = null;
                }
                // 合并 第一和第二部分有序列表，将合并后的头节点拼接在prev后面
                prev.next = merge(head1, head2);
                // 再将prev滑动到新的尾部，注意合并后最后一个节点不一定是head2的尾节点，所以这里不能直接prev=cur
                while (prev.next != null) {
                    prev = prev.next;
                }
                // 将cur赋值为第一二部分之后，第三部分的起点位置，进行下一轮while循环
                cur = next;
            }
        }
        return dummy.next;
    }

    /**
     * 合并两个有序列表
     * @param head1
     * @param head2
     * @return
     */
    private ListNode merge(ListNode head1, ListNode head2) {
        ListNode dummy = new ListNode(-100001);
        ListNode p = head1, q = head2, cur = dummy;
        while (p != null && q != null) {
            // 每次选择更小的那个
            if (p.val <= q.val) {
                cur.next = p;
                p = p.next;
                cur = cur.next;
            } else {
                cur.next = q;
                q = q.next;
                cur = cur.next;
            }
        }
        // 长度不等时，某个链表会有剩余，继续拼接
        if (p != null) {
            cur.next = p;
        }
        if (q != null) {
            cur.next = q;
        }
        return dummy.next;
    }
}
