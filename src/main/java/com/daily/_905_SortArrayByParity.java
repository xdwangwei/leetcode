package com.daily;

/**
 * @author wangwei
 * @date 2022/4/28 10:05
 * @description: _905_SortArrayByParity
 *
 * 905. 按奇偶排序数组
 * 给你一个整数数组 nums，将 nums 中的的所有偶数元素移动到数组的前面，后跟所有奇数元素。
 *
 * 返回满足此条件的 任一数组 作为答案。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [3,1,2,4]
 * 输出：[2,4,3,1]
 * 解释：[4,2,3,1]、[2,4,1,3] 和 [4,2,1,3] 也会被视作正确答案。
 * 示例 2：
 *
 * 输入：nums = [0]
 * 输出：[0]
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 5000
 * 0 <= nums[i] <= 5000
 * 通过次数76,250提交次数107,893
 */
public class _905_SortArrayByParity {

    /**
     * 双指针原地修改数组，左边全是偶数，右边全是奇数
     * i从左往右遇到第一个奇数停下，j从右往左遇到第一个偶数停下，二者交换
     * 在此过程中 i < j
     * @param nums
     * @return
     */
    public int[] sortArrayByParity(int[] nums) {
        int i = 0, j = nums.length - 1;
        // 退出条件
        while (i < j) {
            // i从左往右遇到第一个奇数停下，
            while (i < j && nums[i] % 2 == 0) {
                i++;
            }
            // j从右往左遇到第一个偶数停下
            while (i < j && nums[j] % 2 == 1) {
                j--;
            }

            if (i >= j) {
                break;
            }
            // ，二者交换
            nums[i] = nums[i] ^ nums[j];
            nums[j] = nums[i] ^ nums[j];
            nums[i] = nums[i] ^ nums[j];
            // 指针前进
            i++;
            j--;
        }
        return nums;
    }


    /**
     *
     * 方法二：双指针+一次遍历
     *
     * 创建res数组返回，返回数组不计入空间复杂度
     *
     * 思路
     *
     * 记数组 nums 的长度为 n。
     *
     * 新建一个长度为 n 的数组 \res 用来保存排完序的数组。
     * 遍历一遍 nums，遇到偶数则从 res 左侧开始替换元素，遇到奇数则从 res 右侧开始替换元素。
     * 遍历完成后，res 就保存了排序完毕的数组。
     *
     * 代码
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/sort-array-by-parity/solution/an-qi-ou-pai-xu-shu-zu-by-leetcode-solut-gpmm/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int[] sortArrayByParity2(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        int left = 0, right = n - 1;
        for (int num : nums) {
            if (num % 2 == 0) {
                res[left++] = num;
            } else {
                res[right--] = num;
            }
        }
        return res;
    }
}
