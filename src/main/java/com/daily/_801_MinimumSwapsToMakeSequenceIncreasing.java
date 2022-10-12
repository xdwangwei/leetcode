package com.daily;

/**
 * @author wangwei
 * @date 2022/10/10 13:50
 * @description: _801_MinimumSwapsToMakeSequenceIncreasing
 *
 * 801. 使序列递增的最小交换次数
 * 我们有两个长度相等且不为空的整型数组 nums1 和 nums2 。在一次操作中，我们可以交换 nums1[i] 和 nums2[i]的元素。
 *
 * 例如，如果 nums1 = [1,2,3,8] ， nums2 =[5,6,7,4] ，你可以交换 i = 3 处的元素，得到 nums1 =[1,2,3,4] 和 nums2 =[5,6,7,8] 。
 * 返回 使 nums1 和 nums2 严格递增 所需操作的最小次数 。
 *
 * 数组 arr 严格递增 且  arr[0] < arr[1] < arr[2] < ... < arr[arr.length - 1] 。
 *
 * 注意：
 *
 * 用例保证可以实现操作。
 *
 *
 * 示例 1:
 *
 * 输入: nums1 = [1,3,5,4], nums2 = [1,2,3,7]
 * 输出: 1
 * 解释:
 * 交换 A[3] 和 B[3] 后，两个数组如下:
 * A = [1, 3, 5, 7] ， B = [1, 2, 3, 4]
 * 两个数组均为严格递增的。
 * 示例 2:
 *
 * 输入: nums1 = [0,3,5,8,9], nums2 = [2,1,4,6,9]
 * 输出: 1
 *
 *
 * 提示:
 *
 * 2 <= nums1.length <= 10^5
 * nums2.length == nums1.length
 * 0 <= nums1[i], nums2[i] <= 2 * 10^5
 */
public class _801_MinimumSwapsToMakeSequenceIncreasing {

    /**
     * 方法一：动态规划
     * 思路与算法
     *
     * 题目给定两个长度都为 n 的整型数组 nums1,2nums，每次操作我们可以交换 nums1 和 \nums2中相同位置上的数字。
     * 我们需要求使 nums1 和 nums2严格递增的最小操作次数，题目保证题目用例可以实现操作。
     * 因为每次只能交换相同位置的两个数，所以位置 i 一定至少满足以下两种情况中的一种：
     *      1. nums1[i] > nums1[i - 1] and nums2[i] > nums2[i - 1]
     *      2. nums1[i] > nums2[i - 1] and nums1[i] > nums2[i - 1]
     *
     * 否则无论是否交换 nums1[i] 和 nums2[i] 都不可能使数组 nums1 和 nums2最终严格递增。
     * 因为对于某一个位置来说只有交换和不交换两种情况，
     * 所以我们可以设 dp[i][j]表示下标范围为 [0,i] 的元素，且位置 i 的交换状态为 j 时（其中 j=0 为不交换，j=1 为交换）两数组满足严格递增的最小交换次数。
     *
     * 最终答案为 min(dp[n−1][0],dp[n−1][1])，同时我们有显而易见的初始化条件 dp[0][0]=0, dp[0][1]=1，其余未知状态初始化为正无穷
     *
     * 我们思考如何求解各个状态：
     *
     * 当只满足上述的情况1 而不满足情况2 时，位置 i 的交换情况需要和位置 i−1 的情况保持【一致】：
     *      dp[i][0] = dp[i−1][0]
     *      dp[i][1] = dp[i−1][1] + 1
     * 当只满足上述的情况2 而不满足情况1 时，位置 i 的交换情况需要和位置 i−1 的情况【相反】：
     *      dp[i][0] = dp[i−1][1]
     *      dp[i][1] = dp[i−1][0] + 1
     * 当同时满足上述的情况1 和情况2 时，dp[i][0]，dp[i][1] 取两种情况中的较小值即可：
     *      dp[i][0] = min{dp[i−1][0],dp[i−1][1]}
     *      dp[i][1] = min{dp[i−1][1],dp[i−1][0]} + 1 = dp[i][0] + 1
     *
     * 由于第三个条件是前两个都满足的情况下进行，可以通过两个if完成上述过程，相当于 走完第一个if，继续走第二个if
     *             if (nums1[i] > nums1[i - 1] && nums2[i] > nums2[i - 1]) {
     *                 f[i][0] = f[i - 1][0];
     *                 f[i][1] = f[i - 1][1] + 1;
     *             } // 这里没有else，继续判断，如果满足情况三，会把两个if都进行，否则只会走其中一个
     *             if (nums1[i] > nums2[i - 1] && nums2[i] > nums1[i - 1]) {
     *                 // 注意这里合并后的写法是，上个if的结果，和本应该当前if的结果取最小值
     *                 f[i][0] = Math.min(f[i][0], f[i - 1][1]);
     *                 f[i][1] = Math.min(f[i][1], f[i - 1][0] + 1);
     *             }
     *
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/minimum-swaps-to-make-sequences-increasing/solution/shi-xu-lie-di-zeng-de-zui-xiao-jiao-huan-ux2y/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums1
     * @param nums2
     * @return
     */
    public int minSwap(int[] nums1, int[] nums2) {
        int n = nums1.length;
        // dp[i][j]表示下标范围为 [0,i] 的元素，且位置 i 的交换状态为 j 时（其中 j=0 为不交换，j=1 为交换）两数组满足严格递增的最小交换次数。
        int[][] dp = new int[n][n];
        // base：两数组都只有一个元素
        dp[0][1] = 1;
        // 初始化
        for (int i = 1; i < n; ++i) {
            dp[i][0] = dp[i][1] = n + 1;
        }
        // 位置 i 一定至少满足以下两种情况中的一种：
        // 1. nums1[i] > nums1[i - 1] and nums2[i] > nums2[i - 1]
        // 2. nums1[i] > nums2[i - 1] and nums1[i] > nums2[i - 1]
        for (int i = 1; i < n; ++i) {
            int a1 = nums1[i - 1], a2 = nums1[i], b1 = nums2[i - 1], b2 = nums2[i];
            // 情况一和情况二都满足，前一个位置换不换都可以
            if (a2 > a1 && b2 > b1 && a2 > b1 && b2 > a1) {
                dp[i][0] = Math.min(dp[i - 1][0], dp[i - 1][1]);
                dp[i][1] = dp[i][0] + 1;
                // 满足情况一，当前位置需要和前一个位置保持一致
            } else if (a2 > a1 && b2 > b1) {
                dp[i][0] = dp[i - 1][0];
                dp[i][1] = dp[i - 1][1] + 1;
                // 满足情况二，当前位置需要和前一个位置相反
            } else {
                dp[i][0] = dp[i - 1][1];
                dp[i][1] = dp[i - 1][0] + 1;
            }
        }
        // 取较小值
        return Math.min(dp[n - 1][0], dp[n - 1][1]);
    }

    /**
     * 方法一，简化版，情况三的if省略掉，和情况二合并
     * @param nums1
     * @param nums2
     * @return
     */
    public int minSwap2(int[] nums1, int[] nums2) {
        int n = nums1.length;
        // dp[i][j]表示下标范围为 [0,i] 的元素，且位置 i 的交换状态为 j 时（其中 j=0 为不交换，j=1 为交换）两数组满足严格递增的最小交换次数。
        int[][] dp = new int[n][2];
        // base：两数组都只有一个元素
        dp[0][1] = 1;
        // 初始化
        for (int i = 1; i < n; ++i) {
            dp[i][0] = dp[i][1] = n + 1;
        }
        // 位置 i 一定至少满足以下两种情况中的一种：
        // 1. nums1[i] > nums1[i - 1] and nums2[i] > nums2[i - 1]
        // 2. nums1[i] > nums2[i - 1] and nums1[i] > nums2[i - 1]
        for (int i = 1; i < n; ++i) {
            int a1 = nums1[i - 1], a2 = nums1[i], b1 = nums2[i - 1], b2 = nums2[i];
            // 满足情况一，当前位置需要和前一个位置保持一致
            if (a2 > a1 && b2 > b1) {
                dp[i][0] = dp[i - 1][0];
                dp[i][1] = dp[i - 1][1] + 1;
            }
            // 满足情况二，当前位置需要和前一个位置相反
            if (a2 > b1 && b2 > a1){
                // 若情况一和情况二都满足，则在前一个if的结果基础上和本应该的当前if的取值 取较小值
                dp[i][0] = Math.min(dp[i][0], dp[i - 1][1]);
                dp[i][1] = Math.min(dp[i][1], dp[i - 1][0] + 1);
            }
        }
        // 取较小值
        return Math.min(dp[n - 1][0], dp[n - 1][1]);
    }

    /**
     * 注意到 dp[i][j] 只和 dp[i - 1][j] 有关，优化空间
     *
     * dp_prev_0 = 0, 相当于 dp[i][0] = 0
     * dp_prev_1 = 1; 相当于 dp[i][1] = 1
     * @param nums2
     * @return
     */
    public int minSwap3(int[] nums1, int[] nums2) {
        int n = nums1.length;
        // dp[i][j]表示下标范围为 [0,i] 的元素，且位置 i 的交换状态为 j 时（其中 j=0 为不交换，j=1 为交换）两数组满足严格递增的最小交换次数。
        // base：两数组都只有一个元素
        int dp_prev_0 = 0, dp_prev_1 = 1;
        // 位置 i 一定至少满足以下两种情况中的一种：
        // 1. nums1[i] > nums1[i - 1] and nums2[i] > nums2[i - 1]
        // 2. nums1[i] > nums2[i - 1] and nums1[i] > nums2[i - 1]
        for (int i = 1; i < n; ++i) {
            // 相当于 dp[i][0] dp[i][1] 初始化为 Infinity，递推得到 dp[i][0] dp[i][1]
            int dp_cur_0 = n + 1, dp_cur_1 = n + 1;
            int a1 = nums1[i - 1], a2 = nums1[i], b1 = nums2[i - 1], b2 = nums2[i];
            // 满足情况一，当前位置需要和前一个位置保持一致
            if (a2 > a1 && b2 > b1) {
                // dp[i][0] = dp[i - 1][0];
                // dp[i][1] = dp[i - 1][1] + 1;
                dp_cur_0 = dp_prev_0;
                dp_cur_1 = dp_prev_1 + 1;
            }
            // 满足情况二，当前位置需要和前一个位置相反
            if (a2 > b1 && b2 > a1){
                // 若情况一和情况二都满足，则在前一个if的结果基础上和本应该的当前if的取值 取较小值
                // dp[i][0] = Math.min(dp[i][0], dp[i - 1][1]);
                // dp[i][1] = Math.min(dp[i][1], dp[i - 1][0] + 1);
                dp_cur_0 = Math.min(dp_cur_0, dp_prev_1);
                dp_cur_1 = Math.min(dp_cur_1, dp_prev_0 + 1);
            }
            // 替换 cur 为 prev
            dp_prev_0 = dp_cur_0;
            dp_prev_1 = dp_cur_1;
        }
        // 取较小值
        return Math.min(dp_prev_0, dp_prev_1);
    }
}
