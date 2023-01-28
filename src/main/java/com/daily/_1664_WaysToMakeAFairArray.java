package com.daily;

/**
 * @author wangwei
 * @date 2023/1/28 12:22
 * @description: _1664_WaysToMakeAFairArray
 *
 * 1664. 生成平衡数组的方案数
 * 给你一个整数数组 nums 。你需要选择 恰好 一个下标（下标从 0 开始）并删除对应的元素。请注意剩下元素的下标可能会因为删除操作而发生改变。
 *
 * 比方说，如果 nums = [6,1,7,4,1] ，那么：
 *
 * 选择删除下标 1 ，剩下的数组为 nums = [6,7,4,1] 。
 * 选择删除下标 2 ，剩下的数组为 nums = [6,1,4,1] 。
 * 选择删除下标 4 ，剩下的数组为 nums = [6,1,7,4] 。
 * 如果一个数组满足奇数下标元素的和与偶数下标元素的和相等，该数组就是一个 平衡数组 。
 *
 * 请你返回删除操作后，剩下的数组 nums 是 平衡数组 的 方案数 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [2,1,6,4]
 * 输出：1
 * 解释：
 * 删除下标 0 ：[1,6,4] -> 偶数元素下标为：1 + 4 = 5 。奇数元素下标为：6 。不平衡。
 * 删除下标 1 ：[2,6,4] -> 偶数元素下标为：2 + 4 = 6 。奇数元素下标为：6 。平衡。
 * 删除下标 2 ：[2,1,4] -> 偶数元素下标为：2 + 4 = 6 。奇数元素下标为：1 。不平衡。
 * 删除下标 3 ：[2,1,6] -> 偶数元素下标为：2 + 6 = 8 。奇数元素下标为：1 。不平衡。
 * 只有一种让剩余数组成为平衡数组的方案。
 * 示例 2：
 *
 * 输入：nums = [1,1,1]
 * 输出：3
 * 解释：你可以删除任意元素，剩余数组都是平衡数组。
 * 示例 3：
 *
 * 输入：nums = [1,2,3]
 * 输出：0
 * 解释：不管删除哪个元素，剩下数组都不是平衡数组。
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 104
 * 通过次数12,252提交次数19,662
 */
public class _1664_WaysToMakeAFairArray {


    /**
     * 预处理（奇偶前缀和）
     *
     * 我们设 preOdd[i] 表示位置 0≤i<n 前所有奇数位置元素的和，preEven[i] 表示位置 i 前所有偶数位置元素的和，
     * sufOdd[i] 表示位置 i 后所有奇数位置元素的和，sufEven[i] 表示位置 i 后所有偶数位置元素的和，
     *
     * 将下标 i 的元素进行删除，显而易见下标 i 之前的元素下标并不会因此发生改变，
     * 而下标 i 之后的原本在 j > i 下标的数组元素会移动到下标 j−1，
     * 即下标 i 之后的奇数下标元素会成为偶数下标元素，偶数下标元素会成为奇数下标元素。
     *
     * 所以删除后数组中全部奇数下标元素和为 preOdd[i]+sufEven[i]，全部偶数下标元素和为 preEven[i]+sufOdd[i]，
     *
     * 若两者相等则说明删除下标 i 后的数组为「平衡数组」。
     * 那么我们尝试删除每一个下标 0≤i<n，来统计能生成「平衡数组」的下标即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/ways-to-make-a-fair-array/solution/sheng-cheng-ping-heng-shu-zu-de-fang-an-0mkaj/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int waysToMakeFair(int[] nums) {
        int n = nums.length;
        // i 左边 奇数元素和
        int[] oddsBefore = new int[n];
        // i 左边 偶数元素和
        int[] evensBefore = new int[n];
        // i 右边 奇数元素和
        int[] oddsAfter = new int[n];
        // i 右边 偶数元素和
        int[] evensAfter = new int[n];
        // oddsBefore[0] = 0, evensBefore[0] = 0
        // oddsAfter[n-1] = 0, evensAfter[n-1] = 0
        // 迭代更新，i从1开始，j从n-2开始
        // i从左到右更新 oddsBefore 和 evensBefore
        // j从右到左更新 oddsAfter 和 evensAfter
        for (int i = 1; i < n; i++) {
            // i位置是偶数
            if (i % 2 == 0) {
                // i从1开始，下次进入这里i就是2，不会越界
                oddsBefore[i] = oddsBefore[i - 2] + nums[i - 1];
                evensBefore[i] = evensBefore[i - 1];
            // i位置是奇数
            } else {
                // i从1开始，注意越界
                evensBefore[i] = (i > 2 ? evensBefore[i - 2] : 0) + nums[i - 1];
                oddsBefore[i] = oddsBefore[i - 1];
            }
            int j = n - 1 - i;
            // j位置是偶数
            if (j % 2 == 0) {
                // j从倒数第二个位置开始，可能越界
                oddsAfter[j] = (j + 2 < n ? oddsAfter[j + 2] : 0) + nums[j + 1];
                evensAfter[j] = evensAfter[j + 1];
            } else {
                // 注意越界
                evensAfter[j] = (j + 2 < n ? evensAfter[j + 2] : 0) + nums[j + 1];
                oddsAfter[j] = oddsAfter[j + 1];
            }
        }
        int ans = 0;
        // 从左到右遍历每个位置，移除后是否满足平衡数组
        for (int i = 0; i < n; ++i) {
            // 删除i位置后，全部偶数位置元素和为 左偶 + 右奇，全部奇数位置元素和为 左奇 + 右偶
            if (evensBefore[i] + oddsAfter[i] == oddsBefore[i] + evensAfter[i]) {
                ans++;
            }
        }
        return ans;
    }

    /**
     * 方法二：空间优化
     *
     * 方法一提前迭代更新统计出了四个数组全部元素，实际上我们可以在遍历i的过程中完成迭代更新，从而节省空间
     *
     * 我们先预处理得到数组 nums 的【偶数】下标元素之和 s1 以及【奇数】下标元素之和 s2。
     *
     * 然后从前往后枚举数组 nums 的每个元素 v，用变量 t1 和 t2 分别记录【已遍历过的】偶数下标元素之和、奇数下标元素之和。
     * 【注意 t1、t2 是 i 位置之前元素的统计结果】
     *
     * 对于当前遍历到的元素 v，如果删除了，那么该元素之后的奇偶下标元素之和会发生交换。
     * 删除i位置元素v后，全部【偶数】位置元素和为 【左偶 + 右奇】，全部【奇数】位置元素和为 【左奇 + 右偶】
     * 因此此时，我们先判断该位置下标 i 是奇数还是偶数：
     *      如果是 i 偶数，删除该元素后，
     *          数组的偶数下标元素之和为 t1 + s2 - t2，奇数元素下标之和为 t2 + s1 - t1 - v、
     *          如果这两个和相等，那么就是一个平衡数组，答案加一
     *      如果是 i 奇数，删除该元素后，
     *          数组的偶数下标元素之和为 t1 + s2 - t2 - v，奇数元素下标之和为 t2 + s1 - t1
     *          如果这两个和相等，那么就是一个平衡数组，答案加一
     * 然后我们根据下标的奇偶性更新 t1 或 t2，继续遍历下一个元素。遍历完数组后，即可得到答案。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/ways-to-make-a-fair-array/solution/by-lcbin-3jl3/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int waysToMakeFair2(int[] nums) {
        int n = nums.length;
        int s1 = 0, s2 = 0;
        // s1、s2 分别统计 nums 中所有 偶数、奇数位置元素和
        for (int i = 0; i < n; i++) {
            if ((i & 1) != 0) {
                s2 += nums[i];
            } else {
                s1 += nums[i];
            }
        }
        // t1、t2分别统计已经遍历过的偶数、奇数位置元素和。
        int t1 = 0, t2 = 0, ans = 0;
        // 枚举每个删除位置 i
        // 删除i位置元素v后，全部【偶数】位置元素和为 【左偶 + 右奇】，全部【奇数】位置元素和为 【左奇 + 右偶】
        for (int i = 0; i < n; ++i) {
            // 如果 i 是奇数
            if ((i & 1) != 0) {
                // 偶数下标元素之和为 t1 + s2 - t2 - v，奇数元素下标之和为 t2 + s1 - t1
                if (t1 + s2 - t2 - nums[i] == t2 + s1 - t1) {
                    ans++;
                }
                // 更新 t2
                t2 += nums[i];
            // 如果是 i 偶数
            } else {
                // 偶数下标元素之和为 t1 + s2 - t2，奇数元素下标之和为 t2 + s1 - t1 - v
                if (t1 + s2 - t2 == t2 + s1 - t1 - nums[i]) {
                    ans++;
                }
                // 更新t1
                t1 += nums[i];
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        _1664_WaysToMakeAFairArray obj = new _1664_WaysToMakeAFairArray();
        System.out.println(obj.waysToMakeFair(new int[]{2, 1, 6, 4}));
    }
}
