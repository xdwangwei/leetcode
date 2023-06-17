package com.daily;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/6/14 17:19
 * @description: _2475_NumberOfUnequalTripletsInArray
 *
 * 2475. 数组中不等三元组的数目
 * 给你一个下标从 0 开始的正整数数组 nums 。请你找出并统计满足下述条件的三元组 (i, j, k) 的数目：
 *
 * 0 <= i < j < k < nums.length
 * nums[i]、nums[j] 和 nums[k] 两两不同 。
 * 换句话说：nums[i] != nums[j]、nums[i] != nums[k] 且 nums[j] != nums[k] 。
 * 返回满足上述条件三元组的数目。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [4,4,2,4,3]
 * 输出：3
 * 解释：下面列出的三元组均满足题目条件：
 * - (0, 2, 4) 因为 4 != 2 != 3
 * - (1, 2, 4) 因为 4 != 2 != 3
 * - (2, 3, 4) 因为 2 != 4 != 3
 * 共计 3 个三元组，返回 3 。
 * 注意 (2, 0, 4) 不是有效的三元组，因为 2 > 0 。
 * 示例 2：
 *
 * 输入：nums = [1,1,1,1,1]
 * 输出：0
 * 解释：不存在满足条件的三元组，所以返回 0 。
 *
 *
 * 提示：
 *
 * 3 <= nums.length <= 100
 * 1 <= nums[i] <= 1000
 * 通过次数27,247提交次数35,001
 */
public class _2475_NumberOfUnequalTripletsInArray {

    /**
     * 方法一：枚举
     * 记数组 nums 的大小为 n，使用三重循环，枚举所有 0≤i<j<k<n 的三元组，
     * 如果三元组 (i,j,k) 满足 nums[i] != nums[j] 且 nums[i] != nums[k] 且 nums[j] != nums[k]，那么将结果加 1，
     * 枚举结束后返回最终结果。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/number-of-unequal-triplets-in-array/solution/shu-zu-zhong-bu-deng-san-yuan-zu-de-shu-lnpsn/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int unequalTriplets(int[] nums) {
        int n = nums.length, ans = 0;
        // 使用三重循环，枚举所有 0≤i<j<k<n 的三元组，
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                // 保证 nums[i] != nums[j]
                if (nums[i] == nums[j]) {
                    continue;
                }
                for (int k = j + 1; k < n; ++k) {
                    // 保证 nums[j] != nums[k] 且 nums[k] != nums[i]
                    if (nums[j] != nums[k] && nums[i] != nums[k]) {
                        ans++;
                    }
                }
            }
        }
        // 返回
        return ans;
    }

    /**
     * 方法二：排序
     *
     * 由题目和方法一可知，数组元素的相对顺序不影响结果（我们只关心这样的数对个数，不在乎元素位置）
     * 因此我们可以将数组 nums 从小到大进行排序。
     *
     * 排序后，数组中的相同元素一定是相邻的。
     *
     * 当我们以某一堆相同的数 [i,j) 作为三元组的中间元素时，
     * 这堆相同的元素的左边元素数目为 i，右边元素数目为 n−j，
     * 那么符合条件的三元组数目为：i×(j−i)×(n−j)
     *
     * 对以上结果求和并返回最终结果。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/number-of-unequal-triplets-in-array/solution/shu-zu-zhong-bu-deng-san-yuan-zu-de-shu-lnpsn/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int unequalTriplets2(int[] nums) {
        // 排序后，所有相同元素会放置在一块
        Arrays.sort(nums);
        int n = nums.length, ans = 0;
        // 顺序找到所有 一堆相同的数 [i,j) 作为三元组的中间元素，
        for (int i = 0, j = 0; j < n; i = j) {
            while (j < n && nums[j] == nums[i]) {
                j++;
            }
            // 符合条件的三元组数目为：i×(j−i)×(n−j)
            ans += i * (j - i) * (n - j);
        }
        // 返回
        return ans;
    }

    /**
     * 方法三：哈希表
     *
     * 由方法二可以看出，我们需要寻找所有 连续相同的数字作为中间元素，并得知其左右侧元素数量即可。
     * 实际上，这些 连续相同元素 只需要按照某种顺序排列即可，不需要从小到大，能够知道每个连续部分的个数 和 左右部分个数即可
     *
     * 类似于方法二，我们可以使用哈希表 count 记录各个元素的数目，然后遍历哈希表（此时数组元素按照哈希表的遍历顺序进行排列），、
     * 记当前遍历的元素数目 v，先前遍历的元素总数目为 t，
     * 那么以当前遍历的元素为中间元素的符合条件的三元组数目为：t×v×(n−t−v)
     *
     * 对以上结果求和并返回最终结果。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/number-of-unequal-triplets-in-array/solution/shu-zu-zhong-bu-deng-san-yuan-zu-de-shu-lnpsn/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int unequalTriplets3(int[] nums) {
        Map<Integer, Integer> cnt = new HashMap<>();
        // 统计每个元素出现次数
        for (int num : nums) {
            cnt.merge(num, 1, Integer::sum);
        }
        int n = nums.length, ans = 0, pre = 0;
        // 遍历所有连续块
        for (Integer count : cnt.values()) {
            // 以当前连续块作为中间元素时，左边元素有pre个，剩余元素有 n-pre-count 个
            // 贡献度为 pre * count * (n - pre - count);
            ans += pre * count * (n - pre - count);
            // 更新已遍历过的元素个数 pre
            pre += count;
        }
        // 返回
        return ans;
    }

}
