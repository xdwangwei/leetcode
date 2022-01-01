package com.greed;

import java.util.Arrays;

/**
 * @author wangwei
 * 2022/1/1 20:56
 *
 * 你将会获得一系列视频片段，这些片段来自于一项持续时长为 time 秒的体育赛事。这些片段可能有所重叠，也可能长度不一。
 *
 * 使用数组 clips 描述所有的视频片段，其中 clips[i] = [starti, endi] 表示：某个视频片段开始于 starti 并于 endi 结束。
 *
 * 甚至可以对这些片段自由地再剪辑：
 *
 * 例如，片段 [0, 7] 可以剪切成 [0, 1] + [1, 3] + [3, 7] 三部分。
 * 我们需要将这些片段进行再剪辑，并将剪辑后的内容拼接成覆盖整个运动过程的片段（[0, time]）。返回所需片段的最小数目，如果无法完成该任务，则返回 -1 。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], time = 10
 * 输出：3
 * 解释：
 * 选中 [0,2], [8,10], [1,9] 这三个片段。
 * 然后，按下面的方案重制比赛片段：
 * 将 [1,9] 再剪辑为 [1,2] + [2,8] + [8,9] 。
 * 现在手上的片段为 [0,2] + [2,8] + [8,10]，而这些覆盖了整场比赛 [0, 10]。
 * 示例 2：
 *
 * 输入：clips = [[0,1],[1,2]], time = 5
 * 输出：-1
 * 解释：
 * 无法只用 [0,1] 和 [1,2] 覆盖 [0,5] 的整个过程。
 * 示例 3：
 *
 * 输入：clips = [[0,1],[6,8],[0,2],[5,6],[0,4],[0,3],[6,7],[1,3],[4,7],[1,4],[2,5],[2,6],[3,4],[4,5],[5,7],[6,9]], time = 9
 * 输出：3
 * 解释：
 * 选取片段 [0,4], [4,7] 和 [6,9] 。
 * 示例 4：
 *
 * 输入：clips = [[0,4],[2,8]], time = 5
 * 输出：2
 * 解释：
 * 注意，你可能录制超过比赛结束时间的视频。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/video-stitching
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1024_VideoStitching {

    /**
     * 贪心
     *
     * 区间问题肯定按照区间的起点或者终点进行【排序】
     *
     * 要用若干短视频凑出完成视频[0, T]，至少得有一个短视频的起点是 0（没有一个片段从0开始，那凑个冒险）
     *
     * 如果有几个短视频的起点都相同，那么一定应该选择那个最长（终点最大）的视频（贪心，能少就少啊，一个顶多个）
     * 也就是说按照起点升序排序，起点相同的，按照终点降序排序
     *
     * 排序后，第一个片段起点一定要是0，否则返回-1，如果第一个片段的终点 >= T，那么这一个就搞定了，返回1
     *
     * 然后clips[0]这个视频一定会被选择
     *
     * 当我们确定clips[0]一定会被选择之后，就可以选出第二个会被选择的视频
     *  比较所有起点等于小于clips[0][1]的区间，根据贪心策略，它们中终点最大的那个区间就是第二个会被选中的视频
     *
     * 通过第二个视频区间贪心选择出第三个视频，以此类推，直到覆盖区间[0, T]，或者无法覆盖返回 -1
     *
     * 作者：angela-x
     * 链接：https://leetcode-cn.com/problems/video-stitching/solution/qu-jian-wen-ti-xian-pai-xu-pai-xu-hou-sh-kb62/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param clips
     * @param time
     * @return
     */
    public int videoStitching(int[][] clips, int time) {
        // 按照起点升序排序，起点相同的，按照终点降序排序
        Arrays.sort(clips, (o1, o2) -> o1[0] == o2[0] ? o2[1] - o1[1] : o1[0] - o2[0]);
        int len = clips.length;
        // 第一个片段 起点一定是0
        if (clips[0][0] != 0) {
            return -1;
        }
        // 如果第一个片段就足够了
        if (clips[0][1] >= time) {
            return 1;
        }
        // 第一个片段一定选，curEnd = clips[0][1], count = 1， i 从1开始
        int curEnd = clips[0][1], count = 1, i = 1, nextEnd = 0;
        // 选择 所有起点等于小于clips[0][1]的区间，根据贪心策略，它们中终点最大的那个区间就是第二个会被选中的视频
        // 如果外围while不写clips[i][0] <= curEnd，当内层while条件不满足时i不会增加，会导致死循环
        while (i < len && clips[i][0] <= curEnd) {
            // 选择 所有起点等于小于clips[0][1]的区间，根据贪心策略，它们中终点最大的那个区间就是第二个会被选中的视频
            while (i < len && clips[i][0] <= curEnd) {
                nextEnd = Math.max(nextEnd, clips[i][1]);
                i++;
            }
            // 找到第二个片段，count+1
            count++;
            // 第二个片段（贪心选择）的终点够了
            if (nextEnd >= time) {
                return count;
            }
            // 更新终点，选择第三个片段
            curEnd = nextEnd;
        }
        return -1;
    }

    /**
     * 动态规划
     * dp[i]表示 得到 [0,i] 最少需要几个 片段
     *
     * 由于我们希望子区间的数目尽可能少，因此可以将所有 dp[i] 的初始值设为一个大整数，
     * 并将 dp[0]（即空区间）的初始值设为 0。base case
     *
     * 我们可以枚举所有的子区间来依次计算出所有的 dp 值。我们首先枚举 i，同时对于任意一个子区间 [a, b]
     * 其满足 a <= i <= b, 那么 [0, i] 就 可以 由 [0, a] 和 [a, i] 得到
     * 所以 dp[i] = Math.min(dp[i], dp[a] + 1);
     *
     * 最终的答案即为 dp[time]。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/video-stitching/solution/shi-pin-pin-jie-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param clips
     * @param time
     * @return
     */
    public int videoStitching2(int[][] clips, int time) {
        // 最终返回 dp[time]
        int[] dp = new int[time + 1];
        // 求最小值，初始化为一个很大的数，题目限制取值范围是 1-100，所以选择101
        Arrays.fill(dp, 101);
        // base case
        dp[0] = 0;
        // 递推得到 dp[i]
        for (int i = 1; i <= time; ++i) {
            // 所有区间
            for (int[] clip : clips) {
                // 找到 满足 a <= i <= b,的区间 [a, b]
                if (clip[0] <= i && clip[1] >= i) {
                    // [0, i] 就 可以 由 [0, a] 和 [a, i] 得到
                    dp[i] = Math.min(dp[i], dp[clip[0]] + 1);
                }
            }
        }
        return dp[time] == 101 ? -1 : dp[time];
    }

    /**
     * 贪心
     *
     * 注意到对于所有左端点相同的子区间，其右端点越远越有利。且最佳方案中不可能出现两个左端点相同的子区间。
     * 于是我们预处理所有的子区间，对于每一个位置 i，我们记录以其为左端点的子区间中最远的右端点，记为 maxn[i]。
     *
     * 我们可以参考「55. 跳跃游戏的官方题解」，使用贪心解决这道题。
     *
     * 具体地，我们枚举每一个位置，假设当枚举到位置 i 时，记左端点不大于 i 的所有子区间的最远右端点为 last。这样 last 就代表了当前能覆盖到的最远的右端点。
     *
     * 每次我们枚举到一个新位置，我们都用 maxn[i] 来更新 last。如果更新后 last==i，那么说明下一个位置无法被覆盖，我们无法完成目标。
     *
     * 同时我们还需要记录上一个被使用的子区间的结束位置为 pre，每次我们越过一个被使用的子区间，就说明我们要启用一个新子区间，这个新子区间的结束位置即为当前的 last。
     * 也就是说，每次我们遇到 i==pre，则说明我们用完了一个被使用的子区间。这种情况下我们让答案加 1，并更新 \pre 即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/video-stitching/solution/shi-pin-pin-jie-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param clips
     * @param time
     * @return
     */
    public int videoStiching3(int[][] clips, int time) {
        // 【起点】超过或等于 time，对我们来说没有意义，不用考虑
        int[] maxn = new int[time];
        // 对于每一个位置 i，我们记录以其为左端点的子区间中最远的右端点，记为 maxn[i]。
        for (int[] clip : clips) {
            // 只选择需要的
            if (clip[0] < time) {
                // 同样的左端点，记录最远的右端点
                maxn[clip[0]] = Math.max(maxn[clip[0]], clip[1]);
            }
        }
        int i = 0, preLast = 0, last = 0, count = 0;
        while (i < time) {
            // 当枚举到位置 i 时，记左端点不大于 i 的所有子区间的最远右端点为 last。这样 last 就代表了当前能覆盖到的最远的右端点。
            last = Math.max(last, maxn[i]);
            // 无法再前进
            if (last == i) {
                return -1;
            }
            // 到达上一次的最远位置
            if (preLast == i) {
                // 片段加1
                count++;
                // 更新pre
                preLast = last;
            }
            i++;
        }
        return count;
    }
}
