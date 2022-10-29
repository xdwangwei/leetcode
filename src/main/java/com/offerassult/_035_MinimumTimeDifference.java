package com.offerassult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/10/29 10:28
 * @description: _035_MinimumTimeDifference
 *
 * 剑指 Offer II 035. 最小时间差
 * 给定一个 24 小时制（小时:分钟 "HH:MM"）的时间列表，找出列表中任意两个时间的最小时间差并以分钟数表示。
 *
 *
 *
 * 示例 1：
 *
 * 输入：timePoints = ["23:59","00:00"]
 * 输出：1
 * 示例 2：
 *
 * 输入：timePoints = ["00:00","23:59","00:00"]
 * 输出：0
 *
 *
 * 提示：
 *
 * 2 <= timePoints <= 2 * 104
 * timePoints[i] 格式为 "HH:MM"
 *
 *
 * 注意：本题与主站 539 题相同： https://leetcode-cn.com/problems/minimum-time-difference/
 */
public class _035_MinimumTimeDifference {


    /**
     * 方法一：排序
     * 将 timePoints 排序后，最小时间差必然出现在 timePoints 的两个相邻时间，或者 timePoints 的两个首尾时间中。
     * 因此排序后遍历一遍 timePoints 即可得到最小时间差。
     * 由于时钟的特殊性，相当于围成一圈，圈中所有相邻节点的差值最小值
     *
     * 并且，一天共有 24×60=1440 种不同的时间。由鸽巢原理可知，如果 timePoints 的长度超过 1440，那么必然会有两个相同的时间，此时可以直接返回 0。
     *
     * 排序后：
     *
     * 相邻两个时间的插值    a[1] - a[0]
     *
     * 首尾两个时间的插值    a[0] + 1440 - a[n - 1]，注意这里的计算方式 ！！！！
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/569nqc/solution/zui-xiao-shi-jian-chai-by-leetcode-solut-ue5t/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param timePoints
     * @return
     */
    public int findMinDifference(List<String> timePoints) {
        int n = timePoints.size();
        // 一天最多 1440 分钟
        if (n > 1440) {
            return 0;
        }
        // 直接按字符串排序
        Collections.sort(timePoints);
        // 初始化
        int ans = Integer.MAX_VALUE;
        int t0Minutes = getMinutes(timePoints.get(0));
        int preMinutes = t0Minutes;
        // 计算相邻时间的插值，计算首尾时间的插值
        for (int i = 1; i < n; ++i) {
            int minutes = getMinutes(timePoints.get(i));
            ans = Math.min(ans, minutes - preMinutes); // 相邻时间的时间差
            preMinutes = minutes;
        }
        // 计算首尾时间的插值，注意这里的计算方式！！！
        ans = Math.min(ans, t0Minutes + 1440 - preMinutes); // 首尾时间的时间差
        return ans;
    }

    /**
     * timePoints[i] 格式为 "HH:MM"，计算出整形值，单位 分钟
     * @param t
     * @return
     */
    public int getMinutes(String t) {
        return ((t.charAt(0) - '0') * 10 + (t.charAt(1) - '0')) * 60 + (t.charAt(3) - '0') * 10 + (t.charAt(4) - '0');
    }

    /**
     * 因为一天内最多，1440分钟，是有限整数，本身可以用来排序，因此
     *
     * 首先根据「抽屉原理」，若 timePoints 数量大于 1440，必然有两个相同时间点，直接返回0。
     *
     * 然后，使用数组充当哈希表进行计数，统计每个时刻出现的次数，如果某个时刻出现两次及以上，那么返回0，
     *
     * 需要注意跨天的情况，（比如 01:00 和 23:00，这个01:00既可以表示今天的1点，也可以认为是第二天的1:00），
     *
     * 因此，认为共有 1440 * 2880个时间点，也就是说数组大小是2880，并且这样的话因为已经考虑跨天情况，就不再需要单独计算首尾时刻时间差
     *
     * 然后就可以利用时刻作为数组下标本身有序的特性来找到间隔最小两个时间点，这种利用「桶排序」的思路避免了对 timePoints 所对应的偏移量进行排序，
     * 而 O(C) 的复杂度使得所能处理的数据范围没有上限。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/minimum-time-difference/solution/gong-shui-san-xie-jian-dan-mo-ni-ti-by-a-eygg/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param timePoints
     * @return
     */
    public int findMinDifference2(List<String> timePoints) {
        int n = timePoints.size();
        if (n > 1440) return 0;
        // 每个时刻出现的次数，为解决跨天情况，二倍大小
        int[] cnts = new int[1440 * 2 + 10];
        for (String s : timePoints) {
            String[] ss = s.split(":");
            int h = Integer.parseInt(ss[0]), m = Integer.parseInt(ss[1]);
            // 当天时刻
            cnts[h * 60 + m]++;
            // 作为跨天时刻
            cnts[h * 60 + m + 1440]++;
        }
        // 时刻作为数组元素下标本身已经有序，如果对应次数为0，则忽略，次数>1，则返回0，否则就是一个有效时刻，last记录上一个有效时刻
        int ans = 1440, last = -1;
        for (int i = 0; i <= 1440 * 2 && ans != 0; i++) {
            // 如果对应次数为0，则忽略
            if (cnts[i] == 0) continue;
            // 当前时刻出现多次，间隔为0
            if (cnts[i] > 1) ans = 0;
            // 当前时刻和上一个有效时刻的差距
            else if (last != -1) ans = Math.min(ans, i - last);
            // 更新last
            last = i;
        }
        // 因为已经考虑跨天情况，这里不再需要计算首尾时刻时间差
        return ans;
    }

    public static void main(String[] args) {
        _035_MinimumTimeDifference obj = new _035_MinimumTimeDifference();
        System.out.println(obj.findMinDifference(Arrays.asList("00:00","04:00","22:00")));
    }
}
