package com.array;

/**
 * @author wangwei
 * 2020/4/3 22:19
 * 相关题目 33 81
 * 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
 * <p>
 * ( 例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2] )。
 * <p>
 * 请找出其中最小的元素。
 * <p>
 * 你可以假设数组中不存在重复元素。
 * <p>
 * 示例 1:
 * <p>
 * 输入: [3,4,5,1,2]
 * 输出: 1
 * 示例 2:
 * <p>
 * 输入: [4,5,6,7,0,1,2]
 * 输出: 0
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _153_FindMinInSolatedSoredArray {

    public int solution(int[] nums, int target) {
        if (nums.length == 0) return -1;
        if (nums.length == 1) return nums[0];
        int low = 0, high = nums.length - 1, mid;
        while (low <= high) {
            mid = low + (high - low) / 2;
            // 发生旋转的位置 3,4,5,0,1
            if (nums[mid] > nums[mid + 1])
                return nums[mid + 1];
            // 前半段有序，旋转点在后半段，注意这个 = 很关键，
            // 如 4，5，1，2，3，不加等号执行失败
            // 如同 33 和 81题，因数组是增序，所以尽可能左半部分扩大，让下次二分落在左边区域内，可以以这个角度出发理解这个 =
            // 采用方式二二分法(见33题题解2)，此处不加等号 mid和low相等时，if不成立,进入else造成high=mid-1,从而错过了正确答案
            else if (nums[low] <= nums[mid])
                low = mid + 1;
            else
                // 旋转点就在此之前
                high = mid - 1;
        }
        return -1;
    }
}
