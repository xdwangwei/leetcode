package com.daily;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/5/23 21:04
 * @description: _1090_LargestValuesFromLabels
 *
 * 1090. 受标签影响的最大值
 * 我们有一个 n 项的集合。给出两个整数数组 values 和 labels ，第 i 个元素的值和标签分别是 values[i] 和 labels[i]。还会给出两个整数 numWanted 和 useLimit 。
 *
 * 从 n 个元素中选择一个子集 s :
 *
 * 子集 s 的大小 小于或等于 numWanted 。
 * s 中 最多 有相同标签的 useLimit 项。
 * 一个子集的 分数 是该子集的值之和。
 *
 * 返回子集 s 的最大 分数 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：values = [5,4,3,2,1], labels = [1,1,2,2,3], numWanted = 3, useLimit = 1
 * 输出：9
 * 解释：选出的子集是第一项，第三项和第五项。
 * 示例 2：
 *
 * 输入：values = [5,4,3,2,1], labels = [1,3,3,3,2], numWanted = 3, useLimit = 2
 * 输出：12
 * 解释：选出的子集是第一项，第二项和第三项。
 * 示例 3：
 *
 * 输入：values = [9,8,8,7,6], labels = [0,0,0,1,1], numWanted = 3, useLimit = 1
 * 输出：16
 * 解释：选出的子集是第一项和第四项。
 *
 *
 * 提示：
 *
 * n == values.length == labels.length
 * 1 <= n <= 2 * 104
 * 0 <= values[i], labels[i] <= 2 * 104
 * 1 <= numWanted, useLimit <= n
 * 通过次数18,078提交次数27,298
 */
public class _1090_LargestValuesFromLabels {

    /**
     * 方法一：贪心 + 排序 + 哈希表
     *
     * 根据题目描述，
     * 我们需要从 n 个元素的集合中选出一个子集，
     * 子集元素个数不超过 numWanted，且子集中最多有相同标签的 useLimit 项，使得子集的值之和最大。
     *
     * 因此，我们应该[贪心]选择集合中值较大的元素，同时记录每个标签出现的次数，
     * 当某个标签出现的次数达到 useLimit 时，我们就不能再选择该标签对应的元素了。
     *
     * 具体地，
     *
     * 我们先将集合中的元素按照值从大到小进行排序，然后从前往后遍历排序后的元素。
     * 在遍历的过程中，我们使用一个哈希表 cnt 记录已经使用的元素中，每个标签出现的次数，
     * 如果某个标签出现的次数达到了 useLimit，那么我们就跳过当前元素，
     * 否则我们就将该元素的值加到最终的答案中，并将该标签出现的次数加 1。
     *
     * 同时，我们用一个变量 num 记录当前子集中的元素个数，当 num 达到 numWanted 时，我们就可以结束遍历了。
     *
     * 遍历结束后，我们就得到了最大的分数。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/largest-values-from-labels/solution/python3javacgotypescript-yi-ti-yi-jie-ta-evaq/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int largestValsFromLabels(int[] values, int[] labels, int numWanted, int useLimit) {
        int n = values.length;
        int[][] pairs = new int[n][2];
        // 将集合中的元素按照值从大到小进行排序，
        for (int i = 0; i < n; ++i) {
            pairs[i] = new int[]{values[i], labels[i]};
        }
        Arrays.sort(pairs, (a, b) -> b[0] - a[0]);
        // 哈希表 cnt 记录已经使用的元素中，每个标签出现的次数，
        Map<Integer, Integer> cnt = new HashMap<>();
        // ans 记录子集元素和，num记录子集元素个数
        int ans = 0, num = 0;
        // 然后从前往后遍历排序后的元素。num 不能超过 numWanted
        for (int i = 0; i < n && num < numWanted; ++i) {
            // 当前元素值和标签
            int v = pairs[i][0], l = pairs[i][1];
            // 这个标签个数没有到限制
            if (cnt.getOrDefault(l, 0) < useLimit) {
                // 加入这个元素
                ans += v;
                // 子集元素个数+1
                num += 1;
                // 此标签使用次数+1
                cnt.merge(l, 1, Integer::sum);
            }
        }
        // 返回 子集元素和
        return ans;
    }
}
