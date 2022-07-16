package com.common;

/**
 * @author wangwei
 * @date 2022/4/4 9:55
 *
 * 树状数组 模板
 *
 * 小知识：对于 x ， x 的二进制表示中 只保留最后一个1 得到的结果是 lowbit(x) = x & -x
 *        比如 6 = 0110， -6 = 1010， 6 & -6 = 0010 = 4 ，只保留了 最后一个1
 *
 * 前缀和数组能够快速得到 区间和，也能快速完成 [l,r] + u 的操作，但是若只是对单个点进行修改，那么会涉及整个前缀和数组的修改，时间复杂度 O(N)
 * 因此对于 多次进行某个点的修改以及某个区间和的查询 这种题目，前缀和数组最坏情况下时间复杂度为 O(N^2) N次操作(修改单个点)，每次 O(N)
 *
 * 而树状数组能够做到在 O(logN)时间复杂度内完成这些操作，它是一个树形结构，类似于二叉堆，也是用数组形式维护父子关系，【下标从1开始】。
 * 在原数组基础上，每个节点保存一部分前缀和，根节点就是全部和
 * 具体结构参考 TreeArray.jpg
 *   https://www.bilibili.com/video/BV1pE41197Qj?spm_id_from=333.337.search-card.all.click
 *
 * 在树状数组tree[]中，每个节点覆盖到的原数组元素个数正好是 lowbit(x)，那么 tree[x] = sum(arr[j, x]) 其中 j = x-lowbit(x)+1, j 到 x 刚好 lowbit(x) 个元素
 * 而每个节点的父节点为：x + lowbit(x)，每个节点的左上节点为 x - lowbit(x)
 * 整棵树的深度为 logN+1
 *
 * 那么如果修改原数组某个元素arr[x]，给他加上值 k，那么 从 tree[x]到根节点tree[N]的整个路径上的元素都是加k，
 *      for (; x <= N; x += lowbit(x)) tree[x] += k;
 *
 * 对于查询某个节点的前缀和tree[x] = sum(arr[0...x])，从树状数组的结构来看，需要 从 x节点开始找到它上左上节点，一直找到第一个节点tree[1],ans进行累加，即
 *      for (; x >= 1; x -= lowbit(x)) ans += tree[x];
 *
 * 那么 查询区间和 就是  ask(r + 1) - ask(l)，查询单点就是 ask(x),即原数组 arr[0...x]
 *
 * 相当于 前缀和数组是以 线性表 结构存储前缀和
 * 而  树状数组 是以 树形结构 动态存储前缀和
 *
 * 以上 是 树状数组 对于 ①[区间查询，单点修改] 的应用。
 *      它也可以用于    ②[区间修改，单点查询]
 *             此时需要先得到原数组arr的差分数组diff，再以树状数组tree去维护差分数组diff的前缀和。就将问题转换成了 ①
 *                  add(l, u); add(r+1, -u); ask(x)
 *             具体参考 https://www.cnblogs.com/dilthey/p/9366491.html
 *         也可用于     ③[区间修改，区间查询]
 *
 *
 * 针对不同的题目，我们有不同的方案可以选择（假设我们有一个数组）：
 *
 *      数组不变，求区间和：「前缀和」、「树状数组」、「线段树」
 *      多次修改某个数，求区间和：「树状数组」、「线段树」
 *      多次整体修改某个区间，求区间和：「线段树」、「树状数组」（看修改区间的数据范围）
 *      多次将某个区间变成同一个数，求区间和：「线段树」、「树状数组」（看修改区间的数据范围）
 *
 * 这样看来，「线段树」能解决的问题是最多的，那我们是不是无论什么情况都写「线段树」呢？
 * 答案并不是，而且恰好相反，只有在我们遇到第 4 类问题，不得不写「线段树」的时候，我们才考虑线段树。
 * 因为「线段树」代码很长，而且常数很大，实际表现不算很好。我们只有在不得不用的时候才考虑「线段树」。
 * 总结一下，我们应该按这样的优先级进行考虑：
 *
 *      简单求区间和，用「前缀和」
 *      多次将某个区间变成同一个数，用「线段树」
 *      其他情况，用「树状数组」
 *
 * 作者：AC_OIer
 * 链接：https://leetcode-cn.com/problems/range-sum-query-mutable/solution/guan-yu-ge-lei-qu-jian-he-wen-ti-ru-he-x-41hv/
 * 来源：力扣（LeetCode）
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 *
 *
 */
public class TreeArray {

    private int[] tree;
    private int n;

    /**
     * 由nums构造树状数组，动态维护其前缀和
     * @param nums
     */
    public TreeArray(int[] nums) {
        n = nums.length;
        // tree[] 下标从1开始，到n结束
        tree = new int[n + 1];
        // 初始化构造树状数组tree
        for (int i = 0; i < n; ++i) {
            add(i + 1, nums[i]);
        }
    }

    /**
     * 得到 x 只保留二进制位中 最后一个1 得到的结果
     * @param x
     * @return
     */
    private int lowbit(int x) {
        return x & -x;
    }

    /**
     * 给原数组 索引为 x 的位置 增加 增量k
     * @param x
     * @param k
     */
    private void add(int x, int k) {
        // 在 tree[] 中， x 的父节点是 x + lowbit(x)
        for(; x <= n; x += lowbit(x)) {
            tree[x] += k;
        }
    }

    /**
     * 单点查询，得到 原数组 arr[0...x] 的和
     * @param x
     * @return
     */
    private int ask(int x) {
        int ans = 0;
        // 在 tree[] 中， x 的左上点是 x - lowbit(x)
        for(; x >= 1; x -= lowbit(x)) {
            ans += tree[x];
        }
        return ans;
    }

    /**
     * 区间查询，得到原数组 arr[l,...,r] 的和
     * @param l
     * @param r
     * @return
     */
    private int intervalSum(int l, int r) {
        return ask(r + 1) - ask(l);
    }
}
