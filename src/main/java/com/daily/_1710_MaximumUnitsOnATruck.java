package com.daily;


import java.util.Arrays;

/**
 *
 * @author wangwei
 * @date 2022/11/15 10:41
 * @description: _1710_MaximumUnitsOnATruck
 * 1710. 卡车上的最大单元数
 * 请你将一些箱子装在 一辆卡车 上。给你一个二维数组 boxTypes ，其中 boxTypes[i] = [numberOfBoxesi, numberOfUnitsPerBoxi] ：
 *
 * numberOfBoxesi 是类型 i 的箱子的数量。
 * numberOfUnitsPerBoxi 是类型 i 每个箱子可以装载的单元数量。
 * 整数 truckSize 表示卡车上可以装载 箱子 的 最大数量 。只要箱子数量不超过 truckSize ，你就可以选择任意箱子装到卡车上。
 *
 * 返回卡车可以装载 单元 的 最大 总数。
 *
 *
 *
 * 示例 1：
 *
 * 输入：boxTypes = [[1,3],[2,2],[3,1]], truckSize = 4
 * 输出：8
 * 解释：箱子的情况如下：
 * - 1 个第一类的箱子，里面含 3 个单元。
 * - 2 个第二类的箱子，每个里面含 2 个单元。
 * - 3 个第三类的箱子，每个里面含 1 个单元。
 * 可以选择第一类和第二类的所有箱子，以及第三类的一个箱子。
 * 单元总数 = (1 * 3) + (2 * 2) + (1 * 1) = 8
 * 示例 2：
 *
 * 输入：boxTypes = [[5,10],[2,5],[4,7],[3,9]], truckSize = 10
 * 输出：91
 *
 *
 * 提示：
 *
 * 1 <= boxTypes.length <= 1000
 * 1 <= numberOfBoxesi, numberOfUnitsPerBoxi <= 1000
 * 1 <= truckSize <= 106
 * 通过次数27,613提交次数37,960
 */
public class _1710_MaximumUnitsOnATruck {

    /**
     * 方法一：贪心
     * 思路
     *
     * 只能装 truckSize 个箱子到卡车上，根据贪心的思路，只需要每次都拿当前剩下的箱子里单元数量最大的箱子即可。
     *
     * 对 boxTypes 按照 numberOfUnitsPerBox 进行逆序排序，然后从左至右填充 truckSize 即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/maximum-units-on-a-truck/solution/qia-che-shang-de-zui-da-dan-yuan-shu-by-ynaqv/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int maximumUnits(int[][] boxTypes, int truckSize) {
        // 按能承载的单元数对所有类型箱子排序
        Arrays.sort(boxTypes, (a, b) -> b[1] - a[1]);
        int ans = 0;
        // 从大到小
        for (int[] e : boxTypes) {
            // 当前箱子数量，能承载的单元数
            int cnt = e[0], unit = e[1];
            // truckSize 表示 还能使用的 箱子个数
            ans += unit * Math.min(truckSize, cnt);
            // 选完后更新 可选数量
            truckSize -= cnt;
            // 如果不能再选了，提前结束
            if (truckSize <= 0) {
                break;
            }
        }
        return ans;
    }
}
