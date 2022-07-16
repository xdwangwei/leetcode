package com.order;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author wangwei
 * 2020/9/1 12:12
 * 在考场里，一排有N个座位，分别编号为0, 1, 2, ..., N-1。
 *
 * 当学生进入考场后，他必须坐在能够使他与离他最近的人之间的距离达到最大化的座位上。如果有多个这样的座位，他会坐在编号最小的座位上。(另外，如果考场里没有人，那么学生就坐在 0 号座位上。)
 *
 * 返回ExamRoom(int N)类，它有两个公开的函数：其中，函数ExamRoom.seat()会返回一个int（整型数据），代表学生坐的位置；函数ExamRoom.leave(int p)代表坐在座位 p 上的学生现在离开了考场。每次调用ExamRoom.leave(p)时都保证有学生坐在座位p上。
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
 * 在所有的测试样例中ExamRoom.seat()和ExamRoom.leave()最多被调用10^4次。
 * 保证在调用ExamRoom.leave(p)时有学生正坐在座位 p 上。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/exam-room
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 */
public class _855_ExamRoom {

    class Solutin {

        /**
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
         * 为了操作统一，我们初始情况下给集合中加入一个虚拟线段([-1, N])，类似于列表头结点的作用
         * 所以关于线段[a,b]的操作中，需要考虑a=-1和y=N的情况
         *
         * 问题一：
         * 因为leave的时候要知道某个点p相连的两个线段，也就是p为右端点的线段和p为左端点的线段。
         * 所以我们使用两个map。leftMap和rightMap分别保存每个点作为左端点的线段和它作为右端点的线段。
         * 这样在合并的时候我们能快速找到这两个线段。
         *
         * 问题二：
         * 红黑树集合中线段按照长度排序(最长的在最后)，每次seat就取出最后一个元素(元素)，
         * 然后将新的点放在这个线段的中间。
         * 现在有序集合里有线段 [0,4] 和 [4,9]，那么最长线段 longest 就是后者[4,9]，
         * 按照 seat 的逻辑，就会分割 [4,9]，也就是返回座位 6。但正确答案应该是座位 2，
         * 因为 [0,2][2,4] 和 [4,6][6,9] 都满足最大化相邻考生距离的条件，题目要求有多个选择时取索引较小的。
         *
         * 那么要如何设计排序规则：
         *      我们发现这种情况的问题就在于虽然这两个线段长度不一样，
         *      但是他们的一半是一样的 (right - left) / 2，实际上是选择这两个线段进行分割后的效果是一样的
         *      我们可以让排序按照线段长度的一半进行比较，然后如果相等的话，我们让索引更小的线段排到更后面
         *      然后每次取last的时候，多个选择的情况下救能选出索引最小的那个线段
         *
         *
         *
         *
         * @param N
         */

        // 将端点 p 映射到以 p 为左端点的线段
        private Map<Integer, int[]> startMap;
        // 将端点 p 映射到以 p 为右端点的线段
        private Map<Integer, int[]> endMap;
        // 根据线段长度从小到大存放所有线段
        private TreeSet<int[]> pq;
        private int N;

        // 构造函数，传入座位总数 N
        public Solutin(int N) {
            this.N = N;
            startMap = new HashMap<>();
            endMap = new HashMap<>();
            pq = new TreeSet<>((intv1, intv2) -> {
                int len1 = lengthOf(intv1);
                int len2 = lengthOf(intv2);
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
            int[] longest = pq.last();
            int l = longest[0], r = longest[1];
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
        // 可以认为 p 位置一定坐有考生
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
            // 集合中移除线段
            pq.remove(intv);
            // 这个点关联的两个线段的映射
            startMap.remove(intv[0]);
            endMap.remove(intv[1]);
        }

        /* 增加一个线段 */
        private void addInterval(int[] intv) {
            // 集合中插入线段
            pq.add(intv);
            // 这个点关联的两个线段的映射
            startMap.put(intv[0], intv);
            endMap.put(intv[1], intv);
        }

        /**
         * 返回区间[a,b]代表的线段的一半长度，有效位置从 0 到 N - 1, -1 和 N 是边界
         * 注意这个一半实际代表的是将新的点放入中点后，会距离左端多远
         * @param intv
         * @return
         */
        private int lengthOf(int[] intv) {
            // [l , r]
            int l = intv[0], r = intv[1];
            // -1(l) . . . 人(r) . . . N，下一次一定安排在左边第一个点，所以距离就是r
            if (l == -1) return r;
            // -1 人(l) . . . . .N(r)，下一次一定安排在右边第一个点，所以距离是N-l-1
            if (r == N) return N - l - 1;
            // 上面两种相当于特殊情况
            // -1 . . l . . . r. . .N，下一次一定安排在l和r中间
            return (r - l) / 2;
        }

    }


}
