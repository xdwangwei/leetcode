package com.hot100;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/5/4 16:49
 * @description: _581_ShortestContinuousSubarray
 *
 * 581. 最短无序连续子数组
 * 给你一个整数数组 nums ，你需要找出一个 连续子数组 ，如果对这个子数组进行升序排序，那么整个数组都会变为升序排序。
 *
 * 请你找出符合题意的 最短 子数组，并输出它的长度。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [2,6,4,8,10,9,15]
 * 输出：5
 * 解释：你只需要对 [6, 4, 8, 10, 9] 进行升序排序，那么整个表都会变为升序排序。
 * 示例 2：
 *
 * 输入：nums = [1,2,3,4]
 * 输出：0
 * 示例 3：
 *
 * 输入：nums = [1]
 * 输出：0
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 104
 * -105 <= nums[i] <= 105
 *
 *
 * 进阶：你可以设计一个时间复杂度为 O(n) 的解决方案吗？
 */
public class _581_ShortestContinuousSubarray {


    /**
     * 方法一：排序
     * 思路与算法
     *
     * 我们将给定的数组 nums 表示为三段子数组拼接的形式，分别记作 numsA、numsB，numsC
     *
     * 当我们对 numsB 进行排序，整个数组将变为有序。换而言之，当我们对整个序列进行排序，numsA 和 numsC 都不会改变。
     *
     * 本题要求我们找到最短的 numsB，即找到最大的 numsA 和 numsC 的长度之和。
     * 因此我们将原数组 nums 排序与原数组进行比较，取最长的相同的前缀为 numsA，取最长的相同的后缀为 numsC，这样我们就可以取到最短的 numsB
     *
     * 具体地，我们创建数组 nums 的拷贝，记作数组 arr，并对该数组进行排序，
     * 然后我们从左向右找到第一个两数组不同的位置，即为 numsB 的左边界。同理也可以找到 numsB 右边界。
     * 最后我们输出 numsB 的长度即可。
     *
     * 特别地，当原数组有序时，numsB 的长度为 0，我们可以直接返回结果。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/shortest-unsorted-continuous-subarray/solution/zui-duan-wu-xu-lian-xu-zi-shu-zu-by-leet-yhlf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int findUnsortedSubarray(int[] nums) {
        int n = nums.length;
        // 只有一个元素
        if (n == 1) {
            return 0;
        }
        // 拷贝一份，并排序
        int[] arr = Arrays.copyOf(nums, n);
        Arrays.sort(arr);
        int i = 0;
        // 从左往右找到第一个改变了的元素位置
        for (; i < n; ++i) {
            if (arr[i] != nums[i]) {
                break;
            }
        }
        // 说明原数组是有序数组，直接返回0
        if (i == n) {
            return 0;
        }
        // 从右往左，找到第一个改变了的元素位置
        int j = n - 1;
        for (; j >= 0; --j) {
            if (arr[j] != nums[j]) {
                break;
            }
        }
        // 这两个元素之间的元素 就是 原数组中 需要排序的部分长度，并且是 最小的
        // 因为你完全可以从 left - 1 排序 到 right + 1， 最后结果都是一样的，但是 left-1 和 right+1 这两元素没必要排序，排序完还在原位置
        return j - i + 1;
    }


    /**
     *
     * 方法二：一次遍历
     * 思路与算法
     *
     * 假设 numsB 在 nums 中对应区间为 [left,right]。
     *
     * 注意到 numsB 和 numsC 中任意一个数都大于等于 numsA 中任意一个数。
     *
     * 因此有 numsA 中每一个数 都满足：nums[i](numsA) ≤ min[j](numsB,C),  i+1 <= j <= n-1
     *
     * 所以，我们可以从大到小枚举 i，用一个变量 minn 记录 。
     * 每次移动 i，都可以 O(1) 地更新 minn。
     * 因为要找i右边的最小值，肯定是从小到达枚举i的时候，从右往左找minn
     *
     * 这样最后一个使得不等式不成立的 i 即为 left。left 左侧即为 numsA 能取得的最大范围。
     *
     * 同理，我们可以用类似的方法确定 right。在实际代码中，我们可以在一次循环中同时完成左右边界的计算。
     *
     * 具体过程请看代码
     *
     * 【！注意】原理：最小元素左边比它大的元素都要参与排序，最大元素右边比它小的元素都要调整
     *
     * 特别地，我们需要特判 nums 有序的情况，此时 numsB 的长度为 0。
     * 当我们计算完成左右边界，即可返回 numsB 的长度。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/shortest-unsorted-continuous-subarray/solution/zui-duan-wu-xu-lian-xu-zi-shu-zu-by-leet-yhlf/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param nums
     * @return
     */
    public int findUnsortedSubarray2(int[] nums) {
        // numsA右边的最小值minn，numsC左边的最大值maxn，注意初始值
        int minn = Integer.MAX_VALUE, maxn = Integer.MIN_VALUE;
        // numsB的范围是 [left, right]
        int left = -1, right = -1;
        // 从右往左枚举i(numsA)，才能得到i的右边全部元素的最小值，注意i是递减
        // numsA中元素应该比右边的最小值更小
        for (int i = nums.length - 1; i >= 0; --i) {
            // minn已经是右边最小了，说明 i 到 minn 这部分需要被排序，，就属于 numB，也就是 left 要往左扩
            // 因为i是往左递减的，所以每次破坏时应该是 numsB的左边界继续往左扩
            // 注意这里不需要等号，相等时可排序可不排序，不影响结果，我们要尽可能排序少的元素
            if (nums[i] > minn) {
                // 【原理：最小元素左边比它大的元素都要参与排序，最大元素右边比它小的元素都要调整】
                left = i;
            } else {
                // 否则就更新minn
                minn = nums[i];
            }
        }
        // 如果原数组本身就有序，那么 left 就不会被更新，此时只要返回0
        if (left == -1) {
            return 0;
        }
        // 然后从左往右枚举i(numsC),求nums[i]左边的最大元素，注意i是递增
        // numsC中元素应该比左边的最小值更大
        for (int i = 0; i < nums.length; ++i) {
            // maxn已经是i左边最大的了，你不应该比它还大，说明 maxn 到 i 之间需要排序，，就属于 numB
            // 那么 right 就要往右扩
            if (nums[i] < maxn) {
                right = i;
            } else {
                // 更新maxn
                maxn = nums[i];
            }
        }
        // numsB 是 [left, right]
        return right - left + 1;
    }

    /**
     * 方法二的两次扫描可以一次完成
     * @param nums
     * @return
     */
    public int findUnsortedSubarray3(int[] nums) {
        int minn = Integer.MAX_VALUE, maxn = Integer.MIN_VALUE;
        // numsB的范围是 [left, right]
        int left = -1, right = -1;
        // 【原理：最小元素左边比它大的元素都要参与排序，最大元素右边比它小的元素都要调整】
        // 从右往左枚举i(numsA)，才能得到i的右边全部元素的最小值，注意i是递减
        // 然后从左往右枚举i(numsC),求nums[i]左边的最大元素，注意i是递增

        // 这里 i 从右往左，枚举的 是 numsC，能够更新的是 i 左边的最大值，也就是 maxn
        for (int i = 0; i < nums.length; ++i) {
            // numsC中元素应该比左边的maxn更大，否则 maxn到i这部分就要参与排序，也就属于 numsB
            if (nums[i] < maxn) {
                right = i;
            } else {
                // 否则就更新
                maxn = nums[i];
            }

            // i 从左往右，对应的 j 就从右往左，枚举的 是 numsA，能够更新的是 i 右边的最小值，也就是 minn
            int j = nums.length - i - 1;
            // numsA中元素应该都比右边最小值更小，否则从i到minn这部分就需要参与排序，就属于 numB
            if (nums[j] > minn) {
                left = j;
            } else {
                // 否则就更新minn
                minn = nums[j];
            }
        }
        // 注意区别原数组本身有序的情况
        return right == -1 ? 0 : right - left + 1;
    }
}
