package com.daily;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * @author wangwei
 * @date 2023/6/1 20:26
 * @description: _2517_MaximumTastinessOfCandyBasket
 * 2517. 礼盒的最大甜蜜度
 * 给你一个正整数数组 price ，其中 price[i] 表示第 i 类糖果的价格，另给你一个正整数 k 。
 *
 * 商店组合 k 类 不同 糖果打包成礼盒出售。礼盒的 甜蜜度 是礼盒中任意两种糖果 价格 绝对差的最小值。
 *
 * 返回礼盒的 最大 甜蜜度。
 *
 *
 *
 * 示例 1：
 *
 * 输入：price = [13,5,1,8,21,2], k = 3
 * 输出：8
 * 解释：选出价格分别为 [13,5,21] 的三类糖果。
 * 礼盒的甜蜜度为 min(|13 - 5|, |13 - 21|, |5 - 21|) = min(8, 8, 16) = 8 。
 * 可以证明能够取得的最大甜蜜度就是 8 。
 * 示例 2：
 *
 * 输入：price = [1,3,1], k = 2
 * 输出：2
 * 解释：选出价格分别为 [1,3] 的两类糖果。
 * 礼盒的甜蜜度为 min(|1 - 3|) = min(2) = 2 。
 * 可以证明能够取得的最大甜蜜度就是 2 。
 * 示例 3：
 *
 * 输入：price = [7,7,7,7], k = 2
 * 输出：0
 * 解释：从现有的糖果中任选两类糖果，甜蜜度都会是 0 。
 *
 *
 * 提示：
 *
 * 1 <= price.length <= 105
 * 1 <= price[i] <= 109
 * 2 <= k <= price.length
 * 通过次数14,426提交次数19,955
 *
 */
public class _2517_MaximumTastinessOfCandyBasket {

    /**
     * 思路
     *
     * 「任意两种糖果价格绝对差的最小值」等价于「排序后，任意两种相邻糖果价格绝对差的最小值」。
     * 若「任意两种糖果价格绝对差的最小值 为 ans」等价于「排序后，任意两种相邻糖果价格绝对差 都 大于等于 ans」。
     *
     * 如果题目中有「最大化最小值」或者「最小化最大值」，一般都是二分答案，请记住这个套路。
     *
     * 为什么？对于本题来说，甜蜜度越大，能选择的糖果越少，有单调性，所以可以二分。
     *
     * 定义 f(d) 表示甜蜜度至少为 d 时，至多能选多少类糖果。
     *
     * 二分答案 d：
     *
     * 如果 f(d) ≥ k，说明答案（最大的可能的甜蜜度）至少为 d。
     * 如果 f(d)<k，说明答案至多为 d−1。
     * 二分结束后，设答案为 d0，那么 f(d0)≥k 且 f(d0+1)<k。
     *
     *
     * 如何计算 f(d)？对 price 从小到大排序，贪心地计算 f(d)：
     * 从 price[0] 开始选；假设上一个选的数是  pre，那么当 price[i]≥pre+d 时，才可以选 price[i]。
     *
     * 二分下界可以取 1，上界可以取 max(price)−min(price) （当k=2的时候）
     *
     * 请注意，二分的区间的定义是：尚未确定 f(d) 与 k 的大小关系的 d 的值组成的集合（范围）。
     * 在区间左侧外面的d 都是f(d)≥k 的，在区间右侧外面的d 都是f(d)<k 的。
     * 在理解二分时，请牢记区间的定义及其性质。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/maximum-tastiness-of-candy-basket/solution/er-fen-da-an-by-endlesscheng-r418/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int maximumTastiness(int[] price, int k) {
        int n = price.length;
        // 排序
        Arrays.sort(price);
        // 甜蜜度最大值为 price[n - 1] - price[0]（如果k=2，否则随着k增加，甜蜜度只可能更小）
        // 最小值为0
        // 二分搜索，寻找甜蜜度可能的最大值，因此是寻找右边界
        int l = 0, r = price[n - 1] - price[0] + 1;
        while (l < r) {
            // 可能的甜蜜度
            int m = l + r >> 1;
            // 这种甜蜜度限制下，是否存在k类糖果，每种间的绝对差都小于等于 甜蜜度
            int cnt = check(price, m);
            // 当前甜蜜度限制下，最多可以选择的糖果种类数 >= K，那么说明甜蜜度可能可以更高
            if (cnt >= k) {
                l = m + 1;
            } else {
                // 否则，甜蜜度太高了，可选的糖果种类数不能超过k，那么减少甜蜜度，即减小下界
                r = m;
            }
        }
        // 在二分中，对于满足要求的m，会走 l=m+1的逻辑，所以最后l-1是有效答案
        return l - 1;
    }

    /**
     * 在有序数据arr中，最多有几个元素满足 元素间的差的最小值 大于等于 diff
     * 各个元素间的差的最小值小于等于 diff    ==     任意两个元素间的差 >= diff
     * 由于是有序数组，那么从第一个元素开始arr[0]选，下一个可选的元素至少为 arr[0]+diff，以此类推
     * @param arr
     * @param diff
     * @return
     */
    private int check(int[] arr, int diff) {
        // 贪心，从 arr[0] 开始选（从越小的元素开始选，最终可能的序列就越长），初始 cnt = 1
        int cnt = 1, pre = arr[0];
        // 遍历，pre 记录上一个上一个选择的元素
        for (int x : arr) {
            // 必须满足二者之间的差 >= x
            if (x - pre >= x) {
                cnt++;
                // 更新 pre
                pre = x;
            }
        }
        // 返回 cnt
        return cnt;
    }

}
