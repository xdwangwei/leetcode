package com.offerassult;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author wangwei
 * @date 2022/11/8 10:37
 * @description: _041_AverageInSlidingWindow
 *
 * 剑指 Offer II 041. 滑动窗口的平均值
 * 给定一个整数数据流和一个窗口大小，根据该滑动窗口的大小，计算滑动窗口里所有数字的平均值。
 *
 * 实现 MovingAverage 类：
 *
 * MovingAverage(int size) 用窗口大小 size 初始化对象。
 * double next(int val) 成员函数 next 每次调用的时候都会往滑动窗口增加一个整数，请计算并返回数据流中最后 size 个值的移动平均值，即滑动窗口里所有数字的平均值。
 *
 *
 * 示例：
 *
 * 输入：
 * inputs = ["MovingAverage", "next", "next", "next", "next"]
 * inputs = [[3], [1], [10], [3], [5]]
 * 输出：
 * [null, 1.0, 5.5, 4.66667, 6.0]
 *
 * 解释：
 * MovingAverage movingAverage = new MovingAverage(3);
 * movingAverage.next(1); // 返回 1.0 = 1 / 1
 * movingAverage.next(10); // 返回 5.5 = (1 + 10) / 2
 * movingAverage.next(3); // 返回 4.66667 = (1 + 10 + 3) / 3
 * movingAverage.next(5); // 返回 6.0 = (10 + 3 + 5) / 3
 *
 *
 * 提示：
 *
 * 1 <= size <= 1000
 * -105 <= val <= 105
 * 最多调用 next 方法 104 次
 *
 *
 * 注意：本题与主站 346 题相同： https://leetcode-cn.com/problems/moving-average-from-data-stream/
 */
public class _041_AverageInSlidingWindow {


    /**
     * 简单模拟
     *
     * 滑动窗口的大小为给定的参数 size。
     * 当数据流中的数字个数不超过滑动窗口的大小时，计算数据流中的所有数字的平均值；
     * 当数据流中的数字个数超过滑动窗口的大小时，只计算滑动窗口中的数字的平均值，数据流中更早的数字被移出滑动窗口。
     *
     * 由于数字进入滑动窗口和移出滑动窗口的规则符合先进先出，因此可以使用【队列】存储滑动窗口中的数字
     * 同时维护滑动窗口的大小以及滑动窗口的数字之和。
     *
     * 初始时，队列为空，滑动窗口的大小设为给定的参数 size，滑动窗口的数字之和为 0。
     *
     * 每次调用 next 时，需要将 val 添加到滑动窗口中，并加到滑动窗口的数字之和中，同时确保滑动窗口中的数字个数不超过 size，
     * 如果数字个数超过 size 则需要将多余的数字（【队首】）移除，将移除的数字从滑动窗口的数字之和中减去。
     * 由于每次调用只会将一个数字添加到滑动窗口中，因此每次调用最多只需要将一个多余的数字移除。具体操作如下。
     *
     * 计算滑动窗口的数字之和与队列中的数字个数之商，即为滑动窗口中所有数字的平均值。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/qIsx9U/solution/hua-dong-chuang-kou-de-ping-jun-zhi-by-l-0rxf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class MovingAverage {

        private Deque<Integer> queue;

        // 队列元素个数上限
        private int size;

        // 队列中元素和
        private double sum;

        /** Initialize your data structure here. */
        public MovingAverage(int size) {
            queue = new LinkedList<>();
            this.size = size;
            sum = 0.0;
        }

        public double next(int val) {
            // 当前元素入队列，更新元素和
            sum += val;
            queue.offer(val);
            // 如果元素个数超过size
            if (queue.size() > size) {
                // 移除队首，更新sum
                sum -= queue.poll();
            }
            // 平均值 = sum / 元素个数
            return sum / queue.size();
        }
    }
}
