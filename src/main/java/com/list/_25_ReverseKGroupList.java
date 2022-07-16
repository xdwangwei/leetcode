package com.list;

import com.common.ListNode;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/12/12 周四 12:49
 *
 * 给你一个链表，每 k 个节点一组进行翻转，请你返回翻转后的链表。
 *
 * k 是一个正整数，它的值小于或等于链表的长度。
 *
 * 如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 *
 *  
 * 5
 *
 * 示例：
 *
 * 给你这个链表：1->2->3->4->5
 *
 * 当 k = 2 时，应当返回: 2->1->4->3->5
 *
 * 当 k = 3 时，应当返回: 3->2->1->4->5
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-nodes-in-k-group
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 **/
@SuppressWarnings("ALL")
public class _25_ReverseKGroupList {

	/**
	 * 递归2， 先反转前k个节点，再连接上后面的反转
	 * @param head
	 * @param k
	 * @return
	 */
	public ListNode solution2(ListNode head, int k) {
		if (head == null) return null;
		ListNode end = head;
		// 找到head开始的第k+1个节点
		for (int i = 0; i < k; ++i) {
			// 不足k个就直接返回
			if (end == null) return head;
			end = end.next;
		}
		// 反转前k个节点，反转后head成为前k个节点的最后一个节点
		ListNode newHead = reverseBetween(head, end);
		// 递归反转从end开始的k个节点，连接到前k个节点最后一个节点的后面
		head.next = solution2(end, k);
		// 返回前k个节点反转后的头结点，也就是新链表的头结点
		return newHead;
	}

	/**
	 * 反转 [a, b) 之间的链表，注意是左闭右开
	 * @param a
	 * @param b
	 * @return
	 */
	private ListNode reverseBetween(ListNode a, ListNode b) {
		ListNode pre = null, cur = a, nxt = null;
		while (cur != b) {
			nxt = cur.next;
			// 箭头反向
			cur.next = pre;
			pre = cur;
			cur = nxt;
		}
		// 返回反转后的头结点
		return pre;
	}

	/**
	 * 递归1
	 * @param head
	 * @param k
	 * @return
	 */
	public ListNode solution(ListNode head, int k) {
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		// pre指向头结点
		ListNode pre = dummy, temp;
		int count;
		// 每k组进行反转
		while (null != pre.next) {
			temp = pre.next;
			count = 1;
			// 判断当前这一组是否够k个
			while (count < k && null != temp) {
				temp = temp.next;
				++count;
			}
			// 足够k个
			if (null != temp)
				// count = k，够k个，就把pre之后的k个节点反转，pre移到这k个节点的最后一个节点
				// 作为下一组要反转的k的节点的头结点
				pre = reverseKNodes(pre, k);
				// 不足k个节点，保留
			else
				break;
		}
		return dummy.next;
	}

	/**
	 * 反转head之后的k个节点，并返回反转后的K个的最后一个节点的位置
	 * 链表的倒序，头结点不变，节点一个一个拆下来往前面放(每次拆下来的节点都会放在头结点的后面，成为新的首节点)
	 * 如果倒序k个节点，则需要拆k-1个，第一个节点不用拆
	 * 如：head -> 1 -> 2 -> 3 -> 4，反转4个节点
	 * 拆2放在1前面(head后面)  head -> 2 -> 1 -> 3 -> 4
	 * 拆3放在2前面(head后面)  head -> 3 -> 2 -> 1 -> 4
	 * 拆4放在3前面(head后面)  head -> 4 -> 3 -> 2 -> 1
	 * @param head
	 * @param k
	 * @return
	 */
	private ListNode reverseKNodes(ListNode head, int k) {
		// k个中的第一个
		ListNode cur = head.next, nxt;
		while (--k > 0){
			// 第二个节点
			nxt = cur.next;
			// 拆下第二个节点
			cur.next = nxt.next;
			// 第二个节点放在第一个节点前面
			nxt.next = head.next;
			// 头结点指向第二个节点
			head.next = nxt;
			// 一次循环后，节点1自动变到第二个位置，后面跟着3
		}
		// 返回反转后的最后位置节点
		head = cur;
		return head;
	}
}
