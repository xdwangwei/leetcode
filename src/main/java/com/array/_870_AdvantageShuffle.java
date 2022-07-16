package com.array;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * @author wangwei
 * 2021/11/30 17:20
 *
 * 给定两个大小相等的数组A和B，A 相对于 B 的优势可以用满足A[i] > B[i]的索引 i的数目来描述。
 *
 * 返回A的任意排列，使其相对于 B的优势最大化。
 *
 *
 *
 * 示例 1：
 *
 * 输入：A = [2,7,11,15], B = [1,10,4,11]
 * 输出：[2,11,7,15]
 * 示例 2：
 *
 * 输入：A = [12,24,8,32], B = [13,25,32,11]
 * 输出：[24,32,8,12]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/advantage-shuffle
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _870_AdvantageShuffle {

    /**
     * 双指针 + 优先队列
     *
     * 简单来说：干的过就自己上，干不过就找菜鸡顶替我
     *
     * 对于 nums2[i] ， 如果 nums1[i] > nums2[i]， 那么 i 位置 就选择 nums1[i]
     *                 否则，                      i 位置 放置最菜的那个
     * @param nums1
     * @param nums2
     * @return
     */
    public int[] advantageCount(int[] nums1, int[] nums2) {
        // 不能打乱nums2的顺序，所以 使用优先队列，同时记录索引值和对应值，然后选择最大堆
        PriorityQueue<int[]> queue = new PriorityQueue<>(((o1, o2) -> o2[1] - o1[1]));
        int len = nums1.length;
        // nums2 降序保存
        for (int i = 0; i < len; ++i) {
            queue.offer(new int[]{i, nums2[i]});
        }
        // nums1 可以改变原顺序，所以直接排序，成升序了
        Arrays.sort(nums1);
        // 对于nums2的每个元素，nums1对应的位置应该放多少
        int[] res = new int[len];
        // 双指针，left指向nums1最菜的那个，right指向nums1最好的那个
        int left = 0, right = len - 1;
        // nums从大到小出战
        while (!queue.isEmpty()) {
            int[] poll = queue.poll();
            int index = poll[0], val = poll[1];
            // 如果nums1当前的最大值 比它大，那么自己上阵
            if (nums1[right] > val) {
                res[index] = nums1[right];
                // 最厉害的变成了倒数第二个
                right--;
            } else {
                // 打不过，保留老大，让小弟去送死
                res[index] = nums1[left];
                // 最菜的小弟挂了，倒数第二菜的准备上了
                left++;
            }
        }
        return res;
    }
}
