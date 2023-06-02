package com.daily;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * @date 2023/6/2 21:26
 * @description: _2559_CountVowelStringsInRange
 *
 * 2559. 统计范围内的元音字符串数
 * 给你一个下标从 0 开始的字符串数组 words 以及一个二维整数数组 queries 。
 *
 * 每个查询 queries[i] = [li, ri] 会要求我们统计在 words 中下标在 li 到 ri 范围内（包含 这两个值）并且以元音开头和结尾的字符串的数目。
 *
 * 返回一个整数数组，其中数组的第 i 个元素对应第 i 个查询的答案。
 *
 * 注意：元音字母是 'a'、'e'、'i'、'o' 和 'u' 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：words = ["aba","bcb","ece","aa","e"], queries = [[0,2],[1,4],[1,1]]
 * 输出：[2,3,0]
 * 解释：以元音开头和结尾的字符串是 "aba"、"ece"、"aa" 和 "e" 。
 * 查询 [0,2] 结果为 2（字符串 "aba" 和 "ece"）。
 * 查询 [1,4] 结果为 3（字符串 "ece"、"aa"、"e"）。
 * 查询 [1,1] 结果为 0 。
 * 返回结果 [2,3,0] 。
 * 示例 2：
 *
 * 输入：words = ["a","e","i"], queries = [[0,2],[0,1],[2,2]]
 * 输出：[3,2,1]
 * 解释：每个字符串都满足这一条件，所以返回 [3,2,1] 。
 *
 *
 * 提示：
 *
 * 1 <= words.length <= 105
 * 1 <= words[i].length <= 40
 * words[i] 仅由小写英文字母组成
 * sum(words[i].length) <= 3 * 105
 * 1 <= queries.length <= 105
 * 0 <= queries[j][0] <= queries[j][1] < words.length
 * 通过次数19,452提交次数30,767
 */
public class _2559_CountVowelStringsInRange {

    /**
     * 方法二：前缀和
     *
     * 我们可以创建一个长度为 n+1 的前缀和数组 s，其中 s[i] 表示数组 words 的前 i 个字符串中以元音开头和结尾的字符串的数目。
     *
     * 让 s 的长度为 i + 1 是为了避免 i == 0 时的特殊处理
     *
     * 这种情况下，s[i] 代表 sum(nums[0...i-1])
     *
     * 初始时 s[0]=0。
     *
     * 接下来，我们遍历数组 words，如果当前字符串以元音开头和结尾，那么 s[i+1]=s[i]+1，否则 s[i+1]=s[i]。
     *
     * 最后，我们遍历每个查询 [l,r]，那么当前查询的答案就是 s[r+1] − s[l]。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/count-vowel-strings-in-ranges/solution/python3javacgotypescript-yi-ti-shuang-ji-lhef/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param words
     * @param queries
     * @return
     */

    public int[] vowelStrings(String[] words, int[][] queries) {
        // 所有元音字符
        Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u');
        int n = words.length;
        // 前缀和数组
        int[] s = new int[n + 1];
        for (int i = 0; i < n; ++i) {
            // 当前单词的第一个和最后一个字符
            char a = words[i].charAt(0), b = words[i].charAt(words[i].length() - 1);
            // 注意这里更新的是 s[i + 1]
            // 如果当前单词符合要求，则 s[i + 1] = s[i] + 1，否则 s[i + 1] = s[i]
            s[i + 1] = s[i] + (vowels.contains(a) && vowels.contains(b) ? 1 : 0);
        }
        // 遍历查询
        int m = queries.length;
        int[] ans = new int[m];
        for (int i = 0; i < m; ++i) {
            // 区间[l, r]
            int l = queries[i][0], r = queries[i][1];
            // 利用前缀和 O(1) 计算答案
            ans[i] = s[r + 1] - s[l];
        }
        return ans;
    }
}
