package com.daily;

/**
 * @author wangwei
 * @date 2023/1/13 16:51
 * @description: _2287_RearrangeCharactersToMakeTargetString
 *
 * 2287. 重排字符形成目标字符串
 * 给你两个下标从 0 开始的字符串 s 和 target 。你可以从 s 取出一些字符并将其重排，得到若干新的字符串。
 *
 * 从 s 中取出字符并重新排列，返回可以形成 target 的 最大 副本数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "ilovecodingonleetcode", target = "code"
 * 输出：2
 * 解释：
 * 对于 "code" 的第 1 个副本，选取下标为 4 、5 、6 和 7 的字符。
 * 对于 "code" 的第 2 个副本，选取下标为 17 、18 、19 和 20 的字符。
 * 形成的字符串分别是 "ecod" 和 "code" ，都可以重排为 "code" 。
 * 可以形成最多 2 个 "code" 的副本，所以返回 2 。
 * 示例 2：
 *
 * 输入：s = "abcba", target = "abc"
 * 输出：1
 * 解释：
 * 选取下标为 0 、1 和 2 的字符，可以形成 "abc" 的 1 个副本。
 * 可以形成最多 1 个 "abc" 的副本，所以返回 1 。
 * 注意，尽管下标 3 和 4 分别有额外的 'a' 和 'b' ，但不能重用下标 2 处的 'c' ，所以无法形成 "abc" 的第 2 个副本。
 * 示例 3：
 *
 * 输入：s = "abbaccaddaeea", target = "aaaaa"
 * 输出：1
 * 解释：
 * 选取下标为 0 、3 、6 、9 和 12 的字符，可以形成 "aaaaa" 的 1 个副本。
 * 可以形成最多 1 个 "aaaaa" 的副本，所以返回 1 。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 100
 * 1 <= target.length <= 10
 * s 和 target 由小写英文字母组成
 * 通过次数23,872提交次数36,721
 */
public class _2287_RearrangeCharactersToMakeTargetString {


    /**
     * 方法一：哈希表计数
     * 这道题要求计算使用ss 中的字符可以形成的 target 的最大副本数，
     * 因此需要统计 target 中的每个字符的出现次数，以及统计这些字符在 s 中的出现次数。
     *
     * 如果一个字符在 target 中出现 x 次（x>0），在 s 中出现 y 次，
     * 则在只考虑该字符的情况下，可以形成的 target 的最大副本数是 ⌊y/x⌋。
     * 特别地，如果 y<x，则不能形成 target 的副本。
     *
     * 我们统计字符串 s 和 target 中每个字符出现的次数，记为 cnt1 和 cnt2。
     *
     * 接下来，对于 target 中的每个字符，我们计算 cnt1 中该字符出现的次数除以 cnt2 中该字符出现的次数，取最小值即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/rearrange-characters-to-make-target-string/solution/zhong-pai-zi-fu-xing-cheng-mu-biao-zi-fu-v5te/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param target
     * @return
     */
    public int rearrangeCharacters(String s, String target) {
        // 统计 s 和 target 中每个字符出现次数
        int[] cnt1 = new int[26];
        int[] cnt2 = new int[26];
        for (char c : s.toCharArray()) {
            cnt1[c - 'a']++;
        }
        for (char c : target.toCharArray()) {
            cnt2[c - 'a']++;
        }
        // 对于每个字符，s 中有 cnt1[i] 个，target 中需要 cnt2[i] 个，则 s 可以形成此字符的副本为 cnt1[i] / cnt2[i]
        // 所有字符，取最小值
        int ans = s.length();
        for (int i = 0; i < 26; ++i) {
            // 只统计target中需要的字符
            if (cnt2[i] != 0) {
                ans = Math.min(ans, cnt1[i] / cnt2[i]);
            }
        }
        // 返回
        return ans;
    }
}
