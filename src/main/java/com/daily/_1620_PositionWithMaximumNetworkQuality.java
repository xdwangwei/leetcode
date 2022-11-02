package com.daily;

/**
 * @author wangwei
 * @date 2022/11/2 12:11
 * @description: _1620_PositionWithMaximumNetworkQuality
 *
 * 1620. 网络信号最好的坐标
 * 给你一个数组 towers 和一个整数 radius 。
 *
 * 数组  towers  中包含一些网络信号塔，其中 towers[i] = [xi, yi, qi] 表示第 i 个网络信号塔的坐标是 (xi, yi) 且信号强度参数为 qi 。所有坐标都是在  X-Y 坐标系内的 整数 坐标。两个坐标之间的距离用 欧几里得距离 计算。
 *
 * 整数 radius 表示一个塔 能到达 的 最远距离 。如果一个坐标跟塔的距离在 radius 以内，那么该塔的信号可以到达该坐标。在这个范围以外信号会很微弱，所以 radius 以外的距离该塔是 不能到达的 。
 *
 * 如果第 i 个塔能到达 (x, y) ，那么该塔在此处的信号为 ⌊qi / (1 + d)⌋ ，其中 d 是塔跟此坐标的距离。一个坐标的 信号强度 是所有 能到达 该坐标的塔的信号强度之和。
 *
 * 请你返回数组 [cx, cy] ，表示 信号强度 最大的 整数 坐标点 (cx, cy) 。如果有多个坐标网络信号一样大，请你返回字典序最小的 非负 坐标。
 *
 * 注意：
 *
 * 坐标 (x1, y1) 字典序比另一个坐标 (x2, y2) 小，需满足以下条件之一：
 * 要么 x1 < x2 ，
 * 要么 x1 == x2 且 y1 < y2 。
 * ⌊val⌋ 表示小于等于 val 的最大整数（向下取整函数）。
 *
 *
 * 示例 1：
 *
 *
 * 输入：towers = [[1,2,5],[2,1,7],[3,1,9]], radius = 2
 * 输出：[2,1]
 * 解释：
 * 坐标 (2, 1) 信号强度之和为 13
 * - 塔 (2, 1) 强度参数为 7 ，在该点强度为 ⌊7 / (1 + sqrt(0)⌋ = ⌊7⌋ = 7
 * - 塔 (1, 2) 强度参数为 5 ，在该点强度为 ⌊5 / (1 + sqrt(2)⌋ = ⌊2.07⌋ = 2
 * - 塔 (3, 1) 强度参数为 9 ，在该点强度为 ⌊9 / (1 + sqrt(1)⌋ = ⌊4.5⌋ = 4
 * 没有别的坐标有更大的信号强度。
 * 示例 2：
 *
 * 输入：towers = [[23,11,21]], radius = 9
 * 输出：[23,11]
 * 解释：由于仅存在一座信号塔，所以塔的位置信号强度最大。
 * 示例 3：
 *
 * 输入：towers = [[1,2,13],[2,1,7],[0,1,9]], radius = 2
 * 输出：[1,2]
 * 解释：坐标 (1, 2) 的信号强度最大。
 *
 *
 * 提示：
 *
 * 1 <= towers.length <= 50
 * towers[i].length == 3
 * 0 <= xi, yi, qi <= 50
 * 1 <= radius <= 50
 * 通过次数12,603提交次数28,267
 */
public class _1620_PositionWithMaximumNetworkQuality {


    /**
     * 模拟
     * 观察数据范围：无论是 towers 数组大小、坐标 (x,y) 的值域大小，还是最远距离 radius，取值均不超过 50。
     *
     * 数据范围有限，我们可以直接采用「模拟」的方式进行求解，而不会面临 TLE 或 MLE 的风险。
     *
     * 具体的，我们建立一个大小为 110×110 的棋盘 g，用于记录每个坐标点的信号值，即 [i][j]=x 代表坐标 (i,j) 的信号值为 x。
     *
     * 其中 110 的大小是利用了「任意坐标 (x,y) 的取值范围不超过 50」，同时「最远距离 radius 不超过 5050」并且「最终答案为非负坐标」而定。
     *
     * 随后，可以枚举所有 towers[i]=(a,b,q)，并检查以该塔为中心点，上下左右扩展radius范围的矩阵中的所有点（该塔所能贡献信号的所有坐标均落在矩阵中），
     * 枚举过程中使用变量 val 记录最大信号值，使用 x 和 y 记录答案坐标。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/coordinate-with-maximum-network-quality/solution/by-ac_oier-xtx3/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param towers
     * @param radius
     * @return
     */
    public int[] bestCoordinate(int[][] towers, int radius) {
        // 所有塔能贡献信号的所有位置的信号轻度强度
        int[][] posNetQuality = new int[110][110];
        // 枚举所有塔
        for (int[] pos : towers) {
            // 坐标，信号强度
            int x = pos[0], y = pos[1], q = pos[2];
            // 向左向右radius，非负
            for (int nx = Math.max(x - radius, 0); nx <= x + radius; ++nx) {
                // 向上向下radius，非负
                for (int ny = Math.max(y - radius, 0); ny <= y + radius; ++ny) {
                    // 距离
                    double distance = getDistance(x, y, nx, ny);
                    // 不能超过radius范围
                    if (distance > radius) {
                        continue;
                    }
                    // 此位置信号强度增加
                    posNetQuality[nx][ny] += Math.floor(q / (1 + distance));
                }
            }
        }
        // 枚举所有点信号强度，找出最强信号所在位置
        int maxQ = 0, x = 0, y = 0;
        for (int i = 0; i < posNetQuality.length; i++) {
            for (int j = 0; j < posNetQuality.length; ++j) {
                // 更强的信号
                if (posNetQuality[i][j] > maxQ) {
                    maxQ = posNetQuality[i][j];
                    x = i; y = j;
                    // 同等强度新号，寻找字典序最小的坐标
                } else if (posNetQuality[i][j] == maxQ && (i < x || (i == x && j == y))) {
                    x = i; y = j;
                }
            }
        }
        // 返回
        return new int[]{x, y};
    }


    /**
     * 两点之间欧几里得距离
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
