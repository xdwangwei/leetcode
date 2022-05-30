package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/5/26 10:22
 * @description: _699_FallingSquares
 *
 * 699. 掉落的方块
 * 在无限长的数轴（即 x 轴）上，我们根据给定的顺序放置对应的正方形方块。
 *
 * 第 i 个掉落的方块（positions[i] = (left, side_length)）是正方形，其中 left 表示该方块最左边的点位置(positions[i][0])，side_length 表示该方块的边长(positions[i][1])。
 *
 * 每个方块的底部边缘平行于数轴（即 x 轴），并且从一个比目前所有的落地方块更高的高度掉落而下。在上一个方块结束掉落，并保持静止后，才开始掉落新方块。
 *
 * 方块的底边具有非常大的粘性，并将保持固定在它们所接触的任何长度表面上（无论是数轴还是其他方块）。邻接掉落的边不会过早地粘合在一起，因为只有底边才具有粘性。
 *
 *
 *
 * 返回一个堆叠高度列表 ans 。每一个堆叠高度 ans[i] 表示在通过 positions[0], positions[1], ..., positions[i] 表示的方块掉落结束后，目前所有已经落稳的方块堆叠的最高高度。
 *
 *
 *
 *
 *
 * 示例 1:
 *
 * 输入: [[1, 2], [2, 3], [6, 1]]
 * 输出: [2, 5, 5]
 * 解释:
 *
 * 第一个方块 positions[0] = [1, 2] 掉落：
 * _aa
 * _aa
 * -------
 * 方块最大高度为 2 。
 *
 * 第二个方块 positions[1] = [2, 3] 掉落：
 * __aaa
 * __aaa
 * __aaa
 * _aa__
 * _aa__
 * --------------
 * 方块最大高度为5。
 * 大的方块保持在较小的方块的顶部，不论它的重心在哪里，因为方块的底部边缘有非常大的粘性。
 *
 * 第三个方块 positions[1] = [6, 1] 掉落：
 * __aaa
 * __aaa
 * __aaa
 * _aa
 * _aa___a
 * --------------
 * 方块最大高度为5。
 *
 * 因此，我们返回结果[2, 5, 5]。
 *
 *
 * 示例 2:
 *
 * 输入: [[100, 100], [200, 100]]
 * 输出: [100, 100]
 * 解释: 相邻的方块不会过早地卡住，只有它们的底部边缘才能粘在表面上。
 *
 *
 * 注意:
 *
 * 1 <= positions.length <= 1000.
 * 1 <= positions[i][0] <= 10^8.
 * 1 <= positions[i][1] <= 10^6.
 *
 */
public class _699_FallingSquares {


    /**
     * 方法一：暴力枚举
     * 我们用数组 heights 记录各个方块掉落后，它所在区间内(方块的left，方块的right)的最大高度。
     * （当一个格子落到其他格子上面以后，后来的格子就只能继续向上堆。所以可以把已经落稳了的格子，看作变成长方形，且其下方把与其有交集格子中重叠的部分覆盖了。）
     * （因此，这里所谓最大高度，就是下落后，这个正方形变成长方形后的高度）
     * （建议看图：https://leetcode.cn/problems/falling-squares/solution/by-fuxuemingzhu-g6vb/）
     *
     * 对于第 i 个掉落的方块，如果它的底部区间与第 j 个（之前的）掉落的方块区间有重叠，那么它掉落后它的区间的高度至少为 heights[j] + size
     * 其中 j<i 且 size 为第 i 个掉落的方块的边长。
     * 因此对于第 i 个掉落的方块，heights[i] 的初始值为 size
     * ，我们暴力枚举所有之前已经掉落的方块，如果两者的底部区间有重叠，那么更新 heights[i] = max(heights[i],heights[j] + size
     *
     * 这里有个问题是，假如 [a2, b2] 和 [a1, b1]有交集，那么 方块2下落后，两个区间内的最大高度都应该变化，为何我们只记录 [a2, b2]的最大高度，而不更新[a1,b1]的最大高度
     * 两个理解方向：
     * 1. 我们说了，这个最大高度可以看作方块下落后形成的长方形的高度，那肯定是只有自己变高了，之前的那些方块或者长方形高度没有变，之际两部分有点重叠而已
     * 2. 因为，我们对于每个下落的方块都会去找之前全部的 方块区间内的最大高度，因为假如新方块和 [a2, b2][a1, b1]都有交集，也能得到正确答案
     *    并且，假如说 [a1, b1] 分为 [a1, c1]和[m,n]，[a2, b2] 分为 [m,n和[c2, b2]，二者重叠部分是 [m,n]
     *    那么假设[a3,b3]之和[a1,c1]有重叠，那么如果你在记录[a2,b2]的时候把整个[a1,b1]的最大高度给改了，此处就会得到错误答案，因为这部分高度没变
     *
     * 总之，理解这个所谓最大高度，就是下落后，以自身为宽，会形成一个新的矩形，这里指的就是这个矩形的最大高度，并不是区间内某个部分的最大高度，而是整个区间的最大高度
     *
     * 我们所记录的是每个方块掉落后它本身所在区间内的最大高度，而题目要求返回的是每个方块落稳后全局的的最大堆叠高度，
     * 因此我们从 i=1 开始，更新 heights[i] = max(heights[i],heights[i−1])，然后返回 heights 即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/falling-squares/solution/diao-luo-de-fang-kuai-by-leetcode-soluti-2dmw/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param positions
     * @return
     */
    public List<Integer> fallingSquares(int[][] positions) {
        int n = positions.length;
        // 下标代表第i个方块下落，值代表 第i个方块下落后，它所在区间内(方块的left，方块的right)的最大高度。
        List<Integer> intervalMaxHeight = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            // 它所在区间
            int curLeft = positions[i][0], curRight = positions[i][0] + positions[i][1] - 1;
            int preMaxHeight = 0;
            // 遍历之前已经掉落的所有方块，找到所有有重叠区间的最大高度
            for (int j = 0; j < i; j++) {
                int preLeft = positions[j][0], preRight = positions[j][0] + positions[j][1] - 1;
                // 如果对方所在区间与自己区间有重叠
                if (curLeft <= preRight && curRight >= preLeft) {
                    // 更新得到重叠区间的最大高度
                    // 这里不要去修改原区间的最大高度，你只有有点重叠而已，下落后你自己变高了，不是别人变高了(重叠不算)
                    preMaxHeight = Math.max(preMaxHeight, intervalMaxHeight.get(j));
                }
            }
            // 那么自己所在区间内的最大高度就为 有重叠的区间内的最大高度 + 当前方块本身的高度
            intervalMaxHeight.add(preMaxHeight + positions[i][1]);
        }
        // 们所记录的是每个方块掉落后它本身所在区间内的最大高度，而题目要求返回的是每个方块落稳后全局的的最大堆叠高度，
        for (int i = 1; i < n; i++) {
            // 由于方块是按顺序掉落。我们的list也是按顺序保存的每个方块下落后它所在区间的最大高度
            // 因此直接向前更新即可
            intervalMaxHeight.set(i, Math.max(intervalMaxHeight.get(i), intervalMaxHeight.get(i - 1)));
        }
        return intervalMaxHeight;
    }



    public static void main(String[] args) {
        _699_FallingSquares obj = new _699_FallingSquares();
        obj.fallingSquares(new int[][]{{1, 2}, {2, 3}, {6, 1}});
        obj.fallingSquares(new int[][]{{9, 7}, {1, 9}, {3, 1}});
    }
}
