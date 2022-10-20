package com.list;

import com.common.ListNode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/10/20 13:04
 * @description: _445_AddToNumbers2
 *
 * 445. 两数相加 II
 * 给你两个 非空 链表来代表两个非负整数。数字最高位位于链表开始位置。它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。
 *
 * 你可以假设除了数字 0 之外，这两个数字都不会以零开头。
 *
 *
 *
 * 示例1：
 *
 *
 *
 * 输入：l1 = [7,2,4,3], l2 = [5,6,4]
 * 输出：[7,8,0,7]
 * 示例2：
 *
 * 输入：l1 = [2,4,3], l2 = [5,6,4]
 * 输出：[8,0,7]
 * 示例3：
 *
 * 输入：l1 = [0], l2 = [0]
 * 输出：[0]
 *
 *
 * 提示：
 *
 * 链表的长度范围为 [1, 100]
 * 0 <= node.val <= 9
 * 输入数据保证链表代表的数字无前导 0
 *
 *
 * 进阶：如果输入链表不能翻转该如何解决
 */
public class _445_AddToNumbers2 {


    /**
     * 先反转链表，低位对其，从低位往高位逐位计算，需要考虑进位，以及最终向最高位进位
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // 有1个是0，返回另一个即可
        if (l1.val == 0) {
            return l2;
        }
        if (l2.val == 0) {
            return l1;
        }
        // 反转后，都是从最低位开始
        l1 = reverseList(l1);
        l2 = reverseList(l2);
        // 向前进位
        int cnt = 0;
        ListNode l3 = new ListNode(-1), cur = l3;
        // while (l1 != null || l2 != null) {
        //     int a = l1 == null ? 0 : l1.val;
        //     int b = l2 == null ? 0 : l2.val;
        //     // 二者相加再加进位
        //     int sum = a + b + cnt;
        //     // 当前位置保留余数
        //     cur.next = new ListNode(sum % 10);
        //     // 更新进位
        //     cnt = sum / 10;
        //     // 指针递进
        //     cur = cur.next;
        //     if (l1 != null) {
        //         l1 = l1.next;
        //     }
        //     if (l2 != null) {
        //         l2 = l2.next;
        //     }
        // }
        // // 存在向最高位进位
        // if (cnt > 0) {
        //     cur.next = new ListNode(cnt);
        // }
        // 合并写法
        while (l1 != null || l2 != null || cnt != 0) {
            int a = l1 == null ? 0 : l1.val;
            int b = l2 == null ? 0 : l2.val;
            // 二者相加再加进位
            int sum = a + b + cnt;
            // 当前位置保留余数
            cur.next = new ListNode(sum % 10);
            // 更新进位
            cnt = sum / 10;
            // 指针递进
            cur = cur.next;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        // 返回前再次反转，让高位在前
        return reverseList(l3.next);
    }


    /**
     * 反转链表
     * @param head
     * @return
     */
    private ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode nh = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return nh;
    }


    /**
     * 不反转链表，通过栈结构完成低位高位调整
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        Deque<Integer> stack1 = new ArrayDeque<>();
        Deque<Integer> stack2 = new ArrayDeque<>();
        while (l1 != null) {
            stack1.push(l1.val);
            l1 = l1.next;
        }
        while (l2 != null) {
            stack2.push(l2.val);
            l2 = l2.next;
        }
        // 向高位进位
        int carry = 0;
        ListNode ans = null;
        // 入栈后，栈顶都是最低位
        while (!stack1.isEmpty() || !stack2.isEmpty() || carry != 0) {
            int a = stack1.isEmpty() ? 0 : stack1.pop();
            int b = stack2.isEmpty() ? 0 : stack2.pop();
            int cur = a + b + carry;
            carry = cur / 10;
            cur %= 10;
            // 头插法
            ListNode curnode = new ListNode(cur);
            curnode.next = ans;
            ans = curnode;
        }
        return ans;
    }
}
