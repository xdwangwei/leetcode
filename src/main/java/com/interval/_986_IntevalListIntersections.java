package com.interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangwei
 * 2020/8/28 20:10
 *
 *
给定两个由一些 闭区间 组成的列表，每个区间列表都是成对不相交的，并且已经排序。

返回这两个区间列表的交集。

（形式上，闭区间 [a, b]（其中 a <= b）表示实数 x 的集合，而 a <= x <= b。两个闭区间的交集是一组实数，要么为空集，要么为闭区间。例如，[1, 3] 和 [2, 4] 的交集为 [2, 3]。）



示例：



输入：A = [[0,2],[5,10],[13,23],[24,25]], B = [[1,5],[8,12],[15,24],[25,26]]
输出：[[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
 */
public class _986_IntevalListIntersections {

    /**
     * 给出的区间都已经排好序
     * 让B的每一个区间[b0,b1]都去和A的每一个区间[a0,a1]求交集
     * 什么情况下会相交不好分析。可以先考虑什么情况下不会相交，那么就是a[0]>b[1]或a[1]<b[0]
     * 对其取反，即当 b0 <= a1 并且 b1 >= a0 时二者才会有相交部分，相交部分应该为 [max(a0,b0),min(a1,b1)]
     * 但其实因为区间已有序，有些情况是不用去考虑的，肯定不会有交集
     * 比如如果B的第一个区间和A的前三个区间都没有交集，那么B的第二区间和那三个区间也肯定不会相交
     * 但这样的for循环会执行这种情况
     *          for (int[] intervalB : B) {
     *             for (int[] intervalA : A) {
     * @param A
     * @param B
     * @return
     */
    public int[][] intervalIntersection(int[][] A, int[][] B) {
        int m = A.length, n = B.length;
        // 保存全部交集
        int[][] res = new int[10000][2];
        // 有效的最后一个交集
        int index = -1;
        // 让B的每一个区间[b0,b1]都去和A的每一个区间[a0,a1]求交集
        for (int[] intervalB : B) {
            for (int[] intervalA : A) {
                // 当 b0 <= a1 并且 b1 >= a0 时二者才会有相交部分
                if (intervalB[0] <= intervalA[1] && intervalB[1] >= intervalA[0]) {
                    // 相交部分应该为 [max(a0,b0),min(a1,b1)]
                    res[++index][0] = Math.max(intervalA[0], intervalB[0]);
                    res[index][1] = Math.min(intervalA[1], intervalB[1]);
                }
            }
        }
        // 截取出有效部分
        return Arrays.copyOf(res, index + 1);
    }

    /**
     * 双指针，避免不必要的循环
     * 先考虑什么情况下不会相交，那么就是a[0]>b[1]或a[1]<b[0]
     * 对其取反，即当 b0 <= a1 并且 b1 >= a0 时二者才会有相交部分，
     * 相交部分应该为 [max(a0,b0),min(a1,b1)]
     */
    public int[][] intervalIntersection2(int[][] A, int[][] B) {
        int m = A.length, n = B.length;
        // 保存全部交集
        List<int[]> res = new ArrayList<>();
        // 让B的每一个区间[b0,b1]都去和A的每一个区间[a0,a1]求交集
        int i = 0, j = 0;
        while (i < m && j <n) {
            // 当 b0 <= a1 并且 b1 >= a0 时二者才会有相交部分
            if (B[j][0] <= A[i][1] && B[j][1] >= A[i][0]) {
                // 相交部分应该为 [max(a0,b0),min(a1,b1)]
                res.add(new int[]{Math.max(A[i][0], B[j][0]), Math.min(A[i][1], B[j][1])});
            }
            // i ,j 指针的后移，比较a和b的右边界哪个大
            if (A[i][1] < B[j][1]) i++;
            else j++;
        }
        // 截取出有效部分
        return res.toArray(new int[res.size()][]);
    }
}
