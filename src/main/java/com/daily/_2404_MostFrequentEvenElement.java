package com.daily;

/**
 * @author wangwei
 * @date 2023/4/13 21:35
 * @description: _2404_MostFrequentEvenElement
 * 2404. 出现最频繁的偶数元素
 * 给你一个整数数组 nums ，返回出现最频繁的偶数元素。
 *
 * 如果存在多个满足条件的元素，只需要返回 最小 的一个。如果不存在这样的元素，返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [0,1,2,2,4,4,1]
 * 输出：2
 * 解释：
 * 数组中的偶数元素为 0、2 和 4 ，在这些元素中，2 和 4 出现次数最多。
 * 返回最小的那个，即返回 2 。
 * 示例 2：
 *
 * 输入：nums = [4,4,4,9,2,4]
 * 输出：4
 * 解释：4 是出现最频繁的偶数元素。
 * 示例 3：
 *
 * 输入：nums = [29,47,21,41,13,37,25,7]
 * 输出：-1
 * 解释：不存在偶数元素。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 2000
 * 0 <= nums[i] <= 105
 * 通过次数30,885提交次数52,973
 *
 */
public class _2404_MostFrequentEvenElement {

    /**
     * 模拟
     *
     * 统计 nums 中为偶数的元素的出现次数，返回 出现次数最多的 元素，如果 有多个，则 返回值最小的那个
     *
     * 元素值 不超过 100000，可以使用 数组代替 hash 表
     * @param nums
     * @return
     */
    public int mostFrequentEven(int[] nums) {
        // 统计每个元素出现次数
        int[] cnt = new int[(int) (1e5 + 1)];
        // 返回值，初始化为 -1，macCnt 记录当前最大出现次数
        int res = -1, maxCnt = 0;
        // 遍历nums元素
        for (int x : nums) {
            // 跳过奇数
            if ((x & 1) == 1) {
                continue;
            }
            // 偶数，如果 次数 超过 maxCnt，或 次数 == maxCnt 但比当前的res更小
            if (++cnt[x] > maxCnt || (x < res && cnt[x] == maxCnt)) {
                // 就更新 res 为当前元素
                res = x;
                // 更新最大次数 为 当前元素出现次数
                maxCnt = cnt[x];
            }
        }
        // 返回
        return res;
    }
}
