package com.array;

import java.util.PriorityQueue;
import java.util.Random;

/**
 * @author wangwei
 * 2022/3/17 10:55
 *
 *
 * 215. 数组中的第K个最大元素
 * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 *
 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 *
 *
 *
 * 示例 1:
 *
 * 输入: [3,2,1,5,6,4] 和 k = 2
 * 输出: 5
 * 示例 2:
 *
 * 输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
 * 输出: 4
 *
 *
 * 提示：
 *
 * 1 <= k <= nums.length <= 104
 * -104 <= nums[i] <= 104
 */
public class _215_KthLargestELementInArray {

    /**
     * 方法，先快速排序，再返回倒数第k个元素
     */

    /**
     * 方法一：优先队列
     * 维护一个大小为k的优先队列（小顶堆），堆顶是最小的元素，每次添加第6个元素时，去除顶堆元素
     * 最终，堆中留下的就是k个最大的元素，并且堆顶元素是其中最小值，也就是原数组中第k大的元素
     * @param nums
     * @param k
     * @return
     */
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int num : nums) {
            pq.offer(num);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        return pq.peek();
    }

    /**
     * 快速排序--->快速选择
     * 快速排序算法的每次 mid = partition都能做到 nums[0...mid-1] < nums[mid] < nums[mid+1...]，
     * 相当于找到了第mid大的元素，那么 如果 mid > k ,我们只需要继续在前mid部分进行partition，
     *                             如果 mid < k，我们就去 nums[mid+1....]进行partition
     *
     *
     * 我们刚说了，partition 函数会将 nums[p] 排到正确的位置，使得 nums[lo..p-1] < nums[p] < nums[p+1..hi]。
     *
     * 那么我们可以把 p 和 k 进行比较，如果 p < k 说明第 k 大的元素在 nums[p+1..hi] 中，如果 p > k 说明第 k 大的元素在 nums[lo..p-1] 中。
     *
     * 所以我们可以复用 partition 函数来实现这道题目，不过在这之前还是要做一下索引转化：
     *
     * 【注意！！！】
     * 题目要求的是「第 k 个最大元素」，这个元素其实就是 nums 升序排序后「索引」为 len(nums) - k 的这个元素。
     *
     * @param nums
     * @param k
     * @return
     */
    public int findKthLargest2(int[] nums, int k) {
        int lo = 0, hi = nums.length - 1;
        // 索引转化
        k = nums.length - k;
        while (lo <= hi) {
            // 在 nums[lo..hi] 中选一个分界点
            int p = partition(nums, lo, hi);
            if (p < k) {
                // 第 k 大的元素在 nums[p+1..hi] 中
                lo = p + 1;
            } else if (p > k) {
                // 第 k 大的元素在 nums[lo..p-1] 中
                hi = p - 1;
            } else {
                // 找到第 k 大元素
                return nums[p];
            }
        }
        return -1;
    }

    /**
     * 快速排序的partition
     *
     * partition 函数会将 nums[p] 排到正确的位置，使得 nums[lo..p-1] < nums[p] < nums[p+1..hi]
     * @param nums
     * @param lo
     * @param hi
     * @return
     */
    private int partition(int[] nums, int lo, int hi) {
        if (lo == hi) return lo;
        // 将 nums[lo] 作为默认分界点 pivot
        int pivot = nums[lo];
        // j = hi + 1 因为 while 中会先执行 --
        int i = lo, j = hi + 1;
        while (true) {
            // 保证 nums[lo..i] 都小于 pivot
            while (nums[++i] < pivot) {
                if (i == hi) break;
            }
            // 保证 nums[j..hi] 都大于 pivot
            while (nums[--j] > pivot) {
                if (j == lo) break;
            }
            if (i >= j) break;
            // 如果走到这里，一定有：
            // nums[i] > pivot && nums[j] < pivot
            // 所以需要交换 nums[i] 和 nums[j]，
            // 保证 nums[lo..i] < pivot < nums[j..hi]
            swap(nums, i, j);
        }
        // 将 pivot 值交换到正确的位置
        // 结束时，i == j，此时已经完成[start:i]都<=pivot,[i:end]都>=pivot
        // 但是pivot还没放放置到合适位置，需要再完成一次交换，因为nums[i]已经<=pivot了，所以直接交换这两个不会破坏
        swap(nums, lo, i);
        // 现在 nums[lo..j-1] < nums[j] < nums[j+1..hi]
        return j;
    }

    /**
     * 数组元素交换
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

    /**
     * partition 函数中需要利用 双指针技巧 遍历 nums[lo..hi]，那么总共遍历了多少元素呢？
     *
     * 最好情况下，每次 p 都恰好是正中间 (lo + hi) / 2，那么遍历的元素总数就是：
     *
     * N + N/2 + N/4 + N/8 + … + 1
     *
     * 这就是等比数列求和公式嘛，求个极限就等于 2N，所以遍历元素个数为 2N，时间复杂度为 O(N)。
     *
     * 但我们其实不能保证每次 p 都是正中间的索引的，最坏情况下 p 一直都是 lo + 1 或者一直都是 hi - 1，遍历的元素总数就是：
     *
     * N + (N - 1) + (N - 2) + … + 1
     *
     * 这就是个等差数列求和，时间复杂度会退化到 O(N^2)，
     *
     * 为了尽可能防止极端情况发生，我们需要在算法开始的时候对 nums 数组来一次随机打乱：shuffle
     *
     * @param nums
     * @param k
     * @return
     */
    public int findKthLargest3(int[] nums, int k) {
        // 首先随机打乱数组
        shuffle(nums);
        // 其他都不变
        int lo = 0, hi = nums.length - 1;
        k = nums.length - k;
        while (lo <= hi) {
            // ...
        }
        return -1;
    }

    /**
     * 对数组元素进行随机打乱
     * 分析洗牌算法正确性的准则：产生的结果必须有 n! 种可能，否则就是错误的。
     * 这个很好解释，因为一个长度为 n 的数组的全排列就有 n! 种，也就是说打乱结果总共有 n! 种。
     * 算法必须能够反映这个事实，才是正确的。
     *
     * 假设数组长度是5
     * for 循环第一轮迭代时，i=0，rand 的取值范围是 [0,4]，有 5 个可能的取值。
     * for 循环第二轮迭代时，i=1，rand 的取值范围是 [1,4]，有 4 个可能的取值。
     * 后面以此类推，直到最后一次迭代，i=4，rand 的取值范围是 [4,4]，只有 1 个可能的取值。
     *
     * 可以看到，整个过程产生的所有可能结果有 5*4*3*2*1=5!=n! 种，所以这个算法是正确的。
     *
     * 此类算法都是靠随机选取元素交换来获取随机性，直接看代码（伪码），该算法有 4 种形式，都是正确的：
     *
     *
     *
     * // 得到一个在闭区间 [min, max] 内的随机整数
     * int randInt(int min, int max);
     *
     * // 第一种写法
     * void shuffle(int[] arr) {
     *     int n = arr.length();
     *     /******** 区别只有这两行 ********
     *      for(int i = 0;i<n;i++){
     *         // 从 i 到最后随机选一个元素
     *          int rand = randInt(i, n - 1);
     *         /*************************
     *          swap(arr[i], arr[rand]);
     *      }
     *
}
     *
             * // 第二种写法
             *for(int i=0;i<n -1;i++)
        *int rand=randInt(i,n-1);
        *
        * // 第三种写法
        *for(int i=n-1;i>=0;i--)
        *int rand=randInt(0,i);
        *
        * // 第四种写法
        *for(int i=n-1;i>0;i--)
        *int rand=randInt(0,i);
     */
    private void shuffle(int[] nums) {
        int n = nums.length;
        Random rand = new Random();
        for (int i = 0 ; i < n; i++) {
            // 从 i 到最后随机选一个元素
            int r = i + rand.nextInt(n - i);
            swap(nums, i, r);
        }
    }
}
