package com.design;

import com.common.ListNode;

import java.util.Random;

/**
 * @author wangwei
 * 2020/8/31 23:11
 *
 * 给定一个单链表，随机选择链表的一个节点，并返回相应的节点值。保证每个节点被选的概率一样。
 *
 * 进阶:
 * 如果链表十分大且长度未知，如何解决这个问题？你能否使用常数级空间复杂度实现？
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/linked-list-random-node
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _382_LinkedListRandomNode {

    /**
     * 在不知道总数的情况下，随机的从中选出1或多个元素，并且要求这些数字被选取的概率相等，并且只能遍历原序列一次。
     * 一般思想：先遍历一次，得到元素总数n，然后遍历第二次，对每个元素以1/n的概率进行选取。但不符合只能遍历一次的要求
     *
     * 蓄水池抽样：
     *      如果抽1个，对于遇到的第i个元素，以 1/i 的概率选取它。
     *      如果抽k个，对于遇到的第i个元素，以 k/i 的概率选取它
     * 很多材料上来就告诉你第 i 个节点要以1/i 概率选取，然后再去证明这个值是对的。
     * 但问题是我要怎么想到这个值呢？
     * 这时候就要用到终局思维了：
     *      当走到最后一个节点 n 时，要保证最后一个节点被选取概率是1/n，
     *      那么这时就以1/n 概率选取这个值，并替换当前值。
     *      这时候再往前推一步，要保证 n-1最终被选取的概率也是1/n，
     *      那么当走到n-1时，要以什么概率选取？
     *      假设这个概率是 x，那么 n-1最终被选取的概率就是x * (1-1/n)，
     *      其含义就是，n-1被选到，并且n没被选到(选择n相当于替换n-1)，
     * 那么最终留下的就是 n-1的值。
     *      那么我们就推出来 x * (1-1/n) == 1/n，x 的值就是1/n-1。有没有霍然开朗的感觉？
     *
     * 以上思维方式，也可以推广到选取 k 个，
     * 稍微一点改变是，在看元素n-1最终是否被保留下来时，等式是 x * (1-k/n * 1/k) == k / n，
     * k/n * 1/k的含义是 n被选取了(k/n)，并且将集合中的 n-1给替换掉了(概率就是从 k 个里选1个进行替换），
     * 再解释一下就是： n-1先被以 x 的概率选取，而后又被 n替换掉。
     * 因此n-1最终保留在集合中的概率就是 x * (1-k/n * 1/k) == k / n，x = k / n-1
     * 所以 k / n-1 的概率选择第 n-1 个元素
     *
     * 按照这种策略(以k/i的概率选择第i个元素)，
     *      那么对于前k个元素，直接选取(概率都大于1)，
     *      对于第k个往后的元素，以 k/i 的概率进行选取，以 1/k 的概率进行替换前k个元素中的某一个
     *
     *
     * 关于正向推理(数学归纳法)：
     *      https://labuladong.gitbook.io/algo/gao-pin-mian-shi-xi-lie/shui-tang-chou-yang
     *      https://leetcode-cn.com/problems/linked-list-random-node/solution/xu-shui-chi-chou-yang-suan-fa-by-jackwener/
     *
     * 作者：li-zi-he
     * 链接：https://leetcode-cn.com/problems/linked-list-random-node/solution/zhong-ju-si-wei-by-li-zi-he/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    private ListNode head;

    /** @param head The linked list's head.
    Note that the head is guaranteed to be not null, so it contains at least one node. */
    public _382_LinkedListRandomNode(ListNode head) {
        this.head = head;
    }

    /** 返回链表中一个随机节点的值，要求每次random选择每个节点的概率值一样 */
    public int getRandom() {
        Random r = new Random();
        ListNode p = head;
        int i = 0, res = 0;
        while (p != null) {
            // 以 1/i 的概率选择当前节点(替换了之前的)
            if (r.nextInt(++i) == 0) {
                res = p.val;
            }
            p = p.next;
        }
        return res;
    }

    /* 返回链表中 k 个随机节点的值 */
    int[] getRandom(ListNode head, int k) {
        Random r = new Random();
        int[] res = new int[k];
        ListNode p = head;
        // 以 k/i 的概率选择第i个元素
        // 前 k 个元素先默认选上(被选的概率大于1)
        for (int j = 0; j < k && p != null; j++) {
            res[j] = p.val;
            p = p.next;
        }
        // 对于第k个之后的元素，以 k/i 的概率进行选择
        int i = k;
        // while 循环遍历链表
        while (p != null) {
            // 生成一个 [0, i) 之间的整数
            int j = r.nextInt(++i);
            // 这个整数小于 k 的概率就是 k/i
            if (j < k) {
                // 当前节点被选择，就会替换前k个中的某个
                res[j] = p.val;
            }
            p = p.next;
        }
        return res;
    }
}
