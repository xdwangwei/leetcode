package com.daily;

import java.util.*;

/**
 * @author wangwei
 * @date 2022/10/22 11:22
 * @description: _1235_MaximumProfitInJobScheduling
 *
 * 1235. 规划兼职工作
 * 你打算利用空闲时间来做兼职工作赚些零花钱。
 *
 * 这里有 n 份兼职工作，每份工作预计从 startTime[i] 开始到 endTime[i] 结束，报酬为 profit[i]。
 *
 * 给你一份兼职工作表，包含开始时间 startTime，结束时间 endTime 和预计报酬 profit 三个数组，请你计算并返回可以获得的最大报酬。
 *
 * 注意，时间上出现重叠的 2 份工作不能同时进行。
 *
 * 如果你选择的工作在时间 X 结束，那么你可以立刻进行在时间 X 开始的下一份工作。
 *
 *
 *
 * 示例 1：
 *
 *
 *
 * 输入：startTime = [1,2,3,3], endTime = [3,4,5,6], profit = [50,10,40,70]
 * 输出：120
 * 解释：
 * 我们选出第 1 份和第 4 份工作，
 * 时间范围是 [1-3]+[3-6]，共获得报酬 120 = 50 + 70。
 * 示例 2：
 *
 *
 *
 * 输入：startTime = [1,2,3,4,6], endTime = [3,5,10,6,9], profit = [20,20,100,70,60]
 * 输出：150
 * 解释：
 * 我们选择第 1，4，5 份工作。
 * 共获得报酬 150 = 20 + 70 + 60。
 * 示例 3：
 *
 *
 *
 * 输入：startTime = [1,1,1], endTime = [2,3,4], profit = [5,6,4]
 * 输出：6
 *
 *
 * 提示：
 *
 * 1 <= startTime.length == endTime.length == profit.length <= 5 * 10^4
 * 1 <= startTime[i] < endTime[i] <= 10^9
 * 1 <= profit[i] <= 10^4
 * 通过次数16,136提交次数30,416
 */
public class _1235_MaximumProfitInJobScheduling {

    private Map<Integer, Integer> memo = new HashMap<>();

    private int[][] jobs;

    private int n;


    /**
     * 方法一：记忆化搜索 + 二分查找
     *
     * 我们可以任意挑选一个工作开始，对于每一份工作，可以选择做，也可以选择不做，
     * 如果不做，那就可以从下一份工作开始，
     * 如果做，那么下一份工作必须要在当前工作结束之后
     *
     * 因此我们先将工作按照开始时间从小到大排序，
     * 然后设计一个函数 dfs(i) 表示从第 i 份工作开始(当前工作不一定做)，可以获得的最大报酬。
     * 最终答案即为 dfs(0)，虽然我们可以从任意一份工作开始，但是由于每一份工作都考虑了做与不做两种情况，所以从第1份工作开始考虑，就一定考虑到了全部可能性
     *
     * 函数 dfs(i) 的计算过程如下：
     *
     * 对于第 i 份工作，我们可以选择做，也可以选择不做。
     *      如果不做，最大报酬就是 dfs(i+1)；
     *      如果做，我们可以通过二分查找，找到在第 i 份工作结束时间之后开始的第一份工作，记为 j，那么最大报酬就是 profit[i]+dfs(j)。
     * 取两者的较大值即可。即：
     *
     * dfs(i)=max(dfs(i+1),profit[i]+dfs(j))
     *
     * 其中 j 是满足 startTime[j] ≥ endTime[i] 的最小的下标。 这里可以使用边界二分搜索
     *
     * 此过程中，我们可以使用记忆化搜索，将每个状态的答案保存下来，避免重复计算。
     *
     * 时间复杂度 O(n×logn)，其中 n 是工作的数量。
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/maximum-profit-in-job-scheduling/solution/by-lcbin-p1xn/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param startTime
     * @param endTime
     * @param profit
     * @return
     */
    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        n = startTime.length;
        jobs = new int[n][3];
        for (int i = 0; i < n; ++i) {
            jobs[i] = new int[]{startTime[i], endTime[i], profit[i]};
        }
        // 按照开始时间排序
        Arrays.sort(jobs, Comparator.comparingInt(a -> a[0]));
        // 返回从第1份工作开始考虑，得到的最大报酬
        return backTrack(0);
    }


    /**
     * 从当前工作开始考虑（选择），能够得到的最大报酬
     * @param idx
     * @return
     */
    private int backTrack(int idx) {
        // 因为下面做选择时，会考虑到idx+1，所以最终会使得idx超过n，
        // 不要让二分搜索找不到答案时返回-1，不然会导致无法退出
        if (idx >= n) {
            return 0;
        }
        // 已经计算过
        if (memo.containsKey(idx)) {
            return memo.get(idx);
        }
        // nextIdx ：当前工作结束后最近的可以开始的工作id，如果 不存在，那么值为n
        int nextIdx = leftBoundBS(jobs, jobs[idx][1]);
        // 当前工作不做，直接从下一个开始
        // 当前工作做，当前利润 + 从下一个开始选择 得到的利润
        // 取较大值
        int ans = Math.max(backTrack(idx + 1), jobs[idx][2] + backTrack(nextIdx));
        // 保存并返回
        memo.put(idx, ans);
        return ans;
    }

    /**
     * 在递增数组nums中寻找第一个 >= enTime 的元素位置；若 全部元素都小于 enTime，则返回-1；否则至少能返回索引0
     * 这里的 jobs 是个三维数组，每个元素是 (start, end, profit) , 他们的start是递增的，其余可以忽略
     *
     * 这里找不到答案时不要返回-1，不然会导致上面的回溯无法顺利退出
     * @param jobs
     * @param enTime
     * @return
     */
    private int leftBoundBS(int[][] jobs, int enTime) {
        int l = 0, r = n;
        while (l < r) {
            int mid = l + ((r - l) >> 1);
            if (jobs[mid][0] >= enTime) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        // 返回l
        // 若所有元素都比endTime小。l一直右移，直到 n
        return l;
    }


    /**
     * 动态规划 + 二分搜索
     * 还是先对所有工作按照开始时间排序
     * dp[i] 表示 从工作i开始选择(不一定做)，能够获得的最大利润
     * 最终答案就是 dp[0]
     * 初始时：dp[n-1] = profit[n-1]
     * @param startTime
     * @param endTime
     * @param profit
     * @return
     */
    public int jobScheduling2(int[] startTime, int[] endTime, int[] profit) {
        n = startTime.length;
        jobs = new int[n][3];
        for (int i = 0; i < n; ++i) {
            jobs[i] = new int[]{startTime[i], endTime[i], profit[i]};
        }
        // 按开始时间排序
        Arrays.sort(jobs, Comparator.comparingInt(a -> a[0]));
        // dp[i] 表示 从工作i开始选择(不一定做)，能够获得的最大利润
        int[] dp = new int[n];
        // base，最后一个工作
        dp[n - 1] = jobs[n - 1][2];
        // 逆推
        for(int i = n - 2; i >= 0; --i) {
            // 当前工作结束后，可以开始的最早的工作id
            int j = leftBoundBS(jobs, jobs[i][1]);
            // 可以不做：直接从下一个开始 dp[i + 1]
            // 也可以做：当前利润 + 当前结束后第一个可以做的工作开始考虑 jobs[i][2] + dp[j]
            dp[i] = Math.max(dp[i + 1], jobs[i][2] + (j < n ? dp[j] : 0));
        }
        // 返回
        return dp[0];
    }

    public static void main(String[] args) {
        _1235_MaximumProfitInJobScheduling obj = new _1235_MaximumProfitInJobScheduling();
        System.out.println(obj.jobScheduling(new int[]{1,2,3,3}, new int[]{3,4,5,6}, new int[]{50,10,40,70}));
        System.out.println(obj.jobScheduling(new int[]{4,2,4,8,2}, new int[]{5,5,5,10,8}, new int[]{1,2,8,10,4}));
    }
}
