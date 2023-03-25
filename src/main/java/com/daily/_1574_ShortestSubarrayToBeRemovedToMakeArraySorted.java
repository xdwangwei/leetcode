package com.daily;

/**
 * @author wangwei
 * @date 2023/3/25 19:20
 * @description: _1574_ShortestSubarrayToBeRemovedToMakeArraySorted
 *
 * 1574. 删除最短的子数组使剩余数组有序
 * 给你一个整数数组 arr ，请你删除一个子数组（可以为空），使得 arr 中剩下的元素是 非递减 的。
 *
 * 一个子数组指的是原数组中连续的一个子序列。
 *
 * 请你返回满足题目要求的最短子数组的长度。
 *
 *
 *
 * 示例 1：
 *
 * 输入：arr = [1,2,3,10,4,2,3,5]
 * 输出：3
 * 解释：我们需要删除的最短子数组是 [10,4,2] ，长度为 3 。剩余元素形成非递减数组 [1,2,3,3,5] 。
 * 另一个正确的解为删除子数组 [3,10,4] 。
 * 示例 2：
 *
 * 输入：arr = [5,4,3,2,1]
 * 输出：4
 * 解释：由于数组是严格递减的，我们只能保留一个元素。所以我们需要删除长度为 4 的子数组，要么删除 [5,4,3,2]，要么删除 [4,3,2,1]。
 * 示例 3：
 *
 * 输入：arr = [1,2,3]
 * 输出：0
 * 解释：数组已经是非递减的了，我们不需要删除任何元素。
 * 示例 4：
 *
 * 输入：arr = [1]
 * 输出：0
 *
 *
 * 提示：
 *
 * 1 <= arr.length <= 10^5
 * 0 <= arr[i] <= 10^9
 * 通过次数19,845提交次数47,745
 */
public class _1574_ShortestSubarrayToBeRemovedToMakeArraySorted {

    /**
     * 方法一：双指针 + 二分查找
     *
     * 我们先找出数组的最长非递减前缀和最长非递减后缀，分别记为 nums[0..i] 和 nums[j..n−1]。
     *
     * 如果 i≥j，说明数组本身就是非递减的，返回 0。
     *
     * 否则，我们可以选择删除右侧后缀（[i+1.....n-1]），也可以选择删除左侧前缀（[0...j-1]），
     *
     * 因此初始时答案为 min(n−i−1,j)。
     *
     * 接下来，考虑删除最短的中间部分 ([k...m]，其中 0<=k<=i，j<=m<n)，让 [0...k-1] + [m + 1....n-1]
     * 这种情况下，相当于左右有序区间各保留一部分
     * 此类问题可以通过枚举一侧区间点，在另一侧进行二分搜索，得到右端点，删除二者之间的部分，留下左右区间合并 完成。
     *
     * 为什么只需要枚举一侧，另一侧不用考虑吗？
     * 因为左右区间各保留一部分，枚举一侧的点，需要在另一侧找对应位置，
     * 那么不管枚举哪一侧，能枚举的有效区域实际上只有两侧有共同取值范围的那部分，（剩下点在另一侧没有对应位置）
     * 而这部分的点不论在哪一侧进行枚举，最后的效果其实是一样的，所以没必要两侧都枚举
     *
     * 我们枚举左侧前缀的最右端点 l，通过二分查找，在 nums[j..n−1] 中找到第一个大于等于 nums[l] 的位置，记为 r，
     * 此时我们可以需要删除 nums[l+1..r−1]，并且更新答案 ans=min(ans,r−l−1)。
     *
     * 继续枚举 l，最终得到答案。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/shortest-subarray-to-be-removed-to-make-array-sorted/solution/python3javacgo-yi-ti-shuang-jie-shuang-z-85bh/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param arr
     * @return
     */
    public int findLengthOfShortestSubarray(int[] arr) {
        int n = arr.length;
        int i = 0, j = n - 1;
        // 找出数组的最长非递减前缀和最长非递减后缀，分别记为 nums[0..i] 和 nums[j..n−1]。
        while (i + 1 < n && arr[i] <= arr[i + 1]) i++;
        while (j - 1 >= 0 && arr[j - 1] <= arr[j]) j--;
        // 如果 i≥j，说明数组本身就是非递减的，返回 0。
        if (i >= j) {
            return 0;
        }
        // 否则，我们可以选择删除右侧后缀（[i+1.....n-1]），也可以选择删除左侧前缀（[0...j-1]），因此初始时答案为 min(n−i−1,j)。
        int ans = Math.min(n - i - 1, j);
        // 然后，考虑删除最短的中间部分 ([k...m]，其中 0<=k<=i，j<=m<n)，让 [0...k-1] + [m + 1....n-1]
        // 这种情况下，相当于左右有序区间各保留一部分
        // 此类问题可以通过枚举一侧区间点，在另一侧进行二分搜索，得到右端点，删除二者之间的部分，留下左右区间合并 完成。
        // 枚举左侧点arr[k]
        for (int k = 0; k <= i; ++k) {
            // 在右侧区间寻找第一个满足大于等于arr[k]的位置idx，删除 [k+1,r-1]部分，剩下的拼接起来就可以了
            int idx = leftBoundBinarySearch(arr, j, arr[k]);
            // 更新答案，选择最短删除部分长度
            ans = Math.min(ans, idx - 1 - k);
        }
        // 返回
        return ans;
    }

    /**
     * 在有序数组 arr[s....] 范围内查找第一个大于等于 target 的元素索引
     * @param arr
     * @param s
     * @param target
     * @return
     */

    private int leftBoundBinarySearch(int[] arr, int s, int target) {
        int l = s, r = arr.length;
        while (l < r) {
            int m = (l + r) >> 1;
            // 当前位置满足，继续往前搜索
            if (arr[m] >= target) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        // 满足 arr[m] >= target 时会更新 r 为 m，所以 r为符合要求的位置
        // 最终退出时 l 和 r相等，都可以返回
        // 若 arr 全部满足，则 返回 s；若 arr 全部不满足，则返回 n
        return r;
    }
}
