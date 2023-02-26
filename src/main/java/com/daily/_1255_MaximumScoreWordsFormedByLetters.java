package com.daily;

/**
 * @author wangwei
 * @date 2023/2/26 13:41
 * @description: _1255_MaximumScoreWordsFormedByLetters
 *
 * 1255. 得分最高的单词集合
 * 你将会得到一份单词表 words，一个字母表 letters （可能会有重复字母），以及每个字母对应的得分情况表 score。
 *
 * 请你帮忙计算玩家在单词拼写游戏中所能获得的「最高得分」：能够由 letters 里的字母拼写出的 任意 属于 words 单词子集中，分数最高的单词集合的得分。
 *
 * 单词拼写游戏的规则概述如下：
 *
 * 玩家需要用字母表 letters 里的字母来拼写单词表 words 中的单词。
 * 可以只使用字母表 letters 中的部分字母，但是每个字母最多被使用一次。
 * 单词表 words 中每个单词只能计分（使用）一次。
 * 根据字母得分情况表score，字母 'a', 'b', 'c', ... , 'z' 对应的得分分别为 score[0], score[1], ..., score[25]。
 * 本场游戏的「得分」是指：玩家所拼写出的单词集合里包含的所有字母的得分之和。
 *
 *
 * 示例 1：
 *
 * 输入：words = ["dog","cat","dad","good"], letters = ["a","a","c","d","d","d","g","o","o"], score = [1,0,9,5,0,0,3,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0]
 * 输出：23
 * 解释：
 * 字母得分为  a=1, c=9, d=5, g=3, o=2
 * 使用给定的字母表 letters，我们可以拼写单词 "dad" (5+1+5)和 "good" (3+2+2+5)，得分为 23 。
 * 而单词 "dad" 和 "dog" 只能得到 21 分。
 * 示例 2：
 *
 * 输入：words = ["xxxz","ax","bx","cx"], letters = ["z","a","b","c","x","x","x"], score = [4,4,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,10]
 * 输出：27
 * 解释：
 * 字母得分为  a=4, b=4, c=4, x=5, z=10
 * 使用给定的字母表 letters，我们可以组成单词 "ax" (4+5)， "bx" (4+5) 和 "cx" (4+5) ，总得分为 27 。
 * 单词 "xxxz" 的得分仅为 25 。
 * 示例 3：
 *
 * 输入：words = ["leetcode"], letters = ["l","e","t","c","o","d"], score = [0,0,1,1,1,0,0,0,0,0,0,1,0,0,1,0,0,0,0,1,0,0,0,0,0,0]
 * 输出：0
 * 解释：
 * 字母 "e" 在字母表 letters 中只出现了一次，所以无法组成单词表 words 中的单词。
 *
 *
 * 提示：
 *
 * 1 <= words.length <= 14
 * 1 <= words[i].length <= 15
 * 1 <= letters.length <= 100
 * letters[i].length == 1
 * score.length == 26
 * 0 <= score[i] <= 10
 * words[i] 和 letters[i] 只包含小写的英文字母。
 * 通过次数9,465提交次数12,037
 */
public class _1255_MaximumScoreWordsFormedByLetters {

    private int max = 0;
    private int[] cnt = new int[26];
    private int[] score;

    /**
     * 方法：回溯
     *
     * 由于 单词表最多有14个单词，因此对于给定的单词表，我们可以枚举出所有的单词组合，然后判断每个单词组合是否满足题目要求（现有字符能够凑出这些单词），
     * 如果满足则计算其得分，最后取得分最大的单词组合。
     *
     * 先统计给出的字符每种字符的数目 cnt[]
     *
     * 通过回溯完成对单词表的不同子集的选择
     *
     * dfs(string[] words, int idx, int score) 表示对于 words中的单词，从idx位置开始选，枚举可能的选择组合，记录每种组合获得的分数
     * 初始时，idx = 0，score = 0
     *
     * 当 idx == words.length 时，说明完成了一种子集的选择，用 score 去更新 ans
     * 否则：
     *      对于单词表当前单词 word = words[i]，可以选择 凑出word 和 不凑出 word，【做选择】
     *      选择一：不凑出 word，则进行下一单词的选择，即 dfs(words, i + 1, score)
     *      选择二：凑出 word，则 遍历 word中的每个字符 c，从 cnt 中对c的次数进行减1，并 对 score 累加 c 的分数
     *              若在此过程中cnt[c] < 0，说明 当前剩余的 字符不足以 凑出 word，此选择无效
     *      回溯，每个选择后都要进行【撤销】，由于选择一并未出现现有状态的改变，因此无恢复操作，而选择二对cnt进行了修改，需要恢复
     *
     * 对于当前
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/maximum-score-words-formed-by-letters/solution/python3javacgo-yi-ti-yi-jie-er-jin-zhi-m-nv0j/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param words
     * @param letters
     * @param score
     * @return
     */
    public int maxScoreWords(String[] words, char[] letters, int[] score) {
        this.score = score;
        // 统计给出的每个字符的次数
        for (char c : letters) {
            cnt[c - 'a']++;
        }
        // 回溯，枚举可能的words子集，对于可以凑出的每种组合，统计分数，取最大值
        backTrack(words, 0, 0);
        // 返回
        return max;
    }

    /**
     * 回溯
     * @param words
     * @param idx
     * @param curScore
     */
    private void backTrack(String[] words, int idx, int curScore) {
        // 完成了成了一种子集的选择，用 score 去更新 ans
        if (idx == words.length) {
            max = Math.max(max, curScore);
            return;
        }
        // 做选择
        // 选择一，选择凑出 word
        char[] chars = words[idx].toCharArray();
        // 剩余字符能够凑出word
        boolean charsEnough = true;
        // 凑出word可以得到的分数
        int wordScore = 0;
        // 尝试凑出word
        for (char c : chars) {
            // 扣减此字符数目，如果剩余字符不够，说明无法凑出
            if (--cnt[c - 'a'] < 0) {
                charsEnough = false;
            }
            // 统计此字符分数
            wordScore += score[c - 'a'];
        }
        // 只有在字符足够的情况才能完成 选择一
        if (charsEnough) {
            backTrack(words, idx + 1, curScore + wordScore);
        }
        // 插销选择，恢复
        for (char c : chars) {
            cnt[c - 'a']++;
        }
        // 选择二：不拼凑 words[idx]
        backTrack(words, idx + 1, curScore);
    }
}
