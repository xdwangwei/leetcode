package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/10/16 10:01
 * @description: _886_PossibleBiPartition\
 * \
 * 886. 可能的二分法
 * 给定一组 n 人（编号为 1, 2, ..., n）， 我们想把每个人分进任意大小的两组。每个人都可能不喜欢其他人，那么他们不应该属于同一组。
 *
 * 给定整数 n 和数组 dislikes ，其中 dislikes[i] = [ai, bi] ，表示不允许将编号为 ai 和  bi的人归入同一组。当可以用这种方法将所有人分进两组时，返回 true；否则返回 false。
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 4, dislikes = [[1,2],[1,3],[2,4]]
 * 输出：true
 * 解释：group1 [1,4], group2 [2,3]
 * 示例 2：
 *
 * 输入：n = 3, dislikes = [[1,2],[1,3],[2,3]]
 * 输出：false
 * 示例 3：
 *
 * 输入：n = 5, dislikes = [[1,2],[2,3],[3,4],[4,5],[1,5]]
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= n <= 2000
 * 0 <= dislikes.length <= 104
 * dislikes[i].length == 2
 * 1 <= dislikes[i][j] <= n
 * ai < bi
 * dislikes 中每一组都 不同
 */
public class _886_PossibleBiPartition {

    Map<Integer, List<Integer>> graph = new HashMap<>();
    Set<Integer>[] sets = new HashSet[2];
    Set<Integer> visited = new HashSet<>();

    public boolean possibleBipartition(int n, int[][] dislikes) {


        if (n <= 2) {
            return true;
        }
        sets[0] = new HashSet<>();
        sets[1] = new HashSet<>();
        for (int[] pair : dislikes) {
            int from = pair[0], to = pair[1];
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
            graph.get(to).add(from);
        }
        for (int i = 1; i <= n; ++i) {
            if (!visited.contains(i) && !dfs(i, 0)) {
                return false;
            }
        }
        return true;
    }

    private boolean dfs(int i, int idx) {
        sets[idx].add(i);
        visited.add(i);
        int nidx = idx == 0 ? 1 : 0;
        if (graph.containsKey(i)) {
            for (int j: graph.get(i)) {
                if (visited.contains(j)) {
                    if (sets[idx].contains(j)) {
                        return false;
                    }
                } else if (!dfs(j, nidx)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        _886_PossibleBiPartition obj = new _886_PossibleBiPartition();
        obj.possibleBipartition(3, new int[][]{{1,2}, {1,3},{2,3}});
    }
}
