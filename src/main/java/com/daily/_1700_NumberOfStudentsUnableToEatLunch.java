package com.daily;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/10/19 11:55
 * @description: _1700_NumberOfStudentsUnableToEatLunch
 *
 * 1700. 无法吃午餐的学生数量
 * 学校的自助午餐提供圆形和方形的三明治，分别用数字 0 和 1 表示。所有学生站在一个队列里，每个学生要么喜欢圆形的要么喜欢方形的。
 * 餐厅里三明治的数量与学生的数量相同。所有三明治都放在一个 栈 里，每一轮：
 *
 * 如果队列最前面的学生 喜欢 栈顶的三明治，那么会 拿走它 并离开队列。
 * 否则，这名学生会 放弃这个三明治 并回到队列的尾部。
 * 这个过程会一直持续到队列里所有学生都不喜欢栈顶的三明治为止。
 *
 * 给你两个整数数组 students 和 sandwiches ，其中 sandwiches[i] 是栈里面第 i​​​​​​ 个三明治的类型（i = 0 是栈的顶部）， students[j] 是初始队列里第 j​​​​​​ 名学生对三明治的喜好（j = 0 是队列的最开始位置）。请你返回无法吃午餐的学生数量。
 *
 *
 *
 * 示例 1：
 *
 * 输入：students = [1,1,0,0], sandwiches = [0,1,0,1]
 * 输出：0
 * 解释：
 * - 最前面的学生放弃最顶上的三明治，并回到队列的末尾，学生队列变为 students = [1,0,0,1]。
 * - 最前面的学生放弃最顶上的三明治，并回到队列的末尾，学生队列变为 students = [0,0,1,1]。
 * - 最前面的学生拿走最顶上的三明治，剩余学生队列为 students = [0,1,1]，三明治栈为 sandwiches = [1,0,1]。
 * - 最前面的学生放弃最顶上的三明治，并回到队列的末尾，学生队列变为 students = [1,1,0]。
 * - 最前面的学生拿走最顶上的三明治，剩余学生队列为 students = [1,0]，三明治栈为 sandwiches = [0,1]。
 * - 最前面的学生放弃最顶上的三明治，并回到队列的末尾，学生队列变为 students = [0,1]。
 * - 最前面的学生拿走最顶上的三明治，剩余学生队列为 students = [1]，三明治栈为 sandwiches = [1]。
 * - 最前面的学生拿走最顶上的三明治，剩余学生队列为 students = []，三明治栈为 sandwiches = []。
 * 所以所有学生都有三明治吃。
 * 示例 2：
 *
 * 输入：students = [1,1,1,0,0,1], sandwiches = [1,0,0,0,1,1]
 * 输出：3
 *
 *
 * 提示：
 *
 * 1 <= students.length, sandwiches.length <= 100
 * students.length == sandwiches.length
 * sandwiches[i] 要么是 0 ，要么是 1 。
 * students[i] 要么是 0 ，要么是 1 。
 */
public class _1700_NumberOfStudentsUnableToEatLunch {

    /**
     * 队列模拟，逐个遍历栈顶三明治，按提议堆栈进行操作，如果 某一次，对于栈顶三明治，没人吃，就结束
     * @param students
     * @param sandwiches
     * @return
     */
    public int countStudents(int[] students, int[] sandwiches) {
        // 学生入队
        Deque<Integer> deque = new ArrayDeque<>();
        for (int stu : students) {
            deque.addLast(stu);
        }
        // 逐个遍历栈顶三明治
        for (int i = 0; i < sandwiches.length; ++i) {
            int j = 0;
            while (!deque.isEmpty()) {
                // 有人吃，下一个栈顶
                if (deque.peekFirst() == sandwiches[i]) {
                    deque.removeFirst();
                    break;
                }
                // 没人吃，
                deque.addLast(deque.removeFirst());
                // 没人吃的学生数=队列人数，结束
                if (++j == deque.size()) {
                    return j;
                }
            }
        }
        // 返回队列剩余人数
        return deque.size();
    }

    /**
     * 简单写法
     *
     * 假设喜欢吃圆形三明治的学生数量为 s0，喜欢吃方形三明治的学生数量为 s1
     * 栈顶的三明治能否被拿走取决于队列剩余的学生中是否有喜欢它的，而学生在队列的相对位置不影响整个过程（学生可以出队再入队，或者说三明治不关心哪个学生吃，只要有人吃就行），
     * 因此只需要记录队列剩余的学生中 s0 和 s1 的值。
     *
     * 我们对整个过程进行模拟，
     * 如果栈顶的元素为 0 ：
     *      如果 s0 == 0，说明已经没人喜欢0号了，那么此时返回s1，因为剩下的都是喜欢1号的
     *      否则，s0--，当前栈顶被消费
     * 如果栈顶的元素为 1 ：
     *      如果 s1 == 0，说明已经没人喜欢1号了，那么此时返回s0，因为剩下的都是喜欢0号的
     *      否则，s1--，当前栈顶被消费
     * 最终返回 s0 + s1
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/number-of-students-unable-to-eat-lunch/solution/wu-fa-chi-wu-can-de-xue-sheng-shu-liang-fv3f5/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param students
     * @param sandwiches
     * @return
     */
    public int countStudents2(int[] students, int[] sandwiches) {
        // 吃圆形三明治的学生数量为 s0，喜欢吃方形三明治的学生数量为 s1
        int s0 = 0, s1 = 0;
        for (int s : students) {
            if (s == 0) {
                s0++;
            } else {
                s1++;
            }
        }
        // 遍历栈顶
        for (int s : sandwiches) {
            // 0号三明治
            if (s == 0) {
                // 没人喜欢吃0号了
                if (s0 == 0) {
                    return s1;
                }
                // 吃掉栈顶
                s0--;
            } else {
                // 没人喜欢1号了
                if (s1 == 0) {
                    return s0;
                }
                // 吃掉栈顶
                s1--;
            }
        }
        // 最后剩余学生，应该是0
        return s0 + s1;
    }

    /**
     * 更简单的写法
     * @param students
     * @param sandwiches
     * @return
     */
    public int countStudents3(int[] students, int[] sandwiches) {
        // 吃圆形三明治的学生数量为 cnt[0]，喜欢吃方形三明治的学生数量为 cnt[1]
        int[] cnt = new int[2];
        for (int v : students) ++cnt[v];
        // 0号三明治没人喜欢时，返回喜欢1号数量；1号三明治没人喜欢时，返回0号数量，这就是异或的作用
        for (int v : sandwiches) if (cnt[v]-- == 0) return cnt[v ^ 1];
        return 0;
    }
}
