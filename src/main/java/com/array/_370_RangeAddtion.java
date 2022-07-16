package com.array;

import com.common.DiffArray;

/**
 * @author wangwei
 * 2021/11/29 9:58
 *
 * 假设你有一个长度为 n 的数组，初始情况下所有的数字均为 0，你将会被给出 k 个更新的操作。
 * 其中，每个操作会被表示为一个三元组：[startIndex,endIndex,inc]，
 * 你需要将子数组A[startIndex ... endIndex] (包括startIndex 和endIndex)增加 inc。
 * 请你返回 k 次操作后的数组。
 *
 * 示例:
 * 输入：Length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]]
 * 输出：[-2,0,3,5,3]
 * 解释：
 * 初始状态：
 * [0,0,0,0,0]
 * 进行了操作 [1,3,2] 后的状态：
 * [0,2,2,2,0]
 * 进行了操作 [2,4,3] 后的状态：
 * [0,2,5,5,3]
 * 进行了操作 [0,2,-2] 后的状态：
 * [-2,0,3,5,3]
 */
public class _370_RangeAddtion {

    /**
     * 频繁对某个区间内元素进行同增量的增减操作，差分数组
     * @param length
     * @param updates
     * @return
     */
    public int[] solution(int length, int[][] updates) {
        // 根据传入的数组构造差分数组, 初始元素全是0
        int[] nums = new int[length];
        // 差分数组
        DiffArray diffArray = new DiffArray(nums);
        // 对某个区间内的元素统一进行增减操作，直接操作差分数组
        for (int[] update : updates) {
            int i = update[0];
            int j = update[1];
            int delta = update[2];
            diffArray.increment(i, j, delta);
        }
        // 由目前差分数组恢复出对应的数组
        return diffArray.result();
    }
}
