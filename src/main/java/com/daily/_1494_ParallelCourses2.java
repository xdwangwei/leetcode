package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2023/6/16 14:37
 * @description: _1494_ParallelCourses2
 *
 * 1494. 并行课程 II
 * 给你一个整数 n 表示某所大学里课程的数目，编号为 1 到 n ，数组 relations 中， relations[i] = [xi, yi]  表示一个先修课的关系，也就是课程 xi 必须在课程 yi 之前上。同时你还有一个整数 k 。
 *
 * 在一个学期中，你 最多 可以同时上 k 门课，前提是这些课的先修课在之前的学期里已经上过了。
 *
 * 请你返回上完所有课最少需要多少个学期。题目保证一定存在一种上完所有课的方式。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：n = 4, relations = [[2,1],[3,1],[1,4]], k = 2
 * 输出：3
 * 解释：上图展示了题目输入的图。在第一个学期中，我们可以上课程 2 和课程 3 。然后第二个学期上课程 1 ，第三个学期上课程 4 。
 * 示例 2：
 *
 *
 *
 * 输入：n = 5, relations = [[2,1],[3,1],[4,1],[1,5]], k = 2
 * 输出：4
 * 解释：上图展示了题目输入的图。一个最优方案是：第一学期上课程 2 和 3，第二学期上课程 4 ，第三学期上课程 1 ，第四学期上课程 5 。
 * 示例 3：
 *
 * 输入：n = 11, relations = [], k = 2
 * 输出：6
 *
 *
 * 提示：
 *
 * 1 <= n <= 15
 * 1 <= k <= n
 * 0 <= relations.length <= n * (n-1) / 2
 * relations[i].length == 2
 * 1 <= xi, yi <= n
 * xi != yi
 * 所有先修关系都是不同的，也就是说 relations[i] != relations[j] 。
 * 题目输入的图是个有向无环图。
 * 通过次数7,753提交次数16,475
 */
public class _1494_ParallelCourses2 {

    /**
     * 方法：状态压缩 + BFS + 子集枚举 + 贪心
     *
     * 【前置】
     *
     * 为什么不是拓扑排序？
     *
     * 题目加了一学期最多选择k门课的限制，拓扑+贪心是不是也能解出来？
     *
     * 问题是要怎么贪，取到入度为0的，然后如果存在多个那你要怎么选呢，贪的难点就在这里了。
     * 我之前试了按出度排序、按深度排序、按子节点入度为1这几种方法排序取前k个入度为0的，但是都会存在特例过不去。。。
     *
     * 因此，每次有多个可选课程时，都要考虑选则哪k个课，存在枚举行为
     *
     * 【知识点】
     * 二进制表示集合时，判断 a 是 b 的子集，则 a == (a & b)
     *
     * 从 a 中移除 b， a ^= b
     *
     * 【解题】
     *
     * 我们用数组 d[i] 表示课程 i 的先修课程的集合。
     * 由于数据规模 n<15，我们可以用一个整数的二进制位（状态压缩）来表示集合，其中第 j 位为 1 表示课程 j 是课程 i 的先修课程。
     *
     * bfs；节点：（当前已经修完的课程，当前是第几学期）
     *
     * 我们用一个状态变量 cur 表示当前已经上过的课程的集合，terms 表示已经经过了几个学期，初始时 cur = 0，terms = 0。
     * 一共 n 个节点，编号 从 1 到 n，二进制占用 n+1 个位置（编号从1开始），
     * 那么当全部课程选完后，数字为 target = 1 << (n+1) - 2
     *
     * 因此，在 bfs 的过程中，如果当前节点 cur=target，表示所有课程都上过了，返回当前学期terms即可。
     *
     * 否则，对于当前节点，已经选过的课程集合 cur，当前的学期 terms，遍历所有课程及其先修课
     *
     * 如果课程 i 的先修课程 d[i] 的集合是 cur 的子集（(d[i] & cur) == cur），说明课程i的先修课已经全部上完，课程 i 可以上。
     * 这样我们可以找到当前所有能够选择的课程集合 canSel，记得要去除已经修过的课程，即 canSel ^= cur
     *
     * 那么 cur 状态的下一个状态 newMask 就是 cur | canSel，表示后续学期可以上的课程集合，学期数变为 terms + 1。
     *
     * 这里存在的问题是 canSel 的二进制中1的个数，即当前可以选择的课程数量，
     *
     * 如果 canSel 的二进制表示中 1 的个数小于等于 k，说明后续学期可以上的课程数不超过 k，我们就可以将 (newMask, terms+1) 加入队列中。
     * 否则，说明后续学期可以上的课程数超过 k，那么我们就应该从后续可以上的课程中选择 k 门课程，这样才能保证后续学期可以上的课程数不超过 k。
     *          这里可以枚举 canSel 的所有子集x（二进制1的个数==k），将子集加入队列中，（x|cur,terms+1）。
     *          这里不用考虑 canSel 的子集中 二进制1 的个数 < k 的情况，
     *                  因为，既然一次能上k门课，为什么要考虑只上k-1或k-2甚至更少的课？这样只会导致学期数更多
     *
     * 代码实现时，由于先学课程 1，再学课程 2，或者先学课程 2，再学课程 1，都会递归到「学完课程 1 和 2」的状态上。
     * 因此，对于新的节点 （选完的课程，学期），需要对 节点（已修完的课程） 去重后，再判断是否加入队列
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/parallel-courses-ii/solution/python3javacgo-yi-ti-yi-jie-zhuang-tai-y-dabh/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param relations
     * @param k
     * @return
     */
    public int minNumberOfSemesters(int n, int[][] relations, int k) {
        // d[i] 表示课程 i 的先修课程的集合（二进制为1）
        int[] d = new int[n + 1];
        for (int[] r : relations) {
            d[r[1]] |= 1 << r[0];
        }
        // bfs节点（当前已经修完的课程，当前是第几学期）
        Deque<int[]> q = new ArrayDeque<>();
        // 去重，考虑 （当前已经修完的课程）即可。
        Set<Integer> vis = new HashSet<>();
        // 初始化
        q.offer(new int[]{0, 0});
        vis.add(0);
        // bfs
        while (!q.isEmpty()) {
            int sz = q.size();
            // 层序遍历
            while (sz-- > 0) {
                // 出队列
                int[] poll = q.poll();
                int mask = poll[0], terms = poll[1];
                // 已经全部修完，返回 学期数
                if (mask == (1 << n + 1) - 2) {
                    return terms;
                }
                // 判断当前可以选择的课程集合
                int canSel = 0;
                for (int i = 1; i <= n; ++i) {
                    // i的先修课程已经全部修完，即 d[i] 是 mask 的子集
                    if ((d[i] & mask) == d[i]) {
                        canSel |= 1 << i;
                    }
                }
                // 排除掉已经修过的课程
                canSel ^= mask;
                // 如果不超过k门课
                if (Integer.bitCount(canSel) <= k) {
                    // 新状态没有遍历过
                    if (vis.add(mask | canSel)) {
                        // 加入队列
                        q.offer(new int[]{mask | canSel, terms + 1});
                    }
                } else {
                    // 超过k门可选课
                    // 枚举子集，只考虑 二进制1的个数等于 k 的子集，
                    // 二进制枚举
                    int x = canSel;
                    while (x > 0) {
                        // 只考虑 二进制1的个数等于 k 的子集，并且新状态没有被访问过
                        if (Integer.bitCount(x) == k && vis.add(mask | x)) {
                            q.offer(new int[]{mask | x, terms + 1});
                        }
                        // 注意是在 canSel 里面选
                        x = (x - 1) & canSel;
                    }
                }
            }
        }
        // 题目保证一定有解，此处不会到达
        return -1;
    }

    public static void main(String[] args) {
        _1494_ParallelCourses2 obj = new _1494_ParallelCourses2();
        System.out.println(obj.minNumberOfSemesters(4, new int[][]{{2, 1}, {3, 1}, {1, 4}}, 2));
    }
}
