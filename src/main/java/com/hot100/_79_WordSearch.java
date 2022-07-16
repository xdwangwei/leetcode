package com.hot100;

/**
 * @author wangwei
 * 2022/4/16 17:40
 *
 * 79. 单词搜索
 * 给定一个 m x n 二维字符网格 board 和一个字符串单词 word 。如果 word 存在于网格中，返回 true ；否则，返回 false 。
 *
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
 * 输出：true
 * 示例 2：
 *
 *
 * 输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
 * 输出：true
 * 示例 3：
 *
 *
 * 输入：board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
 * 输出：false
 *
 *
 * 提示：
 *
 * m == board.length
 * n = board[i].length
 * 1 <= m, n <= 6
 * 1 <= word.length <= 15
 * board 和 word 仅由大小写英文字母组成
 */
public class _79_WordSearch {

    /**
     * 每个点可以往上下左右四个方向走
     */
    private final int[][] dirs = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};

    /**
     * 每个字符不能重复使用
     */
    private boolean[][] visited;

    /**
     * 方法一：回溯
     * 首先，可以从每一个格子开始，所以外围是一个 m x n 的循环
     * 对于每一个起点开始的回溯，保存沿途所有组成的字符串，当和目标串匹配时，返回true
     * @param board
     * @param word
     * @return
     */
    public boolean exist(char[][] board, String word) {
        int m = board.length, n = board[0].length;
        boolean res = false;
        // 每个格子只能用一次
        visited = new boolean[m][n];
        // 每个格子都可以作为起点
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                // 回溯的起点至少得和word首字符相同吧
                if (board[i][j] != word.charAt(0)) {
                    continue;
                }
                // 改变起点，重新回溯，需要将visited数组恢复，
                // 但是 因为全局共用visited，要么上一次回溯找到了答案，已经结束了，
                // 要么上一次所有路径走完了没找到答案，那这种情况下visited自然也被回溯为初始状态，所以不用充值
                // for (int k = 0; k < m; ++k) {
                //     Arrays.fill(visited[k], false);
                // }
                // 只要有1次成功就行
                res |= backTrack(board, i, j, new StringBuilder(), word);
                if (res) {
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 回溯，[i,j]当前遍历到的位置，下标不能越界，不能重复访问，否则直接返回false
     * @param board
     * @param i
     * @param j
     * @param builder 当前路径上的字符序列
     * @param word
     * @return
     */
    private boolean backTrack(char[][] board, int i, int j, StringBuilder builder, String word) {
        int m = board.length, n = board[0].length;
        // 剪枝
        if (i < 0 || j < 0 || i >= m || j >= n || visited[i][j]) {
            return false;
        }
        // 加入当前格子
        builder.append(board[i][j]);
        visited[i][j] = true;
        String key = builder.toString();
        // 找到目标，提前返回，注意回溯
        if (key.equals(word)) {
            builder.deleteCharAt(builder.length() - 1);
            visited[i][j] = false;
            return true;
        }
        // 剪枝，比目标字符串更长了，肯定不可能
        // 或者当前字符，和目标单词对应位置字符不一样，那也提前返回
        if (key.length() >= word.length() || board[i][j] != word.charAt(builder.length() - 1)) {
            builder.deleteCharAt(builder.length() - 1);
            visited[i][j] = false;
            return false;
        }
        // 从当前位置，继续往四周扩散
        boolean res = false;
        for (int[] dir : dirs) {
            int dx = dir[0], dy = dir[1];
            // 有一次成功，提前返回
            res |= backTrack(board, i + dx, j + dy, builder, word);
            if (res) {
                break;
            }
        }
        // 回溯当前格子
        builder.deleteCharAt(builder.length() - 1);
        visited[i][j] = false;
        return res;
    }

    /**
     * 优化回溯
     * @param board
     * @param word
     * @return
     */
    public boolean exist2(char[][] board, String word) {
        int m = board.length, n = board[0].length;
        boolean res = false;
        // 每个格子只能用一次
        visited = new boolean[m][n];
        // 每个格子都可以作为起点
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                // 改变起点，重新回溯，需要将visited数组恢复，
                // 但是 因为全局共用visited，要么上一次回溯找到了答案，已经结束了，
                // 要么上一次所有路径走完了没找到答案，那这种情况下visited自然也被回溯为初始状态，所以不用充值
                // for (int k = 0; k < m; ++k) {
                //     Arrays.fill(visited[k], false);
                // }
                // 只要有1次成功就好
                if (backTrack2(board, i, j, word, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 优化一下，因为我们每次回溯内只选择1个字符，那么其实我们只需要保证每次选择和word对应位置匹配的字符就可以，不用保存所有路径
     * @param board
     * @param i
     * @param j
     * @param k 当前需要选择的是与word[k]一样的字符
     * @param word
     * @return
     */
    private boolean backTrack2(char[][] board, int i, int j, String word, int k) {
        // 进入下一次回溯前已经做了坐标判断
        // 不匹配
        if (board[i][j] != word.charAt(k)) {
            return false;
        }
        // 匹配，并且 已经是 最后一个待匹配字符了，那么这里匹配成功
        if (k == word.length() - 1) {
            return true;
        }
        // 当前位置匹配，继续匹配后续字符
        visited[i][j] = true;
        // 从当前位置，继续往四周扩散
        boolean res = false;
        int m = board.length, n = board[0].length;
        for (int[] dir : dirs) {
            // 下一个临接位置
            int nx = i + dir[0], ny = j + dir[1];
            // 跳过非法位置
            if (nx < 0 || ny < 0 || nx >= m || ny >= n || visited[nx][ny]) {
                continue;
            }
            // 有一次成功，提前返回
            res |= backTrack2(board, nx, ny, word, k + 1);
            if (res) {
                break;
            }
        }
        // 回溯当前选择
        visited[i][j] = false;
        return res;
    }
}
