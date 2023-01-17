package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/1/17 10:31
 * @description: _1814_CountNicePairsInAArray
 *
 * 1814. 统计一个数组中好对子的数目
 * 给你一个数组 nums ，数组中只包含非负整数。定义 rev(x) 的值为将整数 x 各个数字位反转得到的结果。比方说 rev(123) = 321 ， rev(120) = 21 。我们称满足下面条件的下标对 (i, j) 是 好的 ：
 *
 * 0 <= i < j < nums.length
 * nums[i] + rev(nums[j]) == nums[j] + rev(nums[i])
 * 请你返回好下标对的数目。由于结果可能会很大，请将结果对 109 + 7 取余 后返回。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [42,11,1,97]
 * 输出：2
 * 解释：两个坐标对为：
 *  - (0,3)：42 + rev(97) = 42 + 79 = 121, 97 + rev(42) = 97 + 24 = 121 。
 *  - (1,2)：11 + rev(1) = 11 + 1 = 12, 1 + rev(11) = 1 + 11 = 12 。
 * 示例 2：
 *
 * 输入：nums = [13,10,35,24,76]
 * 输出：4
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * 0 <= nums[i] <= 109
 * 通过次数10,201提交次数23,134
 * 请问您在哪类招聘中遇到此题？
 */
public class _1814_CountNicePairsInAArray {


    /**
     * 等式变换 + 哈希表
     *
     * 首先对于题目的式子  nums[i] + rev(nums[j]) = nums[j] + rev(nums[i])
     *
     * i 和 j 都出现在了两边，我们将其统一移动在一方，得到
     *
     *      nums[i] - rev(nums[i]) = nums[j] - rev(nums[j])
     *
     * 那么问题就转化为：求出 (i,j) 对，使得 nums[i]−rev(nums[i]) = nums[j]−rev(nums[j]),
     *
     * 很明显我们可以用哈希表来求解。
     * 用哈希表来保存某个数出现的次数，
     * 我们从前往后遍历数组中所有数，哈希表中迭代更新 key = x - rev(x) 的出现次数
     * 当遍历到数字 x时，我们求得 key = x−rev(x)，由于哈希表里面保存的是x之前的数字y-rev(v)各种取值的出现次数，
     * 那么我们直接对 ans 累加 key 为 x - rev(x) 的键值(出现次数)即可，就相当于对nums[j]找到了多少i<j，满足等式要求
     *
     * 作者：Tizzi
     * 链接：https://leetcode.cn/problems/count-nice-pairs-in-an-array/solution/by-tizzi-a8z6/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int countNicePairs(int[] nums) {
        // 保存数字出现次数
        Map<Integer, Integer> countMap = new HashMap<>();
        // 最终结果对其取模
        final int mod = (int) (1e9 + 7);
        // 记录结果
        int ans = 0;
        // 顺序遍历
        for (int num : nums) {
            // 计算当前数字对应的 key = x - rev(x)
            int x = num, rev = 0;
            while (x > 0) {
                rev = rev * 10 + x % 10;
                x /= 10;
            }
            // 累加x之前的数字产生的结果中，key的出现次数，对 mod 取模
            ans = (ans + countMap.getOrDefault(num - rev, 0)) % mod;
            // 更新map中key的出现次数
            countMap.put(num - rev, countMap.getOrDefault(num - rev, 0) + 1);
        }
        // 返回
        return ans;
    }

    public static void main(String[] args) {
        _1814_CountNicePairsInAArray obj = new _1814_CountNicePairsInAArray();
        System.out.println(obj.countNicePairs(new int[]{42, 11, 1, 97}));
    }
}
