package com.common;

/**
 * @author wangwei
 * @date 2022/5/27 22:20
 * @description: SegmentTree
 *
 * 线段树 建树，区间查询，区间修改，懒标记
 * 所谓区间修改lazy标记，是指当 当前点所管理区间 完全包含于 目标区间之内 时，直接更新当前点的值，并打上lazy标记，并不继续向下递归更新底层节点
 * 因为没必要，我已经知道 [1,5]的区间和了，我记录这个区间每个元素值都加1，那就是node.val+=1*5,node.lazy=5，我做好记录就行，我不需要再去更新[1-3]和[4-5]啊
 * 因为我已经知道[1-5]了，所以不用继续向下递归
 *
 * 若没有完全覆盖呢？说明此时我可能得分两部分去更新，那么这个时候，因为从自身节点无法得到答案，必须要去访问子区间的时候，我得先把自己的lazy传递下去，
 * 也就是通知左右区间，上一次有个更新任务我并没有发给你们，你们得先把之前的变更处理完，不然的话，子区间更新完也只是进行了当前更新，之前的变化它不知道啊
 * 所以，这里要进行lazy的下方，左右区间更新完毕后，再回馈更新当前区间值，node.val = node.left.val + node.right.val
 *
 * 同样，因为加入了lazy标记，在查询某个区间和的时候，当 当前点所管理区间 完全包含于 目标区间之内 时，直接返回当前点的值，
 * 但是如果不完全包含，要分开求和，也就是要分别访问左右子区间时，那也得先将当前节点的lazy下方，保证左右子区间先处理完之前的变更
z*/
public class SegmentTree {


    // 原数组，下表统一从1开始
    private int[] nums;


    // 线段树数组，下表统一从1开始
    private TreeNode[] tree;


    // 线段树节点
    class TreeNode {
        // 所管理区间
        // l nums中的起点
        // r nums中的终点
        // val 区间和
        // lazy标记
        int s, e, val, lazy;

        TreeNode(int s, int e) {
            this.s = s;
            this.e = e;
        }
    }


    // 构造函数
    SegmentTree(int[] arr) {
        int n = arr.length;
        nums = new int[n + 1];
        // 转移并存储
        for (int i = 1; i <= n; ++i) {
            nums[i] = arr[i - 1];
        }
        // 初始化线段树数组空间
        tree = new TreeNode[4 * n];
        // 构造线段树，从根节点开始，根节点编号1，负责整个nums区间
        buildTree(1, 1, n);
    }


    /**
     * 递归构造线段树，从根节点开始，根节点编号1，负责整个nums区间
     * @param node
     * @param l
     * @param r
     */
    private void buildTree(int node, int l, int r) {
        // 先创建对应节点，设置左右区间
        tree[node] = new TreeNode(l, r);
        if (l == r) {
            // 如果时叶子节点
            tree[node].val = nums[l];
            // 返回
            return;
        }
        int m = (l + r) >> 1;
        // 递归构造左右子区间
        buildTree(node << 1, l, m);
        buildTree(node << 1 | 1, m + 1, r);
        // 回馈当前节点
        tree[node].val = tree[node << 1].val + tree[node << 1 | 1].val;
    }


    /**
     * 范围查询
     * @param node
     * @param l
     * @param r
     * @return
     */
    private int queryRange(int node, int l, int r) {
        // 如果 当前点所管理区间 完全包含于 目标区间之内，当前部分区间和直接返回，不需要递归子区间求和
        if (l <= tree[node].s && tree[node].e <= r) {
            return tree[node].val;
        }
        // 必须分段统计时，需要先下方lazy标记
        pushdown(node);
        // 分段统计
        int sum = 0;
        // 如果目标区间和 左子区间有交集，注意判断条件写法和递归传参
        if (tree[node << 1].e >= l) {
            sum += queryRange(node << 1, l, r);
        }
        // 如果目标区间和 右子区间有交集，注意判断条件写法和递归传参
        if (tree[node << 1 | 1].s <= r) {
            sum += queryRange(node << 1 | 1, l , r);
        }
        // 返回累加结果
        return sum;
    }


    /**
     * 区间更新
     * @param node
     * @param l
     * @param r
     * @param delta 这个是增量，不是直接修改
     */
    private void updateRange(int node, int l, int r, int delta) {
        // 如果 当前点所管理区间 完全包含于 目标区间之内，当前部分区间和直接更新，打上lazy标记，不需要递归子区间更新
        if (l <= tree[node].s && tree[node].e <= r) {
            // 当前区间，每个元素都要增加delta，区间和累加 区间长度*delta
            tree[node].val += (tree[node].e - tree[node].s + 1) * delta;
            // 打上lazy标记，标记当前区间元素值并未更新
            tree[node].lazy += delta;
            return;
        }
        // 如果不是全包含，必须分区间处理，必须先下方lazy，保证左右区间先把之前的修改处理完
        pushdown(node);
        // 如果目标区间和 左子区间有交集，注意判断条件写法和递归传参
        if (tree[node << 1].e >= l) {
            updateRange(node << 1, l, r, delta);
        }
        // 如果目标区间和 左子区间有交集，注意判断条件写法和递归传参
        if (tree[node << 1 | 1].s <= r) {
            updateRange(node << 1 | 1, l, r, delta);
        }
        // 左右子区间修改完后回馈当前节点
        tree[node].val = tree[node << 1].val + tree[node << 1 | 1].val;
    }


    /**
     * lazy下放
     * 所谓lazy下放就是通知左右子区间区间和变更，下放也只是下放一层，所以左右子区间继承这个lazy
     * 然后清除当前节点的lazy即可
     * @param node
     */
    private void pushdown(int node) {
        int lazy = tree[node].lazy;
        // 无更新，直接返回
        if (lazy == 0) {
            return;
        }
        // 所谓lazy下方
        int s = tree[node].s, e = tree[node].e, m = (s + e) >> 1;
        // 通知左右子区间区间和变更
        // 注意区间和变更值为 区间长度 * 增量
        // 由于只下方一层，因为左右子区间更新完区间和后，打上lazy标记，表示叶子节点实际值并未更新
        // 左子区间更新
        tree[node << 1].val += (m - s + 1) * lazy;
        tree[node << 1].lazy += lazy;
        // 右子区间更新
        tree[node << 1 | 1].val += (e - m) * lazy;
        tree[node << 1 | 1].lazy += lazy;
        // 清除当前节点的lazy标记
        tree[node].lazy = 0;
    }
}
