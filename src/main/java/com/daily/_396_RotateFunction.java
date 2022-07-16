package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/4/22 12:54
 * @description: _396_RotateFunction
 *
 * 396. 旋转函数
 * 给定一个长度为 n 的整数数组 nums 。
 *
 * 假设 arrk 是数组 nums 顺时针旋转 k 个位置后的数组，我们定义 nums 的 旋转函数  F 为：
 *
 * F(k) = 0 * arrk[0] + 1 * arrk[1] + ... + (n - 1) * arrk[n - 1]
 * 返回 F(0), F(1), ..., F(n-1)中的最大值 。
 *
 * 生成的测试用例让答案符合 32 位 整数。
 *
 *
 *
 * 示例 1:
 *
 * 输入: nums = [4,3,2,6]
 * 输出: 26
 * 解释:
 * F(0) = (0 * 4) + (1 * 3) + (2 * 2) + (3 * 6) = 0 + 3 + 4 + 18 = 25
 * F(1) = (0 * 6) + (1 * 4) + (2 * 3) + (3 * 2) = 0 + 4 + 6 + 6 = 16
 * F(2) = (0 * 2) + (1 * 6) + (2 * 4) + (3 * 3) = 0 + 6 + 8 + 9 = 23
 * F(3) = (0 * 3) + (1 * 2) + (2 * 6) + (3 * 4) = 0 + 2 + 12 + 12 = 26
 * 所以 F(0), F(1), F(2), F(3) 中的最大值是 F(3) = 26 。
 * 示例 2:
 *
 * 输入: nums = [100]
 * 输出: 0
 *
 *
 * 提示:
 *
 * n == nums.length
 * 1 <= n <= 105
 * -100 <= nums[i] <= 100
 */
public class _396_RotateFunction {

    /**
     * 找规律，迭代
     *
     * 长度为n的数组，最多旋转 n - 1次，发现每一次旋转后的右端点就是原数组元素从最后逐个往前
     *
     * 记数组 nums 的元素之和为 numSum。根据公式，可以得到：
     *
     * 观察规律： nums = [3,4,2,6]
     *
     * F(0) = (0 * 4) + (1 * 3) + (2 * 2) + (3 * 6) = 0 + 3 + 4 + 18 = 25
     * F(1) = (0 * 6) + (1 * 4) + (2 * 3) + (3 * 2) = 0 + 4 + 6 + 6 = 16
     * F(2) = (0 * 2) + (1 * 6) + (2 * 4) + (3 * 3) = 0 + 6 + 8 + 9 = 23
     * F(3) = (0 * 3) + (1 * 2) + (2 * 6) + (3 * 4) = 0 + 2 + 12 + 12 = 26
     *
     * F0 = sum(i * nums[i])
     * F1 = F0 + 除过右端点外所有元素再加一遍，再 - (n - 1)上一个右端点 = F0+ sum − n×上一个右端点 = F0+ sum − n×nums[n-1]
     * F2 = F1 + 除过右端点外所有元素再加一遍，再 - (n - 1)上一个右端点 = F1+ sum − n×上一个右端点 = F1+ sum − n×nums[n-2]
     * F(k) = F(k−1) + numSum − n×nums[n−k], 1 <= k <= n - 1
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/rotate-function
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     * F(0)=0×nums[0]+1×nums[1]+…+(n−1)×nums[n−1]
     * F(1)=1×nums[0]+2×nums[1]+…+0×nums[n−1]=F(0)+numSum−n×nums[n−1]
     * 更一般地，当 1≤k<n 时，F(k)=F(k−1)+numSum−n×nums[n−k]。我们可以不停迭代计算出不同的 F(k)，并求出最大值。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/rotate-function/solution/xuan-zhuan-shu-zu-by-leetcode-solution-s0wd/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int maxRotateFunction(int[] nums) {
        int n = nums.length;
        // 数组元素和
        int sum = Arrays.stream(nums).sum();
        // 计算k=1，也就是旋转0次时的结果
        int f = 0;
        for (int i = 0; i < n; ++i) {
            f += i * nums[i];
        }
        // 初始化 res
        int res = f;
        // F(k)=F(k−1)+numSum−n×nums[n−k]
        // 旋转1次对应的右端点是 nums[n-1],选择1次对应的右端点是nums[n-2],所以这里枚举右端点从后往前
        for (int i = n - 1; i > 0; --i) {
            // 迭代从Fk-1得到Fk
            f += sum - n * nums[i];
            // 更新res
            res = Math.max(res, f);
        }
        return res;
    }
}
