package com.daily;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangwei
 * @date 2023/6/22 16:08
 * @description: _Mianshi_16_19_PondSizeLCCI
 *
 * 面试题 16.19. 水域大小
 * 你有一个用于表示一片土地的整数矩阵land，该矩阵中每个点的值代表对应地点的海拔高度。
 * 若值为0则表示水域。
 * 由垂直、水平或对角连接的水域为池塘。池塘的大小是指相连接的水域的个数。
 * 编写一个方法来计算矩阵中所有池塘的大小，返回值需要从小到大排序。
 *
 * 示例：
 *
 * 输入：
 * [
 *   [0,2,1,0],
 *   [0,1,0,1],
 *   [1,1,0,1],
 *   [0,1,0,1]
 * ]
 * 输出： [1,2,4]
 * 提示：
 *
 * 0 < len(land) <= 1000
 * 0 < len(land[i]) <= 1000
 * 通过次数33,715提交次数51,949
 */
public class _Mianshi_16_19_PondSizeLCCI {

    private int[][] land;
    private int n, m;

    /**
     * 方法：DFS
     *
     * 题目等效于：寻找 land 所有联通分量的大小，排序后返回
     *
     * 我们可以遍历整数矩阵 land 中的每个点 (i,j)，
     *
     *      如果该点的值为 0，则从该点开始进行深度优先搜索，直到搜索到的点的值不为 0，则停止搜索，
     *      即，找到该点所在的联通分量
     *      此时搜索到的点的个数即为池塘的大小，将其加入答案数组中。
     *
     *      注意：在进行深度优先搜索时，为了避免重复搜索，我们将搜索到的点的值置为 1。
     *
     * 最后，我们对答案数组进行排序，即可得到最终答案。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/pond-sizes-lcci/solution/python3javacgotypescript-yi-ti-yi-jie-df-j343/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param land
     * @return
     */
    public int[] pondSizes(int[][] land) {
        this.land = land;
        n = land.length;
        m = land[0].length;
        // 保存所有联通分量大小
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                // 遇到0点，遍历它所在联通分量，得到其大小，并标志其内点已访问过（赋值为1）
                if (land[i][j] == 0) {
                    ans.add(dfs(i, j));
                }
            }
        }
        // 排序后返回
        return ans.stream().sorted().mapToInt(Integer::valueOf).toArray();
    }

    /**
     * dfs 得到 (i,j) 所在联通分量大小，(i, j) 对应值为 0
     * @param i
     * @param j
     * @return
     */
    private int dfs(int i, int j) {
        // 联通分量，当前点1个
        int res = 1;
        // 标记已访问
        land[i][j] = 1;
        // 上、下、左、右、左上、右上、左下、右下 八个位置的 邻接点
        for (int x = i - 1; x <= i + 1; ++x) {
            for (int y = j - 1; y <= j + 1; ++y) {
                // 不越界，且同样为0
                if (x >= 0 && x < m && y >= 0 && y < n && land[x][y] == 0) {
                    // 继续 dfs，累加答案，注意这里是 +=
                    res += dfs(x, y);
                }
            }
        }
        // 返回
        return res;
    }
}
