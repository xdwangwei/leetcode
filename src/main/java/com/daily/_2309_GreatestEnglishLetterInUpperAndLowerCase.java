package com.daily;

/**
 * @author wangwei
 * @date 2023/1/26 20:16
 * @description: _2309_GreatestEnglishLetterInUpperAndLowerCase
 *
 * 2309. 兼具大小写的最好英文字母
 * 给你一个由英文字母组成的字符串 s ，请你找出并返回 s 中的 最好 英文字母。返回的字母必须为大写形式。如果不存在满足条件的字母，则返回一个空字符串。
 *
 * 最好 英文字母的大写和小写形式必须 都 在 s 中出现。
 *
 * 英文字母 b 比另一个英文字母 a 更好 的前提是：英文字母表中，b 在 a 之 后 出现。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "lEeTcOdE"
 * 输出："E"
 * 解释：
 * 字母 'E' 是唯一一个大写和小写形式都出现的字母。
 * 示例 2：
 *
 * 输入：s = "arRAzFif"
 * 输出："R"
 * 解释：
 * 字母 'R' 是大写和小写形式都出现的最好英文字母。
 * 注意 'A' 和 'F' 的大写和小写形式也都出现了，但是 'R' 比 'F' 和 'A' 更好。
 * 示例 3：
 *
 * 输入：s = "AbCdEfGhIjK"
 * 输出：""
 * 解释：
 * 不存在大写和小写形式都出现的字母。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 1000
 * s 由小写和大写英文字母组成
 * 通过次数10,592提交次数15,750
 */
public class _2309_GreatestEnglishLetterInUpperAndLowerCase {

    /**
     * 统计 + 位运算
     *
     * 方法二：位运算
     *
     * 分别使用 32 位整数 lower 和 upper 表示字符串 s 中小写字母和大写字母的出现情况。
     *
     * 遍历字符串 s，假设当前遍历到的字符为 c，
     * 如果 c 为小写字母，那么将 lower 对应的位置置 1；如果 c 为大写字母，那么将 upper 对应的位置 置1。
     *
     * 【从大到小】枚举英文字母，如果一个英文字母在 lower 和 upper 中都出现，那么【直接返回】该英文字母。
     * 如果所有的英文字母都不符合要求，那么直接返回空字符串。
     *
     * 作者：LeetCode-Solution
     * 链接：https://leetcode.cn/problems/greatest-english-letter-in-upper-and-lower-case/solution/jian-ju-da-xiao-xie-de-zui-hao-ying-wen-o5u2s/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @return
     */
    public String greatestLetter(String s) {
        // 32 位整数 lower 和 upper 表示字符串 s 中小写字母和大写字母的出现情况
        int lower = 0, upper = 0;
        // 遍历字符
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)) {
                // 小写字符。lower对应位置置1
                lower |= 1 << (c - 'a');
            } else {
                // 大写字符。upper对应位置置1
                upper |= 1 << (c - 'A');
            }
        }
        // 从大到小枚举字符，遇到第一个大小写同时出现的字符即可返回，省去了多个符合要求的字符进行比较后返回更大者的过程
        for (int i = 25; i >= 0; i--) {
            // 大小写同时出现，注意这个写法很妙！
            if ((lower & upper & (1 << i)) != 0) {
                // 返回对应大写字符
                return String.valueOf((char) ('A' + i));
            }
        }
        return "";
    }
}
