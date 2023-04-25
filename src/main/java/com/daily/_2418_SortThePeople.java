package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/4/25 21:20
 * @description: _2418_SortThePeople
 *
 * 2418. 按身高排序
 * 给你一个字符串数组 names ，和一个由 互不相同 的正整数组成的数组 heights 。两个数组的长度均为 n 。
 *
 * 对于每个下标 i，names[i] 和 heights[i] 表示第 i 个人的名字和身高。
 *
 * 请按身高 降序 顺序返回对应的名字数组 names 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：names = ["Mary","John","Emma"], heights = [180,165,170]
 * 输出：["Mary","Emma","John"]
 * 解释：Mary 最高，接着是 Emma 和 John 。
 * 示例 2：
 *
 * 输入：names = ["Alice","Bob","Bob"], heights = [155,185,150]
 * 输出：["Bob","Alice","Bob"]
 * 解释：第一个 Bob 最高，然后是 Alice 和第二个 Bob 。
 *
 *
 * 提示：
 *
 * n == names.length == heights.length
 * 1 <= n <= 103
 * 1 <= names[i].length <= 20
 * 1 <= heights[i] <= 105
 * names[i] 由大小写英文字母组成
 * heights 中的所有值互不相同
 * 通过次数13,415提交次数17,516
 */
public class _2418_SortThePeople {

    /**
     * 方法一：排序
     * 思路与算法
     *
     * 我们可以将 names[i] 和 heights[i] 绑定为一个二元组，然后对所有的二元组按照 heights 排序。最后取出其中的 names 即为答案。
     *
     * 除了以上方法，我们还可以创建一个索引数组 indices，其中 indices[i] = i。
     *
     * 对 indices 排序，排序规则是 其对应的 heights 元素，即 heights[indices[i]] 降序
     *
     * 排序完成后，对于所有的  i,j(i<j) 都有 heights[indices[i]] > heights[indices[j]]。
     *
     * 然后我们遍历 i 从 0 到 n−1，将 names[indices[i]] 追加到答案数组中。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/sort-the-people/solution/an-shen-gao-pai-xu-by-leetcode-solution-p6bk/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param names
     * @param heights
     * @return
     */
    public String[] sortPeople(String[] names, int[] heights) {
        int n = names.length;
        // 创建索引数组并初始化
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        // 索引数组排序，按照其对应（元素值作为heights数组的索引）的 heights 元素值
        Arrays.sort(indices, (a, b) -> heights[b] - heights[a]);
        String[] res = new String[n];
        // 得到答案
        for (int i = 0; i < n; i++) {
            // 取 names[indices[i]]
            res[i] = names[indices[i]];
        }
        // 返回
        return res;
    }
}
