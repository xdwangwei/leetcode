package com.daily;

/**
 * @author wangwei
 * @date 2023/1/24 22:10
 * @description: _1828_QueriesOnNumberOfPointsInsideACircle
 *
 * 1828. 统计一个圆中点的数目
 * 给你一个数组 points ，其中 points[i] = [xi, yi] ，表示第 i 个点在二维平面上的坐标。多个点可能会有 相同 的坐标。
 *
 * 同时给你一个数组 queries ，其中 queries[j] = [xj, yj, rj] ，表示一个圆心在 (xj, yj) 且半径为 rj 的圆。
 *
 * 对于每一个查询 queries[j] ，计算在第 j 个圆 内 点的数目。如果一个点在圆的 边界上 ，我们同样认为它在圆 内 。
 *
 * 请你返回一个数组 answer ，其中 answer[j]是第 j 个查询的答案。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：points = [[1,3],[3,3],[5,3],[2,2]], queries = [[2,3,1],[4,3,1],[1,1,2]]
 * 输出：[3,2,2]
 * 解释：所有的点和圆如上图所示。
 * queries[0] 是绿色的圆，queries[1] 是红色的圆，queries[2] 是蓝色的圆。
 * 示例 2：
 *
 *
 * 输入：points = [[1,1],[2,2],[3,3],[4,4],[5,5]], queries = [[1,2,2],[2,2,2],[4,3,2],[4,3,3]]
 * 输出：[2,3,2,4]
 * 解释：所有的点和圆如上图所示。
 * queries[0] 是绿色的圆，queries[1] 是红色的圆，queries[2] 是蓝色的圆，queries[3] 是紫色的圆。
 *
 *
 * 提示：
 *
 * 1 <= points.length <= 500
 * points[i].length == 2
 * 0 <= xi, yi <= 500
 * 1 <= queries.length <= 500
 * queries[j].length == 3
 * 0 <= xj, yj <= 500
 * 1 <= rj <= 500
 * 所有的坐标都是整数。
 * 通过次数13,162提交次数15,161
 */
public class _1828_QueriesOnNumberOfPointsInsideACircle {

    /**
     * 方法一：枚举每个点是否在每个圆中
     * 思路与算法
     *
     * 我们可以使用二重循环，对于每一个查询，枚举所有的点，依次判断它们是否在查询的圆中即可。
     *
     * 如果查询圆的圆心为 (qx, qy)，半径为 qr，枚举的点坐标为 (cx, cy)，
     * 那么点在圆中（包括在圆上的情况）当且仅当点到圆心的距离小于等于半径。
     * 我们可以用以下方法进行判断：
     *
     * (cx-qx)^2 + (cy-qy)^2 <= cr^2
     *
     * 注意这里两侧的距离都进行了平方操作，这样可以避免引入浮点数，产生不必要的误差。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/queries-on-number-of-points-inside-a-circle/solution/tong-ji-yi-ge-yuan-zhong-dian-de-shu-mu-jnylm/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param points
     * @param queries
     * @return
     */
    public int[] countPoints(int[][] points, int[][] queries) {
        int m = points.length, n = queries.length;
        int[] ans = new int[n];
        // 遍历待查询圆圈
        for (int i = 0; i < n; ++i) {
            // 圆心，半径
            int cx = queries[i][0], cy = queries[i][1], cr = queries[i][2];
            // 枚举所有点
            for (int j = 0; j < m; ++j) {
                // 坐标
                int px = points[j][0], py = points[j][1];
                // 计算到圆心的距离是否小于半径，两边通过平方比较，避免开更号引入浮点误差
                if ((cx - px) * (cx - px) + (cy - py) * (cy - py) <= cr * cr) {
                    // 当前圆内点数量增加
                    ++ans[i];
                }
            }
        }
        // 返回
        return ans;
    }
}