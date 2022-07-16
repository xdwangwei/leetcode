package com.common;

/**
 * @author wangwei
 * @date 2022/4/20 19:29
 * @description: _208_ImplementTriePrefixTree
 *
 * 208. 实现 Trie (前缀树)
 * Trie（发音类似 "try"）或者说 前缀树 是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。这一数据结构有相当多的应用情景，例如自动补完和拼写检查。
 *
 * 请你实现 Trie 类：
 *
 * Trie() 初始化前缀树对象。
 * void insert(String word) 向前缀树中插入字符串 word 。
 * boolean search(String word) 如果字符串 word 在前缀树中，返回 true（即，在检索之前已经插入）；否则，返回 false 。
 * boolean startsWith(String prefix) 如果之前已经插入的字符串 word 的前缀之一为 prefix ，返回 true ；否则，返回 false 。
 *
 *
 * 示例：
 *
 * 输入
 * ["Trie", "insert", "search", "search", "startsWith", "insert", "search"]
 * [[], ["apple"], ["apple"], ["app"], ["app"], ["app"], ["app"]]
 * 输出
 * [null, null, true, false, true, null, true]
 *
 * 解释
 * Trie trie = new Trie();
 * trie.insert("apple");
 * trie.search("apple");   // 返回 True
 * trie.search("app");     // 返回 False
 * trie.startsWith("app"); // 返回 True
 * trie.insert("app");
 * trie.search("app");     // 返回 True
 *
 *
 * 提示：
 *
 * 1 <= word.length, prefix.length <= 2000
 * word 和 prefix 仅由小写英文字母组成
 * insert、search 和 startsWith 调用次数 总计 不超过 3 * 104 次
 */
public class TriePrefixTree {

    /**
     * 字典树
     *
     * Trie，又称前缀树或字典树，是一棵有根树，其每个节点包含以下字段：
     *
     * 指向子节点的指针数组 children。对于本题而言，数组长度为 26，即小写英文字母的数量。
     *      此时 children[0] 对应小写字母 a，children[1] 对应小写字母 b，…，children[25] 对应小写字母 z。
     * 布尔字段 isEnd，表示该节点是否为字符串的结尾。因为将一个单词拆组成1条路径，形成了多个节点，然后只有最后一个字符所在节点表标这是单词的末尾
     *  并且，并不是只有叶子节点的isEnd字段是True，比如 the，和 them，m节点isEnd是true代表them是一个插入进来的单词，e节点的isEnd也是true，因为the也是一个插入进来的单词
     *
     * 我们可以看到TrieNode结点中并没有直接保存字符值的数据成员，那它是怎么保存字符的呢？
     * 时字母映射表next 的妙用就体现了，它实际上是把一个单词拆成了一条路径
     * 比如 hello，在字典树中就是 root.children[h].children[e].children[l].children[l].children[o]
     * 这样一来，查询一个单词是否存在，只需要判断这个单词对应的这个完整路径是否存在，也就是这个理论上的路径中不存在null
     * 如果再来一个 helle，字典树中就是 root.children[h].children[e].children[l].children[l].children[o,e]前面都一样，最后一个节点的o和e位置不为kong，也就是children数组5和18不为位置空
     *
     * 所以一个字典树的结构是固定的，root->children[26]，每个children又有26个孩子
     * 这个结构 使得插入、查询全词、查询前缀的时间复杂度与已插入的单词数目无关，这是前缀树（Trie）解法优于有序集合解法的关键。
     *
     *
     * 这TrieNode[26] children中保存了对当前结点而言下一个可能出现的所有字符的链接，因此我们可以通过一个父结点来预知它所有子结点的值：
     *
     * 作者：huwt
     * 链接：https://leetcode-cn.com/problems/implement-trie-prefix-tree/solution/trie-tree-de-shi-xian-gua-he-chu-xue-zhe-by-huwt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 操作分析：
     *
     * 插入字符串
     *
     * 我们从字典树的根开始，插入字符串。顺序遍历单词每个字符，对于当前字符对应的子节点(children[c-'a'])，有两种情况：
     *      子节点存在。沿着指针移动到子节点，继续处理下一个字符。
     *      子节点不存在。创建一个新的子节点(即完成在 children 数组的对应位置的记录)，然后沿着指针移动到子节点，继续搜索下一个字符。
     * 重复以上步骤，直到处理字符串的最后一个字符，然后【将当前节点标记为字符串的结尾】，也就是最后一个字符对应的节点的isEnd属性设置为 true。
     *
     * 查找前缀
     *
     * 我们从字典树的根开始，查找前缀。顺序遍历字符串的每个字符，对于当前字符对应的子节点，有两种情况：
     *
     *      子节点存在。沿着指针移动到子节点，继续搜索下一个字符。
     *      子节点不存在。说明字典树中不包含该前缀，返回空指针。
     * 重复以上步骤，直到返回空指针或搜索完前缀的最后一个字符。
     *
     * 若搜索到了前缀的末尾，就说明字典树中存在该前缀。
     * 此外，若前缀末尾对应节点的 isEnd 为真，则说明字典树中存在该字符串(单词)。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/implement-trie-prefix-tree/solution/shi-xian-trie-qian-zhui-shu-by-leetcode-ti500/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class Trie {

        // 这样写相当于对象自身this作为树根，反正节点不保存值所以完全可行
        // 是否是有效单词结尾
        private boolean isEnd;
        // 子节点列表，即它的后一个位置上所有出现的字符，
        private Trie[] children;

        public Trie() {
            children = new Trie[26];
        }

        /**
         * 按顺序遍历word的每个字符，找到它在树中的对应位置(即它的前一个字符对应节点的孩子列表中[当前字符-'a'位置])
         * 如果这个位置节点存在，即与此前某个单词具有这部分共同前缀，指针移动到这个位置，进行后续遍历
         * 这个位置节点不存在，那就创建一个，代表上一个字符后面出现新分支，然后指针移动到这个位置，继续后序遍历
         * @param word
         */
        public void insert(String word) {
            // 当前对象就是树根
           Trie node = this;
            // 单词的全部字符组成一条链路
            for (int i = 0; i < word.length(); ++i) {
                // 当前字符在上一个字符节点的所有孩子中的位置
                int index = word.charAt(i) - 'a';
                // 如果不存在，则创建
                if (node.children[index] == null) {
                    node.children[index] = new Trie();
                }
                // 移动到当前位置，进行后续遍历
                node = node.children[index];
            }
            // 最后一个字符对应的节点，表示是一个有效单词的结尾，其他位置都是当前单词的某个前缀
            node.isEnd = true;
        }

        /**
         * 判断字典树中是否存在单词word
         * 相当于 判断字典树中是否存在前缀word，并且最后一个字符 d 对应的节点的isEnd属性为真，表示确实有这样一个单词，而不只是个中间节点(某个前缀)
         * @param word
         * @return
         */
        public boolean search(String word) {
            Trie node = searchPrefix(word);
            return node != null && node.isEnd;
        }

        /**
         * 判断字典树中是否存在前缀prefix
         * 调用工具方法即可
         * @param prefix
         * @return
         */
        public boolean startsWith(String prefix) {
            return searchPrefix(prefix) != null;
        }

        /**
         * 工具方法，用于上面两个方法重用
         * 判断字典树中是否存在这样一个前缀，并返回最后一个字符对应的树中节点
         * 也就是判断这个串prefix的字符组成的路径是否在字典树中完整存在
         * @param prefix
         * @return
         */
        public Trie searchPrefix(String prefix) {
            // 从根节点出发
            Trie node = this;
            for (int i = 0; i < prefix.length(); ++i) {
                // 找到每个字符在每一层的对应位置
                int index = prefix.charAt(i) - 'a';
                // 若此节点为null，说明这条路径不存在
                if (node.children[index] == null) {
                    return null;
                }
                // 指针移动
                node = node.children[index];
            }
            return node;
        }
    }
}
