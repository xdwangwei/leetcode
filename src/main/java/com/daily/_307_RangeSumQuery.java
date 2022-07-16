package com.daily;

/**
 * @author wangwei
 * @date 2022/4/4 10:36
 *
 * 307. 区域和检索 - 数组可修改
 * 给你一个数组 nums ，请你完成两类查询。
 *
 * 其中一类查询要求 更新 数组 nums 下标对应的值
 * 另一类查询要求返回数组 nums 中索引 left 和索引 right 之间（ 包含 ）的nums元素的 和 ，其中 left <= right
 * 实现 NumArray 类：
 *
 * NumArray(int[] nums) 用整数数组 nums 初始化对象
 * void update(int index, int val) 将 nums[index] 的值 更新 为 val
 * int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（ 包含 ）的nums元素的 和 （即，nums[left] + nums[left + 1], ..., nums[right]）
 *
 *
 * 示例 1：
 *
 * 输入：
 * ["NumArray", "sumRange", "update", "sumRange"]
 * [[[1, 3, 5]], [0, 2], [1, 2], [0, 2]]
 * 输出：
 * [null, 9, null, 8]
 *
 * 解释：
 * NumArray numArray = new NumArray([1, 3, 5]);
 * numArray.sumRange(0, 2); // 返回 1 + 3 + 5 = 9
 * numArray.update(1, 2);   // nums = [1,2,5]
 * numArray.sumRange(0, 2); // 返回 1 + 2 + 5 = 8
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 3 * 104
 * -100 <= nums[i] <= 100
 * 0 <= index < nums.length
 * -100 <= val <= 100
 * 0 <= left <= right < nums.length
 * 调用 pdate 和 sumRange 方法次数不大于 3 * 104
 */
public class _307_RangeSumQuery {

    /**
     * 方法一：基本操作
     * 14 / 15 个通过测试用例
     * 状态：超出时间限制
     */
    // class NumArray {
    //
    //     private int[] nums;
    //
    //     public NumArray(int[] nums) {
    //         this.nums = nums;
    //     }
    //     // 更新值
    //     public void update(int index, int val) {
    //         nums[index] = val;
    //     }
    //     // 区间和
    //     public int sumRange(int left, int right) {
    //         int ans = 0;
    //         for (int i = left; i <= right; i++) {
    //             ans += nums[i];
    //         }
    //         return ans;
    //     }
    // }

    /**
     * 方法二：前缀和数组
     * 15 / 15 个通过测试用例
     * 状态：超出时间限制
     */
    // class NumArray {
    //     private int[] preSum;
    //
    //     public NumArray(int[] nums) {
    //         int n = nums.length;
    //         this.preSum = new int[n];
    //         preSum[0] = nums[0];
    //         for (int i = 1; i < n; i++) {
    //             preSum[i] = preSum[i - 1] + nums[i];
    //         }
    //     }
    //     // 从index往后的值都有一个增量
    //     public void update(int index, int val) {
    //         // 计算增量
    //         int diff;
    //         // 分情况，避免数组越界
    //         if (index == 0) {
    //             diff = val - preSum[0];
    //         } else {
    //             diff = val - (preSum[index] - preSum[index - 1]);
    //         }
    //         for (int i = index; i < preSum.length; i++) {
    //             preSum[i] += diff;
    //         }
    //     }
    //     // 区间和
    //     public int sumRange(int left, int right) {
    //         // 分情况，避免数组越界
    //         if (left == 0) {
    //             return preSum[right];
    //         }
    //         return preSum[right] - preSum[left - 1];
    //     }
    // }

    /**
     * 方法三：树状数组
     * 可以看到方法一二都是 某个操作时间复杂度是O(1).另一个是O(N)，所以超时
     * 对于 [区间查询，单点修改]，使用树状数组能够保证 时间复杂度 O(logN)
     */
    class NumArray {
        private int[] nums;
        private int[] tree;
        private int n;

        private int lowerbit(int  x) {
            return  x & -x;
        }

        private void add(int x, int k) {
            for (; x <= n; x += lowerbit(x)) {
                tree[x] += k;
            }
        }

        private int ask(int x) {
            int ans = 0;
            for (; x >= 1; x -= lowerbit(x)) {
                ans += tree[x];
            }
            return ans;
        }

        public NumArray(int[] nums) {
            this.nums = nums;
            n = nums.length;
            tree = new int[n + 1];
            for (int i = 0; i < n; i++) {
                add(i + 1, nums[i]);
            }
        }
        // 从index往后的值都有一个增量
        public void update(int index, int val) {
            add(index + 1, val - nums[index]);
        }
        // 区间和
        public int sumRange(int left, int right) {
            return ask(right + 1) - ask(left);
        }
    }

    /**
     * 方法四：分块
     * 设数组大小为 n，我们将数组 nums 分成多个块，每个块大小 size，最后一个块的大小为剩余的不超过 size 的元素数目，那么块的总数为 ⌈n/size⌉，
     * 用一个数组 sum 保存每个块的元素和'
     *
     * 那么改变一个元素值，只改变它所在块的元素和
     * 对于区间求和，设 left 位于第 b1个块内的第 i1个元素，right 位于第 b2个块内的第 i2个元素。
     *  如果 b1=b2，那么直接返回第 b1个块位于区间 [i1,i2] 的元素之和；
     *  否则计算第 b1个块位于区间 [i1, size-1]的元素之和 sum1，第 b2 个块位于区间 [0, i2]] 的元素之和 sum2 ，第 b1+1 个块到第 b2-1个块的元素和的总和 sum3，
     *  返回 sum1 + sum2 + sum3
     *
     *  对于块大小 size 的取值，我们从各个函数的时间复杂度入手。
     *  构造函数的时间复杂度为 O(n)，update 函数的时间复杂度为O(1)，
     *  而 sumRange 函数的时间复杂度为 O(size + n/size)。因为 size+(n/size) >= 2sqrt(n)，仅当 size = sqrt(size)时等号成立。
     *  因此 size 取 ⌊sqrt(N)⌋，此时 sumRange 函数的时间复杂度为 O(sqrt n)
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode-cn.com/problems/range-sum-query-mutable/solution/qu-yu-he-jian-suo-shu-zu-ke-xiu-gai-by-l-76xj/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     */
    class NumArray4 {
        private int[] sum; // sum[i] 表示第 i 个块的元素和
        private int size; // 块的大小
        private int[] nums;

        public NumArray4(int[] nums) {
            this.nums = nums;
            int n = nums.length;
            size = (int) Math.sqrt(n);
            sum = new int[(n + size - 1) / size]; // n/size 向上取整
            // i 位于 第 i /size个块内第 i % size 个元素
            // 得到 块内和 sum 数组
            for (int i = 0; i < n; i++) {
                sum[i / size] += nums[i];
            }
        }
        // 更新，只涉及 元素 nums[index] 本身 及其所在块，O(1)
        public void update(int index, int val) {
            sum[index / size] += val - nums[index];
            nums[index] = val;
        }

        public int sumRange(int left, int right) {
            int b1 = left / size, i1 = left % size, b2 = right / size, i2 = right % size;
            if (b1 == b2) { // 区间 [left, right] 在同一块中
                int sum = 0;
                for (int j = i1; j <= i2; j++) {
                    sum += nums[b1 * size + j];
                }
                return sum;
            }
            // 不在同一个块内，分三段
            // left所在块 [i1, size-1]
            int sum1 = 0;
            for (int j = i1; j < size; j++) {
                sum1 += nums[b1 * size + j];
            }
            // right所在快 [0...i2]
            int sum2 = 0;
            for (int j = 0; j <= i2; j++) {
                sum2 += nums[b2 * size + j];
            }
            // 中间块
            int sum3 = 0;
            for (int j = b1 + 1; j < b2; j++) {
                sum3 += sum[j];
            }
            return sum1 + sum2 + sum3;
        }
    }

    /**
     * 线段树 segmentTree 是一个二叉树，每个结点保存数组 nums 在区间 [s, e] 的最小值、最大值或者总和等信息。
     * s = start, e = end，用于指定原数组区间左右端点，node 指定线段树数组下标
     * 所以记住，给定一个线段树数组索引，必然同时给定原数组一个区间[l,r],tree[x]=sum(nums[l,r])
     */
    class NumArray5 {

        // 线段树数组，大小初始化为原数组大小的4倍
        private int[] segmentTree;
        // 原数组大小
        private int n;

        public NumArray5(int[] nums) {
            n = nums.length;
            segmentTree = new int[4 * n];
            build(0, 0, n - 1, nums);
        }
        // 更新，只涉及 元素 nums[index] 本身 及其所在块，O(1)
        public void update(int index, int val) {
            change(0, 0, n - 1, index, val);
        }

        public int sumRange(int left, int right) {
            return range(0, 0, n - 1, left, right);
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
            // 当前节点的覆盖范围正好是要求的区间，直接返回节点值
            if (left == s && right == e) {
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

}
