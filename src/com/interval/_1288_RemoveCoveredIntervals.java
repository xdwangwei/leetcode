package com.interval;

import java.util.Arrays;

/**
 * @author wangwei
 * 2021/3/28 13:48
 *
 * 给你一个区间列表，请你删除列表中被其他区间所覆盖的区间。
 *
 * 只有当 c <= a 且 b <= d 时，我们才认为区间 [a,b) 被区间 [c,d) 覆盖。
 *
 * 在完成所有删除操作后，请你返回列表中剩余区间的数目。
 *
 *  
 *
 * 示例：
 *
 * 输入：intervals = [[1,4],[3,6],[2,8]]
 * 输出：2
 * 解释：区间 [3,6] 被区间 [2,8] 覆盖，所以它被删除了。
 *  
 *
 * 提示：​​​​​​
 *
 * 1 <= intervals.length <= 1000
 * 0 <= intervals[i][0] < intervals[i][1] <= 10^5
 * 对于所有的 i != j：intervals[i] != intervals[j]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/remove-covered-intervals
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1288_RemoveCoveredIntervals {

    /**
     * 一个区间被另一个区间覆盖，如果我们只按照起点排序，则我们就不知道是哪个区间覆盖哪个区间，因此，我们还需要对终点进行排序。
     *
     * 如果两个区间起点相同，则将终点较大的放在前面。
     * [2,8]放在[2,6]前面，这样，我们按顺序扫描。遇到[2,6]的时候能很快知道它被覆盖了
     *
     * 如果当前区间不被前一个区间覆盖 end > prev_end，则增加 count，指定当前区间为下一步的前一个区间。
     * 否则，当前区间被前一个区间覆盖，不做任何事情。
     *
     * @param intervals
     * @return
     */
    public int removeCoveredIntervals(int[][] intervals) {
        // 起点升序，起点相同终点降序
        Arrays.sort(intervals, (o1, o2) -> o1[0] == o2[0] ? o2[1] - o1[1] : o1[0] - o2[0]);
        // 按顺序扫描
        int count = 0, prevRight = 0, right = 0;
        for (int[] interval : intervals) {
            // 当前区间右端点
            right = interval[1];
            // 当前区间不被上一个覆盖
            if (right > prevRight) {
                count++;
                // 当前区间作为下一轮比较的前一个区间
                prevRight = right;
            }
        }
        return count;
    }
}
