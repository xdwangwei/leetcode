package com.daily;

/**
 * @author wangwei
 * @date 2022/5/17 15:16
 * @description: _953_VarifyingAnAlienDictionary
 *
 * 953. 验证外星语词典
 * 某种外星语也使用英文小写字母，但可能顺序 order 不同。字母表的顺序（order）是一些小写字母的排列。
 *
 * 给定一组用外星语书写的单词 words，以及其字母表的顺序 order，只有当给定的单词在这种外星语中按字典序排列时，返回 true；否则，返回 false。
 *
 *
 *
 * 示例 1：
 *
 * 输入：words = ["hello","leetcode"], order = "hlabcdefgijkmnopqrstuvwxyz"
 * 输出：true
 * 解释：在该语言的字母表中，'h' 位于 'l' 之前，所以单词序列是按字典序排列的。
 * 示例 2：
 *
 * 输入：words = ["word","world","row"], order = "worldabcefghijkmnpqstuvxyz"
 * 输出：false
 * 解释：在该语言的字母表中，'d' 位于 'l' 之后，那么 words[0] > words[1]，因此单词序列不是按字典序排列的。
 * 示例 3：
 *
 * 输入：words = ["apple","app"], order = "abcdefghijklmnopqrstuvwxyz"
 * 输出：false
 * 解释：当前三个字符 "app" 匹配时，第二个字符串相对短一些，然后根据词典编纂规则 "apple" > "app"，因为 'l' > '∅'，其中 '∅' 是空白字符，定义为比任何其他字符都小（更多信息）。
 *
 *
 * 提示：
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length <= 20
 * order.length == 26
 * 在 words[i] 和 order 中的所有字符都是英文小写字母。
 */
public class _953_VarifyingAnAlienDictionary {


    /**
     * 方法一：直接遍历：相当于把重新给定 abcdefg 的排列优先级
     * 思路与算法
     *
     * 题目要求按照给定的字母表 order 的顺序，检测给定的字符串数组是否按照 order 的字典升序排列，
     * 我们只需要依次检测 strs 中的字符串前一个字符串和后一个字符串在给定的字母表下小的字典序即可。
     * 具体检测如下：
     *
     * 首先将给定的 order 转化为字典序索引 index，index[i] 表示字符 i 在字母表 order 的排序索引，
     * index[i]>index[j] 即表示字符 i 在字母表中的字典序比字符 j 的字典序大，
     * index[i]<index[j] 即表示字符 i 在字母表中的字典序比字符 j 的字典序小。
     *
     * 依次检测第 i 个单词 strs[i] 与第 i-1 个单词 strs[i−1] 的字典序大小，
     * 我们可以依次判断两个单词中从左到右每个字符的字典序大小，
     * 【当第一次出现两个字符的字典序不同时】，即可判断两个字符串的字典序的大小。
     *
     * 特殊情况需要处理，设 strs[i] 的长度为 m，strs[i] 的长度小于 strs[i−1] 的长度且 strs[i−1] 的前 m 个字符与 strs[i−1] 的前 m 个字符相等，
     * 此时 strs[i−1] 的字典序大于strs[i] 的字典序。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/verifying-an-alien-dictionary/solution/yan-zheng-wai-xing-yu-ci-dian-by-leetcod-jew7/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param words
     * @param order
     * @return
     */
    public boolean isAlienSorted(String[] words, String order) {
        // 保存每个字母新的优先级
        int[] priority = new int[26];
        int idx = 0;
        for (char c: order.toCharArray()) {
            priority[c - 'a'] = idx++;
        }
        // 按顺序判断每两个单词是否符合字典序
        for (int i = 0; i < words.length - 1; ++i) {
            // 任意两个串不符合字典序，就返回false
            if (!isValid(words[i], words[i + 1], priority)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断串str1和str2是否符合字典序，注意与字符串比较算法不一样
     * 当s1[0]优先级 < s2[0]优先级 时，就满足，返回 true
     * 当s1[0]优先级 > s2[0]优先级 时，就满足，返回 false
     * 只有 当s1[0]优先级 == s2[0]优先级 时，才需要去判断 s[1] 和 s2[1]
     *
     * 特殊情况就是：abc 和 abcd 这种，这种情况下，前缀相同，前缀部分每个字符的优先级也都匹配，此时 abc 应该 排在前面
     * @param str1
     * @param str2
     * @param priority
     * @return
     */
    private boolean isValid(String str1, String str2, int[] priority) {
        int m = str1.length(), n = str2.length();
        int i = 0, j = 0;
        while (i < m && j < n) {
            int p1 = priority[str1.charAt(i) - 'a'];
            int p2 = priority[str2.charAt(j) - 'a'];
            // 当s1[0]优先级 < s2[0]优先级 时，就满足，返回 true
            if (p1 < p2) {
                return true;
            } else if (p1 > p2) {
                // 当s1[0]优先级 > s2[0]优先级 时，就满足，返回 false
                return false;
            }
            // 只有 当s1[0]优先级 == s2[0]优先级 时，才需要去判断 s[1] 和 s2[1]
            i++; j++;
        }
        // 特殊情况就是：abc 和 abcd 这种，这种情况下，前缀相同，前缀部分每个字符的优先级也都匹配，此时 abc 应该 排在前面
        return i < m ? false : true;
    }

    public static void main(String[] args) {
        _953_VarifyingAnAlienDictionary obj = new _953_VarifyingAnAlienDictionary();
        obj.isAlienSorted(new String[]{"word", "world", "row"}, "worldabcefghijkmnpqstuvxyz");
    }
}
