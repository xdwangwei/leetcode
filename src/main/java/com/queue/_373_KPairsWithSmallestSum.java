package com.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author wangwei
 * @date 2022/12/24 15:14
 * @description: _373_KPairsWithSmallestSum
 *
 * 373. 和最小的 k 个数对
 * 给定两个以升序排列的整数数组 nums1 和 nums2 , 以及一个整数 k 。
 *
 * 定义一对值 (u,v)，其中第一个元素来自 nums1，第二个元素来自 nums2 。
 *
 * 请找到和最小的 k 个数对 (u1,v1),  (u2,v2)  ...  (uk,vk) 。
 *
 *
 *
 * 示例 1:
 *
 * 输入: nums1 = [1,7,11], nums2 = [2,4,6], k = 3
 * 输出: [1,2],[1,4],[1,6]
 * 解释: 返回序列中的前 3 对数：
 *     [1,2],[1,4],[1,6],[7,2],[7,4],[11,2],[7,6],[11,4],[11,6]
 * 示例 2:
 *
 * 输入: nums1 = [1,1,2], nums2 = [1,2,3], k = 2
 * 输出: [1,1],[1,1]
 * 解释: 返回序列中的前 2 对数：
 *      [1,1],[1,1],[1,2],[2,1],[1,2],[2,2],[1,3],[1,3],[2,3]
 * 示例 3:
 *
 * 输入: nums1 = [1,2], nums2 = [3], k = 3
 * 输出: [1,3],[2,3]
 * 解释: 也可能序列中所有的数对都被返回:[1,3],[2,3]
 *
 *
 * 提示:
 *
 * 1 <= nums1.length, nums2.length <= 104
 * -109 <= nums1[i], nums2[i] <= 109
 * nums1, nums2 均为升序排列
 * 1 <= k <= 1000
 *
 *
 */
public class _373_KPairsWithSmallestSum {


    /**
     * 方法一：优先队列
     * 思路
     *
     * 对于已经按升序排列的两个数组 nums1,nums2，长度分别为 m,n，
     * 我们可以知道和最小的数对一定为 (num1[0], nums2[0])，和最大的数对一定为 (nums1[m-1], nums2[n-1])
     *
     * 本题要求找到最小的 k 个数对，最直接的办法是可以将所有的数对求出来，然后利用排序或者 TopK 解法求出最小的 k 个数对即可。
     * 实际求解时可以不用求出所有的数对，只需从所有符合待选的数对中选出最小的即可，
     * 假设当前已选的前 n 小数对的索引分别为 (a1,b1),(a2,b2),(a3,b3),...,(an,bn)，
     * 由于两个数组都是按照升序进行排序的，则可以推出
     * 第 n+1 小的数对的索引选择范围为 (a1+1,b1),(a1,b1+1),(a2+1,b2),(a2,b2+1),(a3+1,b3),(a3,b3+1),...,(an+1,bn),(an,bn+1)，
     *
     * 假设我们利用堆的特性可以求出待选范围中最小数对的索引为 (a_i,b_i)，
     * 同时将新的待选的数对 (a_i+1,b_i),(a_i,b_i+1) 加入到堆中，直到我们选出 k 个数对即可。
     *
     * 如果我们每次都将已选的数对 (a_i,b_i) 的待选索引 (a_i+1,b_i),(a_i,b_i+1) 加入到堆中则可能出现重复添加的问题，
     * 一般需要设置标记位解决去重的问题。
     * 我们可以将 nums1 的前 k 个索引数对 (0,0),(1,0),…,(k−1,0) 加入到队列中，
     * 每次从队列中取出元素 (x,y) 时，我们只需要将 nums2 的索引(x, y+1)增加即可，这样避免了重复加入元素的问题。
     *
     * 这其实是一种多路归并的思路：
     * 【多路归并】
     * 令 nums1 的长度为 n，nums2 的长度为 m，所有的点对数量为 n∗m。
     *
     * 其中每个 nums1[i] 参与所组成的点序列为：
     *
     * [(nums1[0],nums2[0]),  (nums1[0],nums2[1]), ... ,(nums1[0],nums2[m−1])]
     * [(nums1[1],nums2[0]),  (nums1[1],nums2[1]), ... ,(nums1[1],nums2[m−1])]
     * ...
     * [(nums1[n−1],nums2[0]),(nums1[n−1],nums2[1]),...,(nums1[n−1],nums2[m−1])]
     *
     * 由于 nums1 和 nums2 均已按升序排序，因此每个 nums1[i] 参与构成的点序列也为升序排序，视为一个有序链表
     * 我们的目的实际上是合并这n个有序序列，并选择最终结果的前k个。
     * 我们可以借助【合并k个有序链表】的思路，来优化空间复杂度到O(k)来完成此过程：
     *
     * 具体的，起始我们将这 n 个序列的首位元素（点对）以二元组 (i,j) 放入优先队列（小根堆），
     * 其中 i 为该点对中 nums1[i] 的下标，j 为该点对中 nums2[j] 的下标，这步操作的复杂度为 O(nlogn)。

     * 每次从优先队列（堆）中取出堆顶元素（含义为当前未被加入到答案的所有点对中的最小值），加入答案，
     * 并将该点对所在序列的下一位（如果有）加入优先队列中。
     *
     * 举个 🌰，首次取出的二元组为 (0,0)，即点对 (nums1[0],nums2[0])，
     * 取完后将序列的下一位点对 (nums1[0],nums2[1]) 以二元组 (0,1) 形式放入优先队列。
     *
     * 可通过「反证法」证明，每次这样的「取当前，放入下一位」的操作，可以确保当前未被加入答案的所有点对的最小值必然在优先队列（堆）中，
     * 即前 k 个出堆的元素必然是所有点对的前 k 小的值。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/find-k-pairs-with-smallest-sums/solution/gong-shui-san-xie-duo-lu-gui-bing-yun-yo-pgw5/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/find-k-pairs-with-smallest-sums/solution/cha-zhao-he-zui-xiao-de-kdui-shu-zi-by-l-z526/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums1
     * @param nums2
     * @param k
     * @return
     */
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        int m = nums1.length, n = nums2.length;
        // 维护大小为k的优先队列，队列元素为(x,y)表示(nums1[x],nums2[y])，按照数对和从小到达排序
        PriorityQueue<int[]> queue = new PriorityQueue<>((o1, o2) -> nums1[o1[0]] + nums2[o1[1]] - nums1[o2[0]] - nums2[o2[1]]);
        // 加入nums1各个元素与nums2[0,1,2,...]形成的有序数对链表的头元素，即（nums1[?],nums2[0]）
        // 由于最终只需要前k个结果，因此我们合并这m个链表中前k个链表即可。
        for (int i = 0; i < Math.min(m, k); ++i) {
            queue.offer(new int[]{i, 0});
        }
        List<List<Integer>> result = new ArrayList<>();
        // 从队列中取出k个元素就停止
        while (k-- > 0 && !queue.isEmpty()) {
            int[] pair = queue.poll();
            int idx1 = pair[0], idx2 = pair[1];
            // 加入当前数对
            result.add(List.of(nums1[idx1], nums2[idx2]));
            // 加入当前元素在当前链表中的下一个元素
            if (idx2 + 1 < n) {
                queue.offer(new int[]{idx1, idx2 + 1});
            }
        }
        // 返回
        return result;
    }
}
