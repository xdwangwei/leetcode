package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/11/17 14:40
 * @description: _792_NumberOfMatchingSubSequences
 *
 * 792. 匹配子序列的单词数
 * 给定字符串 s 和字符串数组 words, 返回  words[i] 中是s的子序列的单词个数 。
 *
 * 字符串的 子序列 是从原始字符串中生成的新字符串，可以从中删去一些字符(可以是none)，而不改变其余字符的相对顺序。
 *
 * 例如， “ace” 是 “abcde” 的子序列。
 *
 *
 * 示例 1:
 *
 * 输入: s = "abcde", words = ["a","bb","acd","ace"]
 * 输出: 3
 * 解释: 有三个是 s 的子序列的单词: "a", "acd", "ace"。
 * Example 2:
 *
 * 输入: s = "dsahjpjauf", words = ["ahjpjau","ja","ahbwzgqnuk","tnmlanowax"]
 * 输出: 2
 *
 *
 * 提示:
 *
 * 1 <= s.length <= 5 * 104
 * 1 <= words.length <= 5000
 * 1 <= words[i].length <= 50
 * words[i]和 s 都只由小写字母组成。
 * 通过次数24,718提交次数49,367
 */
public class _792_NumberOfMatchingSubSequences {


    /**
     * 方法一：暴力搜索，对于 words 每个单词，都去判断 它是否是 s 的子序列，
     * 根据题目数据规模，超时
     * @param s
     * @param words
     * @return
     */
    public int numMatchingSubseq(String s, String[] words) {
        int ans = 0;
        for (String word : words) {
            if (isSubSequence(s, word)) {
                ans++;
            }
        }
        return ans;
    }

    /**
     * 判断 串 t 是否 是 串s 的子序列
     * @param s
     * @param t
     * @return
     */
    private boolean isSubSequence(String s, String t) {
        int m = s.length(), n = t.length();
        if (m < n) {
            return false;
        }
        if (m == n) {
            return s.equals(t);
        }
        int i = 0, j = 0;
        while (i < m && j < n) {
            if (s.charAt(i) == t.charAt(j)) {
                i++;
                j++;
            } else {
                i++;
            }
        }
        return j == n;
    }

    /**
     * 在朴素方法的匹配的过程中，
     * 对于每一个需要匹配的字符 t[j]，
     * 我们都需要将字符串 s 中的指针 i 在当前位置不断往后移动直至找到字符 s[i] 使得 s[i]=t[j]，或者移至结尾，
     *
     * 我们现在考虑能否加速这个过程——
     * 如果我们将字符串 s 中的全部的字符的位置按照对应的字符进行存储，令其为数组 pos，
     * 其中 pos[c] 存储的是字符串 s 中字符为 c 的从小到大排列的位置。
     * 那么对于需要匹配的字符 t[j] 我们就可以通过在对应的 pos 数组中进行「二分查找」来找到第一个大于当前 s[i] 指针的位置
     * ，若不存在则说明匹配不成功，否则就将指针 s[i] 直接移到找到的对应位置，并将指针 j 后移一个单位，这样就加速了指针 s[i] 的移动。
     *
     * 也就是说对于 word 中的每个字符 t[j]，假如 t[j-1] 在 s 中的 出现位置为 1，需要找到 s中 i之后第一个等于字符t[j]的位置
     * 以此类推，如果所有字符都能找到有效位置，那么 word 就是 s 的一个子序列
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/number-of-matching-subsequences/solution/pi-pei-zi-xu-lie-de-dan-ci-shu-by-leetco-vki7/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param words
     * @return
     */
    public int numMatchingSubseq2(String s, String[] words) {
        Map<Character, List<Integer>> charIdxMap = new HashMap<>();
        // 记录s中每个字符在s中的顺序（递增）出现位置
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            List<Integer> list = charIdxMap.getOrDefault(c, new ArrayList<>());
            list.add(i);
            charIdxMap.put(c, list);
        }
        // 逐个判断word是否是s的子序列
        int ans = 0;
        for (String word : words) {
            if (word.length() > s.length()) {
                continue;
            }
            // 假如 t[j-1] 在 s 中的 出现位置为 i，需要找到 s中 i 之后第一个等于字符t[j]的位置
            // idx代表当前遍历到的字符的上一个字符在s中的出现位置
            // 初始 t[-1] 在 s 中出现位置为 -1
            int n = word.length(), idx = -1;
            // 逐个遍历 t 字符
            for (int i = 0; i < n; ++i) {
                char c = word.charAt(i);
                // s中不存在此字符，直接跳出当前word的匹配过程
                // 这里强行将idx赋值为-1是因为可能word前两个字符在s里，第三个字符不在，此时idx不是-1，
                // 为了让for之后的if判断正确，这里强行赋值
                if (!charIdxMap.containsKey(c)) {
                    idx = -1;
                    break;
                }
                // 二分搜索。寻找 t[j] 在 s 中 从 idx 之后位置 第一次出现的 位置
                List<Integer> list = charIdxMap.get(c);
                idx = leftBoundBinarySearch(list, idx);
                // 搜索失败，结束
                if (idx == -1) {
                    break;
                }
                // 否则 更新 idx为当前字符在s中的出现位置，注意二分搜索返回的是索引，这里需要实际值
                idx = list.get(idx);
            }
            // 当前word所有字符寻找过程中，idx需要始终不为-1
            if (idx != -1) {
                ans++;
            }
        }
        return ans;
    }

    /**
     * 左边界二分搜索
     * 在nums中寻找第一个 > target 的元素下标，如果不存在则返回 -1
     * @param nums
     * @param target
     * @return
     */
    private int leftBoundBinarySearch(List<Integer> nums, int target) {
        int l = 0, h = nums.size();
        while (l < h) {
            int m = l + (h - l) / 2;
            if (nums.get(m) > target) {
                h = m;
            } else {
                l = m + 1;
            }
        }
        // 如果元素小于 < target，那么 l 会增长到 nums.size()
        // 如果元素大于 < target，那么 l 和 h 都会变为 0
        return l == nums.size() ? -1 : l;
    }


    /**
     * 方法三：分桶
     *
     * 如果暴力枚举 words 中的每个字符串 w，判断其是否为 s 的子序列，会超时。
     *
     * 我们不妨将 words 中的所有单词根据首字母来分桶，即：
     * 把所有单词按照首字母分到 26 个桶中，每个桶中存储的是所有以该字母开头的所有单词。
     *
     * 比如对于 words = ["a", "bb", "acd", "ace"]，我们得到以下的分桶结果：
     * a: ["a", "acd", "ace"]
     * b: ["bb"]
     *
     * 然后我们从 s 的第一个字符开始遍历，假设当前字符为 'a'，
     *      我们从 'a' 开头的桶中取出所有单词。
     *          对于取出的每个单词，如果此时单词长度为 1，说明该单词已经匹配完毕，我们将答案加 1；
     *          否则我们将单词的首字母去掉，然后放入下一个字母开头的桶中，
     *          比如对于单词 "acd"，去掉首字母 'a' 后，我们将其放入 'c' 开头的桶中。
     *          这一轮结束后，分桶结果变为：
     *          c: ["cd", "ce"]
     *          b: ["bb"]
     * 遍历s下一个字符...
     * 遍历完 s 后，我们就得到了答案。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/number-of-matching-subsequences/solution/by-lcbin-gwyj/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int numMatchingSubseq3(String s, String[] words) {
        Deque<String>[] d = new Deque[26];
        for (int i = 0; i < 26; ++i) {
            d[i] = new ArrayDeque<>();
        }
        // 把所有单词按照首字母分到 26 个桶中，每个桶中存储的是所有以该字母开头的所有单词。
        for (String w : words) {
            d[w.charAt(0) - 'a'].add(w);
        }
        int ans = 0;
        // 从 s 的第一个字符开始遍历
        for (char c : s.toCharArray()) {
            // 取出以当前字符开头（桶）的所有单词
            Deque<String> q = d[c - 'a'];
            for (int k = q.size(); k > 0; --k) {
                // 对于取出的每个单词，如果此时单词长度为 1，说明该单词已经匹配完毕，将答案加 1；
                String t = q.pollFirst();
                if (t.length() == 1) {
                    ++ans;
                } else {
                    // 否则我们将单词的首字母去掉，然后放入下一个字母开头的桶中，
                    d[t.charAt(1) - 'a'].offer(t.substring(1));
                }
            }
        }
        // 返回
        return ans;
    }

    /**
     * 方法三空间优化
     * 实际上，不必在桶中存储实际的字符串，每个桶可以只存储这个串在原单词列表中的下标 i 以及在该单词中对应的截取位置 j，
     * 这样可以节省空间。
     * 当j=words[i].length时，说明word[i]全部字符已匹配上，ans++
     * @param s
     * @param words
     * @return
     */
    public int numMatchingSubseq4(String s, String[] words) {
        Deque<int[]>[] d = new Deque[26];
        for (int i = 0; i < 26; ++i) {
            d[i] = new ArrayDeque<>();
        }
        // 把所有单词按照首字母分到 26 个桶中，每个桶中存储的是所有以该字母开头的所有单词。
        // 优化为，存储 在原单词列表中的单词索引，在当前单词中的截取位置
        // 初始加入完整单词，截取位置就是0
        for (int i = 0; i < words.length; ++i) {
            d[words[i].charAt(0) - 'a'].offer(new int[] {i, 0});
        }
        int ans = 0;
        // 从 s 的第一个字符开始遍历
        for (char c : s.toCharArray()) {
            Deque<int[]> q = d[c - 'a'];
            // 取出以当前字符开头（桶）的所有单词
            for (int t = q.size(); t > 0; --t) {
                int[] p = q.pollFirst();
                // i 是在原单词列表中哪个单词，j 是当前串在该单词中对应的截取位置 j，
                int i = p[0], j = p[1] + 1;
                // 当j=words[i].length时，说明word[i]全部字符已匹配上，ans++
                if (j == words[i].length()) {
                    ++ans;
                } else {
                    // 否则我们将单词的首字母去掉，然后放入下一个字母开头的桶中，
                    d[words[i].charAt(j) - 'a'].offer(new int[] {i, j});
                }
            }
        }
        // 返回
        return ans;
    }
}
