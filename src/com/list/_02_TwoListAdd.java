package com.list;

import com.common.ListNode;

/**
 * @Author: wangwei
 * @Description:
 * @Time: 2019/11/20 周三 17:44
 **/
public class _02_TwoListAdd {

    
    public static ListNode solution(ListNode l1, ListNode l2){
        ListNode head = new ListNode(0); // 建一个头结点使操作统一
        ListNode cur = head; // 当前位置
        ListNode p = l1;
        ListNode q = l2;
        int flag = 0; // 进位标志 0 / 1
        int sum = 0;  // 对应位的和
        while (p != null && q != null){
            sum = p.val + q.val + flag;
            flag = sum / 10; //两数和超出10，进位为1
            cur.next = new ListNode(sum % 10);
            p = p.next;
            q = q.next;
            cur = cur.next;
        }
        while (p != null){ // 某个链表已结束
            sum = flag + p.val;
            flag = sum / 10;
            cur.next = new ListNode(sum % 10);
            p = p.next;
            cur = cur.next;
        }
        while (q != null){
            sum = flag + q.val;
            flag = sum / 10;
            cur.next = new ListNode(sum % 10);
            q = q.next;
            cur = cur.next;
        }
        if (flag > 0){ //长的链表也结束了，但最后一次运算还是进位1了，再尾部追加一个节点
            cur.next = new ListNode(flag);
        }
        return head.next;
    }
    // 官方答案
    public static ListNode solution2(ListNode l1, ListNode l2){
        ListNode head = new ListNode(0); // 建一个头结点使操作统一
        ListNode p = l1, q = l2, cur = head; // 当前位置
        int flag = 0; // 进位标志 0 / 1
        while (p != null || q != null){
            int x = (p != null) ? p.val : 0; // 某个链表已遍历完了，当前位的值用0代替
            int y = (q != null) ? q.val : 0;
            int sum = x + y + flag;
            flag = sum / 10; //两数和超出10，进位为1
            cur.next = new ListNode(sum % 10);
            if (p != null) p = p.next; // 没结束的那个表遍历下一个节点
            if (q != null) q = q.next;
            cur = cur.next;
        }
        if (flag > 0){ //两个链表都结束了，但最后一次运算还是进位1了，在尾部追加一个节点
            cur.next = new ListNode(flag);
        }
        return head.next;
    }

    public static void main(String[] args) {
        
    }
}
