package com.daily;

/**
 * @author wangwei
 * @date 2023/6/6 10:19
 * @description: _2460_ApplyOperationsToAArray
 *
 * 2460. 对数组执行操作
 * 给你一个下标从 0 开始的数组 nums ，数组大小为 n ，且由 非负 整数组成。
 *
 * 你需要对数组执行 n - 1 步操作，其中第 i 步操作（从 0 开始计数）要求对 nums 中第 i 个元素执行下述指令：
 *
 * 如果 nums[i] == nums[i + 1] ，则 nums[i] 的值变成原来的 2 倍，nums[i + 1] 的值变成 0 。否则，跳过这步操作。
 * 在执行完 全部 操作后，将所有 0 移动 到数组的 末尾 。
 *
 * 例如，数组 [1,0,2,0,0,1] 将所有 0 移动到末尾后变为 [1,2,1,0,0,0] 。
 * 返回结果数组。
 *
 * 注意 操作应当 依次有序 执行，而不是一次性全部执行。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [1,2,2,1,1,0]
 * 输出：[1,4,2,0,0,0]
 * 解释：执行以下操作：
 * - i = 0: nums[0] 和 nums[1] 不相等，跳过这步操作。
 * - i = 1: nums[1] 和 nums[2] 相等，nums[1] 的值变成原来的 2 倍，nums[2] 的值变成 0 。数组变成 [1,4,0,1,1,0] 。
 * - i = 2: nums[2] 和 nums[3] 不相等，所以跳过这步操作。
 * - i = 3: nums[3] 和 nums[4] 相等，nums[3] 的值变成原来的 2 倍，nums[4] 的值变成 0 。数组变成 [1,4,0,2,0,0] 。
 * - i = 4: nums[4] 和 nums[5] 相等，nums[4] 的值变成原来的 2 倍，nums[5] 的值变成 0 。数组变成 [1,4,0,2,0,0] 。
 * 执行完所有操作后，将 0 全部移动到数组末尾，得到结果数组 [1,4,2,0,0,0] 。
 * 示例 2：
 *
 * 输入：nums = [0,1]
 * 输出：[1,0]
 * 解释：无法执行任何操作，只需要将 0 移动到末尾。
 *
 *
 * 提示：
 *
 * 2 <= nums.length <= 2000
 * 0 <= nums[i] <= 1000
 * 通过次数28,036提交次数41,066
 */
public class _2460_ApplyOperationsToAnArray {

    /**
     * 方法一：直接模拟
     * 思路与算法
     *
     * 根据题意要求，如果 nums[i]==nums[i+1] ，则需要进行以下变换：
     *      nums[i]=2×nums[i]；
     *      nums[i+1]=0；
     *
     * 在执行上述完操作后，将所有 0 移动到数组的末尾，即将所有非零的元素移动到数组的头部。
     *
     * 双指针i,j = 0
     *
     * 遍历数组下标i，遇到非零元素 arr[i]，与 j 位置元素 arr[j] 进行交换，然后 j++。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/apply-operations-to-an-array/solution/dui-shu-zu-zhi-xing-cao-zuo-by-leetcode-vz70b/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int[] applyOperations(int[] nums) {
        int n = nums.length;
        // 模拟，根据规则，修改数组元素
        for (int i = 0; i < n - 1; ++i) {
            if (nums[i] == nums[i + 1]) {
                nums[i] *= 2;
                nums[i + 1] = 0;
            }
        }
        // 双指针，将所有非零的元素移动到数组的头部。
        for (int i = 0, j = 0; i < n; ++i) {
            // 非零，交换，j++
            if (nums[i] != 0) {
                swap(nums, i, j++);
            }
        }
        return nums;
    }

    /**
     * 交换数组 arr 中 i、j 位置元素
     * @param arr
     * @param i
     * @param j
     */
    private void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    public static void main(String[] args) {
        _2460_ApplyOperationsToAnArray obj = new _2460_ApplyOperationsToAnArray();
        obj.applyOperations(new int[]{1, 2, 2, 1, 1, 0});
    }
}
