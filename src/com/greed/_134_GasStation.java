package com.greed;

/**
 * @author wangwei
 * @version 1.0.0
 * @date 2021/9/23 15:10
 * @description: 在一条环路上有 N 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
 *
 * 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。你从其中的一个加油站出发，开始时油箱为空。
 *
 * 如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
 *
 * 说明: 
 *
 * 如果题目有解，该答案即为唯一答案。
 * 输入数组均为非空数组，且长度相同。
 * 输入数组中的元素均为非负数。
 * 示例 1:
 *
 * 输入:
 * gas  = [1,2,3,4,5]
 * cost = [3,4,5,1,2]
 *
 * 输出: 3
 *
 * 解释:
 * 从 3 号加油站(索引为 3 处)出发，可获得 4 升汽油。此时油箱有 = 0 + 4 = 4 升汽油
 * 开往 4 号加油站，此时油箱有 4 - 1 + 5 = 8 升汽油
 * 开往 0 号加油站，此时油箱有 8 - 2 + 1 = 7 升汽油
 * 开往 1 号加油站，此时油箱有 7 - 3 + 2 = 6 升汽油
 * 开往 2 号加油站，此时油箱有 6 - 4 + 3 = 5 升汽油
 * 开往 3 号加油站，你需要消耗 5 升汽油，正好足够你返回到 3 号加油站。
 * 因此，3 可为起始索引。
 * 示例 2:
 *
 * 输入:
 * gas  = [2,3,4]
 * cost = [3,4,3]
 *
 * 输出: -1
 *
 * 解释:
 * 你不能从 0 号或 1 号加油站出发，因为没有足够的汽油可以让你行驶到下一个加油站。
 * 我们从 2 号加油站出发，可以获得 4 升汽油。 此时油箱有 = 0 + 4 = 4 升汽油
 * 开往 0 号加油站，此时油箱有 4 - 3 + 2 = 3 升汽油
 * 开往 1 号加油站，此时油箱有 3 - 3 + 3 = 3 升汽油
 * 你无法返回 2 号加油站，因为返程需要消耗 4 升汽油，但是你的油箱只有 3 升汽油。
 * 因此，无论怎样，你都不可能绕环路行驶一周。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/gas-station
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _134_GasStation {

    /**
     * 如果计算到达每个站点时的总油量，假如以0为起点，tank=0，那么tank[i] = sum(gas[j]-cost[j])
     * 如果将其看做一个折线图，那么我们要保证这条折线图上所有点都在x轴上方，如何做到？
     * 我们可以找到最低点，以它为起点(水平轴)，那么相当于整个折线图向上平移，最低点前半段拼接到后面(循环数组)。
     * 当然，如果sum(gas) < sum(cost) 那么肯定是到不了的
     * @param gas
     * @param cost
     * @return
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int tank = 0;
        int minTank = Integer.MAX_VALUE, minIndex = 0;
        int n = gas.length;
        for (int i = 0; i < n; i++) {
            tank += gas[i] - cost[i];
            // 找到最低点
            if (tank < minTank) {
                minIndex = i;
                minTank = tank;
            }
        }
        // sum(gas) < sum(cost)
        if (tank < 0) {
            return -1;
        }
        // 以最低点为起点，为什么是最低点加1，因为我们相当于给折线图加了一个起点0，然后第一个加油站是第一个点，所以要往后移动，当然可以直接用示例数据试一下
        // 或者这样想：最低点剩余油量是负数，说明恰好不能到达最低点，那么按照结论，应该从最低点下一个点开始。
        return (minIndex + 1) % n;
    }

    /**
     * 贪心思想
     * 基于如下结论：
     *      如果从i恰好走到j时的油量为负，那么对于任意 i < k < j，从k出发也肯定走不到j，所以我们直接以j+1为起点
     * i走到j油量为负，说明最起码i走到k时的油量是正的(tank[k] > 0)，从k走到j油量变负了，那么如果以k为起点，k位置油量为0，那么就更不可能走到j了
     * 官方题解(数学证明)：https://leetcode-cn.com/problems/gas-station/solution/jia-you-zhan-by-leetcode-solution/
     * 当然，如果sum(gas) < sum(cost) 那么肯定是到不了的
     * @param gas
     * @param cost
     * @return
     */
    public int canCompleteCircuit2(int[] gas, int[] cost) {
        int tank = 0, start = 0;
        int n = gas.length;
        for (int i = 0; i < n; i++) {
            tank += gas[i] - cost[i];
        }
        // 不可能情况,总gas < 总cost
        if (tank < 0) {
            return -1;
        }
        tank = 0;
        for (int i = 0; i < n; i++) {
            tank += gas[i] - cost[i];
            // 直接跳到i后面
            if (tank < 0) {
                start = i + 1;
                tank = 0;
            }
        }
        // 以最低点为起点，为什么是最低点加1，因为我们相当于给折线图加了一个起点0，然后第一个加油站是第一个点，所以要往后移动，当然可以直接用示例数据试一下
        // 或者这样想：最低点剩余油量是负数，说明恰好不能到达最低点，那么按照结论，应该从最低点下一个点开始。
        return start % n;
    }
}
