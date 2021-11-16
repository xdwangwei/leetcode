package com.graph;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangwei
 * 2021/11/14 19:09
 *
 * 给你一个有 n 个节点的 有向无环图（DAG），请你找出所有从节点 0 到节点 n-1 的路径并输出（不要求按特定顺序）
 *
 * 二维数组的第 i 个数组中的单元都表示有向图中 i 号节点所能到达的下一些节点，空就是没有下一个结点了。
 *
 * 译者注：有向图是有方向的，即规定了 a→b 你就不能从 b→a 。
 *
 *  
 *
 * 示例 1：
 *
 *
 *
 * 输入：graph = [[1,2],[3],[3],[]]
 * 输出：[[0,1,3],[0,2,3]]
 * 解释：有两条路径 0 -> 1 -> 3 和 0 -> 2 -> 3
 * 示例 2：
 *
 *
 *
 * 输入：graph = [[4,3,1],[3,2,4],[3],[4],[]]
 * 输出：[[0,4],[0,3,4],[0,1,3,4],[0,1,2,3,4],[0,1,4]]
 * 示例 3：
 *
 * 输入：graph = [[1],[]]
 * 输出：[[0,1]]
 * 示例 4：
 *
 * 输入：graph = [[1,2,3],[2],[3],[]]
 * 输出：[[0,1,2,3],[0,2,3],[0,3]]
 * 示例 5：
 *
 * 输入：graph = [[1,3],[2],[3],[]]
 * 输出：[[0,1,2,3],[0,3]]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/all-paths-from-source-to-target
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _797_AllPathsFromSourceToTarget {

    private List<List<Integer>> res;

    /**
     * 从 0 到 n - 1
     * @param graph
     * @return
     */
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        res = new LinkedList<>();
        if (graph == null) {
            return res;
        }
        LinkedList<Integer> temp = new LinkedList<>();
        // 起点是 0
        dfs(graph, 0, temp);
        return res;
    }

    /**
     * dfs
     * @param graph
     * @param i 当前点的 id
     * @param path
     *
     * 因为本题中的图为有向无环图（\text{DAG}DAG），搜索过程中不会反复遍历同一个点，因此我们无需判断当前点是否遍历过。
     *
     * 注意与回溯法的区别，回溯法是在for循环内做选择->下一层->撤销选择，
     * 而我们是在for循环外面，这就是图遍历和回溯的区别
     *
     * 回溯的写法会 少对根节点的进入和离开信息。
     *
     * 为什么回溯算法框架会用后者？因为回溯算法关注的不是节点，而是【树枝】，不信你看 回溯算法核心套路 里面的图。
     *
     * 当然也可以进入回溯前，先把根入路径，然后 dfs内部就是 for里面 做循环和撤销选择，相当于把对根的处理【补上】
     *
     * 总之，无论深度优先搜索还是回溯，注意 【对称】，做选择-撤销，无论代码在哪里写，这两步都要有
     */
    private void dfs(int[][] graph, int i, LinkedList<Integer> path) {
        // 当前点入路径
        path.addLast(i);
        // 走到了终点，找到了一条路径
        if (i == graph.length - 1) {
            res.add(new LinkedList<>(path));
            // 恢复到上一层
            path.removeLast();
            return;
        }
        // 对于 i 的全部邻接点
        for (int j : graph[i]) {
            // 下一层dfs
            dfs(graph, j, path);
        }
        // 撤销当前点
        path.removeLast();
    }

}
