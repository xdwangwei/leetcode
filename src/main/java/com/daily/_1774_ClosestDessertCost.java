package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2022/12/4 12:29
 * @description: _1774_ClosestDessertCost
 *
 * 1774. 最接近目标价格的甜点成本
 * 你打算做甜点，现在需要购买配料。目前共有 n 种冰激凌基料和 m 种配料可供选购。而制作甜点需要遵循以下几条规则：
 *
 * 必须选择 一种 冰激凌基料。
 * 可以添加 一种或多种 配料，也可以不添加任何配料。
 * 每种类型的配料 最多两份 。
 * 给你以下三个输入：
 *
 * baseCosts ，一个长度为 n 的整数数组，其中每个 baseCosts[i] 表示第 i 种冰激凌基料的价格。
 * toppingCosts，一个长度为 m 的整数数组，其中每个 toppingCosts[i] 表示 一份 第 i 种冰激凌配料的价格。
 * target ，一个整数，表示你制作甜点的目标价格。
 * 你希望自己做的甜点总成本尽可能接近目标价格 target 。
 *
 * 返回最接近 target 的甜点成本。如果有多种方案，返回 成本相对较低 的一种。
 *
 *
 *
 * 示例 1：
 *
 * 输入：baseCosts = [1,7], toppingCosts = [3,4], target = 10
 * 输出：10
 * 解释：考虑下面的方案组合（所有下标均从 0 开始）：
 * - 选择 1 号基料：成本 7
 * - 选择 1 份 0 号配料：成本 1 x 3 = 3
 * - 选择 0 份 1 号配料：成本 0 x 4 = 0
 * 总成本：7 + 3 + 0 = 10 。
 * 示例 2：
 *
 * 输入：baseCosts = [2,3], toppingCosts = [4,5,100], target = 18
 * 输出：17
 * 解释：考虑下面的方案组合（所有下标均从 0 开始）：
 * - 选择 1 号基料：成本 3
 * - 选择 1 份 0 号配料：成本 1 x 4 = 4
 * - 选择 2 份 1 号配料：成本 2 x 5 = 10
 * - 选择 0 份 2 号配料：成本 0 x 100 = 0
 * 总成本：3 + 4 + 10 + 0 = 17 。不存在总成本为 18 的甜点制作方案。
 * 示例 3：
 *
 * 输入：baseCosts = [3,10], toppingCosts = [2,5], target = 9
 * 输出：8
 * 解释：可以制作总成本为 8 和 10 的甜点。返回 8 ，因为这是成本更低的方案。
 * 示例 4：
 *
 * 输入：baseCosts = [10], toppingCosts = [1], target = 1
 * 输出：10
 * 解释：注意，你可以选择不添加任何配料，但你必须选择一种基料。
 *
 *
 * 提示：
 *
 * n == baseCosts.length
 * m == toppingCosts.length
 * 1 <= n, m <= 10
 * 1 <= baseCosts[i], toppingCosts[i] <= 104
 * 1 <= target <= 104
 * 通过次数12,340提交次数22,302
 *
 */
public class _1774_ClosestDessertCost {

    // 返回值，最接近target的搭配方案的成本
    private int res;

    /**
     * 方法一：回溯
     * 思路与算法
     *
     * 首先题目给出长度分别为 n 的冰淇淋基料数组 baseCosts 和长度为 m 的配料数组 toppingCosts，
     * 其中 baseCosts[i] 表示第 i 种冰淇淋基料的价格，toppingCosts[j] 表示一份第 j 种冰淇淋配料的价格，
     * 以及一个整数 target 表示我们需要制作甜点的目标价格。
     *
     * 现在在制作甜品上我们需要遵守以下三条规则：
     *
     *      必须选择一种冰淇淋基料；
     *      可以添加一种或多种配料，也可以不添加任何配料；
     *      每种配料最多两份。
     * 我们希望做的甜点总成本尽可能【接近】（绝对值）目标价格 target，
     *
     * 那么我们现在按照规则对于每一种冰淇淋基料用回溯的方式来针对它进行甜品制作。
     *
     * 又因为每一种配料都是正整数，所以在回溯的过程中总开销只能只增不减，
     * 当回溯过程中当前开销大于目标价格 target 后，继续往下搜索只能使开销与 target 的差值更大，
     * 所以如果此时差值已经大于等于我们已有最优方案的差值，我们可以停止继续往下搜索，及时回溯。
     *
     * 在回溯开始前，我们可以选出冰淇淋基料的最小值min，
     * 如果 min >= target，那么其他任何方案成本只会比min更大，与target的插值更大，此时直接返回min
     * 然后，将 res 初始化 为 min，开始后续回溯过程
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/closest-dessert-cost/solution/zui-jie-jin-mu-biao-jie-ge-de-tian-dian-2ck06/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param baseCosts
     * @param toppingCosts
     * @param target
     * @return
     */
    public int closestCost(int[] baseCosts, int[] toppingCosts, int target) {
        // 冰淇淋基料的最小值min，
        int min = Arrays.stream(baseCosts).min().getAsInt();
        // 其他任何方案成本只会比min更大，与target的插值更大，此时直接返回min
        if (min >= target) {
            return min;
        }
        // 将 res 初始化 为 min，
        res = min;
        // 对每一种冰淇淋基料，进行配料的回溯
        for (int baseCost : baseCosts) {
            // idx 代表待选择配料索引，
            backTrack(toppingCosts, 0, baseCost, target);
        }
        // 返回最优方案成本
        return res;
    }

    /**
     * 回溯 costs数组元素选择，每个元素可以不选，可以选1份，可以选2份
     * @param costs 数组
     * @param idx   当前选择哪个元素
     * @param curCost  当前成本值 （递增）
     * @param target   目标成本值
     */
    private void backTrack(int[] costs, int idx, int curCost, int target) {
        // 当前方案成本价已经大于等于我们已有最优方案的差值，后续选择只会使得成本更大，与target的差值更大
        // 我们可以停止继续往下搜索，及时回溯。
        if (curCost - target >= Math.abs(res - target)) {
            return;
        }
        // 当前方案成本价与target的差值 小于等于 我们已有最优方案的与target的差值
        if (Math.abs(curCost - target) <= Math.abs(res - target)) {
            // 如果当前方案成本更接近target，那么更新最优方案为当前方案
            if (Math.abs(curCost - target) < Math.abs(res - target)) {
                res = curCost;
            } else {
                // 如果当前方案成本与已有方案同样接近target，那么选择其中更小的成本值
                res = Math.min(res, curCost);
            }
        }
        // 上面if不能直接return，因为可能继续curCost<target，再选择基料后，更加接近target
        // 如果所有元素已选择完毕，结束
        if (idx >= costs.length) {
            return;
        }
        // 对于当前元素（基料），三种选择
        backTrack(costs, idx + 1, curCost, target);
        backTrack(costs, idx + 1, curCost + costs[idx], target);
        backTrack(costs, idx + 1, curCost + 2 * costs[idx], target);
    }


    /**
     * 方法二：动态规划
     *
     * 冰淇淋基料最小值为min
     * 从方法一中已经知道，对于 大于 upper=2*target-min的方案，其与target的差值一定大于min与target的差值，可以直接废弃
     *
     * 那么我们可以通过 动态规划来判断是否存在成本在 [min, upper-1] 间的方案
     * 如果存在，从中选择 与target 差值最小的方案即可。
     *
     * base：
     *      因为基料必须选，所以初始时遍历 baseCosts，如果 baseCosts[i] < upper，让 dp[baseCosts[i]] = true
     * 接下来是在基料的基础上选择配料
     * 这里参考0-1背包的写法，假如辅料i的代价为x，在选择辅料i之前dp数组[a1,a2,a3]位置为true
     * 那么选择完辅料i后，dp[a1,a2,a3,a1+x,a2+x,a3+x,a1+2x,a2+2x,a3+2x]为true
     *
     * 也就是说对于辅料i的代价x
     *      遍历dp数组，对于 dp[i] = true，更新 dp[i + x] 为true， dp[i + 2x] 为 true
     * 对下一个辅料进行同样过程
     *
     * 【细节一】：
     *      dp数组必须倒序遍历
     *          对于每个辅料，假如顺序遍历dp
     *          dp[0]=true，更新dp[0+2x]=true， dp[0+x]=true
     *          那么当遍历到dp[x]时，又会更新dp[x+2x]为true，dp[x+x]=true，，？？？这相当于使用了3份辅料i，肯定不对，
     *          因此每次都需要倒序遍历dp
     * 【细节二】
     *      必须在 dp[i] 为 true 的情况下，更新 dp[i + x] 和 dp[i + 2x] 为true
     *      因为 只有方案i存在，那么方案i+x才会存在
     * 【细节三】
     *      因为我们只需要考虑成本在 [min, upper-1] 间的方案，因此对于此范围外的方案一律不用考虑
     * @param baseCosts
     * @param toppingCosts
     * @param target
     * @return
     */
    public int closestCost2(int[] baseCosts, int[] toppingCosts, int target) {
        // 冰淇淋基料的最小值min，
        int min = Arrays.stream(baseCosts).min().getAsInt();
        // 其他任何方案成本只会比min更大，与target的插值更大，此时直接返回min
        if (min >= target) {
            return min;
        }
        // 只需要考虑成本在 [min, upper-1] 间的方案是否存在
        int upper = 2 * target - min;
        boolean[] dp = new boolean[upper];
        // base，基料必选
        for (int baseCost : baseCosts) {
            // 不考虑区间外的方案
            if (baseCost < upper) {
                dp[baseCost] = true;
            }
        }
        // dp迭代，在基料的基础上选择辅料
        for (int toppingCost : toppingCosts) {
            // 倒序遍历dp
            for (int j = upper - 1; j >= min; --j) {
                // 每种辅料可以1份或两份，更新对应的dp为true，区间外不予考虑
                if (dp[j] && (j + 2 * toppingCost < upper)) {
                    dp[j + 2 * toppingCost] = true;
                }
                if (dp[j] && (j + toppingCost < upper)) {
                    dp[j + toppingCost] = true;
                }
            }
        }
        // 在 [min, upper-1]所有存在的方案种找出与target最接近的方案
        int ans = min;
        for (int i = min + 1; i < upper; ++i) {
            if (dp[i]) {
                // 更接近
                if (Math.abs(i - target) < Math.abs(ans - target)) {
                    ans = i;
                // 同样接近，选择更小成本
                } else if (Math.abs(i - target) == Math.abs(ans - target)) {
                    ans = Math.min(ans, i);
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        _1774_ClosestDessertCost obj = new _1774_ClosestDessertCost();
        // System.out.println(obj.closestCost(new int[]{1, 7}, new int[]{3, 4}, 10));
        // System.out.println(obj.closestCost(new int[]{9}, new int[]{7}, 10));
        System.out.println(obj.closestCost2(new int[]{2, 3}, new int[]{4, 5, 100}, 18));
    }
}
