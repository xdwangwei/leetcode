package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/11/23 11:49
 * @description: _1742_MaximumNumberOfBallsInBox
 *
 * 1742. 盒子中小球的最大数量
 * 你在一家生产小球的玩具厂工作，有 n 个小球，编号从 lowLimit 开始，到 highLimit 结束（包括 lowLimit 和 highLimit ，即 n == highLimit - lowLimit + 1）。另有无限数量的盒子，编号从 1 到 infinity 。
 *
 * 你的工作是将每个小球放入盒子中，其中盒子的编号应当等于小球编号上每位数字的和。例如，编号 321 的小球应当放入编号 3 + 2 + 1 = 6 的盒子，而编号 10 的小球应当放入编号 1 + 0 = 1 的盒子。
 *
 * 给你两个整数 lowLimit 和 highLimit ，返回放有最多小球的盒子中的小球数量。如果有多个盒子都满足放有最多小球，只需返回其中任一盒子的小球数量。
 *
 *
 *
 * 示例 1：
 *
 * 输入：lowLimit = 1, highLimit = 10
 * 输出：2
 * 解释：
 * 盒子编号：1 2 3 4 5 6 7 8 9 10 11 ...
 * 小球数量：2 1 1 1 1 1 1 1 1 0  0  ...
 * 编号 1 的盒子放有最多小球，小球数量为 2 。
 * 示例 2：
 *
 * 输入：lowLimit = 5, highLimit = 15
 * 输出：2
 * 解释：
 * 盒子编号：1 2 3 4 5 6 7 8 9 10 11 ...
 * 小球数量：1 1 1 1 2 2 1 1 1 0  0  ...
 * 编号 5 和 6 的盒子放有最多小球，每个盒子中的小球数量都是 2 。
 * 示例 3：
 *
 * 输入：lowLimit = 19, highLimit = 28
 * 输出：2
 * 解释：
 * 盒子编号：1 2 3 4 5 6 7 8 9 10 11 12 ...
 * 小球数量：0 1 1 1 1 1 1 1 1 2  0  0  ...
 * 编号 10 的盒子放有最多小球，小球数量为 2 。
 *
 *
 * 提示：
 *
 * 1 <= lowLimit <= highLimit <= 105
 */
public class _1742_MaximumNumberOfBallsInBox {


    /**
     * 遍历所有的小球，对于编号为 i 的小球，计算它应该放入的盒子编号 box，使用哈希表记录每个盒子中的小球数量，返回遍历结束后 哈希表 中小球数量最大的盒子对应的小球数量即可。
     *
     * 小球放入的盒子box = 小球编号 i 各个数位上的数字加和
     *
     * 因为 编号 取值最大为 10^5，所以 对应的盒子编号最大为 45
     *
     * 因此可以用大小为46的数组替代hash表，下标为盒子编号，值为盒内球的数量，返回数组最大值
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/maximum-number-of-balls-in-a-box/solution/he-zi-zhong-xiao-qiu-de-zui-da-shu-liang-9sfh/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param lowLimit
     * @param highLimit
     * @return
     */
    public int countBalls(int lowLimit, int highLimit) {
        // 保存盒子x内球的数量y
        int[] cnt = new int[46];
        // 遍历每个球
        for (int i = lowLimit; i <= highLimit; ++i) {
            // 所属盒子等于编号每个位置数字加和
            int x = 0, n = i;
            while (n != 0) {
                x += n % 10;
                n /= 10;
            }
            // 盒子内球的数量增加
            cnt[x]++;
        }
        // 返回所有盒子中装有最多球的盒子中球的数量，即数组元素最大值
        return Arrays.stream(cnt).max().getAsInt();
    }
}
