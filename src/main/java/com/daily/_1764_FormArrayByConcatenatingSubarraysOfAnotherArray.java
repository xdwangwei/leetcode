package com.daily;

/**
 * @author wangwei
 * @date 2022/12/18 10:52
 * @description: _1764_FormArrayByConcatenatingSubarraysOfAnotherArray
 *
 *  1764. 通过连接另一个数组的子数组得到一个数组
 * 给你一个长度为 n 的二维整数数组 groups ，同时给你一个整数数组 nums 。
 *
 * 你是否可以从 nums 中选出 n 个 不相交 的子数组，使得第 i 个子数组与 groups[i] （下标从 0 开始）完全相同，且如果 i > 0 ，那么第 (i-1) 个子数组在 nums 中出现的位置在第 i 个子数组前面。（也就是说，这些子数组在 nums 中出现的顺序需要与 groups 顺序相同）
 *
 * 如果你可以找出这样的 n 个子数组，请你返回 true ，否则返回 false 。
 *
 * 如果不存在下标为 k 的元素 nums[k] 属于不止一个子数组，就称这些子数组是 不相交 的。子数组指的是原数组中连续元素组成的一个序列。
 *
 *
 *
 * 示例 1：
 *
 * 输入：groups = [[1,-1,-1],[3,-2,0]], nums = [1,-1,0,1,-1,-1,3,-2,0]
 * 输出：true
 * 解释：你可以分别在 nums 中选出第 0 个子数组 [1,-1,0,1,-1,-1,3,-2,0] 和第 1 个子数组 [1,-1,0,1,-1,-1,3,-2,0] 。
 * 这两个子数组是不相交的，因为它们没有任何共同的元素。
 * 示例 2：
 *
 * 输入：groups = [[10,-2],[1,2,3,4]], nums = [1,2,3,4,10,-2]
 * 输出：false
 * 解释：选择子数组 [1,2,3,4,10,-2] 和 [1,2,3,4,10,-2] 是不正确的，因为它们出现的顺序与 groups 中顺序不同。
 * [10,-2] 必须出现在 [1,2,3,4] 之前。
 * 示例 3：
 *
 * 输入：groups = [[1,2,3],[3,4]], nums = [7,7,1,2,3,4,7,7]
 * 输出：false
 * 解释：选择子数组 [7,7,1,2,3,4,7,7] 和 [7,7,1,2,3,4,7,7] 是不正确的，因为它们不是不相交子数组。
 * 它们有一个共同的元素 nums[4] （下标从 0 开始）。
 *
 *
 * 提示：
 *
 * groups.length == n
 * 1 <= n <= 103
 * 1 <= groups[i].length, sum(groups[i].length) <= 103
 * 1 <= nums.length <= 103
 * -107 <= groups[i][j], nums[k] <= 107
 * 通过次数4,915提交次数9,835
 */
public class _1764_FormArrayByConcatenatingSubarraysOfAnotherArray {

    /**
     * 方法一：贪心 + 双指针
     * 使用变量 i 指向需要匹配的数组，即 groups[i]。遍历数组 nums，假设当前遍历到第 k 个元素：
     *
     * 以 nums[k] 为首元素的子数组与 groups[i] 相同，那么 groups[i] 可以找到对应的子数组。
     *      为了满足不相交的要求，我们将 k 加上数组 groups[i] 的长度，并且将 i 加 1；
     * 以 nums[k] 为首元素的子数组与 groups[i] 不相同，那么我们直接将 k 加 11。
     *
     * 遍历结束时，如果 groups 的所有数组都找到对应的子数组，即 i=n 成立，返回 true；否则返回 false。
     *
     * 贪心的正确性
     *
     * 证明：假设存在 n 个不相交的子数组，使得第 i 个子数组与 groups[i] 完全相同，并且第 i 个子数组的首元素下标为 k，
     * 那么在匹配查找的过程中，如果存在下标 k_1 < k 也满足第 i 个子数组的要求，显然我们将 k_1 替代 k 是不影响后续的匹配的。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/form-array-by-concatenating-subarrays-of-another-array/solution/tong-guo-lian-jie-ling-yi-ge-shu-zu-de-z-xsvx/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param groups
     * @param nums
     * @return
     */
    public boolean canChoose(int[][] groups, int[] nums) {
        int i = 0;
        // 按顺序对groups[i]进行匹配，去nums中寻找满足 nums[k....] 匹配 groups[i][...]的位置k
        for (int k = 0; k < nums.length && i < groups.length;) {
            // 判断nums中从k位置往后 g.length 个位置的元素 是否和 g 中元素一一对应相等
            // 匹配成功，k 直接跳过 g.length 个位置
            // 贪心保证正确性：
            // 若nums[k1....] 匹配 g[i]，nums[k2...]匹配g[i]
            // 若用 nums[k1...] 匹配 g[i]，后序匹配失败，那么用 nums[k2...]匹配g[i]，后序只会更加失败
            // 若用 nums[k1...] 匹配 g[i]，后序匹配成功，那么用 nums[k2...]匹配g[i]，后序匹配不一定成功
            // 所以遇到第一个满足nums[k....]匹配g[i]的k,就用这部分匹配g，然后就可以跳过去匹配g[i+1]了
            if (check(groups[i], nums, k)) {
                k += groups[i].length;
                // 匹配g中下一个子数组
                i++;
            } else {
                // 不相等，k++
                k++;
            }
        }
        return i == groups.length;
    }

    /**
     * 判断nums中从k位置往后 g.length 个位置的元素 是否和 g 中元素一一对应相等
     * @param g
     * @param nums
     * @param k
     * @return
     */
    public boolean check(int[] g, int[] nums, int k) {
        if (k + g.length > nums.length) {
            return false;
        }
        for (int j = 0; j < g.length; j++) {
            if (g[j] != nums[k + j]) {
                return false;
            }
        }
        return true;
    }
}
