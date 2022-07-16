package com.list;

import com.common.ListNode;

import java.util.*;

/**
 * @Author: wangwei
 * @Description: 合并K个有序链表
 * @Time: 2019/12/11 周三 08:53
 *
 * 23. 合并K个升序链表
 * 给你一个链表数组，每个链表都已经按升序排列。
 *
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
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
 **/
public class _23_MergeKLists {


	/**
	 * 方法一：把所有链表的所有节点值都加入一个新的链表
	 * 对新的链表中的元素进行排序
	 * 再根据新的链表元素创建出一条新的链表
	 * @param lists
	 * @return
	 */
	public ListNode solution1(ListNode[] lists) {
		// 保存全部链表的全部节点值
		List<Integer> list = new ArrayList<>();
		int k = lists.length;
		for (int i = 0; i < k; i++) {
			while (null != lists[i]) {
				list.add(lists[i].val);
				lists[i] = lists[i].next;
			}
		}
		// 排序
		Collections.sort(list);
		// 创建新的链表
		ListNode dummy = new ListNode(0);
		ListNode cur = dummy;
		for (int i = 0; i < list.size(); i++) {
			cur.next = new ListNode(list.get(i));
			cur = cur.next;
		}
		// 返回新链表头节点
		return dummy.next;
	}

	/**
	 * 优先队列
	 *
	 * 方法一是将所有节点值加入一个新的集合，再全部排序
	 *
	 * 实际上，我们不需要这么做
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
	public ListNode solution21(ListNode[] lists) {
		if (lists.length == 0) {
			return null;
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
	 * 合并两个列表的升级版
	 * 可以用前一个链表融合在第二个，然后第二个合并到第三个，最后直到最后一个为止。
	 * 只不过这样有一个问题，通过这种方式，链表都遍历了多次，所以肯定要费时一些
	 * 对链表俩俩分组，然后合并，不断分组合并，直到最后只剩一个链表为止
	 * 也就是第0个和第n/2个合并,第1个和第n/2+1个合并，最后将n个列表合并为前n/2个
	 * 对于奇数个列表，将最后一个列表和第一个列表再合并一次即可
	 *
	 * @param lists
	 * @return
	 */
	public ListNode solution3(ListNode[] lists) {
		if (lists.length == 0) return null;
		int k = lists.length;
		while (k > 1){
			int curLen = k / 2;
			// 把后一半的列表合并到前一半对应的列表
			for (int i = 0; i < curLen; i++)
				lists[i] = mergeTwoLists(lists[i], lists[i + curLen]);
			// 如果有奇数个，把最夯一个列表再合并到第一个列表上
			if (k % 2 == 1)
				lists[0] = mergeTwoLists(lists[0], lists[k - 1]);
			// 一次合并后，列表只剩下一半，下一次合并前半部分即可
			k = k / 2;
		}
		// 合并完后只剩下一个列表
		return lists[0];
	}
	
	/**
	 * 递归 合并两个有序列表
	 * @param l1
	 * @param l2
	 * @return
	 */
	private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		if (null == l1) return l2;
		if (null == l2) return l1;
		if (l1.val < l2.val) {
			l1.next = mergeTwoLists(l1.next, l2);
			return l1;
		} else {
			l2.next = mergeTwoLists(l1, l2.next);
			return l2;
		}
	}
	
}
