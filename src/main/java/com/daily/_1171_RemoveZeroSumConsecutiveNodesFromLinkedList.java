package com.daily;

import com.common.ListNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/6/11 19:55
 * @description: _1171_RemoveZeroSumConsecutiveNodesFromLinkedList
 *
 * 1171. 从链表中删去总和值为零的连续节点
 * 给你一个链表的头节点 head，请你编写代码，反复删去链表中由 总和 值为 0 的连续节点组成的序列，直到不存在这样的序列为止。
 *
 * 删除完毕后，请你返回最终结果链表的头节点。
 *
 *
 *
 * 你可以返回任何满足题目要求的答案。
 *
 * （注意，下面示例中的所有序列，都是对 ListNode 对象序列化的表示。）
 *
 * 示例 1：
 *
 * 输入：head = [1,2,-3,3,1]
 * 输出：[3,1]
 * 提示：答案 [1,2,1] 也是正确的。
 * 示例 2：
 *
 * 输入：head = [1,2,3,-3,4]
 * 输出：[1,2,4]
 * 示例 3：
 *
 * 输入：head = [1,2,3,-3,-2]
 * 输出：[1]
 *
 *
 * 提示：
 *
 * 给你的链表中可能有 1 到 1000 个节点。
 * 对于链表中的每个节点，节点的值：-1000 <= node.val <= 1000.
 * 通过次数33,112提交次数62,395
 */
public class _1171_RemoveZeroSumConsecutiveNodesFromLinkedList {

    /**
     * 方法：前缀和 + 哈希表
     *
     * 若链表节点的两个前缀和相等，说明两个前缀和之间的连续节点序列的和为 0，那么可以消去这部分连续节点。
     *
     * 我们第一次遍历链表，用哈希表 last 记录前缀和以及对应的链表节点，对于同一前缀和  s，后面出现的节点覆盖前面的节点，
     *                  即记录某个前缀和【最后一次】出现的位置节点。
     *
     * 接下来，我们再次遍历链表，并累计前缀和，若当前节点 cur 的前缀和 s 在 last 出现，说明 cur 与 last[s] 之间的所有节点和为 0，
     * 我们直接修改 cur 的指向，即 cur.next = last[s].next，这样就删去了这部分和为 0 的连续节点（并且是从第一次出现到组后一次出现的最长部分）。
     *
     * 继续往后遍历，以同样的判断和操作 删除所有和为 0 的连续节点。
     *
     * 最后返回链表的头节点 dummy.next。（这是为了操作统一，避免如 head 节点本身值为0 或 整条链表节点值和为0 等特殊情况）
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/remove-zero-sum-consecutive-nodes-from-linked-list/solution/python3javacgotypescript-yi-ti-yi-jie-qi-3vsy/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param head
     * @return
     */
    public ListNode removeZeroSumSublists(ListNode head) {
        Map<Integer, ListNode> sumMap = new HashMap<>();
        // 创建虚拟头节点dummy，next执向head
        ListNode dummy = new ListNode(0, head), f = dummy;
        // 初始化 f 指向 dummy，sum 为 0
        int sum = 0;
        // 顺序遍历
        while (f != null) {
            // 累加前缀和
            sum += f.val;
            // 更新前缀和对应的节点（再次出现就覆盖，记录最后一次出现对应的节点）
            sumMap.put(sum, f);
            f = f.next;
        }
        // 初始化 f 和 sum
        f = dummy;
        sum = 0;
        // 遍历
        while (f != null) {
            // 计算前缀和
            sum += f.val;
            // 找到这个前缀和最后一次出现时对应的节点，删除中间节点
            // 这里不用 判断 map.contains， 因为 map 记录了每个前缀和对应节点，
            // 如果 当前前缀和只出现了一次，即不存在从当前节点开始连续和为0的序列，那么 map.get(sum) 结果还是 f，这条语句不会有问题
            f.next = sumMap.get(sum).next;
            // 遍历下一个
            f = f.next;
        }
        // 返回 dummy.next
        return dummy.next;
    }
}
