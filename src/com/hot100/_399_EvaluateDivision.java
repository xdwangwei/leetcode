package com.hot100;


import java.util.*;

/**
 * @author wangwei
 * @date 2022/4/27 11:38
 * @description: _399_EvaluateDivision
 *
 * 399. 除法求值
 * 给你一个变量对数组 equations 和一个实数值数组 values 作为已知条件，其中 equations[i] = [Ai, Bi] 和 values[i] 共同表示等式 Ai / Bi = values[i] 。每个 Ai 或 Bi 是一个表示单个变量的字符串。
 *
 * 另有一些以数组 queries 表示的问题，其中 queries[j] = [Cj, Dj] 表示第 j 个问题，请你根据已知条件找出 Cj / Dj = ? 的结果作为答案。
 *
 * 返回 所有问题的答案 。如果存在某个无法确定的答案，则用 -1.0 替代这个答案。如果问题中出现了给定的已知条件中没有出现的字符串，也需要用 -1.0 替代这个答案。
 *
 * 注意：输入总是有效的。你可以假设除法运算中不会出现除数为 0 的情况，且不存在任何矛盾的结果。
 *
 *
 *
 * 示例 1：
 *
 * 输入：equations = [["a","b"],["b","c"]], values = [2.0,3.0], queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
 * 输出：[6.00000,0.50000,-1.00000,1.00000,-1.00000]
 * 解释：
 * 条件：a / b = 2.0, b / c = 3.0
 * 问题：a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?
 * 结果：[6.0, 0.5, -1.0, 1.0, -1.0 ]
 * 示例 2：
 *
 * 输入：equations = [["a","b"],["b","c"],["bc","cd"]], values = [1.5,2.5,5.0], queries = [["a","c"],["c","b"],["bc","cd"],["cd","bc"]]
 * 输出：[3.75000,0.40000,5.00000,0.20000]
 * 示例 3：
 *
 * 输入：equations = [["a","b"]], values = [0.5], queries = [["a","b"],["b","a"],["a","c"],["x","y"]]
 * 输出：[0.50000,2.00000,-1.00000,-1.00000]
 *
 *
 * 提示：
 *
 * 1 <= equations.length <= 20
 * equations[i].length == 2
 * 1 <= Ai.length, Bi.length <= 5
 * values.length == equations.length
 * 0.0 < values[i] <= 20.0
 * 1 <= queries.length <= 20
 * queries[i].length == 2
 * 1 <= Cj.length, Dj.length <= 5
 * Ai, Bi, Cj, Dj 由小写英文字母与数字组成
 */
public class _399_EvaluateDivision {

    /**
     * 首先，根据已有等式，构造图，比如 a / b = 1, a /c = 2, a /d = 3, 那么 a 就有 三个邻接点，
     * 我们用 map 来保存点和其邻接点以及运算结果，也就是 map(a)=List((b,1), (c,2), (d,3)) 这种
     * 所以需要一个结构Node来保存 对于 a 的所有 除数 及 结果，
     *
     * 构造完毕后，如果要计算 a / x， 因为 a / x = a/b * b/c * c/d * ... ?/x
     * 那么就相当于 从 a 出发，找到一条直到 x 的路径，返回这条路径上的节点结果的积
     *
     * 至于这个过程，可以选择 dfs 或 bfs
     *
     * 问题：
     * 如果构造图是有向图，就是只保存 a / b, b/ c这种，那么最后
     *  如果 求 的 是  x / a，会发现 图中没有key为x的点，此时 不能简单的计算 a / x 的倒数
     *  因为 我们求解 a/x的过程是一个拆分后求路径的过程，拆分出的每一部分y/z可能又是原来图中存的z/y的反方向，这给我们带来麻烦
     *
     *  因此采用双向图，a/b=2,也能得到b/a=0.5，这样构建双向图即可
     *
     *  那么这种情况下，在dfs或bfs的过程中，就要考虑出现循环路径的情况，因此需要额外的空间set来剔除已经visited的搜索
     *
     *  我们的写法，dfs比较简单，因此搜索过程采用dfs
     *
     *  当然，完全可以利用map进行string和integer的映射，将每一个字符串对应1个整数，之后完全可以用二维数组构建图，并通过下表索引访问节点了
     */

    // 存储 某个 变量 ？ / var = value 这个式子的 除数 和 商
    class Node {
        String var;
        double value;

        Node(String var, double value) {
            this.var = var;
            this.value = value;
        }
    }


    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // 构建双向图，注意map的结构存的是对于a作为被除数时，所有的除数及对应的商
        Map<String, List<Node>> graph = new HashMap<>();
        double[] res = new double[queries.size()];
        for (int i = 0; i < equations.size(); ++i) {
            List<String> equation = equations.get(i);
            // 被除数
            String dividend = equation.get(0);
            // 除数
            String divisor = equation.get(1);
            // 先初始化，避免空指针
            if (!graph.containsKey(dividend)) {
                graph.put(dividend, new ArrayList<>());
            }
            if (!graph.containsKey(divisor)) {
                graph.put(divisor, new ArrayList<>());
            }
            // 双向图
            graph.get(dividend).add(new Node(divisor, values[i]));
            graph.get(divisor).add(new Node(dividend, 1.0 / values[i]));
        }
        // 遍历要求解的表达式
        for (int i = 0; i < queries.size(); ++i) {
            List<String> query = queries.get(i);
            // 被除数
            String dividend = query.get(0);
            // 除数
            String divisor = query.get(1);
            // 二者均为出现在已经表达式中，题目说明此时返回 -1
            if (!graph.containsKey(dividend) || !graph.containsKey(divisor)) {
                res[i] = -1.0;
            } else {
                // dfs求解这个式子的值，set用于在搜索过程中避免循环
                Set<String> visited = new HashSet<>();
                res[i] = dfs(graph, dividend, divisor, visited);
            }
        }
        return res;
    }

    /**
     * dfs
     * 通过拆分拼接路径的方式求解 x / y 的值
     * @param graph
     * @param dividend
     * @param divisor
     * @param visited
     * @return
     */
    private double dfs(Map<String, List<Node>> graph, String dividend, String divisor, Set<String> visited) {
        // 被除数和除数相等，
        if (dividend.equals(divisor)) {
            return 1.0;
        }
        // 标记
        visited.add(dividend);
        // 对于 被除数 的 所有邻接点（除数）
        for (Node node: graph.get(dividend)) {
            // 如果 是 目标除数，直接返回 对应结果
            if (divisor.equals(node.var)) {
                return node.value;
            } else {
                // 首先，这个邻接点要未访问过
                // 然后，将 x / y 转换为 x / z * z /y 递推到下一层，
                if (!visited.contains(node.var)) {
                    // 去求 z/y
                    double res = dfs(graph, node.var, divisor, visited);
                    // 子问题有解，当前问题才有解
                    if (res != -1.0) {
                        return node.value * res;
                    }
                }
            }
        }
        // 拆分失败，就是根据已知的表达式 无法计算 当前式子
        return -1.0;
    }


    /**
     * 方法二：Floyd 算法
     * 对于查询数量很多的情形，如果为每次查询都独立搜索一次，则效率会变低。为此，我们不妨对图先做一定的预处理，随后就可以在较短的时间内回答每个查询。
     *
     * 在本题中，我们可以使用 Floyd 算法，预先计算出任意两点之间的距离。
     *
     * C++JavaGolangJavaScript
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/evaluate-division/solution/chu-fa-qiu-zhi-by-leetcode-solution-8nxb/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class Solution2 {
        public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
            // 通过map对每个字符串做整数映射
            int nvars = 0;
            Map<String, Integer> variables = new HashMap<String, Integer>();
            int n = equations.size();
            for (int i = 0; i < n; i++) {
                if (!variables.containsKey(equations.get(i).get(0))) {
                    variables.put(equations.get(i).get(0), nvars++);
                }
                if (!variables.containsKey(equations.get(i).get(1))) {
                    variables.put(equations.get(i).get(1), nvars++);
                }
            }
            // 构建图
            double[][] graph = new double[nvars][nvars];
            for (int i = 0; i < nvars; i++) {
                Arrays.fill(graph[i], -1.0);
            }
            for (int i = 0; i < n; i++) {
                int va = variables.get(equations.get(i).get(0)), vb = variables.get(equations.get(i).get(1));
                graph[va][vb] = values[i];
                graph[vb][va] = 1.0 / values[i];
            }
            // floyd算法，计算所有节点之间的距离
            for (int k = 0; k < nvars; k++) {
                for (int i = 0; i < nvars; i++) {
                    for (int j = 0; j < nvars; j++) {
                        if (graph[i][k] > 0 && graph[k][j] > 0) {
                            graph[i][j] = graph[i][k] * graph[k][j];
                        }
                    }
                }
            }

            int queriesCount = queries.size();
            double[] ret = new double[queriesCount];
            for (int i = 0; i < queriesCount; i++) {
                List<String> query = queries.get(i);
                double result = -1.0;
                if (variables.containsKey(query.get(0)) && variables.containsKey(query.get(1))) {
                    int ia = variables.get(query.get(0)), ib = variables.get(query.get(1));
                    if (graph[ia][ib] > 0) {
                        result = graph[ia][ib];
                    }
                }
                ret[i] = result;
            }
            return ret;
        }
    }


    /**
     * 方法三：并查集，建议直接看官方题解
     *
     *
     * 道题是在「力扣」第 990 题（等式方程的可满足性）的基础上，在变量和变量之间有了倍数关系。
     * 由于 变量之间的【倍数关系】具有【传递性】，【处理有传递性关系的问题，可以使用「并查集」】，
     * 我们需要在并查集的「合并」与「查询」操作中 维护这些变量之间的倍数关系。
     *
     * 说明：请大家注意看一下题目中的「注意」和「数据范围」，例如：每个 Ai 或 Bi 是一个表示单个变量的字符串。
     * 所以用例 equation = ["ab", "cd"] ，这里的 ab 视为一个变量，不表示 a * b。
     * 如果面试中遇到这样的问题，一定要和面试官确认清楚题目的条件。
     * 还有 1 <= equations.length <= 20 和 values[i] > 0.0 可以避免一些特殊情况的讨论。
     *
     * 方法：并查集
     * 分析示例 1：
     *
     *      a / b = 2.0 说明 a = 2b， a 和 b 在同一个集合中；
     *
     *      b / c = 3.0 说明 b = 3c ，b 和 c 在同一个集合中。
     *
     * 求 a / c，可以把 a = 2b，b = 3c 依次代入，得到 a / c = 2b / c = 2 x 3c / c = 6.0
     * 求 b / a，很显然根据 a = 2，知道 b/a = 0.5 ，也可以把 b 和 a 都转换成为 c 的倍数，b/a = b/2b = 3c/6c = 0.5
     *
     * 我们计算了两个结果，不难知道：可以将题目给出的 equation 中的两个变量所在的集合进行「合并」，【同在一个集合中的两个变量就可以通过某种方式计算出它们的比值】。
     * 具体来说，可以把 不同的变量的比值转换成为相同的变量的比值，这样在做除法的时候就可以消去相同的变量，然后再计算转换成相同变量以后的系数的比值，就是题目要求的结果。
     * 【统一了比较的标准】，可以以 O(1) 的时间复杂度完成计算。
     *
     * 如果两个变量不在同一个集合中， 返回 -1.0。并且根据题目的意思，如果两个变量中 至少有一个 变量没有出现在所有 equations 出现的字符集合中，也返回 −1.0。
     *
     *
     * 构建有向图
     * 通过例 1 的分析，我们就知道了，题目给出的 equations 和 values 可以表示成一个图，equations 中出现的变量就是图的顶点，
     * 「分子」于「分母」的比值可以表示成一个有向关系（因为「分子」和「分母」是有序的，不可以对换），并且这个图是一个带权图，values 就是对应的有向边的权值。
     * 例 1 中给出的 equations 和 values 表示的「图形表示」、「数学表示」和「代码表示」如下表所示。
     * 其中 parent[a] = b 表示：结点 a 的（直接）父亲结点是 b，与之对应的有向边的权重，记为 weight[a] = 2.0，
     * 即 weight[a] 表示结点 a 到它的 【直接父亲结点】 的有向边的权重。
     *
     * 「统一变量」与「路径压缩」的关系
     *
     * 刚刚在分析例 1 的过程中，提到了：可以把一个一个 query 中的不同变量转换成 同一个变量，这样在计算 query 的时候就可以以 O(1) 的时间复杂度计算出结果，
     * 在「并查集」的一个优化技巧中，「路径压缩」就恰好符合了这样的应用场景。
     *
     * 为了避免并查集所表示的树形结构高度过高，影响查询性能。「路径压缩」就是针对树的高度的优化。
     * 「路径压缩」的效果是：在查询一个结点 a 的根结点同时，把结点 a 到根结点的沿途所有结点的父亲结点都指向根结点。
     * 如下图所示，除了根结点以外，所有的结点的父亲结点都指向了根结点。特别地，也可以认为根结点的父亲结点就是根结点自己。
     * 如下国所示：路径压缩前后，并查集所表示的两棵树形结构等价，路径压缩以后的树的高度为 22，查询性能最好。
     * 由于有「路径压缩」的优化，两个同在一个连通分量中的不同的变量，它们分别到根结点（父亲结点）的权值的比值，就是题目的要求的结果。
     *
     * 如何在「查询」操作的「路径压缩」优化中维护权值变化
     * 如下图所示，我们在结点 a 执行一次「查询」操作。路径压缩会先一层一层向上先找到根结点 d，然后依次把 c、b 、a 的父亲结点指向根结点 d。
     *
     * c 的父亲结点已经是根结点了，它的权值不用更改；
     * b 的父亲结点要修改成根结点，它的权值就是从当前结点到根结点经过的所有有向边的权值的乘积，因此是 3.03.0 乘以 4.04.0 也就是 12.012.0；
     * a 的父亲结点要修改成根结点，它的权值就是依然是从当前结点到根结点经过的所有有向边的权值的乘积，但是我们 没有必要把这三条有向边的权值乘起来，
     * 这是因为 b 到 c，c 到 d 这两条有向边的权值的乘积，我们在把 b 指向 d 的时候已经计算出来了。
     * 因此，a 到根结点的权值就等于 b 到根结点 d 的新的权值乘以 a 到 b 的原来的有向边的权值。
     *
     * 如何在「合并」操作中维护权值的变化
     * 「合并」操作基于这样一个 很重要的前提：我们将要合并的两棵树的高度最多为 22，换句话说两棵树都必需是「路径压缩」以后的效果，两棵树的叶子结点到根结点最多只需要经过一条有向边。
     *
     * 例如已知 a/b = 3.0，d/c = 4.0，又已知 a/d = 6.0
     *
     * 现在合并结点 a 和 d 所在的集合，其实就是把 a 的根结点 b 指向 d 的根结 c，那么如何计算 b 指向 c 的这条有向边的权重呢？
     *
     * 根据 a 经过 b 可以到达 c，a 经过 d 也可以到达 c，因此 【两条路径上的有向边的权值的乘积是一定相等的】。设 b 到 c 的权值为 x，那么 3.0 * x = 6.0 * 4.0，得 x=8.0。
     *
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/evaluate-division/solution/399-chu-fa-qiu-zhi-nan-du-zhong-deng-286-w45d/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public class Solution3 {

        public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
            HashMap<String, Integer> variablesMap = new HashMap<>();
            UnionFind uf = new UnionFind(2 * equations.size());
            int nodes = 0;
            for (int i = 0; i < equations.size(); ++i) {
                List<String> equation = equations.get(i);
                String dividend = equation.get(0);
                String divisor = equation.get(1);
                if (!variablesMap.containsKey(dividend)) {
                    variablesMap.put(dividend, nodes++);
                }
                if (!variablesMap.containsKey(divisor)) {
                    variablesMap.put(divisor, nodes++);
                }
                int p = variablesMap.get(dividend), q = variablesMap.get(divisor);
                uf.union(p, q, values[i]);
            }
            double[] res = new double[queries.size()];
            for (int i = 0; i < queries.size(); ++i) {
                List<String> equation = queries.get(i);
                String dividend = equation.get(0);
                String divisor = equation.get(1);
                Integer p = variablesMap.get(dividend), q = variablesMap.get(divisor);
                if (p == null || q == null) {
                    res[i] = -1.0;
                } else {
                    res[i] = uf.isConnected(p, q);
                }
            }
            return res;
        }


        // 带路径压缩的并查集
        private class UnionFind {

            // 记录连通分量个数，这里不需要
            // private int count;
            // 存储若干棵树
            private int[] parent;
            // 记录树的“重量”，每次联通都是 小树 往 大树上 挂
            private int[] size;

            // 每个【节点到父节点】的权重，也就是 原题目给的出发表达式的值代表的比例关系
            private double[] weights;

            public UnionFind(int n) {
                // count = n;
                parent = new int[n];
                size = new int[n];
                weights = new double[n];
                // 初始化
                for (int i = 0; i < n; i++) {
                    // 自己是自己的根
                    parent[i] = i;
                    size[i] = 1;
                    // 自己到自己的权重是1，自己/自己=1
                    weights[i] = 1.0;
                }
            }

            /* 将 p 和 q 连通 */
            public void union(int p, int q, double value) {
                int rootP = find(p);
                int rootQ = find(q);
                // 已连通
                if (rootP == rootQ)
                    return;

                // 小树接到大树下面，较平衡
                if (size[rootP] > size[rootQ]) {
                    parent[rootQ] = rootP;
                    size[rootP] += size[rootQ];
                    // 画个示意图，根据 同一个点 到 另一个点 的两条路径 的权重 应该一致，得到计算关系
                    weights[rootQ] = weights[p] / (value * weights[q]);
                } else {
                    parent[rootP] = rootQ;
                    size[rootQ] += size[rootP];
                    // 方向不同，计算顺序不同
                    weights[rootP] = (value * weights[q]) / weights[p];
                }
                // count--;
            }

            /* 判断 p 和 q 是否互相连通 */
            public double isConnected(int p, int q) {
                int rootP = find(p);
                int rootQ = find(q);
                // 处于同一棵树上的节点，相互连通，能用同一个变量表示，因此能计算比值，否则无法计算
                return rootP == rootQ ? weights[p] / weights[q] : -1.0;
            }

            /* 返回节点 x 的根节点 */
            private int find(int x) {
                while (parent[x] != x) {
                    // 进行路径压缩，先更新权重，再更换父节点
                    weights[x] *= weights[parent[x]];
                    parent[x] = parent[parent[x]];
                    x = parent[x];
                }
                return x;
            }

            // 当前并查集中连通分量个数
            // public int count() {
            //     return count;
            // }
        }
    }
}
