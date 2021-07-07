package com.array;

/**
 * @author wangwei
 * 2020/4/3 20:08
 * 编写一个函数来判断给定的目标值是否存在于数组中。若存在返回 true，否则返回 false。
 * <p>
 * 示例 1:
 * <p>
 * 输入: nums = [2,5,6,0,0,1,2], target = 0
 * 输出: true
 * 示例 2:
 * <p>
 * 输入: nums = [2,5,6,0,0,1,2], target = 3
 * 输出: false
 * <p>
 * 这是 搜索旋转排序数组 的延伸题目，本题中的 nums  可能包含重复元素。
 * 这会影响到程序的时间复杂度吗？会有怎样的影响，为什么？
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/search-in-rotated-sorted-array-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _81_SearchInRotatedSortedArrayPlus {
    /**
     * 一次全扫描，不合题意，时间复杂度 O(n)
     *
     * @param nums
     * @param target
     * @return
     */
    public boolean solution1(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target)
                return true;
        }
        return false;
    }

    /**
     * 出现 O(logn)想到 二分搜索
     * 因为原数组有序，即使旋转，至少一侧是有序的
     * 如 7，8，0，1，2，3，4，5，6 右侧有序,4,5,6,7,2,3 左侧有序
     * 而无序的那侧，其实也是部分有序，通过二分始终能划分成有序部分，再进行查找即可
     * <p>
     * 在此基础上过滤掉重复元素即可
     *
     * @param nums
     * @param target
     * @return
     */
    public boolean solution2(int[] nums, int target) {
        if (nums.length == 0) return false;
        if (nums.length == 1) return nums[0] == target ? true : false;
        int left = 0, right = nums.length, mid;
        while (left < right) {
            while (left < nums.length - 1 && nums[left] == nums[left + 1]) left++;
            while (right >= 2 && nums[right - 1] == nums[right - 2]) right--;
            // 找中间元素
            mid = left + (right - left) / 2;
            // 判断是否找到目标
            if (nums[mid] == target)
                return true;
            // 左半部分有序
            if (nums[left] < nums[mid]) {
                // 判断target是否在0 - mid之间
                if (nums[left] <= target && target < nums[mid])
                    right = mid; // 在左部分，有边界左移
                else // 不在左边
                    left = mid + 1; // 去右半部分
                // 右半部分有序
            } else {
                // 判断target是否在 mid + 1到 nums.length-1之间
                if (nums[mid] < target && target <= nums[right - 1])
                    left = mid + 1; // 在由部分，左边界右移
                else // 不在右边
                    right = mid; // 去左变
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // new _33_SearchInRotatedSortedArray().solution2(new int[]{4, 5, 6, 7, 0, 1, 2}, 0);
        new _81_SearchInRotatedSortedArrayPlus().solution2(new int[]{3, 1}, 1);
    }
}
