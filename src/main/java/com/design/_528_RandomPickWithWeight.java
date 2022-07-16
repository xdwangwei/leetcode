package com.design;

import java.util.Random;

/**
 * @author wangwei
 * 2022/3/2 12:05
 *
 * 给你一个 下标从 0 开始 的正整数数组w ，其中w[i] 代表第 i 个下标的权重。
 *
 * 请你实现一个函数pickIndex，它可以 随机地 从范围 [0, w.length - 1] 内（含 0 和 w.length - 1）选出并返回一个下标。选取下标 i的 概率 为 w[i] / sum(w) 。
 *
 * 例如，对于 w = [1, 3]，挑选下标 0 的概率为 1 / (1 + 3)= 0.25 （即，25%），而选取下标 1 的概率为 3 / (1 + 3)= 0.75（即，75%）。
 *
 *
 * 示例 1：
 *
 * 输入：
 * ["Solution","pickIndex"]
 * [[[1]],[]]
 * 输出：
 * [null,0]
 * 解释：
 * Solution solution = new Solution([1]);
 * solution.pickIndex(); // 返回 0，因为数组中只有一个元素，所以唯一的选择是返回下标 0。
 * 示例 2：
 *
 * 输入：
 * ["Solution","pickIndex","pickIndex","pickIndex","pickIndex","pickIndex"]
 * [[[1,3]],[],[],[],[],[]]
 * 输出：
 * [null,1,1,1,1,0]
 * 解释：
 * Solution solution = new Solution([1, 3]);
 * solution.pickIndex(); // 返回 1，返回下标 1，返回该下标概率为 3/4 。
 * solution.pickIndex(); // 返回 1
 * solution.pickIndex(); // 返回 1
 * solution.pickIndex(); // 返回 1
 * solution.pickIndex(); // 返回 0，返回下标 0，返回该下标概率为 1/4 。
 *
 * 由于这是一个随机问题，允许多个答案，因此下列输出都可以被认为是正确的:
 * [null,1,1,1,1,0]
 * [null,1,1,1,1,1]
 * [null,1,1,1,0,0]
 * [null,1,1,1,0,1]
 * [null,1,0,1,0,0]
 * ......
 * 诸若此类。
 *
 *
 * 提示：
 *
 * 1 <= w.length <= 104
 * 1 <= w[i] <= 105
 * pickIndex将被调用不超过 104次
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/random-pick-with-weight
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _528_RandomPickWithWeight {

    /**
     * 类似于遗传算法中 轮盘赌选择法，借助 前缀和 和 二分搜索
     *
     * 题目：数组元素全部为正整数
     *
     * 设数组 w 的权重之和为 total。根据题目的要求，我们可以看成将 [1,total] 范围内的所有整数分成 n 个部分（其中 n 是数组 w 的长度），
     * 第 i 个部分恰好包含 w[i] 个整数，并且这 n 个部分两两的交集为空。
     * 随后我们在 [1,total] 范围内随机选择一个整数 x，如果整数 x 被包含在第 i 个部分内，我们就返回 i。
     *
     * 一种较为简单的划分方法是按照从小到大的顺序依次划分每个部分。
     * 例如 w=[3,1,2,4] 时，权重之和 total=10，那么我们按照 [1, 3], [4, 4], [5, 6], [7, 10] 对 [1, 10]进行划分，使得它们的长度恰好依次为 3, 1, 2, 4
     * 可以发现，每个区间的[左边界]是在它之前出现的所有元素的和加上 1，[右边界]是到它为止的所有元素的和。因此，如果我们用 pre[i] 表示数组 w 的前缀和：
     *
     * pre[i]= ∑w[k] (0 <= k <= i)
     *
     * 那么第 i 个区间的左边界就是 pre[i]−w[i]+1，右边界就是 pre[i]。
     *
     * 当划分完成后，假设我们随机到了整数 x，我们希望找到满足：
     *
     * pre[i]−w[i]+1 ≤ x ≤ pre[i]
     *
     * 的 i 并将其作为答案返回。
     * 由于 pre[i] 是单调递增的，因此我们可以使用二分查找在 O(logn) 的时间内快速找到 i，即找出[最小]的满足 x≤pre[i] 的下标 i。(左边界二分搜索)
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/random-pick-with-weight/solution/an-quan-zhong-sui-ji-xuan-ze-by-leetcode-h13t/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class Solution {

        // 前缀和
        private int[] presum;
        // 元素个数
        private int n;
        private Random random;

        public Solution(int[] w) {
            // 初始化
            n = w.length;
            this.random = new Random();
            presum = new int[n];
            // 得到前缀和数组
            presum[0] = w[0];
            for (int i = 1; i < n; i++) {
                presum[i] = presum[i - 1] + w[i];
            }
        }

        public int pickIndex() {
            // 生成一个随机整数，范围 [1, n]
            int x = random.nextInt(presum[n - 1]) + 1;
            // 左边界二分查找 满足 x ≤ pre[i] 的最小的 i
            return binarySearchLeft(x);
        }

        /**
         * 左边界二分查找
         * @param target
         * @return
         */
        private int binarySearchLeft(int target) {
            int l = 0, r = n;
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (presum[mid] < target) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }
            return l;
        }
    }
}
