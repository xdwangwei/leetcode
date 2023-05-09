package com.daily;

/**
 * @author wangwei
 * @date 2023/5/9 19:17
 * @description: _2437_NumberOfValidHours
 *
 * 2437. 有效时间的数目
 * 给你一个长度为 5 的字符串 time ，表示一个电子时钟当前的时间，格式为 "hh:mm" 。最早 可能的时间是 "00:00" ，最晚 可能的时间是 "23:59" 。
 *
 * 在字符串 time 中，被字符 ? 替换掉的数位是 未知的 ，被替换的数字可能是 0 到 9 中的任何一个。
 *
 * 请你返回一个整数 answer ，将每一个 ? 都用 0 到 9 中一个数字替换后，可以得到的有效时间的数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：time = "?5:00"
 * 输出：2
 * 解释：我们可以将 ? 替换成 0 或 1 ，得到 "05:00" 或者 "15:00" 。注意我们不能替换成 2 ，因为时间 "25:00" 是无效时间。所以我们有两个选择。
 * 示例 2：
 *
 * 输入：time = "0?:0?"
 * 输出：100
 * 解释：两个 ? 都可以被 0 到 9 之间的任意数字替换，所以我们总共有 100 种选择。
 * 示例 3：
 *
 * 输入：time = "??:??"
 * 输出：1440
 * 解释：小时总共有 24 种选择，分钟总共有 60 种选择。所以总共有 24 * 60 = 1440 种选择。
 *
 *
 * 提示：
 *
 * time 是一个长度为 5 的有效字符串，格式为 "hh:mm" 。
 * "00" <= hh <= "23"
 * "00" <= mm <= "59"
 * 字符串中有的数位是 '?' ，需要用 0 到 9 之间的数字替换。
 */
public class _2437_NumberOfValidHours {

    /**
     * 方法二：分开枚举
     * 思路与算法
     *
     * 由于题目中小时和分钟的限制不同，因此没有必要枚举所有可能的数字，由于小时和分钟限制如下：
     * “00"≤hh≤“23"；“00"≤mm≤“59"；
     * 我们检测所有符合当前字符串 time 匹配的小时 hh 的数目为 countHour，
     * 同时检测检测所有符合当前字符串 time 匹配的分钟 hh 的数目为 countMinute，
     * 则合法有效的时间数目为 countHour×countMinute。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/number-of-valid-clock-times/solution/you-xiao-shi-jian-de-shu-mu-by-leetcode-j7gqz/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param time
     * @return
     */
    public int countTime(String time) {
        // 得到 0-23 能匹配上的小时数，0-59 能匹配上的 分钟数，返回 二者的乘积
        char ha = time.charAt(0), hb = time.charAt(1), ma = time.charAt(3), mb = time.charAt(4);


        return cnt(ha, hb, 24) * cnt(ma, mb, 60);
    }

    /**
     * 统计 "ha hb" 能匹配 [0,hex-1] 中几个数字
     * @param ha
     * @param hb
     * @param hex
     * @return
     */
    private int cnt(char ha, char hb, int hex) {
        int ans = 0;
        // 枚举 [0,hex-1] 每个数字
        for (int i = 0; i < hex; ++i) {
            // 得到高位和低位
            int a = i / 10, b = i % 10;
            // 判断 ha。hb 是否 能够匹配 a、b
            if ((ha == '?' || ha == a + '0') && (hb == '?' || hb == b + '0')) {
                // 匹配成功，ans++
                ans++;
            }
        }
        return ans;
    }
}
