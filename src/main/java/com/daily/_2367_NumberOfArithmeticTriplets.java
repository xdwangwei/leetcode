package com.daily;

/**
 * @author wangwei
 * @date 2023/3/31 10:15
 * @description: _2367_NumberOfArithmeticTriplets
 *
 * 2367. 算术三元组的数目
 * 给你一个下标从 0 开始、严格递增 的整数数组 nums 和一个正整数 diff 。如果满足下述全部条件，则三元组 (i, j, k) 就是一个 算术三元组 ：
 *
 * i < j < k ，
 * nums[j] - nums[i] == diff 且
 * nums[k] - nums[j] == diff
 * 返回不同 算术三元组 的数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [0,1,4,6,7,10], diff = 3
 * 输出：2
 * 解释：
 * (1, 2, 4) 是算术三元组：7 - 4 == 3 且 4 - 1 == 3 。
 * (2, 4, 5) 是算术三元组：10 - 7 == 3 且 7 - 4 == 3 。
 * 示例 2：
 *
 * 输入：nums = [4,5,6,7,8,9], diff = 2
 * 输出：2
 * 解释：
 * (0, 2, 4) 是算术三元组：8 - 6 == 2 且 6 - 4 == 2 。
 * (1, 3, 5) 是算术三元组：9 - 7 == 2 且 7 - 5 == 2 。
 *
 *
 * 提示：
 *
 * 3 <= nums.length <= 200
 * 0 <= nums[i] <= 200
 * 1 <= diff <= 50
 * nums 严格 递增
 * 通过次数23,283提交次数27,506
 */
public class _2367_NumberOfArithmeticTriplets {

    /**
     * 方法一：暴力枚举
     *
     * 我们注意到，数组 nums 的长度只有不超过 200，因此可以直接暴力枚举 i, j, k，判断是否满足条件，若满足，累加三元组数目。
     * 因为数组严格递增，因此可以加入一些提前剪枝的条件
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/number-of-arithmetic-triplets/solution/python3javacgo-yi-ti-shuang-jie-bao-li-m-nqaq/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param diff
     * @return
     */
    public int arithmeticTriplets(int[] nums, int diff) {
        int n = nums.length;
        int ans = 0;
        // 枚举 i
        for (int i = 0; i < n; ++i) {
            // 枚举 j，从 i+1 开始
            int j = i + 1;
            // 对于 i 来说，如果 nums[j] - nums[i] > diff，那么之后的 j 不用遍历，
            // 对于 i 来说，如果 nums[n-1] - nums[i] < diff，那么之后的 i 都不用遍历，
            while (j < n && nums[j] - nums[i] < diff) { ++j; }
            // while 退出时 j >= n 或 nums[j] - nums[i] >= diff
            // 如果 j >= n，那么 对于之后的 i，都不存在满足要求的 j，提前结束
            if (j >= n) {
                break;
            }
            // 如果 nums[j] - nums[i] == diff
            if (nums[j] - nums[i] == diff) {
                // 继续寻找k
                for (int k = j + 1; k < n; ++k) {
                    if (nums[k] - nums[j] == diff) {
                        ans++;
                        // 因为严格递增，所以这里直接结束
                        break;
                    }
                }
            }
            // 如果 nums[j] - nums[i] > diff，那么之后的 j 不用遍历，直接枚举下一个 i
        }
        // 返回
        return ans;
    }


    /**
     * 方法二：数组或哈希表
     *
     * 我们可以先将 nums 中的元素存入哈希表或数组 vis 中，
     * 然后枚举 nums 中的每个元素 x，判断 x+diff, x+diff+diff 是否也在 vis 中，若是，累加三元组数目。
     *
     * 枚举结束后，返回答案。
     *
     * 本题中，nums 中元素 最大为 200， diff 最大为 50，所以 x + 2*diff 最大为 300，可以用数组代替哈希表
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/number-of-arithmetic-triplets/solution/python3javacgo-yi-ti-shuang-jie-bao-li-m-nqaq/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param diff
     * @return
     */
    public int arithmeticTriplets2(int[] nums, int diff) {
        boolean[] vis = new boolean[301];
        // 将 nums 中的元素存入哈希表或数组 vis 中
        for (int num : nums) {
            vis[num] = true;
        }
        int ans = 0;
        // 枚举 nums 中的每个元素 x，
        // 元素 x 肯定在 vis 中呀，上面那个 for 循环不就是干这事的么
        for (int num : nums) {
            // 判断 x+diff, x+diff+diff 是否也在 vis 中，若是，累加三元组数目。
            if (vis[num + diff] && vis[num + 2 * diff]) {
                ans++;
            }
        }
        // 返回
        return ans;
    }
}
