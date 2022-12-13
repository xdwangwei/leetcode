package com.daily;

/**
 * @author wangwei
 * @date 2022/12/13 10:25
 * @description: _1832_CheckIfSentenceIsPangram
 *
 * 1832. 判断句子是否为全字母句
 * 全字母句 指包含英语字母表中每个字母至少一次的句子。
 *
 * 给你一个仅由小写英文字母组成的字符串 sentence ，请你判断 sentence 是否为 全字母句 。
 *
 * 如果是，返回 true ；否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：sentence = "thequickbrownfoxjumpsoverthelazydog"
 * 输出：true
 * 解释：sentence 包含英语字母表中每个字母至少一次。
 * 示例 2：
 *
 * 输入：sentence = "leetcode"
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= sentence.length <= 1000
 * sentence 由小写英语字母组成
 * 通过次数30,561提交次数36,217
 */
public class _1832_CheckIfSentenceIsPangram {

    /**
     * 遍历 + 二进制表示
     *
     * 由于字符集仅有 26 个，我们可以使用一个长度为 26 的二进制数字来表示字符集合，这个二进制数字使用 32 位带符号整型变量即可。
     *
     * 二进制数字的第 i 位对应字符集中的第 i 个字符，例如 0 对应 a，1 对应 b，23 对应 x 等。
     *
     * 初始化整型变量 exist 为 0，遍历 sentence 中的每个字符 c，如果 c 是字母表中的第 i~(0≤i<26) 个字母，
     * 就将 exist 的二进制表示中第 i 位赋值为 1。
     *
     * 最后，我们需要判断 exist 是否等于 2^26−1，这个数字的第 0∼25 位都为 1，其余位为 0。
     * 如果等于，返回 true，否则返回 false。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/check-if-the-sentence-is-pangram/solution/pan-duan-ju-zi-shi-fou-wei-quan-zi-mu-ju-xc7a/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param sentence
     * @return
     */
    public boolean checkIfPangram(String sentence) {
        // 二进制低26位表示字符c是否出现
        int mask = 0;
        for (char c: sentence.toCharArray()) {
            // 字符出现，将二进制 c-'a' 位置 赋值为1
            mask |= (1 << (c - 'a'));
        }
        // 若26个字母全出现，则 低25位全为1
        return mask == ((1 << 26) - 1);
    }
}
