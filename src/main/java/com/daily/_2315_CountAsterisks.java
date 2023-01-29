package com.daily;

/**
 * @author wangwei
 * @date 2023/1/29 15:43
 * @description: _2315_CountAsterisks
 *
 * 2315. 统计星号
 * 给你一个字符串 s ，每 两个 连续竖线 '|' 为 一对 。换言之，第一个和第二个 '|' 为一对，第三个和第四个 '|' 为一对，以此类推。
 *
 * 请你返回 不在 竖线对之间，s 中 '*' 的数目。
 *
 * 注意，每个竖线 '|' 都会 恰好 属于一个对。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "l|*e*et|c**o|*de|"
 * 输出：2
 * 解释：不在竖线对之间的字符加粗加斜体后，得到字符串："l|*e*et|c**o|*de|" 。
 * 第一和第二条竖线 '|' 之间的字符不计入答案。
 * 同时，第三条和第四条竖线 '|' 之间的字符也不计入答案。
 * 不在竖线对之间总共有 2 个星号，所以我们返回 2 。
 * 示例 2：
 *
 * 输入：s = "iamprogrammer"
 * 输出：0
 * 解释：在这个例子中，s 中没有星号。所以返回 0 。
 * 示例 3：
 *
 * 输入：s = "yo|uar|e**|b|e***au|tifu|l"
 * 输出：5
 * 解释：需要考虑的字符加粗加斜体后："yo|uar|e**|b|e***au|tifu|l" 。不在竖线对之间总共有 5 个星号。所以我们返回 5 。
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 1000
 * s 只包含小写英文字母，竖线 '|' 和星号 '*' 。
 * s 包含 偶数 个竖线 '|' 。
 * 通过次数22,553提交次数25,847
 */
public class _2315_CountAsterisks {

    /**
     * 简单模拟
     *
     * 遍历 s 每个字符，遇到 ‘|’ 时，一直向右遍历，直到遇到下一个 ‘|’，这两个 | 之间的字符不用统计
     * 除此之外，遇到 ‘*’ 就计数加1
     *
     * @param s
     * @return
     */
    public int countAsterisks(String s) {
        int n = s.length(), cnt = 0;
        // 遍历
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            // 遇到 '|'，
            if (c == '|') {
                // 向后遍历，直到下一个（与之配对） ‘|’
                while (++i < n && s.charAt(i) != '|');
            // 此外的 '*' 号，计数加1
            } else if (c == '*') {
                cnt++;
            }
        }
        // 返回
        return cnt;
    }
}
