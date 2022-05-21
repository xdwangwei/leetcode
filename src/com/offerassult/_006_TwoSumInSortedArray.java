package com.offerassult;

/**
 * @author wangwei
 * @date 2022/5/21 10:02
 * @description: _006_TwoSumInSortedArray
 *
 * 剑指 Offer II 006. 排序数组中两个数字之和
 * 给定一个已按照 升序排列  的整数数组 numbers ，请你从数组中找出两个数满足相加之和等于目标数 target 。
 *
 * 函数应该以长度为 2 的整数数组的形式返回这两个数的下标值。numbers 的下标 从 0 开始计数 ，所以答案数组应当满足 0 <= answer[0] < answer[1] < numbers.length 。
 *
 * 假设数组中存在且只存在一对符合条件的数字，同时一个数字不能使用两次。
 *
 *
 *
 * 示例 1：
 *
 * 输入：numbers = [1,2,4,6,10], target = 8
 * 输出：[1,3]
 * 解释：2 与 6 之和等于目标数 8 。因此 index1 = 1, index2 = 3 。
 * 示例 2：
 *
 * 输入：numbers = [2,3,4], target = 6
 * 输出：[0,2]
 * 示例 3：
 *
 * 输入：numbers = [-1,0], target = -1
 * 输出：[0,1]
 *
 *
 * 提示：
 *
 * 2 <= numbers.length <= 3 * 104
 * -1000 <= numbers[i] <= 1000
 * numbers 按 递增顺序 排列
 * -1000 <= target <= 1000
 * 仅存在一个有效答案
 *
 *
 * 注意：本题与主站 167 题相似（下标起点不同）：https://leetcode-cn.com/problems/two-sum-ii-input-array-is-sorted/
 */
public class _006_TwoSumInSortedArray {


    /**
     * 双指针
     *
     * 初始时两个指针分别指向第一个元素位置和最后一个元素的位置。
     * 每次计算两个指针指向的两个元素之和，并和目标值比较。
     * 如果两个元素之和等于目标值，则发现了唯一解。
     * 如果两个元素之和小于目标值，则将左侧指针右移一位。
     * 如果两个元素之和大于目标值，则将右侧指针左移一位。移动指针之后，重复上述操作，直到找到答案。
     *
     * 使用双指针的实质是缩小查找范围。那么会不会把可能的解过滤掉？答案是不会。
     * 假设 numbers[i]+numbers[j]=target 是唯一解，其中 0 <= i<j numbers.length−1。
     * 初始时两个指针分别指向下标 0=0 和下标 numbers.length−1，左指针指向的下标小于或等于 i，右指针指向的下标大于或等于 j。
     * 除非初始时左指针和右指针已经位于下标 i 和 j，否则一定是左指针先到达下标 i 的位置或者右指针先到达下标 j 的位置。
     *
     * 如果左指针先到达下标 i 的位置，此时右指针还在下标 j 的右侧，sum>target，因此一定是右指针左移，左指针不可能移到 i 的右侧。
     * 如果右指针先到达下标 j 的位置，此时左指针还在下标 i 的左侧，sum<target，因此一定是左指针右移，右指针不可能移到 j 的左侧。
     * 由此可见，在整个移动过程中，左指针不可能移到 i 的右侧，右指针不可能移到 j 的左侧，因此不会把可能的解过滤掉。
     * 由于题目确保有唯一的答案，因此使用双指针一定可以找到答案。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/kLl5u1/solution/pai-xu-shu-zu-zhong-liang-ge-shu-zi-zhi-8tv13/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param numbers
     * @param target
     * @return
     */
    public int[] twoSum(int[] numbers, int target) {
        // 原数组有序。直接左右指针
        int low = 0, high = numbers.length - 1;
        while (low < high) {
            int sum = numbers[low] + numbers[high];
            // 找到目标值
            if (sum == target) {
                return new int[]{low, high};
            } else if (sum < target) {
                ++low;
            } else {
                --high;
            }
        }
        return new int[]{-1, -1};
    }
}
