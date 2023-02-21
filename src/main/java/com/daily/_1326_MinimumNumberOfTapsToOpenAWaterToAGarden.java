package com.daily;

/**
 * @author wangwei
 * @date 2023/2/21 14:14
 * @description: _1326_MinimumNumberOfTapsToOpenAWaterToAGarden
 *
 * 1326. 灌溉花园的最少水龙头数目
 * 在 x 轴上有一个一维的花园。花园长度为 n，从点 0 开始，到点 n 结束。
 *
 * 花园里总共有 n + 1 个水龙头，分别位于 [0, 1, ..., n] 。
 *
 * 给你一个整数 n 和一个长度为 n + 1 的整数数组 ranges ，其中 ranges[i] （下标从 0 开始）表示：如果打开点 i 处的水龙头，可以灌溉的区域为 [i -  ranges[i], i + ranges[i]] 。
 *
 * 请你返回可以灌溉整个花园的 最少水龙头数目 。如果花园始终存在无法灌溉到的地方，请你返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：n = 5, ranges = [3,4,1,1,0,0]
 * 输出：1
 * 解释：
 * 点 0 处的水龙头可以灌溉区间 [-3,3]
 * 点 1 处的水龙头可以灌溉区间 [-3,5]
 * 点 2 处的水龙头可以灌溉区间 [1,3]
 * 点 3 处的水龙头可以灌溉区间 [2,4]
 * 点 4 处的水龙头可以灌溉区间 [4,4]
 * 点 5 处的水龙头可以灌溉区间 [5,5]
 * 只需要打开点 1 处的水龙头即可灌溉整个花园 [0,5] 。
 * 示例 2：
 *
 * 输入：n = 3, ranges = [0,0,0,0]
 * 输出：-1
 * 解释：即使打开所有水龙头，你也无法灌溉整个花园。
 *
 *
 * 提示：
 *
 * 1 <= n <= 104
 * ranges.length == n + 1
 * 0 <= ranges[i] <= 100
 * 通过次数15,755提交次数29,626
 */
public class _1326_MinimumNumberOfTapsToOpenAWaterToAGarden {


    /**
     * 方法一：贪心
     *
     * 我们注意到，对于所有能覆盖某个左端点的水龙头，选择能覆盖最远右端点的那个水龙头是最优的。
     *
     * 因此，我们可以先预处理数组 ranges，对于第 i 个水龙头，它能覆盖的左端点 l=max(0,i−ranges[i])，右端点 r=min(n,i+ranges[i])，
     * 我们算出所有能覆盖左端点 l 的水龙头中，右端点最大的那个位置，记录在数组 rightMost[i] 中。
     *
     * 然后我们定义以下三个变量，其中：
     *
     * 变量 ans 表示最终答案，即最少水龙头数目；
     * 变量 mx 表示当前能覆盖到的最远右端点；（代表的是在pre覆盖范围内的i及其左边位置能够覆盖到的最远右位置）
     * 变量 pre 表示上一个水龙头覆盖的最远右端点。
     *
     * 我们在 [0,...n−1] 的范围内遍历所有位置（只需要判断n-1位置能够覆盖到n位置，因此不需要遍历n位置），
     * 对于当前位置 i， 更新 mx = max(mx, rightMost[i])。
     *
     * 如果 mx ≤ i，说明无法覆盖下一个位置，返回 −1。
     * 如果 pre = i，说明当前位于上一个水龙头覆盖到的最远位置，此时需要再放置一个水龙头，因此我们将 ans 加 1，并且更新 pre=mx。
     * 遍历结束后，返回 ans 即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/minimum-number-of-taps-to-open-to-water-a-garden/solution/python3javacgotypescript-yi-ti-yi-jie-ta-hwfe/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param ranges
     * @return
     */
    public int minTaps(int n, int[] ranges) {
        // 记录每个位置作为左端点能够覆盖到的最远的右位置
        int[] rightMost = new int[n + 1];
        for (int i = 0; i < n + 1; ++i) {
            // 每个点覆盖到的左右边界位置，只考虑 0-n
            int left = Math.max(0, i - ranges[i]);
            int right = Math.min(n, i + ranges[i]);
            // 更新
            rightMost[left] = Math.max(rightMost[left], right);
        }
        // right表示当前能覆盖到的最远右端点；（代表的是在preRight覆盖范围内的i及其左边位置能够覆盖到的最远右位置）
        // preRight表示上一个水龙头覆盖的最远右端点。
        int right = 0, preRight = 0, ans = 0;
        for (int i = 0; i < n; ++i) {
            // 更新当前能覆盖到的最远位置
            right = Math.max(right, rightMost[i]);
            // 无法覆盖到下一位置，结束
            if (right == i) {
                return -1;
            }
            // 当前处于上一个水龙头能覆盖到的最远位置
            if (preRight == i) {
                // 增加水龙头
                ans++;
                // 更新能覆盖到的最远位置
                preRight = right;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        _1326_MinimumNumberOfTapsToOpenAWaterToAGarden obj = new _1326_MinimumNumberOfTapsToOpenAWaterToAGarden();
        // System.out.println(obj.binarySearch(new int[]{1, 2, 3, 4, 5, 6, 7}, 0));
        // System.out.println(obj.binarySearch(new int[]{1, 2, 3, 3, 5, 6, 7}, 8));
        // System.out.println(obj.binarySearch(new int[]{1, 2, 3, 3, 5, 6, 7}, 5));
        // System.out.println(obj.binarySearch(new int[]{1, 2, 3, 3, 5, 6, 7}, 3));
        System.out.println(obj.minTaps(3, new int[]{0, 0, 0, 0}));
    }
}
