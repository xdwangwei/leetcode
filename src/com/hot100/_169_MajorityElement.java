package com.hot100;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/4/20 19:28
 * @description: _169_MajorityElement
 *
 * 169. 多数元素
 * 给定一个大小为 n 的数组，找到其中的多数元素。多数元素是指在数组中出现次数 大于 ⌊ n/2 ⌋ 的元素。
 *
 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
 *
 *
 *
 * 示例 1：
 *
 * 输入：[3,2,3]
 * 输出：3
 * 示例 2：
 *
 * 输入：[2,2,1,1,1,2,2]
 * 输出：2
 *
 *
 * 进阶：
 *
 * 尝试设计时间复杂度为 O(n)、空间复杂度为 O(1) 的算法解决此问题。
 */
public class _169_MajorityElement {


    /**
     * 统计每个数字出现次数，返回次数 大于 n/2 的那个数字
     * 时间复杂度 O(n), 空间复杂度O(n)
     * @param nums
     * @return
     */
    public int majorityElement(int[] nums) {
        int n = nums.length;
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            // 出现次数+1
            int count = countMap.getOrDefault(nums[i], 0) + 1;
            if (count > n / 2) {
                // 直接返回
                return nums[i];
            }
            // 更新次数
            countMap.put(nums[i], count);
        }
        return -1;
    }

    /**
     * 因为目标元素出现次数大于n/2,所以排序完这个位置的元素一定是目标元素
     * @param nums
     * @return
     */
    public int majorityElement2(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }


    /**
     * Boyer-Moore 投票算法
     *
     * 思路
     *
     * 目标元素出现次数大于n/2, 比其他元素总和还多。
     *
     * 算法
     *
     * Boyer-Moore 算法的本质和方法四中的分治十分类似。我们首先给出 Boyer-Moore 算法的详细步骤：
     *
     * 我们维护一个候选众数 candidate 和它出现的次数 count。初始时 candidate 可以为任意值，count 为 0；
     *
     * 我们遍历数组 nums 中的所有元素，对于每个元素 x，在判断 x 之前，如果 count 的值为 0，我们先将 x 的值赋予 candidate，随后我们判断 x：
     *
     *      如果 x 与 candidate 相等，那么计数器 count 的值增加 1；
     *
     *      如果 x 与 candidate 不等，那么计数器 count 的值减少 1。
     *
     * 在遍历完成后，candidate 即为整个数组的众数。
     *
     * 通俗理解：投票法，众数个数至少比非众数多一，把COUNT加减当作一个众数抵消掉一个非众数，剩下的一定是众数
     *
     * 随便选一个作为候选人，然后找剩下的一个一个来跟他pk，如果相等就加一条命，如果不相等就跟你一换一。
     * count记录的就是当前命数。
     * count等于0时就再从活着的元素中选一个候选人出来跟其他继续pk。
     * 如果候选人不是众数，那一定会被pk死，更换候选人。
     * 如果候选人是目标元素(多于一半)，那就一定至少有一个能活到最后。
     * 如果是两个不想等的非目标pk掉了，目标元素的人数优势更大。
     * 如果极限情况，非目标都相等，那目标的人数优势也能保证最后至少能活下一个。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/majority-element/solution/duo-shu-yuan-su-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int majorityElement3(int[] nums) {
        int count = 0, candidate = -10086;
        for (int num : nums) {
            // count=0,随机挑选一位选手
            if (count == 0) {
                candidate = num;
            }
            // 如果当前选手和上一个候选人一样，count++，否则count--
            count = candidate == num ? count + 1 : count - 1;
        }
        // 最后至少会剩下一个目标选手
        return candidate;
    }
}
