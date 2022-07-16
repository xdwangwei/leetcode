package com.offer;

/**
 * @author wangwei
 * @date 2022/5/18 20:00
 * @description: _51_ReversePairsInArray
 *
 * 剑指 Offer 51. 数组中的逆序对
 * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，求出这个数组中的逆序对的总数。
 *
 * 限制：
 *
 * 0 <= 数组长度 <= 50000
 *
 *
 * 示例 1:
 *
 * 输入: [7,5,6,4]
 * 输出: 5
 */
public class _51_ReversePairsInArray {

    /**
     *
     * 归并排序
     *
     * 首先为什么会用到排序，因为排序的本质可以看作是把所有逆序对复位，所以排序过程会遇到所有逆序对
     * 然后，为什么是归并排序，冒泡排序可不可以？
     *      首先，即便冒泡排序可以，那不想相当于直接暴力搜索，双重循环去统计每个元素后面比它小的元素个数
     *      然后，冒泡排序每次比较都是相邻两个元素比较，是做不到 当前元素 和 后面所有元素比较的
     *      而归并排序的合并阶段，虽然比较的是l[i]和r[j]，但是l已经是有序的，所以l从i位置剩下元素都比r[j]大，都形成逆序对，我们能直接得到当前部分结果
     *
     * 那么为什么是归并排序？
     *
     * 归并排序」与「逆序对」是息息相关的。归并排序体现了 “分而治之” 的算法思想，具体为：
     *
     * 分： 不断将数组从中点位置划分开（即二分法），将整个数组的排序问题转化为子数组的排序问题；
     * 治： 划分到子数组长度为 1 时，开始向上合并，不断将 较短排序数组 合并为 较长排序数组，直至合并至原数组时完成排序；
     *
     * 合并阶段 本质上是 合并两个排序数组 的过程，而
     *      每当遇到 左子数组当前元素 > 右子数组当前元素 时，意味着 「左子数组当前元素 至 末尾元素」 与 「右子数组当前元素」 构成了若干 「逆序对」 。
     * 因此，考虑【在归并排序的合并阶段统计「逆序对」数量】，完成归并排序时，也随之完成所有逆序对的统计。
     *
     * 这样做为什么是对的
     *
     * 比如 [7 5 6 4] 如果拆分成 [7 5] 和 [6 4]，逆序对就应该是 [7 5] 中的逆序对数 + [6 4]中的逆序对数 + 两部分交叉形成的逆序对数
     * 那么 两部分交叉的逆序对数 在 归并排序的合并阶段可以简单统计出来
     * 而 [7 5] 和 [6 4] 两部分继续按照拆分思想进行归并排序，综上，在所有合并阶段统计逆序对数即可，合理
     *
     *
     * 作者：jyd
     * 链接：https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/solution/jian-zhi-offer-51-shu-zu-zhong-de-ni-xu-pvn2h/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */


    // 保存全部逆序对数
    int count = 0;

    public int reversePairs(int[] nums) {
        // 在归并排序过程中统计逆序对数
        mergeSort(nums, 0, nums.length - 1);
        return count;
    }


    /**
     * 就比归并排序多了一行代码
     * 对数组nums中[start,end]部分进行归并排序
     * @param nums
     * @param start
     * @param end
     */
    private void mergeSort(int[] nums, int start, int end) {
        // 只有一个元素时默认有序，直接返回
        if (start >= end) {
            return;
        }
        // "分"：
        int mid = start + (end - start) / 2;
        // “治” 左部分
        mergeSort(nums, start, mid);
        // “治” 右部分
        mergeSort(nums, mid + 1, end);

        // 如果不需要合并，提前退出
        if (nums[mid] <= nums[mid + 1]) return;

        // 合并有序数组，temp临时保存合并结果
        int[] temp = new int[end - start + 1];
        int i = start, j = mid + 1, k = 0;
        // 归并排序，左部分在前，右部分在后
        while (i <= mid && j <= end) {
            // 【统计逆序对】
            // 前面部分的数字比后面部分某个数字大，形成逆序对
            if (nums[i] > nums[j]) {
                temp[k++] = nums[j++];
                // 虽然比较的是l[i]和r[j]，但是l已经是有序的，所以l从i位置剩下元素都比r[j]大，都形成逆序对
                count += mid - i + 1;
            } else {
                temp[k++] = nums[i++];
            }
        }
        // 归并排序，长度不一致时后序处理
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        while (j <= end) {
            temp[k++] = nums[j++];
        }
        // 将合并结果写入原数组
        i = start;
        k = 0;
        while (i <= end) {
            nums[i++] = temp[k++];
        }
        return;
    }

}
