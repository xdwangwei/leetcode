package com.back;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author wangwei
 * 2020/4/8 17:33
 * <p>
 * n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 * 每个皇后可以攻击它同行、同列、左上方向、右上方向的皇后
 * <p>
 * 给定一个整数 n，返回 n 皇后不同的解决方案的数量。
 * <p>
 * 示例:
 * <p>
 * 输入: 4
 * 输出: 2
 * 解释: 4皇后问题存在如下两个不同的解法
 * [
 * [".Q..",  // 解法 1
 * "...Q",
 * "Q...",
 * "..Q."],
 * ["..Q.",  // 解法 2
 * "Q...",
 * "...Q",
 * ".Q.."]
 * ]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/n-queens
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _52_NQueens2 {

    private int answers = 0;

    /**
     * 回溯法，从第一行第一个位置开始，尝试放一个皇后，进入下一行，会这个选择能一直进行到最后一行
     * 则这是一个成功的选择，答案数量加1，否则进行回溯，撤销这次选择，在下一个位置放皇后，重复这个过程
     *
     * @param n
     * @return
     */
    public int totalNQueens(int n) {
        if (n <= 0) return 0;
        char[][] board = new char[n][n];
        // 模拟一个棋盘并初始化
        for (int i = 0; i < board.length; i++)
            Arrays.fill(board[i], '.');
        // 回溯求解
        backTrack(board, 0);
        return answers;
    }

    private void backTrack(char[][] board, int row) {
        // 达到底层，找到一条成功路径
        if (row == board.length)
            answers++;
        // 遍历选择列表，做选择
        for (int j = 0; j < board.length; j++) {
            // 排除不合法选择
            if (!isValid(board, row, j))
                continue;
            // 做选择，在当前位置放一个皇后
            board[row][j] = 'Q';
            // 试探下一步,一行只需要一个皇后，所以直接进入下一行
            backTrack(board, row + 1);
            // 撤销选择
            board[row][j] = '.';
        }
    }

    /**
     * 判断在当前位置放一个皇后，是否会发生攻击
     * 因为我们的皇后是逐行放置的，所以不用考虑当前行以下
     *
     * @param board
     * @param row
     * @param col
     * @return
     */
    private boolean isValid(char[][] board, int row, int col) {
        // 判断当前列上是否已有皇后
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 'Q')
                return false;
        }
        // 判断左上方向上是否已有皇后
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 'Q')
                return false;
        }
        // 判断右上方向上是否已有皇后
        for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++) {
            if (board[i][j] == 'Q')
                return false;
        }
        return true;
    }

    /**
     * 简化1
     * 对于每个位置来说，与它在同一个有对角线上的位置满足 i - j == 常数
     * 与他在同一个左对角线上的位置满足 i + j == 常数
     * 与他在同一列上的位置满足 j 相等
     * 不用考虑行，因为我们还是从第一行开始，试探每个位置，然后直接进入下一行
     * 一行只会放置一个皇后，下一行只要避免 同一列、同一对角线即可
     *
     * @param n
     */
    public int totalNQueens2(int n) {
        if (n <= 0) return 0;
        // 同一列
        HashSet<Integer> sameColSet = new HashSet<>();
        // 同一左对角线
        HashSet<Integer> sameMainDiagonalSet = new HashSet<>();
        // 同一右对角线
        HashSet<Integer> sameSubDiagonalSet = new HashSet<>();
        // 模拟一个棋盘并初始化
        // 回溯求解
        backTrack2(n, 0, sameColSet, sameMainDiagonalSet, sameSubDiagonalSet);
        return answers;
    }

    /**
     * 回溯2
     * @param N
     * @param row
     * @param sameColSet
     * @param sameMainDiagonalSet
     * @param sameSubDiagonalSet
     */
    private void backTrack2(int N, int row,
                            HashSet<Integer> sameColSet,
                            HashSet<Integer> sameMainDiagonalSet,
                            HashSet<Integer> sameSubDiagonalSet) {
        // 找到一个解
        if (row == N) {
            answers++;
            return;
        }
        // 在当前行找位置放皇后
        for (int j = 0; j < N; j++) {
            // 排除无效选择
            if (sameColSet.contains(j)
                    || sameMainDiagonalSet.contains(row + j)
                    || sameSubDiagonalSet.contains(row - j))
                continue;
            // 做选择
            sameColSet.add(j);
            sameMainDiagonalSet.add(row + j);
            sameSubDiagonalSet.add(row - j);
            // 进入下一层
            backTrack2(N, row + 1, sameColSet, sameMainDiagonalSet, sameSubDiagonalSet);
            // 撤销选择
            sameColSet.remove(j);
            sameMainDiagonalSet.remove(row + j);
            sameSubDiagonalSet.remove(row - j);
        }
    }

    public static void main(String[] args) {
        System.out.println(new _52_NQueens2().totalNQueens(4));
    }
}
