package com.daily;

import java.util.PriorityQueue;

/**
 * @author wangwei
 * @date 2023/2/19 11:39
 * @description: _1792_MaximumAveragePassRatio
 *
 * 1792. 最大平均通过率
 * 一所学校里有一些班级，每个班级里有一些学生，现在每个班都会进行一场期末考试。给你一个二维数组 classes ，其中 classes[i] = [passi, totali] ，表示你提前知道了第 i 个班级总共有 totali 个学生，其中只有 passi 个学生可以通过考试。
 *
 * 给你一个整数 extraStudents ，表示额外有 extraStudents 个聪明的学生，他们 一定 能通过任何班级的期末考。你需要给这 extraStudents 个学生每人都安排一个班级，使得 所有 班级的 平均 通过率 最大 。
 *
 * 一个班级的 通过率 等于这个班级通过考试的学生人数除以这个班级的总人数。平均通过率 是所有班级的通过率之和除以班级数目。
 *
 * 请你返回在安排这 extraStudents 个学生去对应班级后的 最大 平均通过率。与标准答案误差范围在 10-5 以内的结果都会视为正确结果。
 *
 *
 *
 * 示例 1：
 *
 * 输入：classes = [[1,2],[3,5],[2,2]], extraStudents = 2
 * 输出：0.78333
 * 解释：你可以将额外的两个学生都安排到第一个班级，平均通过率为 (3/4 + 3/5 + 2/2) / 3 = 0.78333 。
 * 示例 2：
 *
 * 输入：classes = [[2,4],[3,9],[4,5],[2,10]], extraStudents = 4
 * 输出：0.53485
 *
 *
 * 提示：
 *
 * 1 <= classes.length <= 105
 * classes[i].length == 2
 * 1 <= passi <= totali <= 105
 * 1 <= extraStudents <= 105
 * 通过次数8,557提交次数16,586
 */
public class _1792_MaximumAveragePassRatio {


    /**
     * 方法：贪心 + 优先队列
     *
     * 思路与算法
     *
     * 由于班级总数不会变化，因此题目所求「最大化平均通过率」等价于「最大化总通过率」。
     *
     * 设某个班级的人数为 total，其中通过考试的人数为 pass，那么给这个班级安排一个额外通过考试的学生，其通过率会增加：
     *
     * (pass + 1) / (total + 1) - pass / total
     *
     * 我们每次从 extraStudents 取出一人，将其安排到 通过率增加量 最大 的班级，直到安排完所有 extraStudents
     *
     * 因此，使用优先队列维护所有班级情况，排序规则为 按照增加一个必过学生后班级通过率的提升量 降序排列
     *
     * 然后，每次从 extraStudents 取出一人，取出队列队首元素代表的班级，将其安排到此版中，得到'新的班级'加入队列
     *
     * 最后，遍历队列，累计所有班级的通过率，计算并返回平均通过率
     *
     * 【疑问】
     * 为什么不把 extraStudents 全部安排到一个班级上去 ？
     *
     * 逐个安排 做法的正确性在于 给同一个班级不断地增加安排的学生数量时，其增加的通过率是单调递减的，即：
     *
     * (pass+2) / (total+2) - (pass+1) / total  <  (pass+1) / (total+1) - pass / total
     *
     *
     * 因此我们逐个安排，每一次操作，我们取出优先队列的堆顶元素，令其 pass 和 total 分别加 1，然后再放回优先队列。
     *
     * 最后我们遍历优先队列的每一个班级，计算其平均通过率即可得到答案。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/maximum-average-pass-ratio/solution/zui-da-ping-jun-tong-guo-lu-by-leetcode-dm7y3/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param classes
     * @param extraStudents
     * @return
     */
    public double maxAverageRatio(int[][] classes, int extraStudents) {
        // 使用优先队列维护所有班级情况，排序规则为 按照增加一个必过学生后班级通过率的提升量 降序排列
        PriorityQueue<int[]> pq = new PriorityQueue<>((cls1, cls2) -> {
            int pass1 = cls1[0], total1 = cls1[1];
            int pass2 = cls2[0], total2 = cls2[1];
            // 班级一通过率提升量
            double add1 = 1.0 * (pass1 + 1) / (total1 + 1) - 1.0 * pass1 / total1;
            // 班级二通过率提升量
            // 倒序排列
            double add2 = 1.0 * (pass2 + 1) / (total2 + 1) - 1.0 * pass2 / total2;
            return Double.compare(add2, add1);
        });
        // 加入所有班级
        for (int[] cls : classes) {
            pq.offer(cls);
        }
        // 逐个安排必过学生
        while (extraStudents-- > 0) {
            // 取出队首班级
            int[] cls = pq.poll();
            // 更新后加入队列
            pq.offer(new int[]{cls[0] + 1, cls[1] + 1});
        }
        double ans = 0;
        // 计算安排完毕后所有班级的总通过率
        while (!pq.isEmpty()) {
            int[] cls = pq.poll();
            ans += 1.0 * cls[0] / cls[1];
        }
        // 返回平均值
        return ans / classes.length;
    }
}
