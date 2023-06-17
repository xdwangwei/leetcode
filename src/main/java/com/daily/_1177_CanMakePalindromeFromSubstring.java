package com.daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2023/6/15 18:28
 * @description: _1177_CanMakePalindromeFromSubstring
 *
 * 1177. 构建回文串检测
 * 给你一个字符串 s，请你对 s 的子串进行检测。
 *
 * 每次检测，待检子串都可以表示为 queries[i] = [left, right, k]。我们可以 重新排列 子串 s[left], ..., s[right]，并从中选择 最多 k 项替换成任何小写英文字母。
 *
 * 如果在上述检测过程中，子串可以变成回文形式的字符串，那么检测结果为 true，否则结果为 false。
 *
 * 返回答案数组 answer[]，其中 answer[i] 是第 i 个待检子串 queries[i] 的检测结果。
 *
 * 注意：在替换时，子串中的每个字母都必须作为 独立的 项进行计数，也就是说，如果 s[left..right] = "aaa" 且 k = 2，我们只能替换其中的两个字母。（另外，任何检测都不会修改原始字符串 s，可以认为每次检测都是独立的）
 *
 *
 *
 * 示例：
 *
 * 输入：s = "abcda", queries = [[3,3,0],[1,2,0],[0,3,1],[0,3,2],[0,4,1]]
 * 输出：[true,false,false,true,true]
 * 解释：
 * queries[0] : 子串 = "d"，回文。
 * queries[1] : 子串 = "bc"，不是回文。
 * queries[2] : 子串 = "abcd"，只替换 1 个字符是变不成回文串的。
 * queries[3] : 子串 = "abcd"，可以变成回文的 "abba"。 也可以变成 "baab"，先重新排序变成 "bacd"，然后把 "cd" 替换为 "ab"。
 * queries[4] : 子串 = "abcda"，可以变成回文的 "abcba"。
 *
 *
 * 提示：
 *
 * 1 <= s.length, queries.length <= 10^5
 * 0 <= queries[i][0] <= queries[i][1] < s.length
 * 0 <= queries[i][2] <= s.length
 * s 中只有小写英文字母
 * 通过次数21,669提交次数52,221
 */
public class _1177_CanMakePalindromeFromSubstring {

    /**
     * 方法：前缀和 & 位运算
     *
     * 根据题目示例知：题目允许在替换前重新排列字母，那么只需要考虑 [l,r] 内每个字符的出现次数的奇偶性，具体分析如下：
     *
     * 回文意味着从左往右第 i 个字母和从右往左第 i 个字母是相同的。（回文串关于回文中心是对称的。）
     *
     * 如果有偶数个 a，那么可以均分成两部分，分别放置在字符串的中心对称位置上。
     * 例如有 4 个 a，可以在字符串的最左边放置 2 个 a，最右边放置 2 个 a，这样字符串中的 a 是回文的。
     * 其它字母如果出现偶数次，也同理。
     *
     * 如果有奇数个 a 要单独拿出来讨论：
     *
     * 假如只有 字符 a 出现奇数次，其它字母都出现偶数次。
     *      此时字符串的长度一定是奇数，那么可以把多出的这个 a 放在字符串的中心，我们仍然可以得到一个回文串，无需替换任何字母。
     * 如果有两种字母出现奇数次（假设是字母 a,b），
     *      由于多出的一个 a 和一个 b 无法组成回文串，可以把一个 b 改成 a（或者把一个 a 改成 b），这样 a 和 b 就都出现偶数次了。
     * 如果有三种字母出现奇数次（假设是字母 a,b,c），
     *      把一个 b 改成 c，就转换成只有 a 出现奇数次的情况了。
     * 如果有四种字母出现奇数次（假设是字母 a,b,c,d），
     *      把其中两个字符换成两外两个字符，如 c->a,d->b,这样 a、b都是偶数次了。
     * 如果有五种字母出现奇数次（假设是字母 a,b,c,d,e），
     *      把其中两个字符换成两外两个字符，如 c->a,d->b,
     *      这样 a、b都是偶数次，转换为只有一个字符e是奇数次的情况了
     *
     * 一般地，如果有 m 种字母出现奇数次，只需修改其中 ⌊m/2⌋ 个字母。
     * 换句话说，如果第 i 次询问有 ⌊m/2⌋≤k，那么 answer[i] 为真，反之为假。其中 m 为 s[l..r]范围内 出现奇数次的 字符个数
     *
     * 最后要解决的问题是，如何快速求出子串中每种字母的个数？
     *
     * 可以创建 26 个前缀和数组，分别统计每种字母。
     * 以字母 a 为例，在计算前缀和时，如果 s[i]=a 就视作1，否则视作 0。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/can-make-palindrome-from-substring/solution/yi-bu-bu-you-hua-cong-qian-zhui-he-dao-q-yh5p/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param queries
     * @return
     */
    public List<Boolean> canMakePaliQueries(String s, int[][] queries) {
        int n = s.length();
        // pre[i][j] 表示 s[0...i-1] 中 字符 j 的个数，最多 26 个字符
        int[][] pre = new int[n + 1][26];
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            // s[0...i] 与 s[0...i-1] 每个字符的次数都一样
            pre[i + 1] = pre[i].clone();
            // 只有 s[i] 位置的字符 次数 + 1
            pre[i + 1][c - 'a'] = pre[i][c - 'a'] + 1;
        }
        List<Boolean> ans = new ArrayList<>();
        // 遍历查询
        for (int[] query : queries) {
            int l = query[0], r = query[1], k = query[2];
            // 统计 s[l...r] 范围内 出现次数为奇数的 字符个数
            // s[l...r] 内字符j的出现次数为 pre[r + 1][j] - pre[l][j]
            int oddChars = 0;
            for (int i = 0; i < 26; ++i) {
                oddChars += (pre[r + 1][i] - pre[l][i]) % 2 == 1 ? 1 : 0;
            }
            // 当这些字符个数不超过 k/2 时，能完成操作
            ans.add(oddChars / 2 <= k);
        }
        // 返回
        return ans;
    }

    /**
     * 方法一优化：
     *
     * 实际上我们关心的是 s[l...r] 范围内每个字符出现次数的奇偶性（1 或者 0），而不在意它具体的出现次数
     * 可以在遍历的过程中，完成对字符奇偶性的统计
     * 比如字符 a，第一次遇到 变为 1，再遇到a，变为 0，再遇到a 变为 1，再遇到 a 变为 0.... 符合 异或运算性质，且只需要一个二进制位
     * 由于最多26个字符，所以可以用一个 int 变量来保存 26 个字符 的出线奇偶性
     *
     * 所以可以将 方法一中的 pre[n + 1][26] 优化为 pre[n + 1]，其中 pre[i]的二进制 保存了 s[0...i-1] 中26个字符的 奇偶性
     * 这样，s[l...r] 范围内 出现次数为 奇数 的字符有哪些 ? pre[r+1] ^ pre[l]，
     * 这些字符的个数为：Integer.bitCount(pre[r + 1] ^ pre[l])
     * @param s
     * @param queries
     * @return
     */
    public List<Boolean> canMakePaliQueries2(String s, int[][] queries) {
        int n = s.length();
        // pre[i]的二进制 保存了 s[0...i-1] 中26个字符的 奇偶性
        int[] pre = new int[n + 1];
        // 遍历
        for (int i = 0; i < n; i++) {
            // 当前字符
            char c = s.charAt(i);
            // s[0...i] 与 s[0...i-1] 每个字符的次数都一样 pre[i+1] = pre[i]
            // 只有 s[i] 位置的字符 次数 + 1，奇偶性 改变，pre[i+1] = pre[i+1] ^ (1 << (c - 'a'))
            pre[i + 1] = pre[i] ^ (1 << c - 'a');
        }
        List<Boolean> ans = new ArrayList<>();
        // 遍历查询
        for (int[] query : queries) {
            int l = query[0], r = query[1], k = query[2];
            // 统计 s[l...r] 范围内 出现次数为奇数的 字符个数
            // s[l...r] 内出现次数为 奇数 的字符有哪些 ? pre[r+1] ^ pre[l]，个数为：Integer.bitCount(pre[r + 1] ^ pre[l])
            ans.add(Integer.bitCount(pre[r + 1] ^ pre[l]) / 2 <= k);
        }
        // 返回
        return ans;
    }
}
