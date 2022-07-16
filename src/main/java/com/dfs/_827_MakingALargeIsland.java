package com.dfs;

import java.util.*;

/**
 * @author wangwei
 * 2021/11/10 10:10
 * <p>
 * 给你一个大小为 n x n 二进制矩阵 grid 。最多 只能将一格0 变成1 。
 * <p>
 * 返回执行此操作后，grid 中最大的岛屿面积是多少？
 * <p>
 * 岛屿 由一组上、下、左、右四个方向相连的 1 形成。
 * <p>
 * 示例 1:
 * <p>
 * 输入: grid = [[1, 0], [0, 1]]
 * 输出: 3
 * 解释: 将一格0变成1，最终连通两个小岛得到面积为 3 的岛屿。
 * 示例 2:
 * <p>
 * 输入: grid = [[1, 1], [1, 0]]
 * 输出: 4
 * 解释: 将一格0变成1，岛屿的面积扩大为 4。
 * 示例 3:
 * <p>
 * 输入: grid = [[1, 1], [1, 1]]
 * 输出: 4
 * 解释: 没有0可以让我们变成1，面积依然为 4。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/making-a-large-island
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _827_MakingALargeIsland {

    /**
     * 尝试将每一个海水变为陆地，去求得它所在的岛屿的面积，返回其中的最大值
     * ,会漏掉没有海水的情况，需要单独处理
     * <p>
     * 由于我们每次尝试不同的海水，所以每一次尝试后都要恢复原岛屿，在dfs中，将当前点标记为0，dfs它的上下左右后，恢复为1，
     * 但这样会导致dfs算岛屿面积出现重复，对于[[1,1][1,0]]会得到结果7
     * <p>
     * 所以选择bfs
     *
     * 但是这样做的结果是最后一个测试用例会超时
     *
     * @param grid
     * @return
     */
    public int largestIsland(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int res = 0;
        boolean hasWater = false;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                // 对于每一个海水
                if (grid[i][j] == 0) {
                    // 假设把它变为陆地
                    grid[i][j] = 1;
                    // 去求它所在正整块岛屿的面积
                    res = Math.max(res, bfs(grid, i, j));
                    // 求完后恢复为海水
                    grid[i][j] = 0;
                    hasWater = true;
                }
            }
        }
        return hasWater ? res : grid.length * grid[0].length;
    }

    /**
     * dfs，求出每一个连通分量的面积，由于要恢复岛屿，导致面积重复计算，废弃
     *
     * @param grid
     * @param i
     * @param j
     * @return
     */
    private int dfs(int[][] grid, int i, int j) {
        // 数组越界
        if (!inArea(grid, i, j)) {
            return 0;
        }
        // 当前位置是海水
        if (grid[i][j] == 0) {
            return 0;
        }
        // 淹没它，标记它已访问
        grid[i][j] = 0;
        // 求它所在岛屿(四周)面积
        int area = 1 + dfs(grid, i + 1, j) + dfs(grid, i - 1, j)
                + dfs(grid, i, j + 1) + dfs(grid, i, j - 1);
        // 【恢复它】
        grid[i][j] = 1;
        return area;
    }

    /**
     * bfs计算点grid[i][j]所处岛屿的面积
     * @param grid
     * @param i
     * @param j
     * @return
     */
    private int bfs(int[][] grid, int i, int j) {
        if (!inArea(grid, i, j)) {
            return 0;
        }
        int n = grid.length;
        // 要访问的点
        Queue<Integer> queue = new LinkedList<>();
        // 已访问的点, 避免重复计算
        Set<Integer> visited = new HashSet();
        queue.offer(i * n + j);
        visited.add(i * n + j);
        while (!queue.isEmpty()) {
            Integer point = queue.poll();
            int x = point / n, y = point % n;
            // 找到上下左右四个点，如果是陆地，并且没有统计过，那么入栈，入集合
            if (inArea(grid, x - 1, y) && grid[x - 1][y] == 1 && !visited.contains((x - 1) * n + y)) {
                queue.offer((x - 1) * n + y);
                visited.add((x - 1) * n + y);
            }
            if (inArea(grid, x + 1, y) && grid[x + 1][y] == 1 && !visited.contains((x + 1) * n + y)) {
                queue.offer((x + 1) * n + y);
                visited.add((x + 1) * n + y);
            }
            if (inArea(grid, x, y - 1) && grid[x][y - 1] == 1 && !visited.contains(x * n + y - 1)) {
                queue.offer(x * n + y - 1);
                visited.add(x * n + y - 1);
            }
            if (inArea(grid, x, y + 1) && grid[x][y + 1] == 1 && !visited.contains(x * n + y + 1)) {
                queue.offer(x * n + y + 1);
                visited.add(x * n + y + 1);
            }
        }
        // 集合自动去重，所以集合元素个数就是这块岛屿的面积
        return visited.size();
    }


    /**
     * 两次dfs
     * 第一次，计算所有连通分量的面积，对这些连通分量编号，并将这些位置的值改为对应编号，因为原题目 0 代表海水，1 代表陆地，所以从2开始，代表当前陆地位于哪个岛屿
     * 第二次，遍历所有海水，判断它上下左右四个位置，如果是陆地，那么就累加它所在岛屿的面积，因为将自己填为陆地后就能和它相连，
     *      但是要注意，上面四个位置中可能其中两个或多个位置本身就处于同一块岛屿上，所以要避免重复计算（根据它所在岛屿编号判断）
     *
     * @param grid
     * @return
     */
    public int largestIsland2(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        // 岛屿编号从2开始，0代表海水，1代表陆地
        int res = 0, index = 2;
        // 记录每块岛屿的面积
        HashMap<Integer, Integer> islandsAreaMap = new HashMap<>();
        // 统计所有岛屿面积，并进行编号
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                // 对于每一个陆地，找到它所在岛屿的面积，改变这些位置值为对应岛屿序号
                if (grid[i][j] == 1) {
                    int area = dfs2(grid, i, j, index);
                    // 如果没有海水，那么最终答案会在这里产生
                    res = Math.max(res, area);
                    islandsAreaMap.put(index, area);
                    index++;
                }
            }
        }
        // 遍历所有海水
        HashSet<Integer> islands = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                // 判断它上下左右四个位置，如果是陆地，记录它处于哪个岛屿，将自己变为陆地后，就能和它连通，但要注意区分某几个陆地本身就处于同一个岛屿的情况
                if (grid[i][j] == 0) {
                    // 下
                    if (inArea(grid, i + 1, j) && grid[i + 1][j] > 0) {
                        // 记录这块岛屿的编号，set 自动去重
                        islands.add(grid[i + 1][j]);
                    }
                    // 上
                    if (inArea(grid, i - 1, j) && grid[i - 1][j] > 0) {
                        // 记录这块岛屿的编号，set 自动去重
                        islands.add(grid[i - 1][j]);
                    }
                    // 右
                    if (inArea(grid, i, j + 1) && grid[i][j + 1] > 0) {
                        // 记录这块岛屿的编号，set 自动去重
                        islands.add(grid[i][j + 1]);
                    }
                    // 左
                    if (inArea(grid, i, j - 1) && grid[i][j - 1] > 0) {
                        // 记录这块岛屿的编号，set 自动去重
                        islands.add(grid[i][j - 1]);
                    }
                }
                // 自身面积
                int temp = 1;
                // 结合四周岛屿面积
                for (Integer island : islands) {
                    temp += islandsAreaMap.get(island);
                }
                // 更新返回值
                res = Math.max(res, temp);
                // 清空，为下一个海水位置服务
                islands.clear();
            }
        }
        // 之前已经考虑过没有海水的情况，所以直接返回
        return res;
    }

    /**
     * dfs，求出每一个连通分量的面积，将这些位置的值改为岛屿编号
     *
     * @param grid
     * @param i
     * @param j
     * @param index
     * @return
     */
    private int dfs2(int[][] grid, int i, int j, int index) {
        // 数组越界
        if (!inArea(grid, i, j)) {
            return 0;
        }
        // 当前位置是海水或已统计过
        if (grid[i][j] != 1) {
            return 0;
        }
        // 淹没它，标记它已访问，改变值为岛屿编号
        grid[i][j] = index;
        // 求它所在岛屿(四周)面积，处于同一个岛屿
        return 1 + dfs2(grid, i + 1, j, index) + dfs2(grid, i - 1, j, index)
                + dfs2(grid, i, j + 1, index) + dfs2(grid, i, j - 1, index);
    }

    private boolean inArea(int[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        System.out.println(new _827_MakingALargeIsland().largestIsland(new int[][]{{1, 1}, {1, 0}}));
    }
}
