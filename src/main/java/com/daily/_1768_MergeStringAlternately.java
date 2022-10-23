package com.daily;

/**
 * @author wangwei
 * @date 2022/10/23 12:36
 * @description: _1768_MergeStringAlternately
 *
 * 1768. 交替合并字符串
 * 给你两个字符串 word1 和 word2 。请你从 word1 开始，通过交替添加字母来合并字符串。如果一个字符串比另一个字符串长，就将多出来的字母追加到合并后字符串的末尾。
 *
 * 返回 合并后的字符串 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：word1 = "abc", word2 = "pqr"
 * 输出："apbqcr"
 * 解释：字符串合并情况如下所示：
 * word1：  a   b   c
 * word2：    p   q   r
 * 合并后：  a p b q c r
 * 示例 2：
 *
 * 输入：word1 = "ab", word2 = "pqrs"
 * 输出："apbqrs"
 * 解释：注意，word2 比 word1 长，"rs" 需要追加到合并后字符串的末尾。
 * word1：  a   b
 * word2：    p   q   r   s
 * 合并后：  a p b q   r   s
 * 示例 3：
 *
 * 输入：word1 = "abcd", word2 = "pq"
 * 输出："apbqcd"
 * 解释：注意，word1 比 word2 长，"cd" 需要追加到合并后字符串的末尾。
 * word1：  a   b   c   d
 * word2：    p   q
 * 合并后：  a p b q c   d
 *
 *
 * 提示：
 *
 * 1 <= word1.length, word2.length <= 100
 * word1 和 word2 由小写英文字母组成
 * 通过次数32,819提交次数42,038
 */
public class _1768_MergeStringAlternately {


    /**
     * 我们直接按照题目的要求模拟即可。我们使用两个指针 i 和 j，初始时分别指向两个字符串的首个位置。随后的每次循环中，依次进行如下的两步操作：
     *
     * 如果 i 没有超出 word1 的范围，就将 word1[i] 加入答案，并且将 i 移动一个位置；
     *
     * 如果 j 没有超出 word2 的范围，就将 word2[j] 加入答案，并且将 j 移动一个位置。
     *
     * 当 i 和 j 都超出对应的范围后，结束循环并返回答案即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/merge-strings-alternately/solution/jiao-ti-he-bing-zi-fu-chuan-by-leetcode-ac4ih/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param word1
     * @param word2
     * @return
     */
    public String mergeAlternately(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int i = 0, j = 0;
        StringBuilder builder = new StringBuilder();
        while (i < m || j < n) {
            if (i < m) {
                builder.append(word1.charAt(i++));
            }
            if (j < n) {
                builder.append(word2.charAt(j++));
            }
        }
        return builder.toString();
    }
}
