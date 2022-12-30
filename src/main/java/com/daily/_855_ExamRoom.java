package com.daily;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author wangwei
 * @date 2022/12/30 12:57
 * @description: _855_ExamRoom
 *
 * 855. 考场就座
 * 在考场里，一排有 N 个座位，分别编号为 0, 1, 2, ..., N-1 。
 *
 * 当学生进入考场后，他必须坐在能够使他与离他最近的人之间的距离达到最大化的座位上。如果有多个这样的座位，他会坐在编号最小的座位上。(另外，如果考场里没有人，那么学生就坐在 0 号座位上。)
 *
 * 返回 ExamRoom(int N) 类，它有两个公开的函数：其中，函数 ExamRoom.seat() 会返回一个 int （整型数据），代表学生坐的位置；函数 ExamRoom.leave(int p) 代表坐在座位 p 上的学生现在离开了考场。每次调用 ExamRoom.leave(p) 时都保证有学生坐在座位 p 上。
 *
 *
 *
 * 示例：
 *
 * 输入：["ExamRoom","seat","seat","seat","seat","leave","seat"], [[10],[],[],[],[],[4],[]]
 * 输出：[null,0,9,4,2,null,5]
 * 解释：
 * ExamRoom(10) -> null
 * seat() -> 0，没有人在考场里，那么学生坐在 0 号座位上。
 * seat() -> 9，学生最后坐在 9 号座位上。
 * seat() -> 4，学生最后坐在 4 号座位上。
 * seat() -> 2，学生最后坐在 2 号座位上。
 * leave(4) -> null
 * seat() -> 5，学生最后坐在 5 号座位上。
 *
 *
 * 提示：
 *
 * 1 <= N <= 10^9
 * 在所有的测试样例中 ExamRoom.seat() 和 ExamRoom.leave() 最多被调用 10^4 次。
 * 保证在调用 ExamRoom.leave(p) 时有学生正坐在座位 p 上。
 * 通过次数13,415提交次数29,552
 */
public class _855_ExamRoom {

    class ExamRoom {

        /**
         * 按照题意
         * seat相当于每次都要找到最长的一个线段，然后把它从中间分开成相连的两段
         * 而leave相当于把这个点相连的两个线段合成一个更长的线段。
         *
         * 所以我们需要一个【能够维持有序】的数据结构来存储当前所有的线段(按照线段长度)
         * 二叉堆或红黑树，但是二叉树只能删除堆顶元素。也就是能够每次找出最长线段，
         * 但是却无法完成leave操作，无法移除中间的两个元素。
         * 而红黑树也可以取最值，也可以修改、删除任意一个值，而且时间复杂度都是 O(logN)。
         * 所以选择红黑树。
         *
         * 假设我们取终点的原则是 x = left + (right - left) / 2
         *
         * 由于初始时没有线段，第一次会选择0位置，第二次会选择n-1位置，
         * 因此，为了操作统一，我们初始情况下给集合中加入一个虚拟线段([-1, N])，类似于列表虚拟头结点的作用
         * 所以关于线段[a,b]的操作中，需要考虑a=-1和y=N的特殊情况
         *
         * 问题一：
         * 因为leave的时候要知道某个点p相连的两个线段，也就是p为右端点的线段和p为左端点的线段。
         * 所以我们使用两个map。leftMap和rightMap分别保存每个点作为左端点的线段和它作为右端点的线段。
         * 这样在合并的时候我们能快速找到这两个线段。
         *
         * 问题二：
         * 红黑树集合中线段按照长度排序(最长的在最后)，每次seat就取出最后一个元素(线段)，
         * 然后将新的点放在这个线段的中间。
         * 如果我们按照线段长度进行排序，对于下面这种特殊情况会造成答案错误：
         * 现在有序集合里有线段 [0,4] 和 [4,9]，那么最长线段 longest 就是后者[4,9]，长度为5
         * 此时对于seat操作，会分割 [4,9]，也就是返回座位 6。但正确答案应该是座位 2，
         * 因为 [0,2][2,4] 和 [4,6][6,9] 都满足最大化相邻考生距离的条件，题目要求有多个选择时取索引较小的。
         *
         * 那么要如何设计排序规则：
         *      我们发现这种情况的问题就在于虽然这两个线段长度不一样（只差了1），
         *      但是他们的一半是一样的 (right - left) / 2，实际上是选择这两个线段进行分割后的效果是一样的
         *      因此 按照线段长度的一半（即新加节点到左端点的距离）进行比较，如果相等的话，我们让索引更小的线段排到更后面
         *
         *      对于 a=-1的特殊情况，下一次必然选择0位置，此时它对应的一半距离其实就是选择0位置后与右端点的距离，即 b
         *      对于 b=n的特殊情况，下一次必然选择n-1位置，此时它对应的一半距离其实就是选择n-1位置后与左端点的距离，即 n-1-a
         *
         * 假设 treeset中保存了当前所有可选线段，
         * 那么对于seat操作：
         *      选择 last 元素 [a, b]
         *      选择中点 p
         *      移除区间 [a, b]
         *      增加区间 [a, p]，[p, b]
         * 对于 leave 操作：
         *      查找 p 位置对应的 左右区间 [a1, p], [p, b2]
         *      移除区间 [a1, p], [p, b2]
         *      新加区间 [a1, b2]
         *
         * @param N
         */

        // 将端点 p 映射到以 p 为左端点的线段
        private Map<Integer, int[]> startMap;
        // 将端点 p 映射到以 p 为右端点的线段
        private Map<Integer, int[]> endMap;
        // 根据线段长度从小到大存放所有线段
        private TreeSet<int[]> intervals;
        private int N;

        // 构造函数，传入座位总数 N
        public ExamRoom(int N) {
            this.N = N;
            startMap = new HashMap<>();
            endMap = new HashMap<>();
            // 设置 intervals 排序规则
            intervals = new TreeSet<>((intv1, intv2) -> {
                int len1 = midLengthOf(intv1);
                int len2 = midLengthOf(intv2);
                // 如果两线段的一半长度相等(4,5)，让索引小的线段更靠后，也就是首端点降序
                if (len1 == len2) return intv2[0] - intv1[0];
                // 按照线段长度从小到大排序，升序
                return len1 - len2;
            });
            // 在有序集合中先放一个虚拟线段
            addInterval(new int[] {-1, N});
        }

        // 来了一名考生，返回你给他分配的座位
        public int seat() {
            int seat;
            // 从集合中拿出最长的那个线段
            int[] longest = intervals.last();
            // 安排位置
            int l = longest[0], r = longest[1];
            // 对于特殊情况
            // 左边还没人，安排在最左边
            if (l == -1) seat = 0;
                // 右边还没人，安排在最左边
            else if (r == N) seat = N - 1;
                // 左右都有人，安排在中间
            else seat = l + (r - l) / 2;
            // 安排位置后，相当于把一个长线段划分成两个短的
            // 移除长的
            removeInterval(longest);
            // 加入两短的
            addInterval(new int[] {l, seat});
            addInterval(new int[] {seat, r});
            // 返回安排的位置
            return seat;
        }

        // 坐在 p 位置的考生离开了
        // 题目可以认为 p 位置一定坐有考生
        public void leave(int p) {
            // 把以p为右端点的线段和以p为左端点的线段合并成一个
            int[] left = endMap.get(p);
            int[] right = startMap.get(p);
            // 先移除这两个线段
            removeInterval(right);
            removeInterval(left);
            // 再把合并后的新线段加入
            addInterval(new int[]{left[0], right[1]});
        }

        /* 去除一个线段 */
        private void removeInterval(int[] intv) {
            // 从线段集合中移除此线段
            intervals.remove(intv);
            // 移除这个点关联的两个线段的映射
            startMap.remove(intv[0]);
            endMap.remove(intv[1]);
        }

        /* 增加一个线段 */
        private void addInterval(int[] intv) {
            // 集合中插入线段
            intervals.add(intv);
            // 增加这个点关联的两个线段的映射
            startMap.put(intv[0], intv);
            endMap.put(intv[1], intv);
        }

        /**
         * 返回区间[a,b]代表的线段的一半长度，有效位置从 0 到 N - 1, -1 和 N 是边界
         * 注意这个一半实际代表的是将新的点放入中点后，会距离左端多远
         * 对于 a=-1的特殊情况，下一次必然选择0位置，此时它对应的一半距离其实就是选择0位置后与右端点的距离，即 b
         * 对于 b=n的特殊情况，下一次必然选择n-1位置，此时它对应的一半距离其实就是选择n-1位置后与左端点的距离，即 n-1-a
         * @param intv
         * @return
         */
        private int midLengthOf(int[] intv) {
            // [l , r]
            int l = intv[0], r = intv[1];
            // 下一次一定安排在0位置，所以距离就是r
            if (l == -1) return r;
            // 下一次一定安排在n-1位置，所以距离是N-1-l
            if (r == N) return N - 1 - l;
            // 上面两种相当于特殊情况
            // -1 . . l . . . r. . .N，下一次一定安排在l和r中间
            return (r - l) / 2;
        }

    }
}
