package com.offerassult;

import com.common.Node;

/**
 * @author wangwei
 * @date 2022/10/22 19:04
 * @description: _028_FlattenMultilevelDoublyLinkedList
 *
 * 剑指 Offer II 028. 展平多级双向链表
 * 多级双向链表中，除了指向下一个节点和前一个节点指针之外，它还有一个子链表指针，可能指向单独的双向链表。这些子列表也可能会有一个或多个自己的子项，依此类推，生成多级数据结构，如下面的示例所示。
 *
 * 给定位于列表第一级的头节点，请扁平化列表，即将这样的多级双向链表展平成普通的双向链表，使所有结点出现在单级双链表中。
 *
 *
 *
 * 示例 1：
 *
 * 输入：head = [1,2,3,4,5,6,null,null,null,7,8,9,10,null,null,11,12]
 * 输出：[1,2,3,7,8,11,12,9,10,4,5,6]
 * 解释：
 *
 * 输入的多级列表如下图所示：
 *
 *
 *
 * 扁平化后的链表如下图：
 *
 *
 * 示例 2：
 *
 * 输入：head = [1,2,null,3]
 * 输出：[1,3,2]
 * 解释：
 *
 * 输入的多级列表如下图所示：
 *
 *   1---2---NULL
 *   |
 *   3---NULL
 * 示例 3：
 *
 * 输入：head = []
 * 输出：[]
 *
 *
 * 如何表示测试用例中的多级链表？
 *
 * 以 示例 1 为例：
 *
 *  1---2---3---4---5---6--NULL
 *          |
 *          7---8---9---10--NULL
 *              |
 *              11--12--NULL
 * 序列化其中的每一级之后：
 *
 * [1,2,3,4,5,6,null]
 * [7,8,9,10,null]
 * [11,12,null]
 * 为了将每一级都序列化到一起，我们需要每一级中添加值为 null 的元素，以表示没有节点连接到上一级的上级节点。
 *
 * [1,2,3,4,5,6,null]
 * [null,null,7,8,9,10,null]
 * [null,11,12,null]
 * 合并所有序列化结果，并去除末尾的 null 。
 *
 * [1,2,3,4,5,6,null,null,null,7,8,9,10,null,null,11,12]
 *
 *
 * 提示：
 *
 * 节点数目不超过 1000
 * 1 <= Node.val <= 10^5
 *
 *
 * 注意：本题与主站 430 题相同： https://leetcode-cn.com/problems/flatten-a-multilevel-doubly-linked-list/
 */
public class _028_FlattenMultilevelDoublyLinkedList {

    // prev始终为上一个处理到的节点，初始化
    Node prev = new Node();


    /**
     * dfs
     * 类似先序遍历，先访问自己，再访问child，再访问自己的next
     * 由于访问next时需要知道处理完child后的最后一个节点，所以 用一个全局变量来记录已处理的最后一个节点
     *
     * 当我们遍历到某个节点 node 时，如果它的 child 成员不为空，那么我们需要将 child 指向的链表结构进行扁平化，并且插入 node 与 node 的下一个节点之间。
     *
     * 因此，我们在遇到 child 成员不为空的节点时，就要先去处理 child 指向的链表结构，这就是一个「深度优先搜索」的过程。
     * 当我们完成了对 child 指向的链表结构的扁平化之后，就可以「回溯」到 node 节点。
     *
     * 为了能够将扁平化的链表插入 node 与 node 的下一个节点之间，我们需要知道扁平化的链表的最后一个节点 last，随后进行如下的三步操作：
     *
     *      将 node 与 node 的下一个节点 next 断开：
     *      将 node 与 child 相连；
     *      将 last 与 next 相连。
     *
     * 这样一来，我们就可以将扁平化的链表成功地插入
     *
     * 细节
     *
     * 需要注意的是，node 可能没有下一个节点，即 next 为空。此时，我们只需进行第二步操作。
     * 此外，在插入扁平化的链表后，我们需要将 node 的 child 成员置为空。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/flatten-a-multilevel-doubly-linked-list/solution/bian-ping-hua-duo-ji-shuang-xiang-lian-b-383h/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param h
     * @return
     */
    public Node flatten(Node h) {
        if (h == null) {
            return h;
        }
        dfs(h);
        // 因为有个初始节点，所以会导致 h的prev指向初始节点，需要断开这个链接
        h.prev = null;
        return h;
    }

    private void dfs(Node node) {
        if (node == null) {
            return;
        }
        // 先访问自己，就是 prev 链接 自己，自己 链接prev，
        Node next = node.next;
        prev.next = node;
        node.prev = prev;
        // prev 变为自己
        prev = node;
        // dfs访问自己的child
        if (node.child != null) {
            dfs(node.child);
            // 访问完后一定要断开原来的链接
            node.child = null;
        }
        // 访问完child后，访问next
        dfs(next);
    }


    /**
     * 还是dfs，如果不适用全局变量prev，就需要知道 flatten 得到的链表的最后一个节点
     * @param head
     * @return
     */
    public Node flatten1(Node head) {
        if (head == null) {
            return head;
        }
        dfs1(head);
        return head;
    }

    /**
     * flatten node 开头的链表，返回 flatten 后链表的最后一个节点
     * @param node
     * @return
     */
    private Node dfs1(Node node) {
        // cur 链表当前处理到的节点，last 已处理完部分的最后一个节点
        Node cur = node, last = node;
        while (cur != null) {
            // 先记录next
            Node next = cur.next;
            // 有child
            if (cur.child != null) {
                // 先展开child
                Node childLast = dfs1(cur.child);
                // 将 child 部分的链表 接在 cur 和 next 中间
                cur.next = cur.child;
                cur.child.prev = cur;
                // 断开child指针
                cur.child = null;
                childLast.next = next;
                if (next != null) {
                    next.prev = childLast;
                }
                // 拼接后，last应该是原child的最后一个节点
                last = childLast;
            } else {
                // 没有child，last指向自己
                last = cur;
            }
            // 遍历下一个
            cur = next;
        }
        // 结束时，last就是整个处理完后的链表的最后一个节点
        return last;
    }
}
