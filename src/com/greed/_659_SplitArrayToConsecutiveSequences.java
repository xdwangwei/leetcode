package com.greed;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * 2022/3/20 14:08
 *
 * 659. 分割数组为连续子序列
 * 给你一个按升序排序的整数数组 num（可能包含重复数字），请你将它们分割成一个或多个长度至少为 3 的子序列，其中每个子序列都由连续整数组成。
 *
 * 如果可以完成上述分割，则返回 true ；否则，返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入: [1,2,3,3,4,5]
 * 输出: True
 * 解释:
 * 你可以分割出这样两个连续子序列 :
 * 1, 2, 3
 * 3, 4, 5
 * 示例 2：
 *
 * 输入: [1,2,3,3,4,4,5,5]
 * 输出: True
 * 解释:
 * 你可以分割出这样两个连续子序列 :
 * 1, 2, 3, 4, 5
 * 3, 4, 5
 * 示例 3：
 *
 * 输入: [1,2,3,4,4,5]
 * 输出: False
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 10000
 */
public class _659_SplitArrayToConsecutiveSequences {

    /**
     * 贪心
     *
     * 对于这种涉及【连续整数】的问题，应该条件反射地想到【排序】，不过题目说了，输入的 nums 本就是排好序的。
     *
     * 将 x 加入已有的子序列总是比新建一个只包含 x 的子序列更优，因为前者可以将一个已有的子序列的长度增加 1，
     * 而后者新建一个长度为 1 的子序列，而题目要求分割成的子序列的长度都不小于 3，
     * 因此应该尽量避免新建短的子序列。【贪心】
     *
     * 那么对于元素 x，如何判断它应该接到前面序列末尾，还是应该重开一个序列：
     *      如果存在一个子序列以 x-1结尾，则可以将 x 加入该子序列中。（如果这样的序列有多个，那么随意）
     *      如果 还有 x+1，x+2,没有被使用，那么 x 和 x+1 和 x+2可以组成一个最基本的序列。
     *      上面两种都不可以，那么就会成为一个孤立的数字，直接返回false
     *
     *      当前前提是当前元素x还有可用次数。
     *
     * 基于此，可以通过贪心的方法判断是否可以完成分割。使用两个哈希表，
     *      第一个哈希表存储数组中的每个数字的剩余次数，
     *      第二个哈希表存储数组中的每个数字作为结尾的子序列的数量。
     *
     * 初始时，每个数字的剩余次数即为每个数字在数组中出现的次数，因此遍历数组，初始化第一个哈希表。
     * 在初始化第一个哈希表之后，遍历数组，更新两个哈希表。
     * 只有当一个数字的剩余次数大于 0 时，才需要考虑这个数字是否属于某个子序列。
     * 假设当前元素是 x，进行如下操作。
     *
     * 首先判断是否存在以 x-1 结尾的子序列，即根据第二个哈希表判断 x-1 作为结尾的子序列的数量是否大于 0，如果大于 0，则将元素 x 加入该子序列中。
     * 由于 x 被使用了一次，因此需要在第一个哈希表中将 x 的剩余次数减 1。又由于该子序列的最后一个数字从 x−1 变成了 x，
     * 因此需要在第二个哈希表中将 x−1 作为结尾的子序列的数量减 1，以及将 x 作为结尾的子序列的数量加 1。
     *
     * 否则，x 为一个子序列的第一个数，为了得到长度至少为 3 的子序列，x+1 和 x+2 必须在子序列中，
     * 因此需要判断在第一个哈希表中 x+1 和 x+2 的剩余次数是否都大于 0。
     *
     * 当 x+1 和 x+2 的剩余次数都大于 0 时，可以新建一个长度为 3 的子序列 [x,x+1,x+2]。
     * 由于这三个数都被使用了一次，因此需要在第一个哈希表中将这三个数的剩余次数分别减 1。又由于该子序列的最后一个数字是 x+2，
     * 因此需要在第二个哈希表中将 x+2 作为结尾的子序列的数量加 1。
     *
     * 否则，无法得到长度为 3 的子序列 ][x,x+1,x+2]，因此无法完成分割，返回 false。
     *
     * 如果整个数组遍历结束时，没有遇到无法完成分割的情况，则可以完成分割，返回 true。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/split-array-into-consecutive-subsequences/solution/fen-ge-shu-zu-wei-lian-xu-zi-xu-lie-by-l-lbs5/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * nums已排序
     *
     * @param nums
     * @return
     */
    public boolean isPossible(int[] nums) {
        // 保存每个数字频率（还剩几次可用）
        Map<Integer, Integer> freqMap = new HashMap<>();
        // <k,v> 保存的是 以 k - 1 结尾的序列个数，那么 这些序列后面就可以拼接 k
        // 对于元素x，如果想知道它能否序接到某个序列后面，只需要判断 seqNeedMap[x] > 0 是否成立
        Map<Integer, Integer> seqNeedMap = new HashMap<>();
        // 初始化 freqMap。每个数字的出现次数
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        // 对于有序数组 nums 中的每个元素
        for (int num : nums) {
            // 当前数字已被用完
            int freq = freqMap.get(num);
            if (freq == 0) {
                continue;
            }
            // 先判断是否有以 x - 1结尾的序列
            int count = seqNeedMap.getOrDefault(num, 0);
            // 如果有，那随便选择一个拼接上就好
            if (count > 0) {
                // 那么以 x - 1结尾的序列数目 - 1
                freqMap.put(num, freq - 1);
                // 组成新的序列后，就成了以 x 结尾，那么 以 x 结尾的序列个数增加
                seqNeedMap.put(num + 1, seqNeedMap.getOrDefault(num + 1, 0) + 1);
                // 当前数字x的可用次数 - 1
                seqNeedMap.put(num, count - 1);
                continue;
            }
            // 不能序接，另起炉灶，至少需要三个数字
            // 所以 x + 1. x + 2 必须还有可用次数
            if (freqMap.getOrDefault(num + 1, 0) > 0 && freqMap.getOrDefault(num + 2, 0) > 0) {
                // 这种情况下，数字 x 的可用次数 - 1
                freqMap.put(num, freq - 1);
                // 数字 x + 1 的可用次数 - 1
                freqMap.put(num + 1, freqMap.get(num + 1) - 1);
                // 数字 x + 2 的可用次数 - 1
                freqMap.put(num + 2, freqMap.get(num + 2) - 1);
                // 然后这三个组成一个序列。这个序列 以 x + 2 结尾，那么 更新 freqMap，记录
                seqNeedMap.put(num + 3, seqNeedMap.getOrDefault(num + 3, 0) + 1);
            } else {
                // 否则就会出现孤立的数字，或者长度不够3的短序列，返回false
                return false;
            }
        }
        return true;
    }
}
