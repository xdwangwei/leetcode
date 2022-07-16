package com.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author wangwei
 * 2021/11/15 10:48
 *
 * 现在你总共有 numCourses 门课需要选，记为0到numCourses - 1。给你一个数组prerequisites ，其中 prerequisites[i] = [ai, bi] ，表示在选修课程 ai 前 必须 先选修 bi 。
 *
 * 例如，想要学习课程 0 ，你需要先完成课程1 ，我们用一个匹配来表示：[0,1] 。
 * 返回你为了学完所有课程所安排的学习顺序。可能会有多个正确的顺序，你只要返回 任意一种 就可以了。如果不可能完成所有课程，返回 一个空数组 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：numCourses = 2, prerequisites = [[1,0]]
 * 输出：[0,1]
 * 解释：总共有 2 门课程。要学习课程 1，你需要先完成课程 0。因此，正确的课程顺序为 [0,1] 。
 * 示例 2：
 *
 * 输入：numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
 * 输出：[0,2,1,3]
 * 解释：总共有 4 门课程。要学习课程 3，你应该先完成课程 1 和课程 2。并且课程 1 和课程 2 都应该排在课程 0 之后。
 * 因此，一个正确的课程顺序是[0,1,2,3] 。另一个正确的排序是[0,2,1,3] 。
 * 示例 3：
 *
 * 输入：numCourses = 1, prerequisites = []
 * 输出：[0]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/course-schedule-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _210_CourseSchedule2 {

    /**
     * 方法一：【深度】优先搜索，用一个【栈】来存储所有已经搜索完成的节点。
     *
     * 方法一的深度优先搜索是一种「逆向思维」：最先被放入栈中的节点是在拓扑排序中最后面的节点。
     *
     * B -> A, C -> A 表示先修完B和C，才能修完A，这里有个不好理解的地方是， B的邻接点是A，C的邻接点也是A
     * 为什么要先访问邻接点A，再访问B本身，这样的话，在栈里面 的确是 A <- B, 是合理的，但最后要倒着输出
     * 如果先访问B，那么相当于先序，会导致得到 B -> A -> C, 这样肯定是错的，但反过来 后序遍历 A -> B -> C, 虽然结果要倒过来，但肯定是对的
     * 这里不大好理解，在于我们构造的树和寻常的二叉树比较一下，会发现实际上是一种反向箭头，除非你倒着画这棵树（画个图理解一下？）
     * 所以也可以选择方法二：队列，就是一个顺序思维
     *
     * 对于一个节点 u，如果它的所有相邻节点都已经搜索完成，那么在搜索回溯到 u 的时候，u 本身也标记为搜索完成。
     *
     * 假设我们当前搜索到了节点 u，如果它的所有相邻节点都已经搜索完成，那么这些节点都已经在栈中了，此时我们就可以把 u 入栈。
     * 可以发现，如果我们从栈顶往栈底的顺序看，由于 u 处于栈顶的位置，那么 u 出现在所有 u 的相邻节点的前面。因此对于 u 这个节点而言，它是满足拓扑排序的要求的。
     *
     * 这样以来，我们对图进行一遍深度优先搜索。当每个节点进行回溯的时候，我们把该节点放入栈中。最终从栈顶到栈底的序列就是一种拓扑排序。
     *
     * 算法
     *
     * 对于图中的任意一个节点，它在搜索的过程中有三种状态，即：
     *
     * 「未搜索-0」：我们还没有搜索到这个节点；
     *
     * 「搜索中-1」：我们搜索过这个节点，但是因为先访问邻接点，还没有回溯到该节点，即该节点还没有入栈，还有相邻的节点没有搜索完成；
     *
     * 「已完成-2」：我们搜索过并且回溯过这个节点，即该节点已经入栈，并且所有该节点的相邻节点都出现在栈的更底部的位置，满足拓扑排序的要求。
     *
     * 因为图可能是由多个互不相关的树组成的，所以需要【逐个】以未访问节点开始进行dfs
     *
     * 通过上述的三种状态，我们就可以给出使用深度优先搜索得到拓扑排序的算法流程，
     * 在每一轮的搜索搜索开始时，我们任取一个「未搜索」的节点开始进行深度优先搜索。
     *
     * 我们将当前搜索的节点 u 标记为「搜索中」，遍历该节点的每一个相邻节点 v：
     *
     * 如果 v 为「未搜索」，那么我们开始搜索 v，待搜索完成回溯到 u；
     *
     * 如果 v 为「搜索中」，那么我们就找到了图中的一个环，因此是不存在拓扑排序的；
     *
     * 如果 v 为「已完成」，那么说明 v 已经在栈中了，而 u 还不在栈中，因此 uu无论何时入栈都不会影响到 (u,v) 之前的拓扑关系，以及不用进行任何操作。
     *
     * 当 u 的所有相邻节点都为「已完成」时，我们将 u 放入栈中，并将其标记为「已完成」。
     *
     * 在整个深度优先搜索的过程结束后，如果我们没有找到图中的环，那么栈中存储这所有的 n 个节点，从栈顶到栈底的顺序即为一种拓扑排序。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/course-schedule/solution/ke-cheng-biao-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param numCourses
     * @param prerequisites
     * @return
     */
    // 标记dfs搜索时，每个点的搜索状态
    private int[] visited;
    // 保存最终结果，其实用栈+dfs，相当于 后序遍历了，也可以用一个列表，最后再倒叙返回就行，如果用栈的话实际上就是从栈顶到栈底的所有元素
    private int[] result;
    // 配合result使用，从后往前填充
    private int index;
    // 是否存在拓扑排序
    private boolean valid = true;
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // 构造图
        List<List<Integer>> graph = buildGraph(numCourses, prerequisites);
        // 初始化
        visited = new int[numCourses];
        result = new int[numCourses];
        // 逆向，从后往前填充
        index = numCourses - 1;
        // dfs
        for (int i = 0; i < numCourses; i++) {
            if (visited[i] == 0) {
                dfs(graph, i);
                // 存在环提前结束
                if (!valid) {
                    break;
                }
            }
        }
        return valid ? result : new int[0];
    }

    /**
     * dfs
     * 也可以视为多叉树后序遍历
     * @param graph
     * @param i
     */
    private void dfs(List<List<Integer>> graph, int i) {
        // 标记当前点处于搜索中
        visited[i] = 1;

        // 先搜索邻接点
        for (Integer neighbor : graph.get(i)) {
            if (visited[neighbor] == 0) {
                dfs(graph, neighbor);
                if (!valid) {
                    return;
                }
            }
            // 存在环
            if (visited[neighbor] == 1) {
                valid = false;
                return;
            }
        }

        // 当前点已搜索完。
        visited[i] = 2;
        // 保存结果
        result[index--] = i;
    }

    /**
     * 由给定的节点和边，构造有向图，使用邻接表
     * @param nodes
     * @param edges
     * @return
     */
    private List<List<Integer>> buildGraph(int nodes, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>(nodes);
        // 初始化
        for (int i = 0; i < nodes; i++) {
            graph.add(new ArrayList<>());
        }
        // 题目给出的edge[1][2], 表示修完2才能修1，在图中应该表示为 2 -> 1, 也就是 edge[1] -> edge[0]
        for (int[] edge : edges) {
            graph.get(edge[1]).add(edge[0]);
        }
        return graph;
    }

    /**
     * 方法二：【广度】优先搜索
     * 一门课只有等他的先修课程全部修完才能开始修，体现在图中就是这个点的【入度】为0
     * 所以，我们构造图，并统计每个点的入度，刚开始，用一个【队列】存储所有入度为0的点，这些课程没有先修课，都可以开始修
     * 每一次从队列中取出队首，代表修这个课程，给它的所有邻接点的入度减一，代表这些课的先修课的数目少了一个，如果某一个课的入栈为0了，那么就入队列，下一次就可以修它的
     * 如果最后入度为0的点的个数==课程数目，说明存在拓扑排序，否则这个图肯定存在环，导致有的课的入度无法减为0
     * 这些入度为0的点按照先后顺序就是一个拓扑排序
     * @param numCourses
     * @param prerequisites
     * @return
     */
    private int[] indegree;

    public int[] findOrder2(int numCourses, int[][] prerequisites) {
        // 初始化
        indegree = new int[numCourses];
        result = new int[numCourses];
        // 构造图
        List<List<Integer>> graph = buildGraph2(numCourses, prerequisites);
        Queue<Integer> queue = new LinkedList<>();
        // 正向，从前往后填充
        index = 0;
        // bfs
        for (int i = 0; i < numCourses; i++) {
            // 入度为0的点全部入队列
            if (indegree[i] == 0) {
               queue.offer(i);
            }
        }
        // 逐个出队列，修这门课
        while (!queue.isEmpty()) {
            Integer node = queue.poll();
            // 每次出队列的课都是可以修的，记录
            result[index++] = node;
            // 对于所有临接点x，因为 node -> x， 所以 x的先修课程数目减1
            for (Integer neighbor : graph.get(node)) {
                indegree[neighbor]--;
                // 入度为0表示先修课学完了，可以修了
                if (indegree[neighbor] == 0) {
                    // 入队列
                    queue.offer(neighbor);
                }
            }
        }
        // index = numCOurse, 说明所有课入度都为0了，否则就是存在环
        return index == numCourses ? result : new int[0];
    }

    /**
     * 构造图的同时统计每个点的入度
     * @param nodes
     * @param edges
     * @return
     */
    private List<List<Integer>> buildGraph2(int nodes, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>(nodes);
        // 初始化
        for (int i = 0; i < nodes; i++) {
            graph.add(new ArrayList<>());
        }
        // 题目给出的edge[1][2], 表示修完2才能修1，在图中应该表示为 2 -> 1, 也就是 edge[1] -> edge[0]
        for (int[] edge : edges) {
            graph.get(edge[1]).add(edge[0]);
            // 入度加1
            indegree[edge[0]]++;
        }
        return graph;
    }
}
