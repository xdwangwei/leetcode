package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/3/10 12:38
 * @description: _1590_MakeSumOfArrayDivisibleByP
 *
 * 1590. 使数组和能被 P 整除
 * 给你一个正整数数组 nums，请你移除 最短 子数组（可以为 空），使得剩余元素的 和 能被 p 整除。 不允许 将整个数组都移除。
 *
 * 请你返回你需要移除的最短子数组的长度，如果无法满足题目要求，返回 -1 。
 *
 * 子数组 定义为原数组中连续的一组元素。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [3,1,4,2], p = 6
 * 输出：1
 * 解释：nums 中元素和为 10，不能被 p 整除。我们可以移除子数组 [4] ，剩余元素的和为 6 。
 * 示例 2：
 *
 * 输入：nums = [6,3,5,2], p = 9
 * 输出：2
 * 解释：我们无法移除任何一个元素使得和被 9 整除，最优方案是移除子数组 [5,2] ，剩余元素为 [6,3]，和为 9 。
 * 示例 3：
 *
 * 输入：nums = [1,2,3], p = 3
 * 输出：0
 * 解释：和恰好为 6 ，已经能被 3 整除了。所以我们不需要移除任何元素。
 * 示例  4：
 *
 * 输入：nums = [1,2,3], p = 7
 * 输出：-1
 * 解释：没有任何方案使得移除子数组后剩余元素的和被 7 整除。
 * 示例 5：
 *
 * 输入：nums = [1000000000,1000000000,1000000000], p = 3
 * 输出：0
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 109
 * 1 <= p <= 109
 * 通过次数13,187提交次数39,612
 */
public class _1590_MakeSumOfArrayDivisibleByP {

    /**
     * 方法：前缀和 + 取模运算 + 哈希表
     *
     * 取模运算不具有单调性，无法采用滑动窗口
     *
     * 假设 nums 元素和为 sum，对 p 取模结果为 x，即 sum % p = x
     *
     * 若 x == 0，直接返回 0
     *
     * 若 [j, i) 为满足要求的子数组， 其元素和为 cur，那么 有 (sum - cur) % p = 0
     *
     * 则有 x = sum % p = cur % p
     *
     * 采用前缀和数组加速子数组元素和计算，则 cur = s[i] - s[j]，其中 s[i + 1] = sum(nums[0...i])
     *
     * 则 x = (s[i] - s[j]) % p
     *
     * 移项得  (s[i] - x) % p = s[j] % p
     *
     * 为了避免负数，转变为 (s[i] - x + p) % p = s[j] % p
     *
     * 即，满足要求的子数组[j, i)，长度为 i - j 需要满足下式 ，其中 s[] 为前缀和数组
     *      (s[i] - x + p) % p = s[j] % p
     * 目前是求解最短的子数组，所以 对于 i 来说，需要找到 离他最近的 j，用hashmap记录 每个%p的值最后一次出现的位置 j
     *
     * 并且，上式中，s[i] 是 nums[0...i-1]的元素和，s[j]是nums[0...j-1]的元素和，
     * 因此我们不用求出完整的前缀和数组 s[]，可以在遍历的过程中计算nums[0]到当前位置的累计元素和 cur，即为 s[i]
     * 计算 k = (cur -x + p) % p，如果 map 中存在 k，那么就用 i - map.get(k) 更新答案
     * 之后，将 (cur % p, i) 存入 map
     *
     * 【注意1】
     * 此时，通过遍历nums计算当前元素累加和 cur 及相关运算，省略了 前缀和数组，
     * 但是以上推导所用的前缀和数组 s[]长度是n+1，s[0]=0,s[1]=nums[0]
     * 因此，此时cur的累计是从 nums[0] 直接开始的，那么就要把 原来s[0]=0对应的nums中的索引设置为-1了
     * 即开始时，map.put(0,-1) 元素和0%p = 0，对应下标为-1
     *
     * 【注意2】
     * 由于题目数据范围较大，cur在累加更新过程中可能溢出int，需要用long类型，不过这样会涉及好几处类型转换
     * 我们可以对上面的石子做简单变换
     *      (s[i] - x + p) % p = s[j] % p，变成  (s[i] % p - x + p) % p = s[j] % p
     * 这样，我们在遍历的过程中直接更新 cur 为 (cur + nums[i]) % p，就不会出现数据溢出问题
     *
     * 【注意3】
     * 返回时不要忘记判断 ans == n ? 不能全部移除，所以返回 -1
     * @param nums
     * @param p
     * @return
     */

    public int minSubarray(int[] nums, int p) {
        int x = 0;
        // 计算nums元素和 % p 的值
        for (int num : nums) {
            x = (x + num) % p;
        }
        // 可以整除，直接返回
        if (x == 0) {
            return 0;
        }
        // cur记录 sum(nums[0...i]) -->  sum(nums[0...i]) % p
        int cur = 0, n = nums.length, ans = n;
        // map记录 值为 cur%p --> cur 最后一次出现的位置(nums中位置),如sum(nums[0...i]) = k, map.put(k%p, i)
        Map<Integer, Integer> map = new HashMap<>();
        // 前缀和为0，取模为0，对应nums中位置为-1，
        map.put(0, -1);
        // 遍历，迭代更新累加和 cur
        for (int i = 0; i < n; ++i) {
            // cur += nums[i];
            // 直接更新cur为sum(nums[0...i]) % p，避免类型转换
            cur = (cur + nums[i]) % p;
            // 如果 cur记录累加和，这里要强制类型转换，int k = (int)((cur - x + p) % p);
            int k = (cur - x + p) % p;
            // 寻找满足要求的 j
            if (map.containsKey(k)) {
                // 更新答案
                ans = Math.min(ans, i - map.get(k));
            }
            // 记录当前取模值对应的位置
            // 这里cur本身就是累加和%p的值
            // 如果 cur记录累加和，这里要强制类型转换，map.put((int)(cur % p), i)
            map.put(cur, i);
        }
        // 返回
        return ans == n ? -1 : ans;
    }
}
