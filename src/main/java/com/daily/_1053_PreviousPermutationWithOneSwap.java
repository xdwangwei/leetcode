package com.daily;

/**
 * @author wangwei
 * @date 2023/4/3 21:12
 * @description: _1053_PreviousPermutationWithOneSwap
 *
 * 1053. 交换一次的先前排列
 * 给你一个正整数数组 arr（可能存在重复的元素），请你返回可在 一次交换（交换两数字 arr[i] 和 arr[j] 的位置）后得到的、按字典序排列小于 arr 的最大排列。
 *
 * 如果无法这么操作，就请返回原数组。
 *
 *
 *
 * 示例 1：
 *
 * 输入：arr = [3,2,1]
 * 输出：[3,1,2]
 * 解释：交换 2 和 1
 * 示例 2：
 *
 * 输入：arr = [1,1,5]
 * 输出：[1,1,5]
 * 解释：已经是最小排列
 * 示例 3：
 *
 * 输入：arr = [1,9,4,6,7]
 * 输出：[1,7,4,6,9]
 * 解释：交换 9 和 7
 *
 *
 * 提示：
 *
 * 1 <= arr.length <= 104
 * 1 <= arr[i] <= 104
 * 通过次数22,394提交次数46,935
 */
public class _1053_PreviousPermutationWithOneSwap {


    /**
     * 方法一：贪心
     *
     * 要想交换i、j后小于原arr，那么交换前必须满足 arr[i] > arr[j]
     * 要在所有可行的交换中找最大值，那么交换位置应尽量靠右，
     *
     * 因此
     * 我们先从右到左遍历数组，找到第一个满足 arr[i−1]>arr[i] 的下标, arr[i−1] 就是我们要交换的数字
     * （arr[i...n-1]是非递减的，交换这个范围内的元素只会得到比原来更大或与原来一样的结果）
     *
     * 如果最后 i==0，说明这是一个非递减数组，不可能满足题意，返回数组本身
     *
     * 对于 arr[i-1]，从 arr[i....n-1] 随便选一个 arr[j] 满足 arr[j] < arr[i-1] 进行交换，都可满足要求
     * 但是希望交换后的结果尽可能大，所以希望 arr[j] 尽可能大
     *
     * 所以，在 arr[i...n-1] 中寻找 满足 < arr[i-1] 的最大值所在位置，
     * 这个子数组是非递减的，可以使用二分搜索，也可以顺序遍历，这里选择顺序遍历
     *
     * 因为要找最大值，所以 从右到左遍历数组arr[n-1...i]，找到第一个满足 arr[j] < arr[i−1] 的下标 j，
     * 由于可能存在重复元素，若 arr[j] == arr[j-1]，且都是 arr[n-1...i] 内满足  arr[j] < arr[i−1] 的最大值
     * 那么应该选择 j-1，因为 j-1 位置是高位， 把 arr[i-1] 放在高位上结果更大
     *
     * 最后，交换 arr[i-1] 和 arr[j]
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/previous-permutation-with-one-swap/solution/python3javacgotypescript-yi-ti-yi-jie-ta-pxxt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param arr
     * @return
     */
    public int[] prevPermOpt1(int[] arr) {
        int n = arr.length, i = n - 1;
        // 从右到左遍历数组，找到第一个满足 arr[i−1]>arr[i] 的下标, arr[i−1] 就是我们要交换的数字
        while (i >= 1 && arr[i] >= arr[i - 1]) {
            i--;
        }
        // 如果最后 i==0，说明这是一个非递减数组，不可能满足题意，返回数组本身
        if (i == 0) {
            return arr;
        }
        int j = n - 1;
        // 从右到左遍历数组arr[n-1...i]，找到第一个满足 arr[j] < arr[i−1] 的下标 j，
        // 若存在 arr[j] == arr[j-1]，那么应该选择 j-1，因为 j-1 位置是高位， 把 arr[i-1] 放在高位上结果更大
        while (arr[j] >= arr[i] || arr[j] == arr[j - 1]) {
            j--;
        }
        // 最后，交换 arr[i-1] 和 arr[j]
        arr[i - 1] = arr[i - 1] ^ arr[j];
        arr[j] = arr[i - 1] ^ arr[j];
        arr[i - 1] = arr[i - 1] ^ arr[j];
        // 返回
        return arr;
    }
}
