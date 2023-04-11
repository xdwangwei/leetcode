package com.daily;

import com.common.ListNode;

import java.util.*;

/**
 * @author wangwei
 * @date 2023/4/10 21:09
 * @description: _1019_NextLargerNodeInList
 *
 * 1019. 链表中的下一个更大节点
 * 给定一个长度为 n 的链表 head
 *
 * 对于列表中的每个节点，查找下一个 更大节点 的值。也就是说，对于每个节点，找到它旁边的第一个节点的值，这个节点的值 严格大于 它的值。
 *
 * 返回一个整数数组 answer ，其中 answer[i] 是第 i 个节点( 从1开始 )的下一个更大的节点的值。如果第 i 个节点没有下一个更大的节点，设置 answer[i] = 0 。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：head = [2,1,5]
 * 输出：[5,5,0]
 * 示例 2：
 *
 *
 *
 * 输入：head = [2,7,4,3,5]
 * 输出：[7,0,5,5,0]
 *
 *
 * 提示：
 *
 * 链表中节点数为 n
 * 1 <= n <= 104
 * 1 <= Node.val <= 109
 * 通过次数47,054提交次数73,742
 */
public class _1019_NextLargerNodeInList {

    /**
     * 方法一：单调栈
     *
     * 题目要求找到链表中每个节点的下一个更大的节点，即找到链表中每个节点的右边第一个比它大的节点。我们先遍历链表，将链表中的值存入数组 nums 中。
     *
     * 那么对于数组 nums 中的每个元素，我们只需要找到它右边第一个比它大的元素即可。
     *
     * 求下一个更大的元素的问题可以使用单调栈来解决。
     *
     * 我们从前往后遍历数组 nums，维护一个从栈底到栈顶单调递减的栈 stk（并不严格递减，可能相等），
     *
     * 顺序遍历nums元素过程中，如果栈顶元素小于当前元素，则当前元素是栈顶元素右边最近的比它大的元素，
     *
     * 循环将栈顶元素出栈，直到栈顶元素大于等于当前元素或者栈为空。
     *
     * 当前元素入栈
     *
     * 这样做的问题在于 对于nums的最后一个元素，因为后边没有元素了，没人可以让他出栈，也就是找不到它的右边比它大的元素，
     * 那么按照题意，它的右边比它大的最近元素 需要赋值为 0，
     * 我们用 int[] 记录每个元素右边比它大的最近元素，初始时默认都是0，因此就不需要单独处理，非要让最后一个元素也能出栈
     *
     * 单调栈，栈中记录的是元素的下标
     *
     * 遍历结束后，返回答案数组 ans 即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/next-greater-node-in-linked-list/solution/python3javacgotypescript-yi-ti-yi-jie-da-jghz/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param head
     * @return
     */
    public int[] nextLargerNodes(ListNode head) {
        List<Integer> nums = new ArrayList<>();
        ListNode cur = head;
        // 顺序得到链表长度为每个节点值
        while (cur != null) {
            nums.add(cur.val);
            cur = cur.next;
        }
        int n = nums.size();
        int[] ans = new int[n];
        // 单调栈，求 nums 中 每个元素 下一个更大元素
        Deque<Integer> stack = new ArrayDeque<>();
        // 顺序遍历
        for (int i = 0; i < n; ++i) {
            // 当前元素值
            int x = nums.get(i);
            // 栈非空  &&  当前元素 > 栈顶
            while (!stack.isEmpty() && nums.get(stack.peek()) < x) {
                // 那么，栈顶元素的 下一个更大元素就是 当前元素
                // 注意栈里存的是下标
                ans[stack.pop()] = x;
            }
            // 当前元素下标入栈
            stack.push(i);
        }
        // 返回
        return ans;
    }
}
