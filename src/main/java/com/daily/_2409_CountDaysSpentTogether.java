package com.daily;

/**
 * @author wangwei
 * @date 2023/4/17 21:03
 * @description: _2409_CountDaysSpentTogether
 *
 * 2409. 统计共同度过的日子数
 * Alice 和 Bob 计划分别去罗马开会。
 *
 * 给你四个字符串 arriveAlice ，leaveAlice ，arriveBob 和 leaveBob 。Alice 会在日期 arriveAlice 到 leaveAlice 之间在城市里（日期为闭区间），而 Bob 在日期 arriveBob 到 leaveBob 之间在城市里（日期为闭区间）。每个字符串都包含 5 个字符，格式为 "MM-DD" ，对应着一个日期的月和日。
 *
 * 请你返回 Alice和 Bob 同时在罗马的天数。
 *
 * 你可以假设所有日期都在 同一个 自然年，而且 不是 闰年。每个月份的天数分别为：[31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31] 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：arriveAlice = "08-15", leaveAlice = "08-18", arriveBob = "08-16", leaveBob = "08-19"
 * 输出：3
 * 解释：Alice 从 8 月 15 号到 8 月 18 号在罗马。Bob 从 8 月 16 号到 8 月 19 号在罗马，他们同时在罗马的日期为 8 月 16、17 和 18 号。所以答案为 3 。
 * 示例 2：
 *
 * 输入：arriveAlice = "10-01", leaveAlice = "10-31", arriveBob = "11-01", leaveBob = "12-31"
 * 输出：0
 * 解释：Alice 和 Bob 没有同时在罗马的日子，所以我们返回 0 。
 *
 *
 * 提示：
 *
 * 所有日期的格式均为 "MM-DD" 。
 * Alice 和 Bob 的到达日期都 早于或等于 他们的离开日期。
 * 题目测试用例所给出的日期均为 非闰年 的有效日期。
 * 通过次数20,623提交次数37,080
 */
public class _2409_CountDaysSpentTogether {

    // 快速得到每个月开始前的总天数
    private static final int[] daysPrefixSum = new int[13];

    static {
        // 每个月的天数
        final int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        // i 月前的总天数
        for (int i = 1; i <= 12; ++i) {
            daysPrefixSum[i] = daysPrefixSum[i - 1] + days[i - 1];
        }
    }

    /**
     * 方法一：分别计算出每个日子是一年中的第几天后求差
     * 思路
     *
     * 我们可以设计一个函数
     * calculateDayOfYear
     * calculateDayOfYear 来计算输入中的每个日子在一年中是第几天。计算输入中的每个日子在一年中是第几天时，可以利用前缀和数组来降低每次计算的复杂度。知道每个日子是一年中的第几天后，可以先通过比较算出两人到达日子的最大值，离开日子的最小值，然后利用减法计算重合的日子。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/count-days-spent-together/solution/tong-ji-gong-tong-du-guo-de-ri-zi-shu-by-1iwp/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param arriveAlice
     * @param leaveAlice
     * @param arriveBob
     * @param leaveBob
     * @return
     */
    public int countDaysTogether(String arriveAlice, String leaveAlice, String arriveBob, String leaveBob) {
        // 得到二人的 到达、离开时间
        int aStart = getDays(arriveAlice), aEnd = getDays(leaveAlice), bStart = getDays(arriveBob), bEnd = getDays(leaveBob);
        // 二者之间 更晚的到达时间、更早的离开时间
        int laterArrive = Math.max(aStart, bStart), earlyLeave = Math.min(aEnd, bEnd);
        // 二者之差就是交集，可能无交集，会为负数，此时返回 0
        return Math.max(earlyLeave - laterArrive + 1, 0);
    }

    /**
     * 解析 xx-xx 格式的字符串，得到 月份、天数 后，得到 这是在 一年中的第几天
     * @param date
     * @return
     */
    private int getDays(String date) {
        String[] info = date.split("-");
        int month = Integer.parseInt(info[0]);
        int day = Integer.parseInt(info[1]);
        return daysPrefixSum[month - 1] + day;
    }
}
