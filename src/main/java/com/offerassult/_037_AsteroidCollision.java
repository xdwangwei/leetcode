package com.offerassult;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/10/31 15:58
 * @description: _037_AsteroidCollision
 *
 * 剑指 Offer II 037. 小行星碰撞
 * 给定一个整数数组 asteroids，表示在同一行的小行星。
 *
 * 对于数组中的每一个元素，其绝对值表示小行星的大小，正负表示小行星的移动方向（正表示向右移动，负表示向左移动）。每一颗小行星以相同的速度移动。
 *
 * 找出碰撞后剩下的所有小行星。碰撞规则：两个行星相互碰撞，较小的行星会爆炸。如果两颗行星大小相同，则两颗行星都会爆炸。两颗移动方向相同的行星，永远不会发生碰撞。
 *
 *
 *
 * 示例 1：
 *
 * 输入：asteroids = [5,10,-5]
 * 输出：[5,10]
 * 解释：10 和 -5 碰撞后只剩下 10 。 5 和 10 永远不会发生碰撞。
 * 示例 2：
 *
 * 输入：asteroids = [8,-8]
 * 输出：[]
 * 解释：8 和 -8 碰撞后，两者都发生爆炸。
 * 示例 3：
 *
 * 输入：asteroids = [10,2,-5]
 * 输出：[10]
 * 解释：2 和 -5 发生碰撞后剩下 -5 。10 和 -5 发生碰撞后剩下 10 。
 * 示例 4：
 *
 * 输入：asteroids = [-2,-1,1,2]
 * 输出：[-2,-1,1,2]
 * 解释：-2 和 -1 向左移动，而 1 和 2 向右移动。 由于移动方向相同的行星不会发生碰撞，所以最终没有行星发生碰撞。
 *
 *
 * 提示：
 *
 * 2 <= asteroids.length <= 104
 * -1000 <= asteroids[i] <= 1000
 * asteroids[i] != 0
 *
 *
 * 注意：本题与主站 735 题相同： https://leetcode-cn.com/problems/asteroid-collision/
 */
public class _037_AsteroidCollision {

    /**
     * 因为每个行星需要考虑它左右两边的行星，而考虑右边行星相当于右边行星考虑它左边行星，所以每个行星都存在存在【向后看】的过程，
     * 所以使用 栈 st 模拟行星碰撞，考虑每个行星左边情况
     *
     * 从左往右遍历行星数组 asteroids，当我们遍历到行星 aster 时，
     *
     * 如果它是向左的，并且栈顶（也就是它左边行星）是向右的，此时才会发生碰撞，使用变量 alive 记录当前行星 aster 是否能存活。
     *
     *      若栈顶质量小于当前行星，那么栈顶爆炸，当前行星存活，继续考虑更左边情况
     *      若栈顶质量等于当前行星，那么二者都爆炸，结束
     *      若栈顶质量大于当前行星，那么当前行星爆炸，结束
     *
     * 若栈空或爆炸后当前行星存活，那么当前行星入栈，
     *
     * 因为最后要返回所有初始元素，并保持相对顺序，所以栈中记录下标即可
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/asteroid-collision/solution/xing-xing-peng-zhuang-by-leetcode-soluti-u3k0/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param asteroids
     * @return
     */
    public int[] asteroidCollision(int[] asteroids) {
        Deque<Integer> stack = new ArrayDeque<>();
        // 逐个遍历每个行星
        for (int aster : asteroids) {
            // 假设它存活
            boolean alive = true;
            // 只有它向左，而栈顶向右时，才会发生爆炸
            // 若和左边某个行星爆炸后不再存活，则结束
            while (alive && aster < 0 && !stack.isEmpty() && stack.peek() > 0) {
                // 此时碰撞后是否能存活，不能存活就会退出while
                // 只有栈顶质量<自己时，才能存活
                alive = stack.peek() < -aster;
                // 栈顶行星质量更小，那么栈顶爆炸
                // 质量相当，栈顶爆炸，当前行星不再存活
                if (stack.peek() <= -aster) {
                    stack.pop();
                }
            }
            // 若栈空或者爆炸后，它还存活，那么它入栈
            if (alive) {
                stack.push(aster);
            }
        }
        // 栈中元素恢复到数组中，保留原相对顺序
        int size = stack.size();
        int[] ans = new int[size];
        for (int i = size - 1; i >= 0; i--) {
            ans[i] = stack.pop();
        }
        // 返回
        return ans;
    }
}
