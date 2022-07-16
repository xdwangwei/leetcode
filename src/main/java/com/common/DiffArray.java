package com.common;

/**
 * @author wangwei
 * 2021/11/29 10:01
 * <p>
 * 差分数组工具类
 *
 * 差分数组的主要适用场景是【频繁】对原始数组的某个【区间】的元素进行【增减】。
 */
public class DiffArray {

    // 差分数组
    private int[] diff;

    /* 输入一个初始数组，区间操作将在这个数组上进行，构造它对应的差分数组 */
    // 先对 nums 数组构造一个 diff 差分数组，diff[i] 就是 nums[i] 和 nums[i-1] 之差
    public DiffArray(int[] nums) {
        assert nums.length > 0;
        diff = new int[nums.length];
        // 根据初始数组构造差分数组
        // 0 位置存贮原数组第一个元素
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }
    }

    /**
     * 给闭区间 [i,j]内每个元素值 增加 val（可以是负数）
     * 如果你想对区间 nums[i..j] 的元素全部加 3，那么只需要让 diff[i] += 3，然后再让 diff[j+1] -= 3 即可
     * diff[i] += 3 意味着给 nums[i..] 所有的元素都加了 3，然后 diff[j+1] -= 3 又意味着对于 nums[j+1..] 所有元素再减 3，那综合起来，是不是就是对 nums[i..j] 中的所有元素都加 3 了？
     * @param i
     * @param j
     * @param val
     */
    public void increment(int i, int j, int val) {
        diff[i] += val;
        if (j + 1 < diff.length) {
            diff[j + 1] -= val;
        }
    }

    /**
     * 过这个 diff 差分数组是可以反推出原始数组 nums
     * @return
     */
    public int[] result() {
        int[] res = new int[diff.length];
        // 根据差分数组构造结果数组
        res[0] = diff[0];
        for (int i = 1; i < diff.length; i++) {
            res[i] = res[i - 1] + diff[i];
        }
        return res;
    }
}
