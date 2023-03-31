package com.daily;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author wangwei
 * @date 2023/3/30 21:08
 * @description: _1637_WidestVerticalAreaBetweenTwoPointsContainingNoPoints
 *
 * 给你n个二维平面上的点 points ，其中points[i] = [xi, yi]，请你返回两点之间内部不包含任何点的最宽垂直区域 的宽度。
 *
 * 垂直区域 的定义是固定宽度，而 y 轴上无限延伸的一块区域（也就是高度为无穷大）。 最宽垂直区域 为宽度最大的一个垂直区域。
 *
 * 请注意，垂直区域边上的点不在区域内。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：points = [[8,7],[9,9],[7,4],[9,7]]
 * 输出：1
 * 解释：红色区域和蓝色区域都是最优区域。
 * 示例 2：
 *
 * 输入：points = [[3,1],[9,0],[1,0],[1,4],[5,3],[8,8]]
 * 输出：3
 *
 *
 * 提示：
 *
 * n == points.length
 * 2 <= n <= 105
 * points[i].length == 2
 * 0 <= xi, yi<= 109
 * 通过次数8,688提交次数10,717
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/widest-vertical-area-between-two-points-containing-no-points
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _1637_WidestVerticalAreaBetweenTwoPointsContainingNoPoints {

    /**
     * 方法一：排序
     *
     * 思路
     *
     * 两点之间内部不包含任何点的最宽垂直面积的宽度，即所有点投影到横轴上后，求相邻的两个点的最大距离。
     *
     * 可以先将输入的坐标按照横坐标排序，然后依次求出所有相邻点的横坐标距离，返回最大值。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/widest-vertical-area-between-two-points-containing-no-points/solution/liang-dian-zhi-jian-bu-bao-han-ren-he-di-ol9u/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param points
     * @return
     */
    public int maxWidthOfVerticalArea(int[][] points) {
        // 纵轴无限长，不用考虑，将输入的坐标按照横坐标排序
        Arrays.sort(points, Comparator.comparingInt(a -> a[0]));
        int ans = 0;
        // 求出所有相邻点的横坐标距离，返回最大值。
        for (int i = 0; i < points.length - 1; ++i) {
            ans = Math.max(ans, points[i + 1][0] - points[i][0]);
        }
        return ans;
    }

    /**
     * 方法二：桶排序（线性复杂度）
     *
     * 方法一中排序的时间复杂度为 O(n×logn)，其实我们可以利用桶排序的思想，将时间复杂度降低到 O(n)。
     *
     * 我们将数组 points 的横坐标放入数组 nums 中。
     *
     * 假设数组 nums 有 n 个元素，所有元素从小到大依次是 nums[0],...nums[n-1] ，相邻元素间距最大是 maxGap。
     *
     * 考虑数组中的最大元素和最小元素之差：
     *
     * nums[n-1] - nums[0] = sum(nums[i] - nums[i-1], 1<=i<=n-1)
     *                     <= maxGap * (n-1)
     *
     * 因此
     *          maxGap >= (nums[n-1] - nums[0]) / (n-1)，即 最大相邻元素间距至少为 (max - min) / (n-1)
     *
     * 注意：我们并不需要对 nums 实际排序，只需要一次遍历找到 最大值 max 和 最小值 min，代替上式中的 nums[n-1] 和 nums[0] 就行
     *
     * 接下来，可以利用桶排序的思想，设定桶的大小（即每个桶最多包含的不同元素个数）为 (max - min) / (n-1)，
     *
     * 将元素按照元素值均匀分布到各个桶内，则同一个桶内的任意两个元素之差小于 maxGap，差为 maxGap 的两个元素一定在两个不同的桶内。
     *
     * 对于每个桶，维护桶内的最小值和最大值，初始时每个桶内的最小值和最大值分别是正无穷和负无穷，表示桶内没有元素。
     *
     * 遍历数组 nums 中的所有元素。
     * 对于每个元素，根据该元素与最小元素之差以及桶的大小计算该元素应该分到的桶的编号，可以确保编号小的桶内的元素都小于编号大的桶内的元素，
     * 使用元素值更新元素所在的桶内的最小值和最大值。
     *
     * 遍历数组结束之后，每个非空的桶内的最小值和最大值都可以确定。
     * 按照桶的编号从小到大的顺序依次遍历每个非空桶，当前的桶的最小值和上一个非空的桶的最大值是排序后的【相邻】元素，
     * （因为要求的是相邻元素间的差值的最大值，所以 当前桶的最大值 - 前一个桶的最小值，没有参考意义，因为 二者并不相邻）
     * 计算两个相邻元素之差，并更新最大间距。
     *
     * 遍历桶结束之后即可得到最大间距。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/widest-vertical-area-between-two-points-containing-no-points/solution/python3javacgo-yi-ti-shuang-jie-pai-xu-t-pc0a/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param points
     * @return
     */
    public int maxWidthOfVerticalArea2(int[][] points) {
        int n = points.length;
        // 设置一个 inf
        int inf = 1 << 30;
        // 将数组 points 的横坐标放入数组 nums 中。省略这一步，直接用 points
        // 得到 nums 中的最小值和最大值
        int min = inf, max = -inf;
        for (int[] point : points) {
            min = Math.min(min, point[0]);
            max = Math.max(max, point[0]);
        }
        // 计算 极差
        int diff = max - min;
        // 计算 maxGap，至少为 1
        int bucketSize = Math.max(1, diff / (n - 1));
        // 根据 maxGap 计算桶的数量，考虑到 整除关系，这里最后 + 1，当满足整除时，即便多个一个桶，也不影响最终答案，因为这个桶内不会有元素
        int bucketCount = diff / bucketSize + 1;
        // 创建桶，每个桶内维护最小值、最大值
        int[][] buckets = new int[bucketCount][2];
        // 初始化每个桶的最小值、最大值
        for (int i = 0; i < bucketCount; ++i) {
            buckets[i] = new int[]{inf, -inf};
        }
        // 遍历nums元素
        for (int[] point : points) {
            int x = point[0];
            // 计算所属桶编号
            int idx = (x - min) / bucketSize;
            // 更新桶内最小、最大值
            buckets[idx][0] = Math.min(buckets[idx][0], x);
            buckets[idx][1] = Math.max(buckets[idx][1], x);
        }
        // 计算答案
        int ans = 0;
        // 当前的桶的最小值和上一个非空的桶的最大值是排序后的【相邻】元素，用于 更新 ans
        // 初始时，前面没有桶，prev为前一个桶的最大值，初始化为 inf
        int prev = inf;
        // 遍历桶
        for (int[] bucket : buckets) {
            // 跳过空桶，只要桶内有元素，则 最小值 一定 小于等于 最大值
            if (bucket[0] > bucket[1]) {
                continue;
            }
            // 当前的桶的最小值和上一个非空的桶的最大值 的差值 更新 答案
            ans = Math.max(ans, bucket[0] - prev);
            // 更新 prev 为当前桶的最大值
            prev = bucket[1];
        }
        // 返回
        return ans;
    }
}
