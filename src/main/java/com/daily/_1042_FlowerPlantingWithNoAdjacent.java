package com.daily;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangwei
 * @date 2023/4/15 18:42
 * @description: _1042_FlowerPlantingWithNoAdjacent
 * 1042. 不邻接植花
 * 有 n 个花园，按从 1 到 n 标记。另有数组 paths ，其中 paths[i] = [xi, yi] 描述了花园 xi 到花园 yi 的双向路径。在每个花园中，你打算种下四种花之一。
 *
 * 另外，所有花园 最多 有 3 条路径可以进入或离开.
 *
 * 你需要为每个花园选择一种花，使得通过路径相连的任何两个花园中的花的种类互不相同。
 *
 * 以数组形式返回 任一 可行的方案作为答案 answer，其中 answer[i] 为在第 (i+1) 个花园中种植的花的种类。花的种类用  1、2、3、4 表示。保证存在答案。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 3, paths = [[1,2],[2,3],[3,1]]
 * 输出：[1,2,3]
 * 解释：
 * 花园 1 和 2 花的种类不同。
 * 花园 2 和 3 花的种类不同。
 * 花园 3 和 1 花的种类不同。
 * 因此，[1,2,3] 是一个满足题意的答案。其他满足题意的答案有 [1,2,4]、[1,4,2] 和 [3,2,1]
 * 示例 2：
 *
 * 输入：n = 4, paths = [[1,2],[3,4]]
 * 输出：[1,2,1,2]
 * 示例 3：
 *
 * 输入：n = 4, paths = [[1,2],[2,3],[3,4],[4,1],[1,3],[2,4]]
 * 输出：[1,2,3,4]
 *
 *
 * 提示：
 *
 * 1 <= n <= 104
 * 0 <= paths.length <= 2 * 104
 * paths[i].length == 2
 * 1 <= xi, yi <= n
 * xi != yi
 * 每个花园 最多 有 3 条路径可以进入或离开
 * 通过次数25,583提交次数42,945
 */
public class _1042_FlowerPlantingWithNoAdjacent {


    /**
     * 方法一：颜色标记
     * 思路与算法
     *
     * 由于每个花园最多有3 条路径可以进入或离开，这就说明每个花园最多有 3 个花园与之相邻，即图中每个点的度数至多为 3。
     * 而每个花园可选的种植种类有 4 种，这就保证一定存在合法的种植方案满足题目要求。
     *
     * 花园中种植不同的花可以视为每个花园只能标记为给定的4种颜色为 1,2,3,4 中的一种，初始化时我们可以为每个花园标记为颜色 0。
     *
     * 对于第i 个花园，统计其周围的花园已经被标记的颜色，然后从未标记的颜色中选一种颜色给其标记即可。
     *
     * 整体标记过程如下：
     *
     * 首先建立整个图的邻接列表 adj ；
     * 遍历每个花园，并统计其相邻的花园的颜色标记，并从未被标记的颜色中找到一种颜色给当前的花园进行标记；
     * 因为每个花园的颜色只能取1、2、3、4，所以采用int值的4个二进制位来代替当前颜色有没有被使用
     *
     *
     * 返回所有花园的颜色标记方案即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/flower-planting-with-no-adjacent/solution/bu-lin-jie-zhi-hua-by-leetcode-solution-bv74/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param n
     * @param paths
     * @return
     */
    public int[] gardenNoAdj(int n, int[][] paths) {
        List<Integer>[] g = new List[n];
        // 初始化，这里不能用 Arrays.fill(g, new ArrayList<>())，这样相当于 g[0] = g[1] = g[2] = .. = k，k加个值会导致全部改变哈
        Arrays.setAll(g, k -> new ArrayList<>());
        // 构建图，下标转换为从0开始
        for (int[] path : paths) {
            int f = path[0] - 1, t = path[1] - 1;
            g[f].add(t);
            g[t].add(f);
        }
        // 每个花园的颜色，取值为1到4
        int[] ans = new int[n];
        // 遍历
        for (int i = 0; i < n; ++i) {
            // 统计它的邻接花园已经用过的颜色
            int mask = 0;
            // 遍历邻居
            for (Integer v : g[i]) {
                // 标记当前颜色已被用
                mask |= 1 << ans[v];
            }
            // 判断4种颜色，哪个还没被用，随便选1个就行
            for (int j = 1; j <= 4; ++j) {
                if (((mask >> j) & 1) == 0) {
                    ans[i] = j;
                    break;
                }
            }
        }
        // 返回
        return ans;
    }
}
