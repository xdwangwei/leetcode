package com.common;

/**
 * @author wangwei
 * 2022/4/8 13:35
 *
 * 线段树 不含区间修改版
 *
 * 对于各类「区间求和」问题，该用什么方式进行求解，之前在 这里 提到过。
 *
 * 此处可以再总结一下（加粗字体为最佳方案）：
 *
 * 数组不变，区间查询：【前缀和】、树状数组、线段树；
 * 数组单点修改，区间查询：【树状数组】、线段树；
 * 数组区间修改，单点查询：【差分】、线段树；
 * 数组区间修改，区间查询：【线段树】。
 *
 * 作者：AC_OIer
 * 链接：https://leetcode-cn.com/problems/corporate-flight-bookings/solution/gong-shui-san-xie-yi-ti-shuang-jie-chai-fm1ef/
 * 来源：力扣（LeetCode）
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class SegTree {

    /**
     * 线段树 segmentTree 是一个二叉树，每个结点保存数组 nums 在区间 [s, e] 的最小值、最大值或者总和等信息。
     * s = start, e = end，用于指定原数组区间左右端点，node 指定线段树数组下标
     * 所以记住，给定一个线段树数组索引，必然同时给定原数组一个区间[l,r],tree[x]=sum(nums[l,r])
     *
     * 线段树可以用树也可以用数组（堆式存储）来实现。
     * 对于数组实现，假设根结点的下标为 0，如果一个结点在数组的下标为 node，那么它的左子结点下标为 node×2+1，右子结点下标为 node×2+2。
     *
     * 1.建树 build 函数
     *
     *      我们在结点 node 保存数组 nums 在区间 [s, e]的总和。
     *
     *          s=e 时，结点 node 是叶子结点，它保存的值等于 nums[s]。
     *
     *          s<e 时，结点 node 的左子结点保存区间 [s,(s+e)/2] 的总和，右子结点保存区间 [(s+e)/2+1, e]的总和，那么结点 \textit{node}node 保存的值等于它的两个子结点保存的值之和。
     *
     *      假设 nums 的大小为 n，我们规定根结点 node=0 保存区间 [0,n−1] 的总和，然后自下而上递归地建树。
     *
     * 2.单点修改 change 函数
     *
     *      当我们要修改 nums[index] 的值时，我们找到对应区间 [index,index] 的线段树叶子结点，直接修改叶子结点的值为 val，并自下而上递归地更新父结点的值。
     *
     * 3.范围求和 range 函数
     *
     *      给定区间 [left,right] 时，我们将区间 [left,right] 拆成多个结点对应的区间。
     *
     *          如果结点 node 对应的区间与 [left,right] 相同，可以直接返回该结点的值，即当前区间和。
     *
     *          如果结点 node 对应的区间与 [left,right] 不同，设左子结点对应的区间的右端点为 m，那么将区间 [left,right] 沿点 m 拆成两个区间，分别计算左子结点和右子结点。
     *
     *      我们从根结点开始递归地拆分区间 [left,right]。
     * 4.范围修改 TODO
     *
     * 【凡是出现tree数索引node的同时，一定要出现其对应的原数组区间[s,e]】
     *
     * 时间复杂度：
     *
     * 构造函数：O(n)，其中 n 是数组 nums 的大小。
     *      二叉树的高度不超过 ⌈logn⌉+1，那么 segmentTree 的大小不超过 2 ^ (⌈logn⌉+1) <= 4n
     *      所以 build 的时间复杂度为 O(n)。
     *      并且通常将线段树数组的大小设置为原数组4倍
     *
     * update 函数：O(logn)。因为树的高度不超过 ⌈logn⌉+1，所以涉及更新的结点数不超过 ⌈logn⌉+1。
     *
     * sumRange 函数：O(logn)。每层结点最多访问四个，总共访问的结点数不超过 4×(⌈logn⌉+1)。
     *
     * 空间复杂度：O(n)。
     * 保存 segmentTree 需要 O(n) 的空间。
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/range-sum-query-mutable/solution/qu-yu-he-jian-suo-shu-zu-ke-xiu-gai-by-l-76xj/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    // 线段树数组，大小初始化为原数组大小的4倍
    private int[] segmentTree;
    // 原数组大小
    private int n;

    public SegTree(int[] nums) {
        n = nums.length;
        segmentTree = new int[4 * n];
        build(0, 0, n - 1, nums);
    }

    /**
     * 类似，归并思想，比如 node 负责 [s,e]
     * node的左子树是2node+1,右子树是2node+2，那么左子树负责[s,mid],右子树负责[mid+1,e]，其中mid=(s+e)/2
     * 构建线段树，tree[node]=sum(nums[s,e])
     * @param node node代表的子树根节点保存的是原数组[l,r]范围的和
     * @param s 原数组左边界
     * @param e 原数组右边界
     *          前三个参数一定同时出现，并且一个node对应的s和e是确定的
     * @param nums
     */
    private void build(int node, int s, int e, int[] nums) {
        // 当前子树(根)维护一个单节点
        if (s == e) {
            segmentTree[node] = nums[s];
            return;
        }
        // 分成左右两部分
        int mid = s + (e - s) / 2;
        // 左右节点
        int leftNode = 2 * node + 1;
        int rightNode = 2 * node + 2;
        // 构造左子树，维护 [s,m]
        build(leftNode, s, mid, nums);
        // 构造右子树，维护 [m+1,e]
        build(rightNode, mid + 1, e, nums);
        // 左右子树根节点分别保存两部分的和，相加就是 父根节点
        segmentTree[node] = segmentTree[leftNode] + segmentTree[rightNode];
    }

    /**
     * 修改原数组指定节点的值
     * 这个函数调用时，前四个参数一定是 0,0,n-1，一定是从根节点开始往下找
     * @param node 线段树当前树根
     * @param s    对应的原数组区间左端点
     * @param e    对应原数组区间右端点
     *      前三个参数一定同时出现，并且一个node对应的s和e是确定的
     * @param idx 修改原数组idx位置元素值为val
     * @param val
     */
    public void change(int node, int s, int e, int idx, int val) {
        // 这个子树维度的就是单个原数组元素,那么只需要修改自己
        // 这里，肯定是 s==e==idx的
        // 另外为什么这里改完自己之后不用往上迭代改祖辈节点
        // 因为此函数调用时是从根开始的的，根的覆盖范围是原数组全部
        // 那么必然会走之后的流程(除非一共就一个节点，那此时也没必要往上更新了)
        // 那么在某次迭代后走到出口(叶子节点)，根据最后一行逻辑，自然会在递归一次次返回的过程中把祖辈更新
        if (s == e) {
            segmentTree[node] = val;
            return;
        }
        // 判断要修改的这个节点由左子树还是右子树维护
        // 分成左右两部分
        int mid = s + (e - s) / 2;
        // 左右子树节点
        int leftNode = 2 * node + 1, rightNode = 2 * node + 2;
        // 由左子树维护
        if (idx <= mid) {
            change(leftNode, s, mid, idx, val);
        } else {
            change(rightNode, mid + 1, e, idx, val);
        }
        // 不管左子树还是右子树变了，当前根也要跟着变
        segmentTree[node] = segmentTree[leftNode] + segmentTree[rightNode];
    }

    /**
     * 返回原数组[left,right]范围元素和
     * 这个函数调用时，前四个参数一定是 0,0,n-1，一定是从根节点开始往下找
     * @param node
     * @param s
     * @param e
     *      前三个参数一定同时出现，并且一个node对应的s和e是确定的
     * @param left
     * @param right
     * @return
     */
    public int range(int node, int s, int e, int left, int right) {
        // 当前节点的覆盖范围是要求的区间的一部分，当前部分可以直接返回，不用继续递归
        // 因为此函数调用从根节点开始，所以当 此节点的覆盖范围并未完全填满整个要求的空间时，其余部分肯定再某个递归中也已计算出
        // 最后返回的是各部分的和
        if (left <= s && e <= right) {
            return segmentTree[node];
        }
        // 分成左右两部分
        int mid = s + (e - s) / 2;
        // 左右子树节点
        int leftNode = 2 * node + 1, rightNode = 2 * node + 2;
        // 要求解的区间在左子树覆盖范围
        if (right <= mid) {
            return range(leftNode, s, mid, left, right);
        } else if (left > mid) {
            // 要求解的区间在右子树覆盖范围
            return range(rightNode, mid + 1, e, left, right);
        } else {
            // 交叉，那么 左子树覆盖[s,m]，去计算[left,m]部分
            //           右子树覆盖[m+1,e],去计算[m+1,right]部分
            // 注意这里的传参，左右节点和其负责的区间范围这三个参数是唯一对应的，别写错了
            return range(leftNode, s, mid, left, mid) +
                    range(rightNode, mid + 1, e, mid + 1, right);
        }
    }
}
