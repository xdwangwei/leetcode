package com.design;

import java.util.Random;

/**
 * @author wangwei
 * 2020/9/1 8:07
 *
 * 给定一个可能含有重复元素的整数数组，要求随机输出给定的数字的索引。 您可以假设给定的数字一定存在于数组中。
 *
 * 注意：
 * 数组大小可能非常大。 使用太多额外空间的解决方案将不会通过测试。
 *
 * 示例:
 *
 * int[] nums = new int[] {1,2,3,3,3};
 * Solution solution = new Solution(nums);
 *
 * // pick(3) 应该返回索引 2,3 或者 4。每个索引的返回概率应该相等。
 * solution.pick(3);
 *
 * // pick(1) 应该返回 0。因为只有nums[0]等于1。
 * solution.pick(1);
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/random-pick-index
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _398_RandomPiclIndex {

    private int[] nums;

    public _398_RandomPiclIndex(int[] nums) {
        this.nums = nums;
    }

    /**
     * 在所有值为target的元素对应的下标中随机选择一个即可
     * 仍然是蓄水池抽样问题，比如要找5，原数组中不知道有多少个5，要保证随机取出一个的概率相等
     * 则 对于第i个元素，以 1 / i 的概率选择它
     * @param target
     * @return
     */
    public int pick(int target) {
        int index = 0, i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] == target) {
                Random r = new Random();
                // 以 1/i 的概率选择当前元素
                if (r.nextInt(++i) == 0) {
                    index = j;
                }
            }
        }
        return index;
    }

}

