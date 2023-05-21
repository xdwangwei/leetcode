package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/5/19 20:37
 * @description: _1079_LetterTilePossibility
 *
 * 1079. 活字印刷
 * 你有一套活字字模 tiles，其中每个字模上都刻有一个字母 tiles[i]。返回你可以印出的非空字母序列的数目。
 *
 * 注意：本题中，每个活字字模只能使用一次。
 *
 *
 *
 * 示例 1：
 *
 * 输入："AAB"
 * 输出：8
 * 解释：可能的序列为 "A", "B", "AA", "AB", "BA", "AAB", "ABA", "BAA"。
 * 示例 2：
 *
 * 输入："AAABBC"
 * 输出：188
 * 示例 3：
 *
 * 输入："V"
 * 输出：1
 *
 *
 * 提示：
 *
 * 1 <= tiles.length <= 7
 * tiles 由大写英文字母组成
 * 通过次数17,339提交次数23,445
 */
public class _1079_LetterTilePossibility {

    /**
     * 方法一：计数 + 回溯
     *
     * 题目：tiles 长度为 n，问：tiles中的字母能够组成的 长度为1、2、...、n的不同序列 总数是多少
     *
     * 相当于排列组合，
     *      第一个位置可以选哪些字母(长度为1的序列而个数)，
     *      第一个位置选完后，第二个位置可以选哪些字母(长度为2的序列而个数)，
     *      ....(长度为....的序列而个数)
     *      一直到没有字母可以选择（字母选完了，不会去搜索长度超过n的序列）
     *
     * 每个位置都是在做选择，且可能有多个选择，每个选择后，进行后续的搜索过程，各个选择之间是并列关系，所以需要回溯
     *
     * 是否需要记录当前是在给第几个位置做选择（序列长度）？
     *      不需要，
     *      每次进入回溯，都是求取某种长度下的所有可能的序列，这些都是需要统计的答案
     *      当 没有字符可选的时候，自然会结束，所以不用记录当前是在哪个位置
     *
     * 如果避免重复？
     *    我们统计每个字符和它的出现次数，最多也就26种不同字符，
     *    每次选择时，顺序遍历并选择这些字符，消耗1个，
     *    进入后续过程，
     *    回溯后选择下一个字符，这样就不存在重复
     *
     * 具体地，
     *
     * 我们先用一个哈希表或数组 cnt 统计每个字母出现的次数。
     *
     * 接下来定义一个函数 dfs(cnt)，表示当前剩余字母的计数为 cnt 时，能够组成的不同序列的个数。
     *
     * 在函数 dfs(cnt) 中，
     *
     * 我们枚举 cnt 中每个大于 0 的值 cnt[i]，
     *
     * 将 cnt[i] 减 1 表示使用了这个字母，序列个数加 1，然后进行下一层搜索，
     *
     * 在搜索结束后，累加返回的序列个数，然后将 cnt[i] 加 1（回溯，恢复现场）。
     *
     * 最后返回序列个数。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/letter-tile-possibilities/solution/python3javacgotypescript-yi-ti-yi-jie-ji-cxp7/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    // 记录答案
    private int ans;

    public int numTilePossibilities(String tiles) {
        // 统计每个字符的出现次数
        int[] cnt = new int[26];
        for (char c : tiles.toCharArray()) {
            cnt[c - 'A']++;
        }
        // 回溯
        backTrack(cnt);
        // 返回
        return ans;
    }

    /**
     * 字符计数为 cnt 时，能够组成的不同序列的个数
     * @param cnt
     */
    private void backTrack(int[] cnt) {
        // 遍历这些字符
        for (int j = 0; j < cnt.length; j++) {
            // 没有了，跳过
            if (cnt[j] == 0) {
                continue;
            }
            // 选择当前字符，那么加入当前是在给第i个位置做选择，那么长度为i的序列的个数就会增加1种可能，
            // 对每种长度的序列都是如此，所以不用记录i，只需要每次做不同选择，给ans+1即可
            ans++;
            // 消耗了当前字符
            cnt[j]--;
            // 进入下一层（搜索当前长度+1的序列的可能数）
            backTrack(cnt);
            // 撤销选择，并列进行下一个选择
            cnt[j]++;
        }
    }
}
