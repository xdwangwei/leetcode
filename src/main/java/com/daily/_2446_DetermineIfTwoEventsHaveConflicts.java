package com.daily;

/**
 * @author wangwei
 * @date 2023/5/18 11:18
 * @description: _2446_DetermineIfTwoEventsHaveConflicts
 *
 * 2446. 判断两个事件是否存在冲突
 * 给你两个字符串数组 event1 和 event2 ，表示发生在同一天的两个闭区间时间段事件，其中：
 *
 * event1 = [startTime1, endTime1] 且
 * event2 = [startTime2, endTime2]
 * 事件的时间为有效的 24 小时制且按 HH:MM 格式给出。
 *
 * 当两个事件存在某个非空的交集时（即，某些时刻是两个事件都包含的），则认为出现 冲突 。
 *
 * 如果两个事件之间存在冲突，返回 true ；否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：event1 = ["01:15","02:00"], event2 = ["02:00","03:00"]
 * 输出：true
 * 解释：两个事件在 2:00 出现交集。
 * 示例 2：
 *
 * 输入：event1 = ["01:00","02:00"], event2 = ["01:20","03:00"]
 * 输出：true
 * 解释：两个事件的交集从 01:20 开始，到 02:00 结束。
 * 示例 3：
 *
 * 输入：event1 = ["10:00","11:00"], event2 = ["14:00","15:00"]
 * 输出：false
 * 解释：两个事件不存在交集。
 *
 *
 * 提示：
 *
 * evnet1.length == event2.length == 2.
 * event1[i].length == event2[i].length == 5
 * startTime1 <= endTime1
 * startTime2 <= endTime2
 * 所有事件的时间都按照 HH:MM 格式给出
 * 通过次数28,556提交次数44,840
 */
public class _2446_DetermineIfTwoEventsHaveConflicts {

    /**
     * 方法一：将所有时间字符串转为当日的分钟数，"xx:yy" = int(xx) * 60 + int(yy)
     * 得到两个事件的起始结束时间：s1、e1，s2、e2
     * 判断两个区间是否有交集：!(s1 > e2 || s2 > e1)
     * @param event1
     * @param event2
     * @return
     */
    public boolean haveConflict(String[] event1, String[] event2) {
        // 得到两个事件的起始、结束时间
        int s1 = getMinute(event1[0]), e1 = getMinute(event1[1]);
        int s2 = getMinute(event2[0]), e2 = getMinute(event2[1]);
        // 判断两个区间有交集
        return !(s1 > e2 || s2 > e1);
    }

    /**
     * 方法二：由于 每个时间字符串为有效的 24 小时制且按 HH:MM 格式给出。
     * 那么 字典序 也和 数值序一直
     * 因此可以直接比较字符串，不用转int
     * @param event1
     * @param event2
     * @return
     */
    public boolean haveConflict2(String[] event1, String[] event2) {
        // 得到两个事件的起始、结束时间
        // 判断两个区间有交集
        return !(event1[1].compareTo(event2[0]) < 0 || event2[1].compareTo(event1[0]) < 0);
    }

    /**
     * 时间字符串转为当日的分钟数，"xx:yy" = int(xx) * 60 + int(yy)
     * @param timeStr
     * @return
     */
    private int getMinute(String timeStr) {
        String[] info = timeStr.split(":");
        return Integer.parseInt(info[0]) * 60 + Integer.parseInt(info[1]);
    }
}
