package com.daily;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangwei
 * @date 2023/1/22 18:34
 * @description: _1815_MaximumNumberOfGroupsGettingFreshDonuts
 *
 * 1815. 得到新鲜甜甜圈的最多组数
 * 有一个甜甜圈商店，每批次都烤 batchSize 个甜甜圈。这个店铺有个规则，就是在烤一批新的甜甜圈时，之前 所有 甜甜圈都必须已经全部销售完毕。给你一个整数 batchSize 和一个整数数组 groups ，数组中的每个整数都代表一批前来购买甜甜圈的顾客，其中 groups[i] 表示这一批顾客的人数。每一位顾客都恰好只要一个甜甜圈。
 *
 * 当有一批顾客来到商店时，他们所有人都必须在下一批顾客来之前购买完甜甜圈。如果一批顾客中第一位顾客得到的甜甜圈不是上一组剩下的，那么这一组人都会很开心。
 *
 * 你可以随意安排每批顾客到来的顺序。请你返回在此前提下，最多 有多少组人会感到开心。
 *
 *
 *
 * 示例 1：
 *
 * 输入：batchSize = 3, groups = [1,2,3,4,5,6]
 * 输出：4
 * 解释：你可以将这些批次的顾客顺序安排为 [6,2,4,5,1,3] 。那么第 1，2，4，6 组都会感到开心。
 * 示例 2：
 *
 * 输入：batchSize = 4, groups = [1,3,2,5,2,2,1,6]
 * 输出：4
 *
 *
 * 提示：
 *
 * 1 <= batchSize <= 9
 * 1 <= groups.length <= 30
 * 1 <= groups[i] <= 109
 * 通过次数6,736提交次数14,123
 */
public class _1815_MaximumNumberOfGroupsGettingFreshDonuts {

    /**
     * 方法一：贪心 + 状态压缩（二进制） + 记忆化搜索
     *
     * 题目：每次做 batchSize 个甜甜圈，对于每一组人，如果到来时，没有剩下的甜甜圈（新做）会开心；
     * 那么实际上是要我们找到一种安排顺序，使得前缀和（这里指的是“人数”）与 batchSize 取模后为 0 的组数最多。
     * 并且，实际上我们只关心上一个顾客购买后「余下」的甜甜圈数量，那么购买 1 个和 m+1 个没有任何区别。
     * 所以我们实际上要看的是每一个 groups[i] % batchSize 的结果
     *
     * 【算法】
     * 因此，我们可以将所有顾客按组分成两类：
     *   1. 人数为 batchSize 的整数倍的顾客，这些顾客不会对下一组顾客的甜甜圈产生影响，
     *          我们可以贪心地优先安排这些组的顾客，并且这些组的顾客都会感到开心，“初始答案”即为这些组的数量；
     *   2. 人数不为 batchSize 的整数倍的顾客，这些顾客的安排顺序会影响下一组顾客的甜甜圈。
     *          因此，对于剩下这些组，我们可以使用回溯（记忆化搜索）来进行安排。
     *          回溯时我们需要知道当前剩下哪些组没有被安排，以及前面的组安排完后剩下了几个甜甜圈
     *
     *          对于 前面的组安排完后剩下了几个甜甜圈 的处理，使用 int 类型变量 left 表示即可
     *              因为之前安排完了所有 batchSize 整数倍的 顾客组，所以初始时 left = 0
     *          对于剩下这些组的记录处理：
     *              首先，我们可以对这里每一组的人数 v 模 batchSize，得到的这些余数构成一个集合，
     *              集合中的元素值范围是 [1,2...,batchSize−1]。
     *              并且，由于不同组的取模结果可能相同，
     *              我们可以使用一个计数数组cnt[]保存 每种取模结果对应的组数，
     *              当 cnt[x] > 0 时，表示 还有 cnt[x] 个 对 batchSize 取模结果为 x 的组还未安排
     *
     * 【实现】
     * 我们设计一个函数 backTrack(cnt[], left)，
     * 表示待安排组状态为cnt[]，且已安排完之前小组后剩余甜甜圈为left时，能使得多少组感到开心。
     *
     * 那么 初始 ans = groups[i] % batchSize == 0 的小组数量，在“初始答案”加上backTrack(cnt[],0)，即为最终答案。
     * 在回溯时
     *
     * 函数 backTrack(state,mod) 的实现逻辑如下：
     *
     * 我们枚举 1 到 batchSize−1 的每一个余数 i，
     *      【做选择】任意选取一个 cnt[x] > 0 的 x，更新 cnt[x]--，更新 nleft = (left + x) % batchSize
     *      【下一步】递归调用函数 backTrack(ncnt, nleft)，求出后序状态的最优解，
     *      【撤销选择】cnt[x]++, left
     * 在多个选择得到的解中，取最大值即可，
     * 并且对于每一个选择的子问题的解，还需要判断 当前 left 是否为 0，
     *      如果为 0（选择的当前组肯定也开心），我们需要在子问题的解上加 1。
     *
     * 【优化】
     * 考虑子问题的重复性，对于先1后2、先2后1这两种安排顺序，其结果是一样的，都可以看成是前面的人买了 3 个甜甜圈。
     * 这样就有重复子问题，因此使用记忆化搜索来避免状态的重复计算。
     *
     * 对于记忆化搜索的key，回溯时有两个参数，cnt[] 和 left，
     * 而实际上 当 cnt[] 确定时，我们能够知道已经选择了哪些组，能够通过cnt[]计算得到之前组安排完后剩余的甜甜圈数量，即left
     * 在回溯时的left只是为了避免这个统计过程
     * 因此在记忆化搜索时，key直接用cnt[]完成即可。
     *
     * 对于用数组cnt[]做key，需要将其进行一定运算转为int或string，比较麻烦，而且效率低
     * 观察题目数据取值范围：batchSize 最大为 9，那么 cnt[] 下标 x 最多有9种取值，并且回溯前x=0的组已经被处理
     * 而数组 groups 的长度最大为 30，因此，每个cnt[x]最大不超过 30,可以用 5 个二进制位来表示一个余数的数量，
     * 那么表示这些余数以及对应的数量总共需要的二进制位就是 5×(9−1)=40。我们可以用一个 long 类型变量 cnt 来表示。
     *
     * 那么 对于原来的 cnt[x] 是否 > 0 就可以用 (cnt >> (i - 1) * 5) & 31 是否 == 0 来代替
     *     对于原来的 cnt[x]++       就可以用 cnt += 1l << (i - 1) * 5)  来代替
     *     对于原来的 cnt[x]--       就可以用 cnt -= 1l << (i - 1) * 5)  来代替
     *
     *          （i - 1 是因为，原本的cnt[x]，x 取值从1开始，而我们如果要从最低的5个二进制位开始使用的话，就需要-1，
     *              当然也可以直接跳过最低的5个二进制位，反正45个二进制位也不会超过long的64位
     *              那么条件变为
     *               (cnt >> i * 5) & 31 != 0
                     cnt += 1l << i * 5
                     cnt -= 1l << i * 5
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/maximum-number-of-groups-getting-fresh-donuts/solution/by-lcbin-wtvs/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    // 备忘录，
    // key 为 long类型 cnt,从低到高，第i组5个连续二进制位 表示 对 batchSize 取模为 i 的组的数量
    // value 为 int类型 res ,表示当 cnt表示的组安排完后，可能的最多”高兴“组数量
    private Map<Long, Integer> memo;
    // 用 n 代替 batchSize
    private int n;

    public int maxHappyGroups(int batchSize, int[] groups) {
        n = batchSize;
        // 返回值
        int ans = 0;
        // cnt,从低到高，第i组5个连续二进制位 表示 对 batchSize 取模为 i 的组的数量
        long cnt = 0;
        // 遍历
        for (int group : groups) {
            // 得到取模值
            int i = group % n;
            // 先安排模为0的组，这些组肯定会很高兴，ans++
            if (i == 0) {
                ans++;
            } else {
                // 记录这些模不为0的组的组数，每一个组数用5个二进制位存储
                cnt += 1l << (i - 1) * 5;
            }
        }
        memo = new HashMap<>();
        // 初始答案 加上 安排完剩余组后能够得到的最多”高兴组“数量
        ans += backTrack(0, cnt);
        // 返回
        return ans;
    }

    /**
     * 回溯
     * @param left
     * @param cnt
     * @return
     */
    private int backTrack(int left, long cnt) {
        // 查备忘录
        if (memo.containsKey(cnt)) {
            return memo.get(cnt);
        }
        // 当前返回值
        int ans = 0;
        // 做选择，选一个待安排组，这个组人数取模值为i
        for (int i = 1; i < n; ++i) {
            // 这个组（取模值i对应）人数为 cnt >> (i - 1) * 5
            // 不为0时，需要被安排
            if (((cnt >> (i - 1) * 5) & 31) != 0) {
                // 安排完这一组后，对于剩下组来说，前面的余数更新
                int nleft = (left + i) % n;
                // 安排完这一组后，剩下组的数量（状态）更新
                long ncnt = cnt - (1l << (i - 1) * 5);
                // 安排剩下组能够得到的最多”高兴组“数量
                int nres = backTrack(nleft, ncnt);
                // 多个选择取做大值，需要考虑当前选择（当前组是否”高兴“）
                ans = Math.max(ans, nres + (left == 0 ? 1 : 0));
            }
        }
        // 记录
        memo.put(cnt, ans);
        // 返回
        return ans;
    }
}
