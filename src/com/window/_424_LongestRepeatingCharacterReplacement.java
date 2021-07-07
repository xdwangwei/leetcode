package com.window;

/**
 * @author wangwei
 * 2020/7/29 9:51
 *
 * 给你一个仅由大写英文字母组成的字符串，你可以将任意位置上的字符替换成另外的字符，总共可最多替换 k 次。在执行上述操作后，找到包含重复字母的最长子串的长度。
 *
 * 注意:
 * 字符串长度 和 k 不会超过 104。
 *
 * 示例 1:
 *
 * 输入:
 * s = "ABAB", k = 2
 *
 * 输出:
 * 4
 *
 * 解释:
 * 用两个'A'替换为两个'B',反之亦然。
 * 示例 2:
 *
 * 输入:
 * s = "AABABBA", k = 1
 *
 * 输出:
 * 4
 *
 * 解释:
 * 将中间的一个'A'替换为'B',字符串变为 "AABBBBA"。
 * 子串 "BBBB" 有最长重复字母, 答案为 4。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-repeating-character-replacement
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _424_LongestRepeatingCharacterReplacement {

    /**
     * 滑动窗口
     *
     * maxCount是历史最大 英文官文解释：
     * Since we are only interested in the longest valid substring, our sliding windows need not shrink,
     * even if a window may cover an invalid substring. We either grow the window by appending one char on the right,
     * or shift the whole window to the right by one.
     * And we only grow the window when the count of the new char exceeds the historical max count (from a previous window that covers a valid substring).
     *
     * That is, we do not need the accurate max count of the current window;
     * we only care if the max count exceeds the historical max count;
     * and that can only happen because of the new char.
     *
     * 不需要每次都去重新更新max_count。
     * 比如说"AAABCDEDFG" k=2，这个case，一开始A出现3次,max_count=3，
     * 但是当指针移到D时发现不行了，要移动left指针了。
     * 此时count['A']-=1，但是不需要把max_count更新为2。
     * 为什么呢？ 因为根据我们的算法，当max_count和k一定时，区间最大长度也就定了。
     * 当我们找到一个max_count之后，我们就能说我们找到了一个长度为d=max_count+k的合法区间，所以最终答案一定不小于d。
     * 所以，当发现继续向右扩展right不合法的时候，我们不需要不断地右移left，只需要保持区间长度为d向右滑动即可。
     * 如果有某个合法区间大于d，一定在某个时刻存在count[t]+1>max_count，这时再去更新max_count即可。
     *
     * 相同解法，第_1004_
     *
     * @param s
     * @param k
     * @return
     */
    public int characterReplacement(String s, int k) {
        // 最终结果
        int res = 0;
        int left = 0, right = 0;
        // 历史窗口中，出现次数最多的那个字符的次数
        // 并且，这个maxCount + k >= 此窗口大小
        // 也就是说，这是一个有效窗口
        // 所谓有效窗口，就是 加入这个窗口内出现次数最多的是A，次数是c。
        // 这个窗口能满足，在k次变换的限制下，把整个窗口内的字符都变成A
        int maxCount = 0;

        // 当前窗口范围内，每个字符的出现次数
        int[] count = new int[26];

        while (right < s.length()) {
            // 当前加入窗口的字符
            int c = s.charAt(right) - 'A';
            // 次数加1
            count[c]++;
            // 窗口右边界扩大
            right++;
            // 我们需要找的是最长的有效窗口(能满足maxCount+k>=窗口大小)
            // 所以【只需要关心】maxCount的【扩大】更新，这只可能发生在窗口右边新加入字符
            maxCount = Math.max(maxCount, count[c]);

            // 当前窗口中出现次数最多的字符的次数maxCount，剩余字符最终只能变换k次
            // 也就是说最多把k个其他字符变成次数最多的字符，变换后的总个数还是不能达到窗口大小
            // 这就是个无效窗口，需要收缩左边界
            // while可以用if代替，因为每次添加一个字符，收缩最多收缩一个字符
            while (maxCount + k < right - left) {
                // 移除最左边字符
                int d = s.charAt(left) - 'A';
                // 他在窗口内的出现次数减1
                count[d]--;
                // 左边界右移
                left++;
            }

            // 缩小后的窗口才是有效窗口，此时更新结果
            res = Math.max(res, right - left);
        }
        return res;
    }
}
