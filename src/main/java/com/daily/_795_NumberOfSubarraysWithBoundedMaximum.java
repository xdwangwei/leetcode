package com.daily;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author wangwei
 * @date 2022/11/24 12:02
 * @description: _795_NumberOfSubarraysWithBoundedMaximum
 *
 * 795. 区间子数组个数
 * 给你一个整数数组 nums 和两个整数：left 及 right 。找出 nums 中连续、非空且其中最大元素在范围 [left, right] 内的子数组，并返回满足条件的子数组的个数。
 *
 * 生成的测试用例保证结果符合 32-bit 整数范围。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [2,1,4,3], left = 2, right = 3
 * 输出：3
 * 解释：满足条件的三个子数组：[2], [2, 1], [3]
 * 示例 2：
 *
 * 输入：nums = [2,9,2,5,6], left = 2, right = 8
 * 输出：7
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * 0 <= nums[i] <= 109
 * 0 <= left <= right <= 109
 * 通过次数20,349提交次数36,366
 */
public class _795_NumberOfSubarraysWithBoundedMaximum {


    /**
     * 方法一：单调栈
     *
     * 考虑每个元素对答案的贡献值，先不考虑最大值必须在 left，right 之间的限制
     * 元素x对答案的贡献值为 以x为最大值的子数组个数
     * 假设 nums[i] = x, nums 中 x 左边 第一个大于x的数字在 l位置，nums中 x 右边第一个大于x的数字在 r位置
     * 那么如果以x作为子数组最大值，
     *      子数组左边界可以是 l+1，l+2，...，i，共 i - l 个
     *      子数组右边界可以是 i，i+1，i+2，...，r-1，共 r - i 个
     *      那么 x 对答案的贡献为 (i -l) * (r - i)
     *      当然，如果 x 本身不在 [left, right] 之内，那么 它的贡献值为 0
     *
     * 问题转换为求 nums 中，每个元素左边、右边第一个大于它的元素位置
     *
     * 单调栈具体分析过程参考 _907_SumOfSubarrayMinimums
     *
     * 在给定序列中，找到任意 a[i] 最近一个比其 大 的位置，可使用「单调栈」进行求解。
     * 因为我们需要知道边界的位置，也就是索引，所以记录的元素都是索引
     *
     * 以计算左边界为例，从左到右遍历 arr，用栈维护遍历过的元素，并及时移除无用的元素，
     * 假如 当前元素是a[i]，如果栈顶是 a[j]，如果 a[j] <= a[i]，那么它不可能是a[i]的左边界，弹栈，直到遇到 a[?] > a[i]
     *
     *    移除无用元素后，栈顶的下标就是 arr[i] 的左边界，如果此时栈为空，那么左边界为 −1。
     *    最后，把 i 入栈。
     *
     * 右边界的计算是类似的，【从右往左】遍历 arr 可以算出。
     *
     * 算出左右边界后，累加每个 arr[i] 的贡献，即为答案。
     *
     * 优化：
     *
     * 我们求左边界的时候会将每个大于当前元素arr[i]的元素出栈以向左求解得到第一个大于arr[i]的元素，
     * 那么反过来针对每个出栈的元素，当前元素arr[i]不就是向右比它更大的第一个元素吗？这就得到了右边界。
     *
     * 再观察入栈顺序：每个小于当前元素arr[i]的元素出栈完后的 栈顶元素 就是 当前元素的左边界
     *
     * 那么对于每个要出栈的栈顶元素来说，它下面的元素不就是它的左边界吗？这就得到了左边界。
     *
     * 既然左右边界都能在一次遍历中得到，那么自然我们可以一次遍历就得到贡献值，且无需额外空间。
     *
     * 唯一问题是：遍历到最后一个元素时，它能够让它前面的元素都出栈，并计算出它们的贡献值，但它自己的贡献值怎么办呢？
     * 考虑到arr数组中所有元素都是大于0的，那么我们多进行一次循环，虚拟一个元素0入栈，相当于最后一个元素的右边界，所以也就都会被执行出栈操作了。
     * @param nums
     * @param left
     * @param right
     * @return
     */
    public int numSubarrayBoundedMax(int[] nums, int left, int right) {
        int n = nums.length, ans = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        // 从左到右一次遍历得到 nums 中每个元素 左边、右边第一个比它大的元素位置l、r
        // 那么 nums[i] 对答案的贡献为 (i -l) * (r - i)
        // 多进行一次循环，是为了让最后一个元素的贡献值也能得到计算（数组遍历完了，补一个右边界，让数组最后一个数组能出栈，从而计算贡献值）
        for (int i = 0; i <= n; ++i) {
            int cur = i < n ? nums[i] : Integer.MAX_VALUE;
            // 弹出i左边所有 <= arr[i] 的元素
            while (!stack.isEmpty() && nums[stack.peek()] < cur) {
                // 对于弹出的每一个栈顶，当前元素就是它的右边界
                // 栈顶下面第一个元素就是它的左边界，
                int idx = stack.pop();
                // 如果当前元素不在 [left, right] 范围内，它的贡献值就是0
                if (nums[idx] < left || nums[idx] > right) {
                    continue;
                }
                // 直接计算它的贡献值，累加到答案
                int l = stack.isEmpty() ? -1 : stack.peek();
                ans += (idx - l) * (i - idx);
            }
            // 当前元素索引入栈
            stack.push(i);
        }
        return ans;
    }

    /**
     * 方法二，枚举右端点，简化 [left, right] 为 [a, b]
     *
     * 除了统计「每个 nums[i]nums[i] 作为子数组最大值时，所能贡献的子数组个数」以外，我们还可以统计「每个 nums[i] 作为子数组右端点时，所能贡献的子数组个数」。
     *
     * 具体的，我们从前往后处理每个 nums[i]，并统计其作为子数组右端点时，所能贡献的子数组个数。
     * 以它作为右端点，需要考虑左端点的取值，子数组内不能出现 大于 b 的元素，且必须要有 在 [a, b] 之间的元素。
     * 因此使用变量 j 和 k 分别记录最近一次满足「 值落在 [a,b] 之间」以及「 值大于 b」的下标位置。
     *
     * 遍历过程中根据 nums[i] 与规定范围 [a,b] 之间的关系进行分情况讨论：
     *
     *      nums[i] 大于 b，
     *          nums[i] 作为右端点，必不可能贡献合法子数组。
     *          更新 k = i；
     *      nums[i] 落在范围 [a,b]内，
     *          此时 nums[i] 若要作为右端点，需要找到左边第一个数值大于 b 的元素位置即可（即变量 k，k+1到当前位置都<=b），
     *          累加方案数 i−k。此时更新 j。
     *      nums[i] 小于 a，
     *          此时 nums[i] 想作为右端点的话，子数组必须有满足「范围落在 [a,b] 之间」的其他数，而最近一个满足要求的位置为 j，（至少得把nums[j]加进来）
     *              若有 j>k，说明范围在 (k,j] 均能作为子数组的左端点，累加方案数 j−k；
     *              若有 j<k，说明我们无法找到任何一个左端点，使得形成的子数组满足要求（因为要包括nums[j],j+1到当前位置又存在元素>b）；
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/number-of-subarrays-with-bounded-maximum/solution/by-ac_oier-gmpt/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int numSubarrayBoundedMax2(int[] nums, int left, int right) {
        // lastIn  代表nums[i]左边最近一个在[left, right]范围内  的元素的下标 ,  初始化为-1
        // lastOver代表nums[i]左边最近一个   超过right           的元素的下标，  初始化为-1
        int n = nums.length, ans = 0, lastIn = -1, lastOver = -1;
        // 枚举每一个元素作为子数组右边界时的贡献值（即此时左边界的取值可能性）
        for (int i = 0; i < n; ++i) {
            // 右边界超过right，左边界无法取值，不存在满足要求的子数组
            if (nums[i] > right) {
                // 此时更新 lastOver
                lastOver = i;
            // 右边界在[left,right]范围内，左边最近的大于right的位置是lastOver，那么左边界可以取lastOver后任意位置
            } else if (nums[i] >= left) {
                // 那么左边界可以取lastOver后任意位置
                ans += i - lastOver;
                // 更新 lastIn
                lastIn = i;
            // 右边界小于 left
            } else {
                // 左边最近的大于right的位置是lastOver，
                // 左边最近的在left到right内的位置是lastIn，
                // 子数组必须存在 [left,right]内的元素，又不能存在大于right的元素
                // 所以，[lastIn...] 必选，但是 [lastOver]不能选
                // 所以 如果 lastOver 在 lastIn 之前，那没法选了
                if (lastIn > lastOver) {
                    // 否则左端点可以选 [lastOver+1,lastIn]
                    ans += lastIn - lastOver;
                }
            }
        }
        return ans;
    }

}
