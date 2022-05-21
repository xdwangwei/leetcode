package com.daily;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author wangwei
 * @date 2022/5/20 19:06
 * @description: _436_FindRightInterval
 *
 * 436. 寻找右区间
 * 给你一个区间数组 intervals ，其中 intervals[i] = [starti, endi] ，且每个 starti 都 不同 。
 *
 * 区间 i 的 右侧区间 可以记作区间 j ，并满足 startj >= endi ，且 startj 最小化 。
 *
 * 返回一个由每个区间 i 的 右侧区间 在 intervals 中对应下标组成的数组。如果某个区间 i 不存在对应的 右侧区间 ，则下标 i 处的值设为 -1 。
 *
 *
 * 示例 1：
 *
 * 输入：intervals = [[1,2]]
 * 输出：[-1]
 * 解释：集合中只有一个区间，所以输出-1。
 * 示例 2：
 *
 * 输入：intervals = [[3,4],[2,3],[1,2]]
 * 输出：[-1,0,1]
 * 解释：对于 [3,4] ，没有满足条件的“右侧”区间。
 * 对于 [2,3] ，区间[3,4]具有最小的“右”起点;
 * 对于 [1,2] ，区间[2,3]具有最小的“右”起点。
 * 示例 3：
 *
 * 输入：intervals = [[1,4],[2,3],[3,4]]
 * 输出：[-1,2,-1]
 * 解释：对于区间 [1,4] 和 [3,4] ，没有满足条件的“右侧”区间。
 * 对于 [2,3] ，区间 [3,4] 有最小的“右”起点。
 */
public class _436_FindRightInterval {


    /**
     *
     * 总结：对于 区间[a,b] 找到所有区间中左端点>=b 的最小的 区间 在原区间数组中的索引
     *
     * 最简单的解决方案是对于集合中的每个区间，我们扫描所有区间找到其起点大于当前区间的终点的区间（具有最先差值），时间复杂度为 O(n^2)，在此我们不详细描述。
     *
     * 方法一：二分查找
     *
     * 如果所有区间按照左端点有序，那么 给定一个右端点，就可以用寻找左边界的二分搜素来查找目标位置
     *
     * 需要注意的是：对于每一个右端点，我们找到的目标区间的索引是在初始区间数组中的索引
     * 而我们要对区间进行排序，会改变位置
     *
     * 所以：这里借助 索引数组，对索引数组进行排序，排序时比较的是当前索引对应的区间左端点大小
     *
     * 排序完后，interval[index[0]],interval[index[1]],interval[index[2]],interval[index[3]].... 有序
     * 二分搜索：枚举 0 - n， mid = l + (h - l) / 2; 比较 intervals[index[mid]][0] < 目标右端点
     *
     * 如果觉得索引数组不好用，可以 创建 class {int start, int idx} 来保存原区间左端点及其对应区间索引
     * 或者 创建数组 intervalLeft[n][2] 来保存 原区间 左端点 和 索引值
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/find-right-interval/solution/xun-zhao-you-qu-jian-by-leetcode-solutio-w2ic/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param intervals
     * @return
     */
    public int[] findRightInterval(int[][] intervals) {
        int n = intervals.length;
        // 索引数组
        Integer[] index = new Integer[n];
        for (int i = 0; i < n; ++i) {
            // 初始化
            index[i] = i;
        }
        // 索引数组排序，排序时比较的是索引对应的区间的左端点大小
        Arrays.sort(index, Comparator.comparingInt(a -> intervals[a][0]));
        // 结果值
        // ans[i] 表示 所有 interval 中，左端点 >= interval[i]右端点 的最小 区间在原区间数组中的索引
        int[] ans = new int[n];
        // 对于原来每一个区间
        for (int i = 0; i < n; ++i) {
            // 右端点值
            int target = intervals[i][1];
            // 二分搜索，intervals[i][0] 按照 index[i] 有序
            int l = 0, h = n;
            while (l < h) {
                int mid = l + (h - l) / 2;
                // 注意这里，intervals 是按照 index[mid]指定的索引才有序
                if (intervals[index[mid]][0] < target) {
                    l = mid + 1;
                } else {
                    // 找寻左边界的二分搜索
                    h = mid;
                }
            }
            // 如果不存在
            if (l < 0 || l == n) {
                ans[i] = -1;
            } else {
                // 注意这里，是 index[l]，枚举的0-n是要找 index[l]
                ans[i] = index[l];
            }
        }
        // 返回
        return ans;
    }


    public static void main(String[] args) {
        _436_FindRightInterval obj = new _436_FindRightInterval();
    }

}
