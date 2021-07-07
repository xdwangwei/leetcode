package com.array;

import java.util.Arrays;

/**
 * @author wangwei
 * 2020/8/29 7:57
 *
 * 给定一些标记了宽度和高度的信封，宽度和高度以整数对形式(w, h)出现。
 * 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
 *
 * 请计算最多能有多少个信封能组成一组“俄罗斯套娃”信封（即可以把一个信封放到另一个信封里面）。
 *
 * 说明:
 * 不允许旋转信封。
 *
 * 示例:
 *
 * 输入: envelopes = [[5,4],[6,4],[6,7],[2,3]]
 * 输出: 3
 * 解释: 最多信封的个数为 3, 组合为: [2,3] => [5,4] => [6,7]。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/russian-doll-envelopes
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _354_RussianDollEnvelops {

    public int maxEnvelopes(int[][] envelopes) {
        int len = envelopes.length;
        // 按照【宽度】进行【升序】排序，当宽度相同时按照高度【降序】排序
        // 把所有的高度拿出来，作为一个数组，在这个数组中求LIS，即可、
        // 因为宽度相同的两个信封不能嵌套。所以当宽度相同时，如果还是按照高度升序排序
        // 那么 (4,5) (4,6) 中的 5,6会被算入LIS，实际上这是不合理的，所以要按照高度降序
        Arrays.sort(envelopes, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);

        // 把所有高度拿出来，单独作为一个数组
        int[] height = new int[len];
        for (int i = 0; i < len; i++) {
            height[i] = envelopes[i][1];
        }
        // 按照二分查找（扑克牌算法）求解LIS
        return lengthOfLIS(height);
    }

    private int lengthOfLIS(int[] nums) {
        int len = nums.length;
        // 从左到右每个牌堆顶的牌组成有序序列有序
        int[] top = new int[len];
        // 牌堆数
        int piles = 0;
        for (int i = 0; i < len; i++) {
            // 当前待处理的扑克牌
            int poker = nums[i];

            /** 寻找左侧边界的二分查找 **/
            int left = 0, right = piles;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (top[mid] >= poker) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            // 没找到合适位置，就单独作为一个牌堆，牌堆数加1
            if (left == piles) piles++;
            // 把它放在最左边那个牌堆顶
            top[left] = poker;
        }
        // 牌堆数就是LIS长度
        return piles;
    }
}
