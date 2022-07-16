package com.daily;

import java.util.PriorityQueue;

/**
 * @author wangwei
 * @date 2022/5/18 9:27
 * @description: _668_KthSmallestNumberInMultiplicationTable
 *
 * 668. 乘法表中第k小的数
 * 几乎每一个人都用 乘法表。但是你能在乘法表中快速找到第k小的数字吗？
 *
 * 给定高度m 、宽度n 的一张 m * n的乘法表，以及正整数k，你需要返回表中第k 小的数字。
 *
 * 例 1：
 *
 * 输入: m = 3, n = 3, k = 5
 * 输出: 3
 * 解释:
 * 乘法表:
 * 1	2	3
 * 2	4	6
 * 3	6	9
 *
 * 第5小的数字是 3 (1, 2, 2, 3, 3).
 * 例 2：
 *
 * 输入: m = 2, n = 3, k = 6
 * 输出: 6
 * 解释:
 * 乘法表:
 * 1	2	3
 * 2	4	6
 *
 * 第6小的数字是 6 (1, 2, 2, 3, 4, 6).
 * 注意：
 *
 * m 和 n 的范围在 [1, 30000] 之间。
 * k 的范围在 [1, m * n] 之间。
 */
public class _668_KthSmallestNumberInMultiplicationTable {


    /**
     * 方法一：遍历乘法表每个数字，维护 一个大小为 k 的大顶堆，
     * 每个数字都加入堆，若堆中元素数目大于k，则去除堆顶元素。
     * 这样，最后剩下的k个，就是原乘法表中最小的k个，而堆顶是其中最大的那个，也就是题目说的第k小
     *
     * 超出内存限制 ， 通过 59 / 70
     * m = 9895, n = 28405,k = 100787757
     * @param m
     * @param n
     * @param k
     * @return
     */
    public int findKthNumber(int m, int n, int k) {
        // 大顶堆
        PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> b - a);
        // 遍历乘法表全部元素
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                // 入堆
                queue.offer(i * j);
                // 维护大小为k
                if (queue.size() > k) {
                    queue.poll();
                }
            }
        }
        // 最后的堆顶是最小的k个数字中最大的那个，也就是题目说的第k小
        return queue.peek();
    }

    /**
     * 方法一：二分查找
     * 题目本质上是问：表中哪个数字x满足 小于等于x 的数字 有 k 个
     *
     * x的取值返回是 1 -- m*n(右下角)
     *
     * 由于 m 和 n 很大，直接求出所有数字然后找到第 k 小会超出时间限制。
     *
     * 因此可以 在这个范围内 使用 二分搜索 + 统计 来完成目标x的查找
     *
     * 求第几小等价于求有多少数字不超过 x：
     * 对于乘法表的第 i 行，其最大的元素为 i * n, 因此，
     *      当 i * n <= x 时，这一行及以上行元素均 <= x，而这些行也恰好有 i * n 个数字
     *          我们直接找到最大的那个行号（? * n <= x）,那么有 int count = x / n * n;
     *      当 i * n > x 时，也就是前面那些行之下的部分(i = ?+1, m)，因为第i行每个元素是 i * 1, i * 2, i * 3,....
     *          那么当前行有几个数字小于等于x呢？直接 x / i
     *          所以对于其余行，小于等于x的个数有 count += sum(x / i, i取值[?+1, m])
     * 【注意】
     * 我们二分搜索的数字范围是  1 -- m*n，但是枚举到的数字 可能并不在乘法表里面存在，
     * 比如 比如 m = 3, n = 3 时，乘法表为：
     *          1 2 3
     *          2 4 6
     *          3 6 9
     * 但二分枚举会涉及数字 5、7、8 都不在乘法表中，假设查找 k = 8 的值，会发现 x = 6, 7, 8 时，count = k = 8 都成立，
     * 【所以】当 mid = (l+r)/2, count[mid] == k 时，不能直接返回mid，因此继续缩小r，类似于 寻找左边界的二分搜索
     * 最后直接返回left
     *
     * 那么，既然枚举到的数字不一定存在表中，如何保证left一定存在于表中呢？？？？
     * 【解释一】表中一种m*n个数字，k只要<=m*n，表中一定存在第k个数字满足前面数字都比它小，
     *         其次二分返回的的是第一个满足左边数的数量大于等于 k 的数，也就是678都满足时，返回的一定是6，必然是存在的。
     *         不会出现只有78没有6的情况，表中第一行或者第一列就是1234567...n，你有7了，说明n一定大于等于7，那第一行一定有6
     *         也就是说，当多个数字满足k要求时，至少有一个数字是在表里的，并且二分返回的就是这个
     *         换句话说，二分过程中的 mid 不一定存在，但是最终二分的答案肯定是存在的。
     * 【解释二】除非给的k值不在1到m*n内，否则一定会找到。在每轮循环中，mid = （left + right）//2 ，
     *         mid只要大于等于k，说明当前mid值不论存不存在乘法表中，那答案一定在left到mid之间，否则一定在mid + 1到right之间，
     *         最后循环会停到left = right，这时说明答案在left到right之间且区间是1，而我们前面知道给第k个数一定能在m*n的矩阵内找到，
     *         由此得到left就是答案
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/kth-smallest-number-in-multiplication-table/solution/cheng-fa-biao-zhong-di-kxiao-de-shu-by-l-521a/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param m
     * @param n
     * @param k
     * @return
     */
    public int findKthNumber2(int m, int n, int k) {
        // 二分搜索
        // 表中最小数字是1，最大数字是 m*n，表中一共m*n个数字，第i行最大的数字是i*n
        int l = 1, h = m * n + 1;
        while (l < h) {
            int mid = l + (h - l) / 2;
            // 统计表中 <= mid 的数字个数
            // mid/n 这一行最大的数字是 mid/n（行号） * n = mid 满足 <= mid，并且这一行及之前行总共 mid/n（行号） * n = 个元素
            // 整数除法，这里 count 不一定 = mid
            int count = mid / n * n;
            // 那么从 这一行往下的行呢，每一行有几个数字 <= mid 呢？
            // 因为第i行每个元素是 i * 1, i * 2, i * 3,....那么当前行有几个数字小于等于x呢？直接 x / i
            for (int i = mid / n + 1; i <= m; ++i) {
                count += mid / i;
            }
            // 二分
            if (count < k) {
                l = mid + 1;
            } else {
                // 当count=k时不能直接返回mid，因为此时的mid可能表中不存在，需要去寻找满足count=k的第一个数
                h = mid;
            }
        }
        // 退出时left=right，因为表中一共m*n个数字，第k个数字肯定存在，因此此时的left一定存在于表中
        return -1;
    }

    public static void main(String[] args) {
        _668_KthSmallestNumberInMultiplicationTable obj = new _668_KthSmallestNumberInMultiplicationTable();
        obj.findKthNumber(3, 3, 5);
    }
}
