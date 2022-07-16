package com.hot100;

/**
 * @author wangwei
 * @date 2022/4/20 17:00
 * @description: _152_MaximumProductSubarray
 *
 * 152. 乘积最大子数组
 * 给你一个整数数组 nums ，请你找出数组中乘积最大的非空连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
 *
 * 测试用例的答案是一个 32-位 整数。
 *
 * 子数组 是数组的连续子序列。
 *
 *
 *
 * 示例 1:
 *
 * 输入: nums = [2,3,-2,4]
 * 输出: 6
 * 解释: 子数组 [2,3] 有最大乘积 6。
 * 示例 2:
 *
 * 输入: nums = [-2,0,-1]
 * 输出: 0
 * 解释: 结果不能为 2, 因为 [-2,-1] 不是子数组。
 *
 *
 * 提示:
 *
 * 1 <= nums.length <= 2 * 104
 * -10 <= nums[i] <= 10
 * nums 的任何前缀或后缀的乘积都 保证 是一个 32-位 整数
 */
public class _152_MaximumProductSubarray {

    /**
     * 滑动窗口想法不可行，因为 比如 [i, j] 累乘大于 0， 然后 nums[j] < 0，那么此时会放弃继续扩张，然而如果 nums[j+1]也<0，实际上结果会更大
     * 所以滑动窗口实际上必须滑动整个区域去判断这个区域内的最大累乘结果，这样的话，整个窗口不还是这个题目？所以没有意义
     *
     * 考虑动态规划：dp[i]表示以nums[i]结尾的连续子序列的最大累乘结果
     * 因为只有以当前数字结束，对于下一个数字来说，才能考虑拼接，才能由子问题推出自己，不然无法保证序列连续，不能拆分出子问题
     * 那么 dp[i] = max(dp[i-1] * nums[i], nums[i])，看起来好像没什么问题
     * 但是如果 nums[i+1]是负数呢，
     * 考虑当前位置如果是一个负数的话，那么我们希望以它前一个位置结尾的某个段的积也是个负数，这样就可以负负得正，
     * 并且我们希望这个积尽可能「负得更多」，即尽可能小。
     * 如果当前位置是一个正数的话，我们更希望以它前一个位置结尾的某个段的积也是个正数，并且希望它尽可能地大。
     *
     * 关键点就是这句话：“由于存在负数，那么会导致最大的变最小的，最小的变最大的。因此还需要维护当前最小值imin。”
     * 对于正数来说，希望累积到前面的最大值上去；对于负数来说，希望累积到前面的最小值上去
     *
     * 因此 需要同时保存 以nums[i]结尾的连续子序列的最大和最小累乘结果
     * 然后根据nums[i]的正负，区分更新最大和最小累积结果
     *
     * 所以
     *      maxDp[i]表示以nums[i]结尾的连续子序列的最大累乘结果
     *      minDp[i]表示以nums[i]结尾的连续子序列的最小累乘结果
     *  当 nums[i] >= 0 时：
     *      maxDp[i] = max(maxDp[i-1] * nums[i], nums[i])
     *      minDp[i] = min(minDp[i-1] * nums[i], nums[i])
     *  当 nums[i] < 0 时：
     *      maxDp[i] = max(minDp[i-1] * nums[i], nums[i])
     *      minDp[i] = min(maxDp[i-1] * nums[i], nums[i])
     *
     *  base case : minDp[0] = maxDp[0] = nums[0]
     *  return max(maxDp[i])
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/maximum-product-subarray/solution/cheng-ji-zui-da-zi-shu-zu-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int maxProduct(int[] nums) {
        int n = nums.length;
        // maxDp[i]表示以nums[i][结尾]的连续子序列的[最大]累乘结果
        // minDp[i]表示以nums[i][结尾]的连续子序列的[最小]累乘结果
        int[] minDp = new int[n];
        int[] maxDp = new int[n];
        // base case
        maxDp[0] = minDp[0] = nums[0];
        // init
        int res = maxDp[0];
        for (int i = 1; i < n; ++i) {
            // 正数
            if (nums[i] >= 0) {
                // 对于正数来说，以他结尾要【最大】，那么以它前面那个结尾取【最大】即可；以他结尾要【最小】，那么以它前面那个结尾取【最小】即可
                maxDp[i] = Math.max(maxDp[i - 1] * nums[i], nums[i]);
                minDp[i] = Math.min(minDp[i - 1] * nums[i], nums[i]);
            } else {
                // 对于负数来说，以他结尾要【最大】，那么以它前面那个结尾需要取【最小】；以他结尾要【最小】，那么以它前面那个结尾需要取【最大】
                maxDp[i] = Math.max(minDp[i - 1] * nums[i], nums[i]);
                minDp[i] = Math.min(maxDp[i - 1] * nums[i], nums[i]);
            }
            res = Math.max(res, maxDp[i]);
        }
        return res;
    }

    /**
     * 发现上面的递推过程中，dp[i] 只跟dp[i-1]有关，因此进行压缩
     * @param nums
     * @return
     */
    public int maxProduct2(int[] nums) {
        int n = nums.length;
        // maxDp[i]表示以nums[i][结尾]的连续子序列的[最大]累乘结果
        // minDp[i]表示以nums[i][结尾]的连续子序列的[最小]累乘结果
        // max -- maxDp[i]; min --- minDp[i]
        // base  case
        int min = nums[0], max = nums[0], res = nums[0];
        for (int i = 1; i < n; ++i) {
            if (nums[i] >= 0) {
                max = Math.max(max * nums[i], nums[i]);
                min = Math.min(min * nums[i], nums[i]);
            } else {
                int prevMax = max;
                max = Math.max(min * nums[i], nums[i]);
                // 注意原来这里更新minDp[i]用的i-1状态的maxDp
                // minDp[i] = Math.min(maxDp[i - 1] * nums[i], nums[i]);
                min = Math.min(prevMax * nums[i], nums[i]);
            }
            res = Math.max(res, max);
        }
        return res;
    }

    public static void main(String[] args) {
        _152_MaximumProductSubarray obj = new _152_MaximumProductSubarray();
        obj.maxProduct(new int[]{0,2});
    }
}
