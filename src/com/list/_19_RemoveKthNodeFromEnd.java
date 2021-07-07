package com.list;

import com.common.ListNode;

/**
 * @Author: wangwei
 * @Description: 删除链表的倒数第 n 个节点，并且返回链表的头结点
 * @Time: 2019/12/11 周三 00:40
 **/
public class _19_RemoveKthNodeFromEnd {
	
	public ListNode solution1(ListNode head, int k) {
		int len = 0;
		ListNode h = head;
		// 计算出链表节点个数
		while (h != null) {
			h = h.next;
			++len;
		}
		// 创建一个头节点
		ListNode l = new ListNode(0);
		// 保存初始节点位置
		h = l;
		// 删除正着数第 len -k + 1 个顶点
		int i = len - k + 1;
		while (--i > 0){
			l.next = head;
			l = l.next;
			head = head.next;
		}
		// 跳过次节点
		l.next = head.next;
		return h.next;
	}
	
	/**
	 * 我们可以设想假设设定了双指针 p 和 q 的话，当 q 指向末尾的 NULL，p 与 q 之间相隔的元素个数为 n 时，那么删除掉 p 的下一个指针就完成了要求。
	 * 设置虚拟节点 dummyHead 指向 head
	 * 设定双指针 p 和 q，初始都指向虚拟节点 dummyHead
	 * 移动 q，直到 p 与 q 之间相隔的元素个数为 n
	 * 同时移动 p 与 q，直到 q 指向的为 NULL
	 * 将 p 的下一个节点指向下下个节点
	 */
	public ListNode solutionBetter(ListNode head, int n){
		// 创建头节点让操作统一
		ListNode dummyHead = new ListNode(0);
		dummyHead.next = head;
		ListNode p = dummyHead;
		ListNode q = dummyHead;
		// 先让q向后移N节点i，这里为什么是 <= 画图就能明白
		for (int i = 0; i <= n; ++i){
			q = q.next;
			// 可有可无，此种情况说明head链表的节点数不足n个,则直接返回
			if (q == null) {
				return dummyHead.next;
			}
		}
		// 再让p和q同时后移，当q移动到null时，p就跳过这个节点(此节点就是要删除的节点)
		// 可以画个5个节点的链表自己试一下
		while (q != null){
			q = q.next;
			p = p.next;
		}
		p.next = p.next.next;
		return dummyHead.next;
	}
}
