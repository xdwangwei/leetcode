package com.daily;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/10/26 10:57
 * @description: _862_ShortestSubarrayWithSumLeastK
 *
 * 862. 和至少为 K 的最短子数组
 * 给你一个整数数组 nums 和一个整数 k ，找出 nums 中和至少为 k 的 最短非空子数组 ，并返回该子数组的长度。如果不存在这样的 子数组 ，返回 -1 。
 *
 * 子数组 是数组中 连续 的一部分。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1], k = 1
 * 输出：1
 * 示例 2：
 *
 * 输入：nums = [1,2], k = 4
 * 输出：-1
 * 示例 3：
 *
 * 输入：nums = [2,-1,2], k = 3
 * 输出：3
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * -105 <= nums[i] <= 105
 * 1 <= k <= 109
 * 通过次数29,883提交次数128,882
 */
public class _862_ShortestSubarrayWithSumLeastK {


    /**
     * 不可行思路一：滑动窗口，
     * [left,right) 维护当前窗口，sum维护当前窗口内元素累加和，每当 sum >= k 时，更新答案，并缩小窗口
     *
     * 问题:
     *
     * 若数组中出现负数，如 nums = [84,-37,32,40,95] ， k = 167
     * 此时会得到结果5，也就是从左遍历到尾部，sum 才 满足 >= 167，这是因为里面出现了元素 -37，导致 sum 在中间反而变小
     * 实际上，如果 直接从 32 出发，那么后三个元素和就能达到 k
     *
     * 那么遇到负数是否能直接跳过呢？对于上面那个题目是可以的，但如果是下面这个
     *      nums = [2, -1, 2], k = 3，如果 跳过 -1，根本得不到答案，只有从头到尾扫描才能得到结果
     *
     * 所以这种思路下：你必须考虑跳过负数后能得到的结果，不跳过负数能得到的结果，当有多个负数时，只会比原题目更加负责
     * @param nums
     * @param k
     * @return
     */
    public int shortestSubarray(int[] nums, int k) {
        int n = nums.length, left = 0, right = 0, sum = 0, ans = n + 1;
        while (right < n) {
            // if (nums[right] < 0) {
            //     right++;
            //     left = right;
            //     sum = 0;
            //     continue;
            // }
            sum += nums[right++];
            while (sum >= k) {
                ans = Math.min(ans, right - left);
                sum -= nums[left++];
            }
        }
        return ans == n + 1 ? -1 : ans;
    }

    /**
     * 思路二：先计算前缀和数组，通过 85 / 97
     *      preSum[i] = sum(nums[0....i])
     * 通过 前缀和，就能将 子数组的和转为前缀和数组中两位置的差
     *      sum(nums[i...j]) = sum(nums[0...j]) - sum(nums[0...i-1]) = preSum[j] - preSum[i-1](当i=0时，没有后面这部分)
     *
     * 求出前缀和 后，我们可以写一个暴力算法，枚举所有满足 j>=i 且 preSum[j] - preSum[i-1] ≥ k 的子数组 [i,j]，取其中最小的 j-i+1 作为答案。
     *
     * @param nums
     * @param k
     * @return
     */
    public static int shortestSubarray2(int[] nums, int k) {
        // 初始化，答案有可能是n，所以初始化为n+1
        int n = nums.length, ans = n + 1;
        // 前缀和 preSum[i] = sum(nums[0....i])
        int[] preSum = new int[n];
        preSum[0] = nums[0];
        for (int i = 1; i < n; ++i) {
            preSum[i] = preSum[i - 1] + nums[i];
        }
        // 枚举所有满足 j>=i 且 preSum[j] - preSum[i-1] ≥ k 的子数组 [i,j]，取其中最小的 j-i+1 作为答案。
        for (int i = 0; i < n; ++i) {
            for (int j = i; j < n; ++j) {
                int diff = preSum[j];
                if (i > 0) {
                    diff -= preSum[i - 1];
                }
                if (diff >= k) {
                    ans = Math.min(ans, j - i + 1);
                    // 这里可以提前退出是因为
                    // 假如i,j位置元素和>=k,那么对i来说就不会考虑之后更长的位置了
                    break;
                }
            }
        }
        return ans == n + 1 ? -1 : ans;
    }

    /**
     * 方法二超时是因为时间复杂度为 O(n^2) 的，如何优化呢？
     *
     * 但这个暴力算法是 O(n^2) 的，如何优化呢？
     *
     * 其实从1中，可以看见：假如i,j位置元素和>=k,那么对i来说就不会考虑之后更长的位置了
     *
     * 因此我们可以在遍历preSum[i]的同时用某个合适的数据结构来维护遍历过的 preSum[j]，并及时移除无用的 s[j]。具体来说：（简化preSum用s表示）
     *
     * 遍历到s[i]时，考虑左边遍历过的某个s[j],
     *      如果s[i]-s[j]≥k（此时s[i]>s[j]），此时可以更新一次答案，而对于s[i]右边的数字来说，无论大小如何，都不可能再把s[j]当作子数组的左端点，因为那只会是更长的子数组。因此s[j]没有任何作用了，把它移除掉！
     *      如果s[i]<=s[j]，此时不用更新答案，对于s[i]右边的数字来说，如果把i作为左端点，元素和能够超过k，那么它就不可能再把s[j]当作子数组的左端点，因为这样肯定也能使元素和超过j，但这个肯定只会是更长的子数组。因此s[j]没有任何作用了，把它移除掉！
     *      做完这两个优化后，再把 s[i]s[i] 加到这个数据结构中。
     *
     * 可以看到优化二其实保证了数据结构中的 s[i] 会形成一个递增的序列，
     * 那么按照这个特点，优化一其实是从左往右判断并移除数据结构中的若干元素，、
     *                优化二则是从右往左判断并移除数据结构中的若干元素。
     *             因此我们需要一个数据结构，它支持移除最左端的元素和最右端的元素，以及在最右端添加元素，故选用双端队列。
     *
     *          注：由于双端队列的元素始终保持单调递增，因此这种数据结构也叫做单调队列。
     *
     * 另外对于上面两种优化，可以看到，对于s[i]，需要处理的是在它之前已经访问过的s[j]，因此我们可以在计算前缀数组s的过程中，完成上述过程！
     *
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/solution/liang-zhang-tu-miao-dong-dan-diao-dui-li-9fvh/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */
    public static int shortestSubarray3(int[] nums, int k) {
        // 初始化，答案有可能是n，所以初始化为n+1
        int n = nums.length, ans = n + 1;
        // 前缀和 preSum[i] = sum(nums[0....i])
        // 题目数据范围，可能会越界int
        long[] preSum = new long[n];
        // 已经遍历过的前缀和，这里因为要计算两个前缀和之间的距离，所以队列里保存的是当时的索引
        Deque<Integer> deque = new ArrayDeque<>();
        // 初始化，认为第一个元素左边的前缀和为0，对应的索引假设为-1
        deque.addLast(-1);
        // 前缀和顺序遍历
        for (int i = 0; i < n; ++i) {
            // preSum[i] = sum(nums[0....i])
            preSum[i] = (i > 0 ? preSum[i - 1] : 0) + nums[i];
            // 优化一：
            // 如果 [j...i]的前缀和 >= k，更新ans，并且i之后的位置，不再会把j作为左端点，因为长度更长，因此 从队列中移除掉 j
            // 特殊情况是，队列中是-1时，认为它对应的前缀和为0
            while (!deque.isEmpty() && preSum[i] - (deque.peekFirst() == -1 ? 0 : preSum[deque.peekFirst()]) >= k) {
                // 这里对应的子数组的长度是 s[i] - s[j] = sum(0...i) - sum(0...j)，长度是 j + 1 到 i，长度是 i - j
                ans = Math.min(ans, i - deque.pollFirst());
            }
            // 优化二：
            // 如果 preSum[i] <= preSum[j]，那么对于i右边的数字来说，如果把i作为左端点，元素和能够超过k，那么它就不可能再把j当作子数组的左端点，因为这样肯定也能使元素和超过j，但这个肯定只会是更长的子数组。
            //  因此s[j]没有任何作用了，把它移除掉！
            // 特殊情况是，队列中是-1时，认为它对应的前缀和为0
            while (!deque.isEmpty() && preSum[i] <= (deque.peekLast() == -1 ? 0 : preSum[deque.peekLast()])) {
                deque.pollLast();
            }
            // 把自己入队列
            deque.add(i);
        }
        // 返回，注意判断返回值
        return ans == n + 1 ? -1 : ans;
    }


    public static void main(String[] args) {
        System.out.println(shortestSubarray3(new int[]{45, 95, 97, -34, -42}, 21));
        System.out.println(shortestSubarray3(new int[]{17, 85, 93, -45, -21}, 150));
        System.out.println(shortestSubarray3(new int[]{2, -1, 2}, 3));
    }
}
