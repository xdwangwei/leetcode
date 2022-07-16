package com.graph;

import java.util.*;

/**
 * @author wangwei
 * 2021/11/15 9:46
 *
 * 你这个学期必须选修 numCourses 门课程，记为0到numCourses - 1 。
 *
 * 在选修某些课程之前需要一些先修课程。 先修课程按数组prerequisites 给出，其中prerequisites[i] = [ai, bi] ，表示如果要学习课程 ai 则 必须 先学习课程  bi 。
 *
 * 例如，先修课程对[0, 1] 表示：想要学习课程 0 ，你需要先完成课程 1 。
 * 请你判断是否可能完成所有课程的学习？如果可以，返回 true ；否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：numCourses = 2, prerequisites = [[1,0]]
 * 输出：true
 * 解释：总共有 2 门课程。学习课程 1 之前，你需要完成课程 0 。这是可能的。
 * 示例 2：
 *
 * 输入：numCourses = 2, prerequisites = [[1,0],[0,1]]
 * 输出：false
 * 解释：总共有 2 门课程。学习课程 1 之前，你需要先完成课程 0 ；并且学习课程 0 之前，你还应先完成课程 1 。这是不可能的。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/course-schedule
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _207_CourseSchedule {

    /**
     * 本题是一道经典的「拓扑排序」问题。
     *
     * 给定一个包含 n 个节点的有向图 G，我们给出它的节点编号的一种排列，如果满足：
     *
     * 对于图 G 中的任意一条有向边 (u,v)，u 在排列中都出现在 v 的前面。
     *
     * 那么称该排列是图 G 的「拓扑排序」。
     *
     * 所以所谓拓扑排序其实就是将有向图G拉平，让它的所有边都朝向一致，如果图存在环，那么就不可能
     *
     * 简单来说，判断一个有向图存不存在可能的拓扑排序，其实就是 判断这个图中存不存在环
     *
     */

    /**
     * 方法一：【深度】优先搜索，用一个【栈】来存储所有已经搜索完成的节点。
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
     * 可以发现，如果我们从【栈顶】往【栈底】的顺序看，由于 u 处于栈顶的位置，那么 u 出现在所有 u 的相邻节点的前面。因此对于 u 这个节点而言，它是满足拓扑排序的要求的。
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
    // 每个节点的搜索状态
    private int[] visited;
    // 保存最终结果，从顶向底就是一个拓扑排序
    private Stack<Integer> stack;
    // 是否存在拓扑排序
    boolean isValid = true;
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 构造图
        List<List<Integer>> graph = buildGraph(numCourses, prerequisites);
        // 初始化
        visited = new int[numCourses];
        stack = new Stack<>();
        // 从所有未搜索的点开始，dfs搜索，dfs中如果图存在环，isValid就会变为false。这里也就不用继续下去了。直接返回结果
        for (int i = 0; i < numCourses && isValid; i++) {
            if (visited[i] == 0) {
                dfs(graph, i);
            }
        }
        return isValid;
    }

    /**
     * dfs 搜索
     * 你看这玩意像不像多叉树的后序遍历，先访问所有孩子，再访问自己
     * @param graph
     * @param i
     */
    private void dfs(List<List<Integer>> graph, int i) {
        // 标记当前点 处于 搜索中
        visited[i] = 1;
        // 先去搜搜它的未搜索的邻接点
        for (Integer neighbor : graph.get(i)) {
            // 这个点未访问
            if (visited[neighbor] == 0) {
                dfs(graph, neighbor);
                // 每次dfs完了。就判断一下isValid，如果存在环，那么可以直接返回了
                if (!isValid) {
                    return;
                }
            // 存在环，
            } else if (visited[neighbor] == 1) {
                isValid = false;
                return;
            }
        }
        // 邻接点访问完了，代表前置课程修完了，可以修自己了，入栈（先修课程已经都入栈了。现在自己在栈顶）
        stack.push(i);

        // 标记当前点 搜索完了
        visited[i] = 2;
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
     * @param numCourses
     * @param prerequisites
     * @return
     */
    private int[] indegree;
    private int count;
    public boolean canFinish2(int numCourses, int[][] prerequisites) {
        // 初始化，构造图用到visited了。所以这里要先初始化visited
        indegree = new int[numCourses];
        // 构造图
        List<List<Integer>> graph = buildGraph2(numCourses, prerequisites);
        // 找到所有入度为0的点，入队列
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        // 逐个出队列，统计队列元素个数
        while (!queue.isEmpty()) {
            Integer node = queue.poll();
            // 入度0的点个数+1
            count++;
            // 对于这个点的所有邻接点，当前课修完了。所以这些课的入度都要减1
            for (Integer neighbor : graph.get(node)) {
                indegree[neighbor]--;
                // 某个课入度变0了，就可以修了。入队列
                if (indegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        // 最终有几个点达到入度为0了，如果==numCOUrse，那就不存在环
        return count == numCourses;
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
