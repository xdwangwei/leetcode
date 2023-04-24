package com.daily;

/**
 * @author wangwei
 * @date 2023/4/24 22:07
 * @description: _1163_LastSubstringInLexicographicalOrder
 *
 * 1163. 按字典序排在最后的子串
 * 给你一个字符串 s ，找出它的所有子串并按字典序排列，返回排在最后的那个子串。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "abab"
 * 输出："bab"
 * 解释：我们可以找出 7 个子串 ["a", "ab", "aba", "abab", "b", "ba", "bab"]。按字典序排在最后的子串是 "bab"。
 * 示例 2：
 *
 * 输入：s = "leetcode"
 * 输出："tcode"
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 4 * 105
 * s 仅含有小写英文字符。
 * 通过次数8,285提交次数29,545
 */
public class _1163_LastSubstringInLexicographicalOrder {

    /**
     * 方法：贪心
     *
     * 一、最终返回所有子串中字典序最大的子串，这个子串的开始一定是字符串中的最大字符，否则不可能为最大子串。
     * 二、字典序最大的子串一定是原字符串的后缀。
     *      为什么只有后缀子字符串才可能是排在最后的子字符串？
     *      考虑一个非后缀子字符串 s1，那么s1向后延伸到末尾得到的后缀子字符串 s2 一定比 s1 的字典序更大
     *
     * 问题转变为：假如 c 是 s 中的最大字符，它的出现次数有 a1,a2,a3。。。返回 max(s[a1]...，s[a2]...，s[a3....])
     * 其实就是决定到底选择c的哪个出现位置
     *
     * 实际上只需要找出 c 第一次出现的位置，后续通过遍历并比较的方式就可以得到c全部出现位置，并选择出对应后缀最大的 出现位置
     *
     * 初始时 让 pos = a1，right = a1 + 1，pos表示当前最优的起始位置，right表示当前遍历到哪个字符了
     *
     * 若 s[right] != c，right++；
     * 否则，遇到 c 第二个 出现位置 right，c的两个出现位置中选择更好的
     *      让 i = pos + 1，j = right + 1
     *      while(s[i] == s[j]) i++; j++;
     *      退出 while 时， s[i] != s[j]
     *      如果 s[i] < s[j]，说明 s[right....j] > s[pos....i]，
     *      那么此时 更新 pos 为 right（选择第二个出现位置），right++ 继续往前遍历
     *
     * 最后返回 s.substring(pos)
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/last-substring-in-lexicographical-order/solution/an-zi-dian-xu-pai-zai-zui-hou-de-zi-chua-31yl/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param s
     * @return
     */
    public String lastSubstring(String s) {
        char[] chars = s.toCharArray();
        int n = chars.length;
        // s 中最大的字符 c
        char c = '0';
        // c 第一次的出现位置
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] > c) {
                c = chars[i];
                pos = i;
            }
        }
        // right 是当前遍历到的字符
        int right = pos + 1;
        while (right < n) {
            // 遇到 c 的第二个出现位置，那么就要一教高下，选出二者中更好的位置，也就是后面字符更大
            if (chars[right] != c) {
                right++;
                continue;
            }
            // i、j分别从 c 的两个出现位置 pos、right 的下一个字符出发
            int i = pos + 1, j = right + 1;
            // 二者相等就一直前进，主要不要越界
            while (j < n && chars[i] == chars[j]) {
                i++; j++;
            }
            if (j >= n) {
                break;
            }
            // 说明，c的第二个出现位置往后得到的子串字典序更大，那么更新 pos 为第二个出现位置 right
            if (chars[i] < chars[j]) {
                pos = right;
            }
            // right继续前进遍历下一个字符
            right++;
        }
        // 返回，后缀字符串
        return s.substring(pos);
    }
}
