package com.array;

import java.util.HashSet;

/**
 * @author wangwei
 * 2022/3/25 14:25
 *
 * 给你一个数组 rectangles ，其中 rectangles[i] = [xi, yi, ai, bi] 表示一个坐标轴平行的矩形。这个矩形的左下顶点是 (xi, yi) ，右上顶点是 (ai, bi) 。
 *
 * 如果所有矩形一起精确覆盖了某个矩形区域，则返回 true ；否则，返回 false 。
 *
 *  
 * 示例 1：
 *
 *
 * 输入：rectangles = [[1,1,3,3],[3,1,4,2],[3,2,4,4],[1,3,2,4],[2,3,3,4]]
 * 输出：true
 * 解释：5 个矩形一起可以精确地覆盖一个矩形区域。
 * 示例 2：
 *
 *
 * 输入：rectangles = [[1,1,2,3],[1,3,2,4],[3,1,4,2],[3,2,4,4]]
 * 输出：false
 * 解释：两个矩形之间有间隔，无法覆盖成一个矩形。
 * 示例 3：
 *
 *
 * 输入：rectangles = [[1,1,3,3],[3,1,4,2],[1,3,2,4],[2,2,4,4]]
 * 输出：false
 * 解释：因为中间有相交区域，虽然形成了矩形，但不是精确覆盖。
 *
 *
 * 提示：
 *
 * 1 <= rectangles.length <= 2 * 104
 * rectangles[i].length == 4
 * -105 <= xi, yi, ai, bi <= 105
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/perfect-rectangle
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _391_PerfectRectangle {

    /**
     * 面积角度 + 顶点维度
     *
     * 假设这些小矩形最终形成了一个「完美矩形」，这个完美矩形的左下角顶点坐标 (X1, Y1) 和右上角顶点的坐标 (X2, Y2)？
     * 左下角顶点 (X1, Y1) 就是 rectangles 中所有小矩形中最靠左下角的那个小矩形的左下角顶点；
     * 右上角顶点 (X2, Y2) 就是所有小矩形中最靠右上角的那个小矩形的右上角顶点。
     *
     * 计算出的 X1,Y1,X2,Y2 坐标是完美矩形的「理论坐标」，
     * 如果所有小矩形的面积之和不等于这个完美矩形的理论面积，那么说明最终形成的图形肯定存在空缺或者重叠，肯定不是完美矩形。
     *
     * 那如果面积相等，能说明这些小矩形组成了完美矩形吗？？？这并不一定！！！
     * 比如 你把一个 3x3 的完美矩形(以9个格子来看待) 把中心挖了，那剩下部分是8个格子，分成几个小矩形没难度，至于挖出的那个格子，让他重叠到某个小矩形的一个格子上
     * 那么 这些矩形 + 小格子 的面积 和 原来的完美矩形相等，但是 他们组成的并不是一个完美矩形，因为有一部分重叠。
     *
     * 也就是说
     * 小矩形面积和 ！= 理论完美面积和 ，那 一定不是 完美矩形
     * 反之并不成立，因为可能会存在重叠或者空缺造成面积相等，形状却不完美
     *
     * 那如何弥补，如果是完美矩形，那么它一定只有四个顶点，否则 组成的新图形的顶点 一定多于 4 个。
     * 并且这四个点一定是我们推出的理论完美矩形的四个点 左下角顶点 (X1, Y1) 右上角顶点 (X2, Y2)
     *
     * 那么，如果顶点一致，是否不用判断面积？？
     * 不可以！还是上面的覆盖情况，组成了完美矩形，但是中间重叠了一个块，那么就不算完美拼接(题目示例3)
     *
     * 所以必须面积和顶点一起判断
     *
     * 那如何得到这些矩形拼接后会得到哪些点呢？ 两个矩形拼接时，一个或多个角消失，那么这个角的顶点就是这两个矩阵共有的。
     * 所以，在遍历所有小矩阵的时候，保存它每个顶点，如果这个点已经存在，那么拼接时这个角会消失，也就是说最后剩余的点中不应该有它，所以应该移除。
     * @param rectangles
     * @return
     */
    public boolean isRectangleCover(int[][] rectangles) {
        // 题目取值范围 -10^5 --- 10^5, 为避免 选用 Integer.MAX_VALUE 和 Integer.MIN_VALUE 造成溢出，
        int MAX = 1000000, MIN = -1000000;
        // 初始化完美举行的左下角和右上角左边，注意不要搞反了 MAX 和 MIN
        int minX = MAX, minY = MAX;
        int maxX = MIN, maxY = MIN;
        // 保存所有小矩形的面积之和
        int areaSum = 0;
        // 保存拼接后的图形所有的顶点坐标
        HashSet<String> pointsSet = new HashSet<>();
        for (int[] rec: rectangles) {
            // 每个举行的左下角坐标
            int x1 = rec[0], y1 = rec[1];
            // 右上角坐标
            int x2 = rec[2], y2 = rec[3];
            // 更新得到完美矩阵的左下角和右上角坐标
            minX = Math.min(minX, x1);
            minY = Math.min(minY, y1);
            maxX = Math.max(maxX, x2);
            maxY = Math.max(maxY, y2);
            // 当前小矩形的面积，累加
            areaSum += (y2 - y1) * (x2 - x1);
            // 更新最终图形顶点
            // 得到当前小矩形的四个顶点
            for (String point : getPoint(x1, y1, x2, y2)) {
                // 如果这个顶点已出现，那么两顶点相挨，这个点应该消失
                if (pointsSet.contains(point)) {
                    pointsSet.remove(point);
                } else {
                    pointsSet.add(point);
                }
            }
        }
        // 完美矩形的面积
        int fullArea = (maxY - minY) * (maxX - minX);
        // 面积不匹配
        if (areaSum != fullArea) {
            return false;
        }
        // 面积匹配，判断顶点个数
        // 顶点个数 ！= 4
        if (pointsSet.size() != 4) {
            return false;
        }
        // 顶点个数 =4， 还必须保证这个四个顶点，就是 我们得到的理论上的完美矩形的四个顶点
        for (String point : getPoint(minX, minY, maxX, maxY)) {
            // 判断完美矩形的四个顶点是否 出现在 拼接后的所有顶点集合中
            if (!pointsSet.contains(point)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 由左下角和右上角坐标得到四个点的坐标
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private String[] getPoint(int x1, int y1, int x2, int y2) {
        String[] points = new String[4];
        points[0] = x1 + "," + y1;
        points[1] = x1 + "," + y2;
        points[2] = x2 + "," + y1;
        points[3] = x2 + "," + y2;
        return points;
    }
}
