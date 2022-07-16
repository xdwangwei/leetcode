package com.recursion;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/12/11 周三 08:53
 **/
public class _21_MergeTwoLists {
	
	private static class ListNode {
		int val;
		ListNode next;
		
		ListNode(int x) {
			val = x;
		}
	}
	
	public ListNode solution1(ListNode l1, ListNode l2) {
		ListNode dummyHead = new ListNode(0);
		ListNode p = l1, q = l2, cur = dummyHead;
		while (p != null && q != null) {
			if (p.val < q.val) {
				cur.next = p;
				p = p.next;
			} else {
				cur.next = q;
				q = q.next;
			}
			cur = cur.next;
		}
		// 某个链表遍历结束了，但另一个还没有，就把它剩下的接在后面
		cur.next = p == null ? q : p;
		return dummyHead.next;
	}
	
	/**
	 * 递归
	 * list1[0]+merge(list1[1:],list2)  list1[0]<list2[0]
	 * list2[0]+merge(list1,list2[1:])  otherwise
	 * 两个链表头部较小的一个,剩下元素与另一个链表 merge 操作结果合并，再将结果连接到自己的下一个
	 */
	public ListNode solution2(ListNode l1, ListNode l2) {
		if (l1 == null) return l2;
		else if (l2 == null) return l1;
		else if (l1.val < l2.val) {
			// 注意此处的next
			l1.next = solution2(l1.next, l2);
			return l1;
		} else {
			l2.next = solution2(l2.next, l1);
			return l2;
		}
	}
}
