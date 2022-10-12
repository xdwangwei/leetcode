package com.daily;

import com.common.ListNode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/10/12 18:45
 * @description: _817_LinkedListComponents
 *
 * 817. 链表组件
 * 给定链表头结点 head，该链表上的每个结点都有一个 唯一的整型值 。同时给定列表 nums，该列表是上述链表中整型值的一个子集。
 *
 * 返回列表 nums 中组件的个数，这里对组件的定义为：链表中一段最长连续结点的值（该值必须在列表 nums 中）构成的集合。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入: head = [0,1,2,3], nums = [0,1,3]
 * 输出: 2
 * 解释: 链表中,0 和 1 是相连接的，且 nums 中不包含 2，所以 [0, 1] 是 nums 的一个组件，同理 [3] 也是一个组件，故返回 2。
 * 示例 2：
 *
 *
 *
 * 输入: head = [0,1,2,3,4], nums = [0,3,1,4]
 * 输出: 2
 * 解释: 链表中，0 和 1 是相连接的，3 和 4 是相连接的，所以 [0, 1] 和 [3, 4] 是两个组件，故返回 2。
 *
 *
 * 提示：
 *
 * 链表中节点数为n
 * 1 <= n <= 104
 * 0 <= Node.val < n
 * Node.val 中所有值 不同
 * 1 <= nums.length <= n
 * 0 <= nums[i] < n
 * nums 中所有值 不同
 * 通过次数37,980提交次数62,235
 */
public class _817_LinkedListComponents {

    /**
     * 简化：寻找list中所有 段中数字全部在nums中存在 的所有最长段的数量
     * 根据题意进行模拟即可 :
     * 为了方便判断某个 node.val 是否存在于 nums 中，我们先使用 Set 结构对所有的 nums[i] 进行转存，
     * 随后每次检查连续段（组件）的个数。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/linked-list-components/solution/by-ac_oier-3gl5/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param head
     * @param nums
     * @return
     */
    public int numComponents(ListNode head, int[] nums) {
        int ans = 0;
        Set<Integer> set = new HashSet<>();
        for (int x : nums) {
            set.add(x);
        }
        while (head != null) {
            // 寻找list中所有 段中数字全部在nums中存在 的段的数量
            if (set.contains(head.val)) {
                // 段中数字全部在list中出现的最长段
                while (head != null && set.contains(head.val)) head = head.next;
                ans++;
            } else {
                // 另起炉灶
                head = head.next;
            }
        }
        return ans;
    }
}
