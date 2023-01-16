package com.daily;

/**
 * @author wangwei
 * @date 2023/1/16 11:19
 * @description: _1813_SentenceSimilarity3
 *
 * 1813. 句子相似性 III
 * 一个句子是由一些单词与它们之间的单个空格组成，且句子的开头和结尾没有多余空格。比方说，"Hello World" ，"HELLO" ，"hello world hello world" 都是句子。每个单词都 只 包含大写和小写英文字母。
 *
 * 如果两个句子 sentence1 和 sentence2 ，可以通过往其中一个句子插入一个任意的句子（可以是空句子）而得到另一个句子，那么我们称这两个句子是 相似的 。比方说，sentence1 = "Hello my name is Jane" 且 sentence2 = "Hello Jane" ，我们可以往 sentence2 中 "Hello" 和 "Jane" 之间插入 "my name is" 得到 sentence1 。
 *
 * 给你两个句子 sentence1 和 sentence2 ，如果 sentence1 和 sentence2 是相似的，请你返回 true ，否则返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：sentence1 = "My name is Haley", sentence2 = "My Haley"
 * 输出：true
 * 解释：可以往 sentence2 中 "My" 和 "Haley" 之间插入 "name is" ，得到 sentence1 。
 * 示例 2：
 *
 * 输入：sentence1 = "of", sentence2 = "A lot of words"
 * 输出：false
 * 解释：没法往这两个句子中的一个句子只插入一个句子就得到另一个句子。
 * 示例 3：
 *
 * 输入：sentence1 = "Eating right now", sentence2 = "Eating"
 * 输出：true
 * 解释：可以往 sentence2 的结尾插入 "right now" 得到 sentence1 。
 * 示例 4：
 *
 * 输入：sentence1 = "Luky", sentence2 = "Lucccky"
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= sentence1.length, sentence2.length <= 100
 * sentence1 和 sentence2 都只包含大小写英文字母和空格。
 * sentence1 和 sentence2 中的单词都只由单个空格隔开。
 * 通过次数11,947提交次数26,942
 */
public class _1813_SentenceSimilarity3 {

    /**
     * 方法一：双指针
     *
     * 我们将两个句子按照空格分割成两个单词数组 words1 和 words2，假设 words1 和 words2 的长度分别为 m 和 n，
     * 不妨设 m ≥ n，即 往 word2 中间部分插入句子 形成 words1
     *
     * 那么 words2 左边单词（前缀）必然和 words1左边匹配，words2右边单词（后缀）必然和words1右边匹配
     *
     * 我们使用双指针 i 和 j 分别统计 words2 左右两边和words1左右两边匹配的连续单词数量，初始时 i = j = 0。
     * 接下来，我们循环判断 words1[i] 是否等于 words2[i]，是则 i++；
     * 然后我们循环判断 words1[m - 1 - j] 是否等于 words2[n - 1 - j]，是则 j++。
     * 为避免重复统计，（统计右边时 j 延续到左边），第二次循环时限制 j < n - i
     *
     * 循环结束后，如果 i+j == n，说明两个words2前后部分分别分布在words1前后，两句子相似，返回 true，否则返回 false。
     * 如果 i + j < n，则是类似情况 s1 = abc x e g def, s2 = abc e def
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/sentence-similarity-iii/solution/by-lcbin-7d7k/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param sentence1
     * @param sentence2
     * @return
     */
    public boolean areSentencesSimilar(String sentence1, String sentence2) {
        // 分割句子为单词数组
        String[] words1 = sentence1.split(" ");
        String[] words2 = sentence2.split(" ");
        // 在 m > n 时进行后序操作，否则交换
        int m = words1.length, n = words2.length;
        if (m < n) {
            String[] temp = words1;
            words1 = words2;
            words2 = temp;
            int t = m;
            m = n;
            n = t;
        }
        // m > n，往 words2 中间插入句子 得到 words1
        // i、j 分别统计 words2 左边、右边 和 words1 左边、右边 匹配的连续单词数量
        int i = 0, j = 0;
        // 左边连续单词匹配数量，不要越界
        while (i < n && words1[i].equals(words2[i])) i++;
        // 右边连续单词匹配数量，不要延续到左边已统计过的单词上
        while (j < n - i && words1[m - 1 - j].equals(words2[n - 1 - j])) j++;
        // 说明 words2 分割出的左右两部分单词 恰好和 words1 左右两边匹配
        return i + j == n;
    }

    public static void main(String[] args) {
        _1813_SentenceSimilarity3 obj = new _1813_SentenceSimilarity3();
        System.out.println(obj.areSentencesSimilar("URhnaPlQqSx h", "URhnaPlQqSx RpASX h"));
    }
}
