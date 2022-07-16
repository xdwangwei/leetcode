package com.offerassult;

import com.common.ListNode;


/**
 * @Author: wangwei
 * @Description: 删除链表的倒数第 n 个节点，并且返回链表的头结点
 * @Time: 2019/12/11 周三 00:40
 *
 * 剑指 Offer II 021 删除链表的倒数第 N 个结点
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：head = [1,2,3,4,5], n = 2
 * 输出：[1,2,3,5]
 * 示例 2：
 *
 * 输入：head = [1], n = 1
 * 输出：[]
 * 示例 3：
 *
 * 输入：head = [1,2], n = 1
 * 输出：[1]
 *
 *
 * 提示：
 *
 * 链表中结点的数目为 sz
 * 1 <= sz <= 30
 * 0 <= Node.val <= 100
 * 1 <= n <= sz
 *
 *
 * 进阶：你能尝试使用一趟扫描实现吗？
 **/
public class _021_RemoveKthNodeFromEnd {


    /**
     * 方法一：两次扫描
	 * 第一次扫描：计算出链表长度，从而得到倒数第k是第几个节点
	 * 第二次扫描：完成第n-k个节点的移除
	 *
	 * 方法二：一次扫描，双指针
	 * 先让first前进到第k个位置
	 * 然后让second指向head，然后second和first同时前进，当first变为null时，second恰好是倒数第k个节点
	 * 考虑到移除节点需要找到前驱，并且要移除的节点可能刚好是head
	 * 因此我们创建一个亚节点dummy，并让dummy.next=head
	 * 这样，还是 先让first前进到第k个位置
	 * 然后second从dummy出发，first和second同时前进
	 * 此时，当first变为null时，second恰好指向的是倒数第k个节点的前驱节点
	 * 此时只需要second.next = second.next.next;就能删除目标节点
	 * 最后返回dummy.next即可
	 * @param head
     * @param k
     * @return
	 */
	public ListNode removeNthFromEnd(ListNode head, int k) {
		// 先让first前进到第k个节点
		ListNode first = head;
		for (int i = 0; i < k; ++i) {
			first = first.next;
		}
		// 创建一个亚节点，next指向head
		ListNode dummy = new ListNode(-1, head);
		// 让second指向亚节点
		ListNode second = dummy;
		// second和first一起前进，直到first指向null
		while (first != null) {
			first = first.next;
			second = second.next;
		}
		// 此时second就是倒数第k个节点的前驱节点，移除节点
		second.next = second.next.next;
		// 返回
		return dummy.next;
	}

}
