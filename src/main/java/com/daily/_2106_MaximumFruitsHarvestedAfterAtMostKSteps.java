package com.daily;

/**
 * @author wangwei
 * @date 2023/5/3 16:22
 * @description: _2106_Maximum
 *
 * 2106. 摘水果
 * 在一个无限的 x 坐标轴上，有许多水果分布在其中某些位置。给你一个二维整数数组 fruits ，其中 fruits[i] = [positioni, amounti] 表示共有 amounti 个水果放置在 positioni 上。fruits 已经按 positioni 升序排列 ，每个 positioni 互不相同 。
 *
 * 另给你两个整数 startPos 和 k 。最初，你位于 startPos 。从任何位置，你可以选择 向左或者向右 走。在 x 轴上每移动 一个单位 ，就记作 一步 。你总共可以走 最多 k 步。你每达到一个位置，都会摘掉全部的水果，水果也将从该位置消失（不会再生）。
 *
 * 返回你可以摘到水果的 最大总数 。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：fruits = [[2,8],[6,3],[8,6]], startPos = 5, k = 4
 * 输出：9
 * 解释：
 * 最佳路线为：
 * - 向右移动到位置 6 ，摘到 3 个水果
 * - 向右移动到位置 8 ，摘到 6 个水果
 * 移动 3 步，共摘到 3 + 6 = 9 个水果
 * 示例 2：
 *
 *
 * 输入：fruits = [[0,9],[4,1],[5,7],[6,2],[7,4],[10,9]], startPos = 5, k = 4
 * 输出：14
 * 解释：
 * 可以移动最多 k = 4 步，所以无法到达位置 0 和位置 10 。
 * 最佳路线为：
 * - 在初始位置 5 ，摘到 7 个水果
 * - 向左移动到位置 4 ，摘到 1 个水果
 * - 向右移动到位置 6 ，摘到 2 个水果
 * - 向右移动到位置 7 ，摘到 4 个水果
 * 移动 1 + 3 = 4 步，共摘到 7 + 1 + 2 + 4 = 14 个水果
 * 示例 3：
 *
 *
 * 输入：fruits = [[0,3],[6,4],[8,5]], startPos = 3, k = 2
 * 输出：0
 * 解释：
 * 最多可以移动 k = 2 步，无法到达任一有水果的地方
 *
 *
 * 提示：
 *
 * 1 <= fruits.length <= 105
 * fruits[i].length == 2
 * 0 <= startPos, positioni <= 2 * 105
 * 对于任意 i > 0 ，positioni-1 < positioni 均成立（下标从 0 开始计数）
 * 1 <= amounti <= 104
 * 0 <= k <= 2 * 105
 * 通过次数5,128提交次数14,662
 */
public class _2106_MaximumFruitsHarvestedAfterAtMostKSteps {

    /**
     * 方法：双指针 + 滑动窗口
     *
     * 思路
     *
     * 思考移动轨迹
     *
     * 由于只能一步步地走，人移动的范围必然是一段连续的区间。假如最优的区间是 [l r]
     *
     * 假如 startPos 在 l、r 之间，
     *      那么如果反复左右移动，会白白浪费移动次数，
     *      所以最优方案要么先向右到r再向左到l，要么先向左到l再向右到r。
     * 如果 startPos < l，那么就是从 startPos 一直往右走
     * 如果 startPos > r，那么就是从 startPos 一直往左走
     *
     * 实际分析
     *
     * 假设向左走最远可以到达 fruits[left1][0]，因为最多走 k 步，所以 fruits[left1][0] 必然要 >= startPos−k
     * 那么 left1 是最小的满足 fruits[left1][0] ≥ startPos−k 的下标。
     * 由于 fruits 数组 fruits[0] 是坐标轴递增的，所以通过左边界二分搜索快速得到 left1
     * 那么 第一个方案就是 从 startPos 一直向左走到 left1，此时 right = startPos
     *
     * 对于其他方案，对某个left考虑其 right 位置，当 right 增加时，left 不可能更小（步数更多了），有单调性，因此可以用同向双指针（滑动窗口）解决。
     *
     * 如果 [left, right] 作为滑动窗口，
     *      left 肯定是从 left1 位置开始
     *      那么 right 是从 startPos+1 位置开始，然后right一直增加，当然 right 不能超过 n，且 fruits[right][0] 不可能超过 startPos + k
     * 如何判断 left 是否需要增加呢？
     *      移动区间是 [left,right]，startPos 在二者之间
     *      如果先向右再向左，那么移动距离为 (fruits[right][0]−startPos)+(fruits[right][0]−fruits[left][0])
     *      如果先向左再向右，那么移动距离为 (startPos−fruits[left][0])+(fruits[right][0]−fruits[left][0])
     *      如果上面两个式子均大于 k，就说明这个区间不可能存在，也就是fruits[left][0] 太远了，左边走的太远了，需要近一些，此时需要增加 left。
     *
     * 在移动 left 和 right 的同时，维护窗口内的水果数量 s，同时用 s 更新答案的最大值。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/maximum-fruits-harvested-after-at-most-k-steps/solution/hua-dong-chuang-kou-jian-ji-xie-fa-pytho-1c2d/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param fruits
     * @param startPos
     * @param k
     * @return
     */
    public int maxTotalFruits(int[][] fruits, int startPos, int k) {
        int n = fruits.length;
        // 二分搜索得到最左侧可以到达的位置
        int left = leftBoundBS(fruits, startPos - k);
        int right = left, curSum = 0;
        // 计算 [left,startPos] 之间(方案1) 的水果总数吗，注意 此时 fruits[right][0] 不能超过 startPos
        for (; right < n && fruits[right][0] <= startPos; ++right) {
            curSum += fruits[right][1];
        }
        // 初始化 ans 为方案一的结果
        int ans = curSum;
        // 其他方案，[left,right] 滑动窗口，，right 从 startPos + 1 开始，right 不能超过 n，fruits[right][0] 不超过 startPos+k
        for (; right < n && fruits[right][0] <= startPos + k; ++right) {
            // 当前窗口内的水果数量
            curSum += fruits[right][1];
            // 如果这个区间不可能存在，就是 左边走的太远了，需要近一些，增加 left
            // 注意收缩窗口是个while
            while (startPos + fruits[right][0] - 2 * fruits[left][0] > k
                    && 2 * fruits[right][0] - startPos - fruits[left][0] > k) {
                // 增加左边界的时候更新窗口内的水果数量
                curSum -= fruits[left++][1];
            }
            // 区间合理后，更新ans
            ans = Math.max(ans, curSum);
        }
        // 返回
        return ans;
    }

    /**
     * 在有序数组 arr 中寻找 大于等于 target 的最左侧（小）元素 的 索引
     * @param arr
     * @param target
     * @return
     */
    private int leftBoundBS(int[][] arr, int target) {
        int l = 0, r = arr.length;
        while (l < r) {
            int m = l + r >> 1;
            if (arr[m][0] >= target) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        // arr[m]满足条件时会走第一个if，也就是r=m，所以退出时，r是有效值，退出时l=r，所以返回l或r都可
        return r;
    }

    public static void main(String[] args) {
        System.out.println(1 + (4 >> 1));
    }
}
