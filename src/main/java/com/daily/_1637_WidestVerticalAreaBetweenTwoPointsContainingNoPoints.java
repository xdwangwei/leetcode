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
}
