package com.list;

import com.common.ListNode;

/**
 * @Author: wangwei
 * @Description: 给定一个链表，两两交换其中相邻的节点，并返回交换后的链表
 * @Time: 2019/12/12 周四 00:28
 **/
public class _24_SwapListPairs {
	
	public ListNode solution(ListNode head) {
		if (null == head || null == head.next) return head;
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		ListNode pre = dummy, cur, nxt;
		while (null != pre.next) {
			cur = pre.next;
			
			nxt = cur.next;
			// 当前是最后一个节点，不存在2个一组翻转
			if (null == nxt) break;
			cur.next = nxt.next;
			pre.next = nxt;
			nxt.next = cur;
			
			pre = cur;
		}
		return dummy.next;
	}
	
	public static void main(String[] args) {
		
	}
}
