package com.interval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author wangwei
 * 2022/4/14 12:02
 * <p>
 * 57. 插入区间
 * 给你一个 无重叠的 ，按照区间起始端点排序的区间列表。
 * <p>
 * 在列表中插入一个新的区间，你需要确保列表中的区间仍然有序且不重叠（如果有必要的话，可以合并区间）。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * 输入：intervals = [[1,3],[6,9]], newInterval = [2,5]
 * 输出：[[1,5],[6,9]]
 * 示例 2：
 * <p>
 * 输入：intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
 * 输出：[[1,2],[3,10],[12,16]]
 * 解释：这是因为新的区间 [4,8] 与 [3,5],[6,7],[8,10] 重叠。
 * 示例 3：
 * <p>
 * 输入：intervals = [], newInterval = [5,7]
 * 输出：[[5,7]]
 * 示例 4：
 * <p>
 * 输入：intervals = [[1,5]], newInterval = [2,3]
 * 输出：[[1,5]]
 * 示例 5：
 * <p>
 * 输入：intervals = [[1,5]], newInterval = [2,7]
 * 输出：[[1,7]]
 * <p>
 * <p>
 * 提示：
 * <p>
 * 0 <= intervals.length <= 104
 * intervals[i].length == 2
 * 0 <= intervals[i][0] <= intervals[i][1] <= 105
 * intervals 根据 intervals[i][0] 按 升序 排列
 * newInterval.length == 2
 * 0 <= newInterval[0] <= newInterval[1] <= 105
 */
public class _57_InsertInterval {

    /**
     * 方法一：拿到所有区间，按照 去除重复区间的办法去做
     *
     * @param intervals
     * @param newInterval
     * @return
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        int n = intervals.length;
        if (n == 0) {
            return new int[][]{newInterval};
        }
        // 拿到所有区间
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            list.add(intervals[i]);
        }
        list.add(newInterval);
        // -----去除重复区间--------
        // 按照区间左端点升序排序
        list.sort(Comparator.comparingInt(a -> a[0]));
        List<int[]> res = new ArrayList<>();
        // 存在重叠区域的多个区间的 最小左端点 和 最大右端点
        int minStart = list.get(0)[0];
        int maxEnd = list.get(0)[1];
        for (int i = 1; i < list.size(); ++i) {
            int curLeft = list.get(i)[0];
            int curRight = list.get(i)[1];
            // 另起炉灶
            if (curLeft > maxEnd) {
                // 保存之前的合并结果
                res.add(new int[]{minStart, maxEnd});
                minStart = curLeft;
                maxEnd = curRight;
            } else {
                // 合并，
                maxEnd = Math.max(maxEnd, curRight);
            }
        }
        // for 循环结束后，保存
        res.add(new int[]{minStart, maxEnd});
        return res.toArray(new int[res.size()][2]);
    }

    /**
     * 方法一压根没用到题目给出的给定区间已有序这个条件，效率低下
     * 方法二：分三部分，第一部分：新区间左边的与新区间无重复的所有区间
     * 第二部分：与新区间有重叠的一个到多个区间
     * 第三部分：新区间右边的与新区间无重复的所有区间
     *
     * @param intervals
     * @param newInterval
     * @return
     */
    public int[][] insert2(int[][] intervals, int[] newInterval) {
        int n = intervals.length;
        if (n == 0) {
            return new int[][]{newInterval};
        }
        // 用户保存合并后的所有区间
        List<int[]> list = new ArrayList<>();
        int i = 0;
        // 第一部分：新区间左边的与新区间无重复的所有区间, intervals[i][1] < newinterval[0]
        // 相等也要合并，比如 [1,3] 和 [3, 4]
        for (; i < n; ++i) {
            if (intervals[i][1] < newInterval[0]) {
                list.add(intervals[i]);
            } else {
                break;
            }
        }
        // 第二部分：与新区间有重叠的一个到多个区间
        int left = newInterval[0], right = newInterval[1];
        while (i < n && intervals[i][0] <= newInterval[1]) {
            left = Math.min(left, intervals[i][0]);
            right = Math.max(right, intervals[i][1]);
            i++;
        }
        list.add(new int[]{left, right});
        // 第三部分：新区间右边的与新区间无重复的所有区间, intervals[i][0] > newinterval[1]
        while (i < n) {
            list.add(intervals[i++]);
        }
        return list.toArray(new int[list.size()][2]);
    }
}
