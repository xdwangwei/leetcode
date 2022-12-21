package com.daily;

/**
 * @author wangwei
 * @date 2022/12/20 14:48
 * @description: _1760_MaximumBallsInBag
 *
 * 1760. 袋子里最少数目的球
 * 给你一个整数数组 nums ，其中 nums[i] 表示第 i 个袋子里球的数目。同时给你一个整数 maxOperations 。
 *
 * 你可以进行如下操作至多 maxOperations 次：
 *
 * 选择任意一个袋子，并将袋子里的球分到 2 个新的袋子中，每个袋子里都有 正整数 个球。
 * 比方说，一个袋子里有 5 个球，你可以把它们分到两个新袋子里，分别有 1 个和 4 个球，或者分别有 2 个和 3 个球。
 * 你的开销是单个袋子里球数目的 最大值 ，你想要 最小化 开销。
 *
 * 请你返回进行上述操作后的最小开销。
 *
 *
 *
 * 示例 1：
 *
 * 输入：nums = [9], maxOperations = 2
 * 输出：3
 * 解释：
 * - 将装有 9 个球的袋子分成装有 6 个和 3 个球的袋子。[9] -> [6,3] 。
 * - 将装有 6 个球的袋子分成装有 3 个和 3 个球的袋子。[6,3] -> [3,3,3] 。
 * 装有最多球的袋子里装有 3 个球，所以开销为 3 并返回 3 。
 * 示例 2：
 *
 * 输入：nums = [2,4,8,2], maxOperations = 4
 * 输出：2
 * 解释：
 * - 将装有 8 个球的袋子分成装有 4 个和 4 个球的袋子。[2,4,8,2] -> [2,4,4,4,2] 。
 * - 将装有 4 个球的袋子分成装有 2 个和 2 个球的袋子。[2,4,4,4,2] -> [2,2,2,4,4,2] 。
 * - 将装有 4 个球的袋子分成装有 2 个和 2 个球的袋子。[2,2,2,4,4,2] -> [2,2,2,2,2,4,2] 。
 * - 将装有 4 个球的袋子分成装有 2 个和 2 个球的袋子。[2,2,2,2,2,4,2] -> [2,2,2,2,2,2,2,2] 。
 * 装有最多球的袋子里装有 2 个球，所以开销为 2 并返回 2 。
 * 示例 3：
 *
 * 输入：nums = [7,17], maxOperations = 2
 * 输出：7
 *
 *
 * 提示：
 *
 * 1 <= nums.length <= 105
 * 1 <= maxOperations, nums[i] <= 109
 * 通过次数15,653提交次数25,247
 */
public class _1760_MaximumBallsInBag {


    /**
     * 方法一：二分查找
     * 思路与算法
     *
     * 我们可以将题目中的要求转换成判定问题，即：
     *
     * 给定 maxOperations 次操作次数，能否可以使得单个袋子里球数目的最大值不超过 y。
     *
     * 如果 y=y0 是一个满足要求的答案，那么所有大于 y0的 y 同样也是满足要求的。
     * 因此存在一个 y=yop，使得
     *      当 y >= yop 时都是可以maxOperations次操作内实现的，
     *      当 y < yop 时都是不满足要求的。
     *
     * 这个 yop 就是最终的答案。
     *
     * 因此，我们可以通过二分查找的方式得到答案。
     * 二分查找的下界为 1，上界为数组 nums 中的最大值，即单个袋子中最多的球数。
     *
     * 当我们二分查找到 y 时，对于第 i 个袋子，其中有 nums[i] 个球，那么需要的操作次数为：(nums[i]-1) / y
     *
     * 那么总操作次数即为：sum((nums[i]-1)/y)
     * 当 P ≤ maxOperations 时，我们调整二分查找的上界，否则调整二分查找的下界。
     *
     * 那么 (nums[i]-1) / y 这个计算公式如何来？一次拆成两部分，每部分不能超过y
     *      当 nums[i] 在 [0,y] 范围，所需操作次数为0
     *      当 nums[i] 在 [y+1,2y] 范围，所需操作次数为1（1次拆分）
     *      当 nums[i] 在 [2y+1,3y] 范围，所需操作次数为2
     *          （为了使操作更少，先1次操作，拆出y，剩下的变为[y+1,3y]，即为情况二）
     *      。。。
     *      nums[i] 所需的操作次数为 (nums[i]-1)/2
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/minimum-limit-of-balls-in-a-bag/solution/dai-zi-li-zui-shao-shu-mu-de-qiu-by-leet-boay/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param nums
     * @param maxOperations
     * @return
     */
    public int minimumSize(int[] nums, int maxOperations) {
        int max = 0;
        // 求最大值
        for (int num : nums) {
            max = Math.max(max, num);
        }
        // 寻找右侧边界的二分搜索，左闭右开写法
        // 枚举的是可能的开销值（元素上界）
        int left = 1, right = max + 1;
        // 二分
        while (left < right) {
            // 当前的开销值
            int mid = (left + right) >> 1;
            // 此种开销值下，所需要的最少操作次数
            int cost = getOps(nums, mid);
            // 超过预定值，就增加枚举开销值，从而减少所需操作次数
            if (cost > maxOperations) {
                left = mid + 1;
            } else {
                // 否则就减小枚举开销值
                right = mid;
            }
        }
        // 二分中，cost = maxOperations 时会走 缩小下界逻辑，
        // 由于题目必然有解，因此最终right即为最后一次满足<=maxOperations的枚举cost值
        return right;
    }

    /**
     * 计算，使nums所有元素小于等于target所需要的最少操作次数
     * @param nums
     * @param target
     * @return
     */
    private int getOps(int[] nums, int target) {
        int ans = 0;
        // 累加每个元素所需的最少操作次数
        for (int num : nums) {
            ans += (num - 1) / target;
        }
        return ans;
    }

}
