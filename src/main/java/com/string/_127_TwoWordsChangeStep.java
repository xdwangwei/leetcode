package com.string;

import com.common.Pair;

import java.util.*;

/**
 * @author wangwei
 * 2020/4/1 13:20
 * <p>
 * 给定两个单词（beginWord 和 endWord）和一个字典，找到从 beginWord 到 endWord 的最短转换序列的长度。转换需遵循如下规则：
 * <p>
 * 每次转换只能改变一个字母。
 * 转换过程中的中间单词必须是字典中的单词。
 * 说明:
 * <p>
 * 如果不存在这样的转换序列，返回 0。
 * 所有单词具有相同的长度。
 * 所有单词只由小写字母组成。
 * 字典中不存在重复的单词。
 * 你可以假设 beginWord 和 endWord 是非空的，且二者不相同。
 * <p>
 * 输入:
 * beginWord = "hit",
 * endWord = "cog",
 * wordList = ["hot","dot","dog","lot","log","cog"]
 * <p>
 * 输出: 5
 * <p>
 * 解释: 一个最短转换序列是 "hit" -> "hot" -> "dot" -> "dog" -> "cog",
 * 返回它的长度 5。
 * <p>
 * 输入:
 * beginWord = "hit"
 * endWord = "cog"
 * wordList = ["hot","dot","dog","lot","log"]
 * <p>
 * 输出: 0
 * <p>
 * 解释: endWord "cog" 不在字典中，所以无法进行转换。
 */
public class _127_TwoWordsChangeStep {

    /**
     * 链接：https://leetcode-cn.com/problems/word-ladder/solution/dan-ci-jie-long-by-leetcode/
     *
     * 最重要的步骤是找出相邻的节点，也就是只差一个字母的两个单词，即从当前单词一步能得到哪些单词。
     * 为了快速的找到这些相邻节点，我们对给定的 wordList 做一个预处理，将单词中的某个字母用 * 代替
     * 这个预处理帮我们构造了一个单词变换的通用状态。
     *
     * 我们使用一个Map来存储，键：通用状态；值：能得到这个通用状态的所有单词
     * 例如：Dog ----> D*g <---- Dig，Dog 和 Dig 都指向了一个通用状态 D*g。即键D*g对应的值为[Dog,Dig]
     *
     * 这步预处理找出了单词表中所有单词改变某个字母后的通用状态，并帮助我们更方便也更快的找到相邻节点。
     * 否则，对于每个单词我们需要遍历整个字母表查看是否存在一个单词与它相差一个字母，这将花费很多时间。
     * 预处理操作在广度优先搜索之前高效的建立了邻接表。
     *
     * 例如，在广搜时我们需要访问 Dug 的所有邻接点，也就是想知道由Dug能变换到字典中哪些单词
     * 我们可以先生成 Dug 的所有通用状态：以通用状态为键去获取值就可得到结果
     * Dug => *ug
     * Dug => D*g
     * Dug => Du*
     * 第二个变换 D*g 可以同时映射到 Dog 或者 Dig，因为他们都有相同的通用状态。拥有相同的通用状态意味着两个单词只相差一个字母，他们的节点是相连的。
     *
     */

    /**
     * 广度优先遍历(可认为是寻找全局最优解)
     * 1. 对给定的 wordList 做预处理，找出所有的通用状态。将通用状态记录在字典中，键是通用状态，值是所有具有通用状态的单词。
     * 2. 将键值对 <beginWord，1> 放入队列中，1 代表节点的层次。我们需要返回 endWord 的层次也就是从 beginWord 出发的最短距离。
     * 3. 为了防止出现环，使用访问数组记录当前单词已访问过。
     * 4. 当队列中有元素的时候，取出第一个元素，记为 current_word。
     * 5. 找到 current_word 的所有通用状态，并根据这些通用状态得到其对应的单词列表w1，w2，即从currWord能达到给定字典中的list中的w1和w2
     * 6. w1和w2都和 current_word 相连，因此将他们加入到队列中。
     * 7. 对于新获得的所有单词，向队列中加入元素 (word, level + 1) 其中 level 是 current_word 的层次。
     * 8. 最终当你到达期望的单词，对应的层次就是最短变换序列的长度
     * 相当于要从1得到9，发现1直接到达2和3，再操作2得到4和5，再操作3得到6和7，再操作4得到8和9，达到目标
     * 也就是一个广度优先遍历，因此需要借助 [队列] ，且要避免重复遍历
     */
    public static int solution(String beginWord, String endWord, List<String> wordList) {
        // 给定单词列表中不包括endword，直接返回
        if (!wordList.contains(endWord)) return 0;
        // 题目说明，每个单词长度相同
        int len = beginWord.length();
        // 处理给出的单词字典，转换为全部的通用状态及每个通配词映射的单词集合
        HashMap<String, ArrayList<String>> allComboDict = new HashMap<>();
        // lambda表达式遍历，currWord是当前正在遍历的单词
        wordList.forEach(curWord -> {
            // 每个单词能得到len种通配词(每个位置字符都可变为*)
            for (int i = 0; i < len; i++) {
                // 得到通配词
                String comboWord = curWord.substring(0, i) + "*" + curWord.substring(i + 1, len);
                // 从通配字典全集中拿到这个通配词对应的单词集合，如果是空(第一次得到通配词时)就创建一个新的
                ArrayList<String> comboWordList = allComboDict.getOrDefault(comboWord, new ArrayList<>());
                // 把当前这个单词加进去，因此从这个单词得到了这个通配词
                comboWordList.add(curWord);
                // 更新一个通配字典全集中这个通配词对应的单词集合
                allComboDict.put(comboWord, comboWordList);
            }
        });
        // 广度优先遍历队列
        // LinkedList implements Deque extends Queue
        Queue<Pair<String, Integer>> queue = new LinkedList<>();
        // 记录已遍历过的单词,为什么不用List，因为之后判断节点是否已遍历过时，ArrayList的contains方法太低效了，它的底层是数组
        // ArrayList<String> hasVistedList = new ArrayList<>();
        HashMap<String, Boolean> hasVistedList = new HashMap<>();
        // 开始词作为第一个节点加入队列,深度level是1，标记其已访问
        queue.add(new Pair<>(beginWord, 1));
        // hasVistedList.add(beginWord);
        hasVistedList.put(beginWord, true);
        // 广度优先遍历，逐个取出队列中元素进行操作
        while (!queue.isEmpty()) {
            // 队列第一个节点
            Pair<String, Integer> node = queue.remove();
            // 当前节点对应的<单词，层级>
            String currWord = node.getKey();
            int level = node.getValue();
            for (int i = 0; i < len; i++) {
                // 从当前单词，得到len个通配词
                String currComboWord = currWord.substring(0, i) + "*" + currWord.substring(i + 1, len);
                // 拿到这个通配词映射的单词集合(也就是从当前单词一次转换能得到哪些单词)
                ArrayList<String> currComboWordList = allComboDict.getOrDefault(currComboWord, new ArrayList<>());
                // 遍历其中是否包含目标单词
                for (String word : currComboWordList) {
                    // 包含目标单词，说明当前单词能一次转换到目标单词，经历的步骤数是当前单词的层级 + 1
                    if (word.equals(endWord))
                        return level + 1;
                    // 否则，当前单词能得到这个单词，如果它还没被访问过
                    // if (!hasVistedList.contains(word)){
                    // HashMap.containsKey方法效率远高于ArrayList.contains
                    if (!hasVistedList.containsKey(word)){
                        // 把这个单词加入到队列中
                        queue.add(new Pair<>(word, level + 1));
                        // 标记它为已访问
                        // hasVistedList.add(word);
                        hasVistedList.put(word, true);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 双向广度优先遍历
     * 根据给定字典构造的图可能会很大，而广度优先搜索的搜索空间大小依赖于每层节点的分支数量。
     * 假如每个节点的分支数量相同，搜索空间会随着层数的增长指数级的增加。
     * 考虑一个简单的二叉树，每一层都是满二叉树的扩展，节点的数量会以 2 为底数呈指数增长。
     *
     * 如果使用两个同时进行的广搜可以有效地减少搜索空间。
     * 一边从 beginWord 开始，另一边从 endWord 开始。我们每次从两边各扩展一个节点，
     * 当发现某一时刻两边都访问了某一顶点时就停止搜索。
     * 这就是双向广度优先搜索，它可以可观地减少搜索空间大小，从而降低时间和空间复杂度。
     *
     * 搜索时间会被缩小一半，因为两个搜索会在中间某处相遇。
     *
     * 1. 算法与之前描述的标准广搜方法相类似。
     * 2. 唯一的不同是我们从两个节点同时开始搜索，同时搜索的结束条件也有所变化。
     * 3. 我们现在有两个访问数组，分别记录从对应的起点是否已经访问了该节点。
     * 4. 如果我们发现一个节点被两个搜索同时访问，就结束搜索过程。因为我们找到了双向搜索的交点。过程如同从中间相遇而不是沿着搜索路径一直走。
     * 5. 双向搜索的[结束条件]是找到一个单词被两边搜索都访问过了。
     * 6. 最短变换序列的长度就是中间节点在两边的层次之和。因此，我们可以在访问数组中[记录节点的层次]，而不单单仅记录节点是否被访问。
     *
     */
    public int solution2(String beginWord, String endWord, List<String> wordList){
        // 给定单词列表中不包括endword，直接返回
        if (!wordList.contains(endWord)) return 0;
        // 题目说明，每个单词长度相同
        int len = beginWord.length();
        // 处理给出的单词字典，转换为全部的通用状态及每个通配词映射的单词集合
        HashMap<String, ArrayList<String>> allComboDict = new HashMap<>();
        // lambda表达式遍历，currWord是当前正在遍历的单词
        wordList.forEach(curWord -> {
            // 每个单词能得到len种通配词(每个位置字符都可变为*)
            for (int i = 0; i < len; i++) {
                // 得到通配词
                String comboWord = curWord.substring(0, i) + "*" + curWord.substring(i + 1, len);
                // 从通配字典全集中拿到这个通配词对应的单词集合，如果是空(第一次得到通配词时)就创建一个新的
                ArrayList<String> comboWordList = allComboDict.getOrDefault(comboWord, new ArrayList<>());
                // 把当前这个单词加进去，因此从这个单词得到了这个通配词
                comboWordList.add(curWord);
                // 更新一个通配字典全集中这个通配词对应的单词集合
                allComboDict.put(comboWord, comboWordList);
            }
        });
        // 广度优先遍历队列
        // 正向bfs
        Queue<Pair<String, Integer>> postiveQueue = new LinkedList<>();
        // 开始词作为第一个节点加入正向队列,深度level是1，标记其已访问
        postiveQueue.add(new Pair<>(beginWord, 1));
        // 正向bfs已访问的单词
        HashMap<String, Integer> positiveVistedMap = new HashMap<>();
        positiveVistedMap.put(beginWord, 1);

        // 反向bfs
        Queue<Pair<String, Integer>> oppositeQueue = new LinkedList<>();
        // 结束词作为第一个节点加入反向队列,深度level是1，标记其已访问
        oppositeQueue.add(new Pair<>(endWord, 1));
        // 反向bfs已访问的单词
        HashMap<String, Integer> oppositeVistedMap = new HashMap<>();
        oppositeVistedMap.put(endWord, 1);


        // 双向广度优先遍历，逐个取出队列中元素进行操作
        while (!postiveQueue.isEmpty() && !oppositeQueue.isEmpty()){
            // 取出正向队列第一个节点进行遍历操作
            int res = BFSOnceVisit(len, allComboDict,
                    postiveQueue, positiveVistedMap, oppositeVistedMap);
            // 判断是否找到
            if (res > -1)
                return res;

            // 取出反向队列第一个节点进行遍历操作，注意此时参数顺序调整
            res = BFSOnceVisit(len, allComboDict,
                    oppositeQueue, oppositeVistedMap, positiveVistedMap);
            // 判断是否找到
            if (res > -1)
                return res;
        }
        return 0;
    }

    // 取出队列首节点，进行一次bfs
    private int BFSOnceVisit(int len,
                             // 全部通配词字典
                             HashMap<String, ArrayList<String>> allComboDict,
                             // 正向dfs队列
                             Queue<Pair<String, Integer>> positiveQueue,
                             // 正向开始，遍历过的节点集合
                             HashMap<String, Integer> positiveVisitedMap,
                             // 反向开始，遍历过的节点集合
                             HashMap<String, Integer> oppositeVisitedMap) {
        // 队列第一个节点
        Pair<String, Integer> node = positiveQueue.remove();
        // 当前节点对应的<单词，层级>
        String currWord = node.getKey();
        int level = node.getValue();
        for (int i = 0; i < len; i++) {
            // 从当前单词，得到len个通配词
            String currComboWord = currWord.substring(0, i) + "*" + currWord.substring(i + 1, len);
            // 拿到这个通配词映射的单词集合(也就是从当前单词一次转换能得到哪些单词)
            ArrayList<String> currComboWordList = allComboDict.getOrDefault(currComboWord, new ArrayList<>());
            // 遍历其中是否包含目标单词
            for (String word : currComboWordList) {
                // 双向bfs，搜索成功的条件是，这个单词正反向搜索都访问到
                if (oppositeVisitedMap.containsKey(word))
                    // 总共经历的步骤是：正向步骤+反向步骤
                    return level + oppositeVisitedMap.get(word);
                // 否则，当前单词能得到这个单词，如果它还没被访问过
                if (!positiveVisitedMap.containsKey(word)){
                    // 把这个单词加入到队列中
                    positiveQueue.add(new Pair<>(word, level + 1));
                    // 标记它为已访问
                    positiveVisitedMap.put(word, level + 1);
                }
            }
        }
        return -1;
    }

}
