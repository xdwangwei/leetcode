package com.array;

import java.util.Random;

/**
 * @author wangwei
 * 2020/8/29 10:09
 *
 * 打乱一个没有重复元素的数组。
 *
 *  
 *
 * 示例:
 *
 * // 以数字集合 1, 2 和 3 初始化数组。
 * int[] nums = {1,2,3};
 * Solution solution = new Solution(nums);
 *
 * // 打乱数组 [1,2,3] 并返回结果。任何 [1,2,3]的排列返回的概率应该相同。
 * solution.shuffle();
 *
 * // 重设数组到它的初始状态[1,2,3]。
 * solution.reset();
 *
 * // 随机返回数组[1,2,3]打乱后的结果。
 * solution.shuffle();
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/shuffle-an-array
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _384_ShuffleArray {

    // 保存原始数组的备份
    private int[] original;
    // 用于修改的数组
    private int[] array;
    Random rand = new Random();

    // 分析洗牌算法正确性的准则：产生的结果必须有 n! 种可能，否则就是错误的
    public _384_ShuffleArray(int[] nums) {
        array = nums;
        // 引用传值，使用克隆
        original = nums.clone();
    }

    /** Resets the array to its original configuration and return it. */
    public int[] reset() {
        array = original;
        // 引用传值，使用克隆
        original = original.clone();
        return original;
    }

    /** Returns a random shuffling of the array. */
    // 分析洗牌算法正确性的准则：产生的结果必须有 n! 种可能，否则就是错误的
    // 假设数组长度为5
    public int[] shuffle() {
        // 第一次 i = 4
        for (int i = array.length - 1; i > 0 ; --i) {
            // nextInt(i)  --->   [0, i)，如果不加1，只可能产生0,1,2,3 4中结果
            // 第二次3种，第三次2种，第四次1中， 最终 4 * 3 * 2 * 1 = 4!，实际应该是5！
            int j = rand.nextInt(i + 1);
            swap(i, j);
        }
        return array;
    }

    // 交换 array数组中两个位置元素
    private void swap(int i, int j) {
        if (i == j) return;
        array[i] = array[i] ^ array[j];
        array[j] = array[i] ^ array[j];
        array[i] = array[i] ^ array[j];
    }

}
