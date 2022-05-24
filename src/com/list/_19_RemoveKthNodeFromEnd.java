package com.list;

import com.common.ListNode;

/**
 * @Author: wangwei
 * @Description: 删除链表的倒数第 n 个节点，并且返回链表的头结点
 * @Time: 2019/12/11 周三 00:40
 *
 * 19. 删除链表的倒数第 N 个结点
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
public class _19_RemoveKthNodeFromEnd {


	/**
	 * 方法一：两次扫描
	 * 第一次扫描：计算出链表长度，从而得到倒数第k是第几个节点
	 * 第二次扫描：完成第n-k个节点的移除
	 * @param head
	 * @param k
	 * @return
	 */
	public ListNode removeNthFromEnd1(ListNode head, int k) {
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
	public ListNode removeNthFromEndBetter(ListNode head, int n){
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

	/**
	 * 删除链表倒数第n个节点
	 * 先找到链表倒数第n+1个节点。然后 p.next = p.next.next即可。
	 * 因为我们从后往前多走了一步，所以为了避免空指针，增加一个头结点。它并不会影响最终结果，因为最后还是返回dummy.next
	 * @param head
	 * @param n
	 * @return
	 */
	public ListNode removeNthFromEnd(ListNode head, int n) {
		// 虚拟头节点
		ListNode dummy = new ListNode(-1);
		dummy.next = head;
		// 删除倒数第 n 个，要先找倒数第 n + 1 个节点
		ListNode x = findFromEnd(dummy, n + 1);
		// 删掉倒数第 n 个节点
		x.next = x.next.next;
		return dummy.next;
	}

	/**
	 * 单链表的倒数第k个节点
	 *
	 * 从前往后寻找单链表的第k个节点很简单，一个 for 循环遍历过去就找到了，但是如何寻找从后往前数的第k个节点呢？
	 *
	 * 那你可能说，假设链表有n个节点，倒数第k个节点就是正数第n - k个节点，不也是一个 for 循环的事儿吗？
	 *
	 * 是的，但是算法题一般只给你一个ListNode头结点代表一条单链表，你不能直接得出这条链表的长度n，而需要先遍历一遍链表算出n的值，然后再遍历链表计算第n - k个节点。
	 *
	 * 也就是说，这个解法需要遍历两次链表才能得到出倒数第k个节点。
	 *
	 * 那么，我们能不能只遍历一次链表，就算出倒数第k个节点？可以做到的，如果是面试问到这道题，面试官肯定也是希望你给出只需遍历一次链表的解法。
	 *
	 * 这个解法就比较巧妙了，假设k = 2，思路如下：
	 *
	 * 首先，我们先让一个指针p1指向链表的头节点head，然后走k步：
	 * 现在的p1，只要再走n - k步，就能走到链表末尾的空指针了对吧？
	 *
	 * 趁这个时候，再用一个指针p2指向链表头节点head：
	 * 接下来就很显然了，让p1和p2同时向前走，p1走到链表末尾的空指针时走了n - k步，p2也走了n - k步，也就恰好到达了链表的倒数第k个节点：
	 * 这样，只遍历了一次链表，就获得了倒数第k个节点p2。
	 * @param head
	 * @param k
	 * @return
	 */
	private ListNode findFromEnd(ListNode head, int k) {
		ListNode p1 = head, p2 = head;
		// p1 先向前走k步
		for (int i = 0; i < k; i++) {
			p1 = p1.next;
		}
		// p2和p1同时向前，p1走到null的时候代表着p2走了n-k步，也就是到达了倒数第k个节点
		while (p1 != null) {
			p2 = p2.next;
			p1 = p1.next;
		}
		// 此时返回p2即可
		return p2;
	}
}
