package com.daily;

/**
 * @author wangwei
 * @date 2022/11/1 10:40
 * @description: _1662_CheckIfTwoStringArraysAreEquivalent
 *
 * 1662. 检查两个字符串数组是否相等
 * 给你两个字符串数组 word1 和 word2 。如果两个数组表示的字符串相同，返回 true ；否则，返回 false 。
 *
 * 数组表示的字符串 是由数组中的所有元素 按顺序 连接形成的字符串。
 *
 *
 *
 * 示例 1：
 *
 * 输入：word1 = ["ab", "c"], word2 = ["a", "bc"]
 * 输出：true
 * 解释：
 * word1 表示的字符串为 "ab" + "c" -> "abc"
 * word2 表示的字符串为 "a" + "bc" -> "abc"
 * 两个字符串相同，返回 true
 * 示例 2：
 *
 * 输入：word1 = ["a", "cb"], word2 = ["ab", "c"]
 * 输出：false
 * 示例 3：
 *
 * 输入：word1  = ["abc", "d", "defg"], word2 = ["abcddefg"]
 * 输出：true
 *
 *
 * 提示：
 *
 * 1 <= word1.length, word2.length <= 103
 * 1 <= word1[i].length, word2[i].length <= 103
 * 1 <= sum(word1[i].length), sum(word2[i].length) <= 103
 * word1[i] 和 word2[i] 由小写字母组成
 */
public class _1662_CheckIfTwoStringArraysAreEquivalent {


    /**
     * 方法二：遍历
     * 思路与算法
     *
     * 设置两个指针 p1 和 p2 分别表示遍历到了 word1[p1] 和 word2[p2]，
     * 还需设置两个指针 i 和 j，表示正在对比 word1[p1][i] 和 word2[p2][j]。
     *
     * 如果 word1[p1][i] != word2[p2][j]，则直接返回 false。
     *
     * 否则 i+1，当 i =word1[p1].length 时，表示对比到当前字符串末尾，需要将 p1+1，i 赋值为 0。
     *
     * j 和 p2 同理。
     *
     * 当 p1<word1.length 或者 p2<word2.length 不满足时，算法结束。
     * 最终两个数组相等条件即为 p1=word1.length 并且 p2=word2.length。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/check-if-two-string-arrays-are-equivalent/solution/jian-cha-liang-ge-zi-fu-chuan-shu-zu-shi-9iuo/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param word1
     * @param word2
     * @return
     */
    public boolean arrayStringsAreEqual(String[] word1, String[] word2) {
        // 设置两个指针 p1 和 p2 分别表示遍历到了 word1[p1] 和 word2[p2]，
        // 设置两个指针 i 和 j，表示正在对比 word1[p1][i] 和 word2[p2][j]。
        int p1 = 0, p2 = 0, i = 0, j = 0;
        // 当 p1<word1.length 或者 p2<word2.length 不满足时，算法结束。
        while (p1 < word1.length && p2 < word2.length) {
            // 字符不匹配
            if (word1[p1].charAt(i) != word2[p2].charAt(j)) {
                return false;
            }
            i++;
            // 当 p1 指向单词遍历完毕，p1指向下一个，i重新从0开始
            if (i == word1[p1].length()) {
                p1++;
                i = 0;
            }
            // 当 p2 指向单词遍历完毕，p2指向下一个，j重新从0开始
            j++;
            if (j == word2[p2].length()) {
                p2++;
                j = 0;
            }
        }
        // 最后必须时二者都遍历完毕
        return p1 == word1.length && p2 == word2.length;
    }
}
