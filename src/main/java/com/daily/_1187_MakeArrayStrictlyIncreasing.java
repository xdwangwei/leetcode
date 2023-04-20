package com.daily;

import java.util.Arrays;

/**
 * @author wangwei
 * @date 2023/4/19 21:22
 * @description: _1187_MakeArrayStrictlyIncreasing
 *
 * 1187. 使数组严格递增
 * 给你两个整数数组 arr1 和 arr2，返回使 arr1 严格递增所需要的最小「操作」数（可能为 0）。
 *
 * 每一步「操作」中，你可以分别从 arr1 和 arr2 中各选出一个索引，分别为 i 和 j，0 <= i < arr1.length 和 0 <= j < arr2.length，然后进行赋值运算 arr1[i] = arr2[j]。
 *
 * 如果无法让 arr1 严格递增，请返回 -1。
 *
 *
 *
 * 示例 1：
 *
 * 输入：arr1 = [1,5,3,6,7], arr2 = [1,3,2,4]
 * 输出：1
 * 解释：用 2 来替换 5，之后 arr1 = [1, 2, 3, 6, 7]。
 * 示例 2：
 *
 * 输入：arr1 = [1,5,3,6,7], arr2 = [4,3,1]
 * 输出：2
 * 解释：用 3 来替换 5，然后用 4 来替换 3，得到 arr1 = [1, 3, 4, 6, 7]。
 * 示例 3：
 *
 * 输入：arr1 = [1,5,3,6,7], arr2 = [1,6,3,3]
 * 输出：-1
 * 解释：无法使 arr1 严格递增。
 *
 *
 * 提示：
 *
 * 1 <= arr1.length, arr2.length <= 2000
 * 0 <= arr1[i], arr2[i] <= 10^9
 *
 *
 * 通过次数3,239提交次数6,674
 */
public class _1187_MakeArrayStrictlyIncreasing {

    /**
     * 方法一：动态规划
     *
     * 入手点：arr[1]中每个元素只有两种状态：保留 、 被替换
     *
     * 我们定义 f[i] 表示将 arr1[0,..,i] 转换为严格递增数组，且 arr1[i] 不被替换的最小操作数。
     *
     * 为什么只考虑不替换 arr1[i] 的状态？
     *
     * 因为如果替换该元素，那么到底替换成arr2中的哪个元素，此时就需要另加一个状态维护。
     *
     * 数组 arr1 中的每个元素都可能被替换，第一项、最后一项都可能被替换，这样我们不容易得到要返回的到底是什么
     * 但如果在数组前面增加一个非常小的数，这个数最优选择一定是不被替换；如果在数组最后添加一个非常大的数，这个数一定也不需要被替换，这样我们就可以直接返回 f[最后位置]，这也保证了当前的子状态中一定包含最优解。
     *
     * 对于数组 arr2 中的元素，它只用于替换 arr1 中的元素，索引可以指定，而最终 arr1 必须严格递增，也就是不允许有重复元素，
     * 所以可以不考虑arr2数组中的重复元素，因此预处理去除 arr2的重复元素。
     * 并且对arr2进行排序，这样我们可以通过二分搜索快速得到适合 arr1 元素替换的位置
     *
     * 假设数组 arr1 的长度为 n，数组 arr2 的长度为  m，此时可以知道替换的次数最多为 min(n,m)
     *
     * 状态转移
     *
     * 对于第 i 个元素考虑 dp[i]，由于dp定义不能替换 arr[i]，所以子问题需要考虑 i 之前位置元素的 保留与否
     *
     * 假设 i 之前上一个不需要被替换的元素为 arr1[k]，
     * 则此时 [k+1,i−1] 之间的元素均被替换，
     * 即 arr1[k+1],arr1[k+2],⋯,arr1[i−1] 连续的 i−k−1 个元素均被替换，
     * 此时需要的最小的替换次数为 dp[k] + i−k−1。
     *
     * 根据以上分析可知，需要尝试替换 i 之前的连续 j 个元素，分为以下两种情形讨论:
     *
     *      arr[i] 之前替换的元素为 0 个，即此时保留 arr[i−1]。
     *          如果要保留 arr1[i−1]，则此时一定满足 arr1[i−1]<arr1[i]，此时递推公式为：
     *                  dp[i]=min(dp[i],dp[i−1])
     *
     *      arr1[i] 之前替换的元素为 j（j>0）个，此时 arr1[i]，arr1[i−j−1] 均被保留，
     *              即 arr1[i−j],arr1[i−j+1],⋯,arr1[i−1] 连续的 j 个元素被替换。
     *              如何操作才能使上述替换一定能成立呢？最优选择肯定是在 arr2 中也找到连续的 j 个元素来替换它们。
     *              假设替换的 j 个元素为 arr2[k],arr2[k+1],⋯,arr2[k+j−1]，
     *              由于 arr2 已经是有序的，一定满足 arr2[k]<arr2[k+1]<⋯<arr2[k+j−1]，则这 j 个元素需要满足如下条件即可进行替换：
     *
     *              最小的元素 arr2[k] 一定需要大于 arr1[i−j−1]；
     *              最大的元素 arr2[k+j−1] 一定需要小于 arr1[i]；
     *              上述情形下的此时递推公式即为：dp[i]=min(dp[i],dp[i−j−1]+j)
     *                                          if exist arr2[k]>arr1[i−j−1],arr2[k+j−1]<arr1[i]
     *
     *              根据以上分析可以知道题目难点在于如何找到连续替换的元素。
     *              给定 arr1 的第 i 个元素，此时需要在 arr2 中找到连续的 j 个元素替换 arr1[i] 前面的 j 个元素，
     *              其中最小的元素满足大于 arr1[i−j−1]，最大的元素满足小于 arr1[i]。
     *
     *              根据贪心原则，用以下两种办法均可：
     *
     *                  查找替换元素的左侧起点：通过二分查找可以在 O(logm) 时间复杂度内找到严格大于 arr1[i−j−1] 的最小元素arr2[k]。
     *                      由于需要替换 j 个元素，再检测替换的最大元素arr2[k+j−1] 是否小于 arr1[i] 即可；
     *                  查找替换元素的右侧终点：通过二分查找可以在 O(logm) 时间复杂度内找到严格小于 arr1[i] 的最大元素 arr2[k]。
     *                      由于需要替换 j 个元素，再检测替换的最小元素 arr2[k−j+1] 是否大于 arr1[i−j−1] 即可；
     *
     *                  由于数组 arr1[i] 起点与终点的「哨兵」一定不会被替换的，因此添加「哨兵」不影响最终结果，最终返回 dp[n] 即可。
     *
     *                  上述方法可以进一步优化，首先可以用二分查找找到替换元素的右侧终点，
     *                  即用二分查找找到严格小于 arr1[i] 的最大元素 arr 2[k]，
     *                  然后从 k 起始依次向前枚举连续替换元素的个数 j，即 [arr1[i−j],arr1[i−j+1],⋯,arr1[i−1]] 连续 j 个元素被替换，
     *                  只需要检测 arr2[k−j] > arr1[i−j−1] ，时间复杂度可以进一步优化。
     *
     * 实现：
     *
     * 我们在 arr1 设置首尾两个哨兵 −∞ 和 ∞。得到新的数组 arr，长度为 n+2
     * f[0]一定不需要替换，所以 初始化 f[0]=0，其余 f[i]=∞。
     * 最后一个数一定是不替换，因此 f[n+1] 即为答案。
     *
     * 对数组 arr2 进行排序并去重，方便进行二分查找。
     *
     * 递推：对于 i=1,..,n−1，我们考虑 i 之前上一个不需要被替换的元素为 arr[?]，此时 [?+1,i−1] 之间共 i-?-1 个元素均被替换，
     *      即 考虑 被替换的元素个数为 k
     *      如果 k = 0，必须满足 arr[i−1] < arr[i]，那么 f[i] 可以从 f[i−1] 转移而来，即 f[i]=f[i−1]。
     *      如果 k > 0，那么 上一个不需要被替换的元素为 arr[i-k-1]
     *          根据上面内容，假如 arr2 中 小于 arr[i] 的最大下标为 j
     *          那么， arr[i-k-1] 需要满足 arr[i-k-1] < arr2[j+1-k]
     *          此时，dp[i] = Math.min(dp[i], dp[i - 1 - k] + k);
     *
     * 最后，如果 f[n−1]≥∞，说明无法转换为严格递增数组，返回 −1，否则返回 f[n−1]。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/make-array-strictly-increasing/solution/python3javacgotypescript-yi-ti-yi-jie-do-j5ef/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param arr1
     * @param arr2
     * @return
     */
    public int makeArrayIncreasing(int[] arr1, int[] arr2) {
        // arr2 排序，去重，m 是 arr2 排序去重后的有效部分长度，即 arr2[0...m-1]
        Arrays.sort(arr2);
        int m = 0;
        for (int x : arr2) {
            if (m == 0 || x != arr2[m - 1]) {
                arr2[m++] = x;
            }
        }
        // 给 arr1 增加前后哨兵 -inf 和 inf，得到新数组 arr，长度为 n+2
        int n = arr1.length;
        int[] arr = new int[n + 2];
        final int inf = 1 << 30;
        arr[0] = -inf;
        arr[arr.length - 1] = inf;
        System.arraycopy(arr1, 0, arr, 1, arr1.length);
        // 更新 n
        n += 2;
        // f[i] 表示将 arr1[0,..,i] 转换为严格递增数组，且 arr1[i] 不被替换的最小操作数。
        int[] dp = new int[n];
        // 初始化 dp 为最大值 inf
        Arrays.fill(dp, inf);
        // 左边哨兵必然不需要被替换，所以得到 base case，
        dp[0] = 0;
        // 递推，arr[i]不替换，上一个不替换位置是 arr[?]，假设从 ?+1 到 i-1 之间的元素个数是 k
        for (int i = 1; i < n; ++i) {
            // 如果 需要被替换的元素个数 k 是 0，必须满足 arr[i] > arr[i - 1]，此时可以先更新 dp[i] = dp[i - 1];
            if (arr[i] > arr[i - 1]) {
                dp[i] = dp[i - 1];
            }
            // 再考虑需要被替换的元素个数 k 大于0，那么上一个不被替换位置是 arr[i-1-k]
            // arr2 中 小于 arr[i] 的最大下标为 j
            int j = rightBoundSearch(arr2, m - 1, arr[i]);
            for (int k = 1; k <= Math.min(i - 1, j + 1); ++k) {
                // 必然满足 arr[i - 1 - k] < arr2[j + 1 - k] 才能把 arr中 [i-k...i-1] 这部分全替换成功
                if (arr[i - 1 - k] < arr2[j + 1 - k]) {
                    // 更新，dp[i]
                    dp[i] = Math.min(dp[i], dp[i - 1 - k] + k);
                }
            }
        }
        // 需要返回 dp[n-1]。判断是否有解
        return dp[n - 1] >= inf ? -1 : dp[n - 1];
    }

    /**
     * 在严格递增数组 arr[0...right] 范围内寻找 小于 target 的最大元素的下标
     * @param arr
     * @param right
     * @param target
     * @return
     */
    private int rightBoundSearch(int[] arr, int right, int target) {
        int l = 0, r = right + 1;
        while (l < r) {
            int m = (l + r) >> 1;
            if (arr[m] < target) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        // arr[m]符合条件时，会执行语句 l=m+1，所以最终 l-1 是合理的答案
        return l - 1;
    }
}
