package com.offerassult;

import com.common.ListNode;

import java.util.PriorityQueue;

/**
 * @author wangwei
 * @date 2022/5/24 9:38
 * @description: _78_MergeSortedList
 *
 * 剑指 Offer II 078. 合并排序链表
 * 给定一个链表数组，每个链表都已经按升序排列。
 *
 * 请将所有链表合并到一个升序链表中，返回合并后的链表。
 *
 *
 *
 * 示例 1：
 *
 * 输入：lists = [[1,4,5],[1,3,4],[2,6]]
 * 输出：[1,1,2,3,4,4,5,6]
 * 解释：链表数组如下：
 * [
 *   1->4->5,
 *   1->3->4,
 *   2->6
 * ]
 * 将它们合并到一个有序链表中得到。
 * 1->1->2->3->4->4->5->6
 * 示例 2：
 *
 * 输入：lists = []
 * 输出：[]
 * 示例 3：
 *
 * 输入：lists = [[]]
 * 输出：[]
 *
 *
 * 提示：
 *
 * k == lists.length
 * 0 <= k <= 10^4
 * 0 <= lists[i].length <= 500
 * -10^4 <= lists[i][j] <= 10^4
 * lists[i] 按 升序 排列
 * lists[i].length 的总和不超过 10^4
 *
 *
 * 注意：本题与主站 23 题相同： https://leetcode-cn.com/problems/merge-k-sorted-lists/
 */
public class _078_MergeSortedList {


    /**
     * 优先队列
     *
     * 类似与合并两个有序链表
     * 每一次都选取二者当前位置元素中的较小者，然后结果链表和被选择的那个链表指针同时后移
     *
     * 那么，当有多个有序链表时，同样，每一步都要选择其中最小者，那么使用优先队列能够再O(1)时间内得到最小值
     * 那么，被选择的链表指针后移就相当于把节点的next值加入优先队列
     *
     * 所以，起始我们是每一步都从k个值中选最小值，需要维护的也不过是一个大小为k的优先队列而已
     *
     * 初始时，新链表第一个节点是在所有链表的第一个节点中选最小值，所以初始时将所有链表的首节点值加入优先队列
     *
     * @param lists
     * @return
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }
        // base case 初始时将所有链表的首节点值加入优先队列
        PriorityQueue<ListNode> pq = new PriorityQueue<ListNode>((o1, o2) -> o1.val - o2.val);
        for (int i = 0; i < lists.length; ++i) {
            if (lists[i] != null) {
                pq.offer(lists[i]);
            }
        }
        ListNode dummy = new ListNode(-1);
        ListNode head = dummy;
        // 每次取出队列中最小值
        while (!pq.isEmpty()) {
            ListNode cur = pq.poll();
            // 拼接
            head.next = cur;
            // 后移
            head = head.next;
            // 被选择的那个链表指针后移，也就是把被选择节点的next加入队列
            if (cur.next != null) {
                pq.offer(cur.next);
            }
        }
        return dummy.next;
    }


    /**
     *
     * 方法二：顺序合并每一个链表
     *
     * 假设要返回的链表是ans，初始时ans = null
     * 然后顺序将每一个链表都合并到ans上
     * 最后返回ans
     *
     * 核心还是合并两个有序链表
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/merge-k-sorted-lists/solution/he-bing-kge-pai-xu-lian-biao-by-leetcode-solutio-2/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param lists
     * @return
     */
    public ListNode mergeKLists2(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }
        // 要返回的链表
        ListNode ans = null;
        for (ListNode head : lists) {
            // 将每一个链表合并到ans上
            ans = mergeTwoListsIterate(ans, head);
        }
        // 返回ans
        return ans;
    }


    /**
     *
     * 方法三：分治合并k个链表
     * 思路
     *
     * 考虑优化方法二，用分治的方法进行合并。
     *
     * 将 k 个链表配对并将同一对中的链表合并；
     * 第一轮合并以后， k 个链表被合并成了 k / 2 个链表，平均长度为 2n / k，然 后是 k / 4个链表，k / 8个链表等等；
     * 重复这一过程，直到我们得到了最终的有序链表。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/merge-k-sorted-lists/solution/he-bing-kge-pai-xu-lian-biao-by-leetcode-solutio-2/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param lists
     * @return
     */
    public ListNode mergeKLists3(ListNode[] lists) {
        // 自定向下归并，
        return mergeKLists3(lists, 0, lists.length - 1);
    }

    public ListNode mergeKLists3(ListNode[] lists, int s, int e) {
        // 临界条件
        if (s == e) {
            return lists[s];
        }
        // 临界条件
        if (s > e) {
            return null;
        }
        // 自顶向下归并
        int m = (s + e) >> 1;
        // 将lists[s...m]合并为1个链表，将lists[m+1, e]合并为1个链表
        // 再将二者合并，得到最终结果
        return mergeTwoListsRecusion(mergeKLists3(lists, s, m), mergeKLists3(lists, m + 1, e));
    }


    /**
     * 合并两个有序链表迭代写法
     * @param head1
     * @param head2
     * @return
     */
    private ListNode mergeTwoListsIterate(ListNode head1, ListNode head2) {
        if (head1 == null) {
            return head2;
        }
        if (head2 == null) {
            return head1;
        }
        ListNode dummy = new ListNode(-1);
        ListNode head = dummy, p = head1, q = head2;
        while (p != null && q != null) {
            // 二者选较小值
            if (p.val < q.val) {
                head.next = p;
                p = p.next;
            } else {
                head.next = q;
                q = q.next;
            }
            head = head.next;
        }
        // 后序处理
        head.next = (p == null ? q : p);
        return dummy.next;
    }


    /**
     * 合并两个有序链表递归写法
     * @param head1
     * @param head2
     * @return
     */
    private ListNode mergeTwoListsRecusion(ListNode head1, ListNode head2) {
        if (head1 == null) {
            return head2;
        }
        if (head2 == null) {
            return head1;
        }
        ListNode p = head1, q = head2;
        // 比较头节点大小，选择较小者，将其剩余部分与另一个链表合并，再用next指针链接合并后的表头，返回当前节点
        if (p.val < q.val) {
            p.next = mergeTwoListsRecusion(p.next, q);
            return p;
        } else {
            q.next = mergeTwoListsRecusion(q.next, p);
            return q;
        }
    }
}
