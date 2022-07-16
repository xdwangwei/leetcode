package com.freefish.week01;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * @author wangwei
 * 2021/8/10 11:59
 * <p>
 * 判断矩阵是否有效
 * <p>
 * 给定 M = N^2(N为整数)，和一个 M x M 矩阵，判断其是不是一个数独地解。
 * <p>
 * 一个合法的数独地解应该满足：
 * 对每个子矩阵，数字1到M再其中恰好出现一个；
 * 对于全局矩阵，数字1到M在其每一行和每一列都恰好出现1次。
 * <p>
 * 输出的结果为 yes 或 no
 * <p>
 * 输入规模：1 <= N <= 70, 1 <= M << 4900，矩阵中的元素一定在 1 到 M 之间
 * <p>
 * 输入：
 * 1
 * 1
 * 输出：
 * yes
 * 输入：
 * 2
 * 1 2 3 4
 * 3 4 1 2
 * 2 1 4 3
 * 4 3 2 1
 * 输出：
 * yes
 * 输入：
 * 2
 * 1 1 1 1
 * 2 2 2 2
 * 3 3 3 3
 * 4 4 4 4
 * 输出：
 * yes
 */
public class Main2 {

    /**
     * 验证一个数独是否有效，先判断行，再判断列，在判断每个小数独
     *
     * @param m      大矩阵的长和宽 m = n * n
     * @param n      每个小矩阵的长和宽
     * @param sudoku
     * @return
     */
    public static boolean verifySudoku(int m, int n, int[][] sudoku) {
        //检查每行是否符合要求，同时检查数据规模是否符合要求
        HashSet<Integer> hashset = new HashSet<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                hashset.add(sudoku[i][j]);
            }
            if (hashset.size() < m) {
                return false;
            }
            hashset.clear();
        }
        //检查每列是否符合要求
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                hashset.add(sudoku[j][i]);
            }
            if (hashset.size() < m) {
                return false;
            }
            hashset.clear();
        }
        //检查每个n*n格子内是否符合要求
        for (int i = 0; i < m; i += n) {
            for (int j = 0; j < m; j += n) {
                for (int k = i; k < i + n; k++) {
                    for (int l = j; l < j + n; l++) {
                        hashset.add(sudoku[k][l]);
                    }
                }
                if (hashset.size() < m) {
                    return false;
                }
                hashset.clear();
            }
        }
        return true;
    }

    /**
     * 验证一个数独是否有效，一次遍历，利用 HashMap
     *
     * @param m      大矩阵的长和宽 m = n * n
     * @param n      每个小矩阵的长和宽
     * @param sudoku
     * @return
     */
    public static boolean verifySudoku2(int m, int n, int[][] sudoku) {
        // 判断是否符合规则
        // init data
        HashMap<Integer, Integer>[] rows = new HashMap[m];
        HashMap<Integer, Integer>[] columns = new HashMap[m];
        HashMap<Integer, Integer>[] boxes = new HashMap[m];
        for (int i = 0; i < m; i++) {
            rows[i] = new HashMap<>();
            columns[i] = new HashMap<>();
            boxes[i] = new HashMap<>();
        }
        // validate a board
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                int num = sudoku[i][j] - 1;
                int box_index = (i / n) * n + j / n;
                // keep the current cell value
                rows[i].put(num, rows[i].getOrDefault(num, 0) + 1);
                columns[j].put(num, columns[j].getOrDefault(num, 0) + 1);
                boxes[box_index].put(num, boxes[box_index].getOrDefault(num, 0) + 1);

                // check if this value has been already seen before
                if (rows[i].get(num) > 1 || columns[j].get(num) > 1 || boxes[box_index].get(num) > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 方法二的改进：用二进制长度为3个m的数字来记录已出现过的位置
     * 当m太大时，就没办法了，int 只能存32位，lang 只能存 64 位，矩阵是 9 * 9 时可以用 27位bit取代上面三个hashmap
     * 超出64时，需要的位数太长，超出lang的范围
     *
     * @param m      大矩阵的长和宽 m = n * n
     * @param n      每个小矩阵的长和宽
     * @param sudoku
     * @return
     */
    public static boolean verifySudoku3(int m, int n, int[][] sudoku) {
        // 判断是否符合规则
        // 记录每个数字出现的次数，int[i]是个整数，但是我们利用它的 二进制的后3*m位
        // 前m位记录每个数字在每个小矩阵中出现的次数，中间m位记录每个数字在每一行出现的次数，后m位记录每个数字在每一列中出现的次数
        int[] bitMap = new int[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                // 记录当前数字(索引)
                int num = sudoku[i][j] - 1;
                // 当前数字在之前出现过的位置
                int preLocations = bitMap[num];
                // 当前数字在哪个小矩阵中
                int matrixIndex = (i / n) * n + j / n;
                // 当前数字现在出现的位置
                int curLocations = (1 << (2 * m) + matrixIndex) | (1 << m + i) | (1 << j);
                // 如果重复出现，那么不符合要求
                if ((preLocations & curLocations) != 0) {
                    return false;
                }
                // 当前数字符合要求，更新它出现情况，判断下一个
                bitMap[num] = preLocations | curLocations;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 读取输入
        int n = scanner.nextInt();
        int m = n * n;
        int[][] array = new int[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                array[i][j] = scanner.nextInt();
            }
        }
        if (n == 1 || verifySudoku(m, n, array)) {
            System.out.println("yes");
        } else {
            System.out.printf("no");
        }
        scanner.close();
    }
}
