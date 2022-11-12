package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/11/13 1:26
 * @description: _791_CustomStringrank
 *
 * 791. 自定义字符串排序
 * 给定两个字符串 order 和 s 。order 的所有单词都是 唯一 的，并且以前按照一些自定义的顺序排序。
 *
 * 对 s 的字符进行置换，使其与排序的 order 相匹配。更具体地说，如果在 order 中的字符 x 出现字符 y 之前，那么在排列后的字符串中， x 也应该出现在 y 之前。
 *
 * 返回 满足这个性质的 s 的任意排列 。
 *
 *
 *
 * 示例 1:
 *
 * 输入: order = "cba", s = "abcd"
 * 输出: "cbad"
 * 解释:
 * “a”、“b”、“c”是按顺序出现的，所以“a”、“b”、“c”的顺序应该是“c”、“b”、“a”。
 * 因为“d”不是按顺序出现的，所以它可以在返回的字符串中的任何位置。“dcba”、“cdba”、“cbda”也是有效的输出。
 * 示例 2:
 *
 * 输入: order = "cbafg", s = "abcd"
 * 输出: "cbad"
 *
 *
 * 提示:
 *
 * 1 <= order.length <= 26
 * 1 <= s.length <= 200
 * order 和 s 由小写英文字母组成
 * order 中的所有字符都 不同
 * 通过次数19,168提交次数26,691
 */
public class _791_CustomStringSort {


    /**
     * 方法一：自定义排序
     * 思路与算法
     *
     * 最简单的方法是直接对字符串 s 进行排序。
     *
     * 我们首先遍历给定的字符串 order，将第一个出现的字符的权值赋值为 1，第二个出现的字符的权值赋值为 2，以此类推。
     * 在遍历完成之后，所有未出现字符的权值默认赋值为 0。
     *
     * 随后我们根据权值表，对字符串 s 进行排序，即可得到一种满足要求的排列方案。
     *
     * 时间复杂度：O(nlogn+∣Σ∣)，其中 n 是字符串 s 的长度，Σ 是字符集，在本题中 ∣Σ∣=26。
     *
     * 排序的时间复杂度为 O(nlogn)；
     * 如果我们使用数组存储权值，数组的大小为 O(∣Σ∣)；如果我们使用哈希表存储权值，哈希表的大小与字符串 s 和 order 中出现的字符种类数相同，
     * 为叙述方便也可以记为 O(∣Σ∣)。
     * 空间复杂度：O(∣Σ∣)。即为数组或哈希表需要使用的空间。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/custom-sort-string/solution/zi-ding-yi-zi-fu-chuan-pai-xu-by-leetcod-1qvf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param order
     * @param s
     * @return
     */
    public String customSortString(String order, String s) {
        int[] rank = new int[26];
        char[] chars = order.toCharArray();
        // 统计字符优先级
        for (int i = 0; i < chars.length; i++) {
            rank[chars[i]- 'a'] = i + 1;
        }
        // 基本类型排序不支持自定义比较规则
        List<Character> list = new ArrayList<>();
        for (char c : s.toCharArray()) {
            list.add(c);
        }
        Collections.sort(list, Comparator.comparingInt(c -> rank[c - 'a']));
        StringBuilder sb = new StringBuilder();
        // 排序完重新得到string
        for (Character c : list) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 方法二：计数排序
     * 思路与算法
     *
     * 由于字符集的大小为 26，我们也可以考虑使用计数排序代替普通的排序方法。
     *
     * 我们首先遍历字符串 s，使用数组或哈希表统计每个字符出现的次数。
     * 随后遍历字符串 order 中的每个字符 c，如果其在 s 中出现了 k 次，就在答案的末尾添加 k 个 c，相当于s中这些字符放在了符合order定义的规则位置
     * 并将数组或哈希表中对应的次数置为 0。
     *
     * 最后我们遍历一次哈希表，对于所有次数 k 非 0 的键值对 (c,k)，在答案的末尾添加 k 个 c 即可。这些字符顺序未规定，放最后没问题
     *
     * 复杂度分析
     *
     * 时间复杂度：O(n+∣Σ∣)，其中 n 是字符串 s 的长度，Σ 是字符集，在本题中 ∣Σ∣=26。
     *
     * 空间复杂度：O(∣Σ∣)。即为数组或哈希表需要使用的空间。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/custom-sort-string/solution/zi-ding-yi-zi-fu-chuan-pai-xu-by-leetcod-1qvf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param order
     * @param s
     * @return
     */
    public String customSortString2(String order, String s) {
        int[] cnt = new int[26];
        // 统计 s 中每个字符次数
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }
        StringBuilder sb = new StringBuilder();
        // 按顺序遍历order字符
        for (char c : order.toCharArray()) {
            // 将s中这些字符拿出来
            while (cnt[c - 'a']-- > 0) {
                sb.append(c);
            }
        }
        // 再在末尾补上剩余字符
        for (int i = 0; i < cnt.length; ++i) {
            while (cnt[i]-- > 0) {
                sb.append((char)('a' + i));
            }
        }
        // 返回重构string
        return sb.toString();
    }
}
