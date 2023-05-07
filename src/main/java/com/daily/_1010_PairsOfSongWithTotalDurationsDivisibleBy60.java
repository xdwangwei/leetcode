package com.daily;

/**
 * @author wangwei
 * @date 2023/5/7 18:01
 * @description: _1010_PairsOfSongWithDuration
 *
 * 1010. 总持续时间可被 60 整除的歌曲
 * 在歌曲列表中，第 i 首歌曲的持续时间为 time[i] 秒。
 *
 * 返回其总持续时间（以秒为单位）可被 60 整除的歌曲对的数量。形式上，我们希望下标数字 i 和 j 满足  i < j 且有 (time[i] + time[j]) % 60 == 0。
 *
 *
 *
 * 示例 1：
 *
 * 输入：time = [30,20,150,100,40]
 * 输出：3
 * 解释：这三对的总持续时间可被 60 整除：
 * (time[0] = 30, time[2] = 150): 总持续时间 180
 * (time[1] = 20, time[3] = 100): 总持续时间 120
 * (time[1] = 20, time[4] = 40): 总持续时间 60
 * 示例 2：
 *
 * 输入：time = [60,60,60]
 * 输出：3
 * 解释：所有三对的总持续时间都是 120，可以被 60 整除。
 *
 *
 * 提示：
 *
 * 1 <= time.length <= 6 * 104
 * 1 <= time[i] <= 500
 * 通过次数34,537提交次数70,402
 */
public class _1010_PairsOfSongWithTotalDurationsDivisibleBy60 {

    /**
     * 方法一：数学 + 计数
     *
     * 如果一个数对 (a,b) 之和能被 60 整除，即 (a+b) mod 60 = 0，那么 (a mod 60 + b mod 60) mod 60 = 0，
     * 不妨记 x = a mod 60, y = b mod 60，那么有 (x+y) mod 60 = 0。
     *      x,y 都 属于 [0, 59]
     *      如果 x = 0，那么 y 也只能是 0
     *      如果 0 < x < 60，那么 y = 60 - x
     *      即 y = (60−x) mod 60。
     *
     *      即，若 a、b 要满足 (a+b) mod 60 = 0，x = a mod 60，y = b mod 60，那么必然要满足 y = (60−x) mod 60
     *      即，对于 a 来说，有多少b能和它配对 == a前面有多少数字?%60 == (60−(a%60)) mod 60
     *
     * 因此，我们可以遍历列表，用一个长度为 60 的数组 cnt 记录每个余数 x 出现的次数。
     *
     * 对于当前的 x，如果数组 cnt 中存在余数 y=(60−x)mod60，那么将 cnt[y] 累加进答案中。
     * 然后，将 x 在数组 cnt 中的出现次数加 1。
     *
     * 继续遍历，直到遍历完整个歌曲列表。
     *
     * 遍历结束后，即可得到满足条件的歌曲对数目。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/pairs-of-songs-with-total-durations-divisible-by-60/solution/python3javacgotypescript-yi-ti-yi-jie-sh-8m9z/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param time
     * @return
     */
    public int numPairsDivisibleBy60(int[] time) {
        // cnt[i]统计time中， %60 余数为 i 的数字个数
        int[] cnt = new int[60];
        int ans = 0;
        for (int a : time) {
            // (a+b) mod 60 = 0，x = a mod 60，y = b mod 60，那么必然要满足 y = (60−x) mod 60
            int x = a % 60;
            // 即，对于 a 来说，有多少b能和它配对 == a前面有多少数字?%60 == (60−(a%60)) mod 60
            ans += cnt[(60 - x) % 60];
            // 更新 time 中 %60 为 x 的数字个数
            cnt[x]++;
        }
        // 返回
        return ans;
    }
}
