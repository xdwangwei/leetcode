package com.daily;

/**
 * @author wangwei
 * @date 2023/3/9 19:38
 * @description: _2379_MinimumRecolorsToGetKConsecutiveBlackBlocks
 *
 * 2379. 得到 K 个黑块的最少涂色次数
 * 给你一个长度为 n 下标从 0 开始的字符串 blocks ，blocks[i] 要么是 'W' 要么是 'B' ，表示第 i 块的颜色。字符 'W' 和 'B' 分别表示白色和黑色。
 *
 * 给你一个整数 k ，表示想要 连续 黑色块的数目。
 *
 * 每一次操作中，你可以选择一个白色块将它 涂成 黑色块。
 *
 * 请你返回至少出现 一次 连续 k 个黑色块的 最少 操作次数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：blocks = "WBBWWBBWBW", k = 7
 * 输出：3
 * 解释：
 * 一种得到 7 个连续黑色块的方法是把第 0 ，3 和 4 个块涂成黑色。
 * 得到 blocks = "BBBBBBBWBW" 。
 * 可以证明无法用少于 3 次操作得到 7 个连续的黑块。
 * 所以我们返回 3 。
 * 示例 2：
 *
 * 输入：blocks = "WBWBBBW", k = 2
 * 输出：0
 * 解释：
 * 不需要任何操作，因为已经有 2 个连续的黑块。
 * 所以我们返回 0 。
 *
 *
 * 提示：
 *
 * n == blocks.length
 * 1 <= n <= 100
 * blocks[i] 要么是 'W' ，要么是 'B' 。
 * 1 <= k <= n
 * 通过次数28,319提交次数45,435
 */
public class _2379_MinimumRecolorsToGetKConsecutiveBlackBlocks {

    /**
     * 方法：滑动窗口
     *
     * 我们可以用「滑动窗口」来解决该问题。
     *
     * 我们用一个固定大小为 k 的「滑动窗口」表示出现连续 k 个黑色块的区间
     *
     * 我们需要将该区间全部变为黑色块，此时我们需要的操作次数为该区间中白色块的数目，
     * 那么我们只需要在「滑动窗口」从左向右移动的过程中维护窗口中【白色块】的数目，
     * 最后返回移动过程中白色块数目的最小值 即为 我们需要至少出现一次连续 k 个黑色块的最少操作次数。
     *
     * 窗口固定大小 k
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/minimum-recolors-to-get-k-consecutive-black-blocks/solution/de-dao-kge-hei-kuai-de-zui-shao-tu-se-ci-gjb0/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param blocks
     * @param k
     * @return
     */
    public int minimumRecolors(String blocks, int k) {
        int n = blocks.length();
        // 每个大小为k的窗口中白色块的数目
        int whites = 0;
        // 题目 n >= k，第一个大小为k的窗口
        for (int i = 0; i < k; ++i) {
            whites += blocks.charAt(i) == 'W' ? 1 : 0;
        }
        // 初始化为第一个窗口中白色块数目
        int ans = whites;
        // 滑动，右边界从k开始，窗口大小为k，左边界为 r - k
        for (int i = k ; i < n; ++i) {
            // 加入右边界，移除左边界，更新窗口内白色块数目
            whites += blocks.charAt(i) == 'W' ? 1 : 0;
            whites -= blocks.charAt(i - k) == 'W' ? 1 : 0;
            // ans，取所有窗口中白色块最少值
            ans = Math.min(ans, whites);
        }
        // 返回
        return ans;
    }
}
