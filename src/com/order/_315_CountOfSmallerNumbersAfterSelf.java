package com.order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2022/5/18 21:20
 * @description: _315_CountOfSmallerNumbersAfterSelf
 *
 *
 * 315. 计算右侧小于当前元素的个数
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： counts[i] 的值是  nums[i] 右侧小于 nums[i] 的元素的数量。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [5,2,6,1]
 * 输出：[2,1,1,0]
 * 解释：
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧仅有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 示例 2：
 *
 * 输入：nums = [-1]
 * 输出：[0]
 * 示例 3：
 *
 * 输入：nums = [-1,-1]
 * 输出：[0,0]
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * -104 <= nums[i] <= 104
 */
public class _315_CountOfSmallerNumbersAfterSelf {

    /**
     * 这道题本质上和剑指offer第51题 数组中的逆序对数 是一样的
     * 只不过，offer51是问的总的逆序对，这道题相当于问你具体有哪些逆序对
     *
     * 思路和offer51一样，还是归并排序，在归并排序合并两个有序数组的过程中发现并统计逆序对数
     *
     * offer51题由于只需要统计总数，所以它是这样的
     * 当l[i]>r[j]时，由于l已经有序，因此l[i,mid]均大于r[j]，那么这些都是逆序对，totalCount += mid - i + 1
     *
     * 那么对于本题来说
     *    相当于原数组中 l[i,mid]这些数字右边都有一个数组(r[j])比它小，那么 count[l[?]]++;
     *    这相当于此处有个循环分别去给这些数字对应的count值+1
     *    这样当数组特别长时会导致超时
     *
     *    换个思路，上面思路相当于是在 要合并r[j]的时候（按照合并规则，此时应该nums[k++]=r[j++]） 更新count[]
     *    那能够反过来，也就是在要合并l[i]的时候更新count[]
     *    要合并l[i]的情况是 l[i]<=r[j],此时能保证的是 r[mid+1,j-1]部分都是<l[i]的
     *    为什么？
     *          假设上一步也是合并l，那就是l[i-1]<r[j],nums[k++]=l[(i-1)++],不影响
     *          假设上一步是合并r,那么就是l[i]>r[j-1],nums[k++]=r[(j-1)++],那么能看出来 r[j-1]的确是<l[i]的，一直往前推能得到 r[mid+1,j-1]的确都<l[i]
     *    那么此时对 l[i] 来说，意味着原数组中它后面有 r[mid+1,j-1] 这些数字比它小，那么 count[l[i]] += j - mid - 1
     *    要合并l[i]的情况：
     *      一是 l[i]<=r[j]，因为取等也是可以合并l[i]的，并且取等时 r[mid+1,j-1] < l[i]也成立，你不取等，会发现最后的count会少一部分
     *              或者你这样理解，就是 合并r[j]的反情况呗，这里的合并r[j]更准确来说是 上面说的 需要利用for循环逐个更新count的情况
     *      二是 r已经遍历完了，此时每一次都是合并l[i]
     *    因此，在这两种情况下，都要对对应数的对应count进行更新
     *
     *
     *    【注意】
     *    因为我们最后要返回的是：count[i]代表原数组nums[i]中i后面位置中比nums[i]小的元素个数
     *    而我们在归并排序统计逆序对的过程中，知道的是 某个数字 后面比它小的数字个数，
     *          当然我们也知道这个数字当前的索引，但是，排序过程是改变了原数组的。也就是说这个数字此时的索引并不是它原先在nums中的索引，
     *          这个时候你按照此时的索引去更新对应的count，就会出错
     *
     *    那么怎么办 ？ 借助索引数组，我们进行索引排序，不改变原数组，只是在比较时 比较的是索引值对应的nums数组元素大小而已，改变的是索引数组
     */

    // 原数组
    private int[] nums;
    // 索引数组
    private int[] index;
    // count[i]代表原数组nums[i]中i后面位置中比nums[i]小的元素个数
    private int[] count;

    /**
     * 方法：归并排序
     * @param nums
     * @return
     */
    public List<Integer> countSmaller(int[] nums) {
        this.nums = nums;
        count = new int[nums.length];
        index = new int[nums.length];
        // 初始化索引数组
        // index[i] 表示 当前的 i 位置 对应的是 原数组中的哪个位置
        for(int i = 0; i < index.length; ++i) {
            index[i] = i;
        }
        // 归并排序，对索引数组进行排序
        mergeSort(index, 0, index.length - 1);
        List<Integer> ans = new ArrayList<>();
        // 最后要把数组转为要求的List
        for (int x : count) {
            ans.add(x);
        }
        return ans;
    }


    /**
     * 归并排序，并统计逆序对
     * 就比归并排序多了一行代码
     * 对数组index中[start,end]部分进行归并排序
     * 是对索引进行排序，不改变原数组nums
     * @param index
     * @param start
     * @param end
     */
    private void mergeSort(int[] index, int start, int end) {
        // 只有一个元素时默认有序，直接返回
        if (start >= end) {
            return;
        }
        // "分"：
        int mid = start + (end - start) / 2;
        // “治” 左部分
        mergeSort(index, start, mid);
        // “治” 右部分
        mergeSort(index, mid + 1, end);

        // 如果不需要合并，提前退出
        if (nums[index[mid]] <= nums[index[mid+1]]) return;

        // 合并有序数组，temp临时保存合并结果
        int[] temp = new int[end - start + 1];
        int i = start, j = mid + 1, k = 0;
        // 归并排序，左部分在前，右部分在后
        while (i <= mid && j <= end) {
            // 选择合并 r[j]
            if (nums[index[i]] > nums[index[j]]) {
                temp[k++] = index[j++];
                // 如果是统计所有逆序对，就在此处 count += mid - i + 1;
                // 如果在此处更新count数组
                // for (int v = i; v <= mid; ++v) count[v]++;
            } else {
                // 选择合并 l[i] （合并l[i]的情况①）
                // 按照分析，在此处更新count数组能避免for循环，只是理解角度与上面不同
                count[index[i]] += j - mid - 1;
                temp[k++] = index[i++];
            }
        }
        // 【注意】这里是 合并l[i]的第二种情况，不要遗漏 （合并l[i]的情况②）
        while (i <= mid) {
            temp[k++] = index[i++];
            // 这里也要更新count
            count[index[i]] += j - mid - 1;
        }
        while (j <= end) {
            temp[k++] = index[j++];
        }
        // 将合并结果写入原数组
        i = start;
        k = 0;
        while (i <= end) {
            index[i++] = temp[k++];
        }
        return;
    }

    public static void main(String[] args) {
        _315_CountOfSmallerNumbersAfterSelf obj = new _315_CountOfSmallerNumbersAfterSelf();
        obj.countSmaller(new int[]{6, 7, 5, 3, 8, 2, 1, 9});
    }
}
