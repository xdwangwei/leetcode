package com.daily;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author wangwei
 * @date 2022/5/21 9:28
 * @description: _961_NRepeatedElementInSize2NArray
 *
 * 961. 在长度 2N 的数组中找出重复 N 次的元素
 * 给你一个整数数组 nums ，该数组具有以下属性：
 *
 * nums.length == 2 * n.
 * nums 包含 n + 1 个 不同的 元素
 * nums 中恰有一个元素重复 n 次
 * 找出并返回重复了 n 次的那个元素。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,2,3,3]
 * 输出：3
 * 示例 2：
 *
 * 输入：nums = [2,1,2,5,3,2]
 * 输出：2
 * 示例 3：
 *
 * 输入：nums = [5,1,5,2,5,3,5,4]
 * 输出：5
 *
 *
 * 提示：
 *
 * 2 <= n <= 5000
 * nums.length == 2 * n
 * 0 <= nums[i] <= 104
 * nums 由 n + 1 个 不同的 元素组成，且其中一个元素恰好重复 n 次
 */
public class _961_NRepeatedElementInSize2NArray {

    /**
     * 一共2N个元素，包括 N + 1 个数字
     * 某个数组重复了N次，那么剩下元素都只出现一次
     * 比如 长度6，包含3个元素，1重复3次，1 2 1 3 1 4
     */


    /**
     * 方法一：hash表
     * @param nums
     * @return
     */
    public int repeatedNTimes(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num: nums) {
            if (set.contains(num)) {
                return num;
            }
            set.add(num);
        }
        return -1;
    }


    /**
     * 数学方法：假设重复N次的元素为x，
     *
     * 结论：这些x之间的间隔最大为2
     *
     * x出现n次，其他元素各出现1次，共占n个位置
     *
     * 如果相邻的 x 之间至少都隔了 2 个元素，那么数组的总长度至少为：n + 2(n - 1) = 3n - 2
     * 若要保证 3n - 2 == 2n，即 n = 2， 此时才能让x之间最多间隔2个元素，比如 [4 1 3 4]
     *
     * 当 n>2 时，因为 3n−2>2n，所以不可能满足任意两个x之间都有至少2个其他元素。
     * 因此一定存在两个相邻的 x，它们的位置是连续的，或者只隔了 1 个位置。
     * 也就是说 两个x之间的最小间隔是1
     *
     *
     * 综上，两个x之间的间隔，最近是相邻，最多间隔2
     * 我们只需要遍历所有间隔 2 个位置及以内的下标对，判断对应的元素是否相等即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/n-repeated-element-in-size-2n-array/solution/zai-chang-du-2n-de-shu-zu-zhong-zhao-chu-w88a/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int repeatedNTimes2(int[] nums) {
        int n = nums.length;
        // 两个x之间的间隔，最近是相邻，最多间隔2
        // 枚举下标间隔 1 2 3
        for (int gap = 1; gap <= 3; ++gap) {
            for (int i = 0; i + gap < n; ++i) {
                // 找到x
                if (nums[i] == nums[i + gap]) {
                    return nums[i];
                }
            }
        }
        // 不可能的情况
        return -1;
    }


    /**
     * 方法三：随机选择
     *
     * 我们可以每次随机选择两个不同的下标，判断它们对应的元素是否相等即可。如果相等，那么返回任意一个作为答案。
     *
     * 时间复杂度：期望 O(1)。
     * 选择两个相同元素的概率为 n/2n * (n-1)/2n == 1/4，因此期望 4 次结束循环。
     *
     * 空间复杂度：O(1)。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/n-repeated-element-in-size-2n-array/solution/zai-chang-du-2n-de-shu-zu-zhong-zhao-chu-w88a/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int repeatedNTimes3(int[] nums) {
        int n = nums.length;
        Random random = new Random();

        while (true) {
            // 选择两个相同元素的概率为 n/2n * (n-1)/2n == 1/4，因此期望 4 次结束循环。
            int x = random.nextInt(n), y = random.nextInt(n);
            if (x != y && nums[x] == nums[y]) {
                return nums[x];
            }
        }
    }
}
