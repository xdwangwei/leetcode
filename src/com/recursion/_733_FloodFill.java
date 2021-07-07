package com.recursion;

/**
 * @author wangwei
 * 2020/8/28 19:04
 *
 *
有一幅以二维整数数组表示的图画，每一个整数表示该图画的像素值大小，数值在 0 到 65535 之间。

给你一个坐标 (sr, sc) 表示图像渲染开始的像素值（行 ，列）和一个新的颜色值 newColor，让你重新上色这幅图像。

为了完成上色工作，从初始坐标开始，记录初始坐标的上下左右四个方向上像素值与初始坐标相同的相连像素点，接着再记录这四个方向上符合条件的像素点与他们对应四个方向上像素值与初始坐标相同的相连像素点，……，重复该过程。将所有有记录的像素点的颜色值改为新的颜色值。

最后返回经过上色渲染后的图像。

示例 1:

输入:
image = [[1,1,1],[1,1,0],[1,0,1]]
sr = 1, sc = 1, newColor = 2
输出: [[2,2,2],[2,2,0],[2,0,1]]
解析:
在图像的正中间，(坐标(sr,sc)=(1,1)),
在路径上所有符合条件的像素点的颜色都被更改成2。
注意，右下角的像素没有更改为2，
因为它不是在上下左右四个方向上与初始点相连的像素点。
注意:

image 和 image[0] 的长度在范围 [1, 50] 内。
给出的初始点将满足 0 <= sr < image.length 和 0 <= sc < image[0].length。
image[i][j] 和 newColor 表示的颜色值在范围 [0, 65535]内。
 */
public class _733_FloodFill {

    // 标记每个点是否已经修改过
    private boolean[][] visited;

    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        visited = new boolean[image.length][image[0].length];
        // 保存原来数值
        int oldColor = image[sr][sc];
        // 递归填充
        floodFill(image, sr, sc, oldColor, newColor);
        return image;
    }

    /**、
     * 递归，每一个点，考虑其上下左右四个相连的点
     * 用visited数组来记录每个点是否已处理过，避免重复以及死循环
     * @param image
     * @param sr
     * @param sc
     * @param oldColor
     * @param newColor
     */
    private void floodFill(int[][] image, int sr, int sc, int oldColor, int newColor) {
        // 当前点不在合法范围内，不用考虑
        if (outOfBounds(image, sr, sc)) {
            return;
        }
        // 当前位置已经修改过
        if (visited[sr][sc]) return;
        // 当前点是其他颜色（比如0），不能动
        if (image[sr][sc] != oldColor) return;
        // 修改当前点颜色，并标记为已修改
        visited[sr][sc] = true;
        image[sr][sc] = newColor;

        // 处理它上面那个点
        floodFill(image, sr, sc - 1, oldColor, newColor);
        // 处理它下面那个点
        floodFill(image, sr, sc + 1, oldColor, newColor);
        // 处理它左边那个点
        floodFill(image, sr - 1, sc, oldColor, newColor);
        // 处理它右边那个点
        floodFill(image, sr + 1, sc, oldColor, newColor);
    }

    /**
     * 判断点(i, j)是否是二维数组之外的点
     * @param image
     * @param i
     * @param j
     * @return
     */
    private boolean outOfBounds(int[][] image, int i, int j) {
        int m = image.length;
        int n = image[0].length;
        if (i < 0 || i >= m) return true;
        if (j < 0 || j >= n) return true;
        return false;
    }

    /**
     * 回溯，不需要visited数组
     * @param image
     * @param sr
     * @param sc
     * @param oldColor
     * @param newColor
     */
    private void floodFill2(int[][] image, int sr, int sc, int oldColor, int newColor) {
        // 当前点不在合法范围内，不用考虑
        if (outOfBounds(image, sr, sc)) {
            return;
        }
        // 当前位置已标记，直接返回
        if (image[sr][sc] == -1) return;
        // 当前点是其他颜色（比如0），不能动
        if (image[sr][sc] != oldColor) return;
        // 先将当前点标记为待修改，处理它的上下左右，之后再真正的修改它
        // 既能达到避免重复处理同一个点，又不需要额外数组
        // ，相当于使用一个特殊值 -1 代替 visited 数组的作用，达到不走回头路的效果。
        // 因为题目中说了颜色取值在 0 - 65535 之间，所以 -1 足够特殊，能和颜色区分开。
        visited[sr][sc] = true;

        // 处理它上面那个点
        floodFill(image, sr, sc - 1, oldColor, newColor);
        // 处理它下面那个点
        floodFill(image, sr, sc + 1, oldColor, newColor);
        // 处理它左边那个点
        floodFill(image, sr - 1, sc, oldColor, newColor);
        // 处理它右边那个点
        floodFill(image, sr + 1, sc, oldColor, newColor);

        // 真正地修改这个点
        image[sr][sc] = newColor;
    }

}
