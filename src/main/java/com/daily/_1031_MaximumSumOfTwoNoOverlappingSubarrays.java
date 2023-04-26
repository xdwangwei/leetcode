package com.daily;

/**
 * @author wangwei
 * @date 2023/4/26 21:16
 * @description: _1031_MaximumSumOfTwoNoOverlappingSubarrays
 *
 * 1031. 两个非重叠子数组的最大和
 * 给你一个整数数组 nums 和两个整数 firstLen 和 secondLen，请你找出并返回两个非重叠 子数组 中元素的最大和，长度分别为 firstLen 和 secondLen 。
 *
 * 长度为 firstLen 的子数组可以出现在长为 secondLen 的子数组之前或之后，但二者必须是不重叠的。
 *
 * 子数组是数组的一个 连续 部分。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [0,6,5,2,2,5,1,9,4], firstLen = 1, secondLen = 2
 * 输出：20
 * 解释：子数组的一种选择中，[9] 长度为 1，[6,5] 长度为 2。
 * 示例 2：
 *
 * 输入：nums = [3,8,1,3,2,1,8,9,0], firstLen = 3, secondLen = 2
 * 输出：29
 * 解释：子数组的一种选择中，[3,8,1] 长度为 3，[8,9] 长度为 2。
 * 示例 3：
 *
 * 输入：nums = [2,1,5,6,0,9,5,0,3,8], firstLen = 4, secondLen = 3
 * 输出：31
 * 解释：子数组的一种选择中，[5,6,0,9] 长度为 4，[0,3,8] 长度为 3。
 *
 *
 * 提示：
 *
 * 1 <= firstLen, secondLen <= 1000
 * 2 <= firstLen + secondLen <= 1000
 * firstLen + secondLen <= nums.length <= 1000
 * 0 <= nums[i] <= 1000
 * 通过次数21,537提交次数33,301
 */
public class _1031_MaximumSumOfTwoNoOverlappingSubarrays {

    private int[] preSum;

    /**
     * 方法一：前缀和 + 枚举
     *
     * 我们先预处理得到数组 nums 的前缀和数组 s，其中 s[i] 表示 nums 中前 i 个元素的和。
     *
     * 无非就是 firstLen 个元素的子数组 和 secondLen 个元素的子数组 谁左谁右的问题
     *
     * 那么接下来，我们分两种情况枚举：
     *
     * 假设 firstLen 个元素的子数组在 secondLen 个元素的子数组的左边，
     * 那么我们可以枚举 secondLen 个元素的子数组的左端点 i，
     * 用变量 t 维护左边 firstLen 个元素的子数组的最大和，
     * 那么当前最大和就是 t+s[i+secondLen]−s[i]。
     * 其中 s[i+secondLen]−s[i] 表示 secondLen 个元素的子数组的和。
     * 枚举完所有的 i，就得到了第一种情况下的最大和。
     *
     * 假设 secondLen 个元素的子数组在 firstLen 个元素的子数组的左边，
     * 那么我们可以枚举 firstLen 个元素的子数组的左端点 i，
     * 用变量 t 维护左边 secondLen 个元素的子数组的最大和，
     * 那么当前最大和就是 t+s[i+firstLen]−s[i]。
     * 其中 s[i+firstLen]−s[i] 表示 firstLen 个元素的子数组的和。
     * 枚举完所有的 i，就得到了第二种情况下的最大和。
     *
     * 取两种情况下的最大值作为答案即可。
     *
     * 两种枚举过程可以用一个函数实现，f(a,b) 表示长度为 a 的子数组 在 长度为b的子数组左边时，所有可能情况下的最大元素和
     *
     * 那么答案就是 max(f(firstLen,secondLen),f(secondLen,firstLen))。
     *
     * f(a,b)的具体枚举过程：
     *
     * 长度为a的子数组 arr1 在 长度为 b 的子数组 arr2 左边：
     *
     * 假设的 arr2 左端点位置为 i，那么 arr 的元素和为 s[i+b] - s[i]
     * arr1 长度为 a，那么右端点 j 从 a 开始，此时 arr1 元素和为 s[j+1] - s[j+1-a],  j 要小于 arr2 的左端点 i
     * 那么，可以 让 arr2 左端点为 i 的时候， i - 1 为 arr1 的右端点，
     * 这样，arr2 的元素和为 s[i+b] - s[i]，arr1 的元素和为 s[i] - s[i-a]
     * 用 maxLeftSum 记录 i（arr2）在向右枚举的过程中，arr1 的最大值，即 nums[i]左边部分，长度 为 a 的子数组的最大元素和
     * 这样，对于 每个 arr2的起点 i，就得到了 当前arr1最大值元素和 + 当前arr2元素和
     * 最后，在所有 arr2 的起点的枚举情况中，取 最大值
     *
     * arr2 的起点 i 枚举从 a 开始，保证 arr2 有 b 个元素，所以 i+b-a < n
     *         for (int i = a; i + b - 1 < n; ++i) {
     *             maxLeftSum = Math.max(maxLeftSum, preSum[i] - preSum[i - a]);
     *             ans = Math.max(ans, maxLeftSum + preSum[i + b] - preSum[i]);
     *         }
     *
     *
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/solution/python3javacgotypescript-yi-ti-yi-jie-qi-7nqt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param firstLen
     * @param secondLen
     * @return
     */
    public int maxSumTwoNoOverlap(int[] nums, int firstLen, int secondLen) {
        int n = nums.length;
        // 前缀和数组
        preSum = new int[n + 1];
        for (int i = 0; i < n; ++i) {
            preSum[i + 1] = preSum[i] + nums[i];
        }
        // firstLen 个元素的子数组 和 secondLen 个元素的子数组 谁左谁右 取更大值
        return Math.max(help(nums, firstLen, secondLen), help(nums, secondLen, firstLen));
    }

    /**
     * f(a,b) 表示长度为 a 的子数组 在 长度为b的子数组左边时，所有可能情况下的最大元素和
     * @param nums
     * @param a
     * @param b
     * @return
     */
    private int help(int[] nums, int a, int b) {
        int ans = 0;
        int n = nums.length;
        // i 左边部分，长度 为 a 的子数组的最大元素和
        int maxLeftSum = 0;
        // arr2 的起点 i
        for (int i = a; i + b - 1 < n; ++i) {
            // 更新 左边 arr1 的最大元素和
            maxLeftSum = Math.max(maxLeftSum, preSum[i] - preSum[i - a]);
            // 当前 arr2 元素和 + arr1最大元素和
            // 所有可能的 arr2 下，取最大值
            ans = Math.max(ans, maxLeftSum + preSum[i + b] - preSum[i]);
        }
        return ans;
    }
}
