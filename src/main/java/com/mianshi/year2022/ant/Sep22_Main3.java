package com.mianshi.year2022.ant;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/9/22 9:13
 * @description: Main3
 *
 * 输入n
 * 将 1到n^n这些数字填入n*n的矩阵中，要求每个2X2的小矩阵中元素和为奇数
 * 输出任意一种方案
 * 否则输出-1
 */

public class Sep22_Main3 {

    private static Map<String, Boolean> book = new HashMap<>();

    public static class Main {
        public static void main(String[] args) {
            Scanner in = new Scanner(System.in);
            int n = in.nextInt();
            int[][] board = new int[n][n];
            List<Integer> nums = new ArrayList<>(n * n);
            for (int i = 1; i <= n * n; ++i) {
                nums.add(i);
            }
            boolean[] visited = new boolean[n * n + 1];
            if (!backTrack(board, 0, 0, nums, visited)) {
                System.out.println(-1);
            }
        }
    }

    private static boolean backTrack(int[][] board, int row, int col, List<Integer> nums, boolean[] visited) {
        int n = board.length;
        if (col == n) {
            return backTrack(board, row + 1, 0, nums, visited);
        }
        if (row >= n) {
            printBoard(board);
            return true;
        }
        String key = getKey(board, row, col);
        if (book.containsKey(key)) {
            return book.get(key);
        }
        boolean flag = false;
        for (int i = 0; i < nums.size(); ++i) {
            if (visited[i] || !isValid(board, row, col, nums.get(i))) {
                continue;
            }
            board[row][col] = nums.get(i);
            visited[i] = true;
            if (backTrack(board, row, col + 1, nums, visited)) {
                flag = true;
                break;
            }
            board[row][col] = 0;
            visited[i] = false;
        }
        if (!key.equals("")) {
            book.put(key, flag);
        }
        return flag;
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        int n = board.length;
        if (right(n, row, col - 1) && right(n, row - 1, col) && right(n, row - 1, col  - 1)) {
            int sum = board[row][col - 1] + board[row - 1][col] + board[row - 1][col - 1] + num;
            if (sum % 2 == 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean right(int n, int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }

    private static String getKey(int[][] board, int row, int col) {
        int n = board.length;
        if (right(n, row, col - 1) && right(n, row - 1, col) && right(n, row - 1, col  - 1)) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i <= row; ++i) {
                for (int j = 0; j < col; ++j) {
                    list.add(board[i][j]);
                }
            }
            Collections.sort(list);
            return list.toString();
        }
        return "";
    }

    private static void printBoard(int[][] board) {
        int n = board.length;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (j < n - 1) {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.println(board[i][j]);
                }
            }
            System.out.println();
        }
    }
}
