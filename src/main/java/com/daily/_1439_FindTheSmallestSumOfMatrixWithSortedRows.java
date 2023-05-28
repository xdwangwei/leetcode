package com.daily;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author wangwei
 * @date 2023/5/28 11:37
 * @description: _1439_FindTheSmallestSumOfMatrixWithSortedRows
 *
 * 1439. 有序矩阵中的第 k 个最小数组和
 * 给你一个 m * n 的矩阵 mat，以及一个整数 k ，矩阵中的每一行都以非递减的顺序排列。
 *
 * 你可以从每一行中选出 1 个元素形成一个数组。返回所有可能数组中的第 k 个 最小 数组和。
 *
 *
 *
 * 示例 1：
 *
 * 输入：mat = [[1,3,11],[2,4,6]], k = 5
 * 输出：7
 * 解释：从每一行中选出一个元素，前 k 个和最小的数组分别是：
 * [1,2], [1,4], [3,2], [3,4], [1,6]。其中第 5 个的和是 7 。
 * 示例 2：
 *
 * 输入：mat = [[1,3,11],[2,4,6]], k = 9
 * 输出：17
 * 示例 3：
 *
 * 输入：mat = [[1,10,10],[1,4,5],[2,3,6]], k = 7
 * 输出：9
 * 解释：从每一行中选出一个元素，前 k 个和最小的数组分别是：
 * [1,1,2], [1,1,3], [1,4,2], [1,4,3], [1,1,6], [1,5,2], [1,5,3]。其中第 7 个的和是 9 。
 * 示例 4：
 *
 * 输入：mat = [[1,1,10],[2,2,9]], k = 7
 * 输出：12
 *
 *
 * 提示：
 *
 * m == mat.length
 * n == mat.length[i]
 * 1 <= m, n <= 40
 * 1 <= k <= min(200, n ^ m)
 * 1 <= mat[i][j] <= 5000
 * mat[i] 是一个非递减数组
 * 通过次数8,862提交次数14,775
 */
public class _1439_FindTheSmallestSumOfMatrixWithSortedRows {


    /**
     *
     * topK 问题，
     *
     * 算法一：暴力
     *
     * 看示例 3，mat=[[1,10,10],[1,4,5],[2,3,6]],k=7。
     *
     * 假设已经算出了从前两行中每行选一个数所有情况，这有 3×3=9 个（从 1+1=2 到 10+5=15）。
     * 把这 9 个数从小到大排序，我们需要考虑其中的第 8 个数和第 9 个数吗？
     *
     * 这是不需要的，因为选第三行的第一个数，与这 9 个数的前 7 个数组成数组，就已经形成了 7 个数组和了，
     * 而第 8 个数和第 9 个数不会形成更小的数组和。
     * 所以，不需要考虑第 8 个数和第 9 个数。
     *
     * 这样就得到了一个暴力算法：从上到下遍历矩阵的行，假设计算出了前 i−1 行形成的前 k 个最小数组和（记作 s），
     * 遍历到第 i 行时，分别把 s 与第 i 行中每个数字两两相加（会得到 len(mat[i]) * len(s)个数字），
     * 然后只保留其中最小的 k 个数，作为新的 s，然后继续遍历矩阵的下一行，重复该过程。
     *
     * 遍历结束后，s[k−1] 就是答案（注意 k 从 1 开始，转成下标要减一）。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/find-the-kth-smallest-sum-of-a-matrix-with-sorted-rows/solution/san-chong-suan-fa-bao-li-er-fen-da-an-du-k1vd/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    private int k;
    public int kthSmallest(int[][] mat, int k) {
        this.k = k;
        // pre 表示计算出了前 i−1 行形成的前 k 个最小数组和（记作 s），初始化为 {0}
        int[] pre = new int[]{0};
        // 逐行遍历
        for (int[] row : mat) {
            // 合并pre 和 row，再筛选出 前 k 给最小值作为新的 pre
            pre = merge2(row, pre);
        }
        // 返回
        return pre[k - 1];
    }

    /**
     * 合并两个有序数组，返回所有可能的数字中前k个最小值
     * @param row
     * @param pre
     * @return
     */
    private int[] merge1(int[] row, int[] pre) {
        int m = row.length, n = pre.length;
        int[] arr = new int[m * n];
        int idx = 0;
        // 暴力，求出所有可能的数字
        for (int x : row) {
            for (int y : pre) {
                arr[idx++] = x + y;
            }
        }
        // 排序
        Arrays.sort(arr);
        // 筛选出前k个最小值，如果总数不超过k个就全部返回
        return Arrays.copyOfRange(arr, 0, Math.min(k, m * n));
    }

    /**
     * 合并两个有序数组，返回所有可能的数字中前k个最小值
     *
     * merge1 并没有利用到两个数组有序的特点
     *
     * 利用优先队列，这和第 373 题是一样的，可以用最小堆来优化
     *
     * 为描述方便，下文把 nums1记作 a，nums2 记作 b。
     *
     * 由于数组是有序的，(a[0],b[0]) 是和最小的数对，计入答案。
     * 并且次小的只能是 (a[0],b[1]) 或 (a[1],b[0])，因为其它没有计入答案的数对和不会比这两个更小。
     *
     * 如果要求第 k 小，就要涉及到更多的数对
     *
     * 为了更高效地比大小，我们可以借助最小堆来优化。
     *
     * 堆中保存下标对 (i,j)，即可能成为下一个数对的 a 的下标  i 和 b 的下标 j。
     *
     * 堆顶是最小的 a[i]+b[j]。
     *
     * 初始把 (0,0) 入堆。
     *
     * 每次出堆时，可能成为下一个数对的是 (i+1,j) 和 (i,j+1)，这俩入堆。（和「初步思路」中的讨论一样，其它的不会比这两个更小。）
     *
     * 但这会导致一个问题：例如当 (1,0) 出堆时，会把  (1,1) 入堆；当 (0,1) 出堆时，也会把 (1,1) 入堆，这样堆中会有重复元素。
     *
     * 为了避免有重复元素，还需要额外用一个哈希表记录在堆中的下标对。只有当下标对不在堆中时，才能入堆。
     *
     * 能否不用哈希表呢？
     *
     * 换个角度，如果要把 (i,j) 入堆，那么出堆的下标对是什么？
     *
     * 根据上面的讨论，出堆的下标对只能是 (i−1,j) 和 (i,j−1)。
     *
     * 只要保证 (i−1,j) 和 (i,j−1) 的其中一个会将 (i,j) 入堆，而另一个什么也不做，就不会出现重复了！
     *
     * 不妨规定 (i,j−1) 出堆时，将 (i,j) 入堆；而  (i−1,j) 出堆时只计入答案，其它什么也不做。
     *
     * 换句话说，在 (i,j) 出堆时，只需将 (i,j+1) 入堆，无需将 (i+1,j) 入堆。
     *
     * 但若按照该规则，初始仅把 (0,0) 入堆的话，只会得到 (0,1),(0,2),⋯ 这些下标对。
     *
     * 所以初始不仅要把 (0,0) 入堆，(1,0),(2,0),⋯ 这些都要入堆。
     *
     * 代码实现时，为了方便比较大小，实际入堆的是三元组 (a[i]+b[j], i, j)。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/find-k-pairs-with-smallest-sums/solution/jiang-qing-chu-wei-shi-yao-yi-kai-shi-ya-i0dj/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     *
     * @param row
     * @param pre
     * @return
     */
    private int[] merge2(int[] row, int[] pre) {
        int m = row.length, n = pre.length;
        // 准备结果
        int[] ans = new int[Math.min(k, m * n)];
        // 优先队列，队列元素 (a[i]+b[j], i, j)。
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        // 初始化，(0,0) 入堆，(1,0),(2,0),⋯ 这些都要入堆。
        // 由于最终只求前k个最小，后续元素及其延申出的其它数对不可能更小，所以没必要继续添加
        for (int i = 0; i < Math.min(k, m); ++i) {
            pq.offer(new int[]{row[i] + pre[0], i, 0});
        }
        int idx = 0;
        // k次出队列，拿到前k个最小元素
        while (!pq.isEmpty() && idx < k) {
            int[] cur = pq.poll();
            // 保存当前最小值
            ans[idx++] = cur[0];
            int i = cur[1], j = cur[2];
            //  (i,j) 出堆时，只需将 (i,j+1) 入堆，无需将 (i+1,j) 入堆。
            // 注意不要越界
            if (j + 1 < n) {
                pq.offer(new int[]{row[i] + pre[j + 1], i, j + 1});
            }
        }
        // 返回前k个最小值
        return ans;
    }
}
