package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/6/18 19:59
 * @description: _1170_CompareStringsByFrequencyOfTheSmallestCharacter
 *
 * 1170. 比较字符串最小字母出现频次
 * 定义一个函数 f(s)，统计 s  中（按字典序比较）最小字母的出现频次 ，其中 s 是一个非空字符串。
 *
 * 例如，若 s = "dcce"，那么 f(s) = 2，因为字典序最小字母是 "c"，它出现了 2 次。
 *
 * 现在，给你两个字符串数组待查表 queries 和词汇表 words 。对于每次查询 queries[i] ，需统计 words 中满足 f(queries[i]) < f(W) 的 词的数目 ，W 表示词汇表 words 中的每个词。
 *
 * 请你返回一个整数数组 answer 作为答案，其中每个 answer[i] 是第 i 次查询的结果。
 *
 *
 *
 * 示例 1：
 *
 * 输入：queries = ["cbd"], words = ["zaaaz"]
 * 输出：[1]
 * 解释：查询 f("cbd") = 1，而 f("zaaaz") = 3 所以 f("cbd") < f("zaaaz")。
 * 示例 2：
 *
 * 输入：queries = ["bbb","cc"], words = ["a","aa","aaa","aaaa"]
 * 输出：[1,2]
 * 解释：第一个查询 f("bbb") < f("aaaa")，第二个查询 f("aaa") 和 f("aaaa") 都 > f("cc")。
 *
 *
 * 提示：
 *
 * 1 <= queries.length <= 2000
 * 1 <= words.length <= 2000
 * 1 <= queries[i].length, words[i].length <= 10
 * queries[i][j]、words[i][j] 都由小写英文字母组成
 * 通过次数31,212提交次数47,071
 */
public class _1170_CompareStringsByFrequencyOfTheSmallestCharacter {

    /**
     * 方法：排序 + 二分查找
     *
     * 我们先按照题目描述，实现函数 f(s)，函数返回字符串 s 中按字典序比较最小字母的出现频次。
     *
     * 接下来，我们将 words 中的每个字符串 w 都计算出 f(w)，并将其排序，存放在数组 nums 中。
     *
     * 然后，我们遍历 queries 中的每个查询字符串 q，得到 k = f(q)，我们需要查找 nums 中有多少元素 大于 k
     *
     * 为了避免对每个query进行  在nums中有多少元素大于k   这个查询过程，我们对 nums 进行排序，然后 统一采用二分搜索
     *
     * 我们 在 nums 中二分查找第一个大于 k 的位置 i，则 nums 中下标 i 及其后面的元素都满足 k = f(q) < f(W)，
     *
     * 那么当前查询的答案就是 n−i。
     *
     * 以此得到每个 query 的答案，并返回
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/compare-strings-by-frequency-of-the-smallest-character/solution/python3javacgotypescript-yi-ti-yi-jie-pa-nu6o/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param queries
     * @param words
     * @return
     */
    public int[] numSmallerByFrequency(String[] queries, String[] words) {
        int[] nums = new int[words.length];
        // 计算每个 f(w)，得到 nums数组
        for (int i = 0; i < words.length; i++) {
            nums[i] = f(words[i]);
        }
        // 排序
        Arrays.sort(nums);
        int[] ans = new int[queries.length];
        // 遍历查询
        for (int i = 0; i < queries.length; i++) {
            // 对于查询 query，得到 k = f(query)
            // ans[i] 为 nums中有多少元素大于 k
            // 已经对nums排序，二分搜索 nums 中第一个大于k的元素位置 i，则下标 i 及其后面的元素都满足要求，个数为 n-1 -i + 1 = n-i
            ans[i] = nums.length - leftBS(nums, f(queries[i]));
        }
        // 返回
        return ans;
    }

    /**
     * 返回字符串 s 中按字典序比较最小字母的出现频次。
     * @param word
     * @return
     */
    private int f(String word) {
        int[] cnt = new int[26];
        // 统计每个字母出现次数
        for (char c : word.toCharArray()) {
            cnt[c - 'a']++;
        }
        // 返回最小字母出现次数
        for (int x : cnt) {
            if (x > 0) {
                return x;
            }
        }
        return 0;
    }

    /**
     * 左边界二分搜索：查找 有序数组nums 中第一个大于 k 的元素小标
     * @param nums
     * @param k
     * @return
     */
    private int leftBS(int[] nums, int k) {
        // 左闭右开区间 查找模板
        int l = 0, r = nums.length;
        while (l < r) {
            int m = (l + r) >> 1;
            // 太小，增加 l
            if (nums[m] <= k) {
                l = m + 1;
            } else {
                // > k 时，缩小左边界
                r = m;
            }
        }
        // 符合要求时，会走 r = m 的逻辑，所以最后 r 是符合要求的答案
        return r;
    }
}
