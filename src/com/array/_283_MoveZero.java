package com.array;

/**
 * @author wangwei
 * 2021/12/8 13:16
 *
 * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 *
 * 示例:
 *
 * 输入: [0,1,0,3,12]
 * 输出: [1,3,12,0,0]
 * 说明:
 *
 * 必须在原数组上操作，不能拷贝额外的数组。
 * 尽量减少操作次数。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/move-zeroes
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _283_MoveZero {

    /**
     * 双指针
     *
     * 注意：要保证非0数的原相对顺序，所以不能 i从前往后，j从后往前，i遇到0元素就和j代表的非0交换，这样会导致顺序打乱
     *
     * 可以快慢指针，i=0，j=0，j每次遇到非0元素，i就保存，如果j到头了，那么 i 直接把剩下元素全补0就行，
     * 时间复杂度 O(n)，就遍历了一次数组
     * @param nums
     */
    public void moveZeroes(int[] nums) {
        int i = 0, j = 0;
        while (j < nums.length) {
            // j 遇到非0元素
            if (nums[j] != 0) {
                // i 记录， i 后移
                nums[i++] = nums[j];
            }
            // j 到头了
            if (j == nums.length - 1) {
                // 剩下的全补0
                while (i < nums.length) {
                    nums[i++] = 0;
                }
            }
            // j 始终后移
            j++;
        }
    }
}
