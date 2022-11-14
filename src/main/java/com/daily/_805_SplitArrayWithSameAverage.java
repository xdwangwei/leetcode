package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/11/14 11:18
 * @description: _805_SplitArrayWithSameAverage
 *
 * 805. 数组的均值分割
 * 给定你一个整数数组 nums
 *
 * 我们要将 nums 数组中的每个元素移动到 A 数组 或者 B 数组中，使得 A 数组和 B 数组不为空，并且 average(A) == average(B) 。
 *
 * 如果可以完成则返回true ， 否则返回 false  。
 *
 * 注意：对于数组 arr ,  average(arr) 是 arr 的所有元素除以 arr 长度的和。
 *
 *
 *
 * 示例 1:
 *
 * 输入: nums = [1,2,3,4,5,6,7,8]
 * 输出: true
 * 解释: 我们可以将数组分割为 [1,4,5,8] 和 [2,3,6,7], 他们的平均值都是4.5。
 * 示例 2:
 *
 * 输入: nums = [3,1]
 * 输出: false
 *
 *
 * 提示:
 *
 * 1 <= nums.length <= 30
 * 0 <= nums[i] <= 104
 */
public class _805_SplitArrayWithSameAverage {

    /**
     * 根据题目要求，要判断是否可以将数组 nums 划分为两个子数组 A 和 B，使得两个子数组的平均值相等。
     *
     * 我们记数组 nums 的和为 sum，元素个数为 n。
     * 子数组 A 的和以及个数分别为 s1 和 a，子数组 B 的和 和 个数分别为 s2 和 b
     * 那么 s1 + s2 = sum, a + b = n
     * 假设 A 和 B 的均值为avg, 那么 s1 = a * avg, s2 = b * avg,
     * 那么 sum = s1 + s2 = (a + b) * avg = n * avg
     *
     * 也就是说，要我们找出一个非空非全子数组 A，使得其平均值等于数组 nums 的平均值。
     *
     * 方法一：回溯，每个数字都可以有加入A和不加入A两种选择，题目数据范围数组元素个数 最多30, 那么 时间复杂度 2^30
     *       超时，并且需要考虑 重复数字太多，以及 小数精度问题，
     *       若选择记忆化搜索，如何构造状态？数组和+数字个数？只能解决重复数字，但会造成 (5,5), (4,6)被认为已计算过，错过后续结果
     *       若用二进制记录所有数字的选择状态，又无法避免对于大量重复数字时重复统计过程
     *
     *       回溯代码仅供参考
     *
     * 方法二：二分搜索 + 二进制枚举
     * @param nums
     * @return
     */
    public boolean splitArraySameAverage(int[] nums) {
        int sum = Arrays.stream(nums).sum();
        double avg = sum * 1.0 / nums.length;
        return helper(nums, 0, 0, 0, avg);
    }

    /**
     * 回溯代码仅供参考
     * @param nums
     * @param idx
     * @param curSum
     * @param cnt
     * @param avg
     * @return
     */
    private boolean helper(int[] nums, int idx, double curSum, int cnt, double avg) {
        // 所有数字已经做完选择
        if (idx == nums.length) {
            // 不能空，不能全部，均值要=avg
            return cnt < nums.length ? curSum / cnt == avg : false;
        }
        // 选则当前数字
        boolean res = helper(nums, idx + 1, curSum + nums[idx], cnt + 1, avg);
        if (res) {
            return true;
        }
        // 不选则当前数字
        return helper(nums, idx + 1, curSum, cnt, avg);
    }


    /**
     * 方法二：二分搜索 + 二进制枚举
     *
     * 首先解决小数精度问题
     *
     * 假设原数组和为 sum, 个数 为 n
     * 给每个元素 * n, 那么 新数组和变为 n * sum, 新的均值变为 sum
     * 题目变为：在新数组中寻找非空真子数组A,让均值为sum，时间复杂度并没有改变，还是 2^n
     *
     * 进一步改造
     *
     * 考虑将数组 nums 每个元素都减去数组 nums 的平均值sum，最后新的平均值就为0,
     * 这样问题就转化为了在数组 nums 中找出一个子数组A，使得其和为 0。
     *
     * 二分搜索+二进制枚举
     *
     * 我们可以使用折半查找的方法，将时间复杂度降低到 O(2^{n/2}) + O(2^{n/2})。
     *
     * 我们将数组 nums 分成左右两部分，那么子数组 A 可能存在三种情况：
     *
     * 子数组 A 完全在数组 nums 的左半部分；
     * 子数组 A 完全在数组 nums 的右半部分；
     * 子数组 A 一部分在数组 nums 的左半部分，一部分在数组 nums 的右半部分。
     *
     * 我们可以使用二进制枚举的方法，
     *
     * 先枚举左半部分数字能够选择的所有子数组的和，
     * 如果存在一个子数组和为 0，直接返回 true，（A完全在nums左半部分）
     * 、否则我们将其存入哈希表 set 中，记录左半部分元素能得到的子数组和的可能性；
     *
     * 然后枚举右半部分所有子数组的和，
     * 如果存在一个子数组和为 0，直接返回 true，（A完全在nums右半部分）
     * 否则我们判断此时哈希表 set 中是否存在当前和的相反数，如果存在，直接返回 true。（A 一部分在左半部分，一部分在右半部分。）
     *
     * 需要注意的是，最终A必须非空，剩下的元素也必须非空，所以对于A的左右部分枚举时 [[不能同时为空，不能同时全选 ]]
     * 左半部分长度 m = n /2，右半部分长度 n - m
     * 那么枚举左半部分可以枚举的范围就是 s = [0, (1 << m) - 1], s 二进制第i位为1,    代表选择 nums[i]
     *          s = 0, 那么 nums[0...m-1] 全不选，s = (1 << m) - 1, 那么 nums[0...m-1] 全选
     * 那么枚举右半部分可以枚举的范围就是 s = [1, 1 << (n-m) - 2], s 二进制第i位为1,代表选择 nums[m + i]
     *          s = 1, 那么 nums[m...n-1] 至少选择1个，s = (1 << m) - 2, 那么 nums[m...n-1] 至少一个不选
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/split-array-with-same-average/solution/by-lcbin-1dm3/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public boolean splitArraySameAverage2(int[] nums) {
        int n = nums.length;
        // 不可能情况
        if (n == 1) {
            return false;
        }
        // 原sum
        int sum = Arrays.stream(nums).sum();
        // 每个数字 * n 后，总sum变为 n * sum，平均值变为 sum
        // 每个数字再减去均值，那么 新的 均值 变为 0
        for (int i = 0; i < n; ++i) {
            nums[i] = nums[i] * n - sum;
        }
        // 左半边长度
        int m = n >> 1;
        // 记录数字能组成的子数组和的所有可能情况
        Set<Integer> set = new HashSet<>();
        // 左半边枚举范围 [0, (1 << m) - 1]
        // s = 0,代表全不选，直接跳过，从 1 开始枚举
        for (int s = 1; s < 1 << m; ++s) {
            // s 二进制第i位为1代表nums[i]选择，不同s就能得到不同子数组和
            int tot = 0;
            for (int i = 0; i < m; ++i) {
                if (((s >> i) & 1) == 1) {
                    tot += nums[i];
                }
            }
            // 目标A完全在nums左半部分，（跳过了s=0.这里一定是非空的）
            if (tot == 0) {
                return true;
            }
            // 记录当前和
            set.add(tot);
        }
        // 右半部分枚举范围 [1, 1 << (n-m) - 2]
        for (int s = 1; s < 1 << (n - m) - 1; ++s) {
            // s 二进制第j位为1代表nums[m + j]选择，不同s就能得到不同子数组和
            int tot = 0;
            for (int j = 0; j < (n - m); ++j) {
                if (((s >> j) & 1) == 1) {
                    tot += nums[m + j];
                }
            }
            // 目标A完全在nums左半部分
            // 或者，当前右边选择情况 在 左边枚举过程中 存在 匹配 的另一半
            if (tot == 0 || set.contains(-tot)) {
                return true;
            }
        }
        // 其他
        return false;
    }

    public static void main(String[] args) {
        _805_SplitArrayWithSameAverage obj = new _805_SplitArrayWithSameAverage();
//        System.out.println(obj.splitArraySameAverage(new int[]{3, 1}));
//        System.out.println(obj.splitArraySameAverage(new int[]{1,2,3,4,5,6,7,8}));
//        System.out.println(obj.splitArraySameAverage(new int[]{6,8,18,3,1}));
        System.out.println(obj.splitArraySameAverage2(new int[]{3,4,9,4,4,3,9,8,5,3}));
    }
}
