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
 * 所以，这里要进行lazy的下放，左右区间更新完毕后，再回馈更新当前区间值，node.val = node.left.val + node.right.val
 *
 * 同样，因为加入了lazy标记，在查询某个区间和的时候，当 当前点所管理区间 完全包含于 目标区间之内 时，直接返回当前点的值，
 * 但是如果不完全包含，要分开求和，也就是要分别访问左右子区间时，那也得先将当前节点的lazy下放，保证左右子区间先处理完之前的变更
z*/
public class SegmentTree {


    /**
     * 基本线段树模板，数据规模不能不大，直接创建 4N 大小的node数组
     */
    class BasicSegmentTree {
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
        BasicSegmentTree(int[] arr) {
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
            // 必须分段统计时，需要先下放lazy标记
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
            // 如果不是全包含，必须分区间处理，必须先下放lazy，保证左右区间先把之前的修改处理完
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
            // 所谓lazy下放
            int s = tree[node].s, e = tree[node].e, m = (s + e) >> 1;
            // 通知左右子区间区间和变更
            // 注意区间和变更值为 区间长度 * 增量
            // 由于只下放一层，因为左右子区间更新完区间和后，打上lazy标记，表示叶子节点实际值并未更新
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

    /**
     * 线段树动态开店：数组预估形式、动态指针形式
     * 每种形式下，对于区间统计增加增量 或 同一赋值为同一值
     *
     * 共四种实现模板
     *
     * 力扣：https://leetcode.cn/circle/discuss/H4aMOn/
     *
     * 动态开点线段树数组版 (带懒标记，覆盖式区间修改)
     * 支持：单点修改 / 单点查询 / 区间求和 / 覆盖式区间修改
     */
    class DynamicSegmentTreeArrayAdd{
        private class Node{
            int lIdx, rIdx, lazy, val;
        }
        Node[] tree;
        int n, count;
        public DynamicSegmentTreeArrayAdd(int n, int m){
            this.n = n;
            this.count = 1;
            this.tree = new Node[m];
        }
        public void add(int i, int x){ // 单点修改(驱动): 增量式 nums[i] += x
            add(i, x, 0, n - 1, 1);
        }
        public void update(int i, int x){ // 单点修改(驱动): 覆盖式 nums[i] = x
            update(i, x, 0, n - 1, 1);
        }
        public int query(int i){ // 单点查询 (驱动): 查询 nums[i]
            return query(i, 0, n - 1, 1);
        }
        public void add(int l, int r, int x){ // 区间修改(驱动): 增量式 [l,r] 区间所有元素加上x
            add(l, r, x, 0, n - 1, 1);
        }
        public int sum(int l, int r){ // 区间查询(驱动): nums[l]~nums[r]之和
            return sum(l, r, 0, n - 1, 1);
        }
        public int min(int l, int r){ // 区间查询 (驱动): 查询[l,r]中的最小值
            return min(l, r, 0, n - 1, 1);
        }
        public int max(int l, int r){ // 区间查询 (驱动): 查询[l,r]中的最小值
            return max(l, r, 0, n - 1, 1);
        }
        // 单点修改: 增量式 令nums[idx] += x。修改叶子结点，无关标记。
        private void add(int idx, int x, int s, int t, int i){
            if(s == t) {
                tree[i].val += x; // 增量更新
                return;
            }
            addNode(i); // 动态开点
            int c = s + (t - s) / 2;
            if(tree[i].lazy != 0) pushDown(s, c, t, i); // 是否推送标记
            if(idx <= c) add(idx, x, s, c, tree[i].lIdx);
            else add(idx, x, c + 1, t, tree[i].rIdx);
            pushUp(i); // 后序动作，自底向上更新结点区间和 tree[i]
        }
        // 单点修改: 覆盖式 令nums[idx] = x。修改叶子结点，无关标记。
        private void update(int idx, int x, int s, int t, int i){
            if(s == t) {
                tree[i].val = x; // 覆盖更新
                return;
            }
            addNode(i); // 动态开点
            int c = s + (t - s) / 2;
            if(tree[i].lazy != 0) pushDown(s, c, t, i); // 是否推送标记
            if(idx <= c) update(idx, x, s, c, tree[i].lIdx);
            else update(idx, x, c + 1, t, tree[i].rIdx);
            pushUp(i);  // 后序动作，自底向上更新结点值
        }
        // 单点查询 (具体): 查询 nums[i]，尾递归
        private int query(int idx, int s, int t, int i){
            if(s == t) return tree[i].val;
            addNode(i); // 动态开点
            int c = s + (t - s) / 2;
            if(tree[i].lazy != 0) pushDown(s, c, t, i); // 是否推送标记
            if(idx <= c) return query(idx, s, c, tree[i].lIdx);
            else return query(idx, c + 1, t, tree[i].rIdx);
        }
        // 区间修改: 增量式 [l,r] 区间所有元素加上x
        private void add(int l, int r, int x, int s, int t, int i){
            if(l <= s && t <= r){ // 当前结点代表的区间在所求区间之内
                tree[i].val += (t - s + 1) * x; // 结点i的区间和加上t-s+1个x
                if(s != t) tree[i].lazy += x; // 结点i不是叶子结点，懒标记值加上x
                return;
            }
            addNode(i); // 动态开点
            int c = s + (t - s) / 2;
            if(tree[i].lazy != 0) pushDown(s, c, t, i); // 是否推送标记
            if(l <= c) add(l, r, x, s, c, tree[i].lIdx);
            if(r > c) add(l, r, x, c + 1, t, tree[i].rIdx);
            pushUp(i); // 后序动作，自底向上更新结点区间和 tree[i]
        }
        // 区间查询: 求 nums[l]~nums[r]之和
        private int sum(int l, int r, int s, int t, int i){
            if(l <= s && t <= r) return tree[i].val; // 当前结点代表的区间在所求区间之内
            addNode(i); // 动态开点
            int c = s + (t - s) / 2, sum = 0;
            if(tree[i].lazy != 0) pushDown(s, c, t, i); // 是否推送标记
            if(l <= c) sum += sum(l, r, s, c, tree[i].lIdx);
            if(r > c) sum += sum(l, r, c + 1, t, tree[i].rIdx);
            return sum;
        }
        // 区间查询: 查询[l,r]中的最小值
        private int min(int l, int r, int s, int t, int i){
            if(s == t) return tree[i].val; // 叶子结点
            addNode(i); // 动态开点
            int c = s + (t - s) / 2, lmin = Integer.MAX_VALUE, rmin = Integer.MAX_VALUE;
            if(tree[i].lazy != 0) pushDown(s, c, t, i); // 是否推送标记
            if(l <= c) lmin = min(l, r, s, c, tree[i].lIdx);
            if(r > c) rmin = min(l, r, c + 1, t, tree[i].rIdx);
            return Math.min(lmin, rmin);
        }
        // 区间查询: 查询[l,r]中的最大值
        private int max(int l, int r, int s, int t, int i){
            if(s == t) return tree[i].val;
            addNode(i); // 动态开点
            int c = s + (t - s) / 2, lmax = Integer.MIN_VALUE, rmax = Integer.MIN_VALUE;
            if(tree[i].lazy != 0) pushDown(s, c, t, i);
            if(l <= c) lmax = max(l, r, s, c, tree[i].lIdx);
            if(r > c) rmax = max(l, r, c + 1, t, tree[i].rIdx);
            return Math.max(lmax, rmax);
        }
        // pushup: 更新 cur.val
        private void pushUp(int i){
            Node cur = tree[i], lChild = tree[cur.lIdx], rChild = tree[cur.rIdx];
            cur.val = lChild.val + rChild.val;
        }
        // pushdown: 更新当前结点及其左右子结点的懒标记
        private void pushDown(int s, int c, int t, int i){
            Node cur = tree[i], lChild = tree[cur.lIdx], rChild = tree[cur.rIdx];
            lChild.val += (c - s + 1) * cur.lazy; // 更新其左子结点的区间和
            lChild.lazy += cur.lazy; // 传递懒标记
            rChild.val += (t - c) * cur.lazy;
            rChild.lazy += cur.lazy;
            cur.lazy = 0;
        }
        // 动态开点
        private void addNode(int i){
            if(tree[i] == null) tree[i] = new Node(); // 当前结点未建，创建之
            if(tree[i].lIdx == 0) { // 若 tree[i] 结点无左孩子，添加之
                tree[i].lIdx = ++count; // 赋予结点标号
                tree[tree[i].lIdx] = new Node(); // 开辟实例
            }
            if(tree[i].rIdx == 0) { // 若 tree[i] 结点无右孩子，添加之
                tree[i].rIdx = ++count;
                tree[tree[i].rIdx] = new Node();
            }
        }
    }

    /**
     * 动态开点线段树数组版 (带懒标记，覆盖式区间修改为)
     * 支持：单点修改 / 单点查询 / 区间求和 / 覆盖式区间修改
     */
    class DynamicSegmentTreeArrayUpdate{
        private class Node{
            int lIdx, rIdx, lazy, val;
            boolean updated;
        }
        Node[] tree;
        int n, count;
        public DynamicSegmentTreeArrayUpdate(int n, int m){
            this.n = n;
            this.count = 1;
            this.tree = new Node[m];
        }
        public void add(int i, int x){ // 单点修改(驱动): 增量式 nums[i] += x
            add(i, x, 0, n - 1, 1);
        }
        public void update(int i, int x){ // 单点修改(驱动): 覆盖式 nums[i] = x
            update(i, x, 0, n - 1, 1);
        }
        public int query(int i){ // 单点查询 (驱动): 查询 nums[i]
            return query(i, 0, n - 1, 1);
        }
        public void update(int l, int r, int x){ // 区间修改(驱动): 覆盖式 [l,r] 区间所有元素改为x
            update(l, r, x, 0, n - 1, 1);
        }
        public int sum(int l, int r){ // 区间查询(驱动): nums[l]~nums[r]之和
            return sum(l, r, 0, n - 1, 1);
        }
        public int min(int l, int r){ // 区间查询 (驱动): 查询[l,r]中的最小值
            return min(l, r, 0, n - 1, 1);
        }
        public int max(int l, int r){ // 区间查询 (驱动): 查询[l,r]中的最大值
            return max(l, r, 0, n - 1, 1);
        }
        // 单点修改: 增量式 令nums[idx] += x。修改叶子结点，无关标记。
        private void add(int idx, int x, int s, int t, int i){
            if(s == t) {
                tree[i].val += x; // 增量更新
                return;
            }
            addNode(i); // 动态开点
            int c = s + (t - s) / 2;
            if(tree[i].updated) pushDown(s, c, t, i); // 是否推送标记
            if(idx <= c) add(idx, x, s, c, tree[i].lIdx);
            else add(idx, x, c + 1, t, tree[i].rIdx);
            pushUp(i); // 后序动作，自底向上更新结点区间和 tree[i]
        }
        // 单点修改: 覆盖式 令nums[idx] = x。修改叶子结点，无关标记。
        private void update(int idx, int x, int s, int t, int i){
            if(s == t) {
                tree[i].val = x; // 覆盖更新
                return;
            }
            addNode(i); // 动态开点
            int c = s + (t - s) / 2;
            if(tree[i].updated) pushDown(s, c, t, i); // 是否推送标记
            if(idx <= c) update(idx, x, s, c, tree[i].lIdx);
            else update(idx, x, c + 1, t, tree[i].rIdx);
            pushUp(i);  // 后序动作，自底向上更新结点区间和 tree[i]
        }
        // 单点查询 (具体): 查询 nums[i]，尾递归
        private int query(int idx, int s, int t, int i){
            if(s == t) return tree[i].val;
            addNode(i); // 动态开点
            int c = s + (t - s) / 2;
            if(tree[i].updated) pushDown(s, c, t, i); // 是否推送标记
            if(idx <= c) return query(idx, s, c, tree[i].lIdx);
            else return query(idx, c + 1, t, tree[i].rIdx);
        }
        // 区间修改: 覆盖式 [l,r] 区间所有元素改为x
        private void update(int l, int r, int x, int s, int t, int i){
            if(l <= s && t <= r){ // 当前结点代表的区间在所求区间之内
                tree[i].val = (t - s + 1) * x; // 结点i的区间和等于t-s+1个x
                if(s != t) {
                    tree[i].lazy = x; // 更新懒标记
                    tree[i].updated = true; // 更新updated
                }
                return;
            }
            addNode(i); // 动态开点
            int c = s + (t - s) / 2;
            if(tree[i].updated) pushDown(s, c, t, i); // 是否推送标记
            if(l <= c) update(l, r, x, s, c, tree[i].lIdx);
            if(r > c) update(l, r, x, c + 1, t, tree[i].rIdx);
            pushUp(i); // 后序动作，自底向上更新结点值
        }
        // 区间查询: 求 nums[l]~nums[r]之和
        private int sum(int l, int r, int s, int t, int i){
            if(l <= s && t <= r) return tree[i].val; // 当前结点代表的区间在所求区间之内
            addNode(i); // 动态开点
            int c = s + (t - s) / 2, sum = 0;
            if(tree[i].updated) pushDown(s, c, t, i); // 是否推送标记
            if(l <= c) sum += sum(l, r, s, c, tree[i].lIdx);
            if(r > c) sum += sum(l, r, c + 1, t, tree[i].rIdx);
            return sum;
        }
        // 区间查询: 查询[l,r]中的最小值
        private int min(int l, int r, int s, int t, int i){
            if(s == t) return tree[i].val; // 叶子结点
            addNode(i); // 动态开点
            int c = s + (t - s) / 2, lmin = Integer.MAX_VALUE, rmin = Integer.MAX_VALUE;
            if(tree[i].updated) pushDown(s, c, t, i); // 是否推送标记
            if(l <= c) lmin = min(l, r, s, c, tree[i].lIdx);
            if(r > c) rmin = min(l, r, c + 1, t, tree[i].rIdx);
            return Math.min(lmin, rmin);
        }
        // 区间查询: 查询[l,r]中的最大值
        private int max(int l, int r, int s, int t, int i){
            if(s == t) return tree[i].val;
            addNode(i); // 动态开点
            int c = s + (t - s) / 2, lmax = Integer.MIN_VALUE, rmax = Integer.MIN_VALUE;
            if(tree[i].updated) pushDown(s, c, t, i); // 是否推送标记
            if(l <= c) lmax = max(l, r, s, c, tree[i].lIdx);
            if(r > c) rmax = max(l, r, c + 1, t, tree[i].rIdx);
            return Math.max(lmax, rmax);
        }
        // 更新 cur.val
        private void pushUp(int i){
            Node cur = tree[i], lChild = tree[cur.lIdx], rChild = tree[cur.rIdx];
            cur.val = lChild.val + rChild.val;
        }
        // pushdown: 更新当前结点及其左右子结点的懒标记和updated
        private void pushDown(int s, int c, int t, int i){
            Node cur = tree[i], lChild = tree[cur.lIdx], rChild = tree[cur.rIdx];
            lChild.val = (c - s + 1) * cur.lazy; // 更新左子结点的区间和
            lChild.lazy = cur.lazy; // 传递懒标记(增量标记)到左子结点中
            lChild.updated = true; // 更新左子结点updated
            rChild.val = (t - c) * cur.lazy;
            rChild.lazy = cur.lazy;
            rChild.updated = true;
            cur.lazy = 0; // 重置当前结点懒惰标记值
            cur.updated = false; // 更新updated
        }
        // 动态开点
        private void addNode(int i){
            if(tree[i] == null) tree[i] = new Node();
            if(tree[i].lIdx == 0) { // 若 tree[i] 结点无左孩子，添加之
                tree[i].lIdx = ++count; // 赋予结点标号
                tree[tree[i].lIdx] = new Node(); // 开辟实例
            }
            if(tree[i].rIdx == 0) { // 若 tree[i] 结点无右孩子，添加之
                tree[i].rIdx = ++count;
                tree[tree[i].rIdx] = new Node();
            }
        }
    }

    /**
     *
     * 动态开点线段树指针版 (带懒标记，增量式区间修改)
     * 支持：单点修改 / 单点查询 / 区间求和 / 增量式区间修改
     */
    class DynamicSegmentTreePointerAdd{
        private class Node{
            int lazy, val;
            Node lChild, rChild;
        }
        int n;
        Node root;
        public DynamicSegmentTreePointerAdd(int n){
            this.n = n;
            this.root = new Node();
        }
        public void add(int i, int x){ // 单点修改(驱动): 增量式 nums[i] += x
            add(i, x, 0, n - 1, root);
        }
        public void update(int i, int x){ // 单点修改(驱动): 覆盖式 nums[i] = x
            update(i, x, 0, n - 1, root);
        }
        public int query(int i){ // 单点查询 (驱动): 查询 nums[i]
            return query(i, 0, n - 1, root);
        }
        public void add(int l, int r, int x){ // 区间修改(驱动): 增量式 [l,r] 区间所有元素加上x
            add(l, r, x, 0, n - 1, root);
        }
        public int sum(int l, int r){ // 区间查询(驱动): nums[l]~nums[r]之和
            return sum(l, r, 0, n - 1, root);
        }
        public int min(int l, int r){ // 区间查询 (驱动): 查询[l,r]中的最小值
            return min(l, r, 0, n - 1, root);
        }
        public int max(int l, int r){ // 区间查询 (驱动): 查询[l,r]中的最大值
            return max(l, r, 0, n - 1, root);
        }
        // 单点修改: 增量式 令nums[idx] += x。修改叶子结点，无关标记。
        private void add(int idx, int x, int s, int t, Node cur){
            if(s == t) {
                cur.val += x; // 增量更新
                return;
            }
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2;
            if(cur.lazy != 0) pushDown(s, c, t, cur); // 是否推送标记
            if(idx <= c) add(idx, x, s, c, cur.lChild);
            else add(idx, x, c + 1, t, cur.rChild);
            pushUp(cur); // 后序动作，自底向上更新结点区间和 tree[i]
        }
        // 单点修改: 覆盖式 令nums[idx] = x。修改叶子结点，无关标记。
        private void update(int idx, int x, int s, int t, Node cur){
            if(s == t) {
                cur.val = x; // 覆盖更新
                return;
            }
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2;
            if(cur.lazy != 0) pushDown(s, c, t, cur); // 是否推送标记
            if(idx <= c) update(idx, x, s, c, cur.lChild);
            else update(idx, x, c + 1, t, cur.rChild);
            pushUp(cur);  // 后序动作，自底向上更新结点区间和 tree[i]
        }
        // 单点查询: 查询 nums[i]，尾递归
        private int query(int i, int s, int t, Node cur){
            if(s == t) return cur.val;
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2;
            if(cur.lazy != 0) pushDown(s, c, t, cur); // 是否推送标记
            if(i <= c) return query(i, s, c, cur.lChild);
            else return query(i, c + 1, t, cur.rChild);
        }
        // 区间修改: 增量式 [l,r] 区间所有元素加上x
        private void add(int l, int r, int x, int s, int t, Node cur){
            if(l <= s && t <= r){ // 当前结点代表的区间在所求区间之内
                cur.val += (t - s + 1) * x; // 结点i的区间和加上t-s+1个x
                if(s != t) cur.lazy += x; // 结点i不是叶子结点，懒标记值加上x
                return;
            }
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2;
            if(cur.lazy != 0) pushDown(s, c, t, cur); // 是否推送标记
            if(l <= c) add(l, r, x, s, c, cur.lChild);
            if(r > c) add(l, r, x, c + 1, t, cur.rChild);
            pushUp(cur); // 后序动作，自底向上更新结点区间和 tree[i]
        }
        // 区间查询: 求 nums[l]~nums[r]之和
        private int sum(int l, int r, int s, int t, Node cur){
            if(l <= s && t <= r) return cur.val; // 当前结点代表的区间在所求区间之内
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2, sum = 0;
            if(cur.lazy != 0) pushDown(s, c, t, cur); // 是否推送标记
            if(l <= c) sum += sum(l, r, s, c, cur.lChild);
            if(r > c) sum += sum(l, r, c + 1, t, cur.rChild);
            return sum;
        }
        // 区间查询: 查询[l,r]中的最小值
        private int min(int l, int r, int s, int t, Node cur){
            if(s == t) return cur.val; // 叶子结点
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2, lmin = Integer.MAX_VALUE, rmin = Integer.MAX_VALUE;
            if(cur.lazy != 0) pushDown(s, c, t, cur); // 是否推送标记
            if(l <= c) lmin = min(l, r, s, c, cur.lChild);
            if(r > c) rmin = min(l, r, c + 1, t, cur.rChild);
            return Math.min(lmin, rmin);
        }
        // 区间查询: 查询[l,r]中的最大值
        private int max(int l, int r, int s, int t, Node cur){
            if(s == t) return cur.val;
            addNode(cur);
            int c = s + (t - s) / 2, lmax = Integer.MIN_VALUE, rmax = Integer.MIN_VALUE;
            if(cur.lazy != 0) pushDown(s, c, t, cur);
            if(l <= c) lmax = max(l, r, s, c, cur.lChild);
            if(r > c) rmax = max(l, r, c + 1, t, cur.rChild);
            return Math.max(lmax, rmax);
        }
        // pushup: 更新 cur.val
        private void pushUp(Node cur){
            Node lChild = cur.lChild, rChild = cur.rChild;
            cur.val = lChild.val + rChild.val;
        }
        // pushdown: 更新当前结点及其左右子结点的懒标记
        private void pushDown(int s, int c, int t, Node cur){
            Node lChild = cur.lChild, rChild = cur.rChild;
            lChild.val += (c - s + 1) * cur.lazy; // 更新其左子结点的区间和
            lChild.lazy += cur.lazy; // 传递懒标记
            rChild.val += (t - c) * cur.lazy;
            rChild.lazy += cur.lazy;
            cur.lazy = 0; // 重置当前结点懒惰标记值
        }
        // 动态开点
        private void addNode(Node node){
            if(node.lChild == null) node.lChild = new Node();
            if(node.rChild == null) node.rChild = new Node();
        }
    }

    /**
     * 动态开点线段树指针版 (带懒标记，覆盖式区间修改)
     * 支持：单点修改 / 单点查询 / 区间查询 / 覆盖式区间修改
     */
    class DynamicSegmentTreePointerUpdate{
        private class Node{
            int lazy, val;
            boolean updated;
            Node lChild, rChild;
        }
        int n;
        Node root;
        public DynamicSegmentTreePointerUpdate(int n){
            this.n = n;
            this.root = new Node();
        }
        public void add(int i, int x){ // 单点修改(驱动): 增量式 nums[i] += x
            add(i, x, 0, n - 1, root);
        }
        public void update(int i, int x){ // 单点修改(驱动): 覆盖式 nums[i] = x
            update(i, x, 0, n - 1, root);
        }
        public int query(int i){ // 单点查询(驱动): 查询 nums[i]
            return query(i, 0, n - 1, root);
        }
        public void update(int l, int r, int x){ // 区间修改(驱动): 增量式 [l,r] 区间所有元素加上x
            update(l, r, x, 0, n - 1, root);
        }
        public int sum(int l, int r){ // 区间查询(驱动): nums[l]~nums[r]之和
            return sum(l, r, 0, n - 1, root);
        }
        public int min(int l, int r){ // 区间查询(驱动): 查询[l,r]中的最小值
            return min(l, r, 0, n - 1, root);
        }
        public int max(int l, int r){ // 区间查询(驱动): 查询[l,r]中的最大值
            return max(l, r, 0, n - 1, root);
        }
        // 单点修改: 增量式 令nums[idx] += x。修改叶子结点，无关标记。
        private void add(int idx, int x, int s, int t, Node cur){
            if(s == t) {
                cur.val += x; // 增量更新
                return;
            }
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2;
            if(cur.updated) pushDown(s, c, t, cur); // 是否推送标记
            if(idx <= c) add(idx, x, s, c, cur.lChild);
            else add(idx, x, c + 1, t, cur.rChild);
            pushUp(cur); // 后序动作，自底向上更新结点区间和 tree[i]
        }
        // 单点修改: 覆盖式 令nums[idx] = x。修改叶子结点，无关标记。
        private void update(int idx, int x, int s, int t, Node cur){
            if(s == t) {
                cur.val = x; // 覆盖更新
                return;
            }
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2;
            if(cur.updated) pushDown(s, c, t, cur); // 是否推送标记
            if(idx <= c) update(idx, x, s, c, cur.lChild);
            else update(idx, x, c + 1, t, cur.rChild);
            pushUp(cur);  // 后序动作，自底向上更新结点区间和 tree[i]
        }
        // 单点查询: 查询 nums[i]，尾递归
        private int query(int i, int s, int t, Node cur){
            if(s == t) return cur.val;
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2;
            if(cur.updated) pushDown(s, c, t, cur); // 是否推送标记
            if(i <= c) return query(i, s, c, cur.lChild);
            else return query(i, c + 1, t, cur.rChild);
        }
        // 区间修改: 覆盖式 [l,r] 区间所有元素改为x
        private void update(int l, int r, int x, int s, int t, Node cur){
            if(l <= s && t <= r){ // 当前结点代表的区间在所求区间之内
                cur.val = (t - s + 1) * x; // 结点i的区间和等于t-s+1个x
                if(s != t) { // 结点i不是叶子结点
                    cur.lazy = x; // 更新懒标记
                    cur.updated = true; // 更新updated
                }
                return;
            }
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2;
            if(cur.updated) pushDown(s, c, t, cur); // 是否推送标记
            if(l <= c) update(l, r, x, s, c, cur.lChild);
            if(r > c) update(l, r, x, c + 1, t, cur.rChild);
            pushUp(cur); // 后序动作，自底向上更新结点区间和 tree[i]
        }
        // 区间查询: 求 nums[l]~nums[r]之和
        private int sum(int l, int r, int s, int t, Node cur){
            if(l <= s && t <= r) return cur.val; // 当前结点代表的区间在所求区间之内
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2, sum = 0;
            if(cur.updated) pushDown(s, c, t, cur); // 是否推送标记
            if(l <= c) sum += sum(l, r, s, c, cur.lChild);
            if(r > c) sum += sum(l, r, c + 1, t, cur.rChild);
            return sum;
        }
        // 区间查询: 查询[l,r]中的最小值
        private int min(int l, int r, int s, int t, Node cur){
            if(s == t) return cur.val; // 叶子结点
            addNode(cur); // 动态开点
            int c = s + (t - s) / 2, lmin = Integer.MAX_VALUE, rmin = Integer.MAX_VALUE;
            if(cur.updated) pushDown(s, c, t, cur); // 是否推送标记
            if(l <= c) lmin = min(l, r, s, c, cur.lChild);
            if(r > c) rmin = min(l, r, c + 1, t, cur.rChild);
            return Math.min(lmin, rmin);
        }
        // 区间查询: 查询[l,r]中的最大值
        private int max(int l, int r, int s, int t, Node cur){
            if(s == t) return cur.val;
            addNode(cur);
            int c = s + (t - s) / 2, lmax = Integer.MIN_VALUE, rmax = Integer.MIN_VALUE;
            if(cur.updated) pushDown(s, c, t, cur);
            if(l <= c) lmax = max(l, r, s, c, cur.lChild);
            if(r > c) rmax = max(l, r, c + 1, t, cur.rChild);
            return Math.max(lmax, rmax);
        }
        // pushup: 更新 cur.val
        private void pushUp(Node cur){
            Node lChild = cur.lChild, rChild = cur.rChild;
            cur.val = lChild.val + rChild.val;
        }
        // pushdown: 更新当前结点及其左右子结点的懒标记和updated
        private void pushDown(int s, int c, int t, Node cur){
            Node lChild = cur.lChild, rChild = cur.rChild;
            lChild.val = (c - s + 1) * cur.lazy; // 更新其左子结点的区间和
            lChild.lazy = cur.lazy; // 传递懒标记(增量标记)
            lChild.updated = true;
            rChild.val = (t - c) * cur.lazy;
            rChild.lazy = cur.lazy;
            rChild.updated = true;
            cur.lazy = 0; // 重置当前结点懒惰标记值（增量标记置0）
            cur.updated = false;
        }
        // 动态开点
        private void addNode(Node node){
            if(node.lChild == null) node.lChild = new Node();
            if(node.rChild == null) node.rChild = new Node();
        }
    }
}
