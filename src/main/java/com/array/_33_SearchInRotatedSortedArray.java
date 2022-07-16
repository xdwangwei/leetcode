package com.array;

/**
 * @author wangwei
 * 2020/4/3 20:08
 * 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
 * <p>
 * ( 例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2] )。
 * <p>
 * 搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1 。
 * <p>
 * 你可以假设数组中不存在重复的元素。
 * <p>
 * 你的算法时间复杂度必须是 O(log n) 级别。
 * <p>
 * 示例 1:
 * <p>
 * 输入: nums = [4,5,6,7,0,1,2], target = 0
 * 输出: 4
 * 示例 2:
 * <p>
 * 输入: nums = [4,5,6,7,0,1,2], target = 3
 * 输出: -1
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/search-in-rotated-sorted-array
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _33_SearchInRotatedSortedArray {
    /**
     * 一次全扫描，不合题意，时间复杂度 O(n)
     *
     * @param nums
     * @param target
     * @return
     */
    public int solution1(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target)
                return i;
        }
        return -1;
    }

    /**
     * 出现 O(logn)想到 二分搜索
     * 因为原数组有序，即使旋转，至少一侧是有序的
     * 如 7，8，0，1，2，3，4，5，6 右侧有序,4,5,6,7,2,3 左侧有序
     * 而无序的那侧，其实也是部分有序，通过二分始终能划分成有序部分，再进行查找即可
     *
     * @param nums
     * @param target
     * @return
     */
    public int solution2(int[] nums, int target) {
        if (nums.length == 0) return -1;
        if (nums.length == 1) return nums[0] == target ? 0 : -1;
        int left = 0, right = nums.length, mid;
        while (left < right) {
            // 找中间元素
            mid = left + (right - left) / 2;
            // 判断是否找到目标
            if (nums[mid] == target)
                return mid;
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
        return -1;
    }

    /**
     * 二分查找有两种写法
     * l = 0, h = length - 1, l <= h, l = mid + 1, h = mid - 1
     * l = 0, h = length, l < h, l = mid + 1, h =  mid
     * 此题相当于增加了判断条件是改变后的二分查找
     * 下面这种写法选择方式一 在判断 [3,1] 1,会出错
     * l = 0, h = 1, mid = 0, nums[l] < nums[mid] 不成立，进入第一个 else,但是if无法成立，进入第二个else,mid < 0，推出，返回 -1
     * 若采用方式一，l = 0, h = 2, mid = 1, nums[mid] = target，返回mid
     * 这与单纯二分法的选择没有关系，只是这个题改造了二分法，这个题给出的数组本身是一个增序，
     * 而第二种方式在处理两个元素的数组时，第一次的mid会为1，相当于左半部分划分的更多，对于递增的数组具有优势
     * 而方式一处理两个元素的数组，第一次mid=0,这个特殊用例[3,1] 1导致修改后的二分失败
     *
     * 这种情况下，需要将 判断左边是否有序的条件 if (nums[left] < nums[mid]) 改为 <=，
     * 具体过程，走一次 [3,1] 1的 用例即可
     * @param nums
     * @param target
     * @return
     */
    public int solution21(int[] nums, int target) {
        if (nums.length == 0) return -1;
        if (nums.length == 1) return nums[0] == target ? 0 : -1;
        int left = 0, right = nums.length - 1, mid;
        while (left <= right) {
            // 找中间元素
            mid = left + (right - left) / 2;
            // 判断是否找到目标
            if (nums[mid] == target)
                return mid;
            // 左半部分有序
            // 采用方式二二分法，此处不加等号 mid和low相等时，if不成立,进入else造成high=mid-1,从而错过了正确答案
            if (nums[left] <= nums[mid]) {
                // 判断target是否在0 - mid之间
                if (nums[left] <= target && target < nums[mid])
                    right = mid - 1; // 在左部分，有边界左移
                else // 不在左边
                    left = mid + 1; // 去右半部分
                // 右半部分有序
            } else {
                // 判断target是否在 mid + 1到 nums.length-1之间
                if (nums[mid] < target && target <= nums[right])
                    left = mid + 1; // 在由部分，左边界右移
                else // 不在右边
                    right = mid - 1; // 去左变
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        // new _33_SearchInRotatedSortedArray().solution2(new int[]{4, 5, 6, 7, 0, 1, 2}, 0);
        new _33_SearchInRotatedSortedArray().solution2(new int[]{3, 1}, 1);
    }
}
