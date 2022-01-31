package com.greed;

import java.util.Arrays;

/**
 * @author wangwei
 * 2022/1/1 22:04
 *
 * 给你输入若干形如 [begin, end] 的区间，代表若干会议的开始时间和结束时间，请你计算至少需要申请多少间会议室。
 *
 * 比如给你输入 meetings = [[0,30],[5,10],[15,20]]，算法应该返回 2，因为后两个会议和第一个会议时间是冲突的，至少申请两个会议室才能让所有会议顺利进行。
 */
public class _253_MeetingRooms {

    Integer defaul;

    protected Integer protec;

    /**
     * 如果会议之间的时间有重叠，那就得额外申请会议室来开会，想求至少需要多少间会议室，就是让你计算同一时刻最多有多少会议在同时进行。
     *
     * 换句话说，如果把每个会议的起始时间看做一个线段区间，那么题目就是让你求最多有几个[重叠区间]，仅此而已。
     *
     * 对于这种时间安排的问题，本质上讲就是区间调度问题，十有八九得【排序】，然后找规律来解决。
     *
     * 重复一下题目的本质：
     *
     * 给你输入若干时间区间，让你计算 【 同一时刻「最多」有几个区间重叠 】。
     *
     * 题目的关键点在于，给你任意一个时刻，你是否能够说出这个时刻有几个会议？
     *
     * 如果可以做到，那我遍历所有的时刻，找个最大值，就是需要申请的会议室数量。
     *
     * 有没有一种数据结构或者算法，给我输入若干区间，我能知道每个位置有多少个区间重叠？
     *
     * 老读者肯定可以联想到之前说过的一个算法技巧： 【差分数组】技巧。
     *
     * 把时间线想象成一个初始值为 0 的数组，每个时间区间 [i, j] 就相当于一个子数组，这个时间区间有一个会议，那我就把这个子数组中的元素都加一。
     *
     * 最后，每个时刻有几个会议我不就知道了吗？我遍历整个数组，不就知道至少需要几间会议室了吗？
     *
     * 举例来说，如果输入 meetings = [[0,30],[5,10],[15,20]]，那么我们就给数组中 [0,30],[5,10],[15,20] 这几个索引区间分别加一，最后遍历数组，求个最大值就行了。
     *
     * 还记得吗，差分数组技巧可以在 O(1) 时间对整个区间的元素进行加减，所以可以拿来解决这道题。
     *
     * 不过，这个解法的效率不算高，所以我这里不准备具体写差分数组的解法，参照 差分数组技巧 的原理，有兴趣的读者可以自己尝试去实现。
     *
     * 基于差分数组的思路，我们可以推导出一种更高效，更优雅的解法。
     *
     * 我们首先把这些会议的时间区间进行投影到时间线上：
     *
     * 红色的点代表每个会议的开始时间点，绿色的点代表每个会议的结束时间点。
     *
     * 现在假想有一条带着计数器的线，在时间线上从左至右进行扫描，每遇到红色的点，计数器 count 加一，每遇到绿色的点，计数器 count 减一：
     *
     * 这样一来，每个时刻有多少个会议在同时进行，就是计数器 count 的值，count 的最大值，就是需要申请的会议室数量。
     *
     * 对差分数组技巧熟悉的读者一眼就能看出来了，这个扫描线其实就是差分数组的遍历过程，所以我们说这是差分数组技巧衍生出来的解法。
     *
     * @param meetings
     * @return
     */
    int minMeetingRooms(int[][] meetings) {
        int n = meetings.length;
        // 因为不需要考虑当前时哪个会议，只需要知道当前时刻是否有会议就行，所以直接统计所有会议开始时刻和结束时刻
        // 所有会议的开始时间
        int[] begin = new int[n];
        // 所有会议的结束时间
        int[] end = new int[n];
        for(int i = 0; i < n; i++) {
            begin[i] = meetings[i][0];
            end[i] = meetings[i][1];
        }
        // 将开始时刻和结束时刻投影到时间线上
        Arrays.sort(begin);
        Arrays.sort(end);

        // 扫描过程中的计数器
        int count = 0;
        // 双指针技巧
        int res = 0, i = 0, j = 0;
        while (i < n && j < n) {
            // 最早的会议的开始时间 比 最早的会议的结束时间 早，也就是我开始时上个会议还没结束，那么肯定需要增加一个会议室
            if (begin[i] < end[j]) {
                // 扫描到一个红点，
                count++;
                i++;
            // begin[i] > end[j] 我开始时上个会议已经结束了，那么就可以共用一个会议室
            } else {
                // 扫描到一个绿点
                count--;
                j++;
            }
            // 记录扫描过程中 会议室数量 的最大值
            res = Math.max(res, count);
        }
        return res;
    }

}
