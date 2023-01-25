package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2023/1/25 18:52
 * @description: _1632_RankTransformOfAMatrix
 *
 * 1632. 矩阵转换后的秩
 * 给你一个 m x n 的矩阵 matrix ，请你返回一个新的矩阵 answer ，其中 answer[row][col] 是 matrix[row][col] 的秩。
 *
 * 每个元素的 秩 是一个整数，表示这个元素相对于其他元素的大小关系，它按照如下规则计算：
 *
 * 秩是从 1 开始的一个整数。
 * 如果两个元素 p 和 q 在 同一行 或者 同一列 ，那么：
 * 如果 p < q ，那么 rank(p) < rank(q)
 * 如果 p == q ，那么 rank(p) == rank(q)
 * 如果 p > q ，那么 rank(p) > rank(q)
 * 秩 需要越 小 越好。
 * 题目保证按照上面规则 answer 数组是唯一的。
 *
 *
 *
 * 示例 1：
 *
 *
 * 输入：matrix = [[1,2],[3,4]]
 * 输出：[[1,2],[2,3]]
 * 解释：
 * matrix[0][0] 的秩为 1 ，因为它是所在行和列的最小整数。
 * matrix[0][1] 的秩为 2 ，因为 matrix[0][1] > matrix[0][0] 且 matrix[0][0] 的秩为 1 。
 * matrix[1][0] 的秩为 2 ，因为 matrix[1][0] > matrix[0][0] 且 matrix[0][0] 的秩为 1 。
 * matrix[1][1] 的秩为 3 ，因为 matrix[1][1] > matrix[0][1]， matrix[1][1] > matrix[1][0] 且 matrix[0][1] 和 matrix[1][0] 的秩都为 2 。
 * 示例 2：
 *
 *
 * 输入：matrix = [[7,7],[7,7]]
 * 输出：[[1,1],[1,1]]
 * 示例 3：
 *
 *
 * 输入：matrix = [[20,-21,14],[-19,4,19],[22,-47,24],[-19,4,19]]
 * 输出：[[4,2,3],[1,3,4],[5,1,6],[1,3,4]]
 * 示例 4：
 *
 *
 * 输入：matrix = [[7,3,6],[1,4,5],[9,8,2]]
 * 输出：[[5,1,4],[1,2,3],[6,3,1]]
 *
 *
 * 提示：
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 500
 * -109 <= matrix[row][col] <= 109
 * 通过次数6,410提交次数12,214
 */
public class _1632_RankTransformOfAMatrix {

    /**
     * 方法一：并查集 + 拓扑排序
     *
     * 思路
     *
     * 根据题意，同一行的元素中，相同的元素秩相同；同一列的元素中，相同的元素秩相同。
     * 可以根据这两条性质，利用并查集将同行、同列中相同的元素的不同索引位置进行合并操作。
     * 合并之后，只需求出某个集合里一个元素（索引）的秩，即可将其赋给这个集合里所有元素的秩。
     * 因为每个集合中所有元素的根是统一的，因此对于每个集合，我们计算集合根（索引）的秩。
     *
     * 合并相同元素不同下标时，只需将每行（每列）按照元素的值进行分类，用哈希表暂时保存，
     * 键值对分别是元素值和多个下标，然后将多个下标通过并查集进行合并。
     * 下标数组长度为 k 时，仅需进行 (k−1) 次合并，即对下标数组中 (k−1) 个相邻的下标对进行合并即可
     *
     * 按照各行中相同的元素和各列中相同的元素进行合并后，需要求出各个集合（代表元素（下标））的秩。
     * 根据题目的条件，每行（或每列）中，元素大的秩大，元素小的秩小。
     * 题目又要求秩尽可能小。
     * 因此，需要先求出值小的集合的秩，并尽可能赋一个小的秩（1），再根据大小关系去求值大的集合的秩。
     *
     * 求秩的顺序可以按照【拓扑排序】，将同行同列的大小关系用单向边表示，值小的集合（代表）会有一条单向边指向值大的集合（代表）。
     * 建图时，对每行每列的元素进行去重排序，多个相同元素只选择一个，因为在一个集合中，根是一样的，按照元素值排序
     * 如果有 k 个不重复元素，只需建立 (k−1) 条单向边，即可表示出大小关系。
     * 图中的点为元素下标，而不是元素本身
     *
     * 初始化所有集合（代表元素）的秩为1，开始排序
     * 将所有入度为 0 的集合（只取代表元素（根==自己）下标，点）加入队列 q
     * while (q非空)：
     *      int curIdx = q.poll()
     *      for (int neighbor : curIdx的所有邻接点（出度方向点集合）)
     *          更新 neighbor 的秩为指向该集合的所有集合的秩的最大值加 1（max(r(neighbor, r(curIdx)+1))），
     *          既满足大小关系，又满足秩需要尽可能小。
     * 这样下来，可以保证拓扑排序一定有通路，所有集合的秩都可以计算出来。
     *
     * 最后每个元素的秩即为它所在的集合的秩。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/rank-transform-of-a-matrix/solution/ju-zhen-zhuan-huan-hou-de-zhi-by-leetcod-biw0/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param matrix
     * @return
     */
    public int[][] matrixRankTransform(int[][] matrix) {
        int n = matrix.length, m = matrix[0].length;
        // 二维数组元素，下标转换为一维数组，用并查集保存下标
        UF uf = new UF(n * m);
        // 合并每一行 每个元素的多个出现位置
        for (int i = 0; i < n; ++i) {
            // 记录每个元素多个出现位置
            Map<Integer, List<Integer>> num2idxs = new HashMap<>();
            for (int j = 0; j < m; ++j) {
                num2idxs.putIfAbsent(matrix[i][j], new ArrayList<>());
                num2idxs.get(matrix[i][j]).add(i * m + j);
            }
            // 对于每一个元素的多个出现位置
            for (List<Integer> idxs : num2idxs.values()) {
                // 通过并查集合并
                for (int k = 1; k < idxs.size(); ++k) {
                    uf.union(idxs.get(k - 1), idxs.get(k));
                }
            }
        }
        // 合并每一列 每个元素的多个出现位置
        for (int j = 0; j < m; ++j) {
            // 记录每个元素多个出现位置
            Map<Integer, List<Integer>> num2idxs = new HashMap<>();
            for (int i = 0; i < n; ++i) {
                num2idxs.putIfAbsent(matrix[i][j], new ArrayList<>());
                num2idxs.get(matrix[i][j]).add(i * m + j);
            }
            // 对于每一个元素的多个出现位置
            for (List<Integer> idxs : num2idxs.values()) {
                // 通过并查集合并
                for (int k = 1; k < idxs.size(); ++k) {
                    uf.union(idxs.get(k - 1), idxs.get(k));
                }
            }
        }
        // 下面计算每个集合的秩
        // 在此之前，建立每个集合代表元素间的拓扑图
        // 每个集合取代表元素（根==自己）
        // 计算每个集合（代表元素）的入度
        int[] degrees = new int[n * m];
        // 保存图
        List<Integer>[] adj = new ArrayList[n * m];
        // 同一行，不同元素间，按照大小关系，建立指向（小->大）
        for (int i = 0; i < n; ++i) {
            Map<Integer, Integer> num2Idx = new HashMap<>();
            // 遍历当前行元素，保存 (元素，索引)
            for (int j = 0; j < m; ++j) {
                num2Idx.put(matrix[i][j], i * m + j);
            }
            // 按照元素值排序
            List<Integer> nums = new ArrayList<>(num2Idx.keySet());
            Collections.sort(nums);
            // 给这些元素对应的索引建立指向关系
            for (int k = 1; k < nums.size(); k++) {
                int prev = num2Idx.get(nums.get(k - 1)), cur = num2Idx.get(nums.get(k));
                // 注意，这里要找到它所属集合的根
                prev = uf.find(prev);
                cur = uf.find(cur);
                if (adj[prev] == null) {
                    adj[prev] = new ArrayList<>();
                }
                // 更新prev邻接表
                adj[prev].add(cur);
                // 更新cur的入度
                degrees[cur]++;
            }
        }
        // 相同操作，只是 操作行 改为 操作列
        // 同一列，不同元素间，按照大小关系，建立指向（小->大）
        // 给这些元素对应的索引建立指向关系
        // 注意，这里要找到它所属集合的根
        for (int j = 0; j < m; ++j) {
            Map<Integer, Integer> num2Idx = new HashMap<>();
            for (int i = 0; i < n; ++i) {
                num2Idx.put(matrix[i][j], i * m + j);
            }
            List<Integer> nums = new ArrayList<>(num2Idx.keySet());
            Collections.sort(nums);
            for (int k = 1; k < nums.size(); k++) {
                int prev = num2Idx.get(nums.get(k - 1)), cur = num2Idx.get(nums.get(k));
                prev = uf.find(prev);
                cur = uf.find(cur);
                if (adj[prev] == null) {
                    adj[prev] = new ArrayList<>();
                }
                adj[prev].add(cur);
                degrees[cur]++;
            }
        }

        // 拓扑排序
        // 计算集合代表元素的秩
        int[] rank = new int[n * m];
        // 初始化所有元素秩为1
        Arrays.fill(rank, 1);
        // 准备队列
        Queue<Integer> queue = new ArrayDeque<>();
        // 遍历所有元素
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                int idx = i * m + j;
                // 找到每个集合的代表元素(根 == 自己)，并且入度为0的那些元素
                if (idx == uf.find(idx) && degrees[idx] == 0) {
                    // 加入队列
                    queue.offer(idx);
                }
            }
        }
        // 循环
        while (!queue.isEmpty()) {
            // 出队
            Integer curIdx = queue.poll();
            // 没有邻接点，跳过
            if (adj[curIdx] == null) {
                continue;
            }
            // 对于它的所有邻接点
            for (Integer neig : adj[curIdx]) {
                // 更新其秩为 本身 与 当前点秩+1 二者间的更大值
                rank[neig] = Math.max(rank[neig], rank[curIdx] + 1);
                // 更新邻接点的入度减一，如果入度为0了，也需要入队
                if (--degrees[neig] == 0) {
                    queue.offer(neig);
                }
            }
        }

        // 每个集合的代表元素的秩已经计算出来
        // 然后将每个元素的秩设置为其所在集合代表元素的秩即可
        int[][] ans = new int[n][m];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                // 设置其秩与其所在集合代表元素秩一样
                ans[i][j] = rank[uf.find(i * m + j)];
            }
        }
        // 返回
        return ans;
    }


    /**
     * 并查集
     */
    class UF {
        int n;
        // 每个点对应根节点数组
        int[] fa;
        // 每个树根代表子树的节点个数
        int[] sz;
        // 初始化
        UF(int n) {
            this.n = n;
            this.fa = new int[n];
            this.sz = new int[n];
            for (int i = 0; i < n; ++i) {
                // 根为自己
                fa[i] = i;
                // 大小为1
                sz[i] = 1;
            }
        }
        // 联通两个点
        public void union(int p, int q) {
            // 找到两个点的根
            int rp = find(p), rq = find(q);
            // 已联通
            if (rp == rq) {
                return;
            }
            // 将节点少的树根挂到节点多的树根上
            // 更改小树根的树根为大树根
            // 大树根数节点数增加
            if (sz[rp] >= sz[rq]) {
                fa[rq] = rp;
                sz[rp] += sz[rq];
            } else {
                fa[rp] = rq;
                sz[rq] += sz[rp];
            }
        }

        // 寻找节点p的树根。顺便路径压缩
        public int find(int p) {
            while (p != fa[p]) {
                fa[p] = fa[fa[p]];
                p = fa[p];
            }
            return p;
        }
    }
}
