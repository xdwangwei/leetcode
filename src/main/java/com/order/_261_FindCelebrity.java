package com.order;


import java.util.LinkedList;

/**
 * @author wangwei
 * 2021/11/22 16:34
 * <p>
 * 搜索名人
 * <p>
 * 给你 n 个人的社交关系（你知道任意两个人之间是否认识），然后请你找出这些人中的「名人」。
 * <p>
 * 所谓「名人」有两个条件：
 * <p>
 * 1、所有其他人都认识「名人」。
 * <p>
 * 2、「名人」不认识任何其他人。
 * <p>
 * 在本题中，你可以使用辅助函数 bool knows(a, b) 获取到 A 是否认识 B。请你来实现一个函数 int findCelebrity(n)。
 * <p>
 * 派对最多只会有一个 “名人” 参加。
 * 若 “名人” 存在，请返回他/她的编号；若 “名人” 不存在，请返回 -1。
 * <p>
 * 示例1:
 * 输入: graph = [
 * [1,1,0],
 * [0,1,0],
 * [1,1,1]
 * ]
 * 输出: 1
 * 解析: 有编号分别为 0、1 和 2 的三个人。
 * graph[i][j] = 1 代表编号为 i 的人认识编号为 j 的人，
 * 而 graph[i][j] = 0 则代表编号为 i 的人不认识编号为 j 的人。
 * “名人” 是编号 1 的人，因为 0 和 2 均认识他/她，但 1 不认识任何人。
 * 示例 2:
 * <p>
 * <p>
 * <p>
 * 输入: graph = [
 * [1,0,1],
 * [1,1,0],
 * [0,1,1]
 * ]
 * 输出: -1
 * 解析: 没有 “名人”
 * <p>
 * 注意:
 * 该有向图是以邻接矩阵的形式给出的，是一个 n × n 的矩阵，
 * a[i][j] = 1 代表 i 与 j 认识，a[i][j] = 0 则代表 i 与 j 不认识。
 * 请记住，您是无法直接访问邻接矩阵的。
 */
public class _261_FindCelebrity {

    /**
     * 社交关系嘛，本质上就可以抽象成一幅图。
     *
     * 如果把每个人看做图中的节点，「认识」这种关系看做是节点之间的有向边，那么名人就是这幅图中一个特殊的节点：
     *
     * 这个节点没有一条指向其他节点的有向边；且其他所有节点都有一条指向这个节点的有向边。
     *
     * 或者说的专业一点，名人节点的【出度】为 0，【入度】为 n - 1。
     *
     * 那么，这 n 个人的社交关系是如何表示的呢？
     *
     * 前文 图论算法基础 说过，图有两种存储形式，一种是邻接表，一种是邻接矩阵，邻接表的主要优势是节约存储空间；邻接矩阵的主要优势是可以迅速判断两个节点是否相邻。
     *
     * 对于名人问题，显然会经常需要判断两个人之间是否认识，也就是两个节点是否相邻，所以我们可以用邻接矩阵来表示人和人之间的社交关系。
     *
     * 那么，把名流问题描述成算法的形式就是这样的：
     *
     * 给你输入一个大小为 n x n 的二维数组（邻接矩阵） graph 表示一幅有 n 个节点的图，每个人都是图中的一个节点，编号为 0 到 n - 1。
     *
     * 如果 graph[i][j] == 1 代表第 i 个人认识第 j 个人，如果 graph[i][j] == 0 代表第 i 个人不认识第 j 个人。
     *
     * 有了这幅图表示人与人之间的关系，请你计算，这 n 个人中，是否存在「名人」？
     *
     * 如果存在，算法返回这个名人的编号，如果不存在，算法返回 -1。
     * @param graph
     * @return
     */


    /**
     * 方法一：暴力搜素
     *
     * @param graph
     * @return
     */
    private int[][] graph;

    public int findCelebriy(int n) {
        for (int cand = 0; cand < n; cand++) {
            int other = 0;
            while (other < n) {
                if (other == cand) {
                    continue;
                }
                // 保证其他人都认识 cand，且 cand 不认识任何其他人
                // 否则 cand 就不可能是名人
                if (knows(cand, other) || !knows(other, cand)) {
                    break;
                }
            }
            // cand 是 名人， cand 和所有other都满足，other认识cand，cand不认识other
            if (other == n) {
                return cand;
            }
        }
        // 没有名人
        return -1;
    }

    /**
     * 方法二：优化方法一，任意两个人之间只存在四种关系：
     * A 认识 B ， B 不认识 A      =>  A 不是名人
     * A 认识 B,   B 认识  A      =>  A B 都不是名人
     * A 不认识 B， B 不认识 A     =>  A B 都不是名人
     * A 不认识 B, B 认识 A       =>  B 不是名人
     * 那么，每两个人比较，就会排除掉 至少一个人
     * @param n
     * @return
     */
    public int findCelebriy2(int n) {
        LinkedList<Integer> queue = new LinkedList<>();
        for (int cand = 0; cand < n; cand++) {
            queue.offer(cand);
        }
        // 每次比较两个人
        while (queue.size() >= 2) {
            int i = queue.removeFirst();
            int j = queue.removeFirst();
            boolean flag1 = knows(i, j);
            boolean flag2 = knows(j, i);
            // A 认识 B ， B 不认识 A ， A 不是名人， B 可能是名人， 再入队列
            if (flag1 && !flag2) {
                queue.offer(j);
            // B 认识 A, A 不认识 B, B 不是名人， A 可能是名人，再入队列
            } else if (flag2 && !flag1) {
                queue.offer(i);
            // 剩下两种情况， AB都不可能是名人
            } else {

            }
        }
        // 空，无名人
        if (queue.isEmpty()) {
            return -1;
        }
        // 队列只剩下一个元素，判断它是不是名人
        // 之前只是排除了所有不可能情况，需要对排除剩下的结果进行验证
        int cand = queue.remove();
        for (int j = 0; j < n; j++) {
            if (j == cand) {
                continue;
            }
            // 保证其他人都认识 cand，且 cand 不认识任何其他人
            // 否则 cand 就不可能是名人
            if (knows(cand, j) || !knows(j, cand)) {
                return -1;
            }
        }
        // cand 是 名人， cand 和所有other都满足，other认识cand，cand不认识other
        return cand;
    }

    /**
     * 方法三：优化方法二的空间复杂度，O（N） -> O（1）
     *
     * 因为两个人比较，一定会排除掉一个人，并且只有一个名人，所以只要假设的cand，最后验证是名人，那它就是答案，不可能说只假设出了其中一种结果
     * @param n
     * @return
     */
    public int findCelebriy3(int n) {
        // 先假设 cand 是名人
        int cand = 0;
        // 二者比较必排除一
        // 这里只是排除了所有不可能的情况
        for (int other = 1; other < n; other++) {
            // cand 不可能是名人，排除
            if (!knows(other, cand) || knows(cand, other)) {
                // 假设 other 是名人
                cand = other;
            } else {
                // other 不可能是名人，排除
                // 什么都不用做，继续假设 cand 是名人
            }
        }
        // 需要对排除剩下的结果进行验证
        // 现在的 cand 是排除的最后结果，但不能保证一定是名人
        for (int other = 0; other < n; other++) {
            if (cand == other) continue;
            // 需要保证其他人都认识 cand，且 cand 不认识任何其他人
            if (!knows(other, cand) || knows(cand, other)) {
                return -1;
            }
        }
        return cand;
    }

    /**
     * i 和 j 是否认识
     *
     * @param i
     * @param j
     * @return
     */
    public boolean knows(int i, int j) {
        return graph[i][j] == 1;
    }
}
