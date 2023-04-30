package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/4/30 18:22
 * @description: _1033_MoveStonesUntilConsecutive
 *
 * 1033. 移动石子直到连续
 * 三枚石子放置在数轴上，位置分别为 a，b，c。
 *
 * 每一回合，你可以从两端之一拿起一枚石子（位置最大或最小），并将其放入两端之间的任一空闲位置。
 * 形式上，假设这三枚石子当前分别位于位置 x, y, z 且 x < y < z。
 * 那么就可以从位置 x 或者是位置 z 拿起一枚石子，并将该石子移动到某一整数位置 k 处，其中 x < k < z 且 k != y。
 *
 * 当你无法进行任何移动时，即，这些石子的位置连续时，游戏结束。
 *
 * 要使游戏结束，你可以执行的最小和最大移动次数分别是多少？ 以长度为 2 的数组形式返回答案：answer = [minimum_moves, maximum_moves]
 *
 *
 *
 * 示例 1：
 *
 * 输入：a = 1, b = 2, c = 5
 * 输出：[1, 2]
 * 解释：将石子从 5 移动到 4 再移动到 3，或者我们可以直接将石子移动到 3。
 * 示例 2：
 *
 * 输入：a = 4, b = 3, c = 2
 * 输出：[0, 0]
 * 解释：我们无法进行任何移动。
 *
 *
 * 提示：
 *
 * 1 <= a <= 100
 * 1 <= b <= 100
 * 1 <= c <= 100
 * a != b, b != c, c != a
 * 通过次数20,892提交次数44,349
 */
public class _1033_MoveStonesUntilConsecutive {

    /**
     *
     * 分类讨论
     *
     * a、b、c 只代表3个石头的位置，先排序，不影响过程，只是为了分析顺序的这三个石子如何移动
     *
     * 假设 a,b,c 是有序的（从小到大）。a < b < c
     *
     * 最大移动次数：
     *      a 和 c 向 b 靠拢，每次只移动一个单位长度，答案就是 c−a−2。([a,c]共c-a+1个位置，a、b、c占据3个)
     * 最小移动次数：
     *      如果 c−a=2，已经连续，无需移动。
     *      如果 b−a = 1 或者 c−b=1，说明有两颗石子已经连续，那么只需移动 1 次另一颗石子。
     *      如果 b−a=2 或者 c−b=2，那么把一颗石子移到另外两颗石子之间，只需移动 1 次移动。
     *      否则，a 移动到 b−1，c 移动到 b+1，一共2 次移动。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/moving-stones-until-consecutive/solution/fen-lei-tao-lun-pythonjavacgo-by-endless-2qyo/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param a
     * @param b
     * @param c
     * @return
     */
    public int[] numMovesStones(int a, int b, int c) {
        // 排序
        int[] pos = new int[]{a, b, c};
        Arrays.sort(pos);
        // 顺序的三个石头
        a = pos[0];
        b = pos[1];
        c = pos[2];
        // 最大移动次数，a、c之间所有空位， c-a+1 -3 = c-a-2
        int maxMoves = c - a - 2, minMoves = 0;
        // 已经连续，最小移动次数为0
        if (maxMoves == 0) {
            minMoves = 0;
        //一侧连续，将另一个石头挪过来就行，或 一侧的两个石头间有个空位，将另一个石头放进来
        } else if (b - a <= 2 || c - b <= 2) {
            minMoves = 1;
        // 其他情况，需要将 a 移动到 b-1，将 c 移动到 b+1，b不动，共2次移动
        } else {
            minMoves = 2;
        }
        // 返回 最小移动次数，最大移动次数
        return new int[]{minMoves, maxMoves};
    }
}
