package com.daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/5/27 21:58
 * @description: _Mianshi_17_11_WordDistance
 *
 * 面试题 17.11. 单词距离
 * 有个内含单词的超大文本文件，给定任意两个不同的单词，找出在这个文件中这两个单词的最短距离(相隔单词数)。如果寻找过程在这个文件中会重复多次，而每次寻找的单词不同，你能对此优化吗?
 *
 * 示例：
 *
 * 输入：words = ["I","am","a","student","from","a","university","in","a","city"], word1 = "a", word2 = "student"
 * 输出：1
 * 提示：
 *
 * words.length <= 100000
 */
public class _Mianshi_17_11_WordDistance {


    /**
     * 方法一：两次遍历
     * 统计word1和word2全部出现位置
     * 然后，比较每两个出现位置的距离，返回最小值
     * @param words
     * @param word1
     * @param word2
     * @return
     */
    public int findClosest(String[] words, String word1, String word2) {
        // 统计word1和word2全部出现位置
        List<Integer> word1Index = new ArrayList<>();
        List<Integer> word2Index = new ArrayList<>();
        for (int i = 0; i < words.length; ++i) {
            // 统计word1和word2全部出现位置
            if (words[i].equals(word1)) {
                word1Index.add(i);
            } else if (words[i].equals(word2)) {
                word2Index.add(i);
            }
        }
        // 比较每两个出现位置的距离，返回最小值
        int min = Integer.MAX_VALUE;
        for (int idx1: word1Index) {
            for (int idx2: word2Index) {
                min = Math.min(min, Math.abs(idx1 - idx2));
            }
        }
        return min;
    }


    /**
     * 方法二：一次遍历，
     * 不需要提前储存所有位置，
     * 在遍历过程中，每遇到一次word1或word2就比较当前位置和对方上一次的位置距离，并更新word1或word2最近出现位置为当前位置
     * 这样，在一次次交替比较并更新后，最终实现了和方法一一样的效果
     * @param words
     * @param word1
     * @param word2
     * @return
     */
    public int findClosest2(String[] words, String word1, String word2) {
        int length = words.length;
        int ans = length;
        // 初始化，比较两单词都还未出现
        int index1 = -1, index2 = -1;
        for (int i = 0; i < length; i++) {
            String word = words[i];
            // 更新word1出现位置
            if (word.equals(word1)) {
                index1 = i;
            // 更新word2出现位置
            } else if (word.equals(word2)) {
                index2 = i;
            }
            // 当两个索引都有效时，计算当前位置距离，更新ans
            if (index1 >= 0 && index2 >= 0) {
                ans = Math.min(ans, Math.abs(index1 - index2));
            }
        }
        return ans;
    }
}
