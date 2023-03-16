package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/3/16 16:44
 * @description: _2488_CountSubarraysWithMedianK
 *
 * 2488. 统计中位数为 K 的子数组
 * 给你一个长度为 n 的数组 nums ，该数组由从 1 到 n 的 不同 整数组成。另给你一个正整数 k 。
 *
 * 统计并返回 nums 中的 中位数 等于 k 的非空子数组的数目。
 *
 * 注意：
 *
 * 数组的中位数是按 递增 顺序排列后位于 中间 的那个元素，如果数组长度为偶数，则中位数是位于中间靠 左 的那个元素。
 * 例如，[2,3,1,4] 的中位数是 2 ，[8,4,3,5,1] 的中位数是 4 。
 * 子数组是数组中的一个连续部分。
 *
 *
 * 示例 1：
 *
 * 输入：nums = [3,2,1,4,5], k = 4
 * 输出：3
 * 解释：中位数等于 4 的子数组有：[4]、[4,5] 和 [1,4,5] 。
 * 示例 2：
 *
 * 输入：nums = [2,3,1], k = 3
 * 输出：1
 * 解释：[3] 是唯一一个中位数等于 3 的子数组。
 *
 *
 * 提示：
 *
 * n == nums.length
 * 1 <= n <= 105
 * 1 <= nums[i], k <= n
 * nums 中的整数互不相同
 * 通过次数12,912提交次数26,418
 */
public class _2488_CountSubarraysWithMedianK {

    /**
     * 方法：枚举 + 计数 + 前缀和
     *
     * 先来计算子数组长为奇数的情况。
     *
     * 由于题目保证 nums 中的整数互不相同，k 一定在 nums 中，也一定在 子数组中
     *
     * 那么「k 是子数组的中位数」等价于「子数组中小于 k 的数的个数 = 大于 k 的数的个数」 子数组长度为奇数 。
     * 那么「k 是子数组的中位数」等价于「子数组中小于 k 的数的个数 = 大于 k 的数的个数 - 1」 子数组长度为偶数 。
     *
     * 即 子数组中除去k本身外， 大于k的个数 - 小于k的个数 = 0 或 1
     *
     * 若 k 在 nums 中的索引为 idx，子数组可分为两部分，idx右侧 和 idx 左侧
     * 需要统计左右两侧中 大于k、小于k的元素个数
     *
     * 为了方便计算，把数字等价转换：大于 k 看作 1，小于 k 看作 -1，等于 k 看作 0
     *
     * 这样，[i...j]范围内 大于k的元素个数 - 小于k的元素个数 就可以用前缀和 sum([i...j]) 进行计算
     *
     * 过程：
     *      k元素本身，大于k个数 - 小于k个数 = 0， map.put(0,1)
     *      首先 【考虑右边界的可能性】
     *          从idx+1位置往右遍历，x记录累加前缀和，用map记录每种前缀和（大于k个数-小于k个数）取值的出现次数
     *      接着 将 ans 初始化为 map.get(0) + map.get(1) （子数组取k本身，或从k往右边位置取的可能性）
     *      然后 【考虑左边界的可能性】
     *          从idx-1位置往左遍历，x记录累加前缀和，
     *              对于当前 x 的取值（大于k个数-小于k个数）来说，其后部分需满足 大于k个数-小于k个数 = -x 或 -x+1
     *              则 ans + map.get(-x) + map.get(-x+1)
     *
     * 最后返回 ans
     *
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/count-subarrays-with-median-k/solution/deng-jie-zhuan-huan-pythonjavacgo-by-end-5w11/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */
    public int countSubarrays(int[] nums, int k) {
        int idx = 0;
        // 寻找 k 在 nums 中的位置
        while (nums[idx] != k) idx++;
        int n = nums.length;
        // 记录，每种 大于k个数 - 小于k个数 的取值 的次数
        // 将 大于k元素看作 1， 小于k 元素 看作 -1，转变为 记录 每种 累加前缀和 的取值 的次数
        Map<Integer, Integer> cntMap = new HashMap<>();
        // idx位置，单个元素，前缀和为0，出现次数1
        cntMap.put(0, 1);
        // 考虑子数组右边界可能性，x 代表 从idx+1往右到j位置这部分，大于k个数 - 小于k个数取值 的次数
        int x = 0;
        for (int i = idx + 1; i < n; ++i) {
            // 前缀和 x 代表 大于k个数 - 小于k个数
            x += nums[i] > k ? 1 : -1;
            // 更新取值的次数
            cntMap.put(x, cntMap.getOrDefault(x, 0) + 1);
        }
        // idx位置和右边界考虑完毕，初始化ans为只选择k，或从k往右的子数组 的可能数量
        // 大于k - 小于k 个数必须为 0 或 1
        int ans = cntMap.get(0) + cntMap.getOrDefault(1, 0);
        // 考虑子数组左边界取值，x 代表 从idx-1往左到i位置这部分，大于k个数 - 小于k个数取值 的次数
        x = 0;
        for(int i = idx - 1; i >= 0; --i) {
            // 更新 大于k - 小于k 个数
            x += nums[i] > k ? 1 : -1;
            // 对于当前取值x，希望子数组剩余部分中 大于k个数 - 小于k个数 = -x 或 -x + 1，
            // 这样 整个子数组中就满足 大于k个数 - 小于k个数 = 0 或 1
            // 累加可行的数目
            ans += cntMap.getOrDefault(-x, 0) + cntMap.getOrDefault(-x + 1, 0);
        }
        // 返回
        return ans;
    }

}
