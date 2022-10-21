package com.stack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/10/21 12:31
 * @description: _901_OnlineStockSpan
 *
 * 901. 股票价格跨度
 * 编写一个 StockSpanner 类，它收集某些股票的每日报价，并返回该股票当日价格的跨度。
 *
 * 今天股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
 *
 * 例如，如果未来7天股票的价格是 [100, 80, 60, 70, 60, 75, 85]，那么股票跨度将是 [1, 1, 1, 2, 1, 4, 6]。
 *
 *
 *
 * 示例：
 *
 * 输入：["StockSpanner","next","next","next","next","next","next","next"], [[],[100],[80],[60],[70],[60],[75],[85]]
 * 输出：[null,1,1,1,2,1,4,6]
 * 解释：
 * 首先，初始化 S = StockSpanner()，然后：
 * S.next(100) 被调用并返回 1，
 * S.next(80) 被调用并返回 1，
 * S.next(60) 被调用并返回 1，
 * S.next(70) 被调用并返回 2，
 * S.next(60) 被调用并返回 1，
 * S.next(75) 被调用并返回 4，
 * S.next(85) 被调用并返回 6。
 *
 * 注意 (例如) S.next(75) 返回 4，因为截至今天的最后 4 个价格
 * (包括今天的价格 75) 小于或等于今天的价格。
 *
 *
 * 提示：
 *
 * 调用 StockSpanner.next(int price) 时，将有 1 <= price <= 10^5。
 * 每个测试用例最多可以调用  10000 次 StockSpanner.next。
 * 在所有测试用例中，最多调用 150000 次 StockSpanner.next。
 * 此问题的总时间限制减少了 50%。
 */
public class _901_OnlineStockSpan {


    /**
     * 单调栈
     *
     * 方法一：单调栈
     *
     * 根据题目描述，我们可以知道，对于当日价格 price，从这个价格开始往前找，找到第一个比这个价格大的价格，这两个价格的下标差 cnt 就是当日价格的跨度。
     * 并且，所有数据并不是一次给出的，而是每调用一次next方法，给出一个price，并考虑之前的元素
     *
     * 这实际上是经典的单调栈模型（后退去找已经访问过的元素，栈模型）：对于当前元素，逐个往左遍历，直到遇到一个比当前元素大的元素。
     *
     * 维护栈中元素依次递增，那么对于当前元素cur，当栈顶元素小于等于它时，一直弹栈，直到栈顶元素prev大于自己，二者之间跨度就是本次要返回的答案，然后把cur放入栈顶
     *                   对于下一个元素next，如果它大于等于cur，那么处理cur的过程中弹出的那些东西，对于next来说也是要弹出的，它应该直接去判断prev，所以并没有影响答案
     *                   对于下一个元素next，如果它小于cur，那么cur已经挡住了所有前面的数据，处理cur的过程中弹出的那些东西，对于next来说已经没有意义，所以并没有影响答案
     *
     * 由于要计算跨度，所以还得标记下每个元素入栈时的序号，因此
     * 我们维护一个从栈底到栈顶价格单调递增的栈，栈中每个元素存放的是 (idx，price) 数据对，其中 price 表示价格，idx 表示当前价格的入栈序号。
     *
     * 每次调用 next(price)，我们就去回顾前面的数据，也就是逐个判断栈顶元素，
     *          如果栈顶元素的价格小于等于 price，那就弹栈
     *          否则，停止弹栈，当前元素与当前栈顶之间的跨度就是本次的返回值
     *          然后，将当前元素及当前idx入栈
     *
     * 为了避免第一次弹栈时栈空的情况，我们初始时给栈放入一个特别大的数字，相当于一个边界，这样，栈永远不空，就不用单独判断栈空情况
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/online-stock-span/solution/by-lcbin-cfcm/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class StockSpanner {

        // 单调递增栈
        private Deque<int[]> stack;
        // 当前元素入栈时的下标
        int idx = 0;

        public StockSpanner() {
            // 初始化
            stack = new ArrayDeque<>();
            // 边界点
            stack.push(new int[]{idx++, 1000000});
        }

        public int next(int price) {
            // 逐个弹出栈顶，直到栈顶>当前值
            while (stack.peek()[1] <= price) {
                stack.pop();
            }
            // 二者之间的跨度
            int cnt = idx - stack.peek()[0];
            // 放入当前元素及下标
            stack.push(new int[]{idx++, price});
            // 返回
            return cnt;
        }
    }
}
