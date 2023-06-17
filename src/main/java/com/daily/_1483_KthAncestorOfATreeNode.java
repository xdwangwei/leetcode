package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/6/12 17:44
 * @description: _1483_KthAncestorOfATreeNode
 * 1483. 树节点的第 K 个祖先
 * 给你一棵树，树上有 n 个节点，按从 0 到 n-1 编号。树以父节点数组的形式给出，其中 parent[i] 是节点 i 的父节点。树的根节点是编号为 0 的节点。
 *
 * 树节点的第 k 个祖先节点是从该节点到根节点路径上的第 k 个节点。
 *
 * 实现 TreeAncestor 类：
 *
 * TreeAncestor（int n， int[] parent） 对树和父数组中的节点数初始化对象。
 * getKthAncestor(int node, int k) 返回节点 node 的第 k 个祖先节点。如果不存在这样的祖先节点，返回 -1 。
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：
 * ["TreeAncestor","getKthAncestor","getKthAncestor","getKthAncestor"]
 * [[7,[-1,0,0,1,1,2,2]],[3,1],[5,2],[6,3]]
 *
 * 输出：
 * [null,1,0,-1]
 *
 * 解释：
 * TreeAncestor treeAncestor = new TreeAncestor(7, [-1, 0, 0, 1, 1, 2, 2]);
 *
 * treeAncestor.getKthAncestor(3, 1);  // 返回 1 ，它是 3 的父节点
 * treeAncestor.getKthAncestor(5, 2);  // 返回 0 ，它是 5 的祖父节点
 * treeAncestor.getKthAncestor(6, 3);  // 返回 -1 因为不存在满足要求的祖先节点
 *
 *
 * 提示：
 *
 * 1 <= k <= n <= 5 * 104
 * parent[0] == -1 表示编号为 0 的节点是根节点。
 * 对于所有的 0 < i < n ，0 <= parent[i] < n 总成立
 * 0 <= node < n
 * 至多查询 5 * 104 次
 * 通过次数14,746提交次数32,844
 */
public class _1483_KthAncestorOfATreeNode {

    /**
     *
     * 方法：树上倍增
     *
     * 一、思考
     * 最暴力的做法是，从 node 出发，一步步地往上跳，即
     *
     * node → parent[node] → parent[parent[node]] → ⋯
     *
     * 需要跳 k 次才能到达 node 的第 k 个祖先节点，时间复杂度为 O(k)。
     *
     * 如何优化这个暴力算法呢？
     *
     * 一个初步的想法是，预处理出每个节点的「爷爷节点」，即父节点的父节点，那么就可以两步两步地往上跳，从而减少一半的跳跃次数（循环次数）。
     *
     * 进一步地，再预处理出爷爷节点的爷爷节点，就可以四步四步地往上跳。
     *
     * 请你思考：一般地，要预处理出哪些节点呢？如何利用这些预处理出的节点，快速地找到第 k 个祖先节点？
     *
     * 二、解惑
     *
     * 预处理出每个节点的第 个祖先节点，即第 2^i 个祖先节点，即 2,4,8,⋯ 个祖先节点（注意 x 的第 1 个祖先节点就是 parent[x]）。
     *
     * 由于任意 k 可以分解为若干不同的 2 的幂（例如 13=8+4+1），所以只需要预处理出这些 2^i 祖先节点，就可以快速地到达任意第 k 个祖先节点。
     *
     * 例如 k=13=8+4+1=1101(2)，可以先往上跳 8 步，再往上跳 4 步和 1 步；也可以先往上跳 1 步，再往上跳 4 步和 8 步。
     * 无论如何跳，都只需要跳 3 次就能到达第 13 个祖先节点。
     *
     * 据此，可以得到下面的算法。
     *
     * 三、算法
     * 在构造函数 TreeAncestor 中，预处理出每个节点 x 的第 2^i 个祖先节点，记作 pa[x][i]（若第 2^i 个祖先节点不存在则 pa[x][i]=−1）。
     *
     * 计算方式如下：
     *
     * - 先枚举 x，再枚举 i。相当于先算出所有节点的所有爷爷节点，再算出所有节点的爷爷的爷爷节点，依此类推。
     * - pa[x][0] = parent[x]，即父节点。
     * - pa[x][1] = pa[pa[x][0]][0]，即爷爷节点。
     * - 一般地，pa[x][j] = pa[pa[x][j-1]][j-1]。如果 pa[x][j-1]=−1 则 pa[x][j]=−1。
     *      即：要想找到节点 x的的第 2^i 个祖先节点，我们可以先找到节点 x 的第 2^(j-1) 个祖先节点，然后再找到该节点的第 2^(j-1)个祖先节点即可。
     *      所以，我们要找到每个节点的距离为 2^j 的祖先节点，直到达到树的最大高度。
     *      这里 j 至多为 ⌊log2 n⌋。例如 n=13 时，⌊log2 13⌋ = 3，至多需要预处理到第 3 个祖先节点。
     *      （当然，你也可以先把树高，或者每个节点的深度求出来，再据此做精细地计算。）
     * - 对于 getKthAncestor，需要找到 k 的二进制表示中的所有 1。
     *      可以从小到大枚举 i，
     *          如果 k 右移 i 位后的最低位为 1，就说明 k 的二进制从低到高第 i 位是 1，
     *          那么往上跳 2^i 步，将 node 更新为 pa[node][i]。
     *          如果 node=−1 则说明第 k 个祖先节点不存在。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/solution/mo-ban-jiang-jie-shu-shang-bei-zeng-suan-v3rw/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class TreeAncestor {

        private int[][] p;

        public TreeAncestor(int n, int[] parent) {
            // 树的高度（共n个节点 log2 n） <  二进制1的个数，也可以根据数据范围估算一个常数，或者直接 32
            int m = 32 - Integer.numberOfLeadingZeros(n);
            // 每个节点 x 的第 2^i 个祖先节点，记作 pa[x][i]
            p = new int[n][m];
            // 初始化，-1 代表不存在
            for (int[] e : p) {
                Arrays.fill(e, -1);
            }
            // base，节点的父节点 即 2^0，为 parent[x]
            for (int i = 0; i < n; ++i) {
                p[i][0] = parent[i];
            }
            // 递归，节点 i
            for (int i = 0; i < n; ++i) {
                // 第 2^j 个祖先，注意这里 j 从 1 开始
                for (int j = 1; j < m; ++j) {
                    // 注意提前跳过，避免下面越界
                    if (p[i][j - 1] == -1) {
                        continue;
                    }
                    // 递归 ，x 的第 2^j 个祖先节点 = x 的第 2^(j-1) 个祖先节点 的第 2^(j-1) 个祖先节点
                    p[i][j] = p[p[i][j - 1]][j - 1];
                }
            }
        }

        public int getKthAncestor(int node, int k) {
            // 任意 k 可以分解为若干不同的 2 的幂（例如 13=8+4+1），所以只需要预处理出这些 2^i 祖先节点，就可以快速地到达任意第 k 个祖先节点。
            // 从高到底或从低到高遍历k的二进制，最高位是树高m，即 p[0].length
            for(int i = p[0].length - 1; i >= 0; --i) {
                // 当前第i个二进制位为1
                if ((k >> i & 1) > 0) {
                    // 跳到第 2^i 个祖先节点
                    node = p[node][i];
                }
                // 节点不存在，直接退出
                if (node == -1) {
                    return -1;
                }
            }
            // 返回最终祖先节点
            return node;
        }
    }
}
