package com.daily;

/**
 * @author wangwei
 * @date 2022/10/4 11:34
 * @description: _1694_ReformatTelephoneNumber
 *
 * 1694. 重新格式化电话号码
 * 给你一个字符串形式的电话号码 number 。number 由数字、空格 ' '、和破折号 '-' 组成。
 *
 * 请你按下述方式重新格式化电话号码。
 *
 * 首先，删除 所有的空格和破折号。
 * 其次，将数组从左到右 每 3 个一组 分块，直到 剩下 4 个或更少数字。剩下的数字将按下述规定再分块：
 * 2 个数字：单个含 2 个数字的块。
 * 3 个数字：单个含 3 个数字的块。
 * 4 个数字：两个分别含 2 个数字的块。
 * 最后用破折号将这些块连接起来。注意，重新格式化过程中 不应该 生成仅含 1 个数字的块，并且 最多 生成两个含 2 个数字的块。
 *
 * 返回格式化后的电话号码。
 *
 *
 *
 * 示例 1：
 *
 * 输入：number = "1-23-45 6"
 * 输出："123-456"
 * 解释：数字是 "123456"
 * 步骤 1：共有超过 4 个数字，所以先取 3 个数字分为一组。第 1 个块是 "123" 。
 * 步骤 2：剩下 3 个数字，将它们放入单个含 3 个数字的块。第 2 个块是 "456" 。
 * 连接这些块后得到 "123-456" 。
 * 示例 2：
 *
 * 输入：number = "123 4-567"
 * 输出："123-45-67"
 * 解释：数字是 "1234567".
 * 步骤 1：共有超过 4 个数字，所以先取 3 个数字分为一组。第 1 个块是 "123" 。
 * 步骤 2：剩下 4 个数字，所以将它们分成两个含 2 个数字的块。这 2 块分别是 "45" 和 "67" 。
 * 连接这些块后得到 "123-45-67" 。
 * 示例 3：
 *
 * 输入：number = "123 4-5678"
 * 输出："123-456-78"
 * 解释：数字是 "12345678" 。
 * 步骤 1：第 1 个块 "123" 。
 * 步骤 2：第 2 个块 "456" 。
 * 步骤 3：剩下 2 个数字，将它们放入单个含 2 个数字的块。第 3 个块是 "78" 。
 * 连接这些块后得到 "123-456-78" 。
 * 示例 4：
 *
 * 输入：number = "12"
 * 输出："12"
 * 示例 5：
 *
 * 输入：number = "--17-5 229 35-39475 "
 * 输出："175-229-353-94-75"
 *
 *
 * 提示：
 *
 * 2 <= number.length <= 100
 * number 由数字和字符 '-' 及 ' ' 组成。
 * number 中至少含 2 个数字。
 */
public class _1694_ReformatTelephoneNumber {


    /**
     *
     * 先去除 原串中的 空格和-，
     *
     * 随后我们对 新串 进行一次遍历。在遍历的过程中，我们可以存储剩余的数字数量 n 以及当前遍历到的字符位置 i：
     *
     * 当 n>4 时，我们取出三个连续的字符，作为一个块；
     *
     * 当 n≤4 时，我们根据题目的要求，将剩余的 n 个字符进行分块，并结束遍历。
     *          n == 4，两两分块
     *          n < 4，分为一个块
     *
     * 我们还需要在块之间添加破折号。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/reformat-phone-number/solution/zhong-xin-ge-shi-hua-dian-hua-hao-ma-by-nf6g3/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param number
     * @return
     */
    public String reformatNumber(String number) {
        // 先去除 原串中的 空格和-，
        number = number.replaceAll("[- ]", "");
        StringBuilder builder = new StringBuilder();
        // 存储剩余的数字数量 n 以及当前遍历到的字符位置 i：
        int n = number.length();
        int i = 0;
        while (n > 0) {
            // 当 n>4 时，我们取出三个连续的字符，作为一个块；
            if (n > 4) {
                builder.append(number, i, i + 3).append("-");
                // 更新位置索引i和剩余字符数n
                n -= 3;
                i += 3;
            } else {
                // 当 n≤4 时，我们根据题目的要求，将剩余的 n 个字符进行分块，并结束遍历。
                if (n == 4) {
                    // n == 4，两两分块，注意参数传值
                    builder.append(number, i, i + 2).append("-").append(number, i + 2, i + 4);
                } else {
                    // n < 4，分为一个块，注意参数传值
                    builder.append(number, i, i + n);
                }
            }
        }
        return builder.toString();
    }
}
