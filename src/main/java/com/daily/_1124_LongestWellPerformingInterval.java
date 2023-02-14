package com.daily;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/2/14 10:52
 * @description: _1124_LongestWellPerformingInterval
 *
 * 1124. 表现良好的最长时间段
 * 给你一份工作时间表 hours，上面记录着某一位员工每天的工作小时数。
 *
 * 我们认为当员工一天中的工作小时数大于 8 小时的时候，那么这一天就是「劳累的一天」。
 *
 * 所谓「表现良好的时间段」，意味在这段时间内，「劳累的天数」是严格 大于「不劳累的天数」。
 *
 * 请你返回「表现良好时间段」的最大长度。
 *
 *
 *
 * 示例 1：
 *
 * 输入：hours = [9,9,6,0,6,6,9]
 * 输出：3
 * 解释：最长的表现良好时间段是 [9,9,6]。
 * 示例 2：
 *
 * 输入：hours = [6,6,6]
 * 输出：0
 *
 *
 * 提示：
 *
 * 1 <= hours.length <= 104
 * 0 <= hours[i] <= 16
 * 通过次数24,444提交次数68,113
 */
public class _1124_LongestWellPerformingInterval {

    /**
     * 方法一：单调栈
     *
     * 先把问题转换到我们熟悉的东西上。
     *
     * 「劳累天数大于不劳累天数」等价于「劳累天数减去不劳累天数大于 0」。
     *
     * 那么把劳累的一天视作 nums[i]=1，不劳累的一天视作 nums[i]=−1，则问题变为：
     *
     * 计算 nums 的最长子数组，其元素和大于 0。
     *
     * 既然说到了「子数组的元素和」，那么利用前缀和 s，将问题变为：
     *
     * 找到两个下标 i 和 j，满足 j<i 且 s[j]<s[i]，最大化 i−j 的值。
     *
     * 朴素做法：双重循环，枚举 左、右端点。
     *
     * 【优化】
     *
     * 想一想，哪些值可以作为 j（最长子数组的左端点）呢？
     * 设遍历到 s[i] 时 有 s[i] ≥ s[j] (j < i),
     * 如果 i 可以成为最长子数组的左端点，那么后面必然还有 s[k] > s[i]，则必然也有 s[k]>s[j]
     * 由于 j 离 k 更远，因此 i 不可能是最长子数组的左端点。
     * 因此，在遍历 s 时，所有可能的左端点 s[j] 满足一个单调递减栈（从栈底往栈顶）
     *
     * 对于每个可能的左端点 j，考虑右端点
     *
     * 对于栈顶 s[j], 我们需要考虑比它大的最远的s[i], 那么倒序遍历s,一旦s[i]比栈顶s[j]大，就更新答案并出栈。
     * （只要s[i] > s[j]，那么 i 左边未遍历的 s[k] (j < k < i) 就不可能是 j 的右端点了，因为 i 是最优的，所以 j 出栈）
     *
     * 疑问？
     * 感觉好像把 左右端点分开讨论了？常规情况下是对于每个左端点讨论右端点，再继续下一个左端点。。。
     * 具体由双重for循环优化思考到单调栈的过程可以参考：
     * https://leetcode.cn/problems/longest-well-performing-interval/solution/can-kao-liao-ji-ge-da-shen-de-ti-jie-zhi-hou-zong-/
     * 通常的单调栈题目（例如 496. 下一个更大元素 I）都是求的「最近」或「最短」，本题求的是「最长」，不能一概而论。
     * 就当成是新的模板（划掉）
     * 962. 最大宽度坡 和本题非常像
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/longest-well-performing-interval/solution/liang-chong-zuo-fa-liang-zhang-tu-miao-d-hysl/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param hours
     * @return
     */
    public int longestWPI(int[] hours) {
        int n = hours.length;
        // 前缀和数组 s[0] = 0, s[i] = sum(hours[0...i-1]), sum(hours[i...j]) = s[j+1] - s[i]
        // 找到两个下标 i 和 j，满足 j<i 且 s[j]<s[i]，最大化 i−j 的值。
        int[] sum = new int[n + 1];
        // 可能的左端点，满足 sum 值递减，形成单调栈
        Deque<Integer> stack = new ArrayDeque<>();
        // sum[0] = 0，加入 0
        stack.push(0);
        // 迭代得到 sum[i]
        for(int i = 1; i <= n; ++i) {
            sum[i] = sum[i - 1] + (hours[i - 1] > 8 ? 1 : -1);
            // 若满足单调性，加入栈顶
            if (stack.isEmpty() || sum[i] < sum[stack.peek()]) {
                stack.push(i);
            }
        }
        // 求最大间隔，初始化ans为0
        int ans = 0;
        // 从右往左遍历 sum
        for(int i = n; i >= 0; --i) {
            // 对于栈顶s[j]，遇到从右往左的第一个i满足 s[i] > s[j]，则 i 就是它的右端点，使它的最优选择，出栈
            while (!stack.isEmpty() && sum[i] > sum[stack.peek()]) {
                // 更新答案
                ans = Math.max(ans, i - stack.pop());
            }
        }
        // 返回
        return ans;
    }


    /**
     * 方法二：前缀和 + 哈希表
     *
     * 方法一更加通用，此方法基于 前缀和数组 sum 相邻两个位置元素 相差为1
     *
     * 我们可以利用前缀和的思想，维护一个变量 s，表示从下标 0 到当前下标的这一段，「劳累的天数」与「不劳累的天数」的差值。
     * 如果 s 大于 0，说明从下标 0 到当前下标的这一段，满足「表现良好的时间段」。
     * 另外，用哈希表 pos 记录每个 s 【第一次】出现的下标。
     *
     * 接下来，我们遍历数组 hours，对于每个下标 i：
     *
     * 如果 hours[i] > 8，我们就让 s 加 1，否则减 1。
     * 如果 s 大于 0，说明从下标 0 到当前下标的这一段，满足「表现良好的时间段」，我们更新结果 ans=i+1。
     * 否则，如果 s−1 在哈希表 pos 中，记 j=pos[s−1]，
     *      说明从下标 j+1 到当前下标 i 的这一段，满足「表现良好的时间段」，累加和为 s - (s-1) = 1 > 0
     *      我们更新结果 ans=max(ans, i−j)。
     * 如果 s 【不在】哈希表 pos 中，我们就记录 pos[s]=i。继续遍历下一个。
     *
     * 为什么当 s <= 0 时 不考虑 s-2,s-3 ？？？
     * 假如 s = -1，因为 s 是从0开始的，所以中间过程必然类似 0 -> -1 -> -2 -> -3 -> -4 -> ... -> -3 -> -2
     * 第一次遇到 s=0、-1、-2、-3、-4，记录下标，此时 s-1、s-2、s-3、都不在 hash表中，不用考虑
     * 只有 第二次及之后遇到 s=-2 时，s-1、s-2、s-3、这些才存在于 哈希表中，此时才需要讨论是否考虑
     * 这种情况下，s-1 = -2，s-2 = -3，可以看到 必然是先从0经过-1、再经过-2、再更小、再增加、回到-3、经过-2
     * 那么 pos[s-1] 肯定是 小于 pos[s-2] 的，所以 只需要考虑 s-1
     *
     * 简单思路：
     * 首先对于 s > 0 的时刻，直接更新 ans，
     * 对于 s <= 0 部分将 s 的变化路径想象成 " V " 形状，
     *      在 ”下坡“阶段，s一直变小，每个s都是第一次出现，s-1、s-2、根本不存在于 哈希表，不需要考虑，
     *              或者说，0到这些位置的累加和都是非正数，都是无效的
     *      在”上坡“阶段，因为开始上坡了，所以才存在「表现良好的时间段」，此时 s-1、s-2、已经存在于哈希表中
     *              哈希表记录了s-1、s-2第一次出现的位置，s-1必然在s-2更左的位置（下坡先s-1、再s-2），所以只考虑s-1
     *
     * 遍历结束后，返回答案即可。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/longest-well-performing-interval/solution/python3javacgo-yi-ti-yi-jie-qian-zhui-he-0os2/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param hours
     * @return
     */
    public int longestWPI2(int[] hours) {
        int ans = 0, s = 0;
        // 记录每个 s 值第一次出现的位置
        Map<Integer, Integer> pos = new HashMap<>();
        // 遍历
        for (int i = 0; i < hours.length; ++i) {
            // 累加（+1 或 -1）
            s += hours[i] > 8 ? 1 : -1;
            // 说明从下标 0 到当前下标的这一段，满足「表现良好的时间段」，我们更新结果 ans=i+1。
            if (s > 0) {
                ans = i + 1;
            // 从 s-1 到 s这一段必然满足 「表现良好的时间段」，当然从 s-2 到 s 也满足
            // 但是，记录 s-1 的位置 必然先于 记录 s-2
            // 因此，只考虑 s-1
            } else if (pos.containsKey(s - 1)) {
                // 更新答案
                ans = Math.max(ans, i - pos.get(s - 1));
            }
            // 记录 s 【首次】出现位置
            pos.putIfAbsent(s, i);
        }
        // 返回
        return ans;
    }

    public static void main(String[] args) {
        _1124_LongestWellPerformingInterval obj = new _1124_LongestWellPerformingInterval();
        obj.longestWPI(new int[]{9, 9, 6, 0, 6, 6, 9});
    }
}
