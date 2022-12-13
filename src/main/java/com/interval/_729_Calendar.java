package com.interval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * @author wangwei
 * @date 2022/12/12 15:42
 * @description: _729_Calendar
 *
 * 729. 日程表
 * 请实现一个 MyCalendar 类来存放你的日程安排。如果要添加的时间内没有其他安排，则可以存储这个新的日程安排。
 *
 * MyCalendar 有一个 book(int start, int end)方法。它意味着在 start 到 end 时间内增加一个日程安排，注意，这里的时间是半开区间，即 [start, end), 实数 x 的范围为，  start <= x < end。
 *
 * 当两个日程安排有一些时间上的交叉时（例如两个日程安排都在同一时间内），就会产生重复预订。
 *
 * 每次调用 MyCalendar.book方法时，如果可以将日程安排成功添加到日历中而不会导致重复预订，返回 true。否则，返回 false 并且不要将该日程安排添加到日历中。
 *
 * 请按照以下步骤调用 MyCalendar 类: MyCalendar cal = new MyCalendar(); MyCalendar.book(start, end)
 *
 *
 *
 * 示例:
 *
 * 输入:
 * ["MyCalendar","book","book","book"]
 * [[],[10,20],[15,25],[20,30]]
 * 输出: [null,true,false,true]
 * 解释:
 * MyCalendar myCalendar = new MyCalendar();
 * MyCalendar.book(10, 20); // returns true
 * MyCalendar.book(15, 25); // returns false ，第二个日程安排不能添加到日历中，因为时间 15 已经被第一个日程安排预定了
 * MyCalendar.book(20, 30); // returns true ，第三个日程安排可以添加到日历中，因为第一个日程安排并不包含时间 20
 *
 *
 *
 *
 * 提示：
 *
 * 每个测试用例，调用 MyCalendar.book 函数最多不超过 1000次。
 * 0 <= start < end <= 109
 *
 *
 */
public class _729_Calendar {


    /**
     * 方法一：直接遍历
     * 我们记录下所有已经预订的课程安排区间，当我们预订新的区间 [start,end) 时，
     * 此时检查当前已经预订的每个日程安排是否与新日程安排冲突。若不冲突，则可以添加新的日程安排。
     *
     * 对于两个区间 [s1, e1) 和 [s2, e2)，如果二者没有交集，则此时应当满足 s1 >= e2 || s2 >= e1，
     * 也就意味着如果满足 s1 < e2 并且 s2 < e_1 ，则两者会产生交集。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/my-calendar-i/solution/wo-de-ri-cheng-an-pai-biao-i-by-leetcode-nlxr/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class MyCalendar {

        List<int[]> booked;

        public MyCalendar() {
            booked = new ArrayList<>();
        }

        public boolean book(int start, int end) {
            // 判断是否会和已存在区间相交
            // 区间 [s1, e1) 和 [s2, e2)，如果二者没有交集，则此时应当满足 s1 >= e2 || s2 >= e1，
            // 意味着如果满足 s1 < e2 并且 s2 < e_1 ，则两者会产生交集。
            for (int[] arr : booked) {
                int l = arr[0], r = arr[1];
                if (start < r && end > l) {
                    return false;
                }
            }
            // 无交集，则加入
            booked.add(new int[]{start, end});
            return true;
        }
    }


    /**
     * 方法二：二分查找
     * 方法一线性查找速度较慢
     * 如果我们按时间顺序维护日程安排，则可以通过二分查找日程安排的情况来检查新日程安排是否可以预订，
     * 若可以预订 则在排序结构中更新插入日程安排。
     *
     * 需要一个数据结构能够保持元素排序和支持快速插入，可以用 TreeSet 来构建。按起点排序
     * 对于给定的区间 [start,end)，
     *      如果end比已有区间中最小区间的start还小，那么可以直接放入最前面；
     *      如果start比已有区间中最大区间的end还大，那么可以直接放入最后面；
     *      否则，除非它能放置于某两个区间的中间，否则无法加入；
     *          此时我们查找起点小于 end 的最接近的区间 [l,r)，
     *          如果 r <= start，那么 [start,end) 可以放置在 [l,r) 的后面
     *          （假设[l,r)的下一个区间是[x,y)，因为 [l,r) 是 起点小于 end 的最接近的区间，那么 x 一定 >= end,
     *              所以[start,end)一定能插入）
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/my-calendar-i/solution/wo-de-ri-cheng-an-pai-biao-i-by-leetcode-nlxr/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    class MyCalendar2 {
        // 有序表维护所有区间
        TreeSet<int[]> booked;

        public MyCalendar2() {
            // 根据开始时间排序
            booked = new TreeSet<>(Comparator.comparingInt(a -> a[0]));
        }

        public boolean book(int start, int end) {
            // 如果还没有区间，可以直接加入
            // 如果end比已有区间中最小区间的start还小，那么可以直接放入最前面；
            // 如果start比已有区间中最大区间的end还大，那么可以直接放入最后面；
            if (booked.isEmpty() || start >= booked.last()[1] || end <= booked.first()[0]) {
                booked.add(new int[]{start, end});
                return true;
            }
            // 否则，除非它能放置于某两个区间的中间，否则无法加入；
            // 此时我们查找起点小于 end 的最接近的区间 [l,r)，因为区间按起点排序，所以这里取0不影响结果
            int[] tmp = {end, 0};
            int[] lower = booked.lower(tmp);
            // 这里lower一定存在（因为上面的排除）
            // 除非 r <= start，那么 [start,end) 可以放置在 [l,r) 的后面
            // 并且 [l,r)的下一个区间的起点一定大于等于end
            // 否则无法加入
            if (lower[1] > start) {
                return false;
            }
            // 加入
            booked.add(new int[]{start, end});
            return true;
        }
    }


    /**
     * 线段树（动态开点）
     *
     * 假设我们开辟了数组 arr[0,⋯,10^9]，初始时每个元素的值都为 0，对于每次行程预订的区间 [start,end) ，
     * 则我们将区间中的元素 arr[start,⋯,end−1] 中的每个元素都加 1，
     * 每次调用 book 时，我们只需要检测 arr[start,⋯,end−1] 区间内元素和是否为0即可
     *
     * 但是常规线段树需要 4n 空间，对于本题来说 会 mle，因此采用动态开点线段树实现
     *
     *
     * 关于线段树动态开点模板：common.SegmentTree
     * 动态开点线段树不能像静态线段树一样通过2n和2n+1访问到左右孩子，由于节点是用到时才会创建，因此需要记录节点编号
     *
     * 线段树维护的节点信息包括：
     *
     *      ls/rs: 分别代表当前节点的左右子节点在线段树数组 tr 中的下标；
     *      lazy: 懒标记；
     *      sum: 为当前区间的所包含的点的数量。(元素和)
     *
     * 对于常规的线段树实现来说，都是一开始就调用 build 操作创建空树，而线段树一般以「满二叉树」的形式用数组存储，因此需要 4 * n4∗n 的空间，并且这些空间在起始 build 空树的时候已经锁死。
     *
     * 如果一道题仅仅是「值域很大」的离线题（提前知晓所有的询问），我们还能通过「离散化」来进行处理，将值域映射到一个小空间去，从而解决 MLE 问题。
     *
     * 但对于本题而言，由于「强制在线」的原因，也就是并未提前告诉我们所有涉及询问和修改的区间范围，因此无法离散化。
     * 同时值域大小达到 1e9 级别，因此如果我们想要使用「线段树」进行求解，只能采取「动态开点」的方式进行。
     *
     * 动态开点的优势在于，不需要事前构造空树，而是在插入操作 add 和查询操作 query 时根据访问需要进行「开点」操作。
     * 由于我们不保证查询和插入都是连续的，因此对于父节点 u 而言，我们不能通过 u << 1 和 u << 1 | 1 的固定方式进行访问，
     * 而要将节点 tr[u] 的左右节点所在 tr 数组的下标进行存储，分别记为 ls 和 rs 属性。
     * 对于 tr[u].ls=0 和 tr[u].rs=0 则是代表子节点尚未被创建，当需要访问到它们，而又尚未创建的时候，则将其进行创建。
     *
     * 由于存在「懒标记」，线段树的插入和查询都是 logn 的，因此我们在单次操作的时候，最多会创建数量级为 logn 的点，
     * 因此空间复杂度为 O(mlogn)，而不是 O(4∗n)，但开点数的预估需不能仅仅根据 logn 来进行，还要对常熟进行分析，才能得到准确的点数上界。
     *
     * 动态开点相比于原始的线段树实现，本质仍是使用「满二叉树」的形式进行存储，只不过是按需创建区间，
     * 如果我们是按照连续段进行查询或插入，最坏情况下仍然会占到 4∗n 的空间，因此盲猜 logn 的常数在 4 左右，
     * 保守一点可以直接估算到 6，因此我们可以估算点数为 6∗m∗logn，其中 n=1e9 和 m=1e3 分别代表值域大小和查询次数。
     *
     * 当然一个比较实用的估点方式可以「尽可能的多开点数」，利用题目给定的空间上界和我们创建的自定义类（结构体）的大小，
     * 尽可能的多开（ Java 的 128M128M 可以开到 5 * 10^6 以上）。
     *
     * 关于动态开点相关知识参考common.SegmentTree 或 https://leetcode.cn/circle/discuss/H4aMOn/
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/my-calendar-i/solution/by-ac_oier-hnjl/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    static class MyCalendar3 {
        // 动态开点线段树节点
        class Node {
            // ls 和 rs 分别代表当前节点的左右子节点在 tr 的下标
            // num 当前节点所负责区间内元素和
            // lazy 为懒标记
            int ls, rs, lazy, sum;
        }
        // 数据规模，Q 是 query 次数
        int N = (int)1e9, Q = 1000;
        // 线段树节点
        Node[] tree;
        // 当前待创建的节点编号，编号从1开始，也代表了当前共有几个节点
        int idx;
        public MyCalendar3() {
            idx = 1;
            // 预估大小，通常为  [4-6] * q * logN
            tree = new Node[120010];
        }
        // 线段树：[l,r] 范围内元素和，node 是当前节点编号，[s,e] 是当前节点负责的范围
        public int query(int node, int s, int e, int l, int r) {
            // 如果 当前点所管理区间 完全包含于 目标区间之内，当前部分区间和直接返回，不需要递归子区间求和
            if (s >= l && e <= r) {
                return tree[node].sum;
            }
            // 分区间查询之前，必须进行节点动态创建和懒惰标记下放
            lazyCreate(node);
            pushDown(node, s, e);
            // 取出左右区间下标
            int ls = tree[node].ls, rs = tree[node].rs;
            int m = (s + e) >> 1;
            int ans = 0;
            // 分区间查询
            if (l <= m) {
                ans += query(ls, s, m, l, r);
            }
            if (r > m) {
                ans += query(rs, m + 1, e, l, r);
            }
            // 返回
            return ans;
        }
        // 线段树：[l,r] 范围内元素同一增加delta，node 是当前节点编号，[s,e] 是当前节点负责的范围
        public void update(int node, int s, int e, int l, int r, int delta) {
            // 如果 当前点所管理区间 完全包含于 目标区间之内，当前部分区间和直接更新，打上lazy标记，不需要递归子区间更新
            if (s >= l && e <= r) {
                tree[node].sum += (e - s + 1) * delta;
                tree[node].lazy = delta;
                return;
            }
            // 分区间之前，必须进行节点动态创建和懒惰标记下放
            lazyCreate(node);
            pushDown(node, s, e);
            // 取出左右区间下标
            int ls = tree[node].ls, rs = tree[node].rs;
            int m = (s + e) >> 1;
            // 分区间进行
            if (l <= m) {
                update(ls, s, m, l, r, delta);
            }
            if (r > m) {
                update(rs, m + 1, e, l, r, delta);
            }
            // 左右区间更新完毕，回馈更新当前节点
            pushUp(node);
        }
        // 线段树：动态（延迟）创建节点及其左右子节点
        public void lazyCreate(int node) {
            // 创建指定节点
            if (tree[node] == null) {
                tree[node] = new Node();
            }
            // 创建指定节点左节点
            if (tree[node].ls == 0) {
                tree[node].ls = ++idx;
                tree[idx] = new Node();
            }
            // 创建指定节点右节点
            if (tree[node].rs == 0) {
                tree[node].rs = ++idx;
                tree[idx] = new Node();
            }
        }
        // 线段树：节点node的左右子节点负责的左右区间更新完毕，回馈更新当前节点
        public void pushUp(int node) {
            tree[node].sum = tree[tree[node].ls].sum + tree[tree[node].rs].sum;
        }
        // 线段树：节点node的lazy标记下放，[s,e] 是当前节点负责的范围
        public void pushDown(int node, int s, int e) {
            if (tree[node].lazy == 0) {
                return;
            }
            int lazy = tree[node].lazy, ls = tree[node].ls, rs = tree[node].rs;
            int m = (s + e) >> 1;
            // 通知左右子区间区间和变更
            // 注意区间和变更值为 区间长度 * 增量
            // 由于只下放一层，因为左右子区间更新完区间和后，打上lazy标记，表示叶子节点实际值并未更新
            // 左子区间更新
            tree[ls].sum += (m - s + 1) * lazy;
            tree[ls].lazy += lazy;
            tree[rs].sum += (e - m) * lazy;
            tree[rs].lazy += lazy;
            // 清除当前节点lazy标记
            tree[node].lazy = 0;
        }
        public boolean book(int start, int end) {
            // 每次行程预订的区间 [start,end)，转换为 [start, end - 1]
            // 必须保证  [start, end - 1] 没有点，也就是 元素和为0
            if (query(1, 0, N, start, end - 1) > 0) {
                return false;
            }
            // 插入当前区间，即 [start, end-1]范围内元素统一加1
            update(1, 0, N, start, end - 1, 1);
            return true;
        }
    }

    public static void main(String[] args) {
        MyCalendar3 obj = new MyCalendar3();
        System.out.println(obj.book(10, 20));
    }

}
