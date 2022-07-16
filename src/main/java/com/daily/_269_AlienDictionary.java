package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/5/31 9:48
 * @description: _114_AlienDictionary
 *
 *
 * 269. 外星文字典    会员题目
 * 现有一种使用英语字母的外星文语言，这门语言的字母顺序与英语顺序不同。
 *
 * 给定一个字符串列表 words ，作为这门语言的词典，words 中的字符串已经 按这门新语言的字母顺序进行了排序 。
 *
 * 请你根据该词典还原出此语言中已知的字母顺序，并 按字母递增顺序 排列。若不存在合法字母顺序，返回 "" 。若存在多种可能的合法字母顺序，返回其中 任意一种 顺序即可。
 *
 * 字符串 s 字典顺序小于 字符串 t 有两种情况：
 *
 * 在第一个不同字母处，如果 s 中的字母在这门外星语言的字母顺序中位于 t 中字母之前，那么 s 的字典顺序小于 t 。
 * 如果前面 min(s.length, t.length) 字母都相同，那么 s.length < t.length 时，s 的字典顺序也小于 t 。
 *
 *
 * 示例 1：
 *
 * 输入：words = ["wrt","wrf","er","ett","rftt"]
 * 输出："wertf"
 * 示例 2：
 *
 * 输入：words = ["z","x"]
 * 输出："zx"
 * 示例 3：
 *
 * 输入：words = ["z","x","z"]
 * 输出：""
 * 解释：不存在合法字母顺序，因此返回 "" 。
 *
 *
 * 提示：
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length <= 100
 * words[i] 仅由小写英文字母组成
 */
public class _269_AlienDictionary {

    /**
     * 拓扑排序
     *
     * 为了方便，我们称 words 为 ws，同时将两个字符串 a 和 b 之间的字典序关系简称为「关系」。
     *
     *
     * 首先从前往后处理每个 ws[i]，利用 ws 数组本身已按字典序排序，然后通过 wws[i] 与 ws[i+1] 的关系，来构建字符之间的关系。
     *
     * 具体的，当我们明确字符 c1 比 c2 字典序要小，可以建立从 c1 到 c2 的有向边，增加 c2 的入度。
     *
     * 建图完成后：
     *
     * 首先计算每个节点的入度，只有入度为 0 的节点可能是拓扑排序中最前面的节点。
     * 当一个节点加入拓扑排序之后，该节点的所有相邻节点的入度都减 1，表示相邻节点少了一条入边。
     * 当一个节点的入度变成 0，则该节点前面的节点都已经加入拓扑排序，该节点也可以加入拓扑排序。
     *
     * 具体做法是，使用队列存储可以加入拓扑排序的节点，初始时将所有入度为 0 的节点入队列。
     * 每次将一个节点出队列并加入拓扑排序中，然后将该节点的所有相邻节点的入度都减 1，
     * 如果一个相邻节点的入度变成 0，则将该相邻节点入队列。重复上述操作，直到队列为空时，广度优先搜索结束。
     *
     * [注意]
     * 如果有向图中无环，则每个节点都将加入拓扑排序，因此拓扑排序的长度等于字典中的字母个数。
     * 如果有向图中有环，则环中的节点不会加入拓扑排序，因此拓扑排序的长度小于字典中的字母个数。
     * 广度优先搜索结束时，判断拓扑排序的长度是否等于字典中的字母个数，即可判断有向图中是否有环。
     *
     * 如果拓扑排序的长度等于字典中的字母个数，则拓扑排序包含字典中的所有字母，返回拓扑排序；
     *
     * 如果拓扑排序的长度小于字典中的字母个数，则有向图中有环，不存在拓扑排序。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/Jf1JuT/solution/wai-xing-wen-zi-dian-by-leetcode-solutio-to66/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/Jf1JuT/solution/by-ac_oier-4xmv/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param words
     * @return
     */
    public String alienOrder(String[] words) {
        // 每个字符的邻接字符
        Map<Character, List<Character>> adjMap = new HashMap<>();
        // 每个字符的入度
        Map<Character, Integer> degMap = new HashMap<>();
        for (String word : words) {
            // 初始化每个字符的邻接表
            for (char c : word.toCharArray()) {
                adjMap.putIfAbsent(c, new ArrayList<>());
            }
        }
        // ws[i] 和 ws[i+1] 符合字典序，能够得到一对字符的依赖关系
        // 这里有坑，题目说是 words里面是已经按给定字典序排好的单词，但实际测试用例存在如["abc", "ab"]等不符合字典序的单词
        // 因此，若遇到ws[i] 和 ws[i+1] 不符合字典序，直接返回 “”
        for (int i = 0; i < words.length - 1; ++i) {
            // 从ws[i]和ws[i+1]符合字典序，得到c->d，若不符合，直接返回
            if (!helper(words[i], words[i + 1], adjMap, degMap)) {
                return "";
            }
        }
        // 保存结果，即顺序保留所有入度为0的节点
        StringBuilder builder = new StringBuilder();
        // 拓扑排序
        Deque<Character> queue = new ArrayDeque<>();
        // 先将所有入度为0的节点入队列，入度为0即不存在于degMap
        for (Character c : adjMap.keySet()) {
            if (!degMap.containsKey(c)) {
                queue.offer(c);
            }
        }
        // 拓扑排序
        while (!queue.isEmpty()) {
            // 每次出来一个入度为0的节点
            Character c = queue.poll();
            // 保存
            builder.append(c);
            for (Character d : adjMap.get(c)) {
                // 对它所有邻接点进行入度减1操作
                degMap.put(d, degMap.get(d) - 1);
                // 若出现入度为0，继续入队列
                if (degMap.get(d) == 0) {
                    queue.offer(d);
                }
            }
        }
        // 如果图中有环，环中节点是不存满足入度为0的，也就是最终答案肯定未包含全部字符，这样的话 我们并不能得到一个合理的字典序
        return builder.length() == adjMap.size() ? builder.toString() : "";
    }


    /**
     * 假设 w1 和 w2 满足字典序，由此得到 c -> d，更新d入度；若发现 [abc, ab]等违背字典序情况，返回false
     * @param word1
     * @param word2
     * @param adjMap
     * @param degMap
     * @return
     */
    private boolean helper(String word1, String word2, Map<Character, List<Character>> adjMap, Map<Character, Integer> degMap) {
        int m = word1.length(), n = word2.length();
        int len = Math.min(m, n);
        int i = 0;
        // 注意字典序比较，一个位置不同，就结束了
        while (i < len) {
            char c = word1.charAt(i), d = word2.charAt(i);
            // 说明字典序c<d，得到 c->d的依赖关系
            if (c != d) {
                // c的邻接表加入d
                adjMap.get(c).add(d);
                // d的入度加1
                degMap.put(d, degMap.getOrDefault(d, 0) + 1);
                return true;
            }
            i++;
        }
        // 如果while走完了，说明 w1 和 w2 前len部分完全匹配，此时 如果 m > n就违背字典序原则，返回false
        return m <= n;
    }
}
