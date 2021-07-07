package com.recursion;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author wangwei
 * 2020/9/1 16:46
 *
 * 给定一个二维的矩阵，包含'X'和'O'（字母 O）。
 *
 * 找到所有被 'X' 围绕的区域，并将这些区域里所有的 'O' 用 'X' 填充。
 *
 * 示例:
 *
 * X X X X
 * X O O X
 * X X O X
 * X O X X
 * 运行你的函数后，矩阵变为：
 *
 * X X X X
 * X X X X
 * X X X X
 * X O X X
 * 解释:
 *
 * 被围绕的区间不会存在于边界上，换句话说，任何边界上的'O'都不会被填充为'X'。
 * 任何不在边界上，或不与边界上的'O'相连的'O'最终都会被填充为'X'。如果两个元素在水平或垂直方向相邻，则称它们是“相连”的。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/surrounded-regions
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _130_SurroundedRegions {

    /**
     * 注意哦，必须是四面被围的 O 才能被换成 X，
     * 也就是说【边角上】的 O 一定不会被围，
     * 进一步，与边角上的 O 相连的 O 也不会被 X 围四面，也不会被替换。
     *
     * 先用 for 循环遍历棋盘的四边，把那些与边界相连的 O 换成一个特殊字符，比如 #；
     * 然后再遍历整个棋盘，把剩下的 O 换成 X，把 # 恢复成 O。
     * 时间复杂度 O(MN)。
     * @param board
     */
    public void solve(char[][] board) {
        if (board == null || board.length == 0) return;
        // dfs
        solve1(board);
        // bfs
        // solve1(board);
    }

    /**
     * dfs
     * @param board
     */
    private void solve1(char[][] board) {
        int m = board.length, n = board[0].length;
        // 遍历棋盘的四边，把那些与边界相连的 O 换成 #
        // 第一列和最后一列
        for (int i = 0; i < m; i++) {
            dfs(board, i, 0);
            dfs(board, i, n - 1);
        }
        // 第一行和最后一行
        for (int j = 1; j < n - 1; j++) {
            dfs(board, 0, j);
            dfs(board, m - 1, j);
        }
        // 再遍历整个棋盘，把剩下的 O 换成 X，把 # 恢复成 O。
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O')
                    board[i][j] = 'X';
                else if (board[i][j] == '#')
                    board[i][j] = 'O';
            }
        }
        return;
    }
    // 把和 board[i][j]想连的点都改为 #
    private void dfs(char[][] board, int i, int j) {
        // 递归出口
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != 'O')
            return;
        board[i][j] ='#';
        // 递归它的上下左右点
        dfs(board, i + 1, j);
        dfs(board, i - 1, j);
        dfs(board, i, j - 1);
        dfs(board, i, j + 1);
    }


    /**
     * bfs
     * @param board
     */
    private void solve2(char[][] board) {
        // 寻找上下左右点，常规操作
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        int m = board.length, n = board[0].length;

        Queue<int[]> queue = new LinkedList<>();
        // 遍历棋盘的四边，把那些与边界相连的 O 换成 #
        // 第一列和最后一列的'O'入队列
        for (int i = 0; i < m; i++) {
            if (board[i][0] == 'O')
                queue.offer(new int[]{i , 0});
            if (board[i][n - 1] == 'O')
                queue.offer(new int[]{i , n - 1});
        }
        // 第一行和最后一行的'O'入队列
        for (int j = 1; j < n - 1; j++) {
            if (board[0][j] == 'O')
                queue.offer(new int[]{0, j});
            if (board[m - 1][j] == 'O')
            queue.offer(new int[]{m - 1, j});
        }

        // 找到所有和四边上的'O'连接的点入队列，并改成 '#'
        while (!queue.isEmpty()) {
            int[] ceil = queue.poll();
            int x = ceil[0], y = ceil[1];
            board[x][y] = '#';
            // 判断上下左右
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n
                        || board[nx][ny] != 'O')
                    continue;
                queue.offer(new int[]{nx, ny});
            }
        }
        // 再遍历整个棋盘，把剩下的 O 换成 X，把 # 恢复成 O。
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O')
                    board[i][j] = 'X';
                else if (board[i][j] == '#')
                    board[i][j] = 'O';
            }
        }
        return;
    }
}
