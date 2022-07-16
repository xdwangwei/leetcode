package com.window;

/**
 * @author wangwei
 * 2020/7/29 11:07
 *
 *
给定一个由若干 0 和 1 组成的数组 A，我们最多可以将 K 个值从 0 变成 1 。

返回仅包含 1 的最长（连续）子数组的长度。



示例 1：

输入：A = [1,1,1,0,0,0,1,1,1,1,0], K = 2
输出：6
解释：
[1,1,1,0,0,1,1,1,1,1,1]
粗体数字从 0 翻转到 1，最长的子数组长度为 6。
示例 2：

输入：A = [0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1], K = 3
输出：10
解释：
[0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1]
粗体数字从 0 翻转到 1，最长的子数组长度为 10
 */
public class _1004_MaxConsecutiveOnes3 {

    /**
     * 滑动窗口
     *
     * 关于maxCount的详细解释，请看第_424_
     *
     * @param A
     * @param K
     * @return
     */
    public int longestOnes(int[] A, int K) {
        // 最终结果
        int res = 0;
        int left = 0, right = 0;

        // 历史【有效窗口】中，出现次数最多的那个字符（0或1）的次数
        // 并且，这个maxCount + k >= 此窗口大小
        // 所谓有效窗口，就是 假如这个窗口内出现次数最多的是A，次数是c。
        // 这个窗口能满足，在k次变换的限制下，把整个窗口内的字符都变成A

        // 但是这个题目要求找的是最长的1，所以maxCount应该记录1
        int maxCount = 0;

        // 当前窗口范围内，每个字符(0,1)的出现次数
        int[] count = new int[2];

        while (right < A.length) {
            // 当前加入窗口的字符
            int c = A[right];
            // 次数加1
            count[c]++;
            // 窗口右边界扩大
            right++;
            // 我们需要找的是最长的有效窗口(能满足maxCount+k>=窗口大小)
            // 所以【只需要关心】maxCount的【扩大】更新，这只可能发生在窗口右边新加入字符
            // 这个字符是1的时候才能更新maxCount
            if (c == 1) {
                maxCount = Math.max(maxCount, count[c]);
            }

            // 当前窗口中出现次数最多的字符的次数maxCount，剩余字符最终只能变换k次
            // 也就是说最多把k个其他字符变成次数最多的字符，变换后的总个数还是不能达到窗口大小
            // 这就是个无效窗口，需要收缩左边界
            while (maxCount + K < right - left) {
                // 移除最左边字符
                int d = A[left];
                // 他在窗口内的出现次数减1
                count[d]--;
                // 左边界右移
                left++;
            }

            // 缩小后的窗口才是有效窗口，此时更新结果
            res = Math.max(res, right - left);
        }
        return res;
    }
}
