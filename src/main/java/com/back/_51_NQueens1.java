package com.back;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangwei
 * 2020/4/8 17:33
 * <p>
 * n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 * 每个皇后可以攻击它同行、同列、左上方向、右上方向的皇后
 * <p>
 * 给定一个整数 n，返回所有不同的 n 皇后问题的解决方案。
 * <p>
 * 每一种解法包含一个明确的 n 皇后问题的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。
 * <p>
 * 示例:
 * <p>
 * 输入: 4
 * 输出: [
 * [".Q..",  // 解法 1
 * "...Q",
 * "Q...",
 * "..Q."],
 * <p>
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
public class _51_NQueens1 {

    // 最终结果
    private List<List<String>> res = new ArrayList<>();

    /**
     * 回溯法，从第一行第一个位置开始放皇后，每行只放一个，若当前选择不会产生攻击，则进入下一行，若某个选择会发生攻击，则回溯
     *
     * @param n
     * @return
     */
    public List<List<String>> solution1(int n) {
        // 用字符数组模拟棋盘
        char[][] board = new char[n][n];
        // 初始化所有位置为 .
        for (char[] rowChars : board) Arrays.fill(rowChars, '.');
        // 回溯法
        backTrack(board, 0);
        // 返回
        return res;
    }

    /**
     * 回溯
     * @param board
     * @param row 当前行 0 - board.length - 1
     */
    private void backTrack(char[][] board, int row) {
        // 成功执行到了最后，这条路符合要求，满足终止条件
        if (row == board.length) {
            // 当前棋盘 是一个解，转换为List并加入结果集
            res.add(charArrayToList(board));
            return;
        }
        // 遍历选择列表
        // 对当前行每个位置尝试放 Q
        for (int j = 0; j < board.length; j++) {
            // 排除无效选择
            if (!isVlaid(board, row, j))
                continue;
            // 做选择
            board[row][j] = 'Q';
            // 试探下一层
            backTrack(board, row + 1);
            // 撤销选择
            board[row][j] = '.';
        }
    }

    /**
     * 判断 若 board[row][col] = 'Q'，会不会攻击之前的皇后
     * 因为我们的试探是，每行放一个Q，接着去试探下一行，所以不用考虑同行攻击
     *
     * @param board
     * @param row
     * @param col
     * @return
     */
    private boolean isVlaid(char[][] board, int row, int col) {
        // 判断这一列，是否有其他皇后，因为之后的行还未试探，所以这里只需判断这一行之上的行的这一列
        for (int k = 0; k < row; k++)
            if (board[k][col] == 'Q')
                return false;
        // 判断左上方向上是否存在 Q
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 'Q')
                return false;
        }
        // 判断右上方向上是否存在 Q
        for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++) {
            if (board[i][j] == 'Q')
                return false;
        }
        return true;
    }

    /**
     * 将 二维字符数组转换为 List
     * @param board
     * @return
     */
    private List<String> charArrayToList(char[][] board) {
        ArrayList<String> list = new ArrayList<>();
        for (char[] rowChars : board)
            list.add(String.valueOf(rowChars));
        return list;
    }

    public static void main(String[] args) {
        new _51_NQueens1().solution1(4);
    }
}
