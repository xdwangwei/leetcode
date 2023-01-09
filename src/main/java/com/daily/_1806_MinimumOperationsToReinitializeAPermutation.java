package com.daily;

/**
 * @author wangwei
 * @date 2023/1/9 10:31
 * @description: _1806_MinimumOperationsToReinitializeAPermutation
 *
 * 1806. 还原排列的最少操作步数
 * 给你一个偶数 n ，已知存在一个长度为 n 的排列 perm ，其中 perm[i] == i（下标 从 0 开始 计数）。
 *
 * 一步操作中，你将创建一个新数组 arr ，对于每个 i ：
 *
 * 如果 i % 2 == 0 ，那么 arr[i] = perm[i / 2]
 * 如果 i % 2 == 1 ，那么 arr[i] = perm[n / 2 + (i - 1) / 2]
 * 然后将 arr 赋给 perm 。
 *
 * 要想使 perm 回到排列初始值，至少需要执行多少步操作？返回最小的 非零 操作步数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 2
 * 输出：1
 * 解释：最初，perm = [0,1]
 * 第 1 步操作后，perm = [0,1]
 * 所以，仅需执行 1 步操作
 * 示例 2：
 *
 * 输入：n = 4
 * 输出：2
 * 解释：最初，perm = [0,1,2,3]
 * 第 1 步操作后，perm = [0,2,1,3]
 * 第 2 步操作后，perm = [0,1,2,3]
 * 所以，仅需执行 2 步操作
 * 示例 3：
 *
 * 输入：n = 6
 * 输出：4
 *
 *
 * 提示：
 *
 * 2 <= n <= 1000
 * n 是一个偶数
 */
public class _1806_MinimumOperationsToReinitializeAPermutation {

    /**
     * 模拟 + 数学
     * 根据题目：perm长度n为偶数，perm[i]=i
     * 每一次变换，实际上是将perm前n/2个元素，顺序放置到arr的偶数位置上；将perm后n/2个元素，顺序放置到arr的奇数位置上；
     * 按照此整体变换规律，当某一个数字返回原位置之后，也就代表着整个数组返回排列了，
     * 因此只需要对非头尾的任意一个值进行位置变换模拟，统计它回到原位的最少变换次数即可。
     * （头元素下标为0，始终变换到0位置；尾元素下标为n-1，始终变换到最后一个位置；所以取它两没有意义）
     * @param n
     * @return
     */
    public int reinitializePermutation(int n) {
        // 对1位置进行模拟
        int i = 1, step = 0;
        while (true) {
            // 按照规则，如果 i % 2 == 0 ，那么 arr[i] 来自于 perm[i / 2] 位置
            //         如果 i % 2 == 1 ，那么 arr[i] 来自于 perm[n / 2 + (i - 1) / 2] 位置
            if ((i & 1) != 0) {
                i = (n + i - 1) / 2;
            } else {
                i = i / 2;
            }
            // 操作次数增加
            step++;
            // 多次操作后，i又回到1，结束
            if (i == 1) {
                break;
            }
        }
        // 返回
        return step;
    }

}
