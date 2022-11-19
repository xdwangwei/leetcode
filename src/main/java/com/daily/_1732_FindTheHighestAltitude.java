package com.daily;

/**
 * @author wangwei
 * @date 2022/11/19 12:13
 * @description: _1732_FindTheHighestAltitude
 *
 * 1732. 找到最高海拔
 * 有一个自行车手打算进行一场公路骑行，这条路线总共由 n + 1 个不同海拔的点组成。自行车手从海拔为 0 的点 0 开始骑行。
 *
 * 给你一个长度为 n 的整数数组 gain ，其中 gain[i] 是点 i 和点 i + 1 的 净海拔高度差（0 <= i < n）。请你返回 最高点的海拔 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：gain = [-5,1,5,0,-7]
 * 输出：1
 * 解释：海拔高度依次为 [0,-5,-4,1,1,-6] 。最高海拔为 1 。
 * 示例 2：
 *
 * 输入：gain = [-4,-3,-2,-1,4,3,2]
 * 输出：0
 * 解释：海拔高度依次为 [0,-4,-7,-9,-10,-6,-3,-1] 。最高海拔为 0 。
 *
 *
 * 提示：
 *
 * n == gain.length
 * 1 <= n <= 100
 * -100 <= gain[i] <= 100
 */
public class _1732_FindTheHighestAltitude {

    /**
     *
     * 我们假设每个点的海拔为 h[i]，gain[i] = h[i+1]-h[i]
     * 那么：
     * sum[0...n-1] = gain[i] = h_1 - h_0 + h_2 - h_1 + ... + h_n - h_n-1 = h_n
     *
     * 即：
     *
     * h_i+1 = sum(gain[0]...g[i])
     *
     * 可以发现，每个点的海拔都可以通过前缀和的方式计算出来。因此，我们只需要遍历一遍数组，求出前缀和的最大值，即为最高点的海拔。
     *
     *
     * 实际上题目给出的是个差分数组，并说明第一个元素值为0，对差分数组求前缀和即可得到原海拔数组。然后，遍历返回最大值
     *
     * 作者：lcbin
     * 链接：https://leetcode.cn/problems/find-the-highest-altitude/solution/by-lcbin-9oun/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param gain
     * @return
     */
    public int largestAltitude(int[] gain) {
        int ans = 0, cur = 0;
        // 差分数组求前缀和恢复原数组每个位置元素，返回其中最大值
        for (int g : gain) {
            cur += g;
            ans = Math.max(ans, cur);
        }
        return ans;
    }
}
