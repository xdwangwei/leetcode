package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/11/20 11:57
 * @description: _799_ChampagneTower
 *
 * 799. 香槟塔
 * 我们把玻璃杯摆成金字塔的形状，其中 第一层 有 1 个玻璃杯， 第二层 有 2 个，依次类推到第 100 层，每个玻璃杯 (250ml) 将盛有香槟。
 *
 * 从顶层的第一个玻璃杯开始倾倒一些香槟，当顶层的杯子满了，任何溢出的香槟都会立刻等流量的流向左右两侧的玻璃杯。当左右两边的杯子也满了，就会等流量的流向它们左右两边的杯子，依次类推。（当最底层的玻璃杯满了，香槟会流到地板上）
 *
 * 例如，在倾倒一杯香槟后，最顶层的玻璃杯满了。倾倒了两杯香槟后，第二层的两个玻璃杯各自盛放一半的香槟。在倒三杯香槟后，第二层的香槟满了 - 此时总共有三个满的玻璃杯。在倒第四杯后，第三层中间的玻璃杯盛放了一半的香槟，他两边的玻璃杯各自盛放了四分之一的香槟，如下图所示。
 *
 *
 *
 * 现在当倾倒了非负整数杯香槟后，返回第 i 行 j 个玻璃杯所盛放的香槟占玻璃杯容积的比例（ i 和 j 都从0开始）。
 *
 *
 *
 * 示例 1:
 * 输入: poured(倾倒香槟总杯数) = 1, query_glass(杯子的位置数) = 1, query_row(行数) = 1
 * 输出: 0.00000
 * 解释: 我们在顶层（下标是（0，0））倒了一杯香槟后，没有溢出，因此所有在顶层以下的玻璃杯都是空的。
 *
 * 示例 2:
 * 输入: poured(倾倒香槟总杯数) = 2, query_glass(杯子的位置数) = 1, query_row(行数) = 1
 * 输出: 0.50000
 * 解释: 我们在顶层（下标是（0，0）倒了两杯香槟后，有一杯量的香槟将从顶层溢出，位于（1，0）的玻璃杯和（1，1）的玻璃杯平分了这一杯香槟，所以每个玻璃杯有一半的香槟。
 * 示例 3:
 *
 * 输入: poured = 100000009, query_row = 33, query_glass = 17
 * 输出: 1.00000
 *
 *
 * 提示:
 *
 * 0 <= poured <= 109
 * 0 <= query_glass <= query_row < 100
 * 通过次数13,350提交次数27,991
 */
public class _799_ChampagneTower {

    // 倒入的总量
    private double total;

    // 记忆化
    private Map<String, Double> map = new HashMap<>();

    /**
     * 方法一：自定向下，采用递归形式，模拟倒香槟过程。
     * 首先将所有的 poured 杯香槟全部倒到 row=0 的这个杯子中。
     * 当有溢出时，再将溢出的部分平均倒到下一层的相邻的两个杯子中。
     * 而除了 row=0 的这个杯子中的香槟是直接来自于外部，其他杯子中的香槟均来自于它的上一层的相邻的一个或两个杯子中溢出的香槟。
     * 求出 （row，col） 杯子体积，若为负，说明上层无溢出；若 > 1, 则最后只会剩下1，其余会溢出
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/champagne-tower/solution/xiang-bin-ta-by-leetcode-solution-y87c/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param poured
     * @param query_row
     * @param query_glass
     * @return
     */
    public double champagneTower(int poured, int query_row, int query_glass) {
        this.total = 1.0 * poured;
        double ans = helper(query_row, query_glass);
        // 返回前判断，小于等于0说明上层杯子没有溢出量，大于1则最多只能装满，其余量会溢出到下一层
        return ans <= 0 ? 0 : (ans > 1 ? 1 : ans);
    }


    /**
     * 返回能够到达 row 行 col 列杯子的 总量
     * @param row
     * @param col
     * @return
     */
    private double helper(int row, int col) {
        String key = row + "," + col;
        if (map.containsKey(key)) {
            return map.get(key);
        }
        // 第一行，一个杯子，来自于倒入的总量
        if (row == 0) {
            map.put(key, total);
            return total;
        }
        // 每行第一个杯子的量来自于上一行第一个杯子溢出部分的一半
        if (col == 0) {
            double ans = (helper(row - 1, col) - 1) / 2;
            map.put(key, ans);
            return ans;
        }
        // 每行最后一个杯子的量来自于上一行最后一个杯子溢出部分的一半
        if (col == row) {
            double ans = (helper(row - 1, col - 1) - 1) / 2;
            map.put(key, ans);
            return ans;
        }
        // 其余杯子的量来自于上一行两个杯子溢出量的总和
        // 需要注意，上层两个杯子溢出量不一定一样，可能只有其中一个杯子有溢出量
        // 需要避免一负一正产生抵消导致错误结果
        double left = (helper(row - 1, col - 1) - 1) / 2;
        double right = (helper(row - 1, col) - 1) / 2;
        double ans = (left > 0 ? left : 0) + (right > 0 ? right : 0);
        map.put(key, ans);
        return ans;
    }


    /**
     * 方法一：采用迭代形式，模拟倒香槟过程。
     * 首先将所有的 poured 杯香槟全部倒到 row=0 的这个杯子中。
     * 当有溢出时，再将溢出的部分平均倒到下一层的相邻的两个杯子中。
     * 而除了 row=0 的这个杯子中的香槟是直接来自于外部，其他杯子中的香槟均来自于它的上一层的相邻的一个或两个杯子中溢出的香槟。
     * 根据这个规律，可以自上而下推出每一层每个杯子的体积
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/champagne-tower/solution/xiang-bin-ta-by-leetcode-solution-y87c/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param poured
     * @param query_row
     * @param query_glass
     * @return
     */
    public double champagneTower2(int poured, int query_row, int query_glass) {
        // 第0行，一个杯子，体积是 poured
        double[] currRow = {1.0 * poured};
        // 从第0行开始，迭代计算下一行杯子容量
        // 因此，迭代到第query_row-1行，就能得到第query_row行所有杯子容量
        for (int row = 0; row < query_row; ++row) {
            // 当前行有row+1个杯子，下一行有 row + 2个杯子
            double[] nextRow = new double[row + 2];
            // 本行有 row + 1 个杯子
            for (int j = 0; j <= row; ++j) {
                // j号杯子若满会溢出到下一行j，j+1号杯子
                double val = currRow[j];
                if (val > 1) {
                    nextRow[j] += (val - 1) / 2;
                    nextRow[j + 1] += (val - 1) / 2;
                }
            }
            // 迭代
            currRow = nextRow;
        }
        // 返回
        return Math.min(1, currRow[query_glass]);
    }

    public static void main(String[] args) {
        _799_ChampagneTower obj = new _799_ChampagneTower();
        obj.champagneTower2(25, 6, 1);
    }
}
