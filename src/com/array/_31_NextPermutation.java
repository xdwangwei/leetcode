package com.array;

/**
 * @author: wangwei
 * 2020/4/2 22:00
 * <p>
 * 实现获取下一个排列的函数，算法需要将给定数字序列重新排列成字典序中下一个更大的排列。
 * 如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）。
 * 必须[原地]修改，只允许使用额外常数空间。
 * <p>
 * 以下是一些例子，输入位于左侧列，其相应输出位于右侧列。
 * <p>
 * 1,2,3 → 1,3,2
 * 3,2,1 → 1,2,3
 * 1,1,5 → 1,5,1
 **/
public class _31_NextPermutation {
    /**
     * 题解链接：https://leetcode-cn.com/problems/next-permutation/solution/xia-yi-ge-pai-lie-by-leetcode/
     * 找到比当前序列大的下一个序列，相当于高位不变低位变，如1，2，3变为1，3，2
     * 如果是最大排列，则满足每个位置都是 a[i-1] >= a[i]，
     * 从后往前，找到 a[i - 1] < a[i]的位置元素4    如 2，4，7，5，3，1
     * 在 i - 1之后找一个合适的元素a[j]和a[i-1]交换，这个a[j]一定是大于a[i-1]的最小的元素,5
     * 交换后 2,5,7,4,3,1，这个并不是2,4,7,5,3,1的下一个，应该是2，5，1，3，4，7
     * 也就是把 i及之后的元素整个逆转
     * 其实从后往前找到的那个 i -1,就是破坏了从后往前递增关系的第一个元素，而交换后它并不会改变i-1之后的递增关系
     * 因为用于交换的a[j]一定是大于a[i-1]的元素中最小的那个，所以交换后a[i-1]换到j的位置，一定比它后后面的元素大
     * @param nums
     */
    public void solution(int[] nums) {
        int i = nums.length - 2;
        // 从后往前，找到第一个破坏递增关系的元素 2，[4]，7，6，5，3，1
        while (i >= 0 && nums[i] >= nums[i + 1]) --i;
        // 出现了目标元素
        if (i >= 0){
            // 从后面的元素(7,6,5,3,1)中找到比nums[i]大的所有元素最小的那个
            int j = nums.length - 1;
            while (j >= 0 && nums[j] <= nums[i]) --j;
            // 交换这两个元素
            swap(nums, i ,j);
        }
        // 交换后，反转从i往后的序列
        // 当前排列是最大方式排列时，第一个for循环结束后，i=-1，相当于从0位置开始反转
        reverseArray(nums, i + 1);
    }

    /**
     * 从指定位置处开始，反转后续元素
     * @param nums
     * @param startIndex
     */
    private void reverseArray(int[] nums, int startIndex) {
        int i = startIndex, j = nums.length - 1;
        while(i < j){
            swap(nums, i, j);
            ++i;
            --j;
        }
    }

    /**
     * 交换数组指定位置处两元素
     * @param nums
     * @param i
     * @param j
     */
    private void swap(int[] nums, int i, int j) {
        if (i == j) return;
        nums[i] = nums[i] ^ nums[j];
        nums[j] = nums[i] ^ nums[j];
        nums[i] = nums[i] ^ nums[j];
    }

    public static void main(String[] args) {
        new _31_NextPermutation().solution(new int[]{3, 2, 1, 0});
        new _31_NextPermutation().solution(new int[]{1, 3, 2});
    }
}
