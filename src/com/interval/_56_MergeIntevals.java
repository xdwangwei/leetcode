package com.interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author wangwei
 * 2020/8/28 19:41
 * 给出一个区间的集合，请合并所有重叠的区间。
 *
 * 示例 1:
 *
 * 输入: intervals = [[1,3],[2,6],[8,10],[15,18]]
 * 输出: [[1,6],[8,10],[15,18]]
 * 解释: 区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
 * 示例2:
 *
 * 输入: intervals = [[1,4],[4,5]]
 * 输出: [[1,5]]
 * 解释: 区间 [1,4] 和 [4,5] 可被视为重叠区间。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/merge-intervals
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 */
public class _56_MergeIntevals {

    /**
     * 区间问题，排序排序排序
     * @param intervals
     * @return
     */
    public int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length < 2) return intervals;
        // 选择按区间的 start 排序
        // 对于几个相交区间合并后的结果区间 x，x.start 一定是这些相交区间中 start 最小的，
        // x.end 一定是这些相交区间中 end 最大的。
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
        // 最坏情况区间全部不想交，也就是intervals.length个区间
        int[][] res = new int[intervals.length][2];
        // 第一个区间
        res[0] = intervals[0];
        // 合并后不一定有intervals.length个区间，所以需要一个指针指示res前几个是有效的
        // 同时也是res中的最后一个合并后的区间位置
        int index = 0;
        for (int i = 1; i < intervals.length; ++i) {
            // 它和前面那个区间不相交
            if (intervals[i][0] > res[index][1])
                // 它单独合并，index++
                res[++index] = intervals[i];
            // 它和前面那个区间相交，合并后的区间末尾一定是他们中最大的那个
            else
                res[index][1] = Math.max(res[index][1], intervals[i][1]);
        }
        // res中只有前index个区间有效
        return Arrays.copyOf(res, index + 1);
    }

    /**
     * 复习算法小抄
     * @param intervals
     * @return
     */
    public int[][] merge8(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
        List<int[]> res = new ArrayList<>();
        int n = intervals.length;
        res.add(intervals[0]);
        for (int i = 1; i < n; ++i) {
            int prevMaxRight = res.get(res.size() - 1)[1];
            int curLeft = intervals[i][0];
            int curRight = intervals[i][1];
            // 相交，令加一个区间
            if (curLeft > prevMaxRight) {
                res.add(intervals[i]);
            // 覆盖情况，更新 res.getLast() 的有边界，相当于这些全部合并到 当前部分的第一个区间中去
            } else if (curRight > prevMaxRight) {
                res.get(res.size() - 1)[1] = curRight;
            }
        }
        return res.toArray(new int[res.size()][]);
    }
}
