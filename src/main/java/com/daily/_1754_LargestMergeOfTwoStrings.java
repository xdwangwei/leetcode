package com.daily;

/**
 * @author wangwei
 * @date 2022/12/24 10:24
 * @description: _1754_LargestMergeOfTwoStrings
 *
 * 1754. 构造字典序最大的合并字符串
 * 给你两个字符串 word1 和 word2 。你需要按下述方式构造一个新字符串 merge ：如果 word1 或 word2 非空，选择 下面选项之一 继续操作：
 *
 * 如果 word1 非空，将 word1 中的第一个字符附加到 merge 的末尾，并将其从 word1 中移除。
 * 例如，word1 = "abc" 且 merge = "dv" ，在执行此选项操作之后，word1 = "bc" ，同时 merge = "dva" 。
 * 如果 word2 非空，将 word2 中的第一个字符附加到 merge 的末尾，并将其从 word2 中移除。
 * 例如，word2 = "abc" 且 merge = "" ，在执行此选项操作之后，word2 = "bc" ，同时 merge = "a" 。
 * 返回你可以构造的字典序 最大 的合并字符串 merge 。
 *
 * 长度相同的两个字符串 a 和 b 比较字典序大小，如果在 a 和 b 出现不同的第一个位置，a 中字符在字母表中的出现顺序位于 b 中相应字符之后，就认为字符串 a 按字典序比字符串 b 更大。例如，"abcd" 按字典序比 "abcc" 更大，因为两个字符串出现不同的第一个位置是第四个字符，而 d 在字母表中的出现顺序位于 c 之后。
 *
 *
 *
 * 示例 1：
 *
 * 输入：word1 = "cabaa", word2 = "bcaaa"
 * 输出："cbcabaaaaa"
 * 解释：构造字典序最大的合并字符串，可行的一种方法如下所示：
 * - 从 word1 中取第一个字符：merge = "c"，word1 = "abaa"，word2 = "bcaaa"
 * - 从 word2 中取第一个字符：merge = "cb"，word1 = "abaa"，word2 = "caaa"
 * - 从 word2 中取第一个字符：merge = "cbc"，word1 = "abaa"，word2 = "aaa"
 * - 从 word1 中取第一个字符：merge = "cbca"，word1 = "baa"，word2 = "aaa"
 * - 从 word1 中取第一个字符：merge = "cbcab"，word1 = "aa"，word2 = "aaa"
 * - 将 word1 和 word2 中剩下的 5 个 a 附加到 merge 的末尾。
 * 示例 2：
 *
 * 输入：word1 = "abcabc", word2 = "abdcaba"
 * 输出："abdcabcabcaba"
 *
 *
 * 提示：
 *
 * 1 <= word1.length, word2.length <= 3000
 * word1 和 word2 仅由小写英文组成
 * 通过次数7,491提交次数15,746
 */
public class _1754_LargestMergeOfTwoStrings {

    /**
     * 方法一：贪心 + 双指针
     *
     * 我们用指针 i 和 j 分别指向字符串 word1 和 word2 的第一个字符。
     *
     * 然后循环，每次比较 word1[i:] 和 word2[j:] 的字典序大小，
     * 如果 word1[i:] 比 word2[j:] 大，那么我们就将 word1[i] 加入答案，否则我们就将 word2[j] 加入答案。
     * 循环，直至 i 到达字符串 word1 的末尾，或者 j 到达字符串 word2 的末尾。
     *
     * 最后我们将剩余的字符串加入答案即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/largest-merge-of-two-strings/solution/by-lcbin-t1mu/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param word1
     * @param word2
     * @return
     */
    public String largestMerge(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        // 双指针
        int i = 0, j = 0;
        StringBuilder builder = new StringBuilder();
        while (i < word1.length() && j < word2.length()) {
            // 比较 word1[i:] 和 word2[j:] 的字典序大小，选择字典序较大的串的首字符
            // 前进对应指针
            if (word1.substring(i).compareTo(word2.substring(j)) > 0) {
                builder.append(word1.charAt(i));
                i++;
            } else {
                builder.append(word2.charAt(j));
                j++;
            }
        }
        // 某个串还有剩余
        while (i < m) {
            builder.append(word1.charAt(i++));
        }
        while (j < n) {
            builder.append(word2.charAt(j++));
        }
        // 返回
        return builder.toString();
    }

    public static void main(String[] args) {
        _1754_LargestMergeOfTwoStrings obj = new _1754_LargestMergeOfTwoStrings();
        System.out.println(obj.largestMerge("cabaa", "bcaaa"));

    }
}
