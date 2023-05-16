package com.daily;

import com.sun.jdi.VMOutOfMemoryException;

import java.nio.channels.NonWritableChannelException;
import java.util.*;

/**
 * @author wangwei
 * @date 2023/5/9 20:04
 * @description: _1263_MinimumMovesToMoveABoxToTargetLocation
 *
 * 1263. 推箱子
 * 「推箱子」是一款风靡全球的益智小游戏，玩家需要将箱子推到仓库中的目标位置。
 *
 * 游戏地图用大小为 m x n 的网格 grid 表示，其中每个元素可以是墙、地板或者是箱子。
 *
 * 现在你将作为玩家参与游戏，按规则将箱子 'B' 移动到目标位置 'T' ：
 *
 * 玩家用字符 'S' 表示，只要他在地板上，就可以在网格中向上、下、左、右四个方向移动。
 * 地板用字符 '.' 表示，意味着可以自由行走。
 * 墙用字符 '#' 表示，意味着障碍物，不能通行。
 * 箱子仅有一个，用字符 'B' 表示。相应地，网格上有一个目标位置 'T'。
 * 玩家需要站在箱子旁边，然后沿着箱子的方向进行移动，此时箱子会被移动到相邻的地板单元格。记作一次「推动」。
 * 玩家无法越过箱子。
 * 返回将箱子推到目标位置的最小 推动 次数，如果无法做到，请返回 -1。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：grid = [["#","#","#","#","#","#"],
 *              ["#","T","#","#","#","#"],
 *              ["#",".",".","B",".","#"],
 *              ["#",".","#","#",".","#"],
 *              ["#",".",".",".","S","#"],
 *              ["#","#","#","#","#","#"]]
 * 输出：3
 * 解释：我们只需要返回推箱子的次数。
 * 示例 2：
 *
 * 输入：grid = [["#","#","#","#","#","#"],
 *              ["#","T","#","#","#","#"],
 *              ["#",".",".","B",".","#"],
 *              ["#","#","#","#",".","#"],
 *              ["#",".",".",".","S","#"],
 *              ["#","#","#","#","#","#"]]
 * 输出：-1
 * 示例 3：
 *
 * 输入：grid = [["#","#","#","#","#","#"],
 *              ["#","T",".",".","#","#"],
 *              ["#",".","#","B",".","#"],
 *              ["#",".",".",".",".","#"],
 *              ["#",".",".",".","S","#"],
 *              ["#","#","#","#","#","#"]]
 * 输出：5
 * 解释：向下、向左、向左、向上再向上。
 *
 *
 * 提示：
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 20
 * grid 仅包含字符 '.', '#',  'S' , 'T', 以及 'B'。
 * grid 中 'S', 'B' 和 'T' 各只能出现一个。
 * 通过次数14,153提交次数25,843
 */
public class _1263_MinimumMovesToMoveABoxToTargetLocation {

    /**
     * 方法：bfs
     *
     * 题目中，人是主动动的，箱子是被动推的，将箱子推到目标位置的最小推动次数与箱子位置和玩家位置相关。
     *
     * 我们把箱子位置和玩家位置当成一个状态，那么状态的转移主要由【玩家】向上、下、左、右四个方向移动触发
     * （如果玩家移动后的位置与箱子位置当前位置重叠，那么箱子也会相应的作出【同样】的移动，即一次“推动”）。
     *
     * 我们把玩家的位置和箱子的位置看成一个状态，即 (si,sj,bi,bj)，其中 (si,sj) 是玩家的位置，而 (bi,bj) 是箱子的位置。
     *
     * 在代码实现上，我们定义一个函数 f(i,j)，它将二维坐标 (i,j) 映射到一个一维的状态编号，即 f(i,j)=i×n+j，其中 n 是网格的列数。
     *
     * 那么玩家和箱子的状态就是 (f(si,sj),f(bi,bj))。
     *
     * 我们首先遍历网格，找到玩家和箱子的初始位置，记为 (si,sj) 和 (bi,bj)。
     *
     * 然后，我们定义一个双端队列 q，其中每个元素都是一个三元组 (f(si,sj),f(bi,bj),d)，
     * 表示玩家位于 (si,sj)，箱子位于 (bi,bj)，并且已经进行了 d 次推动。
     *
     * 这里为什么要单独记录d，不能用bfs”齐头并进“写法吗？
     * （因为是人在动，只有人移动到的箱子位置，才会造成箱子被推动，才会造成推动次数+1，所以当前层队列元素能得到的所有下一层元素，不一定都增加推动次数）
     *
     * 而为了保证 bfs 的正确性，按照以前 ”齐头并进“的写法，上一层的节点需要的步数 一定都比 下一层节点需要的步数少
     * 这样，按照 bfs 的逐层遍历顺序，第一次遇到目标位置时，返回的一定是最少的步数
     *
     * 那么在本题中，如果不能用bfs”齐头并进“，每个节点单独维护步数，我们必须要保证 bfs 遍历时 步数少的节点在前面，步数多的节点在后面
     * 这样才能保证第一次遇到目标位置时，返回节点的步数是 预期的答案
     *
     * 因此，对于新得到的节点，如果是 步数增加（箱子动了）的节点，则 加入队列尾部，如果 是步数未增加（箱子没动）的节点，加入队列头部。
     *
     * 具体地，
     *
     * 初始时，我们将 (f(si,sj),f(bi,bj),0) 加入队列 q。
     *
     * 另外，我们用一个二维数组 vis 记录每个状态是否已经访问过，初始时 vis[f(si,sj)][f(bi,bj)] 标记为已访问。
     *
     * 接下来，我们开始进行广度优先搜索。
     *
     * 在每一步搜索中，
     *      我们取出队头元素 (f(si,sj),f(bi,bj),d)，
     *      并检查是否满足 grid[bi][bj] == 'T'，
     *          如果是，说明箱子已经被推到目标位置，此时将 d 作为答案返回即可。
     *          否则，我们枚举【玩家】的下一步移动方向，玩家新的位置记为 (sx,sy)，
     *              如果 (sx,sy) 是一个合法的位置，我们判断此时 (sx,sy) 是否与箱子的位置 (bi,bj) 是否相同：
     *                  如果相同，说明当前玩家到达了箱子的位置，并且推动箱子往前走了一步。
     *                      箱子新的位置为 (bx,by)，（箱子移动方向是由玩家决定的，二者位置方向变化一致）
     *                      如果 (bx,by) 是一个合法的位置，且状态 (f(sx,sy),f(bx,by)) 没有被访问过，
     *                          那么我们就将 (f(sx,sy),f(bx,by)， d+1) 加入队列 q 的末尾，
     *                          并将 vis[f(sx,sy)][f(bx,by)] 标记为已访问。
     *                  如果不同，说明当前玩家没有推动箱子，那么我们只需要判断状态 (f(sx,sy),f(bi,bj)) 是否被访问过，
     *                          如果没有被访问过，那么我们就将 (f(sx,sy),f(bi,bj), d) 加入队列 q 的头部，
     *                              并将 vis[f(sx,sy)][f(bi,bj)] 标记为已访问。
     * 继续进行广度优先搜索，直到队列为空为止。
     *
     * 注意，
     *      如果推动箱子，那么推动次数 d 需要加 1，并且新的状态加入到队列 q 的【末尾】；
     *      如果没推动箱子，那么推动次数 d 不变，新的状态加入到队列 q 的【头部】。
     *
     * 最后，如果没有找到合法的推动方案，那么返回 −1。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/minimum-moves-to-move-a-box-to-their-target-location/solution/python3javacgotypescript-yi-ti-yi-jie-sh-xgcz/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/minimum-moves-to-move-a-box-to-their-target-location/solution/tui-xiang-zi-by-leetcode-solution-spzi/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    private int m, n;

    private char[][] grid;

    public int minPushBox(char[][] grid) {
        this.grid = grid;
        m = grid.length;
        n = grid[0].length;
        // 初始时，人的位置、箱子的位置
        int si = 0, sj = 0, bi = 0, bj = 0;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                // 箱子
                if (grid[i][j] == 'B') {
                    bi = i;
                    bj = j;
                // 人
                } else if (grid[i][j] == 'S') {
                    si = i;
                    sj = j;
                }
            }
        }
        // 方向数组
        final int[] dir = {-1, 0, 1, 0, -1};
        // bfs
        // 双端队列，节点 (人的位置，箱子位置，步数)
        Deque<int[]> queue = new ArrayDeque<>();
        // 访问过的节点位置，
        boolean[][] visited = new boolean[f(m, n)][f(m, n)];
        // 初始节点
        queue.offer(new int[]{f(si, sj), f(bi, bj), 0});
        // 标记已访问
        visited[f(si, sj)][f(bi, bj)] = true;
        // bfs
        while (!queue.isEmpty()) {
            int[] node = queue.poll();
            // 当前 人位置、箱子位置、步数
            int posS = node[0], posB = node[1], d = node[2];
            si = posS / n;
            sj = posS % n;
            bi = posB / n;
            bj = posB % n;
            // 箱子在目标位置，返回 步数
            if (grid[bi][bj] == 'T') {
                return d;
            }
            // 【人】 上下左右移动
            for (int i = 0; i < dir.length - 1; i++) {
                int sx = si + dir[i], sy = sj + dir[i + 1];
                // 无效位置
                if (!valid(sx, sy)) {
                    continue;
                }
                // 人移动到箱子位置，则箱子被推动，人是往哪个方向移动的，箱子就被向哪个方向推动
                if (sx == bi && sy == bj) {
                    // 箱子新的位置，和人的移动轨迹一致
                    int bx = bi + dir[i], by = bj + dir[i + 1];
                    // 无效的位置，或者 (新的人、新的箱子)位置已经访问过，跳过
                    if (!valid(bx, by) || visited[f(sx, sy)][f(bx, by)]) {
                        continue;
                    }
                    // 加入新节点 （新的人、新的箱子、步数 + 1）
                    queue.offer(new int[]{f(sx, sy), f(bx, by), d + 1});
                    // 标记已访问
                    visited[f(sx, sy)][f(bx, by)] = true;
                // 人没有移动到箱子位置，且是有效位置
                } else if (!visited[f(sx, sy)][posB]) {
                    // 加入新节点 （新的人、旧的箱子、步数）
                    // 【注意】这里 必须插入队列首部
                    queue.offerFirst(new int[]{f(sx, sy), posB, d});
                    // 标记已访问
                    visited[f(sx, sy)][posB] = true;
                }
            }
        }
        // 其他，返回 -1
        return -1;
    }

    /**
     * 二维坐标 转 一维坐标
     * @param i
     * @param j
     * @return
     */
    private int f(int i, int j) {
        return i * n + j;
    }

    /**
     * 位置有效：不越界，不是墙
     * @param i
     * @param j
     * @return
     */
    private boolean valid(int i, int j) {
        return i >= 0 && i < m && j >= 0 && j < n && grid[i][j] != '#';
    }

}
