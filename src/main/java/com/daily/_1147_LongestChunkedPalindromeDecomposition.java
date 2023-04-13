package com.daily;

/**
 * @author wangwei
 * @date 2023/4/12 21:38
 * @description: _1147_LongestChunkedPalindromeDecomposition
 *
 * 1147. 段式回文
 * 你会得到一个字符串 text 。你应该把它分成 k 个子字符串 (subtext1, subtext2，…， subtextk) ，要求满足:
 *
 * subtexti 是 非空 字符串
 * 所有子字符串的连接等于 text ( 即subtext1 + subtext2 + ... + subtextk == text )
 * 对于所有 i 的有效值( 即 1 <= i <= k ) ，subtexti == subtextk - i + 1 均成立
 * 返回k可能最大值。
 *
 *
 *
 * 示例 1：
 *
 * 输入：text = "ghiabcdefhelloadamhelloabcdefghi"
 * 输出：7
 * 解释：我们可以把字符串拆分成 "(ghi)(abcdef)(hello)(adam)(hello)(abcdef)(ghi)"。
 * 示例 2：
 *
 * 输入：text = "merchant"
 * 输出：1
 * 解释：我们可以把字符串拆分成 "(merchant)"。
 * 示例 3：
 *
 * 输入：text = "antaprezatepzapreanta"
 * 输出：11
 * 解释：我们可以把字符串拆分成 "(a)(nt)(a)(pre)(za)(tpe)(za)(pre)(a)(nt)(a)"。
 *
 *
 * 提示：
 *
 * 1 <= text.length <= 1000
 * text 仅由小写英文字符组成
 * 通过次数7,179提交次数12,662
 */
public class _1147_LongestChunkedPalindromeDecomposition {

    /**
     * 方法一：贪心 + 双指针
     *
     * 我们可以从字符串的两端开始，寻找长度最短的、相同且不重叠的前后缀：
     *
     * 如果找不到这样的前后缀，那么整个字符串作为一个段式回文，答案加 1；
     * 如果找到了这样的前后缀，那么这个前后缀作为一个段式回文，答案加 2，然后继续寻找剩下的字符串的前后缀。
     * 以上贪心策略的证明如下：
     *
     * 假设有一个前后缀 A1 和 A2 满足条件，又有一个前后缀 B1 和 B4 满足条件，如下图：
     *
     * |         A1       |     ....       |       A2        |
     * | B1 |   C1   | B2 |     ....       | B3 |   C2  | B4 |
     *
     * 由于 A1=A2，且 B1=B4，
     * 那么有 B3 = B1 = B4 = B2，并且 C1 = C2，
     *
     * 因此，如果我们贪心地将 B1 和 B4 分割出来，那么剩下的 C1 和 C2，以及 B2 和 B3 也能成功分割。
     *
     * 因此我们应该贪心地选择长度最短的相同前后缀进行分割，这样剩余的字符串中，将可能分割出更多的段式回文串。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/longest-chunked-palindrome-decomposition/solution/python3javacgotypescript-yi-ti-shuang-ji-ttp1/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param text
     * @return
     */
    public int longestDecomposition(String text) {
        int n = text.length();
        // 贪心地选择长度最短的相同前后缀进行分割
        // 枚举最短的匹配长度，最大为 n / 2
        for (int i = 1; i < n / 2; ++i) {
            // 一旦匹配上，就从此处分割，得到前后两部分 + 中间部分递归求解
            if (text.substring(0, i).equals(text.substring(n - i))) {
                return 2 + longestDecomposition(text.substring(i, n - i));
            }
        }
        // 无法前后匹配，保留整体
        return 1;
    }
}
