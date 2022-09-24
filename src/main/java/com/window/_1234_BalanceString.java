package com.window;

/**
 * @author wangwei
 * @date 2022/9/18 17:53
 * @description: _1234_BalanceString
 *
 * 1234. 替换子串得到平衡字符串
 * 有一个只含有 'Q', 'W', 'E', 'R' 四种字符，且长度为 n 的字符串。
 *
 * 假如在该字符串中，这四个字符都恰好出现 n/4 次，那么它就是一个「平衡字符串」。
 *
 *
 *
 * 给你一个这样的字符串 s，请通过「替换一个子串」的方式，使原字符串 s 变成一个「平衡字符串」。
 *
 * 你可以用和「待替换子串」长度相同的 任何 其他字符串来完成替换。
 *
 * 请返回待替换子串的最小可能长度。
 *
 * 如果原字符串自身就是一个平衡字符串，则返回 0。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "QWER"
 * 输出：0
 * 解释：s 已经是平衡的了。
 * 示例 2：
 *
 * 输入：s = "QQWE"
 * 输出：1
 * 解释：我们需要把一个 'Q' 替换成 'R'，这样得到的 "RQWE" (或 "QRWE") 是平衡的。
 * 示例 3：
 *
 * 输入：s = "QQQW"
 * 输出：2
 * 解释：我们可以把前面的 "QQ" 替换成 "ER"。
 * 示例 4：
 *
 * 输入：s = "QQQQ"
 * 输出：3
 * 解释：我们可以替换后 3 个 'Q'，使 s = "QWER"。
 */
public class _1234_BalanceString {

    /**
     * 对于长度为 n 的字符串 s，首先遍历字符串 s 统计每种字符的出现次数。
     * 如果每种字符的出现次数都是 n/4，则字符串 ss 已经是平衡的，返回 0。
     *
     * 当字符串 s 不平衡时，一定有至少一种字符的出现次数大于 n/4, 以及至少一种字符的出现次数小于 n/4
     *
     * 若字符 c 的出现次数 cnt 大于 n/4，则窗口内至少需要包含 cnt - n/4 个字符c，用来替换缺少的其他字符，来使字符串保持平衡。
     * 如果我能保证我们的窗口中的数量包含多出那部分，我们就能够替换这个窗口内的字符串达到平衡字符串
     *。
     */
    class Solution {
        public int balancedString(String s) {
            int[] cnt = new int[26];
            // 每个字符数量
            for (char c : s.toCharArray()) {
                cnt[c - 'A']++;
            }
            int len = s.length();
            // 每个字符多出来的数量
            int targetQ = Math.max(cnt['Q' - 'A'] - len / 4, 0);
            int targetW = Math.max(cnt['W' - 'A'] - len / 4, 0);
            int targetE = Math.max(cnt['E' - 'A'] - len / 4, 0);
            int targetR = Math.max(cnt['R' - 'A'] - len / 4, 0);
            // 恰好平衡
            if (targetQ == 0 && targetW == 0 && targetE == 0 && targetR == 0) return 0;
            // 当前窗口内每个字符的数量
            int[] cur = new int[26];
            int l = 0, r = 0;
            // 初始化为原串长
            int ans = len;
            // 滑动窗口
            while (r < len) {
                cur[s.charAt(r) - 'A']++;
                // 扩大右边界
                r++;
                // 如果我能保证我们的窗口中的数量包含多出来的那些字符，我们就能够替换这个窗口内的字符串达到平衡字符串
                while (l <= r && cur['Q' - 'A'] >= targetQ
                        && cur['W' - 'A'] >= targetW
                        && cur['E' - 'A'] >= targetE
                        && cur['R' - 'A'] >= targetR) {
                    // r 已经自增了，这里 不需要 r-l+1
                    ans = Math.min(ans, r - l);
                    cur[s.charAt(l) - 'A']--;
                    // 缩小左边界
                    l++;
                }
            }
            return ans;
        }
    }
}
