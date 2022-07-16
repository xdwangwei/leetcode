package com.window;

/**
 * @author wangwei
 * 2020/7/29 11:25
 *
 * 给定一个二进制数组， 计算其中最大连续1的个数。
 *
 * 示例 1:
 *
 * 输入: [1,1,0,1,1,1]
 * 输出: 3
 * 解释: 开头的两位和最后的三位都是连续1，所以最大连续1的个数是 3.
 * 注意：
 *
 * 输入的数组只包含 0 和1。
 * 输入数组的长度是正整数，且不超过 10,000。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/max-consecutive-ones
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class _485_MaxConsecutiveOnes1 {

    /**
     * 一次遍历
     *
     * 用一个计数器 count 记录当前连续 1 的数量，另一个计数器 maxCount 记录之前最大的连续 1 的数量。
     * 当我们遇到 1 时，count 加一。
     * 当我们遇到 0 时：
     * 将 count 与 maxCount 比较，maxCoiunt 记录较大值。
     * 将 count 设为 0。
     * 返回 maxCount
     *
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/max-consecutive-ones/solution/zui-da-lian-xu-1de-ge-shu-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。用一个计数器 count 记录 1 的数量，另一个计数器 maxCount 记录当前最大的 1 的数量。
     *
     * @param nums
     * @return
     */
    public int findMaxConsecutiveOnes1(int[] nums) {
        int count = 0, maxCount = 0;
        for (int i = 0; i < nums.length; i++) {
            // 当前连续1个数
            if (nums[i] == 1) {
                count++;
            // 连续1断了
            } else {
                // 更新结果
                maxCount = Math.max(count, maxCount);
                // 连续1归0
                count = 0;
            }
        }
        // 不能直接返回maxCount，比如 0001111，这种最终没有遇到0，未触发最大值的更新
        return Math.max(count, maxCount);
    }

    /**
     * 双指针，i是每一段连续1的开始，j是每一段连续1的结束
     * @param nums
     * @return
     */
    public int findMaxConsecutiveOnes2(int[] nums) {
        int i = -1, j = 0, res = 0;
        while (j < nums.length) {
            // 连续1
            if (nums[j] == 1) {
                // 如果是第一个1，i记录起始位置
                if (i == -1) i = j;
            // 连续1断了
            } else {
                // 更新结果
                if (i != -1 && j - i > res) res = j - i;
                // 连续1归0，等待下一段连续1
                i = -1;
            }
            j++;
        }
        // 不能直接返回maxCount，比如 0001111，这种最终没有遇到0，未触发最大值的更新
        if (i != -1 && j - i > res)
            // 更新结果
            res = j - i;
        return res;
    }
}
