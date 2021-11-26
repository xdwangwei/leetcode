package com.design;

import java.util.PriorityQueue;

/**
 * @author wangwei
 * 2021/11/25 14:40
 *
 * 中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。
 *
 * 例如，
 *
 * [2,3,4]的中位数是 3
 *
 * [2,3] 的中位数是 (2 + 3) / 2 = 2.5
 *
 * 设计一个支持以下两种操作的数据结构：
 *
 * void addNum(int num) - 从数据流中添加一个整数到数据结构中。
 * double findMedian() - 返回目前所有元素的中位数。
 * 示例：
 *
 * addNum(1)
 * addNum(2)
 * findMedian() -> 1.5
 * addNum(3)
 * findMedian() -> 2
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-median-from-data-stream
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _295_FindMedianFromDataSteam {

    /**
     * 中位数：左边的数字都比它小，右边的数字都比它大
     * 那如果分开来看的话，左边可以维护一个大顶堆，右边可以维护一个小顶堆。并且大顶堆的堆顶元素 <= 小顶堆的堆顶元素
     * 并且因为是中位数分割，两个堆的元素个数只差最大为1，要么为0
     * 当插入一个数字时，
     *                1. 如果 左边堆空，那就放入左边堆
     *                2. 如果 左堆非空：
     *                         2.1 右堆空：能直接放入右边堆吗？ 不能！！
     *                         2.2 右堆非空：理论上为了维护两个堆的大小，应该选择插入元素少的那个堆，但能直接插入吗？？不能！！！
     *   对于2.1为什么不能直接插入：因为要保证左边堆的最大值(堆顶) 小于等于 右边堆的最小值(堆顶)
     *                          直接插入无法保证，但是可以这样：
     *                              把这个值插入左边堆，堆自己维护后，然后把左边堆堆顶拿走，放入右边堆，这就完美了！！
     *   对于2.2也是同样的道理：
     *                 如果 左堆元素个数 < 右堆，应该放入左堆，但是不确定这个元素的大小，
     *                          所以可以先放入右边堆，然后把右边堆的堆顶(最小值)拿走，放入左边堆，右边堆元素都比左边大
     *                 如果 左堆元素个数 > 右堆，应该放入右堆，但是不确定这个元素的大小
     *                          所以先放入左堆，再把左堆堆顶元素拿走，放入右堆
     *
     *   至于求中位数：
     *          如果左堆元素个数 > 右堆元素个数，返回左堆堆顶
     *          否则就是   两个堆顶元素的平均值
     *
     */
    class MedianFinder {

        // 左边堆维护最大堆
        PriorityQueue<Integer> leftHeap;
        // 右边堆维护最小堆
        PriorityQueue<Integer> rightHeap;

        public MedianFinder() {
            leftHeap = new PriorityQueue<>((o1, o2) -> o2 - o1);
            rightHeap = new PriorityQueue<>();
        }

        public void addNum(int num) {
            // 左堆空
            if (leftHeap.isEmpty()) {
                leftHeap.offer(num);
                return;
            }
            // 左堆不空, 维护堆中元素个数
            if (leftHeap.size() > rightHeap.size()) {
                // 应该放入右边堆，为了维护有序，先放入左堆，将堆顶取走，放入右堆
                leftHeap.offer(num);
                rightHeap.offer(leftHeap.poll());
            } else {
                // 应该放入 左堆，为了有序，先放入右堆，将堆顶取走，放入左堆
                rightHeap.offer(num);
                leftHeap.offer(rightHeap.poll());
            }
        }

        public double findMedian() {
            Integer leftMax = leftHeap.poll();
            if (leftHeap.size() > rightHeap.size()) {
                return leftMax;
            }
            return (leftMax + rightHeap.poll()) / 2.0;
        }
    }
}
