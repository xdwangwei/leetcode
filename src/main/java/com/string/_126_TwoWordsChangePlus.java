package com.string;

import java.util.*;

/**
 * @author wangwei
 * 2020/4/1 20:28
 * <p>
 * 给定两个单词（beginWord 和 endWord）和一个字典 wordList，找出所有从 beginWord 到 endWord 的最短转换序列。转换需遵循如下规则：
 * <p>
 * 每次转换只能改变一个字母。
 * 转换过程中的中间单词必须是字典中的单词。
 * 说明:
 * <p>
 * 如果不存在这样的转换序列，返回一个空列表。
 * 所有单词具有相同的长度。
 * 所有单词只由小写字母组成。
 * 字典中不存在重复的单词。
 * 你可以假设 beginWord 和 endWord 是非空的，且二者不相同。
 * 示例 1:
 * <p>
 * 输入:
 * beginWord = "hit",
 * endWord = "cog",
 * wordList = ["hot","dot","dog","lot","log","cog"]
 * <p>
 * 输出:
 * [
 * ["hit","hot","dot","dog","cog"],
 *   ["hit","hot","lot","log","cog"]
 * ]
 * 示例 2:
 * <p>
 * 输入:
 * beginWord = "hit"
 * endWord = "cog"
 * wordList = ["hot","dot","dog","lot","log"]
 * <p>
 * 输出: []
 * <p>
 * 解释: endWord "cog" 不在字典中，所以不存在符合要求的转换序列。
 * <p>
 * 链接：https://leetcode-cn.com/problems/word-ladder-ii
 */
public class _126_TwoWordsChangePlus {

    /**
     * 广度优先遍历，其实就是由一个beginWord扩展成一棵树
     * 第一层 beginWord，
     * 第二层 beginWord一步转换得到的单词集合
     * 第三层 beginWord两次转换得到的单词集合
     * 第四层，beginWord三次转换得到的单词集合
     * 直到某一层出现了目标单词
     *
     * @param beginWord
     * @param endWord
     * @param wordList
     * @return
     */

    public List<List<String>> solution1(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> resList = new ArrayList<>();
        // Set.contains()方法时间复杂度 O(1)
        HashSet<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return resList;
        // 每个单词的长度
        int len = beginWord.length();
        // 因为要返回路径，所以广度优先遍历队列不能只保存下一个要遍历的节点，而要将其之前的路径也保存起来
        // 如 第一层 red
        // 第二层 {red, ted},{red, rex}
        // 第三层 {red, ted, tad}, {red, ted, tex}, {red, rex, tex}
        // 第四层 {red, ted, tad, tax}, {red, ted, tex, tax}, {red, rex, tex, tax}
        // 每次取出队列第一个元素，是一个列表，拿出它最后一个单词进行向下扩展，得到下一层的单词集合
        // 并组合成新的队列加入queue
        // 需要注意的是，遍历过程中既要标记已遍历过的节点，还要保证多个符合的路径都要能找到
        // 也就是在遍历每一层的全部节点元素时，不能当前节点满足要求的就把它标记为已遍历，
        // 导致同一层之后的节点元素再遍历时因为它已被标记而放弃了此条路径，
        // 所以遍历每一层时需要单独记录当前层访问了哪些节点，直到当前层的节点全访问过，再把标记的节点统一合并到全部的标记集合
        Queue<List<String>> queue = new LinkedList<>();
        // 第一层
        queue.add(Arrays.asList(beginWord));
        // 全局已访问节点集合
        Set<String> visited = new HashSet<>();
        // beginWord已访问
        visited.add(beginWord);
        // 是否需要继续向下扩展，如果当前层已出现目标单词就停止，再往下扩展即便也能扩展到目标，路径也会更长
        boolean flag = false;
        // bfs
        while (!queue.isEmpty() && !flag) {
            // 当前队列的大小，就是上一层的元素个数
            // 每次都得到队列大小size，然后进行size次循环，循环中进行queue.remove(),正好把一层的元素全处理完
            // 在此过程中新加入队列的元素就都是下一层元素，所以下次得到的queue.size就又是下一层的元素个数
            int size = queue.size();
            // 记录当前层元素扩展时，遍历到了哪些单词
            Set<String> curLayerVisited = new HashSet<>();
            // 按顺序逐个对上一层的元素进行取出并扩展
            for (int j = 0; j < size; j++) {
                // 取出队列一个元素(路径)，拿出最后一个单词，向下扩展，得到新一层
                List<String> pathList = queue.remove();
                // 得到这个单词能扩展出哪些新的单词
                List<String> nearWords = getNearWordList(pathList.get(pathList.size() - 1), wordSet);
                // 当前路径最后一个单词可以转换成这些单词
                for (String nearWord : nearWords) {
                    // 只考虑没有被访问过的
                    if (!visited.contains(nearWord)) {
                        // 当前路径和它结合，组合成下一层的一个元素
                        ArrayList<String> newPath = new ArrayList<>(pathList);
                        newPath.add(nearWord);
                        if (nearWord.equals(endWord)) {
                            // 新一层出现目标单词，截止到这一层的全部可能结果，不需要再向下扩展，
                            flag = true;
                            // 出现一个目标路径，加入结果集
                            resList.add(newPath);
                        }
                        // 记录当前层遍历到的节点
                        curLayerVisited.add(nearWord);
                        // 形成的下一层元素加入队列
                        queue.add(newPath);
                    }
                }
            }
            // 合并当前层遍历到的节点 到 全局遍历过的节点集合中
            visited.addAll(curLayerVisited);
        }
        return resList;
    }

    /**
     * 从给定的单词集合中，找到 与目标单词只有一个字符不同 的所有单词
     * 把给定单词每一位置的字符都用a-z逐个替换，每次形成新的单词就去看字典集里面是否包含这个新的单词
     * 如果包含，说明它就是满足要求的单词之一
     *
     * @param word
     * @param wordsSet
     * @return
     */
    private List<String> getNearWordList(String word, Set<String> wordsSet) {
        ArrayList<String> resList = new ArrayList<>();
        char[] chars = word.toCharArray();
        for (int i = 0; i < word.length(); i++) {
            char old = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == old)
                    continue;
                chars[i] = c;
                String newWord = new String(chars);
                if (wordsSet.contains(newWord))
                    resList.add(newWord);
            }
            // 把单词复原，下一次改变下一位置字符
            chars[i] = old;
        }
        return resList;
    }

    /**
     * 双向BFS+DFS
     * 方法一边构造搜索树边进行广度优先遍历，效率较低
     * 利用双向BFS构建出每个单词可达的下层单词集，之后根据该关系利用DFS构建路径，输出符合条件的路径即可。
     * <p>
     * 双向BFS构建搜索树，当两个方向遇到了共同的节点，就是我们的最短路径了。
     * 至于每次从哪个方向正向扩展，我们可以每次选择需要扩展的节点数少的方向进行扩展。
     * <p>
     * 例如，一开始需要向下扩展的个数是 1 个，需要向上扩展的个数是 1 个。个数相等，我们就向下扩展。然后需要向下扩展的个数就变成了 4 个，而需要向上扩展的个数是 1 个，所以此时我们向上扩展。接着，需要向上扩展的个数变成了 6 个，需要向下扩展的个数是 4 个，我们就向下扩展......直到相遇。
     * <p>
     * 双向扩展的好处，我们粗略的估计一下时间复杂度。
     * 假设 beginword 和 endword 之间的距离是 d。每个节点可以扩展出 k 个节点。
     * 那么正常的时间复杂就是 k^d
     * 双向搜索的时间复杂度就是 k^{d/2} + k^{d/2}
     * <p>
     * 作者：windliang
     * 链接：https://leetcode-cn.com/problems/word-ladder-ii/solution/xiang-xi-tong-su-de-si-lu-fen-xi-duo-jie-fa-by-3-3/
     *
     * @param beginWord
     * @param endWord
     * @param wordList
     * @return
     */
    public List<List<String>> solution2(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> resList = new ArrayList<>();
        // Set.contains()方法时间复杂度 O(1)
        HashSet<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return resList;
        // 正向DFS
        Set<String> positiveSet = new HashSet<>();
        // 加入起点
        positiveSet.add(beginWord);
        //反向DFS
        Set<String> oppositeSet = new HashSet<>();
        // 加入终点
        oppositeSet.add(endWord);
        // 构造整个映射树
        HashMap<String, List<String>> mapTree = new HashMap<>();
        // buildMapTree只有在找到最短路径时(从上往下，从下往上相遇)才会返回true
        if (buildMapTree(mapTree, true, wordSet, positiveSet, oppositeSet)) {
            // dfs获得结果
            dfs(mapTree, resList, beginWord, endWord, new ArrayList<>());
        }
        return resList;
    }

    /**
     * 我们总会选择需要扩展的节点数少的方向作为正方形进行扩展，所以当正向需要扩展的节点集空，直接返回
     * 或者，正向扩展的出下一层节点中的某个节点已出现在反向已访问集合中，搜索树构建完毕
     *
     * @param wordsSet
     * @param positiveSet
     * @param oppositeSet
     * @param direction
     * @return
     */
    private boolean buildMapTree(HashMap<String, List<String>> mapTree, boolean direction, Set<String> wordsSet, Set<String> positiveSet, Set<String> oppositeSet) {
        if (positiveSet.size() == 0)
            return false;
        // 调整扩展方向
        if (oppositeSet.size() < positiveSet.size())
            return buildMapTree(mapTree, !direction, wordsSet, oppositeSet, positiveSet);
        // 从全部集合中移除正向已访问过的节点
        wordsSet.removeAll(positiveSet);
        // 映射树是否构建完毕
        boolean isOK = false;
        // 记录本层扩展出的下一层全部节点
        Set<String> nextLevelSet = new HashSet<>();
        // 逐个取出本层节点，向下扩展
        for (String curWord : positiveSet) {
            // 找出这个单词能一次转换出哪些单词
            List<String> nearWordList = getNearWordList(curWord, wordsSet);
            // 逐个进行处理
            for (String nearWord : nearWordList) {
                // 加入下一层节点
                nextLevelSet.add(nearWord);
                // 根据扩展方向，调整键值，整个树最终是从上到下映射的
                String key = direction ? curWord : nearWord;
                String val = direction ? nearWord : curWord;
                // 判断是否构建完成(从上到下和从下到上都访问到这个节点)
                if (oppositeSet.contains(nearWord)) {
                    isOK = true;
                }
                // 把这个映射关系添加进映射树
                List<String> list = mapTree.getOrDefault(key, new ArrayList<>());
                list.add(val);
                mapTree.put(key, list);
            }
        }
        // if (isOK)
        //     return true;
        // 把扩展出的下一层作为待扩展节点集进行正向扩展
        // return buildMapTree(mapTree, direction, wordsSet, nextLevelSet, oppositeSet);
        //一般情况下新扩展的元素会多一些，所以我们下次反方向扩展，就可以减少调整方向的次数
        return isOK || buildMapTree(mapTree, !direction, wordsSet, oppositeSet, nextLevelSet);
    }

    /**
     * 根据构建好的映射树，找到从起点到终点的所有路径
     *
     * 这个递归有两出口，要么找到终点，要么顺序执行完代码，但[由于一进来就改变了参数，添加了新元素]
     * 所以两个出前都要移除掉新加元素，作用是：
     * 对于for循环中的多次dfs之间是并列关系，都用了tempPath这个参数，如果进入dfs出来时temp没有还原
     * 那么顺序执行下一次dfs时的temp就是被改变的temp，因此递归出来前必须还原参数
     *
     * @param mapTree
     * @param resList
     * @param beginWord
     * @param endWord
     * @param tempPath
     */
    private void dfs(HashMap<String, List<String>> mapTree, List<List<String>> resList, String beginWord, String endWord, ArrayList<String> tempPath) {
        // 当前起点加入路径
        tempPath.add(beginWord);
        // 到达了目标
        if (beginWord.equals(endWord)) {
            // 当前路径作为一个结果加入结果集
            resList.add(new ArrayList<>(tempPath));
            tempPath.remove(tempPath.size() - 1);
            return;
        }
        // 没有到终点，但当前节点存在映射到下一层节点
        if (mapTree.containsKey(beginWord)) {
            for (String nearWord : mapTree.get(beginWord)) {
                // 下一层节点作为起点，继续dfs
                dfs(mapTree, resList, nearWord, endWord, tempPath);
            }
        }
        tempPath.remove(tempPath.size() - 1);
    }

    public static void main(String[] args) {
        String beginWord = "red";
        String endWord = "tax";
        List<String> wordList = Arrays.asList("ted", "tex", "red", "tax", "tad", "den", "rex", "pee");
        List<List<String>> listList = new _126_TwoWordsChangePlus().solution2(beginWord, endWord, wordList);
        // List<List<String>> listList = new _126_TwoWordsChangePlus().findLadders(beginWord, endWord, wordList);
        for (List<String> list : listList) {
            for (String str : list) {
                System.out.print(str + ",");
            }
            System.out.println();
        }
    }
}
