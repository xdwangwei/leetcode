package com.daily;

import java.util.PriorityQueue;

/**
 * @author wangwei
 * @date 2023/1/2 19:37
 * @description: _1801_NumberOfOrdersInTheBacklog
 *
 * 1801. 积压订单中的订单总数
 * 给你一个二维整数数组 orders ，其中每个 orders[i] = [pricei, amounti, orderTypei] 表示有 amounti 笔类型为 orderTypei 、价格为 pricei 的订单。
 *
 * 订单类型 orderTypei 可以分为两种：
 *
 * 0 表示这是一批采购订单 buy
 * 1 表示这是一批销售订单 sell
 * 注意，orders[i] 表示一批共计 amounti 笔的独立订单，这些订单的价格和类型相同。对于所有有效的 i ，由 orders[i] 表示的所有订单提交时间均早于 orders[i+1] 表示的所有订单。
 *
 * 存在由未执行订单组成的 积压订单 。积压订单最初是空的。提交订单时，会发生以下情况：
 *
 * 如果该订单是一笔采购订单 buy ，则可以查看积压订单中价格 最低 的销售订单 sell 。如果该销售订单 sell 的价格 低于或等于 当前采购订单 buy 的价格，则匹配并执行这两笔订单，并将销售订单 sell 从积压订单中删除。否则，采购订单 buy 将会添加到积压订单中。
 * 反之亦然，如果该订单是一笔销售订单 sell ，则可以查看积压订单中价格 最高 的采购订单 buy 。如果该采购订单 buy 的价格 高于或等于 当前销售订单 sell 的价格，则匹配并执行这两笔订单，并将采购订单 buy 从积压订单中删除。否则，销售订单 sell 将会添加到积压订单中。
 * 输入所有订单后，返回积压订单中的 订单总数 。由于数字可能很大，所以需要返回对 109 + 7 取余的结果。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：orders = [[10,5,0],[15,2,1],[25,1,1],[30,4,0]]
 * 输出：6
 * 解释：输入订单后会发生下述情况：
 * - 提交 5 笔采购订单，价格为 10 。没有销售订单，所以这 5 笔订单添加到积压订单中。
 * - 提交 2 笔销售订单，价格为 15 。没有采购订单的价格大于或等于 15 ，所以这 2 笔订单添加到积压订单中。
 * - 提交 1 笔销售订单，价格为 25 。没有采购订单的价格大于或等于 25 ，所以这 1 笔订单添加到积压订单中。
 * - 提交 4 笔采购订单，价格为 30 。前 2 笔采购订单与价格最低（价格为 15）的 2 笔销售订单匹配，从积压订单中删除这 2 笔销售订单。第 3 笔采购订单与价格最低的 1 笔销售订单匹配，销售订单价格为 25 ，从积压订单中删除这 1 笔销售订单。积压订单中不存在更多销售订单，所以第 4 笔采购订单需要添加到积压订单中。
 * 最终，积压订单中有 5 笔价格为 10 的采购订单，和 1 笔价格为 30 的采购订单。所以积压订单中的订单总数为 6 。
 * 示例 2：
 *
 *
 * 输入：orders = [[7,1000000000,1],[15,3,0],[5,999999995,0],[5,1,1]]
 * 输出：999999984
 * 解释：输入订单后会发生下述情况：
 * - 提交 109 笔销售订单，价格为 7 。没有采购订单，所以这 109 笔订单添加到积压订单中。
 * - 提交 3 笔采购订单，价格为 15 。这些采购订单与价格最低（价格为 7 ）的 3 笔销售订单匹配，从积压订单中删除这 3 笔销售订单。
 * - 提交 999999995 笔采购订单，价格为 5 。销售订单的最低价为 7 ，所以这 999999995 笔订单添加到积压订单中。
 * - 提交 1 笔销售订单，价格为 5 。这笔销售订单与价格最高（价格为 5 ）的 1 笔采购订单匹配，从积压订单中删除这 1 笔采购订单。
 * 最终，积压订单中有 (1000000000-3) 笔价格为 7 的销售订单，和 (999999995-1) 笔价格为 5 的采购订单。所以积压订单中的订单总数为 1999999991 ，等于 999999984 % (109 + 7) 。
 *
 *
 * 提示：
 *
 * 1 <= orders.length <= 105
 * orders[i].length == 3
 * 1 <= pricei, amounti <= 109
 * orderTypei 为 0 或 1
 * 通过次数14,119提交次数28,164
 */
public class _1801_NumberOfOrdersInTheBacklog {

    /**
     * 方法一：优先队列模拟
     * 根据题意，需要遍历数组 orders 中的订单并依次处理。
     * 对于遍历到的每个订单，需要找到【类型相反】的积压订单，
     * 如果可以匹配则执行这两笔订单并将积压订单删除，否则将当前订单添加到积压订单中。
     *
     * 由于寻找已有的积压订单时，需要寻找【价格最高的采购订单】或者【价格最低的销售订单】，
     * 因此可以使用两个优先队列分别存储积压的采购订单和积压的销售订单，
     * 两个优先队列称为采购订单优先队列（大顶堆）和销售订单优先队列（小顶堆），分别满足队首元素是价格最高的采购订单和价格最低的销售订单。
     *
     * 遍历数组 orders，对于 order=[price,amount,orderType]，执行如下操作。
     *
     * 如果 orderType=0，则表示 amount 个价格为 price 的【采购】订单，需要将这些采购订单和积压的【销售】订单匹配并执行。
     *      当【销售】订单优先队列中堆顶元素【小于等于】 price 时，将当前采购订单和积压的销售订单匹配并执行（订单数量抵消），
     *      直到当前采购订单全部匹配执行、积压的销售订单全部匹配执行或者剩余积压的销售订单的价格都大于 price。
     *      如果还有剩余的当前采购订单尚未匹配执行，则将剩余的采购订单添加到采购订单优先队列中。
     *
     * 如果 orderType=1，则表示 amount 个价格为 price 的【销售】订单，需要将这些销售订单和积压的【采购】订单匹配并执行。
     *      当【采购】订单优先队列中堆顶元素【大于等于】 price 时，将当前销售订单和积压的采购订单匹配并执行（订单数量抵消），
     *      直到当前销售订单全部匹配执行、积压的采购订单全部匹配执行或者剩余积压的采购订单的价格都小于 price。
     *      如果还有剩余的当前销售订单尚未匹配执行，则将剩余的销售订单添加到销售订单优先队列中。
     *
     * 遍历数组 orders 之后，两个优先队列中剩余的元素表示输入所有订单之后的积压订单，计算两个优先队列中剩余的订单总数并返回
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/number-of-orders-in-the-backlog/solution/ji-ya-ding-dan-zhong-de-ding-dan-zong-sh-6g22/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param orders
     * @return
     */
    public int getNumberOfBacklogOrders(int[][] orders) {
        // 挤压的销售订单队列，小顶堆，堆顶元素表示价格最低的订单价格及数量
        PriorityQueue<int[]> sellOrders = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);
        // 挤压的采购订单队列，大顶堆，堆顶元素表示价格最高的订单价格及数量
        PriorityQueue<int[]> buyOrders = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);
        // 遍历所有订单
        for (int[] order : orders) {
            int price = order[0], amount = order[1], type = order[2];
            // 当前订单为采购订单
            if (type == 0) {
                // 若积压的销售订单中价格 最低 的销售订单的价格 低于或等于 当前采购订单 buy 的价格，则匹配并执行这两笔订单，
                // 用销售订单抵消当前采购订单，直到amount为0，或没有销售订单满足 价格 小于等于当前订单
                while (!sellOrders.isEmpty() && amount > 0 && sellOrders.peek()[0] <= price) {
                    // 堆顶销售订单数量不够
                    if (sellOrders.peek()[1] <= amount) {
                        // 完全移除堆顶的批销售订单
                        int[] poll = sellOrders.poll();
                        // 更新amount
                        amount -= poll[1];
                    // 堆顶销售订单完全可以抵消掉amount
                    } else {
                        // 不必移除，更新此订单的数量即可
                        sellOrders.peek()[1] -= amount;
                        // 更新amount为0，下一轮循环直接退出
                        amount = 0;
                    }
                }
                // 说明没有足够的价格小于等于当前价格的销售订单
                if (amount > 0) {
                    // 剩余订单，压入采购订单队列
                    buyOrders.offer(new int[]{price, amount});
                }
            // 当前订单为销售订单
            } else {
                // 若积压的采购订单中价格 最低 的采购订单的价格 高于或等于 当前销售订单 buy 的价格，则匹配并执行这两笔订单，
                // 用采购订单抵消当前销售订单，直到amount为0，或没有采购订单满足 价格 大于等于当前订单
                while (!buyOrders.isEmpty() && amount > 0 && buyOrders.peek()[0] >= price) {
                    // 堆顶采购订单数量不够
                    if (buyOrders.peek()[1] <= amount) {
                        // 完全移除堆顶的批采购订单
                        int[] poll = buyOrders.poll();
                        // 更新amount
                        amount -= poll[1];
                    // 堆顶采购订单完全可以抵消掉amount
                    } else {
                        // 不必移除，更新此订单的数量即可
                        buyOrders.peek()[1] -= amount;
                        // 更新amount为0，下一轮循环直接退出
                        amount = 0;
                    }
                }
                // 说明没有足够的价格小于等于当前价格的采购订单
                if (amount > 0) {
                    // 剩余订单，压入销售订单队列
                    sellOrders.offer(new int[]{price, amount});
                }
            }
        }
        // 统计剩余的积压订单总量
        long ans = 0;
        while (!buyOrders.isEmpty()) {
            ans += buyOrders.poll()[1];
        }
        while (!sellOrders.isEmpty()) {
            ans += sellOrders.poll()[1];
        }
        // 返回结果对 10^9+7 取模
        final int mod = (int) (1e9 + 7);
        return (int) (ans % mod);
    }

    public static void main(String[] args) {
        _1801_NumberOfOrdersInTheBacklog obj = new _1801_NumberOfOrdersInTheBacklog();
        obj.getNumberOfBacklogOrders(new int[][]{{10,5,0},{15,2,1},{25,1,1},{30,4,0}});
    }
}
