package com.daily;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author wangwei
 * @date 2023/3/17 18:35
 * @description: _2389_LongestSubsequenceWithLimitedSum
 *
 * 2389. 和有限的最长子序列
 * 给你一个长度为 n 的整数数组 nums ，和一个长度为 m 的整数数组 queries 。
 *
 * 返回一个长度为 m 的数组 answer ，其中 answer[i] 是 nums 中 元素之和小于等于 queries[i] 的 子序列 的 最大 长度  。
 *
 * 子序列 是由一个数组删除某些元素（也可以不删除）但不改变剩余元素顺序得到的一个数组。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [4,5,2,1], queries = [3,10,21]
 * 输出：[2,3,4]
 * 解释：queries 对应的 answer 如下：
 * - 子序列 [2,1] 的和小于或等于 3 。可以证明满足题目要求的子序列的最大长度是 2 ，所以 answer[0] = 2 。
 * - 子序列 [4,5,1] 的和小于或等于 10 。可以证明满足题目要求的子序列的最大长度是 3 ，所以 answer[1] = 3 。
 * - 子序列 [4,5,2,1] 的和小于或等于 21 。可以证明满足题目要求的子序列的最大长度是 4 ，所以 answer[2] = 4 。
 * 示例 2：
 *
 * 输入：nums = [2,3,4,5], queries = [1]
 * 输出：[0]
 * 解释：空子序列是唯一一个满足元素和小于或等于 1 的子序列，所以 answer[0] = 0 。
 *
 *
 * 提示：
 *
 * n == nums.length
 * m == queries.length
 * 1 <= n, m <= 1000
 * 1 <= nums[i], queries[i] <= 106
 * 通过次数27,637提交次数39,022
 */
public class _2389_LongestSubsequenceWithLimitedSum {


    /**
     * 方法：前缀和 + 二分搜索
     *
     * 方法一：二分查找
     * 由题意可知，根据子序列元素和的大小限制求子序列元素个数，nums 的元素次序对结果无影响，因此我们对 nums 从小到大进行排序。
     *
     * 显然和有限的最长子序列由最小的前几个数组成。
     *
     * 使用数组 f 保存 nums 的前缀和，其中 f[i] 表示前 i 个元素之和（不包括 nums[i]）。
     *
     * 遍历 queries，假设当前查询值为 q，使用二分查找获取满足 f[i]>q 的最小的 i，
     * 那么和小于等于 q 的最长子序列长度为 i−1。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/longest-subsequence-with-limited-sum/solution/he-you-xian-de-zui-chang-zi-xu-lie-by-le-xqox/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param queries
     * @return
     */
    public int[] answerQueries(int[] nums, int[] queries) {
        // 排序
        Arrays.sort(nums);
        int n = nums.length;
        // 前缀和
        int[] sum = new int[n + 1];
        for(int i = 1; i <= n ; ++i) {
            sum[i] = sum[i - 1] + nums[i - 1];
        }
        int[] ans = new int[queries.length];
        // 当前查询值为 q，使用二分查找获取满足 f[i]>q 的最小的 i，
        // 那么和小于等于 q 的最长子序列长度为 i−1。
        for (int i = 0; i < queries.length; ++i) {
            ans[i] = binarySearch(sum, queries[i]) - 1;
        }
        // 返回
        return ans;
    }


    /**
     * 二分搜索
     *
     * 在递增序列 nums 中寻找 值 大于 target 的最左侧元素下标，起始位置从1开始
     * @param nums
     * @param target
     * @return
     */
    private int binarySearch(int[] nums, int target) {
        // 寻找左边界二分搜索
        int l = 1, r = nums.length;
        while (l < r) {
            int m = (l + r) >> 1;
            // 寻找大于 target 的最左侧元素
            if (nums[m] > target) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        // nums[m] 大于 target 时， r = m，所以 r 符合要求，结束时 l == r，所以返回 l
        return l;
    }

    /**
     * 方法二：排序 + 离线查询 + 双指针
     *
     * 与方法一类似，我们可以先对数组nums 进行升序排列。
     *
     * 接下来，我们定义一个长度与 queries 相同的下标数组 idx，其中 idx[i]=i，
     * 然后我们对数组 idx 按照 queries 中的元素值进行升序排序。这样，我们就可以按照 queries 中的元素值从小到大的顺序进行处理。
     *
     * 我们使用一个变量 s 记录当前已经选择的元素的和，使用一个变量 j 记录当前已经选择的元素的个数。
     * 初始时 s=j=0。
     *
     * 我们遍历下标数组 idx，对于其中的每个下标 i，我们循环地将数组 nums 中的元素加入到当前的子序列中，直到 s+nums[j]>queries[i]，
     * 此时 j 即为满足条件的子序列的长度，我们将 ans[i] 的值设为 j，然后继续处理下一个下标。
     *
     * 遍历完下标数组 idx 后，我们即可得到答案数组 ans，其中 ans[i] 即为满足queries[i] 的子序列的长度。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/longest-subsequence-with-limited-sum/solution/python3javacgotypescript-yi-ti-yi-jie-pa-kew3/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public int[] answerQueries2(int[] nums, int[] queries) {
        // nums 排序
        Arrays.sort(nums);
        int m = queries.length;
        // queries 的下标数组
        Integer[] idx = new Integer[m];
        // 初始化
        for (int i = 0; i < m; ++i) {
            idx[i] = i;
        }
        // 下标数组排序，按照其对应的queries元素值
        Arrays.sort(idx, Comparator.comparingInt(i -> queries[i]));
        int[] ans = new int[m];
        // 顺序遍历下标，实现queries的递增遍历
        // j记录nums元素下标，s记录nums[0...j]元素和
        int s = 0, j = 0;
        for (int i : idx) {
            // 增加nums元素，直到 元素和超过 queries[i]
            // 对于下一个queries[i]，从当前位置记录累加即可
            while (j < nums.length && s + nums[j] <= queries[i]) {
                s += nums[j++];
            }
            // 记录位置
            ans[i] = j;
        }
        // 返回
        return ans;
    }
}
