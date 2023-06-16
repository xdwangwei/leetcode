package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/6/6 11:00
 * @description: _2611_MiceAndCheese
 *
 * 2611. 老鼠和奶酪
 * 有两只老鼠和 n 块不同类型的奶酪，每块奶酪都只能被其中一只老鼠吃掉。
 *
 * 下标为 i 处的奶酪被吃掉的得分为：
 *
 * 如果第一只老鼠吃掉，则得分为 reward1[i] 。
 * 如果第二只老鼠吃掉，则得分为 reward2[i] 。
 * 给你一个正整数数组 reward1 ，一个正整数数组 reward2 ，和一个非负整数 k 。
 *
 * 请你返回第一只老鼠恰好吃掉 k 块奶酪的情况下，最大 得分为多少。
 *
 *
 *
 * 示例 1：
 *
 * 输入：reward1 = [1,1,3,4], reward2 = [4,4,1,1], k = 2
 * 输出：15
 * 解释：这个例子中，第一只老鼠吃掉第 2 和 3 块奶酪（下标从 0 开始），第二只老鼠吃掉第 0 和 1 块奶酪。
 * 总得分为 4 + 4 + 3 + 4 = 15 。
 * 15 是最高得分。
 * 示例 2：
 *
 * 输入：reward1 = [1,1], reward2 = [1,1], k = 2
 * 输出：2
 * 解释：这个例子中，第一只老鼠吃掉第 0 和 1 块奶酪（下标从 0 开始），第二只老鼠不吃任何奶酪。
 * 总得分为 1 + 1 = 2 。
 * 2 是最高得分。
 *
 *
 * 提示：
 *
 * 1 <= n == reward1.length == reward2.length <= 105
 * 1 <= reward1[i], reward2[i] <= 1000
 * 0 <= k <= n
 * 通过次数6,431提交次数13,404
 */
public class _2611_MiceAndCheese {

    /**
     * 方法一：贪心 + 排序
     * 有 n 块不同类型的奶酪，分别位于下标 0 到 n−1。下标 i 处的奶酪被第一只老鼠吃掉的得分为 reward1[i]，被第二只老鼠吃掉的得分为 reward2[i]。
     *
     * 如果 n 块奶酪都被第二只老鼠吃掉，则得分为数组 reward2 元素之和，记为 sum1。
     *
     * 由于第一只老鼠必须吃掉 k 个奶酪，所以我们必须选出其中 k 给奶酪给第一只老鼠吃，
     *
     * 如果下标 i 处的奶酪被第一只老鼠吃掉，则得分的变化量是 reward1[i]−reward2[i]。
     * 根据贪心思想，为了使总得分最大化，应该选择 使得 reward1[i]−reward2[i] 最大的 前k个奶酪 给老鼠1 吃
     *
     * 那么我们创建长度为 n 的数组 diffs，其中 diffs[i]=reward1[i]−reward2[i]。
     * 对 diffs 进行排序后，选择 diffs[n-k,n-1] 这些位置的奶酪给老鼠1吃，
     * 那么最终答案为 sum1 + sum(diffs[n-k,,,n-1])
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/mice-and-cheese/solution/lao-shu-he-nai-luo-by-leetcode-solution-6ia1/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param reward1
     * @param reward2
     * @param k
     * @return
     */
    public int miceAndCheese(int[] reward1, int[] reward2, int k) {
        int n = reward1.length;
        int ans = 0;
        // 先全部给第二只老鼠吃
        for (int i = 0; i < n; ++i) {
            ans += reward2[i];
            // 用 reward1 数组充当 diffs 数组，记录 i奶酪 给老鼠1吃后 的得分增加量
            reward1[i] -= reward2[i];
        }
        // 排序
        Arrays.sort(reward1);
        // 选择最后k个老鼠1吃
        for (int i = n - k; i < n; ++i) {
            // 加上这些变化量
            ans += reward1[i];
        }
        // 返回
        return ans;
    }
}
