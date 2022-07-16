package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2022/6/6 21:52
 * @description: _732_MyCalender3
 * 732. 我的日程安排表 III
 * 当 k 个日程安排有一些时间上的交叉时（例如 k 个日程安排都在同一时间内），就会产生 k 次预订。
 *
 * 给你一些日程安排 [start, end) ，请你在每个日程安排添加后，返回一个整数 k ，表示所有先前日程安排会产生的最大 k 次预订。
 *
 * 实现一个 MyCalendarThree 类来存放你的日程安排，你可以一直添加新的日程安排。
 *
 * MyCalendarThree() 初始化对象。
 * int book(int start, int end) 返回一个整数 k ，表示日历中存在的 k 次预订的最大值。
 *
 *
 * 示例：
 *
 * 输入：
 * ["MyCalendarThree", "book", "book", "book", "book", "book", "book"]
 * [[], [10, 20], [50, 60], [10, 40], [5, 15], [5, 10], [25, 55]]
 * 输出：
 * [null, 1, 1, 2, 3, 3, 3]
 *
 * 解释：
 * MyCalendarThree myCalendarThree = new MyCalendarThree();
 * myCalendarThree.book(10, 20); // 返回 1 ，第一个日程安排可以预订并且不存在相交，所以最大 k 次预订是 1 次预订。
 * myCalendarThree.book(50, 60); // 返回 1 ，第二个日程安排可以预订并且不存在相交，所以最大 k 次预订是 1 次预订。
 * myCalendarThree.book(10, 40); // 返回 2 ，第三个日程安排 [10, 40) 与第一个日程安排相交，所以最大 k 次预订是 2 次预订。
 * myCalendarThree.book(5, 15); // 返回 3 ，剩下的日程安排的最大 k 次预订是 3 次预订。
 * myCalendarThree.book(5, 10); // 返回 3
 * myCalendarThree.book(25, 55); // 返回 3
 *
 *
 * 提示：
 *
 * 0 <= start < end <= 10^9
 * 每个测试用例，调用 book 函数最多不超过 400次
 */
public class _732_MyCalender3 {


    /**
     * 线段树
     *
     * 利用线段树，假设我们开辟了数组 arr[1, 1e9]，
     * 初始时每个元素的值都为 0，对于每次行程预定的区间 [start,end) ，则我们将区间中的元素 arr[start,⋯,end−1] 中的每个元素加 1，
     * 最终只需要求出数组 arr[1, 1e9] 的最大元素即可。
     *
     * 实际我们不必实际开辟完整数组 arr，可采用动态线段树+懒标记 lazy 标记区间 [l,r] 进行累加增量，
     * tree 记录区间 [l,r] 的最大值，每一次book先update[],再回区间 [1,10^9]中的最大元素即可。
     *
     * 利用HahsMap完成动态线段树
     * map<k, v> k 代表树节点，v 代表 当前树节点所负责区域内的最大值
     * node 的左右孩子分别为 node * 2 和 node * 2 + 1
     * 由于node不保存左右孩子，所以每个函数都需要加上节点node负责的左右范围
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/my-calendar-iii/solution/wo-de-ri-cheng-an-pai-biao-iii-by-leetco-9rif/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    static class MyCalendarThree {

        // 线段树
        private Map<Integer, Integer> tree;
        // 懒标记
        private Map<Integer, Integer> lazy;

        /**
         * 节点node负责原数组[l, r]
         * 目标：给元素组 [start, end] 内么每个元素增加delta
         * @param node
         * @param l
         * @param r
         * @param start
         * @param end
         * @param delta
         */
        private void update(int node, int l, int r, int start, int end, int delta) {
            // 无交集
            if (start > r || end < l) {
                return;
            }
            // 节点负责范围 是目标范围一部分
            if (start <= l && r <= end) {
                // 这部分实行懒更新，区间内最大值直接增加对应增量即可
                tree.put(node, tree.getOrDefault(node, 0) + delta);
                // 懒标记
                lazy.put(node, lazy.getOrDefault(node, 0) + delta);
                // 直接返回
                return;
            }
            // 否则，需要分开进行，就得先传递自身lazy
            pushdown(node);
            // 再分开更新
            int mid = (l + r) / 2;
            // 如果和左区间有交集
            if (r >= start) {
                // 更新左区间，注意参数
                update(node * 2, l, mid, start, end, delta);
            }
            // 和右区间有交集
            if (l <= end) {
                // 更新右区间，注意参数
                update(node * 2 + 1, mid + 1, r, start, end, delta);
            }
            // 左右区间完成更新，回馈父节点
            tree.put(node, Math.max(tree.getOrDefault(node * 2, 0), tree.getOrDefault(node * 2 + 1, 0)));
        }


        /**
         * 懒标记下传一层
         * @param node
         */
        private void pushdown(int node) {
            // lazy为0不用传递
            Integer ly = lazy.getOrDefault(node, 0);
            if (ly == 0) {
                return;
            }
            // 左右子树完成相应变化，继承lazy
            int left = node * 2, right = node * 2 + 1;
            tree.put(left, tree.getOrDefault(left, 0) + ly);
            lazy.put(left, lazy.getOrDefault(left, 0) + ly);
            tree.put(right, tree.getOrDefault(right, 0) + ly);
            lazy.put(right, lazy.getOrDefault(right, 0) + ly);
            // 清空父节点的lazy标记
            lazy.put(node, 0);
        }


        /**
         * 节点node负责原数组[l, r]
         * 目标：返回原数组 [start, end] 范围内最大元素值
         * @param node
         */
        private int query(int node, int l, int r, int start, int end) {
            // 无交集
            if (start > r || end < l) {
                return 0;
            }
            // 节点负责部分 就是 目标部分一部分，那么这一部分的最大值直接就能得到
            if (start <= l && r <= end) {
                return tree.getOrDefault(node, 0);
            }
            // 需要分开寻找最大值，就得先把节点已有的lazy传递下去
            pushdown(node);
            // 再分开查找
            int left = node * 2, right = node * 2 + 1, mid = (l + r) / 2;
            int max = 0;
            // 待查找区间和左子区间有交集，查询左子区间
            if (r >= start) {
                max = Math.max(max, query(left, l, mid, start, end));
            }
            // 待查找区间和右子区间有交集，查询右子区间
            if (l <= end) {
                max = Math.max(max, query(right, mid + 1, r, start, end));
            }
            // 返回左右区间更大值
            return max;
        }

        public MyCalendarThree() {
            tree = new HashMap<>();
            lazy = new HashMap<>();
        }

        // 题目说明的区间是 [)，所以这里右边界要 -1
        // 每一次book，就相当于给 [start, end - 1] 范围内每个元素增加1
        public int book(int start, int end) {
            update(1, 1, (int) 1e9, start, end - 1, 1);
            // 更新完成，再去查找全局范围内的最大值
            return tree.getOrDefault(1, 0);
        }
    }

    public static void main(String[] args) {
        _732_MyCalender3.MyCalendarThree obj = new _732_MyCalender3.MyCalendarThree();
        obj.book(10, 20);
        // System.out.println(obj.book(10, 20));
        System.out.println(obj.book(50, 60));
        System.out.println(obj.book(10, 40));
        System.out.println(obj.book(5, 15));
        System.out.println(obj.book(5, 10));
        System.out.println(obj.book(25, 55));
    }
}
