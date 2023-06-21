package com.daily;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author wangwei
 * @date 2023/6/21 19:21
 * @description: _LCP_41_BlackWhiteReverse
 *
 * LCP 41. 黑白翻转棋
 * 在 n*m 大小的棋盘中，有黑白两种棋子，黑棋记作字母 "X", 白棋记作字母 "O"，空余位置记作 "."。当落下的棋子与其他相同颜色的棋子在行、列或对角线完全包围（中间不存在空白位置）另一种颜色的棋子，则可以翻转这些棋子的颜色。
 *
 * 1.gif2.gif3.gif
 *
 * 「力扣挑战赛」黑白翻转棋项目中，将提供给选手一个未形成可翻转棋子的棋盘残局，其状态记作 chessboard。若下一步可放置一枚黑棋，请问选手最多能翻转多少枚白棋。
 *
 * 注意：
 *
 * 若翻转白棋成黑棋后，棋盘上仍存在可以翻转的白棋，将可以 继续 翻转白棋
 * 输入数据保证初始棋盘状态无可以翻转的棋子且存在空余位置
 * 示例 1：
 *
 * 输入：chessboard = ["....X.","....X.","XOOO..","......","......"]
 *
 * 输出：3
 *
 * 解释：
 * 可以选择下在 [2,4] 处，能够翻转白方三枚棋子。
 *
 * 示例 2：
 *
 * 输入：chessboard = [".X.",".O.","XO."]
 *
 * 输出：2
 *
 * 解释：
 * 可以选择下在 [2,2] 处，能够翻转白方两枚棋子。
 * 2126c1d21b1b9a9924c639d449cc6e65.gif
 *
 * 示例 3：
 *
 * 输入：chessboard = [".......",".......",".......","X......",".O.....","..O....","....OOX"]
 *
 * 输出：4
 *
 * 解释：
 * 可以选择下在 [6,3] 处，能够翻转白方四枚棋子。
 * 803f2f04098b6174397d6c696f54d709.gif
 *
 * 提示：
 *
 * 1 <= chessboard.length, chessboard[i].length <= 8
 * chessboard[i] 仅包含 "."、"O" 和 "X"
 * 通过次数7,907提交次数12,219
 */
public class _LCP_41_BlackWhiteReverse {


    private int n, m;
    private String[] board;

    // 八个方向，左、右、上、下、左上、右上、左下、右下
    private final int[][] dirs = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}, {-1, 1}, {1, 1}, {-1, -1}, {1, -1}};
    private int ans;

    /**
     * 方法一：BFS
     *
     * 我们注意到，题目中棋盘的大小最大为 8×8，
     * 因此，我们可以尝试枚举所有的空余位置作为下一步放置黑棋的位置，
     * 然后使用广度优先搜索的方法计算在该位置下可以翻转的白棋的数量，找出最大值即可。
     *
     * 我们定义一个函数 bfs(i,j)，表示在棋盘上放置黑棋在 (i,j) 位置后，可以翻转的白棋的数量。
     *
     * 在函数中，
     * 我们使用队列来进行广度优先搜索，
     * 初始时将 (i,j) 放入队列中，
     * 然后不断取出队首位置，
     *      遍历棋盘的八个方向，
     *      如果该方向上存在一段连续的白棋，且在末尾是黑棋，
     *      则将该黑棋之前的所有白棋都可以翻转，将这些白棋的位置放入队列中，继续进行广度优先搜索。
     * 最后，我们返回可以翻转的白棋的数量。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/fHi6rV/solution/python3javacgo-yi-ti-yi-jie-bfs-by-lcbin-e4vo/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param chessboard
     * @return
     */
    public int flipChess(String[] chessboard) {
        n = chessboard.length;
        m = chessboard[0].length();
        board = chessboard;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                // 尝试在所有空位放置黑棋后能够翻转的白棋数量，取最大值
                if (board[i].charAt(j) == '.') {
                    ans = Math.max(ans, bfs(i, j));
                }
            }
        }
        return ans;
    }

    /**
     * 在 board[i][j] 位置放置黑棋后，能够翻转的白棋数量
     * @param i
     * @param j
     * @return
     */
    private int bfs(int i, int j) {
        // 棋牌要多次使用，不能原地修改，先拷贝得到新的棋牌，
        char[][] g = new char[n][0];
        for (int k = 0; k < n; ++k) {
            g[k] = board[k].toCharArray();
        }
        // 返回值
        int ret = 0;
        // bfs
        Deque<int[]> q = new ArrayDeque<>();
        // 队列节点，位置，先加入起点
        q.offer(new int[]{i, j});
        // 此处放黑棋
        g[i][j] = 'X';
        // bfs
        while (!q.isEmpty()) {
            // 出队列
            int[] cur = q.poll();
            i = cur[0];
            j = cur[1];
            // 遍历八个方向
            for (int k = 0; k < 8; ++k) {
                // 判断在该方向是否存在一段连续的白棋，且末尾是黑棋
                if (judge(g, i, j, dirs[k][0], dirs[k][1])) {
                    // 如果存在，则将这一段连续的白棋都变成黑棋，并加入队列
                    int x = i + dirs[k][0], y = j + dirs[k][1];
                    while (g[x][y] != 'X') {
                        q.offer(new int[]{x, y});
                        g[x][y] = 'X';
                        x += dirs[k][0];
                        y += dirs[k][1];
                        // 更新ret
                        ++ret;
                    }
                }
            }
        }
        // 返回
        return ret;
    }

    /**
     * 判断从 g[x][y] 到 dx、dy 方向上是否存在一段连续的白棋，且末尾是黑棋
     * @param g
     * @param x
     * @param y
     * @param dx
     * @param dy
     * @return
     */
    public boolean judge(char[][] g, int x, int y, int dx, int dy) {
        // 此方向上第一个点
        x += dx;
        y += dy;
        // 不能越界
        while (0 <= x && x < n && 0 <= y && y < m) {
            // 遇到黑棋，结束，说明 存在
            if (g[x][y] == 'X') {
                return true;
            //  不存在，被 '.' 截断不算
            } else if (g[x][y] == '.') {
                return false;
            }
            // 此方向下一个位置
            x += dx;
            y += dy;
        }
        // 其他，返回 false
        return false;
    }
}
