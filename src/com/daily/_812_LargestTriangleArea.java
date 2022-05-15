package com.daily;

/**
 * @author wangwei
 * @date 2022/5/15 15:24
 * @description: _812_LargestTriangleArea
 *
 * 812. 最大三角形面积
 * 给定包含多个点的集合，从其中取三个点组成三角形，返回能组成的最大三角形的面积。
 *
 * 示例:
 * 输入: points = [[0,0],[0,1],[1,0],[0,2],[2,0]]
 * 输出: 2
 * 解释:
 * 这五个点如下图所示。组成的橙色三角形是最大的，面积为2。
 *
 *
 * 注意:
 *
 * 3 <= points.length <= 50.
 * 不存在重复的点。
 *  -50 <= points[i][j] <= 50.
 * 结果误差值在 10^-6 以内都认为是正确答案
 */
public class _812_LargestTriangleArea {

    /**
     * 方法一：枚举
     * 思路与算法
     *
     * 关于求解三角形面积的公式可以参考百度百科「三角形面积公式」https://leetcode.cn/link/?target=https%3A%2F%2Fbaike.baidu.com%2Fitem%2F%E4%B8%89%E8%A7%92%E5%BD%A2%E9%9D%A2%E7%A7%AF%E5%85%AC%E5%BC%8F。
     *
     * 我们可以枚举所有的三角形，然后计算三角形的面积并找出最大的三角形面积。
     * 设三角形三个顶点的坐标为 (x1, y1)、(x2, y2) 和 (x3, y3)，则三角形面积 S 可以用行列式的绝对值表示：
     *
     * S = \frac{1}{2} \left| \begin{vmatrix} x_1 & y_1 & 1 \\ x_2 & y_2 & 1 \\ x_3 & y_3 &1 \end{vmatrix} \right| = \frac{1}{2} \lvert x_1 y_2 + x_2 y_3 + x_3 y_1 - x_1 y_3 - x_2 y_1 - x_3 y_2 \rvert
     *
     *              || x1 y1 1 ||
     *  s  =  1/2   || x2 y2 1 || = 1/2 * abs(x1y2 + x2y3 + x3y1 - x1y3 - x2y1 - x3y2)
     *              || x3 y3 1 ||
     * 代码
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/largest-triangle-area/solution/zui-da-san-jiao-xing-mian-ji-by-leetcode-yefh/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param points
     * @return
     */
    public double largestTriangleArea(int[][] points) {
        int n  = points.length;
        double res = 0;
        // 枚举三个不同顶点
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                for (int k = j + 1; k < n; ++k) {
                    res = Math.max(res, getArea(points[i][0], points[i][1], points[j][0], points[j][1], points[k][0], points[k][1]));
                }
            }
        }
        return res;
    }

    /**
     * 返回根据行列式规则计算的三角形面积
     *
     * https://baike.baidu.com/item/%E4%B8%89%E8%A7%92%E5%BD%A2%E9%9D%A2%E7%A7%AF%E5%85%AC%E5%BC%8F#reference-[1]-1207774-wrap
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @return
     */
    private double getArea(int x1, int y1, int x2, int y2, int x3, int y3) {
        return 0.5 * Math.abs(x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2);
    }
}
