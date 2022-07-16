package com.daily;

import java.util.Random;

/**
 * @author wangwei
 * @date 2022/6/6 21:50
 * @description: _478_GenerateRandomPointsInCircle
 *
 * 478. 在圆内随机生成点
 * 给定圆的半径和圆心的位置，实现函数 randPoint ，在圆中产生均匀随机点。
 *
 * 实现 Solution 类:
 *
 * Solution(double radius, double x_center, double y_center) 用圆的半径 radius 和圆心的位置 (x_center, y_center) 初始化对象
 * randPoint() 返回圆内的一个随机点。圆周上的一点被认为在圆内。答案作为数组返回 [x, y] 。
 *
 *
 * 示例 1：
 *
 * 输入:
 * ["Solution","randPoint","randPoint","randPoint"]
 * [[1.0, 0.0, 0.0], [], [], []]
 * 输出: [null, [-0.02493, -0.38077], [0.82314, 0.38945], [0.36572, 0.17248]]
 * 解释:
 * Solution solution = new Solution(1.0, 0.0, 0.0);
 * solution.randPoint ();//返回[-0.02493，-0.38077]
 * solution.randPoint ();//返回[0.82314,0.38945]
 * solution.randPoint ();//返回[0.36572,0.17248]
 *
 *
 * 提示：
 *
 * 0 < radius <= 108
 * -107 <= x_center, y_center <= 107
 * randPoint 最多被调用 3 * 104 次
 */
public class _478_GenerateRandomPointsInCircle {


    /**
     * 等概率随机采样
     * 为了方便，我们称圆心为 (x,y)，半径为 r。
     *
     * 对给定圆内的点进行等概率随机采样，容易想到随机化两个信息：一个是距离圆心的距离 len（在范围 [0,r] 中进行随机），另外一个是夹角 ang（在范围 [0,2π] 中随机）
     * （极坐标系）。
     * 两个随机之间相互独立，然后根据 len 和 ang 直接计算对应的点的坐标，这样 可以确保随机出来的点一定在圆内。
     * 但是这样随机半径，并非「等概率」。
     *
     * 在不考虑夹角的情况下，我们本质是在 [0,r] 范围内随机，
     * 这在「一维」上「等概率」是成立的，因为满足「任意连续段中点被抽到的次数与总次数的比例」与「该连续段长度与总长度的比例」一致。
     * 【但在圆中并非如此】
     * 不考虑夹角时，「任意连续段 len 与总长度 r 的比例」和「len 对应面积与总面积比例」并不相等。
     * 例如 len 有 1/2 的概率取到小于等于 r/2 的值，而半径为 r/2 扫过的面积仅为总面积的 1/4
     *
     * 因此我们的 len 不能直接在 [0,r] 范围内随机，
     * 为了消除这种一维转圆导致的「等概率」失效，我们可以从 [0, r^2]内随机再开平方，从而确保距离与面积比例一致。
     *
     * 注意一些常见的思路的错误之处：
     *
     * 随机一个横坐标，计算纵坐标的范围再随机一个纵坐标。
     * 主要错误：纵坐标的范围由横坐标决定，不再独立，越靠近圆边缘越密集，圆中心越稀疏。
     * 随机一个极角，再随机一个半径或半径缩放大小。
     * 主要错误：角度固定后，半径的随机并不应该是均匀的。这样会造成越靠近圆中心越密集，圆边缘稀疏。
     *
     * 作者：himymBen
     * 链接：https://leetcode.cn/problems/generate-random-point-in-a-circle/solution/pythonjavatypescriptgo-cong-mian-ji-chu-r4yzm/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/generate-random-point-in-a-circle/solution/by-ac_oier-btkm/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class Solution {

        Random random;

        // 圆心，半径
        double x, y, r;

        public Solution(double radius, double x_center, double y_center) {
            random = new Random();
            x = x_center;
            y = y_center;
            r = radius;
        }

        public double[] randPoint() {
            // 随机 【0，2pi】的夹角
            double theta = random.nextDouble() * 2 * Math.PI;
            // 不直接随机 0-r的半径
            // 随机 0-r^2的面积，再取根号得到半径
            double len = Math.sqrt(random.nextDouble() * r * r);
            // 根据半径、夹角和圆心得到坐标
            return new double[]{x + len * Math.cos(theta), y + len * Math.sin(theta)};
        }
    }
}
