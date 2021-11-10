package com.list;

import com.common.ListNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @Author: wangwei
 * @Description: 合并K个有序链表
 * @Time: 2019/12/11 周三 08:53
 **/
public class _23_MergeKLists {

	public ListNode solution1(ListNode[] lists) {
		ListNode dummy = new ListNode(0);
		ListNode cur = dummy;
		List<Integer> list = new ArrayList<>();
		int k = lists.length;
		for (int i = 0; i < k; i++) {
			while (null != lists[i]) {
				list.add(lists[i].val);
				lists[i] = lists[i].next;
			}
		}
		Object[] array = list.toArray();
		Arrays.sort(array);
		for (int i = 0; i < array.length; i++) {
			cur.next = new ListNode((Integer) array[i]);
			cur = cur.next;
		}
		return dummy.next;
	}
	
	/**
	 * 优先队列
	 *
	 * @param lists
	 * @return
	 */
	public ListNode solution2(ListNode[] lists) {
		ListNode dummy = new ListNode(0);
		ListNode cur = dummy;
		PriorityQueue<Integer> queue = new PriorityQueue<>();
		int k = lists.length;
		for (int i = 0; i < k; i++) {
			while (null != lists[i]) {
				queue.add(lists[i].val);
				lists[i] = lists[i].next;
			}
		}
		while (!queue.isEmpty()) {
			cur.next = new ListNode(queue.poll());
			cur = cur.next;
		}
		return dummy.next;
	}

	/**
	 * 优先队列
	 *
	 * @param lists
	 * @return
	 */
	public ListNode solution21(ListNode[] lists) {
		if (lists.length == 0) {
			return null;
		}
		PriorityQueue<ListNode> pq = new PriorityQueue<ListNode>((o1, o2) -> o1.val - o2.val);
		for (int i = 0; i < lists.length; ++i) {
			if (lists[i] != null) {
				pq.offer(lists[i]);
			}
		}
		ListNode dummy = new ListNode(-1);
		ListNode head = dummy;
		while (!pq.isEmpty()) {
			ListNode cur = pq.poll();
			dummy.next = cur;
			dummy = dummy.next;
			if (cur.next != null) {
				pq.offer(cur.next);
			}
		}
		return head.next;
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
