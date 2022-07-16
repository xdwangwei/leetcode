package com.greed;

import java.util.Arrays;

/**
 * @author wangwei
 * 2020/4/20 23:22
 *
 * 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
 *
 * 注意:
 *
 * 可以认为区间的终点总是大于它的起点。
 * 区间 [1,2] 和 [2,3] 的边界相互“接触”，但没有相互重叠。
 * 示例 1:
 *
 * 输入: [ [1,2], [2,3], [3,4], [1,3] ]
 *
 * 输出: 1
 *
 * 解释: 移除 [1,3] 后，剩下的区间没有重叠。
 * 示例 2:
 *
 * 输入: [ [1,2], [1,2], [1,2] ]
 *
 * 输出: 2
 *
 * 解释: 你需要移除两个 [1,2] 来使剩下的区间没有重叠。
 * 示例 3:
 *
 * 输入: [ [1,2], [2,3] ]
 *
 * 输出: 0
 *
 * 解释: 你不需要移除任何区间，因为它们已经是无重叠的了。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/non-overlapping-intervals
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _435_NoOverlappingIntevals {

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
     * 如果一个区间不想与 x 的end相交，它的start必须要大于（或等于）x 的end，(题目，相等不算相交)
     *
     * 再看这个问题，不就是用区间总数减去最多的不重复的区间数吗？？？
     *
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return 0;
        // 一共n个区间，最多x个不重复，那至少需要需要 n - x 个区间，才能留下不重复的
        return intervals.length - noOverlapIntervals(intervals);
    }

    /**
     * 找出最多的不相交区间, [1,2] [2,3] 不算相交
     * @param intervals
     * @return
     */
    private int noOverlapIntervals(int[][] intervals){
        if (intervals == null || intervals.length == 0) return 0;
        // 按区间[start,end]结束点end排序，升序
        Arrays.sort(intervals, (o1, o2) -> o1[1] - o2[1]);
        // 初始区间
        int end = intervals[0][1];
        // 初始不重叠区间
        int count = 1;
        // 与之相交的区间，它的start必然会小于当前end
        for (int i = 1; i < intervals.length; i++){
            // 不相交的情况下
            if (intervals[i][0] >= end){
                // 更新end为当前区间的end，每一次都是一个局部最优
                end = intervals[i][1];
                // 不相交区间数加1
                count++;
            }
        }
        return count;
    }
}
