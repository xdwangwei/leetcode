package com.hot100;

/**
 * @author wangwei
 * 2022/4/15 20:13
 *
 * 75. 颜色分类
 * 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums ，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 *
 * 我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
 *
 * 必须在不使用库的sort函数的情况下解决这个问题。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [2,0,2,1,1,0]
 * 输出：[0,0,1,1,2,2]
 * 示例 2：
 *
 * 输入：nums = [2,0,1]
 * 输出：[0,1,2]
 */
public class _75_SortColors {

    /**
     * 两次遍历，先把0元素全部交换到前面，再把1元素全部交换到前面，剩下的2自然在最后面
     * @param nums
     */
    public void sortColors(int[] nums) {
        // 新位置指针
        int ptr = 0;
        for (int i = 0; i < nums.length; ++i) {
            // 先把0全部交换到前面
            if (nums[i] == 0) {
                swap(nums, ptr++, i);
            }
        }
        // 换完0，把1全部挪到前面
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] == 1) {
                swap(nums, ptr++, i);
            }
        }
    }

    /**
     * 一次扫描，将 0 全部移到左边，将 2 全部移到 右边，left 和 right 分别记录左0和右2的位置
     * 能够保证的是 [0, left) == 0， (right, len-1] == 2
     * 这样的话，我们从左往右扫描nums[i]，当 i > right 就能保证全部元素已经到正确位置
     * 并且，由于 0 与 nums[left] 交换完后可能还是 0, 2 与 nums[right] 交换完后可能还是2，我们需要解决这种情况，
     * 1个while就可以，并且解决一种重复（0或2）就可以
     * @param nums
     */
    public void sortColors2(int[] nums) {
        int i = 0, left = 0, right = nums.length - 1;
        // i > right,退出
        while (i <= right) {
            // 这里为什么要解决交换2的重复，因为交换2是和right交换，而right是我们while退出的一部分，与left交换不影响退出条件
            // 我们能确定的是 (right, len-1] == 2，所以对于从左往右扫描来说，right才有价值
            // 那如果这里想解决交换0的重复，交换0会改变left，能确定的是[0, left) == 0，那么应该从右往左扫描才能根据left退出
            // 解决 2 和 nums[right] 交换完还是2，
            while (i <= right && nums[i] == 2) {
                swap(nums, i, right--);
            }
            // 这里不用else，如果上面交换完是0，那么需要和左边交换
            if (nums[i] == 0) {
                swap(nums, i, left++);
            }
            i++;
        }
    }

    /**
     * 交换
     * @param nums
     * @param i
     * @param j
     */
    private void swap(int[] nums, int i, int j) {
        if (i == j) {
            return;
        }
        nums[i] = nums[i] ^ nums[j];
        nums[j] = nums[i] ^ nums[j];
        nums[i] = nums[i] ^ nums[j];
    }

    public static void main(String[] args) {
        _75_SortColors obj = new _75_SortColors();
        obj.sortColors2(new int[]{2,0,1});
    }
}
