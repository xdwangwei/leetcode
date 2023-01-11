package com.daily;

/**
 * @author wangwei
 * @date 2023/1/11 16:31
 * @description: CheckIfNumberHasEqualDigitCountAndDigitNumber
 *
 * 2283. 判断一个数的数字计数是否等于数位的值
 * 给你一个下标从 0 开始长度为 n 的字符串 num ，它只包含数字。
 *
 * 如果对于 每个 0 <= i < n 的下标 i ，都满足数位 i 在 num 中出现了 num[i]次，那么请你返回 true ，否则返回 false 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：num = "1210"
 * 输出：true
 * 解释：
 * num[0] = '1' 。数字 0 在 num 中出现了一次。
 * num[1] = '2' 。数字 1 在 num 中出现了两次。
 * num[2] = '1' 。数字 2 在 num 中出现了一次。
 * num[3] = '0' 。数字 3 在 num 中出现了零次。
 * "1210" 满足题目要求条件，所以返回 true 。
 * 示例 2：
 *
 * 输入：num = "030"
 * 输出：false
 * 解释：
 * num[0] = '0' 。数字 0 应该出现 0 次，但是在 num 中出现了一次。
 * num[1] = '3' 。数字 1 应该出现 3 次，但是在 num 中出现了零次。
 * num[2] = '0' 。数字 2 在 num 中出现了 0 次。
 * 下标 0 和 1 都违反了题目要求，所以返回 false 。
 *
 *
 * 提示：
 *
 * n == num.length
 * 1 <= n <= 10
 * num 只包含数字。
 * 通过次数14,438提交次数17,924
 */
public class _2283_CheckIfNumberHasEqualDigitCountAndDigitNumber {

    /**
     * 哈希表
     *
     * 题目：若 nums[i] = x，则 i 必须在 nums 中出现 x 次
     *
     * 首先将 nums 中的每一个数字的出现次数用哈希表做一个统计，然后遍历每一个位置i来判断是否满足 cnt[i] = nums[i]即可。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/check-if-number-has-equal-digit-count-and-digit-value/solution/pan-duan-yi-ge-shu-de-shu-zi-ji-shu-shi-ozwa7/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param num
     * @return
     */
    public boolean digitCount(String num) {
        // num中只包含数字0-9，
        int[] cnt = new int[10];
        // 统计 num 中每个数字的出现次数
        for (int i = 0; i < num.length(); ++i) {
            cnt[num.charAt(i) - '0']++;
        }
        // 遍历，判断 cnt[i] = num[i] 是否成立
        for (int i = 0; i < num.length(); ++i) {
            if (cnt[i] != num.charAt(i) - '0') {
                return false;
            }
        }
        return true;
    }
}
