package com.daily;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangwei
 * @date 2022/12/29 15:40
 * @description: _2032_ValuesAppearInAtLeastTwoArrays
 *
 * 2032. 至少在两个数组中出现的值
 * 给你三个整数数组 nums1、nums2 和 nums3 ，请你构造并返回一个 元素各不相同的 数组，且由 至少 在 两个 数组中出现的所有值组成。数组中的元素可以按 任意 顺序排列。
 *
 *
 * 示例 1：
 *
 * 输入：nums1 = [1,1,3,2], nums2 = [2,3], nums3 = [3]
 * 输出：[3,2]
 * 解释：至少在两个数组中出现的所有值为：
 * - 3 ，在全部三个数组中都出现过。
 * - 2 ，在数组 nums1 和 nums2 中出现过。
 * 示例 2：
 *
 * 输入：nums1 = [3,1], nums2 = [2,3], nums3 = [1,2]
 * 输出：[2,3,1]
 * 解释：至少在两个数组中出现的所有值为：
 * - 2 ，在数组 nums2 和 nums3 中出现过。
 * - 3 ，在数组 nums1 和 nums2 中出现过。
 * - 1 ，在数组 nums1 和 nums3 中出现过。
 * 示例 3：
 *
 * 输入：nums1 = [1,2,2], nums2 = [4,3,3], nums3 = [5]
 * 输出：[]
 * 解释：不存在至少在两个数组中出现的值。
 *
 *
 * 提示：
 *
 * 1 <= nums1.length, nums2.length, nums3.length <= 100
 * 1 <= nums1[i], nums2[j], nums3[k] <= 100
 * 通过次数22,450提交次数31,110
 */
public class _2032_ValuesAppearInAtLeastTwoArrays {

    /**
     * 方法一：哈希表 + 二进制表示
     * 思路与算法
     *
     * 题目给出三个整数数组 nums1、nums2 和 nums3。
     * 现在我们需要求一个元素各不相同的数组，其中的元素为至少在数组 nums1、nums2 和 nums3 中两个数组出现的全部元素。
     *
     * 我们可以用「哈希表」来统计每个元素出现的次数，但是只统计次数无法区分其是否在两个数组中出现，
     * 因为只有三个数组，因此我们可以使用一个整数的最低三个二进制位来标记某一个数在哪几个数组中，
     * 1 表示该数在对应的数组中的，反之 0 表示不在。
     *
     * 具体的，用map存储，遍历nums1的元素，标记它的值为 x = 1（最低二进制位为1），
     *                  遍历nums2元素，标记它的值为 x | 2（倒数第二位二进制为1）
     *                  遍历nums3元素，标记它的值为 x | 4（倒数第三位二进制为1）
     *
     * 最后遍历map中每个数字及它的标志记录值，如果其标记数字中二进制位个数大于 1个 （x & x-1 != 0）就选择保留。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/two-out-of-three/solution/zhi-shao-zai-liang-ge-shu-zu-zhong-chu-x-5131/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums1
     * @param nums2
     * @param nums3
     * @return
     */
    public List<Integer> twoOutOfThree(int[] nums1, int[] nums2, int[] nums3) {
        // 保存每个数字在三个数组中的出现情况
        Map<Integer, Integer> map = new HashMap<>();
        // 遍历nums1的元素，标记它的值为 x = 1（最低二进制位为1），
        for (int num : nums1) {
            map.put(num, 1);
        }
        // 遍历nums2元素，标记它的值为 x | 2（倒数第二位二进制为1）
        for (int num : nums2) {
            map.put(num, map.getOrDefault(num, 0) | 2);
        }
        // 遍历nums3元素，标记它的值为 x | 4（倒数第三位二进制为1）
        for (int num : nums3) {
            map.put(num, map.getOrDefault(num, 0) | 4);
        }
        // 遍历map中每个数字及它的标志记录值，如果其标记数字中二进制位个数大于 1个 就选择保留
        return map.entrySet().stream()
                .filter(e -> (e.getValue() & e.getValue() - 1) != 0)
                .map(e -> e.getKey())
                .collect(Collectors.toList());
    }
}
