package com.offerassult;

import java.util.PriorityQueue;

/**
 * @author wangwei
 * @date 2022/12/13 10:42
 * @description: _059_KthLargestNumberInAStream
 *
 * 剑指 Offer II 059. 数据流的第 K 大数值
 * 设计一个找到数据流中第 k 大元素的类（class）。注意是排序后的第 k 大元素，不是第 k 个不同的元素。
 *
 * 请实现 KthLargest 类：
 *
 * KthLargest(int k, int[] nums) 使用整数 k 和整数流 nums 初始化对象。
 * int add(int val) 将 val 插入数据流 nums 后，返回当前数据流中第 k 大的元素。
 *
 *
 * 示例：
 *
 * 输入：
 * ["KthLargest", "add", "add", "add", "add", "add"]
 * [[3, [4, 5, 8, 2]], [3], [5], [10], [9], [4]]
 * 输出：
 * [null, 4, 5, 5, 8, 8]
 *
 * 解释：
 * KthLargest kthLargest = new KthLargest(3, [4, 5, 8, 2]);
 * kthLargest.add(3);   // return 4
 * kthLargest.add(5);   // return 5
 * kthLargest.add(10);  // return 5
 * kthLargest.add(9);   // return 8
 * kthLargest.add(4);   // return 8
 *
 *
 * 提示：
 *
 * 1 <= k <= 104
 * 0 <= nums.length <= 104
 * -104 <= nums[i] <= 104
 * -104 <= val <= 104
 * 最多调用 add 方法 104 次
 * 题目数据保证，在查找第 k 大元素时，数组中至少有 k 个元素
 *
 *
 * 注意：本题与主站 703 题相同： https://leetcode-cn.com/problems/kth-largest-element-in-a-stream/
 *
 * 通过次数18,685提交次
 */
public class _059_KthLargestNumberInAStream {

    /**
     * 方法一：优先队列
     * 我们可以使用一个大小为 k 的优先队列来存储前 k 大的元素，其中优先队列的队头为队列中最小的元素，也就是第 k 大的元素。
     *
     * 本题的操作步骤如下：
     *
     * 使用大小为 k 的小根堆，在初始化的时候，将nums中元素加入堆中，并保证堆中的元素个数不超过 K。
     * 在每次 add() 的时候，将新元素 push() 到堆中，如果此时堆中的元素超过了 K，那么需要把堆中的最小元素（堆顶）pop()出来。
     * 此时堆中的最小元素（堆顶）就是整个数据流中的第 K 大元素。
     *
     * 作者：fuxuemingzhu
     * 链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/solution/mian-shi-ti-jing-gao-jing-dian-topk-ben-u7w30/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     *
     * 问答：
     *
     * 为什么使用小根堆？
     *
     * 因为我们需要在堆中保留数据流中的前 K大元素，使用小根堆能保证每次调用堆的 pop()函数时，从堆中删除的是堆中的最小的元素（堆顶）。
     * 为什么能保证堆顶元素是第 K大元素？
     * 因为小根堆中保留的一直是堆中的前 K大的元素，堆的大小是 K，所以堆顶元素是第 K大元素。
     * 每次 add()的时间复杂度是多少？
     * 每次 add()时，调用了堆的 push()和 pop()方法，两个操作的时间复杂度都是 log(K).
     *
     * 作者：fuxuemingzhu
     * 链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/solution/mian-shi-ti-jing-gao-jing-dian-topk-ben-u7w30/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class KthLargest {

        // 维护大小为k的小顶堆存储前k大的元素，堆顶为第k大元素（也就是k个最大值中的最小值）
        PriorityQueue<Integer> queue;

        // 堆大小
        int k;

        public KthLargest(int k, int[] nums) {
            queue = new PriorityQueue<>();
            this.k = k;
            // 将nums元素依次加入堆中
            for (int num: nums) {
                queue.offer(num);
                // 维护堆大小不超过k，超过k时，堆顶（比k个最大值更小的元素）会被移走
                // 移除后，堆顶就是第k大元素
                if (queue.size() > k) {
                    queue.remove();
                }
            }
        }

        public int add(int val) {
            // 如果 堆空 或者 元素个数小于 k，可以直接加入，返回堆顶即可
            if (queue.isEmpty() || queue.size() < k) {
                queue.offer(val);
                return queue.peek();
            }
            // 堆中已有k个元素，如果val比堆顶（最小值）更小，那没有贡献，不用加入
            // 如果val比堆顶（最小值）更大，那移除此时的堆顶，加入val
            if (val > queue.peek()) {
                queue.remove();
                queue.offer(val);
            }
            // 返回堆顶即可
            return queue.peek();
            /*     // 简略版本
            queue.offer(val);
            if (queue.size() > k) {
                queue.remove();
            }
            return queue.peek();*/
        }
    }
}
