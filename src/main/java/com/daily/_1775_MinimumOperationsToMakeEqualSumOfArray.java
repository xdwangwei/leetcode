package com.daily;

/**
 * @author wangwei
 * @date 2022/12/7 10:18
 * @description: _1775_MinimumOperationsToMakeEqualSumOfArray
 *
 * 1775. 通过最少操作次数使数组的和相等
 * 给你两个长度可能不等的整数数组 nums1 和 nums2 。两个数组中的所有值都在 1 到 6 之间（包含 1 和 6）。
 *
 * 每次操作中，你可以选择 任意 数组中的任意一个整数，将它变成 1 到 6 之间 任意 的值（包含 1 和 6）。
 *
 * 请你返回使 nums1 中所有数的和与 nums2 中所有数的和相等的最少操作次数。如果无法使两个数组的和相等，请返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums1 = [1,2,3,4,5,6], nums2 = [1,1,2,2,2,2]
 * 输出：3
 * 解释：你可以通过 3 次操作使 nums1 中所有数的和与 nums2 中所有数的和相等。以下数组下标都从 0 开始。
 * - 将 nums2[0] 变为 6 。 nums1 = [1,2,3,4,5,6], nums2 = [6,1,2,2,2,2] 。
 * - 将 nums1[5] 变为 1 。 nums1 = [1,2,3,4,5,1], nums2 = [6,1,2,2,2,2] 。
 * - 将 nums1[2] 变为 2 。 nums1 = [1,2,2,4,5,1], nums2 = [6,1,2,2,2,2] 。
 * 示例 2：
 *
 * 输入：nums1 = [1,1,1,1,1,1,1], nums2 = [6]
 * 输出：-1
 * 解释：没有办法减少 nums1 的和或者增加 nums2 的和使二者相等。
 * 示例 3：
 *
 * 输入：nums1 = [6,6], nums2 = [1]
 * 输出：3
 * 解释：你可以通过 3 次操作使 nums1 中所有数的和与 nums2 中所有数的和相等。以下数组下标都从 0 开始。
 * - 将 nums1[0] 变为 2 。 nums1 = [2,6], nums2 = [1] 。
 * - 将 nums1[1] 变为 2 。 nums1 = [2,2], nums2 = [1] 。
 * - 将 nums2[0] 变为 4 。 nums1 = [2,2], nums2 = [4] 。
 *
 *
 * 提示：
 *
 * 1 <= nums1.length, nums2.length <= 105
 * 1 <= nums1[i], nums2[i] <= 6
 * 通过次数9,077提交次数17,039
 */
public class _1775_MinimumOperationsToMakeEqualSumOfArray {


    /**
     *
     * 贪心 + 计数排序
     *
     * 详细算法流程
     *
     * 不妨设 nums1 的元素和小于 nums2 的元素和（如果不是则交换这两数组），元素和的差为 diff。
     *
     * 那么 nums1 的元素需要变大，nums2 的元素需要变小。
     *
     * 计算每个元素的【最大变化量】：
     *
     *      nums1[i] 最大能变成 6，最大变化量为 6 − nums1[i]，取值范围在 [1,5]
     *      nums2[i] 最小能变成 1，最大变化量为 nums2[i] − 1，取值范围在 [1,5]
     *
     * 统计这些变化量的个数，记到一个哈希表或长为 6 的数组 cnt 中，也就是有 cnt[i] 个数可以使 diff 减少 i。
     *
     * 为了使操作次数尽可能少，我们从大到小选用这些变化量i，即从大到小枚举 i = 5,4,3,2,1：每个变化量i有cnt[i]个
     *
     *      如果 i⋅cnt[i] < diff，那么可以把这 cnt[i] 个变化量i全部消耗掉，并更新 diff 为 diff − i⋅cnt[i]；操作次数增加 cnt[i]
     *      否则，此时的diff可以通过使用 ⌈diff / i⌉ 个变化量i，来使 diff 恰好为 0，退出循环。
     *              注意这里是向上取整，比如变化量3有4个，diff等于10，那么使用3个3使diff减小到1，
     *              本来应该需要一个1，但是由于i是nums中某个数字的最大变化量，它能提供3就能提供1，再消耗1个i也可达到同样目的
     *              此时操作次数增加 ⌈diff / i⌉
     *
     * 累加需要修改的数的个数，即为答案。
     *
     * 如果无法使 diff=0，返回 −1。
     *
     * 优化
     *
     * 假设 nums1的元素和小于 nums2的元素和。
     * 把 nums1 的所有数都改成 6，nums2的所有数都改成 1，如果 nums1的元素和仍然小于 nums2 的元素和，
     * 则说明无论怎么操作，都无法使这两个数组的元素和相等。
     * 对于 nums1的元素和大于 nums2的元素和 的情况，也同理。
     *
     * 因此，设 m 为 nums1的长度，n 为 nums2 的长度
     * 我们可以在一开始就判断下：如果 6n < m 或 6m < n，则直接返回 −1。
     *
     * 否则，一定可以使两个数组的和相等，
     * 这是因为从「nums1的元素和小于nums2的元素和」变到「nums的元素和大于等于 nums2的元素和」，
     * 由于元素值本身取值在[1,6]，并且可以变成 [1,6] 中的任意值，因此可以每次操作只把一个元素增大 1 或减小 1，
     * 这样必然会遇到元素和相差为 0 的情况。
     *
     * 作者：endlesscheng
     * 链接：https://leetcode.cn/problems/equal-sum-arrays-with-minimum-number-of-operations/solution/mei-xiang-ming-bai-yi-ge-dong-hua-miao-d-ocuu/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums1
     * @param nums2
     * @return
     */
    public int minOperations(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        // 无法完成的情况，nums1全变为6，nums2全变为1，nums1的和仍然比不过nums2，那无法完成；反过来一样
        if (m * 6 < n || n * 6 < m) {
            return -1;
        }
        // 计算元素和，提交发现 stream 太慢了
        // int sum1 = Arrays.stream(nums1).sum();
        // int sum2 = Arrays.stream(nums2).sum();
        // 以 nums1元素和 < nums2元素和 为例，
        // diff 表示 sum(nums2) - sum(num1)，否则就反过来
        int diff = 0;
        for (int i : nums2) diff += i;
        for (int i : nums1) diff -= i;
        // 已经相等，直接返回
        if (diff == 0) {
            return 0;
        }
        // 如果 nums1 元素和 > nums2 元素和，就交换，
        if (diff < 0) {
            int[] temp = nums1;
            nums1 = nums2;
            nums2 = temp;
            // 注意更新diff
            diff = -diff;
        }
        // cnt 表示 nums1 nums2 中每个元素能提供的最大变化量
        int[] cnt = new int[6];
        // nums1[i] 需要变大，最大能变成 6，最大变化量为 6 − nums1[i]，取值范围在 [1,5]
        for (int i : nums2) {
            cnt[i - 1]++;
        }
        // nums2[i] 需要变小，最小能变成 1，最大变化量为 nums2[i] − 1，取值范围在 [1,5]
        for (int i : nums1) {
            cnt[6 - i]++;
        }
        // 贪心，从大到小选择变化量i来凑出diff，每个变化量i有cnt[i]个
        int ans = 0;
        // 变化量取值[1-5]，倒序遍历
        for (int i = 5; i > 0; --i) {
            // diff能够消耗完所有当前变坏量i
            if (i * cnt[i] < diff) {
                // 更新操作次数和diff
                ans += cnt[i];
                diff -= i * cnt[i];
            // 只需 ⌈diff / i⌉ 个 变化量i，就能diff变为0
            } else  {
                // 累加操作次数
                ans += (diff + i - 1) / i;
                // 提前结束
                break;
            }
        }
        // 返回
        return ans;
    }
}
