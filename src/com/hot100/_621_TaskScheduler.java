package com.hot100;

/**
 * @author wangwei
 * @date 2022/5/7 10:53
 * @description: _621_TaskScheduler
 *
 * 621. 任务调度器
 * 给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。
 *
 * 然而，两个 相同种类 的任务之间必须有长度为整数 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
 *
 * 你需要计算完成所有任务所需要的 最短时间 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：tasks = ["A","A","A","B","B","B"], n = 2
 * 输出：8
 * 解释：A -> B -> (待命) -> A -> B -> (待命) -> A -> B
 *      在本示例中，两个相同类型任务之间必须间隔长度为 n = 2 的冷却时间，而执行一个任务只需要一个单位时间，所以中间出现了（待命）状态。
 * 示例 2：
 *
 * 输入：tasks = ["A","A","A","B","B","B"], n = 0
 * 输出：6
 * 解释：在这种情况下，任何大小为 6 的排列都可以满足要求，因为 n = 0
 * ["A","A","A","B","B","B"]
 * ["A","B","A","B","A","B"]
 * ["B","B","B","A","A","A"]
 * ...
 * 诸如此类
 * 示例 3：
 *
 * 输入：tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
 * 输出：16
 * 解释：一种可能的解决方案是：
 *      A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> (待命) -> (待命) -> A -> (待命) -> (待命) -> A
 *
 *
 * 提示：
 *
 * 1 <= task.length <= 104
 * tasks[i] 是大写英文字母
 * n 的取值范围为 [0, 100]
 */
public class _621_TaskScheduler {


    /**
     * 贪心思想，统计每个任务出现的次数，找到出现次数最多的任务，假设是A，它的次数是 maxCount
     * 假设不存在其他任务，每两个A之间也要等冷却时间n，假设n=2，maxCount=3,那么至少序列为
     *      A X X A X X A，此时序列长度为 7 = (maxCount - 1) * (n + 1) + 1
     * 所以， 至少需要 ret = (maxCount - 1) * (n + 1) + 1
     *
     * 此时为了尽量利用X所预占的空间（贪心）使得整个执行序列长度尽量小，将剩余任务往X预占的空间插入
     * 对于剩余的每一类任务(B或C或D)，它的任务次数有两种情况：
     *      1.与A出现次数相同 (假设B有3次)，
     *          那么B任务最优插入结果是  A B X A B X A B，在每两个A之间占掉一个空位，但由于B和A数量一致，所以最终还得再站一个位置，那么 ret += 1
     *      2.比A出现次数少（假设C，D有两次），
     *           如果还有剩余空位，比如 上面情况还剩余两个空位，那么继续站空位 A B C A B C A B，此时序列长度并未增多
     *           若没有空位了（在上面的基础上），由于每一次的ABC之间都保证了间隔>=n，所以我在每一个ABC之后插入一个D，救能保证时间间隔满足要求
     *              A B C D A B C D A B D，此时发现，多占了3个位置，假设没有其他任务了，会发现此时序列长度正好是初始数组的任务个数
     *
     *
     * 也就是说，假设冷却间隔为n，统计每个任务次数，假设最大的次数是maxCount，对应任务是A
     * 那么返回值 ret 至少为 (maxCount - 1) * (n + 1) + 1
     *
     * 对于剩下的每一类任务，
     *      如果它的出现次数和maxCount一样，最后会多占1个位置，也就是 ret++
     *      如果它的次数小于maxCount，
     *          如果还有A留下的空位，那么它 继续使用 A 留下的空位，不会产生新的空位需求
     *          如果空位没有了，将其分别插入到 之前状态的 每个重复单元后面(比如上面的ABC ABC AB)，也能保证间隔，最后会多出新的空位，但是全部插完后此时序列长度正好是整个初始数组任务数
     * 所以，合并起来就是 ret =  Math.max(ret, len);
     *
     * 完整示例：
     *        举例子：A*6; B*6; C*5; D*4; E*3; F*2; n=2; maxCount = 6
     *        插6个A，  A X X | A X X | A X X | A X X | A X X | A                  base  minlen = (6 - 1) * (2 + 1) + 1 = 16;
     *        插6个B，  A B X | A B X | A B X | A B X | A B X | A B                B=A=6, 多占一个位置，因此 minlen += 1;
     *        插5个C，  A B C | A B C | A B C | A B C | A B C | A B                C=5 < 6，空位够用，不产生新的占位需求
     *        插4个D，  A B C D | A B C D | A B C D | A B C D | A B C | A B        此时把D往后(每个重复单元后)插入，同样满足冷却要求，此时长度超过了minlen，但这个时候每个字母之间都没有空位置
     *        插3个E，  A B C D E | A B C D E | A B C D E | A B C D | A B C D | A B C D | A B C | A B        与D类似，插入E的时候同样不会多出多的空位
     *        插2个F，  A B C D E F | A B C D E F | A B C D E | A B C D | A B C D | A B C D | A B C | A B    与E类似，插入F时不会多出空位置
     *              全部插完后发现 ，长度刚好等于tasks.length；
     *
     * 或者看这个桶的思想解释：https://leetcode-cn.com/problems/task-scheduler/solution/tong-zi-by-popopop/
     * @param tasks
     * @param n
     * @return
     */
    public int leastInterval(char[] tasks, int n) {
        // 只有1个任务，或者无需间隔
        if (tasks.length == 1 || n == 0) {
            return tasks.length;
        }
        int len = tasks.length;
        // 统计每个任务出现次数
        int[] count = new int[26];
        // 记录次数最多的那类任务的个数
        int maxCount = 0;
        for (int i = 0; i < len; ++i) {
            int idx = tasks[i] - 'A';
            count[idx]++;
            if (count[idx] > maxCount) {
                maxCount = count[idx];
            }
        }
        // 最基本的序列， A X X A X X A
        int ret = (maxCount - 1) * (n + 1) + 1;
        // 对于其他任务
        for (int i = 0; i < 26; ++i) {
            // 如果它的次数和maxCount一样，那么最后会多占一个位置
            if (count[i] == maxCount) {
                ret++;
            }
            // 比maxCount小，如果还有空位，就不会产生新的占位，如果没有空位，也能间隔插入，会产生多的空位，但是最后会沾满整个序列
        }
        // 上面考虑的是其他任务，但是for循环无条件之间，所以相当于 maxCount成立的条件多触发了一次，这里纠正
        ret--;

        // 最后这个max比较，就是区分那些在已经没空位的情况下完成插入的任务，最终序列长度会=task.length
        return Math.max(ret, len);
    }
}
