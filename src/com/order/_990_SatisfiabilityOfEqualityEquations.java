package com.order;

import com.common.UnionFind;

/**
 * @author wangwei
 * 2021/11/16 10:35
 *
 * 给定一个由表示变量之间关系的字符串方程组成的数组，每个字符串方程 equations[i] 的长度为 4，并采用两种不同的形式之一："a==b" 或 "a!=b"。在这里，a 和 b 是小写字母（不一定不同），表示单字母变量名。
 *
 * 只有当可以将整数分配给变量名，以便满足所有给定的方程时才返回 true，否则返回 false。 
 *
 *  
 *
 * 示例 1：
 *
 * 输入：["a==b","b!=a"]
 * 输出：false
 * 解释：如果我们指定，a = 1 且 b = 1，那么可以满足第一个方程，但无法满足第二个方程。没有办法分配变量同时满足这两个方程。
 * 示例 2：
 *
 * 输入：["b==a","a==b"]
 * 输出：true
 * 解释：我们可以指定 a = 1 且 b = 1 以满足满足这两个方程。
 * 示例 3：
 *
 * 输入：["a==b","b==c","a==c"]
 * 输出：true
 * 示例 4：
 *
 * 输入：["a==b","b!=c","c==a"]
 * 输出：false
 * 示例 5：
 *
 * 输入：["c==c","b==d","x!=z"]
 * 输出：true
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/satisfiability-of-equality-equations
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _990_SatisfiabilityOfEqualityEquations {

    /**
     * 并查集
     *
     * 动态连通性其实就是一种等价关系，具有「自反性」「传递性」和「对称性」，其实 == 关系也是一种等价关系，具有这些性质。
     * 所以这个问题用 Union-Find 算法就很自然。
     *
     * 将 equations 中的算式根据 == 和 != 分成两部分，先处理 == 算式，使得他们通过相等关系各自勾结成门派（连通分量）；
     * 然后处理 != 算式，检查不等关系是否破坏了相等关系的连通性。
     * @param equations
     * @return
     */
    public boolean equationsPossible(String[] equations) {
        // 创建并查集
        UnionFind uf = new UnionFind(26);
        // 遇到等式就把二者连通
        for (String equation : equations) {
            if (equation.charAt(1) == '=') {
                int x = equation.charAt(0) - 'a';
                int y = equation.charAt(3) - 'a';
                uf.union(x, y);
            }
        }
        // 再处理所有 != ， 如果二者是连通的，那肯定不成立
        for (String equation : equations) {
            if (equation.charAt(1) == '!') {
                int x = equation.charAt(0) - 'a';
                int y = equation.charAt(3) - 'a';
                if (uf.connected(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
