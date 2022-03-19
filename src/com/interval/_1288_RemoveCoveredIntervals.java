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

    /**
     * 第二种写法
     * 起点升序，起点相同时终点降序，这样就满足 l[j] >= l[i] (j > i), 只需要判断 r[i] 和 r[j]
     * @param intervals
     * @return
     */
    public int removeCoveredIntervals2(int[][] intervals) {
        // 起点升序，起点相同终点降序
        Arrays.sort(intervals, (o1, o2) -> o1[0] == o2[0] ? o2[1] - o1[1] : o1[0] - o2[0]);
        int n = intervals.length;
        int ans = n;
        // 右边界最大值
        int maxRight = intervals[0][1];
        for (int i = 1; i < n; i++) {
            // 当前区间右边界
            int curRight = intervals[i][1];
            // 当前区间被覆盖
            if (curRight <= maxRight) {
                ans--;
            } else {
                // 相交，更新右边界
                maxRight = curRight;
            }
        }
        return ans;
    }

    /**
     * 第三种，起点升序，起点相同时，终点也升序
     * 这种情况下就会出现 intv[i] 覆盖 intv[i - 1] 或者 intv[i] 覆盖 intv[i + 1]两种覆盖形式，主要是区分这两种覆盖和相交情况
     *
     * 可以先看下面那种错误写法
     *
     * 不太好描述，就是下面那种写法会在某个“巧合”下使得那个if成立，多加了一次count，原因是prevLeft一直在更新，和prevRight不同步，
     * 现在这样写是 让这两个 同步更新（看起来不同步，但是在prevRight变化的过程中，prevLeft是不变的，怎么说的，这只能说明这几个区间左端点相同，相当于pervleft也在更新），
     * @param intervals
     * @return
     */
    public int removeCoveredIntervals3(int[][] intervals) {
        // 起点升序，终点升序
        Arrays.sort(intervals, (o1, o2) -> o1[0] == o2[0] ? o1[1] - o2[1] : o1[0] - o2[0]);
        // 排序之后，区间起点满足递增关系
        int len = intervals.length;
        // 第一个区间
        int prevLeft = intervals[0][0], prevRight = intervals[0][1];
        // 被覆盖的区间数
        int count = 0;
        // 从第二个区间开始
        for (int i = 1; i < len; ++i) {
            int left = intervals[i][0];
            int right = intervals[i][1];
            // 当前区间被上一个区间【覆盖】 ①
            if (right <= prevRight) {
                count++;
            } else {
                // 当前区间终点比上一个大，区分到底是 相交 还是 覆盖
                // 如果起点相同，说明当前区间【覆盖】了上一个  ②
                if (left == prevLeft) {
                    count++;
                    // 更新右边界
                    prevRight = right;
                } else {
                    // 相交情况
                    // 更新左边界
                    prevLeft = left;
                    // 更新右边界
                    prevRight = right;
                }
            }
        }
        // 总区间数 - 被覆盖的区间数
        return len - count;
    }

    /**
     * 第三种，起点升序，起点相同时，终点也升序 ×××
     *
     * 【错误写法】，一直更新 prevLeft
     *
     * prevLeft一直在更新，也就是说，它的确保存的是前一个区间的左边界，但是prevRight相当于是之前所有区间的右边界中最大的那个，
     * 所以我那个if左边界相等count自增，这个判断成立的时候，并不一定是当前覆盖上一个，因为这个prevRight是个历史值
     *
     * 可以先看下面那种错误写法
     * @param intervals
     * @return
     */
    public int removeCoveredIntervals4(int[][] intervals) {
        // 起点升序，终点升序
        Arrays.sort(intervals, (o1, o2) -> o1[0] == o2[0] ? o1[1] - o2[1] : o1[0] - o2[0]);
        // 排序之后，区间起点满足递增关系
        int len = intervals.length;
        // 第一个区间
        int prevLeft = intervals[0][0], prevRight = intervals[0][1];
        // 被覆盖的区间数
        int count = 0;
        // 从第二个区间开始
        for (int i = 1; i < len; ++i) {
            int left = intervals[i][0];
            int right = intervals[i][1];
            // 当前区间被上一个区间【覆盖】
            if (right <= prevRight) {
                count++;
            } else {
                // 当前区间终点比上一个大
                // 如果起点相同，说明当前区间【覆盖】了上一个
                if (left == prevLeft) {
                    count++;
                }
                // 更新右边界
                prevRight = right;
            }
            // 更新左边界，所有左断点是递增，所以随便更新
            prevLeft = left;
        }
        // 总区间数 - 被覆盖的区间数
        return len - count;
    }
}
