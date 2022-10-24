package com.daily;

/**
 * @author wangwei
 * @date 2022/10/24 11:04
 * @description: _915_SplitArray
 *
 * 915. 分割数组
 * 给定一个数组 nums ，将其划分为两个连续子数组 left 和 right， 使得：
 *
 * left 中的每个元素都小于或等于 right 中的每个元素。
 * left 和 right 都是非空的。
 * left 的长度要尽可能小。
 * 在完成这样的分组后返回 left 的 长度 。
 *
 * 用例可以保证存在这样的划分方法。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [5,0,3,8,6]
 * 输出：3
 * 解释：left = [5,0,3]，right = [8,6]
 * 示例 2：
 *
 * 输入：nums = [1,1,1,0,6,12]
 * 输出：4
 * 解释：left = [1,1,1,0]，right = [6,12]
 *
 *
 * 提示：
 *
 * 2 <= nums.length <= 105
 * 0 <= nums[i] <= 106
 * 可以保证至少有一种方法能够按题目所描述的那样对 nums 进行划分。
 */
public class _915_SplitArray {


    /**
     * 两次遍历
     * 根据题意，我们知道本质是求分割点，使得分割点的「左边的子数组的最大值」小于等于「右边的子数组的最小值」。
     *
     * 我们可以先通过一次遍历（从后往前）统计出
     *      所有前缀的最大值 leftMax，其中 leftMax[i] = x 含义为下标范围在 [0,i] 的 nums[i] 的最大值为 x，
     *      所有后缀的最小值 rightMin，其中 rightMin[i] = x 含义为下标范围在 [i,n−1] 的 nums[i] 的最小值为 x，
     * 然后再通过第二次遍历（从前往后），找到第一个符合leftMax[i] <= rightMin[i + 1] 的分割点即是答案。
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/partition-array-into-disjoint-intervals/solution/by-ac_oier-yyen/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int partitionDisjoint(int[] nums) {
        int n = nums.length;
        // leftMax[i] = x 含义为下标范围在 [0,i] 的 nums[i] 的最大值为 x，
        int[] leftMax = new int[n];
        leftMax[0] = nums[0];
        // rightMin[i] = x 含义为下标范围在 [i,n−1] 的 nums[i] 的最小值为 x，
        int[] rightMin = new int[n];
        rightMin[n - 1] = nums[n - 1];
        // 第一次遍历，找到所有前缀最大值，后缀最小值
        for (int i = 1; i < n - 1; ++i) {
            int j = n - i - 1;
            leftMax[i] = Math.max(leftMax[i - 1], nums[i]);
            rightMin[j] = Math.min(rightMin[j + 1], nums[j]);
        }
        // 第二次遍历，找到第一个符合要求的分割点
        for (int i = 0; i < n - 1; ++i) {
            if (leftMax[i] <= rightMin[i + 1]) {
                // 返回left长度
                return i + 1;
            }
        }
        // 题目保证一定有解，所以不会走到这里
        return 0;
    }

    /**
     * 两次遍历空间优化，
     *
     * 在第一次遍历过程中只需要统计所有rightMin[i]
     * 我们完全可以在第二次遍历的同时维护nums[0...i]的最大值，并寻找符合要求的第一个分割点
     *
     * 作者：AC_OIer
     * 链接：https://leetcode.cn/problems/partition-array-into-disjoint-intervals/solution/by-ac_oier-yyen/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int partitionDisjoint2(int[] nums) {
        int n = nums.length;
        // rightMin[i] = x 含义为下标范围在 [i,n−1] 的 nums[i] 的最小值为 x，
        int[] rightMin = new int[n];
        rightMin[n - 1] = nums[n - 1];
        // 第一次遍历，找到所有前缀最大值，后缀最小值
        for (int i = n - 2; i >= 0; --i) {
            rightMin[i] = Math.min(rightMin[i + 1], nums[i]);
        }
        // leftMax代表nums[0...i]的最大值
        int leftMax = 0;
        // 第二次遍历，找到第一个符合要求的分割点
        for (int i = 0; i < n - 1; ++i) {
            leftMax = Math.max(leftMax, nums[i]);
            if (leftMax <= rightMin[i + 1]) {
                // 返回left长度
                return i + 1;
            }
        }
        // 题目保证一定有解，所以不会走到这里
        return 0;
    }


    /**
     * 一次遍历
     *
     * 假设我们预先规定了一个 left 的划分，其最大值为 maxLeft，划分位置为 pos，表示 nums[0,pos] 都属于 left。
     * 如果 pos 右侧所有元素都大于等于它，那么该划分方案是合法的。
     *
     * 但如果我们遇到 nums[i]，其中 i>pos，并且 nums[i] < maxLeft，那么意味着 pos 作为划分位置是非法的，需要把当前元素划入到left中，也就是更新 pos=i，以及 maxLeft=max(0....nums[i])。
     *
     * 因此，我们首先初始化 maxLeft=nums[0]，pos=0，然后在 [1,n−2] 范围内从小到大遍历 i，过程中维护一个变量 curMax，它的值是 max(0....nums[i])。
     * 此时如果有 nums[i] < maxLeft，就按照上述方法更新。(pos = i; leftMax = curMax)
     *
     * 最终遍历结束时，答案就是 pos+1。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/partition-array-into-disjoint-intervals/solution/fen-ge-shu-zu-by-leetcode-solution-t4pm/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    public int partitionDisjoint3(int[] nums) {
        int n = nums.length;
        // pos 划分位置，nums[0,pos] 都属于 left，其最大值为 maxLeft，pos右边的元素必须大于等于leftMax
        // curMax 始终代表 nums[0...i] 的最大值
        int pos = 0, curMax = nums[0], leftMax = nums[0];
        // 向后扫描
        for (int i = 1; i < n - 1; ++i) {
            // 更新curMax
            curMax = Math.max(curMax, nums[i]);
            // 发现pos右边出现了比leftMax小的元素，此时pos非法，必须把此元素划入left
            if (nums[i] < leftMax) {
                // 更新pos和leftMax
                pos = i;
                leftMax = curMax;
            }
        }
        // 返回
        return pos + 1;
    }
}
