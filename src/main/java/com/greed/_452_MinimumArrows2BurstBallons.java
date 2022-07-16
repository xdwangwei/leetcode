package com.greed;

import java.util.Arrays;

/**
 * @author wangwei
 * 2020/4/20 23:38
 *
 * 在二维空间中有许多球形的气球。对于每个气球，提供的输入是水平方向上，气球直径的开始和结束坐标。
 * 由于它是水平的，所以y坐标并不重要，因此只要知道开始和结束的x坐标就足够了。
 * 开始坐标总是小于结束坐标。平面内最多存在104个气球。
 *
 * 一支弓箭可以沿着x轴从不同点完全垂直地射出。
 * 在坐标x处射出一支箭，若有一个气球的直径的开始和结束坐标为 xstart，xend， 且满足 xstart ≤ x ≤ xend，
 * 则该气球会被引爆。可以射出的弓箭的数量没有限制。弓箭一旦被射出之后，可以无限地前进。
 * 我们想找到使得所有气球全部被引爆，所需的弓箭的最小数量。
 *
 * Example:
 *
 * 输入:
 * [[10,16], [2,8], [1,6], [7,12]]
 *
 * 输出:
 * 2
 *
 * 解释:
 * 对于该样例，我们可以在x = 6（射爆[2,8],[1,6]两个气球）和 x = 11（射爆另外两个气球）。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/minimum-number-of-arrows-to-burst-balloons
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _452_MinimumArrows2BurstBallons {

    /**
     * 贪心
     * 什么是贪心选择性质呢，简单说就是：每一步都做出一个局部最优的选择，最终的结果就是全局最优。
     * 注意哦，这是一种特殊性质，其实只有一小部分问题拥有这个性质。
     * 有关贪心算法介绍：https://mp.weixin.qq.com/s/NH8GFMcRm5UK0HmVhdNjMQ
     *
     * 先来看一个经典例子
     * 给你很多形如[start,end]的闭区间，请你设计一个算法，算出这些区间中最多有几个互不相交的区间。
     *
     * 正确的思路其实很简单，可以分为以下三步：
     *
     * 从区间集合 intvs 中选择一个区间 x，这个 x 是在当前所有区间中结束最早的（end 最小）。
     * 把所有与 x 区间相交的区间从区间集合 intvs 中删除。
     * 重复步骤 1 和 2，直到 intvs 为空为止。之前选出的那些 x 就是最大不相交子集。
     *
     * 把这个思路实现成算法的话，可以按每个区间的end数值升序排序，因为这样处理之后实现步骤 1 和步骤 2 都方便很多:
     *
     * 由于我们事先排了序，不难发现所有与 x 相交的区间必然会与 x 的end相交；
     * 如果一个区间不想与 x 的end相交，它的start必须要大于（或等于）x 的end，
     *
     * 再看这个问题，不就是求最多的不重复的区间数吗？？？，
     * 无非就是在这里，边界算相交(射箭嘛，一碰上，肯定两个都破了)
     *
     */

    public int findMinArrowShots(int[][] points) {
        // 有几个不相交区间，就需要几支箭
        return noOverlapIntervals(points);
    }

    /**
     * 求最多的不重复的区间数
     * @param intervals
     * @return
     */
    private int noOverlapIntervals(int[][] intervals){
        if (intervals == null || intervals.length == 0)
            return 0;
        // 将所有区间按区间结束点排序，升序
        Arrays.sort(intervals, (o1, o2) -> o1[1] - o2[1]);
        // 第一个区间作为初始区间
        int end = intervals[0][1];
        // 不重复的区间个数，至少一个
        int count = 1;
        // 遍历之后的区间
        for (int i = 1; i < intervals.length; i++) {
            // 除非它的start大于当前的end(题意，相等算相交),它两才不相交
            if (intervals[i][0] > end){
                // 这是一个局部最优解,更新区间边界
                end = intervals[i][1];
                count++;
            }
        }
        return count;
    }
}
