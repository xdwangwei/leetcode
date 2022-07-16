package com.daily;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/5/7 9:39
 * @description: _433_MinimumGeneticMutation
 *
 * 433. 最小基因变化
 * 基因序列可以表示为一条由 8 个字符组成的字符串，其中每个字符都是 'A'、'C'、'G' 和 'T' 之一。
 *
 * 假设我们需要调查从基因序列 start 变为 end 所发生的基因变化。一次基因变化就意味着这个基因序列中的一个字符发生了变化。
 *
 * 例如，"AACCGGTT" --> "AACCGGTA" 就是一次基因变化。
 * 另有一个基因库 bank 记录了所有有效的基因变化，只有基因库中的基因才是有效的基因序列。
 *
 * 给你两个基因序列 start 和 end ，以及一个基因库 bank ，请你找出并返回能够使 start 变化为 end 所需的最少变化次数。如果无法完成此基因变化，返回 -1 。
 *
 * 注意：起始基因序列 start 默认是有效的，但是它并不一定会出现在基因库中。
 *
 *
 *
 * 示例 1：
 *
 * 输入：start = "AACCGGTT", end = "AACCGGTA", bank = ["AACCGGTA"]
 * 输出：1
 * 示例 2：
 *
 * 输入：start = "AACCGGTT", end = "AAACGGTA", bank = ["AACCGGTA","AACCGCTA","AAACGGTA"]
 * 输出：2
 * 示例 3：
 *
 * 输入：start = "AAAAACCC", end = "AACCCCCC", bank = ["AAAACCCC","AAACCCCC","AACCCCCC"]
 * 输出：3
 *
 *
 * 提示：
 *
 * start.length == 8
 * end.length == 8
 * 0 <= bank.length <= 10
 * bank[i].length == 8
 * start、end 和 bank[i] 仅由字符 ['A', 'C', 'G', 'T'] 组成
 */
public class _433_MinimumGeneticMutation {

    /**
     * 从某个点可以扩展出多个下一状态，求 初始状态 到 末尾状态 的最小变化次数，明显的广度优先遍历
     *
     * 从 某个点到它的所有邻接点 都是一步，从树的结构来看，就是一层 到 另一层 的结构，从一层到另一层，step+1
     * 如何得到邻接状态
     * 每个字符串是由ACTG组成的8位字符串，那么每个位置都可以用ACTG中的某个字符替换，当前，不能和原位置一样
     * 所以，一个字符串，总共会产生 3 ^ 8 个邻接状态，需要记录已经访问过的节点，以及不在给出的基因序列中的节点
     * 如果某个邻接点等于end，直接返回step
     * 当前层节点的全部邻接点找完，入队列后，step + 1，这些点作为下一层节点，继续向下扩展
     * @param start
     * @param end
     * @param bank
     * @return
     */
    public int minMutation(String start, String end, String[] bank) {
        // 特殊情况
        if (start.equals(end)) {
            return 0;
        }
        // 如果 end 不在给定的序列中，直接返回 - 1
        Set<String> sequences = new HashSet<>();
        for (String seq : bank) {
            sequences.add(seq);
        }
        if (!sequences.contains(end)) {
            return -1;
        }
        // 可变邻接状态
        final char[] keys = {'A', 'C', 'T', 'G'};
        // bfs，队列
        Queue<String> queue = new ArrayDeque<>();
        // 判断已访问节点
        Set<String> visited = new HashSet<>();
        // 初始点入队列，标记已访问
        queue.offer(start);
        visited.add(start);
        // 默认
        int step = 1;
        // bfs
        while (!queue.isEmpty()) {
            // 每次进来时，sz 就是 当前层的全部节点个数，在扩展过程中队列中会加入新产生的下一层节点，所以借助sz来控制恰好取完当前层节点
            int sz = queue.size();
            for (int i = 0; i < sz; ++i) {
                // 出队列
                String seq = queue.poll();
                // 8 个位置，每个位置都可以有三种变化
                // 枚举8个位置
                for (int j = 0; j < 8; ++j) {
                    // 先保留原始串
                    StringBuilder builder = new StringBuilder(seq);
                    // 替换此位置字符为ACTG中的一个
                    for (int k = 0; k < 4; ++k) {
                        // 不能和原位置字符一样
                        if (seq.charAt(j) != keys[k]) {
                            // 得到它一个扩展节点
                            builder.setCharAt(j, keys[k]);
                            String newSeq = builder.toString();
                            // 如果正好得到目标，直接返回
                            if (end.equals(newSeq)) {
                                return step;
                            }
                            // 否则作为下一层节点入队列
                            // 前提是没有被访问过，并且 是存在于给定的基因序列中
                            if (!visited.contains(newSeq) && sequences.contains(newSeq)) {
                                queue.offer(newSeq);
                                visited.add(newSeq);
                            }
                        }
                    }
                }
            }
            // 一层的节点都处理完了，step才加1
            step++;
        }
        // 没在bfs过程中返回，说明整个树中节点没有和end匹配的
        return -1;
    }
}
