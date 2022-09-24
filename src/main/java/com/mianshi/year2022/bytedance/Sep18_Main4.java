package com.mianshi.year2022.bytedance;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 书柜
 *
 * n 本书，每组数最高最低不能超过 k
 * 每组书必须按时间顺序摆放，可以 12一组，34一组，不能13一组
 * 输出能摆出几组，每组书按时间顺序输出书序号
 * 可以有多种答案
 */
public class Sep18_Main4 {

    static int n;
    static int threshold;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // n 本书
        n = in.nextInt();
        // 每一组内书最高最低不能超过k
        threshold = in.nextInt();
        int[] heights = new int[n];
        // 按时间顺序的n本书的高度
        for (int i = 0; i < n; ++i) {
            heights[i] = in.nextInt();
        }
        backTrack(heights, new ArrayList<>(), 0);
    }
    
    private static void backTrack(int[] heights, List<Integer> books, int idx) {
        if (books.isEmpty()) {
            books.add(idx);
            backTrack(heights, books, idx + 1);
        }
        for (int i = idx; i < n; ++i) {
            books.add(i);
            backTrack(heights, books, i + 1);
            books.remove(books.size() - 1);
        }
    }
}