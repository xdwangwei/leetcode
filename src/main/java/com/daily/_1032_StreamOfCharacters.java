package com.daily;

/**
 * @author wangwei
 * @date 2023/3/24 10:59
 * @description: _1032_StreamOfCharacters
 *
 * 1032. 字符流
 * 设计一个算法：接收一个字符流，并检查这些字符的后缀是否是字符串数组 words 中的一个字符串。
 *
 * 例如，words = ["abc", "xyz"] 且字符流中逐个依次加入 4 个字符 'a'、'x'、'y' 和 'z' ，你所设计的算法应当可以检测到 "axyz" 的后缀 "xyz" 与 words 中的字符串 "xyz" 匹配。
 *
 * 按下述要求实现 StreamChecker 类：
 *
 * StreamChecker(String[] words) ：构造函数，用字符串数组 words 初始化数据结构。
 * boolean query(char letter)：从字符流中接收一个新字符，如果字符流中的任一非空后缀能匹配 words 中的某一字符串，返回 true ；否则，返回 false。
 *
 *
 * 示例：
 *
 * 输入：
 * ["StreamChecker", "query", "query", "query", "query", "query", "query", "query", "query", "query", "query", "query", "query"]
 * [[["cd", "f", "kl"]], ["a"], ["b"], ["c"], ["d"], ["e"], ["f"], ["g"], ["h"], ["i"], ["j"], ["k"], ["l"]]
 * 输出：
 * [null, false, false, false, true, false, true, false, false, false, false, false, true]
 *
 * 解释：
 * StreamChecker streamChecker = new StreamChecker(["cd", "f", "kl"]);
 * streamChecker.query("a"); // 返回 False
 * streamChecker.query("b"); // 返回 False
 * streamChecker.query("c"); // 返回n False
 * streamChecker.query("d"); // 返回 True ，因为 'cd' 在 words 中
 * streamChecker.query("e"); // 返回 False
 * streamChecker.query("f"); // 返回 True ，因为 'f' 在 words 中
 * streamChecker.query("g"); // 返回 False
 * streamChecker.query("h"); // 返回 False
 * streamChecker.query("i"); // 返回 False
 * streamChecker.query("j"); // 返回 False
 * streamChecker.query("k"); // 返回 False
 * streamChecker.query("l"); // 返回 True ，因为 'kl' 在 words 中
 *
 *
 * 提示：
 *
 * 1 <= words.length <= 2000
 * 1 <= words[i].length <= 200
 * words[i] 由小写英文字母组成
 * letter 是一个小写英文字母
 * 最多调用查询 4 * 104 次
 * 通过次数9,184提交次数17,230
 */
public class _1032_StreamOfCharacters {

    /**
     * 方法一：前缀树
     *
     * 我们可以根据初始化时的字符串数组 words 构建前缀树，前缀树的每个节点包含两个属性：
     *
     * children：指向 26 个字母的指针数组，用于存储当前节点的子节点。
     * is_end：标记当前节点是否为某个字符串(单词)的结尾。
     *
     * 由于题目匹配的是后缀，因此在构造函数中，我们遍历字符串数组 words，
     * 对于每个单词 w，我们将其反转后（反向遍历），逐个字符插入到前缀树中，插入结束后，将当前节点的 is_end 标记为 true。
     *
     * 在 query 函数中，我们将当前字符 c 加入到字符流中，然后【从后往前】遍历字符流，
     * 对于每个字符 c，我们在前缀树中查找是否存在以 c 为结尾的字符串，如果存在，返回 true，否则返回 false。
     *
     * 假设当前数据流对应的字符串为 s，长度为 n），从 s 的尾部开始在 Trie 中进行检索（即从 s[n−1] 开始往回找）。
     * 若在某个位置 idx 时匹配成功（node[idx].is_end=true），意味着 s[idx...(n−1)] 的翻转子串在字典树中，
     * 同时我们又是将每个 words[i] 进行倒序插入，即意味着 s[idx...(n−1)] 的正向子串在 words 中，即满足 s 的某个后缀出现在 words 中。
     *
     * 注意到 words 中的字符串长度不超过 200，因此查询时最多只需要遍历 200 个字符。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/stream-of-characters/solution/python3javacgo-yi-ti-yi-jie-qian-zhui-sh-79kg/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */


    class StreamChecker {
        // 字典树树根
        private Trie root = new Trie();
        // 当前数据流字符串
        private StringBuilder sb = new StringBuilder();

        // 用所有word构建后缀字典树
        public StreamChecker(String[] words) {
            for (String word : words) {
                insert(word);
            }
        }

        // 查询，从 字符流末尾开始倒序往前遍历，并在字典树中查询是否存在 s[n-1....idx] 的单词末尾
        public boolean query(char letter) {
            // 加入当前字符
            sb.append(letter);
            int n = sb.length();
            // 从树根开始搜索
            Trie cur = root;
            // 倒序遍历字符流，由于单词长度最多为200，所以这里最多遍历最后200个字符
            for (int i = n - 1; i >= Math.max(0, n - 200); --i) {
                // 当前字符
                int idx = sb.charAt(i) - 'a';
                // 字典树中不存在对应路径节点，直接返回false，没法继续下去了
                if (cur.children[idx] == null) {
                    return false;
                }
                // 如果当前是某个单词的末尾，说明当前后缀 s[idx...n-1] 匹配到了 words中某个单词
                if (cur.children[idx].isEnd) {
                    return true;
                }
                // 进入对应子节点
                cur = cur.children[idx];
            }
            // 最后200个字符遍历完了，肯定不存在 s[:n-1]（后缀） 对应的单词，返回false
            return false;
        }


        /**
         * 构建后缀字典树
         * @param word
         */
        private void insert(String word) {
            // 从树根开始
            Trie cur = root;
            // 倒序遍历单词字符
            for (int i = word.length() - 1; i >= 0; --i) {
                int idx = word.charAt(i) - 'a';
                // 根据当前字符，跳到对应子节点，若不存在就先创建
                if (cur.children[idx] == null) {
                    cur.children[idx] = new Trie();
                }
                cur = cur.children[idx];
            }
            // 最后一个节点（倒序遍历到单词第一个字符），设置为单词末尾
            cur.isEnd = true;
        }

        /**
         * 字典树
         */
        class Trie {
            // 当前节点是否是某个单词的结尾
            private boolean isEnd;
            // 子节点
            private Trie[] children;

            public Trie() {
                // 初始化
                isEnd = false;
                children = new Trie[26];
            }
        }
    }
}
