package com.hot100;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wangwei
 * 2022/4/19 13:10
 *
 * 128. 最长连续序列
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 *
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [100,4,200,1,3,2]
 * 输出：4
 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * 示例 2：
 *
 * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
 * 输出：9
 *
 *
 * 提示：
 *
 * 0 <= nums.length <= 105
 * -109 <= nums[i] <= 109
 */
public class _128_LongestConsecutiveSequence {

    /**
     * hash表
     *
     * 我们考虑枚举数组中的每个数 x，考虑以其为起点，不断尝试匹配 x+1, x+2, ⋯ 是否存在，假设最长匹配到了 x+y，那么以 x 为起点的最长连续序列即为 x, x+1, x+2,⋯,x+y，其长度为 y+1，我们不断枚举并更新答案即可。
     *
     * 对于匹配的过程，暴力的方法是 O(n) 遍历数组去看是否存在这个数，
     * 但其实更高效的方法是用一个哈希表存储原数组中的所有数字，这样查看一个数字是否存在即能优化至 O(1) 的时间复杂度。
     *
     * 仅仅是这样我们的算法时间复杂度最坏情况下还是会达到 O(n^2)（即外层需要枚举 O(n) 个数，内层需要暴力匹配 O(n)次），无法满足题目的要求。
     * 但仔细分析这个过程，我们会发现其中执行了很多不必要的枚举，
     * 如果已知有一个 x, x+1, x+2,⋯,x+y 的连续序列，而我们却重新从 x+1，x+2 或者是 x+y 处开始尝试匹配，
     * 那么得到的结果肯定不会优于枚举 x 为起点的答案，因此我们在外层循环的时候碰到这种情况跳过即可。
     *
     * 那么怎么判断是否跳过呢？由于我们要枚举的数 x一定是在数组中不存在前驱数 x−1 的，不然按照上面的分析我们会从 x−1 开始尝试匹配，
     * 因此我们每次在哈希表中检查是否存在 x-1 即能判断是否需要跳过了。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/longest-consecutive-sequence/solution/zui-chang-lian-xu-xu-lie-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int ans = 0;
        // 保存数组所有元素
        for (int num : nums) {
            set.add(num);
        }
        for (int num : nums) {
            // 只枚举每个序列起点（不存在前面，断开的位置）
            // 如果已知有一个 x, x+1, x+2,⋯,x+y 的连续序列，而我们却重新从 x+1，x+2 或者是 x+y 处开始尝试匹配，那么得到的结果肯定不会优于枚举 x 为起点的答案，
            if (!set.contains(num - 1)) {
                // 记录最大能枚举到的序列长度
                int curLength = 1, cur = num;
                while (set.contains(cur + 1)) {
                    curLength++;
                    cur++;
                }
                // 更新结果
                ans = Math.max(ans, curLength);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        _128_LongestConsecutiveSequence obj = new _128_LongestConsecutiveSequence();
        System.out.println(obj.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
    }
}
