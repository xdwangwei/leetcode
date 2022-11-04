package com.daily;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/11/4 11:44
 * @description: _754_ReachANumber
 *
 * 754. 到达终点数字
 * 在一根无限长的数轴上，你站在0的位置。终点在target的位置。
 *
 * 你可以做一些数量的移动 numMoves :
 *
 * 每次你可以选择向左或向右移动。
 * 第 i 次移动（从  i == 1 开始，到 i == numMoves ），在选择的方向上走 i 步。
 * 给定整数 target ，返回 到达目标所需的 最小 移动次数(即最小 numMoves ) 。
 *
 *
 *
 * 示例 1:
 *
 * 输入: target = 2
 * 输出: 3
 * 解释:
 * 第一次移动，从 0 到 1 。
 * 第二次移动，从 1 到 -1 。
 * 第三次移动，从 -1 到 2 。
 * 示例 2:
 *
 * 输入: target = 3
 * 输出: 2
 * 解释:
 * 第一次移动，从 0 到 1 。
 * 第二次移动，从 1 到 3 。
 *
 *
 * 提示:
 *
 * -109 <= target <= 109
 * target != 0
 */
public class _754_ReachANumber {


    /**
     * bfs，内存溢出
     * 第一次能走1步，第二次能走2步。。。每次可选择向左向右走，
     * bfs，第x层的节点的两个孩子是 val + x，val - x
     * 到达target时，返回所处层数
     * @param target
     * @return
     */
    public int reachNumber(int target) {
        if (target == 0) {
            return 0;
        }
        // bfs，从 0 到 target
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        int step = 1;
        while (!queue.isEmpty()) {
            // 当前层的每个节点
            int sz = queue.size();
            for (int i = 0; i < sz; ++i) {
                Integer cur = queue.poll();
                // 若左右孩子=target，返回层数
                if (cur - step == target || cur + step == target) {
                    return step;
                }
                // 左右孩子
                queue.offer(cur - step);
                queue.offer(cur + step);
            }
            step++;
        }
        return step;
    }

    /**
     * 数学方法
     *
     * 提示一：数轴上的任意点都以起点（0 点）对称，只需要考虑对称点的任意一边
     *
     * 即：左边所能到达任意一个点，都能通过调整所达路径的方向来将终点调整到右边。
     *
     * 提示二：先往靠近 target 的方向移动，到达或越过 target 的时候则停止
     * 只考虑 target 为正的情况，我们假定起始先往靠近 target 的方向移动（即所有步数均为正值），根据是「到达」还是「越过」target 位置分情况讨论：
     *
     * 若能直接到达 target，此时消耗的必然是最小步数，可直接返回；
     * 若越过了 target，假设此时消耗的步数为 k，所走的距离为 dist = k×(k+1)/2 >target，我们可以考虑是否需要增加额外步数来到达 target。
     *
     * 提示三：越过 target 时，如何不引入额外步数
     * 若不引入额外步数，意味着我们需要将此前某些移动的方向进行翻转，使得调整后的 dist=target。
     *
     * 我们假设需要调整的步数总和为 tot，则有 dist−2×tot=target，变形可得 tot = (dist−target) / 2。
     *
     * 若想满足上述性质，需要确保能找到这样的 tot，即 tot 合法，
     *
     * 不难推导出当 dist 和 target 差值为「偶数」时（两者奇偶性相同），我们可以找到这样的 tot，从而实现不引入额外步数来到达 target 位置。
     *
     * 由于我们的 dist 是由数列 [1,2,3,...,k] 累加而来，因此必然能够在该数列 [1,2,3...k] 中通过「不重复选择某些数」来凑成任意一个小于等于 dist 的数。
     *
     * 提示四：越过 target 时，如何尽量减少引入额外步数
     *
     * 当 dist 和 target 差值不为「偶数」时，我们只能通过引入额外步数（继续往右走）来使得，两者差值为偶数。
     *
     * 可以证明，最多引入步数不超过 4 步，可使用得两者奇偶性相同，即不超过 4 步可以覆盖到「奇数」和「偶数」两种情况。
     *
     * 提示五：如何不通过「遍历」或「二分」的方式找到一个合适的 k 值，再通过不超过 4 步的调整找到答案
     *
     * 我们期望找到一个合适的 k 值，使得 dist = k×(k+1) / 2 <target，随后通过增加 k 值来找到答案。
     *
     * 利用求和公式 dist= k×(k+1)/2，我们可以设定 k = ⌊sqrt(2*target)⌋ 为起始值，随后逐步增大 k 值，直到满足「dist 和 target 奇偶性相同」。
     *
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/reach-a-number/solution/by-ac_oier-o4ze/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param target
     * @return
     */
    public int reachNumber2(int target) {
        // 只考虑正方向
        if (target < 0) target = -target;
        // 先往靠近 target 的方向移动，到达或越过 target 的时候则停止
        // 假设走了k步，那么距离为 dist = k×(k+1)，找到最逼近 target 的 k = ⌊sqrt(2*target)⌋
        int k = (int) Math.sqrt(2 * target), dist = k * (k + 1) / 2;
        // 若 dist和target奇偶性相同，则不用增加额外步数，只需调整中间某些步的方向，就能让dist=target
        // 否则需要增加额外步数，让 dist 和 target 的差值达到偶数，这个过程最多不会超过4
        while (dist < target || (dist - target) % 2 == 1) {
            k++;
            // 重新计算距离，这里可以简化计算过程
            // (k + 1) * (k + 2) / 2 - (k) * (k + 1) / 2 = k + 1，
            // 此时 k 已经变为 k + 1，可以简写为 dist += k
            // dist = k * (k + 1) / 2;
            dist += k;
        }
        return k;
    }

    public static void main(String[] args) {
        _754_ReachANumber obj = new _754_ReachANumber();
        for (int i = -109; i < 109; ++i) {
            System.out.println("i: "+ i + ", ans: " + obj.reachNumber(i));
        }
    }
}
