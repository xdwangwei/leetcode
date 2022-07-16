package com.daily;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author wangwei
 * @date 2022/5/6 22:12
 * @description: _933_NumberOfRecentCalls
 *
 * 933. 最近的请求次数
 * 写一个 RecentCounter 类来计算特定时间范围内最近的请求。
 *
 * 请你实现 RecentCounter 类：
 *
 * RecentCounter() 初始化计数器，请求数为 0 。
 * int ping(int t) 在时间 t 添加一个新请求，其中 t 表示以毫秒为单位的某个时间，并返回过去 3000 毫秒内发生的所有请求数（包括新请求）。确切地说，返回在 [t-3000, t] 内发生的请求数。
 * 保证 每次对 ping 的调用都使用比之前更大的 t 值。
 *
 *
 *
 * 示例 1：
 *
 * 输入：
 * ["RecentCounter", "ping", "ping", "ping", "ping"]
 * [[], [1], [100], [3001], [3002]]
 * 输出：
 * [null, 1, 2, 3, 3]
 *
 * 解释：
 * RecentCounter recentCounter = new RecentCounter();
 * recentCounter.ping(1);     // requests = [1]，范围是 [-2999,1]，返回 1
 * recentCounter.ping(100);   // requests = [1, 100]，范围是 [-2900,100]，返回 2
 * recentCounter.ping(3001);  // requests = [1, 100, 3001]，范围是 [1,3001]，返回 3
 * recentCounter.ping(3002);  // requests = [1, 100, 3001, 3002]，范围是 [2,3002]，返回 3
 *
 *
 * 提示：
 *
 * 1 <= t <= 109
 * 保证每次对 ping 调用所使用的 t 值都 严格递增
 * 至多调用 ping 方法 104 次
 */
public class _933_NumberOfRecentCalls {

    /**
     * 方法一：队列
     *
     * 我们可以用一个队列维护发生请求的时刻，
     *
     * 当在时间 t 收到请求时，需要返回的 [t-3000, t] 时间范围内的请求次数
     *
     * 所以 先将时间 t 入队。然后将队列中 < t - 3000 的元素全部移除，之后剩下的 队列元素个数，就是要返回的结果
     *
     * 由于每次收到的请求的时间都比之前的大，所以对于下一个 个更大的tt来说， [tt-3000, tt] 我们刚才移除的那些，肯定不会影响这个结果
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/number-of-recent-calls/solution/zui-jin-de-qing-qiu-ci-shu-by-leetcode-s-ncm1/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class RecentCounter {

        Queue<Integer> queue;

        public RecentCounter() {
            queue = new ArrayDeque<>();
        }

        /**
         * 返回 [t-3000, t] 时间内的请求数字
         * @param t
         * @return
         */
        public int ping(int t) {
            // 先将此时刻入队列
            queue.offer(t);
            // 移除掉 t-3000 以前的元素
            while (queue.peek() < t - 3000) {
                queue.poll();
            }
            // 剩下队列元素个数即为所求
            return queue.size();
        }
    }
}
