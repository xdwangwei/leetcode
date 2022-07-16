package com.daily;

import java.util.*;

/**
 * @author wangwei
 * 2022/4/6 9:24
 *
 * 310. 最小高度树
 * 树是一个无向图，其中任何两个顶点只通过一条路径连接。 换句话说，一个任何没有简单环路的连通图都是一棵树。
 *
 * 给你一棵包含 n 个节点的树，标记为 0 到 n - 1 。给定数字 n 和一个有 n - 1 条无向边的 edges 列表（每一个边都是一对标签），其中 edges[i] = [ai, bi] 表示树中节点 ai 和 bi 之间存在一条无向边。
 *
 * 可选择树中任何一个节点作为根。当选择节点 x 作为根节点时，设结果树的高度为 h 。在所有可能的树中，具有最小高度的树（即，min(h)）被称为 最小高度树 。
 *
 * 请你找到所有的 最小高度树 并按 任意顺序 返回它们的根节点标签列表。
 *
 * 树的 高度 是指根节点和叶子节点之间最长向下路径上边的数量。
 *
 *
 * 示例 1：
 *
 *
 * 输入：n = 4, edges = [[1,0],[1,2],[1,3]]
 * 输出：[1]
 * 解释：如图所示，当根是标签为 1 的节点时，树的高度是 1 ，这是唯一的最小高度树。
 * 示例 2：
 *
 *
 * 输入：n = 6, edges = [[3,0],[3,1],[3,2],[3,4],[5,4]]
 * 输出：[3,4]
 *
 *
 * 提示：
 *
 * 1 <= n <= 2 * 104
 * edges.length == n - 1
 * 0 <= ai, bi < n
 * ai != bi
 * 所有 (ai, bi) 互不相同
 * 给定的输入 保证 是一棵树，并且 不会有重复的边
 */
public class _310_MinimumHeightTrees {


    /**
     * 类似拓扑排序
     *
     * 首先，我们看了样例，发现这个树并不是二叉树，是多叉树。
     * 然后，我们可能想到的解法是：根据题目的意思，就挨个节点遍历bfs，统计下每个节点的高度，然后用map存储起来，后面查询这个高度的集合里最小的就可以了。
     * 但是这样会超时的。
     * 于是我们看图（题目介绍里面的图）分析一下，发现，【越是靠里面的节点越有可能是最小高度树】。
     * 所以，我们可以这样想，我们可以【倒着】来。
     * 我们从边缘开始，先找到所有出度为1的节点，然后把所有出度为1的节点进队列，然后不断地bfs，最后找到的就是两边同时向中间靠近的节点，那么这个中间节点就相当于把整个距离二分了，那么它当然就是到两边距离最小的点啦，也就是到其他叶子节点最近的节点了。
     * 然后，就可以写代码了。
     *
     * 因为是无向图，所以直接用 度 来衡量即可，度越小(1),就是那种单独吊着的节点，这种节点只会拉长树的高度；度越大，它连接的节点越多，数的高度越小，
     * 所以，每次删除度为1的节点，同时更新与它相连的节点的度，继续重复，直到剩下1个或两个节点
     * 为什么可能有两个
     *
     * 我们所求的节点，实际上应该是这个无向图（树）中那条最长的路径的中点节点，如果这个路径上的节点个数是奇数，那么结果唯一，如果是偶数，那么最中间两个都可以作为根
     * 具体可以参考题目两个示例
     *
     * 作者：xiao-xin-28
     * 链接：https://leetcode-cn.com/problems/minimum-height-trees/solution/zui-rong-yi-li-jie-de-bfsfen-xi-jian-dan-zhu-shi-x/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param edges
     * @return
     */
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        List<Integer> res = new ArrayList<>();
        // 只有一个节点，编号0
        if (n == 1) {
            res.add(0);
            return res;
        }
        // 构建邻接表
        List<Set<Integer>> graph = new ArrayList<>();
        // 每个节点的度
        int[] degrees = new int[n];
        // 初始化
        for (int i = 0; i < n; i++) {
            graph.add(new HashSet<>());
        }
        // 构建
        for (int[] edge : edges) {
            int from = edge[0], to = edge[1];
            graph.get(from).add(to);
            graph.get(to).add(from);
            degrees[from]++;
            degrees[to]++;
        }
        // 保存所有度为1的节点
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (degrees[i] == 1) {
                queue.add(i);
            }
        }
        // 对于中心节点是两个的情况 a----b ，此时二者都应该保留，
        // 对于中心节点只有一个，那么肯定是一拖多个，然后这多个直接被删除，所以也不会n会直接由大于2变为1，也是正确的
        // 所以这里的while不能一直把队列清空，清没了，没结果了
        // 最后队列中的节点个数一定<=2
        // 最后保留1到2个节点
        while (n > 2) {
            // 当前度为1的节点个数
            int sz = queue.size();
            // 移除这些
            n -= sz;
            // 移除
            for (int i = 0; i < sz; i++) {
                Integer node = queue.poll();
                // 节点度减1
                degrees[node]--;
                for (Integer neighbor : graph.get(node)) {
                    // 邻接节点度减1
                    degrees[neighbor]--;
                    // 为1就加入队列
                    if (degrees[neighbor] == 1) {
                        queue.add(neighbor);
                    }
                }
            }
        }
        // 将队列中剩余节点(一定<=2)加入res
        while (!queue.isEmpty()) {
            res.add(queue.poll());
        }
        // 返回
        return res;
    }

}
